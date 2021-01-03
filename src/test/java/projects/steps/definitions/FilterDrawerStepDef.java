package projects.steps.definitions;


import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.bson.BsonDocument;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.json.simple.parser.ParseException;
import org.junit.Assert;

import net.serenitybdd.core.Serenity;
import net.thucydides.core.annotations.Step;
import net.thucydides.core.steps.ScenarioSteps;
import project.pageobjects.CPWPage;
import project.pageobjects.FilterDrawer;
import project.utilities.AppUtils;
import project.utilities.GenericUtils;
import project.utilities.MicroServRestUtils;
import project.utilities.MongoDBUtils;
import project.utilities.OracleDBUtils;
import project.utilities.SeleniumUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;

import com.google.common.base.CharMatcher;
import com.mongodb.client.AggregateIterable;
import com.mongodb.client.model.Accumulators;
import com.mongodb.client.model.Aggregates;
import com.mongodb.client.model.Filters;

import project.exceptions.ElementNotFoundException;
import project.pageobjects.HomePage;
import project.pageobjects.OppurtunityDeck;
import project.variables.MonGoDBQueries;
import project.variables.OracleDBQueries;
import project.variables.ProjectVariables;

public class FilterDrawerStepDef extends ScenarioSteps {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	FilterDrawer onFilterDrawer;
	AppUtils objAppUtils;
	SeleniumUtils objSeleniumUtils;

	GenericUtils oGenericUtils;
	PresentationDeckStepDef refPresentationDeckStepDef;
	MonGoDBQueries oMonGoDBQueries;
	OppurtunityDeck refOppurtunityDeck;

	CPWPage oCPWPage;

	
	@Step
	public void user_selects_given_value_from_Client_drop_down_list(String arg1) throws Throwable {
		Assert.assertTrue(onFilterDrawer.user_selects_given_value_from_Client_drop_down_list(arg1));
	}

	@Step
	public void user_selects_given_value_from_Payer_Shorts(String arg1) throws InterruptedException {
		Assert.assertTrue(onFilterDrawer.user_unchecks_selectAllPayers());
		Assert.assertTrue(onFilterDrawer.user_selects_given_value_from_Payer_Shorts(arg1));
	}

	@Step
		public void user_selects_given_value_from_LOB(String arg1) {
		Assert.assertTrue(onFilterDrawer.user_unchecks_selectAllLOB());
		Assert.assertTrue(onFilterDrawer.user_selects_given_value_from_LOB(arg1));
	}
	@Step
	public void user_selects_given_value_from_Product(String arg1) {
		Assert.assertTrue(onFilterDrawer.user_unchecks_selectAllProduct());
		Assert.assertTrue(onFilterDrawer.user_selects_given_value_from_Product(arg1));
	}
	@Step
	public void user_filters_by_clicking_on_Apply_for_Payer_Shorts() {
		Assert.assertTrue(onFilterDrawer.user_filters_by_clicking_on_Apply_for_Payer_Shorts());
	}
	@Step
	public void user_selects_given_value_from_Medical_Policy_Topic(String arg1) throws InterruptedException {
		Assert.assertTrue(onFilterDrawer.user_unchecks_selectAllPolicies());
		Assert.assertTrue(onFilterDrawer.user_selects_given_value_from_Medical_Policy_Topic(arg1));
	}
	@Step
	public void user_filters_by_clicking_on_Apply_for_Medical_Policy_Topic() {
		Assert.assertTrue(onFilterDrawer.user_filters_by_clicking_on_Apply_for_Medical_Policy_Topic());
	}
	@Step
	public void user_should_view_given_value_in_Client_drop_down_list(String arg1) {
		Assert.assertTrue(onFilterDrawer.user_should_view_given_value_selected_in_Client_drop_down_list(arg1));
	}
	@Step
	public void user_should_view_given_value_in_Payer_Shorts(String arg1) {
		Assert.assertTrue(onFilterDrawer.user_should_view_given_value_selected_in_Payer_Shorts(arg1));
	}
	@Step
	public void user_should_view_given_value_in_LOB(String arg1) {
		Assert.assertTrue(onFilterDrawer.user_should_view_given_value_selected_in_LOB(arg1));
	}
	@Step
	public void user_should_view_given_value_in_Product(String arg1) {
		Assert.assertTrue(onFilterDrawer.user_should_view_given_value_selected_in_Product(arg1));
	}
	@Step
	public void user_should_view_given_value_in_Medical_Policy_Topic(String arg1) {
		Assert.assertTrue(onFilterDrawer.user_should_view_given_value_selected_in_Medical_Policy_Topic(arg1));
	}
	@Step
	public void user_should_view_Reset_button_for_Payer_Shorts() {
		Assert.assertTrue(onFilterDrawer.user_should_view_Reset_button_for_Payer_Shorts());
	}
	@Step
	public void user_should_view_Apply_button_for_Payer_Shorts() {
		Assert.assertTrue(onFilterDrawer.user_should_view_Apply_button_for_Payer_Shorts());
	}
	@Step
	public void user_should_view_Reset_button_for_Medical_Policy_Topics() {
		Assert.assertTrue(onFilterDrawer.user_should_view_Reset_button_for_Medical_Policy_Topics());
	}
	@Step
	public void user_should_view_Apply_button_for_Medical_Policy_Topics() {
		Assert.assertTrue(onFilterDrawer.user_should_view_Apply_button_for_Medical_Policy_Topics());
	}
	@Step
	
