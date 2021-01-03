package project.features.steps;

import java.text.ParseException;

import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import net.thucydides.core.annotations.Steps;
import projects.steps.definitions.CPWStepDef;
import projects.steps.definitions.DBValidationsStepDef;
import projects.steps.definitions.PMServicesStepDef;


public class DBValidationsSteps {

	@Steps
	DBValidationsStepDef oDBValidationsStepDef;

	@Given("^User Retreive the ELL data from oracle and compare with MongoDB$")
	public void user_Retreive_the_ELL_data_from_oracle_and_compare_with_MongoDB() throws Throwable {
		oDBValidationsStepDef.EllDataVerificationwithOracleandMongoDB();


	}


	@Given("^User validate the Topic Description from ELL data from Policy Hierarchy$")
	public void user_validate_the_Topic_Description_from_ELL_data_from_Policy_Hierarchy() throws Throwable {
		oDBValidationsStepDef.TopicLongDescVerificationwithEllandPolicyHierarchy();

	}

	@Given("^User validate the ELL data for MidRules with Oppty collection$")
	public void user_validate_the_ELL_data_for_MidRules_with_Oppty_collection() throws Throwable {
		oDBValidationsStepDef.CompareELLDatawithOpptyCollection();	    
	}


	@Given("^User retrieves the RuleRelationship data from Oracle and compare with MongoDB$")
	public void user_retrieves_the_RuleRelationship_data_from_Oracle_and_compare_with_MongoDB() throws Throwable
	{	
		oDBValidationsStepDef.compareRuleRelationshipData();				
	}


	@Given("^User validate the Sort Order between oracle and MongoDB for \"([^\"]*)\"$")
	public void user_validate_the_Sort_Order_between_oracle_and_MongoDB(String sLevel) throws Throwable {

		oDBValidationsStepDef.CompareSortOrderWithOracleandMongoDB(sLevel);
	}
	//============================================== Chaitanya ====================================================================//

	@Given("^user validate the PPS between \"([^\"]*)\" and \"([^\"]*)\" MongoDB for monthly policy release$")
	public void user_validate_the_PPS_between_and_MongoDB_for_monthly_policy_release(String arg1, String arg2) throws Throwable {
		oDBValidationsStepDef.Validate_the_PPS_between_and_MongoDB_for_monthly_policy_release(arg1,arg2);

	}


	@Given("^Ability to update Monthly Policy changes on PM with status having disabled as \"([^\"]*)\", deactivate as \"([^\"]*)\" and Retired as \"([^\"]*)\" with status \"([^\"]*)\"$")
	public void ability_to_update_Monthly_Policy_changes_on_PM_with_status_having_disabled_as_deactivate_as_and_Retired_as_with_status(String sdisabled, String sDeactivate, String sRetired, String sStatus) throws Throwable {
		oDBValidationsStepDef.CompareEllDatatoOpptyCollectionforStatus(sdisabled, sDeactivate, sRetired, sStatus);
	}

	@Given("^User Ability to track Active/Inactive status for  a sub_rule_key with oracle database \"([^\"]*)\" and mongo collection \"([^\"]*)\"$")
	public void user_Ability_to_track_Active_Inactive_status_for_a_sub_rule_key_with_oracle_database_and_mongo_collection(String arg1, String arg2) throws Throwable {

		oDBValidationsStepDef.trackActiveInactiveStatus(arg1,arg2);

	}

	@Given("^CPW Update opportunities for the eLL changes for EllData to Oppty collection for \"([^\"]*)\"$")
	public void cpw_Update_opportunities_for_the_eLL_changes_for_EllData_to_Oppty_collection_for(String arg1) throws Throwable {
		oDBValidationsStepDef.updateOpportunitiesbetweenElltoOpptyCollection(arg1);
	}

	@Given("^Ability to track when new rule version is created$")
	public void ability_to_track_when_new_rule_version_is_created() throws Throwable {
		oDBValidationsStepDef.abilityToTrackNewVersionCreated();
	}

	@Given("^Notify eLL changes \"([^\"]*)\"on Presentation Profile$")
	public void notify_eLL_changes_on_Presentation_Profile(String sChange) throws Throwable {

		oDBValidationsStepDef.notifyEllChangesOnPresenationProfile(sChange);

	}

