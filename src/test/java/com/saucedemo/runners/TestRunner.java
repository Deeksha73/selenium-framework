package com.saucedemo.runners;


import io.cucumber.testng.AbstractTestNGCucumberTests;
import io.cucumber.testng.CucumberOptions;
import org.testng.annotations.DataProvider;

@CucumberOptions(
        features = "src/test/resources/features",
//          Packages where Cucumber looks for step definitions and hooks
        glue = {
                "com.saucedemo.stepdefinitions",
                "com.saucedemo.hooks"
        },
        plugin = {
//                Prints readable results in the terminal
                "pretty",
//                Generates the HTML report
                "html:target/cucumber-reports.html",
//                Generates results in JSON format
                "json:target/cucumber-reports.json"
        },
        publish = true
        )
// AbstractTestNGCucumberTests --> Makes TestNG execute your Cucumber scenarios
public class TestRunner extends AbstractTestNGCucumberTests {
        @Override
        @DataProvider(parallel = true)
        public Object[][] scenarios() {
                return super.scenarios();
        }

}
