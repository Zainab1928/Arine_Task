@PharmacistPortal @LabTableInQualityTab
Feature: delete the Reason for Non-adherence

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
    # PROD-25645 :TC-06
  Scenario:Verify that the advanced patient search does not show the MBI field while searching for a patient when the config is set or removed.
    When Click on Patient Tab
    And User click on ADVANCE search
    Then Verify there is no MBI field in the Advanced Search modal


  @Regression @Smoke
    # PROD-25645 :TC-01
  Scenario Outline: Verify that the Elastic search works fine while searching for a patient when the config is set.
    When Click on Patient Tab
    And Search patient: "<PatientId>"
    And User click on ADVANCE search
    And User search patient by MBI: "<MBI>"
    And Successfully show the patient matching the inputted MBI from the search results "<MBI>"

    Examples:
      | PatientId                            | MBI          |
      | b2434d2f-59f8-4ea1-9cd9-05931aa3a3a0 | 5VC8R83YW56  |

  @Regression @Smoke
    # PROD-25645 :TC-02
  Scenario Outline:Verify that the user can load the same edited patient profile from the MBI search field on the Patient Advanced Search modal
    When Click on Patient Tab
    And Search patient: "<PatientId>"
    Then Verify user able to Edit the Patients demographic details with new city "<city>"
    And User click on ADVANCE search
    And User search patient by MBI: "<MBI>"
    And Click on patient profile: "<PatientName>"
    Then Verify the same patient profile is loaded with the updated city field "<city>"


    Examples:
      | PatientId                            | MBI          | PatientName             |city      |
      | 6140bc9e-4732-4adc-8aa0-96ea8b2fe920 | 5VC8R83YW56  | TestThomas TestMcgee    |Mumbai    |

  @Regression @Smoke
    # PROD-25645 :TC-04
  Scenario Outline: Verify that the user is not able to click or enter other search filters when mbi input is not empty on the Patient Advanced Search modal
    When Click on Patient Tab
    And Search patient: "<PatientId>"
    And User click on ADVANCE search
    And User search patient by MBI: "<MBI>"
    Then Verify there is no MBI field in the Advanced Search modal

    Examples:
      | PatientId                            | MBI          | PatientName             |city      |
      | 6140bc9e-4732-4adc-8aa0-96ea8b2fe920 | 5VC8R83YW56  | TestThomas TestMcgee    |Mumbai    |


#  @Regression @Smoke
#    # PROD-25645 :TC-05
#  Scenario Outline:Verify that the user can load each patient profile from the search result on the Patient Advanced Search modal.
#    When Click on Patient Tab
#    And Search patient: "<PatientId>"
#    And User click on ADVANCE search
#    And User search patient by MBI: "<MBI>"
#    And Successfully show the patient matching the inputted MBI from the search results "<MBI>"
#    And Click on patient profile: "<PatientName>"
#    Then Verify the same patient profile is loaded
#
#
#    Examples:
#      | PatientId                            | MBI          | PatientName           |
#      | 6140bc9e-4732-4adc-8aa0-96ea8b2fe920 | 5VC8R83YW56  | TestThomas TestMcgee |


  @Regression @Smoke
    #  PROD- 28429
  Scenario Outline: Verify that the system successfully shows the patient matching the inputted MBI from the search results
    When Click on Patient Tab
    And User click on ADVANCE search
    And User search patient by hpid: "<HPID>"
    And Successfully show the patient matching the inputted HPID from the search results."<HPID>"
    Then Verify user able to Edit the Patients demographic details with new address "<NewAddress>"
    And User click on ADVANCE search
    And User search patient by MBI: "<MBI>"
    And Successfully show the patient matching the inputted MBI from the search results "<MBI>"

    Examples:
      | HPID       | MBI          | NewAddress           |
      | 0f5d4a83   | 5VC8R83YW56  | 123 London           |


  @Setup @Regression @Smoke
  Scenario: SETUP: Logout and Close Browser
    When User logout from the application
    Then User close browser