
from playwright.sync_api import Page, expect
from pages.base_page import BasePage


class AddDreamPage(BasePage):

    def __init__(self, page: Page):
        super().__init__(page)

        self.page_title = page.get_by_role(
            "heading", name="Add Dream", exact=True
        )

        self.edit_page_title = page.get_by_role(
            "heading", name="Edit Dream", exact=True
        )

        self.dream_name_input = page.get_by_placeholder(
            "Enter your dream"
        )

        self.days_ago_select = page.locator(
            ".add-dream-container select"
        ).nth(0)

        self.dream_type_select = page.locator(
            ".add-dream-container select"
        ).nth(1)

        self.save_button = page.locator(
            "button[type='submit']"
        )

        self.cancel_button = page.get_by_role(
            "button", name="Back to Dreams Diary"
        )

    def is_add_dream_page_displayed(self) -> bool:
        return (
            self.page_title.is_visible()
            or self.edit_page_title.is_visible()
        )

    def get_page_title(self) -> str:
        if self.page_title.is_visible():
            return self.page_title.inner_text()

        if self.edit_page_title.is_visible():
            return self.edit_page_title.inner_text()

        raise AssertionError(
            "Neither Add Dream nor Edit Dream page is displayed"
        )

    def enter_dream_name(self, dream_name: str):
        self.dream_name_input.fill(dream_name)

    def get_dream_name(self) -> str:
        return self.dream_name_input.input_value()

    def enter_days_ago(self, days: str):
        days_number = int(days)

        if days_number < 1 or days_number > 7:
            raise ValueError("Days ago must be between 1 and 7")

        option_text = (
            "1 day ago"
            if days_number == 1
            else f"{days_number} days ago"
        )

        self.days_ago_select.select_option(
            label=option_text
        )

    def get_selected_days_ago(self) -> str:
        return self.days_ago_select.locator(
            "option:checked"
        ).inner_text()

    def select_good_dream(self):
        self.dream_type_select.select_option(label="Good")

    def select_bad_dream(self):
        self.dream_type_select.select_option(label="Bad")

    def is_good_dream_selected(self) -> bool:
        return (
            self.dream_type_select.locator(
                "option:checked"
            ).inner_text()
            == "Good"
        )

    def is_bad_dream_selected(self) -> bool:
        return (
            self.dream_type_select.locator(
                "option:checked"
            ).inner_text()
            == "Bad"
        )

    
    def click_save_dream(self):
        from pages.dreams_diary_page import DreamsDiaryPage

        self.save_button.click()
        return DreamsDiaryPage(self.page)

    def click_cancel(self):
        from pages.dreams_diary_page import DreamsDiaryPage

        self.cancel_button.click()
        return DreamsDiaryPage(self.page)

    def click_back_to_diary(self):
        from pages.dreams_diary_page import DreamsDiaryPage

        self.cancel_button.click()
        return DreamsDiaryPage(self.page)
