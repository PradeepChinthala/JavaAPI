#Author: chaitanya kumar natuva
@Regression @BOSE2841
Feature: BOSE-2841

  # isssue in the DP COUNT BASED ON CLIENT WITH FILTERS for Dean Health with payer 'DHPMP',insurance 'Medicare' for validating show dropdown as'ALL'
  #have added the Filters.eq("opptyFinalize", null) for second scenario
  ######################################################   Scenario-1  #############################################################
  #issue for partial assigned only filter,8251,9448 dps are not displaying for 48 client
  @BOSE2841_1
  Scenario Outline: BOSE2841_1 Validate the 'Show' dropdown funtionality in Available Opportunity Deck of PM
    Given User "<User>" logged into "PM" application
    When user selects "<Client>" from Client drop down list
    And User selects  multiple Payershorts "All" from Payershorts FilterSection
    And User selects muliple LOBs "All" from LOB FilterSection
    When user selects "All" Claimtypes in filtersection
    And User clicks on "Apply" button to filter
    And User selects Medical Policy/Topic according to "AllMedicalPolicies"
    And user filters by clicking on Apply for Medical Policy/Topic
    Then validate the DPcards in available opportunity deck for "<Show Dropdown>" dropdown functionality

    Examples: 
      | User   | Client | Show Dropdown                                               |
      | nkumar | Cigna  | All,Not Started Only,Partially Assigned Only,Completed Only |

  ######################################################   Scenario-2  #############################################################
  @BOSE2841_2
  Scenario Outline: BOSE2841_2 Validate the 'Show' dropdown funtionality in Available Opportunity Deck of PM with Filters
    Given User "<User>" logged into "PM" application
    When user selects "<Client>" from Client drop down list
    And User selects  multiple Payershorts "<Payershort>" from Payershorts FilterSection
    And User selects muliple LOBs "<Insurance>" from LOB FilterSection
    When user selects "<Claimtypes>" Claimtypes in filtersection
    And User clicks on "Apply" button to filter
    And User selects Medical Policy/Topic according to "AllMedicalPolicies"
    And user filters by clicking on Apply for Medical Policy/Topic
    Then validate the DPcards in available opportunity deck for "<Show Dropdown>" dropdown functionality

    Examples: 
      | User   | Client | Show Dropdown                                                                                                   | Payershort | Insurance | Claimtypes |
      | nkumar | Cigna  | All with filters,Not Started Only with filters,Partially Assigned Only with filters,Completed Only with filters | HSPBR      | Medicare  | A          |