	public void pm_should_display_Clients_assigned_to_the_user_logged_in() throws ParseException {
		
		List<String> clientListfromservice = AppUtils.service_call_to_get_all_assigned_clients();
		List<String> clientListfromUI = onFilterDrawer.get_clientList_populated_on_UI();
		Assert.assertTrue(clientListfromservice.equals(clientListfromUI));
		
	}

	@Step
	public void selectMedicalPoliciesAsPerCriteria(String sScenarioType) throws Exception 
	{
		boolean bstatus=false;
				
		objSeleniumUtils.waitForContentLoad();
		waitFor(ProjectVariables.TImeout_5_Seconds);
		onFilterDrawer.user_unchecks_selectAllPolicies();
		   List<WebElement> MedPoliciesCheckBoxes;
		   List<WebElement> MedPoliciesExpanders;
		   List<WebElement> MedPoliciesTopics = null;
		   waitFor(ProjectVariables.TImeout_2_Seconds);
		   

			//Uncheck the "All MedicalPolicies" Checkbox if already selected		
			onFilterDrawer.user_unchecks_selectAllPolicies();
			
		   
		   MedPoliciesCheckBoxes = getDriver().findElements(By.xpath(onFilterDrawer.MedPoliciesChkBoxes));	
		   
		     if(MedPoliciesCheckBoxes.size() ==0 )
		     {		    	 
		    	 Assert.assertTrue("There are no MedicalPolicies displayed for selected Client",false);
		         getDriver().close();
		     }	
		
		     MedPoliciesExpanders  =objSeleniumUtils.getElementsList("XPATH", onFilterDrawer.MedPoliciesExpanders);
		    	
		  switch (sScenarioType)
		  {
		  
		  case "SingleMedPolicy&Topic":	 
			 
			  boolean bClicked = objSeleniumUtils.clickGivenWebElement(MedPoliciesExpanders.get(0));
			  waitFor(ProjectVariables.TImeout_2_Seconds);
			  if(bClicked == true)
			  {
				  MedPoliciesTopics  = objSeleniumUtils.getElementsList("XPATH", onFilterDrawer.MedPoliciesTopics);				
				   bClicked = objSeleniumUtils.clickGivenWebElement(MedPoliciesTopics.get(0));
			  }
			  if(bClicked == true)
			  {
				  Assert.assertTrue("Single Medical Poliy and Topic got selected",true);
			  } 	
			  else
			  {
				  Assert.assertTrue("Single Medical Poliy and Topic not  selected",false);
			  }
			  
			 break;
			 			 
		  case  "SingleMedPolicy&MultipleTopics":
			  
							   bClicked = objSeleniumUtils.clickGivenWebElement(MedPoliciesExpanders.get(0));
							   waitFor(ProjectVariables.TImeout_2_Seconds);
							  if(bClicked == true)
							  {
								  MedPoliciesTopics  = objSeleniumUtils.getElementsList("XPATH", onFilterDrawer.MedPoliciesTopics);									 
							  }
							  
							    if(MedPoliciesTopics.size() >=2 )
							     {		  
							    	     for(int j=0;j<MedPoliciesTopics.size();j++)
							    	     { 
							    			bClicked = objSeleniumUtils.clickGivenWebElement(MedPoliciesTopics.get(j));
							    	     }	
							    	     
							    	     if(bClicked == false)
										  {
											  Assert.assertTrue("Single Medical Poliy and Multiple Topics not  selected",false);
										  }
							    	     
							     }	
							    else //If there are no 2 or more Topics in a Policy 
							    {							    	       
							    	   for (int m=0;m<MedPoliciesExpanders.size();m++)  //Click each Medical policy and check until we find the 2 or more topics under medical policy
							    	   { 
							    	    	   bClicked = objSeleniumUtils.clickGivenWebElement(MedPoliciesExpanders.get(m-1));
							    	    	   bClicked = objSeleniumUtils.clickGivenWebElement(MedPoliciesExpanders.get(m));
							    	    	   MedPoliciesTopics  = objSeleniumUtils.getElementsList("XPATH", onFilterDrawer.MedPoliciesTopics);	
							    	    	 if(MedPoliciesTopics.size()>=2)
							    	    	 {
							    	    		  for(int j=0;j<MedPoliciesTopics.size();j++)
										    	     { 
										    			bClicked = objSeleniumUtils.clickGivenWebElement(MedPoliciesTopics.get(j));
										    	     }	
							    	    	 }
							    	    	  
								    	     if(bClicked == false)
											  {
												  Assert.assertTrue("Single Medical Poliy and Multiple Topics not  selected",false);
											  }
							    	    	
							    	    }
							    	
							    }
									  
			  
			  break;
			  
			  
		  case  "MultipleMedPolicy&Topics":					  
			 
			          if(MedPoliciesCheckBoxes.size()>=2)
			          {
								  for(int j=0;j<2;j++)
						    	     { 
						    			bClicked = objSeleniumUtils.clickGivenWebElement(MedPoliciesCheckBoxes.get(j));
						    			 MedPoliciesCheckBoxes = getDriver().findElements(By.xpath(onFilterDrawer.MedPoliciesChkBoxes));
						    	     }	
								  
			          }		  
			      else
			      {
			          Assert.assertTrue("There are no mutilple MedicalPolicies (more than 1) available for selected Client",false);
				        getDriver().close();		  
			      }			  
			  break;
			  
		  case "AllMedicalPolicies":
			  
			  		onFilterDrawer.user_selects_all_MedicalPolicies();
			  
			  break;
		  case "Captured Medicalpolicy":

			 			  
			  bstatus=oCPWPage.Enter_the_given_MP_Topic_in_filter_Drawer(Serenity.sessionVariableCalled("Medicalpolicy"));
			  
			 if(bstatus)
			 {
				 objSeleniumUtils.clickGivenXpath(StringUtils.replace(onFilterDrawer.Medicalpolicy_Checkbox, "value", Serenity.sessionVariableCalled("Medicalpolicy")));
			 }
			 else
			 {
				 Assert.assertTrue("Medicalpolicy was not displayed,after seraching in the fileter drawer,MP=>"+Serenity.sessionVariableCalled("Medicalpolicy"), false);
			 }

			  
			 
			  
			  
		  break;
		  case "Captured Topic":
			  bstatus=oCPWPage.Enter_the_given_MP_Topic_in_filter_Drawer(Serenity.sessionVariableCalled("Topic"));
			  if(bstatus)
				 {
					 objSeleniumUtils.clickGivenXpath(StringUtils.replace(onFilterDrawer.Medicalpolicy_Checkbox, "value", Serenity.sessionVariableCalled("Topic")));
				 }
				 else
				 {
					 Assert.assertTrue("Topic was not displayed,after seraching in the fileter drawer,Topic=>"+Serenity.sessionVariableCalled("Topic"), false);
				 }
			 
			  
			  break;
			  
			  
			  default:     
				  Assert.assertTrue("Input not passes for switch case code to execute",false);
				  break;
		  
		  }		
		

	}

