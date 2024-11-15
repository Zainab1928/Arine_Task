@Regression @Smoke
Feature: Fetch user data from Excel file

  @Regression @Smoke
  Scenario Outline: Fetch user data from Excel and verify login with valid credentials
    Given User launched Browser "chrome"
    Then User go to application "$pharmacist_portal_url"
    When User login by "<username>" and "<password>"
    And Verify Login message: "<message>"
    When Click on Patient Tab
    And Search patient by: "<PatientId>"
    When User logout from the application
    Then User close browser

    Examples:
      | username | password | message | PatientId                            |
      |          |          | success | 224d6159-b5d4-46b2-a64a-082c431a5e19 |
      |          |          | success | 575f71a8-722d-43e4-b271-af9ab5842c82 |


