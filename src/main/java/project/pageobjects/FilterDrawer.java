package project.pageobjects;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.Random;

import org.apache.commons.lang3.StringUtils;
import org.bson.BsonDocument;
import org.bson.conversions.Bson;
import org.json.simple.parser.ParseException;
import org.junit.Assert;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Sleeper;

import com.mongodb.client.model.Filters;
import com.typesafe.config.ConfigException.Generic;

import net.thucydides.core.annotations.Step;
import net.thucydides.core.steps.ScenarioSteps;
import net.serenitybdd.core.Serenity;
import net.serenitybdd.core.annotations.findby.By;
import net.serenitybdd.core.annotations.findby.FindBy;
import net.serenitybdd.core.pages.PageObject;
import net.serenitybdd.core.pages.WebElementFacade;
import project.exceptions.ElementNotFoundException;
import project.utilities.GenericUtils;
import project.utilities.MicroServRestUtils;
import project.utilities.MongoDBUtils;
import project.utilities.OracleDBUtils;
import project.utilities.SeleniumUtils;
import project.variables.ProjectVariables;


public class FilterDrawer extends PageObject {
	
	SeleniumUtils objSeleniumUtils;
	//GenericUtils oGenericUtils;
	//ScenarioSteps oScenarioSteps;
	//OppurtunityDeck oOppurtunityDeck;
	//PresentationProfile oPresentationProfile;
	CPWPage oCPWPage; 
	
	// Static xPaths.

	@FindBy(xpath = "//mat-dialog-container[@aria-modal='true']")
	private WebElementFacade popUpSessionEnded;
	@FindBy(xpath = "//button[@class='confirmButton mat-stroked-button']")
	private WebElementFacade btnSessionEndedOk;
	@FindBy(xpath = "//button[@class=\"cpd-filter-opener-button\"]//img[@class=\"filterimage\"]")
	private WebElementFacade ElementFilterImage;
	@FindBy(xpath = "//span[contains(text(),\"Select Client\")]")
	private WebElementFacade ElementClientDropdown;
	@FindBy(xpath = "(//button[contains(text(),'Reset')])[2]")
	private WebElementFacade ElementTopicResetBtn;
	@FindBy(xpath = "(//button[contains(text(),'Apply')])[2]")
	private WebElementFacade ElementTopicApplyBtn;
	@FindBy(xpath = "(//button[contains(text(),'Reset')])[1]")
	private WebElementFacade ElementPayerShortResetBtn;
	@FindBy(xpath = "(//button[contains(text(),'Apply')])[1]")
	private WebElementFacade ElementPayerShortApplyBtn;
	@FindBy(xpath = "//mat-drawer[@class='matDrawer mat-drawer ng-tns-c6-0 ng-trigger ng-trigger-transform mat-drawer-side ng-star-inserted']")
	private WebElementFacade ElementOpenedFilterDrawer;
//	@FindBy(xpath = "//span[@class='ng-tns-c12-2 ng-star-inserted']")	
	//	private WebElementFacade ElementClientDropDownArrow;       //This object is not getting identified now ,so changed to below one  -July 9th
	
	@FindBy(xpath = "//mat-toolbar//mat-select[@placeholder='Select Client']")	
	private WebElementFacade ElementClientDropDownArrow;	
	@FindBy(xpath = "//body//jqxcheckbox//div//..//div//b[contains(text(),'Payer Shorts')]//parent::div//..//div")
	private WebElementFacade ElementSelectAllPayer;
	@FindBy(xpath = "/div/div[1]/span/span")
	private WebElementFacade ElementClientSelected;
//	@FindBy(xpath = "//jqxcheckbox/div/../div/b[contains(text(),'LOB')]/parent::div")
//	private WebElementFacade ElementSelectAllLOB;
	//@FindBy(xpath = "(//app-cpd-policy-set//div[@class='payerlob'])[2]//div[@class='jqx-widget jqx-checkbox']")
	@FindBy(xpath = "//b[contains(text(),'LOB')]/ancestor::div[contains(@class,'checkbox')]")
	private WebElementFacade ElementSelectAllLOB;
	@FindBy(xpath = "//jqxcheckbox/div/../div/b[contains(text(),'Product')]/parent::div")
	private WebElementFacade ElementSelectAllProduct;
	@FindBy(xpath = "//label[contains(text(),'Medical Policy: Ambulance Policy')]")
	private WebElement ElementDP;
	@FindBy(xpath = "//jqxlistbox//div")
	private WebElement ElementToVerifySelectedPayerShorts;
	@FindBy(xpath = "//jqxlistbox//div")
	private WebElement ElementToVerifySelectedLOB;
	@FindBy(xpath = "//div[@class='cdk-overlay-container']")
	private WebElement clientList;

	@FindBy(xpath = "//body//div[@class='cpd-filter-drawer-container ng-star-inserted']//jqxlistbox//following::jqxlistbox[2]//div//..//div//div//following::input")
	private WebElement ElementToVerifySelectedProduct;

	@FindBy(xpath = "//div[@class='four']")
	private WebElementFacade ElementMedicalPolicyTopicGrid;

	// HeadWidget

	// PayerShort xpaths
	@FindBy(xpath = "//body//jqxcheckbox//div//..//div//b[contains(text(),'Payer Shorts')]//parent::div//parent::jqxcheckbox//following-sibling::jqxlistbox/div")
	private WebElementFacade elementPayerShort;

	// LOB xpaths
	@FindBy(xpath = "//body//jqxcheckbox//div//..//div//b[contains(text(),'LOB')]//parent::div//parent::jqxcheckbox//following-sibling::jqxlistbox/div")
	private WebElementFacade elementLOB;

	// Product xpaths
	@FindBy(xpath = "//body//jqxcheckbox//div//..//div//b[contains(text(),'Product')]//parent::div//parent::jqxcheckbox//following-sibling::jqxlistbox/div")
	private WebElementFacade elementProduct;

	// Below two xpaths subxpaths for all three payershort, LOB and Product.
	private final String checkboxesChecked = ".//div[@class='jqx-checkbox chkbox'][@checked='true']";
	private final String labels = ".//following-sibling::span";

	// Dynamic xPaths
	private WebElementFacade ElementClient;
	private WebElementFacade ElementSelectPayer;
	private WebElementFacade ElementSelectLOB;
	private WebElementFacade ElementSelectProduct;
	private WebElementFacade ElementSelectPolicy;
	private WebElementFacade ElementClickPolicy;
	private WebElementFacade ElementSelectTopic;

	//private final String partial_xPathSelectClientList_part1 = "//div[@id=\"cdk-overlay-0\"]//div//div//mat-option//span[contains(text(),\"";
	private final String partial_xPathSelectClientList_part1 = "//div[contains(@id,\"cdk-overlay\")]//div//div//mat-option//span[contains(text(),\"";
	private final String partial_xPathSelectClientList_part2 = "\")]";
	private final String partial_xpathSelectPayerShort1 = "//body//div[@class='cpd-filter-drawer-container ng-star-inserted']//jqxlistbox//div//..//div//span[contains(text(),'";
	private final String partial_xPathSelectPayerShort2 = "')]"; //// preceding-sibling::div/div/div/span";
	private final String partialxPathPolicy_part1 = "//div[contains(text(),'";
	private final String partialxPathPolicy_part2 = "')]"; /// parent::div/preceding-sibling::div/div/div/span";
	private final String partialxPathPolicy_part2_click = "')]/parent::div/preceding-sibling::div/div";
	private final String partialxPathTopic_part2 = "')]/parent::div/preceding-sibling::div[2]/div/div/span";
	private final String sPayershortCheckbox = "//app-cpd-filter-drawer-container//app-cpd-policy-set//span[contains(text(),'arg')]/parent::div/div[@class='jqx-checkbox chkbox']";
	private final  String sAllFilterCheckboxes  =  "//app-cpd-filter-drawer-container//app-cpd-policy-set//div[@class='jqx-checkbox chkbox']";
	public String GetAvailableOppurtunitiesBtn = "//button[contains(text(),'Get Available Opportunities')]";	
	
	public String  sFilterPayershorts =  "(//app-cpd-filter-drawer-container//app-cpd-policy-set//div[@class='payerlob'])[1]/jqxlistbox//input[@type='hidden']";  //they changed to numbers in UI ,so correct values not fetched.
	
	public String  sFilterLOBs =  "(//app-cpd-filter-drawer-container//app-cpd-policy-set//div[@class='payerlob'])[2]/jqxlistbox//input[@type='hidden']";

	public String MedPoliciesExpanders = "//app-cpd-medical-policy-filter//div[@role='grid']//div[@role='row']//div[@class=' jqx-grid-group-cell jqx-grid-cell-pinned jqx-grid-group-collapse jqx-icon-arrow-right']";

	public String MedPoliciesTopics = "//app-cpd-medical-policy-filter//div[@role='grid']//div[@role='row']//div[@class=' jqx-grid-group-cell' and @title='false']";

	public String MedPoliciesChkBoxes = "//app-cpd-medical-policy-filter//div[@role='grid']//div[@role='row']//div[@class=' jqx-grid-group-cell']/div[@class='aw-checkbox-cell']/div";

	public String MedPolicyFilterMsg = "//app-cpd-medical-policy-filter//div[@role='grid']//div[contains(@id,'contenttable')]//span";
	
	public String  sMedPoliciesAllCheckBox =  "//app-cpd-medical-policy-filter//div[@class='jqx-checkbox']/div";
	
	public String  sClientDropdown   =  "//mat-toolbar//mat-select[@placeholder='Select Client']";

	public String AllPayersSelect = "(//body//jqxcheckbox//div//..//div//b[contains(text(),'Payer Shorts')]//parent::div//..//div)[1]";

