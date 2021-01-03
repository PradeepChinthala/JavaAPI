package project.features.steps;

import java.io.IOException;

import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import net.thucydides.core.annotations.Steps;
import projects.steps.definitions.CPWStepDef;


public class CPWSteps {

	@Steps
	CPWStepDef oCPWStepDef;



	@Given("^the \"([^\"]*)\" is logged into the CPW application$")
	public void the_is_logged_into_the_CPW_application(String arg1) throws Throwable {
		oCPWStepDef.the_is_logged_into_the_CPW_application(arg1);
	}

	@Given("^the \"([^\"]*)\" is logged into the NewCPW application$")
	public void the_is_logged_into_the_NewCPW_application(String User) throws Throwable {
		oCPWStepDef.the_is_logged_into_the_NewCPW_application(User);
	}


	@When("^user click on client \"([^\"]*)\" with release \"([^\"]*)\" in the Opportunity Dashboard$")
	public void user_click_on_client_with_release_in_the_Opportunity_Dashboard(String arg1, String arg2) throws Throwable {
		oCPWStepDef.User_click_on_client_with_release_in_the_Opportunity_Dashboard(arg1,arg2);
	}

	@When("^user select Medical Policy from the policy selection through MongoDB$")
	public void user_select_Medical_Policy_from_the_policy_selection_through_MongoDB() throws Throwable {
		oCPWStepDef.user_select_Medical_Policy_from_the_policy_selection_through_MongoDB();
	}


	@Given("^user select Medical Policy from the policy selection through MongoDB for the given latest client decision \"([^\"]*)\"$")
	public void user_select_Medical_Policy_from_the_policy_selection_through_MongoDB_for_the_given_latest_client_decision(
			String arg1) throws Throwable {
		oCPWStepDef
		.user_select_Medical_Policy_from_the_policy_selection_through_MongoDB_for_the_given_latest_client_decision(
				arg1);
	}

	@Then("^validate the capture and update disposition functionality at \"([^\"]*)\" data for \"([^\"]*)\",Disposition as \"([^\"]*)\",update disposition \"([^\"]*)\" with MongoDB$")
	public void validate_the_capture_and_update_disposition_functionality_at_data_for_Disposition_as_update_disposition_with_MongoDB(
			String arg1, String arg2, String arg3, String arg4) throws Throwable {
		oCPWStepDef.Validate_the_capture_and_update_disposition_functionality_with_MongoDB_for(arg1, arg2, arg3, arg4);
	}

	@Then("^Logout CPW application$")
	public void logout_CPW_application() throws Throwable {
		oCPWStepDef.logout_CPW_application();
	}

	@Given("^user retrieve \"([^\"]*)\",\"([^\"]*)\" opportunity data from MongoDB\\.$")
	public void user_retriev_opportunity_data_from_MongoDB(String arg1, String arg2) throws Throwable {
		oCPWStepDef.user_retriev_opportunity_data_from_MongoDB(arg1, arg2);
	}

	@When("^user selects \"([^\"]*)\" under \"([^\"]*)\" as \"([^\"]*)\" and \"([^\"]*)\"$")
	public void user_selects_under_as_and(String arg1, String arg2, String arg3, String arg4) throws Throwable {
		oCPWStepDef.user_selects(arg1, arg2, arg3, arg4);

	}

	@Then("^validate the work todo count on the count with DB$")
	public void validate_the_work_todo_count_on_the_count_with_DB() throws Throwable {
		oCPWStepDef.validate_the_work_todo_count_on_the_count_with_DB();
	}


	@When("^User Retrieves the \"([^\"]*)\" and \"([^\"]*)\" from MongoDB for \"([^\"]*)\"$")
	public void user_Retrieves_the_and_from_MongoDB_for(String DPKeyCount, String ValToRetrieve, String RuleRelationshipCombination,String ClientName) throws Throwable
	{
		oCPWStepDef.retrieveValuesForRuleRelationshipCombination(DPKeyCount,ValToRetrieve,RuleRelationshipCombination,ClientName,"");			
	}


	// ================================================= Chaitanya
	// ==========================================================================//



	@Given("^the \"([^\"]*)\" moving the data from CPW to PM through the service for the client \"([^\"]*)\" with \"([^\"]*)\" DPKey$")
	public void the_moving_the_data_from_CPW_to_PM_through_the_service_for_the_client_with_DPKey(String arg1,String arg2, String arg3) throws Throwable {
		oCPWStepDef.Move_the_data_from_CPW_to_PM_through_the_service(arg1, arg2, arg3);
	}

	@Given("^the \"([^\"]*)\" moving the data from CPW to PM through the service for the client \"([^\"]*)\" with \"([^\"]*)\" DPKey and Priority as \"([^\"]*)\"$")
	public void the_moving_the_data_from_CPW_to_PM_through_the_service_for_the_client_with_DPKey_and_Priority_as(String arg1, String arg2, String arg3, String arg4) throws Throwable {
		oCPWStepDef.Move_the_data_from_CPW_to_PM_through_the_service_for_the_given_priority(arg1,arg2,arg3,arg4);
	}

