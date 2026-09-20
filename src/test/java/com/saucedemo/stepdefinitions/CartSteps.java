package com.saucedemo.stepdefinitions;

import com.saucedemo.driver.DriverManager;
import com.saucedemo.pages.CartPage;
import com.saucedemo.pages.ProductsPage;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.testng.Assert;
import java.util.ArrayList;
import java.util.Collections;

import java.util.List;

public class CartSteps {

    private ProductsPage productsPage;

    @When("I add {string} to the cart")
    public void addBackpackToCart(String productName) {
        productsPage =
                new ProductsPage(DriverManager.getDriver());

        productsPage.addProductToCart(productName);
    }

    @When("I open the shopping cart")
    public void openShoppingCart() {
        productsPage =
                new ProductsPage(DriverManager.getDriver());
        productsPage.openCart();
    }

    @Then("the cart should contain {string}")
    public void verifyCartContainsProduct(String expectedProduct) {
        CartPage cartPage =
                new CartPage(DriverManager.getDriver());

        List<String> actualProducts = cartPage.getProductNames();

        Assert.assertTrue(
                actualProducts.contains(expectedProduct),
                "Expected cart to contain '" + expectedProduct
                        + "', but found: " + actualProducts
        );
    }

    @Then("I wait")
    public void wait_for_sometime() throws InterruptedException {
        Thread.sleep(5000);
    }

    @When("I add the following products to the cart:")
    public void addProductsToCart(DataTable dataTable) {
        List<String> productNames = dataTable.asList(String.class);

        ProductsPage productsPage =
                new ProductsPage(DriverManager.getDriver());

        for (String productName : productNames) {
            productsPage.addProductToCart(productName);
        }
    }

    @Then("the cart should contain the following products:")
    public void verifyCartContainsProducts(DataTable dataTable) {
        List<String> expectedProducts =
                dataTable.asList(String.class);

        CartPage cartPage =
                new CartPage(DriverManager.getDriver());

        List<String> actualProducts = cartPage.getProductNames();

        for (String expectedProduct : expectedProducts) {
            Assert.assertTrue(
                    actualProducts.contains(expectedProduct),
                    "Expected cart to contain '" + expectedProduct
                            + "', but found: " + actualProducts
            );
        }
    }

    @Then("the cart should contain exactly these products:")
    public void verifyExactCartContents(DataTable dataTable) {
        List<String> expectedProducts =
                new ArrayList<>(dataTable.asList(String.class));

        CartPage cartPage =
                new CartPage(DriverManager.getDriver());

        List<String> actualProducts =
                new ArrayList<>(cartPage.getProductNames());

        Collections.sort(expectedProducts);
        Collections.sort(actualProducts);

        Assert.assertEquals(
                actualProducts,
                expectedProducts,
                "Cart contents should match the expected products exactly."
        );
    }
}
