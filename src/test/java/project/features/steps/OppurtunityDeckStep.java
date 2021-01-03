package project.features.steps;

import cucumber.api.PendingException;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import cucumber.api.java.en.And;
import cucumber.api.java.en.Given;
import net.thucydides.core.annotations.Steps;
import projects.steps.definitions.OppurtunityDeckStepDef;

public class OppurtunityDeckStep 
{
	
@Steps
OppurtunityDeckStepDef   OppurtunityDeckStepDef;
		
	@Then("^The User have assigned some DPs to the PresentationProfile$")
	public void the_User_have_assigned_some_DPs_to_the_PresentationProfile() throws Throwable 
		{
			OppurtunityDeckStepDef.assignDPstoPresentationProfile();
		}
		
	@When("^The CPM/CMD clicks on the Available Opportunity deck header$")
	public void the_CPM_CMD_clicks_on_the_Available_Opportunity_deck_header() throws Throwable 
		{
			OppurtunityDeckStepDef.clickAvailableDPsDeck();
	           
		}
	
	@Then("^\"([^\"]*)\" section should be \"([^\"]*)\"$")
	public void section_should_be(String sContainer, String sExpectedState) throws Throwable
	{       
		OppurtunityDeckStepDef.validateOppDeckState(sContainer,sExpectedState);
	}
	
	@Then("^The available OportunitiesDeck with DP count as \"([^\"]*)\" should be displayed$")
	public void the_available_OportunitiesDeck_with_DP_count_as_should_be_displayed(String sCount) throws Throwable
	{
		OppurtunityDeckStepDef.validateDPCount(sCount);
	 
	}
	
	@Then("^User must view checkboxes for all the DPCards in the \"([^\"]*)\" deck$")
	public void user_must_view_checkboxes_for_all_the_DPCards_in_the_deck(String sDeckName) throws Throwable 
	{
		OppurtunityDeckStepDef.validateChkboxesforDPCards(sDeckName);
	}
	
	@Then("^User should be able to \"([^\"]*)\" all DPCards in the \"([^\"]*)\" deck$")
	public void user_should_be_able_to_all_DPCards_in_the_deck(String sActionOnchkbox, String sDeckName) throws Throwable 
	{
		OppurtunityDeckStepDef.validateChkboxesStateforDPCards(sActionOnchkbox,sDeckName);
	}
	
	
	@Then("^A  Popup view of the opportunities assigned to PresentationProfiles should be displayed$")
	public void a_Popup_view_of_the_opportunities_assigned_to_PresentationProfiles_should_be_displayed() throws Throwable 
	{
	    
	}
	
	@Then("^avaiable opportunities should be display for the selected client$")
	public void avaiable_opportunities_should_be_display_for_the_selected_client() throws Throwable {
	    
	}
	
	@When("^\"([^\"]*)\"  is viewing the opportunity assignments from Work to do pop up$")
	public void is_viewing_the_opportunity_assignments_from_Work_to_do_pop_up(String arg1) throws Throwable {
	   
	}
	
	@Then("^the contents should be only for the filtered \"([^\"]*)\" and \"([^\"]*)\" on the filter panel$")
	public void the_contents_should_be_only_for_the_filtered_and_on_the_filter_panel(String arg1, String arg2) throws Throwable {
	   
	}
	
	@Then("^popup window should display \"([^\"]*)\", '<WOrk to do>\"([^\"]*)\"<Payers>\"([^\"]*)\"<LOB>\"([^\"]*)\"<Assignment>\" \\(seperate rows\\)$")
	public void popup_window_should_display_WOrk_to_do_Payers_LOB_Assignment_seperate_rows(String arg1, String arg2, String arg3, String arg4) throws Throwable
	{
	  
	}
	
	@Then("^the contents of the grid are sorted by default by Payers$")
	public void the_contents_of_the_grid_are_sorted_by_default_by_Payers() throws Throwable
	{
		OppurtunityDeckStepDef.validateWorkToDoGridSorting();
		
	}
	
	
	@When("^User is viewing the opportunity assignments with no assignments yet$")
	public void user_is_viewing_the_opportunity_assignments_with_no_assignments_yet() throws Throwable
	{
		OppurtunityDeckStepDef.checkNoAssignmentOpportunities();	
	}
	
