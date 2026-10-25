
from playwright.sync_api import Page
from pages.base_page import BasePage
from pages.dreams_diary_page import DreamsDiaryPage
from pages.dreams_total_page import DreamsTotalPage


class HomePage(BasePage):

    def __init__(self, page: Page):
        super().__init__(page)

        self.page_title = page.get_by_role(
            "heading",
            name="Dream Portal",
            exact=True
        )

        self.my_dreams_button = page.get_by_role(
            "button",
            name="MY DREAMS",
            exact=True
        )

        self.view_summary_button = page.get_by_role(
            "button",
            name="VIEW SUMMARY",
            exact=True
        )

        self.dreams_count = page.locator(
            ".stat-item"
        ).filter(
            has=page.get_by_text("Dreams", exact=True)
        ).locator(".number")

        self.days_count = page.locator(
            ".stat-item"
        ).filter(
            has=page.get_by_text("Days", exact=True)
        ).locator(".number")

    def get_page_title_text(self) -> str:
        return self.page_title.inner_text()

    def is_home_page_displayed(self) -> bool:
        return self.page_title.is_visible()

    def click_my_dreams(self) -> DreamsDiaryPage:
        self.my_dreams_button.click()
        return DreamsDiaryPage(self.page)

    def click_view_summary(self) -> DreamsTotalPage:
        self.view_summary_button.click()
        return DreamsTotalPage(self.page)

    def get_dreams_count(self) -> str:
        return self.dreams_count.inner_text()

    def get_days_count(self) -> str:
        return self.days_count.inner_text()
