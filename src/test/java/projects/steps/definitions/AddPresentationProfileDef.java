package projects.steps.definitions;

import org.junit.Assert;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;

import net.thucydides.core.annotations.Step;
import net.thucydides.core.annotations.Steps;
import net.thucydides.core.annotations.findby.By;
import net.thucydides.core.steps.ScenarioSteps;
import project.pageobjects.*;
import project.utilities.SeleniumUtils;


public class AddPresentationProfileDef extends ScenarioSteps {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	AddPresentationProfile onAddPresentationProfile;

	@Step
	public void user_clicks_on_sign_on_the_Header_section_from_right_top_corner() {
		Assert.assertTrue(onAddPresentationProfile.user_clicks_on_sign_on_the_Header_section_from_right_top_corner());
	}

	@Step
	public void cancel_button_should_be_enabled() {
		Assert.assertTrue(onAddPresentationProfile.cancel_button_should_be_enabled());
	}

	public void ok_button_should_be_disabled() {
		Assert.assertTrue(onAddPresentationProfile.ok_button_should_be_disabled());
	}

	@Step
	public void user_enters_atleast_the_fields(String arg1) {

		if (arg1.equals("ALL")) {
			onAddPresentationProfile.populate_all_fields_on__presentation_profile();
		} else if (arg1.equals("Presentation Name")) {
			onAddPresentationProfile.populate_presentation_name_to_add_presentation_profile();
		}

	}

	public void ok_button_should_be_enabled() {
		Assert.assertTrue(onAddPresentationProfile.ok_button_should_be_enabled());
	}

	public void user_clicks_on_OK_button() {
		Assert.assertTrue(onAddPresentationProfile.user_clicks_on_OK_button());
	}

	public void the_system_should_save_the_details_entered() {
		Assert.assertTrue(onAddPresentationProfile.validate_presentation_profile_created_in_previous_step());
	}

	public void success_should_be_displayed_on_the_screen(String arg1) {
		 Assert.assertTrue(onAddPresentationProfile.user_validates_presentation_profile_created_success_message(arg1));
	}

	public void user_clicks_ok_on_presentation_profile_success_message() {
		Assert.assertTrue(onAddPresentationProfile.user_clicks_ok_on_presentation_profile_success_message());
	}

	public void user_clicks_on_Cancel_button() {
		Assert.assertTrue(onAddPresentationProfile.user_clicks_on_Cancel_button());
	}

	public void the_system_should_collapse_the_Presentation_section() {
		Assert.assertTrue(onAddPresentationProfile.verify_Presentation_section_is_collapsed_or_expanded().equals("collapsed"));
	}
	public void the_system_should_NOT_collapse_the_Presentation_section() {
		Assert.assertTrue(onAddPresentationProfile.verify_Presentation_section_is_collapsed_or_expanded().equals("expanded"));
	}
	public void system_should_alert_the_user_with_Yes_and_No_Buttons() {
		Assert.assertTrue(onAddPresentationProfile.system_should_alert_the_user_with_Yes_and_No_Buttons());
	}

	public void user_clicks_on_on_Confirmation_pop_up(String arg1) {
		Assert.assertTrue(onAddPresentationProfile.user_clicks_on_on_Confirmation_pop_up(arg1));
		
	}

}
