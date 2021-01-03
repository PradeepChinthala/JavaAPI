package project.pageobjects;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.json.simple.parser.ParseException;
import org.junit.Assert;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Action;
import org.openqa.selenium.interactions.Actions;

import io.restassured.response.Response;
import net.serenitybdd.core.Serenity;
import net.serenitybdd.core.annotations.findby.FindBy;
import net.serenitybdd.core.pages.PageObject;
import net.serenitybdd.core.pages.WebElementFacade;
import project.exceptions.ElementNotFoundException;
import project.utilities.GenericUtils;
import project.utilities.MongoDBUtils;
import project.utilities.RestServiceUtils;
import project.utilities.SeleniumUtils;
import project.variables.JsonBody;
import project.variables.MonGoDBQueries;
import project.variables.ProjectVariables;

public class PresentationProfile extends PageObject{

	SeleniumUtils refSeleniumUtils;
	GenericUtils oGenericUtils;
	RestServiceUtils oRestServiceUtils = new RestServiceUtils();
	PresentationDeck oPresentationDeck=new PresentationDeck();
	CPWPage oCPWPage =new CPWPage();
	FilterDrawer oFilterDrawer = new FilterDrawer();
	PresentationProfileValidations oPresentationProfileValidations = new PresentationProfileValidations();
	
	@FindBy(xpath = "//button[@id='jqxWidget1b01b4f7188c']")
	WebElementFacade btnAddPresentationProfile;

	@FindBy(xpath = "//input[@id='nameInput']")
	WebElementFacade txtPresentationName;

	@FindBy(xpath = "//input[@id='mat-input-0']")
	WebElementFacade dateTargetDate;

	@FindBy(xpath = "//*[@id='mat-checkbox-1']/label/div")
	WebElementFacade chkboxPayerShortSelectAll;

	@FindBy(xpath = "//*[@id='mat-checkbox-2']/label/div")
	WebElementFacade chkboxLOBSelectAll;

	@FindBy(xpath = "//div[ contains( text(),'Outpatient')]")
	WebElementFacade chkboxSelectOutPatient;

	@FindBy(xpath = "//*[@id='jqxWidgetfcb0c975c836']/div[2]/div/div/div[6]/mat-selection-list/mat-list-option[1]/div/mat-pseudo-checkbox")
	WebElementFacade chckboxPriorityThresholdHigh;

	@FindBy(xpath = "//*[@id='jqxWidgetaa347e0881ca']/div/textarea")
	WebElementFacade txtPresntationProfileNotes;

	@FindBy(xpath = "//jqxbutton[@id='popupContainer']/button[contains(text(),'Cancel')]")
	WebElementFacade btnCancelAddPresentationProfile;

	@FindBy(xpath = "//button[contains(text(),'OK')]")
	WebElementFacade btnOKAddPresentationProfile;

	@FindBy(xpath = "//*[@id='mat-dialog-1']/app-cpd-dialog/div[1]/p")
	WebElementFacade popupOKAddPresentationProfile;

	@FindBy(xpath = "//div[@role = 'dialog']//div[@class='jqx-window-content jqx-widget-content jqx-rc-b']")
	WebElementFacade popupCancelAddPresentationProfile;

	@FindBy(xpath = "//div[@role = 'dialog']//div[@class='jqx-window-content jqx-widget-content jqx-rc-b']//button[contains(text(),'Yes')]")
	WebElementFacade btnYesCancelAddPresentationProfile;

	@FindBy(xpath = "//div[@role = 'dialog']//div[@class='jqx-window-content jqx-widget-content jqx-rc-b']//button[contains(text(),'No')]")
	WebElementFacade btnNoCancelAddPresentationProfile;

	//	@FindBy(xpath = "//div[@id='addPresentation']/jqxbutton/button")
	//	WebElementFacade AddPresentationBtn;

	@FindBy(xpath = "//input[@id='nameInput']")
	WebElementFacade PresentationNameInput;

	@FindBy(xpath = "//footer[@id='footer']//button[@role='button' and text()='Okay']")
	WebElementFacade PresentationConfirmBtn;

	@FindBy(xpath = "//footer[@id='footer']//button[@role='button' and text()='Cancel']")
	WebElementFacade PresentationCancelBtn;

	@FindBy(xpath= "//mat-dialog-container[contains(@class,'mat-dialog-container')]//div//div[text()='Success']//parent::h4/following-sibling::div/div")
	WebElementFacade PresentationSuccessMsg;

	public String  PresentationSuccessMsgOKBtn =  "//mat-dialog-container//app-cpd-dialog//button";

	public String PresentationsLabel = "//label/b[text()='Presentations']";

	//public String sPresNameTitle =  "//div[@class='pres-deck-container-class']//div/label[@class='name' and text()=' PresNameArg ']";

	public String sPresNameTitle = "//div[@id='addButton']/following-sibling::mat-tab-group//div[@class='mat-tab-label-content']//span/span[text()='PresNameArg ']";

	public String sAllPresTitles =  "//div[@class='pres-deck-container-class']//div/label[@class='name']";

	public String  AllDPCountTexts    =  "//div[@class='pres-deck-container-class']//div[@class='mat-expansion-panel-body']//label[@id='dpLabel']";

	public String AddPresentationBtn = "//div[@id='addButton']/button";

	public String sPresentationStyle = "//mat-expansion-panel[contains(@class,'ng-star-inserted')]//div//label[contains(text(),'sval')]//parent::div//parent::mat-panel-title//parent::span//parent::mat-expansion-panel-header//parent::mat-expansion-panel";

	public static String sApproveNotes = "//label[text() ='Notes:']/following-sibling::jqxtextarea/descendant::textarea";

	public String sModificationNotes = "//label[contains(text(),'Modifications') or contains(text(),'Notes')]/following-sibling::jqxtextarea/descendant::textarea";

	public String  AllPresentationNames  =  "(//mat-tab-group//span[@class='pres_pro_name'])";
	
	public String  PresentationTabName  =  "//mat-tab-group//span[contains(@class,'pres_pro_name') and contains(text(),'PresNameArg')]";

//	public String sPresentationAssignmentOKBtn=   "//div[contains(@class,'jqx-popover')]/div[starts-with(text(),'Assign')]//parent::div//following-sibling::div[@class='jqx-popover-content']//button[@role='button' and text()='Okay']";
//
//	public String sPresentationAssignmentCancelBtn =  "//div[contains(@class,'jqx-popover')]/div[starts-with(text(),'Assign')]//parent::div//following-sibling::div[@class='jqx-popover-content']//button[@role='button' and text()='Okay']" ;
	
	public String sPresentationAssignmentOKBtn=   "//div[@class='jqx-popover right jqx-widget jqx-widget-content jqx-rc-all jqx-popover-arrow-right jqx-selectable']//div[contains(text(),'Assign')]//parent::div//following-sibling::div[@class='jqx-popover-content']//button[@role='button' and text()='Okay']";

	public String sPresentationAssignmentCancelBtn =  "//div[@class='jqx-popover right jqx-widget jqx-widget-content jqx-rc-all jqx-popover-arrow-right jqx-selectable']//div[contains(text(),'Assign')]//parent::div//following-sibling::div[@class='jqx-popover-content']//button[@role='button' and text()='Cancel']" ;
  
    public String RightArrow = "//div[@class = 'mat-tab-header-pagination mat-tab-header-pagination-after mat-elevation-z4 mat-ripple']/div";
	  
	public String PresentationScreen = " //mat-drawer-content[@class = 'mat-drawer-content mat-drawer-content']";
	
	public String PresentationDeleteIcon  =   "//span[contains(text(),'PresNameArg')]/../following-sibling::div/descendant::button";
	
	public String sApprovewithModNotes = "//label[text()='Other']/ancestor::mat-expansion-panel-header/following-sibling::div//textarea";
	
	public String DPCard_Lnk = "//div[@class='pres_dp_main']//label[text()='DP DPkeyArg']";
	
	public String DP_LOBGrid = "(//div[@class='available filtered']//div)[2]";
	
	public String DP_LOBAvailable = "//div[@class='available filtered']//div[@class='lob_tiles_ft'][1]//following-sibling::div[1]";
	
	public String LOBInspectorTitle = "//h4//label[text()='DP DPkeyArg: LOB Inspector/Decision History']";
	
	public String LOBInspectorPayersList = "//ul[@class='payer_lob_mappedData_grid']//span[@class='payer_block']";
	
	public String LOBInspectorLOBTitle = "//span[@class='payer_lob_block']//span[text()=' Insurance ']";
	
	public String LOBInspectorCloseIcon = "//i[@aria-label='close']";
	
	public String LOBInspectorLOBHeader = "//ul[@class='payer_lob_mappedData_grid_header']//li[LOBHeader]/span";
	
	public String LOBInspectorPayerValue = "//ul[@class='payer_lob_mappedData_grid']//span[text()=' Payer ']//parent::li//ul[@class='payer_lob_mappedData_grid_main']//i";
	
	public String Label_With_Contains="//label[contains(text(),'sValue')]";
	
	public String viewDropdown="//mat-select[@aria-label='Select View']//span[normalize-space(text())]";
	
	public String oppurtunityLabel="//div[contains(@class,'label')]//span[contains(text(),'value')]";

	public String presTopicediticon="//mat-icon[contains(@class,'edit')]";

	public String crossOverHeaders="//b[text()='arg']/..//following-sibling::div//b";
	
	public String crossOverOptions="(//b[text()='arg']/..//following-sibling::div//div[@checked='checked' or @checked='true'])";
	
	public String changeOpportunityGridCell="//div[contains(@id,'contenttable')]//div[contains(@class,'value')]";
	
	public String dpHeaderCheckbox="//div[@role='columnheader']//div[contains(@class,'checkbox')][contains(@id,'jqxWidget')]";

	public String decisionGridHeader="((//div[contains(@class,'tabwid')])[1]//th[not(contains(@class,'select'))])";
	
	public String decisionGrid="(//div[contains(@class,'cpd-opp-deck')]//app-cpd-med-policy)[1]";
	
	public String unassignedDP="(//mat-card-content//ul[not(li)]/../../../../..//preceding-sibling::mat-card-header//label[normalize-space(text())][not(contains(@class,'flipLabel'))])";
	
	public String sPPSSelection="(//span[normalize-space(text())]/following-sibling::span[contains(@class,'checkmark')])";
	
	public String allPPS="(//div[contains(@class,'dp_view_main')]//span[@class='checkmark'])[1]";
	
	public String DPLevelAssign="//span[contains(@class,'openAssignPopup')]/button";
	
	public String headerLevelAssign="//span[@class='assignPopover']/i";
	
	public String topicLevelAssign="(//span[contains(@class,'topic')]/i)";
	
	public String profileRadioButton="//div[@class='asign-popover']//span[contains(text(),'value')]/..//preceding-sibling::div";
	
	public String captureProileRadioButton="//div[@class='asign-popover']//div[contains(text(),'value')]/..//preceding-sibling::div";
	
	public String dpFlipper="//label[text()='value']/../../../../following-sibling::mat-card-content";
	
	public String medicalPolicy="//div[@id='assignContainer']//label[contains(text(),'Medical Policy: value')]";
	
	public String topic="(//div[@id='assignContainer']//label[contains(text(),'Topic:')]/../..//mat-icon)";
	
	public String DP="(//label[@class='dpIdLabel'])";
	
	public String assignPopupOkay="(//div[contains(@class,'asign-popover_button')]//button[text()='Okay'])";
	
	public String profileMouseHover="//div[text()='NPP Opportunities']/following-sibling::div[contains(text(),'value')]";
	
	public String profileMouseHoverChangeOpp="//div[text()='Change Opportunities']/following-sibling::div[contains(text(),'value')]";
	
	public String changeOpportunitiesDP="//div[@title='dp']/preceding-sibling::div[not(contains(@class,'collapse'))]/div";
	
	public String ChangeOppAssignIcon = "//span[@class='assignPopover']";
	
	public String ReadyForPresentation="//mat-checkbox[@mattooltip='Ready For Presentation']";

	//********************************************************************************************************************************
	
	public static String getDynamicXpath(String sXpath,Object sVal){

		String sFormattedXpath = null;

		switch (sXpath.toUpperCase()){

		case "FROM DATE":
	        sFormattedXpath  = "(//label[contains(text(),'"+sVal+"')]/following-sibling::mat-form-field/descendant::input[@data-placeholder])[1]";
	        break;    
	    case "TO DATE":
	        sFormattedXpath  = "(//label[contains(text(),'"+sVal+"')]/following-sibling::mat-form-field/descendant::input[@data-placeholder])[2]";
	        break;
		case "DATE":
			sFormattedXpath  = "//input[@data-placeholder='Date']";
			break;
			default:
				Assert.assertTrue("case not found==>"+sXpath, false);
			break;
		}	
		return sFormattedXpath;
	}

	public static String getDynamicXpath(String sXpath,Object sVal,Object sVal1){

		String sFormattedXpath = null;

		switch (sXpath.toUpperCase()){

		case "PAYER/LOB MEDICARE":			
			sFormattedXpath  = "//tbody[@role ='rowgroup']/tr["+sVal+"]/td//span[contains(@class,'text-left d-inline')]";
			break;

		}	
		return sFormattedXpath;
	}

	public String sTotalSavingsTexts = "//mat-tooltip-component//div[contains(text(), 'sval')]";

	public boolean createPresentationProfile() throws InterruptedException
	{		
		try{				
			refSeleniumUtils.highlightElement(AddPresentationBtn);
			refSeleniumUtils.Click_given_Locator(AddPresentationBtn);			

			String sRandNo = String.valueOf(GenericUtils.generateRandomNumberforGivenRange(10000));
			String sPresentationName = "TestPres"+sRandNo;
			PresentationNameInput.sendKeys(sPresentationName);
			refSeleniumUtils.highlightElement(PresentationConfirmBtn);
			PresentationConfirmBtn.click();
			refSeleniumUtils.defaultWait(ProjectVariables.TImeout_5_Seconds);

			refSeleniumUtils.highlightElement(StringUtils.replace(sPresNameTitle, "PresNameArg" , sPresentationName));
			Assert.assertTrue("New presentation success message is verified successfully", refSeleniumUtils.is_WebElement_Displayed(StringUtils.replace(sPresNameTitle, "PresNameArg" , sPresentationName)));
			Serenity.setSessionVariable("PresentationName").to(sPresentationName);

			/*	String sMsg = PresentationSuccessMsg.getText();		
				if(sMsg.equalsIgnoreCase( "The new presentation is created successfully."))
				{			
				 refSeleniumUtils.Click_given_Xpath(PresentationSuccessMsgOKBtn); 
				 Serenity.setSessionVariable("PresentationName").to(sPresentationName);
				 refSeleniumUtils.defaultWait(ProjectVariables.TImeout_5_Seconds);			
				 }*/

		}	catch (Exception e) {

			System.out.println("Exception Message::"+e.getMessage());
			getDriver().quit();
		}
		return true;	
	}

	public boolean createPresentations(String sPresCount,String sValues) throws InterruptedException
	{

		int iPCCount   = Integer.parseInt(sPresCount);
		String sPresentationName  = "";
		List<String> PresNamesList =  new ArrayList<String>();

		for(int p=1;p<=iPCCount;p++)
		{	
			try{									
				refSeleniumUtils.highlightElement(AddPresentationBtn);
				refSeleniumUtils.Click_given_Locator(AddPresentationBtn);									

				String sRandNo = String.valueOf(GenericUtils.generateRandomNumberforGivenRange(10000));
				sPresentationName = "TestPres"+sRandNo;
				PresentationNameInput.sendKeys(sPresentationName);
				refSeleniumUtils.highlightElement(PresentationConfirmBtn);
				PresentationConfirmBtn.click();

				// refSeleniumUtils.highlightElement(StringUtils.replace(sPresNameTitle, "PresNameArg" , sPresentationName));
				// Assert.assertTrue("New presentation success message is verified successfully", refSeleniumUtils.is_WebElement_Displayed(StringUtils.replace(sPresNameTitle, "PresNameArg" , sPresentationName)));
				//Store the Presentation Names	
				if(iPCCount == 1)
				{
					Serenity.setSessionVariable("PresentationName").to(sPresentationName);
					PresNamesList.add(sPresentationName);
					Serenity.setSessionVariable("PresentationNamesList").to(PresNamesList);
				}
				else if(iPCCount>1)			
				{
					PresNamesList.add(sPresentationName);		
					Serenity.setSessionVariable("PresentationNamesList").to(PresNamesList);
				}									
			}	catch (Exception e) {										
				System.out.println("Exception Message::"+e.getMessage());
				getDriver().quit();
			}
		}		

		return true;	
	}

	public boolean createPresentationProfile(String sLoginUserName) throws InterruptedException
	{Map<String, String> UserBasedPresProfile = new HashMap<String, String>();
	try{				
		refSeleniumUtils.highlightElement(AddPresentationBtn);
		refSeleniumUtils.Click_given_Locator(AddPresentationBtn);			

		String sRandNo = String.valueOf(GenericUtils.generateRandomNumberforGivenRange(10000));
		String sPresentationName = "AutoTest"+sRandNo;
		PresentationNameInput.sendKeys(sPresentationName);
		refSeleniumUtils.highlightElement(PresentationConfirmBtn);
		PresentationConfirmBtn.click();
		refSeleniumUtils.defaultWait(ProjectVariables.TImeout_5_Seconds);

		refSeleniumUtils.highlightElement(StringUtils.replace(sPresNameTitle, "PresNameArg" , sPresentationName));
		Assert.assertTrue("New presentation success message is verified successfully", refSeleniumUtils.is_WebElement_Displayed(StringUtils.replace(sPresNameTitle, "PresNameArg" , sPresentationName)));
		Serenity.setSessionVariable("PresentationName").to(sPresentationName);
		Serenity.setSessionVariable(sLoginUserName+"PresMap").to(sPresentationName);			

	}	catch (Exception e) {
		Assert.assertTrue(e.getMessage() ,false);
		System.out.println("Exception Message::"+e.getMessage());
		getDriver().quit();
	}
	return true;	
	}

	public boolean clickPresentationProfile() throws InterruptedException
	{		
		CPWPage oCPWPage=this.switchToPage(CPWPage.class);

	String sXPresentation = "//span[contains(text(),'"+Serenity.sessionVariableCalled("PresentationName")+"')]";
		String sRightArrow = "//div[@class = 'mat-tab-header-pagination mat-tab-header-pagination-after mat-elevation-z4 mat-ripple']/div";
		String sPresentationScreen = "//mat-drawer-content[@class = 'mat-drawer-content']";
		boolean sVal = false;

		do {

				sVal = oGenericUtils.isElementExist(sXPresentation,1);
				if (sVal)
				{			
	              	refSeleniumUtils.defaultWait(ProjectVariables.TImeout_2_Seconds);
					oGenericUtils.clickOnElement(sXPresentation);
	                 refSeleniumUtils.defaultWait(ProjectVariables.TImeout_5_Seconds);
					oGenericUtils.isElementExist(sPresentationScreen);
					return sVal;
				}else
				{
					oGenericUtils.clickOnElement(sRightArrow);
				}

			}while(getDriver().findElements(By.xpath("//div[@class = 'mat-tab-header-pagination mat-tab-header-pagination-after mat-elevation-z4 mat-ripple']/div")).size()>0);

		
		String CheckboxStatus=refSeleniumUtils.Get_Value_By_given_attribute("aria-checked", StringUtils.replace(oCPWPage.Pres_Payer_LOB_All_Chkbox, "filter", "Payers"));
		if(CheckboxStatus!=null)
		{
		//To select the all PPs in Presentation Deck in filterdrawer sections
		oCPWPage.selectTheAllPPSinPresentationDeck();
		}
		else
		{
			System.out.println("No DP is assigned to given presentation==>"+Serenity.sessionVariableCalled("PresentationName"));
		}
		return sVal;
	}

	public boolean clickGivenPresentationProfile(String sPresentation) throws InterruptedException
	{				
		Serenity.setSessionVariable("PresentationName").to(sPresentation);
		return clickPresentationProfile();		
	}

	public boolean clickOtherProfile() throws ElementNotFoundException
	{		

		List<WebElement>  PresProfileNames;

		// String sPresTitle =   StringUtils.replace(sPresNameTitle,"val",Serenity.sessionVariableCalled("PresentationName"));

		PresProfileNames = refSeleniumUtils.getElementsList("XPATH", sAllPresTitles);

		if( PresProfileNames.size()<2)
		{    	 
			Assert.assertTrue("There is only one PresentationProfile in the PresentationContainer,so canoot proceed further to click on the other Profile",false);	
		}

		try{		
			String OtherPresName = PresProfileNames.get(2).getText().trim();
			refSeleniumUtils.highlightElement(PresProfileNames.get(2));			       		     			 
			refSeleniumUtils.clickGivenWebElement(PresProfileNames.get(2)); //Click on the Presentation Profile Name
			Serenity.setSessionVariable("PresentationName").to(OtherPresName);		        
		}
		catch (Exception e) {			
			System.out.println("Exception Message::"+e.getMessage());
			getDriver().quit();
		}

		return true;
	}

	public void getOppurtunityHierarchyView() {

		String sXpath = " //i[contains(text(), 'expand_less')]";
		String sOpprtyView = "//i[contains(text(), 'expand_more')]";

		boolean sView = refSeleniumUtils.is_WebElement_Displayed(sXpath);

		if (!sView){			
			oGenericUtils.clickOnElement(sOpprtyView);	
		}

	}

	public void ExpandAllinHierarchyView() {

		String sXpath  = "//span[@class = 'mat-button-wrapper']/mat-icon[contains(text(), 'add')]";

		do {			
			oGenericUtils.ClickAllElements(sXpath);
		}while(getDriver().findElements(By.xpath(sXpath)).size()>0);


	}

	public void unselectAllinOppView() {

		String sXpath = "(//mat-checkbox[@class = 'mat-checkbox mat-accent'])[1]";
		String sAllCheckbox = "//span[contains(text() , 'ALL')]/../..";

		boolean blnCheckbxState = oGenericUtils.isElementExist(sXpath,1);

		if (!blnCheckbxState){			
			oGenericUtils.clickOnElement(sAllCheckbox);	
		}

	}

	public String selectDPsatGivenLevelinoppHierarchyView(String slevel) {

		unselectAllinOppView();

		String sPolicyLevel = "(//mat-tree[@class = 'cdk-drop-list mat-tree']/mat-tree-node[@aria-level = '3'])[1]/preceding-sibling::mat-tree-node[2]/descendant::div[@class = 'mat-checkbox-inner-container']";
		String sTopicLevel = "(//mat-tree[@class = 'cdk-drop-list mat-tree']/mat-tree-node[@aria-level = '3'])[1]/preceding-sibling::mat-tree-node[1]/descendant::div[@class = 'mat-checkbox-inner-container']";
		String sFirstDP = "((//mat-tree[@class = 'cdk-drop-list mat-tree']/mat-tree-node[@aria-level = '3'])/descendant::div[@class = 'mat-checkbox-inner-container'])[1]";

		String sFirstPolicyValue = "(//mat-tree[@class = 'cdk-drop-list mat-tree']/mat-tree-node[@aria-level = '3'])[1]/preceding-sibling::mat-tree-node[2]/descendant::div[@class = 'mat-checkbox-inner-container']//following-sibling::span";
		String sFirstTopicValue = "(//mat-tree[@class = 'cdk-drop-list mat-tree']/mat-tree-node[@aria-level = '3'])[1]/preceding-sibling::mat-tree-node[1]/descendant::div[@class = 'mat-checkbox-inner-container']//following-sibling::span";
		String sFirstDPValue = "((//mat-tree[@class = 'cdk-drop-list mat-tree']/mat-tree-node[@aria-level = '3'])/descendant::div[@class = 'mat-checkbox-inner-container'])[1]//following-sibling::span";

		String sAssignIcon = "//mat-panel-title[contains(text(),'Opportunities')]/button";
		String sValue = null;
		switch  (slevel.toUpperCase()){

		case "MEDICAL POLICY":			
			oGenericUtils.clickOnElement(sPolicyLevel);
			break;

		case "TOPIC":			
			oGenericUtils.clickOnElement(sTopicLevel);
			break;

		case "DP":
			oGenericUtils.clickOnElement(sFirstDP);
			break;

		case "ALL":
			break;

		default:
			Assert.assertTrue("Invalid selection", false);

		}

		String sMedicalPolicy = getDriver().findElement(By.xpath(sFirstPolicyValue)).getText();
		Serenity.setSessionVariable("MedicalPolicy").to(sMedicalPolicy);
		String sTopic = getDriver().findElement(By.xpath(sFirstTopicValue)).getText();
		Serenity.setSessionVariable("Topic").to(sTopic);
		String sDPValue = getDriver().findElement(By.xpath(sFirstDPValue)).getText();
		Serenity.setSessionVariable("CapturedDPKey").to(sDPValue);

		oGenericUtils.clickOnElement(sAssignIcon);
		oGenericUtils.isElementExist("label", "Capture Decision");

		return sValue;

	}

