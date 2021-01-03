package project.pageobjects;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.json.simple.parser.ParseException;
import org.junit.Assert;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;

import com.paulhammant.ngwebdriver.ByAngular;

import io.restassured.response.Response;
import net.serenitybdd.core.Serenity;
import net.serenitybdd.core.annotations.findby.By;
import net.serenitybdd.core.environment.EnvironmentSpecificConfiguration;
import net.serenitybdd.core.pages.PageObject;
import net.thucydides.core.annotations.Step;
import net.thucydides.core.util.EnvironmentVariables;
import net.thucydides.core.util.SystemEnvironmentVariables;
import project.utilities.ExcelUtils;
import project.utilities.GenericUtils;
import project.utilities.MicroServRestUtils;
import project.utilities.MongoDBUtils;
import project.utilities.RestServiceUtils;
import project.utilities.SeleniumUtils;
import project.variables.JsonBody;
import project.variables.MonGoDBQueries;
import project.variables.ProjectVariables;

public class CPWPage extends PageObject {

	SeleniumUtils oSeleniumUtils;
	// FilterDrawer oFilterDrawer;
	RestServiceUtils oRestServiceUtils = new RestServiceUtils();
	GenericUtils oGenericUtils = new GenericUtils();
	// PresentationProfileValidations oPresentationProfile;

	// Locators for Opportunity Runs page

	public static String sPolicySelection = "//button[@mattooltip='Policy Drawer']";
	public static String sApplyFilters_PS = "//h3[text()='Filters']/..//span[contains(text(),'Apply Filters')]";
	public static String sSearchFeild_MP = "//div[@class='grid-search']//input";
	public static String sSearchIcon = "//div[@class='grid-search']//fa-icon";
	public static String sDPArrow = "//i[@id='dpAngle']";
	public static String sTopicArrow = "//i[@id='topicAngle']";

	public static String sTopicCheckbox = "//div[@class='aw-checkbox-cell']";

	public String DispositionDesclocator = "//button[contains(text(),'value')]/..//following-sibling::div[@class='dp-desc']/button";

	public static String sHeadearItem = null;

	public static String sSelectItem = null;

	public String payershorts_in_AWBPage = "//strong[contains(text(),'Payer Short')]/ancestor::span";

	public static String sClientPolicy = "//h1[contains(text(),'Client Policy')]";

	public static String sGeneric09 = "//h2[contains(text(),'Hi')]";
	public String LoadingIcon = "//div[contains(@class,'loadingModal__container')]";
	public String ApplyResetfiltersbutton = "//div[@class='reset-apply-buttons']//span[contains(text(),'value')]";
	public String ButtonContainsText = "//button[contains(text(),'value')]";

	public String ButtonContainsClass = "//button[contains(@class,'value')]";

	public String ButtonWithText = "//button[text()='svalue']";

	public String Header4__with_text = "//h4[text()='value']";

	public String Div_with_text = "//div[text()='value']";

	public String labelcontainstext = "(//label[contains(text(),'svalue')])";

	public String labelwithtext = "//label[text()='svalue']";

	public String NotesTextarea = "(//mat-label[text()='Note'])/..//textarea";

	public String Inputwithvalue = "//input[@value='text']";

	public String Span_with_text = "(//span[text()='value'])";

	public String Div_with_ID = "//div[@id='value']";

	public String Div_contains_text = "//div[contains(text(),'value')]";

	public String Div_contains_Class = "//div[contains(@class,'value')]";

	public static String sSearchField_AWB = "//input[@placeholder='Search Topic, DP, or DP Description']";
	public static String sSearchField_RWOpp = "//input[contains(@placeholder,'Topic, DP, or DP Description')]";

	public String CPWUserICon = "//button[@class='btn dropdown-toggle dropdown-button btn-default']/span[1][@class='glyphicon fa fa-user']";
	public String CPWSignOut = "//div[@class='dropdown-menu dropdown-menu-right']//button[contains(@class, 'btn-block btn-warning')]";
	public String CPWOppRunsLabel = "//mat-panel-title[contains(text(),'Policy Filters')]";
	public String DPworktodocount = "//span[contains(@class,'DP')]/label";
	public String warningMessage = "//span[contains(@class,'warning')]/label";

	public String Assigneepopup = "(//div[contains(text(),'Assign selected Opportunities to')]/..//div[contains(@class,'popover-content')]//div[@class='mat-radio-container'])";

	public String Assigneepopup_buttons = "//div[contains(text(),'Assign selected Opportunities to')]/following-sibling::div//button[text()='value']";

	public String CurrentDisposition_RWOpp = "(//div[contains(@onclick,'value')])";

	public String Disposition_in_DPWB = "(//p[text()='Disposition']/..//div[@class='formatstring'])[1]";

	public String DPHeader_in_DPWB = "//span[contains(text(),'DP')]//following-sibling::span[text()='value']";

	public String anchorTag_with_text = "//a[text()='value']";

	public String CPQHeaderLabel = "//label[text()='CPQ' and @name ='AppName']";

	public String CPQUserIcon = "//button[@class='btn app-button dropdown-toggle header_dropdown']/i";

	public String CPQSignOut = "//ul[@class='dropdown-menu  pull-right ']//a[@title='Sign out']";

	public String LoadingIconCPQ = "//div[contains(@class,'loadingModal_image')]";

	public static String CPD_Login_header = "//mat-card-title[text()='Client Policy Distribution']";
	public String Select_Application_Dropdown = "//mat-label[text()='Select an application']";
	public String CPD_Credentials = "//input[@formcontrolname='value']";
	public String Span_contains_text = "(//span[contains(text(),'value')])";

	public String Presentation_in_assigneePopup = "//div[contains(text(),'Assign selected Opportunities')]/following-sibling::div//span[contains(text(),'value')]";

	public String Priority_Savings_On_DP = "//label[text()='DP value']/../../../../following-sibling::mat-card-content//div[contains(@class,'data-val')]";

	public String PriorityReasons_On_DP = "(//label[text()='DP value']/../../../../following-sibling::mat-card-content//div[@class='priority-reasons'])";

	public String PrioritiesUnderMP_Topic = "(//label[contains(text(),'value')]//ancestor::mat-expansion-panel-header//following-sibling::div//div[@class='priority-val'])";

	public String PriorityReasonsUnderMP_Topic = "(//label[contains(text(),'value')]/..//span[@class='priority-reasons'])";

	public String AssignIconOnDP = "//span[text()='DP value']/../following-sibling::li//span[contains(@class,'openAssign')]";

	public String AssignPopupbuttons = "//div[@class='asign-popover_button']//button[text()='value']";

	public String Tag_with_I = "//i[text()='value']";

	public String Tag_contains_I = "//i[contains(text(),'value')]";

	public String Tag_with_Li = "//li[text()='value']";

	public String Presentations_On_DP = "//label[text()='DP value']/../../../../following-sibling::mat-card-content//li[text()='name']";

	public String Pres_DP_Savings = "//label[text()='DP value']/..//span[contains(text(),'Savings')]/following-sibling::span[contains(text(),'$')]";

	public String Pres_Payer_LOB_All_Chkbox = "//b[text()='filter']/../..//div[contains(text(),'All')]";

	public String Tag_contains_b = "//b[contains(text(),'value')]";

	public String Tag_with_P = "//p[text()='value']";

	public String Tag_contains_P = "//p[contains(text(),'value')]";

	public String Invalid_unavailable_AvailableDP_insurances = "//span[text()='DP value']/../../../..//div[@class='filtered unavailable' or @class='notApplicable' or @class='unavailable']//div[contains(text(),'insurance')]";

	public String Invalid_unavailable_Pres_insurances = "//label[text()='DP value']/../../../..//div[@class='unavailable filtered' or @class='notApplicable' or @class='unavailable']//div[contains(text(),'insurance')]";

	public String AvailableDPChkbox = "//label[text()='DP value']/../..//input";

	public String Lnk_PM = "//div[@class='mx-dataview-content']//span/a[text()='PM']";

	public String ShowDropDownData = "//span[contains(text(),'value')][@class='mat-option-text']";

	public String DPCards_in_AvailableOpportunity = "(//mat-card[contains(@class,'cardMain')]//label[contains(text(),'DP')])";

	public String Span_with_Class = "(//span[@class='value'])";

	public String Div_Start_with_text = "//div[starts-with(text(),'value')]";

	public String InsuranceChkbox_in_Presentation = "//th//span[text()='value']/..//label//span";

	public String TagContainsClass_I = "//i[contains(@class,'value')]";

	public String Exportpopupchkbox = "//b[text()='mainchkbox']/..//following-sibling::div[1]//div[contains(text(),'secondarychkbox')]";

	public String ExportCancelbutton = "//button[text()='Export']/../..//button[contains(text(),'Cancel')]";

	public String OpportunityStatus = "//span[contains(text(),'Test Only')]/..//input[@aria-checked='status']";

	// public String CaptureDecision_EnteredNotes="//div[@data-value='notes']";

	public String sSelectedReasons = "//mat-label[contains(text(),'Reasons')]/../../..//span[contains(@class,'mat-select')]/span";

	public String sMatradiobutton = "//mat-radio-button[@value='text']";

	public String popupSecondTimeCancel = "//div[@class='popup_blocker_bg']/ancestor::footer[not (@hidden)]//button[contains(text(),'Cancel')]";

	public String Notessection = "//b[text()='Notes']/ancestor::div[@class='dp_discripltion']";

	public String Tag_P_contains_Class = "//p[contains(@class,'value')]";

	public String CaptureDecison_text = "//div[@class='mat-radio-label-content' and contains(text(), 'value')]";

	public String CaptureConfirmationButton = "(//label[contains(text(),'decision capture will lock the profile')]/..//button[contains(text(),'value')])";

	public String MP_TopicSavings = "//div[text()=\"value\"]/..//following-sibling::div/div";

	public String MP_TopicSavings_Contains = "//div[contains(text(),\"value\")]/..//following-sibling::div/div";

	public String DPSavingsInAvailableDeck = "//span[text()='DP value']/../..//span[contains(text(),'$')]";

	public String PayerClaimtypeInAvailableDeck = "(//span[text()='DP value']/ancestor::section//following-sibling::table//span[@class='txt-black'])";

	public String InsurancesInAvailableDeck = "(//span[text()='DP value']/ancestor::section//following-sibling::table//th[not(contains(@class,'payerShort'))])";

	public String PresentationViewDPKey = "//label[@class='dpIdLabel'][text()='DP value']";

	public String ClinetToClick = "//table[@role='presentation']//td//span[contains(text(),'clientNameVal')]//ancestor::td//preceding-sibling::td//span[contains(text(),'ReleaseVal')]";

	public String midRuleStatusXPath = "//p[@class ='midrule-name']/span";

	public String nonRecordsOfMedicalPolicies = "//table[@role='presentation']//tr//td[contains(text(),'No records available.')]";

	public String nonRecordsOfOpportunities = "//div[contains (text(),' No results found that meet the search criteria.')]";
	// public String nonRecordsOfOpportunities="//div[@class='content
	// ng-star-inserted']";

	public String reviewWorkedOpportunities = "//button//span[contains(text(),'Review Worked Opportunities')]";

	public String MedPolicyInputXpath = "//button[contains(text(),'getData')]//preceding-sibling::input";
	public String GetDataXpath = "//button[contains(text(),'getData')]";
	public String SearchFileldXpath = "//div[@class='searchBox']//input";
	public String SearchButtonXpath = "//div[@class='searchBox']//button[@class='search-button mat-flat-button mat-button-base']";
	public String DPKeyAllCheckBoxes = "//div[@class='dp-number']/a[text()='DPKeyArg']//ancestor::td//parent::tr//following-sibling::tr//div[@class='checkbox']";
	public String DPKeyCheckBoxXPath = "//div[@class='dp-number']//button[text()='DPKey']//ancestor::td//div[@class='checkbox']";
	public String TopicExpandButton = "(//div[@class='analysisWorkbench']//div[@class='gridBox ng-star-inserted']//div[@class='payer-group-header ng-star-inserted']//button[@class='mat-flat-button mat-button-base'])[1]";
	public String CaptureDispoBtn = "//div[@class='dispositionsMenu']//button[contains(@class,'dispositions-menu-button')]";
	public String PresentDisposition = "(//button[contains(text(),'Present')])[1]";
	public String PolicySelectionDrawerButton = "//button[@class='policy-selection-button mat-flat-button mat-button-base mat-accent']";
	public String MedPolicySearchBox = "//div[@class='policy-drawer']//div[@class='mat-form-field-infix']//input[contains(@class,'mat-input-element')]";
	public String MedPolicySearchButton = "//div[@class='policy-drawer']//button[@class='search-button mat-flat-button mat-button-base']";
	public String MedPolicyAfterSearch = "//div[@class='policy-drawer']//table[@class='k-grid-table']//div[contains(text(),'MedPolicyValue')]";
	public String ApplyToOpportunityGridBtn = "//div[@class='policy-drawer']//button/span[contains(text(), 'Apply To Opportunity Grid')]/parent::butt";
	public String HighSavingsChkboxXpath = "//div[@class= 'present-disposition-content']//span[text()='High Savings']//ancestor::mat-checkbox//div";
	public String PresentDispositionOKBtn = "//mat-dialog-actions//button/span[text()=' Ok ']//parent::button";
	public String WarningMessageXpath = "//simple-snack-bar//span[@class='mat-button-wrapper']";
	public String TopicRuleRelationshipIcon = "(//i[@class='fa fa-bookmark'])[1]";
	public String DPRuleRelationshipIcon = "(//i[@class='fa fa-bookmark'])[2]";

	public String OppGrid_ClientsList = "(//span[contains(text(),'20')][@class='mat-button-wrapper'][not (contains(text(),'$')) and not (contains(text(),','))])";

	public String Text_Contains_tag_b = "//b[contains(text(),'value')]";

	public String WorkedOpportunityHeader = "//span[contains(text(),'Export Dispositions')]";

	public String AWBgriSearchbutton = "//input[contains(@placeholder,'Topic, DP, or DP Description')]/ancestor::mat-form-field/following-sibling::button";

	public String Text_contains_header3 = "//h3[contains(text(),'value')]";

	public String DPCards_in_Presentation = "(//label[contains(text(),'topic')]/ancestor::mat-expansion-panel-header/following-sibling::div//label[contains(@class,'dp')])";

	public String changeOppCol = "//div[contains(@id,coloumn)]/..//span[text()='value']";

	public static String AWBgridheader = "//div[contains(text(),'Opportunities(RVA)')]";
	public String Div_with_class = "(//div[@class='value'])";
	public String ClientInAWBDropdown = "//span[contains(text(),'client')][@class='mat-option-text']";
	public String StrongtagContainstext="(//strong[contains(text(),value)])";
	public String Tag_th_contains_text="//th[contains(text(),'value')]";
	

	// *****************************************Page Related
	// Methods**********************************************************************************

	public boolean DynamicWaitfortheLoadingIconWithCount(int count) {
		boolean bstatus = false;
		int j = 1;
		System.out.println("Loading is in progress");

		do {
			oSeleniumUtils.defaultWait(ProjectVariables.TImeout_2_Seconds);
			j = j + 1;

			if (j == count) {
				break;
			}

			bstatus = oSeleniumUtils.is_WebElement_Displayed(LoadingIcon);

		} while (bstatus);

		return bstatus;

	}

	// Logout 'CPW' Application
	public void Logout_CPW() {
		getDriver().quit();

	}

	// Login CPW Application
	public void Login_CPW(String sUser) throws Exception {		
		SeleniumUtils oSeleniumUtils = this.switchToPage(SeleniumUtils.class);
		// try{
		Serenity.setSessionVariable("user").to(sUser);

		// Launch Application
		Serenity.setSessionVariable("user").to(sUser);
		System.out.println("AppUrl==========>" + ProjectVariables.CPW_QA_URL);
		getDriver().manage().deleteAllCookies();
		getDriver().get(ProjectVariables.CPW_QA_URL);
		getDriver().manage().window().maximize();
		System.out.println("Waiting to Load Login Page of CPW........");
		// Verify Application page
		Assert.assertTrue("'Client Policy Distribution' header text was not displayed in the Login page of CPD Page",
				oGenericUtils.isElementExist(CPD_Login_header));
		// Enter Username
		Assert.assertTrue("Unable to enter the username in the Login page of CPD Page",
				oGenericUtils.setValue(By.xpath(StringUtils.replace(CPD_Credentials, "value", "username")), sUser));
		// Enter Password

		System.out.println(
				oSeleniumUtils.is_WebElement_Displayed(StringUtils.replace(CPD_Credentials, "value", "password")));
		System.out.println(Get_Password_For_the_given_url(sUser));

		Assert.assertTrue("Unable to enter the password in the Login page of CPD Page",
				oSeleniumUtils.Enter_given_Text_Element(StringUtils.replace(CPD_Credentials, "value", "password"),
						Get_Password_For_the_given_url(sUser)));

		Assert.assertTrue("Unable to select an application dropdown of CPD Page",
				oGenericUtils.clickButton(By.xpath("//div[contains(@class,'mat-select-value')]")));

		Assert.assertTrue("Unable to click the 'CPW' in select adn applciation drodown of CPD Page",
				oGenericUtils.clickButton(By.xpath(StringUtils.replace(Span_contains_text, "value", "CPW"))));

		Assert.assertTrue("Unable to click the Login Button of CPW Page",
				oGenericUtils.clickButton(By.xpath("//span[text()='Login']")));

		Thread.sleep(10000);
		// Loading POPUP
		oSeleniumUtils.DynamicWaitfortheLoadingIconWithCount(50);

		// Verify HomePage
		GenericUtils.Verify("AWB Page should be displayed,after clicking on the Login Button of CPW Page",
				oGenericUtils.isElementExist(AWBgridheader, 30));

	}

	// ****************************************SelectPayer************************************************************************************

	public void SelectPayer(String sRunDate, String sClient) {

		oSeleniumUtils.clickGivenXpath(StringUtils.replace(Div_with_class, "value", "selectionHolder"));
		oSeleniumUtils.defaultWait(ProjectVariables.TImeout_3_Seconds);
		oSeleniumUtils.clickGivenXpath(StringUtils.replace(ClientInAWBDropdown, "client", sClient));
		oSeleniumUtils.defaultWait(ProjectVariables.TImeout_3_Seconds);
		DynamicWaitfortheLoadingIconWithCount(10);
		GenericUtils.Verify("Client '" + sClient + "' should be displayed,after selcting in the dropdown",
				oSeleniumUtils.is_WebElement_Displayed(StringUtils.replace(Span_contains_text, "value", sClient)));

	}

	// *****************************************PolicySelection**********************************************************************************
	public void SelectPolicySelectionAndApplyFilters(String sMedicalPolicy) throws InterruptedException {

		// select the policy selection drawer and apply all checkboxes in that
		// section
		SelectthePolicySelectionDrawerandApplyAllFilters();

		// To Selcet Display MPs/Topic toggle selection
		SelectTheDisplayMPTopicToggleinFilterPanel("MP");//--topic

		// To select the given MP in filterPanel and applytoopportunity grid
		SelectGivenMPinFilterPanel(sMedicalPolicy, "MP");
	}
	
	public void SelectTheDisplayMPTopicToggleinFilterPanel(String dataCriteria)
	{
		
		boolean bstatus=false;
		String dataType=null;
		switch(dataCriteria)
		{
		case "MP":
			bstatus=oSeleniumUtils.is_WebElement_Displayed(StringUtils.replace(Span_contains_text, "value", "Display "+dataCriteria+""));
			if(!bstatus)
			{
				dataType="Topic";
			}
		break;
		case "Topic":
			bstatus=oSeleniumUtils.is_WebElement_Displayed(StringUtils.replace(Span_contains_text, "value", "Display "+dataCriteria+""));
			if(!bstatus)
			{
				dataType="MP";
			}
		break;
		default:
			Assert.assertTrue("Case not found==>"+dataCriteria,false);
		break;
		}
		
		
		if(!bstatus)
		{
			oSeleniumUtils.clickGivenXpath(StringUtils.replace(Span_contains_text, "value", "Display "+dataType+""));
			DynamicWaitfortheLoadingIconWithCount( 10);
			GenericUtils.Verify("'Display "+dataCriteria+"' Toggle selection should be displayed in filterpanel", oSeleniumUtils.is_WebElement_Displayed(StringUtils.replace(Span_contains_text, "value", "Display "+dataCriteria+"")));
				
		}
		else
		{
			GenericUtils.Verify("'Display "+dataCriteria+"' Toggle selection is displayed in filterpanel", true);
		}
		
	}

