# SauceDemo Automation

A Selenium Java framework for testing SauceDemo login and shopping cart flows using Cucumber, TestNG, and the Page Object Model. PicoContainer shares browser and page objects within each scenario.

## Technology stack

Versions configured in `pom.xml`:

| Technology | Version / purpose |
| --- | --- |
| Java | 26 |
| Selenium WebDriver | 4.49.0 — browser automation |
| Cucumber and PicoContainer | 7.34.8 — Gherkin scenarios, step definitions, and scenario-scoped dependency injection |
| TestNG | 7.12.0 — test execution and assertions |
| Maven Compiler Plugin | 3.16.0 |
| Maven Surefire Plugin | 3.6.0 — Maven test execution |

## Current capabilities

- Chrome and Firefox execution, selected through configuration or a command-line property.
- Page objects for the login, products, and cart pages, with shared explicit-wait setup.
- Visible or headless execution with a 1440 × 900 browser window.
- Data-driven login validation and cart assertions using Cucumber scenario outlines and data tables.
- A fresh browser session for each scenario and cleanup after execution.
- Screenshots attached to failed Cucumber scenarios.
- Console output, HTML and JSON Cucumber reports, and Maven Surefire results.

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

To run headlessly (also supported with Firefox):

```sh
mvn clean test -Dbrowser=chrome -Dheadless=true
```

To select the Cucumber runner explicitly:

```sh
mvn test -Dtest=TestRunner
```

To run a subset by tag:

```sh
mvn test -Dcucumber.filter.tags="@smoke"
mvn test -Dcucumber.filter.tags="@login"
mvn test -Dcucumber.filter.tags="@negative"
mvn test -Dcucumber.filter.tags="@cart"
mvn test -Dcucumber.filter.tags="@multipleProducts"
```

Browser, headless, and tag options can be combined in the same command.

## Configuration

Edit `src/test/resources/config/config.properties`:

```properties
base.url=https://www.saucedemo.com/
timeout.seconds=10
browser=chrome
headless=false
```

| Property | Purpose |
| --- | --- |
| `base.url` | Application URL. Keep the trailing `/` because the products page check appends `inventory.html`. |
| `timeout.seconds` | Maximum explicit wait duration for page conditions and elements. |
| `browser` | Supported values: `chrome` and `firefox`. The `-Dbrowser` command-line value overrides this setting. |
| `headless` | `true` or `false`; defaults to `false`. The `-Dheadless` command-line value overrides this setting. |

Only `browser` and `headless` support Java system-property overrides. Change the properties file to update the URL or timeout.

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
    │       ├── CartPage.java
    │       ├── LoginPage.java
    │       └── ProductsPage.java
    └── test/
        ├── java/com/saucedemo/
        │   ├── context/PageObjectManager.java
        │   ├── hooks/CucumberHooks.java
        │   ├── runners/TestRunner.java
        │   └── stepdefinitions/
        │       ├── CartSteps.java
        │       └── LoginSteps.java
        └── resources/
            ├── config/config.properties
            └── features/
                ├── cart.feature
                └── login.feature
```

`TestRunner` connects Cucumber to TestNG and discovers the feature files, step definitions, and hooks. Before each scenario, the hooks create a browser through `DriverManager` and `DriverFactory`. Step definitions call page objects and assert results. After each scenario, the hooks attach a screenshot on failure and close the browser.

## Test coverage

The suite currently contains **seven scenario executions** across two feature files (including three example rows in the login scenario outline).

| Feature | Coverage | Tags |
| --- | --- | --- |
| `login.feature` | Successful login and the `Products` heading | `@login`, `@smoke` |
| `login.feature` | Locked-out user error | `@login` |
| `login.feature` | Missing username, missing password, and incorrect password errors | `@login`, `@negative` |
| `cart.feature` | Add a backpack and verify it appears in the cart | `@cart` |
| `cart.feature` | Add a backpack and bike light; verify presence and exact cart contents, regardless of order | `@cart`, `@multipleProducts` |

Successful login and locked-out user scenarios use SauceDemo's sample password, `secret_sauce`.

## Reports

| Output | Location |
| --- | --- |
| Cucumber HTML report | `target/cucumber-reports.html` |
| Cucumber JSON report | `target/cucumber-reports.json` |
| Maven Surefire results | `target/surefire-reports/` |

After execution, open the HTML report in a browser. Failed-scenario screenshots are attached when a browser is available and capture succeeds. Screenshot failures are logged, and browser cleanup is still attempted. `mvn clean` removes previous reports.

## Adding tests

1. Add a scenario to a `.feature` file under `src/test/resources/features`.
2. Add or reuse step definitions in `com.saucedemo.stepdefinitions`.
3. Keep page locators and browser interactions in page objects under `com.saucedemo.pages`. Extend `BasePage` to reuse the configured explicit wait.
4. Inject `PageObjectManager` into step definition constructors to share pages within a scenario. Add a lazy getter there for any new page object.
5. Keep assertions in the step definitions and use the existing hooks for browser setup and cleanup.
6. Run `mvn clean test` and review the HTML report.

## Current limitations and troubleshooting

- **Parallel execution:** drivers and page objects are scenario-scoped through PicoContainer. Execution remains sequential; parallel execution has not yet been enabled or validated.
- **Invalid headless setting:** use `true` or `false`; other values are rejected by `ConfigManager`.
- **Java compilation errors:** if Maven reports an unsupported target release, check that `mvn -version` uses JDK 26.
- **Unsupported browser:** use `chrome` or `firefox`; other values are rejected by `DriverFactory`.
- **Browser startup failures:** confirm the selected browser is installed and a compatible driver can be resolved in your environment.
- **Wait timeouts:** confirm that the application is reachable and that the expected page or element is present before adjusting `timeout.seconds`.

## Scenario-scoped dependency injection

`cucumber-picocontainer` creates a fresh dependency graph for each scenario.
Hooks receive a `DriverManager`; `LoginSteps` and `CartSteps` receive the same
`PageObjectManager`, which receives that same driver manager. Browser state is
not static. Constructors only store dependencies; the `Before` hook starts the
browser. Page getters lazily create and cache pages after browser startup.
The `After` hook captures failures where possible and always attempts cleanup.
