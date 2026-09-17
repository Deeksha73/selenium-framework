Feature: Login

  Scenario: User is able to login successfully
    Given I am on the Sauce Demo login page
    When I log in with username "standard_user" and password "secret_sauce"
    Then I should see the products page

  Scenario: Locked-out user cannot log in
    Given I am on the Sauce Demo login page
    When I log in with username "locked_out_user" and password "secret_sauce"
    Then I should see the login error "Epic sadface: Sorry, this user has been locked out."