	public void SelectGivenMPinFilterPanel(String sMedicalPolicy,String criteria) throws InterruptedException {
		
		
		List<String> checkboxexList=Arrays.asList(ProjectVariables.filters.split(","));
		
		selectgivenFiltersMP_Topic(sMedicalPolicy, criteria);
		//Click on 'Apply to Opportunity Grid'
		Assert.assertTrue("Unable to click the 'ApplyToOpportunityGrid' button in the policy selection drawer in AWB Page",oGenericUtils.clickOnElement(StringUtils.replace(Span_contains_text, "value", "Apply To Opportunity Grid")));
		//Loading POPUP	
		oSeleniumUtils.defaultWait(ProjectVariables.TImeout_5_Seconds);
		DynamicWaitfortheLoadingIconWithCount(20);
		//defaultWait(ProjectVariables.TImeout_3_Seconds);
        //oAppUtils.DynamicWaitfortheLoadingIconWithCount(30);
 
		
		for (int i = 0; i < checkboxexList.size(); i++) 
		{
			Assert.assertTrue("'"+checkboxexList.get(i)+"' filterhead checkbox is unable to checked in the AWB Page", ApplyFilters(checkboxexList.get(i), "", "CHECK", ""));
		}
		oSeleniumUtils.defaultWait(ProjectVariables.TImeout_3_Seconds);
		Assert.assertTrue("'Saving Status' filterhead checkbox is unable to checked in the AWB Page", ApplyFilters("Savings Status", "", "CHECK", StringUtils.replace(ApplyResetfiltersbutton, "value", "Apply Filters")));
		oSeleniumUtils.defaultWait(ProjectVariables.TImeout_3_Seconds);
		
		
	}

	public void selectgivenFiltersMP_Topic(String sMedicalPolicy, String criteria) throws InterruptedException {
		if(oSeleniumUtils.is_WebElement_Enabled(StringUtils.replace(Div_contains_Class, "value", "selectall-reset-buttons")+"/button[2]"))
		{
			oSeleniumUtils.clickGivenXpath(StringUtils.replace(Div_contains_Class, "value", "selectall-reset-buttons")+"/button[2]");
			oSeleniumUtils.defaultWait(ProjectVariables.TImeout_5_Seconds);
				
		}
		//Enter Medical Policy in 'Search'feild
		Thread.sleep(2000);
		Assert.assertTrue("Unable to enter the medicalpolicy in the search box of policy selection drawer in AWB Page,medicalpolicy ==>"+sMedicalPolicy,oGenericUtils.setValue(By.xpath(sSearchFeild_MP), sMedicalPolicy));
		Thread.sleep(2000);
		//Click 'Search' icon
		Assert.assertTrue("Unable to click the search icon in the policy selection drawer in AWB Page",oGenericUtils.clickButton(By.xpath(sSearchIcon)));
		
		if(criteria.equalsIgnoreCase("Topic"))
		{
			int itopisize=oSeleniumUtils.get_Matching_WebElement_count(StringUtils.replace(StrongtagContainstext, "value", "\""+sMedicalPolicy+"\""));
			for (int i = 1; i <=itopisize; i++) 
			{
				String sTopic=oSeleniumUtils.get_TextFrom_Locator(StringUtils.replace(StrongtagContainstext, "value", "\""+sMedicalPolicy+"\"")+"["+i+"]").trim();
				if(sTopic.equalsIgnoreCase(sMedicalPolicy))
				{
					//Select 'Medical Policy/Topic'
					Assert.assertTrue("Unable to click the Topic '"+sMedicalPolicy+"' in the policy selection drawer in AWB Page",oGenericUtils.clickOnElement(StringUtils.replace(StrongtagContainstext, "value", "\""+sMedicalPolicy+"\"")+"["+i+"]"));
					break;
				}
			}
					
		}
		else
		{
			//Select 'Medical Policy/Topic'
			Assert.assertTrue("Unable to click the medicalpolicy '"+sMedicalPolicy+"' in the policy selection drawer in AWB Page",oGenericUtils.clickOnElement(StringUtils.replace(Div_contains_text, "value", sMedicalPolicy)));
			
		}
	}
	
	public void SelectthePolicySelectionDrawerandApplyAllFilters() throws InterruptedException {

		// To open filter panel
		OpenFilterPanel();

		// Select All Filters like ''Payer Short,Insurance,Product
		if (Serenity.sessionVariableCalled("sPayershort") == null) {
			Assert.assertTrue("Unable to check the 'Payershort' header checkbox in the policy selection drawer",
					ApplyFilters("Payer Short", "", "CHECK", ""));
		} else {
			ApplyFilters("Payer Short", "", "UNCHECK", "");
			ApplyFilters("Payer Short", Serenity.sessionVariableCalled("sPayershort").toString(), "CHECK", "");

		}
		Assert.assertTrue("Unable to check the 'Insurance' header checkbox in the policy selection drawer",
				ApplyFilters("Insurance", "", "CHECK", ""));
		Assert.assertTrue(
				"Unable to check the 'Product' header checkbox and apply filter button in the policy selection drawer",
				ApplyFilters("Product", "", "CHECK", ""));
		Assert.assertTrue(
				"Unable to check the 'latest Client Decision' header checkbox and apply filter button in the policy selection drawer",
				ApplyFilters("Latest Client Decision", "", "CHECK", sApplyFilters_PS));

		// Loading POPUP
		// oAppUtils.WaitUntilPageLoad();

		oSeleniumUtils.defaultWait(ProjectVariables.TImeout_5_Seconds);
		// Loading POPUP
		DynamicWaitfortheLoadingIconWithCount(30);

	}

	public void OpenFilterPanel() {
		// Click 'PolicySelection
		Assert.assertTrue("Unable to click the policy selection drawer link in the AWB Page",
				oGenericUtils.clickButton(By.xpath(sPolicySelection)));
		// Loading POPUP
		DynamicWaitfortheLoadingIconWithCount(20);
		// Verify 'Choose a Medical PolicyTopic' screen

		boolean sGetMedicalPolicy = oGenericUtils.isElementExist("//h3[.='Choose a Medical Policy/Topic']",
				ProjectVariables.TImeout_5_Seconds);
		if (!sGetMedicalPolicy) {
			Assert.assertTrue("Unable to click the policy selection drawer link in the AWB Page",
					oGenericUtils.clickButton(By.xpath(sPolicySelection)));
		}

	}

	// *********************************************APPLY_FILETRS************************************************************************************
	public boolean ApplyFilters(String sHeadearItem, String sSelectItem, String sChbk_Operation, String sApplyFilters) {
		boolean bstatus=false;
		String sChbk_HeadearChild=null;
		//try{
			if(sSelectItem.isEmpty())
			{
				
				 sChbk_HeadearChild="//span[contains(text(),'"+sHeadearItem+"')]/ancestor::mat-checkbox";
			}
			else if(sHeadearItem.equalsIgnoreCase("Product"))
			{
				sChbk_HeadearChild="//span[contains(text(),'"+sHeadearItem+"')]/ancestor::mat-tree-node//following-sibling::mat-tree-node//span[contains(text(),'"+sSelectItem+"')][not (contains(text(),'Professional'))]/ancestor::mat-checkbox";
			}
			else
			{
				sChbk_HeadearChild="//span[contains(text(),'"+sHeadearItem+"')]/ancestor::div[@class='select-all']//following-sibling::div//div[contains(text(),'"+sSelectItem+"')]/preceding-sibling::mat-pseudo-checkbox";
			}
			if(sHeadearItem.equalsIgnoreCase("Latest Client Decision")&&Serenity.sessionVariableCalled("Pagename")!="RWO")
			{
				oSeleniumUtils.scrollingToGivenElement(getDriver(), sChbk_HeadearChild);
				oSeleniumUtils.defaultWait(ProjectVariables.TImeout_2_Seconds);
			}
			
			oSeleniumUtils.clickGivenXpath(sChbk_HeadearChild);
			DynamicWaitfortheLoadingIconWithCount(50);
			
			//boolean sItem = getDriver().findElement(By.xpath(sChbk_HeadearChild)).isSelected();||sClass.contains("indeterminate")
			String sClass=oSeleniumUtils.Get_Value_By_given_attribute("class", sChbk_HeadearChild);
			boolean sItem =sClass.contains("checked");
			//Check Condition
			if((!sItem) && (sChbk_Operation=="CHECK")){
				bstatus=oSeleniumUtils.clickGivenXpath(sChbk_HeadearChild);
				DynamicWaitfortheLoadingIconWithCount(20);
				Assert.assertTrue("unable to click the '"+sHeadearItem+"' header checkbox for filteroption '"+sSelectItem+"'", bstatus);
			}
			else
			{
				bstatus=true;	
			}
			//UnCheck Condition
			if((sItem) && (sChbk_Operation=="UNCHECK")){
				bstatus= oSeleniumUtils.clickGivenXpath(sChbk_HeadearChild);
				DynamicWaitfortheLoadingIconWithCount(20);
				Assert.assertTrue("unable to un-click the '"+sHeadearItem+"' header checkbox for filteroption '"+sSelectItem+"'", bstatus);
			}
			else
			{
				bstatus=true;
			}
			//Click on 'Apply Filters'
			if(!sApplyFilters.isEmpty()){
				bstatus=oSeleniumUtils.clickGivenXpath(sApplyFilters);
				DynamicWaitfortheLoadingIconWithCount(20);
				Assert.assertTrue("unable to click Applyfilter button", bstatus);
			}
			else
			{
				bstatus=true;
			}


		//}catch(Exception e){
			//GenericUtils.Verify("Object not clicked Successfully , Failed due to :="+e.getMessage(),"FAILED");
		//}
		return bstatus;
		
	}

	public void capture_the_data_at_DP_level(String criteriatype, String disposition, String updateddisposition)
			throws InterruptedException {

		ArrayList<String> sGetTopicItems = new ArrayList<>();
		ArrayList<String> sGetDPItems = new ArrayList<>();

		String Payershorts = oSeleniumUtils.get_TextFrom_Locator(payershorts_in_AWBPage);

		List<String> UIPayershortlist = Arrays
				.asList(StringUtils.substringAfter(Payershorts, "Payer Short: ").split(","));

		if (criteriatype.equalsIgnoreCase("Single DPKey")) {
			sGetDPItems = SelectDPKeysInOpportunityGridofAWBPage("TOPIC_DPKEY_CHECKBOX", 1);
			System.out.println("DP Key ===>" + sGetDPItems);
			System.out.println("Selected the single DPKey successfully");
		} else if (criteriatype.equalsIgnoreCase("Multiple DPKeys")) {
			sGetDPItems = SelectDPKeysInOpportunityGridofAWBPage("TOPIC_DPKEY_CHECKBOX", 4);
			System.out.println("DPKEys ===>" + sGetDPItems);
			System.out.println("Selected the multiples DPKeys successfully");
		} else if (criteriatype.equalsIgnoreCase("Multiple Topics")) {
			sGetDPItems = SelectDPKeysInOpportunityGridofAWBPage("TOPIC", 4);
			System.out.println("TOPICS ===>" + sGetDPItems);
			System.out.println("Selected the Multiple Topics successfully");
		} else if (criteriatype.equalsIgnoreCase("Multiple MPs")) {
			// To Open RWO Page
			Open_the_Review_Worked_Opportunity_Page();
			select_the_filters_in_RWO_Page("", "", "");

			sGetDPItems = SelectDPKeysInOpportunityGridofAWBPage("MEDICAL POLICY", 4);
			System.out.println("MEDICAL POLICYS ===>" + sGetDPItems);
			System.out.println("Selected the Multiple MPs successfully");
		}

		click_the_given_cheveron("TOPIC", "up");

		oSeleniumUtils.defaultWait(ProjectVariables.TImeout_5_Seconds);

		// To Retrieve the 'No Disposition' PPS Count from Mongo DB
		// MongoDBUtils.Disposition_Records_in_Mongo_DB_For_the_given_PPS_in_AWB_Page(Serenity.sessionVariableCalled("clientkey"),
		// UIPayershortlist, Serenity.sessionVariableCalled("release"),
		// Serenity.sessionVariableCalled("Medicalpolicy"), sGetTopicItems,
		// sGetDPItems, "Opportunity", "No Disposition", "AWBGrid");

		// System.out.println("Successsfully retrieved the No disposition PPS
		// count from mongo DB for the selected DPKey ==>"+sGetDPItems);

		// To Initialize the disposition fields
		Intialaize_the_Dispositions_fields_to_post(disposition);

		if (criteriatype.equalsIgnoreCase("Multiple MPs")) {
			// Capturing the disposition operation for the given client,release
			// and disposition
			Perform_the_capture_disposition_operation(disposition, ProjectVariables.DispositionReasons,
					ProjectVariables.DispositionNotes, "Review Worked Opportunity");

		} else {
			// Capturing the disposition operation for the given client,release
			// and disposition
			Perform_the_capture_disposition_operation(disposition, ProjectVariables.DispositionReasons,
					ProjectVariables.DispositionNotes, "AWBPage");

			// Verify the captured data is displaying in AWB Grid or not
			verify_the_captured_data_is_not_displayed_in_the_given(sGetDPItems, "DPKey", "AWB Page");

		}
		Serenity.setSessionVariable("DispositionNotes").to(ProjectVariables.DispositionNotes);

		System.out.println("Successsfully captured the Disposition '" + disposition + "' for the selected DPKey ==>"
				+ sGetDPItems);

		System.out.println("Verified the captured DPKEY is not visible in the AWB Grid as expected,Disposition '"
				+ disposition + "'for the selected DPkeys ==>" + sGetDPItems);

		// To Retrieve the Captured PPS Count from Mongo DB
		// MongoDBUtils.Disposition_Records_in_Mongo_DB_For_the_given_PPS_in_AWB_Page(Serenity.sessionVariableCalled("clientkey"),
		// UIPayershortlist, Serenity.sessionVariableCalled("release"),
		// Serenity.sessionVariableCalled("Medicalpolicy"), sGetTopicItems,
		// sGetDPItems, "Opportunity", disposition, "ReviewGrid");

		// System.out.println("Successsfully retrieved the PPS count from mongo
		// DB for the captured DPkeys ==>"+sGetDPItems);

		// validate the captured PPS disposition with MongoDB
		// validate_the_captured_pps_with_mongo_DB(disposition,sGetDPItems,"DPkeys");

		System.out.println(
				"Validation was done between the PPS before captured captured and after captured with Mongo DB for the captured DPkeys ==>"
						+ sGetDPItems + " ,Disposition '" + disposition);

		String sDPKey = Serenity.sessionVariableCalled("DPkey").toString();
		if (!sDPKey.contains(",")) {
			sDPKey = sDPKey.replace("[", "");
			Serenity.setSessionVariable("DPkey").to(sDPKey.replace("]", ""));
			MongoDBUtils.GettheCapturedDispositionPayerLOBClaimTypesFromtheGiven(sDPKey.replace("]", ""));
		}

	}

	// **********************************************SelectDPKeysInOpportunityGridofAWBPage()****************************************************************
	public ArrayList<String> SelectDPKeysInOpportunityGridofAWBPage(String sFunctionality, int sCount) {

		ArrayList<String> sArr = new ArrayList<String>();
		String sArrowup = null;
		String sArrowdown = null;
		String sRows = null;
		String sCheckbox = null;
		String sGetDPKey = null;
		boolean sTopicArrow = false;
		try {

			int i = 0;
			// ============================================================================>
			switch (sFunctionality.toUpperCase()) {
			case "MEDICAL POLICY":
				sArrowdown = "//div[@class='mp-box']//fa-icon/*[name()='svg'][@data-icon='angle-double-down']";
				sArrowup = "//div[@class='mp-box']//fa-icon/*[name()='svg'][@data-icon='angle-double-up']";
				sRows = "//div[contains(@class,'md-desc')]//label";

				sTopicArrow = oSeleniumUtils.is_WebElement_Displayed(sArrowdown);
				// sTopicArrow=oGenericUtils.isElementExist(sArrowdown,ProjectVariables.MIN_COUNT);
				if (!sTopicArrow) {
					oSeleniumUtils.clickGivenXpath(sArrowup);
				}
				break;
			case "TOPIC":

				if (oSeleniumUtils.is_WebElement_Displayed(WorkedOpportunityHeader)) {
					Assert.assertTrue("unable to click the Medicalpolicy chevron in the RWO Grid",
							oSeleniumUtils.clickGivenXpath("//div[@class='mp-box']//fa-icon"));
				}

				sArrowdown = "//div[@class='topic-box']//fa-icon/*[name()='svg'][@data-icon='angle-double-down']";
				sArrowup = "//div[@class='topic-box']//fa-icon/*[name()='svg'][@data-icon='angle-double-up']";
				sRows = "//div[contains(@class,'topic-desc')]//label";

				sTopicArrow = oSeleniumUtils.is_WebElement_Displayed(sArrowdown);
				// sTopicArrow=oGenericUtils.isElementExist(sArrowdown,ProjectVariables.MIN_COUNT);
				if (!sTopicArrow) {
					oSeleniumUtils.clickGivenXpath(sArrowup);
				}
				break;
			case "DPKEYS":
			case "TOPIC_DPKEY_CHECKBOX":
			case "DPKEY_CLICK":
				sArrowdown = "//div[@class='dp-box']//fa-icon/*[name()='svg'][@data-icon='angle-double-down']";
				sArrowup = "//div[@class='dp-box']//fa-icon/*[name()='svg'][@data-icon='angle-double-up']";
				sRows = "//div[contains(@class,'dp-desc')]//span";

				sTopicArrow = oSeleniumUtils.is_WebElement_Displayed(sArrowup);
				// sTopicArrow=oGenericUtils.isElementExist(sArrowup,ProjectVariables.MIN_COUNT);

				if (!sTopicArrow) {
					oSeleniumUtils.clickGivenXpath(sArrowdown);
					oSeleniumUtils.defaultWait(ProjectVariables.TImeout_5_Seconds);

				}

				break;
			default:

				Assert.assertTrue("Given selection was not found ==>" + sFunctionality, false);
				break;

			}
			// ============================================================================>

			// Retrieving Total rows
			if (sCount > 0) {
				List<WebElement> sRowCount = getDriver().findElements(By.xpath(sRows));
				if (sRowCount.size() == 0) {
					Assert.assertTrue("'" + sFunctionality + "' Records not available for Selected MedicalPolicy '"
							+ Serenity.sessionVariableCalled("Medicalpolicy") + "'", false);
				}
				int iRequiredcount = 0;

				if (sRowCount.size() > sCount) {
					iRequiredcount = sCount;
				} else if (sRowCount.size() < sCount) {
					iRequiredcount = sRowCount.size();
				} else {
					iRequiredcount = sRowCount.size();
				}
				if (!sFunctionality.equalsIgnoreCase("DPKEY_CLICK")) {
					for (i = 1; i <= iRequiredcount; i++) {

						if (sFunctionality.equalsIgnoreCase("MEDICAL POLICY")) {
							sGetDPKey = "(//div[contains(@class,'md-desc')]//label)[" + i + "]";
							sCheckbox = "(//div[contains(@class,'md-desc')]//label)[" + i + "]/..//input";
						} else if (sFunctionality.equalsIgnoreCase("TOPIC")) {
							sGetDPKey = "(//div[contains(@class,'topic-desc')]//label)[" + i + "]";
							sCheckbox = "(//div[contains(@class,'topic-desc')]//label)[" + i + "]/..//input";
						} else {
							sGetDPKey = "(//div[contains(@class,'dp-number')]/button)[" + i + "]";
							sCheckbox = "(//div[contains(@class,'dp-desc')]//span)[" + i + "]/../../..//input";
						}

						sTopicArrow = oSeleniumUtils.is_WebElement_Displayed(sCheckbox);

						Assert.assertTrue(sFunctionality + " checkbox was not displayed in the grid", sTopicArrow);

						Assert.assertTrue("Unable to select the '" + sFunctionality + "' checkbox in the Grid",
								oSeleniumUtils.clickGivenXpath(sCheckbox));
						sArr.add(getDriver().findElement(By.xpath(sGetDPKey)).getText());
						System.out.println("Get Data:==" + getDriver().findElement(By.xpath(sGetDPKey)).getText());
					}
				} else {

					// Get Current Disposition
					// Serenity.setSessionVariable("sCurrentDP").to(oGenericUtils.GetElementText("(//div[contains(@class,'dp-number')]/button/ancestor::tr)[1]//td[11]/button"));
					Serenity.setSessionVariable("sTopic").to(
							oSeleniumUtils.get_TextFrom_Locator("(//div[contains(@class,'topic-desc')]//label)[1]"));

					if (Serenity.sessionVariableCalled("Pagename") != null && Serenity.sessionVariableCalled("Pagename")
							.toString().equalsIgnoreCase("Review Worked Opportunities")) {
						Serenity.setSessionVariable("sCurrentDP").to(oSeleniumUtils.get_TextFrom_Locator(
								"(//div[contains(@class,'dp-number')]/button/ancestor::tr)[1]//td[11]/button"));
						Serenity.setSessionVariable("Medicalpolicy").to(
								oSeleniumUtils.get_TextFrom_Locator("(//div[contains(@class,'md-desc')]//label)[1]"));
					}

					// =====================================================================>
					String sGetDPVal = getDriver()
							.findElement(By.xpath("(//div[contains(@class,'dp-number')]/button)[1]")).getText();
					sArr.add(sGetDPVal);
					getDriver().findElement(By.xpath("(//div[@class='dp-number']//button)[1]")).click();
					// Verifying DP screen displayed
					oGenericUtils.isElementExist("//h3[contains(text(),'" + sGetDPVal + "')]");
					// oGenericUtils.isElementExist("span", sGetDPVal);
				}

				// }else{
				// GenericUtils.Verify("These many '"+sFunctionality+"' Records
				// not available for Selected MedicalPolicy
				// '"+Serenity.sessionVariableCalled("Medicalpolicy")+"',reuquired
				// count===>"+sCount,"FAILED");
				// }
			} else {
				System.out.println(
						"Given checkboxes count for '" + sFunctionality + "' was zero from the gherkin ==>" + sCount);
			}

		} catch (Exception e) {
			GenericUtils.Verify("Object not clicked Successfully , Failed due to :=" + e.getMessage(), "FAILED");

		}

		if (sFunctionality.equalsIgnoreCase("Medical Policy")) {
			Serenity.setSessionVariable("Medicalpolicy").to(sArr);
			System.out.println("Medialpolicy ==>" + Serenity.sessionVariableCalled("Medicalpolicy").toString());
		} else if (sFunctionality.equalsIgnoreCase("Topic")) {
			Serenity.setSessionVariable("Topic").to(sArr);
			System.out.println("Topic ==>" + Serenity.sessionVariableCalled("Topic").toString());
		} else if (sFunctionality.equalsIgnoreCase("DPKeys")
				|| sFunctionality.equalsIgnoreCase("TOPIC_DPKEY_CHECKBOX")) {
			Serenity.setSessionVariable("DPkey").to(sArr);
			System.out.println("DPKeys ==>" + Serenity.sessionVariableCalled("DPkey").toString());
		}

		return sArr;

	}

