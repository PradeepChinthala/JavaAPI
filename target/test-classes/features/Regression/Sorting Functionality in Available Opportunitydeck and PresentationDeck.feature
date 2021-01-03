#Author: chaitanya kumar natuva
@Regression @Sorting @BOSE2502 @BOSE2579 @BOSE2898  @ReRun
Feature: Sorting Functinonality

  #####################################################   Scenario-1  #############################################################
  @Sorting_1
  Scenario Outline: Sorting_1 Validate the sorting functionality in available opportunity deck for option sortby "<Sort by>"
    Given User "<User>" logged into "PM" application
    When user selects "<Client>" from Client drop down list
    And User selects  multiple Payershorts "All" from Payershorts FilterSection
    And User selects muliple LOBs "All" from LOB FilterSection
    When user selects "All" Claimtypes in filtersection
    And User clicks on "Apply" button to filter
    And User selects Medical Policy/Topic according to "AllMedicalPolicies"
    And user filters by clicking on Apply for Medical Policy/Topic
    Then validate the sorting functionality for sortby option "<Sort by>" in "Availble Opportunity"

    Examples: 
      | User   | Client         | Sort by                                                       |
      | nkumar | Tufts Health Plan | Alphanumeric,Priority,eLL Order,Savings by Highest Dollar DP,Savings by Highest Dollar Policy |
      

  #####################################################   Scenario-2  #############################################################
  @Sorting_2
  Scenario Outline: Sorting_2 Validate the sorting functionality in PresentationDeck for option sortby "<Sort by>"
    Given User "<User>" logged into "" application with Services
    Given the "<User>" moving the data from CPW to PM through the service for the client "<Client>" with "Multiple" DPKey
    Then Create the presenatation profile with given request data client "<Client>" payershort "" lob "" product "" and prority "" with "<User>"
    Then assign multiple dps to created profile
    Given User "<User>" logged into "PM" application
    When user selects "<Client>" from Client drop down list
    Then The CPMorCMD clicks on the "Presentation having DPs"
    And Apply All Payers and LOBs filters in Presenation profile
    And Expand all the items in presenation hierarchy view
    Then validate the sorting functionality for sortby option "<Sort by>" in "PresentatinDeck"

    Examples: 
      | User   | Client         | Sort by                                                                                       |
      | nkumar | Aetna Medicaid | Alphanumeric,Priority,eLL Order,Savings by Highest Dollar DP,Savings by Highest Dollar Policy |