	@Given("^capture \"([^\"]*)\" DP with new Payer short for the \"([^\"]*)\" through Service$")
	public void capture_DP_with_new_Payer_short_for_the_through_Service(String arg1, String arg2) throws Throwable {
		oCPWStepDef.CaptureSingleDPToNewPayerShort(arg2, arg1);
	}

	@Then("^verify header of \"([^\"]*)\" application$")
	public void verify_header_of_application(String arg1) throws Throwable {
		oCPWStepDef.validateHomePage_CPQ_CPW(arg1);

	}

	@Then("^validate the captured \"([^\"]*)\" DPKey at \"([^\"]*)\"$")
	public void validate_the_captured_DPKey_at(String arg1, String arg2) throws Throwable {
		oCPWStepDef.validate_the_captured_DPKey_in_PM_at(arg1,arg2);

	}

	@Then("^the user should not view \"([^\"]*)\" at \"([^\"]*)\"$")
	public void the_user_should_not_view_at(String arg1, String arg2) throws Throwable {

		oCPWStepDef.User_should_not_view_the_given(arg1,arg2);
	}



	@Then("^validate the DPcards in available opportunity deck for \"([^\"]*)\" dropdown functionality$")
	public void validate_the_DPcards_in_available_opportunity_deck_for_dropdown_functionality(String arg1) throws Throwable {
		oCPWStepDef.validate_the_DPcards_in_available_opportunity_deck_for_given_selection_of_dropdown(arg1);

	}



	//**************   Udayakiran **************************************

	@When("^User Retrieves the \"([^\"]*)\" and \"([^\"]*)\" from MongoDB for \"([^\"]*)\" for \"([^\"]*)\"$")
	public void user_Retrieves_the_and_from_MongoDB_for_for(String DPKeyCount, String ValToRetrieve, String RuleRelationshipCombination, String Client) throws Throwable {
		oCPWStepDef.retrieveValuesForRuleRelationshipCombination(DPKeyCount,ValToRetrieve,RuleRelationshipCombination,Client,"");		
	}

	@Given("^User Retrieves the \"([^\"]*)\" and \"([^\"]*)\" from MongoDB for \"([^\"]*)\" for \"([^\"]*)\" for warning messages check$")
	public void user_Retrieves_the_and_from_MongoDB_for_for_for_warning_messages_check(String DPKeyCount, String ValToRetrieve, String RuleRelationshipCombination, String Client) throws Throwable 
	{	   
		oCPWStepDef.retrieveValuesForRuleRelationshipCombinationForMessages(DPKeyCount,ValToRetrieve,RuleRelationshipCombination,Client,"");		

	}


	@When("^User captures Disposition \"([^\"]*)\" for the \"([^\"]*)\"$")
	public void user_captures_Disposition_for_the(String DispositionTocapture, String DPKeyCount) throws Throwable
	{	  
		oCPWStepDef.captureDisposition(DispositionTocapture,DPKeyCount,"");		
	}	


	@When("^User click on client \"([^\"]*)\" with release \"([^\"]*)\" in the Opportunity Dashboard for new CPW App$")
	public void user_click_on_client_with_release_in_the_Opportunity_Dashboard_for_new_CPW_App(String ClientName, String Release) throws Throwable 
	{
		oCPWStepDef.navigateToAWBForAClientAndRelease(ClientName,Release);
	}


	@Given("^Capture DPKey for the \"([^\"]*)\" which has disposition value \"([^\"]*)\"  from \"([^\"]*)\" collection$")
	public void capture_DPKey_for_the_which_has_disposition_value_from_collection(String ClientName, String DispositionVal, String MDBCollectionName) throws Throwable 
	{		
		oCPWStepDef.captureValuesForDispositionUpdate(ClientName,DispositionVal,MDBCollectionName);	 
	}

	@Given("^Capture the \"([^\"]*)\" for the \"([^\"]*)\" with disposition value \"([^\"]*)\" from \"([^\"]*)\" collection$")
	public void capture_the_for_the_with_disposition_value_from_collection(String ValtoRetrieve, String Criteria, String DispositionVal, String MDBCollectionName) throws Throwable
	{
		oCPWStepDef.captureValuesforDPKeyafterUpdateDisposition(ValtoRetrieve,Criteria,DispositionVal,MDBCollectionName);
	}


	@Then("^the \"([^\"]*)\" should be displayed$")
	public void the_should_be_displayed(String expectedWarningMessage) throws Throwable
	{
		oCPWStepDef.validateWarningMessage(expectedWarningMessage);
	}


