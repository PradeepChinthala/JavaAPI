package project.features.steps;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import org.json.simple.parser.ParseException;

import cucumber.api.PendingException;
import cucumber.api.java.en.And;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import net.serenitybdd.core.Serenity;
import net.thucydides.core.annotations.Steps;
import net.thucydides.core.steps.ScenarioSteps;
import projects.steps.definitions.FilterDrawerStepDef;

public class FilterDrawerStep extends ScenarioSteps {

	private static final long serialVersionUID = 1L;

	@Steps
	FilterDrawerStepDef oFilterDrawerStepDef;


	@When("^user selects \"([^\"]*)\" from Client drop down list$")
	public void user_selects_from_Client_drop_down_list(String arg1) throws Throwable {
		
		oFilterDrawerStepDef.user_selects_given_value_from_Client_drop_down_list(arg1);
	}

	@Then("^user selects \"([^\"]*)\" from Payer Shorts$")
	public void user_selects_from_Payer_Shorts(String arg1) throws Throwable {
		oFilterDrawerStepDef.user_selects_given_value_from_Payer_Shorts(arg1);
	}

	@Then("^user selects \"([^\"]*)\" from LOB$")
	public void user_selects_from_LOB(String arg1) throws Throwable {
		oFilterDrawerStepDef.user_selects_given_value_from_LOB(arg1);
	}

	@Then("^user selects \"([^\"]*)\" from Product$")
	public void user_selects_from_Product(String arg1) throws Throwable {
		oFilterDrawerStepDef.user_selects_given_value_from_Product(arg1);
	}

	@Then("^user filters by clicking on Apply for Payer Shorts$")
	public void user_filters_by_clicking_on_Apply_for_Payer_Shorts() throws Throwable {
		oFilterDrawerStepDef.user_filters_by_clicking_on_Apply_for_Payer_Shorts();
	}

	@Then("^user selects \"([^\"]*)\" from Medical Policy/Topic$")
	public void user_selects_from_Medical_Policy_Topic(String arg1) throws Throwable {
		oFilterDrawerStepDef.user_selects_given_value_from_Medical_Policy_Topic(arg1);
	}

	@Then("^user filters by clicking on Apply for Medical Policy/Topic$")
	public void user_filters_by_clicking_on_Apply_for_Medical_Policy_Topic() throws Throwable {
		oFilterDrawerStepDef.user_filters_by_clicking_on_Apply_for_Medical_Policy_Topic();
	}

	@Then("^user should view \"([^\"]*)\" in Client drop down list$")
	public void user_should_view_in_Client_drop_down_list(String arg1) throws Throwable {
		oFilterDrawerStepDef.user_should_view_given_value_in_Client_drop_down_list(arg1);
	}

	@Then("^user should view \"([^\"]*)\" in Payer Shorts$")
	public void user_should_view_in_Payer_Shorts(String arg1) throws Throwable {
		oFilterDrawerStepDef.user_should_view_given_value_in_Payer_Shorts(arg1);

	}

	@Then("^user should view \"([^\"]*)\" in LOB$")
	public void user_should_view_in_LOB(String arg1) throws Throwable {
		oFilterDrawerStepDef.user_should_view_given_value_in_LOB(arg1);
	}

	@Then("^user should view \"([^\"]*)\" in Product$")
	public void user_should_view_in_Product(String arg1) throws Throwable {
		oFilterDrawerStepDef.user_should_view_given_value_in_Product(arg1);
	}

	@Then("^user should view \"([^\"]*)\" in Medical Policy/Topic$")
	public void user_should_view_in_Medical_Policy_Topic(String arg1) throws Throwable {
		oFilterDrawerStepDef.user_should_view_given_value_in_Medical_Policy_Topic(arg1);

	}

	@Then("^user should view Reset button for Payer Shorts$")
	public void user_should_view_Reset_button_for_Payer_Shorts() throws Throwable {
		oFilterDrawerStepDef.user_should_view_Reset_button_for_Payer_Shorts();

	}

	@Then("^user should view Apply button for Payer Shorts$")
	public void user_should_view_Apply_button_for_Payer_Shorts() throws Throwable {
		oFilterDrawerStepDef.user_should_view_Apply_button_for_Payer_Shorts();

	}

