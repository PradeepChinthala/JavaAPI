package project.features.steps;

import java.util.concurrent.TimeUnit;
import cucumber.api.PendingException;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import net.thucydides.core.annotations.Steps;
import net.thucydides.core.steps.ScenarioSteps;
import projects.steps.definitions.AddPresentationProfileDef;

public class AddPresentationProfileStep extends ScenarioSteps{

	private static final long serialVersionUID = 5918264397562761350L;
	
	@Steps
	AddPresentationProfileDef oAddPresentationProfileDef;

	@Then("^user clicks on \\+ sign on the Header section from right top corner$")
	public void user_clicks_on_sign_on_the_Header_section_from_right_top_corner() throws Throwable {
		oAddPresentationProfileDef.user_clicks_on_sign_on_the_Header_section_from_right_top_corner();
	}

	@Then("^cancel button should be enabled$")
	public void only_cancel_button_should_be_enabled() throws Throwable {
		oAddPresentationProfileDef.cancel_button_should_be_enabled();
	}

	@Then("^Ok buton should be disabled$")
	public void ok_button_should_be_disabled() throws Throwable {
		oAddPresentationProfileDef.ok_button_should_be_disabled();
	}

	@Then("^OK button should be enabled$")
	public void ok_button_should_be_enabled() throws Throwable {
		oAddPresentationProfileDef.ok_button_should_be_enabled();
	}
	
	@When("^user enters atleast the \"([^\"]*)\" fields$")
	public void user_enters_atleast_the_fields(String arg1) throws Throwable {
		oAddPresentationProfileDef.user_enters_atleast_the_fields(arg1);
	}

	@When("^user clicks on OK button $")
	public void user_clicks_on_OK_button() throws Throwable {
		oAddPresentationProfileDef.user_clicks_on_OK_button();
	}
	@Then("^Success \"([^\"]*)\" should be displayed on the screen$")
	public void success_should_be_displayed_on_the_screen(String arg1) throws Throwable {
		oAddPresentationProfileDef.success_should_be_displayed_on_the_screen(arg1);
	}

	@Then("^User clicks ok on presentation profile success message$")
	public void user_clicks_ok_on_presentation_profile_success_message() throws Throwable {
		oAddPresentationProfileDef.user_clicks_ok_on_presentation_profile_success_message();
	}

	@Then("^the system should save the details entered $")
	public void the_system_should_save_the_details_entered() throws Throwable {
		oAddPresentationProfileDef.the_system_should_save_the_details_entered();
	}

	@Then("^create the presentation in the system$")
	public void create_the_presentation_in_the_system() throws Throwable {

	}
	
	@Then("^user enters atleast the ALL fields$")
	public void user_enters_atleast_the_ALL_fields() throws Throwable {

	}

	@When("^user clicks on Cancel button$")
	public void user_clicks_on_Cancel_button() throws Throwable {
		oAddPresentationProfileDef.user_clicks_on_Cancel_button();
	}

	@Then("^the system should collapse the Presentation section$")
	public void the_system_should_collapse_the_Presentation_section() throws Throwable {
		oAddPresentationProfileDef.the_system_should_collapse_the_Presentation_section();
	}

	@Then("^the system should NOT collapse the Presentation section$")
	public void the_system_should_NOT_collapse_the_Presentation_section() throws Throwable {
		oAddPresentationProfileDef.the_system_should_NOT_collapse_the_Presentation_section();
	}
	
	@Then("^user enters any details in any <fields>$")
	public void user_enters_any_details_in_any_fields() throws Throwable {

	}

	@Then("^system should alert the user, \"([^\"]*)\" with Yes and No Buttons\\.$")
	public void system_should_alert_the_user_with_Yes_and_No_Buttons(String arg1) throws Throwable {
		oAddPresentationProfileDef.system_should_alert_the_user_with_Yes_and_No_Buttons();
	}

	@Then("^system should collapse the presentation section and refresh the page\\.$")
	public void system_should_collapse_the_presentation_section_and_refresh_the_page() throws Throwable {

	}

	@Then("^user should stay on same page without refreshing the data \\.$")
	public void user_should_stay_on_same_page_without_refreshing_the_data() throws Throwable {
	}

	@When("^user clicks on \"([^\"]*)\" on Confirmation pop up$")
	public void user_clicks_on_on_Confirmation_pop_up(String arg1) throws Throwable {
		oAddPresentationProfileDef.user_clicks_on_on_Confirmation_pop_up(arg1);
	}

}
