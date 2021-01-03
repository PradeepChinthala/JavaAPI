package project.features.steps;

import java.io.IOException;

import org.json.simple.parser.ParseException;

import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import net.thucydides.core.annotations.Steps;
import projects.steps.definitions.CPWStepDef;
import projects.steps.definitions.PMServicesStepDef;


public class PMServicesSteps {

	@Steps
	PMServicesStepDef oPMServicesStepDef;

	@Given("^User \"([^\"]*)\" logged into \"([^\"]*)\" application with Services$")
	public void user_logged_into_application_with_Services(String sUserName, String sPassword) throws Throwable {

		oPMServicesStepDef.logintoApplication(sUserName,sPassword);
	}

	@Then("^Verify the user first name \"([^\"]*)\" and last name \"([^\"]*)\"$")
	public void verify_the_user_first_name_and_last_name(String sfirstname, String sLastname) throws Throwable {
		oPMServicesStepDef.verifyFirstNameAndLastName(sfirstname, sLastname);

	}

	@Then("^verify the clientTeamData assigned for given user \"([^\"]*)\"$")
	public void verify_the_payershorts_assigned_for_given_user(String sUser) throws Throwable {

		oPMServicesStepDef.verifythePayershortsAssignedforGivenUser(sUser);
	}

	@Then("^verify the clients assigned for given user \"([^\"]*)\"$")
	public void verify_the_clients_assigned_for_given_user(String sUser) throws Throwable {
		oPMServicesStepDef.verifytheClientsassignedforgivenuser(sUser);

	}

	@Then("^get the DP and Savings count for given client \"([^\"]*)\"$")
	public void get_the_DP_and_Savings_count_for_given_client(String sClient) throws Throwable {
		oPMServicesStepDef.gettheDPandSavingsCountforGivenClient(sClient);
	}

	@Then("^verify DP and LOB information for given Payershorts \"([^\"]*)\" and Medical policies \"([^\"]*)\" for client \"([^\"]*)\"$")
	public void verify_DP_and_LOB_information_for_given_Payershorts_and_Medical_policies(String sPayershort, String sMedicalpolicies,String sClient) throws Throwable {
		oPMServicesStepDef.gettheDPandLobinfo(sClient,sPayershort,sMedicalpolicies);
	}

	@Then("^Create the presenatation profile with given request data client \"([^\"]*)\" payershort \"([^\"]*)\" lob \"([^\"]*)\" product \"([^\"]*)\" and prority \"([^\"]*)\" with \"([^\"]*)\"$")
	public void create_the_presenatation_profile_with_given_request_data_client_payershort_lob_product_and_prority_with(String sClient, String sPayershorts, String sLobs, String sProduct, String sPriority, String sUser) throws Throwable {
		oPMServicesStepDef.createPresentationProfilewithGivenRequest(sUser,sClient,sPayershorts,sLobs,sProduct,sPriority);
	}

	@Then("^User get the available opportunities for given \"([^\"]*)\" payershort \"([^\"]*)\" lob \"([^\"]*)\" and topics \"([^\"]*)\"$")
	public void user_get_the_available_opportunities_for_given_payershort_lob_and_topics(String client, String payershort, String lob, String topics) throws Throwable {
		oPMServicesStepDef.getAvailableOppurtunitiesforGiven(client,payershort,lob,topics);

	}

	@Given("^get Summary information for given client \"([^\"]*)\"$")
	public void get_Summary_information_for_given_client(String sClient) throws Throwable {
		oPMServicesStepDef.getSummaryinformationforGivenClient(sClient);

	}

	@Then("^user logout from the presentation manager application$")
	public void user_logout_from_the_presentation_manager_application() throws Throwable {
		oPMServicesStepDef.userLogsOutFromThenApplication();
	}

	@Given("^get all payer shorts and lobs for the client \"([^\"]*)\" for given \"([^\"]*)\"$")
	public void get_all_payer_shorts_and_lobs_for_the_client_for_given(String sClient, String sUser) throws Throwable {
		oPMServicesStepDef.getAllPayersforClientforUser(sClient,sUser);

	}

	@Then("^Assign created Dpkey to the profile$")
	public void assignCreatedDPKeytotheProfile() throws Throwable {
		oPMServicesStepDef.AssignDPtoProfile();

	}

	@Then("^Capture the decision with status \"([^\"]*)\"$")
	public void capture_the_decision_with_status(String sStatus) throws Throwable {
		oPMServicesStepDef.capturetheDecisionforGivenStatus(sStatus);
	}

	@Then("^get any presentation profile having opportunities for client \"([^\"]*)\"$")
	public void get_any_presentation_profile_having_opportunities_for_client(String sClient) throws Throwable {
		oPMServicesStepDef.getPresentationHavingOppurtunities(sClient);

	}

