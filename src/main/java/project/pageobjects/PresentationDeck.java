package project.pageobjects;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.junit.Assert;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;

import com.google.common.base.CharMatcher;

import io.restassured.response.Response;
import net.serenitybdd.core.Serenity;
import net.serenitybdd.core.annotations.findby.FindBy;
import net.serenitybdd.core.pages.WebElementFacade;
import project.exceptions.ElementNotFoundException;
import project.utilities.ExcelUtils;
import project.utilities.GenericUtils;
import project.utilities.MongoDBUtils;
import project.utilities.OracleDBUtils;
import project.utilities.RestServiceUtils;
import project.utilities.SeleniumUtils;
import project.variables.MonGoDBQueries;
import project.variables.OracleDBQueries;
import project.variables.ProjectVariables;
import net.serenitybdd.core.pages.PageObject;

public class PresentationDeck extends PageObject {

	SeleniumUtils objSeleniumUtils;
	PresentationProfile objPresentationProfile;
	GenericUtils oGenericUtils;
	FilterDrawer oFilterDrawer;
	CPWPage oCPWPage;
	PresentationProfile oPresentationProfile;
	HomePage oHomePage;
	LoginPage oLoginPage;
	RestServiceUtils oRestServiceUtils = new RestServiceUtils();
	OracleDBQueries oOracleDBQueries;
	MongoDBUtils objMongoDBUtils;

	@FindBy(xpath = "//app-cpd-pres-container//span[@class='mat-content']/div/div")
	public WebElementFacade PresentationHeaderName;

	public String PresDeckHeaderName = "//app-cpd-pres-container//span[@class='mat-content']//div[contains(text(),'val')]";

	public String PresentationContainer = "//app-cpd-pres-container//div[contains(@class,'mat-expansion-panel-content')]";

	public String PresentationContainerMedPolicy = "//app-cpd-pres-container//div[contains(@class,'mat-expansion-panel-content')]//app-cpd-pres-policy";

	public String PresentationContainerMedPolicyAllDPs = "//app-cpd-pres-container//app-cpd-pres-dp//mat-card-header//mat-checkbox[contains(@class,'dpIdLabel')]//span/label";

	public String sAllDPcards = "//app-cpd-pres-container//app-cpd-pres-dp";

	public String PresentationContainerAllMedPolicies = "//app-cpd-pres-container//div[contains(@class,'mat-expansion-panel-content')]//app-cpd-pres-policy//label[starts-with(text(),'Medical Policy:')]";

	public String TopicsForEachMedPolicy = "//app-cpd-pres-container//div[contains(@class,'mat-expansion-panel-content')]//app-cpd-pres-policy//label[starts-with(text(),'arg')]/ancestor::app-cpd-pres-policy//app-cpd-pres-topic//mat-panel-title//label";

	public String DPsForEachTopic = "//app-cpd-pres-container//div[contains(@class,'mat-expansion-panel-content')]//app-cpd-pres-policy//label[starts-with(text(),'arg1')]/ancestor::app-cpd-pres-policy//app-cpd-pres-topic//mat-panel-title//label[text()='arg2']//ancestor::mat-expansion-panel-header/following-sibling::div//mat-card//mat-checkbox/label//label";

	public String sPresDeckHeader = "//app-cpd-pres-container//div[@class='cpd-pres-container-class']/mat-expansion-panel/mat-expansion-panel-header";

	public String sPresDeckExpansion = "//app-cpd-pres-container//div[@class='cpd-pres-container-class']/mat-expansion-panel/mat-expansion-panel-header/following-sibling::div[@class='mat-expansion-panel-content ng-trigger ng-trigger-bodyExpansion']";

	public String sTopicEditIcons = "//app-cpd-pres-container//div[contains(@class,'mat-expansion-panel-content')]//app-cpd-pres-policy//mat-panel-title/mat-icon[contains(@class,'editIcon')]";

	// "//div[contains(@class,'dialog-box')]//span[(text()='Save')]/parent::button";
	//
	// public String sEditDescriptionSubmitBtn =
	// "//div[contains(@class,'dialog-box')]//span[(text()='Submit')]/parent::button";
	//
	// public String sEditDescriptionCancelBtn =
	// "//div[contains(@class,'dialog-box')]//span[(text()='Cancel')]/parent::button";

	public String EditPopup = "//app-cpd-pres-topic-edit-popup";

	public String sEditDescriptionSaveBtn = "//app-cpd-pres-topic-edit-popup//button[text()='Save']";

	public String sEditDescriptionSubmitBtn = "//app-cpd-pres-topic-edit-popup//button[text()='Submit']";

	public String sEditDescriptionCancelBtn = "//app-cpd-pres-topic-edit-popup//button[text()='Cancel']";

	public String sEditDescriptionCloseIcon = "//mat-dialog-container//i[contains(@class,'closeIcon')]";

	//public String sEditDescriptionTextArea = "//mat-dialog-container//textarea[contains(@class,'edit-text-area')]";
	public String sEditDescriptionTextArea =  "//app-cpd-pres-topic-edit-popup//div//mat-expansion-panel[2]//textarea";

	public String EditTopicDescriptionReadOnly = "//mat-dialog-container//pre[@class='description edit-text-area']";

	public String sEditDescription = "//app-cpd-pres-topic-edit-popup//mat-expansion-panel//div//span";

	public String sEditDescriptionEnteredText = "//app-cpd-pres-topic-edit-popup//mat-expansion-panel//div//ins";
	//public String sEditDescriptionEnteredText  = "(//app-cpd-pres-topic-edit-popup//div//mat-expansion-panel)[2]//textarea";
	
	public String TopicEditTextArea = "//app-cpd-pres-topic-edit-popup//mat-expansion-panel)[2]//textarea";

	public String TopicExpandCollapseIcons = "//app-cpd-pres-topic-edit-popup//mat-expansion-panel-header//mat-icon[contains(@class,'expansionIcon')]";

	// public String TopicDPs =
	// "(//app-cpd-pres-topic//mat-panel-title//label[text()='TopicVal']/ancestor::mat-expansion-panel/div//div[@class='cardMargin
	// cdk-drop-list']//span[@class='mat-checkbox-label']//label)[1]";

	// public String TopicDPs =
	// "//app-cpd-pres-topic//mat-panel-title//label[text()='TopicVal']/ancestor::mat-expansion-panel/div//div[@class='cardMargin
	// cdk-drop-list']//span[@class='mat-checkbox-label']//label";

	// public String TopicDPs =
	// "//app-cpd-pres-topic//mat-panel-title//label[text()='TopicVal']//ancestor::mat-panel-title//ancestor::mat-expansion-panel-header/parent::mat-expansion-panel//div[contains(@class,'mat-expansion-panel-content')]//span/label";

	public String TopicDPs = "//app-cpd-topic//mat-panel-title//label[text()='TopicVal']//ancestor::mat-panel-title//ancestor::mat-expansion-panel-header/parent::mat-expansion-panel//div[contains(@class,'mat-expansion-panel-content')]//app-cpd-pres-dp//label[@class='dpIdLabel']";

	public String EditTopicPopup_DPKeys = "//app-cpd-pres-topic-edit-popup/div[@class='dialog-box']//div[contains(@id,'listBoxContent')]//div[@role='option']/span";

	public String EditTopicDPKeys = "//app-cpd-pres-topic-edit-popup/div[@class='dialog-box']//div[contains(@id,'listBoxContent')]//div[@role='option']/span[text()='DPKeyVal']";

	public String EditTopicPopup_DPDescr = "//app-cpd-pres-topic-edit-popup/div[@class='dialog-box']//div[contains(@id,'panelContent')]/div[@class='dpDescLabel']";

	public String DPKeySectionHeader = "//app-cpd-pres-topic-edit-popup//label[@class='dpDescHeader']";

	public String DPDescrSectionHeader = "(//app-cpd-pres-topic-edit-popup//label[@class='dpDescHeader'])[2]";

	// public String sDPKeyTooltipText =
	// "//div[@id='cdk-describedby-message-container']/div[@id='arg']";

	public String sAllTopics = "//app-cpd-pres-container//app-cpd-pres-policy//div[contains(@class,'mat-expansion-panel-content')]//mat-panel-title//label[starts-with(text(),'Topic')]";

	public String sTopicEditicon = "//app-cpd-pres-policy//div[contains(@class,'mat-expansion-panel-content')]//div/label[text()='Arg']//parent::div/following-sibling::mat-icon[contains(@class,'editIcon')]";

	public String sAllDPCheckboxes = "//app-cpd-pres-container//div[contains(@class,'mat-expansion-panel-content')]//app-cpd-pres-policy//app-cpd-pres-dp//mat-card-header//mat-checkbox[contains(@class,'dpIdLabel')]//span/label[starts-with(text(),'arg')]/ancestor::mat-checkbox//div[@class='mat-checkbox-inner-container']";

	public String sDPCheckboxesProp = "//app-cpd-pres-container//div[contains(@class,'mat-expansion-panel-content')]//app-cpd-pres-policy//app-cpd-pres-dp//mat-card-header//mat-checkbox[contains(@class,'dpIdLabel')]//span/label[starts-with(text(),'arg')]/ancestor::mat-checkbox//div[@class='mat-checkbox-inner-container']//input[@type='checkbox']";

	public String sAllDPs = "//app-cpd-pres-container//div[contains(@class,'mat-expansion-panel-content')]//app-cpd-pres-policy//app-cpd-pres-dp//mat-card-header//mat-checkbox[contains(@class,'dpIdLabel')]";

	public String sMedPolicyExpandPanel = "//div[contains(@class,'mat-expansion-panel-content')]//app-cpd-pres-policy/mat-expansion-panel/mat-expansion-panel-header";

	public String sTopicExpandPanel = "//app-cpd-pres-container//div[contains(@class,'mat-expansion-panel-content')]//app-cpd-pres-policy//app-cpd-pres-topic//mat-expansion-panel-header";

	// public String sDPKeyTooltipText =
	// "//div[@id='cdk-describedby-message-container']/div[@id='arg']";

	public String sDPKeyTooltipText = "//div[@class='cdk-overlay-container']//div[@class='cdk-overlay-pane mat-tooltip-panel']//div[contains(@class,'mat-tooltip')]";

