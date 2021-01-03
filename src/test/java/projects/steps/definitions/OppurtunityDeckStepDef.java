package projects.steps.definitions;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.apache.commons.lang3.StringUtils;
import org.junit.Assert;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.seleniumhq.jetty9.util.StringUtil;
import java.io.IOException;
import com.google.common.base.CharMatcher;
import org.json.simple.parser.ParseException;
import net.serenitybdd.core.Serenity;
import net.serenitybdd.core.pages.WebElementFacade;
import net.thucydides.core.annotations.Step;
import net.thucydides.core.steps.ScenarioSteps;
import project.exceptions.ElementNotFoundException;
import project.pageobjects.CPWPage;
import project.pageobjects.FilterDrawer;
import project.pageobjects.OppurtunityDeck;
import project.pageobjects.PresentationDeck;
import project.pageobjects.PresentationProfile;
import project.utilities.AppUtils;
import project.utilities.GenericUtils;
import project.utilities.MongoDBUtils;
import project.utilities.OracleDBUtils;
import project.utilities.SeleniumUtils;
import project.variables.MonGoDBQueries;
import project.variables.OracleDBQueries;
import project.variables.ProjectVariables;

public class OppurtunityDeckStepDef extends ScenarioSteps {

	private static final long serialVersionUID = 1L;
	SeleniumUtils refSeleniumUtils;

	// CPWPage oCPWPage;
	FilterDrawer oFilterDrawer;
	OppurtunityDeck refOppurtunityDeck;
	AppUtils refAppUtils = new AppUtils();;
	PresentationDeck refPresentationDeck;
	//MongoDBUtils refMongoDBUtils = new MongoDBUtils();
	// OppurtunityDeck refOppurtunityDeck;
	GenericUtils oGenericUtils;
	PresentationProfile refPresentationProfile;
	MonGoDBQueries oMonGoDBQueries;
	CPWPage oCPWPage;

	@Step
	public void assignDPstoPresentationProfile() {
		// Capture the DPNos before assigning for verification
		List<String> OppDeckDPsVals = new ArrayList();
		OppDeckDPsVals = refSeleniumUtils.getWebElementValuesAsList(refOppurtunityDeck.sOppDeckAllDPs);
		Serenity.setSessionVariable("OppDeckDpValues").to(OppDeckDPsVals);
		List<WebElement> AssignIcons = new ArrayList();

		try {
			AssignIcons = getDriver().findElements(By.xpath(refOppurtunityDeck.sMedPolicyAssignmentIcons));

			if (AssignIcons.size() == 0) {
				Assert.assertTrue("There is no DPs to assign to Presentation Profile", false);
				getDriver().quit();
			}

			int loopcounter = 0;
			if (AssignIcons.size() > 1) {
				loopcounter = 1;
			} else {
				loopcounter = AssignIcons.size();
			}
			for (int k = 0; k < loopcounter; k++) {

				refSeleniumUtils.defaultWait(ProjectVariables.TImeout_2_Seconds);
				refSeleniumUtils.clickOn(AssignIcons.get(k));
				String sAssignChkBox = StringUtils.replace(refOppurtunityDeck.sPresentationAssignmentCheckbox, "val",
						Serenity.sessionVariableCalled("PresentationName"));
				WebElement we = getDriver().findElement(By.xpath(sAssignChkBox));
				we.click();
				refSeleniumUtils.defaultWait(ProjectVariables.TImeout_2_Seconds);
				refSeleniumUtils.Click_given_Locator(refOppurtunityDeck.sPresentationAssignmentOKBtn);
				refSeleniumUtils.defaultWait(ProjectVariables.TImeout_5_Seconds);
				AssignIcons = getDriver().findElements(By.xpath(refOppurtunityDeck.sMedPolicyAssignmentIcons));
			}
		} catch (Exception e) {
			System.out.println("Exception Message::" + e.getMessage());
			GenericUtils.Verify("Exception Message::" + e.getMessage(), false);
			getDriver().quit();
		}
	}

	@Step
	public void clickAvailableDPsDeck() {
		refOppurtunityDeck.clickOpportunityDeck();
	}

	@Step
	public void validateOppDeckState(String sContainer, String sExpectedState) throws ElementNotFoundException {
		refOppurtunityDeck.validateOppDeck_PresContainerState(sContainer, sExpectedState);
	}

	@Step
	public void validateDPCount(String sCount) {

		String sOppDeckTitle = refSeleniumUtils.getTexFfromLocator(refOppurtunityDeck.sOppDeckHeaderTitle);

		String sTemp1 = (sOppDeckTitle.split("Available DP\\(s\\)"))[1];
		String sDPCount = sTemp1.substring(2, sTemp1.length() - 1);
		int iDPCount = Integer.valueOf(sDPCount);

		if (iDPCount == 0) {
			Assert.assertTrue("The DP Count is 0  in the available OpportunitiesDeck ", true);
		} else {
			Assert.assertTrue("The DP Count is not 0  in the available OpportunitiesDeck ", false);
		}
	}

	@Step
	public void validateChkboxesforDPCards(String sDeckName) throws ElementNotFoundException {
		boolean bResult = refOppurtunityDeck.validateDeckCheckboxes(sDeckName);

		if (bResult == true) {
			Assert.assertTrue("All DP cards with Checkboxes are available for:: " + sDeckName, true);
		} else {
			Assert.assertTrue("All DP cards with Checkboxes are Not available" + sDeckName, false);
			getDriver().quit();
		}

	}

	@Step
	public void validateChkboxesStateforDPCards(String sActionOnchkbox, String sDeckName)
			throws ElementNotFoundException {
		boolean bResult = refOppurtunityDeck.validateDeckCheckboxesState(sActionOnchkbox, sDeckName);

		if (bResult == true) {
			Assert.assertTrue("User is able to" + sActionOnchkbox + " all checkboxes in the Deck:: " + sDeckName, true);
		} else {
			Assert.assertTrue("User is Not able to" + sActionOnchkbox + " all checkboxes in the Deck:: " + sDeckName,
					false);
			getDriver().quit();
		}

	}

	@Step
	public void validateWorkTodoCountPopup(String sDPKeyNo) {
		// Check if there is at least one DP Card
		try {
			refSeleniumUtils.waitForElement(refOppurtunityDeck.sOppDeckAllDPs, "shouldBevisible", 4);
			List<WebElement> DPElements = refSeleniumUtils.getElementsList("XPATH", refOppurtunityDeck.sOppDeckAllDPs);
			List<String> OppDeckDPsVals = refSeleniumUtils.getWebElementValuesAsList(refOppurtunityDeck.sOppDeckAllDPs);
			if (DPElements.size() >= 1) {
				refOppurtunityDeck.clickWorkTodoCountLink("");
				if (refSeleniumUtils.is_WebElement_Displayed(refOppurtunityDeck.sToDoCountPopupTitle)
						&& refSeleniumUtils.is_WebElement_Displayed(refOppurtunityDeck.sPopupContent)) {
					refSeleniumUtils.highlightElement(refOppurtunityDeck.sToDoCountPopupTitle);
					refSeleniumUtils.highlightElement(refOppurtunityDeck.sPopupContent);
					// As we are locking on the First DP card in the Deck,store
					// the DPKey no for later validation
					String sDPKey = refSeleniumUtils
							.getTexFfromLocator("(" + refOppurtunityDeck.sOppDeckAllDPs + ")[1]");
					Serenity.setSessionVariable("FirstDPCardkey").to(sDPKey);
				} else {
					Assert.assertTrue("The DP card WorkToDo count Popup is not displayed on clicking on WorkToDoLink",
							false);
				}
			} else {
				Assert.assertTrue(
						"There are no DP Cards in the OpportunityDeck,so cannot proceed further with Scenario execution",
						false);
				getDriver().quit();
			}

		} catch (ElementNotFoundException e) {
			e.printStackTrace();
		}

	}

	@Step
	public void checkNoAssignmentOpportunities() {

		List<WebElement> OppDeckDPsList = new ArrayList<WebElement>();
		List<String> OppDeckAllDPKeysList = new ArrayList<String>();
		List<String> PayersValsList = new ArrayList<String>();

		List<WebElement> OppDeckDPWorkToDoList = new ArrayList<WebElement>();
		List<String> OppDeckWorkToDoCountList = new ArrayList<String>();

		String sOppDeckTitle = refSeleniumUtils.getTexFfromLocator(refOppurtunityDeck.sOppDeckHeaderTitle);

		String sTemp1 = (sOppDeckTitle.split("Available DP\\(s\\)"))[1];
		String sDPCount = sTemp1.substring(2, sTemp1.length() - 1);
		int iDPCount = Integer.valueOf(sDPCount);
		String sWorkToDoCountText = "";
		String[] sWorkToDoArr;
		boolean bDPCardAvailable = false;

		if (iDPCount == 0) {
			Assert.assertTrue("There are no DPs in the OpportunitiesDeck,so cannot proceed further with execution ... ",
					false);
		}

		try {
			OppDeckDPsList = refSeleniumUtils.getElementsList("XPATH", refOppurtunityDeck.sOppDeckAllDPs);
			OppDeckAllDPKeysList = refSeleniumUtils.getWebElementValuesAsList(refOppurtunityDeck.sOppDeckAllDPs);

			OppDeckWorkToDoCountList = refSeleniumUtils
					.getWebElementValuesAsList(refOppurtunityDeck.sAllDPCardsWorkToDoCountLabel);
			OppDeckDPWorkToDoList = refSeleniumUtils.getElementsList("XPATH",
					refOppurtunityDeck.sAllDPCardsWorkToDoCountLabel);

			for (int k = 0; k < OppDeckDPsList.size(); k++) {
				refSeleniumUtils.evaluateJavascript("arguments[0].scrollIntoView(true);", OppDeckDPsList.get(k));
				sWorkToDoCountText = OppDeckWorkToDoCountList.get(k);
				sWorkToDoArr = sWorkToDoCountText.split("/");
				if (Integer.parseInt(sWorkToDoArr[0]) == Integer.parseInt(sWorkToDoArr[1])) {
					refSeleniumUtils.clickGivenWebElement(OppDeckDPWorkToDoList.get(k));
					refSeleniumUtils.highlightElement(OppDeckDPWorkToDoList.get(k));
					refSeleniumUtils.defaultWait(ProjectVariables.TImeout_3_Seconds);
					bDPCardAvailable = true;
					break;
				}
			} // End for

			if (bDPCardAvailable == false) {
				Assert.assertTrue("There are no DP Cards with No assignments Yet for the Selected Clinet", false);
			}

		} catch (Exception e) {
		}

	}

