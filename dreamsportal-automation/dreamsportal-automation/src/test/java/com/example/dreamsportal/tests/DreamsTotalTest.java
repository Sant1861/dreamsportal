package com.example.dreamsportal.tests;

import com.example.dreamsportal.base.BaseTest;
import com.example.dreamsportal.pages.DreamsTotalPage;
import com.example.dreamsportal.pages.HomePage;
import org.testng.Assert;
import org.testng.annotations.Test;

public class DreamsTotalTest extends BaseTest {

    @Test
    public void verifySummaryPageDisplayed() {

        HomePage homePage =
                new HomePage(driver);

        DreamsTotalPage summaryPage =
                homePage.clickViewSummary();

        Assert.assertTrue(
                summaryPage.isSummaryPageDisplayed(),
                "Dreams Summary page is not displayed"
        );
    }

    @Test
    public void verifySummaryPageTitle() {

        HomePage homePage =
                new HomePage(driver);

        DreamsTotalPage summaryPage =
                homePage.clickViewSummary();

        Assert.assertEquals(
                summaryPage.getPageTitle(),
                "Dreams Summary",
                "Summary page title is incorrect"
        );
    }

    @Test
    public void verifyGoodDreamsCountDisplayed() {

        HomePage homePage =
                new HomePage(driver);

        DreamsTotalPage summaryPage =
                homePage.clickViewSummary();

        String count =
                summaryPage.getGoodDreamsCount();

        Assert.assertFalse(
                count.isEmpty(),
                "Good Dreams count is empty"
        );

        Assert.assertTrue(
                count.matches("\\d+"),
                "Good Dreams count should contain only numbers"
        );

        int goodDreamsCount =
                Integer.parseInt(count);

        Assert.assertTrue(
                goodDreamsCount >= 0,
                "Good Dreams count cannot be negative"
        );
    }

    @Test
    public void verifyBadDreamsCountDisplayed() {

        HomePage homePage =
                new HomePage(driver);

        DreamsTotalPage summaryPage =
                homePage.clickViewSummary();

        String count =
                summaryPage.getBadDreamsCount();

        Assert.assertFalse(
                count.isEmpty(),
                "Bad Dreams count is empty"
        );

        Assert.assertTrue(
                count.matches("\\d+"),
                "Bad Dreams count should contain only numbers"
        );

        int badDreamsCount =
                Integer.parseInt(count);

        Assert.assertTrue(
                badDreamsCount >= 0,
                "Bad Dreams count cannot be negative"
        );
    }

    @Test
    public void verifyTotalDreamsDisplayed() {

        HomePage homePage =
                new HomePage(driver);

        DreamsTotalPage summaryPage =
                homePage.clickViewSummary();

        String count =
                summaryPage.getTotalDreamsCount();

        Assert.assertFalse(
                count.isEmpty(),
                "Total Dreams count is empty"
        );

        Assert.assertTrue(
                count.matches("\\d+"),
                "Total Dreams count should contain only numbers"
        );

        int totalDreamsCount =
                Integer.parseInt(count);

        Assert.assertTrue(
                totalDreamsCount >= 0,
                "Total Dreams count cannot be negative"
        );
    }

    @Test
    public void verifyTotalDaysDisplayed() {

        HomePage homePage =
                new HomePage(driver);

        DreamsTotalPage summaryPage =
                homePage.clickViewSummary();

        String count =
                summaryPage.getTotalDaysCount();

        Assert.assertFalse(
                count.isEmpty(),
                "Total Days count is empty"
        );

        Assert.assertTrue(
                count.matches("\\d+"),
                "Total Days count should contain only numbers"
        );

        int totalDaysCount =
                Integer.parseInt(count);

        Assert.assertTrue(
                totalDaysCount >= 0,
                "Total Days count cannot be negative"
        );
    }

    @Test
    public void verifyBackToHome() {

        HomePage homePage =
                new HomePage(driver);

        DreamsTotalPage summaryPage =
                homePage.clickViewSummary();

        HomePage returnedHomePage =
                summaryPage.clickBackToHome();

        Assert.assertTrue(
                returnedHomePage.isHomePageDisplayed(),
                "Could not return to Home page"
        );
    }
}