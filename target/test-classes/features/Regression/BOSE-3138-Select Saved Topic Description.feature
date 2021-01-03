#Author: Udayakiran.Lanka
@RegressionNov1 @Regression @EditTopic @BOSE3138 @May22
Feature: BOSE-3138 Select Saved Topic Description

  #***NOTE: Topic is removed from Pres if we unassign all DPs-And PresProfile mapping in the Presentations collections also getting deleted
 
  ############################################## Scenario-1 ######################################################################
  @DPUnassignFromTopic @Demo
  Scenario Outline: BOSE-4035_1 Edit Topic and Unassign DP 
    Given User "<User>" logged into "" application with Services
    Given the "<User>" moving the data from CPW to PM through the service for the client "<Client>" with "Single" DPKey
    Given User "<User>" logged into "PM" application
    Then The user should view the PresentationManager HomePage
    When user selects "<Client>" from Client drop down list
    And User selects  multiple Payershorts "<Payershorts>" from Payershorts FilterSection
    And User selects muliple LOBs "<LOBs>" from LOB FilterSection
    When user selects "All" Claimtypes in filtersection
    And User clicks on "Apply" button to filter
    And user select "TOPIC" value "DB" for "SELECT" operation
    And User filters the "AvailableDPsDeck" with filter drpdown value "NotStartedOnly" to show related DPs
    Given CPMorCMD has created a PresentationProfile
    And The User have assigned some DPs to the PresentationProfile
    When The CPMorCMD clicks on the "PresentationProfile"
    Given User clicks on "EditIcon" for a  topic "TopicName" under medical policy "MedicalPolicy"
    And User edits the Topic description as "<TopicDescription>"
    When User clicks on EditPopup "Submit" button
    Then The system should "Save"  the edited "<TopicDescription>" for the "PresentationProfile" in the DB
    When User Unassigns "<NoOfDPs>" DPs from the "Topic" for "PresentationProfile"
    And Capture "Unassign" decision
    Given User clicks on "EditIcon" for a  topic "TopicName" under medical policy "MedicalPolicy"
    Then The last saved "<TopicDescription>" should be "retained" on Edit popup editable section
    Then The system should "Save"  the edited "<TopicDescription>" for the "PresentationProfile" in the DB
    When User clicks on EditPopup "Close" button
    #When User Unassigns "All" DPs from the "Topic" for "PresentationProfile"
    #Then The system should "Delete"  the edited "<TopicDescription>" for the "PresentationProfile" in the DB
    When User clicks on EditPopup "Close" button

    Examples: 
      | User   | Client             | Payershorts | LOBs | MedPolicyTopicType | TopicDescription    | Client2        | NoOfDPs |
      | ulanka | UnitedHealth Group | All         | All  | AllMedicalPolicies | TestAutomation | Aetna Medicaid |       1 |

  #	 | ulanka |random	 |     All          |  All         |    AllMedicalPolicies    |		Entered for Testing		| Aetna Medicaid           |     1              |
  ############################################## Scenario-2 ######################################################################
  ##NOTE *** Topic is removed from Pres if we unassign all DPs under the Topic
  @DPUnassignFromTopic-UnasignatMPLevel @Demo
  Scenario Outline: BOSE-4035_2 Edit Topic and Unassign DP
    Given User "<User>" logged into "" application with Services
    Given the "<User>" moving the data from CPW to PM through the service for the client "<Client>" with "Single" DPKey
    Given User "<User>" logged into "PM" application
    Then The user should view the PresentationManager HomePage
    When user selects "<Client>" from Client drop down list
    And User selects  multiple Payershorts "<Payershorts>" from Payershorts FilterSection
    And User selects muliple LOBs "<LOBs>" from LOB FilterSection
    When user selects "All" Claimtypes in filtersection
    And User clicks on "Apply" button to filter
    And user select "TOPIC" value "DB" for "SELECT" operation
    And User filters the "AvailableDPsDeck" with filter drpdown value "NotStartedOnly" to show related DPs
    Given CPMorCMD has created a PresentationProfile
    And The User have assigned some DPs to the PresentationProfile
    When The CPMorCMD clicks on the "PresentationProfile"
    Given User clicks on "EditIcon" for a  topic "TopicName" under medical policy "MedicalPolicy"
    And User edits the Topic description as "<TopicDescription>"
    When User clicks on EditPopup "Submit" button
    Then The system should "Save"  the edited "<TopicDescription>" for the "PresentationProfile" in the DB
    When User Unassigns "MedicalPolicy" under which that "Topic" is structured
    Then The system should "Delete"  the edited "<TopicDescription>" for the "PresentationProfile" in the DB

    Examples: 
      | User   | Client             | Payershorts | LOBs | MedPolicyTopicType | TopicDescription    | Client2        |
      | ulanka | UnitedHealth Group | All         | All  | AllMedicalPolicies |  TestAutomation  | Aetna Medicaid |
      #| ulanka |  Coventry Health Care - Aetna Commercial | All         | All  | AllMedicalPolicies | Entered for Testing | Aetna Medicaid |
      
     

  ############################################## Scenario-3 ######################################################################
  @Demo  @AbilitytoSave  
  Scenario Outline: BOSE-3270-Ability to save the Edited Topic description  
    Given User "<User>" logged into "" application with Services
    Given the "<User>" moving the data from CPW to PM through the service for the client "<Client>" with "Single" DPKey
    Given User "<User>" logged into "PM" application
    Then The user should view the PresentationManager HomePage
    When user selects "<Client>" from Client drop down list
    And User selects  multiple Payershorts "<Payershorts>" from Payershorts FilterSection
    And User selects muliple LOBs "<LOBs>" from LOB FilterSection
    When user selects "All" Claimtypes in filtersection
    And User clicks on "Apply" button to filter
    And User selects Medical Policy/Topic according to "<MedPolicyTopicType>"
    And user filters by clicking on Apply for Medical Policy/Topic
    And User filters the "AvailableDPsDeck" with filter drpdown value "NotStartedOnly" to show related DPs
    Given CPMorCMD has created a PresentationProfile
    And The User have assigned some DPs to the PresentationProfile
    When The CPMorCMD clicks on the "PresentationProfile"
    Given User clicks on "EditIcon" for a  topic "TopicName" under medical policy "MedicalPolicy"
    And User edits the Topic description as "<TopicDescription>"
    ###BOSE-3669
    And User clicks on EditPopup "Cancel" button
    Then The element "EditNotSaved" should be "Visible"
    ##BOSE-3663
    Given User clicks on "EditIcon" for a  topic "TopicName" under medical policy "MedicalPolicy"
    And User edits the Topic description as "<TopicDescription>"
    When User clicks on EditPopup "Save" button
    Then The system should "save"  the edited "<TopicDescription>" for the "PresentationProfile" in the DB
    Then The system should "Save"  the edited "<TopicDescription>" for the "PresentationProfile" in the DB
    ##BOSE-3669
    And User clicks on EditPopup "Cancel" button
    Then The element "EditNotSaved" should be "NotVisible"
    ###BOSE-3664 Ability to save the Edited Topic description_retain topic desc
    When User clicks on other "PresentationProfile"
    When The CPMorCMD clicks on the "PresentationProfile"
    Given User clicks on "EditIcon" for a  topic "TopicName" under medical policy "MedicalPolicy"
    Then The last saved "<TopicDescription>" should be "Retained" on Edit popup editable section
    When User clicks on EditPopup "Close" button
    And User logsout of the "PM" Application

    Examples: 
      | User   | Client             | Payershorts | LOBs | MedPolicyTopicType | TopicDescription    |
      #  | ulanka |Dean Health Plan	 |     DHPMP          |  All   |    AllMedicalPolicies|		---Topic Description Entered for Testing----												|
      # | ulanka |Aetna Medicaid	 |     All          |  All   |    AllMedicalPolicies|		TestAutomation											|
      | ulanka | UnitedHealth Group | All         | All  | AllMedicalPolicies |  TestAutomation  |

  ############################################## Scenario-4 ######################################################################
  @DeleteTopicDescr @Demo
  Scenario Outline: BOSE-3270_1 Ability to save the Edited Topic description-BOSE-3664  
    Given User "<User>" logged into "" application with Services
    Given the "<User>" moving the data from CPW to PM through the service for the client "<Client>" with "Single" DPKey
    Given User "<User>" logged into "PM" application
    Then The user should view the PresentationManager HomePage
    When user selects "<Client>" from Client drop down list
    And User selects  multiple Payershorts "<Payershorts>" from Payershorts FilterSection
    And User selects muliple LOBs "<LOBs>" from LOB FilterSection
    When user selects "All" Claimtypes in filtersection
    And User clicks on "Apply" button to filter
    And User selects Medical Policy/Topic according to "<MedPolicyTopicType>"
    And user filters by clicking on Apply for Medical Policy/Topic
    And User filters the "AvailableDPsDeck" with filter drpdown value "NotStartedOnly" to show related DPs
    Given CPMorCMD has created a PresentationProfile
    And The User have assigned some DPs to the PresentationProfile
    When The CPMorCMD clicks on the "PresentationProfile"
    Given User clicks on "EditIcon" for a  topic "TopicName" under medical policy "MedicalPolicy"
    And User edits the Topic description as "<TopicDescription>"
    Then The buttons "Save" should be "disabled"
    Then The buttons "Submit" should be "disabled"

    Examples: 
      | User   | Client             | Payershorts | LOBs | MedPolicyTopicType | TopicDescription |
      # | ulanka |Dean Health Plan	 |     DHPMP          |  All   |    AllMedicalPolicies|		---Topic Description Entered for Testing----												|
      # | ulanka 	|Aetna Medicaid	 |     All          |  All   |    AllMedicalPolicies|		---Entered for Testing----												|
      | ulanka | UnitedHealth Group | All         | All  | AllMedicalPolicies | DeleteTopic      |

  ############################################## Scenario-5 ######################################################################
  @EditTopicLogin
  Scenario Outline: BOSE-3270_2 Ability to save the Edited Topic description-BOSE-3664
    Given User "<User>" logged into "" application with Services
    Given the "<User>" moving the data from CPW to PM through the service for the client "<Client>" with "Single" DPKey
    Given User "<User>" logged into "PM" application
    Then The user should view the PresentationManager HomePage
    When user selects "<Client>" from Client drop down list
    And User selects  multiple Payershorts "<Payershorts>" from Payershorts FilterSection
    And User selects muliple LOBs "<LOBs>" from LOB FilterSection
    When user selects "All" Claimtypes in filtersection
    And User clicks on "Apply" button to filter
    And User selects Medical Policy/Topic according to "<MedPolicyTopicType>"
    And user filters by clicking on Apply for Medical Policy/Topic
    And User filters the "AvailableDPsDeck" with filter drpdown value "NotStartedOnly" to show related DPs
    Given CPMorCMD has created a PresentationProfile
    And The User have assigned some DPs to the PresentationProfile
    When The CPMorCMD clicks on the "PresentationProfile"
    Given User clicks on "EditIcon" for a  topic "TopicName" under medical policy "MedicalPolicy"
    And User edits the Topic description as "<TopicDescription>"
    When User clicks on EditPopup "Save" button
    When User clicks on EditPopup "Close" button
    And User logsout of the "PM" Application
    Given User "<User2>" logged into "PM" application
    When user selects "<Client>" from Client drop down list
    When The CPMorCMD clicks on the "PresentationProfile"
    Given User clicks on "EditIcon" for a  topic "TopicName" under medical policy "MedicalPolicy"
    Then The last saved "<TopicDescription>" should be "retained" on Edit popup editable section
    When User clicks on EditPopup "Close" button
    And User logsout of the "PM" Application

    Examples: 
      | User   | Client             | Payershorts | LOBs | MedPolicyTopicType | TopicDescription    | User2        |
      # | ulanka |Dean Health Plan	 |     DHPMP          |  All   |    AllMedicalPolicies     |		---Entered for Testing----		|  iht_ittest05        |
      #| ulanka |Aetna Medicaid	 |     All          |  All   |    AllMedicalPolicies                |		---Entered for Testing----		|  iht_ittest05       |
      | ulanka | UnitedHealth Group | All         | All  | AllMedicalPolicies | TestAutomation | iht_ittest05 |

  ############################################## Scenario-6 ######################################################################
  @SubmitTopic  @NextSubmit
  Scenario Outline: BOSE-3269 -Ability to Submit the changes on the edit topic pop up 
    Given User "<User>" logged into "" application with Services
    Given the "<User>" moving the data from CPW to PM through the service for the client "<Client>" with "Single" DPKey
    Given User "<User>" logged into "PM" application
    Then The user should view the PresentationManager HomePage
    When user selects "<Client>" from Client drop down list
    And User selects  multiple Payershorts "<Payershorts>" from Payershorts FilterSection
    And User selects muliple LOBs "<LOBs>" from LOB FilterSection
    When user selects "All" Claimtypes in filtersection
    And User clicks on "Apply" button to filter
    And User selects Medical Policy/Topic according to "<MedPolicyTopicType>"
    And user filters by clicking on Apply for Medical Policy/Topic
    And User filters the "AvailableDPsDeck" with filter drpdown value "NotStartedOnly" to show related DPs
    Given CPMorCMD has created a PresentationProfile
    And The User have assigned some DPs to the PresentationProfile
    When The CPMorCMD clicks on the "PresentationProfile"
    Given User clicks on "EditIcon" for a  topic "TopicName" under medical policy "MedicalPolicy"
    And User edits the Topic description as "<TopicDescription>"
    When User clicks on EditPopup "Submit" button
    Then The "EditTopicDescription" popup should be closed
    Then The system should "save"  the edited "<TopicDescription>" for the "PresentationProfile" in the DB
    Then The element "TopicEditSaved" should be "Visible"
    When User clicks the "TopicSavedClose" button
    Then The element "TopicEditSaved" should be "NotVisible"
    ##Select another Client
    When user selects "<Client2>" from Client drop down list
    When user selects "<Client>" from Client drop down list
    When The CPMorCMD clicks on the "PresentationProfile"
    Given User clicks on "EditIcon" for a  topic "TopicName" under medical policy "MedicalPolicy"
    Then The last saved "<TopicDescription>" should be "retained" on Edit popup editable section
    When User clicks on EditPopup "Close" button
    And User logsout of the "PM" Application

    Examples: 
      | User   | Client             | Payershorts | LOBs | MedPolicyTopicType | TopicDescription    | Client2        |
      | ulanka | UnitedHealth Group | All         | All  | AllMedicalPolicies | TestAutomation    | Aetna Medicaid |

  ############################################## Scenario-7 ######################################################################
  @DPDescr  @NextOne
  Scenario Outline: BOSE-3753 DP numbers and DP description on Edit topic pop up BOSE-3866  BOSE-3872
    Given User "<User>" logged into "" application with Services
    Given the "<User>" moving the data from CPW to PM through the service for the client "<Client>" with "Single" DPKey
    Given User "<User>" logged into "PM" application
    Then The user should view the PresentationManager HomePage
    When user selects "<Client>" from Client drop down list
    And User selects  multiple Payershorts "<Payershorts>" from Payershorts FilterSection
    And User selects muliple LOBs "<LOBs>" from LOB FilterSection
    When user selects "All" Claimtypes in filtersection
    And User clicks on "Apply" button to filter
    And User selects Medical Policy/Topic according to "<MedPolicyTopicType>"
    And user filters by clicking on Apply for Medical Policy/Topic
    And User filters the "AvailableDPsDeck" with filter drpdown value "NotStartedOnly" to show related DPs
    Given CPMorCMD has created a PresentationProfile
    And The User have assigned some DPs to the PresentationProfile
    When The CPMorCMD clicks on the "PresentationProfile"
    Given User clicks on "EditIcon" for a  topic "TopicName" under medical policy "MedicalPolicy"
    When User clicks on EditPopup "Close" button
    And User captures the "DPs" for the "Topic"
    Given User clicks on "EditIcon" for a  topic "TopicName" under medical policy "MedicalPolicy"
    Then The "DPKey" should be "NotSelected"  in  "EditTopicPopup"
    And "DPSection" headers should be in defined format in "EditTopicPopup"
    Then The  "EditTopicPopup" should display  DPs list that are assigned under that "Topic" for the "PresentationProfile"
    #And "DPDescription" headers should be in defined format in "EditTopicPopup"
    Then User selects a "DPKey" then the "DPDescription" should be displayed in the  "EditTopicPopup" popup
    When User clicks on EditPopup "Close" button
    And User logsout of the "PM" Application

    Examples: 
      | User   | Client             | Payershorts | LOBs | MedPolicyTopicType | TopicDescription    | Client2        |
      | ulanka | UnitedHealth Group | All         | All  | AllMedicalPolicies | TestAutomation| Aetna Medicaid |

  # | ulanka|Aetna Medicaid	 |     All          |  All   |    AllMedicalPolicies|		Entered for Testing		| Aetna Medicaid           |
  ############################################## Scenario-8 ######################################################################
  @BOSE3135  @Next
  Scenario Outline: BOSE-3135 -Presentation Deck: Edit Topic Popup window
    Given User "<User>" logged into "" application with Services
    Given the "<User>" moving the data from CPW to PM through the service for the client "<Client>" with "Single" DPKey
    Given User "<User>" logged into "PM" application
    Then The user should view the PresentationManager HomePage
    When user selects "<Client>" from Client drop down list
    And User selects  multiple Payershorts "<Payershorts>" from Payershorts FilterSection
    And User selects muliple LOBs "<LOBs>" from LOB FilterSection
    When user selects "All" Claimtypes in filtersection
    And User clicks on "Apply" button to filter
    And User selects Medical Policy/Topic according to "<MedPolicyTopicType>"
    And user filters by clicking on Apply for Medical Policy/Topic
    And User filters the "AvailableDPsDeck" with filter drpdown value "NotStartedOnly" to show related DPs
    Given CPMorCMD has created a PresentationProfile
    And The User have assigned some DPs to the PresentationProfile
    When The CPMorCMD clicks on the "PresentationProfile"
    Then The Edit icon should get displayed at the Topic level
    Then The User clicks on the Edit icon and Popup should be displayed with buttons Save Submit and Cancel
    And The "EditTopic" popup should display "TopicName" as Header
    And The TopicDescription should be in "ReadOnly" mode in "OriginalTopicSection"
    And The TopicDescription should be in "Editable" mode in "TopicEditableSection"
    And The Both sections should have Expand/collapse icons with sections in expanded mode by default
    And The Expand/collapse icons should be functional
    And User clicks on EditPopup "Cancel" button
    And User logsout of the "PM" Application

    Examples: 
      | User   | Client             | Payershorts | LOBs | MedPolicyTopicType |
      # | ulanka |Aetna Medicaid	 |     MONTE          |  All   |    SingleMedPolicy&MultipleTopics|
      # | ulanka |Dean Health Plan	 |     DHPMP          |  All   |    SingleMedPolicy&MultipleTopics|
      | ulanka | UnitedHealth Group | All         | All  | AllMedicalPolicies |

  ############################################## Scenario-9 ######################################################################
  #Chaitanya Kumar Natuva
  @PCA18283 @PCA18283_1
  Scenario Outline: PCA18283_1 verify edit functionality in 'Edit Topic' popup at presentation profile
    Given User "<User>" logged into "" application with Services
    Given the "<User>" moving the data from CPW to PM through the service for the client "<Client>" with "Single" DPKey
    Then Create the presenatation profile with given request data client "<Client>" payershort "" lob "" product "" and prority "" with "<User>"
    Then assign multiple dps to created profile
    Then User "<User>" logged into "PM" application
    When user selects "<Client>" from Client drop down list
    Then The CPMorCMD clicks on the "Presentation having DPs"
    And Apply All Payers and LOBs filters in Presenation profile
    And Expand all the items in presenation hierarchy view
    Then verify "Edit" functionality in Edit Topic popup

    Examples: 
      | User   | Client             |
      | nkumar | UnitedHealth Group |
 ############################################## Scenario-10 ######################################################################
  #Chaitanya Kumar Natuva
  @PCA18283 @PCA18283_2
  Scenario Outline: PCA18283_2 verify Delete functionality in 'Edit Topic' popup at presentation profile
    Given User "<User>" logged into "" application with Services
    Given the "<User>" moving the data from CPW to PM through the service for the client "<Client>" with "Single" DPKey
    Then Create the presenatation profile with given request data client "<Client>" payershort "" lob "" product "" and prority "" with "<User>"
    Then assign multiple dps to created profile
    Then User "<User>" logged into "PM" application
    When user selects "<Client>" from Client drop down list
    Then The CPMorCMD clicks on the "Presentation having DPs"
    And Apply All Payers and LOBs filters in Presenation profile
    And Expand all the items in presenation hierarchy view
    Then verify "Delete" functionality in Edit Topic popup

    Examples: 
      | User   | Client             |
      | nkumar | UnitedHealth Group |