package com.saucedemo.pages;

import com.saucedemo.config.ConfigManager;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class ProductPage extends BasePage{


    public ProductPage(WebDriver driver){
        super(driver);
    }

    private final By pageHeading = By.cssSelector("[data-test='title']");

    public void waitUntilLoaded(){
        wait.until(ExpectedConditions.urlToBe(ConfigManager.BaseUrl()+"inventory.html"));
        wait.until(
                ExpectedConditions.visibilityOfElementLocated(pageHeading)
        );
    }

    public String getHeadingText(){
        return wait.until(ExpectedConditions.visibilityOfElementLocated(pageHeading)).getText();
    }

}
