#Author: Sravanthi.devarashetty
@Regression @BOSE2909
Feature: BOSE-2909 Presentation Manager Enhancements

  #Background:
  #Given User "iht_ittest05" logged into "PM" application
  ###################################### SCENARIO-1  ###########################################################
  @BOSE-3392 @BOSE-3709Test
  Scenario Outline: Scenario-1 BOSE-3392_1 Ability to close presentation deck1
    Given User "iht_ittest05" logged into "PM" application
    When user selects "<Client>" from Client drop down list
    Given CPMorCMD has created a PresentationProfile
    When The CPMorCMD clicks on the "PresentationProfile"
    Then verify the header of "PresentationProfile"
    And click on close icon of "PresentationProfile"
    #And  The "PresentationProfile" container should be "Collapsed"
    #Then  "OpportunityDeck" section should be "Expanded"
    And User logsout of the "PM" Application

    Examples: 
      | Client         |
      | Aetna Medicaid |

  ###################################### SCENARIO-2  ###########################################################
  ##Working in Dev2 branch
  @Disable @BOSE2909_2    @ReRun
  Scenario Outline: BOSE-3392_2 Ability to view "Ok" button disable on Assign pop-up from available DP deck
    Given User "iht_ittest05" logged into "PM" application
    When user selects "<Client>" from Client drop down list
    And User selects All PayerShorts
    And User selects All  LOBs
    When user selects "All" Claimtypes in filtersection
    And User clicks on "Apply" button to filter
    And User selects Medical Policy/Topic according to "<MedPolicyTopicType>"
    And user filters by clicking on Apply for Medical Policy/Topic
    And User selects the assign icon at the "MedicalPolicyLevel"
    Then The assign popup "Okay" button should be "disabled"
    And The User clicks on Assign Popup "Cancel" button
    And User selects the assign icon at the "TopicLevel"
    Then The assign popup "Okay" button should be "disabled"
    And The User clicks on Assign Popup "Cancel" button
    And User selects the assign icon at the "DPLevel"
    Then The assign popup "Okay" button should be "disabled"
    And The User clicks on Assign Popup "Cancel" button
    And User logsout of the "PM" Application

    Examples: 
      | Client         | MedPolicyTopicType |
      #|Aetna Medicaid|    AllMedicalPolicies    |
      #|Aetna Medicaid	    |     AllMedicalPolicies    |
      | Aetna Medicaid | AllMedicalPolicies |

  ###################################### SCENARIO-3  ###########################################################
  ##Working in Dev2 branch
  @BOSE-3392-3 @BOSE-3817 @BOSE2909_3   @ReRun
  Scenario Outline: BOSE-3392_3 Ability to view "Ok" button disable on Assign pop-up from available DP deck
    Given User "<User>" logged into "" application with Services
    Given the "<User>" moving the data from CPW to PM through the service for the client "<Client>" with "Single" DPKey
    Given User "ulanka" logged into "PM" application
    Then The user should view the PresentationManager HomePage
    When user selects "<Client>" from Client drop down list
    And User selects All PayerShorts
    And User selects All  LOBs
    When user selects "All" Claimtypes in filtersection
    And User clicks on "Apply" button to filter
    And User selects Medical Policy/Topic according to "<MedPolicyTopicType>"
    And user filters by clicking on Apply for Medical Policy/Topic
    And User filters the "AvailableDPsDeck" with filter drpdown value "NotStartedOnly" to show related DPs
    Then DPCard ageing should display as Captured
    Given CPMorCMD has created a PresentationProfile
    And User selects the assign icon at the "MedicalPolicyLevel"
    And User selects the Presentation for assignment
    Then The assign popup "Okay" button should be "enabled"
    And The User clicks on Assign Popup "Cancel" button
    And User selects the assign icon at the "TopicLevel"
    And User selects the Presentation for assignment
    Then The assign popup "Okay" button should be "enabled"
    And The User clicks on Assign Popup "Cancel" button
    And User selects the assign icon at the "DPLevel"
    And User selects the Presentation for assignment
    Then The assign popup "Okay" button should be "enabled"
    And The User clicks on Assign Popup "Cancel" button
    Given User has "deleted" a PresentationProfile
    And User selects the assign icon at the "MedicalPolicyLevel"
    Then The assign popup "Okay" button should be "disabled"
    And The User clicks on Assign Popup "Cancel" button
    And User selects the assign icon at the "TopicLevel"
    Then The assign popup "Okay" button should be "disabled"
    And The User clicks on Assign Popup "Cancel" button
    And User selects the assign icon at the "DPLevel"
    Then The assign popup "Okay" button should be "disabled"
    And The User clicks on Assign Popup "Cancel" button
    And User logsout of the "PM" Application

    Examples: 
      | Client         | User   | MedPolicyTopicType |
      | Aetna Medicaid | ulanka | AllMedicalPolicies |
