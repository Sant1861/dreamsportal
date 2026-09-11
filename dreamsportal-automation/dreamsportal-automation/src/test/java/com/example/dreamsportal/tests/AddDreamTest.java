package com.example.dreamsportal.tests;

import com.example.dreamsportal.base.BaseTest;
import com.example.dreamsportal.pages.AddDreamPage;
import com.example.dreamsportal.pages.DreamsDiaryPage;
import com.example.dreamsportal.pages.HomePage;
import org.testng.Assert;
import org.testng.annotations.Test;

public class AddDreamTest extends BaseTest {

    @Test
    public void verifyAddDreamPageDisplayed() {

        HomePage homePage =
                new HomePage(driver);

        DreamsDiaryPage diaryPage =
                homePage.clickMyDreams();

        int dreamCount =
                diaryPage.getEditButtonCount();

        if (dreamCount < 10) {

            Assert.assertTrue(
                    diaryPage.isAddDreamEnabled(),
                    "Add Dream should be enabled below 10 dreams"
            );

            AddDreamPage addDreamPage =
                    diaryPage.clickAddDream();

            Assert.assertTrue(
                    addDreamPage.isAddDreamPageDisplayed(),
                    "Add Dream page is not displayed"
            );

            addDreamPage.clickBackToDiary();

        } else {

            Assert.assertEquals(
                    dreamCount,
                    10,
                    "Dream count is greater than 10"
            );

            Assert.assertFalse(
                    diaryPage.isAddDreamEnabled(),
                    "Add Dream should be disabled at 10 dreams"
            );
        }
    }

    @Test
    public void verifyDreamNameField() {

        HomePage homePage =
                new HomePage(driver);

        DreamsDiaryPage diaryPage =
                homePage.clickMyDreams();

        int dreamCount =
                diaryPage.getEditButtonCount();

        if (dreamCount < 10) {

            Assert.assertTrue(
                    diaryPage.isAddDreamEnabled(),
                    "Add Dream should be enabled below 10 dreams"
            );

            AddDreamPage addDreamPage =
                    diaryPage.clickAddDream();

            addDreamPage.enterDreamName(
                    "Test Automation Dream"
            );

            Assert.assertEquals(
                    addDreamPage.getDreamName(),
                    "Test Automation Dream",
                    "Dream name was not entered correctly"
            );

            addDreamPage.clickBackToDiary();

        } else {

            Assert.assertEquals(
                    dreamCount,
                    10,
                    "Dream count is greater than 10"
            );

            Assert.assertFalse(
                    diaryPage.isAddDreamEnabled(),
                    "Add Dream should be disabled at 10 dreams"
            );
        }
    }

    @Test
    public void verifyDaysAgoDropdown() {

        HomePage homePage =
                new HomePage(driver);

        DreamsDiaryPage diaryPage =
                homePage.clickMyDreams();

        int dreamCount =
                diaryPage.getEditButtonCount();

        if (dreamCount < 10) {

            Assert.assertTrue(
                    diaryPage.isAddDreamEnabled(),
                    "Add Dream should be enabled below 10 dreams"
            );

            AddDreamPage addDreamPage =
                    diaryPage.clickAddDream();

            addDreamPage.enterDaysAgo("3");

            Assert.assertEquals(
                    addDreamPage.getSelectedDaysAgo(),
                    "3 days ago",
                    "Days Ago value was not selected correctly"
            );

            addDreamPage.clickBackToDiary();

        } else {

            Assert.assertEquals(
                    dreamCount,
                    10,
                    "Dream count is greater than 10"
            );

            Assert.assertFalse(
                    diaryPage.isAddDreamEnabled(),
                    "Add Dream should be disabled at 10 dreams"
            );
        }
    }

