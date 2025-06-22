package Runners;

import org.junit.runner.RunWith;

import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;

@RunWith(Cucumber.class)
@CucumberOptions(features = { "src/test/resources/Features" }, plugin = { "pretty",
		"html:target/cucumber-reports/html-report.html",
		"json:target/cucumber-reports/Cucumber.json" },
        glue = { "StepsDefinition" })
public class Runner {
}