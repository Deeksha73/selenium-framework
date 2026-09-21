package com.saucedemo.driver;

import com.saucedemo.config.ConfigManager;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.WebDriver;


public class DriverManager {
    private WebDriver driver;

// PicoContainer shares this instance only within the current scenario.
    public void createDriver(){
        if (driver != null) {
            throw new IllegalStateException("Browser has already been started.");
        }
        driver = DriverFactory.createDriver(ConfigManager.getBrowser(),ConfigManager.isHeadless());
        driver.manage().window().setSize(
                new Dimension(1440, 900)
        );
    }

    public boolean hasDriver() {
        return driver != null;
    }

    public WebDriver getDriver(){
        if(driver==null){
            throw new IllegalStateException("Browser has not been started.");
        }
        else
            return driver;
    }

    public void quitDriver(){
        if(driver!=null){
            try{
                driver.quit();
            }
            finally {
                driver=null;
            }
        }
    }


}
