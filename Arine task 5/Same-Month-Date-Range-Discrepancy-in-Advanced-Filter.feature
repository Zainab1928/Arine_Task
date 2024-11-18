@PharmacistPortal @PractitionerTabPrescriberAnalytics @PAFeature
Feature: Same-Month Date Range Discrepancy in Advanced Filter of the Reported DRPs Table

  @Setup @Regression @Smoke
  Scenario Outline: SETUP: Launch Browser and go to application
    Given User launched "chrome"
    Then User go to application "$pharmacist_portal_url"
    When User login with "<username>" and "<password>"
    And Verify Login message: "<message>"
    Examples:
      | username                                           | password                                           | message |
      | $practitioner-tab-reports-analytics.user1.username | $practitioner-tab-reports-analytics.user1.password | success |


  @Regression @Smoke
  Scenario Outline: Same-Month Date Range Discrepancy in Advanced Filter of the Reported DRPs Table
    Given User select organization: "<Organization>"
    When Click on Practitioners Tab
    And User search practitioner by npi: "<NPI>"
    Then Verify the User are able select the campaigns value as: "Test Campaign 1"
    Then Remove all previous applied filters from the campaigns tab by clicking the "Clear Filters" button
    Then Select the reported drp table filter "<searchFilterName>" as "<filterValue>" and click on "Apply" button
    And Verify "<verifyFilter>" column only "<filterValueToVerify>" values in reports drp table


    Examples:
      | Organization                      | NPI        | searchFilterName      | filterValue         |verifyFilter           | filterValueToVerify |

      | Admin Portal Test Patient Org DEV | 1750393690 | Report Created Date   | Date Range          | Report Created        | Date Range          |
      | Admin Portal Test Patient Org DEV | 1750393690 | Report Sent Date      | Date Range          | Report Sent           | Date Range          |

  @Setup @Regression @Smoke
  Scenario: SETUP: Logout and Close Browser
    When User logout from the application
    Then User close browser