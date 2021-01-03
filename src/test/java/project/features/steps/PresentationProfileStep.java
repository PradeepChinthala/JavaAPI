package project.features.steps;

import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import net.thucydides.core.annotations.Steps;
import project.utilities.GenericUtils;
import projects.steps.definitions.FilterDrawerStepDef;
import projects.steps.definitions.PresentationProfileStepDef;

public class PresentationProfileStep
{

	private static final long serialVersionUID = 1L;

	@Steps
	PresentationProfileStepDef refPresentationProfileStepDef;	
	GenericUtils oGenericUtils;

	@Given("^CPMorCMD has created a PresentationProfile$")
	public void cpmorcmd_has_created_a_PresentationProfile() throws Throwable
	{
		refPresentationProfileStepDef.createPresentationProfile();
	}

	@When("^User clicks on the PresentationProfile with no DP cards assignments yet$")
	public void user_clicks_on_the_PresentationProfile_with_no_DP_cards_assignments_yet() throws Throwable
	{
		refPresentationProfileStepDef.clickPresentationProfile();
	}

	@Then("^The PresentationDeck for that PresentationProfile should be empty$")
	public void the_PresentationDeck_for_that_PresentationProfile_should_be_empty() throws Throwable 
	{
		refPresentationProfileStepDef.validatePresentationDeckIsEmpty();
	}


	@When("^The CPMorCMD clicks on the \"([^\"]*)\"$")
	public void the_CPMorCMD_clicks_on_the(String arg1) throws Throwable 
	{
		refPresentationProfileStepDef.clickPresentationProfile();  
	}

	@Then("^The \"([^\"]*)\" container should be \"([^\"]*)\"$")
	public void the_container_should_be(String sContainer, String sExpectedState) throws Throwable 
	{
		refPresentationProfileStepDef.validatePresDeckState(sContainer,sExpectedState);
	}

	@When("^The CPM/CMD clicks on the PresentationProfile$")
	public void the_CPM_CMD_clicks_on_the_PresentationProfile() throws Throwable
	{


	}


	@Given("^The User  \"([^\"]*)\" has created a PresentationProfile$")
	public void the_User_has_created_a_PresentationProfile(String sUserName) throws Throwable 
	{
		refPresentationProfileStepDef.createPresentationProfile(sUserName);
	}


	/*@Then("^The User   \"([^\"]*)\" must view both  PresentationProfiles created for that client irrespective of User$")
public void the_User_must_view_both_PresentationProfiles_created_for_that_client_irrespective_of_User(String sUserName) throws Throwable
{
	refPresentationProfileStepDef.validateClientBasedPresProfileView(sUserName);

}*/

	@When("^User clicks on any other PresentationProfile$")
	public void user_clicks_on_any_other_PresentationProfile() throws Throwable
	{
		refPresentationProfileStepDef.clickOnOtherPresProfile();
	}

	@Then("^The User   \"([^\"]*)\" must view PresentationProfiles created by him for that client and  by User \"([^\"]*)\"$")
	public void the_User_must_view_PresentationProfiles_created_by_him_for_that_client_and_by_User(String sLoginUser, String sUser2) throws Throwable 
	{
		refPresentationProfileStepDef.validateClientBasedPresProfileView(sLoginUser,sUser2);	
	}

	@Given("^CPMorCMD has created Presentations with count \"([^\"]*)\" in  PresentationProfile section with \"([^\"]*)\"$")
	public void cpmorcmd_has_created_Presentations_with_count_in_PresentationProfile_section_with(String sPresCount, String sValues) throws Throwable
	{
		refPresentationProfileStepDef.createPresentations(sPresCount,sValues);

	}

	@Then("^The DPs should be \"([^\"]*)\" the selected PresentationProfiles \"([^\"]*)\"$")
	public void the_DPs_should_be_the_selected_PresentationProfiles(String sCondition, String sPresentation) throws Throwable {
		//refPresentationProfileStepDef.validateOppsAssignmentToProfiles(sCondition,sPresentation);
	}

	@When("^user validate Presentation profile \"([^\"]*)\" functionality$")
	public void user_validate_Presentation_profile_functionality(String arg1) throws Throwable {
		refPresentationProfileStepDef.user_validate_Presentation_profile_functionality(arg1);
	}


