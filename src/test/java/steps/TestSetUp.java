package steps;

import cucumber.api.java.After;
import cucumber.api.java.Before;
import org.openqa.selenium.WebDriver;
import org.springframework.beans.factory.annotation.Autowired;
import services.DriverServices;

import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

public class TestSetUp {

    private static final Logger LOGGER = Logger.getLogger(TestSetUp.class.getName());
    private static WebDriver driver;
    public static final String filePath = System.getProperty("user.dir") + "/src/main/resources/";

    @Autowired
    DriverServices driverServices;

    @Before
    public void setUp() {
        driver = driverServices.create();
        driver.manage().deleteAllCookies();
        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
        driver.manage().window().fullscreen();
        LOGGER.info("Driver object is created");
    }

    public static WebDriver getDriverInstance() {
        return driver;
    }

    @After
    public void tearDown() {
        LOGGER.info("Quiting driver object");
        if (driver != null)
            driver.quit();
    }

}