	@Then("^popup window should display 'No assignment' for the unassigned Payer/LOB set in assignment column$")
	public void popup_window_should_display_No_assignment_for_the_unassigned_Payer_LOB_set_in_assignment_column() throws Throwable 
	{
		OppurtunityDeckStepDef.validateNoAssignmentforUnassignedPayerLOBSet();
	}
	
	@When("^User clicks on the WorkToDoCount hyperlink with \"([^\"]*)\" on a DPCard  in the AvailableOpportunityDeck$")
	public void user_clicks_on_the_WorkToDoCount_hyperlink_with_on_a_DPCard_in_the_AvailableOpportunityDeck(String sArg) throws Throwable 
	{
		OppurtunityDeckStepDef.clickWorkToDoLinkOnDPCard(sArg);	  
	}
	
	@When("^User clicks on the WorkToDoCount hyperlink on a DPCard for a \"([^\"]*)\" in the AvailableOpportunityDeck with PopupView$")
	public void user_clicks_on_the_WorkToDoCount_hyperlink_on_a_DPCard_for_a_in_the_AvailableOpportunityDeck_with_PopupView(String sDPKey) throws Throwable 
	{
		OppurtunityDeckStepDef.validateWorkTodoCountPopup(sDPKey);	
	}
	
	@Then("^Popup window should display DPKey, WorkToDo,Payers and in separate rows to display \"([^\"]*)\" and Assignments$")
	public void popup_window_should_display_DPKey_WorkToDo_Payers_and_in_separate_rows_to_display_and_Assignments(String sArg) throws Throwable
	{
		OppurtunityDeckStepDef.validateWorkTodoPopupUiElements(sArg);
	}
	
	@Given("^The User have assigned \"([^\"]*)\" to the \"([^\"]*)\" PresentationProfiles$")
	public void the_User_have_assigned_to_the_PresentationProfiles(String sArg1, String sArg2) throws Throwable 
	{
		OppurtunityDeckStepDef.assignMultiplePresentaionstoDP(sArg1,sArg2);
	}
	
	@Then("^The WorkToDo popup should display the assigned PresentationNames$")
	public void the_WorkToDo_popup_should_display_the_assigned_PresentationNames() throws Throwable
	{
		OppurtunityDeckStepDef.validatePresNamesAssignedinWorktoDoPopup();
	}
	@Then("^The Grid contents should only be displayed for the filtered \"([^\"]*)\" and \"([^\"]*)\" in the FilterSection$")
	public void the_Grid_contents_should_only_be_displayed_for_the_filtered_and_in_the_FilterSection(String sPayershorts, String sLOBs) throws Throwable
	{
		OppurtunityDeckStepDef.validateWorkToDoGridPayersLOBs(sPayershorts,sLOBs);
	}
	
	@When("^The DPCard WorkToDo Grid should display \"([^\"]*)\" and \"([^\"]*)\" same as in MongoDB for the \"([^\"]*)\"$")
	public void the_DPCard_WorkToDo_Grid_should_display_and_same_as_in_MongoDB_for_the(String sPayershorts, String sLOBs, String sDPKey) throws Throwable 
	{
		 OppurtunityDeckStepDef.validateWorkToDoGridDataWithMongoDB(sPayershorts,sLOBs,sDPKey);	
	}
	
	@When("^User selects the assign icon at the \"([^\"]*)\"$")
	public void user_selects_the_assign_icon_at_the(String sAssignLevel) throws Throwable {
		  OppurtunityDeckStepDef.selectAssignIconAtLevel(sAssignLevel);
	}
	
	@When("^Assign pop-up is displayed with \"([^\"]*)\"  names$")
	public void assign_pop_up_is_displayed_with_names(String sPlaceholderArg) throws Throwable 
	{
		OppurtunityDeckStepDef  .validateAssignPopupPresenationNames(sPlaceholderArg);
	}
	