	public String ClientDropdownArrow =	"//mat-toolbar//div[@class='mat-form-field-infix']/mat-select//div[@class='mat-select-arrow']";

	public String AllLOBSelect =   "//jqxcheckbox/div/../div/b[contains(text(),'LOB')]/parent::div";

	public String ClientSelect  =  "//mat-toolbar//mat-select";

	public String  FilterApplyBtn =  "//button[text()='Apply']";

	public String  MedPolicyFilterApplyBtn = "(//app-cpd-medical-policy-filter//jqxbutton)[2]/button";


	//==============================================Locators added by Rama===========================================================================================>
	public String sRdm_PYS="(//div[@class='payerlob'])[1]//*[contains(text(),'')]/..//span[contains(@class,'listitem')][1]";
	public String sRdm_LOB="(//div[@class='payerlob'])[2]//*[contains(text(),'')]/..//span[contains(@class,'listitem')][1]";
	public String sReset="//button[contains(text(),'Reset')]";
	public String sEdt_MP_Topic="//input[@placeholder='Search for Medical Policy / Topic']";
	public String sBtn_Search="//img[@class='searchimage']";
	public String sBtn_Apply="//button[contains(text(),'Apply')][@role='button']";
	//public String sBtn_Okay="//button[.='Okay'][@role='button']";
	public String sBtn_Okay="//div[@class='asign-popover_button']//button[.='Okay'][@role='button']";
	public String sBtn_Cancel="//button[.='Cancel'][@role='button']";
	public String sOppAssign="//b[contains(text(),'Opportunity assigned to:')]";
	public String sAssnOpp="//b[contains(text(),'Assign selected opportunities to:')]";
	public String sALL="//label[contains(text(),'ALL')]//span";
	//===============================================================================================================================================================>

	
	public String scrollPayerShort = "//b[contains(text(),' Payer Shorts')]/ancestor::div[@class='payerlob']//div[contains(@id,'ScrollThumbverticalScrollBar')]";
		
	public String scrollLOB = "//b[contains(text(),' LOB')]/ancestor::div[@class='payerlob']//div[contains(@id,'ScrollThumbverticalScrollBar')]";
	
	
	public String scrollDwnArrowPayerShort = "//div[@class='payerlob']//div/b[text()=' Payer Shorts']//parent::div//..//following-sibling::jqxlistbox//div[contains(@class,'jqx-reset jqx-icon-arrow-down')]";


	public String  Payershort =  "(//app-cpd-policy-set//div[@class='payerlob'])[1]//div[@role='option']/span[text()='PayershortArg']";

	public String  LOB 	      =  "(//app-cpd-policy-set//div[@class='payerlob'])[2]//div[@role='option']/span[text()='LOBArg']";
	
	public String TopicResetBtn = "//button[contains(text(),'Reset')]";
	
		public String selectedClient = "//mat-select[@role='listbox' and contains(@placeholder, 'Select Client')]//div/span/span";
		
		public String sClientsContainer = "//div[contains(@class,'transformPanel ')]//span";
	//public String  FilterApplyBtn = "(//app-cpd-policy-set//jqxbutton)[2]/button";
	
		public String sPayerShortChkBox = "//span[text()='sval']//parent::div/div";
	
	String AllMedicalPolicies = "//span[contains(text(),'Medical Policy')]/../../../..//div[contains(@class,'checkbox')][@role='checkbox']";
	
	public String firstClientInList = "(//div[@id='cdk-overlay-0']//div//div//mat-option//span)[1]";
	
	//public String Medicalpolicy_Checkbox="//div[text()='value']/../..//div[contains(@id,'Unchecked')]";
	

	public String payerShort = "//span[contains(text(),'svalue')]/..//div[contains(@class,'checkbox')][@checked]";
	
	public String payerShortStatus = "//div[@class='payerlob']//*[contains(text(),'svalue')]/../div";
	
	public String allPPS = "//b[text()=' svalue']/..";
	
	//******************   METHODS *****************************
	
	public String MedicalpolicyRawSavings = "//div[text()='sval']/../following-sibling::div/div";

	public String Medicalpolicy_Checkbox="//div[contains(text(),'value')]/../..//div//span/..";
	
	public String allPayerShortLOBText = "//b[contains(text(),'Payer') or contains(text(),'LOB')]/ancestor::span/following-sibling::jqxlistbox//span[contains(@class,'listitem')]";
	
	


	public static String getDynamicXpath(String sXpath,Object sVal, Object sVal1){

		String sFormattedXpath = null;

		switch (sXpath.toUpperCase()){
		
		case "SELECT ALL CHECKBOX":
			sFormattedXpath  = "//b[contains(text(),'"+sVal+"')]/ancestor::div[@class ='payerlob']//div[@role = 'checkbox']";
			break;	
			
		case "FLITER CHECKBOX BY NUMBER":
			sFormattedXpath  = "//b[contains(text(),'"+sVal+"')]/ancestor::div[@class ='payerlob']/descendant::div[contains(@id,'listBoxContent')]/div/div["+sVal1+"]/div";
			break;			

		case "GET SELECTED FLITER VALUE BY NUMBER":
			sFormattedXpath  = "//b[contains(text(),'"+sVal+"')]/ancestor::div[@class ='payerlob']/descendant::div[contains(@id,'listBoxContent')]/div/div["+sVal1+"]/div/../span";
			break;		
			
		case "FILTER CHECKBOX BY PAYER OR LOB":
			sFormattedXpath  = "((//div[@role='checkbox']//b[contains(text(),'"+sVal+"')]/..//div)[1]/../..//..//..//span[.='"+sVal1+"']/..//div)[1]";
			
			break;	

		}	
		return sFormattedXpath;
	}

	public static String getDynamicXpath(String sXpath,Object sVal){

		String sFormattedXpath = null;

		switch (sXpath.toUpperCase()){

		case "SELECT ALL CHECKBOX":
			sFormattedXpath  = "//b[contains(text(),'"+sVal+"')]/ancestor::div[@class ='payerlob']//div[@role = 'checkbox']";
			break;			
		}	

		return sFormattedXpath;
	}

	/**********************************************************************************************************************
	 * 														Methods
	 **********************************************************************************************************************/


	public WebElementFacade getElementClient(String arg1) {
		ElementClient = find(
				By.xpath(partial_xPathSelectClientList_part1 + arg1 + partial_xPathSelectClientList_part2));
		return ElementClient;
	}

	private WebElementFacade getElementSelectPayer(String arg1) {
		ElementSelectPayer = find(By.xpath(partial_xpathSelectPayerShort1 + arg1 + partial_xPathSelectPayerShort2));
		return ElementSelectPayer;
	}
	
	public  void SelectPayerShort(String arg1) {
		try{
		ElementSelectPayer = find(By.xpath(partial_xpathSelectPayerShort1 + arg1 + partial_xPathSelectPayerShort2));
		ElementSelectPayer.click();
		}catch (Exception e) 
		{
			//scrollingToGivenElement(getDriver(), scrollPayerShort);
			boolean b=false;
			do
			{  
				objSeleniumUtils.Click_given_Locator(scrollDwnArrowPayerShort);
				// b= is_WebElement_Displayed(StringUtils.replace(sPayerShort, "sval", arg1));
				 System.out.println(StringUtils.replace(sPayerShortChkBox, "sval", arg1));
				 b= objSeleniumUtils.is_WebElement_Visible(StringUtils.replace(sPayerShortChkBox, "sval", arg1));
			}while(b==false);
			
			objSeleniumUtils.Click_given_Locator(StringUtils.replace(sPayerShortChkBox, "sval", arg1));
			//ElementSelectPayer = find(By.xpath(partial_xpathSelectPayerShort1 + arg1 + partial_xPathSelectPayerShort2));
			
		}
		
	}

	private WebElementFacade getElementSelectLOB(String arg1) {
		ElementSelectLOB = find(By.xpath(partial_xpathSelectPayerShort1 + arg1 + partial_xPathSelectPayerShort2));
		return ElementSelectLOB;
	}

	private WebElementFacade getElementSelectProduct(String arg1) {
		ElementSelectProduct = find(By.xpath(partial_xpathSelectPayerShort1 + arg1 + partial_xPathSelectPayerShort2));
		return ElementSelectProduct;
	}

	// private WebElementFacade getElementSelectPolicy(String arg1) {
	// ElementSelectPolicy = find(By.xpath(partialxPathPolicy_part1 + arg1 +
	// partialxPathPolicy_part2));
	// return ElementSelectPolicy;
	// }

	private WebElementFacade getElementClickPolicy(String arg1) {
		ElementClickPolicy = find(By.xpath(partialxPathPolicy_part1 + arg1 + partialxPathPolicy_part2_click));
		return ElementClickPolicy;
	}

	// private WebElementFacade getElementSelectTopic(String arg1) {
	// ElementSelectTopic = find(By.xpath(partialxPathPolicy_part1 + arg1 +
	// partialxPathTopic_part2));
	// return ElementSelectTopic;
	// }

	// Actions