    @Test
    public void verifyGoodDreamSelection() {

        HomePage homePage =
                new HomePage(driver);

        DreamsDiaryPage diaryPage =
                homePage.clickMyDreams();

        int dreamCount =
                diaryPage.getEditButtonCount();

        if (dreamCount < 10) {

            Assert.assertTrue(
                    diaryPage.isAddDreamEnabled(),
                    "Add Dream should be enabled below 10 dreams"
            );

            AddDreamPage addDreamPage =
                    diaryPage.clickAddDream();

            addDreamPage.selectGoodDream();

            Assert.assertTrue(
                    addDreamPage.isGoodDreamSelected(),
                    "Good dream was not selected"
            );

            addDreamPage.clickBackToDiary();

        } else {

            Assert.assertEquals(
                    dreamCount,
                    10,
                    "Dream count is greater than 10"
            );

            Assert.assertFalse(
                    diaryPage.isAddDreamEnabled(),
                    "Add Dream should be disabled at 10 dreams"
            );
        }
    }

    @Test
    public void verifyBadDreamSelection() {

        HomePage homePage =
                new HomePage(driver);

        DreamsDiaryPage diaryPage =
                homePage.clickMyDreams();

        int dreamCount =
                diaryPage.getEditButtonCount();

        if (dreamCount < 10) {

            Assert.assertTrue(
                    diaryPage.isAddDreamEnabled(),
                    "Add Dream should be enabled below 10 dreams"
            );

            AddDreamPage addDreamPage =
                    diaryPage.clickAddDream();

            addDreamPage.selectBadDream();

            Assert.assertTrue(
                    addDreamPage.isBadDreamSelected(),
                    "Bad dream was not selected"
            );

            addDreamPage.clickBackToDiary();

        } else {

            Assert.assertEquals(
                    dreamCount,
                    10,
                    "Dream count is greater than 10"
            );

            Assert.assertFalse(
                    diaryPage.isAddDreamEnabled(),
                    "Add Dream should be disabled at 10 dreams"
            );
        }
    }

    @Test
    public void verifyBackWithoutSaving() {

        HomePage homePage =
                new HomePage(driver);

        DreamsDiaryPage diaryPage =
                homePage.clickMyDreams();

        int beforeCount =
                diaryPage.getEditButtonCount();

        if (beforeCount < 10) {

            Assert.assertTrue(
                    diaryPage.isAddDreamEnabled(),
                    "Add Dream should be enabled below 10 dreams"
            );

            AddDreamPage addDreamPage =
                    diaryPage.clickAddDream();

            addDreamPage.enterDreamName(
                    "Temporary Dream"
            );

            DreamsDiaryPage returnedDiary =
                    addDreamPage.clickBackToDiary();

            int afterCount =
                    returnedDiary.getEditButtonCount();

            Assert.assertEquals(
                    afterCount,
                    beforeCount,
                    "Dream count changed even though the dream was not saved"
            );

        } else {

            Assert.assertEquals(
                    beforeCount,
                    10,
                    "Dream count is greater than 10"
            );

            Assert.assertFalse(
                    diaryPage.isAddDreamEnabled(),
                    "Add Dream should be disabled at 10 dreams"
            );
        }
    }

    @Test
    public void verifyTenDreamLimit() {

        HomePage homePage =
                new HomePage(driver);

        DreamsDiaryPage diaryPage =
                homePage.clickMyDreams();

        int dreamCount =
                diaryPage.getEditButtonCount();

        Assert.assertTrue(
                dreamCount <= 10,
                "Dream count must never exceed 10"
        );

        if (dreamCount < 10) {

            Assert.assertTrue(
                    diaryPage.isAddDreamEnabled(),
                    "Add Dream must be enabled when fewer than 10 dreams exist"
            );

        } else {

            Assert.assertEquals(
                    dreamCount,
                    10,
                    "Dream count is greater than 10"
            );

            Assert.assertFalse(
                    diaryPage.isAddDreamEnabled(),
                    "Add Dream must be disabled when 10 dreams exist"
            );
        }
    }
}