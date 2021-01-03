
#Capture Disposition Functionality is working in CPW as of now

#RamaKrishna Bodlapati
 @F2562
Feature: F2562,Capturing Disposition for latestclinetdecision data

  #####################################################   Scenario-1  #############################################################
  @F2562_1 
  Scenario Outline: F2562_1 Available Opportunity DP Cards-  Related LOB Decisions "<latestClientDecision>"
    Given the "<User>" is logged into the CPW application
    When user click on client "<Client>" with release "<Release>" in the Opportunity Dashboard
    And user retrieve "<latestClientDecision>","CDM_DECISION_DATA" opportunity data from MongoDB.
    And user select Medical Policy from the policy selection through MongoDB for the given latest client decision "<latestClientDecision>"
    Then validate the capture and update disposition functionality at "DP level" data for "Single DPKey",Disposition as "<Disposition>",update disposition "<UpdateDisposition>" with MongoDB
    Then Logout CPW application
    Given User "<User>" logged into "PM" application
    When user selects "<Client>" from Client drop down list
    And User selects  multiple Payershorts "All" from Payershorts FilterSection
    And User selects muliple LOBs "All" from LOB FilterSection
    When user selects "All" Claimtypes in filtersection
    And User clicks on "Apply" button to filter
    And User selects Medical Policy/Topic according to "Captured Topic"
    And user filters by clicking on Apply for Medical Policy/Topic
    Then validate client decision "<latestClientDecision>" on DP card in avaliable DP desk.

    Examples: 
      | User   | Release  | Client    | Disposition | latestClientDecision |
      | nkumar | NOV 2019 | Aetna Medicaid | Present     | Approve Library      |

  #####################################################   Scenario-2  #############################################################
  #No data in  Db for 'Approve with Modification' to captured Db in CPW
  @F2562_2
  Scenario Outline: F2562 Available Opportunity DP Cards-  Related LOB Decisions "<latestClientDecision>"
  Given the "<User>" is logged into the CPW application
  When user click on client "<Client>" with release "<Release>" in the Opportunity Dashboard
  And user retrieve "<latestClientDecision>","CDM_DECISION_DATA" opportunity data from MongoDB.
  And user select Medical Policy from the policy selection through MongoDB for the given latest client decision "<latestClientDecision>"
  Then validate the capture and update disposition functionality at "DP level" data for "Single DPKey",Disposition as "<Disposition>",update disposition "<UpdateDisposition>" with MongoDB
  Then Logout CPW application
  Given User "<User>" logged into "PM" application
  When user selects "<Client>" from Client drop down list
  And User selects  multiple Payershorts "All" from Payershorts FilterSection
  And User selects muliple LOBs "All" from LOB FilterSection
  And User clicks on "Apply" button to filter
  And User selects Medical Policy/Topic according to "Captured Topic"
  And user filters by clicking on Apply for Medical Policy/Topic
  Then validate client decision "<latestClientDecision>" on DP card in avaliable DP desk.
  
  Examples:
  | User         | Release  | Client                   | Disposition | latestClientDecision      |
  | iht_ittest05 | MAY 2019 | Intermountain Healthcare | Present     | Approve with Modification |
  #####################################################   Scenario-3  #############################################################
  #No data in  Db for 'Approve with Modification' to captured Db in CPW
  @F2562_3
  Scenario Outline: F2562_Available Opportunity DP Cards-  Related LOB Decisions "<latestClientDecision>"
  Given the "<User>" is logged into the CPW application
  When user click on client "<Client>" with release "<Release>" in the Opportunity Dashboard
  And user retrieve "<latestClientDecision>","CDM_DECISION_DATA" opportunity data from MongoDB.
  And user select Medical Policy from the policy selection through MongoDB for the given latest client decision "<latestClientDecision>"
  Then validate the capture and update disposition functionality at "DP level" data for "Single DPKey",Disposition as "<Disposition>",update disposition "<UpdateDisposition>" with MongoDB
  Then Logout CPW application
  Given User "<User>" logged into "PM" application
  When user selects "<Client>" from Client drop down list
  And User selects  multiple Payershorts "All" from Payershorts FilterSection
  And User selects muliple LOBs "All" from LOB FilterSection
  And User clicks on "Apply" button to filter
  And User selects Medical Policy/Topic according to "Captured Topic"
  And user filters by clicking on Apply for Medical Policy/Topic
  Then validate client decision "<latestClientDecision>" on DP card in avaliable DP desk.
  
  Examples:
  | User         | Release  | Client | Disposition | latestClientDecision |
  | iht_ittest03 | MAY 2019 | UnitedHealth Group  | Present     | Approve              |
  #####################################################   Scenario-4  #############################################################
  @F2562_4
  Scenario Outline: F2562_Available Opportunity DP Cards Related LOB Decisions "Reject"
    Given the "<User>" is logged into the CPW application
    When user click on client "<Client>" with release "<Release>" in the Opportunity Dashboard
    And user retrieve "<latestClientDecision>","CDM_DECISION_DATA" opportunity data from MongoDB.
    And user select Medical Policy from the policy selection through MongoDB for the given latest client decision "<latestClientDecision>"
    Then validate the capture and update disposition functionality at "DP level" data for "Single DPKey",Disposition as "<Disposition>",update disposition "<UpdateDisposition>" with MongoDB
    Then Logout CPW application
    Given User "<User>" logged into "PM" application
    When user selects "<Client>" from Client drop down list
    And User selects  multiple Payershorts "All" from Payershorts FilterSection
    And User selects muliple LOBs "All" from LOB FilterSection
    When user selects "All" Claimtypes in filtersection
    And User clicks on "Apply" button to filter
    And User selects Medical Policy/Topic according to "Captured Topic"
    And user filters by clicking on Apply for Medical Policy/Topic
    Then validate client decision "<latestClientDecision>" on DP card in avaliable DP desk.

    Examples: 
      | User         | Release  | Client  | Disposition | latestClientDecision |
      | iht_ittest05 | NOV 2019 | Aetna Medicaid | Present     | Reject               |

  #####################################################   Scenario-5  #############################################################
  @F2562_5
  Scenario Outline: F2562_Available Opportunity DP Cards-  Related LOB Decisions "No Decision"
    Given the "<User>" is logged into the CPW application
    When user click on client "<Client>" with release "<Release>" in the Opportunity Dashboard
    And user retrieve "<latestClientDecision>","CDM_DECISION_DATA" opportunity data from MongoDB.
    And user select Medical Policy from the policy selection through MongoDB for the given latest client decision "<latestClientDecision>"
    Then validate the capture and update disposition functionality at "DP level" data for "Single DPKey",Disposition as "<Disposition>",update disposition "<UpdateDisposition>" with MongoDB
    Then Logout CPW application
    Given User "<User>" logged into "PM" application
    When user selects "<Client>" from Client drop down list
    And User selects  multiple Payershorts "All" from Payershorts FilterSection
    And User selects muliple LOBs "All" from LOB FilterSection
    When user selects "All" Claimtypes in filtersection
    And User clicks on "Apply" button to filter
    And User selects Medical Policy/Topic according to "Captured Topic"
    And user filters by clicking on Apply for Medical Policy/Topic
    Then validate client decision "<latestClientDecision>" on DP card in avaliable DP desk.

    Examples: 
      | User         | Release  | Client            | Disposition | latestClientDecision |
      | iht_ittest05 | NOV 2019 | Aetna Medicaid | Present     | No Decision          |

  #####################################################   Scenario-6  #############################################################
  @F2562_6
  Scenario Outline: F2562_Available Opportunity DP Cards-  Related LOB Decisions "Suppress"
    Given the "<User>" is logged into the CPW application
    When user click on client "<Client>" with release "<Release>" in the Opportunity Dashboard
    And user retrieve "<latestClientDecision>","CDM_DECISION_DATA" opportunity data from MongoDB.
    And user select Medical Policy from the policy selection through MongoDB for the given latest client decision "<latestClientDecision>"
    Then validate the capture and update disposition functionality at "DP level" data for "Single DPKey",Disposition as "<Disposition>",update disposition "<UpdateDisposition>" with MongoDB
    Then Logout CPW application
    Given User "<User>" logged into "PM" application
    When user selects "<Client>" from Client drop down list
    And User selects  multiple Payershorts "All" from Payershorts FilterSection
    And User selects muliple LOBs "All" from LOB FilterSection
    When user selects "All" Claimtypes in filtersection
    And User clicks on "Apply" button to filter
    And User selects Medical Policy/Topic according to "Captured Topic"
    And user filters by clicking on Apply for Medical Policy/Topic
    Then validate client decision "<latestClientDecision>" on DP card in avaliable DP desk.

    Examples: 
      | User         | Release  | Client  | Disposition | latestClientDecision |
      | iht_ittest03 | NOV 2019 | Aetna Medicaid | Present     | Suppress             |