	public boolean user_selects_given_value_from_Client_drop_down_list(String arg1) throws Throwable {
		GenericUtils oGenericUtils=this.switchToPage(GenericUtils.class);		
		
		oGenericUtils.clickOnElement("//span[contains(text(),'NPP Opportunities')]/span");
		
		oGenericUtils.clickOnElement(sClientDropdown);
		if (arg1 == "random") {
			List<WebElement> AllClients =  new ArrayList<WebElement>();			
			AllClients  = objSeleniumUtils.getElementsList("XPATH","//div[@id='cdk-overlay-0']//div//div//mat-option//span");  	
			int NoOfClients = AllClients.size();
			Random RandGenerator = new Random();
			int RandomInt =  RandGenerator.nextInt(NoOfClients)+1;		
			oGenericUtils.clickOnElement("(//div[@id='cdk-overlay-0']//div//div//mat-option//span)["+RandomInt+"]");
			return oGenericUtils.clickOnElement("//span[contains(text(),'NPP Opportunities')]/span");
		} else {			
			objSeleniumUtils.waitForContentLoad();
			if(arg1.equals("clientkey")){
				arg1=MongoDBUtils.Retrieve_the_PayerKey_From_mongodb_with_the_given_Payershort(Serenity.sessionVariableCalled("clientkey"), "ClientKey");
			}
			getElementClient(arg1).click();
			Serenity.setSessionVariable("SelectClientName").to(arg1);
			objSeleniumUtils.defaultWait(ProjectVariables.TImeout_6_Seconds);	
			objSeleniumUtils.waitForContentLoad();
			
			if(arg1.isEmpty())
			{
				arg1=Serenity.sessionVariableCalled("Client");
			}
			
			if(Serenity.sessionVariableCalled("clientkey")==null&&!arg1.contains("Horizon Healthcare Services"))
			{
				//Serenity.setSessionVariable("clientkey").to(MongoDBUtils.Retrieve_the_PayerKey_From_mongodb_with_the_given_Payershort(arg1, "Client"));
				

				//To retrieve the client key from the given client
				String sClientkey=CPWPage.RetrieveTheClientkeyfromgivenClientthroughservice(arg1);
				
				Serenity.setSessionVariable("clientkey").to(sClientkey);
				
				System.out.println("ClientKey==>"+Serenity.sessionVariableCalled("clientkey"));
			}
		}
		
		return oGenericUtils.clickOnElement("//span[contains(text(),'NPP Opportunities')]/span");
	}

	public boolean user_selects_given_value_from_Payer_Shorts(String arg1) throws InterruptedException {
		Sleeper.SYSTEM_SLEEPER.sleep(Duration.ofSeconds(5));
		getElementSelectPayer(arg1).click();
		Serenity.setSessionVariable("SelectedPayerShort").to(arg1);
		return true;
	}

	public boolean user_selects_given_value_from_LOB(String arg1) {
		getElementSelectLOB(arg1).click();
		return true;

	}

	public boolean user_selects_given_value_from_Product(String arg1) {
		getElementSelectProduct(arg1).click();
		return true;
	}

	public boolean user_filters_by_clicking_on_Apply_for_Payer_Shorts() {
		ElementPayerShortApplyBtn.click();
		return true;
	}

	public boolean user_selects_given_value_from_Medical_Policy_Topic(String arg1) throws InterruptedException {
		Sleeper.SYSTEM_SLEEPER.sleep(Duration.ofSeconds(1));
		String sChecked = getElementClickPolicy(arg1).getAttribute("checked");
		if (sChecked == null){
			getDriver().findElement(By.xpath("//div[contains(text(),'"+arg1+"')]/parent::div/preceding-sibling::div/div/div")).click();
		}
		return true;
	}

	public boolean user_filters_by_clicking_on_Apply_for_Medical_Policy_Topic() {
		SeleniumUtils objSeleniumUtils=this.switchToPage(SeleniumUtils.class);
		if(objSeleniumUtils.is_WebElement_Displayed(MedPolicyFilterApplyBtn))
		{
			objSeleniumUtils.highlightElement(MedPolicyFilterApplyBtn);
			objSeleniumUtils.clickGivenXpath(MedPolicyFilterApplyBtn);	
			objSeleniumUtils.waitForContentLoad();
		
		}	
		return true;
	}

	public boolean user_unchecks_selectAllPayers() {
		objSeleniumUtils.waitForVisibilityOfElement(ElementSelectAllPayer, ProjectVariables.ElementVisibleTimeout);
		ElementSelectAllPayer.click();
		if (ElementSelectAllPayer.getAttribute("aria-checked").toString().equals("true")) {
			ElementSelectAllPayer.click();
		}
		return true;
	}

	public boolean user_unchecks_selectAllLOB() {
		ElementSelectAllLOB.click();
		if (ElementSelectAllLOB.getAttribute("aria-checked").toString().equals("true")) {
			ElementSelectAllLOB.click();
		}
		return true;
	}

	public boolean user_unchecks_selectAllProduct() {
		ElementSelectAllProduct.click();
		if (ElementSelectAllProduct.getAttribute("aria-checked").toString().equals("true")) {
			ElementSelectAllProduct.click();
		}
		return true;
	}

	public boolean user_unchecks_selectAllPolicies() {		
				
				
		//objSeleniumUtils.defaultWait(ProjectVariables.TImeout_3_Seconds);	
		WebElement  AllMedChkBox = getDriver().findElement(By.xpath(AllMedicalPolicies+"/div[contains(@class,'checkbox')]"));
		//objSeleniumUtils.highlightElement(AllMedChkBox);
		objSeleniumUtils.clickGivenWebElement(AllMedChkBox);
		objSeleniumUtils.defaultWait(ProjectVariables.TImeout_2_Seconds);
		String Attribvalue = objSeleniumUtils.Get_Value_By_given_attribute("aria-checked", AllMedicalPolicies);
		
		if (Attribvalue.equalsIgnoreCase("true")|| Attribvalue.equalsIgnoreCase("undefined")) 
		{
			objSeleniumUtils.defaultWait(ProjectVariables.TImeout_5_Seconds);
			objSeleniumUtils.Click_given_Xpath(AllMedicalPolicies+"/div[contains(@class,'checkbox')]");			
		}
	   	return true;
	
	
	}

	public boolean user_should_view_given_value_selected_in_Client_drop_down_list(String arg1) {
		
		//String clientName =objSeleniumUtils.getTexFfromLocator(StringUtils.replace(selectedClient, "sval", arg1));
		
		String clientName =objSeleniumUtils.getTexFfromLocator(selectedClient);
	
		if(clientName.equals(arg1)){
			return true;
		}else{
			return false;
		}
	

	}

	public boolean user_should_view_given_value_selected_in_Payer_Shorts(String arg1) {
		List<String> actual = get_Selected_Elements_for_given_parameter("PayerShort");
		return actual.get(0).equals(arg1);
	}

	public boolean user_should_view_given_value_selected_in_LOB(String arg1) {
		List<String> actual = get_Selected_Elements_for_given_parameter("LOB");
		return actual.get(0).equals(arg1);

	}

	public boolean user_should_view_given_value_selected_in_Product(String arg1) {
		List<String> actual = get_Selected_Elements_for_given_parameter("Product");
		return actual.get(0).equals(arg1);

	}

	public boolean user_should_view_given_value_selected_in_Medical_Policy_Topic(String arg1) {
		List<String> actual = get_Selected_Medical_Policies();
		if (actual.size() > 1)
			return false;
		if (actual.get(0).equals(arg1)) {
			System.out.println("Match");
			return true;
		} else {
			System.out.println("No Match");
			return false;
		}
	}

	public List<String> get_Selected_Elements_for_given_parameter(String arg1) {
		List<WebElement> checkbox = new ArrayList<WebElement>();
		List<String> valuesSelected = new ArrayList<String>();
		switch (arg1) {
		case "PayerShort":
			checkbox = elementPayerShort.findElements(By.xpath(checkboxesChecked));
			if (checkbox != null)
				for (WebElement e : checkbox) {
					valuesSelected.add(e.findElement(By.xpath(labels)).getAttribute("innerHTML"));
					System.out.println("Match");
				}
			return valuesSelected;
		case "LOB":
			checkbox = elementLOB.findElements(By.xpath(checkboxesChecked));
			if (checkbox != null)
				for (WebElement e : checkbox) {
					valuesSelected.add(e.findElement(By.xpath(labels)).getAttribute("innerHTML"));
					System.out.println("Match");
				}
			return valuesSelected;
		case "Product":
			checkbox = elementProduct.findElements(By.xpath(checkboxesChecked));
			if (checkbox != null)
				for (WebElement e : checkbox) {
					valuesSelected.add(e.findElement(By.xpath(labels)).getAttribute("innerHTML"));
					System.out.println("Match");
				}
			return valuesSelected;
		case "Policy": // Existing function to be optimized and put here.
		case "Topic": // TBD
		default:
			valuesSelected.add("No such option avaialble");
			return valuesSelected;
		}
	}

	public List<String> get_Selected_Medical_Policies() {
		List<String> valuesSelected = new ArrayList<String>();
		WebElement checkbox, row;
		int size = ElementMedicalPolicyTopicGrid.findElements(By.xpath(".//div[@role='row']//span")).size();
		for (int i = 1; i <= size; i++) {
			try {
				row = ElementMedicalPolicyTopicGrid.findElement(By.xpath(".//div[@role='row'][" + i + "]"));
				checkbox = row.findElement(By.xpath(".//span"));
				if (checkbox.getAttribute("id").toString().equals("allChecked")) {
					valuesSelected.add(row.findElement(By.xpath(".//div//div//following::div//div")).getText());
					System.out.println("valuesSelected: " + valuesSelected);
				}
			} catch (Exception e) {

				e.printStackTrace();
			}
		}
		return valuesSelected;
	}

	public boolean user_should_view_Reset_button_for_Payer_Shorts() {
		return ElementPayerShortResetBtn.isCurrentlyVisible();
	}

	public boolean user_should_view_Apply_button_for_Payer_Shorts() {
		boolean value = false;
		 value = objSeleniumUtils.is_WebElement_Displayed(FilterApplyBtn);
		return value;
	}

	public boolean user_should_view_Reset_button_for_Medical_Policy_Topics() {
		boolean value = false;
		 value = objSeleniumUtils.is_WebElement_Displayed(TopicResetBtn);
		return value;
	}

	public boolean user_should_view_Apply_button_for_Medical_Policy_Topics() {
		boolean value = false;
		value = objSeleniumUtils.is_WebElement_Displayed(MedPolicyFilterApplyBtn);
		return value;
		
	}

