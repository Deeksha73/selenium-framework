package com.saucedemo.pages;

import com.saucedemo.config.ConfigManager;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;

public class ProductsPage extends BasePage{


    public ProductsPage(WebDriver driver){
        super(driver);
    }

    private final By pageHeading = By.id("inventory_container");
    private final By cartLink =
            By.cssSelector("[data-test='shopping-cart-link']");


    public void waitUntilLoaded(){
        wait.until(ExpectedConditions.urlToBe(ConfigManager.BaseUrl()+"inventory.html"));
        wait.until(
                ExpectedConditions.visibilityOfElementLocated(pageHeading)
        );
    }

    public String getHeadingText(){
        return wait.until(ExpectedConditions.visibilityOfElementLocated(pageHeading)).getText();
    }

    public void addProductToCart(String item) {
        By addToCartButtonItem = By.xpath(
                "//div[text()='"+item+"']" +
                        "/ancestor::div[@class='inventory_item_description']//button[text()='Add to cart']");
        wait.until(ExpectedConditions.elementToBeClickable(addToCartButtonItem)).click();

    }

    public void openCart() {
        wait.until(
                ExpectedConditions.elementToBeClickable(cartLink)
        ).click();
    }

}
