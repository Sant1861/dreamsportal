
from playwright.sync_api import Page, expect
from pages.base_page import BasePage
from pages.add_dream_page import AddDreamPage


class DreamsDiaryPage(BasePage):

    def __init__(self, page: Page):
        super().__init__(page)

        self.page_title = page.get_by_role(
            "heading", name="Dreams Diary", exact=True
        )
        self.heading = self.page_title

        self.back_to_home_button = page.get_by_role(
            "button", name="Back to Home"
        )

        self.add_dream_button = page.get_by_role(
            "button", name="Add Dream", exact=True
        )

        self.edit_buttons = page.get_by_role(
            "button", name="Edit", exact=True
        )

        self.remove_buttons = page.get_by_role(
            "button", name="Remove Dream", exact=True
        )

        self.dream_rows = page.locator("tbody tr")

    def is_diary_page_displayed(self) -> bool:
        return self.page_title.is_visible()

    def get_page_title(self) -> str:
        return self.page_title.inner_text()

    def click_add_dream(self) -> AddDreamPage:
        self.add_dream_button.click()
        return AddDreamPage(self.page)

    
    def is_add_dream_enabled(self) -> bool:
        return self.add_dream_button.is_enabled()

    def click_back_to_home(self):
        from pages.home_page import HomePage

        self.back_to_home_button.click()
        return HomePage(self.page)


    def get_edit_button_count(self) -> int:
        return self.edit_buttons.count()

    def is_dream_displayed(self, dream_name: str) -> bool:
        dream = self.page.get_by_text(dream_name, exact=True)
        return dream.count() > 0

    def click_edit_for_dream(self, dream_name: str) -> AddDreamPage:
        dream_row = self.dream_rows.filter(
            has=self.page.get_by_text(dream_name, exact=True)
        )

        edit_button = dream_row.get_by_role(
            "button", name="Edit", exact=True
        )

        edit_button.click()
        return AddDreamPage(self.page)

    def get_remove_button_count(self) -> int:
        return self.remove_buttons.count()

    def click_remove_dream(self):
        self.remove_buttons.click()

    def accept_remove_confirmation(self):
        self.page.on("dialog", lambda dialog: dialog.accept())

    def cancel_remove_confirmation(self):
        self.page.on("dialog", lambda dialog: dialog.dismiss())

    def is_dream_removed(self, dream_name: str) -> bool:
        return not self.is_dream_displayed(dream_name)
