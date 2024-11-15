@PharmacistPortal @LabTableInQualityTab
Feature: delete the Reason for Non-adherence
#  PROD- 3184

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
  Scenario Outline: Verify that the user can add the Reason for Non-adherence in dropdwon list and verify is added in list or not
    When Click on Patient Tab
    And Search patient: "<PatientId>"
    And Click on tab: "Med List"
    And Click on patient first medicine record
    And User clicks on dropDown in Medlist page and selects "Adherent - filled through cash" option
    And Verify "Adherent - filled through cash" option in dropdown field present in Medlist tab
    And Verify "Adherent - filled through cash" option in dropdown list present in Medlist tab
    And Verify "Adherent - filled through cash" option is not visible in dropdown list present in Medlist tab


    Examples:-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------+***-
      | PatientId                            |
      | 3620b636-5b8e-44b3-8461-130983b7dc4e |

  @Regression @Smoke
  Scenario Outline: Verify that the user can remove the Reason for Non-adherence in dropdown list and verify is remove or not
    When Click on Patient Tab
    And Search patient: "<PatientId>"
    And Click on tab: "Med List"
    And Click on patient first medicine record
    And User clicks on dropDown in Medlist page and selects "Adherent - filled through cash" option
    And Verify "Adherent - filled through cash" option in dropdown field present in Medlist tab
    And Remove "Adherent - filled through cash" option from dropdown by clicking on remove button
    Then Verify "Adherent - filled through cash" option removed successfully from dropdown list


    Examples:-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------+***-
      | PatientId                            |
      | 3620b636-5b8e-44b3-8461-130983b7dc4e |


  @Setup @Regression @Smoke
  Scenario: SETUP: Logout and Close Browser
    When User logout from the application
    Then User close browser