	public void Intialaize_the_Dispositions_fields_to_post(String postedOpportunity) {

		switch (postedOpportunity) {
		case "Present":

			ProjectVariables.Disposition = "Present";
			ProjectVariables.DispositionReasons = "High Savings";

			break;
		case "Do Not Present - CPM Review":

			ProjectVariables.Disposition = "Do Not Present - CPM Review";
			ProjectVariables.DispositionReasons = "Adverse quality score impact";

			break;
		case "Do Not Present":

			ProjectVariables.Disposition = "Do Not Present";
			ProjectVariables.DispositionReasons = "Adverse quality score impact";

			break;

		case "Invalid":

			ProjectVariables.Disposition = "Invalid";
			ProjectVariables.DispositionReasons = "Adverse quality score impact";

			break;

		case "Not Reviewed":

			ProjectVariables.Disposition = "Not Reviewed";
			ProjectVariables.DispositionReasons = "";

			break;

		default:
			Assert.assertTrue(
					"Given selection '" + postedOpportunity
							+ "' was not found in the switch case method======>Intialaize_the_Dispositions_fields_to_post",
					false);
			break;

		}

	}

	public void Perform_the_capture_disposition_operation(String disposition, String dispositionreasons,
			String dispositionnotes, String pagename) throws InterruptedException {

		// validating Capture disposition window popup
		validate_the_capture_disposition_dropdown(pagename);

		if (disposition.equalsIgnoreCase("Present")) {
			Assert.assertTrue(
					"'" + disposition + "' disposition is unable to clicked in the Capture Disposition dropdown of "
							+ pagename + " Page",
					oSeleniumUtils.clickGivenXpath(
							"(//div[@class='mat-menu-content']/button[contains(text(),'Present')])[1]"));
		} else if (disposition.equalsIgnoreCase("Do Not Present")) {
			Assert.assertTrue(
					"'" + disposition + "' disposition is unable to clicked in the Capture Disposition dropdown of "
							+ pagename + " Page",
					oSeleniumUtils.clickGivenXpath(
							"(//div[@class='mat-menu-content']/button[contains(text(),'" + disposition + "')])[2]"));
		} else {
			Assert.assertTrue(
					"'" + disposition + "' disposition is unable to clicked in the Capture Disposition dropdown of "
							+ pagename + " Page",
					oSeleniumUtils.clickGivenXpath(
							"(//div[@class='mat-menu-content']/button[contains(text(),'" + disposition + "')])"));
		}

		oSeleniumUtils.defaultWait(ProjectVariables.TImeout_3_Seconds);

		if (disposition.equalsIgnoreCase("Not Reviewed")) {
			Assert.assertTrue(
					"Unable to enter the notes in the notes textarea of '" + disposition
							+ "' disposition window popup in the Capture Disposition dropdown of " + pagename + " Page",
					oSeleniumUtils.Enter_given_Text_Element(NotesTextarea, dispositionnotes));
		} else {
			if (disposition.equalsIgnoreCase("Present")) {
				Assert.assertTrue(
						"Priority 'High' radio button is not selected by default,it should be selected in the Present Capture disposition window of "
								+ pagename + "",
						oSeleniumUtils.is_WebElement_Selected("//input[@value='High']"));

				Assert.assertTrue(
						"Prioriy '" + ProjectVariables.Priority + "' radio button was unable to clicked in the '"
								+ disposition + "' disposition window popup in the Capture Disposition dropdown of "
								+ pagename + " Page",
						oSeleniumUtils.clickGivenXpath(
								StringUtils.replace(Div_contains_text, "value", ProjectVariables.Priority)));
			}

			Assert.assertTrue(
					"" + dispositionreasons + " reason checkobx was unable to clicked in the '" + disposition
							+ "' disposition window popup in the Capture Disposition dropdown of " + pagename + " Page",
					oSeleniumUtils
							.clickGivenXpath(StringUtils.replace(Span_contains_text, "value", dispositionreasons)));
			Assert.assertTrue(
					"Unable to enter the notes in the notes textarea of '" + disposition
							+ "' disposition window popup in the Capture Disposition dropdown of " + pagename + " Page",
					oSeleniumUtils.Enter_given_Text_Element(NotesTextarea, dispositionnotes));
		}

		Assert.assertTrue(
				"Unable to click the 'Ok' button in the window popup,after clicking on the disposition '" + disposition
						+ "' in the Capture Disposition dropdown of " + pagename + " Page",
				oSeleniumUtils.clickGivenXpath(StringUtils.replace(Span_contains_text, "value", "Ok")));

		oSeleniumUtils.defaultWait(ProjectVariables.TImeout_3_Seconds);

		oSeleniumUtils.DynamicWaitfortheLoadingIconWithCount(20);

		oSeleniumUtils.defaultWait(ProjectVariables.TImeout_3_Seconds);

		oSeleniumUtils.DynamicWaitfortheLoadingIconWithCount(50);

		// Wait untill the given time or the given xpath is not displayed
		Assert.assertFalse(
				"Unable to capture the disposition as the disposition popup is still displaying after clicking on OK button in CPW ",
				oSeleniumUtils.is_WebElement_Displayed(StringUtils.replace(Span_contains_text, "value", "Ok")));

	}

	public void validate_the_capture_disposition_dropdown(String Pagename) {
		if (Pagename.equalsIgnoreCase("AWBPage")) {
			Assert.assertTrue("Capture Disposition Dropdown was unable to clicked in AWB page", oSeleniumUtils
					.clickGivenXpath(StringUtils.replace(Span_contains_text, "value", "Capture Disposition")));
		} else if (Pagename.equalsIgnoreCase("Review Worked Opportunity")) {

			Assert.assertTrue("Update Disposition Dropdown was unable to clicked in Reviewed Worked OppPage",
					oSeleniumUtils
							.clickGivenXpath(StringUtils.replace(Span_contains_text, "value", "Update Disposition")));
		}

		Assert.assertTrue(
				"Present option was not displayed in the Capture Disposition dropdown in " + Pagename + " Page",
				oSeleniumUtils.is_WebElement_Visible(
						"(" + StringUtils.replace(ButtonContainsText, "value", "Present") + ")[1]"));

		Assert.assertTrue(
				"Not Reviewed option was not displayed in the Capture Disposition dropdown in " + Pagename + " Page",
				oSeleniumUtils.is_WebElement_Visible(StringUtils.replace(ButtonContainsText, "value", "Not Reviewed")));

		Assert.assertTrue(
				"Do Not Present - CPM Review option was not displayed in the Capture Disposition dropdown in "
						+ Pagename + " Page",
				oSeleniumUtils.is_WebElement_Visible(
						StringUtils.replace(ButtonContainsText, "value", "Do Not Present - CPM Review")));

		Assert.assertTrue(
				"Do Not Present option was not displayed in the Capture Disposition dropdown in " + Pagename + " Page",
				oSeleniumUtils
						.is_WebElement_Visible(StringUtils.replace(ButtonContainsText, "value", "Do Not Present")));

		Assert.assertTrue(
				"Invalid option was not displayed in the Capture Disposition dropdown in " + Pagename + " Page",
				oSeleniumUtils.is_WebElement_Visible(StringUtils.replace(ButtonContainsText, "value", "Invalid")));

	}

	public void verify_the_captured_data_is_not_displayed_in_the_given(ArrayList<String> CaptureddataList,
			String capturedtypeofdata, String Pagename) {
		
		
		if(!oSeleniumUtils.is_WebElement_Displayed(StringUtils.replace(Div_contains_text, "value", "No results found that meet the search criteria.")))
		{
			//To capture All dpkeys displayed in UI
			Capturing_the_DPkeys_from_the_oppgrid_of_AWB_page(Pagename);
						
			for (String Captureddata : CaptureddataList) {
				if(ProjectVariables.UIDPKeylist.contains(Captureddata.trim()))
				{
					Assert.assertTrue("DPkey '"+Captureddata+"' is still displayed eventhough it's captured in "+Pagename, false);
				}
				else
				{
					System.out.println("DPkey '"+Captureddata+"' is not displayed as expected bcz it's captured in "+Pagename);
				}
			}

		}
		else
		{
			System.out.println("All DPs are captured in the '"+Pagename+"' Grid......................");
		}
		
		


	


	}
	
	public void Capturing_the_DPkeys_from_the_oppgrid_of_AWB_page(String pagename) {

		ProjectVariables.UIDPKeylist.clear();
		String xpath=null;
		
		int j=0; 
		if(pagename.equalsIgnoreCase("eLL"))
		{
			xpath="(//table[contains(@class,'grid-table')]//tr//td[3]/span)";
		}
		else
		{
			xpath="("+StringUtils.replace(Div_contains_Class, "value", "dp-number")+"/button)";
		}
		
		
		int dpsize=oSeleniumUtils.get_Matching_WebElement_count(xpath);
		for (int i = 1; i <=dpsize; i++) {
			SeleniumUtils.scrollingToGivenElement(getDriver(), xpath+"["+i+"]");
			oSeleniumUtils.defaultWait(ProjectVariables.TImeout_3_Seconds);
			ProjectVariables.UIDPKeylist.add(oSeleniumUtils.get_TextFrom_Locator(xpath+"["+i+"]").trim());
		}


		System.out.println("UIDPKeylist ==>"+ProjectVariables.UIDPKeylist);
		System.out.println("UIDPKeysize ==>"+ProjectVariables.UIDPKeylist.size());

	}

	public void validate_the_captured_pps_with_mongo_DB(String disposition, ArrayList<String> CaptureddataList,
			String capturedtypeofdata) {

		Assert.assertTrue(
				"subrule count is not matching between the count before capturing and after capturing the disposition, for the client ==>"
						+ Serenity.sessionVariableCalled("client") + ",release ==>"
						+ Serenity.sessionVariableCalled("release") + "," + capturedtypeofdata + "===>"
						+ CaptureddataList + ",Captured Disposition ==>" + disposition + ",Expected ==>"
						+ ProjectVariables.DB_Nodisposition_subRuleList + ",Actual ==>"
						+ ProjectVariables.DB_subRuleList + ",Expected Rules size ==>"
						+ ProjectVariables.DB_Nodisposition_subRuleList.size() + ",Actual Rules size ==>"
						+ ProjectVariables.DB_subRuleList.size(),
				ProjectVariables.DB_Nodisposition_subRuleList.size() == ProjectVariables.DB_subRuleList.size());

		Assert.assertTrue(
				"insurances count is not matching between the count before capturing and after capturing the disposition, for the client ==>"
						+ Serenity.sessionVariableCalled("client") + ",release ==>"
						+ Serenity.sessionVariableCalled("release") + "," + capturedtypeofdata + "===>"
						+ CaptureddataList + ",Captured Disposition ==>" + disposition + ",Expected ==>"
						+ ProjectVariables.DB_Nodisposition_insuranceList + ",Actual ==>"
						+ ProjectVariables.DB_insuranceList,
				ProjectVariables.DB_Nodisposition_insuranceList.size() == ProjectVariables.DB_insuranceList.size());

		Assert.assertTrue(
				"claimtypes count is not matching between the count before capturing and after capturing the disposition, for the client ==>"
						+ Serenity.sessionVariableCalled("client") + ",release ==>"
						+ Serenity.sessionVariableCalled("release") + "," + capturedtypeofdata + "===>"
						+ CaptureddataList + ",Captured Disposition ==>" + disposition + ",Expected ==>"
						+ ProjectVariables.DB_Nodisposition_claimtypeList + ",Actual ==>"
						+ ProjectVariables.DB_claimtypeList,
				ProjectVariables.DB_Nodisposition_claimtypeList.size() == ProjectVariables.DB_claimtypeList.size());

		Assert.assertTrue(
				"Dpkeys count is not matching between the count before capturing and after capturing the disposition, for the client ==>"
						+ Serenity.sessionVariableCalled("client") + ",release ==>"
						+ Serenity.sessionVariableCalled("release") + "," + capturedtypeofdata + "===>"
						+ CaptureddataList + ",Captured Disposition ==>" + disposition + ",Expected ==>"
						+ ProjectVariables.DB_Nodisposition_DpkeyList + ",Actual ==>" + ProjectVariables.DB_DpkeyList,
				ProjectVariables.DB_Nodisposition_DpkeyList.size() == ProjectVariables.DB_DpkeyList.size());

		// Comparing the PPS lists between before and after capturing the data
		compare_the_given_list_of_data_for_capture_disposition_fucntionality(
				ProjectVariables.DB_Nodisposition_subRuleList, ProjectVariables.DB_subRuleList, CaptureddataList,
				capturedtypeofdata, disposition, "subrule");

		compare_the_given_DP_list_of_data_for_capture_disposition_fucntionality(
				ProjectVariables.DB_Nodisposition_DpkeyList, ProjectVariables.DB_DpkeyList, CaptureddataList,
				capturedtypeofdata, disposition, "DPkey");

		compare_the_given_list_of_data_for_capture_disposition_fucntionality(
				ProjectVariables.DB_Nodisposition_insuranceList, ProjectVariables.DB_insuranceList, CaptureddataList,
				capturedtypeofdata, disposition, "Insurance");

		compare_the_given_list_of_data_for_capture_disposition_fucntionality(
				ProjectVariables.DB_Nodisposition_claimtypeList, ProjectVariables.DB_claimtypeList, CaptureddataList,
				capturedtypeofdata, disposition, "Claimtype");

	}

	private void compare_the_given_list_of_data_for_capture_disposition_fucntionality(HashSet<String> ExpectedList,
			HashSet<String> ActualList, ArrayList<String> CaptureddataList, String capturedtypeofdata,
			String disposition, String PPSType) {

		for (String Expecteddata : ExpectedList) {

			if (!ActualList.contains(Expecteddata)) {
				Assert.assertTrue("" + PPSType
						+ " is not matching between before capturing and after capturing the disposition,Expected ==>"
						+ Expecteddata + ",Actual List==>" + ActualList + ", for the client ==>"
						+ Serenity.sessionVariableCalled("client") + ",release ==>"
						+ Serenity.sessionVariableCalled("release") + "," + capturedtypeofdata + "===>"
						+ CaptureddataList + ",Captured Disposition ==>" + disposition, false);
			}

		}

	}

	private void compare_the_given_DP_list_of_data_for_capture_disposition_fucntionality(HashSet<Long> ExpectedList,
			HashSet<Long> ActualList, ArrayList<String> CaptureddataList, String capturedtypeofdata, String disposition,
			String PPSType) {

		for (Long Expecteddata : ExpectedList) {

			if (!ActualList.contains(Expecteddata)) {
				Assert.assertTrue(
						"" + PPSType
								+ " is not matching between before capturing and after capturing the disposition,Expected ==>+"
								+ Expecteddata + ",Actual List==>" + ActualList + ", for the client ==>"
								+ Serenity.sessionVariableCalled("client") + ",release ==>"
								+ Serenity.sessionVariableCalled("release") + "," + capturedtypeofdata + "===>"
								+ CaptureddataList + ",Captured Disposition ==>" + disposition,
						ProjectVariables.DB_Nodisposition_subRuleList.size() == ProjectVariables.DB_subRuleList.size());
			}

		}

	}

	public void validate_the_work_todo_count_on_the_given_DPCard(String DPKey) {
		String WorktodoCount = null;
		int Payer_LOBCount = 0;
		String Insurance = null;

		// Mongo DB method retrieve the DP worktodo count
		int DPWorktodoCount = MongoDBUtils.GettheCapturedDispositionPayerLOBsFromtheGiven(
				Serenity.sessionVariableCalled("Medicalpolicy"), "Present", DPKey);

		WorktodoCount = oSeleniumUtils.get_TextFrom_Locator(StringUtils.replace(DPworktodocount, "DP", DPKey));

		// ================= Validating the worktodo count on DPCard,after
		// capturing the DP in CPW ==================================//

		// Validating the worktodocount with DB
		Comparetheworktodocount(WorktodoCount, DPWorktodoCount, DPWorktodoCount);

		System.out.println(
				"Worktodo count is validated successfully with DB after capturing the DPCard from CPW ,the DPCard ==>"
						+ DPKey);

		// ================= Validating the worktodo count on DPCard,after the
		// removing one of the payershort from the captured ones
		// ==================================//
		if (ProjectVariables.CapturedInsuranceList.size() > 1) {
			// oSeleniumUtils.clickGivenXpath("//button[@class='cpd-filter-opener-button']/img");

			// oSeleniumUtils.defaultWait(ProjectVariables.TImeout_3_Seconds);

			/////////////////////////
			System.out.println(ProjectVariables.CapturedInsuranceList.get(0));
			Insurance = StringUtils.substringBefore(ProjectVariables.CapturedInsuranceList.get(0).trim(), "-");

			Payer_LOBCount = Integer
					.valueOf(StringUtils.substringAfter(ProjectVariables.CapturedInsuranceList.get(0).trim(), "-"));

			// uncheck the one of the captured payershort to validate the
			// decreased work todo count
			oSeleniumUtils.clickGivenXpath("//div[@checked='true']/..//span[text()='" + Insurance.trim() + "']");
			Assert.assertTrue("Unable to un-check the '" + Insurance + "'  in Insurance filter section", oSeleniumUtils
					.is_WebElement_Displayed("//div[@checked='false']/..//span[text()='" + Insurance.trim() + "']"));

			/////////////////////////

			oSeleniumUtils.clickGivenXpath("//span[contains(text(),'Apply')]");

			oSeleniumUtils.clickGivenXpath("//button[@class='searchbutton']/../..//button[contains(text(),'Apply')]");

			oSeleniumUtils.defaultWait(ProjectVariables.TImeout_3_Seconds);

			WorktodoCount = oSeleniumUtils.get_TextFrom_Locator(StringUtils.replace(DPworktodocount, "DP", DPKey));

			// Validating the worktodocount with DB
			Comparetheworktodocount(WorktodoCount, DPWorktodoCount - Payer_LOBCount, DPWorktodoCount - Payer_LOBCount);

			System.out.println(
					"Worktodo count is validated successfully with DB after removing the one payershort from the captured ones,the DPCard ==>"
							+ DPKey + ",Worktodo count =>" + WorktodoCount);

		} else {
			System.out.println(
					"Decreased Worktodo count is unable to validate with DB because only one insurance is captured from CPW,the DPCard ==>"
							+ DPKey + ",Worktodo count =>" + WorktodoCount);
		}

		// ================= Validating the worktodo count on DPCard,after
		// assigning all Payershort_LOBS to DPCard
		// ==================================//

		SeleniumUtils.scrollingToGivenElement(getDriver(), "//span[@class='" + DPKey + "']/i");

		// clicking the assignee icon on the DPCard
		oSeleniumUtils.clickGivenXpath("//span[@class='" + DPKey + "']/i");

		oSeleniumUtils.defaultWait(ProjectVariables.TImeout_2_Seconds);

		oSeleniumUtils.clickGivenXpath(Assigneepopup + "[1]");

		oSeleniumUtils.defaultWait(ProjectVariables.TImeout_1_Seconds);

		oSeleniumUtils.clickGivenXpath(StringUtils.replace(Assigneepopup_buttons, "value", "Okay"));

		oSeleniumUtils.defaultWait(ProjectVariables.TImeout_3_Seconds);

		WorktodoCount = oSeleniumUtils.get_TextFrom_Locator(StringUtils.replace(DPworktodocount, "DP", DPKey));

		// Validating the worktodocount with DB
		Comparetheworktodocount(WorktodoCount, 0, DPWorktodoCount - Payer_LOBCount);

		System.out.println(
				"Worktodo count is validated successfully with DB after asssigning all PayerLOB's to presentation,the DPCard ==>"
						+ DPKey + ",Worktodo count =>" + WorktodoCount);

		// ================= Validating the worktodo count on DPCard,after the
		// adding one of the payershort,which removed from the captured ones
		// ==================================//
		if (ProjectVariables.CapturedPayershortList.size() > 1) {
			// oSeleniumUtils.clickGivenXpath("//button[@class='cpd-filter-opener-button']/img");

			// oSeleniumUtils.defaultWait(ProjectVariables.TImeout_3_Seconds);

			// check the one of the captured payershort to validate the
			// increased work todo count
			oSeleniumUtils.clickGivenXpath("//div[@checked='false']/..//span[text()='" + Insurance + "']");

			oSeleniumUtils.clickGivenXpath("//span[contains(text(),'Apply')]");

			oSeleniumUtils.clickGivenXpath("//button[@class='searchbutton']/../..//button[contains(text(),'Apply')]");

			oSeleniumUtils.defaultWait(ProjectVariables.TImeout_3_Seconds);

			WorktodoCount = oSeleniumUtils.get_TextFrom_Locator(StringUtils.replace(DPworktodocount, "DP", DPKey));

			// Validating the worktodocount with DB
			Comparetheworktodocount(WorktodoCount, Payer_LOBCount, DPWorktodoCount);

			System.out.println(
					"Worktodo count is validated successfully with DB after adding the one payershort,which is removed from the captured ones,the DPCard ==>"
							+ DPKey + ",Worktodo count =>" + WorktodoCount);

		} else {
			System.out.println(
					"Worktodo count is unable to validate with DB because only one payershort is captured from CPW,the DPCard ==>"
							+ DPKey + ",Worktodo count =>" + WorktodoCount);
		}
	}

