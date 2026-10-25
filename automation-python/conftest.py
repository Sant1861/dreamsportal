
import os
import pytest
from playwright.sync_api import sync_playwright


BASE_URL = os.getenv("FRONTEND_URL", "http://localhost:5173")


@pytest.fixture
def page():
    with sync_playwright() as playwright:
        browser = playwright.chromium.launch(headless=False)

        context = browser.new_context(
            viewport=None
        )

        page = context.new_page()
        page.goto(
            BASE_URL,
            wait_until="networkidle",
            timeout=20000
        )

        yield page

        context.close()
        browser.close()
