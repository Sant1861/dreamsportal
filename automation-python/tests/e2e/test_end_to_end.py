
import re

from playwright.sync_api import expect

from pages.home_page import HomePage


def test_verify_complete_dream_journey(page):
    # 1. Home page
    home_page = HomePage(page)

    assert home_page.is_home_page_displayed(), (
        "Home page is not displayed"
    )

    # 2. Navigate to Dreams Diary
    diary_page = home_page.click_my_dreams()

    assert diary_page.is_diary_page_displayed(), (
        "Dreams Diary page is not displayed"
    )

    # 3. Validate the dream limit
    dream_count = diary_page.get_edit_button_count()

    assert dream_count <= 10, (
        "Dream count exceeds 10"
    )

    if dream_count < 10:
        assert diary_page.is_add_dream_enabled(), (
            "Add Dream should be enabled below 10 dreams"
        )
    else:
        assert not diary_page.is_add_dream_enabled(), (
            "Add Dream should be disabled at 10 dreams"
        )

    # 4. Return to Home
    home_page = diary_page.click_back_to_home()

    assert home_page.is_home_page_displayed(), (
        "Could not return to Home page"
    )

    # 5. Navigate to Dreams Summary
    summary_page = home_page.click_view_summary()

    assert summary_page.is_summary_page_displayed(), (
        "Dreams Summary page is not displayed"
    )

    # 6. Validate Good Dreams count
    good_count = summary_page.get_good_dreams_count()

    assert good_count != "", "Good Dreams count is empty"
    assert re.fullmatch(r"\d+", good_count), (
        "Good Dreams count should contain only numbers"
    )

    # 7. Validate Bad Dreams count
    bad_count = summary_page.get_bad_dreams_count()

    assert bad_count != "", "Bad Dreams count is empty"
    assert re.fullmatch(r"\d+", bad_count), (
        "Bad Dreams count should contain only numbers"
    )

    # 8. Validate Total Dreams count
    total_count = summary_page.get_total_dreams_count()

    assert total_count != "", "Total Dreams count is empty"
    assert re.fullmatch(r"\d+", total_count), (
        "Total Dreams count should contain only numbers"
    )

    # 9. Validate Total Days count
    total_days = summary_page.get_total_days_count()

    assert total_days != "", "Total Days count is empty"
    assert re.fullmatch(r"\d+", total_days), (
        "Total Days count should contain only numbers"
    )