	public String sDPRawSavingVal = "//app-cpd-pres-container//app-cpd-pres-dp//mat-card-header//mat-checkbox[contains(@class,'dpIdLabel')]//span/label[text()='arg']/ancestor::app-cpd-pres-dp//mat-card-content//div[@class='savings-val']";

	// public String FirstMedPolicyExpand=
	// "(//app-cpd-pres-policy//mat-expansion-panel-header//mat-icon[contains(@class,'arrow-icon')])[1]";

	public String FirstMedPolicyExpand = "(//app-cpd-pres-policy//mat-expansion-panel-header//mat-panel-title[@class='mat-expansion-panel-header-title'])[1]";

	public String FirstTopicExpand = "(//app-cpd-pres-policy//mat-expansion-panel-header)[2]";

	public String FirstMedPolicyExpandPanel = "(//app-cpd-pres-policy//mat-expansion-panel-header)[1]";

	//public String FirstTopicEditIcon = " (//app-cpd-pres-policy//mat-expansion-panel-header//mat-icon[contains(text(),'edit')])[1]";
	
	public String FirstTopicEditIcon =  "(//app-cpd-topic//mat-expansion-panel//mat-icon[contains(text(),'edit')])[1]";

	public String FirstTopicLabel = "(//app-cpd-topic//mat-expansion-panel//mat-panel-title//div//label)[2]";

	public String EditNotSavedMsg = "//div[@class='topicEditMessage ng-star-inserted']/label[contains(text(),'Edit not saved')]";

	public String EditSavedMsg = "//div[@class='topicEditMessage ng-star-inserted']/label[contains(text(),'Topic edit saved ')]";

	public String EditSavedCloseIcon = "//div[@class='topicEditMessage ng-star-inserted']/label[contains(text(),'Topic edit saved ')]//span[@aria-label='close']";

	public String AllPresProfileNames = "(//mat-tab-header)[2]//span";

	public String sPresentationDeckCloseIcon = "//span[@class='close-button-text' and text()='X']";

	public String sPresentationDeckHeader = "//div[contains(text(), 'sval')]";

	public String sDPCountOfPresentationDeck = "//div[contains(text(), 'sval')]/div";

	public String sDPCountOfTopic = "//app-cpd-pres-container//div[contains(@class,'mat-expansion-panel-content')]//app-cpd-pres-policy//div[contains(@class,'mat-expansion-panel-content')]//mat-panel-title//label[starts-with(text(),'sval')]//parent::div//parent::mat-panel-title/parent::span//parent::mat-expansion-panel-header//following-sibling::div[@class='mat-expansion-panel-content ng-trigger ng-trigger-bodyExpansion']/div//mat-panel-description/div//app-cpd-pres-dp";

	public String sExpandFilterDrawer = "//mat-drawer[contains(@class ,'matDrawer mat-drawer') and contains(@style,'hidden')]";
	public String sMPUnassignIcon = "//mat-panel-title[@class='mat-expansion-panel-header-title']/div[@class= 'container3pres']/descendant::i[@class='fa fa-arrow-circle-o-right assignIcon']";

	public String BtnExportOfPresentation = "(//ngx-spinner[@name='presDeckSpinner']//following-sibling::app-cpd-pres-tool-bar//i[@aria-hidden='true'])[2]";

	//public String sDeleteIconOfPresentation = "//span[contains(text(),'sval')]/../following-sibling::div//mat-icon[@mattooltip='Delete Profile']";
	public String sDeleteIconOfPresentation = "//span[contains(text(),'sval')]/../following-sibling::div//mat-icon[@mattooltip='Delete Presentation']";
	public String sEditIconOfPresentation = "//ngx-spinner[@name='presDeckSpinner']//following-sibling::app-cpd-pres-tool-bar//button[@mattooltip='Edit Presentation Profile']";

	//public String DPkeysForTopic = "//app-cpd-pres-policy//app-cpd-pres-topic//mat-panel-title//div/label[text()='TopicNameArg']//ancestor::app-cpd-pres-topic//div[@class='mat-expansion-panel-body']//div[@class='pres_dp_main']//label[@class='dpIdLabel']";

	public String DPkeysForTopic =  "//app-cpd-pres-policy//app-cpd-pres-topic//mat-panel-title//div/label[text()='TopicNameArg']//ancestor::app-cpd-pres-topic//div//div[@class='pres_dp_main']//label[@class='dpIdLabel']";
	
	public String MedPolicyForTopic = "(//app-cpd-pres-policy//app-cpd-pres-topic//mat-panel-title//div/label[text()='TopicNameArg']//ancestor::app-cpd-pres-policy//mat-expansion-panel-header)[1]//mat-panel-title//div/label";

	public String MedPolicyUnAssignIconForTopic = "(//app-cpd-topic//div/label[text()='TopicNameArg']//ancestor::mat-expansion-panel)[2]//ancestor::app-cpd-med-policy//div[contains(@class,'container3')]/span/i";

//	public String DPKeyAssignIcon = "//app-cpd-pres-policy//app-cpd-pres-topic//mat-panel-title//div/label[text()='TopicNameArg']//ancestor::app-cpd-pres-topic//div[@class='mat-expansion-panel-body']//div[@class='pres_dp_main']//label[@class='dpIdLabel' and text()='DPKeyArg']/ancestor::div[@class='pres_dp_main']//span/i[@class='assignIcon fa fa-arrow-circle-o-right assignIcon']";

	public String DPKeyAssignIcon = "//app-cpd-pres-policy//app-cpd-pres-topic//mat-panel-title//div/label[text()='TopicNameArg']//ancestor::app-cpd-pres-topic//div//div[@class='pres_dp_main']//label[@class='dpIdLabel' and text()='DPKeyArg']/ancestor::div[@class='pres_dp_main']//span/i[@class='assignIcon fa fa-arrow-circle-o-right assignIcon']";
				
	public String CaptureDecisionChkboxVal = "//app-cpd-capture-decision-popup//mat-radio-group//label[@class='mat-radio-label']//div[@class='mat-radio-label-content' and text()=' DecisionArg ']";

	public String EditPopup_TopicHeader = "//app-cpd-pres-topic-edit-popup//h5[@class='mat-popup-subHeader']/label";

	public String EditPopup_TopicDescrReadOnly = "(//app-cpd-pres-topic-edit-popup//mat-expansion-panel-header)[1]";

	public String EditPopup_TopicDescrEdit = "(//app-cpd-pres-topic-edit-popup//mat-expansion-panel-header)[2]";

	public String availableOppurtunitiesBtn = "//span[contains(text(),'NPP Opportunities')]/span";

	public String PresProfile_DeleteIcon = "//span[@class='pres_pro_name' and contains(text(),'PresnameArg')]/parent::span/following-sibling::div//mat-icon[text()='clear']";

	public String ReadyForPresChkbox = "//mat-checkbox[contains(@class,'ready-for-presentation')]";

	public String PresentationDeck = "//app-cpd-pres-container/div";

	public String FinalizeButton = "//app-cpd-pres-tool-bar//button[text()='FINALIZE']";

	public String FinalizeConfirmationYesBtn = "//div[@class='jqx-popover-content']//button[@role='button' and contains(text(),'Yes')]";

	public String FinalizeConfirmationNoBtn = "//div[@class='jqx-popover-content']//button[@role='button' and contains(text(),'No')]";

	public String BtnFinalize = "//button[text()='FINALIZE']";

	public String Lnk_DP = "//div[@class='pres_dp_main']//label[@class='dpIdLabel']";

	public String Chk_All_Oppurtunities = "//table[@role='grid']//span[contains(text(), 'ALL')]//preceding-sibling::label//descendant::span";

	public String Btn_Yes_FinalizeDecision = "//label[text()=' Finalized decisions can not be edited. Are you sure you want to proceed?']//following-sibling::div//button[contains(text(), 'Yes')]";

	public String Chk_MedicalPolicy = "(//div[contains(@class,'pres-deck')]//mat-checkbox)[1]";

	public String Chk_Topic = "(//div[contains(@class,'pres-deck')]//mat-checkbox)[2]";

	public String Chk_DP = "//div[@class='pres_dp_main']/mat-checkbox";

	public String Chk_FirstPayer = "(//table[@role='grid']//tbody[@role='rowgroup']//label)[1]/span[2]";

	public String firstPayer = "(//table[@role='grid']//tbody[@role='rowgroup']//label)[1]/span";

	public String firstPayerClaimType = "(//table[@role='grid']//tbody[@role='rowgroup']//label)[1]/span/span";

	public String DecisionCaptureHeader = "//label[text()='Capture Decision']";

	public String Rdo_DecisionCapture = "//input[@value='svalue']/parent::div/div[1]";

	public String Chk_DecisionPopup_Environment = "//label[text()='Environment:']/parent::div//mat-checkbox//div[contains(@class,'mat-checkbox-frame')]";

	public String Txt_DecisionPopup_ProcessingDateFrom = "(//label[contains(text(),'Processing date')]/parent::div//div[@class='mat-form-field-infix']/input)[1]";

	public String Txt_DecisionPopup_ProcessingDateTo = "(//label[contains(text(),'Processing date')]/parent::div//div[@class='mat-form-field-infix']/input)[2]";

	public String Cal_Icon_DecisionPopup_ProcessingDateFrom = "(//label[contains(text(),'Processing date')]/parent::div//div[contains(@class,'mat-form-field-suffix')])[1]";

	public String Cal_Icon_DecisionPopup_ProcessingDateTo = "(//label[contains(text(),'Processing date')]/parent::div//div[contains(@class,'mat-form-field-suffix')])[2]";

	public String Txt_DecisionPopup_DateOfServiceFrom = "(//label[contains(text(),'Date of Service')]//parent::div//div[@class='mat-form-field-infix']/input)[1]";

	public String Txt_DecisionPopup_DateOfServiceTo = "(//label[contains(text(),'Date of Service')]//parent::div//div[@class='mat-form-field-infix']/input)[2]";

	public String Cal_Icon_DecisionPopup_DateOfServiceFrom = "(//label[contains(text(),'Date of Service')]//parent::div//div[contains(@class,'mat-form-field-suffix')])[1]";

	public String Cal_Icon_DecisionPopup_DateOfServiceTo = "(//label[contains(text(),'Date of Service')]//parent::div//div[contains(@class,'mat-form-field-suffix')])[2]";

