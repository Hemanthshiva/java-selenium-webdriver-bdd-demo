package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.List;

public class VehicleInformationPage extends BasePage {
    private static final Logger LOGGER = LoggerFactory.getLogger(VehicleInformationPage.class);

    // Multiple possible locators for each field
    private static final By[] REG_NUMBER_LOCATORS = {
            By.cssSelector(".reg-mark-sm"),
            By.xpath("//*[contains(@class,'reg-mark-sm')]")
    };

    private static final By[] MAKE_LOCATORS = {
            By.xpath("//form/dl//*[contains(text(),'Make') or contains(text(),'make')]/following-sibling::*"),
            By.cssSelector("[data-test='vehicle-make']")
    };

    private static final By[] COLOUR_LOCATORS = {
            By.xpath("//form/dl//*[contains(text(),'Colour') or contains(text(),'Color')]/following-sibling::*"),
            By.cssSelector("[data-test='vehicle-colour']")
    };

    private static final By[] BACK_LOCATORS = {
            By.linkText("Back"),
            By.xpath("//a[contains(text(),'Back')] | //button[contains(text(),'Back')]")
    };

    public VehicleInformationPage(WebDriver driver) {
        super(driver);
        logPageState("VehicleInformationPage initialization");
    }

    public String getRegNumber() {
        LOGGER.debug("Getting registration number");
        return findFirstMatchingElement(REG_NUMBER_LOCATORS, "registration number").getText().trim();
    }

    public String getMake() {
        LOGGER.debug("Getting vehicle make");
        return findFirstMatchingElement(MAKE_LOCATORS, "make").getText().trim();
    }

    public String getColour() {
        LOGGER.debug("Getting vehicle colour");
        return findFirstMatchingElement(COLOUR_LOCATORS, "colour").getText().trim();
    }

    public VehicleEnquiryPage goBack() {
        LOGGER.debug("Clicking back button");
        findFirstMatchingElement(BACK_LOCATORS, "back button").click();
        return new VehicleEnquiryPage(driver);
    }

    private WebElement findFirstMatchingElement(By[] locators, String elementDescription) {
        LOGGER.debug("Searching for {} using {} different locators", elementDescription, locators.length);
        logPageState("Before finding " + elementDescription);

        for (By locator : locators) {
            try {
                List<WebElement> elements = driver.findElements(locator);
                if (!elements.isEmpty()) {
                    WebElement element = elements.get(0);
                    if (element.isDisplayed()) {
                        LOGGER.info("Found {} using locator: {}, text: '{}'",
                                elementDescription, locator, element.isDisplayed() ? element.getText().trim() : "not displayed");
                        return element;
                    }
                }
            } catch (Exception e) {
                LOGGER.debug("Failed to find {} using locator: {} - {}",
                        elementDescription, locator, e.getMessage());
            }
        }

        throw new RuntimeException("Could not find " + elementDescription + " using any of the available locators");
    }

    private void logPageState(String context) {
        LOGGER.info("=== Page State: {} ===", context);
        LOGGER.info("Current URL: {}", driver.getCurrentUrl());
        try {
            String pageSource = driver.getPageSource();
            LOGGER.debug("Page source snippet: {}",
                    pageSource.length() > 1000 ? pageSource.substring(0, 1000) + "..." : pageSource);
        } catch (Exception e) {
            LOGGER.error("Error logging page state: {}", e.getMessage());
        }
    }
}
