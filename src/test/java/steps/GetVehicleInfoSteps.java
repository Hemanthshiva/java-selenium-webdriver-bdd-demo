package steps;

import cucumber.api.java.en.And;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import dao.VehicleDao;
import org.junit.Assert;
import org.openqa.selenium.WebDriver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import pages.VehicleEnquiryPage;
import pages.VehicleInfoLandingPage;
import pages.VehicleInformationPage;
import services.DriverServices;
import services.FileServices;
import services.FileServicesImpl;
import util.PropertyLoader;
import util.XlsxUtility;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import static steps.TestSetUp.filePath;
import static steps.TestSetUp.getDriverInstance;


@ContextConfiguration(classes = {FileServicesImpl.class, DriverServices.class, VehicleDao.class})
public class GetVehicleInfoSteps {
    private static final Logger LOGGER = Logger.getLogger(GetVehicleInfoSteps.class.getName());

    private WebDriver driver;
    private File[] xlsxFiles;
    private String dataFilePath;
    private VehicleInformationPage vehicleInformationPage;
    private VehicleEnquiryPage vehicleEnquiryPage;
    private XlsxUtility xlsxUtility;
    List<VehicleDao> vehicleDaoList;

    @Autowired
    FileServices fileServices;

    @Autowired
    VehicleDao vehicleDao;

    @Given("^I have vehicle information stored in \"([^\"]*)\" file in \"([^\"]*)\" folder$")
    public void iHaveVehicleInformationStoredInFileInFolder(String fileType, String folderName) {
        xlsxFiles = fileServices.getFilesByFileType(new File(filePath + folderName), fileType);
        LOGGER.info("Retrieved " + xlsxFiles.length + " files with " + fileType + " extension from " + folderName + " folder");
    }


    @And("^I have retrieved all the vehicle information from \"([^\"]*)\" file$")
    public void iHaveRetrievedAllTheVehicleInformationFromFile(String fileName) {

        for (File file : xlsxFiles) {
            if (fileServices.getFileName(file).equalsIgnoreCase(fileName))
                dataFilePath = file.getAbsolutePath();
        }
        xlsxUtility = new XlsxUtility(dataFilePath);

        LOGGER.info("Retrieved " + dataFilePath + " path with test data.");

        vehicleDaoList = new ArrayList<>();
        LOGGER.info("Found " + (xlsxUtility.getRowCount("VehicleData") - 1) + " sets of test data");
        for (int i = 2; i <= xlsxUtility.getRowCount("VehicleData"); i++) {
            vehicleDao = new VehicleDao();

            vehicleDao.setRegNumber(xlsxUtility.getCellData("VehicleData", "PLATENUMBER", i));
            vehicleDao.setMake(xlsxUtility.getCellData("VehicleData", "MAKE", i));
            vehicleDao.setColour(xlsxUtility.getCellData("VehicleData", "COLOUR", i));

            vehicleDaoList.add(vehicleDao);
            LOGGER.info("Loading test data set " + (i - 1) + " to vehicleDaoList");
        }

    }


    @When("^I navigate to vehicle information page and verify vehicle data$")
    public void iNavigateToVehicleInformationPageAndVerifyVehicleData() {
        driver = getDriverInstance();
        LOGGER.info("Driver initialized");
        driver.navigate().to(PropertyLoader.loadProperty("URL"));
        LOGGER.info("Navigated to " + PropertyLoader.loadProperty("URL"));
        vehicleEnquiryPage = new VehicleInfoLandingPage(driver)
                .startVehicleEnquiry();

        LOGGER.info("Staring vehicle search");
    }


    @Then("^All the vehicle information should match the data from the external source$")
    public void allTheVehicleInformationShouldMatchTheDataFromTheExternalSource() {

        for (VehicleDao vehicle : vehicleDaoList) {
            vehicleInformationPage = vehicleEnquiryPage.enterRegistrationNumber(vehicle.getRegNumber())
                    .searchVehicleInformation();

            LOGGER.info("Vehicle info being verified " + vehicle.getRegNumber() + "-" + vehicle.getMake() + "-" + vehicle.getColour());
            Assert.assertEquals("Make do not match with the expected ", vehicleInformationPage.getMake(), vehicle.getMake());
            Assert.assertEquals("Colour do not match with the expected ", vehicleInformationPage.getColour(), vehicle.getColour());
            Assert.assertEquals("Reg number do not match with the expected ", vehicleInformationPage.getRegNumber().replace(" ", ""), vehicle.getRegNumber());
            vehicleInformationPage.goBack();
        }

    }
}
