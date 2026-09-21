package com.saucedemo.driver;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;

import java.util.Locale;

public class DriverFactory {

    private DriverFactory(){

    }

    public static WebDriver createDriver(String browser, boolean headless){
        switch(browser.trim().toLowerCase(Locale.ROOT)){
            case "chrome" :
                ChromeOptions chromeOptions = new ChromeOptions();
                if(headless){
                    chromeOptions.addArguments("--headless=new");
                }
                return new ChromeDriver(chromeOptions);
            case "firefox" :
                FirefoxOptions firefoxOptions = new FirefoxOptions();
                if(headless){
                    firefoxOptions.addArguments("-headless");
                }
                return new FirefoxDriver(firefoxOptions);
            default:
                throw new IllegalStateException(
                        "Unsupported browsers "+browser+" supported browses -> chrome, firefox"
                );
        }
    }
}
