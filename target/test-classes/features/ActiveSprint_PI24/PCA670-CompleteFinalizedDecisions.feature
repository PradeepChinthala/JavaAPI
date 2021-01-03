#Author: Udayakiran.Lanka
@Regression @PCA670
Feature: PCA-670 -  Complete Finalized Decisions

  ##PCA-2687 :  Disable delete for presentations with decisions
  @PCA2687  @RuleRel
  Scenario Outline: Disable delete for presentations with decisions
 		Given User "<User>" logged into "Ihealth123" application with Services
    Given the "<User>" moving the data from CPW to PM through the service for the client "<Client>" with "Single" DPKey
    Then Create the presenatation profile with given request data client "<Client>" payershort "<Payershort>" lob "<LOB>" product "<Product>" and prority "<Priority>" with "<User>"
    Then assign multiple dps to created profile    
    Given User "<User>" logged into "PM" application
    Then The user should view the PresentationManager HomePage
    When user selects "<Client>" from Client drop down list
    Then The CPMorCMD clicks on the "Presentation having DPs"
    And Apply All Payers and LOBs filters in Presenation profile
    And Expand all the items in presenation hierarchy view
    Then Select DPs at "<Level>" level in presentation hierarchy view
    And Capture "<Decision>" decision
    Then The "Delete" icon should be "NotVisible" for the Presentation on the ProfileTab
    And User logsout of the "PM" Application

    Examples: 
      | User         | Client      | Level | Decision |
      |ulanka | UnitedHealth Group | DP    | Approve  |


  ##added for testing
  ##PCA-2644 :  Disable assignment for presentations with decisions
  @Demo2644
  Scenario Outline: Disable assignment for presentations with decisions
    #Given the "<User>" moving the data from CPW to PM through the service for the client "<Client>" with "Multiple" DPKey
    Given User "<User>" logged into "PM" application
    Then The user should view the PresentationManager HomePage
    When user selects "<Client>" from Client drop down list
    And User selects  multiple Payershorts "All" from Payershorts FilterSection
    And User selects muliple LOBs "All" from LOB FilterSection
    And User clicks on "Apply" button to filter
    And User selects Medical Policy/Topic according to "AllMedicalPolicies"
    And user filters by clicking on Apply for Medical Policy/Topic
    Given CPMorCMD has created a PresentationProfile
    And User filters the "AvailableDPsDeck" with filter drpdown value "NotStartedOnly" to show related DPs
    And The User have assigned some DPs to the PresentationProfile
    When The CPMorCMD clicks on the "PresentationProfile"
    And Apply All Payers and LOBs filters in Presenation profile
    And Expand all the items in presenation hierarchy view
    Then Select DPs at "<Level>" level in presentation hierarchy view
    And Capture "<Decision>" decision
    And click on "AVAILABLE OPPURTUNITIES" in filter drawer section
    And User filters the "AvailableDPsDeck" with filter drpdown value "NotStartedOnly" to show related DPs
    And User selects the assign icon at the "HeaderLevel"
    And The Presentation should be "NotAvailable" in the OpportunitiesAssignment Popup
    And The User clicks on Assign Popup "Cancel" button
    And User reloads the ApplicationPage
    And User filters the "AvailableDPsDeck" with filter drpdown value "NotStartedOnly" to show related DPs
    And User selects the assign icon at the "MedicalPolicyLevel"
    And The Presentation should be "NotAvailable" in the OpportunitiesAssignment Popup
    And The User clicks on Assign Popup "Cancel" button
    And User selects the assign icon at the "TopicLevel"
    And The Presentation should be "NotAvailable" in the OpportunitiesAssignment Popup
    And The User clicks on Assign Popup "Cancel" button
    And User selects the assign icon at the "DPLevel"
    And The Presentation should be "NotAvailable" in the OpportunitiesAssignment Popup
    And The User clicks on Assign Popup "Cancel" button
    And User logsout of the "PM" Application

    Examples: 
      | User   | Client             | Level | Decision |
      | ulanka | UnitedHealth Group | DP    | Approve  |

  @PCA-2991
  Scenario Outline: DP count and Savings when Opportunities are assigned to Presentaion
  Given User "<User>" logged into "Ihealth123" application with Services
    Given the "<User>" moving the data from CPW to PM through the service for the client "<Client>" with "Single" DPKey
    And User "<User>" logged into "PM" application
    Then The user should view the PresentationManager HomePage
    When user selects "<Client>" from Client drop down list
    And User selects  multiple Payershorts "All" from Payershorts FilterSection
    And User selects muliple LOBs "All" from LOB FilterSection
    And User clicks on "Apply" button to filter
    And User selects Medical Policy/Topic according to "AllMedicalPolicies"
    And user filters by clicking on Apply for Medical Policy/Topic
    Given CPMorCMD has created a PresentationProfile
    And User filters the "AvailableDPsDeck" with filter drpdown value "NotStartedOnly" to show related DPs
    Then Navigate to the "CapturedDPKey"  in the "AvaialbleOpportunityView"
   #When User get present "savings" summary from cpw opportunities "SavingsBeforeMaping"
    Given User assigns the "CapturedDPkey" to the "Presentation"  at level "DPKeyLevel"
   #When User get present "savings" summary from cpw opportunities "SavingsAfterMapping"
    Then User should see "savingValue" difference from "SavingsBeforeMaping" and "SavingsAfterMapping"

    Examples: 
      | User         | Client       |
      | iht_ittest05 | Humana, Inc. |