	@Given("^Clean Data sheet \"([^\"]*)\"$")
	public void clean_Data_sheet(String sPath) throws Throwable {
		oDBValidationsStepDef.CleanDataSheet(sPath);

	}

	@Then("^Validate the RuleRelationship  details in MongoDB \"([^\"]*)\" collection against OracleDB$")
	public void validate_the_RuleRelationship_details_in_MongoDB_collection_against_OracleDB(String MDBCollectionName) throws Throwable 
	{
		oDBValidationsStepDef.validateRuleRelationDetailsinMDB(MDBCollectionName);
	}
	
	@Given("^Validate the Notifications from \"([^\"]*)\"$")
	public void validate_the_Notifications_from(String sWorkbookPath) throws Throwable {
		oDBValidationsStepDef.validateNofications(sWorkbookPath);
	}

	@Given("^Capture  \"([^\"]*)\" details  from Oracle \"([^\"]*)\" for the  \"([^\"]*)\"  for  \"([^\"]*)\"$")
	public void capture_details_from_Oracle_for_the_for(String ValuesToFetch, String DBInstanceName, String Client, String InputValues) throws Throwable
	{
		oDBValidationsStepDef.retrieveRuleRelationDetailsFromOracle(ValuesToFetch,DBInstanceName,Client,InputValues);	

	}
	

	@Given("^User validate the Mongo Monthly collection \"([^\"]*)\" for fields \"([^\"]*)\" oppty Collections as PrimaryKey \"([^\"]*)\"$")
	public void user_validate_the_Mongo_Monthly_collection_for_fields_oppty_Collections_as_PrimaryKey(String arg1, String arg2, String arg3) throws Throwable {
		oDBValidationsStepDef.ValidateMonthlyToOpptyCollections(arg1,arg2,arg3);
	}
	
	@Given("^Validate Switch key for given clientkey \"([^\"]*)\"$")
	public void validate_Switch_key_for_given_clientkey(String arg1) throws Throwable {
		oDBValidationsStepDef.SwitchKey(arg1, "", "");
	    
	}
	
	@Given("^Ability to update and notify when new rule version is created$")
	public void ability_to_update_and_notify_when_new_rule_version_is_created() throws Throwable
	{
		oDBValidationsStepDef.abilityToUpdateAndNotifyNewVersionCreated();
	}
	
	@Given("^Ability to track, update and notify when new Mid rule is created$")
	public void ability_to_track_update_and_notify_when_new_Mid_rule_is_created() throws Throwable 
	{
	oDBValidationsStepDef.abilityToTrackUpdateAndNotifyNewMidRuleisCreated();
	}

	//=================================================================================================
	
	@Given("^user validate the duplicate DP PPS in \"([^\"]*)\" collection for \"([^\"]*)\"$")
	public void user_validate_the_duplicate_DP_PPS_in_collection_for(String arg1, String arg2) throws ParseException 
	{
		oDBValidationsStepDef.verifyTheDuplicateDPandPPSIn(arg1,arg2);
	}

	@When("^User compares \"([^\"]*)\" from Oracle to Mongo$")
	public void user_compares_from_Oracle_to_Mongo(String arg1) {
		oDBValidationsStepDef.userComparesDataFromOracleToMongo(arg1);
	}
	
	@Given("^Test Rules for DP from Oracle to MongoDB$")
	public void test_Rules_for_DP_from_Oracle_to_MongoDB() {
		
		oDBValidationsStepDef.compareDpsFromOracleToMongo();
	 
	}
	
	@Given("^User Retreive the Latest Collection data from oracle and compare with MongoDB for \"([^\"]*)\"$")
	public void user_Retreive_the_Latest_Collection_data_from_oracle_and_compare_with_MongoDB(String dataType) throws Exception {
		
		oDBValidationsStepDef.compareLatestColctnFromOracleToMongo(dataType);
	   
	}
	
	//################################## Duplicate Desc Step ###########################################################
	@Given("^user validate the duplicate \"([^\"]*)\" Desc in \"([^\"]*)\" collection for \"([^\"]*)\"$")
	public void user_validate_the_duplicate_Desc_in_collection_for(String arg1, String arg2, String arg3) throws ParseException 
	{
		oDBValidationsStepDef.verifyTheDuplicateDesc(arg1, arg2, arg3);   
	}
}

