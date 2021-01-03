#Author: Udayakiran.Lanka
@SmokeSanity
Feature: Smoke and EndToEndTest

  #####################################################   Scenario-1  #############################################################
  @QASmoke
  Scenario Outline: Smoke Test validate finlaize decision
    Given User "<User>" logged into "" application with Services
    Given the "<User>" moving the data from CPW to PM through the service for the client "<Client>" with "Single" DPKey
    Then Create the presenatation profile with given request data client "<Client>" payershort "" lob "" product "" and prority "" with "<User>"
    Then assign multiple dps to created profile
    Then User "<User>" logged into "PM" application
    When user selects "<Client>" from Client drop down list
    Then The CPMorCMD clicks on the "Presentation having DPs"
    And Expand all the items in presenation hierarchy view
    Then Select DPs at "<Level>" level in presentation hierarchy view
    And Capture "<Decision>" decision
    Then Select DPs at "<Level>" level
    And Finalize "<Decision>" Decisions for "<Level>" payershorts
    And User logsout of the "PM" Application

    Examples: 
      | User   | Client             | Level  | Decision |
      | nkumar | UnitedHealth Group | DP ALL | Approve  |

  Scenario Outline: SmokeTest Ability to login to Presentation Manager Application
    Given CPMorCMD launches CPD Application
    Then CPD Login page should be displayed with Username,Password,Application Dropdown,Login button
    And User should be able to enter "<UserName>" and Password
    When User selects Application values "PM,CPW,CPQ" in the Application dropdown then related names should be displayed below the Dropdown
    When User selects Application name "PM"  from the Application dropdown
    And User clicks on "Login" button
    Then The user should view the PresentationManager HomePage
    And User logsout of the "PM" Application

    Examples: 
      | UserName |
      | ulanka   |

  @Sanity
  Scenario Outline: Sanity Test
    Given User "iht_ittest05" logged into "PM" application
    Then The user should view the PresentationManager HomePage
    When user selects "<Client>" from Client drop down list
    And User selects All PayerShorts
    And User selects All  LOBs
    And User clicks on "Apply" button to filter
    And User selects Medical Policy/Topic according to "<MedPolicyTopicType>"
    And user filters by clicking on Apply for Medical Policy/Topic
    Then User must view checkboxes for all the DPCards in the "AvailableOpportunities" deck
    And User should be able to "Select" all DPCards in the "AvailableOpportunities" deck
    And User should be able to "DeSelect" all DPCards in the "AvailableOpportunities" deck
    #WorkToDo count popup is out of scope now ,now commented
    #When User clicks on the WorkToDoCount hyperlink on a DPCard for a "DPKey" in the AvailableOpportunityDeck with PopupView
    #Then Popup window should display DPKey, WorkToDo,Payers and in separate rows to display "LOBs" and Assignments
    #Given CPMorCMD has created a PresentationProfile
    And User logsout of the "PM" Application

    Examples: 
      | UserName | Client           | MedPolicyTopicType |
      | ulanka   | Dean Health Plan | AllMedicalPolicies |
