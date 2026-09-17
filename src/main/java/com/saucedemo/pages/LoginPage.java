package com.saucedemo.pages;

import com.saucedemo.config.ConfigManager;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class LoginPage extends BasePage{


    public LoginPage(WebDriver driver){
       super(driver);
    }

    private final By usernameField = By.id("user-name");
    private final By passwordField = By.id("password");
    private final By loginButton = By.id("login-button");
    private final By errorMessage = By.cssSelector("[data-test='error']");

    public void open(){
        driver.get(ConfigManager.BaseUrl());
    }

    public void login(String username, String password){
        WebElement usernameInput =
                wait.until(ExpectedConditions.visibilityOfElementLocated(usernameField));
        usernameInput.clear();
        usernameInput.sendKeys(username);
        WebElement passwordInput =
                wait.until(ExpectedConditions.visibilityOfElementLocated(passwordField));
        passwordInput.clear();
        passwordInput.sendKeys(password);

        wait.until(ExpectedConditions.elementToBeClickable(loginButton)).click();

    }

    public String getErrorMessage(){
        return wait.until(ExpectedConditions.visibilityOfElementLocated(errorMessage)).getText();
    }

}
