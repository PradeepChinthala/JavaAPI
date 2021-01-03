package projects.steps.definitions;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;
import org.junit.Assert;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import net.serenitybdd.core.Serenity;
import net.thucydides.core.annotations.Step;
import net.thucydides.core.steps.ScenarioSteps;
import project.exceptions.ElementNotFoundException;
import project.pageobjects.FilterDrawer;
import project.pageobjects.OppurtunityDeck;
import project.pageobjects.PresentationDeck;
import project.pageobjects.PresentationProfile;
import project.pageobjects.PresentationProfileValidations;
import project.utilities.AppUtils;
import project.utilities.GenericUtils;
import project.utilities.MongoDBUtils;
import project.utilities.SeleniumUtils;
import project.variables.ProjectVariables;
import project.pageobjects.CPWPage;

public class PresentationDeckStepDef extends ScenarioSteps {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * 
	 */

	PresentationDeck refPresentationDeck;
	SeleniumUtils objSeleniumUtils;
	AppUtils refAppUtils;
	MongoDBUtils refMongoDBUtils = new MongoDBUtils();
	OppurtunityDeck refOppurtunityDeck;
	FilterDrawer oFilterDrawer;
	GenericUtils oGenericUtils;
	PresentationProfile refPresentationProfile;
	PresentationProfileValidations refPresProfileValidations;
	CPWPage refCPWPage;

	@Step
	public void validateDPCardsAssignmentfromOppDeck() {
		List<String> PresDeckDPValues = new ArrayList();
		List<String> OppDeckDPValues = new ArrayList();
		boolean bDPPresent = false;

		OppDeckDPValues = Serenity.sessionVariableCalled("OppDeckDpValues");
		PresDeckDPValues = objSeleniumUtils
				.getWebElementValuesAsList(refPresentationDeck.PresentationContainerMedPolicyAllDPs);

		if (OppDeckDPValues.size() == PresDeckDPValues.size()) {
			for (int r = 0; r < OppDeckDPValues.size(); r++) {
				bDPPresent = false;
				for (int t = 0; t < PresDeckDPValues.size(); t++) {
					if (OppDeckDPValues.get(r).equalsIgnoreCase(PresDeckDPValues.get(t))) {
						bDPPresent = true;
						System.out.println("DP Value present in the PresentationDeck::" + OppDeckDPValues.get(r));
						break;
					}

				}
			}
		} else {
			Assert.assertTrue(
					"All the DP cards assigned to the respective PresentationProfile not displayed in the PresentationDeck",
					false);
		}
		if (bDPPresent == true) {
			Assert.assertTrue(
					"All the DP cards assigned to the respective PresentationProfile displayed in the PresentationDeck",
					true);
		} else {
			Assert.assertTrue(
					"All the DP cards assigned to the respective PresentationProfile not displayed in the PresentationDeck",
					false);
		}

	}

	@Step
	public void validatePresentationDeckSorting(String sSortingCheckType, String arg2) {
		List<String> MedicalPolicyTitles = new ArrayList();
		List<String> MedicalPolicyTitlesTrimmed = new ArrayList();
		Map<String, List<String>> MedicalPolicyAssginedTopics = new HashMap<String, List<String>>();
		Map<String, List<String>> MedicalPolicyAssginedTopicsTrimmed = new HashMap<String, List<String>>();
		Map<String, List<String>> TopicwiseDPs = new HashMap<String, List<String>>();

		// Expand All here
		oGenericUtils.clickOnElement("u", "Expand All");

		MedicalPolicyTitles = objSeleniumUtils
				.getWebElementValuesAsList(refPresentationDeck.PresentationContainerAllMedPolicies);

		MedicalPolicyTitles = objSeleniumUtils
				.getWebElementValuesAsList(refPresentationDeck.PresentationContainerAllMedPolicies);

		switch (sSortingCheckType) {
		case "MedicalPolicies":
			String sMedValue[];
			String sMedValue1 = "";
			String sMedValue2 = "";
			for (int r = 0; r < MedicalPolicyTitles.size(); r++) {
				sMedValue1 = (MedicalPolicyTitles.get(r).split(":"))[1];
				sMedValue2 = (sMedValue1.split("Policy"))[0];
				sMedValue2 = sMedValue2 + "Policy";
				MedicalPolicyTitlesTrimmed.add(sMedValue2);
			}

			Serenity.setSessionVariable("MedicalPoliesTrimmed").to(MedicalPolicyTitlesTrimmed);
			boolean isSorted = true;
			if (MedicalPolicyTitlesTrimmed.size() > 1) // if there is more than
														// one Medical Policy
														// then check for
														// Sorting
			{
				for (int i = 0; i < MedicalPolicyTitlesTrimmed.size() - 1; i++) {
					// Current String is > than the next one (if there are equal
					// list is still sorted)
					if (MedicalPolicyTitlesTrimmed.get(i)
							.compareToIgnoreCase(MedicalPolicyTitlesTrimmed.get(i + 1)) > 0) {
						isSorted = false;
						break;
					}
					System.out.println("Medical Policy ::" + MedicalPolicyTitlesTrimmed.get(i) + "<"
							+ MedicalPolicyTitlesTrimmed.get(i + 1));
				}
			}
			if (isSorted == true) {
				Assert.assertTrue("All the MedicalPolicies in the PresentationDeck are Sorted", true);
			} else {
				Assert.assertTrue("All the MedicalPolicies in the PresentationDeck are Not Sorted", false);
				getDriver().quit();
			}

			break;

		case "Topics":

			int LeftBraceCount = 0;
			String sTopicValue1 = "";
			String sTopicValue2 = "";
			int index = 0;
			String sTopicsXPath = "";
			boolean isSortedTopics = true;
			List<String> MedPoliciesTrimmed = new ArrayList<String>();
			MedPoliciesTrimmed = Serenity.sessionVariableCalled("MedicalPoliesTrimmed");
			// Put all the Topics related to MedicalPolicy in different HashMaps
			for (int i = 0; i < MedPoliciesTrimmed.size(); i++) {
				sTopicsXPath = "";
				// sTopicsXPath =
				// StringUtils.replace(refPresentationDeck.TopicsForEachMedPolicy,"MedPolicyArg","Medical
				// Policy: "+MedicalPolicyTitles.get(i).trim());
				sTopicsXPath = StringUtils.replace(refPresentationDeck.TopicsForEachMedPolicy, "MedPolicyArg",
						"Medical Policy: " + MedPoliciesTrimmed.get(i).trim());
				List<String> TopicsTitles = objSeleniumUtils.getWebElementValuesAsList(sTopicsXPath);
				Serenity.setSessionVariable("TopicsTitleswithDPNos").to(TopicsTitles);

				MedicalPolicyAssginedTopics.put(MedicalPolicyTitles.get(i).trim(), TopicsTitles);

				List<String> TopicsTitlesTrimmed = new ArrayList();
				// Put Trimmed ones Separately
				// Put all the Topics related to MedicalPolicy in different
				// HashMaps
				for (int k = 0; k < TopicsTitles.size(); k++) {
					String sTopic1 = TopicsTitles.get(k);
					String sTopicValueNew = ((sTopic1.split(":"))[1]).trim();
					String sTemp = (sTopicValueNew.split("\\(\\d|\\dd\\s\\BDPs"))[0];
					sTopicValue2 = sTemp.trim();
					TopicsTitlesTrimmed.add(sTopicValue2);

				} // End of Trimmed Topics For
				Serenity.setSessionVariable("TopicsNames").to(TopicsTitlesTrimmed);
				MedicalPolicyAssginedTopicsTrimmed.put(MedicalPolicyTitles.get(i).trim(), TopicsTitlesTrimmed);

			} // End of All Medical Policies For

			// Check if Sorting applied for The Topics for each Medical Policy

			Set<String> MedPolicies = MedicalPolicyAssginedTopicsTrimmed.keySet();
			for (String key : MedPolicies) {

				List<String> TopicVals = MedicalPolicyAssginedTopicsTrimmed.get(key); // Put
																						// the
																						// returned
																						// List
																						// values
																						// in
																						// a
																						// List

				if (TopicVals.size() > 1) // Check for Sorting order if there is
											// more than one Topic in the
											// Medical Policy
				{
					for (int i = 0; i < TopicVals.size() - 1; i++) {
						// current String is > than the next one (if there are
						// equal list is still sorted)
						if (TopicVals.get(i).compareToIgnoreCase(TopicVals.get(i + 1)) > 0) {
							isSortedTopics = false;
							break;
						}
						System.out.println("Topics in Medical Policy  ::" + key + "-->" + TopicVals.get(i) + "<"
								+ TopicVals.get(i + 1));
					}
					// System.out.println("String Sorted for
					// Topic::"+key+"is"+isSortedTopics);
				}
			}
			if (isSortedTopics == true) {
				Assert.assertTrue("All the Topics under MedicalPolicy in the PresentationDeck are Sorted", true);
			} else {
				Assert.assertTrue("All the Topics under MedicalPolicy in the PresentationDeck are Not Sorted", false);
				getDriver().quit();
			}

			Serenity.setSessionVariable("MedicalPolicies-Topics").to(MedicalPolicyAssginedTopics);
			Serenity.setSessionVariable("MedicalPolicies-TopicsActualNames").to(MedicalPolicyAssginedTopicsTrimmed);

			break;

		case "DPs":
			boolean isSortedDPs = true;
			List<String> TopicsTitlesTrimmed = new ArrayList();
			Map<String, List<String>> PolicyTopicMap = Serenity.sessionVariableCalled("MedicalPolicies-Topics");

			TopicsTitlesTrimmed = Serenity.sessionVariableCalled("TopicsNames");

			if (TopicsTitlesTrimmed.size() > 1) // if there is more than one
												// Medical Policy then check for
												// Sorting
			{
				for (int k = 0; k < TopicsTitlesTrimmed.size() - 1; k++) {
					// String sDPsXPath1 =
					// StringUtils.replace(refPresentationDeck.DPsForEachTopic,"arg1",key);
					String sDPsXPath2 = StringUtils.replace(refPresentationDeck.DPsForEachTopic, "TopicArg",
							"Topic: " + TopicsTitlesTrimmed.get(k).trim());
					List<String> DPs = objSeleniumUtils.getWebElementValuesAsList(sDPsXPath2);

					TopicwiseDPs.put(TopicsTitlesTrimmed.get(k).trim(), DPs); // Put
																				// TopicWise
																				// DPs
																				// in
																				// HashMap

					// Check sorting for DPs

					if (DPs.size() > 1) // Check for Sorting order if there is
										// more than one Topic in the Medical
										// Policy
					{
						for (int i = 0; i < DPs.size() - 1; i++) {
							// Get the Integer Part and compare as the Prefix DP
							// is same for all
							int iDPNo1 = Integer.parseInt((DPs.get(i).split("DP"))[1].trim());
							int iDPNo2 = Integer.parseInt((DPs.get(i + 1).split("DP"))[1].trim());

							if (iDPNo1 > iDPNo2) {
								isSortedDPs = false;
								break;
							}
							System.out.println("DPs in Topic-->" + TopicsTitlesTrimmed.get(k) + "-->" + DPs.get(i) + "<"
									+ DPs.get(i + 1));
						}

					} // end of If

				}
			}
			if (isSortedDPs == true) {
				Assert.assertTrue("All the DPs under a Topic in the PresentationDeck are Sorted", true);
			} else {
				Assert.assertTrue("All the DPs under a Topic in the PresentationDeck are Mot Sorted", false);
				getDriver().quit();
			}

			break;
		}

	}

