package project.features.steps;

import cucumber.api.PendingException;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import net.thucydides.core.annotations.Steps;
import projects.steps.definitions.*;


public class LoginSteps {

	@Steps
	LoginStepDef oLoginStepDef;
	CPWStepDef oCPWStepDef;
	
	@Given("^User is logged into the PM application$")
	public void user_is_logged_into_the_PM_application() throws Throwable {
		oLoginStepDef.user_is_logged_into_the_PM_application();
	}
	@When("^user does Re-login into the PM application$")
	public void user_does_Re_login_into_the_PM_application() throws Throwable {
		oLoginStepDef.user_does_Re_login_into_the_PM_application();
	}
	
	@Given("^CPW User logs in to Presenation Manager Application for the first time$")
	public void cpw_User_logs_in_to_Presenation_Manager_Application_for_the_first_time() throws Throwable {
	    // Write code here that turns the phrase above into concrete actions
	    throw new PendingException();
	}
	
	@Then("^user logs out of the CPW application$")
	public void user_logs_out_of_the_CPW_application() throws Throwable {
		oLoginStepDef.user_logs_out_of_the_CPW_application();
	
	}
	
	@Then("^Login screen should be displayed on CPW browser$")
	public void screen_should_be_displayed_on_CPW_browser() throws Throwable {
		oLoginStepDef.Login_screen_should_be_displayed_on_CPW_browser();
		
	}

	@Then("^Presentation Manager Applications should be logged off$")
	public void presentation_Manager_Applications_should_be_logged_off() throws Throwable {
		oLoginStepDef.presentation_Manager_Applications_should_be_logged_off();
	}
	
	@Then("^Login screen should be displayed on PM browser$")
	public void login_screen_should_be_displayed_on_PM_browser() throws Throwable {
		oLoginStepDef.Login_screen_should_be_displayed_on_PM_browser();
	}
	

	/*@Given("^The User \"([^\"]*)\" is logged into the PM application$")
	public void the_User_is_logged_into_the_PM_application(String sUserName) throws Throwable
	{
		oLoginStepDef.user_is_logged_into_the_PM_application(sUserName);
	}*/

	@Given("^User \"([^\"]*)\" logged into \"([^\"]*)\" application$")
	public void user_is_logged_into_the_CPD_PM_application_with_UserName(String sUserName,String sApp) throws Throwable {
		oLoginStepDef.userLogsinCPDPMApplication(sUserName,sApp);
	}
	
	@Given("^CPMorCMD launches CPD Application$")
	public void cpmorcmd_launches_CPD_Application() throws Throwable {
		oLoginStepDef.validateCPDLoginPage();
	}

	@Then("^CPD Login page should be displayed with Username,Password,Application Dropdown,Login button$")
	public void cpd_Login_page_should_be_displayed_with_Username_Password_Application_Dropdown_Login_button() throws Throwable
	{
		oLoginStepDef.validateLoginPageFields();
	}

	@Then("^User should be able to enter \"([^\"]*)\" and Password$")
	public void user_should_be_able_to_enter_and_Password(String sUserName) throws Throwable {
		oLoginStepDef.validateCredentialsEntry(sUserName);
	}

	@When("^User selects Application values \"([^\"]*)\" in the Application dropdown then related names should be displayed below the Dropdown$")
	public void user_selects_Application_values_in_the_Application_dropdown_then_related_names_should_be_displayed_below_the_Dropdown(String sAppName) throws Throwable {
		oLoginStepDef.validateApplicationDropdownValues(sAppName);
	}

	@When("^User selects Application name \"([^\"]*)\"  from the Application dropdown$")
	public void user_selects_Application_name_from_the_Application_dropdown(String sAppName) throws Throwable {
		oLoginStepDef.selectApplication(sAppName);		
	}
	
	@Given("^User clicks on \"([^\"]*)\" button$")
	public void user_clicks_on_button(String sPlaceholderArg) throws Throwable {
		oLoginStepDef.clickLogin(sPlaceholderArg);
	}
	
	/* @Then("^The User should view the CPW HomePage$")
	 public void the_User_should_view_the_CPW_HomePage() throws Throwable
	 {
	     oCPWStepDef.validateHomePage();
	 }  */
	
	@Given("^User is logged into the CPD PM application with invalid \"([^\"]*)\" or \"([^\"]*)\"$")
	public void user_is_logged_into_the_CPD_PM_application_with_invalid_or(String sUsrname, String sPwd) throws Throwable {
		oLoginStepDef.userLogsinCPDPMApplication_invalidCredentials(sUsrname, sPwd);
	}

	@Then("^verify message for invalid credentials$")
	public void verify_message_for_invalid_credentials() throws Throwable {
		oLoginStepDef.validateInvalidCredentials();
	}
	
	

	
	
}
