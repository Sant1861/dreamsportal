package com.example.dreamsportal.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class HomePage {

    private WebDriver driver;

    private By pageTitle =
            By.xpath("//h1[normalize-space()='Dream Portal']");

    private By myDreamsButton =
            By.xpath("//button[normalize-space()='MY DREAMS']");

    private By viewSummaryButton =
            By.xpath("//button[normalize-space()='VIEW SUMMARY']");

    private By dreamsCount =
            By.xpath("//*[normalize-space()='Dreams']/preceding-sibling::*[1]");

    private By daysCount =
            By.xpath("//*[normalize-space()='Days']/preceding-sibling::*[1]");

    public HomePage(WebDriver driver) {
        this.driver = driver;
    }

    public String getPageTitleText() {
        return driver.findElement(pageTitle).getText();
    }

    public boolean isHomePageDisplayed() {
        return driver.findElement(pageTitle).isDisplayed();
    }

    public DreamsDiaryPage clickMyDreams() {
        driver.findElement(myDreamsButton).click();
        return new DreamsDiaryPage(driver);
    }

    public DreamsTotalPage clickViewSummary() {
        driver.findElement(viewSummaryButton).click();
        return new DreamsTotalPage(driver);
    }

    public String getDreamsCount() {
        return driver.findElement(dreamsCount).getText();
    }

    public String getDaysCount() {
        return driver.findElement(daysCount).getText();
    }
}