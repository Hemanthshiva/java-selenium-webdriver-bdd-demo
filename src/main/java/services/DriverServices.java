package services;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.springframework.stereotype.Service;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.openqa.selenium.SessionNotCreatedException;
import io.github.bonigarcia.wdm.config.WebDriverManagerException;

import java.time.Duration;

@Service
public class DriverServices {
    private static final Logger LOGGER = LoggerFactory.getLogger(DriverServices.class);
    private static final int IMPLICIT_WAIT_SECONDS = 10;
    private static final int PAGE_LOAD_TIMEOUT_SECONDS = 30;
    private static final int MAX_RETRY_ATTEMPTS = 2;

    public WebDriver create() {
        return createConfiguredDriver();
    }

    private WebDriver createConfiguredDriver() {
        int attempts = 0;
        while (attempts < MAX_RETRY_ATTEMPTS) {
            try {
                return attemptDriverCreation();
            } catch (SessionNotCreatedException e) {
                attempts++;
                handleSessionCreationFailure(attempts, e);
            } catch (WebDriverManagerException e) {
                return handleWebDriverManagerFailure(e);
            } catch (Exception e) {
                handleUnexpectedError(e);
            }
        }
        throw new WebDriverInitializationException(
            "Failed to initialize WebDriver after exhausting all recovery attempts");
    }

    private void handleSessionCreationFailure(int attempts, SessionNotCreatedException e) {
        String errorMessage = String.format("Failed to create Chrome session (Attempt %d/%d)", 
            attempts, MAX_RETRY_ATTEMPTS);
        LOGGER.error(errorMessage, e);
        
        if (attempts >= MAX_RETRY_ATTEMPTS) {
            throw new WebDriverInitializationException(
                String.format("Failed to create Chrome session after %d attempts", MAX_RETRY_ATTEMPTS), e);
        }
        
        try {
            LOGGER.info("Attempting to reset ChromeDriver");
            WebDriverManager.chromedriver().reset();
        } catch (Exception resetEx) {
            LOGGER.warn("Failed to reset ChromeDriver", resetEx);
        }
    }

    private WebDriver handleWebDriverManagerFailure(WebDriverManagerException e) {
        LOGGER.error("ChromeDriver setup failed", e);
        try {
            LOGGER.info("Attempting to reset and update ChromeDriver");
            WebDriverManager.chromedriver().reset();
            WebDriverManager.chromedriver().forceDownload().setup();
            return attemptDriverCreation();
        } catch (Exception retryEx) {
            throw new WebDriverInitializationException(
                "Failed to setup ChromeDriver even after forced update", e);
        }
    }

    private void handleUnexpectedError(Exception e) {
        LOGGER.error("Critical error during WebDriver initialization", e);
        throw new WebDriverInitializationException(
            "Unexpected error during WebDriver initialization", e);
    }

    private WebDriver attemptDriverCreation() {
        LOGGER.info("Setting up ChromeDriver using WebDriverManager");
        // Configure WebDriverManager to automatically download matching driver
        WebDriverManager.chromedriver().clearDriverCache().setup();
        
        LOGGER.debug("Configuring Chrome options");
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--remote-allow-origins=*");
        options.addArguments("--start-maximized");
        options.addArguments("--disable-extensions");
        options.addArguments("--disable-popup-blocking");
        
        LOGGER.debug("Initializing ChromeDriver with configured options");
        WebDriver driver = new ChromeDriver(options);
        
        LOGGER.debug("Setting driver timeouts");
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(IMPLICIT_WAIT_SECONDS));
        driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(PAGE_LOAD_TIMEOUT_SECONDS));
        
        LOGGER.info("Successfully created and configured ChromeDriver instance");
        return driver;
    }
}
