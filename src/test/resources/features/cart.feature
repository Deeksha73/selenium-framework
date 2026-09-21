@cart
Feature: Shopping cart

  Scenario: Add a backpack to the cart
    Given I am on the Sauce Demo login page
    When I log in with username "standard_user" and password "secret_sauce"
    Then I should see the products page
    When I add "Sauce Labs Backpack" to the cart
    And I open the shopping cart
    Then the cart should contain "Sauce Labs Backpack"

  @multipleProducts
  Scenario: Add multiple products to the cart
    Given I am on the Sauce Demo login page
    When I log in with username "standard_user" and password "secret_sauce"
    Then I should see the products page
    When I add the following products to the cart:
      | Sauce Labs ackpack   |
      | Sauce Labs Bike Light |
    And I open the shopping cart
    Then the cart should contain the following products:
      | Sauce Labs Backpack   |
      | Sauce Labs Bike Light |
    Then the cart should contain exactly these products:
      | Sauce Labs Backpack   |
      | Sauce Labs Bike Light |