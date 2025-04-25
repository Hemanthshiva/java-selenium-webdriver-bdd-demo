package steps;

import dao.VehicleDao;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.junit.Assert;
import org.openqa.selenium.WebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import pages.VehicleEnquiryPage;
import pages.VehicleInfoLandingPage;
import pages.VehicleInformationPage;
import services.FileServices;
import util.PropertyLoader;
import util.XlsxUtility;

import static steps.TestSetUp.FILE_PATH;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class GetVehicleInfoSteps {
    private static final Logger LOGGER = LoggerFactory.getLogger(GetVehicleInfoSteps.class);
    private static final String VEHICLE_DATA_SHEET = "VehicleData";

    private File[] xlsxFiles;
    private XlsxUtility xlsxUtility;
    private List<VehicleDao> vehicleDaoList;
    private VehicleInformationPage vehicleInformationPage;
    private VehicleEnquiryPage vehicleEnquiryPage;

    @Autowired
    private FileServices fileServices;

    @Given("^I have vehicle information stored in \"([^\"]*)\" file in \"([^\"]*)\" folder$")
    public void iHaveVehicleInformationStoredInFileInFolder(String fileType, String folderName) {
        File folder = new File(FILE_PATH + folderName);
        if (!folder.exists()) {
            throw new RuntimeException("Folder not found: " + folder.getAbsolutePath());
        }
        
        xlsxFiles = fileServices.getFilesByFileType(folder, fileType);
        LOGGER.info("Found {} files with {} extension in {} folder", 
            xlsxFiles.length, fileType, folderName);
    }

    @And("^I have retrieved all the vehicle information from \"([^\"]*)\" file$")
    public void iHaveRetrievedAllTheVehicleInformationFromFile(String fileName) {
        String dataFilePath = findDataFile(fileName)
            .orElseThrow(() -> new RuntimeException("File not found: " + fileName));
            
        xlsxUtility = new XlsxUtility(dataFilePath);
        loadVehicleData();
    }

    @When("^I navigate to vehicle information page and verify vehicle data$")
    public void iNavigateToVehicleInformationPageAndVerifyVehicleData() {
        WebDriver driver = TestSetUp.getDriverInstance();
        if (driver == null) {
            throw new RuntimeException("WebDriver not initialized");
        }

        String url = PropertyLoader.loadProperty("URL");
        driver.navigate().to(url);
        LOGGER.info("Navigated to {}", url);
        
        vehicleEnquiryPage = new VehicleInfoLandingPage(driver)
                .startVehicleEnquiry();
        LOGGER.info("Started vehicle enquiry process");
    }

    @Then("^All the vehicle information should match the data from the external source$")
    public void allTheVehicleInformationShouldMatchTheDataFromTheExternalSource() {
        vehicleDaoList.forEach(this::verifyVehicleInformation);
    }

    private Optional<String> findDataFile(String fileName) {
        for (File file : xlsxFiles) {
            if (fileServices.getFileName(file).equalsIgnoreCase(fileName)) {
                LOGGER.info("Found data file: {}", file.getAbsolutePath());
                return Optional.of(file.getAbsolutePath());
            }
        }
        return Optional.empty();
    }

    private void loadVehicleData() {
        vehicleDaoList = new ArrayList<>();
        int rowCount = xlsxUtility.getRowCount(VEHICLE_DATA_SHEET);
        LOGGER.info("Found {} sets of test data", rowCount - 1);
        
        for (int i = 2; i <= rowCount; i++) {
            VehicleDao vehicle = new VehicleDao();
            vehicle.setRegNumber(xlsxUtility.getCellData(VEHICLE_DATA_SHEET, "PLATENUMBER", i));
            vehicle.setMake(xlsxUtility.getCellData(VEHICLE_DATA_SHEET, "MAKE", i));
            vehicle.setColour(xlsxUtility.getCellData(VEHICLE_DATA_SHEET, "COLOUR", i));
            
            vehicleDaoList.add(vehicle);
            LOGGER.info("Loaded test data set {}", i - 1);
        }
    }

    private void verifyVehicleInformation(VehicleDao vehicle) {
        vehicleInformationPage = vehicleEnquiryPage
            .enterRegistrationNumber(vehicle.getRegNumber())
            .searchVehicleInformation();

        LOGGER.info("Verifying vehicle: {}-{}-{}", 
            vehicle.getRegNumber(), vehicle.getMake(), vehicle.getColour());
            
        Assert.assertEquals("Make does not match", 
            vehicleInformationPage.getMake(), vehicle.getMake());
        Assert.assertEquals("Colour does not match", 
            vehicleInformationPage.getColour(), vehicle.getColour());
        Assert.assertEquals("Registration number does not match",
            vehicleInformationPage.getRegNumber().replace(" ", ""), 
            vehicle.getRegNumber());

        vehicleInformationPage.goBack();
    }
}
