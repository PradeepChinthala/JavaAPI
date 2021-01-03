@Regression @Regression @DeletePresentations
Feature: DeletePresentations

 
  ######################################################   Scenario-2  #############################################################
  @PCA-2241 @DeleteWarningMessage_1
  Scenario Outline: PCA 1897_Deleting a Presentation with no Dps Assigned
    Given User "<User>" logged into "" application with Services
    Then Create the presenatation profile with given request data client "<Client>" payershort "" lob "" product "" and prority "" with "<User>"
    Then User "<User>" logged into "PM" application
    When user selects "<Client>" from Client drop down list
    And user Delete the Presentation profile with warning message "This action cannot be undone."

    Examples: 
      | User         | Client       | LOB                          | Payershort                          | Product    | Priority |
      | iht_ittest05 | Aetna Medicaid | Commercial;Medicaid;Medicare | HUMCC;HUMCF;HUMCH;HUMCM;HUMCP;HUMIL | Outpatient | High     |

  ######################################################   Scenario-3  #############################################################
  @PCA-1899 @DeleteWarningMessage @PCA-1899Test
  Scenario Outline: Ability to delete a Presentation Profile and un-assign all opportunities back to respective multiple DPs
    Given User "<User>" logged into "Ihealth123" application with Services
    Then Create the presenatation profile with given request data client "<Client>" payershort "<Payershort>" lob "<LOB>" product "<Product>" and prority "<Priority>" with "<User>"
    Then the "<User>" moving the data from CPW to PM through the service for the client "<Client>" with "Multiple" DPKey
    Then assign multiple dps to created profile
    Then User "<User>" logged into "PM" application
    When user selects "<Client>" from Client drop down list
    Then apply filters having multiple DPs oppurtunities
    And user Delete the Presentation profile with warning message "This action cannot be undone."
    And Click on "Expand All"
    Then Verify deleted profiles are removed from DP Card
    And User logsout of the "PM" Application

    Examples: 
      | User         | Client         | LOB                          | Payershort                          | Product    | Priority |
      | iht_ittest05 | Humana, Inc. | Commercial;Medicaid;Medicare  | HUMCC;HUMCF;HUMCH;HUMCM;HUMCP;HUMIL | Outpatient | High      |
      ######################################################   Scenario-4  #############################################################
#	@DeleteWarningMessage1
#	Scenario Outline: PCA 1897_Deleting a Presentation with decisions captured
#	Given User "iht_ittest05" logged into "Ihealth123" application with Services
#	Then the "iht_ittest05" moving the data from CPW to PM through the service for the client "<Client>" with "Single" DPKey
#	Then Create the presenatation profile with given request data client "<Client>" payershort "<Payershort>" lob "<LOB>" product "<Product>" and prority "<Priority>" with "<User>"
#	Then Assign created Dpkey to the profile
#	Then Capture the decision with status "Approve"
#	Then User "<User>" logged into "PM" application
#	When user selects "<Client>" from Client drop down list
#	And user Delete the Presentation profile with warning message "Decisions captured will be deleted, this action cannot be undone."
#	
#	Examples:
#		   |   User       |  Client     					  |LOB												    |Payershort			  									 | LOB					     |Priority|
#		   | iht_ittest03 |  UnitedHealth Group			|Commercial;Medicaid;Medicare   |UHCAK;UHCAZ;UHCCA;UHCCO;UHCDE;UHCHI | Outpatient        |High    |
#
#
