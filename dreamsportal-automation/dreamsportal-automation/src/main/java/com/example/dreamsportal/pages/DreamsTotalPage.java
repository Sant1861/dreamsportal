package com.example.dreamsportal.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class DreamsTotalPage {

    private WebDriver driver;
    private WebDriverWait wait;

    private By pageTitle =
            By.xpath("//h1[normalize-space()='Dreams Summary']");

    private By backToHomeButton =
            By.xpath("//button[contains(normalize-space(), 'Back to Home')]");

    private By goodDreamsCount =
            By.xpath(
                    "//td[normalize-space()='Good Dreams']" +
                    "/following-sibling::td[1]"
            );

    private By badDreamsCount =
            By.xpath(
                    "//td[normalize-space()='Bad Dreams']" +
                    "/following-sibling::td[1]"
            );

    private By totalDreamsCount =
            By.xpath(
                    "//td[normalize-space()='Total Dreams']" +
                    "/following-sibling::td[1]"
            );

    private By totalDaysCount =
            By.xpath(
                    "//td[normalize-space()='Total Days']" +
                    "/following-sibling::td[1]"
            );

    public DreamsTotalPage(WebDriver driver) {

        this.driver = driver;

        this.wait =
                new WebDriverWait(
                        driver,
                        Duration.ofSeconds(10)
                );
    }

    public String getPageTitle() {

        return wait.until(
                ExpectedConditions.visibilityOfElementLocated(
                        pageTitle
                )
        ).getText();
    }

    public boolean isSummaryPageDisplayed() {

        return wait.until(
                ExpectedConditions.visibilityOfElementLocated(
                        pageTitle
                )
        ).isDisplayed();
    }

    public String getGoodDreamsCount() {

        return waitForCount(goodDreamsCount);
    }

    public String getBadDreamsCount() {

        return waitForCount(badDreamsCount);
    }

    public String getTotalDreamsCount() {

        return waitForCount(totalDreamsCount);
    }

    public String getTotalDaysCount() {

        return waitForCount(totalDaysCount);
    }

    private String waitForCount(By locator) {

        return wait.until(
                ExpectedConditions.textMatches(
                        locator,
                        java.util.regex.Pattern.compile("\\d+")
                )
        ) ?
                driver.findElement(locator).getText()
                :
                driver.findElement(locator).getText();
    }

    public HomePage clickBackToHome() {

        wait.until(
                ExpectedConditions.elementToBeClickable(
                        backToHomeButton
                )
        ).click();

        return new HomePage(driver);
    }
}