	@Step
	public void selectAllPayerShorts() 
	{
		onFilterDrawer.user_selects_AllPayerShorts(); 		
	}

	@Step
	public void selectAllLOBs() {
		onFilterDrawer.user_selects_All_LOBs("");
		
	}

	@Step
	public void selectAllProducts() {	
		onFilterDrawer.user_selects_all_Products("");
	}

	@Step
	public void selectClientWithoutOpportunities(String sClientName) throws Throwable 
	{
		onFilterDrawer.user_selects_given_value_from_Client_drop_down_list(sClientName);			
	}
		
	@Step
	public void validateMedicalFilterDrawerMessage() 
	{		
		objSeleniumUtils.waitForElement(onFilterDrawer.MedPolicyFilterMsg, "shouldBevisible", 12);
		String sMsg = objSeleniumUtils.getTexFfromLocator(onFilterDrawer.MedPolicyFilterMsg);
		
		if(sMsg.equalsIgnoreCase("No opportunities match your selections"))
		{
			Assert.assertTrue("The Expected message ::No opportunities match your selections  is displayed ", true);			
		}
		else
		{
			Assert.assertTrue("The Expected message ::No opportunities match your selections  is not displayed ", false);		
			 getDriver().quit();
		}
		
	}

	@Step
	public void userAppliesFilter(String sButtonName) 
	{
		onFilterDrawer.user_filters_by_clicking_on_Button(sButtonName);		 
	}
	
	@Step
	public void selectMultiplePayershorts(String sPayershorts) throws ElementNotFoundException
	{
		onFilterDrawer.userSelectsMultiplePayershorts(sPayershorts);	
	}

	@Step
	public void selectMultipleLOBs(String sLOBs) throws ElementNotFoundException
	{
		onFilterDrawer.userSelectsMultipleLOBs(sLOBs);		
	}
	
	/*@Step
	public void updateSelectAllCheckboxState() {
		String  sPayerShortState = onFilterDrawer.updateAndGetCheckBoxState("Payer Shorts","SELECT ALL CHECKBOX","","Update");
		Serenity.setSessionVariable("payerShortState").to(sPayerShortState);
		
		String  sLOBState = onFilterDrawer.updateAndGetCheckBoxState("LOB","SELECT ALL CHECKBOX","","Update");
		Serenity.setSessionVariable("LOBState").to(sLOBState);
		
	}*/

	@Step
	public void userShouldViewSavedFilters() {
		String SavedPayershortView = Serenity.sessionVariableCalled("payerShortState");
		boolean blnPayerState =onFilterDrawer.GetCheckBoxState("Payer Shorts","SELECT ALL CHECKBOX","").equalsIgnoreCase(SavedPayershortView);
		GenericUtils.Verify("Saved view status should be "+SavedPayershortView,  blnPayerState);
	
		String SavedLOBView = Serenity.sessionVariableCalled("LOBState");
		boolean blnLOBState =onFilterDrawer.GetCheckBoxState("LOB","SELECT ALL CHECKBOX","").equalsIgnoreCase(SavedLOBView);
		GenericUtils.Verify("Saved view status should be "+SavedLOBView,  blnLOBState);
		
	}
	
