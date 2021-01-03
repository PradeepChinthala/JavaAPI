#Author: chaitanya kumar natuva
@Regression @PCA2556
Feature: PCA2556

  #==>Sceanrio:1 MP/Topics with savings for filter Payershorts,Claimtypes and LOBs 'All'
  #==>Sceanrio:2 MP/Topics with savings for filter Payershorts,Claimtypes and LOBs 'with filter'
  #==>Sceanrio:3 PayerLOB grid in available opportunity deck for filter Payershorts,Claimtypes and LOBs 'All'
  #==>Sceanrio:4 PayerLOB grid in available opportunity deck for filter Payershorts,Claimtypes and LOBs 'with filter'
  #####################################################   Scenario-1  #############################################################
  @PCA2556_1
  Scenario Outline: PCA2556_1 validate the Medicalpolicies and thier savings for the applied "ALL" PPS in filter section
    Given User "<User>" logged into "PM" application
    When user selects "<Client>" from Client drop down list
    And User selects  multiple Payershorts "All" from Payershorts FilterSection
    And User selects muliple LOBs "All" from LOB FilterSection
    When user selects "All" Claimtypes in filtersection
    And User clicks on "Apply" button to filter
    Then validate the "Medicalpolicies" and thier raw savings with DB in filter drawer section

    Examples: 
      | User   | Client |
      | nkumar | Humana |

  #####################################################   Scenario-2  #############################################################
  @PCA2556_2
  Scenario Outline: PCA2556_2 validate the Topics and thier savings for the applied "ALL" PPS in filter section
    Given User "<User>" logged into "PM" application
    When user selects "<Client>" from Client drop down list
    And User selects  multiple Payershorts "All" from Payershorts FilterSection
    And User selects muliple LOBs "All" from LOB FilterSection
    When user selects "All" Claimtypes in filtersection
    And User clicks on "Apply" button to filter
    Then validate the "Topics" and thier raw savings with DB in filter drawer section

    Examples: 
      | User   | Client |
      | nkumar | Humana |

  #####################################################   Scenario-3  #############################################################
  @PCA2556_3
  Scenario Outline: PCA2556_3 validate the Medicalpolicies and thier savings for the applied "filter" PPS in filter section
    Given User "<User>" logged into "PM" application
    When user selects "<Client>" from Client drop down list
    And User selects  multiple Payershorts "<Payershort>" from Payershorts FilterSection
    And User selects muliple LOBs "<LOB>" from LOB FilterSection
    When user selects "<Claimtypes>" Claimtypes in filtersection
    And User clicks on "Apply" button to filter
    Then validate the "Medicalpolicies with filter" and thier raw savings with DB in filter drawer section

    Examples: 
      | User   | Client | Payershort  | LOB        | Claimtypes |
      | nkumar | Humana | HUMCC,HUMCF | Commercial | A          |

  #####################################################   Scenario-4  #############################################################
  @PCA2556_4
  Scenario Outline: PCA2556_4 validate the Topics and thier savings for the applied "filter" PPS in filter section
    Given User "<User>" logged into "PM" application
    When user selects "<Client>" from Client drop down list
    And User selects  multiple Payershorts "<Payershort>" from Payershorts FilterSection
    And User selects muliple LOBs "<LOB>" from LOB FilterSection
    When user selects "<Claimtypes>" Claimtypes in filtersection
    And User clicks on "Apply" button to filter
    Then validate the "Topics with filter" and thier raw savings with DB in filter drawer section

    Examples: 
      | User   | Client | Payershort  | LOB      | Claimtypes |
      | nkumar | Humana | HUMCC,HUMCF | Medicare | A          |

  #####################################################   Scenario-5  #############################################################
  @PCA2556_5
  Scenario Outline: PCA2556_5 validate the DP Payer_LOB grid and thier savings for the applied "ALL" PPS in filter section
    Given User "<User>" logged into "PM" application
    When user selects "<Client>" from Client drop down list
    And User selects  multiple Payershorts "All" from Payershorts FilterSection
    And User selects muliple LOBs "All" from LOB FilterSection
    When user selects "All" Claimtypes in filtersection
    And User clicks on "Apply" button to filter
    Then validate the "DPKey" and thier raw savings with DB in filter drawer section

    Examples: 
      | User   | Client |
      | nkumar | Humana  |

  #####################################################   Scenario-6  #############################################################
  @PCA2556_6
  Scenario Outline: PCA2556_6 validate the DP Payer_LOB grid and thier savings for the applied "filter" PPS in filter section
    Given User "<User>" logged into "PM" application
    When user selects "<Client>" from Client drop down list
    And User selects  multiple Payershorts "<Payershort>" from Payershorts FilterSection
    And User selects muliple LOBs "<LOB>" from LOB FilterSection
    When user selects "<Claimtypes>" Claimtypes in filtersection
    And User clicks on "Apply" button to filter
    Then validate the "DPKey with filter" and thier raw savings with DB in filter drawer section

    Examples: 
      | User   | Client | Payershort  | LOB        | Claimtypes |
       | nkumar | Humana | HUMCC,HUMCF | Commercial | A          |
