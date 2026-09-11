package com.example.dreamsportal.tests;

import com.example.dreamsportal.base.DatabaseConnection;

import org.testng.Assert;
import org.testng.annotations.Test;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

public class DreamsDatabaseTest {

    @Test
    public void verifyDatabaseConnection() throws SQLException {

        try (Connection connection =
                     DatabaseConnection.getConnection()) {

            Assert.assertNotNull(
                    connection,
                    "Database connection is null"
            );

            Assert.assertFalse(
                    connection.isClosed(),
                    "Database connection is closed"
            );
        }
    }

    @Test
    public void verifyDreamsTableExists() throws SQLException {

        String query =
                "SELECT 1 FROM dreams LIMIT 1";

        try (Connection connection =
                     DatabaseConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(query);
             ResultSet resultSet =
                     statement.executeQuery()) {

            Assert.assertNotNull(
                    resultSet,
                    "Could not query dreams table"
            );
        }
    }

    @Test
    public void verifyDreamsTableHasRecords() throws SQLException {

        String query =
                "SELECT COUNT(*) FROM dreams";

        try (Connection connection =
                     DatabaseConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(query);
             ResultSet resultSet =
                     statement.executeQuery()) {

            Assert.assertTrue(
                    resultSet.next(),
                    "Could not retrieve dream count"
            );

            int dreamCount =
                    resultSet.getInt(1);

            Assert.assertTrue(
                    dreamCount >= 0,
                    "Dream count cannot be negative"
            );

            System.out.println(
                    "Dream records in database: " +
                    dreamCount
            );
        }
    }

    @Test
    public void verifyDreamsTableColumns() throws SQLException {

        String query =
                "SELECT * FROM dreams LIMIT 1";

        try (Connection connection =
                     DatabaseConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(query);
             ResultSet resultSet =
                     statement.executeQuery()) {

            ResultSetMetaData metadata =
                    resultSet.getMetaData();

            int columnCount =
                    metadata.getColumnCount();

            Assert.assertTrue(
                    columnCount > 0,
                    "Dreams table has no columns"
            );

            System.out.println(
                    "Dreams table column count: " +
                    columnCount
            );
        }
    }

    @Test
    public void verifyDreamRecordsHaveValidValues() throws SQLException {

        String query =
                "SELECT id, name, days_ago, type, created_at " +
                "FROM dreams";

        try (Connection connection =
                     DatabaseConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(query);
             ResultSet resultSet =
                     statement.executeQuery()) {

            while (resultSet.next()) {

                int id =
                        resultSet.getInt("id");

                String name =
                        resultSet.getString("name");

                int daysAgo =
                        resultSet.getInt("days_ago");

                String type =
                        resultSet.getString("type");

                Object createdAt =
                        resultSet.getObject("created_at");

                Assert.assertTrue(
                        id > 0,
                        "Dream ID must be greater than 0"
                );

                Assert.assertNotNull(
                        name,
                        "Dream name must not be null"
                );

                Assert.assertFalse(
                        name.trim().isEmpty(),
                        "Dream name must not be empty"
                );

                Assert.assertTrue(
                        daysAgo >= 1 && daysAgo <= 7,
                        "Days Ago must be between 1 and 7"
                );

                Assert.assertTrue(
                        type.equals("Good") ||
                        type.equals("Bad"),
                        "Dream type must be Good or Bad"
                );

                Assert.assertNotNull(
                        createdAt,
                        "created_at must not be null"
                );
            }
        }
    }

    @Test
    public void verifyDreamCountDoesNotExceedMaximum() throws SQLException {

        String query =
                "SELECT COUNT(*) FROM dreams";

        try (Connection connection =
                     DatabaseConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(query);
             ResultSet resultSet =
                     statement.executeQuery()) {

            Assert.assertTrue(
                    resultSet.next(),
                    "Could not retrieve dream count"
            );

            int dreamCount =
                    resultSet.getInt(1);

            System.out.println(
                    "Current dream count: " +
                    dreamCount
            );

            Assert.assertTrue(
                    dreamCount <= 10,
                    "Dream count must not exceed 10"
            );
        }
    }

    @Test
    public void verifyDreamIdsAreUnique() throws SQLException {

        String query =
                "SELECT id, COUNT(*) " +
                "FROM dreams " +
                "GROUP BY id " +
                "HAVING COUNT(*) > 1";

        try (Connection connection =
                     DatabaseConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(query);
             ResultSet resultSet =
                     statement.executeQuery()) {

            Assert.assertFalse(
                    resultSet.next(),
                    "Duplicate dream IDs found in database"
            );
        }
    }
}