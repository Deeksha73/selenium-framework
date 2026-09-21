package com.saucedemo.hooks;

import com.saucedemo.driver.DriverManager;
import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.Scenario;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

public class CucumberHooks {

    private final DriverManager driverManager;

    public CucumberHooks(DriverManager driverManager) {
        this.driverManager = driverManager;
    }

    @Before
    public void setUp() {
        driverManager.createDriver();
    }

    @After
    public void tearDown(Scenario scenario) {
        try {
            if (scenario.isFailed() && driverManager.hasDriver()) {
                try {
                    WebDriver driver = driverManager.getDriver();
                    if (driver instanceof TakesScreenshot screenshotDriver) {
                        scenario.attach(
                            screenshotDriver.getScreenshotAs(OutputType.BYTES),
                            "image/png",
                            "Failure Screenshot"
                        );
                    }
                } catch (RuntimeException exception) {
                    scenario.log("Screenshot could not be captured: " + exception.getMessage());
                }
            }
        } finally {
            try {
                driverManager.quitDriver();
            } catch (RuntimeException exception) {
                if (!scenario.isFailed()) {
                    throw exception;
                }
                scenario.log("Browser cleanup failed: " + exception.getMessage());
            }
        }
    }
}
