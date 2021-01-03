@RestServices
Feature: BOSE- Rest Services

  @DeletePresentationservice
  Scenario Outline: Delete Presentations for the clients
    Given User "<User>" logged into "" application with Services
    And Delete the presentations for client "<Client>"

    Examples: 
      | User         | Client                                                                                                                 |
      | iht_ittest05 | 84,11,19,122,114,48,79,72,112,138,47,8,9,60,3,25,37,38,27,128,32,4,14,12,43,46,44,97,56,53,24,75,110,51,20,77,22,16,58 |
 			#| iht_ittest05 | 19 |