	public void ClickonDecision(String sOperation)
	{
		String sXpath = "//div[contains(text(),'"+sOperation+"')]//preceding-sibling::div";

		oGenericUtils.clickOnElement(sXpath);

	}

	
	public String captureDecision(String sOperation){CPWPage oCPWPage=this.switchToPage(CPWPage.class);
	
	String sFirstAssignmentChkbox = "(//mat-radio-group[@role = 'radiogroup'])[2]//label/div";
	String sFirstAssignmentValue = "(//mat-radio-group[@role = 'radiogroup'])[2]//label/div[2]";
	String sValue = null;
	switch  (sOperation.toUpperCase()){
	case "APPROVE TEST ONLY":
	case "APPROVE":	
	case "APPROVE CHANGE":
		if(sOperation.equalsIgnoreCase("APPROVE CHANGE"))
			ClickonDecision("Approve Change");
		else
			ClickonDecision("Approve");
		boolean blnEntered =enterCaptureDetails(ProjectVariables.DOSFROM, ProjectVariables.DOSTO, ProjectVariables.PROCESSIONINGFROM, ProjectVariables.PROCESSIONINGTO);
		GenericUtils.Verify("Entered Date Values Successfully", blnEntered);
		oGenericUtils.setValue(By.xpath(sApproveNotes),"Test Approve Automation Notes");
		Serenity.setSessionVariable("Note").to("Test Approve Automation Notes");
		
		break;
	case "APPROVE CHANGE WITH MOD":
	case "APPROVE WITH MOD TEST ONLY":
	case "APPROVE WITH MOD":
		if(sOperation.equalsIgnoreCase("Approve Change with Mod"))
			ClickonDecision("Approve with Mod");
		else
			ClickonDecision("Approve with Mod");
		boolean blnEnteredMod = enterCaptureDetails(ProjectVariables.DOSFROM, ProjectVariables.DOSTO, ProjectVariables.PROCESSIONINGFROM, ProjectVariables.PROCESSIONINGTO);
		GenericUtils.Verify("Entered Date Values Successfully", blnEnteredMod);
		oGenericUtils.clickOnElement(StringUtils.replace(oCPWPage.labelwithtext, "svalue", "Other"));
		refSeleniumUtils.defaultWait(ProjectVariables.TImeout_3_Seconds);
		oGenericUtils.setValue(By.xpath(sApprovewithModNotes),"Test Approve with Modification Notes");
		Serenity.setSessionVariable("Note").to("Test Approve with Modification Notes");
		oGenericUtils.clickOnElement(StringUtils.replace(oCPWPage.labelwithtext, "svalue", "Capture Decision"));
		refSeleniumUtils.defaultWait(ProjectVariables.TImeout_2_Seconds);
		break;
	case "RE-ASSIGN":
		ClickonDecision("Re-Assign");
		oGenericUtils.clickOnElement(sFirstAssignmentChkbox);
		sValue = getDriver().findElement(By.xpath(sFirstAssignmentValue)).getText();
		Serenity.setSessionVariable("AssignedPresentation").to(sValue);
		
		break;
	case "RE-ASSIGN_READY":
		ClickonDecision("Re-Assign");
		String sOldPres=Serenity.sessionVariableCalled("sFristPres").toString().trim();
		String sPres_ReadyPres="//div[contains(text(),'"+sOldPres+"')]/span[contains(text(),'Ready For Presentation')]";
		oGenericUtils.isElementExist(sPres_ReadyPres);
		oGenericUtils.clickOnElementContainsText("button", "Cancel");
		oGenericUtils.clickOnElementContainsText("button", "Cancel");
		sValue = "true";
		return sValue;	
	case "UNASSIGN":
		ClickonDecision("Unassign");
		oGenericUtils.clickOnElement("button", "Capture");
		boolean blnVal = oGenericUtils.IsElementExistWithContains("div","Unassigning an opportunity with a decision captured will delete the decision. Would you like to continue? ");
		GenericUtils.Verify("Unassigning an opportunity with a decision captured will delete the decision. Would you like to continue? , message should be displayed", blnVal);						
		break;
	case "REJECT":
		ClickonDecision("Reject");
		//Enter the required data for reject decision in capture decision window
		validateTheReasonsDropdownandEntergivenReasons(ProjectVariables.RejectReasons);
		oGenericUtils.setValue(By.xpath(sModificationNotes),"Test Reject Notes");
		Serenity.setSessionVariable("Note").to("Test Reject Notes");
		break;
	case "DEFER":
		ClickonDecision("Defer");
		oGenericUtils.setValue(By.xpath(getDynamicXpath("DATE","")),ProjectVariables.PROCESSIONINGTO);
		oGenericUtils.setValue(By.xpath(sModificationNotes),"Test Defer Notes");
		Serenity.setSessionVariable("Note").to("Test Defer Notes");
		break;
	case "FOLLOW UP":
		ClickonDecision("Follow up");
		oGenericUtils.setValue(By.xpath(getDynamicXpath("DATE","")),ProjectVariables.PROCESSIONINGTO);
		oGenericUtils.clickOnElementContainsText("div", "Cotiviti");
		oGenericUtils.setValue(By.xpath(sModificationNotes),"Test Follow up Notes");
		Serenity.setSessionVariable("Note").to("Test Follow up Notes");
		Serenity.setSessionVariable("Responsibleparty").to("Cotiviti");
		break;
					
		
	default:
		Assert.assertTrue("case not found===>"+sOperation, false);
	break;
	}
	
	if(sOperation.toUpperCase().equalsIgnoreCase("APPROVE TEST ONLY")||sOperation.toUpperCase().equalsIgnoreCase("APPROVE WITH MOD TEST ONLY"))
	{
		oGenericUtils.clickGivenXpath(StringUtils.replace(oCPWPage.Span_contains_text, "value", "Test Only"));
		Serenity.setSessionVariable("OpportunityStatus").to("0");
	}
	else if(sOperation.toUpperCase().equalsIgnoreCase("APPROVE")||sOperation.toUpperCase().equalsIgnoreCase("APPROVE WITH MOD"))
	{
		Serenity.setSessionVariable("OpportunityStatus").to("-1");
	}
	oGenericUtils.clickOnElementContainsText("button", "Capture");
	oGenericUtils.defaultWait(ProjectVariables.TImeout_5_Seconds);
	if(!sOperation.toUpperCase().equalsIgnoreCase("UNASSIGN")&&!sOperation.toUpperCase().equalsIgnoreCase("RE-ASSIGN"))
	{
		if(oGenericUtils.is_WebElement_Displayed(StringUtils.replace(oCPWPage.CaptureConfirmationButton, "value", "Yes")))
			oGenericUtils.clickOnElement(StringUtils.replace(oCPWPage.CaptureConfirmationButton, "value", "Yes"));	
	}
	oGenericUtils.defaultWait(ProjectVariables.TImeout_10_Seconds);

	refSeleniumUtils.waitForContentLoad();
	
	
	return sValue;
	}

	public boolean validateTheReasonsDropdownandEntergivenReasons(String rejectReasons) 
	{
		CPWPage oCPWPage=this.switchToPage(CPWPage.class);
		List<String> ExpectedReasonsList=Arrays.asList(ProjectVariables.RejectReasonsList.split(";"));
		
		int ReasonsSize=0;
		String Reason=null;
		
		oGenericUtils.clickGivenXpath("//mat-label[contains(text(),'Reasons')]/../../..//mat-select");
		refSeleniumUtils.defaultWait(ProjectVariables.TImeout_2_Seconds);
		ReasonsSize=refSeleniumUtils.get_Matching_WebElement_count(StringUtils.replace(oCPWPage.Span_with_Class, "value", "mat-option-text"));
		
		for (int i = 1; i <=ReasonsSize; i++) 
		{
			Reason=refSeleniumUtils.get_TextFrom_Locator(StringUtils.replace(oCPWPage.Span_with_Class, "value", "mat-option-text")+"["+i+"]");
			if(ExpectedReasonsList.contains(Reason.trim()))
			{
				GenericUtils.Verify("'"+Reason.trim()+"' is displayed in the Reject Reasons Dropdown of capture decsions window as expected", true);
			}
			else
			{
				GenericUtils.Verify("'"+Reason.trim()+"' is not displayed in the Reject Reasons Dropdown of capture decsions window as expected", false);
			}
		}
		
		oGenericUtils.clickOnElementContainsText("span", rejectReasons);
		Serenity.setSessionVariable("Reason").to(rejectReasons);
		return true;
	}


	public boolean assigneeProfileShouldNotbeDisplayedunderReassigneeList(String sAssigneePrf) {

		String sXList = "//mat-radio-group[@class = 'decision-radio-group mat-radio-group ng-pristine ng-valid ng-touched' ]/mat-radio-button/descendant::div[text()]";
		List<WebElement> sList = getDriver().findElements(By.xpath(sXList));

		for (int i=0;i<sList.size();i++){
			String sAssignee = sList.get(i).getText().trim();
			if (sAssignee.equalsIgnoreCase(sAssigneePrf)){
				return false;
			}

		}
		return true;
	}

	public void selectDPsatGivenLevelinPayerLOBView(String slevel) {

		getOppurtunityHierarchyView();

		String sHeaderLevelCheckbox = "//th[@role= 'columnheader']/descendant::span";
		String sHeaderLevelIcon = "//button[@class = 'fa fa-arrow-circle-o-right captureIcon toolkit-button-icon']";
		String sFirstDPValue = "//ul[@class= 'dp_view_lt_block']/descendant::span";

		String sDPValue = getDriver().findElement(By.xpath(sFirstDPValue)).getText();
		Serenity.setSessionVariable("CapturedDPKey").to(sDPValue);

		String sFirstTopicValue = "//label[contains(text() ,'"+sDPValue+"')]/ancestor::mat-tree-node/preceding-sibling::mat-tree-node[@role ='group'][1]/descendant::span[2]";
		String sTopic = getDriver().findElement(By.xpath(sFirstTopicValue)).getText();
		Serenity.setSessionVariable("Topic").to(sTopic.trim());

		String sFirstMedicalPolicyValue = "//label[contains(text() ,'"+sDPValue+"')]/ancestor::mat-tree-node/preceding-sibling::mat-tree-node[@role ='group'][2]/descendant::span[2]";
		String sMedicalPolicy = getDriver().findElement(By.xpath(sFirstMedicalPolicyValue)).getText();
		Serenity.setSessionVariable("MedicalPolicy").to(sMedicalPolicy.trim());

		switch  (slevel.toUpperCase()){

		case "HEADER":			
			oGenericUtils.clickOnElement(sHeaderLevelCheckbox);
			oGenericUtils.clickOnElement(sHeaderLevelIcon);

			break;

		default:
			Assert.assertTrue("Invalid selection", false);

		}

		oGenericUtils.isElementExist("label", "Capture Decision");	
	}

	public void verifyCancelFunctionality() {

		oGenericUtils.clickOnElementContainsText("button", "Cancel");	
		String sXpath = "//div[contains(text() , 'Are you sure you want to cancel? Changes will not be saved')]";

		boolean blnExist = oGenericUtils.isElementExist(sXpath);
		GenericUtils.Verify("Are you sure you want to cancel? Changes will not be saved , message should be displayed", blnExist);

		oGenericUtils.clickOnElementContainsText("button", "Go Back");	


	}

	public boolean editFunctionality() throws InterruptedException {

		String sXpath = "//button[@class = 'toolkit-button-icon edit_image']";
		oGenericUtils.clickOnElement(sXpath);	
		String sEditprofile = "//input[@placeholder= 'Presentation Name']";
		String sEditPrfName = "AutoTestEdit"+GenericUtils.GetRandomNumber();
		oGenericUtils.setValue(By.xpath(sEditprofile), sEditPrfName);
		verifyCancelFunctionality();
		oGenericUtils.clickOnElementContainsText("button", "Okay");
		Thread.sleep(2000);
		String sUpdatedProfileName = "//span[contains(text() ,'"+sEditPrfName+"')]";
		return oGenericUtils.isElementExist(sUpdatedProfileName);		

	}

	public boolean ClickonPayerShortwithScrolldown(String sXPayer) throws InterruptedException
	{		

		String sFirstElement  = "(//div[@class='payerlob']/descendant::div[@role ='option'])[1]";
		WebElement sElement = getDriver().findElement(By.xpath(sFirstElement));

		Actions builder = new Actions(getDriver());
		Action mouseOverHome = builder
				.click(sElement).click()
				.build();

		mouseOverHome.perform();

		Action KeyboardDown = builder
				.sendKeys(Keys.ARROW_DOWN).build();

		boolean sVal = false;
		int iTimes = 0;
		do {

			iTimes = iTimes+1;
			setImplicitTimeout(500, ChronoUnit.MILLIS);            
			sVal = getDriver().findElements(By.xpath(sXPayer)).size()>0;
			if (sVal){
				Thread.sleep(2000);		
				return sVal;
			}else{KeyboardDown.perform();}

		}while((sVal ==false)|| iTimes!=15);		
		resetImplicitTimeout();
		return sVal;
	}

	public void deleteGivenPresentation(String sPresenation,String sMessage) throws InterruptedException {

		boolean blnClick = clickGivenPresentationProfile(sPresenation);
		GenericUtils.Verify("Click on Presentation "+sPresenation, blnClick);

		String XdeleteIcon = "//span[contains(text(), '"+sPresenation+"')]/../following-sibling::div/descendant::button";
		oGenericUtils.clickOnElement(XdeleteIcon);	
		boolean blnExist = oGenericUtils.IsElementExistWithContains("*", sMessage);
		GenericUtils.Verify(sMessage+" should be displayed", blnExist);
		oGenericUtils.clickOnElement("span", "Yes");	

	}

	public void navigateToNextDP() {

		bringtobaseState();

		List<WebElement> sDPElements = getDriver().findElements(By.xpath("//a[contains(text(), 'DP')]"));

		for(int i=0; i<sDPElements.size();i++){
			String sDP = sDPElements.get(i).getText().trim();
			String sDPView = "//span[contains(text(), '"+sDP+"')]";
			boolean blnNavigate = oGenericUtils.isElementExist(sDPView);				
			GenericUtils.Verify("Verify DP"+sDP+" Display in Decision view", blnNavigate);		
			boolean blnNextEnabled = getDriver().findElement(By.xpath("//button[text() = 'Next']")).isEnabled();
			if (blnNextEnabled){
				oGenericUtils.clickOnElementContainsText("button", "Next");
			}
		}

	}

	public void navigateToPreviousDP() {

		bringtobaseState();

		do {
			boolean blnNextEnabled = getDriver().findElement(By.xpath("//button[text() = 'Next']")).isEnabled();
			if (blnNextEnabled){
				oGenericUtils.clickOnElementContainsText("button", "Next");
			}		
		}while(getDriver().findElement(By.xpath("//button[text() = 'Next']")).isEnabled());


		List<WebElement> sDPElements = getDriver().findElements(By.xpath("//label[contains(@class,'DpClick')]"));

		for(int i=sDPElements.size()-1;i>=0;i--){
			String sDP = sDPElements.get(i).getText().trim();
			String sDPView = "//span[contains(text(), '"+sDP+"')]";
			boolean blnNavigate = oGenericUtils.isElementExist(sDPView);				
			GenericUtils.Verify("Verify DP"+sDP+" Display in Decision view", blnNavigate);		
			boolean blnNextEnabled = getDriver().findElement(By.xpath("//button[text() = 'Previous']")).isEnabled();
			if (blnNextEnabled){
				oGenericUtils.clickOnElementContainsText("button", "Previous");
			}
		}

	}

	public void abilityToViewDPinDecisionView() {

		List<WebElement> sDPElements = getDriver().findElements(By.xpath("//label[contains(@class,'DpClick')]"));

		for(int i=0; i<sDPElements.size();i++){
			String sDP = sDPElements.get(i).getText().trim();
			oGenericUtils.clickOn(sDPElements.get(i));
			String sDPView = "//span[contains(text(), '"+sDP+"')]";
			boolean blnNavigate = oGenericUtils.isElementExist(sDPView);				
			GenericUtils.Verify("Verify DP"+sDP+" Display in Decision view", blnNavigate);
		}

	}

	public void bringtobaseState(){

		List<WebElement> sDPElements = getDriver().findElements(By.xpath("//label[contains(text(), 'DP')]"));
		oGenericUtils.clickOn(sDPElements.get(0));
	}

	public boolean enterCaptureDetails(String sDOSFrom,String sDOSTo, String sProcessingFrom, String sProcessingTo){


		String xModFromDate = getDynamicXpath("FROM DATE","Date of Service");
		boolean blnVal1 = oGenericUtils.setValue(By.xpath(xModFromDate),sDOSFrom);

		String xModToDate = getDynamicXpath("TO DATE","Date of Service");
		boolean blnVal2 = oGenericUtils.setValue(By.xpath(xModToDate),sDOSTo);

		String xProcessingModFromDate = getDynamicXpath("FROM DATE","Processing date");
		boolean blnVal3 = oGenericUtils.setValue(By.xpath(xProcessingModFromDate),sProcessingFrom);

		String xProcessingModToDate = getDynamicXpath("TO DATE","Processing date");
		boolean blnVal4 = oGenericUtils.setValue(By.xpath(xProcessingModToDate),sProcessingTo);

		return blnVal1&&blnVal2&&blnVal3&&blnVal4;


	}

	public void verifyGridUpdatedwithStatus(String sStatus, String sDP) {
		String sDecision=null;
		
		if(sStatus.contains("Test Only"))
		{
			sDecision=StringUtils.substringBefore(sStatus, "Test Only").trim();
		}
		else if(sStatus.contains("Follow up"))
		{
			sDecision="Follow up";
		}
		else
		{
			sDecision=sStatus;
		}
		boolean blnVerify = false;
		List<WebElement> oRows = getDriver().findElements(By.xpath("//tbody[@role ='rowgroup']/tr"));
		List<WebElement> oColumns = getDriver().findElements(By.xpath("//tbody[@role ='rowgroup']/tr[1]/td"));

		for(int i=1; i<=oRows.size();i++){

			for (int j=2;j<=oColumns.size();j++){
				String sxMediCareCol = getDynamicXpath("PAYER/LOB MEDICARE",i,j);
				String sMedicareStatus = getDriver().findElement(By.xpath(sxMediCareCol)).getText().trim();

				if (!sMedicareStatus.isEmpty()){
					blnVerify = sDecision.equalsIgnoreCase(sMedicareStatus) || sMedicareStatus.contains("Invalid");
					GenericUtils.Verify("Grid Values Expected as "+sDecision+" or Invalid and Actual is "+sMedicareStatus+",for the DP==>"+sDP, blnVerify);
				}
				else
				{
					GenericUtils.Verify("Grid Values Expected as "+sDecision+" and Actual is 'Empty',for the DP==>"+sDP, blnVerify);
				}

			}

		}

		GenericUtils.Verify("All Grid Values displayed", blnVerify);

	}

	public void verifyPayerLobGridPopUp(String sStatus, String dOSFROM, String dOSTO, String pROCESSIONINGFROM,
			String pROCESSIONINGTO,String DPKey) {CPWPage oCPWPage=this.switchToPage(CPWPage.class);
			String sOpportunitystatus=null;
			String sNotes=null;
			String sSelectedReasons=null;
			String sResponsibleParty=null;
			switch(sStatus)
			{
			case "Approve with Mod Test Only":
			case "Approve Test Only":
			case "Approve":
			case "Approve with Mod":
				String xModFromDate = getDynamicXpath("FROM DATE","Date of Service");
				String sPopUpDosFrom = getDriver().findElement(By.xpath(xModFromDate)).getAttribute("data-placeholder").trim();
				//sPopUpDosFrom=sPopUpDosFrom.replaceFirst("0", "");
				GenericUtils.Verify("Expected Date of Service From in DB is "+dOSFROM+" and Actual is "+sPopUpDosFrom,dOSFROM.equalsIgnoreCase(sPopUpDosFrom));

				String xModToDate = getDynamicXpath("TO DATE","Date of Service");
				String sPopUpDosTo = getDriver().findElement(By.xpath(xModToDate)).getAttribute("data-placeholder").trim();
				GenericUtils.Verify("Expected Date of Service From in DB is "+dOSTO+" and Actual is "+sPopUpDosTo,dOSTO.equalsIgnoreCase(sPopUpDosTo));

				String xProcessingModFromDate = getDynamicXpath("FROM DATE","Processing date");
				String sPopUpProcessingFrom = getDriver().findElement(By.xpath(xProcessingModFromDate)).getAttribute("data-placeholder").trim();
				GenericUtils.Verify("Expected Date of Service From in DB is "+pROCESSIONINGFROM+" and Actual is "+sPopUpProcessingFrom,pROCESSIONINGFROM.equalsIgnoreCase(sPopUpProcessingFrom));

				String xProcessingModToDate = getDynamicXpath("TO DATE","Processing date");
				String sPopUpProcessingTo = getDriver().findElement(By.xpath(xProcessingModToDate)).getAttribute("data-placeholder").trim();
				GenericUtils.Verify("Expected Date of Service From in DB is "+pROCESSIONINGTO+" and Actual is "+sPopUpProcessingTo,pROCESSIONINGTO.equalsIgnoreCase(sPopUpProcessingTo));

				if(refSeleniumUtils.is_WebElement_Displayed(StringUtils.replace(oCPWPage.OpportunityStatus, "status", "false")))
				{
					sOpportunitystatus="-1";
				}
				else 
				{
					sOpportunitystatus="0";
				}
				
				GenericUtils.Verify("PayerLOB grid Expected Opportunity status in DB is '"+Serenity.sessionVariableCalled("OpportunityStatus")+"' and Actual is '"+sOpportunitystatus+"',For the Dp=>"+DPKey,sOpportunitystatus.equalsIgnoreCase(Serenity.sessionVariableCalled("OpportunityStatus")));
				
				
			break;
			case "Reject":
				sSelectedReasons=refSeleniumUtils.get_TextFrom_Locator(oCPWPage.sSelectedReasons);
				GenericUtils.Verify("Expected Reason in PayerLOB grid is '"+Serenity.sessionVariableCalled("Reason")+"',Actual is '"+sSelectedReasons+"',for the DP=>"+DPKey, sSelectedReasons.equalsIgnoreCase(Serenity.sessionVariableCalled("Reason")));
			break;
			case "Defer":
				//entered date is not visible in html page
			break;
			case "Follow up":
				//entered date is not visible in html page
				 sResponsibleParty = refSeleniumUtils.Get_Value_By_given_attribute("class", StringUtils.replace(oCPWPage.sMatradiobutton, "text", Serenity.sessionVariableCalled("Responsibleparty")));
				 GenericUtils.Verify("Expected ResponsibleParty '"+Serenity.sessionVariableCalled("Responsibleparty")+"' is not selected ,For the Dp=>"+DPKey,sResponsibleParty.contains("checked"));

				break;
			default:
				Assert.assertTrue("case not found===>"+sStatus, false);
			break;
				
			}
			
			if(!sStatus.equalsIgnoreCase("Approve with Mod"))
			{
				sNotes=refSeleniumUtils.Get_Value_By_given_attribute("data-value", "//div[@data-value]");
				
				GenericUtils.Verify("PayerLOB grid Expected Note/Modifications in DB is "+Serenity.sessionVariableCalled("Note")+" and Actual is '"+sNotes+"',For the Dp=>"+DPKey,sNotes.equalsIgnoreCase(Serenity.sessionVariableCalled("Note")));
				
			}
		}
  
  	
	//========================================= Chaitanya =======================================================================//
	
	public String createPresentationThroughService(String sUser, String sClient, String sPayershorts, String sLobs,
			String sProduct, String sPriority) throws ParseException, IOException {
		String sClientKey=null;
		if(Serenity.sessionVariableCalled("clientkey")==null)
		{
			sClientKey=CPWPage.RetrieveTheClientkeyfromgivenClientthroughservice(sClient);
			
			Serenity.setSessionVariable("clientkey").to(sClientKey);
		}
		else
		{
			sClientKey=Serenity.sessionVariableCalled("clientkey");
		}
		HashMap<String,Object> sFields = new HashMap<String,Object>();
		sFields.put("profileName", "AutoService"+GenericUtils.GetRandomNumber());
		sFields.put("targetDate", "2019-09-12");
		sFields.put("createdUser", sUser);
		sFields.put("clientKey", sClientKey);
		sFields.put("lobs", sLobs.split(";"));
		sFields.put("products", sProduct.split(";"));
		sFields.put("priorities",sPriority.split(";"));
		sFields.put("payerShorts",sPayershorts.split(";"));
		sFields.put("note", "Notes"+GenericUtils.GetRandomNumber());

		String sBody = JsonBody.getRequestPayload("createPresentationProfile", sFields);
		String sEndpoint = ProjectVariables.sServices.get("createPresentationProfile").get("EndpointURL");
		io.restassured.path.json.JsonPath oReponse = oRestServiceUtils.getResponseBodyforPostServiceWithSessionID(sEndpoint, sBody);
		String sProfileID = oReponse.get("id");		
		String sProfileName = oReponse.get("profileName");

		return sProfileID+"-"+sProfileName;
		
		
		
	}
	
	public String createChangePresentationThroughService(String sUser, String sClient, String sPayershorts, String sLobs,
			String sProduct, String sPriority) throws ParseException, IOException {
		
		HashMap<String,Object> sFields = new HashMap<String,Object>();
		sFields.put("profileName", "AutoService"+GenericUtils.GetRandomNumber());
		sFields.put("targetDate", "2021-09-12");
		sFields.put("createdUser", sUser);
		sFields.put("clientKey", Serenity.sessionVariableCalled("clientkey").toString());
		sFields.put("lobs", sLobs.split(";"));
		sFields.put("products", sProduct.split(";"));
		sFields.put("priorities",sPriority.split(";"));
		sFields.put("payerShorts",sPayershorts.split(";"));
		sFields.put("note", "Notes"+GenericUtils.GetRandomNumber());

		String sBody = JsonBody.getRequestPayload("createChangePresentationProfile", sFields);
		String sEndpoint = ProjectVariables.sServices.get("createChangePresentationProfile").get("EndpointURL");
		io.restassured.path.json.JsonPath oReponse = oRestServiceUtils.getResponseBodyforPostServiceWithSessionID(sEndpoint, sBody);
		String sProfileID = oReponse.get("id");		
		String sProfileName = oReponse.get("profileName");

		return sProfileID+"-"+sProfileName;
		
		
		
	}