	@When("^The User assigns DPCard to a \"([^\"]*)\" PresentationProfiles at the level \"([^\"]*)\"$")
	public void the_User_assigns_DPCard_to_a_PresentationProfiles_at_the_level(String sPresCount, String sAssignLevel) throws Throwable 
	{
		OppurtunityDeckStepDef.assignDPToPresentation(sPresCount,sAssignLevel);
	}
	
	@Then("^The pop-up should be closed$")
	public void the_pop_up_should_be_closed() throws Throwable 
	{
		OppurtunityDeckStepDef.validateAssignPopupClosed()  ;
	}
	
	@When("^The User assigns DPCard to a \"([^\"]*)\" PresentationProfiles at the level \"([^\"]*)\" and \"([^\"]*)\"$")
	public void the_User_assigns_DPCard_to_a_PresentationProfiles_at_the_level_and(String sPresCount, String sLevel, String sPlaceholderArg) throws Throwable
	{
		OppurtunityDeckStepDef.assignMultiplePresentations(sPresCount,sLevel,sPlaceholderArg);
		
	}
	
	@When("^The User clicks on Assign Popup \"([^\"]*)\" button$")
	public void the_User_clicks_on_Assign_Popup_button(String sBtnName) throws Throwable
	{
		OppurtunityDeckStepDef.clickAssignPopupButton(sBtnName); 
		
	}
	
	@Then("^verify \"([^\"]*)\" message$")
	public void verify_message(String sMessage) throws Throwable {
		OppurtunityDeckStepDef.validateNoOppurtunityMessage(sMessage);
	}
	
	@Then("^verify \"([^\"]*)\" message is disappeared$")
	public void verify_message_is_disappeared(String sMessage) throws Throwable {
		OppurtunityDeckStepDef.verifyOppurtunitiesInOppurtunityGrid(sMessage);
	}
	
	@Then("^Profile names are removed from flip view of DP card from available opportunity deck$")
	public void verifyProfilenamesareRemovedfromflipViewinOppurtunityDeck() throws Throwable {
		OppurtunityDeckStepDef.verifyProfilenamesareRemovedfromflipViewinOppurtunityDeck();
	}
	
	@Then("^The DPs should be assigned to the selected \"([^\"]*)\"$")
	public void the_DPs_should_be_assigned_to_the_selected(String sPlaceholderArg) throws Throwable
	{
		//OppurtunityDeckStepDef.validateDPsAssignment(sPlaceholderArg);	
	}
	
	@Then("^verify \"([^\"]*)\" functionality$")
	public void verify_functionality(String arg1) throws Throwable {
		OppurtunityDeckStepDef.verifyExpandAndCollapseAllBehaviour(arg1);
	}
	
	@Given("^verify \"([^\"]*)\" functionality at Medical Policy Level$")
	public void verify_functionality_at_Medical_Policy_Level(String arg1) throws Throwable {
		OppurtunityDeckStepDef.verifyExpandAndCollapseAtMedicalPolicyLevel(arg1);
	}
	
	@Given("^verify \"([^\"]*)\" functionality at Topic Level$")
	public void verify_functionality_at_Topic_Level(String arg1) throws Throwable {
		OppurtunityDeckStepDef.verifyExpandAndCollapseAtTopicLevel(arg1);  
	}

//********************************************************************* Chaitanya *******************************************************************************//

	@Then("^validate the captured \"([^\"]*)\" under Avaialbe DPs section$")
	public void validate_the_captured_under_Avaialbe_DPs_section(String arg1) throws Throwable {
		OppurtunityDeckStepDef.validate_the_captured_data_under_Avaialbe_DPs_section(arg1);
	    
	}

	@Then("^validate the avaliable DPs count at \"([^\"]*)\"$")
	public void validate_the_avaliable_DPs_count_at(String arg1) throws Throwable {
		OppurtunityDeckStepDef.validate_the_avaliable_DPs_count_at(arg1);
	}
	
