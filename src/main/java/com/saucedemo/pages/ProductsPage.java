package com.saucedemo.pages;

import com.saucedemo.config.ConfigManager;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebElement;


public class ProductsPage extends BasePage{


    public ProductsPage(WebDriver driver){
        super(driver);
    }

    private final By pageHeading = By.cssSelector("[data-test='title']");
    private final By cartLink =
            By.cssSelector("[data-test='shopping-cart-link']");
    private final By listItems = By.className("inventory_list");


    public void waitUntilLoaded(){
        wait.until(ExpectedConditions.urlToBe(ConfigManager.BaseUrl()+"inventory.html"));
        wait.until(
                ExpectedConditions.visibilityOfElementLocated(pageHeading)

        );
        wait.until(ExpectedConditions.visibilityOfElementLocated(listItems));
    }

    public String getHeadingText(){
        return wait.until(ExpectedConditions.visibilityOfElementLocated(pageHeading)).getText();
    }

    public void addProductToCart(String item) {

        By addToCartButtonItem = By.xpath(
                "//div[text()='"+item+"']" +
                        "/ancestor::div[@class='inventory_item_description']//button[text()='Add to cart']");
        wait.until(ExpectedConditions.elementToBeClickable(addToCartButtonItem)).click();

        By removeFromCartButtonItem = By.xpath(
                "//div[text()='"+item+"']" +
                        "/ancestor::div[@class='inventory_item_description']//button[text()='Remove']");
        try {
            wait.until(
                    ExpectedConditions.visibilityOfElementLocated(
                            removeFromCartButtonItem
                    )
            );
        } catch (TimeoutException exception) {
            System.out.println("Failed product: " + item);
            System.out.println("Current URL: " + driver.getCurrentUrl());

            for (WebElement button : driver.findElements(addToCartButtonItem)) {
                System.out.println(
                        "Add button at failure: "
                                + button.getDomProperty("outerHTML")
                );
            }

            for (WebElement button : driver.findElements(removeFromCartButtonItem)) {
                System.out.println(
                        "Remove button at failure: "
                                + button.getDomProperty("outerHTML")
                                + " | displayed=" + button.isDisplayed()
                );
            }

            throw exception;
        }



    }

    public void openCart() {
        wait.until(
                ExpectedConditions.elementToBeClickable(cartLink)
        ).click();
    }

}
