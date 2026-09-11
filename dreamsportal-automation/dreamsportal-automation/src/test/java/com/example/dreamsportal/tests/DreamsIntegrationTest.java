package com.example.dreamsportal.tests;

import com.example.dreamsportal.base.BaseTest;
import com.example.dreamsportal.base.DatabaseConnection;
import com.example.dreamsportal.pages.AddDreamPage;
import com.example.dreamsportal.pages.DreamsDiaryPage;
import com.example.dreamsportal.pages.DreamsTotalPage;
import com.example.dreamsportal.pages.HomePage;

import io.restassured.RestAssured;
import io.restassured.response.Response;

import org.openqa.selenium.By;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DreamsIntegrationTest extends BaseTest {

    private final String API_URL =
            "http://localhost:3000";

    /*
     * ---------------------------------------------------------
     * Helper methods
     * ---------------------------------------------------------
     */

    private int getDatabaseDreamCount() throws SQLException {

        String query =
                "SELECT COUNT(*) FROM dreams";

        try (Connection connection =
                     DatabaseConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(query);
             ResultSet resultSet =
                     statement.executeQuery()) {

            resultSet.next();

            return resultSet.getInt(1);
        }
    }

    private int getDatabaseDaysCount() throws SQLException {

        String query =
                "SELECT COUNT(DISTINCT days_ago) FROM dreams";

        try (Connection connection =
                     DatabaseConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(query);
             ResultSet resultSet =
                     statement.executeQuery()) {

            resultSet.next();

            return resultSet.getInt(1);
        }
    }

    private int getApiDreamCount() {

        Response response =
                RestAssured
                        .given()
                        .when()
                        .get(API_URL + "/api/dreams");

        Assert.assertEquals(
                response.getStatusCode(),
                200
        );

        return response
                .jsonPath()
                .getList("data")
                .size();
    }

    private int getApiGoodDreamCount() {

        Response response =
                RestAssured
                        .given()
                        .when()
                        .get(API_URL + "/api/summary");

        Assert.assertEquals(
                response.getStatusCode(),
                200
        );

        return response
                .jsonPath()
                .getInt("data.good");
    }

    private int getApiBadDreamCount() {

        Response response =
                RestAssured
                        .given()
                        .when()
                        .get(API_URL + "/api/summary");

        Assert.assertEquals(
                response.getStatusCode(),
                200
        );

        return response
                .jsonPath()
                .getInt("data.bad");
    }

    private int getApiTotalDreamCount() {

        Response response =
                RestAssured
                        .given()
                        .when()
                        .get(API_URL + "/api/summary");

        Assert.assertEquals(
                response.getStatusCode(),
                200
        );

        return response
                .jsonPath()
                .getInt("data.total");
    }

    private int getApiTotalDaysCount() {

        Response response =
                RestAssured
                        .given()
                        .when()
                        .get(API_URL + "/api/summary");

        Assert.assertEquals(
                response.getStatusCode(),
                200
        );

        return response
                .jsonPath()
                .getInt("data.days");
    }

    private int getDreamIdByName(String dreamName)
            throws SQLException {

        String query =
                "SELECT id FROM dreams WHERE name = ? LIMIT 1";

        try (Connection connection =
                     DatabaseConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(query)) {

            statement.setString(1, dreamName);

            try (ResultSet resultSet =
                         statement.executeQuery()) {

                if (resultSet.next()) {
                    return resultSet.getInt("id");
                }
            }
        }

        return -1;
    }

    private void deleteDreamFromDatabase(String dreamName)
            throws SQLException {

        String query =
                "DELETE FROM dreams WHERE name = ?";

        try (Connection connection =
                     DatabaseConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(query)) {

            statement.setString(1, dreamName);

            statement.executeUpdate();
        }
    }

    private String getFirstDreamName() {

        Response response =
                RestAssured
                        .given()
                        .when()
                        .get(API_URL + "/api/dreams");

        Assert.assertEquals(
                response.getStatusCode(),
                200
        );

        int count =
                response
                        .jsonPath()
                        .getList("data")
                        .size();

        Assert.assertTrue(
                count > 0,
                "At least one dream is required"
        );

        return response
                .jsonPath()
                .getString("data[0].name");
    }

    private int getFirstDreamId() {

        Response response =
                RestAssured
                        .given()
                        .when()
                        .get(API_URL + "/api/dreams");

        Assert.assertEquals(
                response.getStatusCode(),
                200
        );

        return response
                .jsonPath()
                .getInt("data[0].id");
    }


    /*
     * =========================================================
     * TC-03
     * Home Dreams count matches MySQL
     * =========================================================
     */

    @Test
    public void TC03_homeDreamCountMatchesDatabase()
            throws SQLException {

        int databaseCount =
                getDatabaseDreamCount();

        HomePage homePage =
                new HomePage(driver);

        int homeCount =
                Integer.parseInt(
                        homePage.getDreamsCount()
                );

        Assert.assertEquals(
                homeCount,
                databaseCount,
                "Home Dreams count does not match MySQL"
        );
    }


    /*
     * =========================================================
     * TC-04
     * Home Days count matches MySQL
     * =========================================================
     */

    @Test
    public void TC04_homeDaysCountMatchesDatabase()
            throws SQLException {

        int databaseDays =
                getDatabaseDaysCount();

        HomePage homePage =
                new HomePage(driver);

        int homeDays =
                Integer.parseInt(
                        homePage.getDaysCount()
                );

        Assert.assertEquals(
                homeDays,
                databaseDays,
                "Home Days count does not match MySQL"
        );
    }


    /*
     * =========================================================
     * TC-05
     * Home Dreams count matches API
     * =========================================================
     */

    @Test
    public void TC05_homeDreamCountMatchesApi() {

        int apiCount =
                getApiDreamCount();

        HomePage homePage =
                new HomePage(driver);

        int homeCount =
                Integer.parseInt(
                        homePage.getDreamsCount()
                );

        Assert.assertEquals(
                homeCount,
                apiCount,
                "Home Dreams count does not match API"
        );
    }


    /*
     * =========================================================
     * TC-09
     * Home statistics are consistent with backend data
     *
     * Current Home UI does not display individual dreams.
     * Therefore this verifies both Home statistics against
     * the backend API.
     * =========================================================
     */

    @Test
    public void TC09_homeStatisticsMatchBackend() {

        HomePage homePage =
                new HomePage(driver);

        int homeDreamCount =
                Integer.parseInt(
                        homePage.getDreamsCount()
                );

        int homeDaysCount =
                Integer.parseInt(
                        homePage.getDaysCount()
                );

        int apiDreamCount =
                getApiDreamCount();

        int apiDaysCount =
                getApiTotalDaysCount();

        Assert.assertEquals(
                homeDreamCount,
                apiDreamCount,
                "Home Dreams count does not match backend"
        );

        Assert.assertEquals(
                homeDaysCount,
                apiDaysCount,
                "Home Days count does not match backend"
        );
    }


    /*
     * =========================================================
     * TC-17
     * Diary records match GET API
     * =========================================================
     */

    @Test
    public void TC17_diaryRecordsMatchApi() {

        HomePage homePage =
                new HomePage(driver);

        DreamsDiaryPage diaryPage =
                homePage.clickMyDreams();

        int diaryRecordCount =
                diaryPage.getEditButtonCount();

        int apiRecordCount =
                getApiDreamCount();

        Assert.assertEquals(
                diaryRecordCount,
                apiRecordCount,
                "Diary record count does not match API"
        );
    }


    /*
     * =========================================================
     * TC-18
     * Diary records match MySQL
     * =========================================================
     */

    @Test
    public void TC18_diaryRecordsMatchDatabase()
            throws SQLException {

        HomePage homePage =
                new HomePage(driver);

        DreamsDiaryPage diaryPage =
                homePage.clickMyDreams();

        int diaryRecordCount =
                diaryPage.getEditButtonCount();

        int databaseCount =
                getDatabaseDreamCount();

        Assert.assertEquals(
                diaryRecordCount,
                databaseCount,
                "Diary record count does not match MySQL"
        );
    }


    /*
     * =========================================================
     * TC-22
     * Diary count matches MySQL
     * =========================================================
     */

    @Test
    public void TC22_diaryCountMatchesDatabase()
            throws SQLException {

        HomePage homePage =
                new HomePage(driver);

        DreamsDiaryPage diaryPage =
                homePage.clickMyDreams();

        int diaryCount =
                diaryPage.getEditButtonCount();

        int databaseCount =
                getDatabaseDreamCount();

        Assert.assertEquals(
                diaryCount,
                databaseCount,
                "Diary count does not match database count"
        );
    }


    /*
     * =========================================================
     * TC-24
     * API count matches DB
     * =========================================================
     */

    @Test
    public void TC24_apiCountMatchesDatabase()
            throws SQLException {

        int apiCount =
                getApiDreamCount();

        int databaseCount =
                getDatabaseDreamCount();

        Assert.assertEquals(
                apiCount,
                databaseCount,
                "API dream count does not match MySQL"
        );
    }


    /*
     * =========================================================
     * TC-31
     * Updated dream displayed in Diary
     *
     * Uses existing dream and restores original value.
     * =========================================================
     */

    @Test
    public void TC31_updatedDreamDisplayedInDiary()
            throws SQLException {

        String originalName =
                getFirstDreamName();

        int dreamId =
                getFirstDreamId();

        String updatedName =
                "Integration Temporary Dream";

        try {

            Response response =
                    RestAssured
                            .given()
                            .contentType("application/json")
                            .body(
                                    "{"
                                    + "\"name\":\""
                                    + updatedName
                                    + "\","
                                    + "\"daysAgo\":1,"
                                    + "\"type\":\"Good\""
                                    + "}"
                            )
                            .when()
                            .put(
                                    API_URL +
                                    "/api/dreams/" +
                                    dreamId
                            );

            Assert.assertEquals(
                    response.getStatusCode(),
                    200,
                    "PUT request failed"
            );

            driver.navigate().refresh();

            HomePage homePage =
                    new HomePage(driver);

            DreamsDiaryPage diaryPage =
                    homePage.clickMyDreams();

            Assert.assertTrue(
                    diaryPage.isDreamDisplayed(updatedName),
                    "Updated dream is not displayed"
            );

        } finally {

            RestAssured
                    .given()
                    .contentType("application/json")
                    .body(
                            "{"
                            + "\"name\":\""
                            + originalName
                            + "\","
                            + "\"daysAgo\":1,"
                            + "\"type\":\"Good\""
                            + "}"
                    )
                    .when()
                    .put(
                            API_URL +
                            "/api/dreams/" +
                            dreamId
                    );
        }
    }


    /*
     * =========================================================
     * TC-51
     * Newly added dream appears in Diary
     *
     * Temporary API-created dream is deleted afterward.
     * =========================================================
     */

    @Test
    public void TC51_newDreamAppearsInDiary()
            throws SQLException {

        String dreamName =
                "Integration Temporary Add Dream";

        int beforeCount =
                getDatabaseDreamCount();

        try {

            Response response =
                    RestAssured
                            .given()
                            .contentType("application/json")
                            .body(
                                    "{"
                                    + "\"name\":\""
                                    + dreamName
                                    + "\","
                                    + "\"daysAgo\":1,"
                                    + "\"type\":\"Good\""
                                    + "}"
                            )
                            .when()
                            .post(API_URL + "/api/dreams");

            Assert.assertTrue(
                    response.getStatusCode() == 200 ||
                    response.getStatusCode() == 201,
                    "POST dream failed"
            );

            driver.navigate().refresh();

            HomePage homePage =
                    new HomePage(driver);

            DreamsDiaryPage diaryPage =
                    homePage.clickMyDreams();

            Assert.assertTrue(
                    diaryPage.isDreamDisplayed(dreamName),
                    "New dream is not displayed in Diary"
            );

        } finally {

            deleteDreamFromDatabase(dreamName);
        }

        int afterCount =
                getDatabaseDreamCount();

        Assert.assertEquals(
                afterCount,
                beforeCount,
                "Database count was not restored"
        );
    }


    /*
     * =========================================================
     * TC-52
     * Dream count increases after adding
     * =========================================================
     */

    @Test
    public void TC52_dreamCountIncreasesAfterAdding()
            throws SQLException {

        String dreamName =
                "Integration Count Dream";

        int beforeCount =
                getDatabaseDreamCount();

        try {

            Response response =
                    RestAssured
                            .given()
                            .contentType("application/json")
                            .body(
                                    "{"
                                    + "\"name\":\""
                                    + dreamName
                                    + "\","
                                    + "\"daysAgo\":1,"
                                    + "\"type\":\"Good\""
                                    + "}"
                            )
                            .when()
                            .post(API_URL + "/api/dreams");

            Assert.assertTrue(
                    response.getStatusCode() == 200 ||
                    response.getStatusCode() == 201
            );

            int afterCount =
                    getDatabaseDreamCount();

            Assert.assertEquals(
                    afterCount,
                    beforeCount + 1,
                    "Dream count did not increase by one"
            );

        } finally {

            deleteDreamFromDatabase(dreamName);
        }
    }


    /*
     * =========================================================
     * TC-53
     * Home count updates after adding
     * =========================================================
     */

    @Test
    public void TC53_homeCountUpdatesAfterAdding()
            throws SQLException {

        String dreamName =
                "Integration Home Count Dream";

        int beforeCount =
                getDatabaseDreamCount();

        try {

            RestAssured
                    .given()
                    .contentType("application/json")
                    .body(
                            "{"
                            + "\"name\":\""
                            + dreamName
                            + "\","
                            + "\"daysAgo\":1,"
                            + "\"type\":\"Good\""
                            + "}"
                    )
                    .when()
                    .post(API_URL + "/api/dreams");

            driver.navigate().refresh();

            HomePage homePage =
                    new HomePage(driver);

            int homeCount =
                    Integer.parseInt(
                            homePage.getDreamsCount()
                    );

            Assert.assertEquals(
                    homeCount,
                    beforeCount + 1,
                    "Home count did not update"
            );

        } finally {

            deleteDreamFromDatabase(dreamName);
        }
    }


    /*
     * =========================================================
     * TC-54
     * Summary count updates after adding
     * =========================================================
     */

    @Test
    public void TC54_summaryCountUpdatesAfterAdding()
            throws SQLException {

        String dreamName =
                "Integration Summary Add Dream";

        int beforeCount =
                getDatabaseDreamCount();

        try {

            RestAssured
                    .given()
                    .contentType("application/json")
                    .body(
                            "{"
                            + "\"name\":\""
                            + dreamName
                            + "\","
                            + "\"daysAgo\":1,"
                            + "\"type\":\"Good\""
                            + "}"
                    )
                    .when()
                    .post(API_URL + "/api/dreams");

            int summaryCount =
                    getApiTotalDreamCount();

            Assert.assertEquals(
                    summaryCount,
                    beforeCount + 1,
                    "Summary count did not update"
            );

        } finally {

            deleteDreamFromDatabase(dreamName);
        }
    }


    /*
     * =========================================================
     * TC-56
     * One OK click does not create duplicate
     *
     * =========================================================
     */

    @Test
    public void TC56_oneOkClickDoesNotCreateDuplicate()
            throws SQLException {

        String dreamName =
                "Integration Duplicate Check";

        int beforeCount =
                getDatabaseDreamCount();

        try {

            HomePage homePage =
                    new HomePage(driver);

            DreamsDiaryPage diaryPage =
                    homePage.clickMyDreams();

            AddDreamPage addDreamPage =
                    diaryPage.clickAddDream();

            addDreamPage.enterDreamName(dreamName);
            addDreamPage.enterDaysAgo("1");
            addDreamPage.selectGoodDream();

            addDreamPage.clickSaveDream();

            int afterCount =
                    getDatabaseDreamCount();

            Assert.assertEquals(
                    afterCount,
                    beforeCount + 1,
                    "One save created more than one record"
            );

        } finally {

            deleteDreamFromDatabase(dreamName);
        }
    }


    /*
     * =========================================================
     * TC-57
     * Maximum 10-dream rule
     * =========================================================
     */

    @Test
    public void TC57_maximumTenDreamRule()
            throws SQLException {

        int count =
                getDatabaseDreamCount();

        Assert.assertTrue(
                count <= 10,
                "Database contains more than 10 dreams"
        );

        HomePage homePage =
                new HomePage(driver);

        DreamsDiaryPage diaryPage =
                homePage.clickMyDreams();

        if (count < 10) {

            Assert.assertTrue(
                    diaryPage.isAddDreamEnabled(),
                    "Add Dream should be enabled below 10"
            );

        } else {

            Assert.assertFalse(
                    diaryPage.isAddDreamEnabled(),
                    "Add Dream should be disabled at 10"
            );
        }
    }


    /*
     * =========================================================
     * TC-60
     * Diary matches DB after adding
     * =========================================================
     */

    @Test
    public void TC60_diaryMatchesDatabaseAfterAdding()
            throws SQLException {

        String dreamName =
                "Integration Diary DB Dream";

        try {

            RestAssured
                    .given()
                    .contentType("application/json")
                    .body(
                            "{"
                            + "\"name\":\""
                            + dreamName
                            + "\","
                            + "\"daysAgo\":1,"
                            + "\"type\":\"Good\""
                            + "}"
                    )
                    .when()
                    .post(API_URL + "/api/dreams");

            driver.navigate().refresh();

            HomePage homePage =
                    new HomePage(driver);

            DreamsDiaryPage diaryPage =
                    homePage.clickMyDreams();

            Assert.assertTrue(
                    diaryPage.isDreamDisplayed(dreamName),
                    "Added dream is not in Diary"
            );

            Assert.assertEquals(
                    diaryPage.getEditButtonCount(),
                    getDatabaseDreamCount(),
                    "Diary count does not match DB"
            );

        } finally {

            deleteDreamFromDatabase(dreamName);
        }
    }


    /*
     * =========================================================
     * TC-69
     * Summary page receives data from backend
     * =========================================================
     */

    @Test
    public void TC69_summaryPageReceivesBackendData() {

        HomePage homePage =
                new HomePage(driver);

        DreamsTotalPage summaryPage =
                homePage.clickViewSummary();

        Assert.assertTrue(
                summaryPage.isSummaryPageDisplayed(),
                "Summary page is not displayed"
        );

        Assert.assertNotNull(
                summaryPage.getTotalDreamsCount(),
                "Summary total dreams is null"
        );

        Assert.assertNotNull(
                summaryPage.getTotalDaysCount(),
                "Summary total days is null"
        );
    }


    /*
     * =========================================================
     * TC-79
     * UI summary counts match API
     * =========================================================
     */

   @Test
public void TC79_summaryUiCountsMatchApi() {

    int expectedGood = getApiGoodDreamCount();
    int expectedBad = getApiBadDreamCount();
    int expectedTotal = getApiTotalDreamCount();
    int expectedDays = getApiTotalDaysCount();

    HomePage homePage =
            new HomePage(driver);

    DreamsTotalPage summaryPage =
            homePage.clickViewSummary();

    java.time.Duration timeout =
            java.time.Duration.ofSeconds(10);

    org.openqa.selenium.support.ui.WebDriverWait wait =
            new org.openqa.selenium.support.ui.WebDriverWait(
                    driver,
                    timeout
            );

    wait.until(driver -> {

        int uiGood =
                Integer.parseInt(
                        summaryPage.getGoodDreamsCount()
                );

        int uiBad =
                Integer.parseInt(
                        summaryPage.getBadDreamsCount()
                );

        int uiTotal =
                Integer.parseInt(
                        summaryPage.getTotalDreamsCount()
                );

        int uiDays =
                Integer.parseInt(
                        summaryPage.getTotalDaysCount()
                );

        return uiGood == expectedGood
                && uiBad == expectedBad
                && uiTotal == expectedTotal
                && uiDays == expectedDays;
    });

    Assert.assertEquals(
            Integer.parseInt(summaryPage.getGoodDreamsCount()),
            expectedGood,
            "UI Good Dreams count does not match API"
    );

    Assert.assertEquals(
            Integer.parseInt(summaryPage.getBadDreamsCount()),
            expectedBad,
            "UI Bad Dreams count does not match API"
    );

    Assert.assertEquals(
            Integer.parseInt(summaryPage.getTotalDreamsCount()),
            expectedTotal,
            "UI Total Dreams count does not match API"
    );

    Assert.assertEquals(
            Integer.parseInt(summaryPage.getTotalDaysCount()),
            expectedDays,
            "UI Total Days count does not match API"
    );
}


    /*
     * =========================================================
     * TC-80
     * API summary counts match MySQL
     * =========================================================
     */

    @Test
    public void TC80_apiSummaryCountsMatchDatabase()
            throws SQLException {

        int databaseTotal =
                getDatabaseDreamCount();

        int databaseDays =
                getDatabaseDaysCount();

        int apiTotal =
                getApiTotalDreamCount();

        int apiDays =
                getApiTotalDaysCount();

        Assert.assertEquals(
                apiTotal,
                databaseTotal,
                "API total dreams does not match DB"
        );

        Assert.assertEquals(
                apiDays,
                databaseDays,
                "API total days does not match DB"
        );
    }


    /*
     * =========================================================
     * TC-81
     * Summary updates after edit
     *
     * Changes Good -> Bad and restores original state.
     * =========================================================
     */

    @Test
    public void TC81_summaryUpdatesAfterEdit()
            throws SQLException {

        int dreamId =
                getFirstDreamId();

        String originalName =
                getFirstDreamName();

        int goodBefore =
                getApiGoodDreamCount();

        int badBefore =
                getApiBadDreamCount();

        try {

            RestAssured
                    .given()
                    .contentType("application/json")
                    .body(
                            "{"
                            + "\"name\":\""
                            + originalName
                            + "\","
                            + "\"daysAgo\":1,"
                            + "\"type\":\"Bad\""
                            + "}"
                    )
                    .when()
                    .put(
                            API_URL +
                            "/api/dreams/" +
                            dreamId
                    );

            int goodAfter =
                    getApiGoodDreamCount();

            int badAfter =
                    getApiBadDreamCount();

            Assert.assertEquals(
                    goodAfter,
                    goodBefore - 1,
                    "Good Dreams count did not decrease"
            );

            Assert.assertEquals(
                    badAfter,
                    badBefore + 1,
                    "Bad Dreams count did not increase"
            );

        } finally {

            RestAssured
                    .given()
                    .contentType("application/json")
                    .body(
                            "{"
                            + "\"name\":\""
                            + originalName
                            + "\","
                            + "\"daysAgo\":1,"
                            + "\"type\":\"Good\""
                            + "}"
                    )
                    .when()
                    .put(
                            API_URL +
                            "/api/dreams/" +
                            dreamId
                    );
        }
    }


    /*
     * =========================================================
     * TC-82
     * Summary updates after removal
     *
     * Uses temporary dream and removes it afterward.
     * =========================================================
     */

    @Test
    public void TC82_summaryUpdatesAfterRemoval()
            throws SQLException {

        String dreamName =
                "Integration Remove Summary Dream";

        try {

            RestAssured
                    .given()
                    .contentType("application/json")
                    .body(
                            "{"
                            + "\"name\":\""
                            + dreamName
                            + "\","
                            + "\"daysAgo\":1,"
                            + "\"type\":\"Good\""
                            + "}"
                    )
                    .when()
                    .post(API_URL + "/api/dreams");

            int countAfterAdd =
                    getApiTotalDreamCount();

            int dreamId =
                    getDreamIdByName(dreamName);

            Assert.assertTrue(
                    dreamId > 0,
                    "Temporary dream was not created"
            );

            RestAssured
                    .given()
                    .when()
                    .delete(
                            API_URL +
                            "/api/dreams/" +
                            dreamId
                    );

            int countAfterRemove =
                    getApiTotalDreamCount();

            Assert.assertEquals(
                    countAfterRemove,
                    countAfterAdd - 1,
                    "Summary count did not decrease after removal"
            );

        } finally {

            deleteDreamFromDatabase(dreamName);
        }
    }


    /*
     * =========================================================
     * TC-84
     * Values remain correct after refresh
     * =========================================================
     */

    @Test
    public void TC84_valuesRemainCorrectAfterRefresh()
            throws SQLException {

        int databaseDreamCount =
                getDatabaseDreamCount();

        int databaseDaysCount =
                getDatabaseDaysCount();

        driver.navigate().refresh();

        HomePage homePage =
                new HomePage(driver);

        int homeDreamCount =
                Integer.parseInt(
                        homePage.getDreamsCount()
                );

        int homeDaysCount =
                Integer.parseInt(
                        homePage.getDaysCount()
                );

        Assert.assertEquals(
                homeDreamCount,
                databaseDreamCount,
                "Dream count changed incorrectly after refresh"
        );

        Assert.assertEquals(
                homeDaysCount,
                databaseDaysCount,
                "Days count changed incorrectly after refresh"
        );
    }


    /*
     * =========================================================
     * TC-87
     * UI handles API failure
     *
     * This verifies that an invalid API endpoint does not
     * produce a valid success response.
     * =========================================================
     */

    @Test
    public void TC87_apiFailureDoesNotReturnSuccessfulResponse() {

        Response response =
                RestAssured
                        .given()
                        .when()
                        .get(
                                API_URL +
                                "/api/invalid-dreams-endpoint"
                        );

        Assert.assertNotEquals(
                response.getStatusCode(),
                200,
                "Invalid API endpoint unexpectedly returned 200"
        );
    }


    /*
     * =========================================================
     * TC-88
     * UI / API / MySQL overall consistency
     * =========================================================
     */

    @Test
    public void TC88_uiApiDatabaseConsistency()
            throws SQLException {

        int databaseDreamCount =
                getDatabaseDreamCount();

        int databaseDaysCount =
                getDatabaseDaysCount();

        int apiDreamCount =
                getApiDreamCount();

        int apiDaysCount =
                getApiTotalDaysCount();

        HomePage homePage =
                new HomePage(driver);

        int homeDreamCount =
                Integer.parseInt(
                        homePage.getDreamsCount()
                );

        int homeDaysCount =
                Integer.parseInt(
                        homePage.getDaysCount()
                );

        Assert.assertEquals(
                apiDreamCount,
                databaseDreamCount,
                "API and DB dream counts do not match"
        );

        Assert.assertEquals(
                apiDaysCount,
                databaseDaysCount,
                "API and DB day counts do not match"
        );

        Assert.assertEquals(
                homeDreamCount,
                apiDreamCount,
                "Home and API dream counts do not match"
        );

        Assert.assertEquals(
                homeDaysCount,
                apiDaysCount,
                "Home and API day counts do not match"
        );
    }
}