
import os

import mysql.connector
from dotenv import load_dotenv
from mysql.connector import Error

# Load environment variables from the project-root .env file.
load_dotenv()


def get_db_connection():
    """Connect to the Dreams Portal MySQL database."""
    try:
        connection = mysql.connector.connect(
            host=os.getenv("DB_HOST", "127.0.0.1"),
            port=int(os.getenv("DB_PORT", "3306")),
            user=os.getenv("DB_USER"),
            password=os.getenv("DB_PASSWORD"),
            database=os.getenv("DB_NAME"),
            connection_timeout=5,
        )

        if connection.is_connected():
            return connection

        connection.close()
        raise RuntimeError("MySQL connection was not established.")

    except Error as error:
        raise RuntimeError(
            f"MySQL connection failed: {error}"
        ) from error
