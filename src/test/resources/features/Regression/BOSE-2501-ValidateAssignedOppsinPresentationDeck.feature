#Author: Udayakiran.Lanka
@Regression @BOSE2501 @May22
Feature: BOSE-2501 Presentation Deck: Validate Assigned Opportunities in the Presentation

  ##################################### Scenario-1 ###################################################################
  @ValidateAssign_1
  Scenario Outline: BOSE-2795-Presentation Deck| Display DP cards BOSE-3316-Presentation Deck| Display Presentation Deck
    Given User "<UserName>" logged into "" application with Services
    Given the "<UserName>" moving the data from CPW to PM through the service for the client "<Client>" with "Multiple" DPKey
    Given User "<UserName>" logged into "PM" application
    Then The user should view the PresentationManager HomePage
    When user selects "<Client>" from Client drop down list
    And User selects All PayerShorts
    And User selects All  LOBs
    When user selects "All" Claimtypes in filtersection
    And User clicks on "Apply" button to filter
    And User selects Medical Policy/Topic according to "<MedPolicyTopicType>"
    And user filters by clicking on Apply for Medical Policy/Topic
    And User filters the "AvailableDPsDeck" with filter drpdown value "NotStartedOnly" to show related DPs
    Given CPMorCMD has created a PresentationProfile
    And The User have assigned  "<Multiple>"  MedicalPolicies to the PresentationProfile
    When The CPMorCMD clicks on the "PresentationProfile"
    And "MedicalPolicies" should be displayed in alphabetic ascending Order according to  "<MedPolicyTopicType>"
    And "Topics" should be displayed in alphabetic ascending Order according to  "<MedPolicyTopicType>"
    #And  "DPs" should be displayed in alphabetic ascending Order according to  "<MedPolicyTopicType>"    *** NEED TO LOOK INTO THIS CODE
    And User logsout of the "PM" Application

    Examples: 
      | UserName     | Client           | MedPolicyTopicType             | Multiple |
      #| ulanka       | Aetna Medicaid   | SingleMedPolicy&Topic          |          |
      | iht_ittest09 | Cigna   | AllMedicalPolicies             |        3 |
      #| ulanka       | Aetna Medicaid   | SingleMedPolicy&MultipleTopics |          |
      #| ulanka       | Dean Health Plan | MultipleMedPolicy&Topics       |          |

  ##################################### Scenario-2 ###################################################################
  ###need to add  ACS State Healthcare, LLC. 	Client to my UserID ulanka
  #@BOSE2968 										 @BOSE3337
  #Scenario Outline: BOSE-2968_Scenario-3_Ability to view no DP cards on available opportunities deck when client is selected for the first time with no opportunities
  #When User selects client "<Client>" from Client drop down list which has no Opportunities
  #Then  An empty filter drawer with "No opportunities match your selections" message  should be displayed
  #And   The available OportunitiesDeck with DP count as "0" should be displayed
  #And  User logsout of the "PM" Application
  #
  #Examples:
  #	|UserName	|         Client  |    Payer Shorts   |       LOB           |     Product       |  Medical Policy/Topic   |  MedPolicyTopicType|
  #	|ulanka        | ACS State Healthcare, LLC. 			 |     DHPMP         |  Commercial   |   Outpatient    |    Ambulance Policy      |  SingleMedPolicy&Topic|
  #	|ulanka        | ACS State Healthcare, LLC. 			 |     TM1TX         |  Medicaid   |   Outpatient    |    Ambulance Policy      |  SingleMedPolicy&Topic|
  ##################################### Scenario-3 ###################################################################
  ### WORKING in Dev2
  @ValidateAssign4 @BOSE2968 @BOSE-3327
  Scenario Outline: BOSE-2968_BOSE-3327-Scenario-Opportunity Deck | Save and Sync with Filter drawer
    Given User "<User1>" logged into "PM" application
    Then The user should view the PresentationManager HomePage
    When user selects "<Client>" from Client drop down list
    And User selects All PayerShorts
    And User selects All  LOBs
    When user selects "All" Claimtypes in filtersection
    And User clicks on "Apply" button to filter
    And User selects Medical Policy/Topic according to "<MedPolicyTopicType>"
    And user filters by clicking on Apply for Medical Policy/Topic
    And The User  "<User1>" has created a PresentationProfile
    And User logsout of the "PM" Application
    Given User "<User2>" logged into "PM" application
    Then The user should view the PresentationManager HomePage
    When user selects "<Client>" from Client drop down list
    And User selects All PayerShorts
    And User selects All  LOBs
    When user selects "All" Claimtypes in filtersection
    And User clicks on "Apply" button to filter
    And User selects Medical Policy/Topic according to "<MedPolicyTopicType>"
    And user filters by clicking on Apply for Medical Policy/Topic
    Given The User  "<User2>" has created a PresentationProfile
    Then The User   "<User2>" must view PresentationProfiles created by him for that client and  by User "<User1>"
    And User logsout of the "PM" Application
    Given User "<User1>" logged into "PM" application
    Then The User   "<User1>" must view PresentationProfiles created by him for that client and  by User "<User2>"
    And User logsout of the "PM" Application

    Examples: 
      | UserName | User1        | User2        | Client         | MedPolicyTopicType |
      | ulanka   | iht_ittest05 | iht_ittest09 | Aetna Medicaid | AllMedicalPolicies |

  ##################################### Scenario-4 ###################################################################
  ### WORKING in Dev2
  @BOSE2810DP @BOSE3422 @BOSE3433
  Scenario Outline: BOSE-2810-Presentation & Available Opportunity Deck| Select DP cards
    Given User "<UserName>" logged into "PM" application
    Then The user should view the PresentationManager HomePage
    When user selects "<Client>" from Client drop down list
    And User selects All PayerShorts
    And User selects All  LOBs
    When user selects "All" Claimtypes in filtersection
    And User clicks on "Apply" button to filter
    And User selects Medical Policy/Topic according to "<MedPolicyTopicType>"
    And user filters by clicking on Apply for Medical Policy/Topic
    Then User must view checkboxes for all the DPCards in the "AvailableOpportunities" deck
    And User should be able to "Select" all DPCards in the "AvailableOpportunities" deck
    And User should be able to "DeSelect" all DPCards in the "AvailableOpportunities" deck
    And User logsout of the "PM" Application

    Examples: 
      | UserName | Client         | MedPolicyTopicType       |
      #|ulanka |Aetna Medicaid	 |       MultipleMedPolicy&Topics|
      #|ulanka |UnitedHealth Group	      |       MultipleMedPolicy&Topics|
      | ulanka   | Aetna Medicaid | MultipleMedPolicy&Topics |

  ##################################### Scenario-5 ###################################################################
  ### WORKING in Dev2
  @BOSE-2811 @Sanity
  Scenario Outline: BOSE-2811- Presentation Deck| Count of DP
    Given User "<UserName>" logged into "PM" application
    Then The user should view the PresentationManager HomePage
    When user selects "<Client>" from Client drop down list
    And user "DESELECT" "Payer Shorts:LOB" under Payershort/LOB section
    And user "SELECT" "Payer Shorts:LOB" under Payershort/LOB section
    When user selects "All" Claimtypes in filtersection
    And user select "MP_TOPIC_ALL" value "" for "DESELECT" operation
    And user select "MP_TOPIC_ALL" value "" for "SELECT" operation
    Given CPMorCMD has created a PresentationProfile
    And User filters the "AvailableDPsDeck" with filter drpdown value "NotStartedOnly" to show related DPs
    And The User have assigned first Medical Policy DPs to the PresentationProfile
    When The CPMorCMD clicks on the "PresentationProfile"
    Then verify the header of "PresentationProfile"
    And verify the count of DP in Presentation deck
    And User logsout of the "PM" Application

    Examples: 
      | UserName | Client             | Payershorts | LOBs              | MedPolicyTopicType             |
      | ulanka   | UnitedHealth Group | AMHDC,AMHDE | Medicaid,Medicare | SingleMedPolicy&MultipleTopics |
