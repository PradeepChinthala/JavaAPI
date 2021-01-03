#Author: chaitanya kumar natuva
@Regression @BOSE2832
Feature: BOSE-2832

  #Available Opportunity COunt was not matching for Aetna and Amerihealth
  ######################################################   Scenario-1  #############################################################
  @BOSE2832_1
  Scenario Outline: BOSE2832_Scenario_1 validate the Available DPs count at heade,MP and Topic Level
    Given User "<User>" logged into "PM" application
    When user selects "<Client>" from Client drop down list
    And User selects  multiple Payershorts "All" from Payershorts FilterSection
    And User selects muliple LOBs "All" from LOB FilterSection
    When user selects "All" Claimtypes in filtersection
    And User clicks on "Apply" button to filter
    And User selects Medical Policy/Topic according to "AllMedicalPolicies"
    And user filters by clicking on Apply for Medical Policy/Topic
    Then validate the avaliable DPs count at "Header level"
    And validate the avaliable DPs count at "Medicalpolicy level"
    Then validate the avaliable DPs count at "Header level"

    Examples: 
      | User   | Client |
      | nkumar | Cigna  |

  #UnitedHealth Group
  ######################################################   Scenario-2  #############################################################
  @BOSE2832_2
  Scenario Outline: BOSE2832_Scenario_2 validate the Available DPs count at heade,MP and Topic Level With Filters
    Given User "<User>" logged into "PM" application
    When user selects "<Client>" from Client drop down list
    And User selects  multiple Payershorts "<Payershort>" from Payershorts FilterSection
    And User selects muliple LOBs "<Insurance>" from LOB FilterSection
    When user selects "All" Claimtypes in filtersection
    And User clicks on "Apply" button to filter
    And User selects Medical Policy/Topic according to "AllMedicalPolicies"
    And user filters by clicking on Apply for Medical Policy/Topic
    Then validate the avaliable DPs count at "DP COUNT BASED ON CLIENT WITH FILTERS"
    And validate the avaliable DPs count at "DP COUNT BASED ON MP WITH FILTERS"
    Then validate the avaliable DPs count at "DP COUNT BASED ON TOPIC WITH FILTERS"

    Examples: 
      | User   | Client         | Payershort | Insurance         |
      | nkumar | Aetna Medicaid | SACKY      | Medicare,Medicaid |

  ######################################################   Scenario-3  #############################################################
  #Sravanthi
  @PCA-1935 @PCA-1531
  Scenario Outline: PCA 1531_Available opportunities Header view
    Given User "iht_ittest05" logged into "PM" application
    When user selects "<Client>" from Client drop down list
    Then verify Available oppurtunities elements and its functionality

    Examples: 
      | User         | Client         |
      | iht_ittest09 | Aetna Medicaid |

  ######################################################   Scenario-4  #############################################################
  #Sravanthi
  @PCA-1345_1 @PCA-1345_3 @PCA-1584 @PCA-1585
  Scenario Outline: PCA-1345_Scenario-3_Ability to view LOB data on a DP card based on filters and configuration of the client on available DP card
    Given User "<User>" logged into "Ihealth123" application with Services
    #Then the "<User>" moving the data from CPW to PM through the service for the client "<Client>" with "SingleDP-MultiplePayer-MultipleLOB" DPKey
    Then the "<User>" moving the data from CPW to PM through the service for the client "<Client>" with "Single-Multiple LOB" DPKey
    Given User "iht_ittest09" logged into "PM" application
    When user selects "<Client>" from Client drop down list
    And User selects All PayerShorts
    And User selects All  LOBs
    When user selects "All" Claimtypes in filtersection
    And User clicks on "Apply" button to filter
    And User selects Medical Policy/Topic according to "Captured Topic"
    And user filters by clicking on Apply for Medical Policy/Topic
    Then verify LOB status bar in "DP Detailed View"
    Then verify LOB status bar in "DP Decision View"

    Examples: 
      | User         | Client         |
      | iht_ittest09 | Aetna Medicaid |

  #####################################################   Scenario-5  #############################################################
  #Need to chnage the code for selecting the multiple payers,LObs
  @BOSE-2924 @BOSE2832_5
  Scenario Outline: BOSE-2924 validation of Presentation Manager application after "Relogin"
    Given User "iht_ittest09" logged into "PM" application
    Then the user views the Presentation Manager Home Page
    When user selects "<Client>" from Client drop down list
    And User Select Any PayerShorts and LOB Filters
    When user selects "All" Claimtypes in filtersection
    And User clicks on "Apply" button to filter
    #And User Select Any Medical Policy
    And user filters by clicking on Apply for Medical Policy/Topic
    And User logsout of the "PM" Application
    When User "iht_ittest09" logged into "PM" application
    And The user should view the PresentationManager HomePage
    When user selects "<Client>" from Client drop down list
    Then User must view the saved filters
    #And User view the saved filters for Medical Policy
    And User logsout of the "PM" Application

    Examples: 
      | Client         |
      | Aetna Medicaid |

  #####################################################   Scenario-6  #############################################################
  #Sravanthi
  @BOSE-4016 @BOSE2832_6
  Scenario Outline: BOSE-4016 -Payer Policy set details from the Payer Config table
    Given User "iht_ittest09" logged into "PM" application
    When user selects "<Client>" from Client drop down list
    Then validate Payershort and LOB of "<Client>" in application acccording to toad

    Examples: 
      | Client           |
      | Dean Health Plan |

  #####################################################   Scenario-7  #############################################################
  @BOSE-3875 @BOSE-2642 @BOSE2832_7   @ReRun
  Scenario Outline: BOSE-3875 - Actions with Get available Opportunities button

  Given User "iht_ittest09" logged into "" application with Services
    Given capture "Single" DP with new Payer short for the "<Client>" through Service
    Given User "iht_ittest09" logged into "PM" application
    When user selects "<Client>" from Client drop down list
    And user "SELECT" "Payer Shorts:LOB" under Payershort/LOB section
    And user "DESELECT" "Payer Shorts:LOB" under Payershort/LOB section
    And user "SELECT" "LOB" under Payershort/LOB section
    When user selects "All" Claimtypes in filtersection
    And filter "first" payer short and "Topic" retrieved through Service
    Then validate RawSavings in DB and UI for DP captured through Service corresponding to "first" payer short
    Given capture "Same" DP with new Payer short for the "<Client>" through Service
    And click on "Get Available Oppurtunities" in filter drawer section
    And user "SELECT" "LOB" under Payershort/LOB section
    When user selects "All" Claimtypes in filtersection
    And filter "second" payer short and "Topic" retrieved through Service
    Then validate RawSavings in DB and UI for DP captured through Service corresponding to "second" payer short
    And filter "first" payer short and "Topic" retrieved through Service
    Then validate RawSavings in DB and UI for DP captured through Service corresponding to "both" payer short
    Then User logsout of the "PM" Application

    Examples: 
      | Client |
      | Cigna  |

  #####################################################   Scenario-8  #############################################################
  @PCA774_1
  Scenario Outline: PCA774_1 validation of Presentation view for no of DPs,DP savings,Priority,Priority Reasons under Topic and MP
    Given User "<User>" logged into "" application with Services
    Given the "<User>" moving the data from CPW to PM through the service for the client "<Client>" with "<DPKey>" DPKey and Priority as "<Priority>"
    Then Create the presenatation profile with given request data client "<Client>" payershort "" lob "" product "" and prority "" with "<User>"
    Then assign multiple dps to created profile
    When User "<User>" logged into "PM" application
    When user selects "<Client>" from Client drop down list
    Then The CPMorCMD clicks on the "Presentation having DPs"
    And Apply All Payers and LOBs filters in Presenation profile
    And Expand all the items in presenation hierarchy view
    And validate the list of DPs in Presentationview

    Examples: 
      | User   | Client         | Priority        | DPKey    |
      | nkumar | Aetna Medicaid | High,Medium,Low | Multiple |

  