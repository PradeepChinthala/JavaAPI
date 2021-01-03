#Author: Udayakiran.Lanka
@RegressionNov1 @Regression @BOSE3052
Feature: BOSE-3052- CPDUnifiedAccess:Unified Login for CPW, PM and CPQ

  ##################################### Scenario-1 ############################################################
  @Regression3582 @Sanity
  Scenario Outline: BOSE-3582-Validation of CPD Login page-BOSE-3192 Ability to login to Presentation Manager Application
    Given CPMorCMD launches CPD Application
    Then CPD Login page should be displayed with Username,Password,Application Dropdown,Login button
    And User should be able to enter "<UserName>" and Password
    When User selects Application values "PM,CPW" in the Application dropdown then related names should be displayed below the Dropdown
    When User selects Application name "PM"  from the Application dropdown
    And User clicks on "Login" button
    Then The user should view the PresentationManager HomePage
    And User logsout of the "PM" Application
    Given CPMorCMD launches CPD Application
    And User should be able to enter "<UserName>" and Password
    When User selects Application name "CPW"  from the Application dropdown

    #And  User clicks on "Login" button
    #Then verify header of "CPW" application
    #And  User logsout of the "CPW" Application
    ## ***CPQ Application is  down now so commenting below Gherkin steps
    #Given CPMorCMD launches CPD Application
    #Given User "<UserName>" logged into "PM" application
    #And User should be able to enter "<UserName>" and Password
    #When User selects Application name "CPQ"  from the Application dropdown
    #And  User clicks on "Login" button
    #Then verify header of "CPQ" application
    #And  User logsout of the "CPQ" Application
    Examples: 
      | UserName     |
      | iht_ittest09 |

  ##################################### Scenario-2 ############################################################
  @BOSE-3495 @BOSE-3627 @BOSE3052_2
  Scenario Outline: BOSE-3495-Ability to handle Invalid Username and Password during login
    Given User is logged into the CPD PM application with invalid "<UserName>" or "<Pwd>"
    Then verify message for invalid credentials

    Examples: 
      | UserName     | Pwd              |
      | iht_ittest09 | test34           |
      | sdfsdf       | SWhlYWx0aDEyMw== |

  ##################################### Scenario-3 ############################################################
  @BOSE-3194 @BOSE-3687
  Scenario: BOSE-3194-Ability to navigate to CPW from PM
    Given User "iht_ittest05" logged into "PM" application
    Then The user should view the PresentationManager HomePage
    And navigate to "CPW" application
    Then verify header of "CPW" application
    And User logsout of the "CPW" Application
 ##  CPQ is application down now,when it is up below gherking will be uncommented
 #@BOSE-3628 @BOSE-3941
 #Scenario: Validation of navigation to CPQ from PM
 #Given User "iht_ittest09" logged into "PM" application
 #Then The user should view the PresentationManager HomePage
 #And navigate to "CPQ" application
 #Then verify header of "CPQ" application
 #And User logsout of the "CPQ" Application
