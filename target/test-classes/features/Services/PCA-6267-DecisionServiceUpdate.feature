@RestServices
Feature: Decision Service Update - Rest Service

  @DecisionServiceUpdate
  Scenario Outline: Create a new decision service to insert the CDM decision data into the decision collection
    Given User "<User>" logged into "" application with Services
    Given new decision service for inserting data of CDM decision into the decision collection
    Then verify the data persistance in mongo collection
    Examples: 
      | User         | Client | 
      | iht_ittest05 | Humana |