
import requests


BASE_URL = "http://localhost:3000"


def test_verify_get_dreams_api_status_code():
    response = requests.get(
        f"{BASE_URL}/api/dreams",
        timeout=10
    )

    assert response.status_code == 200, (
        "GET /api/dreams did not return 200"
    )


def test_verify_get_dreams_api_returns_data():
    response = requests.get(
        f"{BASE_URL}/api/dreams",
        timeout=10
    )

    assert response.content is not None, (
        "API response body is null"
    )
    assert response.text.strip() != "", (
        "API response body is empty"
    )

    response_data = response.json()

    assert response_data.get("success") is True, (
        "API success field is false"
    )


def test_verify_dreams_api_response_is_json():
    response = requests.get(
        f"{BASE_URL}/api/dreams",
        timeout=10
    )

    content_type = response.headers.get("Content-Type", "")

    assert "application/json" in content_type.lower(), (
        "Response is not JSON"
    )


def test_verify_dreams_api_response_contains_expected_fields():
    response = requests.get(
        f"{BASE_URL}/api/dreams",
        timeout=10
    )

    response_data = response.json()

    assert response_data.get("success") is True, (
        "API success field is false"
    )

    dreams = response_data.get("data")

    assert isinstance(dreams, list), (
        "API data field should be a list"
    )

    if len(dreams) > 0:
        dream = dreams[0]

        expected_fields = [
            "id",
            "name",
            "days_ago",
            "type",
            "created_at"
        ]

        for field in expected_fields:
            assert field in dream, f"{field} field is missing"
            assert dream[field] is not None, (
                f"{field} field is null"
            )


def test_verify_get_single_dream_api():
    all_dreams_response = requests.get(
        f"{BASE_URL}/api/dreams",
        timeout=10
    )

    assert all_dreams_response.status_code == 200, (
        "GET /api/dreams did not return 200"
    )

    dreams = all_dreams_response.json().get("data", [])

    if len(dreams) > 0:
        dream_id = dreams[0]["id"]

        single_dream_response = requests.get(
            f"{BASE_URL}/api/dreams/{dream_id}",
            timeout=10
        )

        assert single_dream_response.status_code == 200, (
            "GET single dream did not return 200"
        )

        assert single_dream_response.json().get("success") is True, (
            "Single dream API success field is false"
        )
