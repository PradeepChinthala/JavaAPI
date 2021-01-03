package project.features.steps;

import cucumber.api.PendingException;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import net.thucydides.core.annotations.Steps;
import net.thucydides.core.steps.ScenarioSteps;
import projects.steps.definitions.PresentationDeckStepDef;

public class PresentationDeckSteps extends ScenarioSteps
{
	@Steps
	PresentationDeckStepDef  refPresentationDeckStepDef;



@When("^Presentation profile name should be displayed in the header section$")
public void presentation_profile_name_should_be_displayed_in_the_header_section() throws Throwable {
	refPresentationDeckStepDef.validateProflNameinHeader();

}

@When("^MedicalPolicy and Topics should be expanded$")
public void medicalpolicy_and_Topics_should_be_expanded() throws Throwable {
   

}

@Then("^All the DP cards assigned to the respective PresentationProfile  should be displayed in the PresentationDeck$")
public void all_the_DP_cards_assigned_to_the_respective_PresentationProfile_should_be_displayed_in_the_PresentationDeck() throws Throwable 
{
   
	refPresentationDeckStepDef.validateDPCardsAssignmentfromOppDeck();

}

@Then("^The DP cards should be displayed based on MedicalPolicy and Topic$")
public void the_DP_cards_should_be_displayed_based_on_MedicalPolicy_and_Topic() throws Throwable {
   

}

@Then("^the DP number should be displayed on each DP card$")
public void the_DP_number_should_be_displayed_on_each_DP_card() throws Throwable 
{
	refPresentationDeckStepDef.validateDPNoOnDPCards();

}

@Then("^\"([^\"]*)\" should be displayed in alphabetic ascending Order according to  \"([^\"]*)\"$")
public void should_be_displayed_in_alphabetic_ascending_Order_according_to(String sSortingCheckType, String arg2) throws Throwable {
	refPresentationDeckStepDef.validatePresentationDeckSorting(sSortingCheckType,arg2);

}

@Then("^DPs should be displayed in alphanumeric ascending Order within the Topics according to  \"([^\"]*)\"$")
public void dps_should_be_displayed_in_alphanumeric_ascending_Order_within_the_Topics_according_to(String arg1) throws Throwable 
{

}

@Given("^The User is viewing details on a \"([^\"]*)\"$")
public void the_User_is_viewing_details_on_a(String arg1) throws Throwable {
   

}

@When("^The User clicks on any other PresentationProfile$")
public void the_User_clicks_on_any_other_PresentationProfile() throws Throwable {
   

}

@Then("^The presentation deck should display details of the  PresentationProfile  \"([^\"]*)\"$")
public void the_presentation_deck_should_display_details_of_the_PresentationProfile(String arg1) throws Throwable {
   

}

@Then("^The Edit icon should get displayed at the Topic level$")
public void the_Edit_icon_should_get_displayed_at_the_Topic_level() throws Throwable
{
	refPresentationDeckStepDef.validateTopicsEditIcons();
}



@Then("^The User clicks on the Edit icon and Popup should be displayed with buttons Save Submit and Cancel$")
public void the_User_clicks_on_the_Edit_icon_and_Popup_should_be_displayed_with_buttons_Save_Submit_and_Cancel() throws Throwable {
	refPresentationDeckStepDef.validateEditTopicDescrBtns();
	
}
	
@Then("^The presentation deck should display details of the other profile$")
public void the_presentation_deck_should_display_details_of_the_other_profile() throws Throwable
{
	refPresentationDeckStepDef.validateProflNameinHeader();
}

@Then("^User must view checkboxes for all the DPCards in the \"([^\"]*)\"$")
public void user_must_view_checkboxes_for_all_the_DPCards_in_the(String sDeckName) throws Throwable 
{
	refPresentationDeckStepDef.validateDPCardCheckBoxes(sDeckName);    
}

@Then("^User should be able to \"([^\"]*)\" all DPCards in the \"([^\"]*)\"$")
public void user_should_be_able_to_all_DPCards_in_the(String sActionOnChkbox, String sDeckName) throws Throwable
{
	refPresentationDeckStepDef.validateChkboxesSelection(sActionOnChkbox,sDeckName);
}

@When("^The User hovers over the DP Key on the \"([^\"]*)\"$")
public void the_User_hovers_over_the_DP_Key_on_the(String arg1) throws Throwable 
{
   
}

@Then("^The DPDescription should be visible in a Tooltip$")
public void the_DPDescription_should_be_visible_in_a_Tooltip() throws Throwable 
{
    
}

@Then("^\"([^\"]*)\" should be same as in Opportunity collection for that DP Key$")
public void should_be_same_as_in_Opportunity_collection_for_that_DP_Key(String sName) throws Throwable
{
	refPresentationDeckStepDef.validateDPKeyDescriptionfromDB(sName);   
}

@When("^The User hovers over the DP Key so that DPDescription visible on Tooltip on the \"([^\"]*)\"$")
public void the_User_hovers_over_the_DP_Key_so_that_DPDescription_visible_on_Tooltip_on_the(String sKey) throws Throwable
{
	refPresentationDeckStepDef.validateDPKeyDescToolTip(sKey);
}

@Given("^The CPM is viewing the PresentationDeck$")
public void the_CPM_is_viewing_the_PresentationDeck() throws Throwable
{
  
}

@Then("^The sum of the RawSavings  should be displayed on the \"([^\"]*)\"$")
public void the_sum_of_the_RawSavings_should_be_displayed_on_the(String arg1) throws Throwable 
{
	refPresentationDeckStepDef.validateRawSavingsOnDPCardWithDB(arg1);
}

@Then("^The  RawSavings displayed on the \"([^\"]*)\"  should be same as DB Value$")
public void the_RawSavings_displayed_on_the_should_be_same_as_DB_Value(String sDPCard) throws Throwable 
{
	refPresentationDeckStepDef.validateRawSavingsOnDPCardWithDB(sDPCard);
}

@When("^There is Raw Opportunity RVA Savings for the assigned Payershorts \"([^\"]*)\" and LOBs \"([^\"]*)\" in the PresentationDeck$")
public void there_is_Raw_Opportunity_RVA_Savings_for_the_assigned_Payershorts_and_LOBs_in_the_PresentationDeck(String sPayershorts, String sLOBs) throws Throwable
{
	refPresentationDeckStepDef.checkRAWSavingsforthePresentation(sPayershorts,sLOBs);
}

@Given("^User clicks on \"([^\"]*)\" for a  topic \"([^\"]*)\" under medical policy \"([^\"]*)\"$")
public void user_clicks_on_for_a_topic_under_medical_policy(String sIconName, String sTopic, String sMedPolicy) throws Throwable 
{
	refPresentationDeckStepDef.clickOnEditTopicBasedonCriteria(sIconName,sTopic,sMedPolicy);  
}

@Given("^User edits the Topic description as \"([^\"]*)\"$")
public void user_edits_the_Topic_description_as(String sTopicDescription) throws Throwable 
{
	refPresentationDeckStepDef.editTopicDescription(sTopicDescription);
  
}

@When("^User clicks on EditPopup \"([^\"]*)\" button$")
public void user_clicks_on_EditPopup_button(String sBtnName) throws Throwable 
{
	refPresentationDeckStepDef.clickEditTopicButton(sBtnName);  
}


@Then("^The system should \"([^\"]*)\"  the edited \"([^\"]*)\" for the \"([^\"]*)\" in the DB$")
public void the_system_should_the_edited_for_the_in_the_DB(String sExpectedState, String sTopicDescr, String sPresProfile) throws Throwable
{
	refPresentationDeckStepDef.validateTopicDescwithDB(sExpectedState,sTopicDescr,sPresProfile)  ;
}


@Given("^User clicks on \"([^\"]*)\" based on indexnumber \"([^\"]*)\"$")
public void user_clicks_on_based_on_indexnumber(String arg1, String arg2) throws Throwable
{

}

@Then("^The element \"([^\"]*)\" should be \"([^\"]*)\"$")
public void the_element_should_be(String sElement, String sExpectedValue) throws Throwable
{
	refPresentationDeckStepDef.verifyElement(sElement,sExpectedValue);
}

@When("^User clicks on other \"([^\"]*)\"$")
public void user_clicks_on_other(String sElementToClick) throws Throwable
{
	refPresentationDeckStepDef.clickOtherPresProfile(sElementToClick);
  
}

@Given("^User clicks on \"([^\"]*)\" for a MedicalPolicy \"([^\"]*)\" and topic \"([^\"]*)\"$")
public void user_clicks_on_for_a_MedicalPolicy_and_topic(String arg1, String arg2, String arg3) throws Throwable
{
   
  
}


@Then("^The last saved \"([^\"]*)\" should be \"([^\"]*)\" on Edit popup editable section$")
public void the_last_saved_should_be_on_Edit_popup_editable_section(String sExpectedstate, String sTopicDescription) throws Throwable {
	     refPresentationDeckStepDef.verifyEditDescriptionSaved(sExpectedstate,sTopicDescription);
}


@When("^User deletes the \"([^\"]*)\"$")
public void user_deletes_the(String sTopicDesc) throws Throwable 
{
	refPresentationDeckStepDef.deleteTopicDescription(sTopicDesc);
  
}

@Then("^The buttons \"([^\"]*)\" should be \"([^\"]*)\"$")
public void the_buttons_should_be(String sBtnName, String sPlaceHolderArg) throws Throwable
{
	refPresentationDeckStepDef.validateButtonStatus(sBtnName,sPlaceHolderArg);
  
}

@Given("^CPM has logged in with \"([^\"]*)\"$")
public void cpm_has_logged_in_with(String arg1) throws Throwable
{
   
  
}

@Given("^is on the Edit topic Pop up for \"([^\"]*)\" of \"([^\"]*)\"and \"([^\"]*)\"$")
public void is_on_the_Edit_topic_Pop_up_for_of_and(String arg1, String arg2, String arg3) throws Throwable 
{
   
  
}

@When("^CPM does some changes on the Topic description and save$")
public void cpm_does_some_changes_on_the_Topic_description_and_save() throws Throwable 
{
   
  
}

@When("^logs out and logs in with other \"([^\"]*)\"$")
public void logs_out_and_logs_in_with_other(String arg1) throws Throwable
{
   
  
}

@When("^access the same \"([^\"]*)\"<Presentation profile>\"$")
public void access_the_same_Presentation_profile(String arg1) throws Throwable 
{
   
  
}

@Then("^the changes done by \"([^\"]*)\" for \"([^\"]*)\" should be displayed$")
public void the_changes_done_by_for_should_be_displayed(String arg1, String arg2) throws Throwable 
{
   
  
}

@Then("^verify the header of \"([^\"]*)\"$")
public void verify_the_header_of(String arg1) throws Throwable {
	refPresentationDeckStepDef.verifyPresentationDeckHeader();
}

@Then("^click on close icon of \"([^\"]*)\"$")
public void click_on_close_icon_of(String arg1) throws Throwable {
	refPresentationDeckStepDef.clickPresentationDeckCloseIcon();
}

@Then("^verify the count of DP in Presentation deck$")
public void verify_the_count_of_DP_in_Presentation_deck() throws Throwable {
	refPresentationDeckStepDef.verifyTotalDPCount();
}

@Given("^verify the count of DP's at Medical Policy Level$")
public void verify_the_count_of_DP_s_at_Medical_Policy_Level() throws Throwable {
	refPresentationDeckStepDef.verifyDPCountForMedicalPolicies();
}

@Given("^verify the count of DP's at Topic Level$")
public void verify_the_count_of_DP_s_at_Topic_Level() throws Throwable {
	refPresentationDeckStepDef.verifyDPCountForEachTopic();
}

@Given("^unassign the created presentation at \"([^\"]*)\" level$")
public void unassignTheCreatedPresentationatAllLevels(String sLevel) throws Throwable {
	refPresentationDeckStepDef.unAssigntheCreatedPresentationatAllLevels(sLevel);
}

@Then("^filter unassigned \"([^\"]*)\"$")
public void FilterUnassignedMedicalPolicy(String sCategory) throws Throwable {
	refPresentationDeckStepDef.selectGivenMedicalPolicyorTopic(sCategory);
}

@Then("^verify icons of Presentation$")
public void verify_icons_of_Presentation() throws Throwable {
	refPresentationDeckStepDef.verifyPresentationIcons();
}

@Then("^The \"([^\"]*)\" popup should be closed$")
public void the_popup_should_be_closed(String sPopupTitle) throws Throwable {
	refPresentationDeckStepDef.validatePopupClosed(sPopupTitle);
}

@When("^User clicks the \"([^\"]*)\" button$")
public void user_clicks_the_button(String sBtnName) throws Throwable 
{
	refPresentationDeckStepDef.clickButton(sBtnName);
	
}

@Given("^User captures the \"([^\"]*)\" for the \"([^\"]*)\"$")
public void user_captures_the_for_the(String sToCapture, String sCaptureFor) throws Throwable 
{
	refPresentationDeckStepDef.capureDPs(sToCapture,sCaptureFor);
   
}

@Then("^The  \"([^\"]*)\" should display  DPs list that are assigned under that \"([^\"]*)\" for the \"([^\"]*)\"$")
public void the_should_display_DPs_list_that_are_assigned_under_that_for_the(String sScreenName, String scapturedFor, String sPresProfileName) throws Throwable 
{
	refPresentationDeckStepDef.validateDPsinUI(sScreenName,scapturedFor,sPresProfileName);
   
}

@When("^User selects a \"([^\"]*)\"  in the  \"([^\"]*)\" popup$")
public void user_selects_a_in_the_popup(String arg1, String arg2) throws Throwable
{
   
   
}

@Then("^User selects a \"([^\"]*)\" then the \"([^\"]*)\" should be displayed in the  \"([^\"]*)\" popup$")
public void user_selects_a_then_the_should_be_displayed_in_the_popup(String sRequiredVal, String sRequiredVal2, String sComponent) throws Throwable
{
	refPresentationDeckStepDef.validateDPDescrinPopup(sRequiredVal,sRequiredVal2,sComponent);   
}

@Then("^The \"([^\"]*)\" should be \"([^\"]*)\"  in  \"([^\"]*)\"$")
public void the_should_be_in(String sFieldToValidate, String sExpectedStatus, String sFieldContainer) throws Throwable
{

	String PlaceHolderArg1 = "";
	String PlaceHolderArg2 = "";
	refPresentationDeckStepDef.validateFieldInitialStatus(sFieldToValidate,sExpectedStatus, sFieldContainer,PlaceHolderArg1);
     
}

@Then("^\"([^\"]*)\" headers should be in defined format in \"([^\"]*)\"$")
public void headers_should_be_in_defined_format_in(String sFieldToValidate, String sFiledContainer) throws Throwable 
{
	String sPlaceHolderArg1 = "";
	String sPlaceHolderArg2 = "";
	refPresentationDeckStepDef.validateFieldHeaders(sFieldToValidate,sFiledContainer,sPlaceHolderArg1,sPlaceHolderArg2);
}

@When("^User Unassigns \"([^\"]*)\" under which that \"([^\"]*)\" is structured$")
public void user_Unassigns_under_which_that_is_structured(String ParentItem, String ChildItem) throws Throwable
{
	String PlaceHolderArg1 = "";
	String PlaceHolderArg2 = "";
	refPresentationDeckStepDef.unAssignforParentMPorTopic(ParentItem,ChildItem,PlaceHolderArg1,PlaceHolderArg2);
    
}


@When("^The \"([^\"]*)\" popup should display \"([^\"]*)\" as Header$")
public void the_popup_should_display_as_Header(String PopupName, String ValToCheck) throws Throwable
{
	refPresentationDeckStepDef.validateEditPopupHeader(PopupName,ValToCheck);
}

@When("^The TopicDescription should be in \"([^\"]*)\" mode in \"([^\"]*)\"$")
public void the_TopicDescription_should_be_in_mode_in(String ExpectedState, String SectionName) throws Throwable 
{
	refPresentationDeckStepDef.validateTopicDescriptionSections(ExpectedState,SectionName);		
}

@When("^The Both sections should have Expand/collapse icons with sections in expanded mode by default$")
public void the_Both_sections_should_have_Expand_collapse_icons_with_sections_in_expanded_mode_by_default() throws Throwable 
{
	refPresentationDeckStepDef.validateExpandCollpaseIcons();		
}


@When("^The Expand/collapse icons should be functional$")
public void the_Expand_collapse_icons_should_be_functional() throws Throwable
{
	refPresentationDeckStepDef.validateExpandCollpaseOperations();
}

@Then("^DPCard ageing should display as Captured$")
public void dpcard_ageing_should_display_as_Captured() throws Throwable
{
	refPresentationDeckStepDef.validateDPCardAgeing(); 
}

@Given("^User selects the Presentation for assignment$")
public void user_selects_the_Presentation_for_assignment() throws Throwable {
	refPresentationDeckStepDef.selectPresentationForAssignment ();
}


@Given("^User has \"([^\"]*)\" a PresentationProfile$")
public void user_has_a_PresentationProfile(String sOperation) throws Throwable
{
	refPresentationDeckStepDef.PresentationProfileOperation(sOperation);
}


@Then("^The checkbox for \"([^\"]*)\" should be \"([^\"]*)\"$")
public void the_checkbox_for_should_be(String ChkBoxName, String Expectedstatus) throws Throwable 
{
	refPresentationDeckStepDef.validateChkboxStatus(ChkBoxName,Expectedstatus);
}

@When("^The User clicks on the \"([^\"]*)\"$")
public void the_User_clicks_on_the(String ControlName) throws Throwable 
{
	refPresentationDeckStepDef.clickControl(ControlName);
}

@Then("^The system should mark the \"([^\"]*)\" status as \"([^\"]*)\" in DB$")
public void the_system_should_mark_the_status_as_in_DB(String PreasentationProfileName, String ExpecteDBStatus) throws Throwable 
{
	 refPresentationDeckStepDef.validateProfileStatusinDB(PreasentationProfileName,ExpecteDBStatus);
}


@Given("^The ProfilePage should have the Red border to indicate lockedview$")
public void the_ProfilePage_should_have_the_Red_border_to_indicate_lockedview() throws Throwable 
{
	refPresentationDeckStepDef.validateUIforMarkedPreasentation();
	
}

@Given("^Click on Popup \"([^\"]*)\" button$")
public void click_on_Popup_button(String BtnName) throws Throwable 
{
	refPresentationDeckStepDef.ClickPopupButton(BtnName);	
}

@Then("^Refresh the page$")
public void refresh_the_page() throws Throwable {
	refPresentationDeckStepDef.RefreshPage();
    
}

@When("^user clicks created \"([^\"]*)\"$")
public void user_clicks_created(String arg1) throws Throwable {
	refPresentationDeckStepDef.clickCreatedPresentation();
}

@Then("^select oppurtunities at \"([^\"]*)\" level and finalize the oppurtunities$")
public void select_oppurtunities_at_level_and_finalize_the_oppurtunities(String arg1) throws Throwable {
	refPresentationDeckStepDef.selectAndFinalizeOpurtunities(arg1);
}

@Given("^verify check box is removed for \"([^\"]*)\" finalized oppurtunity$")
public void verify_check_box_is_removed_for_finalized_oppurtunity(String arg1) throws Throwable {
	refPresentationDeckStepDef.verifyChkboxOfFinalizedOppurtunity(arg1);
}

@Given("^verify check box exists for \"([^\"]*)\" if decisions are not finalized for all oppurtunities$")
public void verify_check_box_exists_for_if_decisions_are_not_finalized_for_all_oppurtunities(String arg1) throws Throwable {
	refPresentationDeckStepDef.verifyChkboxExistsIfAllPPSNotFinalized(arg1);
}


@Given("^validate decision capture pop up by choosing \"([^\"]*)\"$")
public void validate_decision_capture_pop_up_by_choosing(String arg1) throws Throwable {
	refPresentationDeckStepDef.validateCaptureDecisionPopup(arg1); 
}

@Then("^validate disabled icons of presentation deck$")
public void validate_disabled_icons_of_presentation_deck() throws Throwable {
	refPresentationDeckStepDef.validateDisabledIconsOfDeck();
}

@Then("^validate DP count and savings for both presentation profiles after reassigning \"([^\"]*)\"$")
public void validate_DP_count_and_savings_for_both_presentation_profiles_after_reassigning(String arg1) throws Throwable {
	refPresentationDeckStepDef.verifyDPCountAndSavingsForBothPP(arg1);
}

@Then("^verify raw savings and DP count of PresentationProfile \"([^\"]*)\"$")
public void verify_raw_savings_and_DP_count_of(String arg1) throws Throwable {
	refPresentationDeckStepDef.verifySavingsAndDPCountOfPresentation(arg1);
	
}

@Then("^The corresponding Companion, Counterpart, and Out of Sequence Rules or DPs for the \"([^\"]*)\"  are included in the assignment to the same \"([^\"]*)\"$")
public void the_corresponding_Companion_Counterpart_and_Out_of_Sequence_Rules_or_DPs_for_the_are_included_in_the_assignment_to_the_same(String CapturedDPKey, String PresName) throws Throwable
{
	refPresentationDeckStepDef.verifyAssociatedDPsinthePresentation(CapturedDPKey,PresName);
}
@When("^The user selects the \"([^\"]*)\"PayerLOBGrid\"$")
public void the_user_selects_the_PayerLOBGrid(String arg1) throws Throwable {
   
}


@Then("^Validate the RuleRelationship Grid data details in  \"([^\"]*)\"$")
public void validate_the_RuleRelationship_Grid_data_details_in(String ApplicationPage) throws Throwable
{
	refPresentationDeckStepDef.validateTheRuleRelationshipGridDetails(ApplicationPage);
}

@When("^User selects \"([^\"]*)\" and validate DP's with DP Type as \"([^\"]*)\" and latest decision as \"([^\"]*)\"$")
public void user_selects_and_validate_DP_s_with_DP_Type_as_and_latest_decision_as(String arg1, String arg2, String arg3) {
	refPresentationDeckStepDef.validateDPWithDPtypeAndLatestDecision(arg1,arg2,arg3);
}

//############################ Method to verify second part of subsequent change in PM ########################################################

	@Then("^verify PM after pipeline for the captured data \"([^\"]*)\",\"([^\"]*)\",\"([^\"]*)\",\"([^\"]*)\"$")
	public void verify_PM_after_pipeline_for_the_captured_data(String arg1, String arg2, String arg3, String arg4) throws Throwable {
		refPresentationDeckStepDef.verifyPMafterPipeline(arg1, arg2, arg3);
	}


}
