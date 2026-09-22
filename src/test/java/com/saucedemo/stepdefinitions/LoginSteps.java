package com.saucedemo.stepdefinitions;

import com.saucedemo.context.PageObjectManager;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.testng.Assert;

public class LoginSteps {

    private final PageObjectManager pages;

    public LoginSteps(PageObjectManager pages) {
        this.pages = pages;
    }

    @Given("I am on the Sauce Demo login page")
    public void i_am_on_the_sauce_demo_login_page() {
        pages.getLoginPage().open();
    }

    @When("I log in with username {string} and password {string}")
    public void i_log_in_with_username_and_password(String username, String password) {
        pages.getLoginPage().login(username,password);
    }
    @Then("I should see the products page")
    public void i_should_see_the_products_page() {

        pages.getProductsPage().waitUntilLoaded();

//        Assert.assertEquals(
//                pages.getProductsPage().getHeadingText(),
//                "Products",
//                "The products page heading should be displayed after login."
//        );


    }

    @Then("I should see the login error {string}")
    public void verifyLoginError(String expectedMessage) {
        Assert.assertEquals(
                pages.getLoginPage().getErrorMessage(),
                expectedMessage,
                "The login error should match the expected message."
        );
    }
}
