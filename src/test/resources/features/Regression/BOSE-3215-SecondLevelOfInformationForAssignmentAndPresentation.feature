#Author: Udayakiran.Lanka
@Regression @BOSE3215 @Regression @RegNov13  @May22   @ReRun
Feature: BOSE-3215-SecondLevelOfInformationForAssignmentAndPresentation

  @BOSE-3644 @ExpandCollapse
  Scenario Outline: BOSE-3644 - Expand/Collapse on Available opportunity deck
    Given User "<User>" logged into "PM" application
    Then The user should view the PresentationManager HomePage
    When user selects "<Client>" from Client drop down list
    Then verify "Expand All" functionality
    Then verify "Collapse All" functionality
    And verify "Expand" functionality at Medical Policy Level
    Then verify "Expand All" functionality
    And verify "collapse" functionality at Medical Policy Level
    Then verify "Collapse All" functionality
    And verify "Expand" functionality at Topic Level
    Then verify "Expand All" functionality
    And verify "Collapse" functionality at Topic Level
    And User logsout of the "PM" Application

    Examples: 
      | User   | Client         |
      | ulanka | Aetna Medicaid |

  @BOSE2592 @FlipOnClick @BOSE3407 @BOSE3408 @BOSE3215_2
  Scenario Outline: Assignment State/Flip view- Available Opportunities Deck-SummaryView and  Flip On Click
    Given the "<User>" moving the data from CPW to PM through the service for the client "<Client>" with "Single" DPKey
    Given User "<User>" logged into "PM" application
    Then The user should view the PresentationManager HomePage
    When user selects "<Client>" from Client drop down list
    And User selects  multiple Payershorts "<Payershorts>" from Payershorts FilterSection
    And User selects muliple LOBs "<LOBs>" from LOB FilterSection
    When user selects "All" Claimtypes in filtersection
    And User clicks on "Apply" button to filter
    And User selects Medical Policy/Topic according to "<MedPolicyTopicType>"
    And user filters by clicking on Apply for Medical Policy/Topic
    And User filters the "AvailableDPsDeck" with filter drpdown value "CompletedOnly" to show related DPs
    When User is viewing the opportunity assignments "DPCard" with WorkToDoCount "0"
    Then The "DPCard" should be in state  "AssignedView" State
    And The AssignedView or flipped side of the "DPCard" should display names of the all assigned "Presentations" for the "DPCard"
    Given CPMorCMD has created Presentations with count "<PresentationsCount>" in  PresentationProfile section with "Values"
    And User filters the "AvailableDPsDeck" with filter drpdown value "NotStartedOnly" to show related DPs
    And User selects the assign icon at the "DPLevel"
    And User selects the Presentation for assignment
    And The User clicks on Assign Popup "OK" button
    And User filters the "AvailableDPsDeck" with filter drpdown value "CompletedOnly" to show related DPs
    Then AssignedView of the "DPCard" should display the names of the assigned "Presentations" for the "DPLevel"
    And User logsout of the "PM" Application

    Examples: 
      | User   | Client             | Payershorts | LOBs | MedPolicyTopicType | PresentationsCount |
      | ulanka | UnitedHealth Group | All         | All  | AllMedicalPolicies |                  1 |

  @BOSE2592 @BOSE3407 @FlipFilter @BOSE3215_3
  Scenario Outline: Assignment State/Flip view- Available Opportunities Deck-Change in Filter
    Given the "<User>" moving the data from CPW to PM through the service for the client "<Client>" with "Multiple" DPKey
    Given User "<User>" logged into "PM" application
    Then The user should view the PresentationManager HomePage
    When user selects "<Client>" from Client drop down list
    When User selects  multiple Payershorts "<Payershorts>" from Payershorts FilterSection
    And User selects muliple LOBs "All" from LOB FilterSection
    When user selects "All" Claimtypes in filtersection
    And User clicks on "Apply" button to filter
    When User selects Medical Policy/Topic according to "<MedPolicyTopicType>"
    And user filters by clicking on Apply for Medical Policy/Topic
    And User filters the "AvailableDPsDeck" with filter drpdown value "NotStartedOnly" to show related DPs
    And User retrieves the "DPKey" with required "LOBs"
    And User selects muliple LOBs "Medicaid" from LOB FilterSection
    And User clicks on "Apply" button to filter
    When User selects Medical Policy/Topic according to "<MedPolicyTopicType>"
    And user filters by clicking on Apply for Medical Policy/Topic
    Given CPMorCMD has created Presentations with count "<PresentationsCount>" in  PresentationProfile section with "Values"
    And User filters the "AvailableDPsDeck" with filter drpdown value "NotStartedOnly" to show related DPs
    And User clicks the "Assign" button for the "DPCard" with DP "DPKey"
    When The User assigns DPCard to a "Multiple" PresentationProfiles at the level "DPLevel"
    And The User clicks on Assign Popup "OK" button
    And User filters the "AvailableDPsDeck" with filter drpdown value "CompletedOnly" to show related DPs
    Then The "DPCard" should be in state  "AssignedView" State
    When User selects  multiple Payershorts "<Payershorts>" from Payershorts FilterSection
    And User selects muliple LOBs "All" from LOB FilterSection
    And User clicks on "Apply" button to filter
    When User selects Medical Policy/Topic according to "<MedPolicyTopicType>"
    And user filters by clicking on Apply for Medical Policy/Topic
    And User filters the "AvailableDPsDeck" with filter drpdown value "PartiallyAssignedOnly" to show related DPs
    Then The "DPCard" should be in state  "SummaryView" State
    And User should be able to click on the "DPCard" to return to view "AssignedView"
    Then AssignedView of the "DPCard" should display the names of the assigned "Presentations" for the "DPLevel"
    And User logsout of the "PM" Application

    Examples: 
      | User   | Client             | Payershorts | LOBs | MedPolicyTopicType | PresentationsCount |
      | ulanka | UnitedHealth Group | All         | All  | AllMedicalPolicies |                  1 |

  @BOSE2592 @BOSE3407 @RetainFlip
  Scenario Outline: Assignment State/Flip view- Available Opportunities Deck_Retain of Flip
    Given User "<User>" logged into "PM" application
    Then The user should view the PresentationManager HomePage
    When user selects "<Client>" from Client drop down list
    And User selects  multiple Payershorts "<Payershorts>" from Payershorts FilterSection
    And User selects muliple LOBs "<LOBs>" from LOB FilterSection
    When user selects "All" Claimtypes in filtersection
    And User clicks on "Apply" button to filter
    And User selects Medical Policy/Topic according to "<MedPolicyTopicType>"
    And user filters by clicking on Apply for Medical Policy/Topic
    ## Retain Flip after Logout scenario
    And User filters the "AvailableDPsDeck" with filter drpdown value "CompletedOnly" to show related DPs
    When User is viewing the opportunity assignments "DPCard" with WorkToDoCount "0"
    Then The "DPCard" should be in state  "AssignedView" State
    And User logsout of the "PM" Application
    Given User "<User>" logged into "PM" application
    Then The user should view the PresentationManager HomePage
    When user selects "<Client>" from Client drop down list
    And User selects  multiple Payershorts "<Payershorts>" from Payershorts FilterSection
    And User selects muliple LOBs "<LOBs>" from LOB FilterSection
    And User clicks on "Apply" button to filter
    And User selects Medical Policy/Topic according to "<MedPolicyTopicType>"
    And user filters by clicking on Apply for Medical Policy/Topic
    And User filters the "AvailableDPsDeck" with filter drpdown value "CompletedOnly" to show related DPs
    Then The "DPCard" should be in state  "AssignedView" State
    ## Retain Flip after Client Change scenario
    When user selects "<Client2>" from Client drop down list
    When user selects "<Client>" from Client drop down list
    And User selects  multiple Payershorts "<Payershorts>" from Payershorts FilterSection
    And User selects muliple LOBs "<LOBs>" from LOB FilterSection
    And User clicks on "Apply" button to filter
    And User selects Medical Policy/Topic according to "<MedPolicyTopicType>"
    And user filters by clicking on Apply for Medical Policy/Topic
    And User filters the "AvailableDPsDeck" with filter drpdown value "CompletedOnly" to show related DPs
    Then The "DPCard" should be in state  "AssignedView" State
    And User logsout of the "PM" Application

    Examples: 
      | User   | Client         | Client2            | Payershorts | LOBs | MedPolicyTopicType | PresentationsCount |
      | ulanka | Aetna Medicaid | UnitedHealth Group | All         | All  | AllMedicalPolicies |                  2 |
