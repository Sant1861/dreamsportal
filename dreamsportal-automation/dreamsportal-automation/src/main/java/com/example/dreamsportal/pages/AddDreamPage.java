package com.example.dreamsportal.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class AddDreamPage {

    private WebDriver driver;

    private WebDriverWait wait;

    private By pageTitle =
            By.xpath(
                    "//h1[normalize-space()='Add Dream' " +
                    "or normalize-space()='Edit Dream']"
            );

    private By dreamNameInput =
            By.xpath(
                    "//input[@type='text' " +
                    "and @placeholder='Enter your dream']"
            );

    private By daysAgoSelect =
            By.xpath(
                    "//label[normalize-space()='Days Ago']" +
                    "/following-sibling::select[1]"
            );

    private By dreamTypeSelect =
            By.xpath(
                    "//label[normalize-space()='Dream Type']" +
                    "/following-sibling::select[1]"
            );

    private By saveButton =
            By.xpath("//button[@type='submit']");

    private By cancelButton =
            By.xpath(
                    "//button[contains(" +
                    "normalize-space(), " +
                    "'Back to Dreams Diary')]"
            );

    public AddDreamPage(WebDriver driver) {

        this.driver = driver;

        this.wait =
                new WebDriverWait(
                        driver,
                        Duration.ofSeconds(10)
                );
    }

    public boolean isAddDreamPageDisplayed() {

        return wait.until(
                ExpectedConditions
                        .visibilityOfElementLocated(pageTitle)
        ).isDisplayed();
    }

    public String getPageTitle() {

        return wait.until(
                ExpectedConditions
                        .visibilityOfElementLocated(pageTitle)
        ).getText();
    }

    public void enterDreamName(String dreamName) {

        WebElement input =
                wait.until(
                        ExpectedConditions
                                .visibilityOfElementLocated(
                                        dreamNameInput
                                )
                );

        input.clear();

        input.sendKeys(dreamName);
    }

    public String getDreamName() {

        WebElement input =
                wait.until(
                        ExpectedConditions
                                .visibilityOfElementLocated(
                                        dreamNameInput
                                )
                );

        return input.getAttribute("value");
    }

    public void enterDaysAgo(String days) {

        WebElement dropdown =
                wait.until(
                        ExpectedConditions
                                .elementToBeClickable(
                                        daysAgoSelect
                                )
                );

        Select select =
                new Select(dropdown);

        String optionText =
                days.equals("1")
                        ? "1 day ago"
                        : days + " days ago";

        select.selectByVisibleText(optionText);
    }

    public String getSelectedDaysAgo() {

        WebElement dropdown =
                wait.until(
                        ExpectedConditions
                                .visibilityOfElementLocated(
                                        daysAgoSelect
                                )
                );

        Select select =
                new Select(dropdown);

        return select
                .getFirstSelectedOption()
                .getText();
    }

    public void selectGoodDream() {

        WebElement dropdown =
                wait.until(
                        ExpectedConditions
                                .elementToBeClickable(
                                        dreamTypeSelect
                                )
                );

        Select select =
                new Select(dropdown);

        select.selectByVisibleText("Good");
    }

    public void selectBadDream() {

        WebElement dropdown =
                wait.until(
                        ExpectedConditions
                                .elementToBeClickable(
                                        dreamTypeSelect
                                )
                );

        Select select =
                new Select(dropdown);

        select.selectByVisibleText("Bad");
    }

    public boolean isGoodDreamSelected() {

        WebElement dropdown =
                wait.until(
                        ExpectedConditions
                                .visibilityOfElementLocated(
                                        dreamTypeSelect
                                )
                );

        Select select =
                new Select(dropdown);

        return select
                .getFirstSelectedOption()
                .getText()
                .equals("Good");
    }

    public boolean isBadDreamSelected() {

        WebElement dropdown =
                wait.until(
                        ExpectedConditions
                                .visibilityOfElementLocated(
                                        dreamTypeSelect
                                )
                );

        Select select =
                new Select(dropdown);

        return select
                .getFirstSelectedOption()
                .getText()
                .equals("Bad");
    }

    public DreamsDiaryPage clickSaveDream() {

        wait.until(
                ExpectedConditions
                        .elementToBeClickable(saveButton)
        ).click();

        return new DreamsDiaryPage(driver);
    }

    public DreamsDiaryPage clickCancel() {

        wait.until(
                ExpectedConditions
                        .elementToBeClickable(cancelButton)
        ).click();

        return new DreamsDiaryPage(driver);
    }

    public DreamsDiaryPage clickBackToDiary() {

        wait.until(
                ExpectedConditions
                        .elementToBeClickable(cancelButton)
        ).click();

        return new DreamsDiaryPage(driver);
    }
}