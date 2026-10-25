
from pages.home_page import HomePage


def test_verify_summary_page_displayed(page):
    home_page = HomePage(page)
    summary_page = home_page.click_view_summary()

    assert summary_page.is_summary_page_displayed(), (
        "Dreams Summary page is not displayed"
    )


def test_verify_summary_page_title(page):
    home_page = HomePage(page)
    summary_page = home_page.click_view_summary()

    assert summary_page.get_page_title() == "Dreams Summary", (
        "Summary page title is incorrect"
    )


def test_verify_good_dreams_count_displayed(page):
    home_page = HomePage(page)
    summary_page = home_page.click_view_summary()

    count = summary_page.get_good_dreams_count()

    assert count.strip() != "", "Good Dreams count is empty"
    assert count.isdigit(), (
        "Good Dreams count should contain only numbers"
    )

    good_dreams_count = int(count)

    assert good_dreams_count >= 0, (
        "Good Dreams count cannot be negative"
    )


def test_verify_bad_dreams_count_displayed(page):
    home_page = HomePage(page)
    summary_page = home_page.click_view_summary()

    count = summary_page.get_bad_dreams_count()

    assert count.strip() != "", "Bad Dreams count is empty"
    assert count.isdigit(), (
        "Bad Dreams count should contain only numbers"
    )

    bad_dreams_count = int(count)

    assert bad_dreams_count >= 0, (
        "Bad Dreams count cannot be negative"
    )


def test_verify_total_dreams_displayed(page):
    home_page = HomePage(page)
    summary_page = home_page.click_view_summary()

    count = summary_page.get_total_dreams_count()

    assert count.strip() != "", "Total Dreams count is empty"
    assert count.isdigit(), (
        "Total Dreams count should contain only numbers"
    )

    total_dreams_count = int(count)

    assert total_dreams_count >= 0, (
        "Total Dreams count cannot be negative"
    )


def test_verify_total_days_displayed(page):
    home_page = HomePage(page)
    summary_page = home_page.click_view_summary()

    count = summary_page.get_total_days_count()

    assert count.strip() != "", "Total Days count is empty"
    assert count.isdigit(), (
        "Total Days count should contain only numbers"
    )

    total_days_count = int(count)

    assert total_days_count >= 0, (
        "Total Days count cannot be negative"
    )


def test_verify_back_to_home(page):
    home_page = HomePage(page)
    summary_page = home_page.click_view_summary()

    returned_home_page = summary_page.click_back_to_home()

    assert returned_home_page.is_home_page_displayed(), (
        "Could not return to Home page"
    )