	@Step
	public void userShouldViewPayerandLOBSavedFilters() {
		String SavedPayershort = Serenity.sessionVariableCalled("SelectedPayer");
		String sSavedPayerShortState = Serenity.sessionVariableCalled("payerShortState");		
		String sCurrentSelectedPayerState = onFilterDrawer.GetCheckBoxState("Payer Shorts","FILTER CHECKBOX BY PAYER OR LOB",SavedPayershort);
		boolean blnPayerState =sSavedPayerShortState.equalsIgnoreCase(sCurrentSelectedPayerState);
		GenericUtils.Verify("Saved view status should be "+sSavedPayerShortState+" for Payer "+SavedPayershort,  blnPayerState);
		
		String SavedLOB = Serenity.sessionVariableCalled("SelectedLOB");
		String SavedLOBView = Serenity.sessionVariableCalled("LOBState");		
		String sCurrenLOBState = onFilterDrawer.GetCheckBoxState("LOB","FILTER CHECKBOX BY PAYER OR LOB",SavedLOB);
		
		boolean blnLOBState =SavedLOBView.equalsIgnoreCase(sCurrenLOBState);
		GenericUtils.Verify("Saved view status should be "+SavedLOBView+" for LOB "+SavedLOB,  blnLOBState);
		
	}
	
	@Step
	public void verify(String sDescription, String Status){
		GenericUtils.Verify(sDescription,Status);
	}
	
	@Step
	public void closetheBrowser() {
		getDriver().quit();
		
	}

	@Step
	public void userSelectPayerandLOBFilters(String sCheckBox,String sValue, String sOperation) throws InterruptedException {
		
		onFilterDrawer.updateFilterCheckBox("Payer Shorts",sCheckBox,sValue,sOperation);

		onFilterDrawer.updateFilterCheckBox("LOB",sCheckBox,sValue,sOperation);		
		
	}
	
	@Step
	public void getSelectedPayerandLOB(String sCheckBox,String sValue, String sOperation) throws InterruptedException {
		onFilterDrawer.updateFilterCheckBox("Payer Shorts",sCheckBox,sValue,sOperation);
		String sPayerShortState = onFilterDrawer.GetCheckBoxState("Payer Shorts",sCheckBox,sValue);
		Serenity.setSessionVariable("payerShortState").to(sPayerShortState);
		
		String sXSelectedPayer =FilterDrawer.getDynamicXpath("GET SELECTED FLITER VALUE BY NUMBER", "Payer Shorts",sValue);
		String sSelectedPayer = getDriver().findElement(By.xpath(sXSelectedPayer)).getText();
		Serenity.setSessionVariable("SelectedPayer").to(sSelectedPayer);
		
		GenericUtils.Verify("Saved view Payer is "+Serenity.sessionVariableCalled("SelectedPayer")+" with state "+Serenity.sessionVariableCalled("payerShortState"), true);
		

		onFilterDrawer.updateFilterCheckBox("LOB",sCheckBox,sValue,sOperation);
		String sLOBState = onFilterDrawer.GetCheckBoxState("LOB",sCheckBox,sValue);
		Serenity.setSessionVariable("LOBState").to(sLOBState);
		
		String sXSelectedLOB = FilterDrawer.getDynamicXpath("GET SELECTED FLITER VALUE BY NUMBER", "LOB",sValue);
		String sSelectLOB = getDriver().findElement(By.xpath(sXSelectedLOB)).getText();
		Serenity.setSessionVariable("SelectedLOB").to(sSelectLOB);
		
		GenericUtils.Verify("Saved view Payer is "+Serenity.sessionVariableCalled("SelectedLOB")+" with state "+Serenity.sessionVariableCalled("LOBState"), true);
		
		
	}

	@Step
	public void ClickOnLink(String sText) {
		oGenericUtils.clickOnElement("a", sText);
		
	}

	public void SelectValuefromShowFilter(String sValue) {
		oGenericUtils.clickOnElement("//mat-select[@id='mat-select-2']");
		oGenericUtils.clickOnElement("//span[contains(text() , '"+sValue+"')]");
		
	}	

	//==============================================================================================================================>
	@Step
	public void user_under_Payershort_LOB_section(String arg1, String arg2) throws Throwable {
		
		
		onFilterDrawer.selectPayershortsAndLOB(arg2, arg1);
	}

