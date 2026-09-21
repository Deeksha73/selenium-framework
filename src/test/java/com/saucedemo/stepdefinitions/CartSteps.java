package com.saucedemo.stepdefinitions;

import com.saucedemo.context.PageObjectManager;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.testng.Assert;
import java.util.ArrayList;
import java.util.Collections;

import java.util.List;

public class CartSteps {

    private final PageObjectManager pages;

    public CartSteps(PageObjectManager pages) {
        this.pages = pages;
    }

    @When("I add {string} to the cart")
    public void addProductToTheCart(String productName) {

        pages.getProductsPage().addProductToCart(productName);
    }

    @When("I open the shopping cart")
    public void openShoppingCart() {
        pages.getProductsPage().openCart();
    }

    @Then("the cart should contain {string}")
    public void verifyCartContainsProduct(String expectedProduct) {

        List<String> actualProducts = pages.getCartPage().getProductNames();

        Assert.assertTrue(
                actualProducts.contains(expectedProduct),
                "Expected cart to contain '" + expectedProduct
                        + "', but found: " + actualProducts
        );
    }

    @When("I add the following products to the cart:")
    public void addProductsToCart(DataTable dataTable) {
        List<String> productNames = dataTable.asList(String.class);


        for (String productName : productNames) {
            pages.getProductsPage().addProductToCart(productName);
        }
    }

    @Then("the cart should contain the following products:")
    public void verifyCartContainsProducts(DataTable dataTable) {
        List<String> expectedProducts =
                dataTable.asList(String.class);


        List<String> actualProducts = pages.getCartPage().getProductNames();

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


        List<String> actualProducts =
                new ArrayList<>(pages.getCartPage().getProductNames());

        Collections.sort(expectedProducts);
        Collections.sort(actualProducts);

        Assert.assertEquals(
                actualProducts,
                expectedProducts,
                "Cart contents should match the expected products exactly."
        );
    }
}
