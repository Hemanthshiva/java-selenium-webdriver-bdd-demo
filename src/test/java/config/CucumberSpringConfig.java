package config;

import io.cucumber.spring.CucumberContextConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.ContextConfiguration;
import services.DriverServices;
import services.FileServicesImpl;
import dao.VehicleDao;

@CucumberContextConfiguration
@ContextConfiguration(classes = {
    FileServicesImpl.class,
    DriverServices.class,
    VehicleDao.class
})
@ComponentScan(basePackages = {"steps", "services", "dao"})
public class CucumberSpringConfig {
    // Configuration class for Cucumber-Spring integration
}