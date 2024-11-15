@PharmacistPortal @LabTableInQualityTab
Feature: Verify that the upper right dropdown working properly

  @Setup @Regression @Smoke
  Scenario Outline: SETUP: Launch Browser and go to application
    Given User launched "chrome"
    Then User go to application "$pharmacist_portal_url"
    When User login with "<username>" and "<password>"
    And Verify Login message: "<message>"
    Examples:
      | username                                 | password                                 | message |
      | $lab-table-in-quality-tab.user2.username | $lab-table-in-quality-tab.user2.password | success |


  @Regression @Smoke
    #FS-382:TC-01
  Scenario: Verify that the user can see the following options in the upper right with the user’s initials 1.Manage Access,2.Layout,3.Sign Out
    When User clicks on "user’s initials" dropDown
    Then Verify that the user can see the options in the upper right "Manage Access, Layout, Sign Out"

  @Regression @Smoke
    #FS-382:TC-02
  Scenario: Verify that the user can see the following options by hovering on the Manage Access field: 1.MFA Settings, 2.Register SSO with Google
    When User clicks on Manage Access field
    Then Verify that the user can see the options by hovering on the Manage Access field "MFA Settings" and "Register SSO with Google"


  @Regression @Smoke
    #FS-382:TC-03
  Scenario: Verify that the user can see the following options by hovering on the 'Layout' field 1.Lock/Unlock Layout, 2.Reset layout
#    When User clicks on "user’s initials" dropDown
    And User clicks on "Layout" field
    Then Verify that the user can see the options by hovering on the Layout field "Unlock Layout" and "Reset layout"


  @Setup @Regression @Smoke
  Scenario: SETUP: Logout and Close Browser
    When User logout from the application
    Then User close browser