	public void Perform_the_CaptureDecision_functionality_for_the_given(String DPKey,String captureDecision, String DPKeyCriteria, String ReAssignedPresentaionName) 
	{
		CPWPage oCPWPage=this.switchToPage(CPWPage.class);
		String sInsurance=null;
		String sPayershorts=null;
		List<String> DPKeyList=null;
		
		switch(DPKeyCriteria)
		{
		case "DPLOBLevel":
		case "First-DPLOBLevel":
		case "First-DPLevel":
			System.out.println("PresentationName==>"+Serenity.sessionVariableCalled("PresentationName"));
			//Select the given presentation and DPkey

			Assert.assertTrue("uanble to select the DPKey from given presentation,Presname==>"+Serenity.sessionVariableCalled("PresentationName")+",DPkey===>"+DPKey, oCPWPage.select_the_given_DPkey_at_Presentation_view(Serenity.sessionVariableCalled("PresentationName"), DPKey,""));
			String xpath = "//span[text()='svalue']";
			refSeleniumUtils.clickGivenXpath(StringUtils.replace(oCPWPage.labelwithtext, "svalue", "DP "+DPKey+"")+"[@class='dpIdLabel']");
			//refSeleniumUtils.clickGivenXpath(StringUtils.replace(xpath, "svalue", "DP "+DPKey+""));
			
			
			refSeleniumUtils.defaultWait(ProjectVariables.TImeout_3_Seconds);
			
			if(DPKeyCriteria.equalsIgnoreCase("First-DPLevel"))
			{
				refSeleniumUtils.clickGivenXpath(StringUtils.replace(oCPWPage.Span_with_text, "value", "ALL")+"/..//span[@class='checkmark']");	
			}
			else if(DPKeyCriteria.equalsIgnoreCase("First-DPLOBLevel")||DPKeyCriteria.equalsIgnoreCase("DPLOBLevel"))
			{
				System.out.println("CapturedPayerLOB=>"+ProjectVariables.CapturedPayerLOBList);
				System.out.println(ProjectVariables.CapturedPayerLOBList.get(0));
				sInsurance=StringUtils.substringBefore(ProjectVariables.CapturedPayerLOBList.get(0), "-");
				sPayershorts=StringUtils.substringAfter(ProjectVariables.CapturedPayerLOBList.get(0), "-");
				
				refSeleniumUtils.clickGivenXpath(StringUtils.replace(oCPWPage.InsuranceChkbox_in_Presentation, "value", sInsurance));
			}
			
		break;
		case "Last-DPLevel":
			DPKeyList=Arrays.asList(DPKey.split(","));
			
			//Select the given presentation and DPkey
			Assert.assertTrue("uanble to select the DPKey from given presentation,Presname==>"+Serenity.sessionVariableCalled("PresentationName")+",DPkey===>"+DPKeyList.get(0).trim(), oCPWPage.select_the_given_DPkey_at_Presentation_view(Serenity.sessionVariableCalled("PresentationName"), DPKeyList.get(0).trim(),""));
			
			refSeleniumUtils.clickGivenXpath(StringUtils.replace(oCPWPage.labelwithtext, "svalue", "DP "+DPKeyList.get(0).trim()+""));
			refSeleniumUtils.defaultWait(ProjectVariables.TImeout_3_Seconds);
			while(refSeleniumUtils.is_WebElement_enabled(StringUtils.replace(oCPWPage.ButtonWithText, "svalue", "Next")))
			{
				refSeleniumUtils.clickGivenXpath(StringUtils.replace(oCPWPage.ButtonWithText, "svalue", "Next"));
			}
			
			String CapturedDPkey=StringUtils.substringAfter(refSeleniumUtils.get_TextFrom_Locator(StringUtils.replace(oCPWPage.Span_contains_text, "value", "DP")), "DP ");
			Serenity.setSessionVariable("CapturedDPKey").to(CapturedDPkey);
			
			refSeleniumUtils.clickGivenXpath(StringUtils.replace(oCPWPage.Span_with_text, "value", "ALL")+"/..//span[@class='checkmark']");
			
			break;
		default:
			Assert.assertTrue("Case not found===>"+DPKeyCriteria, false);
		break;
		}
		
		//Capture the given decision
		refSeleniumUtils.clickGivenXpath(StringUtils.replace(oCPWPage.ButtonContainsClass, "value", "capture"));
		refSeleniumUtils.defaultWait(ProjectVariables.TImeout_3_Seconds);
		//refSeleniumUtils.clickGivenXpath(StringUtils.replace(oCPWPage.Div_contains_text, "value", captureDecision));		
		refSeleniumUtils.clickGivenXpath(StringUtils.replace(oCPWPage.CaptureDecison_text, "value", captureDecision));
		if(captureDecision.equalsIgnoreCase("Re-Assign"))
		{
			refSeleniumUtils.clickGivenXpath(StringUtils.replace(oCPWPage.Div_contains_Class, "value", "assign-popover")+"/.."+StringUtils.replace(oCPWPage.Div_contains_text, "value", ReAssignedPresentaionName));
			refSeleniumUtils.clickGivenXpath(StringUtils.replace(oCPWPage.ButtonContainsText, "value", "Capture"));
		}
		else if(captureDecision.equalsIgnoreCase("Unassign"))
		{
			refSeleniumUtils.clickGivenXpath(StringUtils.replace(oCPWPage.ButtonContainsText, "value", "Capture"));	
			refSeleniumUtils.clickGivenXpath("("+StringUtils.replace(oCPWPage.ButtonContainsText, "value", "Capture")+")[2]");
		}
		
		refSeleniumUtils.defaultWait(ProjectVariables.TImeout_3_Seconds);
		
	}

	public void validate_the_Presentation_after_capturing_the_decision(String ReAssignedPresentaionName, String captureDecision, String DPKeyCriteria) 
	{
		CPWPage oCPWPage=this.switchToPage(CPWPage.class);
		boolean bStatus=false;
		switch(DPKeyCriteria)
		{
		case "First-DPLevel":
			
			bStatus=refSeleniumUtils.is_WebElement_Displayed(StringUtils.replace(oCPWPage.Tag_with_P, "value", "This presentation has no content yet."));
				
			if(captureDecision.equalsIgnoreCase("Re-Assign"))
			{
				Assert.assertTrue("'This presentation has no content yet' message is not displayed,after '"+captureDecision+"' DPkey also,PresName=>"+Serenity.sessionVariableCalled("PresentationName")+",DPkey=>"+Serenity.sessionVariableCalled("DPkey"), bStatus);
				//Select the given presentation and DPkey
				Assert.assertTrue("Unable to see the re-assigned DPkey in PresentationDeck,Presname==>"+ReAssignedPresentaionName+",DPkey===>"+Serenity.sessionVariableCalled("DPkey"), oCPWPage.select_the_given_DPkey_at_Presentation_view(ReAssignedPresentaionName, Serenity.sessionVariableCalled("DPkey"),""));
			}
			else
			{
				Assert.assertTrue("'This presentation has no content yet' message is not displayed,after '"+captureDecision+"' DPkey also,PresName=>"+Serenity.sessionVariableCalled("PresentationName")+",DPkey=>"+Serenity.sessionVariableCalled("DPkey"), bStatus);
				
				//validate the unassigned DPkey in available opportunity deck
				oCPWPage.validate_the_updated_DPkey_in_available_opportunity_deck(Serenity.sessionVariableCalled("DPkey"));
				
				
			}
			System.out.println("'"+captureDecision+"' functionality is validated successfully for the captured DPkey=>"+Serenity.sessionVariableCalled("DPkey")+",PresenationNames=>"+Serenity.sessionVariableCalled("PresentationName")+","+ReAssignedPresentaionName);
		
		break;
		case "First-DPLOBLevel":
			
			bStatus=refSeleniumUtils.is_WebElement_Displayed(StringUtils.replace(oCPWPage.Span_with_text, "value", "DP "+Serenity.sessionVariableCalled("DPkey")+""));
			Assert.assertTrue("DPKey is not displayed,after '"+captureDecision+"' one of the PPS,PresName=>"+Serenity.sessionVariableCalled("PresentationName")+",DPkey=>"+Serenity.sessionVariableCalled("DPkey")+",'"+captureDecision+"' PPS=>"+ProjectVariables.CapturedPayerLOBList.get(0), bStatus);	
			if(captureDecision.equalsIgnoreCase("Re-Assign"))
			{
				//Select the given presentation and DPkey
				Assert.assertTrue("Unable to see the re-assigned DPkey in PresentationDeck,Presname==>"+ReAssignedPresentaionName+",DPkey===>"+Serenity.sessionVariableCalled("DPkey"), oCPWPage.select_the_given_DPkey_at_Presentation_view(ReAssignedPresentaionName, Serenity.sessionVariableCalled("DPkey"),""));
				refSeleniumUtils.clickGivenXpath(StringUtils.replace(oCPWPage.labelwithtext, "svalue", "DP "+Serenity.sessionVariableCalled("DPkey")+"")+"[@class='dpIdLabel']");
				refSeleniumUtils.defaultWait(ProjectVariables.TImeout_3_Seconds);
				
				Assert.assertTrue("Unable to see the re-assigned PPS of DPkey in PresentationDeck,Presname==>"+ReAssignedPresentaionName+",DPkey=>"+Serenity.sessionVariableCalled("DPkey")+",PPS=>"+ProjectVariables.CapturedPayerLOBList.get(0), refSeleniumUtils.is_WebElement_Displayed(StringUtils.replace(oCPWPage.InsuranceChkbox_in_Presentation, "value", StringUtils.substringBefore(ProjectVariables.CapturedPayerLOBList.get(0), "-"))));
				
			}
			else
			{
				//validate the unassigned DPkey in available opportunity deck
				oCPWPage.validate_the_updated_DPkey_in_available_opportunity_deck(Serenity.sessionVariableCalled("DPkey"));
				
			}
			System.out.println("'"+captureDecision+"' functionality is validated successfully at LOB level of the captured DPkey=>"+Serenity.sessionVariableCalled("DPkey")+",PresenationNames=>"+Serenity.sessionVariableCalled("PresentationName")+","+ReAssignedPresentaionName);
		
		break;
		case "Last-DPLevel":
			bStatus=refSeleniumUtils.is_WebElement_Displayed(StringUtils.replace(oCPWPage.Span_with_text, "value", "DP "+Serenity.sessionVariableCalled("CapturedDPKey")+""));
			Assert.assertFalse("DPKey is still displayed,after '"+captureDecision+"' DPkey,PresName=>"+Serenity.sessionVariableCalled("PresentationName")+",DPkey=>"+Serenity.sessionVariableCalled("CapturedDPKey"), bStatus);	
			
			if(captureDecision.equalsIgnoreCase("Re-Assign"))
			{
				//Select the given presentation and DPkey
				Assert.assertTrue("Unable to see the re-assigned DPkey in PresentationDeck,Presname==>"+ReAssignedPresentaionName+",DPkey===>"+Serenity.sessionVariableCalled("CapturedDPKey"), oCPWPage.select_the_given_DPkey_at_Presentation_view(ReAssignedPresentaionName, Serenity.sessionVariableCalled("CapturedDPKey"),""));
				
				
			}
			else
			{
				//validate the unassigned DPkey in available opportunity deck
				oCPWPage.validate_the_updated_DPkey_in_available_opportunity_deck(Serenity.sessionVariableCalled("CapturedDPKey"));
				
			}
			System.out.println("'"+captureDecision+"' functionality is validated successfully at LOB level of the captured DPkey=>"+Serenity.sessionVariableCalled("DPkey")+",PresenationNames=>"+Serenity.sessionVariableCalled("PresentationName")+","+ReAssignedPresentaionName);
		
			break;
		case "DPLOBLevel":
			System.out.println("CapturedPayerLOB=>"+ProjectVariables.CapturedPayerLOBList);
			System.out.println(ProjectVariables.CapturedPayerLOBList.get(0));
			String sInsurance=StringUtils.substringBefore(ProjectVariables.CapturedPayerLOBList.get(0), "-");
			
			bStatus=refSeleniumUtils.is_WebElement_Displayed(StringUtils.replace(oCPWPage.Span_with_text, "value", "DP "+Serenity.sessionVariableCalled("DPkey")+""));
			GenericUtils.Verify("DPKey should be displayed,after '"+captureDecision+"' one of the PPS,PresName=>"+Serenity.sessionVariableCalled("PresentationName")+",DPkey=>"+Serenity.sessionVariableCalled("DPkey")+",Decision==>'"+Serenity.sessionVariableCalled("Decision")+"' PPS=>"+ProjectVariables.CapturedPayerLOBList.get(0), bStatus);	
			bStatus=refSeleniumUtils.is_WebElement_Displayed(StringUtils.replace(oCPWPage.InsuranceChkbox_in_Presentation, "value", sInsurance));
			GenericUtils.Verify("LOB should be displayed,after '"+captureDecision+"' one of the PPS,PresName=>"+Serenity.sessionVariableCalled("PresentationName")+",DPkey=>"+Serenity.sessionVariableCalled("DPkey")+",Decision==>'"+Serenity.sessionVariableCalled("Decision")+"' PPS=>"+ProjectVariables.CapturedPayerLOBList.get(0), bStatus);
			break;
		default:
			Assert.assertTrue("Case not found===>"+DPKeyCriteria, false);
		break;
		}
		
		
	}

	public void validate_the_export_popup_headers_presentationdeck() 
	{
		CPWPage oCPWPage=this.switchToPage(CPWPage.class);
		
		refSeleniumUtils.clickGivenXpath(StringUtils.replace(oCPWPage.TagContainsClass_I, "value", "sign-out"));
		refSeleniumUtils.defaultWait(ProjectVariables.TImeout_3_Seconds);
		Assert.assertTrue("'Export Presentation Documents' header is not displayed,after clicking on export button,Presentation=>"+Serenity.sessionVariableCalled("PresentationName"), refSeleniumUtils.is_WebElement_Displayed(StringUtils.replace(oCPWPage.Div_contains_text, "value", "Export Presentation Documents")));
		Assert.assertTrue("'Select the presentation documents to export' header is not displayed in the export popup,Presentation=>"+Serenity.sessionVariableCalled("PresentationName"), refSeleniumUtils.is_WebElement_Displayed(StringUtils.replace(oCPWPage.Tag_with_P, "value", "Select the presentation documents to export")));
		Assert.assertTrue("'All' headercheckbox is not displayed in the export popup,Presentation=>"+Serenity.sessionVariableCalled("PresentationName"), refSeleniumUtils.is_WebElement_Displayed(StringUtils.replace(oCPWPage.Tag_contains_P, "value", "export")+"/.."+StringUtils.replace(oCPWPage.Tag_contains_b, "value", "All")));
		Assert.assertTrue("'Policy Presentation' headercheckbox is not displayed in the export popup,Presentation=>"+Serenity.sessionVariableCalled("PresentationName"), refSeleniumUtils.is_WebElement_Displayed(StringUtils.replace(oCPWPage.Tag_contains_P, "value", "export")+"/.."+StringUtils.replace(oCPWPage.Tag_contains_b, "value", "Policy Presentation")));
		Assert.assertTrue("'Supplemental' headercheckbox is not displayed in the export popup,Presentation=>"+Serenity.sessionVariableCalled("PresentationName"), refSeleniumUtils.is_WebElement_Displayed(StringUtils.replace(oCPWPage.Tag_contains_P, "value", "export")+"/.."+StringUtils.replace(oCPWPage.Tag_contains_b, "value", "Supplemental")));
		Assert.assertTrue("'Decisions' headercheckbox is not displayed in the export popup,Presentation=>"+Serenity.sessionVariableCalled("PresentationName"), refSeleniumUtils.is_WebElement_Displayed(StringUtils.replace(oCPWPage.Tag_contains_P, "value", "export")+"/.."+StringUtils.replace(oCPWPage.Tag_contains_b, "value", "Decisions")));
		
		Assert.assertTrue("(*.docx) is not displayed at 'Policy Presentation' headercheckbox in the export popup,Presentation=>"+Serenity.sessionVariableCalled("PresentationName"), refSeleniumUtils.get_TextFrom_Locator(StringUtils.replace(oCPWPage.Tag_contains_P, "value", "export")+"/.."+StringUtils.replace(oCPWPage.Tag_contains_b, "value", "Policy Presentation")+"/..").contains("(*.docx)"));
		Assert.assertTrue("(Savings, Decision, etc.) (*.xlsx)  is not displayed at 'Supplemental' headercheckbox in the export popup,Presentation=>"+Serenity.sessionVariableCalled("PresentationName"), refSeleniumUtils.get_TextFrom_Locator(StringUtils.replace(oCPWPage.Tag_contains_P, "value", "export")+"/.."+StringUtils.replace(oCPWPage.Tag_contains_b, "value", "Supplemental")+"/..").contains("(Savings, Decision, etc.) (*.xlsx)"));
		
		Assert.assertTrue("'Internal' headercheckbox of 'Policy Presentation' is not displayed in the export popup,Presentation=>"+Serenity.sessionVariableCalled("PresentationName"), refSeleniumUtils.is_WebElement_Displayed(StringUtils.replace(StringUtils.replace(oCPWPage.Exportpopupchkbox, "mainchkbox", "Policy Presentation"),"secondarychkbox","Internal")));
		Assert.assertTrue("'External' headercheckbox of 'Policy Presentation' is not displayed in the export popup,Presentation=>"+Serenity.sessionVariableCalled("PresentationName"), refSeleniumUtils.is_WebElement_Displayed(StringUtils.replace(StringUtils.replace(oCPWPage.Exportpopupchkbox, "mainchkbox", "Policy Presentation"),"secondarychkbox","External")));
		Assert.assertTrue("'Internal' headercheckbox of 'Supplemental' is not displayed in the export popup,Presentation=>"+Serenity.sessionVariableCalled("PresentationName"), refSeleniumUtils.is_WebElement_Displayed(StringUtils.replace(StringUtils.replace(oCPWPage.Exportpopupchkbox, "mainchkbox", "Supplemental"),"secondarychkbox","Internal")));
		Assert.assertTrue("'External' headercheckbox of 'Supplemental' is not displayed in the export popup,Presentation=>"+Serenity.sessionVariableCalled("PresentationName"), refSeleniumUtils.is_WebElement_Displayed(StringUtils.replace(StringUtils.replace(oCPWPage.Exportpopupchkbox, "mainchkbox", "Supplemental"),"secondarychkbox","External")));
		Assert.assertTrue("'Internal' headercheckbox of 'Supplemental' is not displayed in the export popup,Presentation=>"+Serenity.sessionVariableCalled("PresentationName"), refSeleniumUtils.is_WebElement_Displayed(StringUtils.replace(StringUtils.replace(oCPWPage.Exportpopupchkbox, "mainchkbox", "Supplemental"),"secondarychkbox","Internal")));
		Assert.assertTrue("'Decision Matrix' headercheckbox of 'Decisions' is not displayed in the export popup,Presentation=>"+Serenity.sessionVariableCalled("PresentationName"), refSeleniumUtils.is_WebElement_Displayed(StringUtils.replace(StringUtils.replace(oCPWPage.Exportpopupchkbox, "mainchkbox", "Decisions"),"secondarychkbox","Decision Matrix")));
		Assert.assertTrue("'Finalized Decision Summay' headercheckbox of 'Decisions' is not displayed in the export popup,Presentation=>"+Serenity.sessionVariableCalled("PresentationName"), refSeleniumUtils.is_WebElement_Displayed(StringUtils.replace(StringUtils.replace(oCPWPage.Exportpopupchkbox, "mainchkbox", "Decisions"),"secondarychkbox","Finalized Decision Summay")));

		Assert.assertTrue("'(Contains sensitive information)' label of 'Internal' headercheckbox of 'Policy Presentation' is not displayed in the export popup,Presentation=>"+Serenity.sessionVariableCalled("PresentationName"), refSeleniumUtils.get_TextFrom_Locator(StringUtils.replace(StringUtils.replace(oCPWPage.Exportpopupchkbox, "mainchkbox", "Policy Presentation"),"secondarychkbox","Internal")+"/span").contains("(Contains sensitive information)"));
		Assert.assertTrue("'(Contains sensitive information)' label of 'Internal' headercheckbox of 'Supplemental' is not displayed in the export popup,Presentation=>"+Serenity.sessionVariableCalled("PresentationName"), refSeleniumUtils.get_TextFrom_Locator(StringUtils.replace(StringUtils.replace(oCPWPage.Exportpopupchkbox, "mainchkbox", "Supplemental"),"secondarychkbox","Internal")+"/span").contains("(Contains sensitive information)"));
	
		System.out.println("All the headers and checkoxes lables are displayed as expected in Export popup..............");
	}

	public void validate_the_export_popup_checkkbox_functionality() 
	{
		CPWPage oCPWPage=this.switchToPage(CPWPage.class);
		String Attribute=null;
		
		Attribute=refSeleniumUtils.Get_Value_By_given_attribute("aria-disabled", StringUtils.replace(oCPWPage.ButtonWithText, "svalue", "Export"));
		Assert.assertTrue("Export button is in 'Enabled' mode,expected should be 'Disabled' as by default(without clicking on checkbox)", Attribute.equalsIgnoreCase("true"));
		
		Attribute=refSeleniumUtils.Get_Value_By_given_attribute("aria-disabled", oCPWPage.ExportCancelbutton);
		Assert.assertTrue("Cancel button is in 'Disabled' mode in Export popup,expected should be 'Enabled' as by default", Attribute.equalsIgnoreCase("false"));
		
		refSeleniumUtils.clickGivenXpath(StringUtils.replace(oCPWPage.Tag_contains_P, "value", "export")+"/.."+StringUtils.replace(oCPWPage.Tag_contains_b, "value", "All"));
		refSeleniumUtils.defaultWait(ProjectVariables.TImeout_2_Seconds);
		//verify checkboxes,after selecting 'All' checkbox
		validate_the_checkboxes_of_export("Policy Presentation","","Selected");
		validate_the_checkboxes_of_export("Supplemental","","Selected");
		validate_the_checkboxes_of_export("Decisions","","Selected");
		
		Attribute=refSeleniumUtils.Get_Value_By_given_attribute("aria-disabled", StringUtils.replace(oCPWPage.ButtonWithText, "svalue", "Export"));
		Assert.assertTrue("Export button is in 'Disbled' mode,expected should be 'Enabled' as 'All' checkbox is selected", Attribute.equalsIgnoreCase("false"));
		
		refSeleniumUtils.clickGivenXpath(StringUtils.replace(oCPWPage.Tag_contains_P, "value", "export")+"/.."+StringUtils.replace(oCPWPage.Tag_contains_b, "value", "All"));
		refSeleniumUtils.defaultWait(ProjectVariables.TImeout_2_Seconds);
		//verify checkboxes,after de-selecting 'All' checkbox
		validate_the_checkboxes_of_export("Policy Presentation","","Not Selected");
		validate_the_checkboxes_of_export("Supplemental","","Not Selected");
		validate_the_checkboxes_of_export("Decisions","","Not Selected");
		
		Attribute=refSeleniumUtils.Get_Value_By_given_attribute("aria-disabled", StringUtils.replace(oCPWPage.ButtonWithText, "svalue", "Export"));
		Assert.assertTrue("Export button is in 'Enabled' mode,expected should be 'Disabled' as 'All' checkbox is un-selected", Attribute.equalsIgnoreCase("true"));
		
		//verify checkboxes,after de-selecting 'All' checkbox
		validate_the_checkboxes_of_export("Policy Presentation","Internal","Selected");
		validate_the_checkboxes_of_export("Policy Presentation","Internal","Not Selected");
		validate_the_checkboxes_of_export("Policy Presentation","External","Selected");
		validate_the_checkboxes_of_export("Policy Presentation","External","Not Selected");
		
		validate_the_checkboxes_of_export("Supplemental","Internal","Selected");
		validate_the_checkboxes_of_export("Supplemental","Internal","Not Selected");
		validate_the_checkboxes_of_export("Supplemental","External","Selected");
		validate_the_checkboxes_of_export("Supplemental","External","Not Selected");
		
		validate_the_checkboxes_of_export("Decisions","Decision Matrix","Selected");
		validate_the_checkboxes_of_export("Decisions","Decision Matrix","Not Selected");
		validate_the_checkboxes_of_export("Decisions","Finalized Decision Summay","Selected");
		validate_the_checkboxes_of_export("Decisions","Finalized Decision Summay","Not Selected");
		
		refSeleniumUtils.clickGivenXpath(oCPWPage.ExportCancelbutton);
		refSeleniumUtils.defaultWait(ProjectVariables.TImeout_2_Seconds);
		Assert.assertFalse("'Export Presentation Documents' header is still displayed,after clicking on Cancel button of export popup,Presentation=>"+Serenity.sessionVariableCalled("PresentationName"), refSeleniumUtils.is_WebElement_Displayed(StringUtils.replace(oCPWPage.Div_contains_text, "value", "Export Presentation Documents")));
		
	}

