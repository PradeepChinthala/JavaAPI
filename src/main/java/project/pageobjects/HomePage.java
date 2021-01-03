package project.pageobjects;

import org.apache.commons.lang3.StringUtils;
import org.junit.Assert;
import org.openqa.selenium.WebElement;
import net.serenitybdd.core.annotations.findby.FindBy;
import net.serenitybdd.core.pages.PageObject;
import net.serenitybdd.core.pages.WebElementFacade;
import project.utilities.GenericUtils;
import project.utilities.SeleniumUtils;
import project.variables.ProjectVariables;

public class HomePage extends PageObject {

	/*
	 * This page has home presentation manager home page header objects for
	 * verification purpose
	 */
	SeleniumUtils objSeleniumUtils;
	CPWPage objCPWPage;  
	FilterDrawer onFilterDrawer;

	@FindBy(xpath = "//span[contains(text(),'Presentation Manager')]")
	WebElementFacade ElementPresentationManagerLabel;
	@FindBy(xpath = "//img[@src=\"assets/img/cotiviti.png\"]")
	WebElementFacade ElementCotivitiLogo;

	@FindBy(xpath = "//div[@class=\"app-cpd-opp-deck\"]")
	WebElementFacade ElementAvailableOppurDesk;
	@FindBy(xpath = "//div[@class=\"app-cpd-pres-deck\"]")
	WebElementFacade ElementPresentationContainer;

	public final String FILTER_PANEL_IMAGE = "//button[@class=\"cpd-filter-opener-button\"]//img[@class=\"filterimage\"]";
	public final String SELECT_ALL_PAYERSHORTS = "//div[@id=\"jqxWidgetd5535e5a537a\"]";
	public final String SELECT_ALL_PAYERSHORTS_LABEL = "//div[@id=\"jqxWidgetd5535e5a537a\"]//b";
	public final String PRESENTATION_MANAGER_LABEL = "//span[contains(text(),\"Presentation Manager\")]";
	public final String  CLIENT_DROPDOWN_LIST = "//span[contains(text(),\"Select Client\")]";
	public final String COTIVITI_LOGO = "//img[@src=\"assets/img/cotiviti.png\"]";
	public final String AVAILABLE_OPPURTUNITIES_DECK = "";
	public final String  PRESENTATIONS_CONTAINER= "";
	public String cotivitiLogo="//img[@src=\"assets/img/cotiviti.png\"]";

	@FindBy(css = "button[value='Search']")
	WebElement searchButton;


	public String PresManagerLabel =  "//span[contains(text(),'Presentation Manager')]";

	//** New ones

	//CPW Signout Objects
	//	public String   CPWUserICon  = "//button[@class='btn dropdown-toggle dropdown-button btn-default']/span[1][@class='glyphicon fa fa-user']";		
	//	public String   CPWSignOut   = "//div[@class='dropdown-menu dropdown-menu-right']//button[contains(@class, 'btn-block btn-warning')]"; 
	//	public String  CPWOppRunsLabel =  "//h2[@class='mx-text mx-name-text15 section__title' and text()='Opportunity Runs']";

	public String sContentLoading =  "//app-cpd-loading-indicator/span[text()='Please wait. Content loading...']";			
	public String   PersonIcon   = "//app-cpd-header//div[@class='notification']//mat-icon[text()='person']";	
	public String  SignOutLink  =  "//div[@class='mat-menu-content']/button[text()='Sign out']";
	public String  SigninLabel =  "//div[@role='heading' and  text()='Sign in']"; 
	public String switchingAppLink = "//button[contains(@class,'cpwapplink mat-button mat-button-base')]/span[text()='sval']";



	public boolean verify_Presentation_Manager_logo_is_visible() {
		GenericUtils oGenericUtils=this.switchToPage(GenericUtils.class);
		//waitForElement(PresManagerLabel, "",30);
		oGenericUtils.isElementExist(cotivitiLogo);
		GenericUtils.Verify("PM HomePage is displayed ", "PASSED");
		GenericUtils.Verify("PM HomePage is displayed ", true);
		
		return ElementPresentationManagerLabel.isVisible();
	}

	public boolean verify_Cotiviti_Logo_Is_Visible() {
		return ElementCotivitiLogo.isVisible();
	}

	public boolean user_logs_out_of_the_application(String sAppName)
	{
		boolean  bLogout =  true;		
		switch(sAppName)
		{/*		

		case "PM":					
			objSeleniumUtils.clickGivenXpath(PersonIcon);
			objSeleniumUtils.clickGivenXpath(SignOutLink);                           
			try{
				WebDriverWait wait1= new WebDriverWait(getDriver(),10,ProjectVariables.ExplicitWait_PollTime);                                             
				wait1.until(ExpectedConditions.invisibilityOfElementLocated(By.xpath(PresManagerLabel)));
				bLogout =  true;                                        
			} catch (Exception e) {}

			break;

		case "CPW":				
			objSeleniumUtils.clickGivenXpath(objCPWPage.CPWUserICon);
			objSeleniumUtils.clickGivenXpath(objCPWPage.CPWSignOut);			
			try{
				WebDriverWait wait1= new WebDriverWait(getDriver(),10,ProjectVariables.ExplicitWait_PollTime);
				wait1.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(SigninLabel)));
				bLogout =  true;				
			} catch (Exception e) {}

			break;		

		case "CPQ":				
			objSeleniumUtils.clickGivenXpath(objCPWPage.CPQUserIcon);
			objSeleniumUtils.clickGivenXpath(objCPWPage.CPQSignOut);			
			try{
				WebDriverWait wait1= new WebDriverWait(getDriver(),10,ProjectVariables.ExplicitWait_PollTime);
				wait1.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(SigninLabel)));
				bLogout =  true;			
			} catch (Exception e) {}

			break;		
		*/}


		return bLogout;				
	}
	public boolean openPMApplication(){
		open();
		waitFor(ProjectVariables.APP_RENDER_WAIT_TIME);
		return true;
	}

	public void switchApplication(String sAppName)
	{
		if(sAppName.equalsIgnoreCase("PM")){
			objSeleniumUtils.Click_given_Locator(objCPWPage.Lnk_PM);
			objSeleniumUtils.waitForContentLoad();	
		}else{
			objSeleniumUtils.waitForElement(StringUtils.replace(switchingAppLink, "sval", sAppName), "", 10);
			objSeleniumUtils.Click_given_Locator(StringUtils.replace(switchingAppLink, "sval", sAppName));
		}

	}

	public void verifyPPS(){

		if(objSeleniumUtils.is_WebElement_Displayed(StringUtils.replace(onFilterDrawer.allPPS, "svalue", "Payer Shorts")) && objSeleniumUtils.is_WebElement_Displayed(StringUtils.replace(onFilterDrawer.allPPS, "svalue", "LOB"))){
			Assert.assertTrue("All Payer Shorts and LOB are displayed - verification is successful",true);
		}else{
			Assert.assertTrue("All Payer Shorts and LOB are not displayed - verification is not successful",false);
		}

		if(!objSeleniumUtils.is_WebElement_Displayed(StringUtils.replace(onFilterDrawer.allPPS, "svalue", "Product Type"))){
			Assert.assertTrue("All Product type details are not displayed - verification is successful",true);
		}else{
			Assert.assertTrue("All Product type details are displayed - verification is not successful",false);

		}

	}




}
