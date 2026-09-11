package com.example.dreamsportal.tests;

import com.example.dreamsportal.base.BaseTest;
import com.example.dreamsportal.pages.DreamsDiaryPage;
import com.example.dreamsportal.pages.DreamsTotalPage;
import com.example.dreamsportal.pages.HomePage;
import org.testng.Assert;
import org.testng.annotations.Test;

public class EndToEndTest extends BaseTest {

    @Test
    public void verifyCompleteDreamJourney() {

        // Home
        HomePage homePage =
                new HomePage(driver);

        Assert.assertTrue(
                homePage.isHomePageDisplayed(),
                "Home page is not displayed"
        );

        // Dreams Diary
        DreamsDiaryPage diaryPage =
                homePage.clickMyDreams();

        Assert.assertTrue(
                diaryPage.isDiaryPageDisplayed(),
                "Dreams Diary page is not displayed"
        );

        // Validate dream limit
        int dreamCount =
                diaryPage.getEditButtonCount();

        Assert.assertTrue(
                dreamCount <= 10,
                "Dream count exceeds 10"
        );

        if (dreamCount < 10) {

            Assert.assertTrue(
                    diaryPage.isAddDreamEnabled(),
                    "Add Dream should be enabled below 10 dreams"
            );

        } else {

            Assert.assertFalse(
                    diaryPage.isAddDreamEnabled(),
                    "Add Dream should be disabled at 10 dreams"
            );
        }

        // Back to Home
        homePage =
                diaryPage.clickBackToHome();

        Assert.assertTrue(
                homePage.isHomePageDisplayed(),
                "Could not return to Home page"
        );

        // Summary
        DreamsTotalPage summaryPage =
                homePage.clickViewSummary();

        Assert.assertTrue(
                summaryPage.isSummaryPageDisplayed(),
                "Dreams Summary page is not displayed"
        );

        // Validate Good Dreams count
        String goodDreamsCount =
                summaryPage.getGoodDreamsCount();

        Assert.assertFalse(
                goodDreamsCount.isEmpty(),
                "Good Dreams count is empty"
        );

        Assert.assertTrue(
                goodDreamsCount.matches("\\d+"),
                "Good Dreams count should contain only numbers"
        );

        // Validate Bad Dreams count
        String badDreamsCount =
                summaryPage.getBadDreamsCount();

        Assert.assertFalse(
                badDreamsCount.isEmpty(),
                "Bad Dreams count is empty"
        );

        Assert.assertTrue(
                badDreamsCount.matches("\\d+"),
                "Bad Dreams count should contain only numbers"
        );

        // Validate Total Dreams count
        String totalDreamsCount =
                summaryPage.getTotalDreamsCount();

        Assert.assertFalse(
                totalDreamsCount.isEmpty(),
                "Total Dreams count is empty"
        );

        Assert.assertTrue(
                totalDreamsCount.matches("\\d+"),
                "Total Dreams count should contain only numbers"
        );

        // Validate Total Days count
        String totalDaysCount =
                summaryPage.getTotalDaysCount();

        Assert.assertFalse(
                totalDaysCount.isEmpty(),
                "Total Days count is empty"
        );

        Assert.assertTrue(
                totalDaysCount.matches("\\d+"),
                "Total Days count should contain only numbers"
        );
    }
}