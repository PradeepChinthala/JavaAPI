package projects.steps.definitions;
import org.apache.commons.lang3.StringUtils;
import org.bson.BsonDocument;
import org.bson.conversions.Bson;
import org.junit.Assert;

import com.mongodb.client.model.Filters;

import net.serenitybdd.core.Serenity;
import net.thucydides.core.annotations.Step;
import net.thucydides.core.steps.ScenarioSteps;
import project.pageobjects.CPWPage;
import project.pageobjects.FilterDrawer;
import project.pageobjects.LoginPage;
import project.pageobjects.OppurtunityDeck;
import project.utilities.AppUtils;
import project.utilities.GenericUtils;
import project.utilities.MongoDBUtils;
import project.utilities.SeleniumUtils;
import project.variables.ProjectVariables;

public class LoginStepDef extends ScenarioSteps {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	LoginPage onLoginPage;
	FilterDrawer onFilterDrawer;
	ProjectVariables projectVariables;
	AppUtils refAppUtils;
	SeleniumUtils  objSeleniumUtils;
	CPWPage oCPWPage;
	GenericUtils oGenericUtils;
	OppurtunityDeck oOppurtunityDeck;

	public void user_is_logged_into_the_PM_application() throws Exception {
		onLoginPage.open();
		objSeleniumUtils.waitForPageLoad();
		onLoginPage.enter_User_Name(projectVariables.USER_NAME);
		onLoginPage.enter_Password(GenericUtils.decode(projectVariables.PASSWORD));
		onLoginPage.click_Login_button();
		objSeleniumUtils.waitForPageLoad();
		onLoginPage.click_On_PresentationManager_Link();
		objSeleniumUtils.waitForPageLoad();
		objSeleniumUtils.switchHandleToPMBrowser();

	}
	
	@Step
	public void user_does_Re_login_into_the_PM_application() throws InterruptedException {

		onLoginPage.click_On_PresentationManager_Link();
		objSeleniumUtils.waitForPageLoad();
		objSeleniumUtils.switchHandleToPMBrowser();
	}
	
	@Step
	public void user_logs_out_of_the_CPW_application() 
	{
		/*objSeleniumUtils.switchHandleToCPWBrowser();
		Assert.assertTrue(onLoginPage.user_clicks_on_Logout());*/
	}

	@Step
	public void Login_screen_should_be_displayed_on_CPW_browser() 
	{
		Assert.assertTrue(onLoginPage.Login_screen_should_be_displayed_on_CPW_browser());
		

	}

	@Step
	public void presentation_Manager_Applications_should_be_logged_off() throws Throwable {
		objSeleniumUtils.switchHandleToPMBrowser();
		Assert.assertTrue(onFilterDrawer.user_selects_given_value_from_Client_drop_down_list("random"));
		waitABit(projectVariables.TImeout_5_Seconds);
		Assert.assertTrue(onFilterDrawer.user_verifies_sessionTimeout_pop_and_clicks_ok());
//		waitForPageLoad();
	}

	@Step
	public void Login_screen_should_be_displayed_on_PM_browser() {
		Assert.assertTrue(onLoginPage.Login_screen_should_be_displayed_on_CPW_browser());		
	}
	
	/*@Step
	public void user_is_logged_into_the_PM_application(String sUserName) throws Exception 
	{	
		onLoginPage.open();
		
		objSeleniumUtils.waitForPageLoad();
		onLoginPage.enter_User_Name(sUserName);
		onLoginPage.enter_Password(GenericUtils.decode(projectVariables.PASSWORD));
		onLoginPage.click_Login_button();
		objSeleniumUtils.waitForPageLoad();				
		onLoginPage.click_On_PresentationManager_Link();	
		waitFor(ProjectVariables.TImeout_10_Seconds);
		objSeleniumUtils.waitForPageLoad();		
		objSeleniumUtils.switchHandleToPMBrowser();		
	}*/

	@Step
	public void userLogsinCPDPMApplication(String sUserName, String sApplication) 