	@Then("^User filters the \"([^\"]*)\" with filter drpdown value \"([^\"]*)\" to show related DPs$")
	public void user_filters_the_with_filter_drpdown_value_to_show_related_DPs(String sPlaceHolder, String sFilterValue) throws Throwable
	{
		OppurtunityDeckStepDef.filterRelatedDPs(sPlaceHolder,sFilterValue);
	}

	@Given("^verify presentation name on each DP card$")
	public void verify_presentation_name_on_each_DP_card() throws Throwable {
		OppurtunityDeckStepDef.verifyPresentationNameOnDPCard();
	}

	@Given("^verify total savings as sum of each DP card$")
	public void verify_total_savings_as_sum_of_each_DP_card() throws Throwable {
		OppurtunityDeckStepDef.verifyTotalRawSavings();   
	}


	@Then("^validate the priority/priority reasons for the captured \"([^\"]*)\" with Priority \"([^\"]*)\" at \"([^\"]*)\"$")
	public void validate_the_priority_priority_reasons_for_the_captured_with_Priority_at(String arg1, String arg2, String arg3) throws Throwable {
		OppurtunityDeckStepDef.validate_the_priority_and_reasons_for_the_captured_data_at(arg1,arg2,arg3);
	}

	@Then("^AssignedView of the \"([^\"]*)\" should display the names of the assigned \"([^\"]*)\" for the \"([^\"]*)\"$")
	public void assignedview_of_the_should_display_the_names_of_the_assigned_for_the(String arg1, String arg2, String arg3) throws Throwable 
	{
		OppurtunityDeckStepDef.validateDPCardAssignmentViewForNewPresProfile(arg1,arg2,arg3);	
	}

	@Then("^The \"([^\"]*)\" should be in state  \"([^\"]*)\" State$")
	public void the_should_be_in_state_State(String sPlaceHolderArg, String sDPCardExpectedState) throws Throwable
	{
		OppurtunityDeckStepDef.validateDPCardState(sPlaceHolderArg,sDPCardExpectedState);	
	}	

	@Then("^Verify Reassinged Presentation in oppurtunity Available Deck$")
	public void verifyPresentationunderDP() throws Throwable
	{
		OppurtunityDeckStepDef.verifyPresentationforDP();	
	}
	
	@Then("^get work to do count for for given Dps$")
	public void getWorktoDoCountforDPs() throws Throwable
	{
		OppurtunityDeckStepDef.getWorktoDoCountforDPs();	
	}
		
	@Then("^Verify work to do count for for Dps after deleting the profile$")
	public void VerifyWorktoDoCountforDPs() throws Throwable
	{
		OppurtunityDeckStepDef.verifyWorktoDoCountforDPs();	
	}
	
	@Then("^Verify deleted profiles are removed from DP Card$")
	public void VerifyDeletedProfilesareremovedfromDPCard() throws Throwable
	{
		OppurtunityDeckStepDef.deletedProfilenamesareRemovedFromOppurtunityDeck();	
	}
  
  @Given("^The User have assigned first Medical Policy DPs to the PresentationProfile$")
	public void the_User_have_assigned_first_Medical_Policy_DPs_to_the_PresentationProfile() throws Throwable {
		OppurtunityDeckStepDef.assignFirstMedPolicyDPstoPresentationProfile();
	}
  
  @Given("^The User have assigned  \"([^\"]*)\"  MedicalPolicies to the PresentationProfile$")
	public void the_User_have_assigned_MedicalPolicies_to_the_PresentationProfile(String MedPolicyCount) throws Throwable 
	{
		  OppurtunityDeckStepDef.assignMultipleMedicalPoliciestoPresentationProfile(MedPolicyCount);
	}
	
