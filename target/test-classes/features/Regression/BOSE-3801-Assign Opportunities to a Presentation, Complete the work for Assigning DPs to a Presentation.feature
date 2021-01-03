#Author: Ravi Theja M
@Regression @BOSE3801
Feature: BOSE-3801 Assign Opportunities to a Presentation: Complete the work for Assigning DPs to a Presentation

  @BOSE-2968
  Scenario Outline: Scenario_1 Ability to view filters save and sync respective of user
    Given User "iht_ittest09" logged into "PM" application
    Then The user should view the PresentationManager HomePage
    When user selects "<Client>" from Client drop down list
    And User Select Any PayerShorts and LOB Filters
    When user selects "All" Claimtypes in filtersection
    And User clicks on "Apply" button to filter
    And User logsout of the "PM" Application
    When User "iht_ittest09" logged into "PM" application
    And The user should view the PresentationManager HomePage
    When user selects "<Client>" from Client drop down list
    Then User must view the saved filters
    And User logsout of the "PM" Application

    Examples: 
      | Client         |
      #|   Dean Health Plan      |
      | Aetna Medicaid |

  @BOSE-2
  Scenario Outline: Scenario_2 Ability to view filters save and sync respective of user
    Given User "iht_ittest09" logged into "PM" application
    Then The user should view the PresentationManager HomePage
    When user selects "<Client>" from Client drop down list
    And User Select Any PayerShorts and LOB Filters
    When user selects "All" Claimtypes in filtersection
    And User clicks on "Apply" button to filter
    And User logsout of the "PM" Application
    Given User "ulanka" logged into "PM" application
    Then The user should view the PresentationManager HomePage
    When user selects "<Client>" from Client drop down list
    And User "unselect all" PayerShorts and LOB Filters
    And User clicks on "Apply" button to filter
    And User logsout of the "PM" Application
    When User "iht_ittest09" logged into "PM" application
    And The user should view the PresentationManager HomePage
    When user selects "<Client>" from Client drop down list
    Then User must view the saved filters
    And User logsout of the "PM" Application

    Examples: 
      | Client         |
      #|   Dean Health Plan      |
      | Aetna Medicaid |

  #@BOSE-296
  #Scenario Outline: Scenario_3 Ability to view filters save and sync respective of user
  #	Given User "<User>" logged into "" application with Services
    #Then Create the presenatation profile with given request data client "<Client>" payershort "" lob "" product "" and prority "" with "<User>"
    #Given User "iht_ittest05" logged into "PM" application
    #Then The user should view the PresentationManager HomePage
    #When user selects "<Client>" from Client drop down list
    #And User "select all" PayerShorts and LOB Filters
    #When user selects "All" Claimtypes in filtersection
    #And User clicks on "Apply" button to filter
    #And User selects Medical Policy/Topic according to "AllMedicalPolicies"
    #And user filters by clicking on Apply for Medical Policy/Topic
    #Then User click on "Collapse All" link
    #Then unassign the created presentation at "<Level>" level
    #Then filter unassigned "<Level>"
    #And user filters by clicking on Apply for Medical Policy/Topic
    #Then Profile names are removed from flip view of DP card from available opportunity deck
    #And User logsout of the "PM" Application
#
    #Examples: 
      #| Client         | Level |
      #| Aetna Medicaid | Topic |

  @BOSE-2798 @BOSE-2643
  Scenario Outline: BOSE-2798- Validation of Scrollbars in Presentation Manager
    Given User "iht_ittest09" logged into "PM" application
    Then The user should view the PresentationManager HomePage
    When user selects "<Client>" from Client drop down list
    Then validate scroll bars in Filter panel
    And User logsout of the "PM" Application

    Examples: 
      | Client                            |
      | Horizon Healthcare Services, Inc. |

  @BOSE-2875 @BOSE-2611
  Scenario Outline: BOSE-2875 -Validation of Client details
    Given User "iht_ittest05" logged into "PM" application
    Then The user should view the PresentationManager HomePage
    And Post the user access "<Request URl>" and fetch client names
    Then verify the clients are displayed according to logged in User
    When user selects "<Client>" from Client drop down list
    ##Need to add PPS details validation according to mongoDB query[still in enhancements]
    And User logsout of the "PM" Application

    Examples: 
      | Request URl                                              | Client         |
      # |http://10.170.32.20:10036/micro/teammanagement/1/clients |   Dean Health Plan       |
      | https://cpd-gateway-qa2.cotiviti.com/micro/teammanagement/1/clients | Aetna Medicaid |

  #	|http://10.170.32.20:10036/micro/teammanagement/1/clients |   Anthem, Inc				       |
  @BOSE-3249
  Scenario Outline: BOSE-3249 - Validation of client,PPS details as per the CPW USER
    Given User "iht_ittest09" logged into "PM" application
    Then The user should view the PresentationManager HomePage
    And Post the user access "<Request URl>" and fetch client names
    Then verify the clients are displayed according to logged in User
    And User logsout of the "PM" Application

    Examples: 
      | Request URl                                              |
      | https://cpd-gateway-qa2.cotiviti.com/micro/teammanagement/1/clients |

  @BOSE-2877 @BOSE-2571
  Scenario Outline: BOSE-2877 - Ability to validate Oppurtunity Deck based on client selection
    Given User "iht_ittest05" logged into "PM" application
    Then The user should view the PresentationManager HomePage
    And user should view client list dropdown
    And user should view Reset button for Payer Shorts
    And user should view Apply button for Payer Shorts
    Then verify clients list are sorted in alphabetical order
    When user selects "<Client1>" from Client drop down list
    And User selects  multiple Payershorts "<Payershorts1>" from Payershorts FilterSection
    And User selects muliple LOBs "<LOBs1>" from LOB FilterSection
    And User clicks on "Apply" button to filter
    Then verify "No opportunities match your selections" message
    When user selects "<Client2>" from Client drop down list
    And User selects  multiple Payershorts "<Payershorts2>" from Payershorts FilterSection
    And User selects muliple LOBs "<LOBs2>" from LOB FilterSection
    And User clicks on "Apply" button to filter
    Then verify "No opportunities match your selections" message is disappeared
    And User logsout of the "PM" Application

    Examples: 
      | Client1            | Payershorts1 | LOBs1      | Client2        | Payershorts2 | LOBs2 |
      | UnitedHealth Group | UHCCA        | Commercial | Aetna Medicaid | ALL          | ALL   |

  @BOSE-2876 @BOSE-2556
  Scenario Outline: BOSE-2876 - Ability to validate Client Subsequent View in Presentation Manager
    Given User "iht_ittest09" logged into "PM" application
    Then The user should view the PresentationManager HomePage
    When user selects "<Client>" from Client drop down list
    And User Select Any PayerShorts and LOB Filters
    When user selects "All" Claimtypes in filtersection
    And User Select Any Medical Policy
    And user filters by clicking on Apply for Medical Policy/Topic
    And User clicks on "Apply" button to filter
    And User logsout of the "PM" Application
    Given User "iht_ittest09" logged into "PM" application
    And The user should view the PresentationManager HomePage
    When user selects "<Client>" from Client drop down list
    Then User must view the saved filters
    And User view the saved filters for Medical Policy
    And User logsout of the "PM" Application

    Examples: 
      | Client         |
      | Aetna Medicaid |
