
from playwright.sync_api import Page, expect
from pages.base_page import BasePage


class DreamsTotalPage(BasePage):

    def __init__(self, page: Page):
        super().__init__(page)

        self.page_title = page.get_by_role(
            "heading", name="Dreams Summary", exact=True
        )

        self.back_to_home_button = page.get_by_role(
            "button", name="Back to Home"
        )

        self.good_dreams_count = page.locator(
            "tr"
        ).filter(
            has=page.get_by_text("Good Dreams", exact=True)
        ).locator("td").nth(1)

        self.bad_dreams_count = page.locator(
            "tr"
        ).filter(
            has=page.get_by_text("Bad Dreams", exact=True)
        ).locator("td").nth(1)

        self.total_dreams_count = page.locator(
            "tr"
        ).filter(
            has=page.get_by_text("Total Dreams", exact=True)
        ).locator("td").nth(1)

        self.total_days_count = page.locator(
            "tr"
        ).filter(
            has=page.get_by_text("Total Days", exact=True)
        ).locator("td").nth(1)

    def get_page_title(self) -> str:
        expect(self.page_title).to_be_visible()
        return self.page_title.inner_text()

    def is_summary_page_displayed(self) -> bool:
        return self.page_title.is_visible()

    def get_good_dreams_count(self) -> str:
        return self._wait_for_count(self.good_dreams_count)

    def get_bad_dreams_count(self) -> str:
        return self._wait_for_count(self.bad_dreams_count)

    def get_total_dreams_count(self) -> str:
        return self._wait_for_count(self.total_dreams_count)

    def get_total_days_count(self) -> str:
        return self._wait_for_count(self.total_days_count)

    def _wait_for_count(self, locator) -> str:
        expect(locator).to_be_visible()
        expect(locator).to_have_text(
            __import__("re").compile(r"^\d+$")
        )
        return locator.inner_text()

    
    def _wait_for_count(self, locator) -> str:
        expect(locator).to_be_visible()
        expect(locator).to_have_text(
            __import__("re").compile(r"^\d+$")
        )
        return locator.inner_text()

    def click_back_to_home(self):
        from pages.home_page import HomePage

        self.back_to_home_button.click()
        return HomePage(self.page)

