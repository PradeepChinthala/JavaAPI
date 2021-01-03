@RestServices @ApproveWithMODServices
Feature: BOSE- Rest Services

  @PCA-4111 @PCA-4113
  Scenario Outline: Verify the Login Functionality with user "<User>"
    Given User "<User>" logged into "Ihealth123" application with Services
    And verify Approve with MOD DB and Services count for "CPT CODE" 
    And verify Approve with MOD DB and Services count for "ICD CODE" 
    And verify Approve with MOD DB and Services count for "REASON CODE" 
    Then user logout from the presentation manager application
    Examples: 
      | User         | firstname | lastName |
      | iht_ittest05 | Generic05 | ITTest   |
   
   
  @PCA-4112 @PCA-4114
  Scenario Outline: Verify the Login Functionality with user "<User>"
    Given User "<User>" logged into "Ihealth123" application with Services
    And verify Approve with MOD DB and Services data for "CPT CODE" 
    And verify Approve with MOD DB and Services data for "ICD CODE" 
    And verify Approve with MOD DB and Services data for "REASON CODE" 
    And verify Approve with MOD DB and Services data for "AGE FILTER-1" 
    And verify Approve with MOD DB and Services data for "AGE FILTER0" 
    Then user logout from the presentation manager application
    Examples: 
      | User         | firstname | lastName |
      | iht_ittest05 | Generic05 | ITTest   |   
