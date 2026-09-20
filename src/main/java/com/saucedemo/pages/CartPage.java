package com.saucedemo.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;

import java.util.List;

public class CartPage extends BasePage{

    private final By cartContainer =
            By.cssSelector("[data-test='cart-list']");

    private final By productNames =
            By.cssSelector(
                    "[data-test='cart-list'] [data-test='inventory-item-name']"
            );

    public CartPage(WebDriver driver) {
        super(driver);
    }

    public List<String> getProductNames(){
        wait.until(
                ExpectedConditions.visibilityOfElementLocated(cartContainer)
        );
        return driver.findElements(productNames)
                .stream()
                .map(WebElement::getText)
                .toList();
    }



}
