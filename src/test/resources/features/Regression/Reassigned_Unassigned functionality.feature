#Author: chaitanya kumar natuva
@Regression @PCA1682 @Reassign_Unassign
Feature: ReAssign and UnAssign

  ######################################################   Scenario-1  #############################################################
  @PCA_1682_1
  Scenario Outline: PCA_1682_1 Validation of DP decision view when the DP is reassigned(MP/Topic is having only 1 DP)
    Given User "<User>" logged into "" application with Services
    Given the "<User>" moving the data from CPW to PM through the service for the client "<Client>" with "Single" DPKey
    Then Create the presenatation profile with given request data client "<Client>" payershort "" lob "" product "" and prority "" with "<User>"
    Then assign multiple dps to created profile
    Given User "<User>" logged into "PM" application
    When user selects "<Client>" from Client drop down list
    And User selects  multiple Payershorts "All" from Payershorts FilterSection
    And User selects muliple LOBs "All" from LOB FilterSection
    When user selects "All" Claimtypes in filtersection
    And User clicks on "Apply" button to filter
    And validate the "Re-Assign" functionaity of assigned "First-DPLevel" DPkey for the created presentation

    Examples: 
      | User   | Client |
      | nkumar | Humana |

  ######################################################   Scenario-2  #############################################################
  @PCA_1682_2 @Sanity
  Scenario Outline: PCA_1682_2 Validation of DP decision view when the DP is un-Assigned(MP/Topic is having only 1 DP)
    Given User "<User>" logged into "" application with Services
    Then the "<User>" moving the data from CPW to PM through the service for the client "<Client>" with "Single" DPKey
    Then Create the presenatation profile with given request data client "<Client>" payershort "" lob "" product "" and prority "" with "<User>"
    Then assign multiple dps to created profile
    Given User "<User>" logged into "PM" application
    When user selects "<Client>" from Client drop down list
    And User selects  multiple Payershorts "All" from Payershorts FilterSection
    And User selects muliple LOBs "All" from LOB FilterSection
    When user selects "All" Claimtypes in filtersection
    And User clicks on "Apply" button to filter
    And validate the "Unassign" functionaity of assigned "First-DPLevel" DPkey for the created presentation

    Examples: 
      | User   | Client         |
      | nkumar | Aetna Medicaid |

  ######################################################   Scenario-3  #############################################################
  @PCA_1682_3
  Scenario Outline: PCA_1682_3 Validation of DP decision view when the DP is reassigned(MP/Topic is having only 1 DP) at LOB level
    Given User "<User>" logged into "" application with Services
    Then the "<User>" moving the data from CPW to PM through the service for the client "<Client>" with "Single-Multiple LOB" DPKey
    Then Create the presenatation profile with given request data client "<Client>" payershort "" lob "" product "" and prority "" with "<User>"
    Then assign multiple dps to created profile
    Given User "<User>" logged into "PM" application
    When user selects "<Client>" from Client drop down list
    And User selects  multiple Payershorts "All" from Payershorts FilterSection
    And User selects muliple LOBs "All" from LOB FilterSection
    When user selects "All" Claimtypes in filtersection
    And User clicks on "Apply" button to filter
    And validate the "Re-Assign" functionaity of assigned "First-DPLOBLevel" DPkey for the created presentation

    Examples: 
      | User   | Client |
      | nkumar | Cigna  |

  ######################################################   Scenario-4  #############################################################
  @PCA_1682_4
  Scenario Outline: PCA_1682_4 Validation of DP decision view when the DP is un-Assigned(MP/Topic is having only 1 DP) at LOB level
    Given User "<User>" logged into "" application with Services
    Then the "<User>" moving the data from CPW to PM through the service for the client "<Client>" with "Single-Multiple LOB" DPKey
    Then Create the presenatation profile with given request data client "<Client>" payershort "" lob "" product "" and prority "" with "<User>"
    Then assign multiple dps to created profile
    Given User "<User>" logged into "PM" application
    When user selects "<Client>" from Client drop down list
    And User selects  multiple Payershorts "All" from Payershorts FilterSection
    And User selects muliple LOBs "All" from LOB FilterSection
    When user selects "All" Claimtypes in filtersection
    And User clicks on "Apply" button to filter
    And validate the "Unassign" functionaity of assigned "First-DPLOBLevel" DPkey for the created presentation

    Examples: 
      | User   | Client         |
      | nkumar | Aetna Medicaid |

  ######################################################   Scenario-5  #############################################################
  @PCA_1682_5
  Scenario Outline: PCA_1682_5 Validation of DP decision view when the last DP is reassigned(MP/Topic is having Multiple DPs)
    Given User "<User>" logged into "" application with Services
    Then the "<User>" moving the data from CPW to PM through the service for the client "<Client>" with "Multiple" DPKey
    Then Create the presenatation profile with given request data client "<Client>" payershort "" lob "" product "" and prority "" with "<User>"
    Then assign multiple dps to created profile
    Given User "<User>" logged into "PM" application
    When user selects "<Client>" from Client drop down list
    And User selects  multiple Payershorts "All" from Payershorts FilterSection
    And User selects muliple LOBs "All" from LOB FilterSection
    When user selects "All" Claimtypes in filtersection
    And User clicks on "Apply" button to filter
    And validate the "Re-Assign" functionaity of assigned "Last-DPLevel" DPkey for the created presentation

    Examples: 
      | User   | Client             |
      | nkumar | UnitedHealth Group |

  ######################################################   Scenario-6  #############################################################
  @PCA_1682_6
  Scenario Outline: PCA_1682_6 Validation of DP decision view when the last DP is Un-Assigned(MP/Topic is having Multiple DPs)
    Given User "<User>" logged into "Ihealth123" application with Services
    Then the "<User>" moving the data from CPW to PM through the service for the client "<Client>" with "Multiple" DPKey
    Then Create the presenatation profile with given request data client "<Client>" payershort "" lob "" product "" and prority "" with "<User>"
    Then assign multiple dps to created profile
    Given User "<User>" logged into "PM" application
    When user selects "<Client>" from Client drop down list
    And User selects  multiple Payershorts "All" from Payershorts FilterSection
    And User selects muliple LOBs "All" from LOB FilterSection
    When user selects "All" Claimtypes in filtersection
    And User clicks on "Apply" button to filter
    And validate the "Unassign" functionaity of assigned "Last-DPLevel" DPkey for the created presentation

    Examples: 
      | User   | Client |
      | nkumar | Molina |

  ######################################################   Scenario-7  #############################################################
  #Sravanthi
  @PCA723_1 @Reunassign_7
  Scenario Outline: PCA723_1 - Ability to Un-assign from Presentation Deck_all opportunities
    Given User "<User>" logged into "" application with Services
    Then the "<User>" moving the data from CPW to PM through the service for the client "<Client>" with "Single" DPKey
    Then Create the presenatation profile with given request data client "<Client>" payershort "" lob "" product "" and prority "" with "<User>"
    Then assign multiple dps to created profile
    Given User "<User>" logged into "PM" application
    When user selects "<Client>" from Client drop down list
    And User selects  multiple Payershorts "All" from Payershorts FilterSection
    And User selects muliple LOBs "All" from LOB FilterSection
    When user selects "All" Claimtypes in filtersection
    And User clicks on "Apply" button to filter
    And validate the "Unassign" functionaity of assigned "First-DPLevel" DPkey for the created presentation
    Then validate profile name is removed from DP card after unassign
    And User logsout of the "PM" Application

    Examples: 
      | User         | Client         |
      | iht_ittest09 | Aetna Medicaid |

  ######################################################   Scenario-8  #############################################################
  #Sravanthi
  @PCA-723_2 @Reunassign_8
  Scenario Outline: PCA723_2 - Ability to Un-assign from Presentation Deck_single PPS
    Given User "<User>" logged into "" application with Services
    Then the "<User>" moving the data from CPW to PM through the service for the client "<Client>" with "Single-Multiple LOB" DPKey
    Then Create the presenatation profile with given request data client "<Client>" payershort "" lob "" product "" and prority "" with "<User>"
    Then assign multiple dps to created profile
    Given User "<User>" logged into "PM" application
    When user selects "<Client>" from Client drop down list
    And User selects  multiple Payershorts "All" from Payershorts FilterSection
    And User selects muliple LOBs "All" from LOB FilterSection
    When user selects "All" Claimtypes in filtersection
    And User clicks on "Apply" button to filter
    And validate the "Unassign" functionaity of assigned "First-DPLOBLevel" DPkey for the created presentation
    Then validate profile name is removed from DP card after unassign
    And User logsout of the "PM" Application

    Examples: 
      | User         | Client             |
      | iht_ittest09 | UnitedHealth Group |

  #####################################################   Scenario-9  #############################################################
  #Ravitheja
  @PCA-982 @ReAssignPresView @Reunassign_9
  Scenario Outline: PCA982_1 Re-Assign the Oppurtunities to other presentation profile in presentation view at "<Level>"
    Given User "<User>" logged into "" application with Services
    Given the "<User>" moving the data from CPW to PM through the service for the client "<Client>" with "Single" DPKey
    Then Create the presenatation profile with given request data client "<Client>" payershort "" lob "" product "" and prority "" with "<User>"
    Then assign multiple dps to created profile
    Then User "<User>" logged into "PM" application
    When user selects "<Client>" from Client drop down list
    And User selects  multiple Payershorts "All" from Payershorts FilterSection
    And User selects muliple LOBs "All" from LOB FilterSection
    When user selects "All" Claimtypes in filtersection
    And User clicks on "Apply" button to filter
    Then The CPMorCMD clicks on the "Presentation having DPs"
    And Apply All Payers and LOBs filters in Presenation profile
    And Expand all the items in presenation hierarchy view
    Then Select DPs at "<Level>" level in presentation hierarchy view
    And Assinee profile should not be displayed under Re-assignee list
    And Capture "Re-Assign" decision
    Then filter the captured policies for "<Level>"
    And Click on "Expand All"
    And Verify Reassinged Presentation in oppurtunity Available Deck
    And User logsout of the "PM" Application

    Examples: 
      | User         | Client         | Level          |
      | iht_ittest03 | Aetna Medicaid | DP             |
      | iht_ittest03 | Aetna Medicaid | Topic          |
      | iht_ittest03 | Aetna Medicaid | Medical Policy |

  #####################################################   Scenario-10  #############################################################
  @PCA-982 @ReAssingPayerLOBView @Reunassign_10
  Scenario Outline: PCA982_2 Re-Assign the Oppurtunities to other presentation profile in Payer/LOB Filter view
    Given User "<User>" logged into "" application with Services
    Given the "<User>" moving the data from CPW to PM through the service for the client "<Client>" with "Single" DPKey
    Then Create the presenatation profile with given request data client "<Client>" payershort "" lob "" product "" and prority "" with "<User>"
    Then assign multiple dps to created profile
    Then User "<User>" logged into "PM" application
    When user selects "<Client>" from Client drop down list
    And User selects  multiple Payershorts "All" from Payershorts FilterSection
    And User selects muliple LOBs "All" from LOB FilterSection
    When user selects "All" Claimtypes in filtersection
    And User clicks on "Apply" button to filter
    Then The CPMorCMD clicks on the "Presentation having DPs"
    And Apply All Payers and LOBs filters in Presenation profile
    And Expand all the items in presenation hierarchy view
    Then Select DPs at "<Level>" level in Payer/LOB Filter view
    And Assinee profile should not be displayed under Re-assignee list
    And Capture "Re-Assign" decision
    Then filter the captured policies for "<Level>"
    And Click on "Expand All"
    And Verify Reassinged Presentation in oppurtunity Available Deck
    And User logsout of the "PM" Application

    Examples: 
      | User         | Client         | Level  |
      | iht_ittest03 | Aetna Medicaid | HEADER |

  #####################################################   Scenario-11  #############################################################
  @Unassign @Reunassign_11
  Scenario Outline: PCA1682_10 UnAssign the Oppurtunities in Presentation view at "<Level>"
    Given User "<User>" logged into "" application with Services
    Given the "<User>" moving the data from CPW to PM through the service for the client "<Client>" with "Single" DPKey
    Then Create the presenatation profile with given request data client "<Client>" payershort "" lob "" product "" and prority "" with "<User>"
    Then assign multiple dps to created profile
    Then User "<User>" logged into "PM" application
    When user selects "<Client>" from Client drop down list
    And User selects  multiple Payershorts "All" from Payershorts FilterSection
    And User selects muliple LOBs "All" from LOB FilterSection
    When user selects "All" Claimtypes in filtersection
    And User clicks on "Apply" button to filter
    Then The CPMorCMD clicks on the "Presentation having DPs"
    And Apply All Payers and LOBs filters in Presenation profile
    And Expand all the items in presenation hierarchy view
    Then Select DPs at "<Level>" level in presentation hierarchy view
    And Capture "Unassign" decision
    Then filter the captured policies for "<Level>"
    And Click on "Expand All"
    Then Profile names are removed from flip view of DP card from available opportunity deck
    And User logsout of the "PM" Application

    Examples: 
      | User         | Client         | Level          |
      | iht_ittest03 | Aetna Medicaid | DP             |
      | iht_ittest03 | Aetna Medicaid | Topic          |
      | iht_ittest03 | Aetna Medicaid | Medical Policy |

  #####################################################   Scenario-12  #############################################################
  @PCA-1719 @CancelFuntionality @Reunassign_12
  Scenario Outline: PCA1719 Cancel functionality and validating warning message for "<Level>"
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
    And verify the cancel functionality at "Re-Assign"
    And verify the cancel functionality at "Unassign"
    And verify the cancel functionality at "Approve"

    Examples: 
      | User         | Client         | Level          |
      | iht_ittest03 | Aetna Medicaid | DP             |
      | iht_ittest03 | Aetna Medicaid | Topic          |
      | iht_ittest03 | Aetna Medicaid | Medical Policy |

  #####################################################   Scenario-13  #############################################################
  @PCA2364_1 @Reunassign_13
  Scenario Outline: PCA2364_1 validation of Display hover text in Presentation view for Priority,Priority Reasons under Topic and MP
    Given User "<User>" logged into "" application with Services
    Given the "<User>" moving the data from CPW to PM through the service for the client "<Client>" with "<DPKey>" DPKey and Priority as "<Priority>"
    Then Create the presenatation profile with given request data client "<Client>" payershort "" lob "" product "" and prority "" with "<User>"
    Then assign multiple dps to created profile
    When User "<User>" logged into "PM" application
    When user selects "<Client>" from Client drop down list
    Then The CPMorCMD clicks on the "Presentation having DPs"
    And Apply All Payers and LOBs filters in Presenation profile
    And Expand all the items in presenation hierarchy view
    Then validate the display hove text of priortiy reasons in presentaiondeck

    Examples: 
      | User   | Client         | Priority        | DPKey  |
      | nkumar | Aetna Medicaid | High,Medium,Low | Single |
