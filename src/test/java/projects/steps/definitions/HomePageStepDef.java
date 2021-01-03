package projects.steps.definitions;

import org.apache.commons.lang3.StringUtils;
import org.junit.Assert;
import org.openqa.selenium.By;

import net.serenitybdd.core.annotations.findby.How;
import net.serenitybdd.core.pages.WebElementFacade;
import net.thucydides.core.annotations.Step;
import net.thucydides.core.annotations.Steps;
import net.thucydides.core.annotations.WhenPageOpens;
import net.thucydides.core.steps.ScenarioSteps;
import project.exceptions.ElementNotFoundException;
import project.utilities.GenericUtils;
import project.utilities.SeleniumUtils;
import project.variables.ProjectVariables;
import project.pageobjects.FilterDrawer;
import project.pageobjects.HomePage;

public class HomePageStepDef  extends ScenarioSteps{
	private static final long serialVersionUID = 1L;
	
	
	HomePage oHomePageObjects;
	SeleniumUtils objSeleniumUtils;
	

	@Step
	public boolean openPMApplication(){
		oHomePageObjects.openPMApplication();
		return true;
	}
	
	@WhenPageOpens
	public void waitUntilTitleAppears() {
		try {
			getDriver().manage().window().maximize();
			objSeleniumUtils.elementShouldBeVisible(objSeleniumUtils.getElement(How.XPATH.toString(), oHomePageObjects.COTIVITI_LOGO, ProjectVariables.TImeout_5_Seconds));
			GenericUtils.logMessage("Cotiviti Logo is visible..");
		} catch (ElementNotFoundException e) {
			new ElementNotFoundException("Cotiviti Logo not found...");
			GenericUtils.logErrorMesage("Cotiviti Logo not found...");
		}
	}

	@Step	
	public boolean verifyHomePageElementExists(String arg1) throws ElementNotFoundException {
		boolean flag = false;
		switch (arg1) {
			case "Presentation Manager label":
				objSeleniumUtils.isElementPresent(objSeleniumUtils.getElement("XPATH",oHomePageObjects.PRESENTATION_MANAGER_LABEL), arg1);
//				highlightElement(element);
				flag = true;
				break;
			case "Client drop down list to select from":
				break;
			case "Filter Panel image":
					WebElementFacade element = objSeleniumUtils.getElement("XPATH",oHomePageObjects.FILTER_PANEL_IMAGE);
					objSeleniumUtils.waitForPresenceOfElement(element,2);
					objSeleniumUtils.elementShouldBeVisible(element);	
					objSeleniumUtils.highlightElement(element);
					flag = true;
					break;
			case "Cotiviti logo":

				break;
			case "Available Oppurtunities deck":

				break;
			case "Presentations container":
				break;
			default:
				GenericUtils.logErrorMesage("No such element found on Home Page");
				break;
			}
			return flag;
		}
	
	public boolean the_user_views_the_Presentation_Manager_Home_Page() {
		Assert.assertTrue(oHomePageObjects.verify_Cotiviti_Logo_Is_Visible());
		Assert.assertTrue(oHomePageObjects.verify_Presentation_Manager_logo_is_visible());
		return true;
	}
	
	public void user_logs_out_of_the_application(String sAppName) {
		oHomePageObjects.user_logs_out_of_the_application(sAppName);
		objSeleniumUtils.switchHangleToCPW();
	}

	@Step
	public void userViewsPMHomepage() 
	{		
		Assert.assertTrue(oHomePageObjects.verify_Presentation_Manager_logo_is_visible());
		GenericUtils.Verify("PM HomePage is displayed ", "PASSED");
		GenericUtils.Verify("PM HomePage is displayed ", true);
		
	}

	@Step
	public void logoutOfApplication(String sAppName) 
	{
		Assert.assertEquals(oHomePageObjects.user_logs_out_of_the_application(sAppName),true);	    
		getDriver().quit();
	}	
	
	@Step
	public void navigationToApplication(String sAppName){
		oHomePageObjects.switchApplication(sAppName);
		objSeleniumUtils.switchHandleToPMBrowser();
		
	}
	
	@Step
	public void validatePPSExistence(){
		oHomePageObjects.verifyPPS();
	
	}
	
	}
	
	
	
	
