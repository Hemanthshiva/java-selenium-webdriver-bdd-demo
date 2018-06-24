package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.springframework.stereotype.Component;

@Component
public class VehicleInfoLandingPage extends BasePage {

    public VehicleInfoLandingPage(WebDriver driver) {
        super(driver);
    }

    private static final By START_BUTTON = By.xpath("//a[contains(text(),'Start now')]");


    /**
     * Clicks on Start button
     *
     * @return VehicleEnquiryPage
     */
    public VehicleEnquiryPage startVehicleEnquiry() {
        findElementBy(START_BUTTON).click();
        return new VehicleEnquiryPage(driver);
    }
}
