package com.saucedemo.pages;

import com.saucedemo.config.ConfigManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.sql.Driver;
import java.time.Duration;

public class BasePage {

    protected final WebDriverWait wait;
    protected final WebDriver driver;

    protected BasePage(WebDriver driver){
        this.driver = driver;
        this.wait= new WebDriverWait(driver, Duration.ofSeconds(ConfigManager.getTimeoutSeconds()));
    }



}