	private void validate_the_checkboxes_of_export(String Exportfunctilaity, String Exportfunctilaitycheckbox, String criteria)
	{
		CPWPage oCPWPage=this.switchToPage(CPWPage.class);
		String Attribute=null;
		
		switch(Exportfunctilaity)
		{
		case "Supplemental":
		case "Policy Presentation":
			if(Exportfunctilaitycheckbox.isEmpty()&&criteria.equalsIgnoreCase("Selected"))
			{
				Assert.assertTrue("'Internal' headercheckbox of '"+Exportfunctilaity+"' is not selected,it should be selected as 'All' checkbox is selected,Presentation=>"+Serenity.sessionVariableCalled("PresentationName"), refSeleniumUtils.Get_Value_By_given_attribute("class",StringUtils.replace(StringUtils.replace(oCPWPage.Exportpopupchkbox, "mainchkbox",Exportfunctilaity),"secondarychkbox","Internal")+"/div[contains(@class,'checkbox')]//span").contains("checked"));
				Assert.assertTrue("'External' headercheckbox of '"+Exportfunctilaity+"' is not selected,it should be selected as 'All' checkbox is selected,Presentation=>"+Serenity.sessionVariableCalled("PresentationName"), refSeleniumUtils.Get_Value_By_given_attribute("class",StringUtils.replace(StringUtils.replace(oCPWPage.Exportpopupchkbox, "mainchkbox", Exportfunctilaity),"secondarychkbox","External")+"/div[contains(@class,'checkbox')]//span").contains("checked"));
			}
			else if(Exportfunctilaitycheckbox.isEmpty()&&criteria.equalsIgnoreCase("Not Selected"))
			{
				Assert.assertTrue("'Internal' headercheckbox of '"+Exportfunctilaity+"' is selected,it should be un-selected as 'All' checkbox is un-selected,Presentation=>"+Serenity.sessionVariableCalled("PresentationName"), refSeleniumUtils.Get_Value_By_given_attribute("class",StringUtils.replace(StringUtils.replace(oCPWPage.Exportpopupchkbox, "mainchkbox", Exportfunctilaity),"secondarychkbox","Internal")+"/div[contains(@class,'checkbox')]//span").isEmpty());
				Assert.assertTrue("'External' headercheckbox of '"+Exportfunctilaity+"' is selected,it should be un-selected as 'All' checkbox is un-selected,Presentation=>"+Serenity.sessionVariableCalled("PresentationName"), refSeleniumUtils.Get_Value_By_given_attribute("class",StringUtils.replace(StringUtils.replace(oCPWPage.Exportpopupchkbox, "mainchkbox", Exportfunctilaity),"secondarychkbox","External")+"/div[contains(@class,'checkbox')]//span").isEmpty());
			}
			
		break;
		case "Decisions":
			if(Exportfunctilaitycheckbox.isEmpty()&&criteria.equalsIgnoreCase("Selected"))
			{
				Assert.assertTrue("'Decision Matrix' headercheckbox of 'Decisions' is not selected,it should be selected as 'All' checkbox is selected,Presentation=>"+Serenity.sessionVariableCalled("PresentationName"), refSeleniumUtils.Get_Value_By_given_attribute("class",StringUtils.replace(StringUtils.replace(oCPWPage.Exportpopupchkbox, "mainchkbox", "Decisions"),"secondarychkbox","Decision Matrix")+"/div[contains(@class,'checkbox')]//span").contains("checked"));
				Assert.assertTrue("'Finalized Decision Summay' headercheckbox of 'Decisions' is not selected,it should be selected as 'All' checkbox is selected,Presentation=>"+Serenity.sessionVariableCalled("PresentationName"), refSeleniumUtils.Get_Value_By_given_attribute("class",StringUtils.replace(StringUtils.replace(oCPWPage.Exportpopupchkbox, "mainchkbox", "Decisions"),"secondarychkbox","Finalized Decision Summay")+"/div[contains(@class,'checkbox')]//span").contains("checked"));
			}
			else if(Exportfunctilaitycheckbox.isEmpty()&&criteria.equalsIgnoreCase("Not Selected"))
			{
				Assert.assertTrue("'Decision Matrix' headercheckbox of 'Decisions' is selected,it should be un-selected as 'All' checkbox is un-selected,Presentation=>"+Serenity.sessionVariableCalled("PresentationName"), refSeleniumUtils.Get_Value_By_given_attribute("class",StringUtils.replace(StringUtils.replace(oCPWPage.Exportpopupchkbox, "mainchkbox", "Decisions"),"secondarychkbox","Decision Matrix")+"/div[contains(@class,'checkbox')]//span").isEmpty());
				Assert.assertTrue("'Finalized Decision Summay' headercheckbox of 'Decisions' is selected,it should be un-selected as 'All' checkbox is un-selected,Presentation=>"+Serenity.sessionVariableCalled("PresentationName"), refSeleniumUtils.Get_Value_By_given_attribute("class",StringUtils.replace(StringUtils.replace(oCPWPage.Exportpopupchkbox, "mainchkbox", "Decisions"),"secondarychkbox","Finalized Decision Summay")+"/div[contains(@class,'checkbox')]//span").isEmpty());
			}
			

		break;
		default:
			Assert.assertTrue("case not found===>"+Exportfunctilaity, false);
		break;
		}
		
		if(!Exportfunctilaitycheckbox.isEmpty()&&criteria.equalsIgnoreCase("Not Selected"))
		{
			if(!refSeleniumUtils.Get_Value_By_given_attribute("class",StringUtils.replace(StringUtils.replace(oCPWPage.Exportpopupchkbox, "mainchkbox", Exportfunctilaity),"secondarychkbox",Exportfunctilaitycheckbox)+"/div[contains(@class,'checkbox')]//span").isEmpty())
			{
				refSeleniumUtils.clickGivenXpath(StringUtils.replace(StringUtils.replace(oCPWPage.Exportpopupchkbox, "mainchkbox", Exportfunctilaity),"secondarychkbox",Exportfunctilaitycheckbox));
				refSeleniumUtils.defaultWait(ProjectVariables.TImeout_2_Seconds);
			}
			
			
			Assert.assertTrue("'"+Exportfunctilaitycheckbox+"' headercheckbox of '"+Exportfunctilaity+"' is selected,it should be un-selected as it is un-selected,Presentation=>"+Serenity.sessionVariableCalled("PresentationName"), refSeleniumUtils.Get_Value_By_given_attribute("class",StringUtils.replace(StringUtils.replace(oCPWPage.Exportpopupchkbox, "mainchkbox", Exportfunctilaity),"secondarychkbox",Exportfunctilaitycheckbox)+"/div[contains(@class,'checkbox')]//span").isEmpty());
			
			Attribute=refSeleniumUtils.Get_Value_By_given_attribute("aria-disabled", StringUtils.replace(oCPWPage.ButtonWithText, "svalue", "Export"));
			Assert.assertTrue("Export button is in 'Enabled' mode,expected should be 'Disabled' as '"+Exportfunctilaitycheckbox+"' checkbox is selected", Attribute.equalsIgnoreCase("true"));
			
		}
		else if(!Exportfunctilaitycheckbox.isEmpty()&&criteria.equalsIgnoreCase("Selected"))
		{
			if(refSeleniumUtils.Get_Value_By_given_attribute("class",StringUtils.replace(StringUtils.replace(oCPWPage.Exportpopupchkbox, "mainchkbox", Exportfunctilaity),"secondarychkbox",Exportfunctilaitycheckbox)+"/div[contains(@class,'checkbox')]//span").isEmpty())
			{
				refSeleniumUtils.clickGivenXpath(StringUtils.replace(StringUtils.replace(oCPWPage.Exportpopupchkbox, "mainchkbox", Exportfunctilaity),"secondarychkbox",Exportfunctilaitycheckbox));
				refSeleniumUtils.defaultWait(ProjectVariables.TImeout_2_Seconds);
			}
			
			Assert.assertTrue("'"+Exportfunctilaitycheckbox+"' headercheckbox of '"+Exportfunctilaity+"' is un-selected,it should be selected as it is un-selected,Presentation=>"+Serenity.sessionVariableCalled("PresentationName"), refSeleniumUtils.Get_Value_By_given_attribute("class",StringUtils.replace(StringUtils.replace(oCPWPage.Exportpopupchkbox, "mainchkbox", Exportfunctilaity),"secondarychkbox",Exportfunctilaitycheckbox)+"/div[contains(@class,'checkbox')]//span").contains("checked"));
			
			Attribute=refSeleniumUtils.Get_Value_By_given_attribute("aria-disabled", StringUtils.replace(oCPWPage.ButtonWithText, "svalue", "Export"));
			Assert.assertTrue("Export button is in 'Disabled' mode,expected should be 'Enabled' as '"+Exportfunctilaitycheckbox+"' checkbox is selected", Attribute.equalsIgnoreCase("false"));
			
		}

	}
	
	public void validateThelistofDPsTopicsPriorityandSavings(String DPkey) 
	{
		CPWPage oCPWPage=this.switchToPage(CPWPage.class);
		OppurtunityDeck refOppurtunityDeck=this.switchToPage(OppurtunityDeck.class);
		int iPriorityReasonsSize=0;
		String DPsCount=null;
		String TopicsCount=null;
		String Medicalpolicy=null;
		String Topic=null;
		String sPriority=null;
		String sRawSavings=null;
		List<String> TopicsList=null;
		ArrayList<String> sPriorityReasons=new ArrayList<>();
		
		List<String> DPkeysList=Arrays.asList(Serenity.sessionVariableCalled("DPkey").toString().split(","));
		//TopicsList=Arrays.asList(Serenity.sessionVariableCalled("Topic").toString().split(","));
		String sPresentationName=Serenity.sessionVariableCalled("PresentationName");
		
		Medicalpolicy=Serenity.sessionVariableCalled("Medicalpolicy");
		Topic=Serenity.sessionVariableCalled("Topic");
		DPsCount=StringUtils.substringBetween(refSeleniumUtils.get_TextFrom_Locator(StringUtils.replace(oCPWPage.labelcontainstext, "svalue", Medicalpolicy)),"(","DP(s)").trim();
		TopicsCount=StringUtils.substringBetween(refSeleniumUtils.get_TextFrom_Locator(StringUtils.replace(oCPWPage.labelcontainstext, "svalue", Medicalpolicy)),"DP(s) in","Topic(s)").trim();
		sPriority=StringUtils.substringAfter(refSeleniumUtils.get_TextFrom_Locator(StringUtils.replace(oCPWPage.labelcontainstext, "svalue", DPkey)+"/..//span[contains(text(),'Priority')]"), "Priority:").trim();
		iPriorityReasonsSize=refSeleniumUtils.get_Matching_WebElement_count(StringUtils.replace(oCPWPage.labelcontainstext, "svalue", DPkey)+"/..//span[contains(@class,'priority')]");
		sRawSavings=StringUtils.substringAfter(refSeleniumUtils.get_TextFrom_Locator(StringUtils.replace(oCPWPage.labelcontainstext, "svalue", DPkey)+"/..//span[contains(text(),'$')]"), "$").replaceAll(",", "");
		
		//DBMethod to retrieve the priority,priorityreasons and savings  
		MongoDBUtils.GetThePriorityandSavings(DPkey);
		String Priority=Serenity.sessionVariableCalled("Priority");
		String DBRawsavings=Serenity.sessionVariableCalled("Rawsavings");
		
					
		
		GenericUtils.Verify("Expected Medicalpolicy=>"+Medicalpolicy+",under presentation=>"+sPresentationName, refSeleniumUtils.is_WebElement_Displayed(StringUtils.replace(oCPWPage.labelcontainstext, "svalue", Medicalpolicy)));
		GenericUtils.Verify("Expected Topic=>"+Topic+",under presentation=>"+sPresentationName, refSeleniumUtils.is_WebElement_Displayed(StringUtils.replace(oCPWPage.labelcontainstext, "svalue", Topic)));
		GenericUtils.Verify("Expected DPkey=>"+DPkey+",under presentation=>"+sPresentationName, refSeleniumUtils.is_WebElement_Displayed(StringUtils.replace(oCPWPage.labelcontainstext, "svalue", DPkey)));
		GenericUtils.Verify("MP Level Expected DPscount=>"+DPkeysList.size()+",Actual DPscount=>"+DPsCount+",under presentation=>"+sPresentationName+",MP=>"+Medicalpolicy, DPkeysList.size()==Long.valueOf(DPsCount));
		GenericUtils.Verify("Expected TopicsCount=>1,Actual TopicsCount=>"+TopicsCount+",under presentation=>"+sPresentationName+",at MP=>"+Medicalpolicy, 1==Long.valueOf(TopicsCount));
		
		DPsCount=StringUtils.substringBefore(StringUtils.substringAfterLast(StringUtils.substringBeforeLast(refSeleniumUtils.get_TextFrom_Locator(StringUtils.replace(oCPWPage.labelcontainstext, "svalue", Topic)), "("),"("),"DP").trim();
		//DPsCount=StringUtils.substringBetween(refSeleniumUtils.get_TextFrom_Locator(StringUtils.replace(oCPWPage.labelcontainstext, "svalue", Topic)),"(","DP(s)").trim();
		GenericUtils.Verify("Topic level Expected DPscount=>"+DPkeysList.size()+",Actual DPscount=>"+DPsCount+",under presentation=>"+sPresentationName+",Topic=>"+Topic, DPkeysList.size()==Long.valueOf(DPsCount));
		
		//To retrieve the exact priority
		Priority=refOppurtunityDeck.RetrieveTheExactPriority(Priority);
		GenericUtils.Verify("Expected Priority=>"+Priority+",Actual Priority=>"+sPriority+",under presentation=>"+sPresentationName+",DP=>"+DPkey, sPriority.equalsIgnoreCase(Priority));
		
		for (int j = 1; j <=iPriorityReasonsSize; j++) 
		{
			sPriorityReasons.add(refSeleniumUtils.get_TextFrom_Locator("("+StringUtils.replace(oCPWPage.labelcontainstext, "svalue", DPkey)+"/..//span[contains(@class,'priority')])["+j+"]"));	
		}
		
		GenericUtils.CompareTwoValues("PriorityReasons of DP=>"+DPkey+",Presentation=>"+sPresentationName, sPriorityReasons, ProjectVariables.StaticOrderofPriorityReasons);
		
		GenericUtils.Verify("Expected Rawsavings=>"+DBRawsavings+",Actual Rawsavings=>"+sRawSavings+",under presentation=>"+sPresentationName+",DP=>"+DPkey, DBRawsavings.equalsIgnoreCase(sRawSavings));
	
		
	}

	public void validateTheDisplayhovertextoftheGivenPriorityreason(String Priorityreason) 
	{
		CPWPage oCPWPage=this.switchToPage(CPWPage.class);
		boolean bStatus=false;
		String sDPKey=Serenity.sessionVariableCalled("DPkey");
		String sPriorityreasonname=null;
		String sMousehovertext=null;
		String sPresentationName=Serenity.sessionVariableCalled("PresentationName");
		int iPriorityreasonSize=refSeleniumUtils.get_Matching_WebElement_count(StringUtils.replace(oCPWPage.Span_with_text, "value", Priorityreason));
		
		if(iPriorityreasonSize==0)
		{
			GenericUtils.Verify("Priorityreason size '0',Expected should be '3' for the given prioritreason=>"+Priorityreason+",DP=>"+sDPKey+",PresentationName=>"+sPresentationName, false);
		}
		
		for (int i = 1; i <=iPriorityreasonSize; i++) 
		{
			refSeleniumUtils.moveTo(StringUtils.replace(oCPWPage.Span_with_text, "value", Priorityreason)+"["+i+"]");
			refSeleniumUtils.defaultWait(ProjectVariables.TImeout_1_Seconds);
			sMousehovertext=refSeleniumUtils.Get_Value_By_given_attribute("title",StringUtils.replace(oCPWPage.Span_with_text, "value", Priorityreason)+"["+i+"]");
			
			switch(Priorityreason)
			{
			case "CR":
				sPriorityreasonname="Client Requested";
			break;
			case "BR":
				sPriorityreasonname="Business Reason";
			break;
			case "HS":
				sPriorityreasonname="High Savings";
			break;
			
			default:
			Assert.assertTrue("case not found==>"+Priorityreason, false);	
			break;
			}
			
			bStatus=sMousehovertext.equalsIgnoreCase(sPriorityreasonname);
			GenericUtils.Verify("Expected Priorityreason displayhover=>"+sPriorityreasonname+",Actual Priorityreason displayhover=>"+sMousehovertext+",DP=>"+sDPKey+",PresentationName=>"+sPresentationName, bStatus);
			
		}
		

	}
	//=======================================>
	//====================================================================================================================================================================>
		public void selectDPTest(String sValidation,String sDPValue){
			CPWPage oCPWPage=this.switchToPage(CPWPage.class);
			try{
				String sDPALL="//span[text()='ALL']/..//label/span";
				String sFirstPayershort = "(//tr[@role='row'][contains(@class,'mat-row')])[1]/td[1]";
				String sSecondPayershort = "(//tr[@role='row'][contains(@class,'mat-row')])[2]/td[1]";
				
				String sPayerChbk1="((//tr[@role='row'][contains(@class,'mat-row')])[1]/td[1]//span)[3]";
				String sPayerChbk2="((//tr[@role='row'][contains(@class,'mat-row')])[2]/td[1]//span)[3]";
				
				
				
				switch (sValidation.toUpperCase()){
				case "DP":
				case "DP ALL":
					oGenericUtils.clickOnElement(sDPALL);
					break;
				case "DP MULTIPLE":
					 List<WebElement> sList=getDriver().findElements(By.xpath("//tr[@role='row'][contains(@class,'mat-row')]"));
					 if(sList.size()>=2){
						 String sPayer1=getDriver().findElement(By.xpath(sFirstPayershort)).getText().trim();
						 String sPayer2=getDriver().findElement(By.xpath(sSecondPayershort)).getText().trim();
						 oGenericUtils.clickButton(By.xpath(sPayerChbk1));
						 oGenericUtils.clickButton(By.xpath(sPayerChbk2));
						 Serenity.setSessionVariable("sPayershort1").to(sPayer1.trim());
						 Serenity.setSessionVariable("sPayershort2").to(sPayer2.trim());
					 }else{
						 GenericUtils.Verify("No Payershorts found :payershort count is not equal to 2","FAILED");
					 }
					break;
				case "DPLOBLEVEL":
					System.out.println("CapturedPayerLOB=>"+ProjectVariables.CapturedPayerLOBList);
					System.out.println(ProjectVariables.CapturedPayerLOBList.get(0));
					String sInsurance=StringUtils.substringBefore(ProjectVariables.CapturedPayerLOBList.get(0), "-");
					
					refSeleniumUtils.clickGivenXpath(StringUtils.replace(oCPWPage.InsuranceChkbox_in_Presentation, "value", sInsurance));
					
					break;
				default:
					Assert.assertTrue("Invalid selection==>"+sValidation.toUpperCase(), false);
				
				}
			}catch(Exception e){
				GenericUtils.Verify("Object not found , Failed due to :="+e.getMessage(),"FAILED");
			}
		}

		
		//====================================================================================================================================================================>
		public void validate_the_Presentations_created_for_the_client(String clientname) throws java.text.ParseException
		{
			CPWPage oCPWPage=this.switchToPage(CPWPage.class);
			boolean bstatus=false;
			int j=0;
			int iPresentationsSize=0;
			String sPresentationname=null;
			String sPreviousPresentationname=null;
			HashSet<String> UIPresentationList=new HashSet<>();
			HashSet<String> NotUIPresentationList=new HashSet<>();
			String sRightArrow = "//div[@class = 'mat-ripple mat-tab-header-pagination mat-tab-header-pagination-after mat-elevation-z4']/div";
			
			iPresentationsSize=refSeleniumUtils.get_Matching_WebElement_count(StringUtils.replace(oCPWPage.Span_with_Class, "value", "pres_pro_name"));
			
			//To Retrieve the Presentations for client
			for (int i = 1; i <=iPresentationsSize; i++) 
			{
				if(refSeleniumUtils.is_WebElement_Displayed(StringUtils.replace(oCPWPage.Span_with_Class, "value", "pres_pro_name")+"["+i+"]"))
				{
					sPresentationname=refSeleniumUtils.get_TextFrom_Locator(StringUtils.replace(oCPWPage.Span_with_Class, "value", "pres_pro_name")+"["+i+"]");
					if(sPresentationname.contains("..."))
					{
						sPresentationname=StringUtils.substringBefore(sPresentationname, "...").trim();
					}
					
					
				}
				else
				{
					do
					{
						oGenericUtils.clickOnElement(sRightArrow);	
						j=i-3;
					}
					while(!refSeleniumUtils.is_WebElement_Displayed(StringUtils.replace(oCPWPage.Span_with_Class, "value", "pres_pro_name")+"["+i+"]"));
					i=j;
				}
				
				if(!UIPresentationList.contains(sPresentationname))
				{
					//To validate the sorting order of the presentations based on created date
					validateTheSortingorderBasedonCreateddateofPresentation(sPresentationname,sPreviousPresentationname,clientname);
					sPreviousPresentationname=sPresentationname;
				}
				UIPresentationList.add(sPresentationname);
			
			}
			
			//To validate the Presentations for client with DB
			for (String DBPresname : ProjectVariables.ProfilewithCreatedTime) 
			{
				String sExactDBPresname=StringUtils.substringBefore(DBPresname, ";").trim();
				if(UIPresentationList.contains(sExactDBPresname))
				{
					System.out.println(sExactDBPresname+" Presentation is displayed as expected with DB for the client =>"+clientname);
				}
				else
				{
					for (String UIPrese : UIPresentationList) 
					{
						if(sExactDBPresname.contains(UIPrese.trim()))
						{
							bstatus=true;
							break;
						}
						
					}
					if(bstatus==false)
					{
						NotUIPresentationList.add(sExactDBPresname);
						GenericUtils.Verify(sExactDBPresname+" Presentation is not displayed as expected with DBfor the client =>"+clientname, false);
					}
					
				}
			}
			
			
		}

		//====================================================================================================================================================================>
		
		public void validateTheSortingorderBasedonCreateddateofPresentation(String Presentationname,String PreviousPresentationname, String clientname) throws java.text.ParseException 
		{
			 SimpleDateFormat sdfo = new SimpleDateFormat("dd/MM/yyyy h:mm:ss a"); 
			   
			String sPresentationCreatedTime=null;
			String sPreviousPresentationCreatedTime=null;
			
			if(PreviousPresentationname!=null)
			{
				
				for (String DBPresname : ProjectVariables.ProfilewithCreatedTime) 
				{
					sPresentationCreatedTime=StringUtils.substringAfter(DBPresname, ";").trim();
					if(DBPresname.contains(Presentationname))
					{
						break;
					}
				}
				
				for (String DBPresname : ProjectVariables.ProfilewithCreatedTime) 
				{
					
					sPreviousPresentationCreatedTime=StringUtils.substringAfter(DBPresname, ";").trim();
					if(DBPresname.contains(PreviousPresentationname))
					{
						break;
					}
				}
				
				 // Get the two dates to be compared 
		        Date Date1 = sdfo.parse(sPresentationCreatedTime); 
		        Date Date2 = sdfo.parse(sPreviousPresentationCreatedTime); 
				
				if(Date1.compareTo(Date2)<0||Date1.compareTo(Date2)==0)
				{
					System.out.println(Presentationname+" is in sorting order as per the creation time for the client=>"+clientname);
				}
				else
				{
					GenericUtils.Verify("'"+Presentationname+"-->"+sPresentationCreatedTime+"' Presentation is not in sorting order with Previous Presentaion=>'"+PreviousPresentationname+"--->"+sPreviousPresentationCreatedTime+"' as per the creation time for the client=>"+clientname, false);
				}
			}
			
		}

		public void validateRuleRelationToolTip(String levelToCheck, String deckName) throws ElementNotFoundException 
		{
				
			String TooltipText = "//div[@class='cdk-overlay-container']//div[@class='cdk-overlay-pane mat-tooltip-panel']//div[contains(@class,'mat-tooltip')]";
			
			WebElement  element = null;
			String sTooltip = "";
			
			 int iLoopCounter = 0;
			
			 List<WebElement>  AllDPsElmnts = new ArrayList();	
			 String AllTopics  ="//app-cpd-pres-policy//mat-expansion-panel//mat-panel-description//app-cpd-pres-topic";
			 String AllDPs = "//app-cpd-pres-policy//mat-expansion-panel//mat-panel-description//app-cpd-pres-dp//label[@class='dpIdLabel']";
			 String  MedPolicyRelationshipIcon  =  "(//mat-expansion-panel-header//i[@class='fa fa-bookmark ng-star-inserted'])[1]";  
			 String  TopicRelationshipIcon  =  "(//mat-expansion-panel-header//i[@class='fa fa-bookmark ng-star-inserted'])[2]";
			 String  AllDPsRuleRelationIcons = "//app-cpd-pres-dp//i[@class='fa fa-bookmark ng-star-inserted']";
		
			 
			 WebElement  MedPolicyRelationshipIconElmnt  =  getDriver().findElement(By.xpath(MedPolicyRelationshipIcon));
			 WebElement  TopicRelationshipIconElmnt  =  getDriver().findElement(By.xpath(TopicRelationshipIcon));
		
			 //Rule Relationship Alert  2 DPs in 1 Topic
			 AllDPsElmnts =  refSeleniumUtils.getElementsList("XPATH", AllDPsRuleRelationIcons);
			 			
				switch(levelToCheck)
					{
								case "MedicalPolicyLevel":						
									      	 		//Move to MedicalPolicy and take text and compare with count of Topics and DPs		
									 			
													if(refSeleniumUtils.is_WebElement_Displayed(MedPolicyRelationshipIcon))
													{
															refSeleniumUtils.moveToElement(MedPolicyRelationshipIconElmnt);	
													}
													else {   Assert.assertTrue("RuleRelationship icon not displayed for the MedicalPolicy",false) ;
													 			getDriver().quit();
															}
									 				
									 				//objSeleniumUtils.waitForElement(MedPolicyRelationshipIcon, "shouldBevisible", 4);
									 				
									 				if(refSeleniumUtils.is_WebElement_Displayed(TooltipText))
									 				{	
									 				    sTooltip = getDriver().findElement(By.xpath(TooltipText)).getText().trim();	
									 				    Assert.assertTrue("Tooltip::"+sTooltip+" is displayed when User hovers mouse for  MedicalPolicy",true);	
									 				}
									 				else
									 				{
									 					 Assert.assertTrue("Tooltip::"+sTooltip+" is not displayed when User hovers mouse for  MedicalPolicy",false);
									 				}								 												 				
									 				
									 				if(sTooltip.contains("Rule Relationship Alert"))
											 				{
											 					 Assert.assertTrue("Tooltip is  displayed when User hovers mouse for  MedicalPolicy "+sTooltip,true);
											 				}
											 				else
											 				{
											 					Assert.assertTrue("Tooltip is not displayed when User hovers mouse for  MedicalPolicy "+sTooltip,false);
											 					getDriver().quit();
											 				}		
									 				 break;
								
								case "TopicLevel":																
															//objSeleniumUtils.moveToElement(TopicRelationshipIconElmnt);	
															if(refSeleniumUtils.is_WebElement_Displayed(TopicRelationshipIcon))
															{
																	refSeleniumUtils.moveToElement(TopicRelationshipIconElmnt);	
															}
															else {   Assert.assertTrue("RuleRelationship icon not displayed for the Topic",false) ;
															 			getDriver().quit();
																	}														
															
											 				//objSeleniumUtils.waitForElement(TopicRelationshipIcon, "shouldBevisible", 4);
											 				refSeleniumUtils.moveToElement(TopicRelationshipIconElmnt);	
											 				
											 				
											 				    sTooltip = getDriver().findElement(By.xpath(TooltipText)).getText().trim();	
											 				 if(!sTooltip.isEmpty())
												 				{	 
											 				    
											 				    Assert.assertTrue("Tooltip::"+sTooltip+" is displayed when User hovers mouse for  Topic",true);	
											 				}
											 				else
											 				{
											 					 Assert.assertTrue("Tooltip::"+sTooltip+" is not displayed when User hovers mouse for  Topic",false);
											 				}	
											 				
											 				
											 				if(sTooltip.contains("Rule Relationship Alert"))
											 				{
											 					 Assert.assertTrue("Tooltip is  displayed when User hovers mouse for  Topic with count of  DPs::"+sTooltip,true);
											 				}
											 				else
											 				{
											 					Assert.assertTrue("Tooltip is not displayed when User hovers mouse for  Topic with count of  DPs::"+sTooltip,false);
											 					getDriver().quit();
											 				}														
								
														break;
							
								case "DPLevel":
															 for(int m=0;m<iLoopCounter;m++)
															 {		
																	 element = AllDPsElmnts.get(m);
																	 refSeleniumUtils.evaluateJavascript("arguments[0].scrollIntoView(true);",element);																		
																	 refSeleniumUtils.moveToElement(element);																 
																	refSeleniumUtils.waitForElement(TooltipText, "shouldBevisible", 4);
																	if(refSeleniumUtils.is_WebElement_Displayed(TooltipText))
																	{	
																	    sTooltip = getDriver().findElement(By.xpath(TooltipText)).getText().trim();	
																	    boolean TooltipStatus = sTooltip.equalsIgnoreCase("Rule Relationship Alert");;
																	    GenericUtils.Verify("Tooltip::"+sTooltip+" is displayed when User hovers mouse for  DPKey::", TooltipStatus);
																	}
															 }					
																break;
							
				};
				
			
				
			}
		
		
		public void selectDPsatGivenLevelatPresView(String slevel) 
		{
			

			String sPolicyLevel = "(//span[contains(@class,'policy')]/i)[1]";
			String sTopicLevel = "(//span[contains(@class,'topic')]/i)[1]";		
			String sFirstDP =  "((//app-cpd-pres-dp//div//mat-checkbox//div[contains(@class,'mat-checkbox-inner-container')]))[1]";
			String sDPALL="//span[text()='ALL']/..//label/span";

			String sFirstPolicyValue = "(//label[contains(text(),'Medical Policy:')])[1]";
			String sFirstTopicValue = "(//label[contains(text(),'Topic:')])[1]";
			String sFirstDPValue = "(//div[@class = 'pres_dp_main'])[1]/descendant::label[text()]";

			String sMedicalPolicy = getDriver().findElement(By.xpath(sFirstPolicyValue)).getText();
			sMedicalPolicy = sMedicalPolicy.substring(15, sMedicalPolicy.lastIndexOf("DP(s)"));
			sMedicalPolicy = sMedicalPolicy.substring(0, sMedicalPolicy.lastIndexOf("("));	

			String sTopic = getDriver().findElement(By.xpath(sFirstTopicValue)).getText();
			sTopic = sTopic.substring(6, sTopic.lastIndexOf("DP(s)"));
			sTopic = sTopic.substring(0, sTopic.lastIndexOf("("));	

			String sDPValue = getDriver().findElement(By.xpath(sFirstDPValue)).getText();

			switch  (slevel.toUpperCase()){

			case "MEDICAL POLICY":			
				oGenericUtils.clickOnElement(sPolicyLevel);
				break;

			case "TOPIC":			
				oGenericUtils.clickOnElement(sTopicLevel);
				break;

			//case "DP":
				//oGenericUtils.clickOnElement(sFirstDP);
				//break;

			case "ALL":
				oGenericUtils.clickOnElement(sDPALL);
				break;
			case "DP":
			case "DPLOBLEVEL":
			case "DP MULTIPLE":
			case "DP ALL":	
				String sDPArrow="//span[.='"+sDPValue+"']/following::button[contains(@class,'arrow')]";			
				
				oGenericUtils.clickOnElement("//label[contains(text(),'"+sDPValue+"')]");
				refSeleniumUtils.waitForContentLoad();
				selectDPTest(slevel, sDPValue);
				oGenericUtils.clickOnElement(sDPArrow);
				refSeleniumUtils.defaultWait(ProjectVariables.TImeout_3_Seconds);
				break;
			
			default:
				Assert.assertTrue("Invalid selection==>"+slevel.toUpperCase(), false);
				
			}

			oGenericUtils.isElementExist("label", "Capture Decision");

			Serenity.setSessionVariable("MedicalPolicy").to(sMedicalPolicy.trim());
			Serenity.setSessionVariable("Topic").to(sTopic.trim());
			Serenity.setSessionVariable("CapturedDPKey").to(sDPValue);

		}