	@Then("^user should view Reset button for Medical Policy/Topics$")
	public void user_should_view_Reset_button_for_Medical_Policy_Topics() throws Throwable {
		oFilterDrawerStepDef.user_should_view_Reset_button_for_Medical_Policy_Topics();

	}

	@Then("^user should view Apply button for Medical Policy/Topics$")
	public void user_should_view_Apply_button_for_Medical_Policy_Topics() throws Throwable {
		oFilterDrawerStepDef.user_should_view_Apply_button_for_Medical_Policy_Topics();

	}

	@Then("^user should view \"([^\"]*)\" in Available Oppurtunity panel$")
	public void user_should_view_in_Available_Oppurtunity_panel(String arg1) throws Throwable {
		// Write code here that turns the phrase above into concrete actions
		throw new PendingException();
	}

	@Then("^user should view \"([^\"]*)\" in Presentation Panel$")
	public void user_should_view_in_Presentation_Panel(String arg1) throws Throwable {
		// Write code here that turns the phrase above into concrete actions
		throw new PendingException();
	}

	@Then("^PM should display Clients assigned to the user logged in$")
	public void pm_should_display_Clients_assigned_to_the_user_logged_in() throws Throwable {
		oFilterDrawerStepDef.pm_should_display_Clients_assigned_to_the_user_logged_in();
	}

	@Then("^user should view user specific Payer Shorts in Payer Shorts for each Client$")
	public void user_should_view_user_specific_Payer_Shorts_in_Payer_Shorts_for_each_Client() throws Throwable {
		// Write code here that turns the phrase above into concrete actions
		throw new PendingException();
	}

	@Then("^user should view user specific LOB in LOB_for each Client$")
	public void user_should_view_user_specific_LOB_in_LOB_for_each_Client() throws Throwable {
		// Write code here that turns the phrase above into concrete actions
		throw new PendingException();
	}

	@Then("^user should view user specific Product in Product_for each Client$")
	public void user_should_view_user_specific_Product_in_Product_for_each_Client() throws Throwable {
		// Write code here that turns the phrase above into concrete actions
		throw new PendingException();
	}
		

@Given("^User selects All PayerShorts$")
public void user_selects_All_PayerShorts() throws Throwable {
	oFilterDrawerStepDef.selectAllPayerShorts();
}

@Given("^User selects All  LOBs$")
public void user_selects_All_LOBs() throws Throwable {
	oFilterDrawerStepDef.selectAllLOBs();
}

@Given("^User selects All Products$")
public void user_selects_All_Products() throws Throwable {
	oFilterDrawerStepDef.selectAllProducts();
}

@When("^User selects client \"([^\"]*)\" from Client drop down list which has no Opportunities$")
public void user_selects_client_from_Client_drop_down_list_which_has_no_Opportunities(String sClientName) throws Throwable
{
	oFilterDrawerStepDef.selectClientWithoutOpportunities(sClientName);
}

@Then("^An empty filter drawer with \"([^\"]*)\" message  should be displayed$")
public void an_empty_filter_drawer_with_message_should_be_displayed(String arg1) throws Throwable 
{
	oFilterDrawerStepDef.validateMedicalFilterDrawerMessage();   
}

@Then("^User selects Medical Policy/Topic according to \"([^\"]*)\"$")
public void user_selects_Medical_Policy_Topic_according_to(String sScenarioType) throws Throwable 
{
	oFilterDrawerStepDef.selectMedicalPoliciesAsPerCriteria(sScenarioType);	
   }

@Given("^User clicks on \"([^\"]*)\" button to filter$")
public void user_clicks_on_button_to_filter(String sButtonName) throws Throwable {
	
	oFilterDrawerStepDef.userAppliesFilter(sButtonName);
}

@When("^User selects  multiple Payershorts \"([^\"]*)\" from Payershorts FilterSection$")
public void user_selects_multiple_Payershorts_from_Payershorts_FilterSection(String sPayershorts) throws Throwable 
{
	
	oFilterDrawerStepDef.selectMultiplePayershorts(sPayershorts);
}

@When("^User selects muliple LOBs \"([^\"]*)\" from LOB FilterSection$")
public void user_selects_muliple_LOBs_from_LOB_FilterSection(String sLOBs) throws Throwable
{
	
	oFilterDrawerStepDef.selectMultipleLOBs(sLOBs);
}

