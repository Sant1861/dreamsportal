package com.example.dreamsportal.tests;

import com.example.dreamsportal.base.BaseTest;
import com.example.dreamsportal.pages.AddDreamPage;
import com.example.dreamsportal.pages.DreamsDiaryPage;
import com.example.dreamsportal.pages.HomePage;
import org.testng.Assert;
import org.testng.annotations.Test;

public class DreamsDiaryTest extends BaseTest {

    @Test
    public void verifyDreamsDiaryPageDisplayed() {

        HomePage homePage =
                new HomePage(driver);

        DreamsDiaryPage diaryPage =
                homePage.clickMyDreams();

        Assert.assertTrue(
                diaryPage.isDiaryPageDisplayed(),
                "Dreams Diary page is not displayed"
        );
    }

    @Test
    public void verifyDreamsDiaryPageTitle() {

        HomePage homePage =
                new HomePage(driver);

        DreamsDiaryPage diaryPage =
                homePage.clickMyDreams();

        Assert.assertEquals(
                diaryPage.getPageTitle(),
                "Dreams Diary",
                "Dreams Diary page title is incorrect"
        );
    }

    @Test
    public void verifyBackToHome() {

        HomePage homePage =
                new HomePage(driver);

        DreamsDiaryPage diaryPage =
                homePage.clickMyDreams();

        HomePage returnedHomePage =
                diaryPage.clickBackToHome();

        Assert.assertTrue(
                returnedHomePage.isHomePageDisplayed(),
                "Could not navigate back to Home page"
        );
    }

    @Test
    public void verifyAddDreamButton() {

        HomePage homePage =
                new HomePage(driver);

        DreamsDiaryPage diaryPage =
                homePage.clickMyDreams();

        int dreamCount =
                diaryPage.getEditButtonCount();

        Assert.assertTrue(
                dreamCount <= 10,
                "Dream count exceeds the maximum limit of 10"
        );

        if (dreamCount < 10) {

            Assert.assertTrue(
                    diaryPage.isAddDreamEnabled(),
                    "Add Dream button should be enabled when fewer than 10 dreams exist"
            );

        } else {

            Assert.assertFalse(
                    diaryPage.isAddDreamEnabled(),
                    "Add Dream button should be disabled when 10 dreams exist"
            );
        }
    }

    @Test
    public void verifyDreamCountDoesNotExceedTen() {

        HomePage homePage =
                new HomePage(driver);

        DreamsDiaryPage diaryPage =
                homePage.clickMyDreams();

        int dreamCount =
                diaryPage.getEditButtonCount();

        Assert.assertTrue(
                dreamCount <= 10,
                "Dream count exceeds the maximum limit of 10"
        );
    }

    @Test
    public void verifyAddDreamPageOpensWithoutSaving() {

        HomePage homePage =
                new HomePage(driver);

        DreamsDiaryPage diaryPage =
                homePage.clickMyDreams();

        int dreamCount =
                diaryPage.getEditButtonCount();

        if (dreamCount < 10) {

            Assert.assertTrue(
                    diaryPage.isAddDreamEnabled(),
                    "Add Dream should be enabled when fewer than 10 dreams exist"
            );

            AddDreamPage addDreamPage =
                    diaryPage.clickAddDream();

            Assert.assertTrue(
                    addDreamPage.isAddDreamPageDisplayed(),
                    "Add Dream page is not displayed"
            );

            addDreamPage.clickBackToDiary();

        } else {

            Assert.assertFalse(
                    diaryPage.isAddDreamEnabled(),
                    "Add Dream should be disabled at 10 dreams"
            );
        }
    }
}