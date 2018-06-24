package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class VehicleEnquiryPage extends BasePage {

    public VehicleEnquiryPage(WebDriver driver) {
        super(driver);
    }

    private static final By INPUT_BOX = By.id("Vrm");
    private static final By CONTINUE_BUTTON = By.name("Continue");


    /**
     * Input Reg Number into the Input box
     *
     * @return VehicleEnquiryPage
     */
    public VehicleEnquiryPage enterRegistrationNumber(String regNumber) {
        findElementBy(INPUT_BOX).sendKeys(regNumber);
        return this;
    }


    /**
     * Clicks on the Continue button
     *
     * @return VehicleInformationPage
     */
    public VehicleInformationPage searchVehicleInformation() {
        findElementBy(CONTINUE_BUTTON).click();
        return new VehicleInformationPage(driver);
    }

}
