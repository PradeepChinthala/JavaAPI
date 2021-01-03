#Author: Udayakiran.Lanka
@Regression @PCA1518
Feature: PCA-1518 -  Notification of eLL Changes to Opportunities

  ##PCA-2377 - Ability to mark a presentation profile as -'Ready for presentation'
  @DemoActive2
  Scenario Outline: Ability to mark a presentation profile as -'Ready for presentation
    Given User "<User>" logged into "" application with Services
    Given the "<User>" moving the data from CPW to PM through the service for the client "<Client>" with "Multiple" DPKey
    Given User "<User>" logged into "PM" application
    Then The user should view the PresentationManager HomePage
    When user selects "<Client>" from Client drop down list
    And User selects  multiple Payershorts "All" from Payershorts FilterSection
    And User selects muliple LOBs "All" from LOB FilterSection
    And User clicks on "Apply" button to filter
    And User selects Medical Policy/Topic according to "AllMedicalPolicies"
    And user filters by clicking on Apply for Medical Policy/Topic
    Given CPMorCMD has created a PresentationProfile
    When The CPMorCMD clicks on the "PresentationProfile"
    Then The checkbox for "ReadyforPresentation" should be "Disabled"
    And click on "AVAILABLE OPPURTUNITIES" in filter drawer section
    And User filters the "AvailableDPsDeck" with filter drpdown value "NotStartedOnly" to show related DPs
    And The User have assigned some DPs to the PresentationProfile
    When The CPMorCMD clicks on the "PresentationProfile"
    And The User clicks on the "ReadyforPresentation"
    Then The system should mark the "PresentationProfile" status as "Ready for Presentation" in DB
    And The ProfilePage should have the Red border to indicate lockedview
    When User clicks on other "PresentationProfile"
    When The CPMorCMD clicks on the "PresentationProfile"
    ##Following is the validation for the Check box should be retained until "<CPM User>" manually unchecks
    Then The checkbox for "ReadyforPresentation" should be "EnabledStatusRetained"
    And User logsout of the "PM" Application

    Examples: 
      | User   | Client             | Level | Decision |
      | ulanka | UnitedHealth Group | DP    | Approve  |
