
from pages.home_page import HomePage


def open_add_dream_page_if_available(page):
    """Open Add Dream only when fewer than 10 dreams exist."""
    home_page = HomePage(page)
    diary_page = home_page.click_my_dreams()

    dream_count = diary_page.get_edit_button_count()

    assert dream_count <= 10, "Dream count is greater than 10"

    if dream_count == 10:
        assert not diary_page.is_add_dream_enabled(), (
            "Add Dream should be disabled at 10 dreams"
        )
        return None

    assert diary_page.is_add_dream_enabled(), (
        "Add Dream should be enabled below 10 dreams"
    )

    return diary_page.click_add_dream()


def test_verify_add_dream_page_displayed(page):
    add_dream_page = open_add_dream_page_if_available(page)

    if add_dream_page is None:
        return

    assert add_dream_page.is_add_dream_page_displayed(), (
        "Add Dream page is not displayed"
    )

    add_dream_page.click_back_to_diary()


def test_verify_dream_name_field(page):
    add_dream_page = open_add_dream_page_if_available(page)

    if add_dream_page is None:
        return

    add_dream_page.enter_dream_name("Test Automation Dream")

    assert add_dream_page.get_dream_name() == "Test Automation Dream", (
        "Dream name was not entered correctly"
    )

    add_dream_page.click_back_to_diary()


def test_verify_days_ago_dropdown(page):
    add_dream_page = open_add_dream_page_if_available(page)

    if add_dream_page is None:
        return

    add_dream_page.enter_days_ago("3")

    assert add_dream_page.get_selected_days_ago() == "3 days ago", (
        "Days Ago value was not selected correctly"
    )

    add_dream_page.click_back_to_diary()


def test_verify_good_dream_selection(page):
    add_dream_page = open_add_dream_page_if_available(page)

    if add_dream_page is None:
        return

    add_dream_page.select_good_dream()

    assert add_dream_page.is_good_dream_selected(), (
        "Good dream was not selected"
    )

    add_dream_page.click_back_to_diary()


def test_verify_bad_dream_selection(page):
    add_dream_page = open_add_dream_page_if_available(page)

    if add_dream_page is None:
        return

    add_dream_page.select_bad_dream()

    assert add_dream_page.is_bad_dream_selected(), (
        "Bad dream was not selected"
    )

    add_dream_page.click_back_to_diary()


def test_verify_back_without_saving(page):
    home_page = HomePage(page)
    diary_page = home_page.click_my_dreams()

    before_count = diary_page.get_edit_button_count()

    assert before_count <= 10, "Dream count is greater than 10"

    if before_count == 10:
        assert not diary_page.is_add_dream_enabled(), (
            "Add Dream should be disabled at 10 dreams"
        )
        return

    assert diary_page.is_add_dream_enabled(), (
        "Add Dream should be enabled below 10 dreams"
    )

    add_dream_page = diary_page.click_add_dream()
    add_dream_page.enter_dream_name("Temporary Dream")

    returned_diary = add_dream_page.click_back_to_diary()
    after_count = returned_diary.get_edit_button_count()

    assert after_count == before_count, (
        "Dream count changed even though the dream was not saved"
    )


def test_verify_ten_dream_limit(page):
    home_page = HomePage(page)
    diary_page = home_page.click_my_dreams()

    dream_count = diary_page.get_edit_button_count()

    assert dream_count <= 10, "Dream count must never exceed 10"

    if dream_count < 10:
        assert diary_page.is_add_dream_enabled(), (
            "Add Dream must be enabled when fewer than 10 dreams exist"
        )
    else:
        assert dream_count == 10, "Dream count is greater than 10"

        assert not diary_page.is_add_dream_enabled(), (
            "Add Dream must be disabled when 10 dreams exist"
        )
