package pages;

import org.openqa.selenium.*;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;

public abstract class BasePage {
    private static final Logger LOGGER = LoggerFactory.getLogger(BasePage.class);
    protected WebDriver driver;
    private static final Duration DEFAULT_TIMEOUT = Duration.ofSeconds(20);

    protected BasePage(WebDriver driver) {
        this.driver = driver;
        PageFactory.initElements(this.driver, this);
    }

    protected boolean waitForElementPresent(By by) {
        try {
            LOGGER.debug("Waiting for element: {}", by);
            new WebDriverWait(driver, DEFAULT_TIMEOUT)
                .until(ExpectedConditions.visibilityOfElementLocated(by));
            LOGGER.debug("Element found: {}", by);
            return true;
        } catch (TimeoutException e) {
            LOGGER.error("Element not found within {} seconds: {}", DEFAULT_TIMEOUT.getSeconds(), by, e);
            logPageState();
            return false;
        }
    }

    protected WebElement findElementBy(By by) {
        if (!waitForElementPresent(by)) {
            throw new NoSuchElementException("Element not found: " + by);
        }
        try {
            WebElement element = driver.findElement(by);
            LOGGER.debug("Found element: {}", by);
            return element;
        } catch (StaleElementReferenceException e) {
            LOGGER.warn("Element became stale, retrying: {}", by);
            waitForElementPresent(by);
            return driver.findElement(by);
        }
    }

    private void logPageState() {
        LOGGER.debug("Current URL: {}", driver.getCurrentUrl());
        LOGGER.debug("Page source snippet: {}", getPageSourceSnippet());
    }

    private String getPageSourceSnippet() {
        String pageSource = driver.getPageSource();
        int maxLength = 500;
        if (pageSource.length() > maxLength) {
            return pageSource.substring(0, maxLength) + "...";
        }
        return pageSource;
    }
}