	private void Comparetheworktodocount(String WorktodoCount, int DBNumenatorWorktodoCount,
			int DBDenominatorWorktodoCount) {
		boolean bstatus = false;

		// validating the numanetor of the worktodo count on DPCard
		bstatus = StringUtils.substringBefore(WorktodoCount, "/")
				.equalsIgnoreCase(String.valueOf(DBNumenatorWorktodoCount));

		Assert.assertTrue(
				"Numenator of the worktodo count is not matching with DB count on the DPCard for the first time captured DPcard ==>"
						+ Serenity.sessionVariableCalled("DPkey") + ",Medicalpolicy ==>"
						+ Serenity.sessionVariableCalled("Medicalpolicy") + ",Expected==>" + DBNumenatorWorktodoCount
						+ ",Actual ==>" + StringUtils.substringBefore(WorktodoCount, "/"),
				bstatus);

		// validating the Denaminator of the worktodo count on DPCard
		bstatus = StringUtils.substringAfter(WorktodoCount, "/")
				.equalsIgnoreCase(String.valueOf(DBDenominatorWorktodoCount));

		Assert.assertTrue(
				"Denominator of the worktodo count is not matching with DB count on the DPCard for the first time captured DPcard ==>"
						+ Serenity.sessionVariableCalled("DPkey") + ",Medicalpolicy ==>"
						+ Serenity.sessionVariableCalled("Medicalpolicy") + ",Expected==>" + DBDenominatorWorktodoCount
						+ ",Actual ==>" + StringUtils.substringAfter(WorktodoCount, "/"),
				bstatus);

	}

	public void capture_the_disposition_as(String criteriatype, String disposition, String datatype)
			throws InterruptedException {
		ArrayList<String> sGetItems = new ArrayList<>();

		if (criteriatype.contains("Single")) {
			sGetItems = SelectDPKeysInOpportunityGridofAWBPage(datatype, 1);
			System.out.println("" + datatype + " name ===>" + sGetItems);
			System.out.println("Selected the single " + datatype + " successfully");
		} else if (criteriatype.contains("Multiple")) {
			sGetItems = SelectDPKeysInOpportunityGridofAWBPage(datatype, 4);
			System.out.println("" + datatype + " name ===>" + sGetItems);
			System.out.println("Selected the Multiple " + datatype + " successfully");
		} else {
			Assert.assertTrue("Given selection was not found ==>" + criteriatype, false);
		}

		// To Initialize the disposition fields
		Intialaize_the_Dispositions_fields_to_post(disposition);

		if (datatype.equalsIgnoreCase("Medical Policy")) {
			// Capturing the disposition operation for the given client,release
			// and disposition
			Perform_the_capture_disposition_operation(disposition, ProjectVariables.DispositionReasons,
					ProjectVariables.DispositionNotes, "Review Worked Opportunity");

		} else {
			// Capturing the disposition operation for the given client,release
			// and disposition
			Perform_the_capture_disposition_operation(disposition, ProjectVariables.DispositionReasons,
					ProjectVariables.DispositionNotes, "AWBPage");

		}

		System.out.println("Successsfully captured the Disposition as '" + disposition
				+ "' for the selected medicalpolicy ==>" + sGetItems);

		if (datatype.equalsIgnoreCase("Medical Policy")) {
			// validate the captured PPS disposition in Review worked
			// Opportunity Page
			validate_the_captured_pps_in_review_worked_opportunity_page(disposition, sGetItems, datatype, "RWO");

		} else {
			// Verify the captured data is displaying in AWB Grid or not
			verify_the_captured_data_is_not_displayed_in_the_given(sGetItems, "DPKey", "AWB Page");

		}

		System.out.println(
				"Verified the captured " + criteriatype + " is not visible in the AWB Grid as expected,Disposition '"
						+ disposition + "'for the selected " + criteriatype + " ==>" + sGetItems);

	}

	// ===========================================================================================================================================================//

	public void Open_the_Review_Worked_Opportunity_Page() {
		
				
		
		if(!oSeleniumUtils.is_WebElement_Displayed(WorkedOpportunityHeader))
		{
			Assert.assertTrue("Reviewed Worked Opportunity button was unable to clicked in AWB page", oSeleniumUtils.clickGivenXpath(StringUtils.replace(Span_contains_text, "value", "Review Worked Opportunities")));
			
			oSeleniumUtils.defaultWait(ProjectVariables.TImeout_3_Seconds);
			
			DynamicWaitfortheLoadingIconWithCount(10);
			
			oSeleniumUtils.defaultWait(ProjectVariables.TImeout_3_Seconds);
			
			DynamicWaitfortheLoadingIconWithCount(10);
			
			Assert.assertTrue("Worked Opportunities header is not displaying in the Review WorkedOpp Page,after clicking on that link in AWB Page", oSeleniumUtils.Wait_Untill_Element_is_displayed(10, WorkedOpportunityHeader));
	
			
		}
		
			
		}

	// ============================================================================================================================================//

	public void select_the_filters_in_RWO_Page(String filtername, String filterdata, String applyfiltersbutton) {

		if (filtername.isEmpty()) {
			List<String> FiltersList = Arrays.asList(ProjectVariables.RWO_filters.split(","));

			for (int i = 0; i < FiltersList.size(); i++) {

				Assert.assertTrue(
						"'" + FiltersList.get(i) + "' filterhead checkbox is unable to checked in the RWO Page",
						ApplyFilters(FiltersList.get(i), FiltersList.get(i), "CHECK", ""));

			}

			Assert.assertTrue("unable to click the 'Apply Filters' button in the RWO Page", oSeleniumUtils
					.clickGivenXpath(StringUtils.replace(ApplyResetfiltersbutton, "value", "apply-filters")));

		} else {
			Assert.assertTrue("'" + filtername + "' filterhead checkbox is unable to un-check in the RWO Page",
					ApplyFilters(filtername, filtername, "UNCHECK", ""));

			if (applyfiltersbutton.isEmpty()) {
				Assert.assertTrue("'" + filterdata + "' filterdata checkbox is unable to check in the filter '"
						+ filtername + "' of RWO Page", ApplyFilters(filtername, filterdata, "CHECK", ""));
			} else {
				Assert.assertTrue("'" + filterdata + "' filterdata checkbox is unable to check in the filter '"
						+ filtername + "' of RWO Page",
						ApplyFilters(filtername, filterdata, "CHECK", applyfiltersbutton));
			}

		}

		// combination of Static and dynamic wait
		oSeleniumUtils.defaultWait(ProjectVariables.TImeout_2_Seconds);
		DynamicWaitfortheLoadingIconWithCount(10);
		oSeleniumUtils.defaultWait(ProjectVariables.TImeout_2_Seconds);

		System.out.println("Selected the filters sucessfully in RWO Page....");

	}

	public void validate_the_captured_pps_in_review_worked_opportunity_page(String disposition,
			ArrayList<String> CaptureddataList, String capturedtypeofdata, String Pagename) {
		
	
				
		if(Pagename.equalsIgnoreCase("RWO"))
		{
			//click on review worked opportunity button and check the page is opening or not
			Open_the_Review_Worked_Opportunity_Page();
		}
		for (String Captureddata : CaptureddataList) 
		{
			if(capturedtypeofdata.contains("DP")&&Captureddata.length()<4)
			{
				System.out.println("Captured DPkey length < '4',so we are not searching that to eliminate duplicate data,DPkey===>"+Captureddata);
			}
			else
			{
				//Validating the captured disposition is displaying or not in the Grid
				verify_the_given_disposition_is_displayed_in_Review_Worked_OppGrid(disposition,Captureddata,capturedtypeofdata);
			}
		}
		
			
		
		
		
	}

	private void verify_the_given_disposition_is_displayed_in_Review_Worked_OppGrid(String disposition,
			String Captureddata, String capturedtypeofdata) {
		
		int iCaptureddataSize=0;
		
		String ExpectedDisposition=null;
		ExpectedDisposition=disposition;
		
		int dispositionsize=0;
		String Dispositionlocator=null;
		
		Assert.assertTrue("Unable to enter the "+capturedtypeofdata+" in the search field of Reviewwoked Opportunity Page,"+capturedtypeofdata+" ===>"+Captureddata, oSeleniumUtils.Enter_given_Text_Element(sSearchField_RWOpp,Captureddata));

		Assert.assertTrue("Unable to click the search icon beside search box in the Reviewwoked Opportunity Page", oSeleniumUtils.clickGivenXpath(AWBgriSearchbutton));

		oSeleniumUtils.defaultWait(ProjectVariables.TImeout_5_Seconds);
		
		click_the_given_cheveron(capturedtypeofdata,"up");
		
		oSeleniumUtils.defaultWait(ProjectVariables.TImeout_5_Seconds);
		
		if(capturedtypeofdata.contains("DP")||capturedtypeofdata.contains("Payershort"))
		{
			iCaptureddataSize=oSeleniumUtils.get_Matching_WebElement_count(StringUtils.replace(ButtonContainsText, "value",Captureddata));
		}
		else
		{
			iCaptureddataSize=oSeleniumUtils.get_Matching_WebElement_count(StringUtils.replace(labelcontainstext, "svalue",Captureddata));
		}
		
		if(iCaptureddataSize>=1)
		{
			if(capturedtypeofdata.contains("DP")||capturedtypeofdata.contains("Payershort"))
			{
				Assert.assertTrue("Captured "+capturedtypeofdata+" is not displaying in the Review Worked Opp Grid,Captured "+capturedtypeofdata+" ===>"+Captureddata+",Disposition ==>"+disposition, oSeleniumUtils.is_WebElement_Displayed(StringUtils.replace(ButtonContainsText, "value",Captureddata)));
			}
			else
			{
				Assert.assertTrue("Captured "+capturedtypeofdata+" is not displaying in the Review Worked Opp Grid,Captured "+capturedtypeofdata+" ===>"+Captureddata+",Disposition ==>"+disposition, oSeleniumUtils.is_WebElement_Displayed(StringUtils.replace(labelcontainstext, "svalue",Captureddata)));	
			}
			
			if(capturedtypeofdata.equalsIgnoreCase("Medical Policy"))
			{
				Assert.assertTrue("unable to click the Topic chevron", oSeleniumUtils.clickGivenXpath("//div[@class='topic-box']//fa-icon"));
			}
			
			//((//label[contains(text(),'Clinician-Administered Drugs - Infliximab (J1745, Q5103, Q5104')]/ancestor::tr/following-sibling::tr)/td[11]/button)
			
			if(capturedtypeofdata.contains("DP")||capturedtypeofdata.contains("Payershort"))
			{
				dispositionsize=oSeleniumUtils.get_Matching_WebElement_count("(//button[contains(text(),'"+Captureddata+"')]/ancestor::tr/following-sibling::tr/td[11]/button)");
				Dispositionlocator="(//button[contains(text(),'"+Captureddata+"')]/ancestor::tr/following-sibling::tr/td[11]/button)";
			}
			else
			{
				dispositionsize=oSeleniumUtils.get_Matching_WebElement_count("((//label[contains(text(),'"+Captureddata+"')]/ancestor::tr/following-sibling::tr)/td[11]/button)");
				Dispositionlocator="((//label[contains(text(),'"+Captureddata+"')]/ancestor::tr/following-sibling::tr)/td[11]/button)";
			}
				
			
			
			if(!capturedtypeofdata.equalsIgnoreCase("Payershort"))
			{
				for (int i = 1; i <= dispositionsize-1; i++) 
				{
					SeleniumUtils.scrollingToGivenElement(getDriver(), Dispositionlocator+"["+i+"]");
					oSeleniumUtils.defaultWait(ProjectVariables.TImeout_2_Seconds);
					String currentDisposition=oSeleniumUtils.get_TextFrom_Locator(Dispositionlocator+"["+i+"]");
					
					if(currentDisposition.equalsIgnoreCase("Multiple"))
					{
						disposition="Multiple";
					}
					else
					{
						disposition=ExpectedDisposition;
					}
					//!Disp_in_DPWB.equalsIgnoreCase(disposition)&&!Disp_in_DPWB.equalsIgnoreCase("Multiple")
					if(!currentDisposition.equalsIgnoreCase(disposition))
					{
						Assert.assertTrue("Current Disposition column data are not matching with captured disposition in Review Worked OppPage,Expected ==>"+disposition+",Actual ===>"+currentDisposition+",for the client ==>"+Serenity.sessionVariableCalled("client")+",release ==>"+Serenity.sessionVariableCalled("release")+","+capturedtypeofdata+"===>"+Captureddata, false);
					}
					
				}
			}
			else
			{
				
				for (int i = 0; i <ProjectVariables.sGetPayershorts.size(); i++) 
				{
					String currentDisposition=oSeleniumUtils.get_TextFrom_Locator("(//button[contains(text(),'"+ProjectVariables.sGetPayershorts.get(i)+"')]/ancestor::td/following-sibling::td)[7]//button");
					
					//!Disp_in_DPWB.equalsIgnoreCase(disposition)&&!Disp_in_DPWB.equalsIgnoreCase("Multiple")
					if(!currentDisposition.equalsIgnoreCase(disposition))
					{
						Assert.assertTrue("Current Disposition column data are not matching with captured disposition in Review Worked OppPage,Expected ==>"+disposition+",Actual ===>"+currentDisposition+",for the client ==>"+Serenity.sessionVariableCalled("client")+",release ==>"+Serenity.sessionVariableCalled("release")+",DPKey===>"+Captureddata+",Payershorts ==>"+ProjectVariables.sGetPayershorts.get(i), false);
					}
					
				}
				
			}
		}
		else if(iCaptureddataSize==0)
		{
			Assert.assertTrue("Captured "+capturedtypeofdata+" count is showing as 'zero' in the Review Worked Opp Grid,Captured "+capturedtypeofdata+" ===>"+Captureddata, false);
		}
		else
		{
			System.out.println("Captured "+capturedtypeofdata+" is  showing as 'Multiple' count in the Review Worked Opp Grid,Captured "+capturedtypeofdata+" ===>"+Captureddata+",Disposition ==>"+disposition);
		}
		
		
		
		
	}

	// **********************************************Clicking
	// Chevron****************************************************************

	public void click_the_given_cheveron(String chevronname, String condition) {
		String sArrow=null;
		String sChevron=null;
		boolean sArrowstatus=false;
		
		switch(chevronname.toUpperCase())
		{
		case "TOPIC":
			sChevron="//div[@class='topic-box']//fa-icon";
			Assert.assertTrue("unable to click the "+chevronname+" chevron", oSeleniumUtils.clickGivenXpath(sChevron));
			
			sArrow="//div[@class='topic-box']//fa-icon/*[@data-icon='angle-double-"+condition+"']";
		break;
		case "PAYERSHORT":
		case "DPKEYS":
			sChevron="//div[@class='dp-box']//fa-icon";
			Assert.assertTrue("unable to click the "+chevronname+" chevron", oSeleniumUtils.clickGivenXpath(sChevron));
			sArrow="//div[@class='dp-box']//fa-icon/*[@data-icon='angle-double-"+condition+"']";
		break;
		case "MEDICAL POLICY":
			sChevron="//div[@class='mp-box']//fa-icon";
			Assert.assertTrue("unable to click the "+chevronname+" chevron", oSeleniumUtils.clickGivenXpath(sChevron));
			sArrow="//div[@class='mp-box']//fa-icon/*[@data-icon='angle-double-"+condition+"']";
		break;
		default:
			Assert.assertTrue("given selection was not found ==>"+chevronname, false);
		break;
		}
		
		sArrowstatus=oSeleniumUtils.is_WebElement_Displayed(sArrow);
		if(!sArrowstatus){
			Assert.assertTrue("unable to click the "+chevronname+" chevron", oSeleniumUtils.clickGivenXpath(sChevron));
		}
		
		
	}

	private void verify_the_given_disposition_is_displayed_in_DPWB_Page(String disposition, String Captureddata,
			String capturedtypeofdata) {

		Assert.assertTrue(
				"Unable to click the " + capturedtypeofdata + " in the Reviewwoked Opportunity Page, "
						+ capturedtypeofdata + " ==>" + Captureddata,
				oSeleniumUtils.clickGivenXpath(StringUtils.replace(ButtonContainsText, "value", Captureddata)));

		oSeleniumUtils.defaultWait(ProjectVariables.TImeout_3_Seconds);

		oSeleniumUtils.DynamicWaitfortheLoadingIconWithCount(10);

		oSeleniumUtils.defaultWait(ProjectVariables.TImeout_3_Seconds);

		oSeleniumUtils.DynamicWaitfortheLoadingIconWithCount(10);

		Assert.assertTrue(
				"" + capturedtypeofdata
						+ " was not displayed in the DP WorkbenchPage,after clicking on that in Review Worked Opp Page "
						+ capturedtypeofdata + " ==>" + Captureddata,
				oSeleniumUtils.Wait_Untill_Element_is_displayed(10,
						StringUtils.replace(Text_contains_header3, "value", Captureddata)));

		String Disp_in_DPWB = StringUtils
				.substringBefore(oSeleniumUtils.get_TextFrom_Locator(Disposition_in_DPWB), "Disposition").trim();

		if (!Disp_in_DPWB.equalsIgnoreCase(disposition)) {
			if (!Disp_in_DPWB.equalsIgnoreCase("Multiple")) {
				Assert.assertTrue(
						"Disposition displayed in the DPWB is not matching with captured disposition,Expected ==>"
								+ disposition + ",Actual ===>" + Disp_in_DPWB + ",for the client ==>"
								+ Serenity.sessionVariableCalled("client") + ",release ==>"
								+ Serenity.sessionVariableCalled("release") + "," + capturedtypeofdata + "===>"
								+ Captureddata + ",MedicalPolicy ==>" + Serenity.sessionVariableCalled("Medicalpolicy"),
						false);
			}

		}

		Assert.assertTrue(
				"Unable to click the Worked Opportunity link in the DPWBPage," + capturedtypeofdata + " ==>"
						+ Captureddata,
				oSeleniumUtils
						.clickGivenXpath(StringUtils.replace(anchorTag_with_text, "value", "Worked Opportunities")));

		oSeleniumUtils.defaultWait(ProjectVariables.TImeout_3_Seconds);

		oSeleniumUtils.DynamicWaitfortheLoadingIconWithCount(10);

		oSeleniumUtils.defaultWait(ProjectVariables.TImeout_3_Seconds);

		oSeleniumUtils.DynamicWaitfortheLoadingIconWithCount(10);

		Assert.assertTrue(
				"Worked Opportunities header is not displaying in the Review WorkedOpp Page,after clicking on that link in DPWB Page "
						+ capturedtypeofdata + " ==>" + Captureddata,
				oSeleniumUtils.Wait_Untill_Element_is_displayed(10, WorkedOpportunityHeader));

	}

	// ===============================================================================================================================================================================
	// //

	public void Capture_the_disposition_through_service(String Requestbody) {

		System.out.println("Requestbody==>" + Requestbody);

		// Service method to post the data
		Response response = oRestServiceUtils
				.getPostResponsewithSessionID(ProjectVariables.CaptureDispositionServiceUrl, Requestbody);

		System.out.println("ResponseCode ==>" + response.getStatusCode());

		if (response.getStatusCode() == 200 || response.getStatusCode() == 201) {
			System.out.println("Disposition was captured successfully through the service,status code ==>"
					+ response.getStatusCode() + ",ServiceUrl==>" + ProjectVariables.CaptureDispositionServiceUrl
					+ ",RequestBody==>" + Requestbody);
		} else {
			Assert.assertTrue("Disposition was unable to captured through the service,status code ==>"
					+ response.getStatusCode() + ",ServiceUrl==>" + ProjectVariables.CaptureDispositionServiceUrl
					+ ",RequestBody==>" + Requestbody, false);
		}

		if (!Requestbody.contains("No Disposition")) {
			// validate the captured data with MongoDB
			MongoDBUtils.Check_the_captured_record_exists_or_not();

		}

	}

