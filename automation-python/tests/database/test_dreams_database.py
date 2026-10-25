
from utils.database.db_connection import get_db_connection


def test_verify_database_connection():
    connection = get_db_connection()

    try:
        assert connection is not None, "Database connection is null"
        assert connection.is_connected(), "Database connection is closed"
    finally:
        if connection is not None and connection.is_connected():
            connection.close()



def test_verify_dreams_table_exists():
    query = "SELECT 1 FROM dreams LIMIT 1"

    connection = get_db_connection()

    try:
        cursor = connection.cursor()
        cursor.execute(query)

        # Fetch any returned row so the result is consumed.
        cursor.fetchone()

        assert cursor is not None, "Could not query dreams table"

        cursor.close()
    finally:
        connection.close()



def test_verify_dreams_table_has_records():
    query = "SELECT COUNT(*) FROM dreams"

    connection = get_db_connection()

    try:
        cursor = connection.cursor()
        cursor.execute(query)

        result = cursor.fetchone()

        assert result is not None, "Could not retrieve dream count"

        dream_count = result[0]

        assert dream_count >= 0, "Dream count cannot be negative"

        print(f"Dream records in database: {dream_count}")

        cursor.close()
    finally:
        connection.close()



def test_verify_dreams_table_columns():
    query = "SELECT * FROM dreams LIMIT 1"

    connection = get_db_connection()

    try:
        cursor = connection.cursor()
        cursor.execute(query)

        column_count = len(cursor.description)

        # Consume the query result before closing the cursor.
        cursor.fetchone()

        assert column_count > 0, "Dreams table has no columns"

        print(f"Dreams table column count: {column_count}")

        cursor.close()
    finally:
        connection.close()



def test_verify_dream_records_have_valid_values():
    query = """
        SELECT id, name, days_ago, type, created_at
        FROM dreams
    """

    connection = get_db_connection()

    try:
        cursor = connection.cursor(dictionary=True)
        cursor.execute(query)

        for dream in cursor.fetchall():
            dream_id = dream["id"]
            name = dream["name"]
            days_ago = dream["days_ago"]
            dream_type = dream["type"]
            created_at = dream["created_at"]

            assert dream_id > 0, "Dream ID must be greater than 0"

            assert name is not None, "Dream name must not be null"
            assert name.strip() != "", "Dream name must not be empty"

            assert 1 <= days_ago <= 7, (
                "Days Ago must be between 1 and 7"
            )

            assert dream_type in ("Good", "Bad"), (
                "Dream type must be Good or Bad"
            )

            assert created_at is not None, (
                "created_at must not be null"
            )

        cursor.close()
    finally:
        connection.close()


def test_verify_dream_count_does_not_exceed_maximum():
    query = "SELECT COUNT(*) FROM dreams"

    connection = get_db_connection()

    try:
        cursor = connection.cursor()
        cursor.execute(query)

        result = cursor.fetchone()

        assert result is not None, "Could not retrieve dream count"

        dream_count = result[0]

        print(f"Current dream count: {dream_count}")

        assert dream_count <= 10, (
            "Dream count must not exceed 10"
        )

        cursor.close()
    finally:
        connection.close()


def test_verify_dream_ids_are_unique():
    query = """
        SELECT id, COUNT(*)
        FROM dreams
        GROUP BY id
        HAVING COUNT(*) > 1
    """

    connection = get_db_connection()

    try:
        cursor = connection.cursor()
        cursor.execute(query)

        result = cursor.fetchone()

        assert result is None, (
            "Duplicate dream IDs found in database"
        )

        cursor.close()
    finally:
        connection.close()
