
import pytest
import requests
from playwright.sync_api import expect

from pages.home_page import HomePage
from utils.database.db_connection import get_db_connection


API_URL = "http://localhost:3000"
MAX_DREAMS = 10
TIMEOUT = 10


# ---------------------------------------------------------
# Helper methods: MySQL
# ---------------------------------------------------------

def get_database_dream_count():
    connection = get_db_connection()
    cursor = None

    try:
        cursor = connection.cursor()
        cursor.execute("SELECT COUNT(*) FROM dreams")
        return cursor.fetchone()[0]
    finally:
        if cursor is not None:
            cursor.close()
        connection.close()


def get_database_days_count():
    connection = get_db_connection()
    cursor = None

    try:
        cursor = connection.cursor()
        cursor.execute(
            "SELECT COUNT(DISTINCT days_ago) FROM dreams"
        )
        return cursor.fetchone()[0]
    finally:
        if cursor is not None:
            cursor.close()
        connection.close()


def get_database_dream(dream_id):
    connection = get_db_connection()
    cursor = None

    try:
        cursor = connection.cursor(dictionary=True)
        cursor.execute(
            """
            SELECT id, name, days_ago, `type`
            FROM dreams
            WHERE id = %s
            """,
            (dream_id,),
        )
        return cursor.fetchone()
    finally:
        if cursor is not None:
            cursor.close()
        connection.close()


# ---------------------------------------------------------
# Helper methods: API
# ---------------------------------------------------------

def get_api_dreams():
    response = requests.get(
        f"{API_URL}/api/dreams",
        timeout=TIMEOUT,
    )
    assert response.status_code == 200
    return response.json()["data"]


def get_api_summary():
    response = requests.get(
        f"{API_URL}/api/summary",
        timeout=TIMEOUT,
    )
    assert response.status_code == 200
    return response.json()["data"]


def find_api_dream_by_id(dream_id):
    return next(
        (
            dream
            for dream in get_api_dreams()
            if int(dream["id"]) == int(dream_id)
        ),
        None,
    )


def find_api_dream_by_name(name):
    return next(
        (
            dream
            for dream in get_api_dreams()
            if dream["name"] == name
        ),
        None,
    )


def create_temporary_dream(name):
    """
    Create only when there is room under the app's 10-dream limit.
    Returns the created dream's ID.
    """
    if get_database_dream_count() >= MAX_DREAMS:
        pytest.skip(
            "Database already has 10 dreams; "
            "cannot safely create a temporary test dream."
        )

    response = requests.post(
        f"{API_URL}/api/dreams",
        json={
            "name": name,
            "daysAgo": 1,
            "type": "Good",
        },
        timeout=TIMEOUT,
    )

    assert response.status_code in (200, 201), (
        f"Creating temporary dream failed: "
        f"{response.status_code} {response.text}"
    )

    dream = find_api_dream_by_name(name)
    assert dream is not None, "Created dream was not found in API"

    return int(dream["id"])


def delete_temporary_dream(dream_id):
    """
    Delete only the temporary dream ID created by this test.
    Never delete a personal dream by name.
    """
    if dream_id is None:
        return

    existing = find_api_dream_by_id(dream_id)

    if existing is not None:
        response = requests.delete(
            f"{API_URL}/api/dreams/{dream_id}",
            timeout=TIMEOUT,
        )
        assert response.status_code in (200, 204), (
            f"Temporary dream cleanup failed: "
            f"{response.status_code} {response.text}"
        )


# ---------------------------------------------------------
# TC-03: Home dream count matches MySQL
# ---------------------------------------------------------

def test_home_dream_count_matches_database(page):
    database_count = get_database_dream_count()

    home_page = HomePage(page)
    home_count = int(home_page.get_dreams_count())

    assert home_count == database_count


# ---------------------------------------------------------
# TC-04: Home days count matches MySQL
# ---------------------------------------------------------

def test_home_days_count_matches_database(page):
    database_days = get_database_days_count()

    home_page = HomePage(page)
    home_days = int(home_page.get_days_count())

    assert home_days == database_days


# ---------------------------------------------------------
# TC-05: Home dream count matches API
# ---------------------------------------------------------

def test_home_dream_count_matches_api(page):
    api_count = len(get_api_dreams())

    home_page = HomePage(page)
    assert int(home_page.get_dreams_count()) == api_count


# ---------------------------------------------------------
# TC-09: Home statistics match backend
# ---------------------------------------------------------

def test_home_statistics_match_backend(page):
    home_page = HomePage(page)
    summary = get_api_summary()

    assert int(home_page.get_dreams_count()) == summary["total"]
    assert int(home_page.get_days_count()) == summary["days"]


# ---------------------------------------------------------
# TC-17: Diary records match API
# ---------------------------------------------------------

def test_diary_records_match_api(page):
    diary_page = HomePage(page).click_my_dreams()

    assert diary_page.get_edit_button_count() == len(get_api_dreams())


# ---------------------------------------------------------
# TC-18: Diary records match MySQL
# ---------------------------------------------------------