	@Step
	public void validateTopicsEditIcons() {
		int iLoopCounter = 0;

		oGenericUtils.clickOnElement("u", "Expand All");
		objSeleniumUtils.defaultWait(3);

		// Get all the topics from serenity session variables
		List<String> TopicsTitlesWithDPs = objSeleniumUtils.getWebElementValuesAsList(refPresentationDeck.sAllTopics);
		Serenity.setSessionVariable("TopicsTitleswithDPNos").to(TopicsTitlesWithDPs);

		if (TopicsTitlesWithDPs.size() > 2) {
			iLoopCounter = 2;
		} else {
			iLoopCounter = TopicsTitlesWithDPs.size();
		}

		String sEditXpath = "";
		for (int i = 0; i < iLoopCounter; i++) {
			sEditXpath = StringUtils.replace(refPresentationDeck.sTopicEditicon, "Arg",
					TopicsTitlesWithDPs.get(i).trim());

			if (objSeleniumUtils.is_WebElement_Displayed(sEditXpath)) {
				objSeleniumUtils.highlightElement(sEditXpath);
				Assert.assertTrue("Edit Icon displayed for the Topic::" + TopicsTitlesWithDPs.get(i), true);
			} else {
				Assert.assertTrue("Edit Icon not displayed for the Topic::" + TopicsTitlesWithDPs.get(i), false);
			}
		}

	}

	@Step
	public void validateEditTopicDescrBtns() {
		List<String> TopicsTitlesWithDPs = Serenity.sessionVariableCalled("TopicsTitleswithDPNos");
		String sEditXpath = "";
		int iLoopCounter = 0;

		if (TopicsTitlesWithDPs.size() > 2) {
			iLoopCounter = 1;
		} else {
			iLoopCounter = TopicsTitlesWithDPs.size();
		}

		// Capture the first edit topic title
		int count = 0;
		int pos = 0;

		String TopicTitleUI = objSeleniumUtils.get_TextFrom_Locator(refPresentationDeck.FirstTopicLabel);

		// Extract the actual Topic Name from UI value to pass it for DB query
		String temp0 = (TopicTitleUI.split("Topic:"))[1].trim();
		String temp1 = temp0.split("DP")[0]; // Split by "DP" text Ex: Topic:
												// Abatacept (J0129) (200
												// DP(s))"
		for (int k = 0; k < temp1.length(); k++) // Capture the count of Left
													// paranthesis
		{
			char c = temp1.charAt(k);
			if (c == '(') {
				count = count + 1;
				pos = k;
			}
		}
		String TopicName = temp0.substring(0, pos).trim();
		Serenity.setSessionVariable("TopicName").to(TopicName);

		for (int i = 0; i < iLoopCounter; i++) {
			sEditXpath = StringUtils.replace(refPresentationDeck.sTopicEditicon, "Arg",
					TopicsTitlesWithDPs.get(i).trim());

			objSeleniumUtils.highlightElement(sEditXpath);
			objSeleniumUtils.Click_given_Locator(sEditXpath);
			objSeleniumUtils.waitForElement(refPresentationDeck.sEditDescriptionSaveBtn, "shouldBevisible", 4);
			objSeleniumUtils.defaultWait(ProjectVariables.TImeout_3_Seconds);

			// Save Button
			if (objSeleniumUtils.is_WebElement_Displayed(refPresentationDeck.sEditDescriptionSaveBtn)) {
				objSeleniumUtils.highlightElement(refPresentationDeck.sEditDescriptionSaveBtn);
				Assert.assertTrue("Save button is displayed in the EditTopicDescription popup", true);
			} else {
				Assert.assertTrue("Save button is not displayed in the EditTopicDescription popup", false);
			}

			// Submit Button
			if (objSeleniumUtils.is_WebElement_Displayed(refPresentationDeck.sEditDescriptionSubmitBtn)) {
				objSeleniumUtils.highlightElement(refPresentationDeck.sEditDescriptionSubmitBtn);
				Assert.assertTrue("Submit button is displayed in the EditTopicDescription popup", true);
			} else {
				Assert.assertTrue("Submit button is not displayed in the EditTopicDescription popup", false);
			}

			// Cancel Button
			if (objSeleniumUtils.is_WebElement_Displayed(refPresentationDeck.sEditDescriptionCancelBtn)) {
				objSeleniumUtils.highlightElement(refPresentationDeck.sEditDescriptionCancelBtn);
				Assert.assertTrue("Cancel button is displayed in the EditTopicDescription popup", true);
				objSeleniumUtils.waitForElement(sEditXpath, "shouldBevisible", 4);
			} else {
				Assert.assertTrue("Cancel button is not displayed in the EditTopicDescription popup", false);
			}

		}
		// objSeleniumUtils.defaultWait(2);

	}

	@Step
	public void validateProflNameinHeader() {
		Actions actions = new Actions(getDriver());
		// Scroll Left
		for (int j = 1; j < 28; j++) {
			actions.sendKeys(Keys.ARROW_LEFT).perform();
		}

		String sPresName = Serenity.sessionVariableCalled("PresentationName");
		String sNameXPath = StringUtils.replace(refPresentationDeck.PresDeckHeaderName, "val", sPresName);

		if (objSeleniumUtils.is_WebElement_Displayed(sNameXPath)) {

			objSeleniumUtils.highlightElement(sNameXPath);

			Assert.assertTrue("PresentationProfile Name is displayed in the Header section", true);
		} else {
			Assert.assertTrue("PresentationProfile Name is not displayed in the Header section", false);
		}

	}

	@Step
	public void validateDPNoOnDPCards() throws ElementNotFoundException {

		List<WebElement> PresDeckDPCards = new ArrayList();
		List<WebElement> PresDeckDPValElements = new ArrayList();
		List<String> PresDeckDPValues = new ArrayList();
		boolean bDPNoPresent = false;

		PresDeckDPValues = objSeleniumUtils
				.getWebElementValuesAsList(refPresentationDeck.PresentationContainerMedPolicyAllDPs);
		PresDeckDPCards = objSeleniumUtils.getElementsList("XPATH", refPresentationDeck.sAllDPcards);
		PresDeckDPValElements = objSeleniumUtils.getElementsList("XPATH",
				refPresentationDeck.PresentationContainerMedPolicyAllDPs);

		for (int r = 0; r < PresDeckDPCards.size(); r++) {
			objSeleniumUtils.evaluateJavascript("arguments[0].scrollIntoView(true);", PresDeckDPCards.get(r));
			objSeleniumUtils.highlightElement(PresDeckDPCards.get(r));
			objSeleniumUtils.highlightElement(PresDeckDPValElements.get(r));
			String sDPNo = PresDeckDPValues.get(r);
			if (!sDPNo.isEmpty()) {
				Assert.assertTrue("DPNo is displayed on the DP cards", true);
			} else {
				Assert.assertTrue("DPNo is not displayed on the DP cards", false);
				break;
			}
		}

	}

	@Step
	public void validateDPCardCheckBoxes(String sDeckName) throws ElementNotFoundException {
		// refAppUtils.validateDeckCheckboxes(sDeckName);
	}

	@Step
	public void validateChkboxesSelection(String sActionOnChkbox, String sDeckName) throws ElementNotFoundException {
		// refAppUtils.validateDeckCheckboxesState(sActionOnChkbox,sDeckName);
	}

	@Step
	public void validateDPKeyDescriptionfromDB(String sName) {
		String sDPkeyDescrUI = "";
		String sDPkeyDescrDB = "";
		String sDPKeyNo = " ";
		Map<String, String> DPKeyDescMap = new HashMap<String, String>();

		// Get Client Name
		String sClientName = Serenity.sessionVariableCalled("SelectClientName");
		DPKeyDescMap = Serenity.sessionVariableCalled("DPKeysDescr");
		String sPresProfileName = Serenity.sessionVariableCalled("PresentationName");
		String sclientKey = MongoDBUtils.Retrieve_the_PayerKey_From_mongodb_with_the_given_Payershort(sClientName,
				"Client");

		Set<String> DPKeys = DPKeyDescMap.keySet();
		for (String DPKey : DPKeys) {
			sDPKeyNo = ((DPKey.split("DP"))[1]).trim(); // Split the DP value by
														// -DP- so that to pass
														// the DPNo to MongoDB
			sDPkeyDescrDB = refMongoDBUtils.getDPKeyDescriptionfromDB(sclientKey, sDPKeyNo, sPresProfileName).trim();
			sDPkeyDescrUI = DPKeyDescMap.get(DPKey).trim();
			if (sDPkeyDescrDB.compareTo(sDPkeyDescrUI) == 0) {
				Assert.assertTrue("DPKey Description from UI and MongoDB matched.UI DPKey Desc::" + sDPkeyDescrUI
						+ "-- MongoDB DPDescr::" + sDPkeyDescrDB, true);
				System.out.println("DPKey Description from UI and MongoDB matched.UI DPKey Desc::" + sDPkeyDescrUI
						+ "-- MongoDB DPDescr::" + sDPkeyDescrDB);
			} else {
				Assert.assertTrue("DPKey Description from UI and MongoDB not matched.UI DPKey Desc::" + sDPkeyDescrUI
						+ "-- MongoDB DPDescr::" + sDPkeyDescrDB, false);
			}
		}
	}

	@Step
	public void clickOnEditTopicBasedonCriteria(String sIconName, String sTopic, String sMedPolicy) {

		int count = 0;
		String sDPCount = "";
		int pos = 0;
		String sExpandProp = "";
		String TopicTitleUI = "";

		List<WebElement> ExpdPanel = new ArrayList<WebElement>();
		List<WebElement> TopicExpdPanel = new ArrayList<WebElement>();

		String CapturedTopicName = Serenity.sessionVariableCalled("TopicName");

		if (CapturedTopicName == null) {
			try {
				oGenericUtils.clickOnElement("u", "Expand All");

				// Click on EditIcon
				objSeleniumUtils.clickGivenXpath(refPresentationDeck.FirstTopicEditIcon);
				objSeleniumUtils.defaultWait(3);

				// Split this so that it have only topic name
				TopicTitleUI = objSeleniumUtils.get_TextFrom_Locator(refPresentationDeck.FirstTopicLabel);
				Serenity.setSessionVariable("UITopicName").to(TopicTitleUI);
			} catch (Exception e) {
				System.out.println("Error message::" + e.getMessage());
				Assert.assertTrue("Operations not performed", false);
			}
		} // end if

		else {
			Serenity.setSessionVariable("UITopicName").to(CapturedTopicName);
		}

		// Extract the actual Topic Name from UI value to pass it for DB query
		String temp0 = (TopicTitleUI.split("Topic:"))[1].trim();
		String temp1 = temp0.split("DP")[0]; // Split by "DP" text Ex: Topic:
												// Abatacept (J0129) (200
												// DP(s))"
		for (int k = 0; k < temp1.length(); k++) // Capture the count of Left
													// paranthesis
		{
			char c = temp1.charAt(k);
			if (c == '(') {
				count = count + 1;
				pos = k;
			}
		}

		String TopicName = temp0.substring(0, pos).trim();
		Serenity.setSessionVariable("EditedTopicName").to(TopicName);

	}