	public boolean user_verifies_sessionTimeout_pop_and_clicks_ok() {
		boolean flag = false;
		for (int i = 0; i < 1; i++) {
			try {
				if (popUpSessionEnded.isCurrentlyVisible()) {
					waitABit(ProjectVariables.TImeout_2_Seconds);
					btnSessionEndedOk.click();
					flag = true;
				} else
					flag = false;
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return flag;
	}

	public List<String> get_clientList_populated_on_UI() {
		ElementClientDropDownArrow.click();
		ArrayList<String> clientListString = new ArrayList<String>();
		List<WebElement> clientListElement = new ArrayList<WebElement>();
		clientListElement = clientList.findElements(By.xpath(".//span[@class='mat-option-text']"));
		for (WebElement e : clientListElement) {
			clientListString.add(e.getText());
		}
		Collections.sort(clientListString);
		return clientListString;
	}
	
	//New ones
		
public void user_selects_AllPayerShorts() {
		//waitFor(ProjectVariables.ElementVisibleTimeout);
	objSeleniumUtils.waitForElement(AllPayersSelect, "",6);
		try
		{
				ElementSelectAllPayer.shouldBeVisible();
				if(ElementSelectAllPayer.getAttribute("aria-checked").equalsIgnoreCase("false"))
				{
							ElementSelectAllPayer.click();
				}	
		}		
				catch (Exception e) {					
					System.out.println("Exception Message::"+e.getMessage());
					getDriver().quit();
					}
					
	    	
	}
	
public void user_selects_All_LOBs(String arg1) {
		waitFor(ProjectVariables.ElementVisibleTimeout);		
		
		try
		{	
				ElementSelectAllLOB.shouldBeVisible();
				if(ElementSelectAllLOB.getAttribute("aria-checked").equalsIgnoreCase("false"))
				{
						ElementSelectAllLOB.click();
				}				
		}		
		
		catch (Exception e) {					
			System.out.println("Exception Message::"+e.getMessage());
			getDriver().quit();
			}
	}
	
public void user_selects_all_Products(String arg1) 
	{
		waitFor(ProjectVariables.ElementVisibleTimeout);	
		
	try{	
				ElementSelectAllProduct.shouldBeVisible();
				if(ElementSelectAllProduct.getAttribute("aria-checked").equalsIgnoreCase("false"))
				{	
						ElementSelectAllProduct.click();
			   }
	}
	catch (Exception e) {					
		System.out.println("Exception Message::"+e.getMessage());
		getDriver().quit();
		}
			
	}

public void user_filters_by_clicking_on_Button(String sButtonName) {
		
	if(sButtonName.equalsIgnoreCase("Apply"))
	{	
		if(objSeleniumUtils.is_WebElement_Displayed(FilterApplyBtn))
		{	
			objSeleniumUtils.highlightElement(FilterApplyBtn);
			objSeleniumUtils.clickGivenXpath(FilterApplyBtn);			
		}   
	}
	else if(sButtonName.equalsIgnoreCase("OpenFilterDrawer"))
	{
		objSeleniumUtils.highlightElement(FilterApplyBtn);
		//clickGivenXpath(OpenFilterDrawerBtn);		
	}
	else if(sButtonName.equalsIgnoreCase("Available Oppurtunities"))
	{
		objSeleniumUtils.highlightElement("//span[contains(text(),'NPP Opportunities')]/span");
		objSeleniumUtils.clickGivenXpath("//span[contains(text(),'NPP Opportunities')]/span");
	}
	}
	
public void userSelectsMultiplePayershorts(String sPayershorts) throws ElementNotFoundException
	{
		
		ProjectVariables.CapturedPayershortList.clear();
		String sPayerShort = "";
		String[] sPayerShorttoSelect = new String[50];
		List<String>   SelectedPayershortsList =  new ArrayList<String>();
		int iArraySize = 0;
		
		objSeleniumUtils.waitForElement(AllPayersSelect, "ShouldbeVisible",6);
		
		     if(sPayershorts.contains(","))
		     {
		    	 sPayerShorttoSelect  = sPayershorts.split(",");	
		    	 iArraySize =sPayerShorttoSelect.length;
		     }		
		     else if(!sPayershorts.contains(",") && (!sPayershorts.equalsIgnoreCase("All")))   //if it is a Single Payershort to Select
		     {
		    	 //To Deselect the all the Checkboxes which are if already selected during previous user Login 	
		    	 if(ElementSelectAllPayer.getAttribute("aria-checked").equalsIgnoreCase("true"))
					{  ElementSelectAllPayer.click();  }	
					else  //if Select all is not checked already,then Check it and Uncheck it ,so that all Individual checkboxes are uncheked
					{
						 ElementSelectAllPayer.click();
						 ElementSelectAllPayer.click();
					}
		    	 
		    	 sPayerShorttoSelect[0]= sPayershorts;
		    	  iArraySize =1;
		    	  Serenity.setSessionVariable("SelectedPayerShort").to(sPayershorts); //Just  additional storage,it is also stored in SelectedPayershortsList in the below code
		     }
		     
		    if (!sPayershorts.equalsIgnoreCase("All")) // if only some PayerShorts (more than 1)need to be selected
		    {
					       try{
							     	//Uncheck "PayerShorts" all selection checkbox so that all checkboxes are deselected		     
							        ElementSelectAllPayer.shouldBeVisible();
									if(ElementSelectAllPayer.getAttribute("aria-checked").equalsIgnoreCase("true"))
									{  ElementSelectAllPayer.click();  }	
									else
									{
										 ElementSelectAllPayer.click();
										 ElementSelectAllPayer.click();
									}
									
									for(int i=0;i<iArraySize;i++)
									{										
										String PayerXpath = StringUtils.replace(Payershort, "PayershortArg", sPayerShorttoSelect[i]);
										objSeleniumUtils.moveToElementAndClick(PayerXpath);								
										SelectedPayershortsList.add(sPayerShorttoSelect[i]);
										ProjectVariables.CapturedPayershortList.add(sPayerShorttoSelect[i]);
									}
					         }
								       catch (Exception e) {					
								   		System.out.println("Exception Message::"+e.getMessage());
								   		getDriver().quit();
								   		}
		    }
		    else  //If we need to select all Payershorts
		    {
		    	ElementSelectAllPayer.shouldBeVisible();
				if(ElementSelectAllPayer.getAttribute("aria-checked").equalsIgnoreCase("false"))
				{
							ElementSelectAllPayer.click();							
				}	
				//Code to Store all Payershorts in ArrayList,**Needs change** as XPATH only showing numbers now instead names
			       String sPayershortVals = 	objSeleniumUtils.Get_Value_By_given_attribute("value", sFilterPayershorts);
			   	    sPayerShorttoSelect =   sPayershortVals.split(",");
			   		for(int k=0;k<sPayerShorttoSelect.length;k++)
					{
			   			SelectedPayershortsList.add(sPayerShorttoSelect[k]);
					}
		    }		      
			   Serenity.setSessionVariable("SelectedPayerShorts").to(SelectedPayershortsList);		
			
	}
	
public void userSelectsMultipleLOBs(String sLOBs) throws ElementNotFoundException
	{
		
	ProjectVariables.CapturedInsuranceList.clear();
		String sLOB = "";
		int  	 iArraySize =0;
		String[] sLOBtoSelect = new String[50];
		List<String>   SelectedLOBsList =  new ArrayList<String>();
		objSeleniumUtils.waitForElement(AllLOBSelect, "ShouldbeVisible",3);
		
		     if( sLOBs.contains(","))
		     {
		    	 sLOBtoSelect  = sLOBs.split(",");
		    	 iArraySize = sLOBtoSelect.length;
		     }	
		     else if(!sLOBs.contains(",") && (!sLOBs.equalsIgnoreCase("All")))   //if it is a Single LOB to Select
		     {
		    	 
		    	 //To Deselect the all the Checkboxes which are if already selected during previous user Login 		    	 
		    	 if(	ElementSelectAllLOB.getAttribute("aria-checked").equalsIgnoreCase("true"))
					{  	ElementSelectAllLOB.click();    }	
					else
					{    
						ElementSelectAllLOB.click();
						ElementSelectAllLOB.click();
					}
		    	 
		    	 sLOBtoSelect[0]= sLOBs;
		    	 iArraySize =1;
		     }
		
		     
		    if (!sLOBs.equalsIgnoreCase("All")) 
		    {
					       try{
					    		 //To Deselect the all the Checkboxes which are if already selected during previous user Login 	
									ElementSelectAllLOB.shouldBeVisible();
									if(	ElementSelectAllLOB.getAttribute("aria-checked").equalsIgnoreCase("true"))
									{
										ElementSelectAllLOB.click();
									}	
									else
									{
										ElementSelectAllLOB.click();
										ElementSelectAllLOB.click();
									}
									
									for(int i=0;i<iArraySize;i++)
									{
										String PayerXpath = StringUtils.replace(LOB, "LOBArg", sLOBtoSelect[i]);
										objSeleniumUtils.clickGivenXpath(PayerXpath);									
										SelectedLOBsList.add(sLOBtoSelect[i]);
										ProjectVariables.CapturedInsuranceList.add(sLOBtoSelect[i]);
									}
					         }
								       catch (Exception e) {					
								   		System.out.println("Exception Message::"+e.getMessage());
								   		getDriver().quit();
								   		}
		    }
		    else  //If we need to select all LOBs
		    {
		    	ElementSelectAllLOB.shouldBeVisible();
				if(ElementSelectAllLOB.getAttribute("aria-checked").equalsIgnoreCase("false"))
				{	
					ElementSelectAllLOB.click();
			   }	
			
			 	//SelectedLOBsList = getWebElementValuesAsList(sFilterLOBs);
				
				//Get all LOBs  names and store it in ArralyList	
			       String sLOBVals = 	objSeleniumUtils.Get_Value_By_given_attribute("value", sFilterLOBs);
			       sLOBtoSelect =   sLOBVals.split(",");
			   		for(int k=0;k<sLOBtoSelect.length;k++)
					{
			   			SelectedLOBsList.add(sLOBtoSelect[k]);
					}
		    }
				Serenity.setSessionVariable("SelectedLOBs").to(SelectedLOBsList);		
		
	}
@Step
public void user_selects_Claimtypes_in_filtersection(String filtersection)
{
    List<String> sClaimTypesList=Arrays.asList(filtersection.split(","));
   
    objSeleniumUtils.clickGivenXpath(StringUtils.replace(oCPWPage.Tag_contains_b, "value", "ClaimType"));
    String sCheckoxStatus=objSeleniumUtils.Get_Value_By_given_attribute("aria-checked", StringUtils.replace(oCPWPage.Tag_contains_b, "value", "ClaimType")+"/ancestor::div[@aria-checked]");
    if(sCheckoxStatus.equalsIgnoreCase("true"))
    {
        objSeleniumUtils.clickGivenXpath(StringUtils.replace(oCPWPage.Tag_contains_b, "value", "ClaimType"));
    }
   
    if(filtersection.equalsIgnoreCase("All")||filtersection.isEmpty())
    {
        objSeleniumUtils.clickGivenXpath(StringUtils.replace(oCPWPage.Tag_contains_b, "value", "ClaimType"));
    }
    else if(!filtersection.equalsIgnoreCase("All"))
    {
        for (int i = 0; i < sClaimTypesList.size(); i++)
        {
            if(sClaimTypesList.get(i).trim().equalsIgnoreCase("O")||sClaimTypesList.get(i).trim().equalsIgnoreCase("I")||sClaimTypesList.get(i).trim().equalsIgnoreCase("S"))
            {
                do{
                    objSeleniumUtils.Doubleclick("//b[contains(text(),'ClaimType')]/ancestor::div[@class='payerlob']//div[contains(@class,'arrow-down')]");
                    objSeleniumUtils.Doubleclick("//b[contains(text(),'ClaimType')]/ancestor::div[@class='payerlob']//div[contains(@class,'arrow-down')]");
                    objSeleniumUtils.Doubleclick("//b[contains(text(),'ClaimType')]/ancestor::div[@class='payerlob']//div[contains(@class,'arrow-down')]");
                    if(sClaimTypesList.get(i).trim().equalsIgnoreCase("S"))
                    	objSeleniumUtils.Doubleclick("//b[contains(text(),'ClaimType')]/ancestor::div[@class='payerlob']//div[contains(@class,'arrow-down')]");
                } while (!objSeleniumUtils.is_WebElement_Displayed(StringUtils.replace(oCPWPage.Span_with_text, "value", sClaimTypesList.get(i).trim())+"/..//div[@checked]"));
                objSeleniumUtils.defaultWait(ProjectVariables.TImeout_10_Seconds);
            }
            objSeleniumUtils.clickGivenXpath("//span[text()='"+sClaimTypesList.get(i).trim()+"']/ancestor::div[@role='option']//div[@checked]");
            objSeleniumUtils.defaultWait(ProjectVariables.TImeout_5_Seconds);
            ProjectVariables.CapturedClaimtypesList.add(sClaimTypesList.get(i));
        }
    }
   
}


	@Step
	public void updateFilterCheckBox(String sSection,String sCheckBox,String sValue, String sOperation) throws InterruptedException{
		/*
		 * sCheckBox Ex: SELECT ALL CHECKBOX , FLITER CHECKBOX BY NUMBER ,GET SELECTED FLITER VALUE BY NUMBER , FILTER CHECKBOX BY PAYER OR LOB
		 */
		GenericUtils oGenericUtils=this.switchToPage(GenericUtils.class);
		String sXSectionCheckbox = getDynamicXpath(sCheckBox,sSection,sValue);
		String sState;
		
		switch (sOperation.toUpperCase()){
		
		case "UPDATE":
			oGenericUtils.clickOnElement(sXSectionCheckbox);
			break;
			
		case "UNSELECT ALL":
			sState = GetCheckBoxState(sSection,sCheckBox,"");
			if (sState.equalsIgnoreCase("checked")){
			oGenericUtils.clickOnElement(sXSectionCheckbox);
			}else{
				oGenericUtils.clickOnElement(sXSectionCheckbox);
				oGenericUtils.clickOnElement(sXSectionCheckbox);
			}
			break;
			
		case "UNCHECK":
			sState = GetCheckBoxState(sSection,sCheckBox,sValue);
			if (sState.equalsIgnoreCase("checked")){
				oGenericUtils.clickOnElement(sXSectionCheckbox);
			}
			
		case "CHECK":
			updateFilterCheckBox(sSection,"SELECT ALL CHECKBOX","","UNSELECT ALL");			
			oGenericUtils.clickOnElement(sXSectionCheckbox);
			break;
			
		case "SELECT ALL":
			sState = GetCheckBoxState(sSection,"SELECT ALL CHECKBOX","");
			if (sState.equalsIgnoreCase("unchecked")){
				oGenericUtils.clickOnElement(sXSectionCheckbox);
			}
			break;
		}
		
		Sleeper.SYSTEM_SLEEPER.sleep(Duration.ofSeconds(2));

	}
	
	
	public String GetCheckBoxState(String sSection,String sCheckbox,String sValue){	
		String sXSectionCheckbox = getDynamicXpath(sCheckbox,sSection,sValue);
		String sCheckboxState = getDriver().findElement(By.xpath(sXSectionCheckbox)).getAttribute("checked");
		if (sCheckboxState!=null){
			if(sCheckboxState.equalsIgnoreCase("true") || sCheckboxState.equalsIgnoreCase("checked")){	
			sCheckboxState = "checked";
			}else{
				sCheckboxState = "unchecked";
			}
		}else{
			sCheckboxState = "unchecked";}
		return sCheckboxState;
	}

   //=========================================================================================================================================================>
	//================================Select Payershort and LOB===============================================================>
		public void selectPayershortsAndLOB(String sInput,String sCheckUnCheck){
			GenericUtils oGenericUtils=this.switchToPage(GenericUtils.class);
			PresentationProfile oPresentationProfile=this.switchToPage(PresentationProfile.class);
			String sRandomPYS_LOB=null;
			
			
			try{
				//Sync point
				Thread.sleep(2000);
				
				//Case Random
				if(sInput.equalsIgnoreCase("PYS_RANDOM")){
					sRandomPYS_LOB=sRdm_PYS;				
				}
				if(sInput.equalsIgnoreCase("LOB_RANDOM")){
					sRandomPYS_LOB=sRdm_LOB;
				}
				switch(sInput.toUpperCase()){
				
				case "PYS_RANDOM":
				case "LOB_RANDOM":
					List<WebElement> sList=getDriver().findElements(By.xpath(sRandomPYS_LOB));
					if(sList.size()>0){
						sInput=sList.get(0).getText();
					}else{
						GenericUtils.Verify("Element not found count returns :="+sList.size(), false);
					}
					break;
				}
				String[] sSplitInput=sInput.split(":");
				for(int i=0;i<sSplitInput.length;i++){
					//If section is Payer short or LOB
					
						String sPYS_LOB_Status="(//div[@class='payerlob']//*[contains(text(),'"+sSplitInput[i]+"')]/..//span)[1]";
						
						String sPYS_LOB_Clk="(//div[@class='payerlob']//*[contains(text(),'"+sSplitInput[i]+"')]/..//span)[1]/..";
						oPresentationProfile.ClickonPayerShortwithScrolldown(sPYS_LOB_Clk);
					
					//Retrieve Check box status
					    //oGenericUtils.isElementExist(sPYS_LOB_Clk);
					    String sChbkStatus=getDriver().findElement(By.xpath(sPYS_LOB_Status)).getAttribute("class");
					//Checked Condition
					    if(!sChbkStatus.contains("checked") && sCheckUnCheck.equalsIgnoreCase("SELECT")){
					    	oGenericUtils.clickButton(By.xpath(sPYS_LOB_Clk));
					    }
					//UnChecked Condition
					    if(sChbkStatus.contains("checked") && sCheckUnCheck.equalsIgnoreCase("DESELECT")){
					    	oGenericUtils.clickButton(By.xpath(sPYS_LOB_Clk));
					    }
				}
				//click on 'Apply' button
				//oGenericUtils.clickButton(By.xpath("//span[contains(text(),'Apply')]"));
				oGenericUtils.clickButton(By.xpath("//button[contains(@class,'applyButton brand-font')]"));
				
			}catch(Exception e){
				GenericUtils.Verify("Object not found , Failed due to :="+e.getMessage(),"FAILED");
			}
		}

	//================================================Assign_MP_Topic_DP==============================================================================================>
		public void Assign_MP_Topic_DP(String sFunctionality,String sMPTopicDPInput,String sPresentation){
			OppurtunityDeck oOppurtunityDeck=this.switchToPage(OppurtunityDeck.class);
			GenericUtils oGenericUtils=this.switchToPage(GenericUtils.class);
			try{
				String sMPTopicDP=null;
				String sAssignMPTopicDP=null;
				//Select Medical/Topic/DP item
				switch(sFunctionality.toUpperCase()){
					
				case "MEDICAL POLICY":
					sMPTopicDP="//label[contains(text(),'"+sMPTopicDPInput+"')]//parent::div//parent::mat-panel-title//i";
					sAssignMPTopicDP="//div[contains(text(),'Assign ALL Opportunities in this Policy to:')]";
					break;
				case "TOPIC":
					sMPTopicDP="//label[contains(text(),'"+sMPTopicDPInput+"')]//parent::div//parent::mat-panel-title//i";
					sAssignMPTopicDP="//div[contains(text(),'Assign ALL Opportunities in this Topic to:')]";
					break;
				case "DP":
				case "DP_ASSIGN_POPUP":	
					sMPTopicDP="//span[contains(text(),'"+sMPTopicDPInput+"')]/../..//button";					
					sAssignMPTopicDP="//div[contains(text(),'Assign selected Opportunities to:')]";
					//Click DP item
					oGenericUtils.clickButton(By.xpath("//label[contains(text(),'"+sMPTopicDPInput+"')]"));
					//Select All checkbox
					oGenericUtils.clickButton(By.xpath(sALL));
					
					break;
				case "HEADER":
					sMPTopicDP = oOppurtunityDeck.HeaderLevelAssignIcon;
					sAssignMPTopicDP = "//div[contains(text(),'Assign All the Opportunities from:')]";
					List<String> DPCards  = objSeleniumUtils.getWebElementValuesAsList(oOppurtunityDeck.sOppDeckAllDPs);
					Serenity.setSessionVariable("ListOfDPs").to(DPCards);
					System.out.println("List of DP's "+Serenity.sessionVariableCalled("ListOfDPs"));
                    System.out.println(Serenity.sessionVariableCalled("sPPName").toString());
					sPresentation = Serenity.sessionVariableCalled("sPPName").toString();
					Serenity.setSessionVariable("sPProfileName").to(sPresentation);
				}
				if(sPresentation.equalsIgnoreCase("")){
					sPresentation=Serenity.sessionVariableCalled("sPProfileName").toString();
				}
				//Click on 'Assign' button for respective section
				oGenericUtils.clickButton(By.xpath(sMPTopicDP));
				//Verify Assign pop up
				oGenericUtils.isElementExist(sAssignMPTopicDP);
				//Condition for DP Assign popup
				if(sFunctionality.equalsIgnoreCase("DP_ASSIGN_POPUP")){
					oGenericUtils.isElementExist(sOppAssign);
					oGenericUtils.isElementExist(sAssnOpp);
					//oGenericUtils.isElementExist("//button[.='Okay'][@role='button'][contains(@class,'disabled')]",20);
					//oGenericUtils.isElementExist("//button[.='Okay'][@role='button'][contains(@class,'disabled')]",10);
					objSeleniumUtils.is_WebElement_Displayed("//div[@class='asign-popover_button']//button[.='Okay'][@role='button'][contains(@class,'disabled')]");
					oGenericUtils.isElementExist(sBtn_Cancel);
				}
				//Select radio button for respective presentation
				//oGenericUtils.clickButton(By.xpath("((//div[contains(text(),'"+sPresentation+"')])[2]/..//div)[1]"));
				//oGenericUtils.clickButton(By.xpath("//div[@class='asign-popover']//div[contains(text(),'"+sPresentation+"')]"));
				oGenericUtils.clickButton(By.xpath("//div[@class='mat-radio-label-content']//span[contains(text(),'"+sPresentation+"')]"));
				//Click 'Okay' button
				oGenericUtils.clickButton(By.xpath(sBtn_Okay));
				//Sync point
				Thread.sleep(2000);
				
				
			}catch(Exception e){
				GenericUtils.Verify("Object not found , Failed due to :="+e.getMessage(),"FAILED");
			}
		}
	//=================================================Validate Assigned presentation=======================================================================================>	
		public void verifyAssignedPresentationProfileDetails(String sMedicalPolicy,String sTopic,String sDPVal,String sPresentation){
			GenericUtils oGenericUtils=this.switchToPage(GenericUtils.class);
			try{
				String sMPTopicArrow=null;
				String sAssignMPTopicDP=null;
				String sGetSavings=null;
				//Verify 'Available DP's' screen
				
				//Capture Raw Savings value
				String sDPFlifferRotate="//label[contains(text(),'"+sDPVal+"')]//ancestor::app-cpd-dp-details-container//li[contains(text(),'"+sPresentation+"')]/ancestor::div[@class='flipper rotate']";
				List<WebElement> sList=getDriver().findElements(By.xpath(sDPFlifferRotate));
				if(sList.size()>0){
					oGenericUtils.clickButton(By.xpath(sDPFlifferRotate));
					sGetSavings=getDriver().findElement(By.xpath("(//label[contains(text(),'"+sDPVal+"')]/../../../../..//li[contains(text(),'"+sPresentation+"')]/../../../../../..//div[@class='flipper']//figure)[2]/div[2]")).getText();
				}else{
					sGetSavings=getDriver().findElement(By.xpath("(//label[contains(text(),'"+sDPVal+"')]/../../../../..//li[contains(text(),'"+sPresentation+"')]/../../../../../..//div[@class='flipper']//figure)[2]/div[2]")).getText();
				}
				//Verify Assign DP value
				String sDPFlipper="//label[contains(text(),'"+sDPVal+"')]//ancestor::app-cpd-dp-details-container//li[contains(text(),'"+sPresentation+"')]/ancestor::div[@class='flipper']";
				List<WebElement> sDPFlipList=getDriver().findElements(By.xpath(sDPFlipper));
				if(sDPFlipList.size()>0){
					oGenericUtils.clickButton(By.xpath(sDPFlipper));
				}
				//Verify Presentation for respective DP
				oGenericUtils.isElementExist("//label[contains(text(),'"+sDPVal+"')]/../../../../..//li[contains(text(),'"+sPresentation+"')]");
				//Select Presentation profile
				oGenericUtils.clickButton(By.xpath("//span[contains(text(),'"+sPresentation+"')]"));
				//Verify Selected Medical Policy
				oGenericUtils.isElementExist("//span[@class='ng-star-inserted pres-tab-active']//span[contains(text(),'"+sPresentation+"')]");
				//Verify 'Opportunities screen'
				oGenericUtils.isElementExist("//p[contains(text(),'Opportunities')]");
				//Expand and verify Medical policy	
				String sMPExpand="//label[contains(text(),'"+sMedicalPolicy+"')]/../../mat-icon[contains(text(),'arrow_right')]/..";
				List<WebElement> sMPList=getDriver().findElements(By.xpath(sMPExpand));
				if(sMPList.size()>0){
					oGenericUtils.clickButton(By.xpath(sMPExpand));
				}
				//Expand and verify Topic 	
				String sTopicExpand="//label[contains(text(),'"+sTopic+"')]/../../mat-icon[contains(text(),'arrow_right')]";
				List<WebElement> sTPList=getDriver().findElements(By.xpath(sTopicExpand));
				if(sTPList.size()>0){
					oGenericUtils.clickButton(By.xpath(sTopicExpand));
				}
				//Verify Assigned DP output and Raw Savings
				oGenericUtils.isElementExist("//label[contains(text(),'"+sDPVal+"')]");
			   //Verify Raw Savings
				oGenericUtils.isElementExist("(//label[contains(text(),'"+sDPVal+"')]/../../../../span)[4]");
				String sGetPSSavings=getDriver().findElement(By.xpath("(//label[contains(text(),'"+sDPVal+"')]/../../../../span)[4]")).getText();
				if(sGetPSSavings.equalsIgnoreCase(sGetSavings)){
					GenericUtils.Verify("Savings sucessfully matched:="+sGetPSSavings, "PASSED");
				}else{
					GenericUtils.Verify("Savings not found","FAILED");
				}
				
			}catch(Exception e){
				GenericUtils.Verify("Object not found , Failed due to :="+e.getMessage(),"FAILED");
			}
		}
	
	public void verifyClients() throws ElementNotFoundException
	{
		
		objSeleniumUtils.clickGivenXpath(sClientDropdown);
		String[]  ClientNames = objSeleniumUtils.get_All_Text_from_Locator(sClientsContainer);
		for(int i=0;i<ClientNames.length;i++){
			String name = ClientNames[i];
			if(Serenity.sessionVariableCalled("ClientNamesList").toString().contains(name))
			{
				GenericUtils.logMessage("Client "+name+" displayed in application is one of total clients from Service");
				Assert.assertTrue("Client "+name+" displayed in application is one of total clients from Service", true);
			}else{
				GenericUtils.logMessage("Client "+name+" displayed in application is not one of total clients from Service");
				Assert.assertTrue("Client "+name+" displayed in application is not one of total clients from Service", false);
			}
		}
		
		objSeleniumUtils.Click_given_Locator("(//div[@id='cdk-overlay-0']//div//div//mat-option//span)[1]");
      	objSeleniumUtils.waitForContentLoad();
		
		
		
	}
	
	public boolean validateScrollBarsofPayerShort(){
		return objSeleniumUtils.is_WebElement_Displayed(scrollPayerShort);
	}
	
	public boolean validateScrollBarsofLOB(){
		return objSeleniumUtils.is_WebElement_Displayed(scrollLOB);
	}
	
	public boolean user_should_view_client_dropdown() {
		return ElementClientDropDownArrow.isCurrentlyVisible();
	}
	
	public void verifySortingOderOfClients(){
		
		objSeleniumUtils.clickGivenXpath(sClientDropdown);
		String[]  ClientNames = objSeleniumUtils.get_All_Text_from_Locator(sClientsContainer);
		for(int i=0;i<ClientNames.length;i++)
		{
			ClientNames[i] = ClientNames[i].toLowerCase();
		}
		
		List<String> ClientNameslist = Arrays.asList(ClientNames);
		System.out.println(ClientNameslist);
		List<String> ClientList =ClientNameslist.stream().sorted().collect(Collectors.toList());
		System.out.println(ClientList);
		if(ClientNameslist.equals(ClientList)){
			Assert.assertTrue("Client names displayed are sorted in alphabetical order", true);
		}else{
			Assert.assertTrue("Client names displayed are not sorted in alphabetical order", false);
		}
		
		objSeleniumUtils.Click_given_Locator(firstClientInList);
		
		
		
	}
	
	public static String getDynamicXpathForMedicalPolicy(String sXpath, Object sVal1){

		String sFormattedXpath = null;

		switch (sXpath.toUpperCase()){
		
		case "SELECT ALL CHECKBOX":
			sFormattedXpath = "//span[contains(text(),'Medical Policy / Topic')]//parent::div//parent::div//parent::div/preceding-sibling::div/div/div";
			break;	
			
		case "FLITER CHECKBOX BY NUMBER":
			sFormattedXpath = "//div[contains(@id,'contenttablejqxWidget')]//div["+sVal1+"][@role='row']//div[@class='aw-checkbox-cell']//div//div";
			break;			

		case "GET SELECTED FLITER VALUE BY NUMBER":
			sFormattedXpath  = "//div[contains(@id,'contenttablejqxWidget')]//div["+sVal1+"][@role='row']//div[@class='aw-checkbox-cell']//following-sibling::div/div";			
			break;		
			
		}	
		return sFormattedXpath;
	}
	
	public String GetCheckBoxStateOfMedicalPolicy(String sCheckbox,String sValue){	
		String sXSectionCheckbox = getDynamicXpathForMedicalPolicy(sCheckbox,sValue);
		
		String sCheckboxState = getDriver().findElement(By.xpath(sXSectionCheckbox)).getAttribute("checked");
		if (sCheckboxState!=null){
			if(sCheckboxState.equalsIgnoreCase("true") || sCheckboxState.equalsIgnoreCase("checked")){	
			sCheckboxState = "checked";
			}else{
				sCheckboxState = "unchecked";
			}
		}else{
			sCheckboxState = "unchecked";}
		return sCheckboxState;
	}
	
	@Step
	public void updateFilterCheckBoxOfMedicalPolicy(String sCheckBox,String sValue, String sOperation) throws InterruptedException{
		/*
		 * sCheckBox Ex: SELECT ALL CHECKBOX , FLITER CHECKBOX BY NUMBER ,GET SELECTED FLITER VALUE BY NUMBER 
		 */
		GenericUtils oGenericUtils=this.switchToPage(GenericUtils.class);
		String sXSectionCheckbox = getDynamicXpathForMedicalPolicy(sCheckBox,sValue);
		String sState;
		
		switch (sOperation.toUpperCase()){
		
		case "UPDATE":
			oGenericUtils.clickOnElement(sXSectionCheckbox);
			break;
			
		case "UNSELECT ALL":
			sState = GetCheckBoxStateOfMedicalPolicy(sCheckBox,"");
			if (sState.equalsIgnoreCase("checked")){
			oGenericUtils.clickOnElement(sXSectionCheckbox);
			}else{
				oGenericUtils.clickOnElement(sXSectionCheckbox);
				oGenericUtils.clickOnElement(sXSectionCheckbox);
			}
			break;
			
		case "UNCHECK":
			sState = GetCheckBoxStateOfMedicalPolicy(sCheckBox,sValue);
			if (sState.equalsIgnoreCase("checked")){
				oGenericUtils.clickOnElement(sXSectionCheckbox);
			}
			
		case "CHECK":
			updateFilterCheckBoxOfMedicalPolicy("SELECT ALL CHECKBOX","","UNSELECT ALL");			
			oGenericUtils.clickOnElement(sXSectionCheckbox);
			break;
			
		case "SELECT ALL":
			sState = GetCheckBoxStateOfMedicalPolicy("SELECT ALL CHECKBOX","");
			if (sState.equalsIgnoreCase("unchecked")){
				oGenericUtils.clickOnElement(sXSectionCheckbox);
			}
			break;
		}
		
		Sleeper.SYSTEM_SLEEPER.sleep(Duration.ofSeconds(2));
	}
	
	public void user_selects_all_MedicalPolicies() 
	{	
		String sAttribVal = "";
		try{

			if (objSeleniumUtils.is_WebElement_Displayed(sMedPoliciesAllCheckBox))
			{	    	  

				sAttribVal = objSeleniumUtils.Get_Value_By_given_attribute("aria-checked",sMedPoliciesAllCheckBox+"/parent::div");

				do
				{  
					objSeleniumUtils.Click_given_Locator(sMedPoliciesAllCheckBox);
					sAttribVal = objSeleniumUtils.Get_Value_By_given_attribute("aria-checked",sMedPoliciesAllCheckBox+"/parent::div");
				}while (sAttribVal.equalsIgnoreCase("false")|| sAttribVal.equalsIgnoreCase("undefined"));	  		

			}  

		}
		catch (Exception e) {					
			System.out.println("Exception Message::"+e.getMessage());
			getDriver().quit();
		}

 }
	
	//==============================Select Medical Policy/Topic ==========================================================================================>
	public void selectMedicalPolicyAndTopic(String sFunctionality,String sMPTopicInput,String sCheckUnCheck){
		GenericUtils oGenericUtils=this.switchToPage(GenericUtils.class);
		try{
			String sMPTopic_Chbk_Status=null;
			String sMPTopic_Chbk_Clk=null;
			String MedPolicy = "";
			String Topic = "";
			
			//Click on 'Reset' button
			objSeleniumUtils.highlightElement(sReset);
			oGenericUtils.clickButton(By.xpath(sReset));
			
			if(!sMPTopicInput.equalsIgnoreCase("DB"))
			{
								String[] sSplitInput=sMPTopicInput.split(":");
								for(int i=0;i<sSplitInput.length;i++){
									
									//Sync point
									Thread.sleep(2000);
									//Enter Search item
									oGenericUtils.setValue(By.xpath(sEdt_MP_Topic), sSplitInput[i]);
									//Click on 'Search Item'
									oGenericUtils.clickButton(By.xpath(sBtn_Search));
									//Select Medical Policy/Topic
									//If section is Payer short or LOB
									switch(sFunctionality.toUpperCase()){
										
									case "MEDICAL POLICY":
										sMPTopic_Chbk_Status="//div[contains(text(),'"+sSplitInput[i]+"')]//parent::div//parent::div//span";
										 sMPTopic_Chbk_Clk="//div[contains(text(),'"+sSplitInput[i]+"')]/parent::div/parent::div//div[@id='allUnchecked']";
										 Serenity.setSessionVariable("Medicalpolicy").to(sSplitInput[i]);
										break;
									case "TOPIC":
										sMPTopic_Chbk_Status="//div[contains(text(),'"+sSplitInput[i]+"')]//parent::div//parent::div//span";
										 sMPTopic_Chbk_Clk="//div[contains(text(),'"+sSplitInput[i]+"')]//parent::div//parent::div//span/..";
										break;
									case "MP_TOPIC_ALL":
										sMPTopic_Chbk_Status="(//div[@role='columnheader']//span)[1]";
										 sMPTopic_Chbk_Clk="(//div[@role='columnheader']//span)[1]/..";
										break;
									}
								}		
			 }			
				else if(sMPTopicInput.equalsIgnoreCase("DB"))
				{	
					MedPolicy  =   Serenity.sessionVariableCalled("Medicalpolicy");				
					Topic          =   Serenity.sessionVariableCalled("Topic");
				
						//Sync point
						Thread.sleep(2000);
						//Enter Search item
						oGenericUtils.setValue(By.xpath(sEdt_MP_Topic), Topic);
						//Click on 'Search Item'
						oGenericUtils.clickButton(By.xpath(sBtn_Search));	
						
						switch(sFunctionality.toUpperCase()){
						
						case "MEDICAL POLICY":
							sMPTopic_Chbk_Status="//div[contains(text(),'"+MedPolicy+"')]//parent::div//parent::div//span";
							 sMPTopic_Chbk_Clk="//div[contains(text(),'"+MedPolicy+"')]/parent::div/parent::div//div[@id='allUnchecked']";
							break;
						case "TOPIC":
							sMPTopic_Chbk_Status="//div[contains(text(),'"+Topic+"')]//parent::div//parent::div//span";
							 sMPTopic_Chbk_Clk="//div[contains(text(),'"+Topic+"')]//parent::div//parent::div//span/..";
							break;
						case "MP_TOPIC_ALL":
							sMPTopic_Chbk_Status="(//div[@role='columnheader']//span)[1]";
							 sMPTopic_Chbk_Clk="(//div[@role='columnheader']//span)[1]/..";
							break;
						}						
				}		
						
				//Retrieve Check box status
				    String sChbkStatus=getDriver().findElement(By.xpath(sMPTopic_Chbk_Status)).getAttribute("class");
				//Checked Condition
				    if(!sChbkStatus.contains("checked") && sCheckUnCheck.equalsIgnoreCase("SELECT") && sChbkStatus.isEmpty()){
				    	oGenericUtils.clickButton(By.xpath(sMPTopic_Chbk_Clk));
				    }
				//UnChecked Condition
				    if(sChbkStatus.contains("checked") && sCheckUnCheck.equalsIgnoreCase("DESELECT")){
				    	oGenericUtils.clickButton(By.xpath(sMPTopic_Chbk_Clk));
				    }	
				    
				    if(sFunctionality.equalsIgnoreCase("MEDICAL POLICY") && !sMPTopicInput.equals("")){
				    	
				    	 String sMedPolicyRawSavings =   objSeleniumUtils.get_TextFrom_Locator(StringUtils.replace(MedicalpolicyRawSavings, "sval", Serenity.sessionVariableCalled("Medicalpolicy")));
						 Serenity.setSessionVariable("MedPolicyRawSavingsUI").to(sMedPolicyRawSavings);
				    }
				    
				   
				    if(sFunctionality.equalsIgnoreCase("TOPIC") && !sMPTopicInput.equals("")){
				    	 String sMedPolicyRawSavings =   objSeleniumUtils.get_TextFrom_Locator(StringUtils.replace(MedicalpolicyRawSavings, "sval", Serenity.sessionVariableCalled("Medicalpolicy")));
						 Serenity.setSessionVariable("MedPolicyRawSavingsUI").to(sMedPolicyRawSavings);
				    	
				    	 String sTopicRawSavings =   objSeleniumUtils.get_TextFrom_Locator(StringUtils.replace(MedicalpolicyRawSavings, "sval", Serenity.sessionVariableCalled("Topic")));
						 Serenity.setSessionVariable("TopicRawSavingsUI").to(sTopicRawSavings);
				    }
						
						//click on 'Apply' button
						oGenericUtils.clickButton(By.xpath(sBtn_Apply));
			
			
		}catch(Exception e){
			GenericUtils.Verify("Object not found , Failed due to :="+e.getMessage(),"FAILED");
		}
	}

	public void userSelectsPPSUnder(String selectionType, String sValue, String sHeader) throws IOException, ParseException {
		
		MyGETRequest(sHeader,Serenity.sessionVariableCalled("SelectClientName"));
	
		String sFitlerValue=null;
		
		List<String> sClaimTypesList=Arrays.asList(sValue.split(","));
	    
		List<String> sSelectionTypeList=Arrays.asList(selectionType.split(";"));
	    
		String Xpath;

	    for (int j = 0; j < sSelectionTypeList.size(); j++) {
			
		switch(sSelectionTypeList.get(j)){
		case "Payer Short":
		case "LOB":
		case "Claim Type":			
			//Sys
			for (int i = 0; i < sClaimTypesList.size(); i++)
			{
				if (sValue.equalsIgnoreCase("RandomData")) {
					
					System.out.println(Serenity.sessionVariableCalled(sHeader+"-"+sSelectionTypeList.get(i)).toString());
					
					sFitlerValue=Serenity.sessionVariableCalled(sHeader+"-"+sSelectionTypeList.get(i)).toString();
					
					//objSeleniumUtils.Enter_given_Text_Element("//b[text()='"+sHeader+"']/../..//b[text()=' Payer Shorts']/../..//input", sFitlerValue);
					
					GenericUtils.Verify(selectionType+" from service request for "+sHeader+": "+sFitlerValue, true);
					
					Xpath="//b[text()='"+sHeader+"']/..//following-sibling::div//span[text()='"+sFitlerValue.trim()+"']/ancestor::div[@role='option']//div[@checked]";
											
				}else {
					
					Xpath="//b[text()='"+sHeader+"']/..//following-sibling::div//span[text()='"+sClaimTypesList.get(i).trim()+"']/ancestor::div[@role='option']//div[@checked]";
				}
				
				if(!objSeleniumUtils.is_WebElement_Displayed(Xpath))
		            {
		                do{
		                    objSeleniumUtils.Doubleclick("//b[text()='"+sHeader+"']/..//following-sibling::div/div[@class='payerlob']//b[contains(text(),'"+selectionType+"')]/../..//div[contains(@class,'arrow-down')]");
		                    objSeleniumUtils.Doubleclick("//b[text()='"+sHeader+"']/..//following-sibling::div/div[@class='payerlob']//b[contains(text(),'"+selectionType+"')]/../..//div[contains(@class,'arrow-down')]");
		                    objSeleniumUtils.Doubleclick("//b[text()='"+sHeader+"']/..//following-sibling::div/div[@class='payerlob']//b[contains(text(),'"+selectionType+"')]/../..//div[contains(@class,'arrow-down')]");
		                } while (!objSeleniumUtils.is_WebElement_Displayed(Xpath));
		                objSeleniumUtils.defaultWait(ProjectVariables.TImeout_10_Seconds);
		            }
			
		            objSeleniumUtils.clickGivenXpath(Xpath);
		            
		            objSeleniumUtils.defaultWait(ProjectVariables.TImeout_3_Seconds);	
		            
		            objSeleniumUtils.defaultWait(ProjectVariables.TImeout_5_Seconds);	        
		    }
			break;
		case "Decision Type":
			for (int i = 0; i < sClaimTypesList.size(); i++)
			{
				Xpath="//b[text()='"+selectionType+"']/..//following-sibling::div//div[contains(text(),'All')]";
				objSeleniumUtils.clickGivenXpath(Xpath);
				if(sClaimTypesList.get(i).trim().equals("All"))
					Xpath="//b[text()='"+selectionType+"']/..//following-sibling::div//div[contains(text(),'"+sClaimTypesList.get(i).trim()+"')]";
				else
					Xpath="//b[text()='"+selectionType+"']/..//following-sibling::div//span[text()='"+sClaimTypesList.get(i).trim()+"']/ancestor::div[@role='option']//div[@checked]";
				objSeleniumUtils.clickGivenXpath(Xpath);
	            objSeleniumUtils.defaultWait(ProjectVariables.TImeout_5_Seconds);					        
		    }
			
			break;
		default:Assert.assertTrue("Invalid case selection",false);
		
		}
	}	

	}
	
	public static void MyGETRequest(String sHeader,String Client) throws IOException, ParseException {
	    
		//String sClientkey=CPWPage.RetrieveTheClientkeyfromgivenClientthroughservice(Client);
		
		String sClientkey="43";
		
	    URL urlForGetRequest = new URL("https://cpd-gateway-qa1.cotiviti.com/mdm-data/v1/policyset/"+sClientkey);
	    String readLine = null;
	    HttpURLConnection conection = (HttpURLConnection) urlForGetRequest.openConnection();
	    conection.setRequestMethod("GET");
	    //conection.setRequestProperty("userId", "a1bcdef"); // set userId its a sample here
	    int responseCode = conection.getResponseCode();
	    if (responseCode == HttpURLConnection.HTTP_OK) {
	        BufferedReader in = new BufferedReader(
	            new InputStreamReader(conection.getInputStream()));
	        StringBuffer response = new StringBuffer();
	        while ((readLine = in .readLine()) != null) {
	            response.append(readLine);
	        } in .close();
	        // print result
	       // System.out.println("JSON String Result " + response.toString());
	        
	        String ExactData=response.toString().replaceAll("\"","");
	        
	        String colData=StringUtils.substringBetween(ExactData, "result:[","error:false");
	            
	       	String[] jsonData = colData.split("prod_not_test_10");
	       	
	       //	System.out.println("dsfaasdf"+jsonData);
	     
	       	ArrayList<String> sPayerShot=new ArrayList<String>();
	       	
	       	ArrayList<String> sPayerID=new ArrayList<String>();
	       	
	       	ArrayList<String> sInsuranceKey=new ArrayList<String>();
	       	
	       	ArrayList<String> sClaimType=new ArrayList<String>();
	       	
	       	for(int i=0;i<jsonData.length;i++){
	       		
	       		sInsuranceKey.remove(null);
	       		
	       		sInsuranceKey.add(StringUtils.substringBetween(jsonData[i], "insurance_key:",","));
	       		
	       		sPayerShot.remove(null);
	       		
	       		sPayerShot.add(StringUtils.substringBetween(jsonData[i], "payer_short:",","));
	       		
	       		sPayerID.remove(null);
	       		
	       		sPayerID.add(StringUtils.substringBetween(jsonData[i], "payer_id:",","));
	       		
	       		sClaimType.remove(null);
	       		
	       		sClaimType.add(StringUtils.substringBetween(jsonData[i], "claim_type:","}"));
	       		
	       	}
	       	
	       	Serenity.setSessionVariable("iRandomVal").to(GenericUtils.generateRandomNumberforGivenRange(sPayerShot.size()));
	       	
	       	Serenity.setSessionVariable(sHeader+"-Payer Short").to(sPayerShot.get(Serenity.sessionVariableCalled("iRandomVal")));
	       
	       	Serenity.setSessionVariable(sHeader+"-PayerID").to(sPayerID.get(Serenity.sessionVariableCalled("iRandomVal")));
	       	
	       	Serenity.setSessionVariable(sHeader+"-Cliam Type").to(sClaimType.get(Serenity.sessionVariableCalled("iRandomVal")));
	       	
	       	Serenity.setSessionVariable(sHeader+"-LOB").to(sInsuranceKey.get(Serenity.sessionVariableCalled("iRandomVal")));
	           
	        //GetAndPost.POSTRequest(response.toString());
	    } else {
	        System.out.println("GET NOT WORKED");
	    }
	}

	public void validate_Decision_count_with_Mongo(String arg1) {
	
		long sPayerID=Long.parseLong(Serenity.sessionVariableCalled("Source-PayerID"));
		
		//long sPayerID=Long.parseLong("1058");
		
		//String sClaimType=Serenity.sessionVariableCalled("SourceCliamType");
		
		String sClaimType="A";
		
		Bson MatchFilter = new BsonDocument();
		
		MatchFilter = Filters.and(Filters.eq("_id.payerKey", sPayerID),Filters.in("_id.insuranceKey", 7l),Filters.in("_id.claimType", sClaimType),Filters.eq("decStatusDesc", arg1));
		
		long iMongoCount=MongoDBUtils.retrieveAllDocuments("cdm", "latestDecision", MatchFilter);
		
		objSeleniumUtils.defaultWait(ProjectVariables.TImeout_10_Seconds);
		
		long iUICount=objSeleniumUtils.get_Matching_WebElement_count("//i[contains(@class,'fa fa-arrow-circle-o-right assignIcon')]");
		
		GenericUtils.Verify("Ui decision count for" +arg1+":"+iUICount+" Mongo decision count for" +arg1+":"+iMongoCount, iUICount==iMongoCount);
		
	    
	}
	
	
}