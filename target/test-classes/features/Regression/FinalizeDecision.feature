#RamaKrishna
@Regression @PCA709 @Finalaize
Feature: Finalaized Decision,PCA-709

  #####################################################   Scenario-1  #############################################################
  @Regression @PCA709_1test
  Scenario Outline: PCA_709_1 Validate the capture and finalize decision "Approve" funcitonality in Presentation deck at "<Level>" Level for the Client "<Client>"
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
      | User         | Client             | Level  | Decision |
      | iht_ittest09 | UnitedHealth Group | DP ALL | Approve  |

  #####################################################   Scenario-2  #############################################################
  @Regression @PCA709_2
  Scenario Outline: PCA_709_2 Validate the capture and finalize decision "Approve with Mod" funcitonality in Presentation deck at "<Level>" Level for the Client "<Client>"
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
      | User   | Client             | Level  | Decision         |
      | nkumar | UnitedHealth Group | DP ALL | Approve with Mod |

  #####################################################   Scenario-3  #############################################################
  @PCA709_3
  Scenario Outline: PCA_709_3 Validate the capture and finalize decision "Reject" funcitonality in Presentation deck at "<Level>" Level for the Client "<Client>"
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
      | nkumar | UnitedHealth Group | DP ALL | Reject   |

  #####################################################   Scenario-4  #############################################################
  @Regression @PCA709_4
  Scenario Outline: PCA_709_4 Validate the capture and finalize decision "Defer" funcitonality in Presentation deck at "<Level>" Level for the Client "<Client>"
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
      | nkumar | UnitedHealth Group | DP ALL | Defer    |

  #####################################################   Scenario-5  #############################################################
  @Regression @PCA709_5
  Scenario Outline: PCA_709_5 Validate the capture and finalize decision "Follow up" funcitonality in Presentation deck at "<Level>" Level for the Client "<Client>"
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
      | User   | Client             | Level  | Decision  |
      | nkumar | UnitedHealth Group | DP ALL | Follow up |

  #####################################################   Scenario-6  #############################################################
  @Regression @PCA709_6
  Scenario Outline: PCA_709_6 Validate the capture and finalize decision "Approve" funcitonality in Presentation deck at "<Level>" Level for the Client "<Client>"
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
      | User   | Client             | Level       | Decision |
      | nkumar | UnitedHealth Group | DP MULTIPLE | Approve  |

  #####################################################   Scenario-7  #############################################################
  @PCA709_7
  Scenario Outline: PCA_709_7 Validate the capture and finalize decision "Approve with Test only" funcitonality in Presentation deck at "<Level>" Level for the Client "<Client>"
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
      | User         | Client             | Level  | Decision          |
      | iht_ittest09 | UnitedHealth Group | DP ALL | Approve Test Only |

  #####################################################   Scenario-8  #############################################################
  @PCA709_8
  Scenario Outline: PCA_709_8 Validate the capture and finalize decision "Approve with Mod Test Only" funcitonality in Presentation deck at "<Level>" Level for the Client "<Client>"
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
      | User   | Client             | Level  | Decision                   |
      | nkumar | UnitedHealth Group | DP ALL | Approve with Mod Test Only |

  #####################################################   Scenario-9  #############################################################
  @Regression @PCA709_9
  Scenario Outline: PCA_709_9 Validate the capture and finalize decision "Approve" and ReAssign and Unassign disable funcitonality in Presentation deck at "<Level>" Level for the Client "<Client>"
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
    Then Select DPs at "DP ALL REASSIGN_UNASSIGN_DISABLED" level
    And Finalize "<Decision>" Decisions for "<Level>" payershorts
    And User logsout of the "PM" Application

    Examples: 
      | User   | Client             | Level  | Decision |
      | nkumar | UnitedHealth Group | DP ALL | Approve  |

  #####################################################   Scenario-10  #############################################################
  #Sravanthi
  @PCA-2329 @PCA709_10
  Scenario Outline: PCA-2329_1 Validation of Finalized opportunities in the available opportunity view in PM
    Given User "<User>" logged into "" application with Services
    Then the "<User>" moving the data from CPW to PM through the service for the client "<Client>" with "Single" DPKey
    Then Create the presenatation profile with given request data client "<Client>" payershort "" lob "" product "" and prority "" with "<User>"
    Then assign multiple dps to created profile
    Given User "<User>" logged into "PM" application
    When user selects "<Client>" from Client drop down list
    When user selects "All" Claimtypes in filtersection
    And user "SELECT" "Payer Shorts:LOB" under Payershort/LOB section
    And user select "MP_TOPIC_ALL" value "" for "DESELECT" operation
    And user select "MEDICAL POLICY" value "DB" for "SELECT" operation
    Then verify raw savings and DP count of PresentationProfile ""
    When user clicks created "PresentationProfile"
    And Expand all the items in presenation hierarchy view
    Then Select DPs at "DP" level in presentation hierarchy view
    And Capture "Approve" decision
    And Expand all the items in presenation hierarchy view
    Then select oppurtunities at "ALL" level and finalize the oppurtunities
    And User clicks on "Available Oppurtunities" button to filter
    And User selects  multiple Payershorts "All" from Payershorts FilterSection
    And User selects muliple LOBs "All" from LOB FilterSection
    When user selects "All" Claimtypes in filtersection
    And User clicks on "Apply" button to filter
    And user select "MP_TOPIC_ALL" value "" for "DESELECT" operation
    And user verify RawSavings updated for medical policy
    And User filters the "AvailableDPsDeck" with filter drpdown value "CompletedOnly" to show related DPs
    And verify DP in Available oppurtunity deck
    And User logsout of the "PM" Application

    Examples: 
      | User   | Client             | Level | Decision | OppLevel |
      | nkumar | UnitedHealth Group | DP    | Approve  | ALL      |

  #####################################################   Scenario-11  #############################################################
  #Sravanthi
  @PCA-2206_2 @PCA709_11
  Scenario Outline: PCA-2206_2 Ability to view check box is removed for the Medical Policy for the finalized opportunity
    Given User "<User>" logged into "Ihealth123" application with Services
    Then the "<User>" moving the data from CPW to PM through the service for the client "<Client>" with "Single" DPKey
    Then Create the presenatation profile with given request data client "<Client>" payershort "" lob "" product "" and prority "" with "<User>"
    Then assign multiple dps to created profile
    Given User "<User>" logged into "PM" application
    When user selects "<Client>" from Client drop down list
    When user clicks created "PresentationProfile"
    And Expand all the items in presenation hierarchy view
    Then Select DPs at "DP" level in presentation hierarchy view
    And Capture "Approve" decision
    And Expand all the items in presenation hierarchy view
    Then select oppurtunities at "ALL" level and finalize the oppurtunities
    And Expand all the items in presenation hierarchy view
    And verify check box is removed for "PPS" finalized oppurtunity
    Then Refresh the page
    When user clicks created "PresentationProfile"
    And Expand all the items in presenation hierarchy view
    And verify check box is removed for "MP" finalized oppurtunity
    And verify check box is removed for "Topic" finalized oppurtunity
    And verify check box is removed for "DP" finalized oppurtunity
    And User logsout of the "PM" Application

    Examples: 
      | User   | Client    | Level | Decision | OppLevel |
      | nkumar | WellPoint | DP    | Approve  | ALL      |

  #####################################################   Scenario-12  #############################################################
  #Sravanthi
  @PCA-2206_1 @PCA709_12
  Scenario Outline: PCA-2206_3 Ability to view check box is removed from the Opportunity in the payer/LOB grid for the finalized opportunity
    Given User "<User>" logged into "Ihealth123" application with Services
    Then the "<User>" moving the data from CPW to PM through the service for the client "<Client>" with "Single" DPKey
    Then Create the presenatation profile with given request data client "<Client>" payershort "" lob "" product "" and prority "" with "<User>"
    Then assign multiple dps to created profile
    Given User "<User>" logged into "PM" application
    When user selects "<Client>" from Client drop down list
    When user clicks created "PresentationProfile"
    And Expand all the items in presenation hierarchy view
    Then Select DPs at "DP" level in presentation hierarchy view
    And Capture "Approve" decision
    And Expand all the items in presenation hierarchy view
    Then select oppurtunities at "PPS" level and finalize the oppurtunities
    And Expand all the items in presenation hierarchy view
    And verify check box is removed for "SINGLE PPS" finalized oppurtunity
    Then Refresh the page
    When user clicks created "PresentationProfile"
    And Expand all the items in presenation hierarchy view
    And verify check box exists for "MP" if decisions are not finalized for all oppurtunities
    And verify check box exists for "TOPIC" if decisions are not finalized for all oppurtunities
    And verify check box exists for "DP" if decisions are not finalized for all oppurtunities
    And User logsout of the "PM" Application

    Examples: 
      | User   | Client    | Level | Decision | OppLevel |
      | nkumar | WellPoint | DP    | Approve  | ALL      |
