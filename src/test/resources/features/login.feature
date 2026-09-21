@login
Feature: Login

  @smoke
  Scenario: User is able to login successfully
    Given I am on the Sauce Demo login page
    When I log in with username "standard_user" and password "secret_sauce"
    Then I should see the products page

  Scenario: Locked-out user cannot log in
    Given I am on the Sauce Demo login page
    When I log in with username "locked_out_usr" and password "secret_sauce"
    Then I should see the login error "Epic sadface: Sorry, this user has been locked out."

  @negative
  Scenario Outline: Reject login with invalid credentials
    Given I am on the Sauce Demo login page
    When I log in with username "<username>" and password "<password>"
    Then I should see the login error "<errorMessage>"

    Examples:
      | username      | password       | errorMessage                                                              |
      |               |                | Epic sadface: Username is required                                         |
      | standard_user |                | Epic sadface: Password is required                                         |
      | standard_user | wrong_password | Epic sadface: Username and password do not match any user in this service |