	@Step
	public void validateDPKeyDescToolTip(String sKey) throws ElementNotFoundException {

		WebElement element = null;
		String sTooltip = "";
		String DPKey = "";
		Map<String, String> DPKeyDescriptionMap = new HashMap<String, String>();
		int iLoopCounter = 0;

		List<WebElement> PresDeckDPCards = new ArrayList();
		PresDeckDPCards = objSeleniumUtils.getElementsList("XPATH",
				refPresentationDeck.PresentationContainerMedPolicyAllDPs);

		if (PresDeckDPCards.size() == 0) {
			Assert.assertTrue(
					"There are no DPs in the PresentationProfile,so cannot proceed further with Scenario execution...",
					false);
		}

		if (PresDeckDPCards.size() > 3) {
			iLoopCounter = 3;
		} else {
			iLoopCounter = PresDeckDPCards.size();
		}

		for (int m = 0; m < iLoopCounter; m++) {
			element = PresDeckDPCards.get(m);
			objSeleniumUtils.evaluateJavascript("arguments[0].scrollIntoView(true);", element);
			DPKey = element.getText().trim();
			objSeleniumUtils.moveToElement(element);
			objSeleniumUtils.waitForElement(refPresentationDeck.sDPKeyTooltipText, "shouldBevisible", 4);
			if (objSeleniumUtils.is_WebElement_Displayed(refPresentationDeck.sDPKeyTooltipText)) {
				sTooltip = getDriver().findElement(By.xpath(refPresentationDeck.sDPKeyTooltipText)).getText().trim();
				Assert.assertTrue("Tooltip::" + sTooltip + " is displayed when User hovers mouse for  DPKey::" + DPKey,
						true);
			} else {
				Assert.assertTrue(
						"Tooltip::" + sTooltip + " is not displayed when User hovers mouse for  DPKey::" + DPKey,
						false);
			}
			DPKeyDescriptionMap.put(DPKey, sTooltip);
			System.out.println("Tooltip::" + sTooltip);
			objSeleniumUtils.clickOn(element);
			PresDeckDPCards = objSeleniumUtils.getElementsList("XPATH",
					refPresentationDeck.PresentationContainerMedPolicyAllDPs);
		}
		Serenity.setSessionVariable("DPKeysDescr").to(DPKeyDescriptionMap);
	}

	@Step
	public void validateRawSavingsOnDPCardWithDB(String arg1) {
		String sDPkeyDescrUI = "";
		String sDPkeyDescrDB = "";
		String sDPKeyNo = " ";
		String sRawsavings_DB = "";
		String sRawsavings_UI = "";
		int m = 0;
		List<String> UISelectedpayershorts = new ArrayList<String>();
		List<String> UISelectedLOBs = new ArrayList<String>();
		Map<String, String> DPCardRawsavingMap = new HashMap<String, String>();
		List<WebElement> PresDeckDPCards = new ArrayList();

		try {
			PresDeckDPCards = objSeleniumUtils.getElementsList("XPATH",
					refPresentationDeck.PresentationContainerMedPolicyAllDPs);
		} catch (ElementNotFoundException e) {
			e.printStackTrace();
		}
		// Get Client Name
		String sClientName = Serenity.sessionVariableCalled("SelectClientName");
		String sclientKey = MongoDBUtils.Retrieve_the_PayerKey_From_mongodb_with_the_given_Payershort(sClientName,
				"Client");

		String sPresProfileName = Serenity.sessionVariableCalled("PresentationName");
		UISelectedpayershorts = Serenity.sessionVariableCalled("SelectedPayerShorts");
		UISelectedLOBs = Serenity.sessionVariableCalled("SelectedLOBs");

		DPCardRawsavingMap = Serenity.sessionVariableCalled("DPRawSavingsMap");
		Set<String> DPKeys = DPCardRawsavingMap.keySet();

		for (String DPKey : DPKeys) {
			objSeleniumUtils.evaluateJavascript("arguments[0].scrollIntoView(true);", PresDeckDPCards.get(m));
			objSeleniumUtils.highlightElement(PresDeckDPCards.get(m));
			sDPKeyNo = ((DPKey.split("DP"))[1]).trim(); // Split the DP value by
														// -DP- so that to pass
														// the DPNo to MongoDB
			sRawsavings_DB = MongoDBUtils.getRawsavingsforDPCard(sclientKey, sDPKeyNo, UISelectedpayershorts,
					UISelectedLOBs, sPresProfileName);
			sRawsavings_DB = "$" + sRawsavings_DB; // Prefixing with Dollar
													// symbol
			sRawsavings_UI = DPCardRawsavingMap.get(DPKey);
			sRawsavings_UI = sRawsavings_UI.replaceAll(",", ""); // Replacing
																	// the comma
																	// between
																	// the
																	// numbers
																	// with the
																	// empty
																	// value

			if (sRawsavings_DB.isEmpty()) // If DB value is empty ,suffix with 0
											// to compare with UI value $0
			{
				sRawsavings_DB = sRawsavings_DB + "0";
			}

			if ((sRawsavings_DB.compareTo(sRawsavings_UI) == 0)) {
				Assert.assertTrue("The Rawsavings for the DPCard::" + sDPKeyNo + " is matched with MongoDB value::"
						+ sRawsavings_DB + " for Presentation Profile::" + sPresProfileName, true);
				System.out.println("The Rawsavings for the DPCard::" + sDPKeyNo + "in UI::" + sRawsavings_UI
						+ "  is matched with MongoDB value::" + sRawsavings_DB + " for Presentation Profile::"
						+ sPresProfileName);
				GenericUtils
						.logMessage("The Rawsavings for the DPCard::" + sDPKeyNo + " is matched with MongoDB value::"
								+ sRawsavings_DB + " for Presentation Profile::" + sPresProfileName);
			} else {
				Assert.assertTrue("The Rawsavings for the DPCard::" + sDPKeyNo + "UI Value::" + sRawsavings_UI
						+ " is not matched with MongoDB value::" + sRawsavings_DB + " for Presentation Profile::"
						+ sPresProfileName, false);
				GenericUtils.logErrorMesage("The Rawsavings for the DPCard::" + sDPKeyNo + "UI Value::" + sRawsavings_UI
						+ " is not matched with MongoDB value::" + sRawsavings_DB + " for Presentation Profile::"
						+ sPresProfileName);
			}

			m = m + 1;
		}

	}

	@Step
	public void checkRAWSavingsforthePresentation(String sPayershorts, String sLOBs) throws ElementNotFoundException {

		Map<String, String> DP_RawSavingsMap = new HashMap<String, String>();

		List<String> PresDeckDPCardsKeys = new ArrayList();
		List<WebElement> PresDeckDPCards = new ArrayList();
		int iCounter = 0;
		PresDeckDPCardsKeys = objSeleniumUtils
				.getWebElementValuesAsList(refPresentationDeck.PresentationContainerMedPolicyAllDPs);
		PresDeckDPCards = objSeleniumUtils.getElementsList("XPATH",
				refPresentationDeck.PresentationContainerMedPolicyAllDPs);

		if (PresDeckDPCardsKeys.size() == 0) {
			Assert.assertTrue(
					"There are no DPs in the PresentationProfile,so cannot proceed further with Scenario execution...",
					false);
		}

		if (PresDeckDPCardsKeys.size() > 3) // Only checking for 3DPs if there
											// are more DPs
		{
			iCounter = 3;
		} else {
			iCounter = PresDeckDPCardsKeys.size();
		}

		boolean bRawSavingsavaialble = false;
		for (int m = 0; m < iCounter; m++) {
			String sRawSavingXpath = StringUtils.replace(refPresentationDeck.sDPRawSavingVal, "arg",
					PresDeckDPCardsKeys.get(m));
			objSeleniumUtils.evaluateJavascript("arguments[0].scrollIntoView(true);", PresDeckDPCards.get(m));
			objSeleniumUtils.highlightElement(PresDeckDPCards.get(m));
			String sDPRawSavings = objSeleniumUtils.getTexFfromLocator(sRawSavingXpath);
			if (!sDPRawSavings.isEmpty()) {
				bRawSavingsavaialble = true;
				DP_RawSavingsMap.put(PresDeckDPCardsKeys.get(m), sDPRawSavings);
			}
		} // End For

		if (bRawSavingsavaialble == true) {
			Serenity.setSessionVariable("DPRawSavingsMap").to(DP_RawSavingsMap);
		} else {
			Assert.assertTrue(
					"There are no Rawsavings for any DPCards in the PresentationProfile,so cannot proceed further with Scenario execution...",
					false);
		}
	}

	@Step
	public void verifyPresentationDeckHeader() {
		refPresentationDeck.verifyPresentationDeckHeader();
	}

	@Step
	public void clickPresentationDeckCloseIcon() {
		refPresentationDeck.clickOnCloseIconOfPresentationDeck();
	}

	@Step
	public void verifyTotalDPCount() {
		refPresentationDeck.verifyCountOfDPOfPresentationDeck();
	}

	@Step
	public void verifyDPCountForMedicalPolicies() {
		refPresentationDeck.verifyCountOfDPOfEachMedicalPolicy();
	}

	@Step
	public void verifyDPCountForEachTopic() {
		refPresentationDeck.verifyCountOfDPOfEachTopic();
	}

	@Step
	public void unAssigntheCreatedPresentationatAllLevels(String sLevel) {

		//Serenity.setSessionVariable("CreatedPresentation").to("CHC_Test2");

		String sPresentation = Serenity.sessionVariableCalled("PresentationName");

		clickonCreatedPresentation(sPresentation);

		String sSelectedValue = refPresentationDeck.unAssigntheCreatedPresentationatAllLevels(sPresentation, sLevel);

		Serenity.setSessionVariable(sLevel.trim()).to(sSelectedValue);

	}

	@Step
	public void clickonCreatedPresentation(String sPresentation) {

		String sXPath = "//span[contains(text() ,'" + sPresentation + "')]";
		oGenericUtils.clickOnElement(sXPath);

		String sXPresDeck = " //div[contains(text() ,'" + sPresentation + "')]";
		objSeleniumUtils.is_WebElement_Displayed(sXPresDeck);
	}

	@Step
	public void selectGivenMedicalPolicyorTopic(String sCategory) throws InterruptedException {

		String strCategory = Serenity.sessionVariableCalled(sCategory);

		refPresentationDeck.expandFilterDrawer();
		oFilterDrawer.user_unchecks_selectAllPolicies();
		oFilterDrawer.user_filters_by_clicking_on_Apply_for_Medical_Policy_Topic();
		refPresentationDeck.expandFilterDrawer();
		oGenericUtils.setValue(By.xpath("//input[@placeholder = 'Search for Medical Policy / Topic']"), strCategory);
		oGenericUtils.clickOnElement("//img[@class= 'searchimage']");
		oFilterDrawer.user_selects_given_value_from_Medical_Policy_Topic(strCategory);
	}

	@Step
	public void verifyPresentationIcons() {
		refPresentationDeck.verifyIconsOfPresentation();
	}

