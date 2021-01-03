#Author: sravanthi.devarashetty
@Regression @BOSE2988
Feature: BOSE-2988-Hide 'Product Type' from the filter panel

  @BOSE-3464 @BOSE-2988
  Scenario Outline: BOSE-3464 - Ability to hide 'Product Type' from the filter panel
    Given User "iht_ittest09" logged into "PM" application
    When user selects "<Client>" from Client drop down list
    Then validate existence of PPS details in HomePage
    And User logsout of the "PM" Application

    Examples: 
      | Client             |
      | UnitedHealth Group |

  @BOSE-3465 @BOSE-2988 @BOSE2988_2
  Scenario Outline: BOSE-3465 - Ability to view combined product (Professional& outpatient)raw savings on medical policy summary section
  	Given User "iht_ittest09" logged into "" application with Services
    Given capture "Single" DP with new Payer short for the "<Client>" through Service
    Given User "iht_ittest09" logged into "PM" application
    When user selects "<Client>" from Client drop down list
    And user "SELECT" "Payer Shorts:LOB" under Payershort/LOB section
    And user "DESELECT" "Payer Shorts:LOB" under Payershort/LOB section
    And user "SELECT" "LOB" under Payershort/LOB section
    When user selects "All" Claimtypes in filtersection
    And filter "first" payer short and "Topic" retrieved through Service
    Then validate RawSavings in DB and UI for MedicalPolicy captured through Service corresponding to "first" payer short
    Then User logsout of the "PM" Application

    Examples: 
      | Client |
      # |Gateway Health Plan|
      | Cigna  |