		public void userValidates(String sValidation, String sDeckType) {
			switch(sDeckType){
			case "Available Oppurtunities":
				switch(sValidation){
				case "DP Type":
					GenericUtils.Verify("displaying "+sValidation+" dropdown", refSeleniumUtils.is_WebElement_Displayed(StringUtils.replace(Label_With_Contains, "sValue", sValidation)+"/..//span"));
					GenericUtils.Verify("clicking on "+sValidation+" dropdown", oGenericUtils.clickOnElement(StringUtils.replace(Label_With_Contains, "sValue", sValidation)+"/..//span"));
					String[] DPTypeOptions=ProjectVariables.DPTypeOptions.split(",");
					for(int i=0;i<DPTypeOptions.length;i++){
						GenericUtils.Verify("displaying DP Type dropdown option as:"+DPTypeOptions[i], refSeleniumUtils.get_TextFrom_Locator("("+StringUtils.replace(oCPWPage.Div_contains_Class, "value", "transformPanel")+"//span)["+(i+1)+"]").trim().equalsIgnoreCase(DPTypeOptions[i]));
					}	
					oGenericUtils.clickOnElement("("+StringUtils.replace(oCPWPage.Div_contains_Class, "value", "transformPanel")+"//span)[1]");
					break;
					default:Assert.assertTrue("Invalid case selection",false);
				}
				break;
			case "NPP Opportunities Dropdown":
				GenericUtils.Verify("for displaying default selection as:"+StringUtils.substringBefore(sDeckType, "Dropdown").trim(),
						refSeleniumUtils.get_TextFrom_Locator(viewDropdown).equalsIgnoreCase(StringUtils.substringBefore(sDeckType, "Dropdown").trim()));
				try {
					//validating NPP Label by changing client
					oGenericUtils.clickOnElement(oFilterDrawer.sClientDropdown);
					oFilterDrawer.getElementClient("Dean Health Plan").click();					
					refSeleniumUtils.defaultWait(ProjectVariables.TImeout_6_Seconds);
					GenericUtils.Verify("for displaying label as NPP Opportunities even after changing the client as Dean Health Plan",refSeleniumUtils.is_WebElement_Displayed(StringUtils.replace(oCPWPage.Span_contains_text, "value", StringUtils.substringBefore(sDeckType, "Dropdown").trim())+"/span"));
					//selecting Cross over
					GenericUtils.Verify("for clicking on:"+sDeckType,refSeleniumUtils.clickGivenXpath(viewDropdown));
					GenericUtils.Verify("for selecting "+sValidation,refSeleniumUtils.clickGivenXpath(StringUtils.replace(oCPWPage.Span_contains_text, "value", sValidation)));
					refSeleniumUtils.defaultWait(ProjectVariables.TImeout_3_Seconds);
					//validating cross over label
					GenericUtils.Verify("for displaying "+sValidation+" as label",refSeleniumUtils.is_WebElement_Displayed(StringUtils.replace(oppurtunityLabel, "value", sValidation)));
					//validating cross over label by changing client
					oGenericUtils.clickOnElement(oFilterDrawer.sClientDropdown);
					oFilterDrawer.getElementClient("Cigna").click();					
					refSeleniumUtils.defaultWait(ProjectVariables.TImeout_6_Seconds);					
					GenericUtils.Verify("for displaying "+sValidation+" as label even after changing the client as Cigna",refSeleniumUtils.is_WebElement_Displayed(StringUtils.replace(oppurtunityLabel, "value", sValidation)));
				} catch (Throwable e) {					
					e.printStackTrace();
				}
				break;
			case "Presentation Profile":			
				String[] sDPLabels="Rules,Information Only,Configuration Only".split(",");
				GenericUtils.Verify("displaying rules as default in dropdown", refSeleniumUtils.is_WebElement_Displayed(StringUtils.replace(Label_With_Contains, "sValue", "DP Type")+"/..//mat-select//span[contains(text(),'Rules')]"));
				for(int i=0;i<sDPLabels.length;i++){					
					//selecting DP Type
					GenericUtils.Verify("clicking on DP Type dropdown", oGenericUtils.clickOnElement(StringUtils.replace(Label_With_Contains, "sValue", "DP Type")+"/..//span"));
					GenericUtils.Verify("clicking on "+sDPLabels[i]+" dropdown option", oGenericUtils.clickOnElement(StringUtils.replace(oCPWPage.Div_contains_Class, "value", "transformPanel")+"//span[contains(text(),'"+sDPLabels[i]+"')]"));
					refSeleniumUtils.defaultWait(ProjectVariables.TImeout_3_Seconds);
					if(refSeleniumUtils.get_Matching_WebElement_count(oPresentationDeck.sDP)>0){						
						Serenity.setSessionVariable(sDPLabels[i]).to(refSeleniumUtils.get_TextFrom_Locator(oPresentationDeck.sDP));
					}else{
						Serenity.setSessionVariable(sDPLabels[i]).to("");
					}					
				}				
				GenericUtils.Verify("clicking on presentation : "+ProjectVariables.MockPresentation, oGenericUtils.clickGivenXpath(StringUtils.replace(oCPWPage.Span_contains_text, "value", ProjectVariables.MockPresentation)));
				refSeleniumUtils.defaultWait(ProjectVariables.TImeout_5_Seconds);
				GenericUtils.Verify("displaying rules as default in dropdown", refSeleniumUtils.is_WebElement_Displayed(StringUtils.replace(Label_With_Contains, "sValue", "DP Type")+"/..//mat-select//span[contains(text(),'Rules')]"));
				for(int i=0;i<sDPLabels.length;i++){					
					//selecting DP Type
					GenericUtils.Verify("clicking on DP Type dropdown", oGenericUtils.clickOnElement(StringUtils.replace(Label_With_Contains, "sValue", "DP Type")+"/..//span"));
					GenericUtils.Verify("clicking on "+sDPLabels[i]+" dropdown option", oGenericUtils.clickOnElement(StringUtils.replace(oCPWPage.Div_contains_Class, "value", "transformPanel")+"//span[contains(text(),'"+sDPLabels[i]+"')]"));
					refSeleniumUtils.defaultWait(ProjectVariables.TImeout_3_Seconds);
					GenericUtils.Verify("clicking on Expand all link", oGenericUtils.clickOnElement("//u[contains(text(),'Expand All')]"));
					if(Serenity.sessionVariableCalled(sDPLabels[i]).toString().isEmpty()){
						if(sDPLabels[i].contains("Information"))
							GenericUtils.Verify("displaying "+ProjectVariables.InformationMockDP+" in "+sDPLabels[i], refSeleniumUtils.is_WebElement_Displayed(StringUtils.replace(Label_With_Contains, "sValue", ProjectVariables.InformationMockDP)));
						else{
							String[] configurationDP=ProjectVariables.ConfigurationMockDP.split(",");
							for(int j=0;j<configurationDP.length;j++){
								GenericUtils.Verify("displaying "+configurationDP[j]+" in "+sDPLabels[i], refSeleniumUtils.is_WebElement_Displayed(StringUtils.replace(Label_With_Contains, "sValue", configurationDP[j])));
							}
						}					
					}else{
						GenericUtils.Verify("displaying "+Serenity.sessionVariableCalled(sDPLabels[i]).toString()+" in "+sDPLabels[i], refSeleniumUtils.is_WebElement_Displayed(StringUtils.replace(Label_With_Contains, "sValue", Serenity.sessionVariableCalled(sDPLabels[i]).toString())));
					}					
				}					
				break;
			case "Cross Over Opportunities":
				switch(sValidation){
				case "Template":
					String[] sTemplateHeaders="Source,Target,Decision Type".split(",");
					for(int i=0;i<sTemplateHeaders.length;i++){
						for(int j=0;j<getOptionsForCrossOverHeaders(sTemplateHeaders[i]).length;j++){
							if(!sTemplateHeaders[i].equals("Decision Type"))
								GenericUtils.Verify("displaying "+getOptionsForCrossOverHeaders(sTemplateHeaders[i])[j].toString()+" under "+sTemplateHeaders[i]+" section", refSeleniumUtils.get_TextFrom_Locator("("+StringUtils.replace(crossOverHeaders, "arg", sTemplateHeaders[i])+")["+(j+1)+"]").trim().equals(getOptionsForCrossOverHeaders(sTemplateHeaders[i])[j]));
							else
								if (j==0)
									GenericUtils.Verify("displaying "+getOptionsForCrossOverHeaders(sTemplateHeaders[i])[j].toString()+" option as checked by default under "+sTemplateHeaders[i]+" section", getDriver().findElement(By.xpath(StringUtils.replace(crossOverOptions, "arg", sTemplateHeaders[i])+"["+(j+1)+"]")).getText().trim().equals(getOptionsForCrossOverHeaders(sTemplateHeaders[i])[j]));
								else
									GenericUtils.Verify("displaying "+getOptionsForCrossOverHeaders(sTemplateHeaders[i])[j].toString()+" option as checked by default under "+sTemplateHeaders[i]+" section", refSeleniumUtils.get_TextFrom_Locator(StringUtils.replace(crossOverOptions, "arg", sTemplateHeaders[i])+"["+(j+1)+"]/../span").trim().equals(getOptionsForCrossOverHeaders(sTemplateHeaders[i])[j]));
						}							
					}
					GenericUtils.Verify("displaying Apply button", refSeleniumUtils.is_WebElement_Displayed(StringUtils.replace(oCPWPage.ButtonWithText, "svalue", "Apply")));
					GenericUtils.Verify("displaying Reset button", refSeleniumUtils.is_WebElement_Displayed(StringUtils.replace(oCPWPage.ButtonWithText, "svalue", "Reset")));
					GenericUtils.Verify("clicking on Filter Icon", oGenericUtils.clickGivenXpath(StringUtils.replace(oCPWPage.ButtonContainsClass, "value", "filterimage")));
					refSeleniumUtils.defaultWait(ProjectVariables.TImeout_3_Seconds);					
					GenericUtils.Verify("collapsing filters in cross over opportunities",!refSeleniumUtils.is_WebElement_Displayed(StringUtils.replace(crossOverHeaders, "arg", "Source")));
					GenericUtils.Verify("clicking on Filter Icon", oGenericUtils.clickGivenXpath(StringUtils.replace(oCPWPage.ButtonContainsClass, "value", "filterimage")));
					refSeleniumUtils.defaultWait(ProjectVariables.TImeout_3_Seconds);
					break;	
				case "Hierarchical View":
					String Xpath="("+decisionGrid+"//label[not(contains(@class,'checkbox'))])";
					GenericUtils.Verify("displaying 'X' icon", refSeleniumUtils.is_WebElement_Displayed("//ul[@class='toolkit']//i"));
					GenericUtils.Verify("displaying FINALIZE button", refSeleniumUtils.is_WebElement_Displayed(StringUtils.replace(oCPWPage.ButtonContainsText, "value", "FINALIZE")));
					GenericUtils.Verify("displaying hierarchy as Medical Policy",refSeleniumUtils.get_TextFrom_Locator(Xpath+"[1]").contains("Medical Policy"));
					GenericUtils.Verify("displaying hierarchy as Topic",refSeleniumUtils.get_TextFrom_Locator(Xpath+"[1]").contains("Topic"));
					String[] sHeaders=ProjectVariables.DecisionGridHeader.split(",");
					for(int i=1;i<sHeaders.length;i++){
						GenericUtils.Verify("displaying decision grid header as:"+sHeaders[i], refSeleniumUtils.get_TextFrom_Locator(decisionGridHeader+"["+i+"]").trim().equals(sHeaders[i]));
					}
					String rowXpath=decisionGrid+"//tr[not(contains(@class,'header'))]";					
					for(int i=1;i<=refSeleniumUtils.get_Matching_WebElement_count(rowXpath);i++){
						String colXpath=decisionGrid+"//tr["+i+"]//td[not(contains(@class,'select'))]";
						for(int j=1;j<=refSeleniumUtils.get_Matching_WebElement_count(colXpath);j++){
							if(j==1){
								GenericUtils.Verify("Column "+sHeaders[j]+" with assign icon as non editable for the row:"+i,refSeleniumUtils.is_WebElement_Displayed(colXpath+"["+j+"]//i[contains(@class,'assignIcon')]"));
							}else if(j==4)
								GenericUtils.Verify("Column "+sHeaders[j]+" as editable for the row:"+i, refSeleniumUtils.is_WebElement_Displayed(colXpath+"["+j+"]//mat-form-field[contains(@class,'input')]"));								
							else
								GenericUtils.Verify("Column "+sHeaders[j]+" as non editable for the row:"+i, !refSeleniumUtils.is_WebElement_Displayed(colXpath+"["+j+"]//mat-form-field[contains(@class,'input')]"));								
						}
					}
					String randomString="AutoTest #$*@112233 "+RandomStringUtils.randomAlphanumeric(25).toUpperCase();		
					GenericUtils.Verify("entering random string in Comments section", refSeleniumUtils.Enter_given_Text_Element(decisionGrid+"//tr[1]//td[not(contains(@class,'select'))][4]//input", randomString));
					refSeleniumUtils.clickGivenXpath(decisionGrid+"//tr[1]//td[not(contains(@class,'select'))][5]"); 					
					WebElement element = getDriver().findElement(By.xpath(decisionGrid+"//tr[1]//td[not(contains(@class,'select'))][4]//mat-form-field")); 
					GenericUtils.Verify("displaying mouse hover text as : "+randomString, refSeleniumUtils.moveToElement(element));
					
					break;
				}
				break;
			case "Change Opportunities":
				switch(sValidation){
				case "DP List":
					refSeleniumUtils.defaultWait(ProjectVariables.TImeout_5_Seconds);
					String[] sDP=Arrays.stream(refSeleniumUtils.get_All_Text_from_Locator(StringUtils.replace(changeOpportunityGridCell, "value", "grid-cell")+"[@columnindex='2']/div")).
						filter(value -> value != null && value.length() > 0).toArray(size -> new String[size]);
					for(int i=0;i<sDP.length;i++){
						GenericUtils.Verify("displaying expand icon for DP:"+sDP[i], refSeleniumUtils.is_WebElement_Displayed("("+StringUtils.replace(changeOpportunityGridCell, "value", "collapse")+")["+(i+1)+"]"));
						GenericUtils.Verify("displaying checkbox for DP:"+sDP[i], refSeleniumUtils.is_WebElement_Displayed("("+StringUtils.replace(changeOpportunityGridCell, "value", "checkbox")+"[contains(@id,'jqxWidget')])["+(i+1)+"]"));
					}
					String[] Validation="Selection,De-selection".split(",");
					for(int j=0;j<Validation.length;j++){
						GenericUtils.Verify("clicking on DP header level checkbox", refSeleniumUtils.clickGivenXpath(dpHeaderCheckbox));				
						if(j==0)
							GenericUtils.Verify("displaying DP header level checkbox as selected", refSeleniumUtils.Get_Value_By_given_attribute("aria-checked",dpHeaderCheckbox).equals("true"));
						else
							GenericUtils.Verify("displaying DP header level checkbox as not selected", refSeleniumUtils.Get_Value_By_given_attribute("aria-checked",dpHeaderCheckbox).equals("false"));
						for(int i=0;i<sDP.length;i++){
							if (j==0){								
								GenericUtils.Verify("selecting checkbox for the DP:"+sDP[i], refSeleniumUtils.Get_Value_By_given_attribute("checked", "("+StringUtils.replace(changeOpportunityGridCell, "value", "checkbox")+"[contains(@id,'jqxWidget')])["+(i+1)+"]").equals("true"));
							}else
								GenericUtils.Verify("de-selecting checkbox for the DP:"+sDP[i], refSeleniumUtils.is_WebElement_Displayed("("+StringUtils.replace(changeOpportunityGridCell, "value", "checkbox")+"[contains(@id,'jqxWidget')][not(contains(@checked,'true'))])["+(i+1)+"]"));
						}
					}
					GenericUtils.Verify("selecting single checkbox for DP:"+sDP[0], refSeleniumUtils.clickGivenXpath("("+StringUtils.replace(changeOpportunityGridCell, "value", "checkbox-default")+")[1]"));
					refSeleniumUtils.defaultWait(ProjectVariables.TImeout_2_Seconds);
					GenericUtils.Verify("clicking on expand icon for DP:"+sDP[0], refSeleniumUtils.clickGivenXpath("("+StringUtils.replace(changeOpportunityGridCell, "value", "collapse")+")[1]"));
					refSeleniumUtils.defaultWait(ProjectVariables.TImeout_2_Seconds);
					GenericUtils.Verify("expanding DP:"+sDP[0], refSeleniumUtils.is_WebElement_Displayed("("+StringUtils.replace(changeOpportunityGridCell, "value", "expand")+")[1]"));
					break;
				}
				break;
			default:Assert.assertTrue("Invalid case selection",false);
			}
			
		}
		

	public void userVerifyEditandDeletefunctionalityinTopicDesc(String operation)
	{
		/*Serenity.setSessionVariable("clientkey").to("122");
		Serenity.setSessionVariable("Topic").to("Impacted Cerumen Removal");
		Serenity.setSessionVariable("PresentationName").to("TestPres6889");
		*/
		boolean bstatus=false;
		String sDBTopicLongDesc=null;
		String sUITopicLongDesc=null;
		String sUIEditTopic=null;
		String sTopic=Serenity.sessionVariableCalled("Topic");
		String sPresename=Serenity.sessionVariableCalled("PresentationName");
		
		//To open edit topic popup
		bstatus=refSeleniumUtils.clickGivenXpath(presTopicediticon);
		GenericUtils.Verify("Topic edit icon should be clicked for Topic::"+sTopic+",at prese::"+sPresename, bstatus);
		refSeleniumUtils.defaultWait(ProjectVariables.TImeout_3_Seconds);
		//verify edittopic popup is opened
		bstatus=refSeleniumUtils.is_WebElement_Displayed(StringUtils.replace(oCPWPage.labelcontainstext, "svalue", "Edit Topic Description"));
		GenericUtils.Verify("Edit window should be opened after clicking on editicon of Topic::"+sTopic+",at prese::"+sPresename, bstatus);
		//verifying all the buttons should displayed as default
		bstatus=refSeleniumUtils.is_WebElement_Displayed(StringUtils.replace(oCPWPage.ButtonContainsText, "value", "Save")+"[@aria-disabled='true']");
		GenericUtils.Verify("Save button should be disabled by default in EditTopic window for Topic::"+sTopic+",at prese::"+sPresename, bstatus);
		bstatus=refSeleniumUtils.is_WebElement_Displayed(StringUtils.replace(oCPWPage.ButtonContainsText, "value", "Submit")+"[@aria-disabled='true']");
		GenericUtils.Verify("Submit button should be disabled by default in EditTopic window for Topic::"+sTopic+",at prese::"+sPresename, bstatus);
		bstatus=refSeleniumUtils.is_WebElement_Displayed(StringUtils.replace(oCPWPage.Div_contains_Class, "value", "footer")+StringUtils.replace(oCPWPage.ButtonContainsText, "value", "Cancel")+"[@aria-disabled='false']");
		GenericUtils.Verify("Cancel button should be enabled by default in EditTopic window for Topic::"+sTopic+",at prese::"+sPresename, bstatus);
		
		//verifying the UItopic longdesc with DB
		sDBTopicLongDesc=MongoDBUtils.retrieveEditTopicDescrDBValues("", sPresename, sTopic,"ValueToFetch-TopicDescription", "").trim();
		sDBTopicLongDesc = sDBTopicLongDesc.replaceAll("[\\\r\\\n]+", "");
		sDBTopicLongDesc = sDBTopicLongDesc.replaceAll("[\\n\\t ]", "");
		
		String sUITopic=refSeleniumUtils.get_TextFrom_Locator("//pre//span").trim();
		sUITopicLongDesc = sUITopic.replaceAll("[\\\r\\\n]+", "");
		sUITopicLongDesc = sUITopicLongDesc.replaceAll("[\\n\\t ]", "");
		
		bstatus=sUITopicLongDesc.equalsIgnoreCase(sDBTopicLongDesc);
		GenericUtils.Verify("UITopicDesc(sysytem,readonly)::"+sUITopicLongDesc+",DB TopicDesc::"+sDBTopicLongDesc+",in EditTopic window for Topic::"+sTopic+",at prese::"+sPresename, bstatus);
		
		switch(operation)
		{
		case "Edit":
			//veifying edit fucntionality in edit topicpopup
			refSeleniumUtils.Enter_given_Text_Element("//textarea[contains(@class,'description')]", sUITopic+" "+ProjectVariables.DispositionNotes);
			
			//verifying the UI Edit topic longdesc with expected
			sUIEditTopic=refSeleniumUtils.get_TextFrom_Locator("//pre//ins").trim();
			bstatus=sUIEditTopic.equalsIgnoreCase(ProjectVariables.DispositionNotes);
			GenericUtils.Verify("UI EditTopicDesc(sysytem,readonly)::"+sUIEditTopic+",Expected EditTopic::"+ProjectVariables.DispositionNotes+",in EditTopic window without saving for Topic::"+sTopic+",at prese::"+sPresename, bstatus);
			
		break;
		case "Delete":
			String deleTopicDesc=sUITopic.substring(0, 5).trim();
			String deleReqTopicDesc=StringUtils.substringAfter(sUITopic, deleTopicDesc);
			
			//veifying edit fucntionality in edit topicpopup
			refSeleniumUtils.Enter_given_Text_Element("//textarea[contains(@class,'description')]", deleReqTopicDesc);
					
			//verifying the deleted UI Edit topic longdesc with expected
			sUIEditTopic=refSeleniumUtils.get_TextFrom_Locator("//pre//del").trim();
			bstatus=sUIEditTopic.equalsIgnoreCase(deleTopicDesc);
			GenericUtils.Verify("UI deleted EditTopicDesc(sysytem,readonly)::"+sUIEditTopic+",DB EditTopic::"+sDBTopicLongDesc+",in deleted EditTopic window after saving for Topic::"+sTopic+",at prese::"+sPresename, bstatus);
			
		break;
		default:
			Assert.assertTrue("case not found::"+operation, false);
		break;
		}
		
		//To close edit topic popup
		oGenericUtils.clickOnElement(StringUtils.replace(oCPWPage.ButtonContainsText, "value", "Save"));
		oGenericUtils.clickOnElement(StringUtils.replace(oCPWPage.ButtonContainsText, "value", "Submit"));
		refSeleniumUtils.defaultWait(ProjectVariables.TImeout_5_Seconds);
		//To open edit topic popup
		bstatus=refSeleniumUtils.clickGivenXpath(presTopicediticon);
		GenericUtils.Verify("Topic edit icon should be clicked for Topic::"+sTopic+",at prese::"+sPresename, bstatus);
		refSeleniumUtils.defaultWait(ProjectVariables.TImeout_3_Seconds);
	
		//verifying the UI Edit topic longdesc with expected
		//verifying the UItopic longdesc with DB
		sDBTopicLongDesc=MongoDBUtils.retrieveEditTopicDescrDBValues("", sPresename, sTopic,"ValueToFetch-TopicDescription", "").trim();
		sDBTopicLongDesc = sDBTopicLongDesc.replaceAll("[\\\r\\\n]+", "");
		sDBTopicLongDesc = sDBTopicLongDesc.replaceAll("[\\n\\t ]", "");
		
		
		if(operation.equalsIgnoreCase("Edit"))
		{
			sUIEditTopic=refSeleniumUtils.get_TextFrom_Locator("//pre//span").trim();
			sUIEditTopic=sUIEditTopic+refSeleniumUtils.get_TextFrom_Locator("//pre//ins").trim();	
		}
		else
		{
			sUIEditTopic="";
			int uitopicsize=refSeleniumUtils.get_Matching_WebElement_count("//pre//span");
			for (int i = 1; i <= uitopicsize; i++) 
			{
				sUIEditTopic=sUIEditTopic+refSeleniumUtils.get_TextFrom_Locator("(//pre//span)["+i+"]").trim();	
			}
			
			//sUIEditTopic=sUIEditTopic+refSeleniumUtils.get_TextFrom_Locator("//pre//del").trim();
		}
		sUIEditTopic = sUIEditTopic.replaceAll("[\\\r\\\n]+", "");
		sUIEditTopic = sUIEditTopic.replaceAll("[\\n\\t ]", "");
		
		
		bstatus=sUIEditTopic.equalsIgnoreCase(sDBTopicLongDesc);
		GenericUtils.Verify("UI EditTopicDesc(sysytem,readonly)::"+sUIEditTopic+",DB EditTopic::"+sDBTopicLongDesc+",in EditTopic window after saving for Topic::"+sTopic+",at prese::"+sPresename, bstatus);
		
		
		
	}

