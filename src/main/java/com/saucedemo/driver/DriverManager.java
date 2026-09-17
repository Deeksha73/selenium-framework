package com.saucedemo.driver;

import com.saucedemo.config.ConfigManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;


public class DriverManager {
    private static WebDriver driver;

//  The static field gives your hooks and steps access to the same driver.
    public static void createDriver(){
        driver = DriverFactory.createDriver(ConfigManager.getBrowser());
        driver.manage().window().maximize();
    }

    public static WebDriver getDriver(){
        if(driver==null){
            throw new IllegalStateException("Browser has not been started.");
        }
        else
            return driver;
    }

    public static void quitDriver(){
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
