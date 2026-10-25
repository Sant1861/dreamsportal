
*** Settings ***
Library    Browser
Library    RequestsLibrary
Suite Setup    Open Dreams Portal
Suite Teardown    Close Browser

*** Variables ***
${BASE_URL}    http://localhost:5173
${API_URL}    http://localhost:3000

*** Keywords ***
Open Dreams Portal
    New Browser    chromium    headless=False
    New Context
    New Page    ${BASE_URL}

*** Test Cases ***
TC01 Verify Dreams Portal Home Page
    Get Text    body    contains    Dream

TC02 Verify My Dreams Page And Record Limit
    New Page    ${BASE_URL}
    Click    text=My Dreams
    Get Text    body    contains    Dream
    ${count}=    Get Element Count    button:has-text("Edit")
    Should Be True    ${count} <= 10

TC03 Verify Dreams Summary Page
    New Page    ${BASE_URL}
    Click    text=View Summary
    Get Text    body    contains    Dreams Summary

    ${good}=    Get Text    tr:has-text("Good Dreams") td:nth-child(2)
    ${bad}=    Get Text    tr:has-text("Bad Dreams") td:nth-child(2)
    ${total}=    Get Text    tr:has-text("Total Dreams") td:nth-child(2)
    ${days}=    Get Text    tr:has-text("Total Days") td:nth-child(2)

    Should Match Regexp    ${good}    ^[0-9]+$
    Should Match Regexp    ${bad}    ^[0-9]+$
    Should Match Regexp    ${total}    ^[0-9]+$
    Should Match Regexp    ${days}    ^[0-9]+$

    ${calculated_total}=    Evaluate    int($good) + int($bad)
    Should Be Equal As Integers    ${calculated_total}    ${total}


TC04 Verify Summary API Returns Valid Data
    ${response}=    GET    ${API_URL}/api/summary    expected_status=200
    ${body}=    Set Variable    ${response.json()}

    Should Be True    ${body}[success]
    ${summary}=    Set Variable    ${body}[data]

    Should Contain    ${summary}    good
    Should Contain    ${summary}    bad
    Should Contain    ${summary}    total
    Should Contain    ${summary}    days

    Should Be True    ${summary}[good] >= 0
    Should Be True    ${summary}[bad] >= 0
    Should Be True    ${summary}[total] >= 0
    Should Be True    ${summary}[days] >= 0

    ${calculated_total}=    Evaluate    ${summary}[good] + ${summary}[bad]
    Should Be Equal As Integers    ${calculated_total}    ${summary}[total]

