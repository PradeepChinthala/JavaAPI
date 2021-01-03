#Author: chaitanya kumar natuva
@Regression @PCA793
Feature: PCA793
  ######################################################   Scenario-1  #############################################################
  @PCA793_1
  Scenario Outline: PCA793_1 Validate the notes section in presentation deck after assigining for captured DP from CPW
    Given the "<User>" is logged into the CPW application
    When user click on client "<Client>" with release "<Release>" in the Opportunity Dashboard
    And user select Medical Policy from the policy selection through MongoDB
    When user selects "" under "Latest Client Decision" as "CHECK" and ""
    And user selects "" under "Prior Disposition" as "CHECK" and ""
    And user selects "" under "Savings Status" as "UNCHECK" and ""
    And user selects "Opportunity" under "Savings Status" as "CHECK" and "AWB"
    Then validate the capture and update disposition functionality at "DP level" data for "Single DPKey",Disposition as "Present",update disposition "" with MongoDB
    Then Logout CPW application
    Given User "<User>" logged into "" application with Services
    Then Create the presenatation profile with given request data client "<Client>" payershort "" lob "" product "" and prority "" with "<User>"
    Then assign multiple dps to created profile
    When User "<User>" logged into "PM" application
    When user selects "<Client>" from Client drop down list
    Then The CPMorCMD clicks on the "Presentation having DPs"
    And Apply All Payers and LOBs filters in Presenation profile
    And Expand all the items in presenation hierarchy view
    Then validate the notes section in presentationdeck with DB

    Examples: 
      | User   | Release  | Client         |
      | nkumar | NOV 2019 | Aetna Medicaid |

  ######################################################   Scenario-2  #############################################################
  @PCA793_2  @ReRun
  Scenario Outline: PCA793_2 Validate the notes section is empty in presentation deck after assigining for captured DP from CPW
    Given User "<User>" logged into "" application with Services
    Given the "<User>" moving the data from CPW to PM through the service for the client "<Client>" with "<DPKey>" DPKey and Priority as "<Priority>"
    Then Create the presenatation profile with given request data client "<Client>" payershort "" lob "" product "" and prority "" with "<User>"
    Then assign multiple dps to created profile
    When User "<User>" logged into "PM" application
    When user selects "<Client>" from Client drop down list
    Then The CPMorCMD clicks on the "Presentation having DPs"
    And Apply All Payers and LOBs filters in Presenation profile
    And Expand all the items in presenation hierarchy view
    Then validate the notes section in presentationdeck with DB

    Examples: 
      | User   | Client         | DPKey  | Priority        |
      | nkumar | Aetna Medicaid | Single | High,Medium,Low |