	@Then("^validate payershort section based on the payers in the RVA run for the \"([^\"]*)\",\"([^\"]*)\"$")
	public void validate_payershort_section_based_on_the_payers_in_the_RVA_run_for_the(String arg1, String arg2) throws Throwable {
		refPresentationProfileStepDef.validate_payershort_section_based_on_the_payers_in_the_RVA_run_for_the(arg1, arg2);
	}

	@Then("^\"([^\"]*)\" Presentation Profile functionality$")
	public void presentation_Profile_functionality(String arg1) throws Throwable {
		refPresentationProfileStepDef.presentation_Profile_functionality(arg1);
	}

	@Then("^validate client decision \"([^\"]*)\" on DP card in avaliable DP desk\\.$")
	public void validate_client_decision_on_DP_card_in_avaliable_DP_desk(String arg1) throws Throwable {
		refPresentationProfileStepDef.validate_client_decision_on_DP_card_in_avaliable_DP_desk(arg1);
	}
	@Then("^validate captured disposition present under Avaialbe DPs$")
	public void validate_captured_disposition_present_under_Avaialbe_DPs() throws Throwable {
		refPresentationProfileStepDef.validate_captured_disposition_present_under_Avaialbe_DPs();
	}

	@Given("^verify \"([^\"]*)\" icon displayed on screen$")
	public void verify_icon_displayed_on_screen(String arg1) throws Throwable {
		refPresentationProfileStepDef.verify_icon_displayed_on_screen(arg1);
	}
	@Then("^validate view assign pop-up when no opportunity is assigned to a profile$")
	public void validate_view_assign_pop_up_when_no_opportunity_is_assigned_to_a_profile() throws Throwable {
		refPresentationProfileStepDef.validate_view_assign_pop_up_when_no_opportunity_is_assigned_to_a_profile();

	}

	@Then("^Select DPs at \"([^\"]*)\" level in oppurtunity hierarchy view$")
	public void selectDPsatGivenLevel(String slevel) throws Throwable {
		refPresentationProfileStepDef.selectDPsatGivenLevelinOpptntyView(slevel);

	}

	@Then("^Verify the message \"([^\"]*)\"$")
	public void verify_the_message(String sMessage) throws Throwable {
		String sXpath ="//*[contains(text(),'"+sMessage+"')]";
		boolean blnVal = oGenericUtils.isElementExist(sXpath);
		GenericUtils.Verify(sMessage+ "should be displayed", blnVal);

	}

	@Then("^Expand all the items in PayerLOB Grid under presenation profile section$")
	public void ExpandAllItemsinPayerLOBGrid() throws Throwable {
		refPresentationProfileStepDef.ExpandAllinPresenationPrf();

	}


	@Given("^Capture \"([^\"]*)\" decision$")
	public void capture_decision(String sOperation) throws Throwable {
		refPresentationProfileStepDef.captureDecision(sOperation);
		

	}
	@Given("^Finalize \"([^\"]*)\" Decisions for \"([^\"]*)\" payershorts$")
	public void finalize_Decisions_for_payershorts(String arg1, String arg2) throws Throwable {
		refPresentationProfileStepDef.finalize_Decisions_for_payershorts(arg1, arg2);
	}



	@Given("^verify \"([^\"]*)\" validation for \"([^\"]*)\"$")
	public void verify_validation_for(String arg1, String arg2) throws Throwable {
		refPresentationProfileStepDef.verify_validation_for(arg1, arg2);
}
	@When("^User Unassigns \"([^\"]*)\" DPs from the \"([^\"]*)\" for \"([^\"]*)\"$")
	public void user_Unassigns_DPs_from_the_for(String NoOfDPsToUnassign, String UnassignForTopicorMedPolicy, String sPresProfile) throws Throwable 
	{    
		String sPlaceHolderArg1 = "";
		String sPlaceHolderArg2 = "";
		refPresentationProfileStepDef.UnassignDPFromPresDeck(NoOfDPsToUnassign,UnassignForTopicorMedPolicy,sPresProfile,sPlaceHolderArg1,sPlaceHolderArg2);

	}


	@When("^User Reassigns the \"([^\"]*)\" DPs to the \"([^\"]*)\"$")
	public void user_Reassigns_the_DPs_to_the(String arg1, String arg2) throws Throwable {


	}

