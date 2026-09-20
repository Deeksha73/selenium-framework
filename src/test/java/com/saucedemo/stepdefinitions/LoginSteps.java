package com.saucedemo.stepdefinitions;

import com.saucedemo.config.ConfigManager;
import com.saucedemo.driver.DriverManager;
import com.saucedemo.pages.LoginPage;
import com.saucedemo.pages.ProductsPage;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.testng.Assert;

public class LoginSteps {

    private LoginPage loginPage;
    private ProductsPage productsPage;

    @Given("I am on the Sauce Demo login page")
    public void i_am_on_the_sauce_demo_login_page() {
        loginPage = new LoginPage(DriverManager.getDriver());
        loginPage.open();
    }

    @When("I log in with username {string} and password {string}")
    public void i_log_in_with_username_and_password(String username, String password) {
        loginPage.login(username,password);
    }
    @Then("I should see the products page")
    public void i_should_see_the_products_page() {
        productsPage = new ProductsPage(DriverManager.getDriver());

        productsPage.waitUntilLoaded();

        Assert.assertEquals(
                productsPage.getHeadingText(),
                "Products",
                "The products page heading should be displayed after login."
        );


    }

    @Then("I should see the login error {string}")
    public void verifyLoginError(String expectedMessage) {
        Assert.assertEquals(
                loginPage.getErrorMessage(),
                expectedMessage,
                "The login error should match the expected message."
        );
    }
}