	public String Notes_DecisionPopup = "//label[contains(text(),'Notes')]//..//textarea";

	public String Btn_Cancel_DecisionPopup = "//button[text()='Capture']//parent::jqxbutton//following-sibling::jqxbutton//button[text()='Cancel ']";

	public String Modifications_DecisionPopup = "//label[contains(text(),'Modifications')]//..//textarea";

	public String Btn_Cancel_Changes_DecisionPopup = "//div[text()=' Are you sure you want to cancel? Changes will not be saved ']//parent::div//following-sibling::div//button[text()='Cancel ']";

	public String Txt_DecisionPopup_Date = "//div[@class='mat-form-field-infix']/input[@placeholder='Date']";

	public String Rdo_DecisionPopup_ResParty = "//label[text()='Responsible Party:']/parent::div//input[@value='svalue']";

	public String DD_DecisionPopup_Reasons = "//label[text()='Capture Decision']/../../following-sibling::div//div[@class='mat-select-arrow-wrapper']";

	public String ReasonsGrid_DecisionPopup = "//div[contains(@class, 'ng-trigger-transformPanelWrap')]/div";

	public String ReassignGrid_DecisionPopup = "//label[contains(text(), ' Assign selected Opportunities to')]//parent::div//following-sibling::div[@class='assign-popover']";

	public String Chk_SelectAll_header = "//span[text()=' Select All ']//preceding-sibling::div";

	public String DD_SortBy_header = "//label[text()=' Sort by: ']/..//span//div[contains(@class,'mat-form-field')]/mat-select";

	public String Lnk_Expand_Collapse_All_header = "//u[text()= 'svalue']";

	public String ExpandIcon_MP_Topic_HierarchySection = "//span[text()='svalue']//preceding-sibling::button//mat-icon";
	
	public String sDP = "//div[contains(@class,'dpWrap')]//label[normalize-space(text())][not(contains(@class,'flipLabel'))]";
	
	public String dpLOB="//div[@class='available filtered']/div[contains(text(),'MCR')]";
	
	// ************** METHODS ******************************

	public static String getDynamicXpath(String sXpath, Object sVal) {

		String sFormattedXpath = null;

		switch (sXpath.toUpperCase()) {

		case "PRESENTATION CHECKBOX":
			sFormattedXpath = "//span[text() = '" + sVal + "']/../div";
			break;

		}
		return sFormattedXpath;
	}

	public boolean ClickEditTopicButton(String sBtnName) {

		boolean ButtonClicked = false;

		try {
			switch (sBtnName) {
			case "Submit":
				objSeleniumUtils.highlightElement(sEditDescriptionSubmitBtn);
				objSeleniumUtils.clickGivenXpath(sEditDescriptionSubmitBtn);
				ButtonClicked = true;
				break;

			case "Save":
				// objSeleniumUtils.highlightElement(sEditDescriptionSaveBtn);
				objSeleniumUtils.clickGivenXpath(sEditDescriptionSaveBtn);
				objSeleniumUtils.Click_given_Locator(sEditDescriptionSaveBtn);
				ButtonClicked = true;
				break;

			case "Cancel":
				objSeleniumUtils.highlightElement(sEditDescriptionCancelBtn);
				objSeleniumUtils.clickGivenXpath(sEditDescriptionCancelBtn);
				ButtonClicked = true;

				break;

			case "Close":
				objSeleniumUtils.highlightElement(sEditDescriptionCloseIcon);
				objSeleniumUtils.clickGivenXpath(sEditDescriptionCloseIcon);
				ButtonClicked = true;
				break;

			default:
				Assert.assertTrue("No option provided from Gherkin for Switch", false);

			}
		} catch (Exception e) {
		}

		return ButtonClicked;

	}

	public boolean verifyElementState(String sElement, String sExpectedStateValue) {

		boolean bFlag = false;
		switch (sElement) {
		case "EditNotSaved":

			try {
				if (sExpectedStateValue.equalsIgnoreCase("Visible")) {
					objSeleniumUtils.highlightElement(EditNotSavedMsg);
					if (objSeleniumUtils.is_WebElement_Displayed(EditNotSavedMsg)) {
						bFlag = true;
					} else {
						bFlag = false;
					}
				}

				if (sExpectedStateValue.equalsIgnoreCase("NotVisible")) {
					objSeleniumUtils.highlightElement(EditNotSavedMsg);
					if (!objSeleniumUtils.is_WebElement_Displayed(EditNotSavedMsg)) {
						bFlag = true;
					} else {
						bFlag = false;
					}
				}
			}

			catch (Exception e) {
				System.out.println("Exception Message::" + e.getMessage());
				getDriver().quit();
			}
			break;

		case "TopicEditSaved":

			try {
				if (sExpectedStateValue.equalsIgnoreCase("Visible")) {
					objSeleniumUtils.highlightElement(EditSavedMsg);
					if (objSeleniumUtils.is_WebElement_Displayed(EditSavedMsg)) {
						bFlag = true;
					} else {
						bFlag = false;
					}
				}

				if (sExpectedStateValue.equalsIgnoreCase("NotVisible")) {

					if (!objSeleniumUtils.is_WebElement_Displayed(EditSavedMsg)) {
						bFlag = true;
					} else {
						bFlag = false;
					}
				}
			}

			catch (Exception e) {
				System.out.println("Exception Message::" + e.getMessage());
				getDriver().quit();
			}

			break;
		}

		return bFlag;
	}

	public void clickOnCloseIconOfPresentationDeck() {
		try {
			if (objSeleniumUtils.is_WebElement_Visible(availableOppurtunitiesBtn)) {
				objSeleniumUtils.highlightElement(availableOppurtunitiesBtn);
				objSeleniumUtils.Click_given_Locator(availableOppurtunitiesBtn);
				objSeleniumUtils.waitForContentLoad();

			}

		} catch (Exception e) {
			System.out.println("Exception Message::" + e.getMessage());
			getDriver().quit();
		}

	}

	public void verifyCountOfDPOfPresentationDeck() {

		String MedPolicy = objSeleniumUtils.get_TextFrom_Locator("//label[contains(text(), 'Medical Policy:')]");
		String t[] = MedPolicy.split("Medical Policy:");
		System.out.println("Medical policy text is " + t[1]);

		String t2[] = t[1].split(" \\(");
		String MedPolicyName = t2[0].trim();
		System.out.println(MedPolicyName);
		System.out.println(Serenity.sessionVariableCalled("MPName").toString());
		if (MedPolicyName.contains(Serenity.sessionVariableCalled("MPName").toString())) {
			Assert.assertTrue("Medical policy " + Serenity.sessionVariableCalled("MPName")
					+ " in available DP is matched with Medical policy " + MedPolicyName + " in Presentation deck",
					true);
		} else {
			Assert.assertTrue("Medical policy " + Serenity.sessionVariableCalled("MPName")
					+ " in available DP is not matched with Medical policy " + MedPolicyName + " in Presentation deck",
					false);
		}

		String DPAndTopicCount = t2[1].trim();
		System.out.println("DP and Topic count is " + DPAndTopicCount);
		if (DPAndTopicCount.equalsIgnoreCase(Serenity.sessionVariableCalled("DPTopicCount").toString())) {
			Assert.assertTrue("Medical policy DP and topic count " + Serenity.sessionVariableCalled("DPTopicCount")
					+ " in available DP is matched with Medical policy y DP and topic count " + DPAndTopicCount
					+ " in Presentation deck", true);
		} else {
			Assert.assertTrue("Medical policy DP and topic count " + Serenity.sessionVariableCalled("DPTopicCount")
					+ " in available DP is not matched with Medical policy y DP and topic count " + DPAndTopicCount
					+ " in Presentation deck", false);
		}

		String[] DPCountInTopics = t2[1].split(" DP");
		System.out.println("DPs count is " + DPCountInTopics[0]);
		if (DPCountInTopics[0].equalsIgnoreCase(Serenity.sessionVariableCalled("DPCount").toString())) {
			Assert.assertTrue("Count of DP's in available DP is equal to Presentation deck DP count", true);
		} else {
			Assert.assertTrue("Count of DP's in available DP is not equal to Presentation deck DP count", false);
		}

		String[] TopicCountInTopics = DPCountInTopics[1].split(" in ");
		String[] t3 = TopicCountInTopics[1].split(" Topic");
		System.out.println("Topics count is " + t3[0]);
		if (t3[0].equalsIgnoreCase(Serenity.sessionVariableCalled("TopicCount").toString())) {
			Assert.assertTrue("Count of Topics of Medical policy " + Serenity.sessionVariableCalled("MPName")
					+ " in available DP is equal to count of Topics of Medical policy " + t2[0].trim()
					+ " Presentation deck DP count", true);
		} else {
			Assert.assertTrue("Count of Topics of Medical policy " + Serenity.sessionVariableCalled("MPName")
					+ " in available DP is not equal to count of Topics of Medical policy " + t2[0].trim()
					+ " Presentation deck DP count", false);
		}

	}

	public void verifyCountOfDPOfEachMedicalPolicy() {

		int DPCountMedPolicy = findAll(PresentationContainerAllMedPolicies).size();

		for (int i = 0; i < DPCountMedPolicy; i++) {
			String sMedicalPolicy = objSeleniumUtils.get_TextFrom_Locator(PresentationContainerAllMedPolicies);
			String sDPcount = StringUtils.substringBetween(sMedicalPolicy, "(", " DP(s)");
			String sTopiccount = StringUtils.substringBetween(sMedicalPolicy, "in ", " Topic(s)");

			int DPCount = findAll(sAllDPs).size();
			if (Integer.parseInt(sDPcount) == DPCount) {
				Assert.assertTrue("Total count of DP's for a Medical policy is equal to individual DP cards count",
						true);
			} else {
				Assert.assertTrue("Total count of DP's for a Medical policy is not equal to individual DP cards count",
						false);
			}

			int TopicCount = findAll(sAllTopics).size();
			if (Integer.parseInt(sTopiccount) == TopicCount) {
				Assert.assertTrue("Total count of Topics for a Medical policy is matched with individual topic", true);
			} else {
				Assert.assertTrue("Total count of Topics for a Medical policy is not matched with individual topic",
						false);
			}

		}

	}

