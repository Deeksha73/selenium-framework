package com.saucedemo.context;

import com.saucedemo.driver.DriverManager;
import com.saucedemo.pages.CartPage;
import com.saucedemo.pages.LoginPage;
import com.saucedemo.pages.ProductsPage;

/** One instance per scenario, shared by step classes through PicoContainer. */
public final class PageObjectManager {

    private final DriverManager driverManager;
    private LoginPage loginPage;
    private ProductsPage productsPage;
    private CartPage cartPage;

    public PageObjectManager(DriverManager driverManager) {
        this.driverManager = driverManager;
    }

    // Pages are created after the Before hook starts the browser.
    public LoginPage getLoginPage() {
        if (loginPage == null) {
            loginPage = new LoginPage(driverManager.getDriver());
        }
        return loginPage;
    }

    public ProductsPage getProductsPage() {
        if (productsPage == null) {
            productsPage = new ProductsPage(driverManager.getDriver());
        }
        return productsPage;
    }

    public CartPage getCartPage() {
        if (cartPage == null) {
            cartPage = new CartPage(driverManager.getDriver());
        }
        return cartPage;
    }
}
