package com.example.dreamsportal.tests;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.Test;

public class DreamsApiTest {

    private final String BASE_URL =
            "http://localhost:3000";

    @Test
    public void verifyGetDreamsApiStatusCode() {

        Response response =
                RestAssured
                        .given()
                        .when()
                        .get(BASE_URL + "/api/dreams");

        Assert.assertEquals(
                response.getStatusCode(),
                200,
                "GET /api/dreams did not return 200"
        );
    }

    @Test
    public void verifyGetDreamsApiReturnsData() {

        Response response =
                RestAssured
                        .given()
                        .when()
                        .get(BASE_URL + "/api/dreams");

        Assert.assertNotNull(
                response.getBody(),
                "API response body is null"
        );

        Assert.assertFalse(
                response.getBody().asString().isEmpty(),
                "API response body is empty"
        );

        Assert.assertTrue(
                response.jsonPath().getBoolean("success"),
                "API success field is false"
        );
    }

    @Test
    public void verifyDreamsApiResponseIsJson() {

        Response response =
                RestAssured
                        .given()
                        .when()
                        .get(BASE_URL + "/api/dreams");

        String contentType =
                response.getContentType();

        Assert.assertTrue(
                contentType.contains("application/json"),
                "Response is not JSON"
        );
    }

    @Test
    public void verifyDreamsApiResponseContainsExpectedFields() {

        Response response =
                RestAssured
                        .given()
                        .when()
                        .get(BASE_URL + "/api/dreams");

        Assert.assertTrue(
                response.jsonPath().getBoolean("success"),
                "API success field is false"
        );

        int dreamCount =
                response.jsonPath()
                        .getList("data")
                        .size();

        if (dreamCount > 0) {

            Assert.assertNotNull(
                    response.jsonPath().getString("data[0].id"),
                    "id field is missing"
            );

            Assert.assertNotNull(
                    response.jsonPath().getString("data[0].name"),
                    "name field is missing"
            );

            Assert.assertNotNull(
                    response.jsonPath().getString("data[0].days_ago"),
                    "days_ago field is missing"
            );

            Assert.assertNotNull(
                    response.jsonPath().getString("data[0].type"),
                    "type field is missing"
            );

            Assert.assertNotNull(
                    response.jsonPath().getString("data[0].created_at"),
                    "created_at field is missing"
            );
        }
    }

    @Test
    public void verifyGetSingleDreamApi() {

        Response allDreamsResponse =
                RestAssured
                        .given()
                        .when()
                        .get(BASE_URL + "/api/dreams");

        Assert.assertEquals(
                allDreamsResponse.getStatusCode(),
                200,
                "GET /api/dreams did not return 200"
        );

        int dreamCount =
                allDreamsResponse
                        .jsonPath()
                        .getList("data")
                        .size();

        if (dreamCount > 0) {

            int dreamId =
                    allDreamsResponse
                            .jsonPath()
                            .getInt("data[0].id");

            Response singleDreamResponse =
                    RestAssured
                            .given()
                            .when()
                            .get(
                                    BASE_URL +
                                    "/api/dreams/" +
                                    dreamId
                            );

            Assert.assertEquals(
                    singleDreamResponse.getStatusCode(),
                    200,
                    "GET single dream did not return 200"
            );

            Assert.assertTrue(
                    singleDreamResponse
                            .jsonPath()
                            .getBoolean("success"),
                    "Single dream API success field is false"
            );
        }
    }
}