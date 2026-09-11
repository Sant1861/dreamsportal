package com.example.dreamsportal.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.List;

public class DreamsDiaryPage {

    private WebDriver driver;

    private By pageTitle =
            By.xpath("//h1[normalize-space()='Dreams Diary']");

    private By backToHomeButton =
            By.xpath("//button[contains(normalize-space(), 'Back to Home')]");

    private By addDreamButton =
            By.xpath("//button[normalize-space()='Add Dream']");

    private By editButtons =
            By.xpath("//button[normalize-space()='Edit']");

    private By removeButtons =
            By.xpath("//button[normalize-space()='Remove Dream']");

    public DreamsDiaryPage(WebDriver driver) {
        this.driver = driver;
    }

    public boolean isDiaryPageDisplayed() {
        return driver.findElement(pageTitle).isDisplayed();
    }

    public String getPageTitle() {
        return driver.findElement(pageTitle).getText();
    }

    public AddDreamPage clickAddDream() {
        driver.findElement(addDreamButton).click();
        return new AddDreamPage(driver);
    }

    public boolean isAddDreamEnabled() {
        return driver.findElement(addDreamButton).isEnabled();
    }

    public HomePage clickBackToHome() {
        driver.findElement(backToHomeButton).click();
        return new HomePage(driver);
    }

    public int getEditButtonCount() {

        List<WebElement> buttons =
                driver.findElements(editButtons);

        return buttons.size();
    }

    public boolean isDreamDisplayed(String dreamName) {

        By dream =
                By.xpath("//*[normalize-space()='" + dreamName + "']");

        return !driver.findElements(dream).isEmpty();
    }

    public AddDreamPage clickEditForDream(String dreamName) {

        By dreamRow =
                By.xpath(
                        "//tr[.//*[normalize-space()='" +
                        dreamName +
                        "']]"
                );

        WebElement editButton =
                driver.findElement(dreamRow)
                        .findElement(
                                By.xpath(
                                        ".//button[normalize-space()='Edit']"
                                )
                        );

        editButton.click();

        return new AddDreamPage(driver);
    }

    public int getRemoveButtonCount() {

        List<WebElement> buttons =
                driver.findElements(removeButtons);

        return buttons.size();
    }

    public void clickRemoveDream() {

        driver.findElement(removeButtons).click();
    }

    public void acceptRemoveConfirmation() {

        driver.switchTo().alert().accept();
    }

    public void cancelRemoveConfirmation() {

        driver.switchTo().alert().dismiss();
    }

    public boolean isDreamRemoved(String dreamName) {

        return !isDreamDisplayed(dreamName);
    }
}