	{	
      /*  long sPayerID=1318;
		
		String sClaimType="A";
		
		Bson MatchFilter = new BsonDocument();
		
		MatchFilter = Filters.and(Filters.eq("_id.payerKey", sPayerID),Filters.in("_id.insuranceKey", 7l),Filters.in("_id.claimType", sClaimType),Filters.eq("decStatusDesc", "Reject"));
		
		long ival=MongoDBUtils.retrieveAllDocuments("cdm", "latestDecision", MatchFilter);
		
		System.out.println(ival);*/
		
		
		Serenity.setSessionVariable("user").to(sUserName);		
		onLoginPage.launchCPDApplication();			
		System.out.println("CPD application is launched successfully.............");
		try {
			
		if(sUserName.isEmpty())
		{	
		waitFor(ProjectVariables.TImeout_2_Seconds);
		onLoginPage.enterUserName(projectVariables.USER_NAME);		
		onLoginPage.enterPassword(GenericUtils.decode(projectVariables.PASSWORD));		
		onLoginPage.selectApplicationFromDrowpdown("PM");
		onLoginPage.clickLoginButton();
		objSeleniumUtils.waitForContentLoad();	
		}


			waitFor(ProjectVariables.TImeout_2_Seconds);
			onLoginPage.enterUserName(sUserName);		
			String sPassword = GenericUtils.gfnReadDataFromPropertyfile(sUserName, ProjectVariables.sTestUsers);
			onLoginPage.enterPassword(GenericUtils.decode(sPassword));		
			onLoginPage.selectApplicationFromDrowpdown(sApplication);
			onLoginPage.clickLoginButton();
			objSeleniumUtils.waitForContentLoad();	
		
			//objSeleniumUtils.is_WebElement_Displayed("//label[text()='DP 7610']");
			
		} catch (Exception e) {


			Assert.assertTrue("LoginPage details not entered "+e.getMessage(),false);
		}
	}
	

	@Step
	public void validateCPDLoginPage()
	{
		boolean  bLaunched = onLoginPage.launchCPDApplication();
		if(bLaunched == true)
		{
			Assert.assertTrue("CPD Application  launched",true);	
		}
		else
		{
			Assert.assertTrue("CPD Application not launched",false);	
		}
		
	}
	

	@Step
	public void validateLoginPageFields()
	{
		
		    //Check if all fields are displayed
			if(objSeleniumUtils.is_WebElement_Displayed(onLoginPage.UserNameTxtBox))
			{
				 Assert.assertTrue("UserName field displayed in LoginPage",true);		
			} else {   Assert.assertTrue("UserName field not displayed in LoginPage",false);	}	
			 
			if(objSeleniumUtils.is_WebElement_Displayed(onLoginPage.PasswordTxtBox))
			{
				 Assert.assertTrue("Password field displayed in LoginPage",true);		
			} else {   Assert.assertTrue("Password field not displayed in LoginPage",false);	}	
			
			if(objSeleniumUtils.is_WebElement_Displayed(onLoginPage.LoginButton))
			{
				 Assert.assertTrue("LoginButton  displayed in LoginPage",true);		
			} else {   Assert.assertTrue("LoginButton  not displayed in LoginPage",false);	}	
			
			objSeleniumUtils.clickGivenXpath(onLoginPage.ArrowIcon);
			if(objSeleniumUtils.is_WebElement_Displayed(onLoginPage.SelectAppDrpDownPanel))
			{
				 Assert.assertTrue("SelectApplication Dropdown  displayed in LoginPage",true);		
			} else {   Assert.assertTrue("SelectApplication Dropdown  not displayed in LoginPage",false);	}	
		
		
			String sAppOptionXPath = StringUtils.replace(onLoginPage.SelectApplicationDropdown, "Arg", "PM");
			objSeleniumUtils.Click_given_Locator(sAppOptionXPath);
	}


	@Step
	public void validateCredentialsEntry(String  sUserName) 
	{
		try{
						if(!sUserName.isEmpty())
						{			
							onLoginPage.enterUserName(sUserName);		
							onLoginPage.enterPassword(GenericUtils.decode(projectVariables.PASSWORD));					
						}
							waitFor(ProjectVariables.TImeout_2_Seconds);
							onLoginPage.enterUserName(sUserName);		
							String sPassword = GenericUtils.gfnReadDataFromPropertyfile(sUserName, ProjectVariables.sTestUsers);
							onLoginPage.enterPassword(GenericUtils.decode(sPassword));	
						
						} 
	   	catch (Exception e) {			
			Assert.assertTrue("LoginPage details not entered "+e.getMessage(),false);
            getDriver().quit();
		}		
	}