	@Then("^The \"([^\"]*)\" dropdown in the Toolbar should have the \"([^\"]*)\"$")
	public void the_dropdown_in_the_Toolbar_should_have_the(String DropdownName, String Options) throws Throwable
	{	   
		OppurtunityDeckStepDef.validateDropdownValues(DropdownName,Options);
	}	
  
  	
  @When("^User is viewing the opportunity assignments \"([^\"]*)\" with WorkToDoCount \"([^\"]*)\"$")
  public void user_is_viewing_the_opportunity_assignments_with_WorkToDoCount(String sPlaceHolderArg, String WTDCount) throws Throwable 
  {
  	OppurtunityDeckStepDef.checkandClickDPBasedOnWTDCount(sPlaceHolderArg,WTDCount);
  }
  
@Then("^The AssignedView or flipped side of the \"([^\"]*)\" should display names of the all assigned \"([^\"]*)\" for the \"([^\"]*)\"$")
  public void the_AssignedView_or_flipped_side_of_the_should_display_names_of_the_all_assigned_for_the(String sPlaceholderArg1, String sPresentations, String sPlaceholderArg2) throws Throwable 
  {
  	OppurtunityDeckStepDef.validatePresNamesOnDPCard(sPlaceholderArg1,sPresentations,sPlaceholderArg2);
  }
	@When("^User retrieves the \"([^\"]*)\" with required \"([^\"]*)\"$")
	public void user_retrieves_the_with_required(String sArg1, String sArg2) throws Throwable 
	{
		OppurtunityDeckStepDef.retrieveDPForAvailableLOBs(sArg1,sArg2);
	}
  
    
  @Given("^User clicks the \"([^\"]*)\" button for the \"([^\"]*)\" with DP \"([^\"]*)\"$")
  public void user_clicks_the_button_for_the_with_DP(String arg1, String arg2, String arg3) throws Throwable
  {
  	OppurtunityDeckStepDef.clickAssignPopupButton(arg1,arg2,arg3);  
  }
  
  
  @Then("^User should be able to click on the \"([^\"]*)\" to return to view \"([^\"]*)\"$")
  public void user_should_be_able_to_click_on_the_to_return_to_view(String sCardType, String sViewName) throws Throwable
  {
  	OppurtunityDeckStepDef.ValidateDPCardView(sCardType,sViewName);
  }
  

  @Then("^verify PPS combination in DP detailed view$")
  public void verify_PPS_combination_in_DP_detailed_view() throws Throwable {
	  OppurtunityDeckStepDef.ValidatePPSDetailsinDPDetailedView(); 
  }
  
  @Given("^select All PPS and assign DP to newly created presentation profile$")
  public void select_All_PPS_and_assign_DP_to_newly_created_presentation_profile() throws Throwable {
	  OppurtunityDeckStepDef.SelectAllPPSAndAssignDPToPP();
  }

  @Then("^verify presentation name for all PPS in DP detailed view$")
  public void verify_presentation_name_for_all_PPS_in_DP_detailed_view() throws Throwable {
	  OppurtunityDeckStepDef.verifyPresentationNameForPPS();
  }
  
  @Given("^verify presentation name on DP card$")
  public void verify_presentation_name_on_DP_card() throws Throwable {
	  OppurtunityDeckStepDef.verifyPresentationNameForDP();
  }
  
  @Then("^verify Available oppurtunities elements and its functionality$")
  public void verify_Available_oppurtunities_elements_and_its_functionality() throws Throwable {
	  OppurtunityDeckStepDef.verifyAvailableOppurtunitiesElementsInHeader();
  }
  
  @Then("^validate profile name is removed from DP card after unassign$")
  public void validate_profile_name_is_removed_from_DP_card_after_unassign() throws Throwable {
	  OppurtunityDeckStepDef.verifyProfilenameRemovalfromflipViewinOppurtunityDeck();
  }

 
  @Then("^verify LOB status bar in \"([^\"]*)\"$")
  public void verify_LOB_status_bar_in(String arg1) throws Throwable {
	  OppurtunityDeckStepDef.verifyLOBstatusbarforDP(arg1);
  }


  //**************************************************************************************************************
  
