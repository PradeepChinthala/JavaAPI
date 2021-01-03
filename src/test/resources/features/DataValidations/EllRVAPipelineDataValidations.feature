#Author: your.email@your.domain.com
#Sample Feature Definition Template
@RVA
Feature: Title of your feature
  I want to use this template for my feature file

  @RVAValidations
  Scenario Outline: setup  the data for ELL/RVA pipeline
    Given User "<User>" logged into "" application with Services
    When user setup the data for "<change>"

    Examples: 
      | User         | change    |
      | iht_ittest09 | DP Retire |

  @RVAValidations
  Scenario Outline: Validate the captured data in CPW and PM after RVA/ELL pipeline.
  Given the "<User>" is logged into the CPW application
    Then verify AWB and RWO pages after pipeline for the captured data
    Given User "<User>" logged into "PM" application
    When user slect the data from excel and apply
    And User logsout of the "PM" Application

    Examples: 
      | User         |
      | iht_ittest09 |

  