	@Then("^The Original \"([^\"]*)\" should be displayed  for the \"([^\"]*)\"$")
	public void the_Original_should_be_displayed_for_the(String arg1, String arg2) throws Throwable {  

	}

	@Then("^Application should display all the PresentationProfiles associated with the \"([^\"]*)\"  from DB$")
	public void application_should_display_all_the_PresentationProfiles_associated_with_the_from_DB(String ClientName) throws Throwable 
	{
		refPresentationProfileStepDef.validatePresentationsforClient(ClientName);	   
	}

	@Given("^Apply All Payers and LOBs filters in Presenation profile$")
	public void apply_All_filters_in_Presentation_profile() throws Throwable {
		refPresentationProfileStepDef.applyAllFiltersinPresentationProfile();
	}
	@Then("^The assign popup \"([^\"]*)\" button should be disabled$")
	public void the_assign_popup_button_should_be_disabled(String BtnName) throws Throwable {
		refPresentationProfileStepDef.validatePopupButton(BtnName);
	}

	@Given("^user Delete the Presentation profile with warning message \"([^\"]*)\"$")
	public void user_Delete_the_Presentation_profile(String sMessage) throws Throwable {
		refPresentationProfileStepDef.deletethePresentationProfile(sMessage);

	}

	@Then("^verify the ability to navigate to previous and next DP$")
	public void verify_the_ability_to_navigate_to_previous_and_next_DP() throws Throwable {
		refPresentationProfileStepDef.navigateToPreviousandNextDP();
	}

	@Then("^verify the \"([^\"]*)\" status updated in database for DP$")
	public void verify_the_status_updated_in_database_for_DP(String sStatus) throws Throwable {
		refPresentationProfileStepDef.verifyStatusUpdatedinDatabase(sStatus);	  
	}

	@Then("^verify the captured decision \"([^\"]*)\" in Payer/LOB Grid$")
	public void verify_the_captured_decision_in_Payer_LOB_Grid(String sStatus) throws Throwable {
		refPresentationProfileStepDef.verifyCapturedDecisioninPayerLOBGrid(sStatus);
	}

	@Then("^verify the captured decision values \"([^\"]*)\" in Payer/LOB Grid decision popup$")
	public void verify_the_captured_decision_values_in_Payer_LOB_Grid_decision_popup(String sStatus) throws Throwable {
		refPresentationProfileStepDef.verifyCapturedDecisionValuesinPayerLOBGridPopup(sStatus);

	}

	@When("^User Modify the captured decison values in Payer/LOB Grid decision popup$")
	public void user_Modify_the_captured_decison_values_in_Payer_LOB_Grid_decision_popup() throws Throwable {
		refPresentationProfileStepDef.modifyCapturedDecisionValuesinPayerLOBGridPopup();

	}

	@Then("^verify the modified captured \"([^\"]*)\" values updated in database for DP$")
	public void verify_the_modified_captured_values_updated_in_database_for_DP(String sStatus) throws Throwable {
		refPresentationProfileStepDef.verifyModifiedValuesinDatabase(sStatus);

	}
	

	@Given("^Expand all the items in presenation hierarchy view$")
	public void expand_all_the_items_in_presenation_hierarchy_view() throws Throwable {
		oGenericUtils.clickOnElement("u", "Expand All");

	}
	
	@Then("^Select DPs at \"([^\"]*)\" level in presentation hierarchy view$")
	public void select_DPs_at_level_in_presentation_hierarchy_view(String slevel) throws Throwable {
		refPresentationProfileStepDef.selectDPsatGivenLevelinPresentationView(slevel);

	}
	
	@Then("^Select DPs at \"([^\"]*)\" level$")
	public void select_DPs_at_level(String arg1) throws Throwable {
		refPresentationProfileStepDef.select_DPs_at_level(arg1);
	}
	
	@Given("^apply filters having multiple DPs oppurtunities$")
	public void apply_filters_having_multiple_DPs_oppurtunities() throws Throwable {
		refPresentationProfileStepDef.applyFiltersforMultipleDPs();
	  
	}
	
	@Then("^Click on \"([^\"]*)\"$")
	public void Click_On(String sText) throws Throwable {
		oGenericUtils.clickOnElement("*", sText);
	}
	
	@Given("^apply filters having no oppurtunities$")
	public void apply_filters_having_no_oppurtunites() throws Throwable {
		refPresentationProfileStepDef.applyFiltershavingNooppurtunities();

	}
	