  @Given("^Capture \"([^\"]*)\" for the \"([^\"]*)\" which has disposition value \"([^\"]*)\"  from \"([^\"]*)\" collection  for \"([^\"]*)\"$")
  public void capture_for_the_which_has_disposition_value_from_collection_for(String valToCapture, String ClientName, String DispositionToApply, String CollectionName, String ReqRelationship) throws Throwable 
  {
	  OppurtunityDeckStepDef.captureValuesFromCollectionForRuleRelationshipType(valToCapture,ClientName,DispositionToApply,CollectionName,ReqRelationship);
  }
  
  
  @Then("^Navigate to the \"([^\"]*)\"  in the \"([^\"]*)\"$")
  public void navigate_to_the_in_the(String CapturedOrDB, String DeckName) throws Throwable
  {
	  OppurtunityDeckStepDef.navigateToTheDPCard(CapturedOrDB,DeckName);
  }
  
  @Then("^RuleRelationshipIcon and text on hover should be displayed on the \"([^\"]*)\"$")
  public void rulerelationshipicon_and_text_on_hover_should_be_displayed_on_the(String DPCardorLOBGrid) throws Throwable 
  {
	  OppurtunityDeckStepDef.validateRuleRelationshipIconandText(DPCardorLOBGrid);
  }

  @Given("^User assigns the \"([^\"]*)\" to the \"([^\"]*)\"  at level \"([^\"]*)\"$")
  public void user_assigns_the_to_the_at_level(String DPKeySource, String Presentation, String AssignmentLevel) throws Throwable 
  {
	    OppurtunityDeckStepDef.assignDPstoThePresentationAtLevel(DPKeySource,Presentation,AssignmentLevel) ;
  }

 

  @Then("^The Application should \"([^\"]*)\"  the information message \"([^\"]*)\"$")
  public void the_Application_should_the_information_message(String ExpectedCondition, String ExpectedMessage) throws Throwable 
  {
	  OppurtunityDeckStepDef.validateTheAssignmentInfoMsg(ExpectedCondition,ExpectedMessage);
  }
  

  @Then("^verify LOB status bar in \"([^\"]*)\" for the client \"([^\"]*)\"$")
  public void verify_LOB_status_bar_in_for_the_client(String arg1, String arg2) throws Throwable {
	  OppurtunityDeckStepDef.verifyLOBstatusbarforDP(arg1, arg2);
  }
  
  @Given("^verify DP in Available oppurtunity deck$")
  public void verify_DP_in_Available_oppurtunity_deck() throws Throwable {
	  OppurtunityDeckStepDef.verifyDPInAvailableOppurtunityDeck();
  }
  
  @Given("^Capture the corresponding DPs for the \"([^\"]*)\"$")
  public void capture_the_corresponding_DPs_for_the(String DPKey) throws Throwable 
  {
	  OppurtunityDeckStepDef.captureCorrespondingDPsandRules(DPKey);
  }

  

  @Then("^validate the \"([^\"]*)\" and thier raw savings with DB in filter drawer section$")
  public void validate_the_and_thier_raw_savings_with_DB_in_filter_drawer_section(String arg1) throws Throwable 
  {
	  OppurtunityDeckStepDef.Validate_filter_drawer_section_with_DB_for(arg1);
  
  }
  

//********************* Sorting *****************************************************************************************
  

	@Then("^validate the sorting functionality for sortby option \"([^\"]*)\" in \"([^\"]*)\"$")
	public void validate_the_sorting_functionality_for_sortby_option_in(String arg1, String arg2) throws Throwable 
	{
		OppurtunityDeckStepDef.validate_the_sorting_functionality_in_deck(arg1,arg2);
	    
	}
	@Then("^user should select \"([^\"]*)\" tab$")
	public void user_should_select_tab(String arg1) {
		OppurtunityDeckStepDef.userShouldSelectTab(arg1);
	}

	@Then("^user should view all the coloumns in Change Opportunities$")
	public void user_should_view_all_the_coloumns_in_Change_Opportunities() {
		OppurtunityDeckStepDef.usershouldviewallcoloumnin("");
	   
	}
}
