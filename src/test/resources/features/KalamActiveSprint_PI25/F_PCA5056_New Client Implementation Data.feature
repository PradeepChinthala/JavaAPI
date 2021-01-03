#Author: shailaja.nuthi@cotiviti.com
@Regression @Kalam
Feature: New Client Implementation Data
#Test Data dependent scenario's,Config DP's must exist to run scenario's#
  @PCA15167
  Scenario Outline: PCA_15167,PCA_15170 Available DP deck| Display DP type dropdown
    Given User "<User>" logged into "" application with Services
    Given the "<User>" moving the data from CPW to PM through the service for the client "<Client>" with "Single" DPKey
    Then Create the presenatation profile with given request data client "<Client>" payershort "" lob "" product "" and prority "" with "<User>"
    Then assign multiple dps to created profile
    Then User "<User>" logged into "PM" application
    When user selects "<Client>" from Client drop down list
    Then User validates "DP Type" in "Available Oppurtunities"
    Then The CPMorCMD clicks on the "Presentation having DPs"
    Then User validates "DP Type" in "Available Oppurtunities"
    Given CPMorCMD has created a PresentationProfile
    And The User clicks on the "NewPresentationProfile"
    #Then The buttons "DP Type" should be "disabled"
    And User logsout of the "PM" Application

    Examples: 
      | User   | Client             |
      | nkumar | UnitedHealth Group |

  @PCA16200
  Scenario: Validate DP's from Oracle
    When User compares "DP's" from Oracle to Mongo

  @PCA-15652
  Scenario Outline: PCA-15652 Available DP Deck| Display Configuration Only DPs
    Given User "<User>" logged into "PM" application
    When user selects "<Client>" from Client drop down list
    When User selects "<PPS>" and validate DP's with DP Type as "Configuration" and latest decision as "Approve Lib"
    When User selects "<PPS1>" and validate DP's with DP Type as "Configuration" and latest decision as "Supress"
    When User selects "<PPS2>" and validate DP's with DP Type as "Configuration" and latest decision as "Supress and No Decision"
    When User selects "<PPS3>" and validate DP's with DP Type as "Configuration" and latest decision as "Approve Lib and No Decision"
    And User logsout of the "PM" Application

    Examples: 
      | User   | Client           | PPS                         | PPS1                        | PPS2               | PPS3               |
      | nkumar | Health Net, Inc. | HNTCA:Commercial,Medicare:O | HNTCA:Commercial,Medicare:A | HNTCA:Medicare:A,O | HNTCA:Medicare:A,O |

  @PCA-15168
  Scenario Outline: PCA-15168 Available DP Deck| Display Configuration Only DPs
    Given User "<User>" logged into "PM" application
    When user selects "<Client>" from Client drop down list
    When User selects "<PPS>" and validate DP's with DP Type as "Information" and latest decision as ""
    And User logsout of the "PM" Application

    Examples: 
      | User   | Client           | PPS              |
      | nkumar | Health Net, Inc. | HNTCA:Medicare:A |

  @PCA-15169_1
  Scenario Outline: Assign Informational & Configuration DPs for "<Validation>"
    Given User "<User>" logged into "PM" application
    When user selects "<Client>" from Client drop down list
    And User selects  multiple Payershorts "All" from Payershorts FilterSection
    And User selects muliple LOBs "All" from LOB FilterSection
    When user selects "All" Claimtypes in filtersection
    And User clicks on "Apply" button to filter
    And User selects Medical Policy/Topic as "<Medical Policy>"
    And User selects Medical Policy/Topic according to "AllMedicalPolicies"
    And user filters by clicking on Apply for Medical Policy/Topic
    Then User validates the Assignment of Dp's for the Profile "<Validation>" with DPType as "<DPType>"
    And User logsout of the "PM" Application

    Examples: 
      | User         | Client                           | Medical Policy        | Validation     | DPType            |
      | iht_ittest09 | Blue Cross Blue Shield Minnesota | CMS Coverage Policies | DP+PPS Partial | Rules,No DP,No DP |

  @PCA-15169
  Scenario Outline: Assign Informational & Configuration DPs with "<Profile1>"
    Given User "<User>" logged into "PM" application
    When user selects "<Client>" from Client drop down list
    And User selects  multiple Payershorts "All" from Payershorts FilterSection
    And User selects muliple LOBs "All" from LOB FilterSection
    When user selects "All" Claimtypes in filtersection
    And User clicks on "Apply" button to filter
    And User selects Medical Policy/Topic as "<Medical Policy>"
    And User selects Medical Policy/Topic according to "AllMedicalPolicies"
    And user filters by clicking on Apply for Medical Policy/Topic
    Then User validates the Assignment of Dp's for the Profile "<Profile1>" with DPType as "<DPType1>"
    Then User validates the Assignment of Dp's for the Profile "<Profile2>" with DPType as "<DPType2>"
    And User logsout of the "PM" Application

    Examples: 
      | User         | Client                           | Medical Policy                      | Profile1                | Profile2                    | DPType1                                   | DPType2                                   |
      | iht_ittest09 | UnitedHealth Group               | Multiple Procedure Reduction Policy | DP+PPS With All DPTypes | New DP+PPS With All DPTypes | Rules,Information Only,Configuration Only | Rules,Information Only,No DP              |
      | iht_ittest09 | Wellmark                         | Multiple Procedure Reduction Policy | DP Level                | New DP Level                | Rules,Information Only,Configuration Only | Rules,Information Only,No DP              |
      | iht_ittest09 | Blue Cross Blue Shield Minnesota | Multiple Procedure Reduction Policy | Topic Level             | New Topic Level             | Rules,Information Only,Configuration Only | Rules,Information Only,Configuration Only |

  @PCA-18185_1to5 @PCA-15172_1to3
  Scenario Outline: Assign Informational & Configuration DPs with "<Functionality>"
    Given User "<User>" logged into "PM" application
    When user selects "<Client>" from Client drop down list
    And User selects  multiple Payershorts "All" from Payershorts FilterSection
    And User selects muliple LOBs "All" from LOB FilterSection
    When user selects "All" Claimtypes in filtersection
    And User clicks on "Apply" button to filter
    And User selects Medical Policy/Topic as "<Medical Policy>"
    And User selects Medical Policy/Topic according to "AllMedicalPolicies"
    And user filters by clicking on Apply for Medical Policy/Topic
    Then User validates "Re-Assign" functionality for the validation "Rules,Information Only,Configuration Only" and DPType "<Functionality>"
    And User logsout of the "PM" Application

    Examples: 
      | User         | Client | Medical Policy                      | Functionality      |
      | iht_ittest09 | Anthem | Multiple Procedure Reduction Policy | Configuration Only |
      | iht_ittest09 | Anthem | Multiple Procedure Reduction Policy | Information Only   |

  @PCA-18185_6to10 @PCA-15172_4to6
  Scenario Outline: Assign Informational & Configuration DPs with "<DPType>" and "<Functionality>"
    Given User "<User>" logged into "PM" application
    When user selects "<Client>" from Client drop down list
    And User selects  multiple Payershorts "All" from Payershorts FilterSection
    And User selects muliple LOBs "All" from LOB FilterSection
    When user selects "All" Claimtypes in filtersection
    And User clicks on "Apply" button to filter
    And User selects Medical Policy/Topic as "<Medical Policy>"
    And User selects Medical Policy/Topic according to "AllMedicalPolicies"
    And user filters by clicking on Apply for Medical Policy/Topic
    Then User validates "Unassign" functionality for the validation "<DPType>" and DPType "<Functionality>"
    And User logsout of the "PM" Application

    Examples: 
      | User         | Client           | Medical Policy                      | DPType                  | Functionality      |
      | iht_ittest09 | Dean Health Plan | Multiple Procedure Reduction Policy | DP+PPS Partial          | Configuration Only |
      | iht_ittest09 | Dean Health Plan | Multiple Procedure Reduction Policy | DP+PPS With All DPTypes | Configuration Only |
      | iht_ittest09 | Dean Health Plan | Multiple Procedure Reduction Policy | DP Level                | Configuration Only |
      | iht_ittest09 | Dean Health Plan | Multiple Procedure Reduction Policy | Topic Level             | Configuration Only |
      | iht_ittest09 | Dean Health Plan | Multiple Procedure Reduction Policy | MP Level                | Configuration Only |
      | iht_ittest09 | Dean Health Plan | Multiple Procedure Reduction Policy | DP Level                | Information Only   |
      | iht_ittest09 | Dean Health Plan | Multiple Procedure Reduction Policy | Topic Level             | Information Only   |
      | iht_ittest09 | Dean Health Plan | Multiple Procedure Reduction Policy | MP Level                | Information Only   |

  @PCA-18185_11to19 @PCA-18257 @PCA-18723
  Scenario Outline: Assign Informational & Configuration DPs with "<sValidation>"
    Given User "<User>" logged into "PM" application
    When user selects "<Client>" from Client drop down list
    And User selects  multiple Payershorts "All" from Payershorts FilterSection
    And User selects muliple LOBs "All" from LOB FilterSection
    When user selects "All" Claimtypes in filtersection
    And User clicks on "Apply" button to filter
    And User selects Medical Policy/Topic as "<Medical Policy>"
    And User selects Medical Policy/Topic according to "AllMedicalPolicies"
    And user filters by clicking on Apply for Medical Policy/Topic
    Then User validates "<sValidation>" functionality for the validation "<DPType>" and DPType ""
    And User logsout of the "PM" Application

    Examples: 
      | User         | Client           | Medical Policy                      | DPType                  | sValidation   |
      | iht_ittest09 | Cigna            | Modifier Processing Policy          | DP+PPS With All DPTypes | All Decisions |
      | iht_ittest09 | Dean Health Plan | Multiple Procedure Reduction Policy | DP+PPS With All DPTypes | Capture       |

  @PCA19444
  Scenario Outline: Ability to Unassign & Reassign Test decisions
    Given User "<User>" logged into "PM" application
    When user selects "<Client>" from Client drop down list
    And User selects  multiple Payershorts "All" from Payershorts FilterSection
    And User selects muliple LOBs "All" from LOB FilterSection
    When user selects "All" Claimtypes in filtersection
    And User clicks on "Apply" button to filter
    And User selects Medical Policy/Topic as "<Medical Policy>"
    And User selects Medical Policy/Topic according to "AllMedicalPolicies"
    And user filters by clicking on Apply for Medical Policy/Topic
    Then User validates "Test Decisions" functionality for the validation "<DPType>" and DPType "<Functionality>"
    And User logsout of the "PM" Application

    Examples: 
      | User         | Client           | Medical Policy             | DPType                  | Functionality      |
      | iht_ittest09 | Dean Health Plan | Modifier Processing Policy | DP+PPS Partial Test     | Configuration Only |
      #| iht_ittest09 | Dean Health Plan | Modifier Processing Policy | DP+PPS With All DPTypes | Configuration Only |
      #| iht_ittest09 | Dean Health Plan | Modifier Processing Policy | DP Level                | Configuration Only |
      #| iht_ittest09 | Dean Health Plan | Modifier Processing Policy | Topic Level             | Configuration Only |
      #| iht_ittest09 | Dean Health Plan | Modifier Processing Policy | MP Level                | Configuration Only |

  @PCA-19846 @PCA-17408
  Scenario Outline: Validation of Edit Topic functionality at profile level
    Given User "<User>" logged into "PM" application
    When user selects "<Client>" from Client drop down list
    And User selects  multiple Payershorts "All" from Payershorts FilterSection
    And User selects muliple LOBs "All" from LOB FilterSection
    When user selects "All" Claimtypes in filtersection
    And User clicks on "Apply" button to filter
    And User selects Medical Policy/Topic as "<Medical Policy>"
    And User selects Medical Policy/Topic according to "AllMedicalPolicies"
    And user filters by clicking on Apply for Medical Policy/Topic
    Then User validates the Assignment of Dp's for the Profile "<Validation>" with DPType as "<DPType>"
    And validate edit topic description
    And User logsout of the "PM" Application

    Examples: 
      | User         | Client                          | Medical Policy        | Validation              | DPType            |
      | iht_ittest09 | Blue Cross Blue Shield Michigan | CMS Coverage Policies | DP+PPS With All DPTypes | Rules,No DP,No DP |

  @PCA-17887
  Scenario Outline: Validating Ready for Presentation Functionality
    Given User "<User>" logged into "PM" application
    When user selects "<Client>" from Client drop down list
    And User selects  multiple Payershorts "All" from Payershorts FilterSection
    And User selects muliple LOBs "All" from LOB FilterSection
    When user selects "All" Claimtypes in filtersection
    And User clicks on "Apply" button to filter
    And User selects Medical Policy/Topic as "<Medical Policy>"
    And User selects Medical Policy/Topic according to "AllMedicalPolicies"
    And user filters by clicking on Apply for Medical Policy/Topic
    Then User validates the Assignment of Dp's for the Profile "<Validation>" with DPType as "<DPType>"
    Then capture decision to one PPS combination
    And User logsout of the "PM" Application

    Examples: 
      | User         | Client                          | Medical Policy  | Validation              | DPType            |
      | iht_ittest09 | Blue Cross Blue Shield Michigan | Modifier Policy | DP+PPS With All DPTypes | Rules,No DP,No DP |
