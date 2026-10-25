
from pages.home_page import HomePage
from playwright.sync_api import expect


def test_verify_dreams_diary_page_displayed(page):
    home_page = HomePage(page)
    diary_page = home_page.click_my_dreams()

    assert diary_page.is_diary_page_displayed(), \
        "Dreams Diary page is not displayed"


def test_verify_dreams_diary_page_title(page):
    home_page = HomePage(page)
    diary_page = home_page.click_my_dreams()

    assert diary_page.get_page_title() == "Dreams Diary", \
        "Dreams Diary page title is incorrect"


def test_verify_back_to_home(page):
    home_page = HomePage(page)
    diary_page = home_page.click_my_dreams()

    returned_home_page = diary_page.click_back_to_home()

    assert returned_home_page.is_home_page_displayed(), \
        "Could not navigate back to Home page"


def test_verify_add_dream_button(page):
    home_page = HomePage(page)
    diary_page = home_page.click_my_dreams()

    dream_count = diary_page.get_edit_button_count()

    assert dream_count <= 10, \
        "Dream count exceeds the maximum limit of 10"

    if dream_count < 10:
        assert diary_page.is_add_dream_enabled(), \
            "Add Dream button should be enabled when fewer than 10 dreams exist"
    else:
        assert not diary_page.is_add_dream_enabled(), \
            "Add Dream button should be disabled when 10 dreams exist"


def test_verify_dream_count_does_not_exceed_ten(page):
    home_page = HomePage(page)
    diary_page = home_page.click_my_dreams()

    dream_count = diary_page.get_edit_button_count()

    assert dream_count <= 10, \
        "Dream count exceeds the maximum limit of 10"


def test_verify_add_dream_page_opens_without_saving(page):
    home_page = HomePage(page)
    diary_page = home_page.click_my_dreams()

    dream_count = diary_page.get_edit_button_count()

    if dream_count < 10:
        assert diary_page.is_add_dream_enabled(), \
            "Add Dream should be enabled when fewer than 10 dreams exist"

        add_dream_page = diary_page.click_add_dream()

        assert add_dream_page.is_add_dream_page_displayed(), \
            "Add Dream page is not displayed"

        add_dream_page.click_back_to_diary()

        expect(diary_page.page_title).to_be_visible()
    else:
        assert not diary_page.is_add_dream_enabled(), \
            "Add Dream should be disabled at 10 dreams"
