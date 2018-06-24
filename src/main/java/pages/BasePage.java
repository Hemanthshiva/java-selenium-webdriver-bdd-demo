package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.logging.Logger;

public abstract class BasePage {

    private static final Logger LOGGER = Logger.getLogger(BasePage.class.getName());

    WebDriver driver;
    private final int DEFAULT_TIMEOUT = 10;

    BasePage(WebDriver driver) {
        this.driver = driver;
        PageFactory.initElements(this.driver, this);
    }


    /**
     * Waits for the element using the BY locator until DEFAULT_TIMEOUT
     *
     * @return boolean
     */
    private boolean waitForElementPresent(By by) {
        Boolean result = true;
        try {
            new WebDriverWait(driver, DEFAULT_TIMEOUT).until(ExpectedConditions.visibilityOfElementLocated(by));
        } catch (TimeoutException e) {
            LOGGER.info("Unable to find the element using locator " + by.toString());
            result = false;
        } catch (Throwable t) {
            LOGGER.info("Error " + t.getLocalizedMessage() + " was thrown while trying to find element " + by.toString());
            throw new Error(t);
        }
        return result;
    }


    /**
     * Finds the web element using BY locator
     *
     * @return WebElement
     */
    protected WebElement findElementBy(By by) {
        if (waitForElementPresent(by)) {
            return driver.findElement(by);
        } else {
            throw new Error("Unable to find element");
        }
    }

}