	@Step
	public void validateNoAssignmentforUnassignedPayerLOBSet() {
		List<String> SelectedPayershorts = new ArrayList<String>();
		List<String> SelectedLOBs = new ArrayList<String>();
		List<WebElement> PopupTableRowsList = new ArrayList<WebElement>();

		String sPresXPath1 = "";
		String sPresXPath2 = "";
		String sPresAssignmentVal = "";
		String sColXPath = "";

		List<String> PresAssignmentValsList = new ArrayList<String>();
		List<String> PayersValsList = new ArrayList<String>();

		PresAssignmentValsList = refSeleniumUtils.getWebElementValuesAsList(refOppurtunityDeck.sWorkToDoPresAssgnVals);

		try {
			if (PresAssignmentValsList.size() == 0) {
				Assert.assertTrue("There are no Assignments in the WorkToDo popup Grid,so cannot proceed further..",
						false);
			}

			for (int i = 0; i < PresAssignmentValsList.size(); i++) {
				if (PresAssignmentValsList.get(i).equalsIgnoreCase("No Assignment")) {
					Assert.assertTrue(
							"For the UnAssigned Payer/LOB set the PresentationAssignment  column value in WorkToDoCount Popup grid is--> No Assignment",
							true);
				} else {
					Assert.assertTrue(
							"For the UnAssigned Payer/LOB set the PresentationAssignment  column value in WorkToDoCount Popup grid is  Not--> No Assignment",
							false);
				}
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// Close worktodo Popup
		refSeleniumUtils.clickGivenXpath(refOppurtunityDeck.sWorkToDoPopupCloseBtn);

	}

	@Step
	public void validateWorkToDoGridSorting() {
		List<String> PayersValsList = new ArrayList<String>();

		PayersValsList = refSeleniumUtils.getWebElementValuesAsList(refOppurtunityDeck.sWorkToDoPayerVals);

		if (PayersValsList.size() == 0) {
			Assert.assertTrue("There are no Payers in the WorkToDo popup Grid,so cannot proceed further..", true);
		}

		boolean isSorted = true;
		if (PayersValsList.size() > 1) // if there is more than one Medical
			// Policy then check for Sorting
		{
			for (int i = 0; i < PayersValsList.size() - 1; i++) {
				// Current String is > than the next one (if there are equal
				// list is still sorted)
				if (PayersValsList.get(i).compareToIgnoreCase(PayersValsList.get(i + 1)) > 0) {
					isSorted = false;
					break;
				}
				System.out.println("Payershort::" + PayersValsList.get(i) + "<" + PayersValsList.get(i + 1));
			}
		}
		if (isSorted == true) {
			Assert.assertTrue("All the Payershort in the WorkToDo Popup are Sorted", true);
		} else {
			Assert.assertTrue("All the Payershort in the  in the WorkToDo Popup are Not Sorted", false);
		}
		// Close worktodo Popup
		refSeleniumUtils.clickGivenXpath(refOppurtunityDeck.sWorkToDoPopupCloseBtn);

	}

	@Step
	public void clickWorkToDoLinkOnDPCard(String sArg) {

		List<WebElement> OppDeckDPsList = new ArrayList<WebElement>();
		List<String> OppDeckAllDPKeysList = new ArrayList<String>();
		List<String> PayersValsList = new ArrayList<String>();

		List<WebElement> OppDeckDPWorkToDoList = new ArrayList<WebElement>();
		List<String> OppDeckWorkToDoCountList = new ArrayList<String>();

		String sOppDeckTitle = refSeleniumUtils.getTexFfromLocator(refOppurtunityDeck.sOppDeckHeaderTitle);

		String sTemp1 = (sOppDeckTitle.split("Available DP\\(s\\)"))[1];
		String sDPCount = sTemp1.substring(2, sTemp1.length() - 1);
		int iDPCount = Integer.valueOf(sDPCount);
		String sWorkToDoCountText = "";
		String[] sWorkToDoArr;
		int m = 0;

		if (iDPCount == 0) {
			Assert.assertTrue("There are no DPs in the OpportunitiesDeck,so cannot proceed further with execution ... ",
					false);
		}

		try {
			OppDeckDPsList = refSeleniumUtils.getElementsList("XPATH", refOppurtunityDeck.sOppDeckAllDPs);
			OppDeckAllDPKeysList = refSeleniumUtils.getWebElementValuesAsList(refOppurtunityDeck.sOppDeckAllDPs);

			OppDeckWorkToDoCountList = refSeleniumUtils
					.getWebElementValuesAsList(refOppurtunityDeck.sAllDPCardsWorkToDoCountLabel);
			OppDeckDPWorkToDoList = refSeleniumUtils.getElementsList("XPATH",
					refOppurtunityDeck.sAllDPCardsWorkToDoCountLabel);

			for (int k = 0; k < OppDeckDPsList.size(); k++) {
				refSeleniumUtils.evaluateJavascript("arguments[0].scrollIntoView(true);", OppDeckDPsList.get(k));
				sWorkToDoCountText = OppDeckWorkToDoCountList.get(k);

				refSeleniumUtils.clickGivenWebElement(OppDeckDPWorkToDoList.get(k));
				refSeleniumUtils.defaultWait(ProjectVariables.TImeout_2_Seconds);
				PayersValsList = refSeleniumUtils.getWebElementValuesAsList(refOppurtunityDeck.sWorkToDoPayerVals);
				while (PayersValsList.size() == 1) // If there is only one payer
					// in the Grid,we cannot
					// check sorting,so checking
					// till we find more than
					// one Payer in the Grid
				{
					m = m + 1;
					refSeleniumUtils.Click_given_Locator(refOppurtunityDeck.sWorkToDoPopupCloseBtn);
					refSeleniumUtils.evaluateJavascript("arguments[0].scrollIntoView(true);", OppDeckDPsList.get(m));
					refSeleniumUtils.clickGivenWebElement(OppDeckDPWorkToDoList.get(m));
					refSeleniumUtils.defaultWait(ProjectVariables.TImeout_2_Seconds);
					PayersValsList = refSeleniumUtils.getWebElementValuesAsList(refOppurtunityDeck.sWorkToDoPayerVals);
				}
				break;

			} // End for

		} catch (Exception e) {
		}

	}

	@Step
	public void validateWorkTodoPopupUiElements(String sArg) {

		List<WebElement> LOBsList = new ArrayList<WebElement>();
		List<WebElement> LOBsRowsList = new ArrayList<WebElement>();

		// Validate DPKey present
		String sDPKey = Serenity.sessionVariableCalled("FirstDPCardkey");
		if (refSeleniumUtils.getTexFfromLocator(refOppurtunityDeck.sToDoCountPopupTitle).contains(sDPKey)) {
			Assert.assertTrue("The DPKey is displayed in the WorkToDo count Popup header", true);
		} else {
			Assert.assertTrue("The DPKey is displayed in the WorkToDo count Popup header", false);
		}

		// Validate WorkToDo label present
		if (refSeleniumUtils.getTexFfromLocator(refOppurtunityDeck.sToDoCountPopupTitle)
				.contains("Opportunities To Work")) {
			Assert.assertTrue("The :: Opportunities to work ::label  is displayed in the WorkToDo count Popup header",
					true);
		} else {
			Assert.assertTrue(
					"The  ::Opportunities to work:: label is not displayed in the WorkToDo count Popup header", false);
		}

		// Validate LOBs displayed in separate rows
		try {
			LOBsList = refSeleniumUtils.getElementsList("XPATH", refOppurtunityDeck.sAllLOBs);
			LOBsRowsList = refSeleniumUtils.getElementsList("XPATH", refOppurtunityDeck.sAllLOBsRows);

			if (LOBsList.size() == LOBsRowsList.size()) {
				Assert.assertTrue("The LOBs displayed in separate rows in the WorkToDoCount Grid", true);
			} else {
				Assert.assertTrue("The LOBs are not  displayed in separate rows in the WorkToDoCount Grid", false);
			}

			// Validate Assignments displayed in separate rows
			LOBsList = refSeleniumUtils.getElementsList("XPATH", refOppurtunityDeck.sWorkToDoPresAssgnVals);
			LOBsRowsList = refSeleniumUtils.getElementsList("XPATH", refOppurtunityDeck.sWorkToDoPresAssgnValsRows);
			if (LOBsList.size() == LOBsRowsList.size()) {
				Assert.assertTrue("The Assignment Values displayed in separate rows in the WorkToDoCount Grid", true);
			} else {
				Assert.assertTrue(
						"The Assignment Values  are not  displayed in separate rows in the WorkToDoCount Grid", false);
			}

			// Close the Pop up
			if (refSeleniumUtils.is_WebElement_Displayed(refOppurtunityDeck.sWorkToDoPopupCloseBtn)) {
				refSeleniumUtils.Click_given_Locator(refOppurtunityDeck.sWorkToDoPopupCloseBtn);
			} else {
				refSeleniumUtils.Click_given_Locator(refOppurtunityDeck.sWorkToDoPopupCloseBtn1);
			}
		}

		catch (ElementNotFoundException e) {

			e.printStackTrace();
		}

	}

	@Step
	public void assignMultiplePresentaionstoDP(String sArg1, String sPresCount) throws ElementNotFoundException {

		List<String> PresNamesList = new ArrayList<String>();
		PresNamesList = Serenity.sessionVariableCalled("PresentationNamesList");
		List<String> AssignedPresNamesList = new ArrayList<String>();

		List<WebElement> DPAssignmentBtns = new ArrayList<WebElement>();
		DPAssignmentBtns = refSeleniumUtils.getElementsList("XPATH", refOppurtunityDeck.sDPAssignmentBtns);
		String sAssignChkBox = "";

		// Capture the DPNos before assigning for verification
		List<String> OppDeckDPsVals = new ArrayList();
		OppDeckDPsVals = refSeleniumUtils.getWebElementValuesAsList(refOppurtunityDeck.sOppDeckAllDPs);
		Serenity.setSessionVariable("OppDeckDpValues").to(OppDeckDPsVals);
		List<WebElement> AssignIcons = new ArrayList();

		if (PresNamesList.size() == 0) {
			Assert.assertTrue("There are no PresentationProfiles created to proceed further..", false);
		}

		try {
			// Click on the first DPCard Assignment Button
			refSeleniumUtils.clickOn(DPAssignmentBtns.get(0));

			for (int m = 0; m < PresNamesList.size(); m++) {
				sAssignChkBox = StringUtils.replace(refOppurtunityDeck.sPresentationAssignmentCheckbox, "val",
						PresNamesList.get(m));
				refSeleniumUtils.Click_given_Locator(sAssignChkBox);
				AssignedPresNamesList.add(PresNamesList.get(m));
				Serenity.setSessionVariable("AssignedPresentationsNames").to(AssignedPresNamesList);
			}
			// Click on OK Button in the Assignment Pop up
			refSeleniumUtils.Click_given_Locator(refOppurtunityDeck.sPresentationAssignmentOKBtn);

		}

		catch (Exception e) {
			System.out.println("Exception Message::" + e.getMessage());
			getDriver().quit();
		}

	}

	@Step
	public void validatePresNamesAssignedinWorktoDoPopup() {

		List<String> AssignedPresNamesList = new ArrayList<String>();
		AssignedPresNamesList = Serenity.sessionVariableCalled("AssignedPresentationsNames");

		List<String> WorkToDoPopupPresAssignmentValsList = new ArrayList<String>();

		// Click on the FirstDp card WorkToDo assignment link
		refOppurtunityDeck.clickWorkTodoCountLink("");

		WorkToDoPopupPresAssignmentValsList = refSeleniumUtils
				.getWebElementValuesAsList(refOppurtunityDeck.sWorkToDoPresAssgnVals);
		if (WorkToDoPopupPresAssignmentValsList.size() == 0) {
			Assert.assertTrue(
					"There are no Assignments with Assigned Presentation names in the WorkToDo popup Grid  after we have done assignments",
					false);
		}

		// Flag to check whether Presentation name displayed in the WorktoDo
		// popup
		boolean bPresentationFlag = false;

		// Comparing the Assigned presentation names with the WorktoDo popup
		// names to check if they are available.
		try {
			for (int i = 0; i < AssignedPresNamesList.size(); i++) {
				bPresentationFlag = false;
				for (int j = 0; j < WorkToDoPopupPresAssignmentValsList.size(); j++) {
					if (AssignedPresNamesList.get(i).equalsIgnoreCase(WorkToDoPopupPresAssignmentValsList.get(j))) {
						bPresentationFlag = true;
						break;
					}
				}
			}

		} catch (Exception e) {
			System.out.println(e.getMessage());
		}

		// Close worktodo Popup
		refSeleniumUtils.clickGivenXpath(refOppurtunityDeck.sWorkToDoPopupCloseBtn1);

		if (bPresentationFlag == false) {
			Assert.assertTrue(
					"There are no Assignments with Assigned Presentation names in the WorkToDo popup Grid  after we have done assignments",
					false);
		} else {
			Assert.assertTrue(
					"There   are Assignments with Assigned Presentation names displayed in the WorkToDo popup Grid  after we have done assignments",
					true);
		}

	}

	@Step
	public void validateWorkToDoGridPayersLOBs(String sPayershorts, String sLOBs) {
		List<String> UISelectedpayershorts = new ArrayList<String>();
		List<String> UISelectedLOBs = new ArrayList<String>();
		List<String> PayersValsList = new ArrayList<String>();
		List<String> LOBsValsList = new ArrayList<String>();

		Map<String, List<String>> DPKeyWTDPayershortsUI = new HashMap<String, List<String>>();
		Map<String, List<String>> DPKeyWTDLOBsUI = new HashMap<String, List<String>>();

		List<WebElement> DPElements = new ArrayList<WebElement>();
		List<WebElement> OppDeckDPWorkToDoElmntsList = new ArrayList<WebElement>();
		int iDPCounter = 0;

		// Retrieve the Payershort List
		// Retrieve LOBs List
		UISelectedpayershorts = Serenity.sessionVariableCalled("SelectedPayerShorts");
		UISelectedLOBs = Serenity.sessionVariableCalled("SelectedLOBs");

		boolean bPayershortFlag = false;
		boolean bLOBFlag = false;

		refSeleniumUtils.waitForElement(refOppurtunityDeck.sOppDeckAllDPs, "shouldBevisible", 4);

		try {
			DPElements = refSeleniumUtils.getElementsList("XPATH", refOppurtunityDeck.sOppDeckAllDPs);
		} catch (ElementNotFoundException e) {
			e.printStackTrace();
		}
		List<String> OppDeckDPsVals = refSeleniumUtils.getWebElementValuesAsList(refOppurtunityDeck.sOppDeckAllDPs);
		if (DPElements.size() > 6) {
			iDPCounter = 2;
		} else {
			iDPCounter = DPElements.size();
		}

		try {
			OppDeckDPWorkToDoElmntsList = refSeleniumUtils.getElementsList("XPATH",
					refOppurtunityDeck.sAllDPCardsWorkToDoCountLabel);
		} catch (ElementNotFoundException e) {
			e.printStackTrace();
		}

		// Loop through the each DPcard and click on the WorkToDo link
		for (int k = 0; k <= iDPCounter; k++) {
			refSeleniumUtils.highlightElement(OppDeckDPWorkToDoElmntsList.get(k));
			String sDPKey = OppDeckDPsVals.get(k);
			refSeleniumUtils.clickGivenWebElement(OppDeckDPWorkToDoElmntsList.get(k));
			refSeleniumUtils.defaultWait(ProjectVariables.TImeout_2_Seconds);

			// Get Payers and LObs List from Grid
			PayersValsList = refSeleniumUtils.getWebElementValuesAsList(refOppurtunityDeck.sWorkToDoPayerVals);
			LOBsValsList = refSeleniumUtils.getWebElementValuesAsList(refOppurtunityDeck.sAllLOBs);

			DPKeyWTDPayershortsUI.put(sDPKey, PayersValsList);
			DPKeyWTDLOBsUI.put(sDPKey, LOBsValsList);

			// Loop through the Payers list in the Grid and check whether each
			// payer is in the one of the Filtered Payershorts
			for (int i = 0; i < PayersValsList.size(); i++) {
				bPayershortFlag = false;
				for (int j = 0; j < UISelectedpayershorts.size(); j++) {
					if (PayersValsList.get(i).equalsIgnoreCase(UISelectedpayershorts.get(j))) {
						bPayershortFlag = true;
						break;
					}
				}
				if (bPayershortFlag == false) {
					Assert.assertTrue("The WorkToDo Grid Payershort::" + PayersValsList.get(i)
					+ " not in the payershorts from FilterSection", false);
				}
			}

			if (bPayershortFlag == true) {
				Assert.assertTrue("The WorkToDo Grid Payershorts are  in the payershorts from FilterSection", true);
			}

			// Loop through the LOBs list in the Grid and check whether each
			// payer is in the one of the Filtered LOBs
			for (int i = 0; i < LOBsValsList.size(); i++) {
				bLOBFlag = false;
				for (int j = 0; j < UISelectedLOBs.size(); j++) {
					if (LOBsValsList.get(i).equalsIgnoreCase(UISelectedLOBs.get(j))) {
						bLOBFlag = true;
						break;
					}
				}
				if (bLOBFlag == false) {
					Assert.assertTrue(
							"The WorkToDo Grid LOB::" + LOBsValsList.get(i) + " not in the LOBs from FilterSection",
							false);
				}
			}

			if (bLOBFlag == true) {
				Assert.assertTrue("The WorkToDo Grid LOBs are  in the LOBs from FilterSection", true);
			}

			// Close the opened Popup
			refSeleniumUtils.Click_given_Locator(refOppurtunityDeck.sWorkToDoPopupCloseBtn);
			refSeleniumUtils.defaultWait(ProjectVariables.TImeout_2_Seconds);

		} // DP Card for loop

		Serenity.setSessionVariable("DPKeyWTDPayershortsMap").to(DPKeyWTDPayershortsUI);
		Serenity.setSessionVariable("DPKeyWTDLOBsMap").to(DPKeyWTDLOBsUI);
	}

	@Step
	public void validateWorkToDoGridDataWithMongoDB(String sPayershorts, String sLOBs, String sDPKey) {

		List<String> UISelectedpayershorts = new ArrayList<String>();
		List<String> UISelectedLOBs = new ArrayList<String>();
		List<String> PayersValsList = new ArrayList<String>();
		List<String> LOBsValsList = new ArrayList<String>();
		List<String> DBPayershortsList = new ArrayList<String>();
		List<String> DBLOBsList = new ArrayList<String>();
		List<String> UIPayers = new ArrayList<String>();
		List<String> UILOBs = new ArrayList<String>();

		Map<String, List<String>> DPKeyWTDPayershortsUI = new HashMap<String, List<String>>();
		Map<String, List<String>> DPKeyWTDLOBsUI = new HashMap<String, List<String>>();
		List<WebElement> OppDeckDPWorkToDoElmntsList = new ArrayList<WebElement>();
		List<WebElement> DPElements = new ArrayList<WebElement>();

		DPKeyWTDPayershortsUI = Serenity.sessionVariableCalled("DPKeyWTDPayershortsMap");
		DPKeyWTDLOBsUI = Serenity.sessionVariableCalled("DPKeyWTDLOBsMap");

		Set<String> DPkeysSet = DPKeyWTDPayershortsUI.keySet();

		try {
			DPElements = refSeleniumUtils.getElementsList("XPATH", refOppurtunityDeck.sOppDeckAllDPs);
		} catch (ElementNotFoundException e) {
			e.printStackTrace();
		}
		List<String> OppDeckDPkeysList = refSeleniumUtils.getWebElementValuesAsList(refOppurtunityDeck.sOppDeckAllDPs);
		try {
			OppDeckDPWorkToDoElmntsList = refSeleniumUtils.getElementsList("XPATH",
					refOppurtunityDeck.sAllDPCardsWorkToDoCountLabel);
		} catch (ElementNotFoundException e) {
			e.printStackTrace();
		}

		String sDPkeyDescrUI = "";
		String sDPkeyDescrDB = "";
		String sDPKeyNo = " ";
		Set<String> hSetPayersUI;
		Set<String> hSetPayersDB;
		Set<String> hSetLOBsUI;
		Set<String> hSetLObsDB;
		boolean bPayersUIDB = false;
		boolean bLOBsUIDB = false;
		boolean bRecordExistsiDB = false;

		// Get Client Name
		String sClientName = Serenity.sessionVariableCalled("SelectClientName");

		for (int m = 0; m < DPkeysSet.size(); m++) {
			refSeleniumUtils.clickGivenWebElement(OppDeckDPWorkToDoElmntsList.get(m));
			refSeleniumUtils.defaultWait(ProjectVariables.TImeout_2_Seconds);
			UIPayers = DPKeyWTDPayershortsUI.get(OppDeckDPkeysList.get(m));
			sDPKeyNo = ((OppDeckDPkeysList.get(m).split("DP"))[1]).trim(); // Split
			// the
			// DP
			// value
			// by
			// -DP-
			// so
			// that
			// to
			// pass
			// the
			// DPNo
			// to
			// MongoDB

			for (int n = 0; n < UIPayers.size(); n++) {
				bRecordExistsiDB = false;
				String sLOBXPath = StringUtil.replace(refOppurtunityDeck.sLOBForPS, "PSArg", UIPayers.get(n));
				String sLOB = refSeleniumUtils.get_TextFrom_Locator(sLOBXPath);
				String sClientDesc = MongoDBUtils.validateDPKeyPSLOBExistsinDB(sDPKeyNo, UIPayers.get(n), sLOB, "",
						"");

				if (sClientDesc.equalsIgnoreCase(sClientName)) {
					bRecordExistsiDB = true;
					System.out.println("Payershort:: " + UIPayers.get(n) + " and LOB::" + sLOB
							+ " combination of WTD popup is same as DB for DP Key::" + sDPKeyNo);
					Assert.assertTrue("Payershort:: " + UIPayers.get(n) + " and LOB::" + sLOB
							+ " combination of WTD popup is Not  same as DB for DP Key::" + sDPKeyNo, true);
				} else {
					Assert.assertTrue("Payershort:: " + UIPayers.get(n) + " and LOB::" + sLOB
							+ " combination of WTD popup is Not  same as DB for DP Key::" + sDPKeyNo, false);
				}
			}

			// Close the opened Popup
			refSeleniumUtils.Click_given_Locator(refOppurtunityDeck.sWorkToDoPopupCloseBtn);
			refSeleniumUtils.defaultWait(ProjectVariables.TImeout_2_Seconds);

		} // End of DP Key for loop

		if (bRecordExistsiDB == true) {
			Assert.assertTrue("All Payer LOB records exists in DB", true);
		} else {
			Assert.assertTrue("All Payer LOB records dont  exists in DB", false);
		}
	}

	@Step
	public void selectAssignIconAtLevel(String sAssignLevel) throws ElementNotFoundException {
		List<WebElement> MedPolicyAssignIcons = new ArrayList();
		List<WebElement> TopicAssignIcons = new ArrayList();
		List<WebElement> DPAssignIcons = new ArrayList();
		List<String> MedicalPolicyTitles = new ArrayList();
		List<String> TopicTitles = new ArrayList();
		List<WebElement> DPCards = new ArrayList();
		String MedPolicy = "";
		String Topic = "";
		WebElement DP;
		String[] TempStr;
		String TempStr2 = "";
		String sDPCount = "";
		String sTemp = "";
		int count = 0;
		try {

			switch (sAssignLevel) {

			case "HeaderLevel":
				refSeleniumUtils.clickGivenXpath(refOppurtunityDeck.HeaderLevelAssignIcon);
				break;

			case "MedicalPolicyLevel":
				// Retrieve DPs count in the First Medical Policy
				MedicalPolicyTitles = refSeleniumUtils.getWebElementValuesAsList(refOppurtunityDeck.AllMedPolicies);
				MedPolicy = MedicalPolicyTitles.get(0);// Get the First Medical
				// Policy value
				TempStr2 = MedPolicy.split("in")[0]; // Split by "in" word Ex:
				// "Medical Policy:
				// Anesthesia Policy
				// (3561 DP(s) in 8
				// Topic(s))";
				// sTemp = (TempStr2.split("\\("))[1]; //Split by left
				// Parathesis
				// sDPCount = (sTemp.split("DP"))[0].trim(); //Split by text
				// "DP" to get the number of DPs

				// Store the value
				// Serenity.setSessionVariable("DPCount").to(sDPCount);
				// Serenity.setSessionVariable("MedPolicyDPCount").to(sDPCount);
				// ]

				MedPolicyAssignIcons = refSeleniumUtils.getElementsList("XPATH",
						refOppurtunityDeck.sMedPolicyAssignmentIcons);
				refSeleniumUtils.highlightElement(MedPolicyAssignIcons.get(0));
				refSeleniumUtils.clickGivenWebElement(MedPolicyAssignIcons.get(0));
				// refSeleniumUtils.clickOn(MedPolicyAssignIcons.get(0));
				// //Click on the First MedicalPolicy assignment Icon
				refSeleniumUtils.defaultWait(ProjectVariables.TImeout_2_Seconds);

				break;

			case "TopicLevel":
				// Retrieve DPs count in the First Topic
				TopicTitles = refSeleniumUtils.getWebElementValuesAsList(refOppurtunityDeck.AllTopics);
				Topic = TopicTitles.get(0);

				String temp1 = Topic.split("DP")[0]; // Split by "DP" text Ex:
				// Topic: Abatacept
				// (J0129) (200 DP(s))"
				for (int k = 0; k < temp1.length(); k++) // Capture the count of
					// Left paranthesis
				{
					char c = temp1.charAt(k);
					if (c == '(') {
						count = count + 1;
					}
				}
				sDPCount = (temp1.split("\\("))[count]; // Capture the DP Count
				// based on paranthesis
				// count Ex: 200

				// Store the value
				Serenity.setSessionVariable("DPCount").to(sDPCount.trim());
				Serenity.setSessionVariable("TopicDPCount").to(sDPCount.trim());
				TopicAssignIcons = refSeleniumUtils.getElementsList("XPATH", refOppurtunityDeck.sTopicAssignmentBtns);
				refSeleniumUtils.highlightElement(TopicAssignIcons.get(0));
				refSeleniumUtils.clickOn(TopicAssignIcons.get(0)); // Click on
				// the First
				// Topic
				// assignment
				// Icon
				refSeleniumUtils.defaultWait(ProjectVariables.TImeout_2_Seconds);

				break;

			case "DPLevel":
				// If it is DP Level assignment the count is one as only one DP
				// is assigned,so always 1 is the count
				Serenity.setSessionVariable("DPCount").to("1");
				refSeleniumUtils.defaultWait(ProjectVariables.TImeout_3_Seconds);
				String DPKey = refSeleniumUtils.getTexFfromLocator(refOppurtunityDeck.DPKeyXpath).trim();
				Serenity.setSessionVariable("CapturedDPKey").to(DPKey);
				List<WebElement> DPKeys = refSeleniumUtils.getElementsList("XPATH", refOppurtunityDeck.DPKeyXpath);
				refSeleniumUtils.clickOn(DPKeys.get(0));
				String sALL = "//label[contains(text(),'ALL')]//span";
				refSeleniumUtils.Click_given_Xpath(sALL);
				String AssignIcon = StringUtils.replace(refOppurtunityDeck.DPAssignArrow, "DPKeyArg", DPKey);
				oGenericUtils.clickOnElement(AssignIcon);
				String DPAssignBtnXPath = "//span[contains(text(),'DP')]/../..//button";
				//refSeleniumUtils.Click_given_Xpath(DPAssignBtnXPath);

				break;
			}
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}

	}

	@Step
	public void validateAssignPopupPresenationNames(String sPlaceholderArg) {

		List<String> CreatedPresNamesList = new ArrayList<String>();
		CreatedPresNamesList = Serenity.sessionVariableCalled("PresentationNamesList");
		List<String> AssignedPopupPresNamesList = new ArrayList<String>();

		AssignedPopupPresNamesList = refSeleniumUtils
				.getWebElementValuesAsList(refOppurtunityDeck.AssignPopupPresNames);

		List<WebElement> AssignIcons = new ArrayList();

		if (CreatedPresNamesList.size() == 0) {
			Assert.assertTrue("There are no PresentationProfiles created to proceed further..", false);
		}

		if (AssignedPopupPresNamesList.size() == 0) {
			Assert.assertTrue(
					"There are no PresentationProfiles names in the Assigned To popup,cannot proceed further..", false);
			getDriver().quit();
		}

		boolean bProfilePresent = false;
		String sProfileNotPresent = "";

		try {
			for (int m = 0; m < CreatedPresNamesList.size(); m++) {
				bProfilePresent = false;
				for (int n = 0; n < AssignedPopupPresNamesList.size(); n++) {
					if (CreatedPresNamesList.get(m).equalsIgnoreCase(AssignedPopupPresNamesList.get(n))) {
						bProfilePresent = true;
					} else {
						sProfileNotPresent = CreatedPresNamesList.get(m);
					}
				}

				if (bProfilePresent == false) {
					break;
				}
			} // End for

			if (bProfilePresent == false) {
				Assert.assertTrue(
						"PresentationProfiles name:" + sProfileNotPresent + " not available  in the Assigned To popup",
						false);
				getDriver().quit();
			} else {
				Assert.assertTrue("PresentationProfiles names available  in the Assigned To popup", true);
			}

		}

		catch (Exception e) {
			System.out.println("Exception Message::" + e.getMessage());
			getDriver().quit();
		}

	}

	@Step
	public void assignDPToPresentation(String sPresCount, String sAssignLevel) {
		try {
			selectAssignIconAtLevel(sAssignLevel);
		} catch (ElementNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		List<String> PresNamesToAssign = new ArrayList<String>();
		List<String> AssignedPresNamesList = new ArrayList<String>();
		String sPresName = "";
		String sAssignChkBox = "";
		int iLoopCounter = -1;
		Map<String, String> PresProfileDPMap = new HashMap<String, String>();
		switch (sPresCount) {

		case "Single":
			sPresName = Serenity.sessionVariableCalled("PresentationName");
			PresNamesToAssign.add(sPresName);
			iLoopCounter = 1;
			break;

		case "Multiple":
			PresNamesToAssign = Serenity.sessionVariableCalled("PresentationNamesList");
			iLoopCounter = PresNamesToAssign.size();
			break;
		}

		try {
			for (int m = 0; m < iLoopCounter; m++) {

				// Get the presentation name index and click checkbox against
				// it--add code
				sAssignChkBox = StringUtils.replace(refOppurtunityDeck.sPresentationAssignmentCheckbox, "val",
						PresNamesToAssign.get(m));
				refSeleniumUtils.Click_given_Locator(sAssignChkBox);
				AssignedPresNamesList.add(PresNamesToAssign.get(m));
				String sDPCount = Serenity.sessionVariableCalled("DPCount");
				PresProfileDPMap.put(PresNamesToAssign.get(m), sDPCount);

			}
			Serenity.setSessionVariable("AssignedPresentationsNames").to(AssignedPresNamesList);
			Serenity.setSessionVariable("PresProfileDPMap").to(PresProfileDPMap);

		} catch (Exception e) {
			System.out.println("Exception Message::" + e.getMessage());
			getDriver().quit();
		}
	}

	@Step
	public void validateAssignPopupClosed() {
		boolean bFlag;
		bFlag = refSeleniumUtils.is_WebElement_Displayed(refOppurtunityDeck.AssignOppsPopup);
		Assert.assertEquals("Assign opportunities Popup  should be closed after cliking on OK button", false, bFlag);
	}

	@Step
	public void assignMultiplePresentations(String sPresCount, String sLevel, String sSaveDPAssignments) {
		List<String> PresNamesList = new ArrayList<String>();
		PresNamesList = Serenity.sessionVariableCalled("PresentationNamesList");
		List<String> AssignedPresNamesList = new ArrayList<String>();
		Map<String, String> PresProfileDPMap = new HashMap<String, String>();
		String sAssignChkBox = "";

		if (PresNamesList.size() == 0) {
			Assert.assertTrue("There are no PresentationProfiles created to proceed further..", false);
		}

		try {
			for (int m = 0; m < PresNamesList.size(); m++) {
				sAssignChkBox = StringUtils.replace(refOppurtunityDeck.sPresentationAssignmentCheckbox, "val",
						PresNamesList.get(m));
				refSeleniumUtils.Click_given_Locator(sAssignChkBox);
				AssignedPresNamesList.add(PresNamesList.get(m));
				Serenity.setSessionVariable("AssignedPresentationsNames").to(AssignedPresNamesList);

				// Store the PresentationProfileName to DP Mapping in HashMap
				if (!sSaveDPAssignments.isEmpty()) {
					PresProfileDPMap.put(PresNamesList.get(m), Serenity.sessionVariableCalled("DPCount"));
					Serenity.setSessionVariable("PresProfileDPMap").to(PresProfileDPMap);
				}
			}

		} catch (Exception e) {
			System.out.println("Exception Message::" + e.getMessage());
			getDriver().quit();
		}

	}

	@Step
	public void clickAssignPopupButton(String sBtnName) {
		refSeleniumUtils.defaultWait(ProjectVariables.TImeout_5_Seconds);
		Assert.assertEquals(refOppurtunityDeck.AssignPopupBtnclick(sBtnName), true);
		refSeleniumUtils.waitForContentLoad();

	}

	@Step
	public void clickAssignPopupButton(String arg1, String arg2, String arg3) {
		String sDPKey = Serenity.sessionVariableCalled("CapturedDPKey");

		String DPKeyXpath = StringUtils.replace(refOppurtunityDeck.DPKeyDynamicXpath, "DPKeyArg", sDPKey);
		refSeleniumUtils.Click_given_Xpath(DPKeyXpath);
		String sALL = "//label[contains(text(),'ALL')]//span";
		refSeleniumUtils.Click_given_Xpath(sALL);
		String DPAssignBtnXPath = "//span[contains(text(),'DP')]/../..//button";
		refSeleniumUtils.Click_given_Xpath(DPAssignBtnXPath);
	}

	@Step
	public void validateAssignPopupState(String arg1, String sChkBoxState) {

		// Get the previous Presentation profile names
		// if it is unselected,then state not retained after clicking cancel

		List<String> PresentaionsAssignedTo = new ArrayList<String>();
		PresentaionsAssignedTo = Serenity.sessionVariableCalled("AssignedPresentationsNames");

		switch (sChkBoxState) {

		case "Unselected":
			// Search for the PresName in the Presentations Panel and retrieve
			// the Count
			for (int k = 0; k < PresentaionsAssignedTo.size(); k++) {
				String sAssignChkBox = StringUtils.replace(refOppurtunityDeck.PresProfileChkBoxNotChecked,
						"ProfileNameArg", PresentaionsAssignedTo.get(k));

				if (refSeleniumUtils.is_WebElement_Displayed(sAssignChkBox)) {
					Assert.assertTrue("There Checkboxes state is deselected after Popup Cancel ", true);
				} else {
					Assert.assertTrue("There Checkboxes state is deselected after Popup Cancel ", false);
				}

			}
			break;

		case "PartiallySelected":

			for (int k = 0; k < PresentaionsAssignedTo.size(); k++) {
				String sAssignChkBox = StringUtils.replace(refOppurtunityDeck.PresProfileChkBoxPartiallySelected,
						"ProfileNameArg", PresentaionsAssignedTo.get(k));

				if (refSeleniumUtils.is_WebElement_Displayed(sAssignChkBox)) {
					refSeleniumUtils.highlightElement(sAssignChkBox);
					Assert.assertTrue(
							"There Checkboxes state is -PartiallySelected- as some partial assignments have been done ",
							true);
				} else {
					Assert.assertTrue(
							"There Checkboxes state is -PartiallySelected- as some partial assignments have been done",
							false);
				}

			}
			break;

		}

	}

	@Step
	public void assignUnassignPopupOperation(String sAssignorUnassign) {
		String sPresName = "";
		// Identify the Check box which is Selected take presentation name for
		// that and store the name
		List<WebElement> AssignIcons = new ArrayList<WebElement>();
		try {
			AssignIcons = refSeleniumUtils.getElementsList("XPATH", refOppurtunityDeck.AssignPopupVisibleCheckboxes);
		} catch (Exception e) {
		}

		boolean bPresentationExist = false;

		for (int k = 0; k < AssignIcons.size(); k++) {
			String checkedval = refSeleniumUtils.getWebElementAttributeVal("checked", AssignIcons.get(k));
			if (checkedval.equalsIgnoreCase("true")) {
				bPresentationExist = true;
				// Get the Presentation Name
				sPresName = refSeleniumUtils.get_TextFrom_Locator(refOppurtunityDeck.AssignPopupVisibleCheckboxes
						+ refOppurtunityDeck.AssignPopupPresNamesPartialXpath).trim();
				String sAssignChkBox = StringUtils.replace(refOppurtunityDeck.sPresentationAssignmentCheckbox, "val",
						sPresName);
				refSeleniumUtils.Click_given_Locator(sAssignChkBox);
				break;
			}
		}

		if (bPresentationExist == false) {
			Assert.assertTrue(
					"There are no Presentations in the Assign Popup which are already selected and visible on the window",
					false);
		}

		// Store Presentation in Session variable
		Serenity.setSessionVariable("DeSelectedPresentation").to(sPresName);
	}

	@Step
	public void validatePopupStateRetained() {
		// Retrieve the Presentation name from Storage
		String PresName = Serenity.sessionVariableCalled("DeSelectedPresentation");
		String sAssignChkBox = StringUtils.replace(refOppurtunityDeck.sPresentationAssignmentCheckbox, "val", PresName);
		// validate the Check box state is checked
		String checkedval = refSeleniumUtils.Get_Value_By_given_attribute("checked", PresName);
		if (checkedval.equalsIgnoreCase("true")) {
			Assert.assertTrue(
					"In the AssignmentPopup Selected Checbox state retained as expected after we deselect the checkbox and click Cancel button on the Popup ",
					true);
		} else {
			Assert.assertTrue(
					"In the AssignmentPopup Selected Checbox state not retained after we deselect the checkbox and click Cancel button on the Popup ",
					false);
		}
	}

	@Step
	public void validateDPCardState(String sPlaceHolderArg, String sDPCardExpectedState) {
		List<String> DPKeysVals = new ArrayList<String>();
		String DPKey = "";

		// If more than one DPKeys are there then Store them in List else store
		// in String
		if ((Serenity.sessionVariableCalled("CapturedDPKey")) instanceof ArrayList) {
			DPKeysVals = Serenity.sessionVariableCalled("CapturedDPKey");
		} else {
			DPKey = Serenity.sessionVariableCalled("CapturedDPKey");
			DPKeysVals.add(DPKey);
		}

		// Loop through the DPCards to check the Flip status
		for (int k = 0; k < DPKeysVals.size(); k++) {
			String DPCard = StringUtils.replace(refOppurtunityDeck.DPCardFlip, "DPKeyArg", DPKeysVals.get(k).trim());
			String bFlipFlag = refSeleniumUtils.Get_Value_By_given_attribute("class", DPCard);

			switch (sDPCardExpectedState) {

			case "AssignedView": // Flipped side
				if (bFlipFlag.equalsIgnoreCase("flipper rotate")) {
					Assert.assertTrue(
							"The DPCard flipped when the work to do count equals zero (0) available opportunities for a DP card (all opportunities have been assigned to a presentations) ",
							true);
				} else {
					Assert.assertTrue(
							"The DPCard not  flipped when the work to do count equals zero (0) available opportunities for a DP card (all opportunities have been assigned to a presentations) ",
							false);
					getDriver().quit();
				}
				break;

			case "SummaryView": // Front side
				if (bFlipFlag.equalsIgnoreCase("flipper")) // if the DPCard is
					// not Flipped
				{
					Assert.assertTrue("The DPCard in SummaryView State ", true);
				} else {
					Assert.assertTrue("The DPCard not in SummaryView State", false);
					getDriver().quit();
				}
				break;

			default:
				Assert.assertTrue("No value provided for Switch case from Gherkin", false);

			}

		}
	}

	@Step
	public void validatePresNamesOnDPCard(String sPlaceholderArg1, String sPresentations, String sPlaceholderArg2) {
		List<String> AssignedPresProflsToDP = new ArrayList<String>();
		List<String> AssignedPresProflsDB = new ArrayList<String>();
		List<String> DPKeysVals = new ArrayList<String>();

		DPKeysVals = Serenity.sessionVariableCalled("CapturedDPKey");
		String PresentationsXpath = StringUtils.replace(refOppurtunityDeck.AssignedPresentationsforDPCard, "DPKeyArg",
				DPKeysVals.get(1));
		AssignedPresProflsToDP = refSeleniumUtils.getWebElementValuesAsList(PresentationsXpath);

		// Get Client Name
		String sClientName = Serenity.sessionVariableCalled("SelectClientName");
		String sClientKey = MongoDBUtils.Retrieve_the_PayerKey_From_mongodb_with_the_given_Payershort(sClientName,
				"Client");
		DPKeysVals = Serenity.sessionVariableCalled("CapturedDPKey");
		AssignedPresProflsDB = MongoDBUtils.getPresentationProfileNamesForDPKey(sClientKey, DPKeysVals.get(1), "",
				"", "");

		if (AssignedPresProflsToDP.size() == AssignedPresProflsDB.size()) {
			Assert.assertTrue("DP Card Assignments count UI and DB are same ", true);

		} else {
			Assert.assertTrue("DP Card Assignments count UI and DB are not same ", false);
			getDriver().quit();
		}

		// compare assignment values with DB and UI
		if (AssignedPresProflsDB.equals(AssignedPresProflsToDP)) {
			Assert.assertTrue("All the PresentationNames assigned to DPCard displayed in the Assignment View ", true);
		} else {
			Assert.assertTrue("All the PresentationNames assigned to DPCard  not displayed in the Assignment View ",
					false);
			getDriver().quit();
		}
	}

	@Step
	public void ValidateDPCardView(String sCardType, String sViewName) {
		List<String> DPKeysVals = new ArrayList<String>();
		String DPKey = " ";
		String DPCard = "";

		if ((Serenity.sessionVariableCalled("CapturedDPKey")) instanceof ArrayList) {
			DPKeysVals = Serenity.sessionVariableCalled("CapturedDPKey");
			DPCard = StringUtils.replace(refOppurtunityDeck.DPCardFlip, "DPKeyArg", DPKeysVals.get(0));
		} else {
			DPKey = Serenity.sessionVariableCalled("CapturedDPKey");
			DPCard = StringUtils.replace(refOppurtunityDeck.DPCardFlip, "DPKeyArg", DPKey);
		}

		refSeleniumUtils.clickGivenXpath(DPCard);
		this.validateDPCardState("", sViewName);

	}

	@Step
	public void clickOnDPCard(String sDPKey, String sView) {
		String sDPKey1 = Serenity.sessionVariableCalled("CapturedDPKey");
		String DPCard = StringUtils.replace(refOppurtunityDeck.DPCardFlip, "DPKeyArg", sDPKey1);
		refSeleniumUtils.clickGivenXpath(DPCard);

	}

	@Step
	public void checkandClickDPBasedOnWTDCount(String sPlaceHolderArg, String WTDCount) {
		List<WebElement> OppDeckDPsList = new ArrayList<WebElement>();
		List<String> OppDeckAllDPKeysList = new ArrayList<String>();
		List<String> PayersValsList = new ArrayList<String>();

		List<WebElement> DPKeysElmnts = new ArrayList<WebElement>();
		List<String> CapturedDPKeysVals = new ArrayList<String>();

		List<WebElement> OppDeckDPWorkToDoList = new ArrayList<WebElement>();
		List<String> OppDeckWorkToDoCountList = new ArrayList<String>();

		boolean bDPCardAvailable = false;

		try {
			OppDeckDPsList = refSeleniumUtils.getElementsList("XPATH", refOppurtunityDeck.sOppDeckAllDPs);
			OppDeckAllDPKeysList = refSeleniumUtils.getWebElementValuesAsList(refOppurtunityDeck.sOppDeckAllDPs);

			int iloopCounter = 0;
			if (OppDeckDPsList.size() >= 3) {
				iloopCounter = 3;
			} else {
				iloopCounter = OppDeckDPsList.size();
			}

			for (int k = 0; k < iloopCounter; k++) {
				String DPKey = OppDeckAllDPKeysList.get(k);
				CapturedDPKeysVals.add(DPKey);
				Serenity.setSessionVariable("CapturedDPKey").to(CapturedDPKeysVals);
				bDPCardAvailable = true;

			} // End for

			if (bDPCardAvailable == false) {
				Assert.assertTrue(
						"There are no DP Cards with " + WTDCount + " available in the OpportunityDeck for the Client",
						false);
				getDriver().quit();
			}

		} catch (Exception e) {
			Assert.assertTrue(e.getMessage(), false);
			getDriver().quit();

		}
	}

	@Step
	public void validateDPCardAssignmentViewForNewPresProfile(String arg1, String arg2, String arg3) {
		List<String> DPKeysVals = new ArrayList<String>();
		List<String> AssignedPresProflsOnDP = new ArrayList<String>();
		List<String> AssignedPresentations = new ArrayList<String>();

		String DPKey = Serenity.sessionVariableCalled("CapturedDPKey");

		// Get the first DPKey values in the Available Opp Deck
		// String DPKey =
		// refSeleniumUtils.get_TextFrom_Locator(refOppurtunityDeck.DPKeyXpath).trim();
		String DPCardXPath = StringUtils.replace(refOppurtunityDeck.AssignedPresentationsforDPCard, "DPKeyArg", DPKey);

		AssignedPresProflsOnDP = refSeleniumUtils.getWebElementValuesAsList(DPCardXPath);
		AssignedPresentations = Serenity.sessionVariableCalled("PresentationNamesList");
		boolean bNewPresentationAssigned = false;

		// Compare the Assignment names on the DPCard Assigned view the Assigned
		// Presentations
		for (int r = 0; r < AssignedPresProflsOnDP.size(); r++) {
			for (int t = 0; t < AssignedPresentations.size(); t++) {
				bNewPresentationAssigned = false;
				if (AssignedPresentations.get(t).equalsIgnoreCase(AssignedPresProflsOnDP.get(r))) {
					bNewPresentationAssigned = true;
				}
			}
		}
		if (bNewPresentationAssigned == false) {
			Assert.assertTrue(
					"New Presentation created and assigned to the DP Card is not displayed on the DPCard AssignView",
					false);
			getDriver().quit();
		} else {
			Assert.assertTrue(
					"New Presentation created and assigned to the DP Card is  displayed on the DPCard AssignView",
					true);
		}

	}

	@Step
	public void retrieveDPForAvailableLOBs(String sArg1, String sArg2) {
		List<WebElement> DPsWithSummaryView = new ArrayList<WebElement>();
		List<WebElement> AvailableLOBs = new ArrayList<WebElement>();
		WebElement DPKey = null;
		try {
			DPsWithSummaryView = refSeleniumUtils.getElementsList("XPATH", refOppurtunityDeck.AllDPshavingSummaryView);
		} catch (ElementNotFoundException e) {
			e.printStackTrace();
		}

		System.out.println("size::" + DPsWithSummaryView.size());

		for (int k = 1; k <= DPsWithSummaryView.size(); k++) {
			AvailableLOBs = getDriver().findElements(By.xpath("(" + refOppurtunityDeck.AllDPshavingSummaryView + ")"
					+ "[" + k + "]" + refOppurtunityDeck.AvaialbleLOBForDPPartialXpath));
			System.out.println("size::" + AvailableLOBs.size());
			if (AvailableLOBs.size() == 2) {
				DPKey = getDriver().findElement(By.xpath("(" + refOppurtunityDeck.AllDPshavingSummaryView + ")" + "["
						+ k + "]" + refOppurtunityDeck.DPKeyXpath));
				break;
			}
			if (k == DPsWithSummaryView.size() && AvailableLOBs.size() != 3) {
				Assert.assertTrue(
						"3 Active LOBs of DP are not available in Available DP's section with Not started state",
						false);
				getDriver().quit();
			}
		}
		
		if(DPKey==null)
		{
			Assert.assertTrue("There is no Dp,which is having '2' LOBS ", false);
		}
		
		System.out.println("DP Key::" + DPKey.getText());
		Serenity.setSessionVariable("CapturedDPKey").to(DPKey.getText().trim());

	}

	@Step
	public void filterRelatedDPs(String sPlaceHolder, String sFilterValue) throws InterruptedException {
		Assert.assertEquals(refOppurtunityDeck.applyAvailableDPsFilter(sFilterValue), true);
	}

	@Step
	public void captureRequiredDetails(String sTypeName, String sCriteria) {

		String Criteria = sCriteria;
		String MedPolicyName = "";
		int MedPolicyIndex;
		String TopicName = "";
		int TopicIndex;

		switch (sTypeName) {
		case "MedicalPolicy":

			String[] temp = Criteria.split("-");
			int ItemCount = Integer.parseInt(temp[0]);
			String Type = temp[1];

			List<String> MedicalPolicyTitles = new ArrayList();
			MedicalPolicyTitles = refSeleniumUtils.getWebElementValuesAsList(refOppurtunityDeck.AllMedPolicies);

			for (int r = 0; r < MedicalPolicyTitles.size(); r++) {
				if (MedicalPolicyTitles.get(r).contains("2 Topic(s)")
						|| MedicalPolicyTitles.get(r).contains("3 Topic(s)")) {
					MedPolicyName = MedicalPolicyTitles.get(r);
					MedPolicyIndex = r;
					break;
				}
			}

			Serenity.setSessionVariable("MedPolicyName").to(MedPolicyName);
			break;

		case "Topic":

			// temp = Criteria.split("-");
			// ItemCount = Integer.parseInt(temp[0]);
			// Type = temp[1];

			// By default it searches for the Topic with more than One DP
			List<String> TopicTitles = new ArrayList();
			TopicTitles = refSeleniumUtils.getWebElementValuesAsList(refOppurtunityDeck.AllTopics);

			for (int r = 0; r < TopicTitles.size(); r++) {
				if (!TopicTitles.get(r).contains("1 DP(s)")) // If topic
					// contains more
					// than 2 DPs
					// then capture
					// the TopicName
				{
					TopicName = TopicTitles.get(r);
					TopicIndex = r;
					break;
				}
			}

			Serenity.setSessionVariable("TopicName").to(TopicName);

			break;

		}

	}

	@Step
	public void clickAssignIcon(String sLevel, String MedicalPolicyorTopic) {

		String sMedPolicyCaptured = Serenity.sessionVariableCalled("MedPolicyName");
		String sTopicNameCaptured = Serenity.sessionVariableCalled("TopicName");
		List<WebElement> webElList = new ArrayList<WebElement>();
		String sAssignIconXpath = "";

		switch (sLevel) {

		case "TopicLevel":

			if (MedicalPolicyorTopic.equalsIgnoreCase("MedicalPolicy")) {
				sAssignIconXpath = StringUtils.replace(refOppurtunityDeck.TopicAssignIconforMedPolicy, "MedPolicyArg",
						sMedPolicyCaptured.trim());
			} else if (MedicalPolicyorTopic.equalsIgnoreCase("Topic")) {
				sAssignIconXpath = StringUtils.replace(refOppurtunityDeck.TopicAssignIconforTopic, "TopicNameArg",
						sTopicNameCaptured.trim());
			}

			try {
				webElList = refSeleniumUtils.getElementsList("XPATH", sAssignIconXpath);
			} catch (ElementNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			refSeleniumUtils.clickOn(webElList.get(0));
			break;

		case "MedicalPolicyLevel":

			if (MedicalPolicyorTopic.equalsIgnoreCase("MedicalPolicy")) {
				sAssignIconXpath = StringUtils.replace(refOppurtunityDeck.AssignIconforMedPolicy, "MedPolicyArg",
						sMedPolicyCaptured.trim());
			} else if (MedicalPolicyorTopic.equalsIgnoreCase("Topic")) {
				sAssignIconXpath = StringUtils.replace(refOppurtunityDeck.AssignIconforMedPolicyBasedOnTopic,
						"TopicNameArg", sTopicNameCaptured.trim());
			}
			refSeleniumUtils.clickGivenXpath(sAssignIconXpath);
			break;

		case "DPLevel":
			sAssignIconXpath = StringUtils.replace(refOppurtunityDeck.DPAssignIconforTopic, "TopicNameArg",
					sTopicNameCaptured.trim());
			try {
				webElList = refSeleniumUtils.getElementsList("XPATH", sAssignIconXpath);
			} catch (ElementNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			refSeleniumUtils.clickOn(webElList.get(0));
			break;

		}

	}

	@Step
	public void validateNoOppurtunityMessage(String sMessage) {
		refOppurtunityDeck.verifyNoOppurtunityMessage(sMessage);
	}

	@Step
	public void verifyOppurtunitiesInOppurtunityGrid(String sMessage) {
		refOppurtunityDeck.verifyOppurtunitiesAndValidateNoOppurtunityMessageDisappeared(sMessage);
	}

	@Step
	public void verify(String sDescription, boolean blnStatus) {
		GenericUtils.Verify(sDescription, blnStatus);
	}

	@Step
	public void verifyExpandAndCollapseAllBehaviour(String arg1) throws ElementNotFoundException {
		refOppurtunityDeck.verifyExpandAndCollapseAllFunctionality(arg1);
	}

	@Step
	public void verifyExpandAndCollapseAtMedicalPolicyLevel(String arg1) throws ElementNotFoundException {
		refOppurtunityDeck.verifyExpandAndCollapseAtMedicalPolicyLevel(arg1);
	}

	@Step
	public void verifyExpandAndCollapseAtTopicLevel(String arg1) throws ElementNotFoundException {

		refOppurtunityDeck.verifyExpandAndCollapseAtTopicLevel(arg1);
	}

	@Step
	public void verifyPresentationNameOnDPCard() throws ElementNotFoundException {
		List<String> AssignedDPCards = Serenity.sessionVariableCalled("ListOfDPs");
		for (int i = 0; i < AssignedDPCards.size(); i++) {
			System.out.println("DP card is " + StringUtils.replace(
					"//label[text()='" + AssignedDPCards.get(i) + "']" + refOppurtunityDeck.sPresNameOnDPCard, "sval",
					Serenity.sessionVariableCalled("sPProfileName").toString()));
			Assert.assertTrue("Presentation name is verified successfully on each DP card::"+AssignedDPCards.get(i)+",Prsename::"+Serenity.sessionVariableCalled("sPProfileName"),
					refSeleniumUtils.is_WebElement_Displayed(StringUtils.replace(
							"//label[text()='" + AssignedDPCards.get(i) + "']" + refOppurtunityDeck.sPresNameOnDPCard,
							"sval", Serenity.sessionVariableCalled("sPProfileName").toString())));
		}

	}

	@Step
	public void verifyTotalRawSavings() throws ElementNotFoundException {
		int totalSavings = 0;
		int j = 0;
		List<String> AssignedDPCards = Serenity.sessionVariableCalled("ListOfDPs");
		for (int i = 0; i < AssignedDPCards.size(); i++) {
			j = i + 1;
			String rawSavings = refSeleniumUtils.getTexFfromLocator(
					"//label[text()='"+AssignedDPCards.get(i)+"']/../../../../following-sibling::mat-card-content//mat-grid-tile[@class='mat-grid-tile dpSavings']//div[2]");

			String rawSavingsAfter$ = rawSavings.split("\\$")[1];
			String rawSavingsInt = rawSavingsAfter$.replaceAll("\\,", "");
			totalSavings = totalSavings + Integer.parseInt(rawSavingsInt);
			System.out.println("Number of DP cards assigned to the presentation are " + j);

		}
		refSeleniumUtils.moveTo(StringUtils.replace(refPresentationProfile.sPresNameTitle, "PresNameArg",
				Serenity.sessionVariableCalled("sPProfileName").toString()));
		refSeleniumUtils.defaultWait(ProjectVariables.TImeout_3_Seconds);
		refSeleniumUtils.highlightElement(StringUtils.replace(refPresentationProfile.sTotalSavingsTexts, "sval",
				Serenity.sessionVariableCalled("sPProfileName").toString()));
		System.out.println(
				refSeleniumUtils.is_WebElement_Displayed(StringUtils.replace(refPresentationProfile.sTotalSavingsTexts,
						"sval", Serenity.sessionVariableCalled("sPProfileName").toString())));
		String t1 = refSeleniumUtils.get_TextFrom_Locator(StringUtils.replace(refPresentationProfile.sTotalSavingsTexts,
				"sval", Serenity.sessionVariableCalled("sPProfileName").toString()));
		String[] t2 = t1.split("\\$");
		String[] sTotalSavings = t2[1].split(" ");
		String[] t3 = t1.split(" DP");
		String[] t4 = t3[0].split(Serenity.sessionVariableCalled("sPProfileName").toString() + "\n");
		int sTotalDPs = Integer.parseInt(t4[1]);

		System.out.println("Savings value from tooltip is " + sTotalSavings[0]);
		System.out.println("element display status upon highlight " + refSeleniumUtils
				.is_WebElement_Displayed(StringUtils.replace(refPresentationProfile.sTotalSavingsTexts, "sval",
						Serenity.sessionVariableCalled("sPProfileName").toString())));
		Assert.assertTrue("Tooltip::" + sTotalSavings[0] + " is displayed when User hovers mouse on Presentation::",
				refSeleniumUtils.is_WebElement_Displayed(StringUtils.replace(refPresentationProfile.sTotalSavingsTexts,
						"sval", Serenity.sessionVariableCalled("sPProfileName").toString())));

		int ActualTotalSavings = Integer.parseInt(CharMatcher.DIGIT.retainFrom(sTotalSavings[0]));
		Assert.assertEquals("Total savings displayed on presentation profile is sum of rawsavings on each DP card",
				totalSavings, ActualTotalSavings);
		//Assert.assertEquals("Total DP cards displayed on presentation profile is equal to DP cards in DB with not started state", j,sTotalDPs);
	}

	// ===================================================== Chaitanya
	// =============================================================================//
	@Step
	public void validate_the_captured_data_under_Avaialbe_DPs_section(String CapturedDataCriteria) {

		String value = null;
		String DpKey = null;
		// System.out.println(Serenity.sessionVariableCalled("Medicalpolicy").toString());

		System.out.println("validting the captured " + CapturedDataCriteria + " in PM from CPW.....");

		List<String> Dpkeyslist = null;
		switch (CapturedDataCriteria) {
		case "Single DPkey":
			value = Serenity.sessionVariableCalled("DPkey").toString().replace("[", "");
			DpKey = value.replace("]", "");

			Serenity.setSessionVariable("DPkey").to(DpKey);
			// validate the given DPkey is displayed in available opportunity
			// deck or not
			refOppurtunityDeck.validatethegivenDatainOpportunityDeck(
					Serenity.sessionVariableCalled("DPkey").toString().trim(), "DPkey");

			break;
		case "Multiple DPkeys":
			value = Serenity.sessionVariableCalled("DPkey").toString().replace("[", "");
			DpKey = value.replace("]", "");

			Serenity.setSessionVariable("DPkey").to(DpKey);
			Dpkeyslist = Arrays.asList(Serenity.sessionVariableCalled("DPkey").toString().split(","));

			for (int i = 0; i < Dpkeyslist.size(); i++) {

				// validate the given DPkey is displayed in available
				// opportunity deck or not
				refOppurtunityDeck.validatethegivenDatainOpportunityDeck(Dpkeyslist.get(i).trim(), "DPkey");

			}

			break;
		case "Multiple Topics":

			/*
			 * ArrayList<String> DPKeyslist=new ArrayList<>(); DPKeyslist.add(
			 * "Automatic External Defibrillators (E0617, K0606)");
			 * DPKeyslist.add("Hospital Beds and Accessories"); DPKeyslist.add(
			 * "Breast Pumps"); DPKeyslist.add("Modifiers for DMEPOS");
			 * 
			 * Serenity.setSessionVariable("clientkey").to("9");
			 * Serenity.setSessionVariable("release").to("201905");
			 * Serenity.setSessionVariable("Medicalpolicy").to(
			 * "Durable Medical Equipment and Supplies Policy");
			 * Serenity.setSessionVariable("Topic").to(DPKeyslist);
			 */

			value = Serenity.sessionVariableCalled("Topic").toString().replace("[", "");
			DpKey = value.replace("]", "");
			Serenity.setSessionVariable("Topic").to(DpKey);
			List<String> Topicslist = Arrays.asList(Serenity.sessionVariableCalled("Topic").toString().split(","));

			for (int i = 0; i < Topicslist.size(); i++) {

				// Unselect the all filter checkbox in filter drawer
				oFilterDrawer.user_unchecks_selectAllPolicies();

				// Select the captured topic in tyhe filter drawer section
				refOppurtunityDeck.Select_the_given_Topic_or_MP_in_filterDrawer(Topicslist.get(i).trim(), "Topic");

				// CLicking on apply button of medicalpolicy/topic section
				oFilterDrawer.user_filters_by_clicking_on_Apply_for_Medical_Policy_Topic();

				// validate the given Topic is displayed in available
				// opportunity deck or not
				refOppurtunityDeck.validatethegivenDatainOpportunityDeck(Topicslist.get(i).trim(), "Topic");

				// if(oCPWPage.is_WebElement_Displayed(StringUtils.replace(oCPWPage.labelcontainstext,
				// "svalue", "Topic: "+Topicslist.get(i).trim()+"")))
				{
					/*
					 * //Retrieve the DPkeys from the given Topic from the DB
					 * refOppurtunityDeck.
					 * validatethegivenDatainOpportunityDeckwithDB(Topicslist.
					 * get(i).trim(),"Topic");
					 * 
					 * 
					 * Dpkeyslist=Arrays.asList(Serenity.sessionVariableCalled(
					 * "DPkey").toString().split(","));
					 * 
					 * for (int j = 0; j < Dpkeyslist.size(); j++) { //validate
					 * the given DPkey is displayed in available opportunity
					 * deck or not
					 * refOppurtunityDeck.validatethegivenDatainOpportunityDeck(
					 * Dpkeyslist.get(j).trim(),"DPkey");
					 * 
					 * }
					 */

				}
				// else
				{
					// System.out.println("Captured Topic contains ',' in the
					// decsription,Topic==>"+Topicslist.get(i));
				}
				// refOppurtunityDeck.clickGivenXpath("//button[@class='cpd-filter-opener-button']/img");

				// refOppurtunityDeck.defaultWait(ProjectVariables.TImeout_3_Seconds);

			}

			break;
		case "Multiple MPs":
			/*
			 * ArrayList<String> DPKeyslist=new ArrayList<>(); DPKeyslist.add(
			 * "Durable Medical Equipment and Supplies Policy"); DPKeyslist.add(
			 * "Anesthesia Policy"); DPKeyslist.add("Maximum Units Policy");
			 * DPKeyslist.add("Ambulance Policy");
			 * 
			 * Serenity.setSessionVariable("clientkey").to("9");
			 * Serenity.setSessionVariable("release").to("201905");
			 * Serenity.setSessionVariable("Medicalpolicy").to(DPKeyslist);
			 */

			System.out.println(Serenity.sessionVariableCalled("Medicalpolicy").toString());

			value = Serenity.sessionVariableCalled("Medicalpolicy").toString().replace("[", "");
			DpKey = value.replace("]", "");
			Serenity.setSessionVariable("Medicalpolicy").to(DpKey);

			List<String> MPslist = Arrays.asList(Serenity.sessionVariableCalled("Medicalpolicy").toString().split(","));

			for (int i = 0; i < MPslist.size(); i++) {
				// Unselect the all filter checkbox in filter drawer
				oFilterDrawer.user_unchecks_selectAllPolicies();

				// Select the captured topic in tyhe filter drawer section
				refOppurtunityDeck.Select_the_given_Topic_or_MP_in_filterDrawer(MPslist.get(i).trim(), "Medicalpolicy");

				// CLicking on apply button of medicalpolicy/topic section
				oFilterDrawer.user_filters_by_clicking_on_Apply_for_Medical_Policy_Topic();

				// validate the given Topic is displayed in available
				// opportunity deck or not
				refOppurtunityDeck.validatethegivenDatainOpportunityDeck(MPslist.get(i).trim(), "Medicalpolicy");

				// validate the given DPKEys are displayed in available
				// opportunity deck or not for the captured Topic
				// refOppurtunityDeck.validatethegivenDatainOpportunityDeckwithDB(MPslist.get(i).trim(),"Medicalpolicy");

				/*
				 * Dpkeyslist=Arrays.asList(Serenity.sessionVariableCalled(
				 * "DPkey").toString().split(","));
				 * 
				 * for (int j = 0; j < Dpkeyslist.size(); j++) { //validate the
				 * given DPkey is displayed in available opportunity deck or not
				 * refOppurtunityDeck.validatethegivenDatainOpportunityDeck(
				 * Dpkeyslist.get(j).trim(),"DPkey");
				 * 
				 * }
				 * 
				 */
				// refOppurtunityDeck.clickGivenXpath("//button[@class='cpd-filter-opener-button']/img");

				// refOppurtunityDeck.defaultWait(ProjectVariables.TImeout_3_Seconds);

			}

			break;

		default:
			Assert.assertTrue("case not found ==>" + CapturedDataCriteria, false);
			break;

		}

	}

	@Step
	public void validate_the_avaliable_DPs_count_at(String AvailableDPsCriteria) {

		switch (AvailableDPsCriteria) {
		case "ASSIGNEE POPUP DP COUNT BASED ON TOPIC WITH FILTERS":
		case "ASSIGNEE POPUP DP COUNT BASED ON TOPIC":
		case "ASSIGNEE POPUP DP COUNT BASED ON MP WITH FILTERS":
		case "ASSIGNEE POPUP DP COUNT BASED ON MP":
		case "ASSIGNEE POPUP DP COUNT BASED ON CLIENT WITH FILTERS":
		case "ASSIGNEE POPUP DP COUNT BASED ON CLIENT":
		case "DP COUNT BASED ON TOPIC WITH FILTERS":
		case "DP COUNT BASED ON MP WITH FILTERS":
		case "DP COUNT BASED ON CLIENT WITH FILTERS":
			// Valdiate the UI DPCount with DB
			refOppurtunityDeck.validate_the_avaliable_DPs_count_at(AvailableDPsCriteria);
			break;
		case "Header level":
			// Validate the UI DPCount with DB
			refOppurtunityDeck.validate_the_avaliable_DPs_count_at("DP COUNT BASED ON CLIENT");
			break;
		case "Medicalpolicy level":
			// Validate the UI DPCount with DB
			refOppurtunityDeck.validate_the_avaliable_DPs_count_at("DP COUNT BASED ON MP");
			break;
		case "Topic level":
			// Validate the UI DPCount with DB
			refOppurtunityDeck.validate_the_avaliable_DPs_count_at("DP COUNT BASED ON TOPIC");
			break;

		default:
			Assert.assertTrue("Case not found ==>" + AvailableDPsCriteria, false);
			break;
		}

	}

	@Step
	public void verifyPresentationforDP() {
		String sCapturedDP = Serenity.sessionVariableCalled("CapturedDPKey");
		String sAssignedPres = Serenity.sessionVariableCalled("AssignedPresentation");
		boolean blnValue = refOppurtunityDeck.verifyPresentationatGivenDP(sCapturedDP, sAssignedPres);
		verify("Verify " + sAssignedPres + " under " + sCapturedDP, blnValue);

	}

	@Step
	public void verifyProfilenamesareRemovedfromflipViewinOppurtunityDeck() {
		String sPresentation = Serenity.sessionVariableCalled("PresentationName");
		String sCapturedDP = Serenity.sessionVariableCalled("CapturedDPKey");
		oGenericUtils.clickOnElement("a", "Expand All");
		refOppurtunityDeck.flipAllDPCards("Presentation view");
		boolean blnValue = refOppurtunityDeck.verifyPresentationatGivenDP(sCapturedDP, sPresentation);
		verify("unassigned presentation " + sPresentation + " should not be displayed in " + sCapturedDP, !blnValue);

	}

	@Step
	public void deletedProfilenamesareRemovedFromOppurtunityDeck() {
		String sPresentation = Serenity.sessionVariableCalled("profileName");
		oGenericUtils.clickOnElement("a", "Expand All");
		refOppurtunityDeck.flipAllDPCards("Presentation view");

		List<String> DPKeysList = Arrays.asList(Serenity.sessionVariableCalled("DPkey").toString().split(","));

		for (int i = 0; i < DPKeysList.size(); i++) {
			String sDPKey = DPKeysList.get(i).trim();
			boolean blnValue = refOppurtunityDeck.verifyPresentationatGivenDP(sDPKey, sPresentation);
			verify("Deleted presentation " + sPresentation + " should not be displayed in " + sDPKey, !blnValue);
		}
	}

	public void getWorktoDoCountforDPs() {

		HashMap<String, String> sToDoCount = new HashMap<String, String>();

		List<String> DPKeysList = Arrays.asList(Serenity.sessionVariableCalled("DPkey").toString().split(","));

		for (int i = 0; i < DPKeysList.size(); i++) {
			String sDPKey = DPKeysList.get(i).trim();
			// String sValue = refOppurtunityDeck.getWorktoDoCountforDP(sDPKey);
			// sToDoCount.put(sDPKey, sValue);
			// verify("To Do Count for DP "+sDPKey+" is "+sValue, true);
		}

		Serenity.setSessionVariable("WorkToDoCount").to(sToDoCount);

	}

	@Step
	public void validate_the_priority_and_reasons_for_the_captured_data_at(String capturedDP, String Priorities,
			String capturedDataCriteria) throws Exception {

		switch (capturedDataCriteria) {
		case "DP Level":
			// Validate the priority and reasons on the given DP card
			refOppurtunityDeck.Validate_the_priority_and_reasons_on_the_given_DPCard(capturedDP, Priorities);
			break;
		case "Topic Level":

			// Validate the priority and reasons on the captured Topic or MP
			refOppurtunityDeck.Validate_the_priority_and_reasons_for_the_captured_Topic_MP(
					Serenity.sessionVariableCalled("Topic"), "Topic");

			// Validate the priority and reasons on the given DP card
			refOppurtunityDeck.Validate_the_priority_and_reasons_on_the_given_DPCard(capturedDP, Priorities);
			break;
		case "MP Level":
			// Validate the priority and reasons on the captured Topic or MP
			refOppurtunityDeck.Validate_the_priority_and_reasons_for_the_captured_Topic_MP(
					Serenity.sessionVariableCalled("Medicalpolicy"), "Medicalpolicy");

			// Validate the priority and reasons on the given DP card
			refOppurtunityDeck.Validate_the_priority_and_reasons_on_the_given_DPCard(capturedDP, Priorities);
			break;

		default:
			Assert.assertTrue("Case not found ==>" + capturedDataCriteria, false);
			break;
		}
	}

	public void verifyWorktoDoCountforDPs() {

		HashMap<String, String> sToDoCount = Serenity.sessionVariableCalled("WorkToDoCount");
		List<String> DPKeysList = Arrays.asList(Serenity.sessionVariableCalled("DPkey").toString().split(","));

		for (int i = 0; i < DPKeysList.size(); i++) {
			String sDPKey = DPKeysList.get(i).trim();
			// String sValue = refOppurtunityDeck.getWorktoDoCountforDP(sDPKey);
			String oldtoDoCount = sToDoCount.get(sDPKey).trim();
			// verify("To Do count for DP "+sDPKey+" before is "+oldtoDoCount+"
			// and after deleting updated to"+sValue ,
			// !oldtoDoCount.equalsIgnoreCase(sValue));
		}

	}

	@Step
	public void assignFirstMedPolicyDPstoPresentationProfile() {
		// Capture the DPNos before assigning for verification
		List<String> OppDeckDPsVals = new ArrayList();
		OppDeckDPsVals = refSeleniumUtils.getWebElementValuesAsList(refOppurtunityDeck.sOppDeckAllDPs);
		Serenity.setSessionVariable("OppDeckDpValues").to(OppDeckDPsVals);
		List<WebElement> AssignIcons = new ArrayList();

		try {
			AssignIcons = getDriver().findElements(By.xpath("//label[contains(text(), 'Medical Policy:')]"));
			int loopcounter = 0;
			if (AssignIcons.size() > 1) {
				loopcounter = 1;
			} else {
				loopcounter = AssignIcons.size();
			}
			for (int k = 0; k < loopcounter; k++) {
				String MedPolicy = refSeleniumUtils
						.get_TextFrom_Locator("(//label[contains(text(), 'Medical Policy:')])[1]");
				System.out.println("Medical Policy value is " + MedPolicy);
				String t[] = MedPolicy.split("Medical Policy:");
				System.out.println("Medical policy text is " + t[1]);

				String t2[] = t[1].split(" \\(");
				String MedPolicyName = t2[0].trim();
				System.out.println("Medical policy name is " + MedPolicyName);
				Serenity.setSessionVariable("MPName").to(MedPolicyName);

				String DPAndTopicCount = t2[1].trim();
				System.out.println("Medical policy DP and topic count is " + DPAndTopicCount);
				Serenity.setSessionVariable("DPTopicCount").to(DPAndTopicCount);
				//String[] DPCountInTopics = t2[1].split(" DP");
				System.out.println("DPs count is " + StringUtils.substringBetween(t[1],"(", "DP(s)").trim());
				Serenity.setSessionVariable("DPCount").to(StringUtils.substringBetween(t[1],"(", "DP(s)").trim());
				/*String[] TopicCountInTopics = DPCountInTopics[1].split(" in ");
				String[] t3 = TopicCountInTopics[1].split(" Topic");*/
				System.out.println("Topics count is " + StringUtils.substringBetween(DPAndTopicCount,"in","Topic(s)").trim());
				Serenity.setSessionVariable("TopicCount").to(StringUtils.substringBetween(DPAndTopicCount,"in","Topic(s)").trim());
				refSeleniumUtils.defaultWait(ProjectVariables.TImeout_2_Seconds);
				refSeleniumUtils.clickGivenXpath("(//label[contains(text(), 'Medical Policy:')])[1]/..//following-sibling::div//i");
				String sAssignChkBox = StringUtils.replace(refOppurtunityDeck.sPresentationAssignmentCheckbox, "val",
						Serenity.sessionVariableCalled("PresentationName"));				
				refSeleniumUtils.clickOnGivenElemntByJavaScript(sAssignChkBox);				
				refSeleniumUtils.defaultWait(ProjectVariables.TImeout_2_Seconds);
				refSeleniumUtils.Click_given_Locator(refOppurtunityDeck.sPresentationAssignmentOKBtn);
				refSeleniumUtils.defaultWait(ProjectVariables.TImeout_5_Seconds);
				AssignIcons = getDriver().findElements(By.xpath(refOppurtunityDeck.sMedPolicyAssignmentIcons));
			}
		} catch (Exception e) {
			System.out.println("Exception Message::" + e.getMessage());
			getDriver().quit();
			GenericUtils.Verify("Exception Message::" + e.getMessage(), false);
		}
	}

	@Step
	public void validateDropdownValues(String dropdownName, String Options) {
		List<String> DropdownActualValues = new ArrayList<String>();
		List<String> DropdownExpectedValues = new ArrayList<String>();

		// Splitting the expected array values
		String[] DropdownVals = Options.split(",");

		for (int m = 0; m < DropdownVals.length; m++) {
			DropdownExpectedValues.add(DropdownVals[m].trim());
			System.out.println("Dropdown Value::" + DropdownVals[m].trim());
		}
		//refSeleniumUtils.highlightElement(refOppurtunityDeck.SortbyDropdownArrow);
		refSeleniumUtils.clickGivenXpath(refOppurtunityDeck.SortbyDropdownArrow);

		DropdownActualValues = refSeleniumUtils.getWebElementValuesAsList(refOppurtunityDeck.DropdownValues);

		if (DropdownActualValues.equals(DropdownExpectedValues)) {
			Assert.assertTrue("Sortby Dropdown Values are displayed as expected", true);
		} else {
			Assert.assertTrue("Sortby Dropdown Values are not displayed as expected", false);
		}

		String ValueXpath = StringUtils.replace(refOppurtunityDeck.SortbyDropdownValues, "DropDownVal", "Alphanumeric");
		refSeleniumUtils
		.clickGivenXpath("//mat-option//span[@class='mat-option-text' and contains(text(), Alphanumeric)]");
		refSeleniumUtils.defaultWait(2);
	}

	public void ValidatePPSDetailsinDPDetailedView() throws ElementNotFoundException {

		String payer = "";
		String claimType = "";
		String insurance = "";
		String DPXpath = StringUtils.replace(refOppurtunityDeck.DPKeyDynamicXpath, "DPKeyArg",
				"DP " + Serenity.sessionVariableCalled("DPkey"));

		refSeleniumUtils.clickGivenXpath(DPXpath);

		MongoDBUtils.GettheCapturedDispositionPayerLOBClaimTypesFromtheGiven(Serenity.sessionVariableCalled("DPkey"));

		for (int i = 0; i < ProjectVariables.PPSList.size(); i++) {
			String PPS = ProjectVariables.PPSList.get(i);
			String[] temp = StringUtils.split(PPS, "-");
			payer = temp[0];
			insurance = temp[1];
			claimType = temp[2];
			String t = StringUtils.replace(refOppurtunityDeck.DPPayer, "DPKeyArg",
					Serenity.sessionVariableCalled("DPkey"));
			String Payer = StringUtils.replace(t, "Payer", payer);
			Assert.assertTrue("Payer Short " + payer + " is not displayed in DP detailed view",
					refSeleniumUtils.is_WebElement_Displayed(Payer));

			String u = StringUtils.replace(refOppurtunityDeck.DPClaimType, "DPKeyArg",
					Serenity.sessionVariableCalled("DPkey"));
			String ClaimType = StringUtils.replace(u, "ClaimType", claimType);
			Assert.assertTrue("ClaimType " + claimType + " is not displayed in DP detailed view",
					refSeleniumUtils.is_WebElement_Displayed(ClaimType));

		}

		boolean assignIconState = refSeleniumUtils.is_WebElement_enabled(StringUtils
				.replace(refOppurtunityDeck.DP_AssignIcon, "DPKeyArg", Serenity.sessionVariableCalled("DPkey")));
		if (!assignIconState) {
			Assert.assertFalse("Assign icon is not in disabled state though no PPS combination is selected", false);
		} else {
			Assert.assertFalse("Assign icon is not in enabled state though no PPS combination is selected", true);
		}

		String[] Lobs = insurance.split(",");
		for (int j = 0; j < Lobs.length; j++) {
			insurance = Lobs[j];
			String v = StringUtils.replace(refOppurtunityDeck.DPLOB, "DPKeyArg",
					Serenity.sessionVariableCalled("DPkey"));
			String Insurance = StringUtils.replace(v, "Insurance", insurance);
			Serenity.setSessionVariable("Insurance").to(insurance);
			Assert.assertTrue(
					"Insurance " + Serenity.sessionVariableCalled("Insurance")
					+ " is not displayed in DP detailed view",
					refSeleniumUtils.is_WebElement_Displayed(Insurance));

			String LOB = refOppurtunityDeck.getLOB(insurance);
			String LOBGridValue = refSeleniumUtils.Get_Value_By_given_attribute("class",
					StringUtils.replace(refOppurtunityDeck.DP_LOBStatus, "LobValue", LOB));
			if (LOBGridValue.equalsIgnoreCase("available filtered")) {
				Assert.assertTrue("LOB value " + insurance + " is not displayed in bold", true);
			} else if (LOBGridValue.equalsIgnoreCase("unavailable filtered")) {
				Assert.assertTrue("LOB value " + insurance + " is not greyed out", true);
			} else {
				Assert.assertTrue("LOB value " + insurance + " is displayed though it should not display", true);
			}

		}

	}

	@Step
	public void SelectAllPPSAndAssignDPToPP() {

		refSeleniumUtils.Click_given_Locator(StringUtils.replace(refOppurtunityDeck.DP_ALL_chk, "DPKeyArg",
				Serenity.sessionVariableCalled("DPkey")));

		boolean assignIconState = refSeleniumUtils.is_WebElement_enabled(StringUtils
				.replace(refOppurtunityDeck.DP_AssignIcon, "DPKeyArg", Serenity.sessionVariableCalled("DPkey")));
		if (assignIconState) {
			Assert.assertTrue("Assign icon is in enabled state after selecting all PPS combination", true);
		} else {
			Assert.assertTrue("Assign icon is not in enabled state after selecting all PPS combination", false);
		}

		refSeleniumUtils.Click_given_Locator(StringUtils.replace(refOppurtunityDeck.DP_AssignIcon, "DPKeyArg",
				Serenity.sessionVariableCalled("DPkey")));

		String sAssignChkBox = StringUtils.replace(refOppurtunityDeck.sPresentationAssignmentCheckbox, "val",
				Serenity.sessionVariableCalled("PresentationName"));
		refSeleniumUtils.Click_given_Locator(sAssignChkBox);

		refSeleniumUtils.highlightElement(refPresentationProfile.sPresentationAssignmentOKBtn);
		refSeleniumUtils.Click_given_Locator(refPresentationProfile.sPresentationAssignmentOKBtn);
	}

	@Step
	public void verifyPresentationNameForPPS() {
		refSeleniumUtils.defaultWait(5);
		String DPXpath = StringUtils.replace(refOppurtunityDeck.DPKeyDynamicXpath, "DPKeyArg",
				"DP " + Serenity.sessionVariableCalled("DPkey"));
		refSeleniumUtils.clickGivenXpath(DPXpath);

		String a = StringUtils.replace(refOppurtunityDeck.DP_LOBRows, "DPKeyArg",
				Serenity.sessionVariableCalled("DPkey"));
		String b = StringUtils.replace(a, "Insurance", Serenity.sessionVariableCalled("Insurance").toString().toLowerCase());
		
		System.out.println(b);
		
		List<WebElementFacade> count = refSeleniumUtils.findAll(b);
		for (int i = 1; i < count.size() + 1; i++) {
			if (refSeleniumUtils.is_WebElement_Displayed(b + "[" + i + "]")) 
			{
				Assert.assertTrue("Presentation name is not displayed for all combination of PPS in DP detailed view",
						true);
			} else {
				Assert.assertTrue("Presentation name is displayed for all combination of PPS in DP detailed view",
						false);

			}
		}
	}

	@Step
	public void verifyPresentationNameForDP() {

		String c = StringUtils.replace(refOppurtunityDeck.DP_Presentationlabel, "DPKeyArg",
				Serenity.sessionVariableCalled("DPkey"));
		Assert.assertTrue("Presentation name is not displayed on DP card", refSeleniumUtils.is_WebElement_Displayed(
				StringUtils.replace(c, "sval", Serenity.sessionVariableCalled("PresentationName").toString())));

	}

	@Step
	public void verifyAvailableOppurtunitiesElementsInHeader() {

		String FilterIconxpath = StringUtils.replace(refOppurtunityDeck.Toolbar_icons, "sval", "Filter Opportunities");
		refSeleniumUtils.highlightElement(FilterIconxpath);
		Assert.assertTrue("Filter icon is not displayed above payer/LOB grid filter section",
				refSeleniumUtils.is_WebElement_Displayed(FilterIconxpath));

		String Notificationsxpath = StringUtils.replace(refOppurtunityDeck.Toolbar_icons, "sval", "Notifications");
		refSeleniumUtils.highlightElement(Notificationsxpath);
		Assert.assertTrue("Notifications icon is not displayed above payer/LOB grid filter section",
				refSeleniumUtils.is_WebElement_Displayed(Notificationsxpath));

		String SearchDPsxpath = StringUtils.replace(refOppurtunityDeck.Toolbar_icons, "sval", "Search for DPs");
		refSeleniumUtils.highlightElement(SearchDPsxpath);
		Assert.assertTrue("Search for DPs icon is not displayed above payer/LOB grid filter section",
				refSeleniumUtils.is_WebElement_Displayed(SearchDPsxpath));

		String EditPresentationxpath = StringUtils.replace(refOppurtunityDeck.Toolbar_icons, "sval",
				"Edit Presentation Profile");
		refSeleniumUtils.highlightElement(EditPresentationxpath);
		Assert.assertTrue("Edit Presentation icon is not displayed above payer/LOB grid filter section",
				refSeleniumUtils.is_WebElement_Displayed(EditPresentationxpath));

		// String PresHierarchyxpath =
		// StringUtils.replace(refOppurtunityDeck.Toolbar_icons, "sval", "View
		// Presentation Hierarchy");
		// String PresHierarchyxpath = "//button[@class='toolkit-button-icon
		// hierarchy_image' and @mattooltip='View Presentation Hierarchy']";
		// refSeleniumUtils.highlightElement(PresHierarchyxpath);
		// Assert.assertTrue("View Presentation Hierarchy is not displayed above
		// payer/LOB grid filter section",
		// refSeleniumUtils.is_WebElement_Displayed(PresHierarchyxpath));

	}

	@Step
	public void verifyProfilenameRemovalfromflipViewinOppurtunityDeck() {
		String sPresentation = Serenity.sessionVariableCalled("PresentationName");
		System.out.println(sPresentation);
		String sCapturedDP = Serenity.sessionVariableCalled("DPkey");
		System.out.println(sCapturedDP);
		refOppurtunityDeck.flipAllDPCards("Presentation view");
		boolean blnValue = refOppurtunityDeck.verifyPresentationatGivenDP(sCapturedDP, sPresentation);
		verify("unassigned presentation " + sPresentation + " should not be displayed in " + sCapturedDP, !blnValue);

		refSeleniumUtils.clickGivenXpath("//label[text()='DP " + Serenity.sessionVariableCalled("DPkey") + "']");
		refSeleniumUtils.clickGivenXpath(refOppurtunityDeck.Chk_AllPPS);

		String AssignIconClass = refSeleniumUtils.Get_Value_By_given_attribute("class", refOppurtunityDeck.AssignIcon);
		if (AssignIconClass.contains("disabled")) {
			Assert.assertTrue("Assign icon of DP should not be disabled after unassigning", true);
		}
		refSeleniumUtils.clickGivenXpath(refOppurtunityDeck.AssignIcon);
		refSeleniumUtils.defaultWait(5);
		//refSeleniumUtils.clickGivenXpath("//div[text()=' " + Serenity.sessionVariableCalled("PresentationName")
		//+ " ']//preceding-sibling::div[@class='mat-radio-container']");
		refSeleniumUtils.clickGivenXpath("//div[@class='mat-radio-label-content']//span[contains(text(),'"+Serenity.sessionVariableCalled("PresentationName")+"')]");
		refSeleniumUtils.clickGivenXpath(refOppurtunityDeck.Btn_Cancel_AssignPopup);
	}

	@Step
	public void verifyLOBstatusbarforDP(String arg1) throws Exception {

		String insurance = "";
		String LOBDecision = "";

		switch (arg1) {

		case "DP Detailed View":
		case "DP Decision View":
			/*if (arg1.equals("DP Decision View")) {
				refSeleniumUtils.Click_given_Locator(StringUtils.replace(refOppurtunityDeck.DPLabel, "sval",
						Serenity.sessionVariableCalled("DPkey")));
			}
*/
			String insuranceKeys = Serenity.sessionVariableCalled("InsuranceKeys");
			String[] temp = insuranceKeys.split(",");
			for (int j = 0; j < temp.length; j++) {
				HashMap<String, String> oHashMap = new HashMap<String, String>();
				oHashMap.put("1", "MCR");
				oHashMap.put("2", "MCD");
				oHashMap.put("3", "DUA");
				oHashMap.put("7", "COM");
				oHashMap.put("8", "BLU");
				oHashMap.put("9", "FED");
				insurance = oHashMap.get(temp[j].trim());

				String Insurance = StringUtils.replace(refOppurtunityDeck.DP_LOB_Bold, "Insurance", insurance);
				Assert.assertTrue(
						"Insurance " + Serenity.sessionVariableCalled("Insurance")
						+ " is not displayed in LOB status bar",
						refSeleniumUtils.is_WebElement_Displayed(Insurance));
			}

			String[] LOBStaticOrder = refSeleniumUtils.get_All_Text_from_Locator(refOppurtunityDeck.DP_LOB_All);

			for (int k = 0; k < LOBStaticOrder.length; k++) {

				if (refSeleniumUtils.findAll(
						StringUtils.replace(refOppurtunityDeck.DP_LOB_Reject_Decision, "sval", LOBStaticOrder[k]))
						.size() > 0) {
					LOBDecision = refSeleniumUtils.Get_Value_By_given_attribute("title",
							StringUtils.replace(refOppurtunityDeck.DP_LOB_Reject_Decision, "sval", LOBStaticOrder[k]));
					if ((LOBDecision.equals("Suppress") || LOBDecision.equals("Reject")
							|| LOBDecision.equals("Suppress,Reject") || LOBDecision.equals("Reject,Suppress"))) {
						Assert.assertTrue("LOB decision taken on LOB " + LOBStaticOrder[k] + " is " + LOBDecision,
								true);
					}
				}

				if (refSeleniumUtils.findAll(
						StringUtils.replace(refOppurtunityDeck.DP_LOB_Approved_Decision, "sval", LOBStaticOrder[k]))
						.size() > 0) {
					LOBDecision = refSeleniumUtils.Get_Value_By_given_attribute("title", StringUtils
							.replace(refOppurtunityDeck.DP_LOB_Approved_Decision, "sval", LOBStaticOrder[k]));
					if ((LOBDecision.equals("Approve Library") || LOBDecision.equals("Approve With Modifications"))) {
						Assert.assertTrue("LOB decision taken on LOB " + LOBStaticOrder[k] + " is " + LOBDecision,
								true);
					}
				}

			}
			if (LOBStaticOrder[0].trim().equals("MCD") && LOBStaticOrder[1].trim().equals("MCR")
					&& LOBStaticOrder[2].trim().equals("COM") && LOBStaticOrder[3].trim().equals("FED")
					&& LOBStaticOrder[4].trim().equals("BLU") && LOBStaticOrder[5].trim().equals("DUA")) {
				Assert.assertTrue(
						"LOB status bar values " + LOBStaticOrder + " are not displayed according to static order",
						true);

			}
			break;
		}
	}

	@Step
	public void verifyLOBstatusbarforDP(String arg1, String arg2) throws Exception {

		String insurance = "";
		String LOBDecision = "";

		switch (arg1) {

		case "DP Detailed View":
		case "DP Decision View":
			if (arg1.equals("DP Decision View")) {
				refSeleniumUtils.Click_given_Locator(StringUtils.replace(refOppurtunityDeck.DPLabel, "sval",
						Serenity.sessionVariableCalled("DPkey")));
			}

			String insuranceKeys = Serenity.sessionVariableCalled("InsuranceKeys");
			List<String> Available_LOB = Arrays.asList(insuranceKeys.split(","));
			System.out.println(Available_LOB);
			String[] temp = insuranceKeys.split(",");
			for (int j = 0; j < temp.length; j++) {
				HashMap<String, String> oHashMap = new HashMap<String, String>();
				oHashMap.put("1", "MCR");
				oHashMap.put("2", "MCD");
				oHashMap.put("3", "DUA");
				oHashMap.put("7", "COM");
				oHashMap.put("8", "BLU");
				oHashMap.put("9", "FED");
				insurance = oHashMap.get(temp[j].trim());

				String Insurance = StringUtils.replace(refOppurtunityDeck.DP_LOB_Bold, "Insurance", insurance);
				Assert.assertTrue(
						"Insurance " + Serenity.sessionVariableCalled("Insurance")
						+ " is not displayed with bold letters in LOB status bar",
						refSeleniumUtils.is_WebElement_Displayed(Insurance));
			}

			Serenity.setSessionVariable("ClientName").to(arg2);
			String sPPS = "insurance_desc";
			Serenity.setSessionVariable("PPS").to(sPPS);
			ArrayList<String> sResultDB2 = OracleDBUtils
					.executeSQLQueryMultipleRows(OracleDBQueries.getOracleDBQuery("DISTINCT PPS OF A CLIENT"));
			Collections.sort(sResultDB2);
			System.out.println("LOB Result from DB for the client is " + sResultDB2);

			String un_LOB = "";
			List<String> Unavailable_LOB = null;
			for (int i = 0; i < sResultDB2.size(); i++) {
				HashMap<String, String> oHashMap1 = new HashMap<String, String>();
				oHashMap1.put("Medicare", "1");
				oHashMap1.put("Medicaid", "2");
				oHashMap1.put("Dual Eligible", "3");
				oHashMap1.put("Commercial", "7");
				oHashMap1.put("Bluecard", "8");
				oHashMap1.put("Federal Employee Program", "9");

				String temp2 = oHashMap1.get(sResultDB2.get(i));
				un_LOB = temp2 + ",";
				System.out.println(un_LOB);
				Unavailable_LOB = Arrays.asList(un_LOB.split(","));
				System.out.println(Unavailable_LOB);
				Serenity.setSessionVariable("GreyLOB").to(Unavailable_LOB);

			}

			for (int l = 0; l < Unavailable_LOB.size(); l++) {
				if (!Serenity.sessionVariableCalled("InsuranceKeys").equals(Unavailable_LOB.get(l))) {
					System.out.println(Unavailable_LOB.get(l));
				}
			}

			// if(!oHashMap.get(temp[j].trim()).equals(oHashMap.get(temp[j].trim()))){
			// String un_Insurance
			// =StringUtils.replace(refOppurtunityDeck.DP_LOB_Grey, "Insurance",
			// insurance);
			// Assert.assertTrue("Insurance
			// "+Serenity.sessionVariableCalled("Insurance")+" is not displayed
			// with greyed letters in LOB status bar",
			// refSeleniumUtils.is_WebElement_Displayed(un_Insurance));
			//
			//
			// }

			String[] LOBStaticOrder = refSeleniumUtils.get_All_Text_from_Locator(refOppurtunityDeck.DP_LOB_All);

			for (int k = 0; k < LOBStaticOrder.length; k++) {

				if (refSeleniumUtils.findAll(
						StringUtils.replace(refOppurtunityDeck.DP_LOB_Reject_Decision, "sval", LOBStaticOrder[k]))
						.size() > 0) {
					LOBDecision = refSeleniumUtils.Get_Value_By_given_attribute("title",
							StringUtils.replace(refOppurtunityDeck.DP_LOB_Reject_Decision, "sval", LOBStaticOrder[k]));
					if ((LOBDecision.equals("Suppress") || LOBDecision.equals("Reject")
							|| LOBDecision.equals("Suppress,Reject") || LOBDecision.equals("Reject,Suppress"))) {
						Assert.assertTrue("LOB decision taken on LOB " + LOBStaticOrder[k] + " is " + LOBDecision,
								true);
					}
				}

				if (refSeleniumUtils.findAll(
						StringUtils.replace(refOppurtunityDeck.DP_LOB_Approved_Decision, "sval", LOBStaticOrder[k]))
						.size() > 0) {
					LOBDecision = refSeleniumUtils.Get_Value_By_given_attribute("title", StringUtils
							.replace(refOppurtunityDeck.DP_LOB_Approved_Decision, "sval", LOBStaticOrder[k]));
					if ((LOBDecision.equals("Approve Library") || LOBDecision.equals("Approve With Modifications"))) {
						Assert.assertTrue("LOB decision taken on LOB " + LOBStaticOrder[k] + " is " + LOBDecision,
								true);
					}
				}

			}
			if (LOBStaticOrder[0].trim().equals("MCD") && LOBStaticOrder[1].trim().equals("MCR")
					&& LOBStaticOrder[2].trim().equals("COM") && LOBStaticOrder[3].trim().equals("FED")
					&& LOBStaticOrder[4].trim().equals("BLU") && LOBStaticOrder[5].trim().equals("DUA")) {
				Assert.assertTrue(
						"LOB status bar values " + LOBStaticOrder + " are not displayed according to static order",
						true);

			}
			break;
		}
	}

	@Step
	public void Validate_filter_drawer_section_with_DB_for(String criteria) {

		List<String> Payers = null;
		List<String> LOBS = null;
		List<String> Claimtypes = null;

		switch (criteria) {
		case "Medicalpolicies":
			// DB Method to retrieve the MPs and their Rawsavings
			MongoDBUtils.GettheMPTopicsAndDPsBasedOnPayerLOBClaimTypes(Payers, LOBS, Claimtypes, "MP");

			refOppurtunityDeck.Validate_filter_drawer_section_with_DB(
					Serenity.sessionVariableCalled("MPwithsavings").toString(), "MP");

			break;
		case "Topics":
			// DB Method to retrieve the Topics and their Rawsavings
			MongoDBUtils.GettheMPTopicsAndDPsBasedOnPayerLOBClaimTypes(Payers, LOBS, Claimtypes, "Topic");

			refOppurtunityDeck.Validate_filter_drawer_section_with_DB(
					Serenity.sessionVariableCalled("Topicwithsavings").toString(), "Topic");

			break;
		case "Medicalpolicies with filter":
			// DB Method to retrieve the MPs and their Rawsavings
			MongoDBUtils.GettheMPTopicsAndDPsBasedOnPayerLOBClaimTypes(ProjectVariables.CapturedPayershortList,
					ProjectVariables.CapturedInsuranceList, ProjectVariables.CapturedClaimtypesList, "MP");

			refOppurtunityDeck.Validate_filter_drawer_section_with_DB(
					Serenity.sessionVariableCalled("MPwithsavings").toString(), "MP");

			break;
		case "Topics with filter":
			// DB Method to retrieve the MPs and their Rawsavings
			MongoDBUtils.GettheMPTopicsAndDPsBasedOnPayerLOBClaimTypes(ProjectVariables.CapturedPayershortList,
					ProjectVariables.CapturedInsuranceList, ProjectVariables.CapturedClaimtypesList, "Topic");

			refOppurtunityDeck.Validate_filter_drawer_section_with_DB(
					Serenity.sessionVariableCalled("Topicwithsavings").toString(), "Topic");

			break;
		case "DPKey":
			// DB Method to retrieve the MPs and their Rawsavings
			MongoDBUtils.GettheMPTopicsAndDPsBasedOnPayerLOBClaimTypes(Payers, LOBS, Claimtypes, "DPKey");

			refOppurtunityDeck.Validate_filter_drawer_section_with_DB(
					Serenity.sessionVariableCalled("DPDetails").toString(), "DPKey");

			break;
		case "DPKey with filter":
			// DB Method to retrieve the MPs and their Rawsavings
			MongoDBUtils.GettheMPTopicsAndDPsBasedOnPayerLOBClaimTypes(ProjectVariables.CapturedPayershortList,
					ProjectVariables.CapturedInsuranceList, ProjectVariables.CapturedClaimtypesList, "DPKey");

			refOppurtunityDeck.Validate_filter_drawer_section_with_DB(
					Serenity.sessionVariableCalled("DPDetails").toString(), "DPKey");
			break;
		case "PayerLOBGrid in presentationview":
			// DB Method to retrieve the MPs and their Rawsavings
			MongoDBUtils.GettheMPTopicsAndDPsBasedOnPayerLOBClaimTypes(Payers, LOBS, Claimtypes, "PPS");

			refOppurtunityDeck.Validate_filter_drawer_section_with_DB(
					Serenity.sessionVariableCalled("DPDetails").toString(), "PayerLOBGrid in presentationview");

			break;
		default:
			Assert.assertTrue("Case not found =>" + criteria, false);
			break;
		}

	}

	@Step
	public void verifyDPInAvailableOppurtunityDeck() {

		StringUtils.replace(refOppurtunityDeck.DPKeyDynamicXpath, "DPKeyArg",
				Serenity.sessionVariableCalled("DPkey").toString());
		GenericUtils.Verify(
				"DP " + Serenity.sessionVariableCalled("DPkey")
				+ " is displayed in Available oppurtunity deck after finalizing all decisions",
				!refSeleniumUtils.is_WebElement_Displayed(refOppurtunityDeck.DPKeyDynamicXpath));

	}

	@Step
	public void captureValuesFromCollectionForRuleRelationshipType(String valToCapture, String clientName,
			String dispositionToApply, String collectionName, String reqRelationship) {
		String ClientKey = MongoDBUtils.Retrieve_the_PayerKey_From_mongodb_with_the_given_Payershort(clientName,
				"Client");
		Serenity.setSessionVariable("ClientKey").to(ClientKey);
		MongoDBUtils.getValuesForRuleRelationship(valToCapture, ClientKey, dispositionToApply, collectionName,
				reqRelationship);
	}

	@Step
	public void assignDPstoThePresentationAtLevel(String dPKeySource, String presentation, String assignmentLevel)
			throws ElementNotFoundException {
		refOppurtunityDeck.assignToPresentation(dPKeySource, presentation, assignmentLevel);

	}

	@Step
	public void validateRuleRelationshipIconandText(String validteForDPCardorLOBGrid) {

		refOppurtunityDeck.validateRuleRelationshipIconDetails(validteForDPCardorLOBGrid);
	}

	@Step
	public void navigateToTheDPCard(String capturedOrDB, String deckName) throws ElementNotFoundException {
		String DPKey = "";
		WebElement DPkeyElement = null;
		List<WebElement> DPkeyElementVal = new ArrayList<WebElement>();
		String DPCard = "//mat-card-header//label[text()='DPVal']//ancestor::mat-card";
		String DPCardXpath = "";
		ArrayList<String> DPKeysList = new ArrayList<String>();
		boolean DPKeyFound = false;

		DPKeysList = Serenity.sessionVariableCalled("DPKeyList");

		if (DPKeysList == null) {
			DPKeysList = new ArrayList<String>();
			DPKeysList.add(Serenity.sessionVariableCalled("DPkey").toString());
		}

		for (int k = 0; k < DPKeysList.size(); k++) {
			String DPXpath = StringUtils.replace(refOppurtunityDeck.DPKeyDynamicXpath, "DPKeyArg",
					"DP " + DPKeysList.get(k));
			if (refSeleniumUtils.is_WebElement_Displayed(DPXpath)) {
				DPKeyFound = true;
				DPKey = DPKeysList.get(k).trim();
				break;
			}
		}

		if (DPKeyFound == false) {
			Assert.assertTrue(
					" DPcard fetched from DB is not avaiable in the PM to assign to presentation,DPkey::" + DPKey,
					true);
			getDriver().quit();
		}

		// Click on the DP Card
		if (capturedOrDB.equalsIgnoreCase("CapturedDPKey")) {
			DPKey = "DP " + DPKey;
			DPCardXpath = StringUtils.replace(DPCard, "DPVal", DPKey);
		}

		if (refSeleniumUtils.is_WebElement_Displayed(DPCardXpath)) {
			SeleniumUtils.scrollingToGivenElement(getDriver(), DPCardXpath);
			refSeleniumUtils.highlightElement(DPCardXpath);
		}

		Serenity.setSessionVariable("DPKey").to(DPKey);

	}

	@Step
	public void captureCorrespondingDPsandRules(String dPKey) {
		refOppurtunityDeck.captureRelatedDPsandRules(dPKey);
	}

	@Step
	public void validateTheAssignmentInfoMsg(String expectedCondition, String expectedMessage) {

		String UIInfoMessage = "";

		refSeleniumUtils.defaultWait(ProjectVariables.TImeout_2_Seconds);

		switch (expectedCondition.toUpperCase()) {

		case "DISPLAY":

			try {
				WebDriverWait wait1 = new WebDriverWait(getDriver(), 10, ProjectVariables.ExplicitWait_PollTime);
				wait1.until(
						ExpectedConditions.visibilityOfElementLocated(By.xpath(refOppurtunityDeck.InfoMessageXpath)));
			} catch (Exception e) {
			}

			refSeleniumUtils.highlightElement(refOppurtunityDeck.InfoMessageXpath);
			UIInfoMessage = refSeleniumUtils.get_TextFrom_Locator(refOppurtunityDeck.InfoMessageXpath);

			if (expectedMessage.trim().equalsIgnoreCase(UIInfoMessage.trim())) {
				Assert.assertTrue("Warning message is displayed", true);
				System.out.println("UI Info Message:" + UIInfoMessage);
				System.out.println("Expected  Info Message:" + expectedMessage);
			} else {
				System.out.println("UI Info Message:" + UIInfoMessage);
				System.out.println("Expected  Info Message:" + expectedMessage);
				Assert.assertTrue("Info message is not displayed", false);
				getDriver().quit();
			}
			break;

		case "SHOULD NOT DISPLAY":

			try {
				WebDriverWait wait1 = new WebDriverWait(getDriver(), 10, ProjectVariables.ExplicitWait_PollTime);
				wait1.until(
						ExpectedConditions.invisibilityOfElementLocated(By.xpath(refOppurtunityDeck.InfoMessageXpath)));
			} catch (Exception e) {
			}

			if (!(refSeleniumUtils.is_WebElement_Displayed(refOppurtunityDeck.InfoMessageXpath))) {
				Assert.assertTrue("Info message is not displayed as expected", true);
			} else {
				Assert.assertTrue("Info message is displayed which is not expected", false);
				getDriver().quit();
			}
			break;
		}

	}

	
	public void assignMultipleMedicalPoliciestoPresentationProfile(String MedPolicyCount) 
	{
		
		 int MedicalPolicyCount = Integer.parseInt(MedPolicyCount);
		
		 //Capture the DPNos before assigning for verification
		 List<String>  OppDeckDPsVals  = new  ArrayList();		
		 OppDeckDPsVals = refSeleniumUtils. getWebElementValuesAsList(refOppurtunityDeck.sOppDeckAllDPs);
		 Serenity.setSessionVariable("OppDeckDpValues").to(OppDeckDPsVals);	 
		  List<WebElement>  AssignIcons =  new ArrayList();		
		    				   
			try{	
		   			AssignIcons =  getDriver().findElements(By.xpath("//label[contains(text(),'Medical Policy:')]/..//following-sibling::div//i"));		
		   		  int loopcounter= 0;
				  if(AssignIcons.size()<2)
				  {
					  Assert.assertTrue("Exception Message::"+"There is only one Medical Policy so cannot proceed further with scenario validation",false);
				  }
				  else if (AssignIcons.size()>MedicalPolicyCount)
				  {
					   loopcounter = MedicalPolicyCount;
				  }
				  else
				  {
					  loopcounter = AssignIcons.size();
				  }
				  
				   for  (int k=0;k<loopcounter;k++)
				   {		
					   refSeleniumUtils.defaultWait(ProjectVariables.TImeout_2_Seconds);	
					   refSeleniumUtils.clickGivenXpath("(//label[contains(text(),'Medical Policy:')]/..//following-sibling::div//i)["+(k+1)+"]");	
					    String sAssignChkBox =   StringUtils.replace(refOppurtunityDeck.sPresentationAssignmentCheckbox,"val",Serenity.sessionVariableCalled("PresentationName"));				    
					    WebElement we = getDriver().findElement(By.xpath(sAssignChkBox));
					    we.click();							  				 
					    refSeleniumUtils.Click_given_Locator(refOppurtunityDeck.sPresentationAssignmentOKBtn);
					    refSeleniumUtils.waitForContentLoad();						
					    refOppurtunityDeck.applyAvailableDPsFilter("NotStartedOnly");
					    refSeleniumUtils.waitForContentLoad();
					    AssignIcons =  getDriver().findElements(By.xpath(refOppurtunityDeck.sMedPolicyAssignmentIcons));			   
				   }	
			}
			catch (Exception e) {			
			System.out.println("Exception Message::"+e.getMessage());
			getDriver().quit();
			GenericUtils.Verify("Exception Message::"+e.getMessage(), false);				
			}		
		
	}
	
	//**************** Sorting ****************************>
	
		@Step
		public void validate_the_sorting_functionality_in_deck(String SortbyOptions, String Deckname) throws ParseException, IOException, InterruptedException 
		{
			//Serenity.setSessionVariable("clientkey").to("11");
			//Serenity.setSessionVariable("PresentationName").to("AutoService16168");
			boolean bstatus=false;
			
			switch(Deckname)
			{
			case "Availble Opportunity":
				//Method to validate the sorting in avaialbel opportunity deck
				refOppurtunityDeck.validateSortingfunctionalityinAvailableOpportunitydeck(SortbyOptions);
			break;
			case "PresentatinDeck":
				System.out.println("PresentationName==>"+Serenity.sessionVariableCalled("PresentationName"));
				bstatus=MongoDBUtils.GetAvailableDPKeyfromAvailableOpportunityDeck(Serenity.sessionVariableCalled("clientkey"), "Multiple", 1, 1, 1);
				if(!bstatus)
				{
					//Method to capture the disposition through service from MongoDB
					oCPWPage.Capture_the_disposition_through_service_from_MongoDBData(Serenity.sessionVariableCalled("clientkey"),"Multiple",Serenity.sessionVariableCalled("User"));

				}
				//Assigning captured DPkeys to created presentation 
				refPresentationDeck.assignMultipleDPstoCreatedProfile();
				getDriver().navigate().refresh();
				refSeleniumUtils.waitForContentLoad();
				
				//Click on the created presentation
				refPresentationProfile.clickPresentationProfile();
				//Method to validate the sorting in Presentation profile
				refOppurtunityDeck.validateSortingfunctionalityinPresentationdeck(SortbyOptions);
				
			break;
			default:
				Assert.assertTrue("Case not found===>"+Deckname, false);
			break;
			
			}
			
		}
    
		@Step
		public void userShouldSelectTab(String tabName) 
		{
			oGenericUtils.clickOnElement(refOppurtunityDeck.NPPDrodown);
			oGenericUtils.isElementExist(StringUtils.replace(oCPWPage.Span_contains_text, "value", tabName));
			oGenericUtils.clickOnElement(StringUtils.replace(oCPWPage.Span_contains_text, "value", tabName));
			oGenericUtils.waitForContentLoad();
			refSeleniumUtils.defaultWait(ProjectVariables.TImeout_3_Seconds);
		}
		
		@Step
		public void usershouldviewallcoloumnin(String tabName) 
		{
			boolean bstatus=false;
			//verify all the coloumns displayed in change opportunities section
			for (int i = 0; i < ProjectVariables.changeOpportunitiesCol.length; i++) 
			{
				oGenericUtils.isElementExist(StringUtils.replace(oCPWPage.changeOppCol, "value", ProjectVariables.changeOpportunitiesCol[i].trim()));
			}
		}
}