	@Step
	public void editTopicDescription(String sTopicDescription) {

		try {
			String sCurrentTopicDescr = objSeleniumUtils.get_TextFrom_Locator(refPresentationDeck.sEditDescription);
			// String sNewTopicDescr = sCurrentTopicDescr+sTopicDescription;

			objSeleniumUtils.enterTextinCotrol("XPATH", refPresentationDeck.sEditDescriptionTextArea, "");// delete
																											// the
																											// existing
																											// topic
																											// descr

			if (!sTopicDescription.equalsIgnoreCase("DeleteTopic"))// If it is
																	// not the
																	// action of
																	// Delete
																	// Topic
																	// then
																	// enter the
																	// new topic
																	// Description
			{
				objSeleniumUtils.enterTextinCotrol("XPATH", refPresentationDeck.sEditDescriptionTextArea,
						sTopicDescription);// enter new Topic Desc
				String sNewTopicDescrUI = objSeleniumUtils
						.get_TextFrom_Locator(refPresentationDeck.sEditDescriptionEnteredText);

				if (sTopicDescription.equalsIgnoreCase(sNewTopicDescrUI)) {
					Assert.assertTrue("Edited Topic description entered in UI EditTopic description TextBox", true);
				} else {
					Assert.assertTrue("Edited Topic description not entered in UI EditTopic description TextBox",
							false);
				}
				Serenity.setSessionVariable("EditedTopicDescr").to(sNewTopicDescrUI);
			}
		} catch (Exception e) {
			System.out.println("Error message::" + e.getMessage());
			Assert.assertTrue("Operations not performed", false);
		}
	}

	public void clickEditTopicButton(String sBtnName) {
		refPresentationDeck.ClickEditTopicButton(sBtnName);

	}

	@Step
	public void verifyElement(String sElement, String sExpectedValue) {
		refPresentationDeck.verifyElementState(sElement, sExpectedValue);

	}