def test_diary_records_match_database(page):
    diary_page = HomePage(page).click_my_dreams()

    assert (
        diary_page.get_edit_button_count()
        == get_database_dream_count()
    )


# ---------------------------------------------------------
# TC-24: API dream count matches MySQL
# ---------------------------------------------------------

def test_api_dream_count_matches_database():
    assert len(get_api_dreams()) == get_database_dream_count()


# ---------------------------------------------------------
# TC-31: Update a real dream, then restore its exact values
# ---------------------------------------------------------

def test_updated_dream_is_displayed_in_diary(page):
    dreams = get_api_dreams()

    if not dreams:
        pytest.skip("No existing dream is available to test")

    # Capture the exact original record from MySQL.
    dream_id = int(dreams[0]["id"])
    original = get_database_dream(dream_id)

    assert original is not None

    temporary_name = f"{original['name']} - Integration Test"
    original_name = original["name"]
    original_days = int(original["days_ago"])
    original_type = original["type"]

    try:
        response = requests.put(
            f"{API_URL}/api/dreams/{dream_id}",
            json={
                "name": temporary_name,
                "daysAgo": original_days,
                "type": original_type,
            },
            timeout=TIMEOUT,
        )

        assert response.status_code in (200, 204), (
            f"Updating dream failed: "
            f"{response.status_code} {response.text}"
        )

        page.reload()

        diary_page = HomePage(page).click_my_dreams()
        assert diary_page.is_dream_displayed(temporary_name)

    finally:
        # Restore the original personal record, even if an assertion fails.
        restore_response = requests.put(
            f"{API_URL}/api/dreams/{dream_id}",
            json={
                "name": original_name,
                "daysAgo": original_days,
                "type": original_type,
            },
            timeout=TIMEOUT,
        )

        assert restore_response.status_code in (200, 204), (
            "WARNING: Failed to restore the original dream. "
            "Check this record in the app and database."
        )


# ---------------------------------------------------------
# TC-51: Newly added dream appears in Diary
# ---------------------------------------------------------

def test_new_dream_appears_in_diary(page):
    dream_name = "Integration Temporary Add Dream"
    dream_id = None

    # Avoid accidentally treating an existing record as test data.
    if find_api_dream_by_name(dream_name) is not None:
        pytest.skip("Temporary test name already exists; choose another name")

    try:
        dream_id = create_temporary_dream(dream_name)

        page.reload()
        diary_page = HomePage(page).click_my_dreams()

        assert diary_page.is_dream_displayed(dream_name)

    finally:
        delete_temporary_dream(dream_id)


# ---------------------------------------------------------
# TC-52: Dream count increases after adding
# ---------------------------------------------------------

def test_dream_count_increases_after_adding():
    dream_name = "Integration Count Temporary Dream"
    dream_id = None
    before_count = get_database_dream_count()

    if find_api_dream_by_name(dream_name) is not None:
        pytest.skip("Temporary test name already exists")

    try:
        dream_id = create_temporary_dream(dream_name)

        assert get_database_dream_count() == before_count + 1

    finally:
        delete_temporary_dream(dream_id)

    assert get_database_dream_count() == before_count


# ---------------------------------------------------------
# TC-53: Home count updates after adding
# ---------------------------------------------------------

def test_home_count_updates_after_adding(page):
    dream_name = "Integration Home Count Temporary Dream"
    dream_id = None
    before_count = get_database_dream_count()

    if find_api_dream_by_name(dream_name) is not None:
        pytest.skip("Temporary test name already exists")

    try:
        dream_id = create_temporary_dream(dream_name)

        page.reload()
        home_page = HomePage(page)

        assert int(home_page.get_dreams_count()) == before_count + 1

    finally:
        delete_temporary_dream(dream_id)


# ---------------------------------------------------------
# TC-54: Summary count updates after adding
# ---------------------------------------------------------

def test_summary_count_updates_after_adding():
    dream_name = "Integration Summary Temporary Dream"
    dream_id = None
    before_count = get_database_dream_count()

    if find_api_dream_by_name(dream_name) is not None:
        pytest.skip("Temporary test name already exists")

    try:
        dream_id = create_temporary_dream(dream_name)

        assert get_api_summary()["total"] == before_count + 1

    finally:
        delete_temporary_dream(dream_id)


# ---------------------------------------------------------
# TC-57: Maximum ten-dream rule
# ---------------------------------------------------------

def test_maximum_ten_dream_rule(page):
    count = get_database_dream_count()

    assert count <= MAX_DREAMS

    diary_page = HomePage(page).click_my_dreams()

    if count == MAX_DREAMS:
        assert not diary_page.is_add_dream_enabled()
    else:
        assert diary_page.is_add_dream_enabled()


# ---------------------------------------------------------
# TC-60: Diary matches DB after adding
# ---------------------------------------------------------