	public void verifyCountOfDPOfEachTopic() {

		String topic = "";
		String sDPcountOfEachTopic = "";

		int DPCountTopic = findAll(sAllTopics).size();
		List<String> topicValues = objSeleniumUtils.getWebElementValuesAsList(sAllTopics);

		for (int i = 0; i < DPCountTopic; i++) {
			String sTopicName = topicValues.get(i);

			int k = objSeleniumUtils.getCountOfCharInAString(sTopicName, '(');
			if (k == 2) {
				topic = StringUtils.substringBefore(sTopicName, " (");
				System.out.println("Topic name excluding count is " + topic);
				sDPcountOfEachTopic = StringUtils.substringBetween(sTopicName, "(", " DP(s)");
				System.out.println("Count of DP cards beside topic name is " + sDPcountOfEachTopic);
			} else if (k == 3) {
				String temp1 = StringUtils.substringBefore(sTopicName, " DP(s)");
				String temp2 = StringUtils.substringBeforeLast(temp1, " (");
				topic = temp2.trim();
				String temp3 = StringUtils.substringAfterLast(temp1, " (");
				sDPcountOfEachTopic = temp3;
			}

			String sDPsXPathOfEachTopic = StringUtils.replace(sDPCountOfTopic, "sval", topic);
			int countOfDPCards = findAll(sDPsXPathOfEachTopic).size();
			if (Integer.parseInt(sDPcountOfEachTopic) == countOfDPCards) {
				Assert.assertTrue("DP cards count " + sDPcountOfEachTopic + " of Topic " + topic
						+ " is matched with count of DP cards " + countOfDPCards + " present beside the topic name",
						true);
			} else {
				Assert.assertTrue("DP cards count " + sDPcountOfEachTopic + " of Topic " + topic
						+ " is not matched with count of DP cards " + countOfDPCards + " present beside the topic name",
						false);
			}

		}

	}

	public void verifyIconsOfPresentation() {

		String deleteIconXpath = StringUtils.replace(sDeleteIconOfPresentation, "sval",
				Serenity.sessionVariableCalled("PresentationName").toString());
		if (objSeleniumUtils.is_WebElement_Visible(deleteIconXpath)) {
			objSeleniumUtils.highlightElement(deleteIconXpath);
			Assert.assertTrue("Delete icon of presentation is verified successfully", true);
		} else {
			Assert.assertTrue("Delete icon of presentation is not verified", false);
		}

		if (objSeleniumUtils.is_WebElement_Visible(sEditIconOfPresentation)) {
			objSeleniumUtils.highlightElement(sEditIconOfPresentation);
			Assert.assertTrue("Edit icon of presentation is verified successfully", true);
		} else {
			Assert.assertTrue("Edit icon of presentation is not verified", false);
		}

		if (objSeleniumUtils.is_WebElement_Visible(BtnExportOfPresentation)) {
			objSeleniumUtils.highlightElement(BtnExportOfPresentation);
			Assert.assertTrue("Export button of presentation is verified successfully", true);
		} else {
			Assert.assertTrue("Export button of presentation is not verified", false);
		}

	}

	public String unAssigntheCreatedPresentationatAllLevels(String sPresentation, String sLevel) {
		String sLevelXpath = null;
		String sFormattedValue = null;
		switch (sLevel.toUpperCase().trim()) {
		case "MEDICAL POLICY":
			sLevelXpath = sMPUnassignIcon;
			String sXSelectedValue = "//mat-panel-title[@class='mat-expansion-panel-header-title']/div[@class= 'container3pres']/descendant::i[@class='fa fa-arrow-circle-o-right assignIcon']/ancestor::mat-panel-title[@class = 'mat-expansion-panel-header-title']/descendant::label[text()=string()]";
			oGenericUtils.clickOnElement(sLevelXpath);
			oGenericUtils.isElementExist("div", "Unassign ALL Opportunities in this Policy from:");
			String sSelectedValue = getDriver().findElement(By.xpath(sXSelectedValue)).getText();
			sFormattedValue = StringUtils.substringAfter(StringUtils.substringBefore(sSelectedValue, "(").trim(), ":")
					.trim();
			break;
		case "TOPIC":
			sLevelXpath = "//mat-panel-title[@class='mat-expansion-panel-header-title']/div[@class= 'containerPres']/descendant::i[@class='fa fa-arrow-circle-o-right assignIcon']";
			String sXTopic = "//mat-panel-title[@class='mat-expansion-panel-header-title']/div[@class= 'containerPres']/descendant::i[@class='fa fa-arrow-circle-o-right assignIcon']/ancestor::mat-panel-title[@class = 'mat-expansion-panel-header-title']/descendant::label[text()=string()]";
			oGenericUtils.clickOnElement(sLevelXpath);
			oGenericUtils.isElementExist("div", "Unassign ALL Opportunities in this Topic from:");
			String sTopicValue = getDriver().findElement(By.xpath(sXTopic)).getText();
			sFormattedValue = StringUtils.substringAfter(StringUtils.substringBefore(sTopicValue, "(").trim(), ":")
					.trim();
			break;
		case "DP CARD":
			sLevelXpath = "(//mat-card[@class='cardMain mat-card']/descendant::i[@class='fa fa-arrow-circle-o-right assignIcon'])[last()]";
			String sXDPCard = "(//mat-card[@class='cardMain mat-card']/descendant::i[@class='fa fa-arrow-circle-o-right assignIcon'])[last()]/../../descendant::label[text()=string()]";
			oGenericUtils.clickOnElement(sLevelXpath);
			oGenericUtils.isElementExist("div", "Unassign selected Opportunities from");
			String sDPCard = getDriver().findElement(By.xpath(sXDPCard)).getText();
			sFormattedValue = sDPCard.trim();
			break;
		case "HEADER":
			sLevelXpath = "//div[@class='pres-deck-subheader']/descendant::i[@class='fa fa-arrow-circle-o-right assignIcon']";
			String sMPCard = "//mat-panel-title[@class='mat-expansion-panel-header-title']/div[@class= 'container3pres']/descendant::i[@class='fa fa-arrow-circle-o-right assignIcon']/ancestor::mat-panel-title[@class = 'mat-expansion-panel-header-title']/descendant::label[text()=string()]";
			oGenericUtils.clickOnElement(sLevelXpath);
			oGenericUtils.isElementExist("div", "Unassign All the Opportunities from:");
			String sMedicalPolicy = getDriver().findElement(By.xpath(sMPCard)).getText();
			sFormattedValue = StringUtils.substringAfter(StringUtils.substringBefore(sMedicalPolicy, "(").trim(), ":")
					.trim();
			;
			break;
		default:
			Assert.assertTrue("incorrect value selected", false);
		}
		String sXPresCheckbox = getDynamicXpath("Presentation checkbox", sPresentation);
		oGenericUtils.clickOnElement(sXPresCheckbox);
		oGenericUtils.clickOnElement("span", "Okay");
		return sFormattedValue;
	}

	public void expandFilterDrawer() {
		boolean sExist = oGenericUtils.isElementExist(sExpandFilterDrawer, 2);
		if (!sExist) {
			oGenericUtils.clickOnElement("//img[@class = 'filterimage']");
		}
	}

	public boolean verifyPresentationDeckHeader() {
		boolean flag = false;
		String PresentationName = StringUtils.replace(sPresentationDeckHeader, "sval",
				Serenity.sessionVariableCalled("PresentationName"));

		if (objSeleniumUtils.is_WebElement_Displayed(PresentationName)) {
			objSeleniumUtils.highlightElement(PresentationName);
			flag = true;
		}
		return flag;

	}

	public void SelectAndFinalizeOppurtunities(String sOppLevel) {
		objSeleniumUtils.defaultWait(ProjectVariables.TImeout_5_Seconds);
		//objSeleniumUtils.Click_given_Locator(Lnk_DP);
		objSeleniumUtils.defaultWait(ProjectVariables.TImeout_3_Seconds);
		switch (sOppLevel.toUpperCase().trim()) {
		case "ALL":
			objSeleniumUtils.Click_given_Locator(Chk_All_Oppurtunities);
			objSeleniumUtils.defaultWait(ProjectVariables.TImeout_2_Seconds);
			break;
		case "PPS":
			String payerclaimType = objSeleniumUtils.get_TextFrom_Locator(firstPayer);
			System.out.println("Payer claim type is " + payerclaimType);
			Serenity.setSessionVariable("PayerClaimType").to(payerclaimType);
			oGenericUtils.clickButton(By.xpath(Chk_FirstPayer));
			objSeleniumUtils.defaultWait(ProjectVariables.TImeout_2_Seconds);

			break;
		default:
			Assert.assertTrue("Invalid argument to select oppurtunities", false);
		}
		objSeleniumUtils.Click_given_Locator(BtnFinalize);
		objSeleniumUtils.defaultWait(ProjectVariables.TImeout_3_Seconds);
		boolean b = objSeleniumUtils.is_WebElement_Displayed(
				"//div[text()='Finalize Selected Decisions']//parent::div//div[@class='jqx-popover-content']//label[contains(text(),' Finalized decisions can not be edited. Are you sure you want to proceed?')]");
		GenericUtils.Verify(
				"Finalized decisions can not be edited. Are you sure you want to proceed? message is verified successfully",
				b);
		objSeleniumUtils.Click_given_Locator(Btn_Yes_FinalizeDecision);
		objSeleniumUtils.defaultWait(ProjectVariables.TImeout_3_Seconds);
		objSeleniumUtils.waitForContentLoad();

	}