	@Given("^verify the cancel functionality at \"([^\"]*)\"$")
	public void verify_the_cancel_message_box_functionality(String sOperation) throws Throwable {
		refPresentationProfileStepDef.verifyCancelFunctionality(sOperation);

	}
	
	@Then("^Assinee profile should not be displayed under Re-assignee list$")
	public void assigneeProfileShouldbeDisplayunderReassigneeList() throws Throwable {
		refPresentationProfileStepDef.assigneeProfileShouldbeDisplayunderReassigneeList();

	}
	
	@Then("^filter the captured policies for \"([^\"]*)\"$")
	public void filterAssigned_captured_policies(String slevel) throws Throwable {
		refPresentationProfileStepDef.filtertheAssignedPolices(slevel);

	}
	
	@Then("^verify the Edit Profile functionality$")
	public void verify_the_editprofilefunctionality() throws Throwable {
		refPresentationProfileStepDef.verifyEditProfileFunctionality();

	}
	
	@Then("^Select DPs at \"([^\"]*)\" level in Payer/LOB Filter view$")
	public void select_DPs_at_level_in_Payer_LOB_Filter_view(String slevel) throws Throwable {
		refPresentationProfileStepDef.selectDPsatGivenLevelinPayerLOBView(slevel);

	}
	
	@Then("^Expand all the items in oppurtunity hierarchy view$")
	public void ExpandAllItemsinPayerLOBGridSection() throws Throwable {
		refPresentationProfileStepDef.ExpandAllinPresenationPrf();
	}	

	@Then("^The assign popup \"([^\"]*)\" button should be \"([^\"]*)\"$")
	public void the_assign_popup_button_should_be(String BtnName, String expectedProperty) throws Throwable
	{
		refPresentationProfileStepDef.validatePopupButton(BtnName,expectedProperty);
	}

	
	@Then("^The \"([^\"]*)\" icon should be \"([^\"]*)\" for the Presentation on the ProfileTab$")
	public void the_icon_should_be_for_the_Presentation_on_the_ProfileTab(String siconName, String sExpectedstatus) throws Throwable 
	{
	        refPresentationProfileStepDef.validateIconStatus(siconName,sExpectedstatus);
	}

	@Given("^The Presentation should be \"([^\"]*)\" in the OpportunitiesAssignment Popup$")
	public void the_Presentation_should_be_in_the_OpportunitiesAssignment_Popup(String status) throws Throwable
	{	
	   	refPresentationProfileStepDef.validatePresentation(status);				
	}

	@Given("^User reloads the ApplicationPage$")
	public void user_reloads_the_ApplicationPage() throws Throwable
	{
		refPresentationProfileStepDef.reloadAppPage();
	}


		
	//=============================== Chaitanya =========================================//

	@Then("^validate the \"([^\"]*)\" functionaity of assigned \"([^\"]*)\" DPkey for the created presentation$")
	public void validate_the_functionaity_of_assigned_DPkey_for_the_created_presentation(String arg1, String arg2) throws Throwable {
		refPresentationProfileStepDef.validate_the_functionaity_of_assigned_DPkey_for_the_created_presentation(arg1,arg2);
	   
	}


	@Then("^validate the \"([^\"]*)\" in Presentation deck$")
	public void validate_the_in_Presentation_deck(String arg1) throws Throwable {
		refPresentationProfileStepDef.validate_the_Presentation_deck_for(arg1);
	    
	}

	
	@Then("^select assigned DP and verify LOB inspector$")
	public void select_assigned_DP_and_verify_LOB_inspector() throws Throwable {
		refPresentationProfileStepDef.validateLOBInspectorWindow();
	}

	@When("^User Modify the captured decison \"([^\"]*)\" values in Payer/LOB Grid decision popup$")
	public void user_Modify_the_captured_decison_values_in_Payer_LOB_Grid_decision_popup(String arg1) throws Throwable {
		refPresentationProfileStepDef.modifyCapturedDecisionValuesinPayerLOBGridPopup(arg1);
	}
	
	
	@Then("^validate the list of DPs in Presentationview$")
	public void validate_the_list_of_DPs_in_Presentationview() throws Throwable {
		refPresentationProfileStepDef.validate_the_list_of_DPs_in_Presentationview();
	    
	}
	
