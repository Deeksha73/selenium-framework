package com.saucedemo.stepdefinitions;

import com.saucedemo.driver.DriverManager;
import com.saucedemo.pages.LoginPage;
import com.saucedemo.pages.ProductPage;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.testng.Assert;

import java.time.Duration;

public class LoginSteps {

    private LoginPage loginPage;
    private ProductPage productPage;

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
        productPage = new ProductPage(DriverManager.getDriver());

        productPage.waitUntilLoaded();

        Assert.assertEquals(
                productPage.getHeadingText(),
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