	// ==========================================================Rama========================================================================================================>
	public void DecisionCapture(String sDPItem, String sCaptureDs) {
		try {
			String[] sCPDecision = sCaptureDs.split("::");
			// Click on 'DP' item
			oGenericUtils.clickButton(By.xpath("//label[contains(text(),'" + sDPItem + "')]"));
			// Retrieve Total rows
			List<WebElement> sList = getDriver()
					.findElements(By.xpath("//tr[@role='row'][contains(@class,'mat-row')]"));
			String sCD = "//label[.='Capture Decision']";
			// Verify Payer short rows
			if (sList.size() > 0) {
				for (int i = 0; i <= sList.size(); i++) {
					getDriver()
							.findElement(By.xpath("(//tr[@role='row'][contains(@class,'mat-row')])[" + i + "]/td[1]"))
							.getText();
					// Retrieve Total LOB in each row
					List<WebElement> sTotalLOB = getDriver()
							.findElements(By.xpath("(//tr[@role='row'][contains(@class,'mat-row')])[" + i + "]/td"));
					for (int j = 1; j <= sTotalLOB.size(); j++) {
						// Select Check box for available payer short
						sTotalLOB.get(j).click();
						// Check DP Arrow item is enabled
						if (getDriver().findElement(By.xpath("//li/span[.='" + sDPItem + "']/following::button"))
								.isEnabled()) {
							// Click on "DP" arrow item
							oGenericUtils.clickButton(By.xpath("//li/span[.='" + sDPItem + "']/following::button"));
							// Verify Capture Decision screen
							oGenericUtils.isElementExist(sCD);
							// Verify

						}
					}
				}

			} else {
				GenericUtils.Verify("No Payershorts found :payershort count is zero", "FAILED");
			}
		} catch (Exception e) {
			GenericUtils.Verify("Object not found , Failed due to :=" + e.getMessage(), "FAILED");
		}
	}

	// ======================================================================================================================================================================>
	// ======================================================================================================================================================================>
	public void FinalizeDescisions(String sValidation) {
		GenericUtils oGenericUtils=this.switchToPage(GenericUtils.class);
		SeleniumUtils objSeleniumUtils=this.switchToPage(SeleniumUtils.class);
		try {
			String sFinalize = "//button[.='FINALIZE']";
			String sFinalDec = "//div[.='Finalize Selected Decisions']";
			String sFinalDecMsg = "//label[contains(text(),'Finalized decisions can not be edited. Are you sure you want to proceed?')]";
			String sNo = "//button[contains(text(),'No')]";
			// Verify Finalize button enable
			if (getDriver().findElement(By.xpath(sFinalize)).isEnabled()) {
				// Click on 'Finalize' button
				oGenericUtils.clickButton(By.xpath(sFinalize));
				// Verify 'Finalize Selected Decisions' pop up
				oGenericUtils.isElementExist(sFinalDec);
				// Verify 'Finalize Decision pop up message'
				oGenericUtils.isElementExist(sFinalDecMsg);
				// Verify "Yes/No" button present on screen
				oGenericUtils.isElementExist(sNo);
				// Click on 'Yes' button
				if(getDriver().findElements(By.xpath("(//button[contains(text(),'Yes')])[6]")).size()>0)
					getDriver().findElement(By.xpath("(//button[contains(text(),'Yes')])[6]")).click();
				else	
					oGenericUtils.clickOnElementContainsText("button", "Yes");
				objSeleniumUtils.waitForContentLoad();
			} else {
				GenericUtils.Verify("Finalize button is not enabled", "FAILED");
			}
		} catch (Exception e) {
			GenericUtils.Verify("Object not found , Failed due to :=" + e.getMessage(), "FAILED");
		}
	}

	public void verifySavingsAndDPCountOfPresentation(String PresentationName) {
		objSeleniumUtils
				.moveTo(StringUtils.replace(objPresentationProfile.sPresNameTitle, "PresNameArg", PresentationName));
		objSeleniumUtils.defaultWait(ProjectVariables.TImeout_3_Seconds);
		objSeleniumUtils.highlightElement(
				StringUtils.replace(objPresentationProfile.sTotalSavingsTexts, "sval", PresentationName));
		System.out.println(objSeleniumUtils.is_WebElement_Displayed(
				StringUtils.replace(objPresentationProfile.sTotalSavingsTexts, "sval", PresentationName)));
		String t1 = objSeleniumUtils.get_TextFrom_Locator(
				StringUtils.replace(objPresentationProfile.sTotalSavingsTexts, "sval", PresentationName));
		String[] t2 = t1.split("\\$");
		String[] sTotalSavings = t2[1].split(" ");
		String[] t3 = t1.split(" DP");
		String[] t4 = t3[0].split(PresentationName + "\n");
		int sTotalDPs = Integer.parseInt(t4[1]);
		Serenity.setSessionVariable("DPCount").to(sTotalDPs);
		System.out.println("Savings value from tooltip is " + sTotalSavings[0]);
		System.out.println("element display status upon highlight " + objSeleniumUtils.is_WebElement_Displayed(
				StringUtils.replace(objPresentationProfile.sTotalSavingsTexts, "sval", PresentationName)));
		Assert.assertTrue("Tooltip::" + sTotalSavings[0] + " is displayed when User hovers mouse on Presentation::",
				objSeleniumUtils.is_WebElement_Displayed(
						StringUtils.replace(objPresentationProfile.sTotalSavingsTexts, "sval", PresentationName)));

		int ActualTotalSavings = Integer.parseInt(CharMatcher.DIGIT.retainFrom(sTotalSavings[0]));
		Serenity.setSessionVariable("PresentationSavings").to(ActualTotalSavings);

	}

	// ====================================================================================================================================================================>
	public void selectDPItem(String sValidation, String sDPValue) {
		try {
			String sDPALL = "//span[text()='ALL']/..//label/span";
			String sFirstPayershort = "(//tr[@role='row'][contains(@class,'mat-row')])[1]/td[1]";
			String sSecondPayershort = "(//tr[@role='row'][contains(@class,'mat-row')])[2]/td[1]";

			String sPayerChbk1 = "((//tr[@role='row'][contains(@class,'mat-row')])[1]/td[1]//span)[3]";
			String sPayerChbk2 = "((//tr[@role='row'][contains(@class,'mat-row')])[2]/td[1]//span)[3]";
			String sDPArrow = "//span[.='" + sDPValue + "']/following::button[contains(@class,'arrow')]";
			String sUnAssign = "//mat-radio-button[contains(@class,'disabled')]//div[contains(text(),'Unassign')]";

			switch (sValidation.toUpperCase()) {

			case "DP ALL":
			case "DP ALL REASSIGN_UNASSIGN_DISABLED":
				oGenericUtils.clickOnElement(sDPALL);
				/*if (sValidation.equalsIgnoreCase("DP ALL REASSIGN_UNASSIGN_DISABLED")) {
					oGenericUtils.clickButton(By.xpath(sDPArrow));
					// Verify 'ReAssign and Un assign' fields are disabled ,
					// commented Re assigned as the functionality is changed
					// oGenericUtils.isElementExist(sReAssign);
					boolean blnVal = oGenericUtils.isElementExist(sUnAssign);
					GenericUtils.Verify("Un Assign Button should be disabled", blnVal);
					// click on 'Cancel' button
					oGenericUtils.clickOnElementContainsText("button", "Cancel");
				}*/
				break;
			case "DP MULTIPLE":
				List<WebElement> sList = getDriver()
						.findElements(By.xpath("//tr[@role='row'][contains(@class,'mat-row')]"));
				if (sList.size() >= 2) {
					String sPayer1 = getDriver().findElement(By.xpath(sFirstPayershort)).getText().trim();
					String sPayer2 = getDriver().findElement(By.xpath(sSecondPayershort)).getText().trim();
					oGenericUtils.clickButton(By.xpath(sPayerChbk1));
					oGenericUtils.clickButton(By.xpath(sPayerChbk2));
					Serenity.setSessionVariable("sPayershort1").to(sPayer1.trim());
					Serenity.setSessionVariable("sPayershort2").to(sPayer2.trim());
				} else {
					GenericUtils.Verify("No Payershorts found :payershort count is not equal to 2", "FAILED");
				}
				break;
			default:
				Assert.assertTrue("case not found==>"+sValidation, false);
			break;
			}
		} catch (Exception e) {
			GenericUtils.Verify("Object not found , Failed due to :=" + e.getMessage(), "FAILED");
		}
	}

	// ==========================================================VERIFY
	// DECISIONS==================================================================================================>
	public void verifyDecisions(String sValidation, String sStatus) {
		try {
			switch (sValidation.toUpperCase()) {

			case "DP ALL":
				List<String> DPKeyList = Arrays.asList(Serenity.sessionVariableCalled("DPkey").toString().split(","));
				for (int i = 0; i < DPKeyList.size(); i++) {
					String sDate = GenericUtils.SystemTime_in_the_given_format("MMMMMMMM dd, yyyy");
					String sDP = DPKeyList.get(i).trim();

					objPresentationProfile.verifyGridUpdatedwithStatus(sStatus + "\n" + sDate, sDP);
				}
				break;
			case "DP MULTIPLE":

				String[] sPayer1 = Serenity.sessionVariableCalled("sPayershort1").toString().split(" ");
				String sCPDs = "//label[text()='Capture Decision']";
				String sDecisionStatus = "//span[contains(text(),'" + sPayer1[0] + "')]/span[contains(text(),'"
						+ sPayer1[1] + "')]/ancestor::tr//span[contains(text(),'" + sStatus + "')]";
				String sDCDisabled = "//mat-radio-button[contains(@class,'disabled'|'radio-checked')]/..//div[contains(text(),'"
						+ sStatus + "')]";
				String sLeftPaneReadonly = "//div[@class='leftBorder readOnly']";
				String sDCStatus_Testonly = "(//span[contains(text(),'" + sPayer1
						+ "')]/ancestor::tr//span[contains(text(),'" + sStatus + "')]/../..//span)[3]";
				// Condition for 'Test only'
				if (sStatus.contains("Test Only") || sStatus.contains("Follow up")) {
					oGenericUtils.isElementExist(sDCStatus_Testonly);
				} else {
					// Verify Decision status and Date
					oGenericUtils.isElementExist(sDecisionStatus);
					// Verify Final Decisions are Disable and read only
					oGenericUtils.clickButton(By.xpath(sDecisionStatus));
					// Verify 'Capture decision screen'
					oGenericUtils.isElementExist(sCPDs);
					// Verify 'Capture Decisions' are disabled
					oGenericUtils.isElementExist(sDCDisabled);
					// Verify 'Left Pane' are read only
					oGenericUtils.isElementExist(sLeftPaneReadonly);
					// Click on 'Cancel' button
					oGenericUtils.clickOnElementContainsText("button", "Cancel");
				}
				break;
			}

		} catch (Exception e) {
			GenericUtils.Verify("Object not found , Failed due to :=" + e.getMessage(), "FAILED");
		}
	}