	@Step
	public void clickOtherPresProfile(String sElementToClick) {
		// Created Presentation Name
		String sPresProfileName = Serenity.sessionVariableCalled("PresentationName");
		// Get all Pres Profile names
		// If it is not equal to currently created Pres Profile then click on it

		List<String> AllPresProfileNames = new ArrayList();
		List<WebElement> AllPresProfileElmnts = new ArrayList();

		AllPresProfileNames = objSeleniumUtils.getWebElementValuesAsList(refPresentationDeck.AllPresProfileNames);
		try {
			AllPresProfileElmnts = objSeleniumUtils.getElementsList("XPATH", refPresentationDeck.AllPresProfileNames);
		} catch (ElementNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		for (int k = 0; k < AllPresProfileNames.size(); k++) {
			if (!sPresProfileName.equalsIgnoreCase(AllPresProfileNames.get(k))) {
				objSeleniumUtils.clickOn(AllPresProfileElmnts.get(k));
				break;
			}
		}
	}

	@Step
	public void verifyEditDescriptionSaved(String sTopicDescription, String sExpectedState) {
		sExpectedState = sExpectedState.toUpperCase();

		String SavedTopicDescr = Serenity.sessionVariableCalled("EditedTopicDescr");
		String CurrentTopicDescr = objSeleniumUtils
				.get_TextFrom_Locator(refPresentationDeck.sEditDescriptionEnteredText);

		System.out.println("Last saved Topic Descr ::" + SavedTopicDescr);
		System.out.println("Current Topic Descr in UI::" + CurrentTopicDescr);

		switch (sExpectedState) {

		case "RETAINED":
			if (SavedTopicDescr.equalsIgnoreCase(CurrentTopicDescr)) {
				Assert.assertTrue("Last saved description retained ", true);
			} else {
				Assert.assertTrue("Last saved description not retained ", false);
			}
			break;

		case "DELETED":
			if (CurrentTopicDescr.isEmpty()) {
				Assert.assertTrue("Last saved description is deleted in UI ", true);
			} else {
				Assert.assertTrue("Last saved description is not  deleted in UI ", false);
			}
			break;
		}
	}

	@Step
	public void validateTopicDescwithDB(String sExpectedState, String sTopicDescr, String sPresProfile) {
		String EditedDescrDB = "";
		sExpectedState = sExpectedState.toUpperCase();
		String PresProfileID = "";

		String ClientName = Serenity.sessionVariableCalled("SelectClientName");
		String PresName = Serenity.sessionVariableCalled("PresentationName");
		String EditedTopicTitle = Serenity.sessionVariableCalled("EditedTopicName");
		String sEditedTopicDescrUI = Serenity.sessionVariableCalled("EditedTopicDescr");

		System.out.println("ClientName::" + ClientName);
		System.out.println("PresentationProfileName::" + PresName);
		System.out.println("TopicTitle::" + EditedTopicTitle);
		System.out.println("**********************************************");
		System.out.println("EditedTopicDescription in UI::" + sEditedTopicDescrUI);

		switch (sExpectedState) {

		case "SAVE":

			EditedDescrDB = refMongoDBUtils.retrieveEditTopicDescrDBValues(ClientName, PresName, EditedTopicTitle,
					"ValueToFetch-TopicDescription", "");

			if (sEditedTopicDescrUI.equalsIgnoreCase(EditedDescrDB)) {
				Assert.assertTrue("Updated Topic description in UI and DB matched.", true);
			} else {
				Assert.assertTrue("Updated Topic description in UI and DB not matched.", false);
			}
			PresProfileID = refMongoDBUtils.retrieveEditTopicDescrDBValues(ClientName, PresName, EditedTopicTitle,
					"ValueToFetch-PresentationProfileID", "");
			break;

		case "DELETE":

			PresProfileID = refMongoDBUtils.retrieveEditTopicDescrDBValues(ClientName, PresName, EditedTopicTitle,
					"ValueToFetch-PresentationProfileID", "");

			if (PresProfileID.isEmpty() || PresProfileID == null) {
				Assert.assertTrue(
						"Presentation profile ID and Edited Topic description are deleted in DB as all the DPs in the Topic are Unassigned/MP under which the Edited Topic is structured unassigned from Presentation.",
						true);
			} else {
				Assert.assertTrue(
						"Presentation profile ID and Edited Topic description are not deleted in DB as all the DPs in the Topic are Unassigned/MP under which the Edited Topic is structured unassigned  from Presentation",
						false);
			}
			break;

		}

	}

	@Step
	public void deleteTopicDescription(String sTopicDesc) {
		objSeleniumUtils.highlightElement(refPresentationDeck.sEditDescriptionEnteredText);
		objSeleniumUtils.enterTextinCotrol("XPATH", refPresentationDeck.sEditDescriptionTextArea, "");
		String sEditedDescrUI = objSeleniumUtils.get_TextFrom_Locator(refPresentationDeck.sEditDescriptionTextArea);

		if (sEditedDescrUI.isEmpty()) {
			Assert.assertTrue("Edited Topic description in UI is deleted", true);
		} else {
			Assert.assertTrue("Edited Topic description in UI is not deleted.", false);
		}

	}

	@Step
	public void validateButtonStatus(String sBtnName, String sExpectedProperty) {
		String sDisabledProperty = "";
		List<WebElement> Button = new ArrayList<WebElement>();

		sExpectedProperty = sExpectedProperty.toUpperCase();

		try {
			if (sBtnName.equalsIgnoreCase("Save")) {
				Button = objSeleniumUtils.getElementsList("XPATH", refPresentationDeck.sEditDescriptionSaveBtn);
			} else if (sBtnName.equalsIgnoreCase("Submit")) {
				Button = objSeleniumUtils.getElementsList("XPATH", refPresentationDeck.sEditDescriptionSubmitBtn);
			} else if (sBtnName.equalsIgnoreCase("Cancel")) {
				Button = objSeleniumUtils.getElementsList("XPATH", refPresentationDeck.sEditDescriptionCancelBtn);
			} else if(sBtnName.equalsIgnoreCase("DP Type")){
				objSeleniumUtils.defaultWait(ProjectVariables.TImeout_10_Seconds);
				Assert.assertTrue("Failed to display 'Presentation has no content yet'",objSeleniumUtils.is_WebElement_Displayed(StringUtils.replace(refCPWPage.Div_contains_Class, "value", "no-opp-msg")));
				Button = objSeleniumUtils.getElementsList("XPATH", StringUtils.replace(refPresentationProfile.Label_With_Contains, "sValue", sBtnName)+"/..//mat-select");
			}

			sDisabledProperty = Button.get(0).getAttribute("aria-disabled");
		}

		catch (Exception e) {
			System.out.println("Error message::" + e.getMessage());
			Assert.assertTrue("Operations not performed", false);
		}

		switch (sExpectedProperty) {
		case "ENABLED":

			if (sDisabledProperty.equalsIgnoreCase("false")) {
				objSeleniumUtils.highlightElement(Button.get(0));
				Assert.assertTrue("Button:" + sBtnName + "is in expected state::" + sExpectedProperty, true);
			} else {
				Assert.assertTrue("Button:" + sBtnName + "is not in expected state::" + sExpectedProperty, false);
			}

			break;

		case "DISABLED":

			if (sDisabledProperty.equalsIgnoreCase("true")) {
				objSeleniumUtils.highlightElement(Button.get(0));
				Assert.assertTrue("Button:" + sBtnName + "is in expected state::" + sExpectedProperty, true);
			} else {
				Assert.assertTrue("Button:" + sBtnName + "is not in expected state::" + sExpectedProperty, false);
			}

			break;

		}
	}

	@Step
	public void validatePopupClosed(String sPopupTitle) {
		boolean bPopupClosed = oGenericUtils.isElementExist(refPresentationDeck.EditPopup);

		if (bPopupClosed == true) {
			Assert.assertTrue("The Popup::" + sPopupTitle + " is closed ", true);
		} else {
			Assert.assertTrue("The Popup::" + sPopupTitle + " is not closed ", false);
		}

	}

	@Step
	public void clickButton(String sBtnName) {
		objSeleniumUtils.highlightElement(refPresentationDeck.EditSavedCloseIcon);
		boolean bClicked = oGenericUtils.clickOnElement(refPresentationDeck.EditSavedCloseIcon);

		if (bClicked == true) {
			Assert.assertTrue("The Button name::" + sBtnName + " is clicked ", true);
		} else {
			Assert.assertTrue("The Button name::" + sBtnName + " is not clicked ", false);
		}
	}

	@Step
	public void capureDPs(String sToCapture, String sCaptureFor) {

		String TopicNameUI = Serenity.sessionVariableCalled("UITopicName");
		List<String> DPValues = new ArrayList<String>();

		if (sCaptureFor.equalsIgnoreCase("Topic")) {
			// String DPsXPath =
			// StringUtils.replace(refPresentationDeck.TopicDPs, "TopicVal",
			// Serenity.sessionVariableCalled("TopicNameUI"));
			String DPsXPath = StringUtils.replace(refPresentationDeck.TopicDPs, "TopicVal", TopicNameUI);
			DPValues = objSeleniumUtils.getWebElementValuesAsList(DPsXPath);
			// String txt = objSeleniumUtils.get_TextFrom_Locator(DPsXPath) ;
			Serenity.setSessionVariable("PresentationTopicDPKeys").to(DPValues);

		} else if (sCaptureFor.equalsIgnoreCase("MedicalPolicy")) {

		}

	}

	@Step
	public void validateDPsinUI(String sScreenName, String scapturedFor, String sPresProfileName) {
		List<String> EditTopicDPKeys = new ArrayList<String>();
		List<String> PresTopicDPKeys = new ArrayList<String>();

		objSeleniumUtils.highlightElement(refPresentationDeck.EditTopicPopup_DPKeys);
		EditTopicDPKeys = objSeleniumUtils.getWebElementValuesAsList(refPresentationDeck.EditTopicPopup_DPKeys);
		PresTopicDPKeys = Serenity.sessionVariableCalled("PresentationTopicDPKeys");

		System.out.println("****Presentation Topic DP Keys::***");
		for (int k = 0; k < PresTopicDPKeys.size(); k++) {
			System.out.println(PresTopicDPKeys.get(k) + " , ");
		}

		System.out.println("****Edit TopicPopup DP Keys::***");
		for (int k = 0; k < EditTopicDPKeys.size(); k++) {
			System.out.println(EditTopicDPKeys.get(k) + " , ");
		}

		if (EditTopicDPKeys.equals(PresTopicDPKeys)) {
			Assert.assertTrue("All DP Keys assigned to the Presentation are displayed in the EditTopicPopup", true);
		} else {
			Assert.assertTrue("All DP Keys assigned to the Presentation are displayed in the EditTopicPopup", false);
		}

	}

	@Step
	public void validateDPDescrinPopup(String sRequiredVal, String popup, String Component) {

		// Click on Each DP and check whether description is displayed or not
		List<String> PresTopicDPKeys = new ArrayList<String>();
		PresTopicDPKeys = Serenity.sessionVariableCalled("PresentationTopicDPKeys");
		String DPKeyXPath = "";
		String DPKey = "";
		String DPKeyNo = "";
		String DPkeyDescrDB = "";
		int Counter = 0;

		String sClientName = Serenity.sessionVariableCalled("SelectClientName");
		String sclientKey = MongoDBUtils.Retrieve_the_PayerKey_From_mongodb_with_the_given_Payershort(sClientName,
				"Client");

		// Reducing the no of DP keys to validate if there are more DPKeys
		if (PresTopicDPKeys.size() > 2) {
			Counter = 2;
		} else {
			Counter = PresTopicDPKeys.size();
		}

		for (int k = 0; k < Counter; k++) {
			DPKey = PresTopicDPKeys.get(k).trim();
			DPKeyXPath = StringUtils.replace(refPresentationDeck.EditTopicDPKeys, "DPKeyVal", DPKey);
			objSeleniumUtils.highlightElement(DPKeyXPath);
			objSeleniumUtils.clickGivenXpath(DPKeyXPath);
			String sDPKeyDescrUI = objSeleniumUtils.get_TextFrom_Locator(refPresentationDeck.EditTopicPopup_DPDescr);

			if (!sDPKeyDescrUI.isEmpty()) // if DP Description is not empty
			{
				Assert.assertTrue(
						"DP Description is displayed for the DPKey when we select the key on DPs in the Topic section::"
								+ DPKey + " in the EditTopicPopup",
						true);
			} else {
				Assert.assertTrue(
						"DP Description is not displayed for the DPKey when we select the key on DPs in the Topic section::"
								+ DPKey + " in the EditTopicPopup",
						false);
			}

			// Validate DP Key description with DB
			DPKeyNo = ((DPKey.split("DP"))[1]).trim(); // Split the DP value by
														// -DP- so that to pass
														// the DPNo to MongoDB
			DPkeyDescrDB = refMongoDBUtils.getDPKeyDescriptionfromDB(sclientKey, DPKeyNo, "").trim();

			System.out.println("*******");
			System.out.println("DPKey::" + DPKey);
			System.out.println("UI Descr::" + sDPKeyDescrUI);
			System.out.println("DB Descr::" + DPkeyDescrDB);
			System.out.println("*******");
			System.out.println("\n");

			if (sDPKeyDescrUI.equalsIgnoreCase(DPkeyDescrDB)) // if DP
																// Description
																// is not empty
			{
				Assert.assertTrue(
						"DP Description is same as that of DB for DPKey::" + DPKeyNo + "DBDescr::" + DPkeyDescrDB,
						true);
			} else {
				Assert.assertTrue(
						"DP Description is not same as that of DB for DPKey::" + DPKeyNo + "DBDescr::" + DPkeyDescrDB,
						false);
			}

		} // end of For

	}

	@Step
	public void validateFieldInitialStatus(String sFieldToValidate, String sExpectedState, String sFieldContainer,
			String placeHolderArg1) {

		List<WebElement> ElementObjects = new ArrayList<WebElement>();
		boolean bFlag = false;

		try {

			switch (sFieldToValidate) {
			case "DPKey":
				ElementObjects = objSeleniumUtils.getElementsList("XPATH", refPresentationDeck.EditTopicPopup_DPKeys);

				for (int k = 0; k < ElementObjects.size(); k++) {
					bFlag = false;
					String ClassValue = objSeleniumUtils.getWebElementAttributeVal("class", ElementObjects.get(k));
					if (sExpectedState.equals("NotSelected")) {
						if (!ClassValue.contains("jqx-listitem-state-selected")) // if
																					// the
																					// class
																					// value
																					// does
																					// NOT
																					// contain
																					// Selected
																					// value
																					// then
																					// it
																					// is
																					// not
																					// selected
																					// by
																					// default
																					// as
																					// expected
						{
							bFlag = true;
						}
					}
				} // End For

				if (bFlag == true) {
					Assert.assertTrue("No DP Key in the list selected by default as expected", true);
					System.out.println("No DP Key in the list selected by default as expected");
				} else {
					Assert.assertTrue("DP Key in the list selected by default which is not expected", false);
					System.out.println("DP Key in the list selected by default which is not expected");
				}

				// Check DPKey description is blank or not
				String DPDescription = objSeleniumUtils
						.get_TextFrom_Locator(refPresentationDeck.EditTopicPopup_DPDescr);
				if (DPDescription.isEmpty()) {
					Assert.assertTrue("DPDescription is blank by default as expected", true);
				} else {
					Assert.assertTrue("DPDescription is  not blank by default", false);
				}

				break;
			}
		}

		catch (ElementNotFoundException e) {
			e.printStackTrace();
		}

		// EditTopicDPKeys =
		// objSeleniumUtils.getWebElementValuesAsList(refPresentationDeck.EditTopicPopup_DPKeys);
		// PresTopicDPKeys =
		// Serenity.sessionVariableCalled("PresentationTopicDPKeys");

	}

	public void validateFieldHeaders(String sFieldToValidate, String sFieldContainer, String sPlaceHolderArg1,
			String sPlaceHolderArg2) {

		switch (sFieldToValidate) {

		case "DPSection":

			// Validate DPDescription Section Header
			objSeleniumUtils.highlightElement(refPresentationDeck.DPKeySectionHeader);
			String SectionHeaderUI = objSeleniumUtils.getTexFfromLocator(refPresentationDeck.DPKeySectionHeader).trim();
			if (SectionHeaderUI.equalsIgnoreCase("DP(s) in this topic:")) {
				Assert.assertTrue("DPDescription section Header Title is displayed as Expected", true);
			} else {
				Assert.assertTrue(
						"DPDescription section Header Title is not displayed as Expected.UI title::" + SectionHeaderUI,
						false);
			}

			// DPKeySection header
			objSeleniumUtils.highlightElement(refPresentationDeck.DPDescrSectionHeader);
			String DpKeyHeaderUI = objSeleniumUtils.getTexFfromLocator(refPresentationDeck.DPDescrSectionHeader).trim();
			if (DpKeyHeaderUI.contains("DP Description")) {
				Assert.assertTrue("DPKey section Header Title is displayed as Expected", true);
			} else {
				Assert.assertTrue("DPKey section Header Title is not displayed as Expected.UI title::" + DpKeyHeaderUI,
						false);
			}
			break;
		case "DPDescription":
			objSeleniumUtils.highlightElement(refPresentationDeck.DPDescrSectionHeader);
			DpKeyHeaderUI = objSeleniumUtils.getTexFfromLocator(refPresentationDeck.DPDescrSectionHeader).trim();
			Pattern ptrn = Pattern.compile("DP Description \\(DP \\d{4}|\\d{5}|\\)");
			Matcher m = ptrn.matcher(DpKeyHeaderUI);
			boolean bMatched = m.matches();
			if (bMatched == true) {
				System.out.println("matched");
			} else {
				System.out.println("Not matched");
			}

			break;

		}

	}

	@Step
	public void unAssignforParentMPorTopic(String ParentItem, String ChildItem, String placeHolderArg1,
			String placeHolderArg2) {
		String TopicName = "";

		// Click Unassign Icon for the Parent Item to unassign from Presentation
		if (ParentItem.equalsIgnoreCase("MedicalPolicy")) {
			TopicName = Serenity.sessionVariableCalled("UITopicName");
			String MedPolicyAssignIconXpath = StringUtils.replace(refPresentationDeck.MedPolicyUnAssignIconForTopic,
					"TopicNameArg", TopicName);
			objSeleniumUtils.clickGivenXpath(MedPolicyAssignIconXpath);
			String sAssinedPrestn = refPresentationProfile.captureDecision("Unassign");
		}

	}

	@Step
	public void validateEditPopupHeader(String popupName, String valToCheck) {
		String CapturedTopicName = Serenity.sessionVariableCalled("TopicName");
		objSeleniumUtils.highlightElement(refPresentationDeck.EditPopup_TopicHeader);
		String EditPopupTopicName = objSeleniumUtils.get_TextFrom_Locator(refPresentationDeck.EditPopup_TopicHeader);
		if (EditPopupTopicName.equalsIgnoreCase(CapturedTopicName)) {
			Assert.assertTrue("The Edit Topic popup displayed TopicName as Header", true);
		} else {
			Assert.assertTrue("The Edit Topic popup not displayed TopicName as Header", false);
		}

	}

	@Step
	public void validateTopicDescriptionSections(String expectedState, String Section) {

		if (Section.equalsIgnoreCase("OriginalTopicSection")) {

			try {
				String EditedTopicDescr = "Testing";
				objSeleniumUtils.enterTextinCotrol("XPATH", refPresentationDeck.EditTopicDescriptionReadOnly,
						EditedTopicDescr);
				String TopicDscrUI = objSeleniumUtils
						.get_TextFrom_Locator(refPresentationDeck.EditTopicDescriptionReadOnly);

				if (!EditedTopicDescr.equalsIgnoreCase(TopicDscrUI)) {
					Assert.assertTrue("Origical TopicSection is in Read-Only Mode", true);
				} else {
					Assert.assertTrue("Origical TopicSection is in Write Mode", false);
				}
			}

			catch (Exception e) {
				System.out.println("Error message::" + e.getMessage());
				Assert.assertTrue("Operations not performed", false);
			}
		}

		if (Section.equalsIgnoreCase("TopicEditableSection")) {

			try {
				String sCurrentTopicDescr = objSeleniumUtils.get_TextFrom_Locator(refPresentationDeck.sEditDescription);
				String EditedTopicDescr = "Testing";
				objSeleniumUtils.enterTextinCotrol("XPATH", refPresentationDeck.sEditDescriptionTextArea,
						EditedTopicDescr);
				String TopicDscrUI = objSeleniumUtils
						.get_TextFrom_Locator(refPresentationDeck.sEditDescriptionEnteredText);

				if (EditedTopicDescr.equalsIgnoreCase(TopicDscrUI)) {
					Assert.assertTrue("TopicEditableSection is in Editable Mode", true);
				} else {
					Assert.assertTrue("TopicEditableSection is not in Editable Mode", false);
				}

			}

			catch (Exception e) {
				System.out.println("Error message::" + e.getMessage());
				Assert.assertTrue("Operations not performed", false);
			}
		}

	}

	@Step
	public void validateExpandCollpaseIcons() {
		List<WebElement> ExpandCollapseIcons = new ArrayList<WebElement>();

		try {
			ExpandCollapseIcons = objSeleniumUtils.getElementsList("XPATH",
					refPresentationDeck.TopicExpandCollapseIcons);

			if (ExpandCollapseIcons.size() == 2) {
				Assert.assertTrue("Expand/Collapse Icons displayed for both the Sections", true);
			} else {
				Assert.assertTrue("Expand/Collapse Icons not displayed for both the Sections", false);
			}

			for (int l = 0; l < ExpandCollapseIcons.size(); l++) {
				objSeleniumUtils.highlightElement(ExpandCollapseIcons.get(l));
			}

			String PropVal1 = "";
			String PropVal2 = "";

			// Check if both Sections Expanded
			PropVal1 = objSeleniumUtils.Get_Value_By_given_attribute("aria-expanded",
					refPresentationDeck.EditPopup_TopicDescrReadOnly);

			PropVal2 = objSeleniumUtils.Get_Value_By_given_attribute("aria-expanded",
					refPresentationDeck.EditPopup_TopicDescrEdit);

			if (PropVal1.equalsIgnoreCase("true") && PropVal2.equalsIgnoreCase("true")) {
				Assert.assertTrue("Edit Topic Popup both description Sections are in expanded mode", true);
			} else {
				Assert.assertTrue("Edit Topic Popup both description Sections are not in expanded mode", false);
			}

		}

		catch (ElementNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	@Step
	public void validateExpandCollpaseOperations() {

		List<WebElement> ExpandCollapseIcons = new ArrayList<WebElement>();
		String PropVal1 = "";
		boolean ExpandCollapseFlag = false;

		try {
			ExpandCollapseIcons = objSeleniumUtils.getElementsList("XPATH",
					refPresentationDeck.TopicExpandCollapseIcons);

			for (int l = 0; l < ExpandCollapseIcons.size(); l++) {
				ExpandCollapseFlag = false;
				objSeleniumUtils.highlightElement(ExpandCollapseIcons.get(l));
				objSeleniumUtils.clickGivenWebElement(ExpandCollapseIcons.get(l));
				PropVal1 = objSeleniumUtils.Get_Value_By_given_attribute("aria-expanded",
						refPresentationDeck.EditPopup_TopicDescrReadOnly);
				if (PropVal1.equalsIgnoreCase("false")) {
					ExpandCollapseFlag = true;
				}
				ExpandCollapseFlag = false;

				objSeleniumUtils.clickGivenWebElement(ExpandCollapseIcons.get(l));
				PropVal1 = objSeleniumUtils.Get_Value_By_given_attribute("aria-expanded",
						refPresentationDeck.EditPopup_TopicDescrReadOnly);
				if (PropVal1.equalsIgnoreCase("true")) {
					ExpandCollapseFlag = true;
				}

			}
		} catch (Exception e) {
			System.out.println("Script not executed;" + e.getMessage());
			Assert.assertTrue("Script not executed", false);
		}

		if (ExpandCollapseFlag == true) {
			Assert.assertTrue(
					"Edit Topic Popup ,Expand Collpase icons in both description Sections are  working as expected",
					true);
		} else {
			Assert.assertTrue("Edit Topic Popup ,Expand Collpase icons in both description Sections are not working ",
					false);
		}

	}

	@Step
	public void validateDPCardAgeing() {

		String Title = objSeleniumUtils.get_TextFrom_Locator(refOppurtunityDeck.DPCardAgeingCaption);
		if (Title.equalsIgnoreCase("Captured")) {
			Assert.assertTrue("Ageing caption described as: Captured: as expected ", true);
		} else {
			Assert.assertTrue("Ageing caption not  described as: Captured:  ", false);
		}

	}

	@Step
	public void selectPresentationForAssignment() {
		String sAssignChkBox = StringUtils.replace(refOppurtunityDeck.sPresentationAssignmentCheckbox, "val",
				Serenity.sessionVariableCalled("PresentationName"));
		WebElement we = getDriver().findElement(By.xpath(sAssignChkBox));
		we.click();
	}

	@Step
	public void PresentationProfileOperation(String sOperation) {
		List<WebElement> sPPDelete = getDriver().findElements(By.xpath(refPresProfileValidations.sGetPres));
		if (sPPDelete.size() > 0) {
			String PresName = Serenity.sessionVariableCalled("PresentationName");
			String TopicXpath = StringUtils.replace(refPresentationDeck.PresProfile_DeleteIcon, "PresnameArg",
					PresName);
			objSeleniumUtils.clickGivenXpath(TopicXpath);
			objSeleniumUtils.defaultWait(3);
			oGenericUtils.isElementExist(refPresProfileValidations.sDeletePres); // Verify
																					// 'Delete
																					// Presentation.'
																					// message
			oGenericUtils.clickButton(By.xpath("//span[@class='mat-button-wrapper'][.='Yes']")); // Click
																									// on
																									// 'OK'
																									// button
			objSeleniumUtils.defaultWait(2);

			// Verify After deleted records count
			List<WebElement> sPPAfterDelete = getDriver().findElements(By.xpath(refPresProfileValidations.sGetPres));
			if (sPPDelete.size() != sPPAfterDelete.size()) {
				GenericUtils.Verify(
						"Presentation records deleted sucessfully:=" + sPPDelete.size() + ":::" + sPPAfterDelete.size(),
						"PASSED");
			} else {
				GenericUtils.Verify(
						"Failed to delete Presentation records " + sPPDelete.size() + ":::" + sPPAfterDelete.size(),
						"FAILED");
			}
		} else {
			GenericUtils.Verify("No Records for Presentation Profil", "FAILED");
		}
	}

	@Step
	public void validateChkboxStatus(String chkBoxName, String expectedstatus) {

		String ControlName = "";

		if (chkBoxName.equalsIgnoreCase("ReadyforPresentation")) {
			ControlName = refPresentationDeck.ReadyForPresChkbox;
		}

		objSeleniumUtils.highlightElement(ControlName);
		String ClassProperty = objSeleniumUtils.Get_Value_By_given_attribute("class", ControlName);

		switch (expectedstatus.toUpperCase()) {

		case "ENABLED":
		case "ENABLEDSTATUSRETAINED":

			if (!ClassProperty.contains("disabled")) // if it is not disabled
			{
				Assert.assertTrue("The" + ControlName + "Enabled", true);
			} else {
				Assert.assertTrue("The" + ControlName + "Not Enabled", false);
				getDriver().quit();
			}
			break;

		case "DISABLED":

			if (ClassProperty.contains("disabled")) {
				Assert.assertTrue("The" + ControlName + "Disabled", true);
			} else {
				Assert.assertTrue("The" + ControlName + "Not Disabled", false);
				getDriver().quit();
			}
			break;
		}

	}

	@Step
	public void validateProfileStatusinDB(String preasentationProfileName, String expecteDBStatus) {

		String sDPkeyDescrUI = "";
		String sDPkeyDescrDB = "";
		String sDPKeyNo = " ";
		Map<String, String> DPKeyDescMap = new HashMap<String, String>();
		List<String> PresProfileStatus = new ArrayList<String>();

		// Get Client Name
		String sClientName = Serenity.sessionVariableCalled("SelectClientName");
		String sPresProfileName = Serenity.sessionVariableCalled("PresentationName");
		PresProfileStatus = refMongoDBUtils.retrievePresentationProfileCollectionValues(sClientName,
				"PresentationProfileStatus", "");

		String PresProfileStatus1 = PresProfileStatus.get(0);

		if (PresProfileStatus1.trim().equalsIgnoreCase(expecteDBStatus)) {
			Assert.assertTrue("PresProfile Status updated as expected", true);
			System.out.println("Pres Profile DB Status::" + PresProfileStatus1);
			System.out.println(
					"PresProfile Status updated as Ready for Presentation for Presentation::" + sPresProfileName);
		} else {
			Assert.assertTrue("PresProfile Status not updated as expected", false);
			System.out.println(
					"PresProfile Status not updated as Ready for Presentation for Presentation::" + sPresProfileName);
			getDriver().quit();
		}

	}

	@Step
	public void clickControl(String controlName) {
		switch(controlName){
		case "ReadyforPresentation":
			objSeleniumUtils.clickGivenXpath(refPresentationDeck.ReadyForPresChkbox);
			objSeleniumUtils.waitForContentLoad();
			break;
		case "NewPresentationProfile":
			clickonCreatedPresentation(Serenity.sessionVariableCalled("PresentationName").toString());
			break;
		}
		
	}

	@Step
	public void validateUIforMarkedPreasentation() {
		objSeleniumUtils.highlightElement(refPresentationDeck.PresentationDeck);
		String DeckUI = objSeleniumUtils.Get_Value_By_given_attribute("class", refPresentationDeck.PresentationDeck);

		if (DeckUI.contains("red-border")) {
			Assert.assertTrue("The ProfilePage have Red border to indicate lockedview", true);
		} else {
			Assert.assertTrue("The ProfilePage don't  have Red border to indicate lockedview", false);
			getDriver().quit();
		}

	}

	@Step
	public void ClickPopupButton(String btnName) {
		String CtrlName = "";
		if (btnName.equalsIgnoreCase("Yes")) {
			CtrlName = refPresentationDeck.FinalizeConfirmationYesBtn;
		} else if (btnName.equalsIgnoreCase("No")) {
			CtrlName = refPresentationDeck.FinalizeConfirmationNoBtn;
		}
		objSeleniumUtils.highlightElement(CtrlName);
		objSeleniumUtils.clickGivenXpath(CtrlName);

	}

	@Step
	public void RefreshPage() {
		getDriver().navigate().refresh();
		objSeleniumUtils.waitForContentLoad();
	}

	@Step
	public void clickCreatedPresentation() {
		// Serenity.setSessionVariable("Medicalpolicy").to("Drug and Biological
		// Policy");
		// Serenity.setSessionVariable("PresentationName").to("AutoService23142");
		String sPresProfileName = Serenity.sessionVariableCalled("PresentationName");
		System.out.println("Presentation profile name is " + sPresProfileName);
		// Serenity.setSessionVariable("DPkey").to("DP 8341");
		String sDPKey = Serenity.sessionVariableCalled("DPkey");
		System.out.println("DP Key is " + sDPKey);
		refCPWPage.select_the_given_DPkey_at_Presentation_view(sPresProfileName, sDPKey,"");
	}

	@Step
	public void selectAndFinalizeOpurtunities(String arg1) {
		refPresentationDeck.SelectAndFinalizeOppurtunities(arg1);
	}

	@Step
	public void verifyChkboxOfFinalizedOppurtunity(String sLevel) {
		switch (sLevel.toUpperCase().trim()) {
		case "MP":
			System.out.println("Mp checkbox existence "
					+ objSeleniumUtils.is_WebElement_Displayed(refPresentationDeck.Chk_MedicalPolicy));
			Assert.assertTrue("Check box is removed for Medical Policy with finalized oppurtunity",
					objSeleniumUtils.is_WebElement_NotDisplayed(refPresentationDeck.Chk_MedicalPolicy));
			break;
		case "TOPIC":
			System.out.println("Topic checkbox existence "
					+ objSeleniumUtils.is_WebElement_Displayed(refPresentationDeck.Chk_Topic));
			Assert.assertTrue("Check box is removed for Topic with finalized oppurtunity",
					objSeleniumUtils.is_WebElement_NotDisplayed(refPresentationDeck.Chk_Topic));
			break;
		case "DP":
			System.out.println(
					"DP checkbox existence " + objSeleniumUtils.is_WebElement_Displayed(refPresentationDeck.Chk_DP));
			Assert.assertTrue("Check box is removed for DP with finalized oppurtunity",
					objSeleniumUtils.is_WebElement_NotDisplayed(refPresentationDeck.Chk_DP));
			break;
		case "PPS":
			System.out.println("DP checkbox existence "
					+ objSeleniumUtils.is_WebElement_Displayed(refPresentationDeck.Chk_All_Oppurtunities));
			Assert.assertTrue("Check box is removed for all payers with finalized oppurtunity",
					objSeleniumUtils.is_WebElement_NotDisplayed(refPresentationDeck.Chk_All_Oppurtunities));
			break;
		// PresentationDeckStepDef.java
		case "SINGLE PPS":
			System.out.println("First payer checkbox existence "
					+ objSeleniumUtils.is_WebElement_Displayed(refPresentationDeck.Chk_FirstPayer));
			Assert.assertTrue("Check box is removed for first payer with finalized oppurtunity",
					objSeleniumUtils.is_WebElement_NotDisplayed(refPresentationDeck.Chk_FirstPayer));
			break;
		default:
			Assert.assertTrue("Invalid argument to verify checkbox for finalized oppurtunities", false);
		}
	}

	@Step
	public void verifyChkboxExistsIfAllPPSNotFinalized(String sLevel) {switch (sLevel.toUpperCase().trim()) {
	case "MP":
		System.out.println(
				"Mp checkbox existence " + objSeleniumUtils.is_WebElement_Displayed(refPresentationDeck.Chk_MedicalPolicy));
		Assert.assertTrue("Check box is removed for Medical Policy with finalized oppurtunity",
				objSeleniumUtils.is_WebElement_Displayed(refPresentationDeck.Chk_MedicalPolicy));
		break;
	case "TOPIC":
		System.out
				.println("Topic checkbox existence " + oGenericUtils.is_WebElement_Displayed(refPresentationDeck.Chk_Topic));
		Assert.assertTrue("Check box is removed for Topic with finalized oppurtunity",
				oGenericUtils.is_WebElement_Displayed(refPresentationDeck.Chk_Topic));
		break;
	case "DP":
		System.out.println("DP checkbox existence " + oGenericUtils.is_WebElement_Displayed(refPresentationDeck.Chk_DP));
		Assert.assertTrue("Check box is removed for DP with finalized oppurtunity",
				oGenericUtils.is_WebElement_Displayed(refPresentationDeck.Chk_DP));
		break;
	// case "PPS":
	// System.out.println("DP checkbox existence
	// "+objSeleniumUtils.is_WebElement_Displayed(refPresentationDeck.Chk_All_Oppurtunities));
	// Assert.assertTrue("Check box is removed for all payers with finalized
	// oppurtunity",
	// objSeleniumUtils.is_WebElement_Displayed(refPresentationDeck.Chk_All_Oppurtunities));
	// break;
	// case "SINGLE PPS":
	// System.out.println("First payer checkbox existence
	// "+objSeleniumUtils.is_WebElement_Displayed(refPresentationDeck.Chk_FirstPayer));
	// Assert.assertTrue("Check box is removed for first payer with
	// finalized oppurtunity",
	// objSeleniumUtils.is_WebElement_Displayed(refPresentationDeck.Chk_FirstPayer));
	// break;
	default:
		Assert.assertTrue("Invalid argument to verify checkbox for finalized oppurtunities", false);
	}
}

	@Step
	public void validateCaptureDecisionPopup(String sDecison) throws ElementNotFoundException {
		String ProcessingDateFrom = "";
		String ProcessingDateTo = "";
		String DateOfServiceFrom = "";
		String DateOfServiceTo = "";

		Assert.assertTrue("Decision capture pop up is not displayed after clicking on DP assign icon",
				oGenericUtils.isElementExist(refPresentationDeck.DecisionCaptureHeader));

		switch (sDecison.toUpperCase().trim()) {

		case "APPROVE":
		case "APPROVE WITH MOD":
			objSeleniumUtils.Click_given_Locator(
					StringUtils.replace(refPresentationDeck.Rdo_DecisionCapture, "svalue", sDecison));
			GenericUtils.Verify("Environment checkbox is verified in Decision capture pop up",
					objSeleniumUtils.is_WebElement_Displayed(refPresentationDeck.Chk_DecisionPopup_Environment));
			GenericUtils.Verify("Processing date 'FROM' field is verified in Decision capture pop up",
					objSeleniumUtils.is_WebElement_Displayed(refPresentationDeck.Txt_DecisionPopup_ProcessingDateFrom));
			ProcessingDateFrom = objSeleniumUtils.Get_Value_By_given_attribute("placeholder",
					refPresentationDeck.Txt_DecisionPopup_ProcessingDateFrom);
			System.out.println("Processing date 'FROM' value from CMS is " + ProcessingDateFrom);
			GenericUtils.Verify("Processing date 'TO' field is verified in Decision capture pop up",
					objSeleniumUtils.is_WebElement_Displayed(refPresentationDeck.Txt_DecisionPopup_ProcessingDateTo));
			ProcessingDateTo = objSeleniumUtils.Get_Value_By_given_attribute("placeholder",
					refPresentationDeck.Txt_DecisionPopup_ProcessingDateTo);
			System.out.println("Processing date 'To' value from CMS is " + ProcessingDateTo);
			GenericUtils.Verify("Processing date 'FROM' calendar icon is verified in Decision capture pop up",
					objSeleniumUtils
							.is_WebElement_Displayed(refPresentationDeck.Cal_Icon_DecisionPopup_ProcessingDateFrom));
			GenericUtils.Verify("Processing date 'TO' calendar icon is verified in Decision capture pop up",
					objSeleniumUtils
							.is_WebElement_Displayed(refPresentationDeck.Cal_Icon_DecisionPopup_ProcessingDateTo));
			GenericUtils.Verify("Date of service 'FROM' field is verified in Decision capture pop up",
					objSeleniumUtils.is_WebElement_Displayed(refPresentationDeck.Txt_DecisionPopup_DateOfServiceFrom));
			DateOfServiceFrom = objSeleniumUtils.Get_Value_By_given_attribute("placeholder",
					refPresentationDeck.Txt_DecisionPopup_DateOfServiceFrom);
			System.out.println("Date of Service 'FROM' value from CMS is " + DateOfServiceFrom);
			GenericUtils.Verify("Date of service 'TO' field is verified in Decision capture pop up",
					objSeleniumUtils.is_WebElement_Displayed(refPresentationDeck.Txt_DecisionPopup_DateOfServiceTo));
			DateOfServiceTo = objSeleniumUtils.Get_Value_By_given_attribute("placeholder",
					refPresentationDeck.Txt_DecisionPopup_DateOfServiceTo);
			System.out.println("Date of Service 'TO' value from CMS is " + DateOfServiceTo);
			GenericUtils.Verify("Date of service 'FROM' calendar icon is verified in Decision capture pop up",
					objSeleniumUtils
							.is_WebElement_Displayed(refPresentationDeck.Cal_Icon_DecisionPopup_DateOfServiceFrom));
			GenericUtils.Verify("Date of service 'TO' calendar icon is verified in Decision capture pop up",
					objSeleniumUtils
							.is_WebElement_Displayed(refPresentationDeck.Cal_Icon_DecisionPopup_DateOfServiceTo));
			if (sDecison.equalsIgnoreCase("APPROVE WITH MOD")) {
				GenericUtils.Verify("Modifications section text area is verified in Decision capture pop up",
						objSeleniumUtils.is_WebElement_Displayed(refPresentationDeck.Modifications_DecisionPopup));
			} else if (sDecison.equalsIgnoreCase("APPROVE")) {
				GenericUtils.Verify("Notes section text area is verified in Decision capture pop up",
						objSeleniumUtils.is_WebElement_Displayed(refPresentationDeck.Notes_DecisionPopup));
			}
			break;
		case "REJECT":
			objSeleniumUtils.Click_given_Locator(
					StringUtils.replace(refPresentationDeck.Rdo_DecisionCapture, "svalue", sDecison));
			GenericUtils.Verify("Notes section text area is verified in Decision capture pop up",
					objSeleniumUtils.is_WebElement_Displayed(refPresentationDeck.Notes_DecisionPopup));
			objSeleniumUtils.Click_given_Locator(refPresentationDeck.DD_DecisionPopup_Reasons);
			String[] Reasons = objSeleniumUtils
					.get_All_Text_from_Locator(refPresentationDeck.ReasonsGrid_DecisionPopup);
			String[] ReasonsActualList = Reasons[0].trim().split(" \\n ");
			String[] ReasonsExpList = ProjectVariables.DecisionCapturePopup_ReasonDropdownValues.split(";");
			Assert.assertArrayEquals("Reason drowdown values are verified in Decision capture pop up", ReasonsExpList,
					ReasonsActualList);
			objSeleniumUtils.Click_given_Locator(refPresentationDeck.ReasonsGrid_DecisionPopup + "//span");
			break;
		case "DEFER":
		case "FOLLOW UP":
			objSeleniumUtils.Click_given_Locator(
					StringUtils.replace(refPresentationDeck.Rdo_DecisionCapture, "svalue", sDecison));
			GenericUtils.Verify("Date field is verified in Decision capture pop up",
					objSeleniumUtils.is_WebElement_Displayed(refPresentationDeck.Txt_DecisionPopup_Date));
			GenericUtils.Verify("Notes section text area is verified in Decision capture pop up",
					objSeleniumUtils.is_WebElement_Displayed(refPresentationDeck.Notes_DecisionPopup));
			if (sDecison.equalsIgnoreCase("FOLLOW UP")) {
				GenericUtils.Verify("Responsible Party option Client is verified in Decision capture pop up",
						objSeleniumUtils.is_WebElement_Displayed(StringUtils
								.replace(refPresentationDeck.Rdo_DecisionPopup_ResParty, "svalue", "Client")));
				GenericUtils.Verify("Responsible Party option Cotiviti is verified in Decision capture pop up",
						objSeleniumUtils.is_WebElement_Displayed(StringUtils
								.replace(refPresentationDeck.Rdo_DecisionPopup_ResParty, "svalue", "Cotiviti")));
			}
			break;
		case "RE-ASSIGN":
			objSeleniumUtils.Click_given_Locator(
					StringUtils.replace(refPresentationDeck.Rdo_DecisionCapture, "svalue", sDecison));
			GenericUtils.Verify(
					"Assign oppurtunities pop up options are verified successfully in Decision capture pop up",
					objSeleniumUtils.is_WebElement_Displayed(refPresentationDeck.ReassignGrid_DecisionPopup));
			break;
		default:
			objSeleniumUtils.Click_given_Locator(refPresentationDeck.Btn_Cancel_DecisionPopup);
			objSeleniumUtils.Click_given_Locator(refPresentationDeck.Btn_Cancel_Changes_DecisionPopup);
			break;
		}

	}

	@Step
	public void validateDisabledIconsOfDeck() {

		objSeleniumUtils.Click_given_Locator("//label[text()='DP " + Serenity.sessionVariableCalled("DPkey") + "']");

		GenericUtils.Verify("Select all Check box is removed from presentation deck header tool bar",
				!objSeleniumUtils.is_WebElement_Displayed(refPresentationDeck.Chk_SelectAll_header));

		GenericUtils.Verify("Sort by dropdown is disabled in presentation deck header tool bar",
				objSeleniumUtils.Get_Value_By_given_attribute("aria-disabled", refPresentationDeck.DD_SortBy_header)
						.equalsIgnoreCase("true"));

		GenericUtils.Verify("Collapse All link is disabled in presentation deck header tool bar",
				objSeleniumUtils
						.Get_Value_By_given_attribute("class", StringUtils
								.replace(refPresentationDeck.Lnk_Expand_Collapse_All_header, "svalue", "Collapse All"))
						.contains("collapseLinkDisabled"));

		GenericUtils.Verify("Expand All link is disabled in presentation deck header tool bar",
				objSeleniumUtils
						.Get_Value_By_given_attribute("class", StringUtils
								.replace(refPresentationDeck.Lnk_Expand_Collapse_All_header, "svalue", "Expand All"))
						.contains("collapseLinkDisabled"));

		GenericUtils
				.Verify("Medical policy expand icon is removed from presentation hierarchy view",
						objSeleniumUtils
								.get_TextFrom_Locator(
										StringUtils.replace(refPresentationDeck.ExpandIcon_MP_Topic_HierarchySection,
												"svalue", Serenity.sessionVariableCalled("Medicalpolicy")))
								.equalsIgnoreCase("remove"));

		GenericUtils
				.Verify("Topic expand icon is removed from presentation hierarchy view",
						objSeleniumUtils
								.get_TextFrom_Locator(
										StringUtils.replace(refPresentationDeck.ExpandIcon_MP_Topic_HierarchySection,
												"svalue", Serenity.sessionVariableCalled("Topic")))
								.equalsIgnoreCase("remove"));

	}

	@Step
	public void verifyDPCountAndSavingsForBothPP(String arg1) {

		refPresentationDeck.verifySavingsAndDPCountOfPresentation(
				Serenity.sessionVariableCalled("SecondPresentationName").toString());
		int SavingsOfSecondPresentation = Integer
				.parseInt(Serenity.sessionVariableCalled("PresentationSavings").toString());

		int CountOfDPForSecondPresentation = Integer.parseInt(Serenity.sessionVariableCalled("DPCount").toString());
		if (arg1.equalsIgnoreCase("someOppurtunities")) {
			GenericUtils.Verify("Savings for reassigned presentation is " + SavingsOfSecondPresentation,
					!Serenity.sessionVariableCalled("PresentationSavings").toString().equalsIgnoreCase("0"));
			GenericUtils.Verify("DP count for reassigned presentation is " + CountOfDPForSecondPresentation,
					Serenity.sessionVariableCalled("DPCount").toString().equalsIgnoreCase("1"));
		} else {
			GenericUtils.Verify(
					"DP count increased after reassigning for presentation is " + CountOfDPForSecondPresentation,
					Serenity.sessionVariableCalled("DPCount").toString().equalsIgnoreCase("1"));
			GenericUtils.Verify(
					"Savings increased after reassigning for presentation is " + SavingsOfSecondPresentation,
					!Serenity.sessionVariableCalled("PresentationSavings").toString().equalsIgnoreCase("0"));
		}
		refPresentationDeck
				.verifySavingsAndDPCountOfPresentation(Serenity.sessionVariableCalled("PresentationName").toString());
		int SavingsOfFirstPresentation = Integer
				.parseInt(Serenity.sessionVariableCalled("PresentationSavings").toString());

		int CountOfDPForFirstPresentation = Integer.parseInt(Serenity.sessionVariableCalled("DPCount").toString());

		if (arg1.equalsIgnoreCase("someOppurtunities")) {
			GenericUtils.Verify("Savings for first presentation is " + SavingsOfFirstPresentation,
					!Serenity.sessionVariableCalled("PresentationSavings").toString().equalsIgnoreCase("0"));
			GenericUtils.Verify("DP count for first presentation is " + CountOfDPForFirstPresentation,
					Serenity.sessionVariableCalled("DPCount").toString().equalsIgnoreCase("1"));
		} else {
			GenericUtils.Verify("Savings for first presentation is " + SavingsOfFirstPresentation,
					Serenity.sessionVariableCalled("PresentationSavings").toString().equalsIgnoreCase("0"));
			GenericUtils.Verify("DP count for first presentation is " + CountOfDPForFirstPresentation,
					Serenity.sessionVariableCalled("DPCount").toString().equalsIgnoreCase("0"));

		}
	}

	@Step
	public void verifySavingsAndDPCountOfPresentation(String arg1) {
		if (arg1.isEmpty()) {
			refPresentationDeck.verifySavingsAndDPCountOfPresentation(
					Serenity.sessionVariableCalled("PresentationName").toString());
		} else {
			refPresentationDeck.verifySavingsAndDPCountOfPresentation(arg1);
		}
		int SavingsOfFirstPresentation = Integer
				.parseInt(Serenity.sessionVariableCalled("PresentationSavings").toString());
		System.out.println(SavingsOfFirstPresentation);
		int CountOfDPForFirstPresentation = Integer.parseInt(Serenity.sessionVariableCalled("DPCount").toString());
		System.out.println(CountOfDPForFirstPresentation);

	}

	@Step
	public void verifyAssociatedDPsinthePresentation(String capturedDPKey, String presName) {
		refPresentationDeck.verifyAssociatedDPsExistsInPresentation(capturedDPKey, presName);
	}

	@Step
	public void validateTheRuleRelationshipGridDetails(String applicationPage) {

		refPresentationDeck.clickOnDPKeyPayershort();
		refPresentationDeck.validateGriddetailswithDB(applicationPage);

	}

	public void validateDPWithDPtypeAndLatestDecision(String PPS, String DPType, String Decision) {
		refPresentationDeck.validateDPWithDPtypeAndLatestDecision(PPS,DPType,Decision);
		
	}
	
	//############################ Method to verify second part of subsequent change in PM ########################################################
	@Step
	public void verifyPMafterPipeline(String sChange, String sDisposition, String dpType) throws Throwable{
		oFilterDrawer.user_selects_given_value_from_Client_drop_down_list(Serenity.sessionVariableCalled("client").toString());
		refCPWPage.Select_the_Available_OpportunityDeck_from_Presentationview()	;
		switch(sChange){
		case "DP RETIRE":
			refCPWPage.validate_the_updated_DPkey_in_available_opportunity_deck(Serenity.sessionVariableCalled("DPkey").toString()); 
			
			Assert.assertFalse(refCPWPage.select_the_given_DPkey_at_Presentation_view(Serenity.sessionVariableCalled("NotRFP_Presentation").toString(),Serenity.sessionVariableCalled("DPkey").toString(),sChange));
			Assert.assertTrue(refCPWPage.select_the_given_DPkey_at_Presentation_view(Serenity.sessionVariableCalled("RFP_Presentation").toString(),Serenity.sessionVariableCalled("DPkey").toString(),sChange));
			refCPWPage.Validate_the_rawsavings_for_the_given_DP_at_PresentaionDeck(Serenity.sessionVariableCalled("DPkey").toString(), Serenity.sessionVariableCalled("RFP_Presentation").toString(), Serenity.sessionVariableCalled("RFP_Savings").toString());
			break;
		case "MP RETIRE":
			 
			Assert.assertFalse(refCPWPage.Enter_the_given_MP_Topic_in_filter_Drawer(Serenity.sessionVariableCalled("Medicalpolicy")));
			Assert.assertFalse(refCPWPage.select_the_given_DPkey_at_Presentation_view(Serenity.sessionVariableCalled("NotRFP_Presentation").toString(),Serenity.sessionVariableCalled("Medicalpolicy").toString(),sChange));
			Assert.assertTrue(refCPWPage.select_the_given_DPkey_at_Presentation_view(Serenity.sessionVariableCalled("RFP_Presentation").toString(),Serenity.sessionVariableCalled("Medicalpolicy").toString(),sChange));
			refCPWPage.Validate_the_rawsavings_for_the_given_DP_at_PresentaionDeck(Serenity.sessionVariableCalled("Medicalpolicy").toString(), Serenity.sessionVariableCalled("RFP_Presentation").toString(), Serenity.sessionVariableCalled("RFP_Savings").toString());
			break;
		case "TOPIC RETIRE":
			Assert.assertFalse(refCPWPage.Enter_the_given_MP_Topic_in_filter_Drawer(Serenity.sessionVariableCalled("Topic")));
			
			Assert.assertFalse(refCPWPage.select_the_given_DPkey_at_Presentation_view(Serenity.sessionVariableCalled("NotRFP_Presentation").toString(),Serenity.sessionVariableCalled("Topic").toString(),sChange));
			Assert.assertTrue(refCPWPage.select_the_given_DPkey_at_Presentation_view(Serenity.sessionVariableCalled("RFP_Presentation").toString(),Serenity.sessionVariableCalled("Topic").toString(),sChange));
			refCPWPage.Validate_the_rawsavings_for_the_given_DP_at_PresentaionDeck(Serenity.sessionVariableCalled("Topic").toString(), Serenity.sessionVariableCalled("RFP_Presentation").toString(), Serenity.sessionVariableCalled("RFP_Savings").toString());
			break;
		case "DP DESCRIPTION":
			refCPWPage.validate_the_updated_DPkey_in_available_opportunity_deck(Serenity.sessionVariableCalled("DPkey").toString());
			Assert.assertFalse(refCPWPage.select_the_given_DPkey_at_Presentation_view(Serenity.sessionVariableCalled("NotRFP_Presentation").toString(),Serenity.sessionVariableCalled("DPkey").toString(),sChange));
			Assert.assertTrue(refCPWPage.select_the_given_DPkey_at_Presentation_view(Serenity.sessionVariableCalled("RFP_Presentation").toString(),Serenity.sessionVariableCalled("Medicalpolicy").toString(),sChange));
			refCPWPage.Validate_the_rawsavings_for_the_given_DP_at_PresentaionDeck(Serenity.sessionVariableCalled("Medicalpolicy").toString(), Serenity.sessionVariableCalled("RFP_Presentation").toString(), Serenity.sessionVariableCalled("RFP_Savings").toString());
			break;
		case "MP NAME":
			Assert.assertFalse(refCPWPage.Enter_the_given_MP_Topic_in_filter_Drawer(Serenity.sessionVariableCalled("Medicalpolicy")));
			Assert.assertFalse(refCPWPage.select_the_given_DPkey_at_Presentation_view(Serenity.sessionVariableCalled("NotRFP_Presentation").toString(),Serenity.sessionVariableCalled("Medicalpolicy").toString(),sChange));
			Assert.assertTrue(refCPWPage.select_the_given_DPkey_at_Presentation_view(Serenity.sessionVariableCalled("RFP_Presentation").toString(),Serenity.sessionVariableCalled("Medicalpolicy").toString(),sChange));
			refCPWPage.Validate_the_rawsavings_for_the_given_DP_at_PresentaionDeck(Serenity.sessionVariableCalled("Medicalpolicy").toString(), Serenity.sessionVariableCalled("RFP_Presentation").toString(), Serenity.sessionVariableCalled("RFP_Savings").toString());
			break;
		case "TOPIC NAME":
			Assert.assertFalse(refCPWPage.Enter_the_given_MP_Topic_in_filter_Drawer(Serenity.sessionVariableCalled("Topic")));
			
			Assert.assertFalse(refCPWPage.select_the_given_DPkey_at_Presentation_view(Serenity.sessionVariableCalled("NotRFP_Presentation").toString(),Serenity.sessionVariableCalled("Topic").toString(),sChange));
			Assert.assertTrue(refCPWPage.select_the_given_DPkey_at_Presentation_view(Serenity.sessionVariableCalled("RFP_Presentation").toString(),Serenity.sessionVariableCalled("Topic").toString(),sChange));
			refCPWPage.Validate_the_rawsavings_for_the_given_DP_at_PresentaionDeck(Serenity.sessionVariableCalled("Topic").toString(), Serenity.sessionVariableCalled("RFP_Presentation").toString(), Serenity.sessionVariableCalled("RFP_Savings").toString());
			break;
		}
		
	}
	
	

}