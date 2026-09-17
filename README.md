# SauceDemo Automation

A Selenium Java framework for testing the SauceDemo login flow using Cucumber, TestNG, and the Page Object Model. The framework is under development and currently covers successful login and locked-out user validation.

## Technology stack

Versions configured in `pom.xml`:

| Technology | Version / purpose |
| --- | --- |
| Java | 26 |
| Selenium WebDriver | 4.49.0 — browser automation |
| Cucumber | 7.34.8 — Gherkin scenarios and step definitions |
| TestNG | 7.12.0 — test execution and assertions |
| Maven Compiler Plugin | 3.16.0 |
| Maven Surefire Plugin | 3.6.0 — Maven test execution |

## Current capabilities

- Chrome and Firefox execution, selected through configuration or a command-line property.
- Page objects for the login and products pages, with shared explicit-wait setup.
- A fresh browser session for each scenario and cleanup after execution.
- Screenshots attached to failed Cucumber scenarios.
- Console output and an HTML Cucumber report.

## Prerequisites

- JDK 26, matching the compiler settings in `pom.xml`.
- Maven installed and available on your command line; the repository does not include a Maven wrapper.
- Chrome or Firefox installed, depending on the browser selected.
- Access to the SauceDemo application and dependency downloads. Driver startup may also require access to download a compatible browser driver.

Check the Java version Maven uses:

```sh
java -version
mvn -version
```

## Getting started

Open a terminal in the repository root, where `pom.xml` is located, and run:

```sh
mvn clean test
```

The default browser is Chrome. Tests run with a visible browser window.

To select a browser explicitly:

```sh
mvn clean test -Dbrowser=chrome
mvn clean test -Dbrowser=firefox
```

To select the Cucumber runner explicitly:

```sh
mvn test -Dtest=TestRunner
```

## Configuration

Edit `src/test/resources/config/config.properties`:

```properties
base.url=https://www.saucedemo.com/
timeout.seconds=10
browser=chrome
```

| Property | Purpose |
| --- | --- |
| `base.url` | Application URL. Keep the trailing `/` because the products page check appends `inventory.html`. |
| `timeout.seconds` | Maximum explicit wait duration for page conditions and elements. |
| `browser` | Supported values: `chrome` and `firefox`. The `-Dbrowser` command-line value overrides this setting. |

Only `browser` currently supports a Java system-property override. Change the properties file to update the URL or timeout.

## Project structure

```text
saucedemo-automation/
├── pom.xml
└── src/
    ├── main/java/com/saucedemo/
    │   ├── config/
    │   │   └── ConfigManager.java
    │   ├── driver/
    │   │   ├── DriverFactory.java
    │   │   └── DriverManager.java
    │   └── pages/
    │       ├── BasePage.java
    │       ├── LoginPage.java
    │       └── ProductPage.java
    └── test/
        ├── java/com/saucedemo/
        │   ├── hooks/CucumberHooks.java
        │   ├── runners/TestRunner.java
        │   └── stepdefinitions/LoginSteps.java
        └── resources/
            ├── config/config.properties
            └── features/login.feature
```

`TestRunner` connects Cucumber to TestNG and discovers the feature files, step definitions, and hooks. Before each scenario, the hooks create a browser through `DriverManager` and `DriverFactory`. Step definitions call page objects and assert results. After each scenario, the hooks attach a screenshot on failure and close the browser.

## Test coverage

The scenarios in `login.feature` verify:

1. `standard_user` can log in and reach the products page, where the heading is `Products`.
2. `locked_out_user` receives the expected locked-out error message.

Both scenarios use SauceDemo's sample password, `secret_sauce`.

## Reports

After execution, open `target/cucumber-reports.html` in a browser. Failed-scenario screenshots are attached to the Cucumber report. Maven Surefire also writes test results under `target/surefire-reports/`.

**Known reporting configuration issue:** the second plugin entry in `TestRunner.java` is currently `html:targer/cucumber-reports.json`. It writes HTML content to a file with a `.json` extension in the misspelled `targer` directory. JSON reporting requires changing that entry to:

```java
"json:target/cucumber-reports.json"
```

`mvn clean` removes the standard `target` directory; it does not clean the separate `targer` directory.

## Adding tests

1. Add a scenario to a `.feature` file under `src/test/resources/features`.
2. Add or reuse step definitions in `com.saucedemo.stepdefinitions`.
3. Keep page locators and browser interactions in page objects under `com.saucedemo.pages`. Extend `BasePage` to reuse the configured explicit wait.
4. Keep assertions in the step definitions and use the existing hooks for browser setup and cleanup.
5. Run `mvn clean test` and review the HTML report.

## Current limitations and troubleshooting

- **Parallel execution:** the framework stores one shared static WebDriver instance. Keep scenarios sequential until driver management is isolated per thread.
- **Headless execution:** no headless option is currently implemented.
- **Java compilation errors:** if Maven reports an unsupported target release, check that `mvn -version` uses JDK 26.
- **Unsupported browser:** use `chrome` or `firefox`; other values are rejected by `DriverFactory`.
- **Browser startup failures:** confirm the selected browser is installed and a compatible driver can be resolved in your environment.
- **Wait timeouts:** confirm that the application is reachable and that the expected page or element is present before adjusting `timeout.seconds`.