	// =======================================================Ready for
	// presentations============================================================================================>
	public void ready_for_presentations(String sPresentation, String sValidation) {
		try {
			// Click on 'Ready for Presentation'
			String sReadyPres = "//mat-checkbox[@mattooltip='Ready For Presentation']//div[@class='mat-checkbox-background']";
			String sAllOpp = "//div[contains(text(),'Assign ALL Opportunities in this Policy')]";
			String sPres_ReadyPres = "//span[contains(text(),'"+ sPresentation+"')]/../span[contains(text(),'Ready For Presentation')]";
			String sAssignPres = "(//i[@class='fa fa-arrow-circle-o-right assignIcon'])[2]";
			Serenity.setSessionVariable("sFristPres").to(sPresentation);
			// click on 'Ready for presentation'
			WebElement element = getDriver().findElement(By.xpath(sReadyPres));
			JavascriptExecutor executor = (JavascriptExecutor) getDriver();
			executor.executeScript("arguments[0].click();", element);
			objSeleniumUtils.waitForContentLoad();
			// Click on 'Available opportunities' and filter topic
			oCPWPage.Select_the_Available_OpportunityDeck_from_Presentationview();
			oFilterDrawer.selectMedicalPolicyAndTopic("Topic", Serenity.sessionVariableCalled("Topic"), "Select");
			// click on 'presentation'
			oGenericUtils.clickButton(By.xpath(sAssignPres));
			// clicking dp item code pending
			oGenericUtils.isElementExist(sAllOpp);
			// Verify 'Ready for presentation' message
			oGenericUtils.isElementExist(sPres_ReadyPres);
			// Click on 'Cancel' button
			oGenericUtils.clickOnElementContainsText("button", "Cancel");
			// ===============================================================================================================>
			switch (sValidation) {

			case "UnAssign":
				// Select presentation profile
				oGenericUtils.clickButton(By.xpath("//span[contains(text(),'" + sPresentation + "')]"));
				// Verify Selected Presentation highlighted
				oGenericUtils.isElementExist(
						"//span[contains(@class,'pres-tab-active')]//span[contains(text(),'" + sPresentation + "')]");
				// UnCheck 'Ready for Presentation'
				WebElement element1 = getDriver().findElement(By.xpath(sReadyPres));
				JavascriptExecutor executor1 = (JavascriptExecutor) getDriver();
				executor1.executeScript("arguments[0].click();", element1);
				// Verify 'Confirmation' pop up
				oGenericUtils.IsElementExistWithContains("div", "Confirmation");
				// Verify pop up message
				oGenericUtils.IsElementExistWithContains("label",
						"The opportunities under this profile will be auto updated. Are you sure you want to unmark this profile from Ready for Presentation?");
				// Click on 'Yes' button
				oGenericUtils.clickOnElementContainsText("button", "Yes");
				objSeleniumUtils.waitForContentLoad();
				oCPWPage.Select_the_Available_OpportunityDeck_from_Presentationview();
				oFilterDrawer.selectMedicalPolicyAndTopic("Topic", Serenity.sessionVariableCalled("Topic"), "Select");
				// Click on assign icon
				oGenericUtils.clickButton(By.xpath(sAssignPres));
				// Click on 'Cancel' button
				oGenericUtils.clickOnElementContainsText("button", "Cancel");
				break;
			}
		} catch (Exception e) {
			GenericUtils.Verify("Object not found , Failed due to :=" + e.getMessage(), "FAILED");
		}
	}

	// =================================================================================================================================>
	public void verifyNotifications(String sPath) throws Throwable {
		int i = 0;
		int sRowcount = 0;
		String sExcelPath = sPath;

		try {
			String sNotification = "//mat-icon[contains(@class,'notifiction')]|//small[text()='Notifications']/..//button";
			String sNotificationScreen = "//p[text()='Notifications']|//span[text()='Notifications']";
			String sNotificationVal = "//span[contains(@class,'note_disc')]";
			boolean blnResult = false;
			sRowcount = ExcelUtils.GetRowCount(0, sExcelPath);

			for (i = 1; i <= sRowcount; i++) {
				String sStatus = ExcelUtils.getCellData(i, 5, 0, sExcelPath);
				if (sStatus == null) {
					String sGetClinet = ExcelUtils.getCellData(i, 1, 0, sExcelPath);
					String sTypeOfDeck = ExcelUtils.getCellData(i, 0, 0, sExcelPath);
					String sGetMessage = ExcelUtils.getCellData(i, 3, 0, sExcelPath);
					String sGetPresentation = ExcelUtils.getCellData(i, 2, 0, sExcelPath);
					// Client selection
					switch (sTypeOfDeck.toUpperCase()) {
					case "PRESENTATION DECK":
					case "AVAILABLE DECK":
						oLoginPage.LoginCPDApplucation("iht_ittest09", "PM");
						oFilterDrawer.user_selects_given_value_from_Client_drop_down_list(sGetClinet);
						if (sTypeOfDeck.equalsIgnoreCase("PRESENTATION DECK")) {
							boolean sPresent = oPresentationProfile.clickGivenPresentationProfile(sGetPresentation);
							GenericUtils.Verify("Presentation Click", sPresent);
						}

						break;
					case "CPW DECK":
						oLoginPage.LoginCPDApplucation("iht_ittest09", "CPW");
						oCPWPage.clickonclient(sGetClinet, "2019");
						break;

					default:
						GenericUtils.Verify("Provided case not found" + sTypeOfDeck, "FAILED");
					}
					System.out.println("Test");

					// CLick on 'Notification' button
					oGenericUtils.clickOnElement(sNotification);
					// Verify 'Notifications' screen
					oGenericUtils.isElementExist(sNotificationScreen);

					// Verify 'Notifications' message
					blnResult = oGenericUtils.IsElementExistWithText(sNotificationVal, sGetMessage);
					if (blnResult == false) {
						System.out.println("Message not found:=\nClient details:=" + sGetClinet + "\nType of Deck:="
								+ sTypeOfDeck + "\nMessege details:=" + sGetMessage);
						ExcelUtils.SetCellDataXlsm(i, 5, 0, "FAIL", sExcelPath);
					} else {
						ExcelUtils.SetCellDataXlsm(i, 5, 0, "PASS", sExcelPath);
					}

					// Logout Application
					switch (sTypeOfDeck.toUpperCase()) {
					case "PRESENTATION DECK":
					case "AVAILABLE DECK":
						oHomePage.user_logs_out_of_the_application("PM");
						getDriver().quit();
						break;
					case "CPW DECK":
						// oHomePage.user_logs_out_of_the_application("CPW");
						getDriver().quit();
						break;

					}
				} // if end's
			}
		} catch (Exception e) {
			GenericUtils.Verify("Object not found , Failed due to :=" + e.getMessage(), "FAILED");
		} finally {
			if (i <= sRowcount) {
				ExcelUtils.SetCellDataXlsm(i, 5, 0, "FAIL", sExcelPath);
				verifyNotifications(sPath);
			}

		}
	}

	// ==============================================================================================================================>
	public void validations_approvewithMOD_DB_SERVICES_COUNT(String sDBInputs, String sServicesInput) {
		try {

			String sEndPointApproveMod = ProjectVariables.sServices.get(sServicesInput).get("EndpointURL");

			// System.out.println(sEndPointApproveMod);

			io.restassured.path.json.JsonPath sResponse = oRestServiceUtils.getService(sEndPointApproveMod).jsonPath();

			String sServiceCount = sResponse.get("totalElements").toString();

			// DB validations
			ArrayList<String> sGetDBData = OracleDBUtils.executeSQLQueryMultipleRows(
					OracleDBQueries.getOracleDBQuery(sDBInputs), ProjectVariables.DB_CONNECTION_URL, "MICRO_ETL_APP",
					"MICRO_ETL_APP");
			System.out.println(sGetDBData.size());
			for (int i = 0; i < sGetDBData.size(); i++) {
				String sDBCount = sGetDBData.get(i).toString();
				if (sDBCount.equalsIgnoreCase(sServiceCount)) {
					GenericUtils.Verify("Data Matched:=\nDB Count:=" + sDBCount + "Service Count:=" + sServiceCount,
							"PASSED");
				} else {
					GenericUtils.Verify("Data Matched:=\nDB Count:=" + sDBCount + "Service Count:=" + sServiceCount,
							"FAILED");
				}
			}

		} catch (Exception e) {
			GenericUtils.Verify("Error found , Failed due to :=" + e.getMessage(), "FAILED");
		}
	}

