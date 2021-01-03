#Author: sravanthi.devarashetty
@Regression @BOSE3081
Feature: BOSE-3081-Assign Opportunities to a Presentation: Complete the work for Assigning DPs to a Presentation

  @BOSE-3713 @BOSE-3129
  Scenario Outline: BOSE-3713 - Ability to view Assign icon on Available Opportunity Header and assign all DP's to Presentation profile
    Given User "<user>" logged into "" application with Services
    Given the "<user>" moving the data from CPW to PM through the service for the client "<Client>" with "Multiple" DPKey
    Given User "<user>" logged into "PM" application
    When user selects "<Client>" from Client drop down list
    When user selects "All" Claimtypes in filtersection
    And user "SELECT" "Payer Shorts:LOB" under Payershort/LOB section
    And user select "MP_TOPIC_ALL" value "" for "DESELECT" operation
    And user select "MP_TOPIC_ALL" value "" for "SELECT" operation
    Then user validate Presentation profile "CREATE" functionality
    And User filters the "AvailableDPsDeck" with filter drpdown value "NotStartedOnly" to show related DPs
    And user assigns presentation "" for respective DP under "HEADER","" level
    And User filters the "AvailableDPsDeck" with filter drpdown value "CompletedOnly" to show related DPs
    And verify presentation name on each DP card
    And verify total savings as sum of each DP card
    Then User logsout of the "PM" Application

    Examples: 
      | user         | Client           |
      #| iht_ittest09 | Health Net, Inc. |
      | iht_ittest09 | Aetna Medicaid   |

  @BOSE-3714 @BOSE-3129
  Scenario Outline: BOSE-3714 - Ability to view Assign some of the DP's to Presentation profile from Available Opportunity Header
    Given User "iht_ittest09" logged into "" application with Services
    Given capture "Single" DP with new Payer short for the "<Client>" through Service
    Given User "iht_ittest09" logged into "PM" application
    When user selects "<Client>" from Client drop down list
    When user selects "All" Claimtypes in filtersection
    And user "SELECT" "Payer Shorts:LOB" under Payershort/LOB section
    And user select "MP_TOPIC_ALL" value "" for "DESELECT" operation
    And user select "MEDICAL POLICY" value "DB" for "SELECT" operation
    Then user validate Presentation profile "CREATE" functionality
    And User filters the "AvailableDPsDeck" with filter drpdown value "NotStartedOnly" to show related DPs
    And user assigns presentation "" for respective DP under "HEADER","" level
    And User filters the "AvailableDPsDeck" with filter drpdown value "CompletedOnly" to show related DPs
    And verify presentation name on each DP card
    And verify total savings as sum of each DP card
    Then User logsout of the "PM" Application

    Examples: 
      | Client         |
      | Aetna Medicaid |
