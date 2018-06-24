package runner;

import cucumber.api.CucumberOptions;
import cucumber.api.junit.Cucumber;
import org.junit.runner.RunWith;

@RunWith(Cucumber.class)
@CucumberOptions(
        features = "classpath:features",
        glue = "steps",
        tags = {"not @wip", "not @ignore", "not @merged"},
        monochrome = true,
        format = {
                "pretty",
                "html:target/features-reports/features-pretty",
                "json:target/features-reports/CucumberTestReport.json"
        })
public class TestRunner {

}