	// ===============================================================================================================================================================================
	// //

	public boolean DynamicWaitfortheCPQLoadingIconWithCount(int count) {
		boolean bstatus = false;
		int j = 1;
		System.out.println("Loading is in progress");

		do {
			oSeleniumUtils.defaultWait(ProjectVariables.TImeout_2_Seconds);
			j = j + 1;

			if (j == count) {
				break;
			}

			bstatus = oSeleniumUtils.is_WebElement_Visible(LoadingIconCPQ);

		} while (bstatus);

		return bstatus;

	}

	// ===============================================================================================================================================================================
	// //

	public String Get_Password_For_the_given_url(String user) throws Exception {

		HashMap<String, String> oHashMap = new HashMap<String, String>();
		oHashMap.put("nkumar", GenericUtils.decode(ProjectVariables.nkumarPassword));
		oHashMap.put("ulanka", GenericUtils.decode(ProjectVariables.ulankaPassword));
		oHashMap.put("iht_ittest09", GenericUtils.decode(ProjectVariables.PASSWORD));
		oHashMap.put("iht_ittest03", GenericUtils.decode(ProjectVariables.PASSWORD));
		oHashMap.put("iht_ittest04", GenericUtils.decode(ProjectVariables.PASSWORD));
		oHashMap.put("iht_ittest05", GenericUtils.decode(ProjectVariables.PASSWORD));
		return oHashMap.get(user);
	}

	// ===============================================================================================================================================================================
	// //

	public static boolean RetrievetheClientdatafromtheResponse(String username, String serviceurl) throws IOException {

		boolean bstatus = false;

		String ServiceRequestBody = "{\r\n" + "	\"userName\": \"" + username + "\"\r\n" + "}\r\n";

		// method to post the given data in the given service
		MicroServRestUtils.Post_the_Data_with_Rest_Assured_And_Fetch_Clients(ServiceRequestBody, serviceurl);

		// System.out.println("Total Assigned CLients ==>"+j);
		System.out.println("Total Clientskeys ==>" + ProjectVariables.clientKeysList);
		System.out.println("Total Clients ==>" + ProjectVariables.clientNamesList);
		return bstatus;
	}

	// ===============================================================================================================================================================================
	// //

	public void Assign_the_given_DP_to_given_presentation(String DPkey, String PresentationName) {
		SeleniumUtils oSeleniumUtils = this.switchToPage(SeleniumUtils.class);
		boolean bstatus = false;

		oSeleniumUtils.clickGivenXpath(StringUtils.replace(labelwithtext, "svalue", "DP " + DPkey + ""));
		oSeleniumUtils.defaultWait(ProjectVariables.TImeout_3_Seconds);
		bstatus = oSeleniumUtils
				.is_WebElement_Displayed(StringUtils.replace(Span_with_text, "value", "DP " + DPkey + ""));
		if (bstatus) {
			System.out.println("DPView is displayed,after clicking on the DPKey=>" + DPkey
					+ " in the 'Available Opportunity' Deck");
		} else {
			Assert.assertTrue(
					"DPView is not displayed,after clicking on the DPKey=>" + DPkey
							+ " in the 'Available Opportunity' Deck,Topic=>" + Serenity.sessionVariableCalled("Topic"),
					false);
		}
		oSeleniumUtils.clickGivenXpath(StringUtils.replace(labelcontainstext, "svalue", "ALL"));
		oSeleniumUtils.clickGivenXpath(StringUtils.replace(AssignIconOnDP, "value", DPkey));
		oSeleniumUtils.defaultWait(ProjectVariables.TImeout_5_Seconds);
		bstatus = oSeleniumUtils.is_WebElement_Displayed(StringUtils.replace(AssignPopupbuttons, "value", "Okay"));

		/*
		 * if(bstatus) { System.out.println(
		 * "AssignPopup is displayed,after clicking on the Assignee icon of DPKey=>"
		 * +DPkey+" in the 'Available Opportunity' Deck"); } else {
		 * Assert.assertTrue(
		 * "AssignPopup is not displayed,after clicking on the Assignee icon of DPKey=>"
		 * +DPkey+" in the 'Available Opportunity' Deck,Topic=>"
		 * +Serenity.sessionVariableCalled("Topic"), false); }
		 */
		oSeleniumUtils.clickGivenXpath(StringUtils.replace(Presentation_in_assigneePopup, "value", PresentationName));
		String Attributevalue = oSeleniumUtils.Get_Value_By_given_attribute("aria-disabled",
				StringUtils.replace(AssignPopupbuttons, "value", "Okay"));
		if (Attributevalue.equalsIgnoreCase("false")) {
			System.out.println("Okay button is enabled in assignpopup,after selecting the presentation as expected");
		} else {
			Assert.assertTrue(
					"Okay button is still disabled in assignpopup,after selecting the presentation also for the DPKey=>"
							+ DPkey + " in the 'Available Opportunity' Deck,Topic=>"
							+ Serenity.sessionVariableCalled("Topic"),
					false);
		}
		oSeleniumUtils.clickGivenXpath(StringUtils.replace(AssignPopupbuttons, "value", "Okay"));

		oSeleniumUtils.defaultWait(ProjectVariables.TImeout_3_Seconds);
		oSeleniumUtils.waitForContentLoad();

		bstatus = oSeleniumUtils.is_WebElement_Displayed(StringUtils.replace(AssignPopupbuttons, "value", "Okay"));

		if (bstatus) {
			Assert.assertTrue(
					"AssignPopup is still displayed,after clicking on the okay button also of DPKey=>" + DPkey
							+ " in the 'Available Opportunity' Deck,Topic=>" + Serenity.sessionVariableCalled("Topic"),
					false);
		} else {
			System.out.println(
					"AssignPopup is not displayed as expected,after clicking on the okay button also of DPKey=>" + DPkey
							+ " in the 'Available Opportunity' Deck,Topic=>" + Serenity.sessionVariableCalled("Topic"));
		}
		// oSeleniumUtils.clickGivenXpath(StringUtils.replace(Tag_with_I,
		// "value", "close"));
		bstatus = oSeleniumUtils.is_WebElement_Displayed(StringUtils
				.replace(StringUtils.replace(Presentations_On_DP, "value", DPkey), "name", PresentationName));

		if (bstatus) {
			System.out.println("Assigned PresentationName '" + PresentationName + "' is displayed on DPKey=>" + DPkey
					+ " in the 'Available Opportunity' Deck,Topic=>" + Serenity.sessionVariableCalled("Topic"));
		} else {
			Assert.assertTrue(
					"Assigned PresentationName '" + PresentationName + "' is not displayed on DPKey=>" + DPkey
							+ " in the 'Available Opportunity' Deck,Topic=>" + Serenity.sessionVariableCalled("Topic"),
					false);
		}

	}

	// ===============================================================================================================================================================================
	// //

	public void Validate_the_rawsavings_for_the_given_DP_at_PresentaionDeck(String DPkey, String Presentationname,
			String Rawsavings) {
		SeleniumUtils oSeleniumUtils = this.switchToPage(SeleniumUtils.class);
		boolean bstatus = false;
		if (oSeleniumUtils.is_WebElement_Displayed(StringUtils.replace(labelwithtext, "svalue", "DP " + DPkey + ""))) {
			bstatus = select_the_given_DPkey_at_Presentation_view(Presentationname, DPkey,"");
		} else {
			bstatus = true;
		}

		if (bstatus) {
			System.out.println("Assigned DPkey is displayed in the Presentaion View of Pres=>" + Presentationname);
		} else {
			Assert.assertTrue("Assigned DPkey '" + DPkey + "' is not dislpyed in the Presentaion View of Pres=>"
					+ Presentationname + ",Topic=>" + Serenity.sessionVariableCalled("Topic"), false);
		}

		String UI_Rawsavings = ReturnTheExactSavings(
				oSeleniumUtils.get_TextFrom_Locator(StringUtils.replace(Pres_DP_Savings, "value", DPkey)).trim());
		GenericUtils.CompareTwoValues("RawSavings of DP at Presentation=>" + Presentationname + ",DPKey=>" + DPkey,
				UI_Rawsavings, Rawsavings);

	}

	// ===============================================================================================================================================================================
	// //

	public boolean select_the_given_DPkey_at_Presentation_view(String Presentationname, String DPkey) {

		SeleniumUtils oSeleniumUtils=this.switchToPage(SeleniumUtils.class);
		
		boolean bstatus=false;
		
		oSeleniumUtils.clickGivenXpath(StringUtils.replace(Span_contains_text, "value", Presentationname));
		oSeleniumUtils.waitForContentLoad();
      	oSeleniumUtils.waitForContentLoad();
		
		
		if(oSeleniumUtils.is_WebElement_Displayed(StringUtils.replace(Tag_with_P, "value", "This presentation has no content yet.")))
		{
			return false;
		}
		
		//To select the all PPs in Presentation Deck in filterdrawer section
		selectTheAllPPSinPresentationDeck();
		
		/*if(!oSeleniumUtils.is_WebElement_Displayed(StringUtils.replace(labelcontainstext, "svalue", Serenity.sessionVariableCalled("Medicalpolicy"))))
		{
			oSeleniumUtils.clickGivenXpath(StringUtils.replace(Tag_contains_I, "value", "expand_less"));
			oSeleniumUtils.defaultWait(ProjectVariables.TImeout_2_Seconds);
		}
		
		oSeleniumUtils.clickGivenXpath(StringUtils.replace(labelcontainstext, "svalue", Serenity.sessionVariableCalled("Medicalpolicy")));
		oSeleniumUtils.defaultWait(ProjectVariables.TImeout_5_Seconds);
		bstatus=oSeleniumUtils.is_WebElement_Displayed(StringUtils.replace(labelcontainstext, "svalue", Serenity.sessionVariableCalled("Topic")));
		if(bstatus){
			oSeleniumUtils.clickGivenXpath(StringUtils.replace(labelcontainstext, "svalue", Serenity.sessionVariableCalled("Topic")));	
			oSeleniumUtils.defaultWait(ProjectVariables.TImeout_3_Seconds);
		}
		else
		{
			return bstatus;
		}
		*/
		String xpath = "//span[text()='svalue']";
		bstatus=oSeleniumUtils.clickGivenXpath(StringUtils.replace(xpath, "svalue", "DP "+DPkey+""));
		//bstatus=oSeleniumUtils.is_WebElement_Displayed(StringUtils.replace(labelwithtext, "svalue", "DP "+DPkey+"")+"[@class='dpIdLabel']");
		
		return bstatus;
		
	}

	public boolean select_the_given_DPkey_at_Presentation_view(String Presentationname, String DPkey, String sChange) {

		SeleniumUtils oSeleniumUtils = this.switchToPage(SeleniumUtils.class);

		boolean bstatus = false;

		oSeleniumUtils.clickGivenXpath(StringUtils.replace(Span_contains_text, "value", Presentationname));
		oSeleniumUtils.waitForContentLoad();
		oSeleniumUtils.waitForContentLoad();

		if (oSeleniumUtils.is_WebElement_Displayed(
				StringUtils.replace(Tag_with_P, "value", "This presentation has no content yet."))) {
			return false;
		}

		// To select the all PPs in Presentation Deck in filterdrawer section
		selectTheAllPPSinPresentationDeck();

		if(sChange.toUpperCase().equals("DP DESCRIPTION"))
		{
			bstatus = oSeleniumUtils.is_WebElement_Displayed(
					StringUtils.replace(Tag_contains_P, "svalue", Serenity.sessionVariableCalled("RFP_Presentation").toString()));
		}else
		bstatus = oSeleniumUtils.is_WebElement_Displayed(
				StringUtils.replace(labelwithtext, "svalue", "DP " + DPkey + "") + "[@class='dpIdLabel']");

		return bstatus;

	}
	// ===============================================================================================================================================================================
	// //

	public void selectTheAllPPSinPresentationDeck() {
		SeleniumUtils oSeleniumUtils = this.switchToPage(SeleniumUtils.class);
		String CheckboxStatus = null;
		CheckboxStatus = oSeleniumUtils.Get_Value_By_given_attribute("aria-checked",
				StringUtils.replace(Pres_Payer_LOB_All_Chkbox, "filter", "Payers"));
		if (CheckboxStatus == null) {
			Assert.assertTrue("Unable to retrive the checkbox status of Payers in Presentaion view", false);
		}
		if (CheckboxStatus.equalsIgnoreCase("false")) {
			oSeleniumUtils.clickGivenXpath(StringUtils.replace(Pres_Payer_LOB_All_Chkbox, "filter", "Payers"));
		}

		CheckboxStatus = oSeleniumUtils.Get_Value_By_given_attribute("aria-checked",
				StringUtils.replace(Pres_Payer_LOB_All_Chkbox, "filter", "LOB"));
		if (CheckboxStatus == null) {
			Assert.assertTrue("Unable to retrive the checkbox status of LOB in Presentaion view", false);
		}
		if (CheckboxStatus.equalsIgnoreCase("false")) {
			oSeleniumUtils.clickGivenXpath(StringUtils.replace(Pres_Payer_LOB_All_Chkbox, "filter", "LOB"));
		}
		CheckboxStatus = oSeleniumUtils.Get_Value_By_given_attribute("aria-checked",
				StringUtils.replace(Pres_Payer_LOB_All_Chkbox, "filter", "Claim Type"));
		if (CheckboxStatus == null) {
			Assert.assertTrue("Unable to retrive the checkbox status of Claim Type in Presentaion view", false);
		}
		if (CheckboxStatus.equalsIgnoreCase("false")) {
			oSeleniumUtils.clickGivenXpath(StringUtils.replace(Pres_Payer_LOB_All_Chkbox, "filter", "Claim Type"));
		}
		oSeleniumUtils.defaultWait(ProjectVariables.TImeout_3_Seconds);
		oSeleniumUtils.clickGivenXpath(StringUtils.replace(Span_contains_text, "value", "Apply"));
		// oSeleniumUtils.defaultWait(ProjectVariables.TImeout_3_Seconds);
		oSeleniumUtils.waitForContentLoad();

	}

	// ===============================================================================================================================================================================
	// //

	public void Select_the_Available_OpportunityDeck_from_Presentationview() {
		SeleniumUtils oSeleniumUtils = this.switchToPage(SeleniumUtils.class);

		oSeleniumUtils.clickGivenXpath(StringUtils.replace(Span_contains_text, "value", "NPP Opportunities") + "/span");
		// oSeleniumUtils.defaultWait(ProjectVariables.TImeout_2_Seconds);
		oSeleniumUtils.waitForContentLoad();
		oSeleniumUtils.clickGivenXpath(StringUtils.replace(ButtonWithText, "svalue", "Get Available Opportunities"));
		// oSeleniumUtils.defaultWait(ProjectVariables.TImeout_2_Seconds);
		oSeleniumUtils.waitForContentLoad();
		oSeleniumUtils.clickGivenXpath(StringUtils.replace(Tag_contains_b, "value", "Payer Shorts"));
		oSeleniumUtils.clickGivenXpath(StringUtils.replace(Tag_contains_b, "value", "LOB"));
		oSeleniumUtils.clickGivenXpath(StringUtils.replace(Tag_contains_b, "value", "ClaimType"));
		oSeleniumUtils.clickGivenXpath(StringUtils.replace(ButtonWithText, "svalue", "Apply"));
		oSeleniumUtils.defaultWait(ProjectVariables.TImeout_3_Seconds);
		// oFilterDrawer.selectMedicalPolicyAndTopic("Topic",
		// Serenity.sessionVariableCalled("Topic"), "Select");
	}

	// ===============================================================================================================================================================================
	// //

	public void Capture_the_data_for_the_given_disposition(String Disposition, String InsuranceKeys, String User,
			String DPkeyCriteria) {
		List<String> DPKeysList = null;
		String Requestbody = null;
		String capturedcommand = null;
		Serenity.setSessionVariable("Disposition").to(Disposition);

		if (DPkeyCriteria.contains("Same RVA") || DPkeyCriteria.equalsIgnoreCase("Present")
				|| DPkeyCriteria.equalsIgnoreCase("Single LOB-Present")) {
			capturedcommand = "update";
		} else {
			capturedcommand = "insert";
		}
		// To capture the disposition for retrieved Mongo Data through the
		// service
		DPKeysList = Arrays.asList(Serenity.sessionVariableCalled("DPkey").toString().split(","));

		for (int i = 0; i < DPKeysList.size(); i++) {
			ProjectVariables.CapturedDPkey = Long.valueOf(DPKeysList.get(i).trim());

			
			  if (DPkeyCriteria.contains("Same RVA") || DPkeyCriteria.contains("Do Not Present - CPM Review")
					|| DPkeyCriteria.contains("Do Not Present") || DPkeyCriteria.contains("Not Reviewed")
					|| DPkeyCriteria.contains("Invalid")) {
				// Requestbody				
				Requestbody = "{\r\n" + "	\"client_key\":" + Serenity.sessionVariableCalled("clientkey") + ",\r\n"
						+ "	\"clientDesc\": \"" + Serenity.sessionVariableCalled("client") + "\",\r\n"
						+ "	\"decision_points\":[\r\n" + "		{\"decision_point_id\":" + DPKeysList.get(i).trim()
						+ ",\r\n" + "		\"payerPolicySet\":[],\r\n" + "		\"opptySource\":\"rva\",\"payer_ids\":["
						+ Serenity.sessionVariableCalled("Payerkeys") + "],\r\n" + "		\"lob_ids\":["
						+ InsuranceKeys + "],\r\n"
						+ "		\"claim_type_ids\":[\"A\",\"F\",\"P\",\"I\",\"O\",\"S\"]}],\r\n"
						+ "	\"do_not_present_until_next_run\":false,\r\n" + "	\"operation\":\"" + capturedcommand
						+ "\",\r\n" + "	\"userId\":\"" + User + "\",\r\n" + "	\"userName\":\"" + User
						+ "@ihtech.com\",\r\n" + "	\"disposition\":\"" + Disposition + "\",\r\n"
						+ "	\"page_id\":\"Analysis\",\r\n" + "	\"note\":\"\",\r\n" + "	\"reasons\":[],\r\n"
						+ "	\"priority\":\"\"\r\n" + "}\r\n";
			} else {
				Requestbody = "{\r\n" + "	\"client_key\":" + Serenity.sessionVariableCalled("clientkey") + ",\r\n"
						+ "	\"clientDesc\": \"" + Serenity.sessionVariableCalled("client") + "\",\r\n"
						+ "	\"decision_points\":[\r\n" + "		{\"decision_point_id\":" + DPKeysList.get(i).trim()
						+ ",\r\n" + "		\"payerPolicySet\":[],\r\n" + "		\"opptySource\":\"rva\",\"payer_ids\":["
						+ Serenity.sessionVariableCalled("Payerkeys") + "],\r\n" + "		\"lob_ids\":["
						+ InsuranceKeys + "],\r\n"
						+ "		\"claim_type_ids\":[\"A\",\"F\",\"P\",\"I\",\"O\",\"S\"]}],\r\n"
						+ "	\"do_not_present_until_next_run\":false,\r\n" + "	\"operation\":\"" + capturedcommand
						+ "\",\r\n" + "	\"userId\":\"" + User + "\",\r\n" + "	\"userName\":\"" + User
						+ "@ihtech.com\",\r\n" + "	\"disposition\":\"" + Disposition + "\",\r\n"
						+ "	\"page_id\":\"Analysis\",\r\n" + "	\"note\":\"\",\r\n"
						+ "	\"reasons\":[\"High savings\",\"Business Reason\",\"Client Requested\"],\r\n"
						+ "	\"priority\":\"High\"\r\n" + "}\r\n";
			}

			// Method to capture the disposition from CPW through service
			Capture_the_disposition_through_service(Requestbody);
		}

	}

	// ===============================================================================================================================================================================
	// //