	@Step
	public void user_select_value_for_operation(String arg1, String arg2, String arg3) throws Throwable {
		
		if(arg2.equalsIgnoreCase("DB")){
			switch(arg1.toUpperCase()){
				
			case "MEDICAL POLICY":
				onFilterDrawer.selectMedicalPolicyAndTopic(arg1, Serenity.sessionVariableCalled("Medicalpolicy"), arg3);
				break;
			case "TOPIC":
				onFilterDrawer.selectMedicalPolicyAndTopic(arg1, Serenity.sessionVariableCalled("Topic"), arg3);
				break;
			}
			
		}else{
			onFilterDrawer.selectMedicalPolicyAndTopic(arg1, arg2, arg3);
		}		
	}
	
	
    @Step
	public void user_assigns_presentation_for_respective_DP_under_level(String arg1, String arg2, String arg3) throws Throwable {
    	onFilterDrawer.Assign_MP_Topic_DP(arg2, arg3, arg1);
	}
   @Step
   public void user_verify_assigned_presentation_profile_for_respective_DP_under_Medical_policy_Topic(String arg1, String arg2, String arg3, String arg4) throws Throwable {
	  //DB
	   onFilterDrawer.verifyAssignedPresentationProfileDetails(arg3, arg4, arg2, arg1);
   }   

	@Step
	public void selectSinglePayershort(String sPayershort)
	{
		onFilterDrawer.SelectPayerShort(sPayershort);
	}
	
	@Step
	public ArrayList<String> PostUserDataAndFetchClientsList(String seriviceURl) throws IOException {

		String jsonBody = ProjectVariables.userData;
		System.out.println("Json Message body ====>"+jsonBody);
		ArrayList<String> clientsList =MicroServRestUtils.Post_the_Data_with_Rest_Assured_And_Fetch_Clients(jsonBody, seriviceURl);
		System.out.println("Array list of client names is "+clientsList);
		Serenity.setSessionVariable("ClientNamesList").to(clientsList);
		return clientsList;
	}
	
	@Step
	public void ValidateClientsAsPerUser() throws ElementNotFoundException
	{
		onFilterDrawer.verifyClients();
	}
	

	public void verifyScrollBarsofPayerShortAndLOB(){
		Assert.assertTrue("Scroll bar for Payer short is not displayed", onFilterDrawer.validateScrollBarsofPayerShort());
		Assert.assertTrue("Scroll bar for LOB is not displayed", onFilterDrawer.validateScrollBarsofLOB());
	}
	
	public void user_should_view_Client_dropdown() {
		Assert.assertTrue(onFilterDrawer.user_should_view_client_dropdown());
	}
	
	public void verifySortingOrderOfClients()
	{
		onFilterDrawer.verifySortingOderOfClients();
	}
	
	@Step
	public void getSelectedMedicalPolicy(String sCheckBox,String sValue, String sOperation) throws InterruptedException {
		String sMedicalPolicyState ="";
		onFilterDrawer.updateFilterCheckBoxOfMedicalPolicy(sCheckBox,sValue,sOperation);
		String sXSectionCheckbox = FilterDrawer.getDynamicXpathForMedicalPolicy(sCheckBox,sValue);
		String sCheckboxState = getDriver().findElement(By.xpath(sXSectionCheckbox)).getAttribute("id");
		if (sCheckboxState!=null){
			if(sCheckboxState.equalsIgnoreCase("true") || sCheckboxState.equalsIgnoreCase("allChecked")){	
				sMedicalPolicyState = "checked";
			}else{
				sMedicalPolicyState = "unchecked";
			}
		}else{
			sMedicalPolicyState = "unchecked";
		}
	
		Serenity.setSessionVariable("medicalPolicyState").to(sMedicalPolicyState);
		
		String sXSelectedMedicalPolicy =FilterDrawer.getDynamicXpathForMedicalPolicy("GET SELECTED FLITER VALUE BY NUMBER", sValue);
		String sSelectedMedicalPolicy = getDriver().findElement(By.xpath(sXSelectedMedicalPolicy)).getText();
		Serenity.setSessionVariable("SelectedMedicalPolicy").to(sSelectedMedicalPolicy);	
		GenericUtils.Verify("Saved view for Medical Policy is "+Serenity.sessionVariableCalled("SelectedMedicalPolicy")+" with state "+Serenity.sessionVariableCalled("medicalPolicyState"), true);
			
		
	}
	
	@Step
	public void userShouldViewMedicalPolicySavedFilters() {
		String sCurrentSelectedMedPolicyState = "";
		String SavedMedicalPolicy = Serenity.sessionVariableCalled("SelectedMedicalPolicy");
		String sSavedMedicalPolicyState = Serenity.sessionVariableCalled("medicalPolicyState");		
		String sMedPolicyCheckbox = FilterDrawer.getDynamicXpathForMedicalPolicy("FLITER CHECKBOX BY NUMBER","1");
		String sCheckboxState = getDriver().findElement(By.xpath(sMedPolicyCheckbox)).getAttribute("id");
		if (sCheckboxState!=null){
			if(sCheckboxState.equalsIgnoreCase("true") || sCheckboxState.equalsIgnoreCase("allChecked")){	
				sCurrentSelectedMedPolicyState = "checked";
			}else{
				sCurrentSelectedMedPolicyState = "unchecked";
			}
		}else{
			sCurrentSelectedMedPolicyState = "unchecked";
		}
		boolean blnMedPolicyState =sSavedMedicalPolicyState.equalsIgnoreCase(sCurrentSelectedMedPolicyState);
		GenericUtils.Verify("Saved view status should be "+sSavedMedicalPolicyState+" for Medical Policy "+SavedMedicalPolicy,  blnMedPolicyState);
      
		
		
	}