		private String[] getOptionsForCrossOverHeaders(String sHeader){
			HashMap<String,String[]> sOptions = new HashMap<String,String[]>();
			sOptions.put("Source", new String[] {"Payer Shorts","LOB","Claim Type"});
			sOptions.put("Target", new String[] {"Payer Shorts","LOB","Claim Type"});
			sOptions.put("Decision Type", new String[] {"All","Approve","Approve with Mods","Reject","Test Only","Follow-Up","Defer","Suppress"});
			return sOptions.get(sHeader);
		}


		public void userValidatesDPAssignmentforProfileWithDPType(String sProfile, String sDPType) throws InterruptedException {
			
			if(sProfile.equals("ChangedMP")){
				oGenericUtils.clickGivenXpath(oFilterDrawer.sMedPoliciesAllCheckBox);
				oCPWPage.Enter_the_given_MP_Topic_in_filter_Drawer(sProfile.split(":")[1]);
				oFilterDrawer.user_selects_all_MedicalPolicies();
				oFilterDrawer.user_filters_by_clicking_on_Apply_for_Medical_Policy_Topic();
				sProfile="New MP Level";
			}else{
				if(sProfile.contains("New"))
					oGenericUtils.clickOnElement("//span[contains(text(),'NPP Opportunities')]/span");
					refSeleniumUtils.defaultWait(ProjectVariables.TImeout_5_Seconds);
				oPresentationProfileValidations.PresentationProfileValidations("CREATE", "", "");
			}
			String[] DPType=ProjectVariables.DPTypeOptions.split(",");
			String[] sValidation=sDPType.split(",");
			assigningDPValidations("PreAssignment","");
			refSeleniumUtils.defaultWait(ProjectVariables.TImeout_3_Seconds);
			Serenity.setSessionVariable("Rules").to(refSeleniumUtils.getTexFfromLocator(unassignedDP+"[1]"));
			switch(sProfile){
			case "DP+PPS Partial":
				GenericUtils.Verify("clicking on DP:"+Serenity.sessionVariableCalled("Rules"), oGenericUtils.clickOnElement(unassignedDP+"[1]"));
				GenericUtils.Verify("PPS Selection", oGenericUtils.clickOnElement(sPPSSelection+"[1]"));
				GenericUtils.Verify("clicking on DP Level assign icon", oGenericUtils.clickGivenXpath(DPLevelAssign));
				assigningDPValidations("assignDP",sProfile);
				break;
			case "DP+PPS With All DPTypes":
			case "New DP+PPS With All DPTypes":
				GenericUtils.Verify("clicking on DP:"+Serenity.sessionVariableCalled("Rules"), oGenericUtils.clickOnElement(unassignedDP+"[1]"));
				GenericUtils.Verify("All PPS Selection", oGenericUtils.clickOnElement(allPPS));
				GenericUtils.Verify("clicking on DP Level assign icon", oGenericUtils.clickGivenXpath(DPLevelAssign));
				assigningDPValidations("assignDP",sProfile);
				assigningDPValidations("PostAssignment",sProfile);
				break;		
			case "DP Level":
			case "New DP Level":
				GenericUtils.Verify("clicking on DP:"+Serenity.sessionVariableCalled("Rules"), oGenericUtils.clickOnElement(unassignedDP+"[1]/../preceding-sibling::div"));
				GenericUtils.Verify("clicking on DP Level assign icon", oGenericUtils.clickGivenXpath(headerLevelAssign));
				assigningDPValidations("assignDP",sProfile);
				assigningDPValidations("PostAssignment",sProfile);
				break;
			case "Topic Level":
			case "New Topic Level":
				if(sProfile.contains("New"))
					GenericUtils.Verify("clicking on Topic Level assign icon", oGenericUtils.clickGivenXpath(topicLevelAssign+"[2]"));
				else
					GenericUtils.Verify("clicking on Topic Level assign icon", oGenericUtils.clickGivenXpath(topicLevelAssign+"[1]"));
				assigningDPValidations("assignDP",sProfile);
				assigningDPValidations("PostAssignment",sProfile);
				break;
			case "MP Level":
			case "New MP Level":
				GenericUtils.Verify("clicking on MP Level assign icon", oGenericUtils.clickGivenXpath(StringUtils.replace(topicLevelAssign, "topic", "policy")+"[1]"));
				assigningDPValidations("assignDP",sProfile);
				assigningDPValidations("PostAssignment","");
				break;
			}
			//validating assigned DP's count
			if(sProfile.contains("DP") && !sProfile.contains("New")){
				refSeleniumUtils.moveToElement($(StringUtils.replace(profileMouseHover, "value", Serenity.sessionVariableCalled("sPPName"))));
				GenericUtils.Verify("DP Count including All(Rules,Information and Configuration Only) as:"+Serenity.sessionVariableCalled("DPCount").toString(), 
						getDriver().findElement(By.xpath(StringUtils.replace(profileMouseHover, "value", Serenity.sessionVariableCalled("sPPName")))).getAttribute("innerHTML").
						contains(Serenity.sessionVariableCalled("DPCount").toString()+" DP(s)"));
			}			

			//validating assigned DP's  in created profile
			oGenericUtils.clickGivenXpath(StringUtils.replace(PresentationTabName, "PresNameArg", Serenity.sessionVariableCalled("sPPName")));
			refSeleniumUtils.defaultWait(ProjectVariables.TImeout_3_Seconds);
			for(int i=0;i<DPType.length;i++){					
				GenericUtils.Verify("clicking on DP Type dropdown", oGenericUtils.clickOnElement(StringUtils.replace(Label_With_Contains, "sValue", "DP Type")+"/..//span"));
				GenericUtils.Verify("clicking on "+DPType[i]+" dropdown option", oGenericUtils.clickOnElement(StringUtils.replace(oCPWPage.Div_contains_Class, "value", "transformPanel")+"//span[contains(text(),'"+DPType[i]+"')]"));
				refSeleniumUtils.defaultWait(ProjectVariables.TImeout_3_Seconds);
				if(!sValidation[i].equals("No DP")){
					if(i==0)
						GenericUtils.Verify("displaying "+Serenity.sessionVariableCalled(sValidation[i])+" under "+DPType[i], oGenericUtils.is_WebElement_Displayed(StringUtils.replace(Label_With_Contains, "sValue", "DP Type")+"/..//span"));
					else{						
						String[] Expected= Serenity.sessionVariableCalled(DPType[i]);
						String[] Actual= refSeleniumUtils.get_All_Text_from_Locator_Without_Null_Values("//label[@class='dpIdLabel']");
						Arrays.sort(Expected);
						Arrays.sort(Actual);
						GenericUtils.Verify("displaying DP under "+DPType[i]+" Expected:"+Arrays.toString(Expected)+" Actual:"+Arrays.toString(Actual),Arrays.equals(Expected,Actual));					
					}
				}else{
					GenericUtils.Verify("not displaying any DP's under"+DPType[i],refSeleniumUtils.is_WebElement_Displayed(StringUtils.replace(oCPWPage.Div_contains_Class, "value", "no-opp-msg")));
				}
			}
			
		}
		
		private void assigningDPValidations(String validationType,String sProfile){
			switch(validationType){
			case "assignDP":
				//selecting newly created profile
				GenericUtils.Verify("selecting profile:"+Serenity.sessionVariableCalled("sPPName").toString(), 
						oGenericUtils.clickGivenXpath(StringUtils.replace(profileRadioButton, "value", Serenity.sessionVariableCalled("sPPName").toString())));
				if(sProfile.contains("New"))
					refSeleniumUtils.clickGivenXpath(assignPopupOkay+"[2]");
				else
					refSeleniumUtils.clickGivenXpath(assignPopupOkay);
				refSeleniumUtils.defaultWait(ProjectVariables.TImeout_10_Seconds);
				//validating after assigning
				if(!refSeleniumUtils.is_WebElement_Displayed(StringUtils.replace(dpFlipper, "value", Serenity.sessionVariableCalled("Rules"))+"//div[@class='flipper rotate']"))
					oGenericUtils.clickGivenXpath(StringUtils.replace(dpFlipper, "value", Serenity.sessionVariableCalled("Rules")));
				GenericUtils.Verify("assigning "+Serenity.sessionVariableCalled("Rules")+" to profile:"+Serenity.sessionVariableCalled("sPPName").toString(),
						Arrays.asList(refSeleniumUtils.get_All_Text_from_Locator(StringUtils.replace(dpFlipper, "value", Serenity.sessionVariableCalled("Rules"))+"//li")).contains(Serenity.sessionVariableCalled("sPPName").toString()));
				break;
			case "PreAssignment":
			case "PostAssignment":
			case "Delete PostAssignment":
				int count=0;
				String[] DPType=ProjectVariables.DPTypeOptions.split(",");
				for(int i=1;i<DPType.length;i++){
					GenericUtils.Verify("clicking on DP Type dropdown", oGenericUtils.clickOnElement(StringUtils.replace(Label_With_Contains, "sValue", "DP Type")+"/..//span"));
					GenericUtils.Verify("clicking on "+DPType[i]+" dropdown option", oGenericUtils.clickOnElement(StringUtils.replace(oCPWPage.Div_contains_Class, "value", "transformPanel")+"//span[contains(text(),'"+DPType[i]+"')]"));
					refSeleniumUtils.defaultWait(ProjectVariables.TImeout_10_Seconds);
					if(refSeleniumUtils.is_WebElement_Displayed(StringUtils.replace(medicalPolicy, "value", Serenity.sessionVariableCalled("MedicalPolicy")))){
						if(i==1){
							refSeleniumUtils.clickGivenXpath("//a[text()='Collapse All']");
							refSeleniumUtils.defaultWait(ProjectVariables.TImeout_3_Seconds);
							oGenericUtils.clickGivenXpath(StringUtils.replace(medicalPolicy, "value", Serenity.sessionVariableCalled("MedicalPolicy"))+"/../..//mat-icon");
							for(int j=1;j<=oGenericUtils.get_Matching_WebElement_count("("+StringUtils.replace(medicalPolicy, "value", Serenity.sessionVariableCalled("MedicalPolicy"))+"/../../../..//following-sibling::div)[2]//label[contains(text(),'Topic')]/../..//mat-icon");j++){
								oGenericUtils.clickGivenXpath("(("+StringUtils.replace(medicalPolicy, "value", Serenity.sessionVariableCalled("MedicalPolicy"))+"/../../../..//following-sibling::div)[2]//label[contains(text(),'Topic')]/../..//mat-icon)["+j+"]");	
							}
							
						}							
						if(validationType.contains("Pre")){
							Serenity.setSessionVariable(DPType[i]).to(refSeleniumUtils.get_All_Text_from_Locator_Without_Null_Values("("+StringUtils.replace(medicalPolicy, "value", Serenity.sessionVariableCalled("MedicalPolicy"))+"/../../../..//following-sibling::div)[2]//mat-card//label[@aria-describedby]"));
						}else{
							String[] DPs=refSeleniumUtils.get_All_Text_from_Locator_Without_Null_Values("("+StringUtils.replace(medicalPolicy, "value", Serenity.sessionVariableCalled("MedicalPolicy"))+"/../../../..//following-sibling::div)[2]//mat-card//label[@aria-describedby]");									
							count=count+DPs.length;
							for(int j=0;j<DPs.length;j++){
								if(validationType.equals("Delete PostAssignment")||(!sProfile.equals("New Topic Level") && sProfile.contains("New") && DPType[i].contains("Configuration")))
									GenericUtils.Verify("Unassigning "+DPs[j]+" to profile "+Serenity.sessionVariableCalled("sPPName").toString(),!Arrays.asList(refSeleniumUtils.get_All_Text_from_Locator_Without_Null_Values("//label[text()='"+DPs[j]+"']/../../../..//following-sibling::mat-card-content//li"))
											.contains(Serenity.sessionVariableCalled("sPPName").toString()));
								else
									GenericUtils.Verify("assigning "+DPs[j]+" to profile "+Serenity.sessionVariableCalled("sPPName").toString(),Arrays.asList(refSeleniumUtils.get_All_Text_from_Locator_Without_Null_Values("//label[text()='"+DPs[j]+"']/../../../..//following-sibling::mat-card-content//li"))
											.contains(Serenity.sessionVariableCalled("sPPName").toString()));
							}
						}
					}
				}
				Serenity.setSessionVariable("DPCount").to(count+1);
				if(validationType.contains("Pre")){
					GenericUtils.Verify("clicking on DP Type dropdown", oGenericUtils.clickOnElement(StringUtils.replace(Label_With_Contains, "sValue", "DP Type")+"/..//span"));
					GenericUtils.Verify("clicking on Rules dropdown option", oGenericUtils.clickOnElement(StringUtils.replace(oCPWPage.Div_contains_Class, "value", "transformPanel")+"//span[contains(text(),'Rules')]"));
				}
				break;
			}
			
		}

		public void userValidtesFunctionalityForDPType(String sValidation, String sDPType,String sFunctionality) throws InterruptedException {
			switch(sValidation){
			case "Re-Assign":
				String[] sAssignments="DP+PPS Partial,DP+PPS With All DPTypes,DP Level,Topic Level,MP Level".split(",");
				for(int i=0;i<sAssignments.length;i++){
					if(sAssignments[i].equals("DP+PPS Partial"))
						userValidatesDPAssignmentforProfileWithDPType("DP+PPS With All DPTypes",sDPType);
					Serenity.setSessionVariable("oldPresentation").to(Serenity.sessionVariableCalled("sPPName"));
					oPresentationProfileValidations.PresentationProfileValidations("CREATE", "", "");
					assignConfigurationDPsToProfile(sAssignments[i],sValidation,sFunctionality);
					GenericUtils.Verify("not displaying PPS:"+Serenity.sessionVariableCalled("Payer Shorts")+Serenity.sessionVariableCalled("ClaimType")+" at "+sAssignments[i]+" on "+sValidation, 
							!refSeleniumUtils.is_WebElement_Displayed(StringUtils.replace(oCPWPage.Span_contains_text, "value", Serenity.sessionVariableCalled("sPayer"))+"/span[contains(text(),'["+Serenity.sessionVariableCalled("sClaimType")+"]')]"));
					oGenericUtils.clickGivenXpath(StringUtils.replace(PresentationTabName, "PresNameArg", Serenity.sessionVariableCalled("sPPName")));
					refSeleniumUtils.defaultWait(ProjectVariables.TImeout_5_Seconds);
					GenericUtils.Verify("clicking on DP Type dropdown", oGenericUtils.clickOnElement(StringUtils.replace(Label_With_Contains, "sValue", "DP Type")+"/..//span[@class='form-select']"));
					GenericUtils.Verify("clicking on Configuration dropdown option", oGenericUtils.clickOnElement(StringUtils.replace(oCPWPage.Div_contains_Class, "value", "transformPanel")+"//span[contains(text(),'"+sFunctionality+"')]"));
					refSeleniumUtils.defaultWait(ProjectVariables.TImeout_5_Seconds);
					GenericUtils.Verify("clicking on DP:", oGenericUtils.clickOnElement(DP+"[1]"));
					GenericUtils.Verify("displaying PPS:"+Serenity.sessionVariableCalled("Payer Shorts")+Serenity.sessionVariableCalled("ClaimType")+" at "+sAssignments[i]+" on "+sValidation, 
							refSeleniumUtils.is_WebElement_Displayed(StringUtils.replace(oCPWPage.Span_contains_text, "value", Serenity.sessionVariableCalled("Payer Shorts"))+"/span[contains(text(),'["+Serenity.sessionVariableCalled("ClaimType")+"]')]"));					
				}
				break;
			case "Unassign":
				userValidatesDPAssignmentforProfileWithDPType("DP+PPS With All DPTypes","Rules,Information Only,Configuration Only");			
				switch(sDPType){
				case "DP+PPS Partial":						
					assignConfigurationDPsToProfile(sDPType,sValidation,sFunctionality);
					refSeleniumUtils.defaultWait(ProjectVariables.TImeout_5_Seconds);
					GenericUtils.Verify("not displaying PPS:"+Serenity.sessionVariableCalled("Payer Shorts"), 
							!refSeleniumUtils.is_WebElement_Displayed(StringUtils.replace(oCPWPage.Span_contains_text, "value", Serenity.sessionVariableCalled("Payer Shorts"))+"/span[contains(text(),'["+Serenity.sessionVariableCalled("ClaimType")+"]')]"));
					ppsSelection(sDPType,sFunctionality);
					GenericUtils.Verify("Unassigning "+Serenity.sessionVariableCalled("DP")+" to profile "+Serenity.sessionVariableCalled("sPPName").toString(),refSeleniumUtils.get_Matching_WebElement_count("//label[text()='"+Serenity.sessionVariableCalled("DP")+"']/../../../..//following-sibling::mat-card-content//li")
							==0);
					break;
				case "DP+PPS With All DPTypes":
					GenericUtils.Verify("clicking on DP",oGenericUtils.clickOnElement(DP+"[1]"));
				case "DP Level":
				case "Topic Level":
				case "MP Level":
					assignConfigurationDPsToProfile(sDPType,sValidation,sFunctionality);
					GenericUtils.Verify("not displaying any DP's under "+sFunctionality+" Only",refSeleniumUtils.is_WebElement_Displayed(StringUtils.replace(oCPWPage.Div_contains_Class, "value", "no-opp-msg")));
					ppsSelection(sDPType,sFunctionality);
					GenericUtils.Verify("Unassigning DP to profile "+Serenity.sessionVariableCalled("sPPName").toString(),refSeleniumUtils.get_Matching_WebElement_count("//label[text()='"+Serenity.sessionVariableCalled("Rules")+"']/../../../..//following-sibling::mat-card-content//li")
							==0);
					break;
				}
				//deleting information and configuration DP's from backend
				//MongoDBUtils.removeFirstDocument("cpd", "categorizeOppty", MonGoDBQueries.FilterMongoDBQuery("Delete Presentation"));
				break;
			case "All Decisions":
			case "Capture":
				userValidatesDPAssignmentforProfileWithDPType(sDPType,"Rules,Information Only,Configuration Only");
				GenericUtils.Verify("clicking on DP",oGenericUtils.clickOnElement(DP+"[1]"));				
				if(sValidation.equals("All Decisions")){
					String [] sDecision= "Approve,Approve with Mod,Reject,Defer,Follow up,No Decision".split(",");
					for(int i=0;i<sDecision.length;i++){
						Serenity.setSessionVariable("PayerShorts_"+sDecision[i]).to(getDriver().findElements(By.xpath("//tbody[@role ='rowgroup']/tr["+(i+1)+"]/td[1]/label/span")).get(0).getText().split(" ")[0].trim());
						Serenity.setSessionVariable("ClaimType_"+sDecision[i]).to(StringUtils.substringBetween(getDriver().findElements(By.xpath("//tbody[@role ='rowgroup']/tr["+(i+1)+"]/td[1]/label/span/span")).get(0).getText(), "[","]").trim());
						if(!sDecision[i].equals("No Decision")){
							GenericUtils.Verify("PPS Selection", oGenericUtils.clickOnElement(sPPSSelection+"["+(i+1)+"]"));
							GenericUtils.Verify("clicking on DP Level assign icon", oGenericUtils.clickGivenXpath(StringUtils.replace(oCPWPage.ButtonContainsClass, "value", "captureIcon")));
							captureDecision(sDecision[i]);
						}
						refSeleniumUtils.defaultWait(ProjectVariables.TImeout_5_Seconds);
					}
					String[] assignType = "Unassign,Re-Assign".split(",");
					for(int j=0;j<assignType.length;j++){
						if(assignType[j].equals("Re-Assign"))
							oPresentationProfileValidations.PresentationProfileValidations("CREATE", "", "");
						assignConfigurationDPsToProfile("",assignType[j],"Configuration Only");
						for(int i=0;i<sDecision.length;i++){
							if(sDecision[i].equalsIgnoreCase("Follow Up") || sDecision[i].equalsIgnoreCase("No Decision"))
								GenericUtils.Verify(assignType[j]+"ing and not displaying PPS:"+Serenity.sessionVariableCalled("PayerShorts_"+sDecision[i])+"["+Serenity.sessionVariableCalled("ClaimType_"+sDecision[i])+"]", 
									!refSeleniumUtils.is_WebElement_Displayed(StringUtils.replace(oCPWPage.Span_contains_text, "value", Serenity.sessionVariableCalled("PayerShorts_"+sDecision[i]))+"/span[contains(text(),'["+Serenity.sessionVariableCalled("ClaimType_"+sDecision[i])+"]')]"));
							else
								GenericUtils.Verify("not "+assignType[j]+"ing and displaying PPS:"+Serenity.sessionVariableCalled("PayerShorts_"+sDecision[i])+"["+Serenity.sessionVariableCalled("ClaimType_"+sDecision[i])+"]", 
									refSeleniumUtils.is_WebElement_Displayed(StringUtils.replace(oCPWPage.Span_contains_text, "value", Serenity.sessionVariableCalled("PayerShorts_"+sDecision[i]))+"/span[contains(text(),'["+Serenity.sessionVariableCalled("ClaimType_"+sDecision[i])+"]')]"));
						}
					}
					oGenericUtils.clickGivenXpath(StringUtils.replace(PresentationTabName, "PresNameArg", Serenity.sessionVariableCalled("sPPName")));
					GenericUtils.Verify("clicking on DP Type dropdown", oGenericUtils.clickOnElement(StringUtils.replace(Label_With_Contains, "sValue", "DP Type")+"/..//span"));
					GenericUtils.Verify("clicking on Configuration Only dropdown option", oGenericUtils.clickOnElement(StringUtils.replace(oCPWPage.Div_contains_Class, "value", "transformPanel")+"//span[contains(text(),'Configuration Only')]"));
					GenericUtils.Verify("not Re-assigning and displaying PPS",refSeleniumUtils.is_WebElement_Displayed(StringUtils.replace(oCPWPage.Div_contains_Class, "value", "no-opp-msg")));
				}else{
					GenericUtils.Verify("displaying Previous button", oGenericUtils.is_WebElement_Displayed(StringUtils.replace(oCPWPage.ButtonWithText, "svalue", "Previous")));
					GenericUtils.Verify("displaying Next button", oGenericUtils.is_WebElement_Displayed(StringUtils.replace(oCPWPage.ButtonWithText, "svalue", "Next")));
					String Xpath="(//mat-expansion-panel//span[@class='mat-button-wrapper'])";
					oGenericUtils.clickGivenXpath(Xpath);
					System.out.println(oGenericUtils.get_Matching_WebElement_count(Xpath));
					for(int i=2;i<=oGenericUtils.get_Matching_WebElement_count(Xpath);i++){
						refSeleniumUtils.defaultWait(ProjectVariables.TImeout_5_Seconds);
						refSeleniumUtils.clickOnGivenElemntByJavaScript(Xpath+"["+i+"]");
					}
					String[] DP=oGenericUtils.get_All_Text_from_Locator("//mat-expansion-panel//label");
					if(DP.length>=2){
						GenericUtils.Verify("for clicking on "+DP[1], oGenericUtils.clickGivenXpath("//mat-expansion-panel//label[text()='"+DP[1]+"']"));
						GenericUtils.Verify("for clicking on "+DP[1], oGenericUtils.is_WebElement_Displayed("//div[@class='dp_details_block']//span[text()='"+DP[1]+"']"));
						GenericUtils.Verify("clicking on Previous button", oGenericUtils.clickGivenXpath(StringUtils.replace(oCPWPage.ButtonWithText, "svalue", "Previous")));
						refSeleniumUtils.defaultWait(ProjectVariables.TImeout_3_Seconds);
						GenericUtils.Verify("for displaying "+DP[0]+" on clicking previous", oGenericUtils.is_WebElement_Displayed("//div[@class='dp_details_block']//span[text()='"+DP[0]+"']"));
						GenericUtils.Verify("clicking on Next button", oGenericUtils.clickGivenXpath(StringUtils.replace(oCPWPage.ButtonWithText, "svalue", "Next")));
						refSeleniumUtils.defaultWait(ProjectVariables.TImeout_3_Seconds);
						GenericUtils.Verify("for displaying "+DP[1]+" on clicking next", oGenericUtils.is_WebElement_Displayed("//div[@class='dp_details_block']//span[text()='"+DP[1]+"']"));
					}
				}				
				break;
			case "Test Decisions":
				userValidatesDPAssignmentforProfileWithDPType("DP+PPS With All DPTypes","Rules,Information Only,Configuration Only");	
				GenericUtils.Verify("clicking on DP",oGenericUtils.clickOnElement(DP+"[1]"));
				String [] sDecision= "Approve,Approve with Mod,Approve Test Only,Approve with Mod Test Only".split(",");
				for(int i=0;i<sDecision.length;i++){
					Serenity.setSessionVariable("PayerShorts_"+sDecision[i]).to(getDriver().findElements(By.xpath("//tbody[@role ='rowgroup']/tr["+(i+1)+"]/td[1]/label/span")).get(0).getText().split(" ")[0].trim());
					Serenity.setSessionVariable("ClaimType_"+sDecision[i]).to(StringUtils.substringBetween(getDriver().findElements(By.xpath("//tbody[@role ='rowgroup']/tr["+(i+1)+"]/td[1]/label/span/span")).get(0).getText(), "[","]").trim());
					GenericUtils.Verify("PPS Selection", oGenericUtils.clickOnElement(sPPSSelection+"["+(i+1)+"]"));
					GenericUtils.Verify("clicking on DP Level assign icon", oGenericUtils.clickGivenXpath(StringUtils.replace(oCPWPage.ButtonContainsClass, "value", "captureIcon")));
					captureDecision(sDecision[i]);
				}
				refSeleniumUtils.defaultWait(ProjectVariables.TImeout_5_Seconds);
				String[] assignType = "Re-Assign,Unassign".split(",");
				for(int j=0;j<assignType.length;j++){
					if(assignType[j].equals("Re-Assign")){
						Serenity.setSessionVariable("oldPres").to(Serenity.sessionVariableCalled("sPPName"));
						oPresentationProfileValidations.PresentationProfileValidations("CREATE", "", "");
					}else{
						oGenericUtils.clickGivenXpath(StringUtils.replace(PresentationTabName, "PresNameArg", Serenity.sessionVariableCalled("oldPres")));
						GenericUtils.Verify("clicking on DP Type dropdown", oGenericUtils.clickOnElement(StringUtils.replace(Label_With_Contains, "sValue", "DP Type")+"/..//span"));
						GenericUtils.Verify("clicking on Configuration Only dropdown option", oGenericUtils.clickOnElement(StringUtils.replace(oCPWPage.Div_contains_Class, "value", "transformPanel")+"//span[contains(text(),'"+sFunctionality+"')]"));
						refSeleniumUtils.defaultWait(ProjectVariables.TImeout_3_Seconds);
						GenericUtils.Verify("clicking on DP",oGenericUtils.clickOnElement(DP+"[1]"));
					}					
					assignConfigurationDPsToProfile(sDPType,assignType[j],"Configuration Only");
					if(!refSeleniumUtils.is_WebElement_Displayed(sPPSSelection)){
						GenericUtils.Verify("clicking on DP",oGenericUtils.clickOnElement(DP+"[1]"));
					}
					for(int i=0;i<sDecision.length;i++){
						if(sDecision[i].contains("Test"))
							GenericUtils.Verify(assignType[j]+"ing and not displaying PPS:"+Serenity.sessionVariableCalled("PayerShorts_"+sDecision[i])+"["+Serenity.sessionVariableCalled("ClaimType_"+sDecision[i])+"]", 
								!refSeleniumUtils.is_WebElement_Displayed(StringUtils.replace(oCPWPage.Span_contains_text, "value", Serenity.sessionVariableCalled("PayerShorts_"+sDecision[i]))+"/span[contains(text(),'["+Serenity.sessionVariableCalled("ClaimType_"+sDecision[i])+"]')]"));
						else
							GenericUtils.Verify("not "+assignType[j]+"ing and displaying PPS:"+Serenity.sessionVariableCalled("PayerShorts_"+sDecision[i])+"["+Serenity.sessionVariableCalled("ClaimType_"+sDecision[i])+"]", 
								refSeleniumUtils.is_WebElement_Displayed(StringUtils.replace(oCPWPage.Span_contains_text, "value", Serenity.sessionVariableCalled("PayerShorts_"+sDecision[i]))+"/span[contains(text(),'["+Serenity.sessionVariableCalled("ClaimType_"+sDecision[i])+"]')]"));
					}
					//validating Re-assign in old profile
					if(assignType[j].equals("Re-Assign")){						
						oGenericUtils.clickGivenXpath(StringUtils.replace(PresentationTabName, "PresNameArg", Serenity.sessionVariableCalled("sPPName")));
						GenericUtils.Verify("clicking on DP Type dropdown", oGenericUtils.clickOnElement(StringUtils.replace(Label_With_Contains, "sValue", "DP Type")+"/..//span"));
						GenericUtils.Verify("clicking on Configuration Only dropdown option", oGenericUtils.clickOnElement(StringUtils.replace(oCPWPage.Div_contains_Class, "value", "transformPanel")+"//span[contains(text(),'"+sFunctionality+"')]"));
						refSeleniumUtils.defaultWait(ProjectVariables.TImeout_3_Seconds);
						GenericUtils.Verify("clicking on DP",oGenericUtils.clickOnElement(DP+"[1]"));
						for(int i=0;i<sDecision.length;i++){
							if(!sDecision[i].contains("Test"))
								GenericUtils.Verify("not "+assignType[j]+"ing and displaying PPS:"+Serenity.sessionVariableCalled("PayerShorts_"+sDecision[i])+"["+Serenity.sessionVariableCalled("ClaimType_"+sDecision[i])+"]", 
									!refSeleniumUtils.is_WebElement_Displayed(StringUtils.replace(oCPWPage.Span_contains_text, "value", Serenity.sessionVariableCalled("PayerShorts_"+sDecision[i]))+"/span[contains(text(),'["+Serenity.sessionVariableCalled("ClaimType_"+sDecision[i])+"]')]"));
							else
								GenericUtils.Verify(assignType[j]+"ing and not displaying PPS:"+Serenity.sessionVariableCalled("PayerShorts_"+sDecision[i])+"["+Serenity.sessionVariableCalled("ClaimType_"+sDecision[i])+"]", 
									refSeleniumUtils.is_WebElement_Displayed(StringUtils.replace(oCPWPage.Span_contains_text, "value", Serenity.sessionVariableCalled("PayerShorts_"+sDecision[i]))+"/span[contains(text(),'["+Serenity.sessionVariableCalled("ClaimType_"+sDecision[i])+"]')]"));
						}
					}																								
				}
				break;
			}
			if(refSeleniumUtils.is_WebElement_Displayed(StringUtils.replace(PresentationDeleteIcon, "PresNameArg", Serenity.sessionVariableCalled("sPPName")))){
				oGenericUtils.clickGivenXpath(StringUtils.replace(PresentationDeleteIcon, "PresNameArg", Serenity.sessionVariableCalled("sPPName")));
				GenericUtils.Verify("deleting the presentation profile:"+Serenity.sessionVariableCalled("sPPName"), oGenericUtils.clickGivenXpath(
						StringUtils.replace(oCPWPage.Span_with_text, "value", "Yes")));
				refSeleniumUtils.defaultWait(ProjectVariables.TImeout_5_Seconds);
				if(sValidation.equals("Unassign"))
					assigningDPValidations("Delete PostAssignment","DP+PPS With All DPTypes");
			}
			
		}
		
