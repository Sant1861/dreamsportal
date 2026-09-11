package com.example.dreamsportal.tests;

import com.example.dreamsportal.base.BaseTest;
import com.example.dreamsportal.pages.DreamsDiaryPage;
import com.example.dreamsportal.pages.DreamsTotalPage;
import com.example.dreamsportal.pages.HomePage;
import org.testng.Assert;
import org.testng.annotations.Test;

public class HomePageTest extends BaseTest {

    @Test
    public void verifyHomePageDisplayed() {

        HomePage homePage =
                new HomePage(driver);

        Assert.assertTrue(
                homePage.isHomePageDisplayed(),
                "Home page is not displayed"
        );
    }

    @Test
    public void verifyHomePageTitle() {

        HomePage homePage =
                new HomePage(driver);

        String actualTitle =
                homePage.getPageTitleText();

        Assert.assertEquals(
                actualTitle,
                "Dream Portal",
                "Home page title is incorrect"
        );
    }

    @Test
    public void verifyMyDreamsNavigation() {

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
    public void verifyViewSummaryNavigation() {

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
    public void verifyDreamsCountIsDisplayed() {

        HomePage homePage =
                new HomePage(driver);

        String count =
                homePage.getDreamsCount();

        Assert.assertFalse(
                count.isEmpty(),
                "Dream count is empty"
        );

        Assert.assertTrue(
                count.matches("\\d+"),
                "Dream count should contain only numbers"
        );

        int dreamCount =
                Integer.parseInt(count);

        Assert.assertTrue(
                dreamCount >= 0,
                "Dream count cannot be negative"
        );
    }

    @Test
    public void verifyDaysCountIsDisplayed() {

        HomePage homePage =
                new HomePage(driver);

        String count =
                homePage.getDaysCount();

        Assert.assertFalse(
                count.isEmpty(),
                "Days count is empty"
        );

        Assert.assertTrue(
                count.matches("\\d+"),
                "Days count should contain only numbers"
        );

        int daysCount =
                Integer.parseInt(count);

        Assert.assertTrue(
                daysCount >= 0,
                "Days count cannot be negative"
        );
    }
}