#Author: shailaja.nuthi@cotiviti.com
@Kalam
Feature: Cross Over| Copy Decision from a PPS to another PPS

  @PCA6417
  Scenario Outline: PCA_6417, Available DP deck| Display DP type dropdown
    Given User "<User>" logged into "PM" application
    When user selects "<Client>" from Client drop down list
    Then User validates "Cross Over Opportunities" in "NPP Opportunities Dropdown"
    And User logsout of the "PM" Application

    Examples: 
      | User   | Client |
      | nkumar | Cigna  |


  @PCA15171
  Scenario Outline: Presentation Profile| Display Core Policy DPs
    Given User "<User>" logged into "PM" application
    When user selects "<Client>" from Client drop down list
    And User selects  multiple Payershorts "DHPMP" from Payershorts FilterSection
    And User selects muliple LOBs "Medicare" from LOB FilterSection
    When user selects "S" Claimtypes in filtersection
    And User clicks on "Apply" button to filter
    And User selects Medical Policy/Topic according to "AllMedicalPolicies"
    And user filters by clicking on Apply for Medical Policy/Topic
    Then User validates "DPType Data" in "Presentation Profile"

    Examples: 
      | User   | Client           |
      | nkumar | Dean Health Plan |


  @PCA6418
  Scenario Outline: PCA-6418,PCA-15155 Crossover Opportunity and Cross-Over Filter Section | Template
    Given User "<User>" logged into "PM" application
    When user selects "<Client>" from Client drop down list
    Then user should select "Cross Over Opportunities" tab
    And User validates "Template" in "Cross Over Opportunities"
    #When User Select "Payer Short" as "HECNA" Under "Source"
    #When User Select "LOB" as "Medicare" Under "Source"
    #When User Select "Claim Type" as "A" Under "Source"
    #When User Select "Payer Short" as "HSILM" Under "Target"
    #When User Select "LOB" as "Medicare" Under "Target"
    #When User Select "Claim Type" as "A" Under "Target"
    #When User Select "Decision Type" as "All" Under ""
    #And User clicks on "Apply" button to filter
    Then User validates "Hierarchical View" in "Cross Over Opportunities"
    And User logsout of the "PM" Application

    Examples: 

      | User   | Client     |
      | nkumar | Health Net |


  @PCA-15133
  Scenario Outline: Validate Source & Target PPS
    Given User "<User>" logged into "PM" application
    When user selects "<Client>" from Client drop down list
    Then user should select "Cross Over Opportunities" tab
    When User Select "Payer Short" as "RandomData" Under "Source"
    When User Select "LOB" as "Federal Employee Program" Under "Source"
    When User Select "Claim Type" as "A" Under "Source"
    When User Select "Payer Short" as "RandomData" Under "Target"
    When User Select "LOB" as "Commercial" Under "Target"
    When User Select "Claim Type" as "A" Under "Target"
    And User clicks on "Apply" button to filter
    Then system should display a msg "Please select Source Payer Policy Set"
    When User Select "LOB" as "Medicaid" Under "Source"
    When User Select "LOB" as "Medicaid" Under "Target"
    And User clicks on "Apply" button to filter
    Then system should display a msg "Please select Target Payer Policy Set"
    When User Select "LOB" as "Federal Employee Program" Under "Source"
    When User Select "LOB" as "Federal Employee Program" Under "Target"
    And User clicks on "Apply" button to filter
    Then system should display a msg "Please select Source and Target Payer Policy Set"
    #When User Select "LOB" as "Medicaid" Under "Source"
    #When User Select "LOB" as "Medicaid" Under "Target"
    #And User clicks on "Apply" button to filter
    #Then system should display a msg "Please select Target Payer Policy Set"
    And User logsout of the "PM" Application

    Examples: 
      | User         | Client                            |
      | iht_ittest09 | Harvard Pilgrim Health Care, Inc. |

  @PCA-15153
  Scenario Outline: Validate Source & Target PPS
    Given User "<User>" logged into "PM" application
    When user selects "<Client>" from Client drop down list
    Then user should select "Cross Over Opportunities" tab
    When User Select "Payer Short" as "RandomData" Under "Source"
    When User Select "LOB" as "Commercial" Under "Source"
    When User Select "Claim Type" as "A" Under "Source"
    When User Select "Payer Short" as "RandomData" Under "Target"
    When User Select "LOB" as "Commercial" Under "Target"
    When User Select "Claim Type" as "A" Under "Target"
    
    When User Select "Decision Type" as "Approve" Under ""
    And User clicks on "Apply" button to filter
    And validate Decision "Approve Library" count with Mongo
    
    When User Select "Decision Type" as "Approve with Mod" Under ""
    And User clicks on "Apply" button to filter
    And validate Decision "Approve With Modifications" count with Mongo
    
    When User Select "Decision Type" as "Reject" Under ""
    And User clicks on "Apply" button to filter
    And validate Decision "Reject" count with Mongo
    
    And User logsout of the "PM" Application

    Examples: 
      | User         | Client           |
      | iht_ittest09 | Dean Health Plan |