		private void assignConfigurationDPsToProfile(String sProfile,String sValidation,String sFunctionality){
			GenericUtils.Verify("clicking on DP Type dropdown", oGenericUtils.clickOnElement(StringUtils.replace(Label_With_Contains, "sValue", "DP Type")+"/..//span"));
			GenericUtils.Verify("clicking on Configuration Only dropdown option", oGenericUtils.clickOnElement(StringUtils.replace(oCPWPage.Div_contains_Class, "value", "transformPanel")+"//span[contains(text(),'"+sFunctionality+"')]"));
			refSeleniumUtils.defaultWait(ProjectVariables.TImeout_5_Seconds);
			if(!sProfile.isEmpty() && !sProfile.equals("DP+PPS Partial") && sValidation.equals("Unassign")){
				Serenity.setSessionVariable("Payer Shorts").to("All");
				Serenity.setSessionVariable("ClaimType").to("All");
				Serenity.setSessionVariable("LOB").to("All");
			}else if(sProfile.isEmpty()){
				GenericUtils.Verify("All PPS Selection", oGenericUtils.clickOnElement(allPPS));
				GenericUtils.Verify("clicking on DP Level assign icon", oGenericUtils.clickGivenXpath(StringUtils.replace(oCPWPage.ButtonContainsClass, "value", "captureIcon")));
			}
			switch(sProfile){
			case "DP+PPS Partial":
				Serenity.setSessionVariable("DP").to(refSeleniumUtils.get_TextFrom_Locator(DP+"[1]"));
				GenericUtils.Verify("clicking on DP",oGenericUtils.clickOnElement(DP+"[1]"));
				GenericUtils.Verify("PPS Selection", oGenericUtils.clickOnElement(sPPSSelection+"[1]"));
				Serenity.setSessionVariable("Payer Shorts").to(getDriver().findElements(By.xpath("//tbody[@role ='rowgroup']/tr[1]/td[1]/label/span")).get(0).getText().split(" ")[0].trim());
				Serenity.setSessionVariable("ClaimType").to(StringUtils.substringBetween(getDriver().findElements(By.xpath("//tbody[@role ='rowgroup']/tr[1]/td[1]/label/span/span")).get(0).getText(), "[","]").trim());
				if(sValidation.equals("Unassign")){
					String Xpath="(//table[contains(@class,'decision_grid')]//tr[not(contains(@class,'header'))][1]/td[not(contains(@class,'greyOut'))][not(contains(@class,'payer'))])";
					String value="";
					int count= refSeleniumUtils.get_Matching_WebElement_count(Xpath);					
					for(int i=1;i<=count;i++){	
						 value=value+StringUtils.capitalize(StringUtils.substringBetween(refSeleniumUtils.Get_Value_By_given_attribute("class",Xpath+"["+i+"]"),"mat-column-"," "))+",";
					}
					value = value.substring(0, value.length() - 1);
					Serenity.setSessionVariable("LOB").to(value);					
				}					
				GenericUtils.Verify("clicking on DP Level assign icon", oGenericUtils.clickGivenXpath(StringUtils.replace(oCPWPage.ButtonContainsClass, "value", "captureIcon")));			
				break;
			case "DP+PPS Partial Test":
				String [] sDecision= "Approve,Approve with Mod,Approve Test Only,Approve with Mod Test Only".split(",");
				if(sValidation.equals("Unassign")){
					for(int i=0;i<sDecision.length;i++){
						Serenity.setSessionVariable("PayerShorts_"+sDecision[i]).to(getDriver().findElements(By.xpath("//tbody[@role ='rowgroup']/tr["+(i+1)+"]/td[1]/label/span")).get(0).getText().split(" ")[0].trim());
						Serenity.setSessionVariable("ClaimType_"+sDecision[i]).to(StringUtils.substringBetween(getDriver().findElements(By.xpath("//tbody[@role ='rowgroup']/tr["+(i+1)+"]/td[1]/label/span/span")).get(0).getText(), "[","]").trim());
						GenericUtils.Verify("PPS Selection", oGenericUtils.clickOnElement(sPPSSelection+"["+(i+1)+"]"));
						GenericUtils.Verify("clicking on DP Level assign icon", oGenericUtils.clickGivenXpath(StringUtils.replace(oCPWPage.ButtonContainsClass, "value", "captureIcon")));
						captureDecision(sDecision[i]);
					}
				}				
				for(int i=1;i<=sDecision.length;i++){					
					GenericUtils.Verify("PPS Selection", oGenericUtils.clickOnElement(sPPSSelection+"["+i+"]"));
				}
				GenericUtils.Verify("clicking on DP Level assign icon", oGenericUtils.clickGivenXpath(StringUtils.replace(oCPWPage.ButtonContainsClass, "value", "captureIcon")));
				break;
			case "DP+PPS With All DPTypes":				
					GenericUtils.Verify("All PPS Selection", oGenericUtils.clickOnElement(allPPS));
					GenericUtils.Verify("clicking on DP Level assign icon", oGenericUtils.clickGivenXpath(StringUtils.replace(oCPWPage.ButtonContainsClass, "value", "captureIcon")));								
				break;		
			case "DP Level":
				if(!oGenericUtils.is_WebElement_Displayed(DP+"[1]/..//i[contains(@class,'assign')]"))
					oGenericUtils.clickGivenXpath("//i[contains(text(),'expand_less')]");
				GenericUtils.Verify("clicking on DP Level assign icon",oGenericUtils.clickOnElement(DP+"[1]/..//i[contains(@class,'assign')]"));
				break;
			case "Topic Level":
				if(!oGenericUtils.is_WebElement_Displayed(StringUtils.replace(oCPWPage.labelcontainstext, "svalue", "Topic:")+"[1]/..//following-sibling::div//i"))
					oGenericUtils.clickGivenXpath("//i[contains(text(),'expand_less')]");
				GenericUtils.Verify("clicking on Topic Level assign icon", oGenericUtils.clickGivenXpath(StringUtils.replace(oCPWPage.labelcontainstext, "svalue", "Topic:")+"[1]/..//following-sibling::div//i"));
				break;
			case "MP Level":
				if(!oGenericUtils.is_WebElement_Displayed(StringUtils.replace(oCPWPage.labelcontainstext, "svalue", "Medical Policy:")+"[1]/..//following-sibling::div//i"))
					oGenericUtils.clickGivenXpath("//i[contains(text(),'expand_less')]");
				GenericUtils.Verify("clicking on MP Level assign icon", oGenericUtils.clickGivenXpath(StringUtils.replace(oCPWPage.labelcontainstext, "svalue", "Medical Policy:")+"[1]/..//following-sibling::div//i"));
				break;
			}
			GenericUtils.Verify("clicking on "+sValidation, refSeleniumUtils.clickOnGivenElemntByJavaScript("("+StringUtils.replace(oCPWPage.Inputwithvalue, "text", sValidation)+"/..//div)[1]"));
			if(sValidation.equals("Re-Assign")){
				GenericUtils.Verify("selecting profile:"+Serenity.sessionVariableCalled("sPPName").toString(), 
						refSeleniumUtils.clickOnGivenElemntByJavaScript(StringUtils.replace(StringUtils.replace(captureProileRadioButton, "asign", "assign"), "value", Serenity.sessionVariableCalled("sPPName").toString())));
			}
			GenericUtils.Verify("clicking on capture button", oGenericUtils.clickGivenXpath(StringUtils.replace(oCPWPage.ButtonWithText, "svalue", "Capture")));
			if(sValidation.equals("Unassign"))
				GenericUtils.Verify("clicking on capture button", oGenericUtils.clickGivenXpath("("+StringUtils.replace(oCPWPage.ButtonContainsText, "value", "Capture")+")[2]"));
			refSeleniumUtils.defaultWait(ProjectVariables.TImeout_5_Seconds);
		}

	private void ppsSelection(String sAssignment,String sFunctionality){

			oGenericUtils.clickOnElement("//span[contains(text(),'NPP Opportunities')]/span");
			refSeleniumUtils.defaultWait(ProjectVariables.TImeout_10_Seconds);
			String[] sPPS="Payer Shorts,LOB,ClaimType".split(",");
			for(int i=0;i<sPPS.length;i++){				
				refSeleniumUtils.moveToElementAndClick("//b[contains(text(),'"+sPPS[i]+"')]/..//div[contains(@class,'checkbox')]");
				String[] sSelection = Serenity.sessionVariableCalled(sPPS[i]).toString().split(",");
				for(int j=0;j<sSelection.length;j++){
					if(sSelection[j].equals("All"))
						refSeleniumUtils.moveToElementAndClick("//b[contains(text(),'"+sPPS[i]+"')]/..//div[contains(@class,'checkbox')]");
					else
						GenericUtils.Verify("selecting "+sPPS[i]+" as "+sSelection[j], oGenericUtils.clickGivenXpath("//b[contains(text(),'"+sPPS[i]+"')]/../../..//following-sibling::jqxlistbox//span[text()='"+sSelection[j]+"']"));
				}
			}
			//Applying selection
			refSeleniumUtils.clickGivenXpath(oFilterDrawer.FilterApplyBtn);	
			refSeleniumUtils.clickGivenXpath(oFilterDrawer.MedPolicyFilterApplyBtn);
			refSeleniumUtils.defaultWait(ProjectVariables.TImeout_5_Seconds);
			//selecting configuration
			GenericUtils.Verify("clicking on DP Type dropdown", oGenericUtils.clickOnElement(StringUtils.replace(Label_With_Contains, "sValue", "DP Type")+"/..//span"));
			GenericUtils.Verify("clicking on Configuration Only dropdown option", oGenericUtils.clickOnElement(StringUtils.replace(oCPWPage.Div_contains_Class, "value", "transformPanel")+"//span[contains(text(),'"+sFunctionality+"')]"));
			refSeleniumUtils.defaultWait(ProjectVariables.TImeout_5_Seconds);
			if(refSeleniumUtils.is_WebElement_Displayed(StringUtils.replace(medicalPolicy, "value", Serenity.sessionVariableCalled("MedicalPolicy")))){				
				refSeleniumUtils.clickGivenXpath("//a[text()='Collapse All']");
				refSeleniumUtils.defaultWait(ProjectVariables.TImeout_3_Seconds);
				oGenericUtils.clickGivenXpath(StringUtils.replace(medicalPolicy, "value", Serenity.sessionVariableCalled("MedicalPolicy"))+"/../..//mat-icon");
				String Xpath="(("+StringUtils.replace(medicalPolicy, "value", Serenity.sessionVariableCalled("MedicalPolicy"))+"/../../../..//following-sibling::div)[2]//label[contains(text(),'Topic')]/../..//mat-icon)";
				for(int i=1;i<=refSeleniumUtils.get_Matching_WebElement_count(Xpath);i++){
					oGenericUtils.clickGivenXpath(Xpath+"["+i+"]");
				}
				
			}
		}
	
public void validate_edit_topic_description() throws InterruptedException {
			
			boolean blnFlg=false;
			
			//oGenericUtils.gfn_Click_String_object_Xpath("Presenation tab is clicked","//span[@class='pres_pro_name'][contains(text(),'AutoTestPM627')]");
			
			refSeleniumUtils.gfn_Verify_String_Object_Exist("Dp is displayed", DP+"[1]");
			
	        String sDP=refSeleniumUtils.get_TextFrom_Locator(DP+"[1]");
	        
	        GenericUtils.Verify("DP:"+sDP, true);
	        
	        //"DP is clicked under edit icon",
	        
	        refSeleniumUtils.defaultWait(ProjectVariables.TImeout_5_Seconds);
	        
	        oGenericUtils.gfn_Click_String_object_Xpath("",DP+"[1]");
			
			refSeleniumUtils.defaultWait(ProjectVariables.TImeout_5_Seconds);
			
			//refSeleniumUtils.gfn_Verify_String_Object_Exist("Dp Description is displayed", "//b[text()='Description']/..//following-sibling::p");
			
		    String sDPDescription= refSeleniumUtils.get_TextFrom_Locator("//b[text()='Description']/..//following-sibling::p");
		    
		    GenericUtils.Verify("DP Description:"+sDPDescription, true);
		    
		    refSeleniumUtils.defaultWait(ProjectVariables.TImeout_3_Seconds);
		    
		     oGenericUtils.clickOnElement(StringUtils.replace(Label_With_Contains, "sValue", "DP Type") + "/..//span");

			 oGenericUtils.clickOnElement(StringUtils.replace(oCPWPage.Div_contains_Class, "value", "transformPanel")+ "//span[contains(text(),'Information Only')]");

			 oGenericUtils.clickOnElement(StringUtils.replace(Label_With_Contains, "sValue", "DP Type") + "/..//span");

			 oGenericUtils.clickOnElement(StringUtils.replace(oCPWPage.Div_contains_Class, "value", "transformPanel")+ "//span[contains(text(),'Rules')]");
		    
		    /*refSeleniumUtils.gfn_Click_String_object_Xpath("clicking on DP Type dropdown",StringUtils.replace(Label_With_Contains, "sValue", "DP Type")+"/..//span");
		    
			refSeleniumUtils.gfn_Click_String_object_Xpath("clicking on Information Only dropdown option", StringUtils.replace(oCPWPage.Div_contains_Class, "value", "transformPanel")+"//span[contains(text(),'Information Only')]");
			
			refSeleniumUtils.gfn_Click_String_object_Xpath("clicking on DP Type dropdown",StringUtils.replace(Label_With_Contains, "sValue", "DP Type")+"/..//span");
			
			refSeleniumUtils.gfn_Click_String_object_Xpath("clicking on Rules Only dropdown option",StringUtils.replace(oCPWPage.Div_contains_Class, "value", "transformPanel")+"//span[contains(text(),'Rules')]");*/
			 //"Edit button is clicked",
			
			refSeleniumUtils.defaultWait(ProjectVariables.TImeout_5_Seconds); 
			 
			refSeleniumUtils.gfn_Click_On_Object("Edit icon is clicked","mat-icon"," edit");
			
			refSeleniumUtils.defaultWait(ProjectVariables.TImeout_5_Seconds); 
			
			refSeleniumUtils.gfn_Click_String_object_Xpath("DP: "+sDP+" is clicked in Edit Topic Description","//label[text()='DP(s) in this topic:']/../..//span[text()='"+sDP+"']");
			
			String sEditPopDesc=refSeleniumUtils.get_TextFrom_Locator("//label[text()='DP Description ']/../..//div[@class='dpDescLabel']");
			
			blnFlg=sDPDescription.equalsIgnoreCase(sEditPopDesc);
			
			GenericUtils.Verify("DP Description:"+sDPDescription+"EditPopup DP Description"+sEditPopDesc, blnFlg);
			
			String sTopicDescriptionReadOnly=refSeleniumUtils.get_TextFrom_Locator("//mat-panel-title[text()=' Topic Description (System, Read-Only) ']/../../following-sibling::div//pre");
			
			GenericUtils.Verify("Topic Description Captured from Non Editable field"+sTopicDescriptionReadOnly, true);
			
			blnFlg=refSeleniumUtils.Enter_given_Text_Element("//mat-panel-title[text()=' Topic Description Edit: ']/../../following-sibling::div//textarea", "AutoTest");
			
			refSeleniumUtils.defaultWait(ProjectVariables.TImeout_5_Seconds);
			
			GenericUtils.Verify("AutoTest msg is entered in editable field",blnFlg);
			
			//refSeleniumUtils.is_WebElement_Displayed("//label[text()='Edit not saved. ']");
			
			refSeleniumUtils.gfn_Click_On_Object("Save button is clicked", "button", "Save");
			
			refSeleniumUtils.gfn_Verify_Object_Exist("AutoTest is displayed in Non Editable field", "ins", "AutoTest");
			
			refSeleniumUtils.gfn_Verify_Object_Exist("Default text in Non Editable field is in markup", "mat-panel-title", " Topic Description (System, Read-Only) ']/../../following-sibling::div//pre//del");
			
			refSeleniumUtils.gfn_Click_On_Object("Submit button is clicked", "button", "Submit");
			
			refSeleniumUtils.defaultWait(ProjectVariables.TImeout_5_Seconds);
			
			refSeleniumUtils.gfn_Verify_Object_Exist("Topic edit saved flag is dispalyed beside edit icon", "label", "Topic edit saved ");
			
			refSeleniumUtils.gfn_Click_On_Object("Edit icon is clicked","mat-icon"," edit");
			
			refSeleniumUtils.defaultWait(ProjectVariables.TImeout_5_Seconds);
			
			blnFlg=refSeleniumUtils.Enter_given_Text_Element("//mat-panel-title[text()=' Topic Description Edit: ']/../../following-sibling::div//textarea", sTopicDescriptionReadOnly);
			
			refSeleniumUtils.defaultWait(ProjectVariables.TImeout_5_Seconds);
			
			GenericUtils.Verify("Default Description which is captured earlier is entered in editable field", blnFlg);
			
			refSeleniumUtils.gfn_Click_On_Object("Save button is clicked", "button", "Save");
		
			blnFlg=!refSeleniumUtils.is_WebElement_Displayed("//mat-panel-title[text()=' Topic Description (System, Read-Only) ']/../../following-sibling::div//pre//del");
			
			GenericUtils.Verify("Default text in Non Editable field with markup is not displayed", blnFlg);
			
			blnFlg=!refSeleniumUtils.is_WebElement_Displayed("//ins[text()='AutoTest']");
			
			GenericUtils.Verify("Auto Test msg is not displayed", blnFlg);
			
		}


		public void getUniquePPSfromClientconfigService(String clientkey) throws Exception
		{
			
			HashMap<String,Object> parametersMap = new HashMap<String,Object>();
			
			parametersMap.put("userName", Serenity.sessionVariableCalled("user"));
			String sEndPoint = ProjectVariables.sServices.get("getClientConfig").get("EndpointURL");

			Response oResponseBody = oRestServiceUtils.getRequestWithPathParams(sEndPoint+clientkey);
			//System.out.println(oResponseBody.jsonPath().getString("result"));
			String sResult=oResponseBody.jsonPath().getString("result");
			List<String> sList=Arrays.asList(sResult.split("],"));
			for (int i = 0; i < sList.size(); i++) 
			{
				//System.out.println(sList.get(i));
				String spayer=StringUtils.substringBetween(sList.get(i), "payer_short:", ", insurance_key").trim();
				String sinsurancekey=StringUtils.substringBetween(sList.get(i), "insurance_key:", ", policy_set_key").trim();
				String sclaimtype=StringUtils.substringBetween(sList.get(i), "claim_type:", ", payer_short").trim();
				String sinsurance=GenericUtils.Retrieve_the_insuranceDesc_from_insuranceKey(sinsurancekey).trim();
				if(!sclaimtype.equalsIgnoreCase("D")&&!sclaimtype.equalsIgnoreCase("H"))
				{
					ProjectVariables.PPSList.add(spayer+":"+sinsurance+":"+sclaimtype);	
					ProjectVariables.DB_claimtypeList.add(sclaimtype);
				}
				
				//System.out.println(spayer+":"+sinsurance+":"+sclaimtype);
			}
			System.out.println("####################### Service PPS ###############################");
			for (String pps : ProjectVariables.PPSList) {
				//System.out.println(pps);
			}
			System.out.println("DistinctPPS Size::"+ProjectVariables.PPSList.size());
			System.out.println("Distinct Claimtypes::"+ProjectVariables.DB_claimtypeList);
		}
		
		public void userValidatesChangeSummaryData(String sValidationType) {
			ArrayList<String> mongoData;
			if(sValidationType.contains("-"))
				mongoData=MongoDBUtils.userRetrievesChangeSummaryData(MonGoDBQueries.FilterMongoDBQuery(sValidationType.split("-")[0]),sValidationType.split("-")[0]);
			else
				mongoData=MongoDBUtils.userRetrievesChangeSummaryData(MonGoDBQueries.FilterMongoDBQuery(sValidationType),sValidationType);
			switch(sValidationType.split("-")[0]){
			case "ALL":
				for(int i=0;i<mongoData.size();i++){
					String[] rowData=mongoData.get(i).split("-");
					for(int j=0;j<rowData.length;j++){
						GenericUtils.Verify("for displaying details from mongo with UI for DP:"+rowData[0], 
								refSeleniumUtils.get_TextFrom_Locator("//div[@title='"+rowData[0]+"']/..//div[contains(@class,'item-material')][@columnindex='"+(2+j)+"']").equalsIgnoreCase(rowData[j]));
					}
				}
				break;
			case "NEW MP":
			case "NEW DP":
			case "NEW TOPIC":
			case "RECAT TOPIC":
			case "DEACT DP":
			case "CHANGE DP DESC":
			case "NEW RULE":
			case "RECATEGORIZED RULE":
			case "CLAIM TYPE ADDED":
			case "CLAIM TYPE REMOVED":
			case "REFERENCE CHANGE":
			case "RECAT DP":	
			case "DEACTIVATED POLICY":
			case "VERSION CHANGE":
				Collections.sort(mongoData,new Comparator<String>(){
					public int compare(String o1,String o2){
						return new Long(Long.parseLong(o1)).compareTo(Long.parseLong(o2));
					}
				});
				Serenity.setSessionVariable("changeDP").to(mongoData.toArray());
				for(int i=0;i<mongoData.size();i++){
					while(refSeleniumUtils.get_Matching_WebElement_count("//div[@title='"+mongoData.get(i)+"']/..//div[@columnindex='3']")==0){
						for(int j=1;j<=10;j++){						
							refSeleniumUtils.Doubleclick("//div[@class='changeContainer']//div[contains(@id,'jqxScrollBtnDownverticalScrollBar')]");
						}					
						setImplicitTimeout(1000, ChronoUnit.MILLIS);							 					
					}
					resetImplicitTimeout();	
					if(sValidationType.contains("-")){
						oGenericUtils.clickOnElement(StringUtils.replace(changeOpportunitiesDP, "dp", mongoData.get(i)));
					}else{
						System.out.println(refSeleniumUtils.get_TextFrom_Locator("//div[@title='"+mongoData.get(i)+"']/..//div[@columnindex='3']"));
						if(!sValidationType.equals("VERSION CHANGE"))						
							GenericUtils.Verify("displaying DP Change Status as "+sValidationType+" in UI for the DP:"+mongoData.get(i), 
								refSeleniumUtils.get_TextFrom_Locator("//div[@title='"+mongoData.get(i)+"']/..//div[@columnindex='3']").contains(sValidationType));
						if(sValidationType.equals("RECAT DP")||sValidationType.equals("DEACTIVATED POLICY")||sValidationType.equals("VERSION CHANGE")){
							GenericUtils.Verify("displaying details under column:"+sValidationType+" for DP:"+mongoData.get(i)+" as:"+Serenity.sessionVariableCalled(mongoData.get(i)+sValidationType).toString(), 
									refSeleniumUtils.Get_Value_By_given_attribute("title","//div[@title='"+mongoData.get(i)+"']/..//div[@columnindex='"+getColumnIndexOnName(sValidationType)+"']").contains(
											Serenity.sessionVariableCalled(mongoData.get(i)+sValidationType).toString()));
						}
					}											
				}
				break;					
			}
			
			
		}
		