	public void Validate_the_Available_DP_section_for_the_given(String DPkeyCriteria) {

		OppurtunityDeck oOppurtunityDeck = this.switchToPage(OppurtunityDeck.class);
		FilterDrawer oFilterDrawer = this.switchToPage(FilterDrawer.class);

		String Rawsavings = null;
		List<String> DPKeysList = null;

		// To capture the disposition for retrieved Mongo Data through the
		// service
		DPKeysList = Arrays.asList(Serenity.sessionVariableCalled("DPkey").toString().split(","));

		switch (DPkeyCriteria) {
		case "Single-Subsequent RVA":
		case "Subsequent RVA-Do Not Present":
		case "Subsequent RVA-Do Not Present - CPM Review":
			// Method to select the Captured Topic in subsequent RVA run from
			// Presentation View
			Select_the_Available_OpportunityDeck_from_Presentationview();
			oFilterDrawer.selectMedicalPolicyAndTopic("Topic", Serenity.sessionVariableCalled("Topic"), "Select");
			break;

		// default:
		// Assert.assertTrue("Case not found =>"+DPkeyCriteria, false);
		// break;

		}

		for (int i = 0; i < DPKeysList.size(); i++) {
			ProjectVariables.CapturedDPkey = Long.valueOf(DPKeysList.get(i).trim());

			if (!DPkeyCriteria.contains("Do Not Present")) {
				// DB Method to retrieve the Raw savings for the captured DP
				MongoDBUtils.GetDBValuesBasedonAggregation(MonGoDBQueries.FilterMongoDBQuery("RAWSAVINGS FOR DP"),
						"RAWSAVINGS FOR DP");

				System.out.println("OLD RawSavings==>" + Serenity.sessionVariableCalled("Old RawSavings"));

				if (Serenity.sessionVariableCalled("Old RawSavings") != null) {
					System.out.println(Long.valueOf(Serenity.sessionVariableCalled("Old RawSavings"))
							+ Long.valueOf(Serenity.sessionVariableCalled("RawSavings")));
					Serenity.setSessionVariable("RawSavings")
							.to(Long.valueOf(Serenity.sessionVariableCalled("Old RawSavings"))
									+ Long.valueOf(Serenity.sessionVariableCalled("RawSavings")));
					System.out.println("RawSavings==>" + Serenity.sessionVariableCalled("RawSavings"));

				}

			}

			// validate the given DPkey is displayed or not in 'Available
			// Opportunity' Deck
			oOppurtunityDeck.validatethegivenDatainOpportunityDeck(DPKeysList.get(i).trim(), "DPkey");
			Rawsavings = ReturnTheExactSavings(oSeleniumUtils.get_TextFrom_Locator(
					StringUtils.replace(StringUtils.replace(Priority_Savings_On_DP, "value", DPKeysList.get(i).trim()),
							"data", "savings").trim()));
			// validate the Rawsavings of captured DPkey in 'Available
			// Opportunity' Deck with DB
			GenericUtils.CompareTwoValues(
					"RawSavings for DP=>" + DPKeysList.get(i).trim() + ",Topic=>"
							+ Serenity.sessionVariableCalled("Topic"),
					Rawsavings, Serenity.sessionVariableCalled("RawSavings").toString());

		}

	}

	// ===============================================================================================================================================================================
	// //

	public void Validate_the_Presentation_profile_section_for_the_given(String DPkeyCriteria)
			throws InterruptedException {
		SeleniumUtils oSeleniumUtils = this.switchToPage(SeleniumUtils.class);
		FilterDrawer oFilterDrawer = this.switchToPage(FilterDrawer.class);
		GenericUtils oGenericUtils = this.switchToPage(GenericUtils.class);
		PresentationProfileValidations oPresentationProfile = this.switchToPage(PresentationProfileValidations.class);
		;
		List<String> DPKeysList = null;

		// To capture the disposition for retrieved Mongo Data through the
		// service
		DPKeysList = Arrays.asList(Serenity.sessionVariableCalled("DPkey").toString().split(","));

		switch (DPkeyCriteria) {
		case "Multiple":
		case "Single":
		case "Single-First RVA":

			for (int i = 0; i < DPKeysList.size(); i++) {
				if (!oSeleniumUtils.is_WebElement_Displayed(
						StringUtils.replace(AvailableDPChkbox, "value", DPKeysList.get(i).trim()))) {
					// Method to select the Captured Topic in subsequent RVA run
					// from Presentation View
					Select_the_Available_OpportunityDeck_from_Presentationview();
					oFilterDrawer.selectMedicalPolicyAndTopic("Topic", Serenity.sessionVariableCalled("Topic"),
							"Select");

				}

				// To Create the Presentation
				oPresentationProfile.PresentationProfileValidations("Create", "", "");

				System.out.println("Profile name==>" + Serenity.sessionVariableCalled("sPPName"));
				ProjectVariables.CreatedPresentationList.add(Serenity.sessionVariableCalled("sPPName"));

				// Assign the given DP to Given Presetation name
				Assign_the_given_DP_to_given_presentation(DPKeysList.get(i).trim(),
						Serenity.sessionVariableCalled("sPPName"));

				ProjectVariables.CapturedDPkey = Long.valueOf(DPKeysList.get(i).trim());

				// DB Method to retrieve the Raw savings for the captured DP
				MongoDBUtils.GetDBValuesBasedonAggregation(MonGoDBQueries.FilterMongoDBQuery("RAWSAVINGS FOR DP"),
						"RAWSAVINGS FOR DP");

				// validate the Rawsavings of captured DPkey in 'Presentation'
				// Deck with DB,after assigning to the presentation
				Validate_the_rawsavings_for_the_given_DP_at_PresentaionDeck(DPKeysList.get(i).trim(),
						Serenity.sessionVariableCalled("sPPName"), Serenity.sessionVariableCalled("RawSavings"));

				Serenity.setSessionVariable("Old RawSavings").to(Serenity.sessionVariableCalled("RawSavings"));

			}

			break;
		case "Subsequent RVA-Do Not Present":
		case "Subsequent RVA-Do Not Present - CPM Review":
		case "Single-Subsequent RVA-Same Raw Savings":
			System.out.println("OLD Raw savings before capturing DP in subsequent RVA Run=>"
					+ Serenity.sessionVariableCalled("Old RawSavings"));
			// validate the Rawsavings of captured DPkey in 'Presentation' Deck
			// with DB,after assigning to the presentation
			Validate_the_rawsavings_for_the_given_DP_at_PresentaionDeck(Serenity.sessionVariableCalled("DPkey"),
					Serenity.sessionVariableCalled("sPPName"), Serenity.sessionVariableCalled("Old RawSavings"));

			break;
		case "Single-Subsequent RVA-Different Raw Savings":

			oSeleniumUtils.clickGivenXpath(
					StringUtils.replace(labelwithtext, "svalue", "DP " + Serenity.sessionVariableCalled("DPkey") + ""));
			oSeleniumUtils.defaultWait(ProjectVariables.TImeout_3_Seconds);
			oSeleniumUtils.clickGivenXpath(StringUtils.replace(labelcontainstext, "svalue", "ALL"));
			oSeleniumUtils.clickGivenXpath(
					StringUtils.replace(AssignIconOnDP, "value", Serenity.sessionVariableCalled("DPkey")));
			oSeleniumUtils.defaultWait(ProjectVariables.TImeout_5_Seconds);
			System.out
					.println(oGenericUtils.isElementExist(StringUtils.replace(AssignPopupbuttons, "value", "Okay"), 5));

			oGenericUtils.clickOnElement(StringUtils.replace(AssignPopupbuttons, "value", "Okay"));

			oSeleniumUtils.defaultWait(ProjectVariables.TImeout_3_Seconds);
			oSeleniumUtils.waitForContentLoad();
			oSeleniumUtils.clickGivenXpath(StringUtils.replace(Tag_with_I, "value", "close"));

			// validate the Rawsavings of captured DPkey in 'Presentation' Deck
			// with DB,after assigning to the presentation
			Validate_the_rawsavings_for_the_given_DP_at_PresentaionDeck(Serenity.sessionVariableCalled("DPkey"),
					Serenity.sessionVariableCalled("sPPName"), Serenity.sessionVariableCalled("RawSavings").toString());

			break;

		default:
			Assert.assertTrue("Case not found =>" + DPkeyCriteria, false);
			break;

		}

	}

	// ===============================================================================================================================================================================
	// //

	public boolean Enter_the_given_MP_Topic_in_filter_Drawer(String MP_Topic) {

		FilterDrawer oFilterDrawer = this.switchToPage(FilterDrawer.class);
		SeleniumUtils oSeleniumUtils = this.switchToPage(SeleniumUtils.class);
		boolean bstatus = false;
		WebElement AllMedChkBox = getDriver()
				.findElement(By.xpath(oFilterDrawer.AllMedicalPolicies + "/div[contains(@class,'checkbox')]"));
		oSeleniumUtils.clickGivenWebElement(AllMedChkBox);
		oSeleniumUtils.defaultWait(ProjectVariables.TImeout_2_Seconds);
		String Attribvalue = oSeleniumUtils.Get_Value_By_given_attribute("aria-checked",
				oFilterDrawer.AllMedicalPolicies);

		if (Attribvalue.equalsIgnoreCase("true") || Attribvalue.equalsIgnoreCase("undefined")) {
			oSeleniumUtils.defaultWait(ProjectVariables.TImeout_5_Seconds);
			oSeleniumUtils.Click_given_Xpath(oFilterDrawer.AllMedicalPolicies + "/div[contains(@class,'checkbox')]");
		}

		// Enter the captured medicalpolicy in the search field
		oSeleniumUtils.Enter_given_Text_Element("//input[@placeholder='Search for Medical Policy / Topic']", MP_Topic);
		oSeleniumUtils.defaultWait(ProjectVariables.TImeout_3_Seconds);
		// click the search button of search field
		oSeleniumUtils.clickGivenXpath("//button[@class='searchbutton']");
		oSeleniumUtils.defaultWait(ProjectVariables.TImeout_3_Seconds);
		bstatus = oSeleniumUtils
				.is_WebElement_Displayed(StringUtils.replace(oFilterDrawer.Medicalpolicy_Checkbox, "value", MP_Topic));
		Serenity.setSessionVariable("MedicalPolicy").to(MP_Topic);

		return bstatus;
	}

	// ===============================================================================================================================================================================
	// //

	public void validate_the_given_LOB_is_not_visible_On_DP(String DPKey, String InvalidInsuranceKey,
			String PMSection) {
		boolean bstatus = false;
		String Insurance = oGenericUtils.Retrieve_the_insurance_from_insuranceKey(InvalidInsuranceKey);
		switch (PMSection) {
		case "Available DPs Section":

			oSeleniumUtils.clickGivenXpath(StringUtils.replace(labelwithtext, "svalue", "DP " + DPKey + ""));
			bstatus = oSeleniumUtils.is_WebElement_Displayed(StringUtils.replace(
					StringUtils.replace(Invalid_unavailable_AvailableDP_insurances, "insurance", Insurance), "value",
					DPKey));
			if (bstatus) {
				System.out.println("'" + Insurance
						+ "' is displayed as 'unavailable' or 'notapplicable' as expected,after removing that for the DP=>"
						+ DPKey);
			} else {
				Assert.assertTrue("'" + Insurance + "' is displayed,after removing also for the DP=>" + DPKey
						+ ",Topic=>" + Serenity.sessionVariableCalled("Topic"), false);
			}
			break;
		case "Presetation Profile Section":
			// validate the captured DPKeys in Available DP's Section
			bstatus = select_the_given_DPkey_at_Presentation_view(Serenity.sessionVariableCalled("sPPName"),
					Serenity.sessionVariableCalled("DPkey"),"");
			if (bstatus) {
				System.out.println("Captured Topic,DPkey is displayed as expetecd,after removing the one of LOB '"
						+ Serenity.sessionVariableCalled("InvalidInsuranceKey") + "' for the disposition is '"
						+ Serenity.sessionVariableCalled("Disposition") + "' in the Presentation View,DPKey=>"
						+ Serenity.sessionVariableCalled("DPkey") + ",Topic=>" + Serenity.sessionVariableCalled("Topic")
						+ ",Presentation=>" + Serenity.sessionVariableCalled("sPPName"));
			} else {
				Assert.assertTrue("Captured Topic,DPkey is not displayed,after removing the one of LOB '"
						+ Serenity.sessionVariableCalled("InvalidInsuranceKey") + "' for the disposition is '"
						+ Serenity.sessionVariableCalled("Disposition") + "' in the Presentation View,DPKey=>"
						+ Serenity.sessionVariableCalled("DPkey") + ",Topic=>" + Serenity.sessionVariableCalled("Topic")
						+ ",Presentation=>" + Serenity.sessionVariableCalled("sPPName"), false);
			}
			bstatus = oSeleniumUtils.is_WebElement_Displayed(StringUtils.replace(
					StringUtils.replace(Invalid_unavailable_Pres_insurances, "insurance", Insurance), "value", DPKey));
			if (bstatus) {
				System.out.println("'" + Insurance
						+ "' is displayed as 'unavailable' or 'notapplicable' as expected in PresentationView,after removing that for the DP=>"
						+ DPKey);
			} else {
				Assert.assertTrue(
						"'" + Insurance + "' is displayed in PresentationView,after removing also for the DP=>" + DPKey
								+ ",Topic=>" + Serenity.sessionVariableCalled("Topic") + ",PresentatnionName=>"
								+ Serenity.sessionVariableCalled("sPPName"),
						false);
			}

			break;

		default:
			Assert.assertTrue("Case not found=>" + PMSection, false);
			break;
		}

	}

	// ===============================================================================================================================================================================
	// //

	public String ReturnTheExactSavings(String sInput) {

		String sval = sInput;
		// TODO Auto-generated method stub
		String sfinalval = null;

		int sintval = sval.indexOf("$");
		if (sintval == -1) {
			sfinalval = sval.replace(",", "");
		} else {
			sfinalval = sval.replace("$", "").replace(",", "");
		}

		// System.out.println(sfinalval);
		return sfinalval;

	}

	// ===============================================================================================================================================================================
	// //

	public void validate_the_DPcards_in_available_opportunity_deck_for_given_selection_of_dropdown(
			String ShowDropdownOption) {
		int DPCardSize = 0;
		String DPkey = null;
		ArrayList<String> DPkeysList = new ArrayList<>();

		DPCardSize = oSeleniumUtils.get_Matching_WebElement_count(DPCards_in_AvailableOpportunity);
		for (int j = 1; j <= DPCardSize; j++) {
			DPkey = StringUtils
					.substringAfter(
							oSeleniumUtils.get_TextFrom_Locator(DPCards_in_AvailableOpportunity + "[" + j + "]"), "DP")
					.trim();
			DPkeysList.add(DPkey);
		}

		// GenericUtils.CompareTwoValues("DPCard Count in Available
		// opportunityDeck for show dropdown option as
		// '"+ShowDropdownOption+"',UI Data=>"+DPkeysList+",DB
		// Data=>"+ProjectVariables.CapturedDPkeyList,
		// String.valueOf(DPkeysList.size()),
		// String.valueOf(ProjectVariables.CapturedDPkeyList.size()));

		for (int i = 0; i < ProjectVariables.CapturedDPkeyList.size(); i++) {
			if (DPkeysList.contains(ProjectVariables.CapturedDPkeyList.get(i).trim())) {
				System.out.println("DPKey '" + ProjectVariables.CapturedDPkeyList.get(i)
						+ "' is displayed in available opportunitydeck as expected with DB for show dropdown option=>"
						+ ShowDropdownOption);
			} else {
				Assert.assertTrue("DPKey '" + ProjectVariables.CapturedDPkeyList.get(i)
						+ "' is not displayed in available opportunitydeck as expected with DB for show dropdown option=>"
						+ ShowDropdownOption, false);
			}
		}

	}

	public void validate_the_updated_DPkey_in_available_opportunity_deck(String DPkey) {
		SeleniumUtils oSeleniumUtils = this.switchToPage(SeleniumUtils.class);
		FilterDrawer oFilterDrawer = this.switchToPage(FilterDrawer.class);
		OppurtunityDeck oOppurtunityDeck = this.switchToPage(OppurtunityDeck.class);
		boolean bstatus = false;

		// Method to select the Captured Topic in subsequent RVA run from
		// Presentation View
		Select_the_Available_OpportunityDeck_from_Presentationview();

		bstatus = Enter_the_given_MP_Topic_in_filter_Drawer(Serenity.sessionVariableCalled("Medicalpolicy"));

		Assert.assertTrue("Captured topic was not displayed,after seraching in filter drawer,Medicalpolicy=>"
				+ Serenity.sessionVariableCalled("Medicalpolicy"), bstatus);

		oSeleniumUtils.clickGivenXpath(StringUtils.replace(oFilterDrawer.Medicalpolicy_Checkbox, "value",
				Serenity.sessionVariableCalled("Medicalpolicy")));

		oFilterDrawer.user_filters_by_clicking_on_Apply_for_Medical_Policy_Topic();

		oOppurtunityDeck.validatethegivenDatainOpportunityDeck(DPkey, "Updated DPkey");
	}

	// ===============================================================================================================================================================================
	// //

	public static String RetrieveTheClientkeyfromgivenClientthroughservice(String client) throws IOException {
		String sClientkey = null;
		EnvironmentVariables environmentVariables = SystemEnvironmentVariables.createEnvironmentVariables();
		String restURI = EnvironmentSpecificConfiguration.from(environmentVariables).getProperty("restapi.baseuri");
		System.out.println(restURI);
		RetrievetheClientdatafromtheResponse("iht_ittest09", restURI + ProjectVariables.CLIENT_TEAM_DATA_ENDPOINT);

		for (int i = 0; i < ProjectVariables.clientNamesList.size(); i++) {
			if (ProjectVariables.clientNamesList.get(i).contains(client)) {
				sClientkey = StringUtils.substringAfter(ProjectVariables.clientNamesList.get(i), "=").trim();
				break;
			}
		}

		if (sClientkey == null) {
			Assert.assertTrue("Unable to retrive the clientkey through service for the given client=>" + client, false);
		}

		System.out.println("Clientkey for the client '" + client + "' ==>" + sClientkey);
		return sClientkey;
	}

	// ===============================================================================================================================================================================
	// //

	/*
	 * public void UpdateTheDispositionForthegivenClient(String Client, String
	 * requiredDisposition, String Disposition, String Criteria) { String
	 * ExactRelease=null; List<String> ReleaseList=null; String
	 * Requestbody=null;
	 * 
	 * MongoDBUtils.UpdateTheDispositionForthegivenClient(Client, Disposition,
	 * Criteria);
	 * 
	 * ExactRelease=Serenity.sessionVariableCalled("release").toString().replace
	 * ("[", ""); ReleaseList=Arrays.asList(ExactRelease.replace("]",
	 * "").split(","));
	 * Serenity.setSessionVariable("release").to(ExactRelease.replace("]", ""));
	 * 
	 * 
	 * for (int i = 0; i < ReleaseList.size(); i++) {
	 * 
	 * //Requestbody Requestbody =
	 * "{\"data_version\":\""+ReleaseList.get(i).trim()+
	 * "\",\"disposition\":\"No Disposition\",\"decision_points\":[{\"decision_point_id\":"
	 * +Serenity.sessionVariableCalled("DPkey")+",\"payer_ids\":["+Serenity.
	 * sessionVariableCalled("Payerkeys")+"],\"lob_ids\":["+ProjectVariables.
	 * StaticInsurnaces+
	 * "],\"claim_type_ids\":[\"A\",\"F\",\"P\",\"I\",\"O\",\"S\"]}],\"reasons\":[],\"do_not_present_until_next_run\":false,\"operation\":\"update\",\"userId\":\"\",\"userName\":\"\",\"client_key\":"
	 * +Serenity.sessionVariableCalled("clientkey")+",\"priority\":\"\"}\r\n";
	 * 
	 * //Method to capture the disposition from CPW through service
	 * Capture_the_disposition_through_service(Requestbody);
	 * 
	 * }
	 * 
	 * 
	 * 
	 * }
	 */

	public void Login_NewCPW(String sUser) {
		try {
			// Launch Application

			Serenity.setSessionVariable("user").to(sUser);

			JavascriptExecutor js = (JavascriptExecutor) getDriver();
			js.executeScript("window.open()");

			System.out.println(
					oSeleniumUtils.is_WebElement_Displayed(StringUtils.replace(CPD_Credentials, "value", "password")));
			System.out.println(Get_Password_For_the_given_url(sUser));

			ArrayList<String> tabs = new ArrayList<String>(getDriver().getWindowHandles());
			getDriver().switchTo().window(tabs.get(1));
			getDriver().get("https://cpw-bfqa.cotiviti.com");

			Thread.sleep(10000);

		} catch (Exception e) {
			GenericUtils.Verify("Object not clicked Successfully , Failed due to :=" + e.getMessage(), "FAILED");
		}

	}

	// ===============================================================================================================================================================================
	// //

