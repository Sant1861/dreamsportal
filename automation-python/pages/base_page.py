
from playwright.sync_api import Page, expect


class BasePage:

    def __init__(self, page: Page):
        self.page = page

    def navigate_to(self, url: str):
        self.page.goto(url)

    def get_page_title(self) -> str:
        return self.page.title()

    def verify_element_visible(self, locator):
        expect(locator).to_be_visible()

    def click_element(self, locator):
        locator.click()

    def enter_text(self, locator, text: str):
        locator.fill(text)

    def select_dropdown_option(self, locator, value: str):
        locator.select_option(value)
