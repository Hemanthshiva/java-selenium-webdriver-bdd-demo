package pages;

import org.openqa.selenium.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;
import java.util.List;

public class VehicleEnquiryPage extends BasePage {
    private static final Logger LOGGER = LoggerFactory.getLogger(VehicleEnquiryPage.class);
    private static final Duration WAIT_TIMEOUT = Duration.ofSeconds(20);

    // Multiple possible locators for the registration input field
    private static final By[] REG_INPUT_LOCATORS = {
            By.id("wizard_vehicle_enquiry_capture_vrn_vrn"),
            By.cssSelector("input[type='text']"), // Generic text input as fallback
    };

    private static final By[] CONTINUE_BUTTON_LOCATORS = {
            By.id("submit_vrn_button"),
            By.cssSelector("button[type='submit']"),
            By.xpath("//button[contains(text(), 'Continue')]"),
    };

    public VehicleEnquiryPage(WebDriver driver) {
        super(driver);
        logPageState("VehicleEnquiryPage initialization");
    }

    /**
     * @param regNumber
     * @return
     */
    public VehicleEnquiryPage enterRegistrationNumber(String regNumber) {
        LOGGER.info("Attempting to enter registration number: {}", regNumber);
        WebElement inputField = findFirstAvailableElement(REG_INPUT_LOCATORS, "registration number input field");

        try {
            new WebDriverWait(driver, WAIT_TIMEOUT)
                    .until(ExpectedConditions.elementToBeClickable(inputField));
            inputField.clear();
            inputField.sendKeys(regNumber);
            LOGGER.info("Successfully entered registration number");
            return this;
        } catch (Exception e) {
            logPageState("Failed to enter registration number");
            throw new RuntimeException("Failed to enter registration number: " + e.getMessage(), e);
        }
    }

    public VehicleInformationPage searchVehicleInformation() {
        LOGGER.info("Attempting to click continue button");
        WebElement continueButton = findFirstAvailableElement(CONTINUE_BUTTON_LOCATORS, "continue button");

        try {
            new WebDriverWait(driver, WAIT_TIMEOUT)
                    .until(ExpectedConditions.elementToBeClickable(continueButton));
            continueButton.click();
            LOGGER.info("Successfully clicked continue button");
            return new VehicleInformationPage(driver);
        } catch (Exception e) {
            logPageState("Failed to click continue button");
            throw new RuntimeException("Failed to click continue button: " + e.getMessage(), e);
        }
    }

    private WebElement findFirstAvailableElement(By[] locators, String elementDescription) {
        LOGGER.debug("Searching for {} using {} different locators", elementDescription, locators.length);

        for (By locator : locators) {
            try {
                List<WebElement> elements = driver.findElements(locator);
                if (!elements.isEmpty()) {
                    WebElement element = elements.get(0);
                    if (element.isDisplayed()) {
                        LOGGER.info("Found {} using locator: {}", elementDescription, locator);
                        return element;
                    }
                }
            } catch (Exception e) {
                LOGGER.debug("Failed to find {} using locator: {} - {}",
                        elementDescription, locator, e.getMessage());
            }
        }

        logPageState("Failed to find " + elementDescription);
        throw new RuntimeException("Could not find " + elementDescription + " using any of the available locators");
    }

    private void logPageState(String context) {
        LOGGER.info("=== Page State: {} ===", context);
        LOGGER.info("Current URL: {}", driver.getCurrentUrl());
        try {
            String pageSource = driver.getPageSource();
            LOGGER.debug("Page source snippet: {}",
                    pageSource.length() > 1000 ? pageSource.substring(0, 1000) + "..." : pageSource);
            logInputElements();
        } catch (Exception e) {
            LOGGER.error("Error logging page state: {}", e.getMessage());
        }
    }

    private void logInputElements() {
        driver.findElements(By.tagName("input")).forEach(input -> {
            try {
                LOGGER.debug("Input: id='{}', name='{}', type='{}'",
                        input.getAttribute("id"),
                        input.getAttribute("name"),
                        input.getAttribute("type"));
            } catch (StaleElementReferenceException e) {
                LOGGER.debug("Element became stale while logging");
            }
        });
    }
}
