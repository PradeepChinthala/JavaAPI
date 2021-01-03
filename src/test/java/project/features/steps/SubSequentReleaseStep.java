package project.features.steps;

import java.io.IOException;

import org.json.simple.parser.ParseException;

import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import net.thucydides.core.annotations.Steps;
import net.thucydides.core.steps.ScenarioSteps;
import projects.steps.definitions.SubSequentReleaseStepDef;

public class SubSequentReleaseStep extends ScenarioSteps {

	private static final long serialVersionUID = 1L;

	@Steps
	SubSequentReleaseStepDef oSubSequentReleaseStepDef;
	//ChangeOpptyStepDef oChangeOpptyStepDef;
	

	@Then("^Set up the Data for ELL Changes for \"([^\"]*)\",\"([^\"]*)\",\"([^\"]*)\",\"([^\"]*)\"$")
	public void set_up_the_Data_for_ELL_Changes_for(String arg1, String arg2, String arg3, String arg4) throws Throwable {
		oSubSequentReleaseStepDef.setUpDataforELLChanges(arg1,arg2,arg3,arg4);
	}
	
	@Given("^Change Oppty Scenario for \"([^\"]*)\" change$")
	public void change_Oppty_Scenario_for_change(String sChange) throws Exception {
		
		//oSubSequentReleaseStepDef.CreateChngSetup(sChange);
	    
	}

	@Given("^\"([^\"]*)\" sets data for \"([^\"]*)\" with \"([^\"]*)\"$")
	public void sets_data_for_with(String User, String changeType, String changeOpportunity) throws Exception {
		oSubSequentReleaseStepDef.userSetsDataForChangeOpportunity(User,changeType,changeOpportunity);
	}

	@When("^User posts change opportunity data with \"([^\"]*)\"$")
	public void user_posts_change_opportunity_data_with(String changeType) throws ParseException, IOException {
		oSubSequentReleaseStepDef.postChangeOpportunityData(changeType);
	}

	@Then("^User verifies data for changed opportunity with \"([^\"]*)\",\"([^\"]*)\" for \"([^\"]*)\"$")
	public void user_verifies_data_for_changed_opportunity_with_for(String changeType, String changeOpportunity, String criteria) throws Exception {
		oSubSequentReleaseStepDef.verifyDataForChangeOpportunity(changeType,changeOpportunity,criteria);
	}
	
}