	@Step
	public void validateApplicationDropdownValues(String sAppName) 
	{		
		
		
		String  sAppFullForm = "";			
		onLoginPage.selectApplicationFromDrowpdown("CPW");
		sAppFullForm =  objSeleniumUtils.get_TextFrom_Locator(onLoginPage.ApplicationFullformLabel).trim();			
		if(sAppFullForm.equalsIgnoreCase("Client Policy Workbench"))
		{
			 Assert.assertTrue("Application name:;"+sAppFullForm+" is displayed as hint once we select the CPW Application dropdown value", true);		
		}
		else
		{
			 Assert.assertTrue("Application name:;"+sAppFullForm+" is not displayed as hint once we select the CPW Application dropdown value,Actual value::"+sAppFullForm, true);		
		}
		
		onLoginPage.selectApplicationFromDrowpdown("PM");
		sAppFullForm =  objSeleniumUtils.get_TextFrom_Locator(onLoginPage.ApplicationFullformLabel).trim();			
		if(sAppFullForm.equalsIgnoreCase("Presentation Manager"))
		{
			 Assert.assertTrue("Application name:;"+sAppFullForm+" is displayed as hint once we select the PM Application dropdown value", true);		
		}
		else
		{
			 Assert.assertTrue("Application name:;"+sAppFullForm+" is not displayed as hint once we select the PM Application dropdown value,Actual value::"+sAppFullForm, true);		
		}			
		
		//objSeleniumUtils.clickGivenXpath(onLoginPage.ArrowIcon);		
		/*onLoginPage.selectApplicationFromDrowpdown("CPQ");
		sAppFullForm =  objSeleniumUtils.get_TextFrom_Locator(onLoginPage.ApplicationFullformLabel).trim();
		if(sAppFullForm.equalsIgnoreCase("Client Policy Review Queue"))
		{

			 Assert.assertTrue("Application name:;"+sAppFullForm+" is displayed as hint once we select the CPQ Application dropdown value", true);		
		}
		else
		{
			 Assert.assertTrue("Application name:;"+sAppFullForm+" is not displayed as hint once we select the CPQ Application dropdown value,Actual value::"+sAppFullForm, true);				
		}*/
		
	}

	public void selectApplication(String sAppName)
	{	
		onLoginPage.selectApplicationFromDrowpdown(sAppName);
	}
		
	 @Step
     public void validateHomePage()
     {
         oCPWPage.DynamicWaitfortheLoadingIconWithCount(20);
        
         if( objSeleniumUtils.is_WebElement_Displayed(oCPWPage.CPWOppRunsLabel))
         {
                Assert.assertTrue("CPW HomePage displayed",true);
         }
         else
         {
             Assert.assertTrue("CPW HomePage not  displayed",false);
         }
     }
	
	@Step
	public void userLogsinCPDPMApplication_invalidCredentials(String sUserName, String sPwd) 
	{			
		onLoginPage.launchCPDApplication();					
		try {
			
		if(!(sUserName.isEmpty() && sPwd.isEmpty()))
		{
	
		waitFor(ProjectVariables.TImeout_2_Seconds);
		objSeleniumUtils.highlightElement(onLoginPage.UserNameTxtBox);
		onLoginPage.enterUserName(sUserName);
		objSeleniumUtils.highlightElement(onLoginPage.PasswordTxtBox);
		onLoginPage.enterPassword(GenericUtils.decode(sPwd));	
		onLoginPage.selectApplicationFromDrowpdown("PM");
		Serenity.setSessionVariable("appName").to("PM");
		onLoginPage.clickLoginButton();
		objSeleniumUtils.waitForContentLoad();	
		}
		} catch (Exception e) {
			
			Assert.assertTrue("Username or Password is empty "+e.getMessage(),false);
		}
	}
	
	@Step
	public void validateInvalidCredentials()
	{
			
			if(objSeleniumUtils.is_WebElement_Displayed(onLoginPage.invalidCredentialsErrorMsg)){
				objSeleniumUtils.highlightElement(onLoginPage.invalidCredentialsErrorMsg);
				Assert.assertTrue("Invalid username or password error message is displayed", true);
				if(objSeleniumUtils.get_TextFrom_Locator(onLoginPage.invalidCredentialsErrorMsg).equals("Invalid username or password")){
					Assert.assertTrue("Invalid username or password error message is validated successfully", true);
				}else{
					Assert.assertTrue("Invalid username or password error message is not validated successfully", false);
				}
				
			}else{
				Assert.assertTrue("Invalid username or password error message is not displayed and validation is not successful", false);
			}
			
			
			String userName = objSeleniumUtils.get_TextFrom_Locator(onLoginPage.UserNameTxtBox);
			objSeleniumUtils.highlightElement(onLoginPage.UserNameTxtBox);
			String pwd = objSeleniumUtils.getTexFfromLocator(onLoginPage.PasswordTxtBox);
			objSeleniumUtils.highlightElement(onLoginPage.PasswordTxtBox);
			
			if(userName.isEmpty() && pwd.isEmpty()){
				Assert.assertTrue("Null value for Username and password is validated successfully", true);
			}else{
				Assert.assertTrue("Null value for Username and password is not validated", false);
			}
			
			String applicationName = objSeleniumUtils.getTexFfromLocator(onLoginPage.applicationValueDropdown);
			objSeleniumUtils.highlightElement(onLoginPage.applicationValueDropdown);
			if(applicationName.equalsIgnoreCase(Serenity.sessionVariableCalled("appName").toString())){
				Assert.assertTrue("Selected application name is validated even with invalid username or pwd", true);
			}else{
				Assert.assertTrue("Selected application name is not validated when invalid username or pwd is entered", false);
			}
			
			
		}
		
	

	@Step
	public void clickLogin(String sPlaceholderArg) 
	{
		onLoginPage.clickLoginButton();		
	}


}
