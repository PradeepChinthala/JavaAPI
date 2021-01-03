@SubSequentRelease
Feature: SubSequent Release Automation

  ######################################################   Scenario-1  #############################################################
  @datasetup
  Scenario Outline: SubSequent Release Validations
    Given User "<User>" logged into "" application with Services
    Then Set up the Data for ELL Changes for "<User>","<Disposition>","<Changetype>","<DPType>"

    Examples: 
      | User   | Changetype | DPType | Disposition |
      | nkumar | DP Retire  | eLL    | Present     |

  ######################################################   Scenario-2  #############################################################
  @SubsequentValidations
  Scenario Outline: Validate the captured data in CPW and PM after RVA/ELL pipeline.
    Given the "<User>" is logged into the CPW application
    Then verify CPW after pipeline for the captured data "<User>","<Disposition>","<Changetype>","<DPType>"
    Given User "<User>" logged into "PM" application
    Then verify PM after pipeline for the captured data "<User>","<Disposition>","<Changetype>","<DPType>"
    And User logsout of the "PM" Application

    Examples: 
      | User   | Changetype | DPType | Disposition |
      | nkumar | DP Retire  | eLL    | Present     |