	@Step
	public void verifyRawSavingsInDBAndUIForMPCapturedThruService(){
		String rawSavings=null;
		String rawSavingsDB=null;
		/*MongoDBUtils.GetDBValuesBasedonAggregation(oMonGoDBQueries.FilterMongoDBQuery("ANNUAL RAW SAVINGS BASED ON CLIENT AND MEDICALPOLICY"),"ANNUAL RAW SAVINGS BASED ON CLIENT AND PAYERSHORT");
		String rawSavingsDB = Serenity.sessionVariableCalled("RawSavings");
		System.out.println("Raw savings in DB is "+rawSavingsDB);
		String rawSavings = Serenity.sessionVariableCalled("MedPolicyRawSavingsUI");
		String[] temp = rawSavings.split("\\$");
		String UIRawSavings = temp[1].replaceAll(",", "");
		System.out.println("Raw savings number in DB is "+UIRawSavings);
			
			if(UIRawSavings.equalsIgnoreCase(rawSavingsDB)){
				Assert.assertTrue("Raw savings in UI "+UIRawSavings+" and DB "+rawSavingsDB+" are matched - verification is successful", true);
			}else{
				Assert.assertTrue("Raw savings in UI "+UIRawSavings+" and DB "+rawSavingsDB+" are not matched - verification is not successful", false);
			}
			*/
			
		
			MongoDBUtils.GetDBValuesBasedonAggregation(oMonGoDBQueries.FilterMongoDBQuery("ANNUAL RAW SAVINGS BASED ON CLIENT AND TOPIC"),"ANNUAL RAW SAVINGS BASED ON CLIENT AND PAYERSHORT");
			String TopicrawSavingsDB = Serenity.sessionVariableCalled("RawSavings");
			System.out.println("Topic Raw savings in DB is "+TopicrawSavingsDB);
			String TopicrawSavings = Serenity.sessionVariableCalled("TopicRawSavingsUI");
			String[] temp0 = TopicrawSavings.split("\\$");
			String UITopicRawSavings = temp0[1].replaceAll(",", "");
			System.out.println("Raw savings number in DB is "+UITopicRawSavings);
				
				if(UITopicRawSavings.equalsIgnoreCase(TopicrawSavingsDB)){
					Assert.assertTrue("Raw savings in UI "+UITopicRawSavings+" and DB "+TopicrawSavingsDB+" are matched - verification is successful", true);
				}else{
					Assert.assertTrue("Raw savings in UI "+UITopicRawSavings+" and DB "+TopicrawSavingsDB+" are not matched - verification is not successful", false);
	}
				
			
	}
	
	@Step
	public void verifyRawSavingsInDBAndUIForDPCapturedThruService(String sPayershort){
		
		MongoDBUtils.GetDBValuesBasedonAggregation(oMonGoDBQueries.FilterMongoDBQuery("ANNUAL RAW SAVINGS BASED ON CLIENT AND PAYERSHORT"),"ANNUAL RAW SAVINGS BASED ON CLIENT AND PAYERSHORT");
		System.out.println("DP Key value is "+Serenity.sessionVariableCalled("DPkey"));
		System.out.println("Xpath is "+refOppurtunityDeck.sRawSavingsOnDPCard);
		String rawSavings = objSeleniumUtils.get_TextFrom_Locator(refOppurtunityDeck.sRawSavingsOnDPCard);
		System.out.println("Raw savings in UI is "+rawSavings);
		String[] temp = rawSavings.split("\\$");
		String UIRawSavings = temp[1].replaceAll(",", "");
		System.out.println("Raw savings number in UI is "+UIRawSavings);
		if(sPayershort.equalsIgnoreCase("FIRST") || sPayershort.equalsIgnoreCase("SECOND")){
			
			if(Integer.parseInt(UIRawSavings) == (Integer.parseInt(Serenity.sessionVariableCalled("RawSavings")))){
				Assert.assertTrue("Raw savings in UI "+UIRawSavings+" and DB "+Serenity.sessionVariableCalled("RawSavings")+" are matched - verification is successful", true);
			}else{
				Assert.assertTrue("Raw savings in UI "+UIRawSavings+" and DB "+Serenity.sessionVariableCalled("RawSavings")+" are not matched - verification is not successful", false);
			}
		
			if(sPayershort.equalsIgnoreCase("FIRST")){
				Serenity.setSessionVariable("firstPayerShortRawSavings").to(Serenity.sessionVariableCalled("RawSavings"));	
			}
			if(sPayershort.equalsIgnoreCase("SECOND")){
				Serenity.setSessionVariable("secondPayerShortRawSavings").to(Serenity.sessionVariableCalled("RawSavings"));	
			}		
			
		}else if(sPayershort.equalsIgnoreCase("BOTH")){
			int totalRawSavings =Integer.parseInt(Serenity.sessionVariableCalled("firstPayerShortRawSavings")) + Integer.parseInt(Serenity.sessionVariableCalled("secondPayerShortRawSavings"));
			System.out.println("First payer short savings is "+Integer.parseInt(Serenity.sessionVariableCalled("firstPayerShortRawSavings")));
			System.out.println("Second payer short savings is "+Integer.parseInt(Serenity.sessionVariableCalled("secondPayerShortRawSavings")));
			System.out.println("Sum of  both payer shorts savings is "+totalRawSavings);
			if(Integer.parseInt(UIRawSavings)  == totalRawSavings ){
				Assert.assertTrue("Raw savings in UI "+UIRawSavings+" and DB "+totalRawSavings+" are matched - verification is successful", true);
			}else{
				Assert.assertTrue("Raw savings in UI "+UIRawSavings+" and DB "+totalRawSavings+"are not matched - verification is not successful", false);
			}
		}
		
	}
	
	

