package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.stereotype.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.time.Duration;

@Component
public class VehicleInfoLandingPage extends BasePage {
    private static final Logger LOGGER = LoggerFactory.getLogger(VehicleInfoLandingPage.class);
    private static final int WAIT_TIMEOUT_SECONDS = 20;
    private static final By START_BUTTON = By.cssSelector("a.button, a.govuk-button, a[role='button']");

    public VehicleInfoLandingPage(WebDriver driver) {
        super(driver);
    }

    public VehicleEnquiryPage startVehicleEnquiry() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(WAIT_TIMEOUT_SECONDS));
        wait.until(ExpectedConditions.elementToBeClickable(START_BUTTON)).click();
        LOGGER.debug("Current URL after clicking Start: {}", driver.getCurrentUrl());
        LOGGER.debug("Page source snippet after clicking Start: {}", getTrimmedPageSource());
        return new VehicleEnquiryPage(driver);
    }

    private String getTrimmedPageSource() {
        String pageSource = driver.getPageSource();
        int maxLength = 1000;
        if (pageSource.length() > maxLength) {
            return pageSource.substring(0, maxLength) + "...";
        }
        return pageSource;
    }
}
