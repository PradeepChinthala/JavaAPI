#Author: Sravanthi.devarashetty
@Regression @BOSE2587
Feature: BOSE-2587 Presentation Manager Oppurtunity Deck

  Background: 
    Given User "iht_ittest05" logged into "PM" application

  ############################### Scenairo-1 #######################################################################
  @BOSE-3746 @BOSE-3029
  Scenario Outline: BOSE-3029 - validation of "No Opportunities Available Message" in Presentation Manager Application
    When user selects "<Client>" from Client drop down list
    And User selects  multiple Payershorts "<Payershorts1>" from Payershorts FilterSection
    And User selects muliple LOBs "<LOBs1>" from LOB FilterSection
    And User clicks on "Apply" button to filter
    Then verify "No opportunities match your selections" message
    #And user "DESELECT" "Payer Shorts:LOB" under Payershort/LOB section
    #And user "SELECT" "Payer Shorts:LOB" under Payershort/LOB section
    #And User selects single Payershort "<Payershorts2>" from Payershorts FilterSection
    #And User selects  multiple Payershorts "<Payershorts2>" from Payershorts FilterSection
    #And User selects muliple LOBs "<LOBs2>" from LOB FilterSection
    #And User clicks on "Apply" button to filter
    And User selects  multiple Payershorts "All" from Payershorts FilterSection
    And User selects muliple LOBs "All" from LOB FilterSection
    When user selects "All" Claimtypes in filtersection
    And User clicks on "Apply" button to filter
    Then verify "No opportunities match your selections" message is disappeared
    And User logsout of the "PM" Application

    Examples: 
      | Client             | Payershorts1 | LOBs1      | 
      | UnitedHealth Group | UHCAZ        | Commercial | 

  ############################### Scenairo-2 #######################################################################
  @BOSE2898 @Sanity
  Scenario Outline: BOSE-3779-Ability to view Ell order and Priority options in Sort by drop down on both Available Opportunities deck and Presentation deck
    Given User "ulanka" logged into "PM" application
    Then The user should view the PresentationManager HomePage
    When user selects "<Client>" from Client drop down list
    Then The "Sortby" dropdown in the Toolbar should have the "<Options>"
    And User logsout of the "PM" Application

    Examples: 
      | Client             | Payershorts | LOBs | MedPolicyTopicType | Options                                                                                              |
      | UnitedHealth Group | All         | All  | AllMedicalPolicies | Alphanumeric,Priority,eLL Order,Savings by Highest Dollar DP,Savings by Highest Dollar Policy,Custom |