	// ==============================================================================================================================>
	public void validations_approvewithMOD_DB_SERVICES_DATA(String sDBInputs, String sServicesInput) {
		try {
			String sCol1[] = new String[5];

			ArrayList<String> sCode = null;
			ArrayList<String> sDescription = null;
			String sEndPointApproveMod = ProjectVariables.sServices.get(sServicesInput).get("EndpointURL");

			// System.out.println(sEndPointApproveMod);

			io.restassured.path.json.JsonPath sResponse = oRestServiceUtils.getService(sEndPointApproveMod).jsonPath();

			switch (sDBInputs.toUpperCase()) {

			case "APPROVE WITH MOD CPT CODE DATA":
				sCode = sResponse.get("content.code");
				sDescription = sResponse.get("content.description");
				sCol1[0] = "CPT_CODE";
				sCol1[1] = "CPT_DESC";
				break;
			case "APPROVE WITH MOD ICD CODE DATA":
				sCode = sResponse.get("content.code");
				sDescription = sResponse.get("content.description");
				sCol1[0] = "ICD_CODE";
				sCol1[1] = "ICD_DESC";
				break;
			case "APPROVE WITH MOD REASON CODE DATA":
				sCode = sResponse.get("content.code");
				sDescription = sResponse.get("content.description");
				sCol1[0] = "REASON_CODE";
				sCol1[1] = "REASON_DESC";
				break;
			case "APPROVE WITH MOD AGE CODE DATA":
			case "APPROVE WITH MOD AGE CODE DATA0":
				sCode = sResponse.get("key");
				sDescription = sResponse.get("description");
				sCol1[0] = "AGE_FILTER_KEY";
				sCol1[1] = "AGE_FILTER_DESC";
				break;
			}
			String[] sNewcol = { sCol1[0] };
			ArrayList<String> sGetDBCPTCode = OracleDBUtils.getQueryResults(OracleDBQueries.getOracleDBQuery(sDBInputs),
					ProjectVariables.DB_CONNECTION_URL, "MICRO_ETL_APP", "MICRO_ETL_APP", sNewcol);
			System.out.println(sGetDBCPTCode.size());
			System.out.println(sCode.size());
			for (int i = 0; i < sGetDBCPTCode.size(); i++) {
				String sDBData = sGetDBCPTCode.get(i).toString();
				String sServicesData = String.valueOf(sCode.get(i));
				if (sDBData.equalsIgnoreCase(sServicesData)) {
					GenericUtils.Verify("Data Matched:=\nDB Data:=" + sDBData + "::Service Data:=" + sServicesData,
							"PASSED");
				} else {
					GenericUtils.Verify("Data not Matched:=\nDB Data:=" + sDBData + "::Service Data:=" + sServicesData,
							"FAILED");
				}

			}
			// =================================================================>
			String[] sNewcolData = { sCol1[1] };
			ArrayList<String> sGetDBCPTDesc = OracleDBUtils.getQueryResults(OracleDBQueries.getOracleDBQuery(sDBInputs),
					ProjectVariables.DB_CONNECTION_URL, "MICRO_ETL_APP", "MICRO_ETL_APP", sNewcolData);
			System.out.println(sGetDBCPTDesc.size());
			for (int i = 0; i < sGetDBCPTDesc.size(); i++) {
				String sDBCPTDescData = sGetDBCPTDesc.get(i).toString();
				String sServicesDescData = sDescription.get(i).toString();
				if (sDBCPTDescData.equalsIgnoreCase(sServicesDescData)) {
					GenericUtils.Verify(
							"Data Matched:=\nDB Data:=" + sDBCPTDescData + "::Service Data:=" + sServicesDescData,
							"PASSED");
				} else {
					GenericUtils.Verify(
							"Data not Matched:=\nDB Data:=" + sDBCPTDescData + "::Service Data:=" + sServicesDescData,
							"FAILED");
				}

			}

		} catch (Exception e) {
			GenericUtils.Verify("Error found , Failed due to :=" + e.getMessage(), "FAILED");
		}
	}

	public void verifyAssociatedDPsExistsInPresentation(String capturedDPKey, String presName) {
		List<String> PresentationDPKeys = new ArrayList<String>();
		List<String> RuleReationDPKeys = new ArrayList<String>();
		Set<String> RuleReationDPKeysSet = new HashSet<String>();

		String PresProfilename = Serenity.sessionVariableCalled("PresentationName");

		String AllDPsinPresentation = "//app-cpd-pres-dp//label[@class='dpIdLabel']";
		PresentationDPKeys = objSeleniumUtils.getWebElementValuesAsList(AllDPsinPresentation);

		// Get DPkeys captured from the AvailableOpportunityDeck RuleRelation  Popup
		RuleReationDPKeysSet = Serenity.sessionVariableCalled("RuleRelationDPKeys");

	//If thee are no Associated Rules from the DB
	if(RuleReationDPKeysSet.isEmpty())	
	{
		 if(PresentationDPKeys.size()==1)
		 {
			 Assert.assertTrue(
						"There are no assocaited DPKeys with Companion, Counterpart or Out of Sequence relationship the for the assigned DP for Profile and no DP keys are displayed as expected for the Presentation Profile::"
								+ PresProfilename,
						true);
		 }
		 else
		 {
			 Assert.assertTrue(
						"There are no assocaited DPKeys with Companion, Counterpart or Out of Sequence relationship the for the assigned DP for Profile and  DP keys are displayed for the Presentation Profile::"
								+ PresProfilename,
						false);
		 }
	}
	else
	{
							// Convert Set to the List
							for (String DP : RuleReationDPKeysSet)
								RuleReationDPKeys.add(DP);
					
							if (PresentationDPKeys.size()-1 == RuleReationDPKeys.size())  //Considering the Associated DPKeys only,not considering the First DP
							{
								Assert.assertTrue("All the DP Keys available in the Prsentation", true);
							} else {
								Assert.assertTrue("All the DP Keys are not available in the Prsentation", false);
								getDriver().quit();
							}
					
							boolean DPKeysdisplayed = false;
					
							for (int m = 1; m < PresentationDPKeys.size(); m++) // m=1, because considering the Associated DPKeys only,not considering the First DP
							{
								for (int n = 0; n < RuleReationDPKeys.size(); n++) {
									if (PresentationDPKeys.get(m).equalsIgnoreCase(RuleReationDPKeys.get(n))) {
										DPKeysdisplayed = true;
									}
								}
								if (DPKeysdisplayed == false) {
									break;
								}
							}
					
							if (DPKeysdisplayed) {
								Assert.assertTrue(
										"When the user assgined a PPS-DP with a rule with a Companion, Counterpart or Out of Sequence relationship the system  assigned the DP (opportunity status) with the accompanying rule to the same profile::"
												+ PresProfilename+"DPKeys::"+RuleReationDPKeys,true);
							} else {
								Assert.assertTrue(
										"When the user assgined a PPS-DP with a rule with a Companion, Counterpart or Out of Sequence relationship the system  Not assigned the DP (opportunity status) with the accompanying rule to the same profile::"
												+ PresProfilename+"DPKeys::"+RuleReationDPKeys,false);
								getDriver().quit();
							}
	        }//end of else
	}

	public void validateGriddetailswithDB(String applicationPage) {

		objMongoDBUtils.getRuleRelationshipPopupDetails("", "", "");
	}

	public void clickOnDPKeyPayershort() {
		String DPKeyDynamicXpath = "//div[@class='available filtered']//ancestor::app-cpd-dp-details-container//span[1]/label[text()='DPKeyArg']";
		String FirstPayershort = "((//tr[@role='row'][contains(@class,'mat-row')])[1]/td[1]//span)[3]";
		String FirstPayershortxpath = "(//tr[@role='row'][contains(@class,'mat-row')])[1]/td[1]//span";
		String FirstClaimType = "(//tr[@role='row'][contains(@	class,'mat-row')])[1]/td[1]//span/span";
		String InsuranceDesc = "//tr[@role='row']//th[2]//label";
		String DPKey = "";

		if (Serenity.sessionVariableCalled("DPKey") != null) {
			DPKey = Serenity.sessionVariableCalled("DPKey").toString();
		} else {
			DPKey = Serenity.sessionVariableCalled("DPkey").toString();
			DPKey = "DP " + DPKey;
		}

		// click available path
		String DPXpath = StringUtils.replace(DPKeyDynamicXpath, "DPKeyArg", DPKey);
		objSeleniumUtils.clickGivenXpath(DPXpath);
		oGenericUtils.clickOnElement(FirstPayershort);
		// Capture Payershort, InsuranceDesc,ClaimType
		String Payershort = objSeleniumUtils.get_TextFrom_Locator(FirstPayershortxpath).trim();
		String Payershort1 = (Payershort.split("\\["))[0].trim();
		String ClaimType = objSeleniumUtils.get_TextFrom_Locator(FirstClaimType).trim();
		String ClaimType1 = ClaimType.substring(1, 2).trim();
		String InsuranceDesc1 = objSeleniumUtils.get_TextFrom_Locator(InsuranceDesc).trim();

		Serenity.setSessionVariable("AssignedPayershort").to(Payershort1);
		Serenity.setSessionVariable("AssignedClaimtype").to(ClaimType1);
		Serenity.setSessionVariable("AssigneInsuranceDesc").to(InsuranceDesc1);

	}

	
//******************************* assignMultipleDPstoCreatedProfile
		
