package project.pageobjects;

import java.time.Duration;

import org.apache.commons.lang3.StringUtils;
import org.junit.Assert;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Sleeper;
import org.openqa.selenium.support.ui.WebDriverWait;

import net.serenitybdd.core.Serenity;
import net.serenitybdd.core.annotations.findby.By;
import net.serenitybdd.core.annotations.findby.FindBy;
import net.serenitybdd.core.environment.EnvironmentSpecificConfiguration;
import net.serenitybdd.core.pages.PageObject;
import net.serenitybdd.core.pages.WebElementFacade;
import net.thucydides.core.annotations.WhenPageOpens;
import net.thucydides.core.util.EnvironmentVariables;
import project.utilities.GenericUtils;
import project.utilities.OracleDBUtils;
import project.utilities.SeleniumUtils;
import project.variables.ProjectVariables;


public class LoginPage extends PageObject{
	SeleniumUtils objSeleniumUtils;
	ProjectVariables projectVariables;
	
	//Objects	
	@FindBy(xpath = "(//button[@class='btn dropdown-toggle dropdown-button btn-default'])[2]")
	WebElementFacade btnCPWProfile;

	@FindBy(xpath = "//div[@class='dropdown-menu dropdown-menu-right']//button[contains(@class, 'btn-block btn-warning')]")
	//button[contains(text(),'Sign out')]
	WebElementFacade btnCPWSignout;
	
	@FindBy(xpath = "//input[contains(@class, 'username')]")
	WebElementFacade txtUserName;
	
	@FindBy(xpath = "//input[contains(@class, 'password')]")
	WebElementFacade txtPassword;
	
	@FindBy(xpath = "//input[@value='Cotiviti Login']")
	WebElementFacade btnLogin;
		
	@FindBy(xpath = "//span[@id='mxui_widget_LinkButton_1']")
	WebElementFacade lnkPresentationManager;
	
	
//New CPD PM Login page Objects	
 private EnvironmentVariables environmentVariables;
 public String UserNameTxtBox = 	"//app-cpd-authentication//input[@id='mat-input-0']";
 public String  PasswordTxtBox   = "//app-cpd-authentication//input[@id='mat-input-1']";
 public String  ArrowIcon = "//app-cpd-authentication//mat-select//div[contains(@class,'mat-select-value')]";
 public String  SelectApplicationDropdown = "	//div[contains(@class,'mat-select-panel')]/mat-option/span[contains(text(),'Arg')]";
 public String  LoginButton  = 	"//app-cpd-authentication//button/span[text()='Login']";
 public String   ApplicationFullformLabel = "(//app-cpd-authentication//div[contains(@class,'mat-form-field-wrapper')])[3]//mat-hint";
 public String ClientSelect  =  "//mat-toolbar//mat-select";
 public String CPDLoginPageTitle   = "//app-cpd-authentication//mat-card-title";
 public String  SelectAppDrpDownPanel = "//div[contains(@class,'mat-select-panel')]";

 public String invalidCredentialsErrorMsg = "//form[@class='ng-untouched ng-pristine ng-invalid']//div[@class='invalid']";
 public String applicationValueDropdown = "//span[contains(@class,'mat-select-value')]/span";



	
	
	//Possible actions on objects
	public boolean enter_User_Name(String value){
		txtUserName.clear();
		txtUserName.sendKeys(value);
		return true;
	}
	
	public boolean enter_Password(String value){
		txtPassword.clear();
		txtPassword.sendKeys(value);
		return true;
	}
	
	public boolean click_Login_button(){
		btnLogin.click();
		return true;
	}
	
	public boolean click_On_PresentationManager_Link() throws InterruptedException{
		lnkPresentationManager.click();
		Sleeper.SYSTEM_SLEEPER.sleep(Duration.ofSeconds(7));
		return true;
	}
	
	@WhenPageOpens	
	public void waitUntilTitleAppears() throws Throwable {
			getDriver().manage().window().maximize();
		}

	public boolean user_clicks_on_Logout() {
		btnCPWProfile.click();
		objSeleniumUtils.waitForPresenceOfElement(btnCPWSignout, ProjectVariables.TImeout_2_Seconds);
		btnCPWSignout.click();
		objSeleniumUtils.waitForPageLoad();		
		objSeleniumUtils.switchHandleToPMBrowser();
		//waitForPresenceOfElement(txtUserName, ProjectVariables.TImeout_5_Seconds);
	
		return true;
	}

	public boolean Login_screen_should_be_displayed_on_CPW_browser() {
		getDriver().getTitle().equals("CPW - Login");
		txtUserName.isCurrentlyVisible();
		txtPassword.isCurrentlyVisible();
		btnLogin.isCurrentlyVisible();
		return true;
	}

