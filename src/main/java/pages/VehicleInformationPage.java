package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class VehicleInformationPage extends BasePage {

    public VehicleInformationPage(WebDriver driver) {
        super(driver);
    }

    /**
     * Was using these locators however these locators had hidden attributes so changed the locators to the below locators
     */
//    private static final By REGISTRATION_NUMBER = By.id("Vrm");
//    private static final By MAKE = By.id("Make");
//    private static final By COLOUR = By.id("Colour");

    private static final By REGISTRATION_NUMBER = By.xpath("//li[@class='list-summary-item']//following-sibling::span[contains(text(),'Registration number')]/following-sibling::span");
    private static final By MAKE = By.xpath("//li[@class='list-summary-item']//following-sibling::span[contains(text(),'Make')]/following-sibling::span");
    private static final By COLOUR = By.xpath("//li[@class='list-summary-item']//following-sibling::span[contains(text(),'Colour')]/following-sibling::span");
    private static final By BACK = By.linkText("Back");


    /**
     * Retrieve Reg Number form the VehicleInformationPage
     *
     * @return String (Reg Number)
     */
    public String getRegNumber() {
        return findElementBy(REGISTRATION_NUMBER).getText();
    }


    /**
     * Retrieve Make of the Car form the VehicleInformationPage
     *
     * @return String (Make)
     */
    public String getMake() {
        return findElementBy(MAKE).getText();
    }


    /**
     * Retrieve Car Colour form the VehicleInformationPage
     *
     * @return String (Colour)
     */
    public String getColour() {
        return findElementBy(COLOUR).getText();
    }


    /**
     * Clicks on the Back link on the VehicleInformationPage
     *
     * @return VehicleEnquiryPage
     */
    public VehicleEnquiryPage goBack() {
        findElementBy(BACK).click();
        return new VehicleEnquiryPage(driver);
    }
}
