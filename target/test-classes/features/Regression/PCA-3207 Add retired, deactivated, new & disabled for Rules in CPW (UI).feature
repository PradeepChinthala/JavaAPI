#Author: Santosh.aerramoni
@Regression @PCA3207
Feature: PCA-3207-Add retired, deactivated, new & disabled for Rules in CPW (UI)

 #####################################################   Scenario-1  #############################################################
  @PCA-3207
  Scenario Outline: PCA-3207-Ability to view flag for the rule in CPW UI(AWB)
    Given the "<UserName>" is logged into the CPW application
    When user click on client "<Client>" with release "<Release>" in the Opportunity Dashboard
    And validate the rule status based on "<Status>"
    #And "<UserName>" clicks on "DP Description" link of AWB
    Then Logout CPW application

    Examples: 
      | UserName     | Client         | Release  | Status      |
      | iht_ittest05 | Cigna | NOV 2019 | RETIRED     |
      | iht_ittest05 | Cigna | NOV 2019 | DISABLED    |
      | iht_ittest05 | UnitedHealth Group | NOV 2019 | DEACTIVATED |

       #####################################################   Scenario-2  #############################################################
  #@PCA-3762
  #Scenario Outline: PCA-3762-Remove the retired/deactivated DPs and topics from PM & CPW WOB
    #Given the "<UserName>" is logged into the CPW application
    #When user click on client "<Client>" with release "<Release>" in the Opportunity Dashboard
    #Then verify the retired DP/Topic in "<Page>"
    #Then Logout CPW application
#
    #Examples: 
      #| UserName     | Client         | Release  | Page | Status  |
      #| iht_ittest05 | Aetna Medicaid | NOV 2019 | AWB  | RETIRED |
      #| iht_ittest05 | Aetna Medicaid | NOV 2019 | RWO  | RETIRED |

  #####################################################   Scenario-3  #############################################################
  #@PCA-3762_2
  #Scenario Outline: PCA-3762_2-Remove the retired/deactivated DPs and topics from PM
    #Given User "<UserName>" logged into "PM" application
    #When user selects "<Client>" from Client drop down list
    #And User selects  multiple Payershorts "All" from Payershorts FilterSection
    #And User selects muliple LOBs "All" from LOB FilterSection
    #When user selects "All" Claimtypes in filtersection
    #And User clicks on "Apply" button to filter
    #Then verify the retired DP/Topic in "<Page>"
    #And User logsout of the "PM" Application
#
    #Examples: 
      #| UserName     | Client         | Release  | Page | Status  |
      #| iht_ittest05 | Aetna Medicaid | NOV 2019 | PM   | RETIRED |
      