	@Then("^User click on \"([^\"]*)\" link$")
public void ClickOnLinkwithText(String sLink) throws Throwable {
		oFilterDrawerStepDef.ClickOnLink(sLink);
	}
	
	@Then("^Select \"([^\"]*)\" from Show filter dropdown$")
public void SelectValuefromShowFilter(String sValue) throws Throwable {
		oFilterDrawerStepDef.SelectValuefromShowFilter(sValue);
	}
		
@When("^User update All PayerShorts and LOBs checkboxes$")
	public void userUpdateAllPayerShortsandLOBsCheckboxes() throws Throwable
	{
		oFilterDrawerStepDef.userSelectPayerandLOBFilters("SELECT ALL CHECKBOX","","Update");
	}
	
	@When("^User logs out from the application$")
	public void userLogsOutfromtheApplication() throws Throwable
	{
		oFilterDrawerStepDef.closetheBrowser();
	}
	
	@When("^User must view the saved filters$")
	public void userMustViewtheSavedFilters() throws Throwable
	{
		oFilterDrawerStepDef.userShouldViewPayerandLOBSavedFilters();
	}

@Given("^User selects single Payershort \"([^\"]*)\" from Payershorts FilterSection$")
public void user_selects_single_Payershort_from_Payershorts_FilterSection(String arg1) throws Throwable {
	oFilterDrawerStepDef.selectSinglePayershort(arg1);
}

@Given("^Post the user access \"([^\"]*)\" and fetch client names$")
public void post_the_user_access_and_fetch_client_names(String serviceURL) throws Throwable {
	oFilterDrawerStepDef.PostUserDataAndFetchClientsList(serviceURL);
    
}

@Then("^verify the clients are displayed according to logged in User$")
public void verify_the_clients_are_displayed_according_to_logged_in_User() throws Throwable {
	oFilterDrawerStepDef.ValidateClientsAsPerUser(); 
}

@Then("^validate scroll bars in Filter panel$")
public void validate_scroll_bars_in_Filter_panel() throws Throwable {
	oFilterDrawerStepDef.verifyScrollBarsofPayerShortAndLOB();
    
}

@Given("^user should view client list dropdown$")
public void user_should_view_client_list_dropdown() throws Throwable {
	oFilterDrawerStepDef.user_should_view_Client_dropdown();
}

@Then("^verify clients list are sorted in alphabetical order$")
public void verify_clients_list_are_sorted_in_alphabetical_order() throws Throwable {
	oFilterDrawerStepDef.verifySortingOrderOfClients();
}

@Given("^User Select Any Medical Policy$")
public void user_Select_Any_Medical_Policy() throws Throwable {
	oFilterDrawerStepDef.getSelectedMedicalPolicy("FLITER CHECKBOX BY NUMBER","1","CHECK");
}

@Given("^User view the saved filters for Medical Policy$")
public void user_view_the_saved_filters_for_Medical_Policy() throws Throwable {
	oFilterDrawerStepDef.userShouldViewMedicalPolicySavedFilters();
}
	@When("^User \"([^\"]*)\" PayerShorts and LOB Filters$")
	public void userSelectPayerandLOBFilters(String sOperation) throws Throwable
	{
		oFilterDrawerStepDef.userSelectPayerandLOBFilters("SELECT ALL CHECKBOX","",sOperation);
	
	}
	
	@When("^User Select Any PayerShorts and LOB Filters$")
	public void userSelectAnyPayerandLOBFilters() throws Throwable
	{
		oFilterDrawerStepDef.getSelectedPayerandLOB("FLITER CHECKBOX BY NUMBER","1","CHECK");
	}
	//=======================================================================================>

	@Given("^user \"([^\"]*)\" \"([^\"]*)\" under Payershort/LOB section$")
	public void user_under_Payershort_LOB_section(String arg1, String arg2) throws Throwable {
		oFilterDrawerStepDef.user_under_Payershort_LOB_section(arg1, arg2);
	}

