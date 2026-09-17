package com.saucedemo.driver;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;

import java.util.Locale;

public class DriverFactory {

    private DriverFactory(){

    }

    public static WebDriver createDriver(String browser){
        switch(browser.trim().toLowerCase(Locale.ROOT)){
            case "chrome" :
                return new ChromeDriver();
            case "firefox" :
                return new FirefoxDriver();
            default:
                throw new IllegalStateException(
                        "Unsupported browsers "+browser+" supported browses -> chrome, firefox"
                );
        }
    }
}