	public void Capture_the_disposition_through_service_from_MongoDBData(String sClientkey, String DPkeyCriteria,
			String User) throws IOException {
		String ExactRelease = null;
		String Disposition = null;
		String InsuranceKey = null;
		List<String> InsuranceKeyList = null;
		List<String> ReleaseList = null;

		if (User.isEmpty() || User == null) {
			Assert.assertTrue("Given username is empty or null,username==>" + User, false);
		}

		if (DPkeyCriteria.contains("RequiredCount")) {
			DPkeyCriteria = "RequiredCount";
		}

		switch (DPkeyCriteria) {
		case "Any Single DP":
			// case "Any Multiple Dps":
			// //Method to retrieve the data from DB based on the client
			// MongoDBUtils.GetAvailableDPKeyfromCPW(sClientkey,
			// DPkeyCriteria,0);
			// ProjectVariables.DataVersion=Serenity.sessionVariableCalled("release");
			// break;
		case "Present":
			break;
		case "Single Multiple LOB-Do Not Present - CPM Review":
		case "Single Multiple LOB-Do Not Present":
		case "Single Multiple LOB-Not Reviewed":
		case "Single Multiple LOB-Invalid":
			Disposition = StringUtils.substringAfter(DPkeyCriteria, "Single Multiple LOB-");
			Serenity.setSessionVariable("Disposition").to(Disposition);
			// Method to retrieve the data from DB based on the client
			MongoDBUtils.GetAvailableDPKeyfromCPW(sClientkey, DPkeyCriteria, 1);
			ProjectVariables.DataVersion = Serenity.sessionVariableCalled("release");
			break;
		case "Single-Multiple LOB":
			// Method to retrieve the data from DB based on the client
			MongoDBUtils.GetAvailableDPKeyfromCPW(sClientkey, "Single", 1);
			ProjectVariables.DataVersion = Serenity.sessionVariableCalled("release");
			break;
		case "Single-Multiple LOB Without Release":

			MongoDBUtils.GetDBValuesBasedonAggregation(
					MonGoDBQueries.FilterMongoDBQuery("Approve Library without release"), "CDM_DECISION_DATA");
			ProjectVariables.DataVersion = Serenity.sessionVariableCalled("release");
			break;
		case "Single-Do Not Present - CPM Review":
		case "Single-Do Not Present":
		case "Single-Not Reviewed":
		case "Single-Invalid":
		case "Single":
		case "Multiple":
			Disposition = StringUtils.substringAfter(DPkeyCriteria, "Single-");
			Serenity.setSessionVariable("Disposition").to(Disposition);
			// Method to retrieve the data from DB based on the client
			MongoDBUtils.GetAvailableDPKeyfromCPW(sClientkey, DPkeyCriteria, 1);
			ProjectVariables.DataVersion = Serenity.sessionVariableCalled("release");
			break;
		case "RequiredCount":

			// Method to retrieve the data from DB based on the client
			MongoDBUtils.GetRequiredCountOfDPKeysfromCPW(sClientkey, DPkeyCriteria, 0);
			ProjectVariables.DataVersion = Serenity.sessionVariableCalled("release");
			break;

		case "Single-First RVA":
			// Method to retrieve the client keys from the service for the given
			// user
			RetrievetheClientdatafromtheResponse(User,
					ProjectVariables.ROOT_URI + ProjectVariables.CLIENT_TEAM_DATA_ENDPOINT);

			// Method to update the Disposition from 'Present' to 'No
			// Disposition' for the given client
			// oCPWPage.UpdateTheDispositionForthegivenClient(Client, "No
			// Disposition", "Present", DPkeyCriteria);

			// Method to retrieve the DpKey From the subsequent releases
			MongoDBUtils.DPKeysFromSubsequentReleases();

			ExactRelease = Serenity.sessionVariableCalled("release").toString().replace("[", "");
			ReleaseList = Arrays.asList(ExactRelease.replace("]", "").split(","));
			Serenity.setSessionVariable("release").to(ExactRelease.replace("]", ""));

			ProjectVariables.DataVersion = GenericUtils
					.Retrieve_the_data_based_on_criteria("Small", ReleaseList.get(0), ReleaseList.get(1)).trim();
			break;
		case "Single-Subsequent RVA":

			ReleaseList = Arrays.asList(Serenity.sessionVariableCalled("release").toString().split(","));
			ProjectVariables.DataVersion = GenericUtils
					.Retrieve_the_data_based_on_criteria("Big", ReleaseList.get(0), ReleaseList.get(1)).trim();

			break;
		case "Single LOB-Present":
			Disposition = StringUtils.substringAfter(DPkeyCriteria, "Single LOB-");
			Serenity.setSessionVariable("Disposition").to(Disposition);
			InsuranceKeyList = Arrays.asList(Serenity.sessionVariableCalled("InsuranceKeys").toString().split(","));
			InsuranceKey = InsuranceKeyList.get(0);

			break;
		case "Same RVA Single LOB-Do Not Present - CPM Review":
		case "Same RVA Single LOB-Do Not Present":
		case "Same RVA Single LOB-Not Reviewed":
		case "Same RVA Single LOB-Invalid":
			Disposition = StringUtils.substringAfter(DPkeyCriteria, "Same RVA Single LOB-");
			Serenity.setSessionVariable("Disposition").to(Disposition);
			InsuranceKeyList = Arrays.asList(Serenity.sessionVariableCalled("InsuranceKeys").toString().split(","));
			InsuranceKey = InsuranceKeyList.get(0);
			Serenity.setSessionVariable("InvalidInsuranceKey").to(InsuranceKey);
			break;
		case "Same RVA-Do Not Present - CPM Review":
		case "Same RVA-Do Not Present":
		case "Same RVA-Not Reviewed":
		case "Same RVA-Invalid":
			Disposition = StringUtils.substringAfter(DPkeyCriteria, "Same RVA-");
			Serenity.setSessionVariable("Disposition").to(Disposition);
			break;

		case "Subsequent RVA-Do Not Present - CPM Review":
		case "Subsequent RVA-Do Not Present":
			ReleaseList = Arrays.asList(Serenity.sessionVariableCalled("release").toString().split(","));
			ProjectVariables.DataVersion = GenericUtils
					.Retrieve_the_data_based_on_criteria("Big", ReleaseList.get(0), ReleaseList.get(1)).trim();
			Disposition = StringUtils.substringAfter(DPkeyCriteria, "Subsequent RVA-");
			Serenity.setSessionVariable("Disposition").to(Disposition);
			break;
		case "SingleDP-MultiplePayer-MultipleLOB":
			// Method to retrieve the data from DB based on the client
			MongoDBUtils.GetAvailableDPKeyfromCPW(sClientkey, "Single", 2);

			// ProjectVariables.DataVersion=Serenity.sessionVariableCalled("release");
			break;
		case "Single DP Multiple PPS":
			// Method to retrieve the data from DB based on the client
			MongoDBUtils.GetAvailableDPKeyfromCPW(sClientkey, "Single", 1, 1, 1);
			ProjectVariables.DataVersion = Serenity.sessionVariableCalled("release");
			break;
		default:
			Assert.assertTrue("case not found ==>" + DPkeyCriteria, false);
			break;

		}

		// To Capture the disposition through the service for the retrieved the
		// data from the above
		switch (DPkeyCriteria) {

		case "Single LOB-Present":
		case "Same RVA Single LOB-Do Not Present - CPM Review":
		case "Same RVA Single LOB-Do Not Present":
		case "Same RVA Single LOB-Not Reviewed":
		case "Same RVA Single LOB-Invalid":

			// Captured the data for the given disposition,insurances and user
			Capture_the_data_for_the_given_disposition(Disposition, InsuranceKey, User, DPkeyCriteria);

			break;
		case "Single Multiple LOB-Do Not Present - CPM Review":
		case "Single Multiple LOB-Do Not Present":
		case "Single Multiple LOB-Not Reviewed":
		case "Single Multiple LOB-Invalid":
		case "Single-Do Not Present - CPM Review":
		case "Single-Do Not Present":
		case "Single-Not Reviewed":
		case "Single-Invalid":
		case "Subsequent RVA-Do Not Present":
		case "Subsequent RVA-Do Not Present - CPM Review":
		case "Same RVA-Do Not Present - CPM Review":
		case "Same RVA-Do Not Present":
		case "Same RVA-Not Reviewed":
		case "Same RVA-Invalid":

			// Captured the data for the given disposition,insurances and user
			Capture_the_data_for_the_given_disposition(Disposition, ProjectVariables.StaticInsurnaces, User,
					DPkeyCriteria);

			break;

		default:
			// Captured the data for the given disposition,insurances and user
			Capture_the_data_for_the_given_disposition("Present", ProjectVariables.StaticInsurnaces, User,
					DPkeyCriteria);

			break;
		}

	}

	// ==========================================================================================================================>
	public void clickonclient(String clientName, String release) {
		try {
			// Click 'Client/Payer'
			String sClientPayer = "//span[contains(text(),'" + clientName
					+ "')]/ancestor::td/preceding-sibling::td//span[contains(text(),'" + release + "')]";

			int pixelsize = 500;
			int iCount = 0;
			while (!oSeleniumUtils.is_WebElement_Displayed(sClientPayer)) {
				JavascriptExecutor js = (JavascriptExecutor) getDriver();
				js.executeScript("document.getElementsByClassName('k-grid-content')[0].scrollTop = " + pixelsize + "");
				pixelsize = pixelsize + 500;
				iCount = iCount + 1;
				if (iCount == 10) {
					break;
				}
			}

			SeleniumUtils.scrollingToGivenElement(getDriver(), sClientPayer);

			oSeleniumUtils.clickGivenXpath(sClientPayer);
		} catch (Exception e) {
			GenericUtils.Verify("Object not clicked Successfully , Failed due to :=" + e.getMessage(), "FAILED");
		}
	}

	public void findValueinAWB(String searchValue, String pageName) {
		String SearchValue = "";

		// Retrieve Session Variables
		if (searchValue.equalsIgnoreCase("CapturedDPkey")) {
			SearchValue = String.valueOf(Serenity.sessionVariableCalled("DPKey"));
		}

		else if (searchValue.equalsIgnoreCase("MedicalPolicy")) {
			SearchValue = Serenity.sessionVariableCalled("MedicalPolicy");
		}

		// Enter DP in the Opps Search box
		oSeleniumUtils.Enter_given_Text_Element(SearchFileldXpath, SearchValue);
		oSeleniumUtils.clickGivenXpath(SearchButtonXpath);
		oSeleniumUtils.defaultWait(ProjectVariables.TImeout_2_Seconds);

		// Click on the icon to expand the topic
		oSeleniumUtils.clickGivenXpath(TopicExpandButton);

	}

	public void captureDispositioninAWB(String dispositionTocapture, String dPKeyCount, String string) {

		// Retrieve Session Variables
		Long DPKey = Serenity.sessionVariableCalled("DPKey");
		String MedPolicy = Serenity.sessionVariableCalled("MedicalPolicy");

		String SearchFileldXpath = "//div[@class='searchBox']//input";
		String SearchButtonXpath = "//div[@class='searchBox']//button[@class='search-button mat-flat-button mat-button-base']";
		String DPKeyCheckBoxXPath = "//div[@class='dp-number']/a[text()='DPKey']//ancestor::td//div[@class='checkbox']";
		String TopicExpandButton = "(//div[@class='analysisWorkbench']//div[@class='gridBox ng-star-inserted']//div[@class='payer-group-header ng-star-inserted']//button[@class='mat-flat-button mat-button-base'])[1]";
		String CaptureDispoBtn = "//div[@class='dispositionsMenu']//button[contains(@class,'dispositions-menu-button')]";
		String PresentDisposition = "//div[@class='cdk-overlay-container']//div[contains(@class,'mat-menu-panel ')]//div/button[@class='mat-menu-item' and text()='Present']";
		String PolicySelectionDrawerButton = "//button[@class='policy-selection-button mat-flat-button mat-button-base mat-accent']";
		String MedPolicySearchBox = "//div[@class='policy-drawer']//div[@class='mat-form-field-infix']//input[contains(@class,'mat-input-element')]";
		String MedPolicySearchButton = "//div[@class='policy-drawer']//button[@class='search-button mat-flat-button mat-button-base']";
		String MedPolicyAfterSearch = "//div[@class='policy-drawer']//table[@class='k-grid-table']//div[contains(text(),'MedPolicyValue')]";
		String ApplyToOpportunityGridBtn = "//div[@class='policy-drawer']//button/span[contains(text(), 'Apply To Opportunity Grid')]/parent::button";
		String HighSavingsChkboxXpath = "//div[@class= 'present-disposition-content']//span[text()='High Savings']//ancestor::mat-checkbox";
		String PresentDispositionOKBtn = "//mat-dialog-actions//button/span[text()=' Ok ']//parent::button";

		String DPKeyXPathforDisposition = StringUtils.replace(DPKeyCheckBoxXPath, "DPKey", String.valueOf(DPKey));
		String MedPolicyXpath = StringUtils.replace(MedPolicyAfterSearch, "MedPolicyValue", MedPolicy);

		// Click PolicySelectionDrawerButton
		oSeleniumUtils.clickGivenXpath(PolicySelectionDrawerButton);

		// Enter MedicalPolicy in the search Box
		oSeleniumUtils.Enter_given_Text_Element(MedPolicySearchBox, MedPolicy);

		// Click Search Button
		oSeleniumUtils.clickGivenXpath(MedPolicySearchButton);

		// Click the MedPolicy Name so that ApplyToOpportunityGrid Button is
		// enabled
		oSeleniumUtils.clickGivenXpath(MedPolicyXpath);

		// Click ApplyToOpportunityGrid Button
		oSeleniumUtils.clickGivenXpath(ApplyToOpportunityGridBtn);

		oSeleniumUtils.defaultWait(ProjectVariables.TImeout_6_Seconds);

		// Enter DP in the Opps Search box
		oSeleniumUtils.Enter_given_Text_Element(SearchFileldXpath, String.valueOf(DPKey));
		oSeleniumUtils.clickGivenXpath(SearchButtonXpath);

		oSeleniumUtils.defaultWait(ProjectVariables.TImeout_2_Seconds);

		// Click on the icon to expand the topic
		oSeleniumUtils.clickGivenXpath(TopicExpandButton);

		// click on the check box related to DPKey
		oSeleniumUtils.clickGivenXpath(DPKeyXPathforDisposition);

		// Capture Disposition
		oSeleniumUtils.Click_given_Locator(CaptureDispoBtn);
		oSeleniumUtils.defaultWait(ProjectVariables.TImeout_1_Seconds);
		oSeleniumUtils.Click_given_Locator(PresentDisposition);

		oSeleniumUtils.defaultWait(ProjectVariables.TImeout_2_Seconds);
		// Enter Disposition values
		oSeleniumUtils.Click_given_Locator(HighSavingsChkboxXpath);

		// Click OK button
		oSeleniumUtils.Click_given_Locator(PresentDispositionOKBtn);

	}

	public void validateRuleRelationAlertinAWB(String topicLevel, String dPLevel) {

		String TopicRuleRelationshipIcon = "(//i[@class='fa fa-bookmark'])[1]";
		String DPRuleRelationshipIcon = "(//i[@class='fa fa-bookmark'])[2]";
		WebElement TopicIcon;
		WebElement DPIcon;

		HashMap<String, List<String>> DPKeyDetails = new HashMap<String, List<String>>();

		// Retrieve Session Variables

		DPKeyDetails = Serenity.sessionVariableCalled("DPKeyDetails");
		String ClientKey = Serenity.sessionVariableCalled("ClientKey");

		String SearchFileldXpath = "//div[@class='searchBox']//input";
		String SearchButtonXpath = "//div[@class='searchBox']//button[@class='search-button mat-flat-button mat-button-base']";
		String DPKeyAllCheckBoxes = "//div[@class='dp-number']/a[text()='DPKeyArg']//ancestor::td//parent::tr//following-sibling::tr//div[@class='checkbox']";
		String DPKeyCheckBoxXPath = "//div[@class='dp-number']//button[text()='DPKey']//ancestor::td//div[@class='checkbox']";
		String DPKey = "//div[@class='dp-number']//button[text()='DPKeyArg']";
		String TopicExpandButton = "(//div[@class='analysisWorkbench']//div[@class='gridBox ng-star-inserted']//div[@class='payer-group-header ng-star-inserted']//button[@class='mat-flat-button mat-button-base'])[1]";
		String AllDPExpandButton = "//div[@class='dp-box']//button";
		String PolicySelectionDrawerButton = "//button[@class='policy-selection-button mat-flat-button mat-button-base mat-accent']";
		String MedPolicySearchBox = "//div[@class='policy-drawer']//div[@class='mat-form-field-infix']//input[contains(@class,'mat-input-element')]";
		String MedPolicySearchButton = "//div[@class='policy-drawer']//button[@class='search-button mat-flat-button mat-button-base']";
		String MedPolicyAfterSearch = "//div[@class='policy-drawer']//table[@class='k-grid-table']//div[contains(text(),'MedPolicyValue')]";
		String ApplyToOpportunityGridBtn = "//div[@class='policy-drawer']//button/span[contains(text(), 'Apply To Opportunity Grid')]/parent::button";

		String PayershortXPath = "//button[contains(text(),'PayershortArg')]//ancestor::td//parent::tr//td//input[contains(@class,'checkbox')]";
		String Payershortpath = "";

		int DPKeyCountForValidation = 0;
		boolean RulerelationIconValidated = false;

		Set<String> DPKeys = DPKeyDetails.keySet();
		for (String key : DPKeys) {

			List<String> DPDetails = DPKeyDetails.get(key); // Put the returned
															// List values in a
															// List

			// Click PolicySelectionDrawerButton
			oSeleniumUtils.clickGivenXpath(PolicySelectionDrawerButton);
			oSeleniumUtils.defaultWait(ProjectVariables.TImeout_2_Seconds);
			// Enter MedicalPolicy in the search Box
			oSeleniumUtils.Enter_given_Text_Element(MedPolicySearchBox, DPDetails.get(0));
			// Click Search Button
			oSeleniumUtils.clickGivenXpath(MedPolicySearchButton);

			// Click the MedPolicy Name so that ApplyToOpportunityGrid Button is
			// enabled
			String MedPolicyXpath = StringUtils.replace(MedPolicyAfterSearch, "MedPolicyValue", DPDetails.get(0));
			oSeleniumUtils.clickGivenXpath(MedPolicyXpath);

			// Click ApplyToOpportunityGrid Button
			oSeleniumUtils.clickGivenXpath(ApplyToOpportunityGridBtn);
			oSeleniumUtils.defaultWait(ProjectVariables.TImeout_6_Seconds);
			// Enter DP in the Opps Search box
			oSeleniumUtils.Enter_given_Text_Element(SearchFileldXpath, key);
			oSeleniumUtils.clickGivenXpath(SearchButtonXpath);
			oSeleniumUtils.defaultWait(ProjectVariables.TImeout_2_Seconds);

			// Click on the icon to expand the topic
			oSeleniumUtils.highlightElement(TopicExpandButton);
			oSeleniumUtils.clickGivenXpath(TopicExpandButton);
			oSeleniumUtils.defaultWait(ProjectVariables.TImeout_2_Seconds);

			// Click on the DP icon to expand the topic

			oSeleniumUtils.highlightElement(AllDPExpandButton);
			oSeleniumUtils.clickGivenXpath(AllDPExpandButton);

			String DPKeyXPath = StringUtils.replace(DPKey, "DPKeyArg", key);

			if (oSeleniumUtils.is_WebElement_Displayed(DPKeyXPath)) {
				TopicIcon = getDriver().findElement(By.xpath(TopicRuleRelationshipIcon));
				oSeleniumUtils.highlightElement(TopicRuleRelationshipIcon);
				// Validate if RuleRelationshipIcon is present
				if (oSeleniumUtils.is_WebElement_Displayed(TopicRuleRelationshipIcon)) {
					Assert.assertTrue("RuleRelationshipIcon is displayed  for  Topic::", true);
				} else {
					Assert.assertTrue("RuleRelationshipIcon is not displayed  for  Topic::", false);
				}

				DPIcon = getDriver().findElement(By.xpath(DPRuleRelationshipIcon));
				oSeleniumUtils.highlightElement(DPRuleRelationshipIcon);
				// Validate if RuleRelationshipIcon is present
				if (oSeleniumUtils.is_WebElement_Displayed(DPRuleRelationshipIcon)) {
					Assert.assertTrue("RuleRelationshipIcon is displayed  for  Topic::", true);
				} else {
					Assert.assertTrue("RuleRelationshipIcon is not displayed  for  Topic::", false);
				}

				// Validate ruleRelationshipCount for Topic and DP on Mouse
				// Hover

				// Validate if RuleRelationshipIcon Tool tip is present
				oSeleniumUtils.highlightElement(TopicRuleRelationshipIcon);
				oSeleniumUtils.moveToElement(TopicIcon);
				String UITooltipText = oSeleniumUtils.Get_Value_By_given_attribute("title", TopicRuleRelationshipIcon);
				if (UITooltipText.equalsIgnoreCase("Rule Relationship Alert 1 DP(s)")) {
					Assert.assertTrue("Tooltip::" + UITooltipText + " is displayed when User hovers mouse for  Topic::",
							true);
				} else {
					Assert.assertTrue("Tooltip is not displayed when User hovers mouse for  Topic::", false);
				}

				oSeleniumUtils.highlightElement(DPIcon);
				oSeleniumUtils.moveToElement(DPIcon);
				String UITooltipTextDP = oSeleniumUtils.Get_Value_By_given_attribute("title", DPRuleRelationshipIcon);
				if (UITooltipTextDP.equalsIgnoreCase("Rule Relationship Alert")) {
					Assert.assertTrue("Tooltip::" + UITooltipTextDP + " is displayed when User hovers mouse for  DP::",
							true);
				} else {
					Assert.assertTrue("Tooltip is not displayed when User hovers mouse for  DP::", false);
				}
				RulerelationIconValidated = true;

			} // End of IF

			DPKeyCountForValidation = DPKeyCountForValidation + 1;
			if (DPKeyCountForValidation == 5) {
				break;
			}
		}

		if (RulerelationIconValidated == false) {
			Assert.assertTrue("RuleRelationship Icon and Tooltip is validated at Topic & DP levels", true);
		} else {
			Assert.assertTrue(
					"RuleRelationship Icon and Tooltip is not validated at Topic & DP levels asany captured DPKey not displayed in the CPW AWB",
					false);
			getDriver().quit();
		}

	}

