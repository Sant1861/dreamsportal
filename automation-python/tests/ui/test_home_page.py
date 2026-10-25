
from pages.home_page import HomePage
from playwright.sync_api import expect


def test_verify_home_page_displayed(page):
    home_page = HomePage(page)

    assert home_page.is_home_page_displayed(), \
        "Home page is not displayed"


def test_verify_home_page_title(page):
    home_page = HomePage(page)

    actual_title = home_page.get_page_title_text()

    assert actual_title == "Dream Portal", \
        "Home page title is incorrect"


def test_verify_my_dreams_navigation(page):
    home_page = HomePage(page)

    diary_page = home_page.click_my_dreams()

    expect(diary_page.heading).to_be_visible(), \
        "Dreams Diary page is not displayed"


def test_verify_view_summary_navigation(page):
    home_page = HomePage(page)

    summary_page = home_page.click_view_summary()

    assert summary_page.is_summary_page_displayed(), \
        "Dreams Summary page is not displayed"


def test_verify_dreams_count_is_displayed(page):
    home_page = HomePage(page)

    count = home_page.get_dreams_count()

    assert count.strip() != "", "Dream count is empty"
    assert count.isdigit(), "Dream count should contain only numbers"

    dream_count = int(count)

    assert dream_count >= 0, "Dream count cannot be negative"


def test_verify_days_count_is_displayed(page):
    home_page = HomePage(page)

    count = home_page.get_days_count()

    assert count.strip() != "", "Days count is empty"
    assert count.isdigit(), "Days count should contain only numbers"

    days_count = int(count)

    assert days_count >= 0, "Days count cannot be negative"