	@Then("^validate the notes section in presentationdeck with DB$")
	public void validate_the_notes_section_in_presentationdeck_with_DB() throws Throwable {
		refPresentationProfileStepDef.validate_the_notes_section_in_presentationdeck_with_DB();
	}

	@Then("^validate the display hove text of priortiy reasons in presentaiondeck$")
	public void validate_the_display_hove_text_of_priortiy_reasons_in_presentaiondeck() throws Throwable 
	{
		refPresentationProfileStepDef.validate_the_display_hove_text_of_priortiy_reasons_in_presentaiondeck();
	 
	}

	//********************************************************************
	
	@Given("^Assign the \"([^\"]*)\" to the  \"([^\"]*)\"$")
	public void assign_the_to_the(String arg1, String arg2) throws Throwable
	{
	 
	}

	@Given("^RuleRelationshipIcon and text on hover should be displayed for the \"([^\"]*)\" in the \"([^\"]*)\"$")
	public void rulerelationshipicon_and_text_on_hover_should_be_displayed_for_the_in_the(String LevelToCheck, String DeckName) throws Throwable
	{
		refPresentationProfileStepDef.validateTheRuleRelationToolTip(LevelToCheck,DeckName);
	}
	

	@Then("^validate the Presentations created for the client \"([^\"]*)\"$")
	public void validate_the_Presentations_created_for_the_client(String arg1) throws Throwable 
	{
		refPresentationProfileStepDef.validate_the_Presentations_created_for_the_client(arg1);
	    
	}
	
	@Then("^User validates \"([^\"]*)\" in \"([^\"]*)\"$")
	public void user_validates_in(String arg1, String arg2) {
		refPresentationProfileStepDef.userValidates(arg1,arg2);
	}
	

	@Then("^user should be able to create presentation in Change Opportunities$")
	public void user_should_be_able_to_create_presentation_in_Change_Opportunities() {
		refPresentationProfileStepDef.createNewpresentationinChangeOpportunities();
	   
	}
	
	@Then("^user should not create presentation with same name in Change Opportunities$")
	public void user_should_not_create_presentation_with_same_name_in_Change_Opportunities() {
		refPresentationProfileStepDef.userShouldnotChangePresntationwithSamename();
	   
	}

	@Then("^verify \"([^\"]*)\" functionality in Edit Topic popup$")
	public void verify_functionality_in_Edit_Topic_popup(String arg1) 
	{
		refPresentationProfileStepDef.verifygivenfunctionalityinEditTopicPopup(arg1);
	}   

	@Then("^User validates the Assignment of Dp's for the Profile \"([^\"]*)\" with DPType as \"([^\"]*)\"$")
	public void user_validates_the_Assignment_of_Dp_s_for_the_Profile_with_DPType_as(String arg1, String arg2) throws InterruptedException {
		refPresentationProfileStepDef.userValidatesDPAssignmentforProfileWithDPType(arg1,arg2);
	}
	
	@Then("^User validates \"([^\"]*)\" functionality for the validation \"([^\"]*)\" and DPType \"([^\"]*)\"$")
	public void user_validates_functionality_for_the_validation_and_DPType(String arg1, String arg2, String arg3) throws InterruptedException {
		refPresentationProfileStepDef.userValidtesFunctionalityForDPType(arg1,arg2,arg3);
	}
	
	@Then("^validate edit topic description$")
	public void validate_edit_topic_description() throws InterruptedException {
		refPresentationProfileStepDef.validate_edit_topic_description();
	}
	
	@Then("^capture decision to one PPS combination$")
	public void capture_decision_to_one_PPS_combination() throws InterruptedException {
		refPresentationProfileStepDef.capture_decision_to_one_PPS_combination();
	}
	
	@Then("^user validates change summary data for \"([^\"]*)\" for \"([^\"]*)\"$")
	public void user_validates_change_summary_data_for_for(String arg1, String arg2) {
		refPresentationProfileStepDef.userValidatesChangeSummaryData(arg1,arg2);
	}
	
	@Then("^User validates the \"([^\"]*)\" of Dp's for the Profile in \"([^\"]*)\"$")
	public void user_validates_the_of_Dp_s_for_the_Profile_in(String arg1, String arg2) {
		refPresentationProfileStepDef.userValidatesDpForProfile(arg1,arg2);
	}
}