	public void validateRuleRelationshipIcons(String dPorTopicLevel) {
		String TopicRuleRelationshipIcon = "(//i[@class='fa fa-bookmark'])[1]";
		String DPRuleRelationshipIcon = "(//i[@class='fa fa-bookmark'])[2]";
		WebElement TopicIcon;
		WebElement DPIcon;

		switch (dPorTopicLevel) {

		case "TopicLevel":

			TopicIcon = getDriver().findElement(By.xpath(TopicRuleRelationshipIcon));
			// Validate if RuleRelationshipIcon is present
			if (oSeleniumUtils.is_WebElement_Displayed(TopicRuleRelationshipIcon)) {
				Assert.assertTrue("RuleRelationshipIcon is displayed  for  Topic::", true);
			} else {
				Assert.assertTrue("RuleRelationshipIcon is not displayed  for  Topic::", false);
			}

			break;

		case "DPLevel":

			DPIcon = getDriver().findElement(By.xpath(DPRuleRelationshipIcon));
			// Validate if RuleRelationshipIcon is present
			if (oSeleniumUtils.is_WebElement_Displayed(DPRuleRelationshipIcon)) {
				Assert.assertTrue("RuleRelationshipIcon is displayed  for  Topic::", true);
			} else {
				Assert.assertTrue("RuleRelationshipIcon is not displayed  for  Topic::", false);
			}

			break;

		default:
			System.out.println("No switch case input provided");

		}
	}

	public void navigateToRulerelationPopup(String section) {
		oSeleniumUtils.clickGivenXpath(TopicRuleRelationshipIcon);
	}

	public void captureDispositionForProvidedDetailsinAWB(String dispositionToTake, String dPKeyCount,
			String medicalPolicyCount) {

		HashMap<String, List<String>> MedPolicyDetails = new HashMap<String, List<String>>();

		// Retrieve Session Variables

		MedPolicyDetails = Serenity.sessionVariableCalled("MedPolicyDetailsMap");

		String SearchFileldXpath = "//div[@class='searchBox']//input";
		String SearchButtonXpath = "//div[@class='searchBox']//button[@class='search-button mat-flat-button mat-button-base']";

		String DPKeyCheckBoxXPath = "//div[@class='dp-number']//button[text()='DPKey']//ancestor::td//div[@class='checkbox']";
		String TopicExpandButton = "(//div[@class='analysisWorkbench']//div[@class='gridBox ng-star-inserted']//div[@class='payer-group-header ng-star-inserted']//button[@class='mat-flat-button mat-button-base'])[1]";
		String AllDPExpandButton = "//div[@class='dp-box']//button";

		String CaptureDispoBtn = "//div[@class='dispositionsMenu']//button[contains(@class,'dispositions-menu-button')]";
		String PresentDisposition = "(//button[contains(text(),'Present')])[1]";

		String PolicySelectionDrawerButton = "//button[@class='policy-selection-button mat-flat-button mat-button-base mat-accent']";
		String MedPolicySearchBox = "//div[@class='policy-drawer']//div[@class='mat-form-field-infix']//input[contains(@class,'mat-input-element')]";
		String MedPolicySearchButton = "//div[@class='policy-drawer']//button[@class='search-button mat-flat-button mat-button-base']";
		String MedPolicyAfterSearch = "//div[@class='policy-drawer']//table[@class='k-grid-table']//div[contains(text(),'MedPolicyValue')]";
		String ApplyToOpportunityGridBtn = "//div[@class='policy-drawer']//button/span[contains(text(), 'Apply To Opportunity Grid')]/parent::button";

		String HighSavingsChkboxXpath = "//div[@class= 'capture-disposition-content']//span[text()='High Savings']//ancestor::mat-checkbox//div";
		String PresentDispositionOKBtn = "//mat-dialog-actions//button/span[text()=' Ok ']//parent::button";
		String PayershortXPath = "//button[contains(text(),'PayershortArg')]//ancestor::td//parent::tr//td//input[contains(@class,'checkbox')]";
		String Payershortpath = "";

		Set<String> DPKeys = MedPolicyDetails.keySet();
		for (String key : DPKeys) {

			List<String> DPDetails = MedPolicyDetails.get(key); // Put the
																// returned List
																// values in a
																// List

			Serenity.setSessionVariable("DPKey").to(key);
			Serenity.setSessionVariable("PayerShort").to(DPDetails.get(1).trim());

			// Click PolicySelectionDrawerButton
			oSeleniumUtils.clickGivenXpath(PolicySelectionDrawerButton);
			oSeleniumUtils.defaultWait(ProjectVariables.TImeout_2_Seconds);
			// Enter MedicalPolicy in the search Box
			oSeleniumUtils.Enter_given_Text_Element(MedPolicySearchBox, DPDetails.get(0));
			// Click Search Button
			oSeleniumUtils.clickGivenXpath(MedPolicySearchButton);
			oSeleniumUtils.defaultWait(ProjectVariables.TImeout_2_Seconds);

			// Click the MedPolicy Name so that ApplyToOpportunityGrid Button is
			// enabled
			String MedPolicyXpath = StringUtils.replace(MedPolicyAfterSearch, "MedPolicyValue", DPDetails.get(0));
			oSeleniumUtils.clickGivenXpath(MedPolicyXpath);

			// Click ApplyToOpportunityGrid Button
			oSeleniumUtils.clickGivenXpath(ApplyToOpportunityGridBtn);
			oSeleniumUtils.defaultWait(ProjectVariables.TImeout_6_Seconds);
			// Enter DP in the Opps Search box
			oSeleniumUtils.Enter_given_Text_Element(SearchFileldXpath, key);
			oSeleniumUtils.clickGivenXpath(SearchButtonXpath);
			oSeleniumUtils.defaultWait(ProjectVariables.TImeout_2_Seconds);

			// Click on the icon to expand the topic
			oSeleniumUtils.highlightElement(TopicExpandButton);
			oSeleniumUtils.clickGivenXpath(TopicExpandButton);
			oSeleniumUtils.defaultWait(ProjectVariables.TImeout_2_Seconds);

			// Click on the DP icon to expand the topic

			oSeleniumUtils.highlightElement(AllDPExpandButton);
			oSeleniumUtils.clickGivenXpath(AllDPExpandButton);

			// Check if payershort value,if it is not blank ,then take
			// Disposition for the Payershort under the DP
			if (!DPDetails.get(1).trim().isEmpty()) {
				Payershortpath = StringUtils.replace(PayershortXPath, "PayershortArg", DPDetails.get(1).trim());
				oSeleniumUtils.highlightElement(Payershortpath);
				oSeleniumUtils.clickGivenXpath(Payershortpath);
			} else {
				// click on the check box related to DPKey
				String DPKeyXPathforDisposition = StringUtils.replace(DPKeyCheckBoxXPath, "DPKey", key);
				oSeleniumUtils.clickGivenXpath(DPKeyXPathforDisposition);
			}

			// Capture Disposition
			oSeleniumUtils.Click_given_Locator(CaptureDispoBtn);
			oSeleniumUtils.defaultWait(ProjectVariables.TImeout_1_Seconds);

			if (dispositionToTake.equalsIgnoreCase("Present")) {
				oSeleniumUtils.highlightElement(PresentDisposition);
				oSeleniumUtils.clickGivenXpath(PresentDisposition);
			}

			oSeleniumUtils.defaultWait(ProjectVariables.TImeout_5_Seconds);

			// Enter Disposition values
			oSeleniumUtils.highlightElement(HighSavingsChkboxXpath);
			oSeleniumUtils.Click_given_Locator(HighSavingsChkboxXpath);

			// Click OK button
			oSeleniumUtils.Click_given_Locator(PresentDispositionOKBtn);

			// Call MongoDb Method to validate

			boolean AssocaitedDPexists = MongoDBUtils.GetAvailableDPKeyWithRelations("", "", "Present", "oppty");
			if (AssocaitedDPexists == true) {
				break;
			}
		} // end for

		boolean dispo = MongoDBUtils.validateDPDisposition("Present");
		if (dispo == false) {
			Assert.assertTrue("Dispotion not displayed as Present for DPKey::", false);
			getDriver().quit();
		} else {
			Assert.assertTrue("Dispotion  displayed as Present for DPKey::", true);
		}

	}
	
	public void verifygridcolumnsinAWBPae(String gridname) {
		switch (gridname) {
		case "eLL":
			Assert.assertTrue("Unable to click the '" + gridname + "' tab in AWBGrid", 
					oSeleniumUtils.clickGivenXpath(StringUtils.replace(Div_contains_text, "value", "Opportunities(eLL)")));
			oSeleniumUtils.defaultWait(ProjectVariables.TImeout_5_Seconds);
			// To validate the coloumns in eLL Tab in awbgrid
			verifyThecolumnnamesinthegrid(gridname, ProjectVariables.eLLtabcolnames);
			break;
		case "RVA":
			// To validate the coloumns in RVA Tab in awbgrid
			verifyThecolumnnamesinthegrid(gridname, ProjectVariables.RVAtabcolnames);
			GenericUtils.Verify("'Capture Dispositions' button should be displayed in " + gridname + " in AWBGrid",
					oSeleniumUtils.is_WebElement_Displayed(
							StringUtils.replace(Span_contains_text, "value", "Capture Dispositions")));
			break;
		default:
			Assert.assertTrue("case not found==>" + gridname, false);
			break;
		}
		
	}
	
	public void verifyThecolumnnamesinthegrid(String gridname,String gridcolnames) 
	{

		List<String> ColnamesList=Arrays.asList(gridcolnames.split(","));
		
		for (int i = 0; i < ColnamesList.size(); i++) 
		{
			switch(ColnamesList.get(i))
			{
			case "DP":
			case "Raw Savings":
			case "Aggressive Savings":
			case "Edits":
				GenericUtils.Verify("'"+ColnamesList.get(i)+"' colname should be displayed under '"+gridname+"' of AWB page", oSeleniumUtils.is_WebElement_Displayed(StringUtils.replace(Span_contains_text, "value", ColnamesList.get(i))));
			break;
			default:
				GenericUtils.Verify("'"+ColnamesList.get(i)+"' colname should be displayed under '"+gridname+"' of AWB page", oSeleniumUtils.is_WebElement_Displayed(StringUtils.replace(Tag_th_contains_text, "value", ColnamesList.get(i))));
			break;
			}
		}
	}
	
		
	//################################################################################################################
	//Chaitanya eLL methods
	
	//Method to get 2nd type of dpkey
	public void geteLLDPsnotpartofoppty(int rowno,String user,String disposition) throws Exception
	{
		List<String> PPSList= new ArrayList<>();
		PresentationProfile oPresentationProfile=this.switchToPage(PresentationProfile.class);
		boolean bstatus=false;
		ProjectVariables.clientKeysList.clear();
		EnvironmentVariables environmentVariables = SystemEnvironmentVariables.createEnvironmentVariables();
		String restURI = EnvironmentSpecificConfiguration.from(environmentVariables).getProperty("restapi.baseuri");
		System.out.println("Rest API::"+restURI);
		
		//Method to get clientkeys for the given user
		RetrievetheClientdatafromtheResponse(user, restURI + ProjectVariables.CLIENT_TEAM_DATA_ENDPOINT);
		
		for (String clientkey : ProjectVariables.clientKeysList) 
		{
			//To get unique pps from service for the given clientkey
			oPresentationProfile.getUniquePPSfromClientconfigService(clientkey);

			//mongo method to retreive the 2nd type of dpkeys
			bstatus=MongoDBUtils.rvaDPsnotpartofoppty(clientkey);
		
			if(bstatus)
			{
				break;
			}
		}
		
		if(!bstatus)
		{
			Assert.assertTrue("2nd type of dpkeys(eLl without oppty) were not available from the given user and corres clients", false);
		}
		
		PPSList=Arrays.asList(Serenity.sessionVariableCalled("pps").toString().split(","));
		
		//To get pps
		getPPSfromthegiven(PPSList);
		
		String Client=Serenity.sessionVariableCalled("client");
		String dpKey=Serenity.sessionVariableCalled("DPkey");
		String sInsuranceKeys=Serenity.sessionVariableCalled("Insurancekeys");
		String Src="rva";
		
		//Capture method through service
		Capture_the_data_for_the_pipeline(disposition,sInsuranceKeys,user,dpKey,Src,Client);
		
		//To set data into excel
		ExcelUtils.setELLCellData(rowno, "i_DP Key", dpKey);
		ExcelUtils.setELLCellData(rowno, "Client", Client);
		ExcelUtils.setELLCellData(rowno, "Medical_Policy", Serenity.sessionVariableCalled("Medicalpolicy"));
		ExcelUtils.setELLCellData(rowno, "Topic", Serenity.sessionVariableCalled("Topic"));
		ExcelUtils.setELLCellData(rowno, "PPS", Serenity.sessionVariableCalled("pps"));
	}
	
	//get the pps from the given ppsList
	public static void getPPSfromthegiven(List<String> PPSList)
	{
		HashSet<String> insuranceKeys = new HashSet<>();
		HashSet<String> payerKeys = new HashSet<>();
		HashSet<String> Claimtype = new HashSet<>();
		String[] sPPsList = StringUtils.split(PPSList.get(0),";");
		
	
		for (String pps : sPPsList) 
		{
			String payerShort=StringUtils.substringBefore(StringUtils.substringBeforeLast(pps, "-"), "-");
			String insurance=StringUtils.substringBetween(pps, "-", "-");
			String claimType=StringUtils.substringAfterLast(pps, "-");
			payerKeys.add(MongoDBUtils.Retrieve_the_PayerKey_From_mongodb_with_the_given_Payershort(payerShort, "Payershort"));
			insuranceKeys.add(MongoDBUtils.Retrieve_the_PayerKey_From_mongodb_with_the_given_Payershort(insurance, "Insurance"));
			Claimtype.add("\""+claimType+"\"");
		}
		
		String sInsuranceKeys=String.join(",", insuranceKeys);
		Serenity.setSessionVariable("Payerkeys").to(String.join(",", payerKeys));
		Serenity.setSessionVariable("Claimtypes").to(Claimtype);
		Serenity.setSessionVariable("Insurancekeys").to(sInsuranceKeys);
	}
	
	//Method to get 3rd type of dpkey
	public void geteLLDPspartofoppty(int rowno,String user,String disposition) throws Exception
	{
		List<String> PPSList= new ArrayList<>();
		
		//Db method to get 3rd type of dp and corres data
		MongoDBUtils.rvaDPsPartofeLLandOppty();
		
		PPSList=Arrays.asList(Serenity.sessionVariableCalled("pps").toString().split(","));
		
		//To get pps
		getPPSfromthegiven(PPSList);
		

		String Client=Serenity.sessionVariableCalled("client");
		String dpKey=Serenity.sessionVariableCalled("DPkey");
		String sInsuranceKeys=Serenity.sessionVariableCalled("Insurancekeys");
		String Src="rva";
		
		//Capture method through service
		Capture_the_data_for_the_pipeline(disposition,sInsuranceKeys,user,dpKey,Src,Client);
		
		//To set data into excel
		ExcelUtils.setELLCellData(rowno, "i_DP Key", dpKey);
		ExcelUtils.setELLCellData(rowno, "Client", Client);
		ExcelUtils.setELLCellData(rowno, "Medical_Policy", Serenity.sessionVariableCalled("Medicalpolicy"));
		ExcelUtils.setELLCellData(rowno, "Topic", Serenity.sessionVariableCalled("Topic"));
		ExcelUtils.setELLCellData(rowno, "PPS", Serenity.sessionVariableCalled("pps"));

	}

	//Method to capture data for pipeline
	public void Capture_the_data_for_the_pipeline(String Disposition, String InsuranceKeys, String User,
		String DPkey, String sSource, String sClient ) throws IOException {
		
		if (!(Disposition.equalsIgnoreCase("Present")||Disposition.equalsIgnoreCase("Invalid"))){
			GenericUtils.Verify("No Valid disposition given", false);
		}
		
		List<String> DPKeysList = null;
		String Requestbody = null;
		String capturedcommand = null;
		Serenity.setSessionVariable("Disposition").to(Disposition);
		capturedcommand = "insert";	
		String sClientkey=RetrieveTheClientkeyfromgivenClientthroughservice(sClient);
		Serenity.setSessionVariable("clientkey").to(sClientkey);
		// To capture the disposition for retrieved Mongo Data through the
		// service
		

		
			ProjectVariables.CapturedDPkey = Long.valueOf(DPkey);

			
			  if (Disposition=="Invalid"||Disposition=="Not Reviewed") {
				// Requestbody				
				Requestbody = "{\r\n" + "	\"client_key\":" + Serenity.sessionVariableCalled("clientkey") + ",\r\n"
						+ "	\"clientDesc\": \"" + Serenity.sessionVariableCalled("client") + "\",\r\n"
						+ "	\"decision_points\":[\r\n" + "		{\"decision_point_id\":" + DPkey
						+ ",\r\n" + "		\"payerPolicySet\":[],\r\n" + "		\"opptySource\":\""+sSource+"\",\"payer_ids\":["
						+ Serenity.sessionVariableCalled("Payerkeys") + "],\r\n" + "		\"lob_ids\":["
						+ InsuranceKeys + "],\r\n"
						+ "		\"claim_type_ids\":"+ Serenity.sessionVariableCalled("Claimtypes")+"}],\r\n"
						+ "	\"do_not_present_until_next_run\":false,\r\n" + "	\"operation\":\"" + capturedcommand
						+ "\",\r\n" + "	\"userId\":\"" + User + "\",\r\n" + "	\"userName\":\"" + User
						+ "@ihtech.com\",\r\n" + "	\"disposition\":\"" + Disposition + "\",\r\n"
						+ "	\"page_id\":\"Analysis\",\r\n" + "	\"note\":\"\",\r\n" + "	\"reasons\":[],\r\n"
						+ "	\"priority\":\"\"\r\n" + "}\r\n";
			} else {
				Requestbody = "{\r\n" + "	\"client_key\":" + Serenity.sessionVariableCalled("clientkey") + ",\r\n"
						+ "	\"clientDesc\": \"" + Serenity.sessionVariableCalled("client") + "\",\r\n"
						+ "	\"decision_points\":[\r\n" + "		{\"decision_point_id\":" + DPkey
						+ ",\r\n" + "		\"payerPolicySet\":[],\r\n" + "		\"opptySource\":\""+sSource+"\",\"payer_ids\":["
						+ Serenity.sessionVariableCalled("Payerkeys") + "],\r\n" + "		\"lob_ids\":["
						+ InsuranceKeys + "],\r\n"
						+ "		\"claim_type_ids\":"+ Serenity.sessionVariableCalled("Claimtypes")+"}],\r\n"
						+ "	\"do_not_present_until_next_run\":false,\r\n" + "	\"operation\":\"" + capturedcommand
						+ "\",\r\n" + "	\"userId\":\"" + User + "\",\r\n" + "	\"userName\":\"" + User
						+ "@ihtech.com\",\r\n" + "	\"disposition\":\"" + Disposition + "\",\r\n"
						+ "	\"page_id\":\"Analysis\",\r\n" + "	\"note\":\"\",\r\n"
						+ "	\"reasons\":[\"High savings\",\"Business Reason\",\"Client Requested\"],\r\n"
						+ "	\"priority\":\"High\"\r\n" + "}\r\n";				
			}
			 
			// Method to capture the disposition from CPW through service
			Capture_the_disposition_through_service(Requestbody);
		

	}
	
	public void SelectPolicySelectionAndApplyFilters(String sMedicalPolicy,String criteria) throws InterruptedException {

		// select the policy selection drawer and apply all checkboxes in that
		// section
		SelectthePolicySelectionDrawerandApplyAllFilters();

		// To Selcet Display MPs/Topic toggle selection
		SelectTheDisplayMPTopicToggleinFilterPanel(criteria);//--topic

		// To select the given MP in filterPanel and applytoopportunity grid
		SelectGivenMPinFilterPanel(sMedicalPolicy, criteria);
	}

	//Method to get PPS for the given eLL DPkey for 1st type
	public List<String> getPPSforthegiveneLLDP(String dpkey,String user) throws Exception
	{
		List<String> PPSList= new ArrayList<>();
		PresentationProfile oPresentationProfile=this.switchToPage(PresentationProfile.class);
		boolean bstatus=false;
		ProjectVariables.clientKeysList.clear();
		EnvironmentVariables environmentVariables = SystemEnvironmentVariables.createEnvironmentVariables();
		String restURI = EnvironmentSpecificConfiguration.from(environmentVariables).getProperty("restapi.baseuri");
		System.out.println("Rest API::"+restURI);
		
		//Method to get clientkeys for the given user
		RetrievetheClientdatafromtheResponse(user, restURI + ProjectVariables.CLIENT_TEAM_DATA_ENDPOINT);
		
		for (String clientkey : ProjectVariables.clientKeysList) 
		{
			//To get unique pps from service for the given clientkey
			oPresentationProfile.getUniquePPSfromClientconfigService(clientkey);

			//mongo method to retreive the 2nd type of dpkeys
			bstatus=MongoDBUtils.getPPSfromthecdm(dpkey,clientkey);
		
			if(bstatus)
			{
				break;
			}
		}
		
		if(!bstatus)
		{
			Assert.assertTrue("1st type of dpkeys(Pure eLl) were not available from the given user and corres clients", false);
		}
		
		PPSList=Arrays.asList(Serenity.sessionVariableCalled("pps").toString().split(","));
		
		return PPSList;
	}

	
}