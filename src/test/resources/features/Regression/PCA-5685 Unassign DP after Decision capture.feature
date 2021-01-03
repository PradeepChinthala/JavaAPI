#Author: chaitanya kumar natuva
@Regression @PCA5685
Feature: PCA5685

  ######################################################   Scenario-1  #############################################################
  @PCA5685_1   @ReRun
  Scenario Outline: PCA5685_1 Un-assign DP after Decision capture no decision
    Given User "<User>" logged into "" application with Services
    Then the "<User>" moving the data from CPW to PM through the service for the client "<Client>" with "Single" DPKey
    Then Create the presenatation profile with given request data client "<Client>" payershort "" lob "" product "" and prority "" with "<User>"
    Then assign multiple dps to created profile
    Given User "<User>" logged into "PM" application
    When user selects "<Client>" from Client drop down list
    And validate the "Unassign" functionaity of assigned "First-DPLOBLevel" DPkey for the created presentation

    Examples: 
      | User   | Client    |
      #| nkumar | WellPoint |
      | nkumar | UnitedHealth Group|

  #####################################################   Scenario-2  #############################################################
  @PCA5685_2
  Scenario Outline: PCA5685_2 Unassign DP after Decision capture_partial decision with Test and follow up
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
    And validate the "Unassign" functionaity of assigned "First-DPLOBLevel" DPkey for the created presentation

    Examples: 
      | User   | Client             | Level      | Decision          |
      | nkumar |Aetna Medicaid | DPLOBLevel | Approve Test Only |
  | nkumar |Aetna Medicaid | DPLOBLevel | Approve with Mod Test Only |
  | nkumar | Aetna Medicaid | DPLOBLevel | Follow up                  |
  #####################################################   Scenario-3  #############################################################
  @PCA5685_3
  Scenario Outline: PCA5685_3 Unassign DP after Decision capture_partial decision with prod
    Given User "<User>" logged into "" application with Services
    Given the "<User>" moving the data from CPW to PM through the service for the client "<Client>" with "Single" DPKey
    Then Create the presenatation profile with given request data client "<Client>" payershort "" lob "" product "" and prority "" with "<User>"
    Then assign multiple dps to created profile
    Then User "<User>" logged into "PM" application
    When user selects "<Client>" from Client drop down list
    Then The CPMorCMD clicks on the "Presentation having DPs"
    And Apply All Payers and LOBs filters in Presenation profile
    And Expand all the items in presenation hierarchy view
    Then Select DPs at "<Level>" level in presentation hierarchy view
    And Capture "<Decision>" decision
    And validate the "Unassign" functionaity of assigned "<Level>" DPkey for the created presentation

    Examples: 
      | User   | Client | Level      | Decision |
      | nkumar | Aetna Medicaid | DPLOBLevel | Approve  |
      | nkumar | Aetna Medicaid | DPLOBLevel | Approve with Mod |
     | nkumar | Aetna Medicaid | DPLOBLevel | Reject           |
     | nkumar | Aetna Medicaid | DPLOBLevel | Defer            |