	@Given("^Select the PayerShort and LOB combination having no filter criteria$")
	public void select_the_PayerShort_and_LOB_combination_having_no_filter_criteria() throws Throwable {
		oPMServicesStepDef.getthePayershortandLOBhavingNofilterCriteria();

	}

	@Given("^assign dp to created profile$")
	public void assignDPtoCreatedProfile() throws Throwable {
		oPMServicesStepDef.assignDPtoCreatedProfile();	    
	}

	@Given("^assign multiple dps to created profile$")
	public void assignMultipleDPstoCreatedProfile() throws Throwable {
		oPMServicesStepDef.assignMultipleDPstoCreatedProfile();	    
	}

	@Given("^Delete the presentations for client \"([^\"]*)\"$")
	public void deleteThePresenationsforGivenClient(String sClient) throws Throwable {
		oPMServicesStepDef.deletePresentationsforClient(sClient);	    
	}

	@Then("^Finalize the decision based on \"([^\"]*)\"$")
	public void finalize_the_decision_based_on(String sClient) throws Throwable {
		// Write code here that turns the phrase above into concrete actions
		oPMServicesStepDef.logintoApplicationForFinalize(sClient);
	}

	@Then("^Capture the decision \"([^\"]*)\" with one or more status for client \"([^\"]*)\"$")
	public void capture_the_decision_with_one_or_more_status(String sStatus,String sClient) throws Throwable {
		// Write code here that turns the phrase above into concrete actions
		oPMServicesStepDef.capturetheDecisionWithOneorMoreStatus(sStatus,sClient);
	}
	@Then("^verify Approve with MOD DB and Services count for \"([^\"]*)\"$")
	public void verify_Approve_with_MOD_DB_and_Services_count_for(String arg1) throws Throwable {
		oPMServicesStepDef.verify_Approve_with_MOD_DB_and_Services_count_for(arg1);
	}

	@Given("^verify Approve with MOD DB and Services data for \"([^\"]*)\"$")
	public void verify_Approve_with_MOD_DB_and_Services_data_for(String arg1) throws Throwable {
		oPMServicesStepDef.verify_Approve_with_MOD_DB_and_Services_data_for(arg1);
	}

	@Then("^The Opportunity should not be available in the \"([^\"]*)\" page for  \"([^\"]*)\"$")
	public void the_Opportunity_should_not_be_available_in_the_page_for(String PageName, String Client) throws Throwable
	{
		oPMServicesStepDef.validateOppInRWOPage(PageName,Client);
	}
	
	@Given("^The \"([^\"]*)\" should be updated as per the oppurtunity savings value in the \"([^\"]*)\" for \"([^\"]*)\"$")
	public void the_should_be_updated_as_per_the_oppurtunity_savings_value_in_the_for(String SavingsType, String Section, String Client) throws Throwable {
		oPMServicesStepDef.validateSavings(SavingsType,Section,Client);
	}
	
	@Given("^new decision service for inserting data of CDM decision into the decision collection$")
   	public void new_decision_service_for_inserting_data_of_CDM_decision_into_the_decision_collection()throws Throwable 
   	{
		oPMServicesStepDef.service_For_CDM_Decision_Data_Insertion_Into_Decision_Collection();
   	}

   	@Then("^verify the data persistance in mongo collection$")
   	public void verify_the_data_persistance_in_mongo_collection() 
   	{
   		oPMServicesStepDef.verifyDataPersistedInMongoCollection();
   	}
	
   	@Then("^Create the change presenatation profile with given request data client \"([^\"]*)\" payershort \"([^\"]*)\" lob \"([^\"]*)\" product \"([^\"]*)\" and prority \"([^\"]*)\" with \"([^\"]*)\"$")
   	public void create_the_change_presenatation_profile_with_given_request_data_client_payershort_lob_product_and_prority_with(String sClient, String sPayershorts, String sLobs, String sProduct, String sPriority, String sUser) throws ParseException, IOException {
   		oPMServicesStepDef.createChangePresentationProfile(sUser,sClient,sPayershorts,sLobs,sProduct,sPriority);
   	}
   	
   	@Then("^assign change dps to created profile$")
   	public void assign_change_dps_to_created_profile() throws ParseException, IOException {
   		oPMServicesStepDef.assignChangeDPsToProfile();
   	}

   	@Then("^user captures decision on change DP's as \"([^\"]*)\" and select finalize as \"([^\"]*)\"$")
   	public void user_captures_decision_on_change_DP_s_as_and_select_finalize_as(String arg1, String arg2) throws ParseException, IOException {
   		oPMServicesStepDef.userCaptureAndFinalizeDecisionOnChangeDP(arg1,arg2);
   	}
}