	public void assignMultipleDPstoCreatedProfile() 
	{
	List<Map<String , Object>> ppslist  = new ArrayList<Map<String,Object>>();
	HashMap<String,Object> oPPS = new HashMap<String,Object>();
	List<String> payerKeysList=null;
	String Insurance=null;
	String payerkey=null;
	int insuranceKey=0;
	String payershort=null;
	String claimtype=null;
	String clientname=Serenity.sessionVariableCalled("client");
	List<String> DPKeysList=Arrays.asList(Serenity.sessionVariableCalled("DPkey").toString().split(","));
	int iClientKey = Integer.parseInt(Serenity.sessionVariableCalled("clientkey"));
	String user=Serenity.sessionVariableCalled("user");
	for (int p = 0; p < DPKeysList.size(); p++){
		
		ProjectVariables.CapturedDPkey=Long.valueOf(DPKeysList.get(p).trim());
		String sTopicKey = MongoDBUtils.Retrieve_the_PayerKey_From_mongodb_with_the_given_Payershort(Serenity.sessionVariableCalled("Topic").toString(), "Topic Based on DP");
		int iTopicKey = Integer.parseInt(sTopicKey);
		String sMPkey = MongoDBUtils.Retrieve_the_PayerKey_From_mongodb_with_the_given_Payershort(Serenity.sessionVariableCalled("Medicalpolicy").toString(), "MP");
		int iMPKey = Integer.parseInt(sMPkey);

		String sDPKey = DPKeysList.get(p).trim();
		int iDPKey= Integer.parseInt(sDPKey);
	
		//To Retrieve the payer and Lobs from the captured DP
	MongoDBUtils.GettheCapturedDispositionPayerLOBsFromtheGiven(Serenity.sessionVariableCalled("Medicalpolicy"), "Present", sDPKey);
	
	for (int i = 0; i < ProjectVariables.CapturedInsuranceList.size(); i++) 
	{
		String pps=StringUtils.substringBeforeLast(ProjectVariables.CapturedInsuranceList.get(i), "-");
		payershort=StringUtils.substringBefore(StringUtils.substringBeforeLast(pps, "-"),"-");
		Insurance=StringUtils.substringBetween(pps, "-","-");
		claimtype=StringUtils.substringAfterLast(pps, "-");
		
		insuranceKey=Integer.valueOf(GenericUtils.Retrieve_the_insuranceKey_from_insurance(Insurance));
		payerkey=StringUtils.substringAfterLast(ProjectVariables.CapturedInsuranceList.get(i), "-");;
		
			oPPS.put("\"payerKey\"", Integer.valueOf(payerkey));
			oPPS.put("\"insuranceDesc\"", "\""+Insurance+"\"");
			oPPS.put("\"claimType\"", "\""+claimtype+"\"");
			oPPS.put("\"insuranceKey\"", Integer.valueOf(insuranceKey));
			oPPS.put("\"payerShort\"", "\""+payershort+"\"");
	
			ppslist.add(oPPS);
	
			String RequestBody = "{\"isAssign\":true,\"clientKey\":"+iClientKey+",\"clientDesc\":\""+clientname+"\",\"userId\":\""+user+"\",\"profileTopics\":[{\"topicKey\":"+iTopicKey+",\"medicalPolicyKey\":"+iMPKey+",\"dps\":["+iDPKey+"],\"pps\":"+ppslist+"}],\"presentationAddition\":{\"profileId\":\""+Serenity.sessionVariableCalled("profileId")+"\",\"profileName\":\""+Serenity.sessionVariableCalled("PresentationName")+"\"},\"presentationDeletion\":null,\"assignedOpp\":1,\"reload\":true}\r\n";
			String sUpdateAssignmentEndPnt= ProjectVariables.sServices.get("updateAssignments").get("EndpointURL");
	
			Response sResponse = oRestServiceUtils.PostServiceWithSessionID(sUpdateAssignmentEndPnt, RequestBody.replaceAll("=", ":"));
	
	
	
			System.out.println(sResponse.getStatusCode());
	
			if(sResponse.getStatusCode()==200||sResponse.getStatusCode()==201)
			{
				System.out.println(payershort+","+Insurance+" was assigned successfully");
			}
			else
			{
				Assert.assertTrue(payershort+","+Insurance+" was unable assigned,requestBody=>"+RequestBody.replaceAll("=", ":")+",Response=>"+sResponse.getStatusCode(), false);
			}
	
	
			ppslist.clear();
			oPPS.clear();
	
			System.out.println("Request Body===>"+RequestBody.replaceAll("=", ":"));
			System.out.println("profileName===>"+Serenity.sessionVariableCalled("PresentationName"));
			System.out.println("DPKeys===>"+Serenity.sessionVariableCalled("DPkey"));
			//}
	
	
		}
	
	}

}

	
	public void assignSubsequentDPstoCreatedProfile(String sDPKey, String sClient ,String MedicalPolicy,String sTopic, String sPPS,String sPresentation) 
	{
	List<Map<String , Object>> ppslist  = new ArrayList<Map<String,Object>>();
	HashMap<String,Object> oPPS = new HashMap<String,Object>();
	String Insurance=null;
	String payerkey=null;
	int insuranceKey=0;
	String payershort=null;
	String claimtype=null;
	String clientname=Serenity.sessionVariableCalled("client");
	int iClientKey = Integer.parseInt(Serenity.sessionVariableCalled("clientkey"));
	String user=Serenity.sessionVariableCalled("user");
	
	ProjectVariables.CapturedDPkey = Long.valueOf(sDPKey);

	String sTopicKey = MongoDBUtils.Retrieve_the_PayerKey_From_mongodb_with_the_given_Payershort(sTopic, "Topic Based on DP");
	int iTopicKey = Integer.parseInt(sTopicKey);
	String sMPkey = MongoDBUtils.Retrieve_the_PayerKey_From_mongodb_with_the_given_Payershort(MedicalPolicy, "MP");
	int iMPKey = Integer.parseInt(sMPkey);
	int iDPKey= Integer.parseInt(sDPKey);
	
	//To Retrieve the payer and Lobs from the captured DP
	//MongoDBUtils.GettheCapturedDispositionPayerLOBsFromtheGiven(Serenity.sessionVariableCalled("Medicalpolicy"), "Present", sDPKey);

	String[] pps = StringUtils.split(sPPS, "-");
	payershort= pps[0];	
	claimtype=pps[2];
	Insurance=pps[1];
	
	payerkey = MongoDBUtils.Retrieve_the_PayerKey_From_mongodb_with_the_given_Payershort(payershort, "Payershort");
	
	insuranceKey=Integer.valueOf(GenericUtils.Retrieve_the_insuranceKey_from_insurance(Insurance));
	//payerkey=StringUtils.substringAfterLast(sPPS, "-");;
	
		oPPS.put("\"payerKey\"", Integer.valueOf(payerkey));
		oPPS.put("\"insuranceDesc\"", "\""+Insurance+"\"");
		oPPS.put("\"claimType\"", "\""+claimtype+"\"");
		oPPS.put("\"insuranceKey\"", Integer.valueOf(insuranceKey));
		oPPS.put("\"payerShort\"", "\""+payershort+"\"");

		ppslist.add(oPPS);

		String RequestBody = "{\"isAssign\":true,\"clientKey\":"+iClientKey+",\"clientDesc\":\""+clientname+"\",\"userId\":\""+user+"\",\"profileTopics\":[{\"topicKey\":"+iTopicKey+","
				+ "\"medicalPolicyKey\":"+iMPKey+",\"dps\":["+iDPKey+"],\"pps\":"+ppslist+"}],"
						+ "\"presentationAddition\":{\"profileId\":\""+Serenity.sessionVariableCalled("profileId")+"\","
								+ "\"profileName\":\""+Serenity.sessionVariableCalled("PresentationName")+"\"},"
										+ "\"presentationDeletion\":null,\"assignedOpp\":1,\"reload\":true}\r\n";
		String sUpdateAssignmentEndPnt= ProjectVariables.sServices.get("updateAssignments").get("EndpointURL");

		Response sResponse = oRestServiceUtils.PostServiceWithSessionID(sUpdateAssignmentEndPnt, RequestBody.replaceAll("=", ":"));

		System.out.println(sResponse.getStatusCode());

		if(sResponse.getStatusCode()==200||sResponse.getStatusCode()==201)
		{
			System.out.println(payershort+","+Insurance+" was assigned successfully");
		}
		else
		{
			Assert.assertTrue(payershort+","+Insurance+" was unable assigned,requestBody=>"+RequestBody.replaceAll("=", ":")+",Response=>"+sResponse.getStatusCode(), false);
		}


	ppslist.clear();
	oPPS.clear();

	System.out.println("Request Body===>"+RequestBody.replaceAll("=", ":"));
	System.out.println("profileName===>"+Serenity.sessionVariableCalled("PresentationName"));
	System.out.println("DPKeys===>"+Serenity.sessionVariableCalled("DPkey"));


}
	public void validateDPWithDPtypeAndLatestDecision(String sPPS, String sDPType, String sDecision) {		
			try {
				//payer short selection
				oFilterDrawer.userSelectsMultiplePayershorts(sPPS.split(":")[0]);
				//LOB selection
				oFilterDrawer.userSelectsMultipleLOBs(sPPS.split(":")[1]);
				//claim type selection
				oFilterDrawer.user_selects_Claimtypes_in_filtersection(sPPS.split(":")[2]);
				//Applying selection
				oFilterDrawer.user_filters_by_clicking_on_Button("Apply");	
				oFilterDrawer.user_filters_by_clicking_on_Apply_for_Medical_Policy_Topic();
				//validating default selection as rules
				GenericUtils.Verify("displaying rules as default in dropdown", objSeleniumUtils.is_WebElement_Displayed(StringUtils.replace(oPresentationProfile.Label_With_Contains, "sValue", "DP Type")+"/..//mat-select//span[contains(text(),'Rules')]"));
				//selecting DP Type
				GenericUtils.Verify("clicking on DP Type dropdown", oGenericUtils.clickOnElement(StringUtils.replace(oPresentationProfile.Label_With_Contains, "sValue", "DP Type")+"/..//span"));
				GenericUtils.Verify("clicking on "+sDPType+" dropdown option", oGenericUtils.clickOnElement(StringUtils.replace(oCPWPage.Div_contains_Class, "value", "transformPanel")+"//span[contains(text(),'"+sDPType+"')]"));
				objSeleniumUtils.defaultWait(ProjectVariables.TImeout_5_Seconds);
			} catch (ElementNotFoundException e) {
				e.printStackTrace();
			}
			switch(sDPType){
			case "Configuration":
				if(sDecision.equals("Approve Lib"))
					GenericUtils.Verify(" not displaying DP:"+ProjectVariables.getDPBasedonDecision(sDecision.toUpperCase())+" with decision as "+sDecision, objSeleniumUtils.get_Matching_WebElement_count(StringUtils.replace(oPresentationProfile.Label_With_Contains, "sValue",ProjectVariables.getDPBasedonDecision(sDecision.toUpperCase())))==
							MongoDBUtils.retrieveAllDocuments("cdm", "latestDecision", MonGoDBQueries.FilterMongoDBQuery(sDecision.toUpperCase())));
				else 				
					GenericUtils.Verify("for displaying DP:"+ProjectVariables.getDPBasedonDecision(sDecision.toUpperCase())+" with decision as "+sDecision, objSeleniumUtils.get_Matching_WebElement_count(StringUtils.replace(oPresentationProfile.Label_With_Contains, "sValue",ProjectVariables.getDPBasedonDecision(sDecision.toUpperCase()))) <=
					MongoDBUtils.retrieveAllDocuments("cdm", "latestDecision", MonGoDBQueries.FilterMongoDBQuery(sDecision.toUpperCase())));			
				break;
			case "Information":
				String [] DP=objSeleniumUtils.get_All_Text_from_Locator(sDP);
				String [] LOB=objSeleniumUtils.get_All_Text_from_Locator(dpLOB);
				GenericUtils.Verify("displaying LOB as Medicare in DP Card", DP.length==LOB.length);
				for(int i=0;i<DP.length;i++){
					System.out.println(MongoDBUtils.retrieveAllDocuments("cpd", "ellHierarchy", MonGoDBQueries.getDPType(DP[i].split(" ")[1].trim())));
					GenericUtils.Verify(" displaying DP:"+DP[i].split(" ")[1].trim()+" with MongoDB as "+sDPType+" Only", 
							MongoDBUtils.retrieveAllDocuments("cpd", "ellHierarchy", MonGoDBQueries.getDPType(DP[i].split(" ")[1].trim()))>0);
				}
				break;
				default:Assert.assertTrue("Invalid case selection", false);
		
		}
		
	}
	
}