	@Given("^User searches for the \"([^\"]*)\"  in the  \"([^\"]*)\"$")
	public void user_searches_for_the_in_the(String SearchValue, String pageName) throws Throwable
	{
		oCPWStepDef.searchValueinAWB(SearchValue,pageName);
	}

	@Then("^The RuleRelationshipIcon should be displayed  at the \"([^\"]*)\"$")
	public void the_RuleRelationshipIcon_should_be_displayed_at_the(String DPorTopicLevel) throws Throwable
	{
		oCPWStepDef.validateRuleRelatioshipIcon(DPorTopicLevel);
	}

	@Then("^The tooltip should display the \"([^\"]*)\" for the \"([^\"]*)\" on mouse hover at the \"([^\"]*)\"$")
	public void the_tooltip_should_display_the_for_the_on_mouse_hover_at_the(String validationCriteria, String CapturedorfromDB, String DPorTopicorMPLevel) throws Throwable
	{
		oCPWStepDef.validateTooltipInformationInAWB(validationCriteria,CapturedorfromDB,DPorTopicorMPLevel);	
	}


	@When("^The user selects the RuleRelationship icon for the Payer in the \"([^\"]*)\"$")
	public void the_user_selects_the_RuleRelationship_icon_for_the_Payer_in_the(String Section) throws Throwable 
	{
		oCPWStepDef.validateRuleRelationIcon(Section);
	}

	@When("^validate the rule status based on \"([^\"]*)\"$")
	public void validate_the_rule_status_based_on(String arg1) throws Throwable {
		oCPWStepDef.validate_the_rule_status_based_on(arg1);
	}

	@Given("^\"([^\"]*)\" clicks on \"([^\"]*)\" link of AWB$")
	public void clicks_on_link_of_AWB(String arg1, String arg2) throws Throwable {
		oCPWStepDef.clicks_on_link_of_DPDescription(arg1,arg2);

	}

	@Then("^verify the retired DP/Topic in \"([^\"]*)\"$")
	public void verify_the_retired_DP_Topic_in(String arg1) throws Throwable {
		oCPWStepDef.verify_the_DP_Topic_of_retire_status(arg1);
	}

	

@When("^User search for \"([^\"]*)\" in AWB$")
	public void user_search_for_in_AWB(String arg1) throws Throwable {
		oCPWStepDef.user_search_for_in_AWB(arg1);
	}
	
	
	@Then("^User should see filter options from flag \"([^\"]*)\"$")
	public void user_should_see_filter_options_from_flag(String arg1) throws Throwable {
		oCPWStepDef.user_should_see_filter_options_from_flag(arg1);
	}
	
	@Then("^User validate apply filter \"([^\"]*)\" on flag section for client \"([^\"]*)\"$")
	public void user_validate_apply_filter_on_flag_section(String arg1,String arg2) throws Throwable {
		oCPWStepDef.user_validate_apply_filter_on_flag_section_for_client(arg1,arg2);
	}
	
		@Given("^User captures Disposition \"([^\"]*)\" for the \"([^\"]*)\"  and  \"([^\"]*)\"$")
	public void user_captures_Disposition_for_the_and(String DispositionToTake, String DPKeyCount, String MedicalPolicyCount) throws Throwable {
					oCPWStepDef.captureDispositionForProvidedDetails(DispositionToTake,DPKeyCount,MedicalPolicyCount);
	}
	
	
	
	@Then("^RuleRelationshipIcon and  RuleRelationshipAlert Tooltip should be displayed at  \"([^\"]*)\"  and  \"([^\"]*)\"$")
	public void rulerelationshipicon_and_RuleRelationshipAlert_Tooltip_should_be_displayed_at_and(String TopicLevel, String DPLevel) throws Throwable
	{
		oCPWStepDef.validateruleRelationiconAndAlertinAWB(TopicLevel,DPLevel);
		
	}
	
   	@Then("^User should see \"([^\"]*)\" difference from \"([^\"]*)\" and \"([^\"]*)\"$")
	public void user_should_see_difference_from_and(String arg1, String arg2, String arg3) throws Throwable {
		oCPWStepDef.validateSavingValueDifference(arg1,arg2,arg3);
	}
	
   	@Given("^User selects Medical Policy/Topic as \"([^\"]*)\"$")
   	public void user_selects_Medical_Policy_Topic_as(String arg1) {
   		oCPWStepDef.userSelectsMedicalPolicyAs(arg1);
   	}
   	

   	// ###################################### eLL Related steps ##########################################################
   	
   
   	@Then("^verify CPW after pipeline for the captured data \"([^\"]*)\",\"([^\"]*)\",\"([^\"]*)\",\"([^\"]*)\"$")
   	public void verify_CPW_after_pipeline_for_the_captured_data(String arg1, String arg2, String arg3, String arg4) throws Throwable {
   		oCPWStepDef.verify_AWB_and_RWO_pages_after_pipeline_for_the_captured_data(arg1,arg2,arg3,arg4);
   	}

}




