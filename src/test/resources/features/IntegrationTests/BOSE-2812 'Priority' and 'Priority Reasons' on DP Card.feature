#Author: chaitanya kumar natuva
@Regression @BOSE2812
Feature: BOSE2812

  #===> Single DP,Single Priority
  #===> Single DP,Multiple Priority
  #===> Multiple DP,Single Priority
  #===> Multiple DP,Multiple Priority
  #####################################################   Scenario-1  #############################################################
  @BOSE2812_1
  Scenario Outline: BOSE2812_1 Ability to view the Priority "<Priority>" and 'Priority Reasons' on the captured DP Card "<DPKey>" at DPLevel
    Given User "<User>" logged into "" application with Services
    Given the "<User>" moving the data from CPW to PM through the service for the client "<Client>" with "<DPKey>" DPKey and Priority as "<Priority>"
    Given User "<User>" logged into "PM" application
    When user selects "<Client>" from Client drop down list
    And User selects  multiple Payershorts "All" from Payershorts FilterSection
    And User selects muliple LOBs "All" from LOB FilterSection
    When user selects "All" Claimtypes in filtersection
    And User clicks on "Apply" button to filter
    And User selects Medical Policy/Topic according to "Captured Topic"
    And user filters by clicking on Apply for Medical Policy/Topic
    Then validate the priority/priority reasons for the captured "<DPKey>" with Priority "<Priority>" at "DP Level"

    Examples: 
      | User   | Client    | Priority        | DPKey    |
      #| nkumar | UnitedHealth Group    | High            | Single   |
      #| nkumar | AmeriHealth Mercy     | Medium          | Single   |
      #| nkumar | Molina Healthcare     | Low             | Single   |
      #| nkumar | Humana                | High            | Multiple |
      #| nkumar | WellPoint             | Medium          | Multiple |
      #| nkumar | Humana                | Low             | Multiple |
      #| nkumar | WellCare Health Plans | High,Medium,Low | Single   |
      | nkumar | WellPoint | High,Medium,Low | Multiple |

  #####################################################   Scenario-2  #############################################################
  @BOSE2812_2
  Scenario Outline: BOSE2812_2 Ability to view the Priority "<Priority>" and 'Priority Reasons' on the captured DP Card "<DPKey>" at Topic Level
    Given User "<User>" logged into "" application with Services
    Given the "<User>" moving the data from CPW to PM through the service for the client "<Client>" with "<DPKey>" DPKey and Priority as "<Priority>"
    Given User "<User>" logged into "PM" application
    When user selects "<Client>" from Client drop down list
    And User selects  multiple Payershorts "All" from Payershorts FilterSection
    And User selects muliple LOBs "All" from LOB FilterSection
    When user selects "All" Claimtypes in filtersection
    And User clicks on "Apply" button to filter
    And User selects Medical Policy/Topic according to "Captured Topic"
    And user filters by clicking on Apply for Medical Policy/Topic
    Then validate the priority/priority reasons for the captured "<DPKey>" with Priority "<Priority>" at "Topic Level"

    Examples: 
      | User   | Client | Priority        | DPKey    |
      #| nkumar | UnitedHealth Group    | High            | Single   |
      #| nkumar | AmeriHealth Mercy     | Medium          | Single   |
      #| nkumar | Molina Healthcare     | Low             | Single   |
      #| nkumar | Humana                | High            | Multiple |
      #| nkumar | Wellmark              | Medium          | Multiple |
      #| nkumar | Humana                | Low             | Multiple |
      #| nkumar | WellCare Health Plans | High,Medium,Low | Single   |
      | nkumar | Humana | High,Medium,Low | Multiple |

  #####################################################   Scenario-3  #############################################################
  @BOSE2812_3
  Scenario Outline: BOSE2812_3 Ability to view the Priority "<Priority>" and 'Priority Reasons' on the captured DP Card "<DPKey>" at MP Level
    Given User "<User>" logged into "" application with Services
    Given the "<User>" moving the data from CPW to PM through the service for the client "<Client>" with "<DPKey>" DPKey and Priority as "<Priority>"
    Given User "<User>" logged into "PM" application
    When user selects "<Client>" from Client drop down list
    And User selects  multiple Payershorts "All" from Payershorts FilterSection
    And User selects muliple LOBs "All" from LOB FilterSection
    When user selects "All" Claimtypes in filtersection
    And User clicks on "Apply" button to filter
    And User selects Medical Policy/Topic according to "Captured Topic"
    And user filters by clicking on Apply for Medical Policy/Topic
    Then validate the priority/priority reasons for the captured "<DPKey>" with Priority "<Priority>" at "MP Level"

    Examples: 
      | User   | Client   | Priority        | DPKey    |
      #| nkumar | UnitedHealth Group    | High            | Single   |
      #| nkumar | AmeriHealth Mercy     | Medium          | Single   |
      #| nkumar | Molina Healthcare     | Low             | Single   |
      #| nkumar | Humanal               | High            | Multiple |
      #| nkumar | Wellmark              | Medium          | Multiple |
      #| nkumar | Humana                | Low             | Multiple |
      #| nkumar | WellCare Health Plans | High,Medium,Low | Single   |
      | nkumar | Wellmark | High,Medium,Low | Multiple |