		private String getColumnIndexOnName(String columnName){
			HashMap<String,String> Index = new HashMap<String,String>();
			Index.put("RECAT DP", "4");
			Index.put("DEACTIVATED POLICY", "12");
			Index.put("VERSION CHANGE", "11");			
			return Index.get(columnName);
			
		}

		public void userValidatesDpForProfile(String validationType, String grid) {			
			//empty grid validation
			String[] sType;
			String[] sValidation = validationType.split(",");
			String dpKey1 = null;
			String dpKey2 = null;
			String dpKey3 = null;
			oGenericUtils.clickOnElement(StringUtils.replace(oPresentationProfileValidations.sChangeOppPres, "value", Serenity.sessionVariableCalled("PresentationName")));
			refSeleniumUtils.defaultWait(ProjectVariables.TImeout_5_Seconds);
			if(!validationType.contains("CaptureDecision")){				
				GenericUtils.Verify("not displaying any DP's under newly created profile",refSeleniumUtils.is_WebElement_Displayed(StringUtils.replace(oCPWPage.Div_contains_Class, "value", "no-opp-msg")));
				GenericUtils.Verify("displaying 'Ready For Presentation' checkbox is read only",refSeleniumUtils.is_WebElement_Displayed(ReadyForPresentation+"[contains(@class,'disabled')]"));
				//clicking on change opportunities
				GenericUtils.Verify("clicking on "+grid, oGenericUtils.clickOnElement("//div[@role='tab']//span[contains(text(),'Change Opportunities')]"));
				refSeleniumUtils.defaultWait(ProjectVariables.TImeout_5_Seconds);
				dpKey1=refSeleniumUtils.get_TextFrom_Locator("(//div[@columnindex='2'])[1]");
				dpKey2=refSeleniumUtils.get_TextFrom_Locator("(//div[@columnindex='2'])[2]");
				dpKey3=refSeleniumUtils.get_TextFrom_Locator("(//div[@columnindex='2'])[3]");
			}				
			for(int i=0;i<sValidation.length;i++){
				switch(sValidation[i]){
				case "Assignment":
					//selecting newly created profile	
					sType="Cancel,Okay".split(",");		
					for(int j=0;j<sType.length;j++){
						oGenericUtils.clickOnElement(StringUtils.replace(changeOpportunitiesDP, "dp", dpKey1));
						oGenericUtils.clickOnElement(StringUtils.replace(changeOpportunitiesDP, "dp", dpKey2));
						GenericUtils.Verify("clicking on assign icon", refSeleniumUtils.moveToElementAndClick("("+ChangeOppAssignIcon+"//i)[1]"));
						GenericUtils.Verify("selecting profile:"+Serenity.sessionVariableCalled("PresentationName").toString(), 
								oGenericUtils.clickOnElement(StringUtils.replace(profileRadioButton, "value", Serenity.sessionVariableCalled("PresentationName").toString())));
						GenericUtils.Verify("clicking on "+sType[j], oGenericUtils.clickOnElement(StringUtils.replace(assignPopupOkay,"Okay", sType[j])));
						refSeleniumUtils.defaultWait(ProjectVariables.TImeout_5_Seconds);
						if(sType[j].equalsIgnoreCase("Cancel")){
							GenericUtils.Verify("displaying checkbox as unchecked and displaying DP:"+dpKey1, oGenericUtils.is_WebElement_Displayed(StringUtils.replace(changeOpportunitiesDP, "dp", dpKey1)));
							GenericUtils.Verify("displaying checkbox as unchecked and displaying DP:"+dpKey2, oGenericUtils.is_WebElement_Displayed(StringUtils.replace(changeOpportunitiesDP, "dp", dpKey2)));
						}else{
							GenericUtils.Verify("not displaying DP:"+dpKey1, !oGenericUtils.is_WebElement_Displayed(StringUtils.replace(changeOpportunitiesDP, "dp", dpKey1)));
							GenericUtils.Verify("not displaying DP:"+dpKey2, !oGenericUtils.is_WebElement_Displayed(StringUtils.replace(changeOpportunitiesDP, "dp", dpKey2)));
							refSeleniumUtils.moveToElement($(StringUtils.replace(profileMouseHoverChangeOpp, "value", Serenity.sessionVariableCalled("PresentationName"))));
							GenericUtils.Verify("DP Count as:2 on mouse hover",getDriver().findElement(By.xpath(StringUtils.replace(profileMouseHoverChangeOpp, "value", Serenity.sessionVariableCalled("PresentationName")))).getAttribute("innerHTML").
									contains("2 DP(s)"));
							oGenericUtils.clickOnElement(StringUtils.replace(oPresentationProfileValidations.sChangeOppPres, "value", Serenity.sessionVariableCalled("PresentationName")));
							refSeleniumUtils.defaultWait(ProjectVariables.TImeout_5_Seconds);
							GenericUtils.Verify("displaying DP:"+dpKey1+" in assigned profile", oGenericUtils.is_WebElement_Displayed(StringUtils.replace(changeOpportunitiesDP, "dp", dpKey1)));
							GenericUtils.Verify("displaying DP:"+dpKey2+" in assigned profile", oGenericUtils.is_WebElement_Displayed(StringUtils.replace(changeOpportunitiesDP, "dp", dpKey2)));
							Assert.assertTrue("clicking on 'Ready For Presentation' checkbox",oGenericUtils.clickOnElement(ReadyForPresentation));
 							refSeleniumUtils.defaultWait(ProjectVariables.TImeout_2_Seconds);
							GenericUtils.Verify("setting profile to 'Ready For Presentation'",oGenericUtils.is_WebElement_Displayed(StringUtils.replace(oCPWPage.Div_contains_Class, "value", "changeContainer red-border")));
							Assert.assertTrue("clicking on 'Ready For Presentation' checkbox",oGenericUtils.clickOnElement(ReadyForPresentation));
							oGenericUtils.clickOnElement("("+StringUtils.replace(oCPWPage.ButtonContainsText, "value", "Yes")+")[5]");
							refSeleniumUtils.defaultWait(ProjectVariables.TImeout_3_Seconds);
						}
					}
					break;
				case "CaptureDecision":
					dpKey1=refSeleniumUtils.get_TextFrom_Locator("(//div[@columnindex='2'])[1]");
					oGenericUtils.clickOnElement(StringUtils.replace(changeOpportunitiesDP, "dp", dpKey1));
					GenericUtils.Verify("clicking on assign icon", oGenericUtils.moveToElementAndClick("("+ChangeOppAssignIcon+"//i)[2]"));
					captureDecision(grid);
					refSeleniumUtils.defaultWait(ProjectVariables.TImeout_5_Seconds);
					GenericUtils.Verify("setting profile to 'Ready For Presentation'",refSeleniumUtils.is_WebElement_Displayed(StringUtils.replace(oCPWPage.Div_contains_Class, "value", "changeContainer red-border")));
					break;
				case "Capture Decision Popup":
					GenericUtils.Verify("displaying assign icon as disabled", oGenericUtils.is_WebElement_Displayed("("+ChangeOppAssignIcon+"/button[@disabled])[2]"));
					GenericUtils.Verify("displaying Finalize button as disabled", oGenericUtils.is_WebElement_Displayed(StringUtils.replace(oCPWPage.ButtonWithText, "svalue", "FINALIZE")+"[@disabled]"));
					GenericUtils.Verify("displaying Export icon as disabled", oGenericUtils.is_WebElement_Displayed(StringUtils.replace(oCPWPage.ButtonWithText, "svalue", "FINALIZE")+"/..//following-sibling::li//i"));
					GenericUtils.Verify("displaying 'Previous' under decision column", oGenericUtils.is_WebElement_Displayed(StringUtils.replace(oCPWPage.Span_with_text, "value", "Previous")));
					GenericUtils.Verify("displaying 'Current' under decision column", oGenericUtils.is_WebElement_Displayed(StringUtils.replace(oCPWPage.Span_with_text, "value", "Current")));
					sType="Approve,Approve with Mod,Reject,Approve Change,Approve Change with Mod,Reject Change,Defer,Follow up,Re-Assign,Unassign".split(",");
					for(int j=0;j<sType.length;j++){
						refSeleniumUtils.defaultWait(ProjectVariables.TImeout_2_Seconds);
						oGenericUtils.clickGivenXpath(StringUtils.replace(changeOpportunitiesDP, "dp", dpKey1));
						GenericUtils.Verify("clicking on assign icon", oGenericUtils.moveToElementAndClick("("+ChangeOppAssignIcon+"//i)[2]"));
						ClickonDecision(sType[j]);
						if(sType[j].equals("Defer") || sType[j].equals("Follow up"))
							GenericUtils.Verify("displaying "+sType[j]+"as disabled", oGenericUtils.is_WebElement_Displayed("//mat-radio-button[contains(@class,'disabled')]//input[@value='"+sType[j]+"']"));
						else if(sType[j].equals("Reject Change"))
							GenericUtils.Verify("displaying Reasons section as not mandatory for decision:"+sType[j], oGenericUtils.is_WebElement_Displayed(StringUtils.replace(oCPWPage.ButtonWithText, "svalue", "Capture")+"[@aria-disabled='false']"));
						else if(sType[j].contains("Approve")){														
							GenericUtils.Verify("not displaying Test Only checkbox in decision:"+sType[j], !oGenericUtils.is_WebElement_Displayed(StringUtils.replace(oCPWPage.Span_contains_text, "value", "Test Only")));
							if(sType[j].contains("Mod")){
								GenericUtils.Verify("displaying 'Capture' button as disabled", oGenericUtils.is_WebElement_Displayed(StringUtils.replace(oCPWPage.ButtonWithText, "svalue", "Capture")+"[@aria-disabled='true']"));
								String beforeCharLimit=StringUtils.substringBefore(oGenericUtils.get_TextFrom_Locator(StringUtils.replace(oCPWPage.Div_contains_Class, "value", "popup_cancel_text")), "characters").trim();
								oGenericUtils.clickOnElement(StringUtils.replace(oCPWPage.labelwithtext, "svalue", "Other"));
								refSeleniumUtils.defaultWait(ProjectVariables.TImeout_2_Seconds);
								oGenericUtils.setValue(By.xpath(sApprovewithModNotes),"a");
								oGenericUtils.clickOnElement(StringUtils.replace(oCPWPage.labelwithtext, "svalue", "Other"));
								refSeleniumUtils.defaultWait(ProjectVariables.TImeout_2_Seconds);
								GenericUtils.Verify("displaying 'Capture' button as enabled after entering text", oGenericUtils.is_WebElement_Displayed(StringUtils.replace(oCPWPage.ButtonWithText, "svalue", "Capture")+"[@aria-disabled='false']"));
								GenericUtils.Verify("reducing character count from "+beforeCharLimit+" to "+StringUtils.substringBefore(oGenericUtils.get_TextFrom_Locator(StringUtils.replace(oCPWPage.Div_contains_Class, "value", "popup_cancel_text")), "characters").trim(), 
										Integer.parseInt(beforeCharLimit)>Integer.parseInt(StringUtils.substringBefore(oGenericUtils.get_TextFrom_Locator(StringUtils.replace(oCPWPage.Div_contains_Class, "value", "popup_cancel_text")), "characters").trim()));
							}
						}
						refSeleniumUtils.defaultWait(ProjectVariables.TImeout_3_Seconds);
						oGenericUtils.clickOnElement("//div[contains(@class,'pos_rel')]//div[@class='dialog-footer']"+StringUtils.replace(oCPWPage.ButtonContainsText, "value","Cancel"));
						if(oGenericUtils.is_WebElement_Displayed("(//div[@class='popup_blocker_bg']/..//button[contains(text(),'Cancel')])[1]"))
							oGenericUtils.clickOnElement("(//div[@class='popup_blocker_bg']/..//button[contains(text(),'Cancel')])[1]");
						refSeleniumUtils.defaultWait(ProjectVariables.TImeout_3_Seconds);
					}
					break;
				case "Finalize Single DP-Approve Change":
				case "Finalize Single DP-Approve Change with Mod":
				case "Finalize Single DP-Reject Change":
				case "Finalize All DP-Approve Change":
				case "Finalize All DP-Approve Change with Mod":
				case "Finalize All DP-Reject Change":
					String Xpath;
					if(sValidation[i].contains("All")){
						Xpath="//div[@role='columnheader']//div[contains(@class,'checkbox-default')]";
						//dpKey1="";
					}else{
						Xpath=StringUtils.replace(changeOpportunitiesDP, "dp", dpKey1);
					}
					oGenericUtils.clickOnElement(Xpath);
					GenericUtils.Verify("clicking on assign icon", oGenericUtils.moveToElementAndClick("("+ChangeOppAssignIcon+"//i)[2]"));
					captureDecision(sValidation[i].split("-")[1]);
					refSeleniumUtils.defaultWait(ProjectVariables.TImeout_5_Seconds);
					GenericUtils.Verify("setting profile to 'Ready For Presentation'",refSeleniumUtils.is_WebElement_Displayed(StringUtils.replace(oCPWPage.Div_contains_Class, "value", "changeContainer red-border")));
					oGenericUtils.clickOnElement(Xpath);
					oPresentationDeck.FinalizeDescisions(sValidation[i].split("-")[1]);
					GenericUtils.Verify("displaying Ready for Presentation checkbox in disabled mode,after finalaizing to the decision ==>"+sValidation[i].split("-")[1], oGenericUtils.is_WebElement_Displayed(StringUtils.replace(oCPWPage.Span_contains_text, "value", "Ready For Presentation")+"/..//input[@disabled]"));
					//GenericUtils.Verify("validating finalized status as true in mongo", MongoDBUtils.getChangePresentationProfileStatus(dpKey1, Serenity.sessionVariableCalled("clientKey"), Serenity.sessionVariableCalled("PresentationName")));
					break;
				case "Finalize All Decisions":
					//adding one more dp to profile so as to take 3 change decisions
					oGenericUtils.clickOnElement(StringUtils.replace(changeOpportunitiesDP, "dp", dpKey1));
					oGenericUtils.clickOnElement(StringUtils.replace(changeOpportunitiesDP, "dp", dpKey2));
					oGenericUtils.clickOnElement(StringUtils.replace(changeOpportunitiesDP, "dp", dpKey3));
					GenericUtils.Verify("clicking on assign icon", refSeleniumUtils.moveToElementAndClick("("+ChangeOppAssignIcon+"//i)[1]"));
					GenericUtils.Verify("selecting profile:"+Serenity.sessionVariableCalled("PresentationName").toString(), 
							oGenericUtils.clickOnElement(StringUtils.replace(profileRadioButton, "value", Serenity.sessionVariableCalled("PresentationName").toString())));
					GenericUtils.Verify("clicking on okay", oGenericUtils.clickOnElement(assignPopupOkay));
					refSeleniumUtils.defaultWait(ProjectVariables.TImeout_5_Seconds);
					oGenericUtils.clickOnElement(StringUtils.replace(oPresentationProfileValidations.sChangeOppPres, "value", Serenity.sessionVariableCalled("PresentationName")));
					refSeleniumUtils.defaultWait(ProjectVariables.TImeout_5_Seconds);
					sType="Approve Change,Approve Change with Mod,Reject Change".split(",");
					for(int j=0;j<sType.length;j++){
						oGenericUtils.clickGivenXpath("(//div[@role='row']//div[contains(@class,'checkbox-default')])["+(j+1)+"]");
						GenericUtils.Verify("clicking on assign icon", oGenericUtils.clickGivenXpath("("+ChangeOppAssignIcon+"//i)[1]"));
						captureDecision(sValidation[j]);
						refSeleniumUtils.defaultWait(ProjectVariables.TImeout_3_Seconds);
					}
					oPresentationDeck.FinalizeDescisions(sValidation[i].split("-")[1]);
					GenericUtils.Verify("Ready for Presentation checkbox should be in disabled mode,after finalaizing the decision ==>"+sValidation[i].split("-")[1], oGenericUtils.is_WebElement_Displayed(StringUtils.replace(oCPWPage.Span_contains_text, "value", "Ready For Presentation")+"/..//input[@disabled]"));
					break;
				case "Assigning ChangeStatus":
					userValidatesChangeSummaryData(grid+"-ChangeStatus");
					GenericUtils.Verify("clicking on assign icon", refSeleniumUtils.moveToElementAndClick("("+ChangeOppAssignIcon+"//i)[1]"));
					System.out.println(StringUtils.replace(profileRadioButton, "value", Serenity.sessionVariableCalled("PresentationName").toString()));
					GenericUtils.Verify("selecting profile:"+Serenity.sessionVariableCalled("PresentationName").toString(), 
							oGenericUtils.clickOnElement(StringUtils.replace(profileRadioButton, "value", Serenity.sessionVariableCalled("PresentationName").toString())));
					GenericUtils.Verify("clicking on okay", oGenericUtils.clickOnElement(assignPopupOkay));
					refSeleniumUtils.defaultWait(ProjectVariables.TImeout_5_Seconds);
					oGenericUtils.clickOnElement(StringUtils.replace(oPresentationProfileValidations.sChangeOppPres, "value", Serenity.sessionVariableCalled("PresentationName")));
					refSeleniumUtils.defaultWait(ProjectVariables.TImeout_5_Seconds);
					//GenericUtils.Verify("assigning DP's with status "+grid+" to the profile ==> "+Serenity.sessionVariableCalled("PresentationName").toString(), 
							//Arrays.equals(Serenity.sessionVariableCalled("changeDp"), oGenericUtils.get_All_Text_from_Locator("(//div[@columnindex='2'])/div")));
					break;
				}
			}			
		}

		public void userValidatesChangeSummaryData(String sValidationType, String sCriteria) {
			String DP=Serenity.sessionVariableCalled("PREV_DP");
			String sStatus;
			if(sCriteria.equals("Unassigned")) {
				while(refSeleniumUtils.get_Matching_WebElement_count("//div[@title='"+DP+"']/..//div[@columnindex='3']")==0){
					for(int j=1;j<=10;j++){						
						refSeleniumUtils.Doubleclick("//div[@class='changeContainer']//div[contains(@id,'jqxScrollBtnDownverticalScrollBar')]");
					}					
					setImplicitTimeout(1000, ChronoUnit.MILLIS);							 					
				}
				resetImplicitTimeout();
				refSeleniumUtils.moveTo("//div[@title='"+DP+"']/..//div[@columnindex='3']");
			}			
			switch(sValidationType){			
			case "RECAT DP":
				switch(sCriteria) {
				case "Without RFP":
					oGenericUtils.clickOnElement(StringUtils.replace(oPresentationProfileValidations.sChangeOppPres, "value", Serenity.sessionVariableCalled("PresentationName")));
					refSeleniumUtils.defaultWait(ProjectVariables.TImeout_5_Seconds);
				case "Unassigned":
					sStatus=refSeleniumUtils.get_TextFrom_Locator("//div[@title='"+DP+"']/..//div[@columnindex='3']");
					GenericUtils.Verify("displaying status for DP:"+DP+" as:"+sStatus, sStatus.contains("RECAT DP") && sStatus.contains("CHANGE DP DESC"));
					GenericUtils.Verify("displaying DP Recategorized for DP:"+DP+" as checked", refSeleniumUtils.Get_Value_By_given_attribute("title", "//div[@title='"+DP+"']/..//div[@columnindex='4']").contains("true"));
					break;
				case "With RFP":
					/*GenericUtils.Verify("clicking on change grid ", oGenericUtils.clickOnElement("//div[@role='tab']//span[contains(text(),'Change Opportunities')]"));
					refSeleniumUtils.defaultWait(ProjectVariables.TImeout_3_Seconds);
					oGenericUtils.clickOnElement(StringUtils.replace(oPresentationProfileValidations.sChangeOppPres, "value", Serenity.sessionVariableCalled("PresentationName")));
					refSeleniumUtils.defaultWait(ProjectVariables.TImeout_5_Seconds);*/
					sStatus=refSeleniumUtils.get_TextFrom_Locator("//div[@title='"+DP+"']/..//div[@columnindex='3']");
					GenericUtils.Verify("displaying status for DP:"+DP+" as:"+sStatus, sStatus.contains("CHANGE DP DESC"));
					GenericUtils.Verify("displaying DP Recategorized for DP:"+DP+" as Unchecked", refSeleniumUtils.Get_Value_By_given_attribute("title", "//div[@title='"+DP+"']/..//div[@columnindex='4']").contains("false"));
					break;
				}
				GenericUtils.Verify("displaying Description for DP:"+DP+" as checked", refSeleniumUtils.Get_Value_By_given_attribute("title", "//div[@title='"+DP+"']/..//div[@columnindex='6']").contains("true"));
				oGenericUtils.clickOnElement("//div[@title='"+DP+"']/..//div[contains(@class,'arrow-right')]");
				refSeleniumUtils.defaultWait(ProjectVariables.TImeout_3_Seconds);
				refSeleniumUtils.moveTo("("+StringUtils.replace(oCPWPage.Div_with_text, "value", "DP Description")+"/..//following-sibling::mat-grid-list//div[text()='Previous'])[1]");
				GenericUtils.Verify("displaying Previous description under Summary Section", refSeleniumUtils.is_WebElement_Displayed(StringUtils.replace("("+oCPWPage.Div_with_text, "value", "DP Description")+"/..//following-sibling::mat-grid-list//div[text()='Previous'])[1]"));
				GenericUtils.Verify("displaying Current description under Summary Section", refSeleniumUtils.is_WebElement_Displayed(StringUtils.replace("("+oCPWPage.Div_with_text, "value", "DP Description")+"/..//following-sibling::mat-grid-list//div[text()='Current'])[1]"));
				break;	
			case "CLAIM TYPE ADDED":
				switch(sCriteria) {
				case "Without RFP":
					oGenericUtils.clickOnElement(StringUtils.replace(oPresentationProfileValidations.sChangeOppPres, "value", Serenity.sessionVariableCalled("PresentationName")));
					refSeleniumUtils.defaultWait(ProjectVariables.TImeout_5_Seconds);
				case "Unassigned":
					sStatus=refSeleniumUtils.get_TextFrom_Locator("//div[@title='"+DP+"']/..//div[@columnindex='3']");
					GenericUtils.Verify("displaying status for DP:"+DP+" as:"+sStatus, sStatus.contains("CHANGE DP DESC") && sStatus.contains("CLAIM TYPE ADDED"));
					GenericUtils.Verify("displaying Claim Type Added for DP:"+DP+" as checked", refSeleniumUtils.Get_Value_By_given_attribute("title", "//div[@title='"+DP+"']/..//div[@columnindex='7']").contains("true"));
					GenericUtils.Verify("displaying Description for DP:"+DP+" as checked", refSeleniumUtils.Get_Value_By_given_attribute("title", "//div[@title='"+DP+"']/..//div[@columnindex='6']").contains("true"));
					oGenericUtils.clickOnElement("//div[@title='"+DP+"']/..//div[contains(@class,'arrow-right')]");
					refSeleniumUtils.defaultWait(ProjectVariables.TImeout_3_Seconds);
					//refSeleniumUtils.moveTo("("+StringUtils.replace(oCPWPage.Div_with_text, "value", "DP Description")+"/..//following-sibling::mat-grid-list//div[text()='Previous'])[1]");
					oGenericUtils.moveToElementAndClick("(//span[text()=' Opportunities']/..//mat-icon)[1]");
					refSeleniumUtils.defaultWait(ProjectVariables.TImeout_3_Seconds);
					GenericUtils.Verify("displaying claim types after post pipeline as:"+Serenity.sessionVariableCalled("CUR_MID_RULE_CLAIM_TYPES"),Arrays.asList(Serenity.sessionVariableCalled("CUR_MID_RULE_CLAIM_TYPES").toString().split(",")).containsAll(Arrays.asList(oGenericUtils.get_All_Text_from_Locator("//div[contains(@class,'claimFloatMar')]"))));
					break;
				case "With RFP":
					sStatus=refSeleniumUtils.get_TextFrom_Locator("//div[@title='"+DP+"']/..//div[@columnindex='3']");
					GenericUtils.Verify("displaying status for DP:"+DP+" as:"+sStatus, sStatus.contains("CHANGE DP DESC"));
					GenericUtils.Verify("displaying Claim Type Added for DP:"+DP+" as Unchecked", refSeleniumUtils.Get_Value_By_given_attribute("title", "//div[@title='"+DP+"']/..//div[@columnindex='7']").contains("false"));
					GenericUtils.Verify("displaying Description for DP:"+DP+" as checked", refSeleniumUtils.Get_Value_By_given_attribute("title", "//div[@title='"+DP+"']/..//div[@columnindex='6']").contains("true"));
					GenericUtils.Verify("clicking on change grid ", oGenericUtils.clickOnElement("//div[@role='tab']//span[contains(text(),'Change Opportunities')]"));
					refSeleniumUtils.defaultWait(ProjectVariables.TImeout_3_Seconds);
					userValidatesChangeSummaryData("","Unassigned");
					sStatus=refSeleniumUtils.get_TextFrom_Locator("//div[@title='"+DP+"']/..//div[@columnindex='3']");
					GenericUtils.Verify("displaying status for DP:"+DP+" as:"+sStatus, sStatus.contains("CLAIM TYPE ADDED"));
					GenericUtils.Verify("displaying Claim Type Added for DP:"+DP+" as Unchecked", refSeleniumUtils.Get_Value_By_given_attribute("title", "//div[@title='"+DP+"']/..//div[@columnindex='7']").contains("true"));
					GenericUtils.Verify("displaying Description for DP:"+DP+" as checked", refSeleniumUtils.Get_Value_By_given_attribute("title", "//div[@title='"+DP+"']/..//div[@columnindex='6']").contains("false"));
					
					break;
				}
			}
		}		
	}
