#Author: chaitanya kumar natuva
@Regression @BOSE3369  @ReRun
Feature: BOSE-3369

  ######################################################   Scenario-1  #############################################################
  @BOSE3369_1
  Scenario Outline: BOSE3369_Scenario_1 validate the Available DPs count at header,MP and Topic Level of assignee popup
    Given User "<User>" logged into "PM" application
    When user selects "<Client>" from Client drop down list
    And User selects  multiple Payershorts "All" from Payershorts FilterSection
    And User selects muliple LOBs "All" from LOB FilterSection
    When user selects "All" Claimtypes in filtersection
    And User clicks on "Apply" button to filter
    And User selects Medical Policy/Topic according to "AllMedicalPolicies"
    And user filters by clicking on Apply for Medical Policy/Topic
    Then validate the avaliable DPs count at "ASSIGNEE POPUP DP COUNT BASED ON CLIENT"
    And validate the avaliable DPs count at "ASSIGNEE POPUP DP COUNT BASED ON MP"
    Then validate the avaliable DPs count at "ASSIGNEE POPUP DP COUNT BASED ON TOPIC"

    Examples: 

      | User   | Client |
      | nkumar | Humana |


  ######################################################   Scenario-2  #############################################################
  @BOSE3369_2
  Scenario Outline: BOSE3369_Scenario_2 validate the Available DPs count at header,MP and Topic Level With Filters of assignee popup
    Given User "<User>" logged into "PM" application
    When user selects "<Client>" from Client drop down list
    And User selects  multiple Payershorts "<Payershort>" from Payershorts FilterSection
    And User selects muliple LOBs "<Insurance>" from LOB FilterSection
    When user selects "All" Claimtypes in filtersection
    And User clicks on "Apply" button to filter
    And User selects Medical Policy/Topic according to "AllMedicalPolicies"
    And user filters by clicking on Apply for Medical Policy/Topic
    Then validate the avaliable DPs count at "ASSIGNEE POPUP DP COUNT BASED ON CLIENT WITH FILTERS"
    And validate the avaliable DPs count at "ASSIGNEE POPUP DP COUNT BASED ON MP WITH FILTERS"
    Then validate the avaliable DPs count at "ASSIGNEE POPUP DP COUNT BASED ON TOPIC WITH FILTERS"

    Examples: 
      | User   | Client | Payershort  | Insurance         |
      | nkumar | Humana | HUMCC,HUMCF | Medicare,Medicaid |