	@And("^user select \"([^\"]*)\" value \"([^\"]*)\" for \"([^\"]*)\" operation$")
	public void user_select_value_for_operation(String arg1, String arg2, String arg3) throws Throwable {
		oFilterDrawerStepDef.user_select_value_for_operation(arg1, arg2, arg3);
	}
	
	@Given("^user assigns presentation \"([^\"]*)\" for respective DP under \"([^\"]*)\",\"([^\"]*)\" level$")
	public void user_assigns_presentation_for_respective_DP_under_level(String arg1, String arg2, String arg3) throws Throwable {
		oFilterDrawerStepDef.user_assigns_presentation_for_respective_DP_under_level(arg1, arg2, arg3);
	}
	@Given("^user verify assigned presentation \"([^\"]*)\" profile for respective DP \"([^\"]*)\" under Medical policy \"([^\"]*)\" Topic \"([^\"]*)\"$")
	public void user_verify_assigned_presentation_profile_for_respective_DP_under_Medical_policy_Topic(String arg1, String arg2, String arg3, String arg4) throws Throwable {
		oFilterDrawerStepDef.user_verify_assigned_presentation_profile_for_respective_DP_under_Medical_policy_Topic(arg1, arg2, arg3, arg4);
	}
	
	@Given("^filter \"([^\"]*)\" payer short and \"([^\"]*)\" retrieved through Service$")
	public void filter_payer_short_and_retrieved_through_Service(String arg1, String arg2) throws Throwable {
		oFilterDrawerStepDef.SelectPayerShortAndMedicalPolicyFetchedFromService(arg1, arg2);
	}
	
	@Then("^validate RawSavings in DB and UI for DP captured through Service corresponding to \"([^\"]*)\" payer short$")
	public void validate_RawSavings_in_DB_and_UI_for_DP_captured_through_Service_corresponding_to_payer_short(String arg1) throws Throwable {
		oFilterDrawerStepDef.verifyRawSavingsInDBAndUIForDPCapturedThruService(arg1);
	}
	
	@Given("^click on \"([^\"]*)\" in filter drawer section$")
	public void click_on_in_filter_drawer_section(String arg1) throws Throwable {
		oFilterDrawerStepDef.clickOnBtn(arg1);
	}
	
	@Then("^validate RawSavings in DB and UI for MedicalPolicy captured through Service corresponding to \"([^\"]*)\" payer short$")
	public void validate_RawSavings_in_DB_and_UI_for_MedicalPolicy_captured_through_Service_corresponding_to_payer_short(String arg1) throws Throwable {
		oFilterDrawerStepDef.verifyRawSavingsInDBAndUIForMPCapturedThruService();
	}
	
	@Then("^validate Payershort and LOB of \"([^\"]*)\" in application acccording to toad$")
	public void validate_Payershort_and_LOB_of_in_application_acccording_to_toad(String arg1) throws Throwable {
		oFilterDrawerStepDef.verifyPayerShortAndLOBWRTDB(arg1);
	}
	
	@When("^user selects \"([^\"]*)\" Claimtypes in filtersection$")
	public void user_selects_Claimtypes_in_filtersection(String arg1) throws Throwable {
		oFilterDrawerStepDef.user_selects_Claimtypes_in_filtersection(arg1);
	    
	}
	
	@Given("^user verify RawSavings updated for medical policy$")
	public void user_verify_RawSavings_for() throws Throwable {
		oFilterDrawerStepDef.verifyRawSavingsForCapturedMPOrTopic();
	}

	@When("^User Select \"([^\"]*)\" as \"([^\"]*)\" Under \"([^\"]*)\"$")
	public void user_Select_as_Under(String arg1, String arg2, String arg3) throws IOException, ParseException {
		oFilterDrawerStepDef.userSelectsPPSUnder(arg1,arg2,arg3);
	}

	@Then("^system should display a msg \"([^\"]*)\"$")
	public void system_should_display_a_msg(String arg1) {
		oFilterDrawerStepDef.system_should_display_a_msg(arg1);
	}
	
	@Given("^validate Decision \"([^\"]*)\" count with Mongo$")
	public void validate_Decision_count_with_Mongo(String arg1) {
		oFilterDrawerStepDef.validate_Decision_count_with_Mongo(arg1);
	}
}