	@Step
	public void SelectPayerShortAndMedicalPolicyFetchedFromService(String sNewPayershort, String PolicyOrTopic) throws InterruptedException{
		
		boolean flag = false;
		String newPayershort = null;
		Actions action = new Actions(getDriver());
	
		//retrieving session variable and selecting same payer short
		String PayerkeysList = Serenity.sessionVariableCalled("Payerkeys");
		System.out.println("Payer keys list is "+PayerkeysList);
		if(sNewPayershort.equalsIgnoreCase("FIRST")){
			String sPayerKey =  PayerkeysList.split(",")[0];
			newPayershort	= MongoDBUtils.Retrieve_the_PayerKey_From_mongodb_with_the_given_Payershort(sPayerKey,"PayerKey");
		}else{	
			//getDriver().navigate().refresh();
			String sPayerKey =  PayerkeysList.split(",")[1].trim();
			newPayershort	= MongoDBUtils.Retrieve_the_PayerKey_From_mongodb_with_the_given_Payershort(sPayerKey,"PayerKey");
		}
		 
		System.out.println("New Payer short is "+newPayershort);
		Serenity.setSessionVariable("SelectedPayerShort").to(newPayershort);

		String sPayerShort = StringUtils.replace(onFilterDrawer.payerShort, "svalue", newPayershort);
		String sPayerShortStatus = StringUtils.replace(onFilterDrawer.payerShortStatus, "svalue", newPayershort);
		
		flag = objSeleniumUtils.isElemPresent(sPayerShort);
		List<WebElement> sList=getDriver().findElements(By.xpath(onFilterDrawer.sRdm_PYS));
		WebElement sInput=sList.get(0);
		
		/*action.moveToElement(sInput).click().build().perform();
		action.moveToElement(sInput).click().build().perform();
		do
		{
			action.sendKeys(Keys.ARROW_DOWN).perform();
			flag = objSeleniumUtils.isElemPresent(sPayerShort);
		}while(flag==false);*/
		
	  //  String sPayerShortChbkStatus=objSeleniumUtils.Get_Value_By_given_attribute("class", sPayerShort);
		//Check based on Condition
	   // if(!sPayerShortChbkStatus.contains("true")){
	    	oGenericUtils.clickButton(By.xpath(sPayerShort));
	    //}
	    //click on 'Apply' button
		oGenericUtils.clickOnElement(StringUtils.replace(oCPWPage.ButtonWithText, "svalue", "Apply"));
			
		if(PolicyOrTopic.equalsIgnoreCase("MEDICAL POLICY")){
			String MedicalPolicy = Serenity.sessionVariableCalled("Medicalpolicy").toString();
			onFilterDrawer.selectMedicalPolicyAndTopic("MEDICAL POLICY", MedicalPolicy, "SELECT" );
		}else if(PolicyOrTopic.equalsIgnoreCase("TOPIC")){
		String Topic = Serenity.sessionVariableCalled("Topic").toString();
		onFilterDrawer.selectMedicalPolicyAndTopic("TOPIC", Topic, "SELECT" );
		}
	}
	
	@Step
	public void clickOnBtn(String sBtn){
		switch(sBtn.toUpperCase()){
		case "GET AVAILABLE OPPURTUNITIES" :
			objSeleniumUtils.Click_given_Locator(onFilterDrawer.GetAvailableOppurtunitiesBtn);
			objSeleniumUtils.defaultWait(ProjectVariables.TImeout_10_Seconds);
			
			 String sAllPayerShortChbkStatus=getDriver().findElement(By.xpath(StringUtils.replace(onFilterDrawer.allPPS, "svalue", "Payer Shorts"))).getAttribute("aria-checked");
			 String sAllLOBChbkStatus=getDriver().findElement(By.xpath(StringUtils.replace(onFilterDrawer.allPPS, "svalue", "LOB"))).getAttribute("aria-checked");
			    if(sAllPayerShortChbkStatus.contains("false") && sAllLOBChbkStatus.contains("false")){
			    	Assert.assertTrue("All Payer and LOB are unchecked - verification is successful",true);
			    }else{
			    	Assert.assertTrue("All Payer and LOB are not unchecked - verification is not successful",false);
			    }
			
			break;
			
		case "AVAILABLE OPPURTUNITIES":
				objSeleniumUtils.Click_given_Locator("//mat-tab-header//div[@id='mat-tab-label-0-0']");
				objSeleniumUtils.defaultWait(ProjectVariables.TImeout_10_Seconds);
			break;
		}
		

		
	}
	
