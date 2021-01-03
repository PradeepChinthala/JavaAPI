 @Regression @ReadyforPresentation
Feature: ReadyforPresentation
 
 
  #####################################################   Scenario-1  #############################################################
  @ReadyforPresentation @RFP_1
  Scenario Outline: PCA_709 Validate the Ready for Presentattion functionality
    Given User "<User>" logged into "" application with Services
    Given the "<User>" moving the data from CPW to PM through the service for the client "<Client>" with "Single" DPKey
    Then Create the presenatation profile with given request data client "<Client>" payershort "" lob "" product "" and prority "" with "<User>"
    Then assign multiple dps to created profile
    Then User "<User>" logged into "PM" application
    When user selects "<Client>" from Client drop down list
    Then The CPMorCMD clicks on the "Presentation having DPs"
    And Apply All Payers and LOBs filters in Presenation profile
    And verify "Ready For Presentation" validation for "UnAssign"
    And User logsout of the "PM" Application

    Examples: 
      | User   | Client    | Level        | Decision |
      | nkumar | Aetna Medicaid | DP ALL       | Approve  |
      
  @ReadyforPresentation
  Scenario Outline: PCA_709 Validate the Ready for Presentation Re-Assign functionality
    Given User "<User>" logged into "" application with Services
    Given the "<User>" moving the data from CPW to PM through the service for the client "<Client>" with "Single" DPKey
    Then Create the presenatation profile with given request data client "<Client>" payershort "" lob "" product "" and prority "" with "<User>"
    Then assign multiple dps to created profile
    Then User "<User>" logged into "PM" application
    When user selects "<Client>" from Client drop down list
    Then The CPMorCMD clicks on the "Presentation having DPs"
    And Apply All Payers and LOBs filters in Presenation profile
    And verify "Ready For Presentation" validation for "Assign"
    And User logsout of the "PM" Application
    Given User "<User>" logged into "" application with Services
    Given the "<User>" moving the data from CPW to PM through the service for the client "<Client>" with "Single" DPKey
    Then Create the presenatation profile with given request data client "<Client>" payershort "" lob "" product "" and prority "" with "<User>"
    Then assign multiple dps to created profile 
    Then User "<User>" logged into "PM" application  
    Then The CPMorCMD clicks on the "Presentation having DPs"
    And Apply All Payers and LOBs filters in Presenation profile
    And Expand all the items in presenation hierarchy view
    Then Select DPs at "<Level>" level in presentation hierarchy view
    And Capture "<Decision>" decision   
    And User logsout of the "PM" Application

    Examples: 
      | User   | Client    | Level        | Decision       |
      | nkumar | WellPoint | DP ALL       | RE-ASSIGN_READY|


