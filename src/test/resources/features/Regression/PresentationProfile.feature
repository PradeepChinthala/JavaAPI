#Author: ramakrishna.bodlapati
@Regression @PresentationProfileValidations
Feature: Presentation Profile validations

  ######################################################   Scenario-1  #############################################################
  @BOSE-2974
  Scenario Outline: PP_1 PresentationProfile Validations
    Given User "ulanka" logged into "PM" application
    When user selects "<Client>" from Client drop down list
    Then user validate Presentation profile "CANCEL" functionality
    Then user validate Presentation profile "CREATE_ALLFEILDS_CANCEL" functionality
    Then user validate Presentation profile "CREATE" functionality
    Then user validate Presentation profile "CREATE_ALLFEILDS" functionality
    Then user validate Presentation profile "CREATE_35_CHAR" functionality
    And "EDIT" Presentation Profile functionality
    And "DELETE" Presentation Profile functionality
    And "EDIT_35_CHAR" Presentation Profile functionality
    And validate payershort section based on the payers in the RVA run for the "<Client>","_id.payerShort"
    And validate payershort section based on the payers in the RVA run for the "<Client>","insuranceDesc"
    Then User logsout of the "PM" Application

    Examples: 
      | Client         |
      | Aetna Medicaid |

  ######################################################   Scenario-2  #############################################################
  @BOSE-2925 @PP_2
  Scenario Outline: PP_2 Ability to create new presentation and cancel_enable cancel
    Given User "<user>" logged into "" application with Services
    Given the "<user>" moving the data from CPW to PM through the service for the client "<Client>" with "Single" DPKey
    Given User "ulanka" logged into "PM" application
    When user selects "<Client>" from Client drop down list
    And user "SELECT" "Payer Shorts" under Payershort/LOB section
    And user "SELECT" "LOB" under Payershort/LOB section
    When user selects "All" Claimtypes in filtersection
    And User clicks on "Apply" button to filter
    And user select "TOPIC" value "DB" for "SELECT" operation
    Then User logsout of the "PM" Application

    Examples: 
      | Client              | user   |
      | Gateway Health Plan | ulanka |

  ######################################################   Scenario-3  #############################################################
  @PCA-766 @PP_3
  Scenario Outline: PP_3 PCA-766 Assignment and Presentation tabs tool bar_Client selection
    Given User "ulanka" logged into "PM" application
    When user selects "<Client>" from Client drop down list
    And verify "Filter Opportunities" icon displayed on screen
    And verify "Search for DPs" icon displayed on screen
    And verify "Edit Presentation Profile" icon displayed on screen
    Then User logsout of the "PM" Application

    Examples: 
      | Client         |
      | Aetna Medicaid |

  ######################################################   Scenario-4  #############################################################
  @PCA-766Rama
  Scenario Outline: PP_4 PCA-766 Assignment and Presentation tabs tool bar_Presentation tab
    Given User "ulanka" logged into "PM" application
    When user selects "<Client>" from Client drop down list
    Then user validate Presentation profile "CREATE_CLICK" functionality
    And verify "Filter Opportunities" icon displayed on screen
    And verify "View Presentation Hierarchy" icon displayed on screen
    And verify "Search for DPs" icon displayed on screen
    And verify "Edit Presentation Profile" icon displayed on screen
    Then User logsout of the "PM" Application

    Examples: 
      | Client         |
      | Aetna Medicaid |

  ######################################################   Scenario-5  #############################################################
  @PCA-726  @ReRun
  Scenario Outline: PP_5 Ability to view assign pop-up when no opportunity is assigned to a profile
    Given the "<user>" moving the data from CPW to PM through the service for the client "<Client>" with "Single" DPKey
    And User "ulanka" logged into "PM" application
    When user selects "<Client>" from Client drop down list
    And user "SELECT" "Payer Shorts" under Payershort/LOB section
    And user "SELECT" "LOB" under Payershort/LOB section
    When user selects "All" Claimtypes in filtersection
    And User clicks on "Apply" button to filter
    And user select "TOPIC" value "DB" for "SELECT" operation
    And user validate Presentation profile "CREATE" functionality
    Then validate view assign pop-up when no opportunity is assigned to a profile
    Then User logsout of the "PM" Application

    Examples: 
      | Client         | user   |
      | Aetna Medicaid | ulanka |

  ######################################################   Scenario-6  #############################################################
  @PCA-783
  Scenario Outline: PP_6 Ability to move across Presentation Profile tabs
    Given User "ulanka" logged into "PM" application
    When user selects "<Client>" from Client drop down list
    Then user validate Presentation profile "CREATE_CLICK" functionality
    Then user validate Presentation profile "CREATE_CLICK" functionality
    Then User logsout of the "PM" Application

    Examples: 
      | Client         |
      | Aetna Medicaid |

  ######################################################   Scenario-7  #############################################################
  @PresProfile @BOSE2950 @BOSE-3119 @PP_7
  Scenario Outline: PP_7 BOSE2950-Ability to retrieve and display the existing Presentations on client change or login to PM
    Given User "<User>" logged into "PM" application
    Then The user should view the PresentationManager HomePage
    When user selects "<Client>" from Client drop down list
    Then Application should display all the PresentationProfiles associated with the "<Client>"  from DB
    And User logsout of the "PM" Application

    Examples: 
      | User   | Client | Payershorts | LOBs | MedPolicyTopicType |
      | ulanka | Cigna  | All         | All  | AllMedicalPolicies |

  ######################################################   Scenario-8  #############################################################
  #Sravanthi   ##Passed
  @PCA2227_test   @ReRun
  Scenario Outline: PP_8 PCA-2227 - Validatoin of "Payer-ClaimType/LOB" grid in DP of "Available Oppurtunity" deck
    Given User "<User>" logged into "" application with Services
    Then the "<User>" moving the data from CPW to PM through the service for the client "<Client>" with "Single" DPKey
    Given User "<User>" logged into "PM" application
    When user selects "<Client>" from Client drop down list
    And User selects  multiple Payershorts "All" from Payershorts FilterSection
    And User selects muliple LOBs "All" from LOB FilterSection
    When user selects "All" Claimtypes in filtersection
    And User clicks on "Apply" button to filter
    And User selects Medical Policy/Topic according to "Captured Topic"
    And user filters by clicking on Apply for Medical Policy/Topic
    Then verify PPS combination in DP detailed view
    Given CPMorCMD has created a PresentationProfile
    And select All PPS and assign DP to newly created presentation profile
    And verify presentation name on DP card
    Then verify presentation name for all PPS in DP detailed view
    And User logsout of the "PM" Application

    Examples: 
      | User   | Client |
      | nkumar | Cigna  |

  #####################################################   Scenario-9  #############################################################
  #Ravitheja
  @PCA-962 @EditProfile
  Scenario Outline: PP_9 Edit presentation profile in presentation deck view
    Given User "<User>" logged into "" application with Services
    Given the "<User>" moving the data from CPW to PM through the service for the client "<Client>" with "Single" DPKey
    Then Create the presenatation profile with given request data client "<Client>" payershort "" lob "" product "" and prority "" with "<User>"
    Then assign multiple dps to created profile
    Then User "<User>" logged into "PM" application
    When user selects "<Client>" from Client drop down list
    Then The CPMorCMD clicks on the "Presentation having DPs"
    And verify the Edit Profile functionality

    Examples: 
      | User         | Client         |
      | iht_ittest03 | Aetna Medicaid |

  #####################################################   Scenario-10  #############################################################
  #Ravitheja
  @PCA-1899 @NotificationMessage @PP_10
  Scenario Outline: PP_10 Verify Warning message with no filter criteria
    Given User "<User>" logged into "" application with Services
    Then Create the presenatation profile with given request data client "<Client>" payershort "<Payershort>" lob "<LOB>" product "<Product>" and prority "<Priority>" with "<User>"
    Then the "<User>" moving the data from CPW to PM through the service for the client "<Client>" with "Single" DPKey
    Then assign multiple dps to created profile
    Then User "<User>" logged into "PM" application
    When user selects "<Client>" from Client drop down list
    Then The CPMorCMD clicks on the "Presentation having DPs"
    And Select the PayerShort and LOB combination having no filter criteria
    Then apply filters having no oppurtunities
    Then Verify the message "No opportunities match your filter selections"

    Examples: 
      | User   | Client |
      | nkumar | Cigna  |

  #####################################################   Scenario-11  #############################################################
  #Ravitheja
  @PCA-1248 @NextandPreviousLinks @PP_11
  Scenario Outline: PP_11 Ability to navigate to previous and next DP
    Given User "<User>" logged into "Ihealth123" application with Services
    Then Create the presenatation profile with given request data client "<Client>" payershort "<Payershort>" lob "<LOB>" product "<Product>" and prority "<Priority>" with "<User>"
    Then the "<User>" moving the data from CPW to PM through the service for the client "<Client>" with "Multiple" DPKey
    Then assign multiple dps to created profile
    Then User "<User>" logged into "PM" application
    When user selects "<Client>" from Client drop down list
    Then The CPMorCMD clicks on the "Presentation having DPs"
    And Apply All Payers and LOBs filters in Presenation profile
    And Expand all the items in oppurtunity hierarchy view
    Then verify the ability to navigate to previous and next DP

    Examples: 
      | User         | Client         |
      | iht_ittest03 | Aetna Medicaid |

  #####################################################   Scenario-12  #############################################################
  #Ravitheja
  @PCA-982 @NotificationMessage
  Scenario Outline: PP_12 Create Presentation Profile and verify message
    Given User "<User>" logged into "Ihealth123" application with Services
    Then Create the presenatation profile with given request data client "<Client>" payershort "<Payershort>" lob "<LOB>" product "<Product>" and prority "<Priority>" with "<User>"
    Then User "<User>" logged into "PM" application
    When user selects "<Client>" from Client drop down list
    Then The CPMorCMD clicks on the "Presentation having no DPs"
    Then Verify the message "Assign opportunities from the Available Opportunities tab. "

    Examples: 
      | User         | Client       | LOB                          | Payershort                          | Product    | Priority |
      | iht_ittest05 | Humana, Inc. | Commercial;Medicaid;Medicare | HUMCC;HUMCF;HUMCH;HUMCM;HUMCP;HUMIL | Outpatient | High     |

  ######################################################   Scenario-13  #############################################################
  #Chaitanya
  @PCA_1974_1
  Scenario Outline: PP_13 PCA_1974_1 Validation of Export popup in presentationview
    Given User "<User>" logged into "" application with Services
    Given the "<User>" moving the data from CPW to PM through the service for the client "<Client>" with "Single" DPKey
    Then Create the presenatation profile with given request data client "<Client>" payershort "" lob "" product "" and prority "" with "<User>"
    Then assign multiple dps to created profile
    Given User "<User>" logged into "PM" application
    When user selects "<Client>" from Client drop down list
    Then validate the "Export Popup" in Presentation deck

    Examples: 
      | User   | Client |
      | nkumar | Humana |

  ######################################################   Scenario-14  #############################################################
  #sravanthi
  @PCA-1910  @ReRun
  Scenario Outline: PP_14 PCA 1910_Validation of Disabled icons in the header toolabar ,when the user in DP Decisoin view
    Given User "<User>" logged into "Ihealth123" application with Services
    Then the "<User>" moving the data from CPW to PM through the service for the client "<Client>" with "Single" DPKey
    Then Create the presenatation profile with given request data client "<Client>" payershort "" lob "" product "" and prority "" with "<User>"
    Then assign multiple dps to created profile
    Given User "iht_ittest09" logged into "PM" application
    When user selects "<Client>" from Client drop down list
    When user clicks created "PresentationProfile"
    And Expand all the items in presenation hierarchy view
    Then validate disabled icons of presentation deck
    And User logsout of the "PM" Application

    Examples: 
      | User         | Client         |
      | iht_ittest09 | Aetna Medicaid |

  ######################################################   Scenario-15  #############################################################
  #sravanthi
  @PCA-1345_2 @PCA-1345_4 @PCA-1586 @PCA-1587 @PP_15
  Scenario Outline: PP_15 PCA-1345_Scenario-4_Ability to view LOB data on a DP card based on filters and configuration of the client on DP Detailed view/Decision View
    Given User "<User>" logged into "Ihealth123" application with Services
    #Then the "<User>" moving the data from CPW to PM through the service for the client "<Client>" with "SingleDP-MultiplePayer-MultipleLOB" DPKey
    Then the "<User>" moving the data from CPW to PM through the service for the client "<Client>" with "Single-Multiple LOB" DPKey
    Then Create the presenatation profile with given request data client "<Client>" payershort "" lob "" product "" and prority "" with "<User>"
    Then assign multiple dps to created profile
    Given User "iht_ittest09" logged into "PM" application
    When user selects "<Client>" from Client drop down list
    When user clicks created "PresentationProfile"
    And Expand all the items in presenation hierarchy view
    Then Select DPs at "DP" level in presentation hierarchy view
    And Capture "Approve" decision
    And Expand all the items in presenation hierarchy view
    #Then verify LOB status bar in "DP Detailed View" for the client "<Client>"
    Then verify LOB status bar in "DP Detailed View"
    Then verify LOB status bar in "DP Decision View"

    Examples: 
      | User         | Client             |
      | iht_ittest09 | UnitedHealth Group |

  ######################################################   Scenario-16  #############################################################
  #sravanthi
  @PCA-1930
  Scenario Outline: PP_16 PCA 802_Ability to display the Payers and LOBs under each DP in grid view
    Given User "<User>" logged into "Ihealth123" application with Services
    #Then the "<User>" moving the data from CPW to PM through the service for the client "<Client>" with "Single-Multiple LOB Without Release" DPKey
    Then the "<User>" moving the data from CPW to PM through the service for the client "<Client>" with "Single-Multiple LOB" DPKey
    Then Create the presenatation profile with given request data client "<Client>" payershort "" lob "" product "" and prority "" with "<User>"
    Then assign multiple dps to created profile
    Given User "iht_ittest09" logged into "PM" application
    When user selects "<Client>" from Client drop down list
    When user clicks created "PresentationProfile"
    And Expand all the items in presenation hierarchy view
    Then select assigned DP and verify LOB inspector
    And User logsout of the "PM" Application

    Examples: 
      | User         | Client         |
      | iht_ittest09 | Aetna Medicaid |

  #####################################################   Scenario-17  #############################################################
  @PCA2557_1
  Scenario Outline: PP_17 PCA2557_1 validate the PayerLOB grid and thier savings for the applied "ALL" PPS in Presentationview
    Given User "<User>" logged into "" application with Services
    Given the "<User>" moving the data from CPW to PM through the service for the client "<Client>" with "Single DP Multiple PPS" DPKey
    Then Create the presenatation profile with given request data client "<Client>" payershort "" lob "" product "" and prority "" with "<User>"
    Then assign multiple dps to created profile
    Then User "<User>" logged into "PM" application
    When user selects "<Client>" from Client drop down list
    Then The CPMorCMD clicks on the "Presentation having DPs"
    And Apply All Payers and LOBs filters in Presenation profile
    And Expand all the items in presenation hierarchy view
    Then validate the "PayerLOBGrid in presentationview" and thier raw savings with DB in filter drawer section

    Examples: 
      | User   | Client |
      | nkumar | Humana |

  #####################################################   Scenario-18  #############################################################
  @BOSE-2493 @BOSE-3252 @PP_18
  Scenario Outline: PP_17 BOSE-3252-Validation of "Edit,Delete,Export" buttons availabilty
    Given User "iht_ittest05" logged into "PM" application
    Then The user should view the PresentationManager HomePage
    When user selects "<Client>" from Client drop down list
    Given CPMorCMD has created a PresentationProfile
    When The CPMorCMD clicks on the "PresentationProfile"
    Then verify the header of "PresentationProfile"
    Then verify icons of Presentation
    And User logsout of the "PM" Application

    Examples: 
      | Client         |
      | Aetna Medicaid |

  #####################################################   Scenario-19  #############################################################
  @BOSE2950_1 @PP_19   @ReRun
  Scenario Outline: PP_18 BOSE2950_1 Validate the Created presentations and thier sorting order based on date for the given client "<Client>"
    Given User "<User>" logged into "PM" application
    And user selects "<Client>" from Client drop down list
    Then validate the Presentations created for the client "<Client>"

    Examples: 
      | User   | Client             |
      | nkumar | UnitedHealth Group |
