package com.saucedemo.hooks;

import com.saucedemo.driver.DriverManager;
import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.Scenario;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.logging.LogType;

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

                try {
                    StringBuilder browserLogs = new StringBuilder();
                    driverManager.getDriver().manage().logs().get(LogType.BROWSER)
                        .forEach(entry -> browserLogs.append(entry).append("\n"));
                    scenario.attach(
                        browserLogs.isEmpty() ? "No browser console entries captured." : browserLogs.toString(),
                        "text/plain",
                        "Browser Console Logs"
                    );
                } catch (RuntimeException exception) {
                    scenario.log("Browser logs could not be captured: " + exception.getMessage());
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
