#Author: Udayakiran.Lanka
@Regression @BOSE2589 @Regression @RegNov13
Feature: BOSE-2589-Assign Opportunities to a Presentation quickly

  ################################################### Scenario-1 ##################################################
  @BOSE2916_1 @BOSE3086 @BOSE3087 @BOSE3088 @AssignDP @BOSE2589_1
  Scenario Outline: AssignDPCardToPresentationProfile
    Given User "<User>" logged into "" application with Services
    Given the "<User>" moving the data from CPW to PM through the service for the client "<Client>" with "Multiple" DPKey
    Given User "<User>" logged into "PM" application
    Then The user should view the PresentationManager HomePage
    When user selects "<Client>" from Client drop down list
    And User selects  multiple Payershorts "<Payershorts>" from Payershorts FilterSection
    And User selects muliple LOBs "<LOBs>" from LOB FilterSection
    When user selects "All" Claimtypes in filtersection
    And User clicks on "Apply" button to filter
    And User selects Medical Policy/Topic according to "<MedPolicyTopicType>"
    And user filters by clicking on Apply for Medical Policy/Topic
    ##MedicalPolicy Level
    Given CPMorCMD has created Presentations with count "<PresentationsCount>" in  PresentationProfile section with "Values"
    And User filters the "AvailableDPsDeck" with filter drpdown value "NotStartedOnly" to show related DPs
    And User selects the assign icon at the "MedicalPolicyLevel"
    Then Assign pop-up is displayed with "PresentationProfiles"  names
    When The User assigns DPCard to a "Single" PresentationProfiles at the level "MedicalPolicyLevel" and "SaveAssginments"
    And The User clicks on Assign Popup "OK" button
    And The pop-up should be closed
    #Topic Level
    Given CPMorCMD has created Presentations with count "<PresentationsCount>" in  PresentationProfile section with "Values"
    And User filters the "AvailableDPsDeck" with filter drpdown value "NotStartedOnly" to show related DPs
    And User selects the assign icon at the "TopicLevel"
    When The User assigns DPCard to a "Single" PresentationProfiles at the level "TopicLevel"
    And The User clicks on Assign Popup "OK" button
    And The pop-up should be closed
    #DP Level
    Given CPMorCMD has created Presentations with count "<PresentationsCount>" in  PresentationProfile section with "Values"
    And User filters the "AvailableDPsDeck" with filter drpdown value "NotStartedOnly" to show related DPs
    And User selects the assign icon at the "DPLevel"
    When The User assigns DPCard to a "Single" PresentationProfiles at the level "DPLevel"
    And The User clicks on Assign Popup "OK" button
    And The pop-up should be closed
    Then User logsout of the "PM" Application

    Examples: 
      | User   | Client                          | Payershorts | LOBs | MedPolicyTopicType | PresentationsCount | DPAssignmentLevel |
      | ulanka | Blue Cross Blue Shield Michigan | All         | All  | AllMedicalPolicies |                  1 |                   |

  ################################################### Scenario-2 ##################################################
  @BOSE2916_2 @BOSE3089 @BOSE3090 @assignCancel
  Scenario Outline: AssignDPCardToPresentationProfileAssignAndCancel
    Given User "<User>" logged into "" application with Services
    Given the "<User>" moving the data from CPW to PM through the service for the client "<Client>" with "Multiple" DPKey
    Given User "<User>" logged into "PM" application
    Then The user should view the PresentationManager HomePage
    When user selects "<Client>" from Client drop down list
    And User selects  multiple Payershorts "All" from Payershorts FilterSection
    And User selects muliple LOBs "<LOBs>" from LOB FilterSection
    When user selects "All" Claimtypes in filtersection
    And User clicks on "Apply" button to filter
    And User selects Medical Policy/Topic according to "<MedPolicyTopicType>"
    And user filters by clicking on Apply for Medical Policy/Topic
    Given CPMorCMD has created Presentations with count "<PresentationsCount>" in  PresentationProfile section with "Values"
    #MedicalPolicy Level
    And User filters the "AvailableDPsDeck" with filter drpdown value "NotStartedOnly" to show related DPs
    And User selects the assign icon at the "MedicalPolicyLevel"
    Then Assign pop-up is displayed with "PresentationProfiles"  names
    When The User assigns DPCard to a "Single" PresentationProfiles at the level "MedicalPolicyLevel" and "SaveAssginments"
    And The User clicks on Assign Popup "Cancel" button
    And The pop-up should be closed
    Then The DPs should be "NotAssignedTo" the selected PresentationProfiles "PresentationProfiles"
    When User selects the assign icon at the "MedicalPolicyLevel"
    And The User clicks on Assign Popup "Cancel" button
    And The pop-up should be closed
    ##Topic Level
    Given CPMorCMD has created Presentations with count "<PresentationsCount>" in  PresentationProfile section with "Values"
    And User filters the "AvailableDPsDeck" with filter drpdown value "NotStartedOnly" to show related DPs
    And User selects the assign icon at the "TopicLevel"
    When The User assigns DPCard to a "Single" PresentationProfiles at the level "TopicLevel"
    And The User clicks on Assign Popup "Cancel" button
    And The pop-up should be closed
    Then The DPs should be "NotAssignedTo" the selected PresentationProfiles "PresentationProfiles"
    When User selects the assign icon at the "TopicLevel"
    And The User clicks on Assign Popup "Cancel" button
    And The pop-up should be closed
    #DP Level
    Given CPMorCMD has created Presentations with count "<PresentationsCount>" in  PresentationProfile section with "Values"
    And User filters the "AvailableDPsDeck" with filter drpdown value "NotStartedOnly" to show related DPs
    And User selects the assign icon at the "DPLevel"
    When The User assigns DPCard to a "Single" PresentationProfiles at the level "DPLevel"
    And The User clicks on Assign Popup "Cancel" button
    And The pop-up should be closed
    Then User logsout of the "PM" Application

    Examples: 
      | User   | Client                          | Payershorts             | LOBs | MedPolicyTopicType | PresentationsCount | DPAssignmentLevel |
      | ulanka | Blue Cross Blue Shield Michigan | BCMIB,BCMIC,BCMIF,BCMII | All  | AllMedicalPolicies |                  1 |                   |
