package com.saucedemo.hooks;

import com.saucedemo.driver.DriverManager;
import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.Scenario;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;

public class CucumberHooks {
    @Before
    public void setUp(){
        DriverManager.createDriver();
    }

    @After
    public void tearDown(Scenario scenario){
        try{
            if(scenario.isFailed()){
//        Imp syntaxx
                byte[] screenshot = ((TakesScreenshot) DriverManager.getDriver())
                        .getScreenshotAs(OutputType.BYTES);
                /*
                 * WebDriver driver = DriverManager.getDriver();
                 *
                 * TakesScreenshot screenshotDriver = (TakesScreenshot) driver;
                 *
                 * byte[] screenshot =
                 *     screenshotDriver.getScreenshotAs(OutputType.BYTES);
                 *
                 */
                scenario.attach(
                        screenshot,
                        "image/png",
                        "Failure Screenshot"
                );
            }
        }
        finally {
            DriverManager.quitDriver();
        }
    }
}