	public boolean presentation_Manager_Applications_should_be_logged_off() {
				
		return true;
	}
	
	public boolean enterUserName(String sUserName){
		objSeleniumUtils.enterTextinCotrol("XPATH",UserNameTxtBox,sUserName);
		return true;
	}
	
	public boolean enterPassword(String sPassword){
		objSeleniumUtils.enterTextinCotrol("XPATH",PasswordTxtBox,sPassword);
		return true;	
	}
	
	public boolean clickLoginButton()
	{
		objSeleniumUtils.highlightElement(LoginButton);
		objSeleniumUtils.Click_given_Locator(LoginButton);
		objSeleniumUtils.waitForContentLoad();	
		return true;
	}
	
	public boolean selectApplicationFromDrowpdown(String sApplicationName)
	{

		String sAppOptionXPath = "";
		objSeleniumUtils.highlightElement(ArrowIcon);
		//objSeleniumUtils.Click_given_Locator(ArrowIcon);
	
		boolean bFlag = false;
		objSeleniumUtils.highlightElement(ArrowIcon);
		objSeleniumUtils.Click_given_Locator(ArrowIcon);
		try{
				//Click_given_Locator(ArrowIcon);				
				if(sApplicationName.equalsIgnoreCase("PM"))
				{
					sAppOptionXPath = StringUtils.replace(SelectApplicationDropdown, "Arg", "PM");
					objSeleniumUtils.Click_given_Locator(sAppOptionXPath);
				}
						
				if(sApplicationName.equalsIgnoreCase("CPW"))
				{
					sAppOptionXPath = StringUtils.replace(SelectApplicationDropdown, "Arg", "CPW");
					objSeleniumUtils.Click_given_Locator(sAppOptionXPath);
				}
				
				if(sApplicationName.equalsIgnoreCase("CPQ"))
				{
					sAppOptionXPath = StringUtils.replace(SelectApplicationDropdown, "Arg", "CPQ");
					objSeleniumUtils.Click_given_Locator(sAppOptionXPath);
				}
				bFlag = true;
		}		catch (Exception e) { 
			
			Assert.assertTrue("Failed due to exeception ==>"+e, false);
		}
		
		GenericUtils.Verify(sApplicationName+" is selected in dropdown", "PASSED");
		//GenericUtils.Verify(sApplicationName+" is selected in dropdown", true);
		
		return bFlag;
	}
	
	public boolean launchCPDApplication()
	{
		
	  boolean  bLoginPage =  false;
		getDriver().manage().deleteAllCookies();
		
		// = new MongoClient(new MongoClientURI(MongoConURL));
		
		String APP_URL = EnvironmentSpecificConfiguration.from(environmentVariables).getProperty("webdriver.base.url");
		ProjectVariables.DB_CONNECTION_URL = EnvironmentSpecificConfiguration.from(environmentVariables).getProperty("DB_CONNECTION_URL");
		
		System.out.println("APPUrl::"+APP_URL);
		System.out.println("DBUrl::"+ProjectVariables.DB_CONNECTION_URL);
		
		getDriver().get(APP_URL);
	    getDriver().manage().window().maximize();
		try{
		WebDriverWait wait1= new WebDriverWait(getDriver(),10,ProjectVariables.ExplicitWait_PollTime);
	    wait1.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(CPDLoginPageTitle)));
	    bLoginPage =  true;
		} catch (Exception e) {}
		return bLoginPage;	  
	}
	 //=======================================================================================================>
	public void LoginCPDApplucation(String sUserName,String sApplication){
		Serenity.setSessionVariable("user").to(sUserName);	
		launchCPDApplication();			
		System.out.println("CPD application is launched successfully.............");
		try {
			
		if(sUserName.isEmpty())
		{	
		waitFor(ProjectVariables.TImeout_2_Seconds);
		enterUserName(projectVariables.USER_NAME);		
		enterPassword(GenericUtils.decode(projectVariables.PASSWORD));		
		selectApplicationFromDrowpdown("PM");
		clickLoginButton();
		objSeleniumUtils.waitForContentLoad();	
		}


			waitFor(ProjectVariables.TImeout_2_Seconds);
			enterUserName(sUserName);		
			String sPassword = GenericUtils.gfnReadDataFromPropertyfile(sUserName, ProjectVariables.sTestUsers);
			enterPassword(GenericUtils.decode(sPassword));		
			selectApplicationFromDrowpdown(sApplication);
			clickLoginButton();
			objSeleniumUtils.waitForContentLoad();	
			
			
		
		}catch(Exception e){
			Assert.assertTrue("LoginPage details not entered "+e.getMessage(),false);
		}
	}	

	
	
}