def test_diary_matches_database_after_adding(page):
    dream_name = "Integration Diary DB Temporary Dream"
    dream_id = None

    if find_api_dream_by_name(dream_name) is not None:
        pytest.skip("Temporary test name already exists")

    try:
        dream_id = create_temporary_dream(dream_name)

        page.reload()
        diary_page = HomePage(page).click_my_dreams()

        assert diary_page.is_dream_displayed(dream_name)
        assert (
            diary_page.get_edit_button_count()
            == get_database_dream_count()
        )

    finally:
        delete_temporary_dream(dream_id)


# ---------------------------------------------------------
# TC-69: Summary page receives backend data
# ---------------------------------------------------------

def test_summary_page_receives_backend_data(page):
    summary_page = HomePage(page).click_view_summary()

    assert summary_page.is_summary_page_displayed()
    assert summary_page.get_total_dreams_count() is not None
    assert summary_page.get_total_days_count() is not None


# ---------------------------------------------------------
# TC-79: Summary UI counts match API
# ---------------------------------------------------------

def test_summary_ui_counts_match_api(page):
    expected = get_api_summary()
    summary_page = HomePage(page).click_view_summary()

    assert int(summary_page.get_good_dreams_count()) == expected["good"]
    assert int(summary_page.get_bad_dreams_count()) == expected["bad"]
    assert int(summary_page.get_total_dreams_count()) == expected["total"]
    assert int(summary_page.get_total_days_count()) == expected["days"]


# ---------------------------------------------------------
# TC-80: API summary counts match MySQL
# ---------------------------------------------------------

def test_api_summary_counts_match_database():
    summary = get_api_summary()

    assert summary["total"] == get_database_dream_count()
    assert summary["days"] == get_database_days_count()


# ---------------------------------------------------------
# TC-81: Summary updates after edit, then restores original
# ---------------------------------------------------------

def test_summary_updates_after_edit():
    dreams = get_api_dreams()

    if not dreams:
        pytest.skip("No existing dream is available to test")

    dream_id = int(dreams[0]["id"])
    original = get_database_dream(dream_id)

    assert original is not None

    original_name = original["name"]
    original_days = int(original["days_ago"])
    original_type = original["type"]

    # Use the opposite category to check whether summary counts change.
    target_type = "Bad" if original_type.lower() == "good" else "Good"

    before = get_api_summary()

    try:
        response = requests.put(
            f"{API_URL}/api/dreams/{dream_id}",
            json={
                "name": original_name,
                "daysAgo": original_days,
                "type": target_type,
            },
            timeout=TIMEOUT,
        )

        assert response.status_code in (200, 204)

        after = get_api_summary()

        if original_type.lower() == "good":
            assert after["good"] == before["good"] - 1
            assert after["bad"] == before["bad"] + 1
        else:
            assert after["bad"] == before["bad"] - 1
            assert after["good"] == before["good"] + 1

    finally:
        restore_response = requests.put(
            f"{API_URL}/api/dreams/{dream_id}",
            json={
                "name": original_name,
                "daysAgo": original_days,
                "type": original_type,
            },
            timeout=TIMEOUT,
        )

        assert restore_response.status_code in (200, 204), (
            "WARNING: Failed to restore the original dream category."
        )


# ---------------------------------------------------------
# TC-82: Summary updates after removing a temporary dream
# ---------------------------------------------------------

def test_summary_updates_after_removal():
    dream_name = "Integration Remove Temporary Dream"
    dream_id = None

    if find_api_dream_by_name(dream_name) is not None:
        pytest.skip("Temporary test name already exists")

    try:
        dream_id = create_temporary_dream(dream_name)
        count_after_add = get_api_summary()["total"]

        delete_temporary_dream(dream_id)
        dream_id = None

        assert get_api_summary()["total"] == count_after_add - 1

    finally:
        # If deletion failed earlier, retry cleanup of the test record only.
        delete_temporary_dream(dream_id)


# ---------------------------------------------------------
# TC-84: Values remain correct after refresh
# ---------------------------------------------------------

def test_values_remain_correct_after_refresh(page):
    database_count = get_database_dream_count()
    database_days = get_database_days_count()

    page.reload()
    home_page = HomePage(page)

    assert int(home_page.get_dreams_count()) == database_count
    assert int(home_page.get_days_count()) == database_days


# ---------------------------------------------------------
# TC-87: Invalid API endpoint does not return success
# ---------------------------------------------------------

def test_invalid_api_endpoint_does_not_return_success():
    response = requests.get(
        f"{API_URL}/api/invalid-dreams-endpoint",
        timeout=TIMEOUT,
    )

    assert response.status_code != 200


# ---------------------------------------------------------
# TC-88: Overall UI, API and database consistency
# ---------------------------------------------------------

def test_ui_api_database_consistency(page):
    database_count = get_database_dream_count()
    database_days = get_database_days_count()

    api_dreams = get_api_dreams()
    summary = get_api_summary()

    home_page = HomePage(page)

    assert len(api_dreams) == database_count
    assert summary["total"] == database_count
    assert summary["days"] == database_days
    assert int(home_page.get_dreams_count()) == database_count
    assert int(home_page.get_days_count()) == database_days