	@Step
	public void verifyPayerShortAndLOBWRTDB(String sClientName) throws Exception{
		String sPPS = null;
		ArrayList<String> sResultDB = new ArrayList<String>();
		Serenity.setSessionVariable("ClientName").to(sClientName);
		sPPS = "payer_short";
		Serenity.setSessionVariable("PPS").to(sPPS);
		ArrayList<String>sResultDB1 = OracleDBUtils.executeSQLQueryMultipleRows(OracleDBQueries.getOracleDBQuery("DISTINCT PPS OF A CLIENT"));
		Collections.sort(sResultDB1);
		System.out.println("PayerShort Result is "+sResultDB1);
		sPPS = "insurance_desc";
		Serenity.setSessionVariable("PPS").to(sPPS);
		ArrayList<String> sResultDB2 = OracleDBUtils.executeSQLQueryMultipleRows(OracleDBQueries.getOracleDBQuery("DISTINCT PPS OF A CLIENT"));
		Collections.sort(sResultDB2);
		System.out.println("LOB Result is "+sResultDB2);
		/*sPPS = "claim_type";
		Serenity.setSessionVariable("PPS").to(sPPS);
		ArrayList<String> sResultDB3 = OracleDBUtils.executeSQLQueryMultipleRows(OracleDBQueries.getOracleDBQuery("DISTINCT PPS OF A CLIENT"));
		Collections.sort(sResultDB3);
		System.out.println("Claimtype Result is "+sResultDB3);*/
		
		sResultDB.addAll(sResultDB1);
		sResultDB.addAll(sResultDB2);
		//sResultDB.addAll(sResultDB3);
		System.out.println("Combined and sorted list of Payer,LOB  in DB is "+sResultDB);
		String[] sResultUI = objSeleniumUtils.get_All_Text_from_Locator(onFilterDrawer.allPayerShortLOBText);
		System.out.println("List of PayerShort,LOB in UI is "+Arrays.asList(sResultUI));
		Assert.assertEquals(sResultDB.toArray(), sResultUI);
		//Assert.assertArrayEquals("",sResultDB.toArray(), sResultUI);
		
			
	}

	@Step
	public void user_selects_Claimtypes_in_filtersection(String filtersection) 
	{
		onFilterDrawer.user_selects_Claimtypes_in_filtersection(filtersection);
	}
	
	@Step
	public void verifyRawSavingsForCapturedMPOrTopic(){
		
			System.out.println(Serenity.sessionVariableCalled("MedPolicyRawSavingsUI").toString());
			String MedPolicy  =   Serenity.sessionVariableCalled("Medicalpolicy");	
			oGenericUtils.setValue(By.xpath(onFilterDrawer.sEdt_MP_Topic), MedPolicy);
		
			
			oGenericUtils.clickButton(By.xpath(onFilterDrawer.sBtn_Search));
		 
			 String sMedPolicyRawSavings =   objSeleniumUtils.get_TextFrom_Locator(StringUtils.replace(onFilterDrawer.MedicalpolicyRawSavings, "sval", Serenity.sessionVariableCalled("Medicalpolicy")));
			 String t = CharMatcher.DIGIT.retainFrom(sMedPolicyRawSavings.substring(1));
			 Serenity.setSessionVariable("MedPolicyRawSavingsUIAfterFinalizing").to(t);
			 System.out.println(Serenity.sessionVariableCalled("MedPolicyRawSavingsUIAfterFinalizing").toString());
			String RawSavingsMP =  Serenity.sessionVariableCalled("MedPolicyRawSavingsUI").toString().substring(1);
			System.out.println(CharMatcher.DIGIT.retainFrom(RawSavingsMP));
			String RawSavingsPresentation = Serenity.sessionVariableCalled("PresentationSavings").toString();
			System.out.println(RawSavingsPresentation);
			 int updatedSavings = (Integer.parseInt(CharMatcher.DIGIT.retainFrom(RawSavingsMP)))-(Integer.parseInt(Serenity.sessionVariableCalled("PresentationSavings").toString()));
			 System.out.println(updatedSavings);
			 Assert.assertEquals("Raw savings is not matched for MP or topic after finalizing the decisions", updatedSavings, Integer.parseInt(t));	
			 
		
	}

	@Step
	public void userSelectsPPSUnder(String selectionType, String sValue, String sHeader) throws IOException, ParseException {
		onFilterDrawer.userSelectsPPSUnder(selectionType,sValue,sHeader);
		
	}
	
	@Step
	public void system_should_display_a_msg(String arg1) {
		
		objSeleniumUtils.is_WebElement_Displayed("//span[text()='"+arg1+"']");
		
		//GenericUtils.Verify("Msg is displayed", objSeleniumUtils.is_WebElement_Displayed("//span[text()='"+arg1+"']"));
		
		
	}

	@Step
	public void validate_Decision_count_with_Mongo(String arg1) {
		
		onFilterDrawer.validate_Decision_count_with_Mongo(arg1);
		
	}


}
