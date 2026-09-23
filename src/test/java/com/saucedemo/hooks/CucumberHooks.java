package com.saucedemo.hooks;

import com.saucedemo.driver.DriverManager;
import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.BeforeStep;
import org.openqa.selenium.JavascriptExecutor;
import io.cucumber.java.Scenario;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.logging.LogType;

public class CucumberHooks {

    private final DriverManager driverManager;

    public CucumberHooks(DriverManager driverManager) {
        this.driverManager = driverManager;
    }

    @Before
    public void setUp() {
        driverManager.createDriver();
    }

    // Temporary diagnostics: observe real input without changing the click or retrying it.
    @BeforeStep
    public void observeCartClicks() {
        if (!driverManager.hasDriver()) {
            return;
        }
        try {
            ((JavascriptExecutor) driverManager.getDriver()).executeScript("""
                if (!document.querySelector('.inventory_list') || window.__cartTrace) return;
                const trace = window.__cartTrace = [];
                const buttons = [...document.querySelectorAll('.btn_inventory')];
                const record = (type, details) => {
                    if (trace.length < 200) trace.push({time: performance.now(), type, ...details});
                };
                record('initial', {url: location.href, ready: document.readyState,
                    visibility: document.visibilityState,
                    viewport: [innerWidth, innerHeight],
                    buttons: buttons.map(b => ({id: b.id, text: b.textContent}))});
                for (const type of ['pointerdown', 'mousedown', 'pointerup', 'mouseup', 'click']) {
                    document.addEventListener(type, e => {
                        const target = e.target;
                        record(type, {target: target.outerHTML?.slice(0, 600),
                            trusted: e.isTrusted, x: e.clientX, y: e.clientY,
                            originalButtonsConnected: buttons.map(b => b.isConnected)});
                    }, true);
                }
                new MutationObserver(() => record('inventory-change', {
                    buttons: [...document.querySelectorAll('.btn_inventory')]
                        .map(b => ({id: b.id, text: b.textContent})),
                    originalButtonsConnected: buttons.map(b => b.isConnected)
                })).observe(document.querySelector('.inventory_list'),
                    {subtree: true, childList: true, attributes: true, characterData: true});
                """);
        } catch (RuntimeException ignored) {
            // Diagnostics must not prevent a scenario from running.
        }
    }

    @After
    public void tearDown(Scenario scenario) {
        try {
            if (scenario.isFailed() && driverManager.hasDriver()) {
                try {
                    Object trace = ((JavascriptExecutor) driverManager.getDriver()).executeScript("""
                        return JSON.stringify({events: window.__cartTrace || [],
                            url: location.href, ready: document.readyState,
                            buttons: [...document.querySelectorAll('.btn_inventory')]
                                .map(b => ({id: b.id, text: b.textContent})),
                            badge: document.querySelector('.shopping_cart_badge')?.textContent}, null, 2);
                        """);
                    scenario.attach(String.valueOf(trace), "application/json", "Cart Click Trace");
                } catch (RuntimeException exception) {
                    scenario.log("Click trace unavailable: " + exception.getMessage());
                }
                try {
                    WebDriver driver = driverManager.getDriver();
                    if (driver instanceof TakesScreenshot screenshotDriver) {
                        scenario.attach(
                            screenshotDriver.getScreenshotAs(OutputType.BYTES),
                            "image/png",
                            "Failure Screenshot"
                        );
                    }
                } catch (RuntimeException exception) {
                    scenario.log("Screenshot could not be captured: " + exception.getMessage());
                }

                try {
                    StringBuilder browserLogs = new StringBuilder();
                    driverManager.getDriver().manage().logs().get(LogType.BROWSER)
                        .forEach(entry -> browserLogs.append(entry).append("\n"));
                    scenario.attach(
                        browserLogs.isEmpty() ? "No browser console entries captured." : browserLogs.toString(),
                        "text/plain",
                        "Browser Console Logs"
                    );
                } catch (RuntimeException exception) {
                    scenario.log("Browser logs could not be captured: " + exception.getMessage());
                }
            }
        } finally {
            try {
                driverManager.quitDriver();
            } catch (RuntimeException exception) {
                if (!scenario.isFailed()) {
                    throw exception;
                }
                scenario.log("Browser cleanup failed: " + exception.getMessage());
            }
        }
    }
}
