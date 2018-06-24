package services;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.springframework.stereotype.Service;

import java.util.logging.Logger;

@Service
public class DriverServices {

    private static final Logger LOGGER = Logger.getLogger(DriverServices.class.getName());

    private static String OS = System.getProperty("os.name").toLowerCase();
    private String pathToDriver;


    /**
     * Returns an instance of WebDriver
     *
     * @return WebDriver
     */
    public WebDriver create() {
        return driverInstance();
    }


    /**
     * Creates an instance of WebDriver
     *
     * @return WebDriver
     */
    private WebDriver driverInstance() {
        if (OS.contains("win")) {
            pathToDriver = System.getProperty("user.dir") + "/src/test/resources/drivers/windows/chromedriver.exe";
            System.setProperty("webdriver.chrome.driver", pathToDriver);
            LOGGER.info("You are running your tests on Windows machine");
        } else if (OS.contains("mac")) {
            pathToDriver = System.getProperty("user.dir") + "/src/test/resources/drivers/mac/chromedriver";
            System.setProperty("webdriver.chrome.driver", pathToDriver);
            LOGGER.info("You are running your tests on Mac machine");
        }
        LOGGER.info("Returning ChromeDriver object");
        return new ChromeDriver();
    }

}
