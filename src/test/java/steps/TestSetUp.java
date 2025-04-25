package steps;

import io.cucumber.java.After;
import io.cucumber.java.Before;
import org.openqa.selenium.WebDriver;
import org.springframework.beans.factory.annotation.Autowired;
import services.DriverServices;
import java.util.logging.Logger;

public class TestSetUp {
    private static final Logger LOGGER = Logger.getLogger(TestSetUp.class.getName());
    private static WebDriver driver;
    public static final String FILE_PATH = System.getProperty("user.dir") + "/src/main/resources/";

    @Autowired
    private DriverServices driverServices;

    @Before("@ui")
    public void setUp() {
        if (driver == null) {
            driver = driverServices.create();
            LOGGER.info("Driver initialized for test");
        }
    }

    public static WebDriver getDriverInstance() {
        return driver;
    }

    @After("@ui")
    public void tearDown() {
        if (driver != null) {
            LOGGER.info("Cleaning up driver instance");
            try {
                driver.quit();
            } finally {
                driver = null;
            }
        }
    }
}
