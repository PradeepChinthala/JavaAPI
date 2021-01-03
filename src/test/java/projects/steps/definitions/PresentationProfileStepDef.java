package projects.steps.definitions;

import java.io.IOException;
import java.text.ParseException;
import java.util.*;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.junit.Assert;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import com.sun.tools.xjc.addon.sync.SynchronizedMethodAddOn;

import cucumber.api.java.en.Given;
import net.serenitybdd.core.Serenity;
import net.thucydides.core.annotations.Step;

import net.thucydides.core.steps.ScenarioSteps;
import project.exceptions.ElementNotFoundException;
import project.pageobjects.CPWPage;
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

public class PresentationProfileStepDef extends ScenarioSteps
{

	private static final long serialVersionUID = 1L;
	PresentationProfile  refPresentationProfile;
	PresentationProfileValidations oPresentationProfileValidations;
	PresentationDeck    refPresentationDeck;
	SeleniumUtils objSeleniumUtils;
	GenericUtils oGenericUtils;
	AppUtils refAppUtils;		
	MongoDBUtils refMongoDBUtils; 
	//= new  MongoDBUtils();
	FilterDrawer onFilterDrawer;
	CPWPage oCPWPage;
	OppurtunityDeck refOppurtunityDeck;
	SeleniumUtils refSeleniumUtils;
	@Step
	public void createPresentationProfile() throws InterruptedException
	{	    
			refPresentationProfile.createPresentationProfile();
	}

	@Step
	public void clickPresentationProfile()
	{
		try {
			refPresentationProfile.clickPresentationProfile();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Step
	public void validatePresentationDeckIsEmpty() 
	{			      

		objSeleniumUtils.highlightElement(StringUtils.replace(refPresentationDeck.PresDeckHeaderName,"val",Serenity.sessionVariableCalled("PresentationName")));
		String sPresName = refPresentationDeck.PresentationHeaderName.getText();
		List<WebElement> DPCards  = null ; 

		if (sPresName.equalsIgnoreCase(Serenity.sessionVariableCalled("PresentationName")))
		{	      
			try{

				DPCards = getDriver().findElements(By.xpath(refPresentationDeck.PresentationContainerMedPolicy));

				if(DPCards.size()==0)
				{										    	  	        	  
					Assert.assertTrue("Presentation Deck is Empty",true);
				}
				else
				{
					Assert.assertTrue("Presentation Deck should be Empty",false);
				}	

			}
			catch (Exception e) { }			    	  
		}
	}

		@Step
		public void validatePresDeckState(String sContainer, String sExpectedState) throws ElementNotFoundException
		{
			//objSeleniumUtils.validateOppDeck_PresContainerState(sContainer,sExpectedState);			
		}
		@Step
		public void validateClientBasedPresProfileView(String sLoggedinUser, String sUser2)
		{		
			
			String  sPresTitle = ""; 
			String sPresName = "";
			
			  switch (sLoggedinUser)
			  {				  
			     case "iht_ittest09":
				  
				        	 sPresName =  Serenity.sessionVariableCalled(sUser2+"PresMap");
				        	 sPresTitle =   StringUtils.replace(refPresentationProfile.PresentationTabName,"PresNameArg",sPresName);
				        	 try{		
								  if(objSeleniumUtils.is_WebElement_Visible(sPresTitle))
								  {
									  objSeleniumUtils.highlightElement(sPresTitle);	
										 Assert.assertTrue("Presentation Profile created by User1 ::"+sUser2+" is visible to User2::"+sLoggedinUser,true);
								  }									
									 else
									 {
										 Assert.assertTrue("Presentation Profile created by User1::"+sUser2+" is not visible to User2::"+sLoggedinUser,false);
										 getDriver().quit();
									 }	 
								  
								      sPresName =  Serenity.sessionVariableCalled(sLoggedinUser+"PresMap");		        	 
						        	  sPresTitle =   StringUtils.replace(refPresentationProfile.PresentationTabName,"PresNameArg",sPresName);						        	 
									  if(objSeleniumUtils.is_WebElement_Visible(sPresTitle))
									  {
										  objSeleniumUtils.highlightElement(sPresTitle);	
											 Assert.assertTrue("Presentation Profile created by Loggedin user ::"+sLoggedinUser+ " is visible to himself ",true);
									  }									
										 else
										 {
											 Assert.assertTrue("Presentation Profile created by Loggedin user is not visible to himself",false);
											 getDriver().quit();
										 }	 
				        	 }
				        	 catch(Exception e)
				        	 {
				        		 
				        	 }
				  
				  			break;
				  
			  case "iht_ittest05":			  
				  
				  			sPresName =  Serenity.sessionVariableCalled(sUser2+"PresMap");
				  			sPresTitle =   StringUtils.replace(refPresentationProfile.PresentationTabName,"PresNameArg",sPresName);
		        	 try{		
						  if(objSeleniumUtils.is_WebElement_Visible(sPresTitle))
						  {
							  objSeleniumUtils.highlightElement(sPresTitle);	
							  Assert.assertTrue("Presentation Profile created by User1 is visible to User2 ",true);
						  }									
							 else
							 {
								 Assert.assertTrue("Presentation Profile created by User1 is not visible to User2",false);
								 getDriver().quit();
							 }	 
						  
						      sPresName =  Serenity.sessionVariableCalled(sLoggedinUser+"PresMap");		        	 
				        	  sPresTitle =   StringUtils.replace(refPresentationProfile.PresentationTabName,"PresNameArg",sPresName);						        	 
							  if(objSeleniumUtils.is_WebElement_Visible(sPresTitle))
							  {
								    objSeleniumUtils.highlightElement(sPresTitle);	
									 Assert.assertTrue("Presentation Profile created by Loggedin user ::"+sLoggedinUser+ "is visible to himself ",true);
							  }									
								 else
								 {
									 Assert.assertTrue("Presentation Profile created by Loggedin user ::"+sLoggedinUser+ "is not visible to himself",false);
									 getDriver().quit();
								 }	 
		        	 }
		        	 catch(Exception e)
		        	 {
		        		 
		        	 }
				  
		  			break; 				  
			  }       		        
			
		}
	


	

	@Step
	public void createPresentationProfile(String sUserName) throws InterruptedException 		
	{
		refPresentationProfile.createPresentationProfile(sUserName);			
	}

	@Step
	public void clickOnOtherPresProfile() throws ElementNotFoundException 
	{
		refPresentationProfile.clickOtherProfile();			
	}

	@Step
	public void createPresentations(String sPresCount, String sValues)
	{		
		try {
			refPresentationProfile.createPresentations(sPresCount, sValues);
		} catch (InterruptedException e) {				
			e.printStackTrace();
		}			
	}
	//==============================================================================>
	@Step
	public void user_validate_Presentation_profile_functionality(String arg1) throws Throwable {

		oPresentationProfileValidations.PresentationProfileValidations(arg1, "HSILM", "Medicaid");
	}
	@Step
	public void validate_payershort_section_based_on_the_payers_in_the_RVA_run_for_the(String arg1, String arg2) throws Throwable {

		
		oPresentationProfileValidations.PresentationProfileValidations("VERIFY_PAYERSHORTS_LOB_DATA",arg1, arg2);
	}
	@Step
	public void presentation_Profile_functionality(String arg1) throws Throwable {
		oPresentationProfileValidations.Edit_Delete_PresentationProfile(arg1);  
	}
	@Step
	public void validate_client_decision_on_DP_card_in_avaliable_DP_desk(String latestClientDecision) throws Throwable {
		String InsuranceCode=null;
		List<String> InsuranceList=Arrays.asList(Serenity.sessionVariableCalled("Insurances").toString().split(","));
		
		//DP key and Insurance status need to add from DB
		for (int i = 0; i < InsuranceList.size(); i++) {
			//To retrieve the Insurance code from the insurance
			InsuranceCode=oGenericUtils.Retrieve_the_insurance_from_insuranceKey(InsuranceList.get(i).trim());
			
			oPresentationProfileValidations.verifyTopicDPData(Serenity.sessionVariableCalled("DPKey").toString(),InsuranceCode, latestClientDecision);
			
		
		}
		
		
		
		
		
		
		
		
	}
	
	@Step
	public void validate_captured_disposition_present_under_Avaialbe_DPs() throws Throwable {
		oPresentationProfileValidations.verifycapturedDPsData();
	}

	@Step
	public void verify_icon_displayed_on_screen(String arg1) throws Throwable {
		String sIcon="//button[@mattooltip='"+arg1+"']";
		//oGenericUtils.isElementExist(sIcon,1000);
		objSeleniumUtils.is_WebElement_Displayed(sIcon);
	}

	@Step
	public void selectDPsatGivenLevelinOpptntyView(String slevel) {

		refPresentationProfile.selectDPsatGivenLevelinoppHierarchyView(slevel);

	}
	
	@Step
	public void validate_view_assign_pop_up_when_no_opportunity_is_assigned_to_a_profile() throws Throwable {
		System.out.println(Serenity.sessionVariableCalled("DPkey").toString());

		onFilterDrawer.Assign_MP_Topic_DP("DP_ASSIGN_POPUP", Serenity.sessionVariableCalled("DPkey").toString(),Serenity.sessionVariableCalled("sPPName").toString());
	}


	
	@Step
	public void ExpandAllinPresenationPrf() {

		refPresentationProfile.getOppurtunityHierarchyView();

		refPresentationProfile.ExpandAllinHierarchyView();

	}


	@Step
	public void captureDecision(String sOperation) {

		Serenity.setSessionVariable("Decision").to(sOperation);
		String sAssinedPrestn = refPresentationProfile.captureDecision(sOperation);

		Serenity.setSessionVariable("AssignedPresentation").to(sAssinedPrestn);
		
		
	}
	
	@Step
    public void finalize_Decisions_for_payershorts(String sDecision, String slevel) throws Throwable 
    {
		if(sDecision.contains("Test Only"))
		{
			sDecision=StringUtils.substringBefore(sDecision, "Test Only").trim();
		}
        refPresentationDeck.FinalizeDescisions(slevel);
        objSeleniumUtils.defaultWait(ProjectVariables.TImeout_10_Seconds);
        switch(sDecision)
        {
        //case "Approve with Mod Test Only":
        //case "Approve Test Only":
        case "Follow up":
            //Ready for Presentation checkbox
            Assert.assertFalse("Ready for Presentation checkbox should be in enabled mode,after finalaizing the decision==>"+sDecision, oGenericUtils.is_WebElement_Displayed("//input[@type = 'checkbox'][@disabled]"));
                            
        break;
        default:
        	
			 //Ready for Presentation checkbox
	        GenericUtils.Verify("Ready for Presentation checkbox should be in disabled mode,after finalaizing the decision==>"+sDecision, oGenericUtils.is_WebElement_Displayed("//input[@type = 'checkbox'][@disabled]"));
		
            
        break;
        }
        
        refPresentationDeck.verifyDecisions(slevel, sDecision);
    }

	


	@Step

	public void verify_validation_for(String arg1, String arg2) throws Throwable {
		refPresentationDeck.ready_for_presentations(Serenity.sessionVariableCalled("PresentationName").toString().trim(),arg2);
	}


	@Step

	public void UnassignDPFromPresDeck(String noOfDPsToUnassign, String TopicorMedPolicyUnassign,String sPresProfile, String sPlaceHolderArg1, String sPlaceHolderArg2) 
	{	 	
		List<WebElement> DPKeysElmnts =  new ArrayList<WebElement>();
		List<String> DPKeysVals =  new ArrayList<String>();
		  String UnAssignIconXpath = "";
		  String UnAssignIconXpath1 = "";
		  String  TopicTitleUI ="";
		
try{		
		switch(TopicorMedPolicyUnassign)
		{		
		
		case "MedicalPolicy":			
		break;
		
		case "Topic":
			
			 String CapturedTopicName = Serenity.sessionVariableCalled("UITopicName");
			 
		      String TopicXpath = StringUtils.replace(refPresentationDeck.DPkeysForTopic,"TopicNameArg",CapturedTopicName);
			  DPKeysElmnts =  objSeleniumUtils.getElementsList("XPATH", TopicXpath);		
			  DPKeysVals = objSeleniumUtils.getWebElementValuesAsList(TopicXpath);
			  
			           if(noOfDPsToUnassign.equalsIgnoreCase("All"))
			           {
			        	        //Traverse and click on Unassign icon        	   
			        	   
			        	     for(int k=0;k<DPKeysElmnts.size();k++)
			        	     {
			        	    	 UnAssignIconXpath1 = "";
			        	    	 UnAssignIconXpath = "";
			        	    	 oGenericUtils.clickOnElement("u", "Expand All");
			        	    	 TopicTitleUI  = objSeleniumUtils.get_TextFrom_Locator(refPresentationDeck.FirstTopicLabel);
			        	    	 UnAssignIconXpath1 = StringUtils.replace(refPresentationDeck.DPKeyAssignIcon,"TopicNameArg",TopicTitleUI);
			        	         UnAssignIconXpath = StringUtils.replace(UnAssignIconXpath1,"DPKeyArg",DPKeysVals.get(k));
			        	         objSeleniumUtils.Click_given_Xpath(UnAssignIconXpath);     //Click unassign icon for the DP Card		
			        	         captureDecision("Unassign");
			        	     }			        	   
			           }
			           else
			           {
			        	     int  DPCounter =  Integer.parseInt(noOfDPsToUnassign);
			        	     
			        	     //Traverse and click on Unassign icon till DP	Counter			        	     
			        	     for(int k=0;k<DPCounter;k++)
			        	     {		
			        	    	 oGenericUtils.clickOnElement("u", "Expand All");
			        	    	 TopicTitleUI  = objSeleniumUtils.get_TextFrom_Locator(refPresentationDeck.FirstTopicLabel);
			        	          UnAssignIconXpath1 = StringUtils.replace(refPresentationDeck.DPKeyAssignIcon,"TopicNameArg",TopicTitleUI);
			        	         UnAssignIconXpath = StringUtils.replace(UnAssignIconXpath1,"DPKeyArg",DPKeysVals.get(k));
			        	         objSeleniumUtils.highlightElement(UnAssignIconXpath);
			        	         objSeleniumUtils.Click_given_Xpath(UnAssignIconXpath);     //Click unassign icon for the DP Card			        	         
			        	     }
			        	     
			           }			  
			  
		       break;		
		      }
		
}
catch(Exception e)
{
	System.out.println("Script not executed;"+e.getMessage());
	Assert.assertTrue("Script not executed",false);
	getDriver().quit();
}		
	}
	public void verify(String sDescription, boolean blnStatus){
		GenericUtils.Verify(sDescription,blnStatus);
	}

	@Step
	public void selectDPsatGivenLevelinPayerLOBView(String slevel) {

		refPresentationProfile.selectDPsatGivenLevelinPayerLOBView(slevel);

	}

	@Step
	public void verifyCancelFunctionality(String sOperation) {

		refPresentationProfile.ClickonDecision(sOperation);
		refPresentationProfile.verifyCancelFunctionality();


	}

	@Step
	public void verifyEditProfileFunctionality() throws InterruptedException {

		boolean blnEdited = refPresentationProfile.editFunctionality();
		
		verify("Presentation should be edited",blnEdited);

	}

	@Step
	public void applyFiltershavingNooppurtunities() throws InterruptedException {

		String sPayer = Serenity.sessionVariableCalled("NoFilterPayer");
		String sLOB = Serenity.sessionVariableCalled("NoFilterInsurance");

		onFilterDrawer.updateFilterCheckBox("Payers","SELECT ALL CHECKBOX","","UNSELECT ALL");

		onFilterDrawer.updateFilterCheckBox("LOB","SELECT ALL CHECKBOX","","UNSELECT ALL");

		//onFilterDrawer.selectPayershortsAndLOB(sLOB, "SELECT");
		objSeleniumUtils.clickGivenXpath(StringUtils.replace(oCPWPage.Span_contains_text, "value", sPayer)+"[contains(@class,'checkbox')]");
		objSeleniumUtils.clickGivenXpath(StringUtils.replace(oCPWPage.Span_contains_text, "value", sLOB)+"[contains(@class,'checkbox')]");
		objSeleniumUtils.clickGivenXpath(StringUtils.replace(oCPWPage.Span_contains_text, "value", "Apply"));
		objSeleniumUtils.waitForContentLoad();
		//onFilterDrawer.selectPayershortsAndLOB(sPayer, "SELECT");
	}

	@Step
	public void validatePresentationsforClient(String clientName)
	{//Serenity.setSessionVariable("clientkey").to("48");
		ArrayList<String>  NotdisplayedPres =  new ArrayList<String>();
		ArrayList<String>  BigPres =  new ArrayList<String>();
		  List<String>  DBPresProfileNames =  new ArrayList<String>();  
		   List<String>  UIPresProfileNames =  new ArrayList<String>();  
		   List<WebElement>  UIPresProfileNamesElmnts =  new ArrayList<WebElement>();  
		   String Pres1Xpath = "";
		   String Pres2Xpath = "";
		   int i=1;
		
		 //Retrieve PresNames for the Client from DB		  
		  DBPresProfileNames = refMongoDBUtils.retrievePresentationProfileCollectionValues(clientName,"PresentationProfiles","AllProfiles");
		
		  // Compare with UI PresNames
		    //UIPresProfileNames = objSeleniumUtils.getWebElementValuesAsList(refPresentationProfile.AllPresentationNames);	
		    int UIPressize=objSeleniumUtils.get_Matching_WebElement_count(refPresentationProfile.AllPresentationNames);
		    for (int j = 1; j <= UIPressize; j++)
		    {
		    	if(!objSeleniumUtils.is_WebElement_Displayed(refPresentationProfile.AllPresentationNames+"["+j+"]"))
		    	{
		    		do
		    		{
		    			objSeleniumUtils.clickGivenXpath(refPresentationProfile.RightArrow);
		    		}
		    		while(!objSeleniumUtils.is_WebElement_Displayed(refPresentationProfile.AllPresentationNames+"["+j+"]"));
		    	}
		    	String uipres=objSeleniumUtils.get_TextFrom_Locator(refPresentationProfile.AllPresentationNames+"["+j+"]").trim();
		    	
		    	if(!uipres.contains("..."))
		    	{
		    		System.out.println(j+"."+uipres);
			    	UIPresProfileNames.add(uipres);	
		    	}
		    	else
		    	{
		    		BigPres.add(uipres);
		    	}
		    	
		    	
		    	
			}

			  if((DBPresProfileNames.size()==0) &&  (UIPresProfileNames.size()==0) )
			  {
				    Assert.assertTrue("There are no Presentations associated with the client in DB and UI also,So cannot proceed with scenario execution,please choose another client",false);
			    	getDriver().quit();
			  }		    
			//verify Presentation profiles DBList with UIlist 
			for (String dbpres : DBPresProfileNames) 
			{
				if(UIPresProfileNames.contains(dbpres.trim()))
				{
					GenericUtils.Verify("DB Presentation '"+dbpres+"' is available in the UI", true);
				}
				else
				{
					NotdisplayedPres.add(dbpres.trim());
					
				}
				
			}
			
			if(BigPres.size()==NotdisplayedPres.size())
			{
				System.out.println("ALl DB Presentations available in the UI");
			}
			else
			{
				GenericUtils.Verify("NotavailablePrescount::"+NotdisplayedPres.size()+",Presentations '"+NotdisplayedPres+"' are not available in the UI", NotdisplayedPres.size()==0);	
			}
			
			
		    		    
		    /*//Checking the presentation count in DB and UI
		    if(DBPresProfileNames.size() == UIPresProfileNames.size())
		    {
		    	 Assert.assertTrue("All Presentations associated with the client are displayed in UI",true);
		    }
		    else
		    {
		    	Assert.assertTrue("All Presentations associated with the client are not displayed in UI,DBsize::"+DBPresProfileNames.size()+",UIsize::"+UIPresProfileNames.size()+",UIList::"+UIPresProfileNames+"DBList::"+DBPresProfileNames,false);
		    	getDriver().quit();
		    }*/
		    
		    /* int Listsize = DBPresProfileNames.size();
		    
		      if(Listsize>2)
		      {
			     //Checking the First and Last presentation in the DB List with the UI presentation names
			     Pres1Xpath = StringUtils.replace(refPresentationProfile.PresentationTabName,"PresNameArg",DBPresProfileNames.get(0));		
			     Pres2Xpath = StringUtils.replace(refPresentationProfile.PresentationTabName,"PresNameArg",DBPresProfileNames.get(Listsize-1));		   
		        }else if(Listsize ==1)		    	 
			     {
		    	     Pres1Xpath = StringUtils.replace(refPresentationProfile.PresentationTabName,"PresNameArg",DBPresProfileNames.get(0));
			     }
		    
		    //initializing variables
		    boolean sVal = false;
			boolean found = false;
					    
			    do {						
							  sVal = objSeleniumUtils.is_WebElement_Displayed(Pres1Xpath);					   
							if (sVal){		
								objSeleniumUtils.highlightElement(Pres1Xpath);
								oGenericUtils.clickOnElement(Pres1Xpath);		
								objSeleniumUtils.waitForContentLoad();
								oGenericUtils.isElementExist(refPresentationProfile.PresentationScreen);		
								found = true;
							}else{oGenericUtils.clickOnElement(refPresentationProfile.RightArrow);}
			
						}while((sVal ==false)||(found ==false));				
			    
			       //Resetting the boolean values
			           found = false;
			           sVal = false;
			       
			           
			           if(Listsize>2)
					      {  						           
								do {						
											sVal = objSeleniumUtils.is_WebElement_Displayed(Pres2Xpath);
											if (sVal){		
												objSeleniumUtils.highlightElement(Pres2Xpath);
												oGenericUtils.clickOnElement(Pres2Xpath);			
												objSeleniumUtils.waitForContentLoad();
												oGenericUtils.isElementExist(refPresentationProfile.PresentationScreen);		
												found = true;
											}else{oGenericUtils.clickOnElement(refPresentationProfile.RightArrow);}
				
						     	}while((sVal ==false)||(found ==false));		 
			
								   if(found == true)
								    {
								    	 Assert.assertTrue("Presentation Name associated with the client are displayed in UI",true);
								    }
								    else
								    {
								    	Assert.assertTrue("Presentation Name associated with the client are not displayed in UI",false);
								    	getDriver().quit();
								    }
					      }	//End of If	   
*/}

	@Step
	public void validatePopupButton(String btnName) 
	{
		
		String  prop = objSeleniumUtils.Get_Value_By_given_attribute("aria-disabled", refPresentationProfile.sPresentationAssignmentOKBtn);
		 if(prop.equalsIgnoreCase("true"))
		 {
			 Assert.assertTrue("Okay button is disabled",true);
		 }
		 else
		 {
			 Assert.assertTrue("Okay button is not disabled",false);
		 }
	}
		
	@Step
	public void applyAllFiltersinPresentationProfile() throws InterruptedException {

		onFilterDrawer.updateFilterCheckBox("Payers","SELECT ALL CHECKBOX","","SELECT ALL");

		onFilterDrawer.updateFilterCheckBox("LOB","SELECT ALL CHECKBOX","","SELECT ALL");

		oGenericUtils.clickButton(By.xpath("//span[contains(text(),'Apply')]"));
		objSeleniumUtils.waitForContentLoad();
	}
	
	@Step
	public void deletethePresentationProfile(String sWarningMessage) throws InterruptedException {

		String sPresenation = Serenity.sessionVariableCalled("PresentationName");
		refPresentationProfile.deleteGivenPresentation(sPresenation,sWarningMessage);
	}
	
	@Step
	public void navigateToPreviousandNextDP() {
		refPresentationProfile.navigateToNextDP();
		refPresentationProfile.navigateToPreviousDP();
		refPresentationProfile.abilityToViewDPinDecisionView();

	}

	@Step
	public void verifyStatusUpdatedinDatabase(String sStatus) 
	{
	if(sStatus.contains("Test Only"))
	{
		sStatus=StringUtils.substringBefore(sStatus, "Test Only").trim();
	}
	String sNote=null;
	String sProcessingToinDB=null;
	String sDOSFrominDB=null;
	String sDOSToinDB=null;
	String sProcessingFrominDB=null;
	String sOpportunityStatus=null;
	
	List<String> DPKeyList=Arrays.asList(Serenity.sessionVariableCalled("DPkey").toString().split(","));
	//String sDP = Serenity.sessionVariableCalled("CapturedDPKey");
	for (int i = 0; i < DPKeyList.size(); i++) 
	{
		String sDP = DPKeyList.get(i).trim();
		HashMap <String,Object> sCapturedValuesinDB = MongoDBUtils.verifyStatusinDBforGivenDP(sDP);

		String sCapturedStatusinDB = sCapturedValuesinDB.get("Status").toString();
		verify("Expected status in DB is "+sStatus+",For the Dp=>"+sDP,sCapturedStatusinDB.equalsIgnoreCase(sStatus));
		
		switch(sStatus)
		{
		
		case "Approve Test Only":
		case "Approve":
			 sDOSFrominDB = sCapturedValuesinDB.get("DOS FROM").toString();
			verify("Expected Date of Service From in DB is "+ProjectVariables.DOSFROM+" and Actual is "+sDOSFrominDB+",For the Dp=>"+sDP,sDOSFrominDB.equalsIgnoreCase(ProjectVariables.DOSFROM));

			 sDOSToinDB = sCapturedValuesinDB.get("DOS TO").toString();
			verify("Expected Date of Service To in DB is "+ProjectVariables.DOSTO+" and Actual is "+sDOSToinDB+",For the Dp=>"+sDP,sDOSToinDB.equalsIgnoreCase(ProjectVariables.DOSTO));

			 sProcessingFrominDB = sCapturedValuesinDB.get("PROCESSING FROM").toString();
			verify("Expected Processing From date in DB is "+ProjectVariables.PROCESSIONINGFROM+" and Actual is "+sProcessingFrominDB+",For the Dp=>"+sDP,sProcessingFrominDB.equalsIgnoreCase(ProjectVariables.PROCESSIONINGFROM));

			 sProcessingToinDB = sCapturedValuesinDB.get("PROCESSING TO").toString();
			verify("Expected Processing To date in DB is "+ProjectVariables.PROCESSIONINGTO+" and Actual is "+sProcessingToinDB+",For the Dp=>"+sDP,sProcessingToinDB.equalsIgnoreCase(ProjectVariables.PROCESSIONINGTO));

			 sOpportunityStatus = sCapturedValuesinDB.get("OpportunityStatus").toString();
			verify("Expected Opportunity status in DB is '"+Serenity.sessionVariableCalled("OpportunityStatus")+"' and Actual is '"+sOpportunityStatus+"',For the Dp=>"+sDP,sOpportunityStatus.equalsIgnoreCase(Serenity.sessionVariableCalled("OpportunityStatus")));
			
			
			 sNote = sCapturedValuesinDB.get("Note").toString().trim();
			 
			verify("Expected Note/Modifications in DB is "+Serenity.sessionVariableCalled("Note")+" and Actual is '"+sNote+"',For the Dp=>"+sDP,sNote.equalsIgnoreCase(Serenity.sessionVariableCalled("Note")));
			
			
		break;
		case "Approve with Mod":
		 case "Approve with Mod Test Only":
			 sDOSFrominDB = sCapturedValuesinDB.get("DOS FROM").toString();
			verify("Expected Date of Service From in DB is "+ProjectVariables.DOSFROM+" and Actual is "+sDOSFrominDB+",For the Dp=>"+sDP,sDOSFrominDB.equalsIgnoreCase(ProjectVariables.DOSFROM));

			 sDOSToinDB = sCapturedValuesinDB.get("DOS TO").toString();
			verify("Expected Date of Service To in DB is "+ProjectVariables.DOSTO+" and Actual is "+sDOSToinDB+",For the Dp=>"+sDP,sDOSToinDB.equalsIgnoreCase(ProjectVariables.DOSTO));

			 sProcessingFrominDB = sCapturedValuesinDB.get("PROCESSING FROM").toString();
			verify("Expected Processing From date in DB is "+ProjectVariables.PROCESSIONINGFROM+" and Actual is "+sProcessingFrominDB+",For the Dp=>"+sDP,sProcessingFrominDB.equalsIgnoreCase(ProjectVariables.PROCESSIONINGFROM));

			 sProcessingToinDB = sCapturedValuesinDB.get("PROCESSING TO").toString();
			verify("Expected Processing To date in DB is "+ProjectVariables.PROCESSIONINGTO+" and Actual is "+sProcessingToinDB+",For the Dp=>"+sDP,sProcessingToinDB.equalsIgnoreCase(ProjectVariables.PROCESSIONINGTO));

			 sOpportunityStatus = sCapturedValuesinDB.get("OpportunityStatus").toString();
			verify("Expected Opportunity status in DB is '"+Serenity.sessionVariableCalled("OpportunityStatus")+"' and Actual is '"+sOpportunityStatus+"',For the Dp=>"+sDP,sOpportunityStatus.equalsIgnoreCase(Serenity.sessionVariableCalled("OpportunityStatus")));
			
			
			 sNote = StringUtils.substringBetween(sCapturedValuesinDB.get("Note").toString(), "Other:", ";").trim();
			 
			verify("Expected Note/Modifications in DB is "+Serenity.sessionVariableCalled("Note")+" and Actual is '"+sNote+"',For the Dp=>"+sDP,sNote.equalsIgnoreCase(Serenity.sessionVariableCalled("Note")));
			
			
		break;
		
		case "Reject":
			String sReasons = sCapturedValuesinDB.get("Reason").toString();
			verify("Expected reject reason in DB is "+Serenity.sessionVariableCalled("Reason")+" and Actual is '"+sReasons+"',For the Dp=>"+sDP,sReasons.equalsIgnoreCase(Serenity.sessionVariableCalled("Reason")));
			
			 sNote = sCapturedValuesinDB.get("Note").toString();
			verify("Expected Notes in DB is "+Serenity.sessionVariableCalled("Note")+" and Actual is '"+sNote+"',For the Dp=>"+sDP,sNote.equalsIgnoreCase(Serenity.sessionVariableCalled("Note")));
			
		break;
		case "Defer":
			 sProcessingToinDB = sCapturedValuesinDB.get("PROCESSING TO").toString();
			verify("Expected Date in DB is "+ProjectVariables.PROCESSIONINGTO+" and Actual is "+sProcessingToinDB+",For the Dp=>"+sDP,sProcessingToinDB.equalsIgnoreCase(ProjectVariables.PROCESSIONINGTO));

			
		break;
		case "Follow up":
			 sProcessingToinDB = sCapturedValuesinDB.get("PROCESSING TO").toString();
			verify("Expected Date in DB is "+ProjectVariables.PROCESSIONINGTO+" and Actual is "+sProcessingToinDB+",For the Dp=>"+sDP,sProcessingToinDB.equalsIgnoreCase(ProjectVariables.PROCESSIONINGTO));

			String sResponsibleParty = sCapturedValuesinDB.get("FollowUpOwner").toString();
			verify("Expected ResponsibleParty in DB is "+Serenity.sessionVariableCalled("Responsibleparty")+" and Actual is "+sResponsibleParty+",For the Dp=>"+sDP,sResponsibleParty.equalsIgnoreCase(Serenity.sessionVariableCalled("Responsibleparty")));

			
		break;
		default:
			Assert.assertTrue("case not found===>"+sStatus, false);
		break;
		}
		
		if(!sStatus.contains("Approve with Mod"))
		{
			sNote = sCapturedValuesinDB.get("Note").toString().trim();
			verify("Expected Notes in DB is "+Serenity.sessionVariableCalled("Note")+" and Actual is '"+sNote+"',For the Dp=>"+sDP,sNote.equalsIgnoreCase(Serenity.sessionVariableCalled("Note")));
				
		}
				

		
	}
	}

	@Step
	public void verifyModifiedValuesinDatabase(String sStatus) throws ParseException {String sNote=null;
	String sProcessingToinDB=null;
	String sDOSFrominDB=null;
	String sDOSToinDB=null;
	String sProcessingFrominDB=null;
	String sOpportunityStatus=null;
	
	String sDP = Serenity.sessionVariableCalled("CapturedDPKey");
	String sPayer = Serenity.sessionVariableCalled("ModifiedPayer");
	String sPayerKey = MongoDBUtils.Retrieve_the_PayerKey_From_mongodb_with_the_given_Payershort(sPayer, "payerShort");
	String sClaimType = Serenity.sessionVariableCalled("ModifiedClaim");
	HashMap <String,Object> sCapturedValuesinDB = MongoDBUtils.verifyStatusinDBforGivenDP(sDP,sPayerKey,sClaimType);

	
	switch(sStatus)
	{
	
	case "Approve Test Only":
	case "Approve":
		 sDOSFrominDB = sCapturedValuesinDB.get("DOS FROM").toString();
		verify("Expected Date of Service From in DB is "+ProjectVariables.DOSFROM+" and Actual is "+sDOSFrominDB+",For the Dp=>"+sDP,sDOSFrominDB.equalsIgnoreCase(ProjectVariables.DOSFROM));

		 sDOSToinDB = sCapturedValuesinDB.get("DOS TO").toString();
		verify("Expected Date of Service To in DB is "+ProjectVariables.DOSTO+" and Actual is "+sDOSToinDB+",For the Dp=>"+sDP,sDOSToinDB.equalsIgnoreCase(ProjectVariables.DOSTO));

		 sProcessingFrominDB = sCapturedValuesinDB.get("PROCESSING FROM").toString();
		verify("Expected Processing From date in DB is "+ProjectVariables.PROCESSIONINGFROM+" and Actual is "+sProcessingFrominDB+",For the Dp=>"+sDP,sProcessingFrominDB.equalsIgnoreCase(ProjectVariables.PROCESSIONINGFROM));

		 sProcessingToinDB = sCapturedValuesinDB.get("PROCESSING TO").toString();
		verify("Expected Processing To date in DB is "+ProjectVariables.PROCESSIONINGTO+" and Actual is "+sProcessingToinDB+",For the Dp=>"+sDP,sProcessingToinDB.equalsIgnoreCase(ProjectVariables.PROCESSIONINGTO));

		 sOpportunityStatus = sCapturedValuesinDB.get("OpportunityStatus").toString();
		verify("Expected Opportunity status in DB is '"+Serenity.sessionVariableCalled("OpportunityStatus")+"' and Actual is '"+sOpportunityStatus+"',For the Dp=>"+sDP,sOpportunityStatus.equalsIgnoreCase(Serenity.sessionVariableCalled("OpportunityStatus")));
		
		 sNote = sCapturedValuesinDB.get("Note").toString();
		 
		verify("Expected Note/Modifications in DB is "+Serenity.sessionVariableCalled("Note")+" and Actual is '"+sNote+"',For the Dp=>"+sDP,sNote.equalsIgnoreCase(Serenity.sessionVariableCalled("Note")));
		
		
	break;
	case "Approve with Mod":
	case "Approve with Mod Test Only":
		 sDOSFrominDB = sCapturedValuesinDB.get("DOS FROM").toString();
		verify("Expected Date of Service From in DB is "+ProjectVariables.DOSFROM+" and Actual is "+sDOSFrominDB+",For the Dp=>"+sDP,sDOSFrominDB.equalsIgnoreCase(ProjectVariables.DOSFROM));

		 sDOSToinDB = sCapturedValuesinDB.get("DOS TO").toString();
		verify("Expected Date of Service To in DB is "+ProjectVariables.DOSTO+" and Actual is "+sDOSToinDB+",For the Dp=>"+sDP,sDOSToinDB.equalsIgnoreCase(ProjectVariables.DOSTO));

		 sProcessingFrominDB = sCapturedValuesinDB.get("PROCESSING FROM").toString();
		verify("Expected Processing From date in DB is "+ProjectVariables.PROCESSIONINGFROM+" and Actual is "+sProcessingFrominDB+",For the Dp=>"+sDP,sProcessingFrominDB.equalsIgnoreCase(ProjectVariables.PROCESSIONINGFROM));

		 sProcessingToinDB = sCapturedValuesinDB.get("PROCESSING TO").toString();
		verify("Expected Processing To date in DB is "+ProjectVariables.PROCESSIONINGTO+" and Actual is "+sProcessingToinDB+",For the Dp=>"+sDP,sProcessingToinDB.equalsIgnoreCase(ProjectVariables.PROCESSIONINGTO));

		 sOpportunityStatus = sCapturedValuesinDB.get("OpportunityStatus").toString();
		verify("Expected Opportunity status in DB is '"+Serenity.sessionVariableCalled("OpportunityStatus")+"' and Actual is '"+sOpportunityStatus+"',For the Dp=>"+sDP,sOpportunityStatus.equalsIgnoreCase(Serenity.sessionVariableCalled("OpportunityStatus")));
		
		 sNote = StringUtils.substringBetween(sCapturedValuesinDB.get("Note").toString(), "Other:", ";").trim();
		 
		verify("Expected Note/Modifications in DB is "+Serenity.sessionVariableCalled("Note")+" and Actual is '"+sNote+"',For the Dp=>"+sDP,sNote.equalsIgnoreCase(Serenity.sessionVariableCalled("Note")));
		
		
	break;
	
	case "Reject":
		String sReasons = sCapturedValuesinDB.get("Reason").toString();
		verify("Expected reject reason in DB is "+Serenity.sessionVariableCalled("Reason")+" and Actual is '"+sReasons+"',For the Dp=>"+sDP,sReasons.equalsIgnoreCase(Serenity.sessionVariableCalled("Reason")));
		
		 sNote = sCapturedValuesinDB.get("Note").toString();
		verify("Expected Notes in DB is "+Serenity.sessionVariableCalled("Note")+" and Actual is '"+sNote+"',For the Dp=>"+sDP,sNote.equalsIgnoreCase(Serenity.sessionVariableCalled("Note")));
		
	break;
	case "Defer":
		 sProcessingToinDB = sCapturedValuesinDB.get("PROCESSING TO").toString();
		verify("Expected Date in DB is "+ProjectVariables.PROCESSIONINGTO+" and Actual is "+sProcessingToinDB+",For the Dp=>"+sDP,sProcessingToinDB.equalsIgnoreCase(ProjectVariables.PROCESSIONINGTO));

		
	break;
	case "Follow up":
		 sProcessingToinDB = sCapturedValuesinDB.get("PROCESSING TO").toString();
		verify("Expected Date in DB is "+ProjectVariables.PROCESSIONINGTO+" and Actual is "+sProcessingToinDB+",For the Dp=>"+sDP,sProcessingToinDB.equalsIgnoreCase(ProjectVariables.PROCESSIONINGTO));

		String sResponsibleParty = sCapturedValuesinDB.get("FollowUpOwner").toString();
		verify("Expected ResponsibleParty in DB is "+Serenity.sessionVariableCalled("Responsibleparty")+" and Actual is "+sResponsibleParty+",For the Dp=>"+sDP,sResponsibleParty.equalsIgnoreCase(Serenity.sessionVariableCalled("Responsibleparty")));

		
	break;
	default:
		Assert.assertTrue("case not found===>"+sStatus, false);
	break;
	}
	
	if(!sStatus.contains("Approve with Mod"))
	{
		sNote = sCapturedValuesinDB.get("Note").toString().trim();
		 
		verify("Expected Notes in DB is "+Serenity.sessionVariableCalled("Note")+" and Actual is '"+sNote+"',For the Dp=>"+sDP,sNote.equalsIgnoreCase(Serenity.sessionVariableCalled("Note")));
			
	}
	}

	@Step
	public void verifyCapturedDecisioninPayerLOBGrid(String sStatus) {
		List<String> DPKeyList=Arrays.asList(Serenity.sessionVariableCalled("DPkey").toString().split(","));
		refPresentationProfile.getOppurtunityHierarchyView();
		refPresentationProfile.ExpandAllinHierarchyView();
		for (int i = 0; i < DPKeyList.size(); i++) 
		{
			//String sDP = Serenity.sessionVariableCalled("CapturedDPKey");
			String sDP = DPKeyList.get(i).trim();
			
			oGenericUtils.clickOnElementContainsText("label", sDP);
			objSeleniumUtils.defaultWait(1);
			refPresentationProfile.verifyGridUpdatedwithStatus(sStatus,sDP);
			

		}
		
	}

	@Step
	public void verifyCapturedDecisionValuesinPayerLOBGridPopup(String sStatus) {
		if(sStatus.contains("Test Only"))
		{
			sStatus=StringUtils.substringBefore(sStatus,"Test Only").trim();
		}
		
		String sDP = getDriver().findElement(By.xpath("//div[contains(@class,'dp')]//span[contains(text(),'DP')]")).getText().split(" ")[1].trim();
		String sPayer = getDriver().findElements(By.xpath("//tbody[@role ='rowgroup']/tr[1]/td[1]/label/span")).get(0).getText().split(" ")[0].trim();
		String sClaimType = getDriver().findElements(By.xpath("//tbody[@role ='rowgroup']/tr[1]/td[1]/label/span/span")).get(0).getText();
		sClaimType = StringUtils.substringBetween(sClaimType, "[","]").trim();
		String Insurance=StringUtils.substringBetween(objSeleniumUtils.Get_Value_By_given_attribute("class", "(//span[text()='"+sStatus+"'])[1]//ancestor::td"),"column-","mat");
		
		
		Serenity.setSessionVariable("CapturedInsuranceKey").to(oGenericUtils.Retrieve_the_insuranceKey_from_insurance(Insurance.trim()));
		Serenity.setSessionVariable("CapturedDPKey").to(sDP);
		Serenity.setSessionVariable("ModifiedPayer").to(sPayer);
		Serenity.setSessionVariable("ModifiedClaim").to(sClaimType);
		oGenericUtils.clickOnElementContainsText("span", sStatus);
		oGenericUtils.isElementExist("label", "Capture Decision");
		refPresentationProfile.verifyPayerLobGridPopUp(sStatus,ProjectVariables.DOSFROM,ProjectVariables.DOSTO,ProjectVariables.PROCESSIONINGFROM,ProjectVariables.PROCESSIONINGTO,sDP);	

		//To close the popup
		objSeleniumUtils.clickGivenXpath(StringUtils.replace(oCPWPage.ExportCancelbutton, "Export", "Capture"));
		objSeleniumUtils.defaultWait(ProjectVariables.TImeout_1_Seconds);
		objSeleniumUtils.clickGivenXpath(oCPWPage.popupSecondTimeCancel);
		objSeleniumUtils.defaultWait(ProjectVariables.TImeout_3_Seconds);
		Serenity.setSessionVariable("PreviousDecision").to(sStatus);
		
	}

	@Step
	public void navigateToPreviousDP() {

		bringtobaseState();

		do {
			boolean blnNextEnabled = getDriver().findElement(By.xpath("//button[text() = 'Next']")).isEnabled();
			if (blnNextEnabled){
				oGenericUtils.clickOnElementContainsText("button", "Next");
			}		
		}while(getDriver().findElement(By.xpath("//button[text() = 'Next']")).isEnabled());


		List<WebElement> sDPElements = getDriver().findElements(By.xpath("//a[contains(text(), 'DP')]"));

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
	
	@Step
	public void bringtobaseState(){

		List<WebElement> sDPElements = getDriver().findElements(By.xpath("//a[contains(text(), 'DP')]"));
		oGenericUtils.clickOn(sDPElements.get(0));
	}

	@Step
	public void modifyCapturedDecisionValuesinPayerLOBGridPopup()
	{

			ProjectVariables.DOSFROM =GenericUtils.getAddedDate(ProjectVariables.DOSFROM);
			ProjectVariables.DOSTO = GenericUtils.getAddedDate(ProjectVariables.DOSTO);
			ProjectVariables.PROCESSIONINGFROM =GenericUtils.getAddedDate(ProjectVariables.PROCESSIONINGFROM);
			ProjectVariables.PROCESSIONINGTO = GenericUtils.getAddedDate(ProjectVariables.PROCESSIONINGTO);

			boolean blnEnteredMod = refPresentationProfile.enterCaptureDetails(ProjectVariables.DOSFROM, ProjectVariables.DOSTO, ProjectVariables.PROCESSIONINGFROM, ProjectVariables.PROCESSIONINGTO);
			GenericUtils.Verify("Entered Date Values Successfully", blnEnteredMod);	
			oGenericUtils.setValue(By.xpath(refPresentationProfile.sModificationNotes),"Test Modified Notes");
			oGenericUtils.clickOnElementContainsText("button", "Capture");
			objSeleniumUtils.defaultWait(3);
			Serenity.setSessionVariable("Note").to("Test Modified Notes");	

		
	}
		
	@Step
	public void modifyCapturedDecisionValuesinPayerLOBGridPopup(String Decision)
	{boolean blnEnteredMod=false;

	//Select one of the pps to modify the decision
	objSeleniumUtils.clickGivenXpath("(//span[text()='"+Serenity.sessionVariableCalled("PreviousDecision")+"'])[1]//ancestor::td//span[contains(@class,'checkmark')]");
	objSeleniumUtils.defaultWait(ProjectVariables.TImeout_1_Seconds);
	objSeleniumUtils.clickGivenXpath(StringUtils.replace(oCPWPage.ButtonContainsClass, "value", "capture"));
	objSeleniumUtils.defaultWait(ProjectVariables.TImeout_3_Seconds);
	
	
	switch(Decision)
	{
	
	case "Approve Test Only":
	case "Approve":
		refPresentationProfile.ClickonDecision("Approve");
		ProjectVariables.DOSFROM =GenericUtils.getAddedDate(ProjectVariables.DOSFROM);
		ProjectVariables.DOSTO = GenericUtils.getAddedDate(ProjectVariables.DOSTO);
		ProjectVariables.PROCESSIONINGFROM =GenericUtils.getAddedDate(ProjectVariables.PROCESSIONINGFROM);
		ProjectVariables.PROCESSIONINGTO = GenericUtils.getAddedDate(ProjectVariables.PROCESSIONINGTO);

		 blnEnteredMod = refPresentationProfile.enterCaptureDetails(ProjectVariables.DOSFROM, ProjectVariables.DOSTO, ProjectVariables.PROCESSIONINGFROM, ProjectVariables.PROCESSIONINGTO);
		GenericUtils.Verify("Entered Date Values Successfully", blnEnteredMod);	
		//oGenericUtils.clickOnElement(StringUtils.replace(oCPWPage.labelwithtext, "svalue", "Other"));
		objSeleniumUtils.defaultWait(ProjectVariables.TImeout_3_Seconds);
		
		oGenericUtils.setValue(By.xpath(refPresentationProfile.sModificationNotes),"Test Modified Notes");
		Serenity.setSessionVariable("Note").to("Test Modified Notes");	

	break;
	case "Approve with Mod":
	case "Approve with Mod Test Only":
		refPresentationProfile.ClickonDecision("Approve with Mod");
		ProjectVariables.DOSFROM =GenericUtils.getAddedDate(ProjectVariables.DOSFROM);
		ProjectVariables.DOSTO = GenericUtils.getAddedDate(ProjectVariables.DOSTO);
		ProjectVariables.PROCESSIONINGFROM =GenericUtils.getAddedDate(ProjectVariables.PROCESSIONINGFROM);
		ProjectVariables.PROCESSIONINGTO = GenericUtils.getAddedDate(ProjectVariables.PROCESSIONINGTO);

		 blnEnteredMod = refPresentationProfile.enterCaptureDetails(ProjectVariables.DOSFROM, ProjectVariables.DOSTO, ProjectVariables.PROCESSIONINGFROM, ProjectVariables.PROCESSIONINGTO);
		GenericUtils.Verify("Entered Date Values Successfully", blnEnteredMod);	
		oGenericUtils.clickOnElement(StringUtils.replace(oCPWPage.labelwithtext, "svalue", "Other"));
		objSeleniumUtils.defaultWait(ProjectVariables.TImeout_3_Seconds);
		
		oGenericUtils.setValue(By.xpath(refPresentationProfile.sApprovewithModNotes),"Test Modified Notes");
		Serenity.setSessionVariable("Note").to("Test Modified Notes");	
		oGenericUtils.clickOnElement(StringUtils.replace(oCPWPage.labelwithtext, "svalue", "Capture Decision"));
		objSeleniumUtils.defaultWait(ProjectVariables.TImeout_2_Seconds);

	break;
	
	case "Reject":
		refPresentationProfile.ClickonDecision("Reject");
		//Enter the required data for reject decision in capture decision window
		refPresentationProfile.validateTheReasonsDropdownandEntergivenReasons(ProjectVariables.ModifiedRejectReasons);
		oGenericUtils.setValue(By.xpath(refPresentationProfile.sModificationNotes),"Test Modified Reject Notes");
		Serenity.setSessionVariable("Note").to("Test Modified Reject Notes");
		break;
	case "Defer":
		refPresentationProfile.ClickonDecision("Defer");
		oGenericUtils.setValue(By.xpath(PresentationProfile.getDynamicXpath("DATE","")),ProjectVariables.PROCESSIONINGTO);
		oGenericUtils.setValue(By.xpath(refPresentationProfile.sModificationNotes),"Test Modified Defer Notes");
		Serenity.setSessionVariable("Note").to("Test Modified Defer Notes");
		break;
	case "Follow up":
		refPresentationProfile.ClickonDecision("Follow up");
		oGenericUtils.setValue(By.xpath(PresentationProfile.getDynamicXpath("DATE","")),ProjectVariables.PROCESSIONINGTO);
		oGenericUtils.clickOnElementContainsText("div", "Client");
		oGenericUtils.setValue(By.xpath(refPresentationProfile.sModificationNotes),"Test Modified Follow up Notes");
		Serenity.setSessionVariable("Note").to("Test Modified Follow up Notes");
		Serenity.setSessionVariable("Responsibleparty").to("Client");
		break;
	default:
		Assert.assertTrue("case not found===>"+Decision, false);
	break;
	}
	
	if(Decision.toUpperCase().equalsIgnoreCase("APPROVE TEST ONLY")||Decision.toUpperCase().equalsIgnoreCase("APPROVE WITH MOD TEST ONLY"))
	{
		oGenericUtils.clickGivenXpath(StringUtils.replace(oCPWPage.Span_contains_text, "value", "Test Only"));
		Serenity.setSessionVariable("OpportunityStatus").to("0");
	}
	else if(Decision.toUpperCase().equalsIgnoreCase("APPROVE")||Decision.toUpperCase().equalsIgnoreCase("APPROVE WITH MOD"))
	{
		Serenity.setSessionVariable("OpportunityStatus").to("-1");
	}
	
	oGenericUtils.clickOnElementContainsText("button", "Capture");
	oGenericUtils.defaultWait(ProjectVariables.TImeout_3_Seconds);
	//Assert.assertTrue("User confirmation popup is displayed while modifying captured decision",!oGenericUtils.is_WebElement_Displayed(StringUtils.replace(oCPWPage.CaptureConfirmationButton, "value", "Yes")));
	oGenericUtils.clickOnElement(StringUtils.replace(oCPWPage.CaptureConfirmationButton, "value", "Yes"));
	objSeleniumUtils.waitForContentLoad();
	
	}
	
	@Step
	public void selectDPsatGivenLevelinPresentationView(String slevel) {
		
		
		
		refPresentationProfile.selectDPsatGivenLevelatPresView(slevel);

	}
	@Step
	public void select_DPs_at_level(String arg1) throws Throwable {
		refPresentationDeck.selectDPItem(arg1, "");
	}
	
	@Step
	public void applyFiltersforMultipleDPs() {

		onFilterDrawer.selectPayershortsAndLOB("Payer Shorts:LOB","SELECT");

		String sValue = Serenity.sessionVariableCalled("Topic");	

		onFilterDrawer.selectMedicalPolicyAndTopic("Topic", sValue, "SELECT");

	}
	
	@Step
	public void filtertheAssignedPolices(String slevel) {
		String sXpath = "//span[contains(text(),'NPP Opportunities')]/span";
		oGenericUtils.clickOnElement(sXpath);
		
		String sMedicalPolicy = Serenity.sessionVariableCalled("MedicalPolicy");
		Serenity.setSessionVariable("Medicalpolicy").to(sMedicalPolicy);
		
		String sValue = null;

		switch (slevel.toUpperCase()){

		case "MEDICAL POLICY":			
			sValue = Serenity.sessionVariableCalled("MedicalPolicy");			
			break;
		case "TOPIC":
			sValue = Serenity.sessionVariableCalled("Topic");
			break;
		case "DP":case "HEADER":
			sValue = Serenity.sessionVariableCalled("Topic");
			slevel = "Topic";
			break;

		}

		onFilterDrawer.selectMedicalPolicyAndTopic(slevel, sValue, "SELECT");


	}
	
	@Step
	public void assigneeProfileShouldbeDisplayunderReassigneeList() {
		refPresentationProfile.ClickonDecision("Re-Assign");
		String sAssigneePrf = Serenity.sessionVariableCalled("PresentationName");
		boolean sVal = refPresentationProfile.assigneeProfileShouldNotbeDisplayedunderReassigneeList(sAssigneePrf.trim());
		verify(sAssigneePrf+" Presentation should not be displayed under re assinee list",sVal);

	}
	
	@Step
	public void validatePopupButton(String btnName,String ExpectedProperty) 
	{
		objSeleniumUtils.highlightElement(refPresentationProfile.sPresentationAssignmentOKBtn);
		String  prop = objSeleniumUtils.Get_Value_By_given_attribute("aria-disabled", refPresentationProfile.sPresentationAssignmentOKBtn);
		
	switch(ExpectedProperty.toUpperCase())
	{
	
	case "ENABLED":
		 if(prop.equalsIgnoreCase("false"))
		 {
			 Assert.assertTrue("Okay button is enabled",true);
		 }
		 else
		 {
			 Assert.assertTrue("Okay button is not enabled",false);
		 }
		 break;
		 
	case "DISABLED":
		 if(prop.equalsIgnoreCase("true"))
		 {
			 Assert.assertTrue("Okay button is disabled",true);
		 }
		 else
		 {
			 Assert.assertTrue("Okay button is not disabled",false);
		 }
		 break;	  
		 
		 
	}	 
		 
		 
	}


	@Step
	public void validateIconStatus(String iconName,String ExpectedStatus)
	{
		
	String DeleteIcon = StringUtils.replace(refPresentationProfile.PresentationDeleteIcon ,"PresNameArg",Serenity.sessionVariableCalled("PresentationName"));
	 boolean  DeleteIconDisplayed = objSeleniumUtils.is_WebElement_Displayed(DeleteIcon);
		
		switch(ExpectedStatus.toUpperCase())
		{				
				case "NOTVISIBLE":							 
					 if(DeleteIconDisplayed==false)
					 {
						 Assert.assertTrue("Delete Icon for the Presentation not displayed as expected for Profile::"+Serenity.sessionVariableCalled("PresentationName"),true);
					 }
					 else
					 {
						 Assert.assertTrue("Delete Icon for the Presentation  displayed  for Profile::"+Serenity.sessionVariableCalled("PresentationName"),false);
						 getDriver().quit();
					 }
					 break;
					 
				case "VISIBLE":
							 if(DeleteIconDisplayed == true)
							 {
								 Assert.assertTrue("Delete Icon for the Presentation is displayed as expected for Profile::"+Serenity.sessionVariableCalled("PresentationName"),true);
							 }
							 else
							 {
								 Assert.assertTrue("Delete Icon for the Presentation  not displayed  for Profile::"+Serenity.sessionVariableCalled("PresentationName"),false);
								 getDriver().quit();
							 }
							 break;	
		}
	}

	@Step
	public void validatePresentation(String status) 
	{
		
		List<String> UIPresProfileNames =  new ArrayList<String>();
			
		String  AllPresXpath = "//div[@class='jqx-popover-content']//mat-radio-group/mat-radio-button//div[@class='mat-radio-label-content']/span";	
		UIPresProfileNames = objSeleniumUtils.getWebElementValuesAsList(AllPresXpath);		
		String PresName = Serenity.sessionVariableCalled("PresentationName");
		
		for(int i=0;i<UIPresProfileNames.size();i++)
		{	
			 if(UIPresProfileNames.get(i).equalsIgnoreCase(PresName))
			 {
				 Assert.assertTrue("Presentation Name:"+PresName+" is available in the AssignTo popup list of Presentations",false);
			 }			
		}
		
		Assert.assertTrue("Presentation Name:"+PresName+" is not available in the AssignTo popup list of Presentations as expected",true);
		System.out.println("Presentation Name:"+PresName+" is not available in the AssignTo popup list of Presentations as expected");
		
	}

	
	@Step
	public void reloadAppPage() 
	{
			//((Object) oGenericUtils).ReloadWebpage();
			objSeleniumUtils.waitForContentLoad();
	}
	//============================== Chaitanya ==========================================================//
	
	@Step
	public void validate_the_functionaity_of_assigned_DPkey_for_the_created_presentation(String captureDecision, String DPKeyCriteria) throws org.json.simple.parser.ParseException, IOException 
	{
		String SecondPresentaionName=null;
		if(captureDecision.equalsIgnoreCase("Re-Assign"))
		{
			SecondPresentaionName=StringUtils.substringAfter(refPresentationProfile.createPresentationThroughService(Serenity.sessionVariableCalled("User"), Serenity.sessionVariableCalled("Client"), "", "", "", ""), "-");
			Serenity.setSessionVariable("SecondPresentationName").to(SecondPresentaionName);
			getDriver().navigate().refresh();
			objSeleniumUtils.waitForContentLoad();
			System.out.println(SecondPresentaionName);
			
				
		}
		System.out.println(Serenity.sessionVariableCalled("DPkey").toString());
		//Perform the capture decision operation for the given decision
		refPresentationProfile.Perform_the_CaptureDecision_functionality_for_the_given(Serenity.sessionVariableCalled("DPkey"),captureDecision,DPKeyCriteria,SecondPresentaionName);
		
		//Validate the Presentation deck after UnAssign/ReAssign functionality of captured DPKeys
		refPresentationProfile.validate_the_Presentation_after_capturing_the_decision(SecondPresentaionName,captureDecision,DPKeyCriteria);
		
	
	}
	
	@Step
	public void validate_the_Presentation_deck_for(String popupcriteria) 
	{
		switch(popupcriteria)
		{
		case "Export Popup":
			//Select the given presentation and DPkey
			Assert.assertTrue("uanble to select the DPKey from given presentation,Presname==>"+Serenity.sessionVariableCalled("PresentationName")+",DPkey===>"+Serenity.sessionVariableCalled("DPkey"), oCPWPage.select_the_given_DPkey_at_Presentation_view(Serenity.sessionVariableCalled("PresentationName"), Serenity.sessionVariableCalled("DPkey"),""));
			
			//Verify the export popup in the presentation deck
			refPresentationProfile.validate_the_export_popup_headers_presentationdeck();
			
			refPresentationProfile.validate_the_export_popup_checkkbox_functionality();
		break;
		default:
			Assert.assertTrue("Case not found====>"+popupcriteria, false);
		break;
		}
		
	}
	
	@Step
	public void validateLOBInspectorWindow(){
		
		objSeleniumUtils.Click_given_Locator(StringUtils.replace(refPresentationProfile.DPCard_Lnk, "DPkeyArg", Serenity.sessionVariableCalled("DPkey")));
		
		String[] LOBs = objSeleniumUtils.get_All_Text_from_Locator(refPresentationProfile.DP_LOBAvailable);
		
		objSeleniumUtils.Click_given_Locator(refPresentationProfile.DP_LOBGrid);
		objSeleniumUtils.waitForContentLoad();	
		if(objSeleniumUtils.is_WebElement_Displayed(StringUtils.replace(refPresentationProfile.LOBInspectorTitle, "DPkeyArg", Serenity.sessionVariableCalled("DPkey")))){
			Assert.assertTrue("LOB indicator window is verified successfully", true);
		}else{
			Assert.assertTrue("LOB indicator window is not displayed", false);
		}
		
		String[] Payers = objSeleniumUtils.get_All_Text_from_Locator(refPresentationProfile.LOBInspectorPayersList);
		List<String> payerList = Arrays.asList(Payers);
		System.out.println(payerList);
		List<String> PayerListSortedOrder =payerList.stream().sorted().collect(Collectors.toList());
		System.out.println("Sorted Payers list" +PayerListSortedOrder);
		
		if(payerList.equals(PayerListSortedOrder)){
			Assert.assertTrue("Payer names displayed are sorted in alphabetical order", true);
		}else{
			Assert.assertTrue("Payer names displayed are not sorted in alphabetical order", false);
		}
		
		String insuranceKeys = Serenity.sessionVariableCalled("InsuranceKeys");
		String[] temp = insuranceKeys.split(",");
		for(int j = 0; j< temp.length;j++){		
			HashMap<String, String> oHashMap = new HashMap<String, String>();		
			oHashMap.put("1", "Medicare");
			oHashMap.put("2", "Medicaid");
			oHashMap.put("3", "Dual Eligible");
			oHashMap.put("7", "Commercial");
			oHashMap.put("8", "BlueCard");		
			oHashMap.put("9", "Federal Employee Program");		
			String insurance = oHashMap.get(temp[j].trim());	
			String Insurance =StringUtils.replace(refPresentationProfile.LOBInspectorLOBTitle, "Insurance", insurance);
			Serenity.setSessionVariable("Insurance").to(insurance);
			Assert.assertTrue("Insurance "+Serenity.sessionVariableCalled("Insurance")+" is not displayed in LOB inspector window", objSeleniumUtils.is_WebElement_Displayed(Insurance));	
		}
		
		for(int i=0;i<LOBs.length;i++){
			HashMap<String, String> oHashMap = new HashMap<String, String>();	
			oHashMap.put("MCD", "Medicaid");
			oHashMap.put("MCR", "Medicare");
			oHashMap.put("DUA", "Dual Eligible");
			oHashMap.put("COM", "Commercial");
			oHashMap.put("BLU", "BlueCard");		
			oHashMap.put("FED", "Federal Employee Program");		
			
			String LOB = objSeleniumUtils.get_TextFrom_Locator(StringUtils.replace(refPresentationProfile.LOBInspectorLOBHeader, "LOBHeader", Integer.toString(i+1)));
			if(oHashMap.get(LOBs[i]).trim().equalsIgnoreCase(LOB)){
				Assert.assertTrue("LOB "+LOB+" order is same as LOB indicator grid", true);
			}else{
				Assert.assertTrue("LOB "+LOB+" order is not same as LOB indicator grid", false);
			}
		}
		
		
		for(int k=0;k<payerList.size();k++){
			String  LOBindicator = objSeleniumUtils.get_TextFrom_Locator(StringUtils.replace(refPresentationProfile.LOBInspectorPayerValue, "Payer", payerList.get(k)));
			
			System.out.println("LOB indicator "+LOBindicator+" is verified successfully for the payer "+payerList.get(k));
			if(LOBindicator.equalsIgnoreCase("arrow_drop_down")){
				Assert.assertTrue("LOB indicator is verified as rejected in LOB indicator grid", true);
			}else if(LOBindicator.equalsIgnoreCase("arrow_drop_up")){
				Assert.assertTrue("LOB indicator is verified as rejected in LOB indicator grid", true);
			}else if(LOBindicator.equalsIgnoreCase("star")){
				Assert.assertTrue("LOB indicator is verified as no decision taken in LOB indicator grid", true);
			}
		}
		
		objSeleniumUtils.Click_given_Locator(refPresentationProfile.LOBInspectorCloseIcon);
		

		
	}

	@Step
	public void validate_the_list_of_DPs_in_Presentationview()
	{
		String DPkey=null;
		
		List<String> DPkeysList=Arrays.asList(Serenity.sessionVariableCalled("DPkey").toString().split(","));
		
		
		for (int i = 0; i < DPkeysList.size(); i++) 
		{
			DPkey=DPkeysList.get(i).trim();
			
			//validate the list of DPs,Topics,priortiy and corresponding savings 
			refPresentationProfile.validateThelistofDPsTopicsPriorityandSavings(DPkey);
			
		}
		
		
		
		
	}

	@Step
	public void validate_the_notes_section_in_presentationdeck_with_DB() 
	{
		
		boolean bStatus=false;
		String sPayers=null;
		String sLOB=null;
		String sClaimType=null;
		String sUINotes=null;
		String sCapturedUser=GenericUtils.RetreiveTheUsenameFromthegivenUserID(Serenity.sessionVariableCalled("user"));
		String sCapturedNotes=Serenity.sessionVariableCalled("DispositionNotes");
		String sCapturedDate=GenericUtils.SystemTime_in_the_given_format("MM/dd/yyyy");
		String sDPKey=Serenity.sessionVariableCalled("DPkey");
		String sPresentationName=Serenity.sessionVariableCalled("PresentationName");
		
		//DB Method to retrieve the Notessection details
		MongoDBUtils.GettheCapturedDispositionPayerLOBClaimTypesFromtheGiven(sDPKey);
		
		objSeleniumUtils.clickGivenXpath(StringUtils.replace(oCPWPage.labelcontainstext, "svalue", sDPKey));
		objSeleniumUtils.defaultWait(ProjectVariables.TImeout_3_Seconds);
		
		bStatus=objSeleniumUtils.is_WebElement_Displayed(oCPWPage.Notessection+"//p[@class='payes_txt']");
		if(sCapturedNotes!=null)
		{
			GenericUtils.Verify("Notes section displayed status=>"+bStatus+" in presentation=>"+sPresentationName+",DP=>"+sDPKey, bStatus);
			
			sPayers=StringUtils.substringAfter(objSeleniumUtils.get_TextFrom_Locator(oCPWPage.Notessection+"//p[@class='payes_txt']"),":").trim();
			sLOB=StringUtils.substringAfter(objSeleniumUtils.get_TextFrom_Locator(oCPWPage.Notessection+"//span[contains(@class,'LOB_txt')]"),":").trim();
			sClaimType=StringUtils.substringAfter(objSeleniumUtils.get_TextFrom_Locator(oCPWPage.Notessection+"//span[contains(@class,'Claim_Type_txt')]"),":").trim();
			
			GenericUtils.CompareTwoValues("Payers in Notessection for DP=>"+sDPKey+",Presentation=>"+sPresentationName, ProjectVariables.DB_PayershortList, sPayers);
			GenericUtils.CompareTwoValues("LOBs in Notessection for DP=>"+sDPKey+",Presentation=>"+sPresentationName, ProjectVariables.DB_insuranceList, sLOB);
			GenericUtils.CompareTwoValues("Claimtypes in Notessection for DP=>"+sDPKey+",Presentation=>"+sPresentationName, ProjectVariables.DB_claimtypeList, sClaimType);
			
			bStatus=objSeleniumUtils.is_WebElement_Displayed(StringUtils.replace(oCPWPage.Tag_contains_P, "value", sCapturedDate+"("+sCapturedUser+")"));
			GenericUtils.Verify("CapturedDate with Username=>"+sCapturedDate+"("+sCapturedUser+")"+" displayed status=>"+bStatus+" in presentation=>"+sPresentationName+",DP=>"+sDPKey, bStatus);
			
			sUINotes=objSeleniumUtils.get_TextFrom_Locator(StringUtils.replace(oCPWPage.Tag_P_contains_Class, "value", "notes_scroll"));
			GenericUtils.CompareTwoValues("Captured Notes in CPW,Presentation=>"+sPresentationName+",DPKey=>"+sDPKey, sUINotes, sCapturedNotes);
		
		}
		else
		{
			System.out.println("Notes section displayed Expectedstatus=>'false',Actual=>"+bStatus+" in presentation=>"+sPresentationName+",DP=>"+sDPKey);
			Assert.assertFalse("Notes section displayed Expectedstatus=>'false',Actual=>"+bStatus+" in presentation=>"+sPresentationName+",DP=>"+sDPKey, bStatus);
		}
		
		}

	@Step
	public void validate_the_display_hove_text_of_priortiy_reasons_in_presentaiondeck()
	{
		List<String> PriorityReasonsList=Arrays.asList(ProjectVariables.StaticOrderofPriorityReasons.split(","));
		
		for (int i = 0; i < PriorityReasonsList.size(); i++) 
		{
			//validate the display hover text for the given priority reason
			refPresentationProfile.validateTheDisplayhovertextoftheGivenPriorityreason(PriorityReasonsList.get(i).trim());
		}

	}

	@Step
	public void validate_the_Presentations_created_for_the_client(String Clientname) throws ParseException 
	{
		
		//DB method to retrieve the created presentations and thier times
		MongoDBUtils.GetTheConfiguredPresentationsforThegivenClientKey(Serenity.sessionVariableCalled("clientkey"));
		
		//Validate the Created presentations and thier order  with Db
		refPresentationProfile.validate_the_Presentations_created_for_the_client(Clientname);
		
		
		
		
	}

	@Step
	public void validateTheRuleRelationToolTip(String levelToCheck, String deckName) throws ElementNotFoundException 
	{
	
		refPresentationProfile.validateRuleRelationToolTip(levelToCheck, deckName);		
			
		}

	@Step
	public void userValidates(String sValidation, String sDeckType) {
		refPresentationProfile.userValidates(sValidation,sDeckType);
		
	}
	
	@Step
	public void createNewpresentationinChangeOpportunities()
	{
		//Enter Value
		Random randomGenerator = new Random();  
		int randomInt = randomGenerator.nextInt(1000);
		 String sPPName="AutoTestPM"+randomInt;
		Serenity.setSessionVariable("PresentationName").to(sPPName);
		oPresentationProfileValidations.createNewpresentationInChangeOpportunities(sPPName);
		oGenericUtils.isElementExist(StringUtils.replace(oPresentationProfileValidations.sChangeOppPres, "value", sPPName));
	}
	
	@Step
	public void userShouldnotChangePresntationwithSamename()
	{
	 String sPPName=Serenity.sessionVariableCalled("PresentationName");
		oPresentationProfileValidations.createNewpresentationInChangeOpportunities(sPPName);
		oGenericUtils.isElementExist(StringUtils.replace(oCPWPage.Span_contains_text, "value", ProjectVariables.ChangePresAlert));
		//Verify 'Cancel' button enabled
		oGenericUtils.isElementExist(oPresentationProfileValidations.sCancelEnable);
		//Verify 'OK ' button disabled
		Assert.assertTrue("okay button was not disabled,it should be disabled,after entering same presname", objSeleniumUtils.is_WebElement_Displayed(oPresentationProfileValidations.sOkDisable));;
		objSeleniumUtils.clickGivenXpath(oPresentationProfileValidations.sCancelEnable);
		objSeleniumUtils.defaultWait(ProjectVariables.TImeout_2_Seconds);
		Assert.assertTrue("Cancel alert message was not disabled,after clicking on that in changepres popup", objSeleniumUtils.is_WebElement_Displayed(StringUtils.replace(oCPWPage.Div_contains_text, "value", ProjectVariables.cancelPresAlert)));;
		//objSeleniumUtils.clickGivenXpath(StringUtils.replace(oPresentationProfileValidations.sCancelEnable, "Okay", "Go Back"));
		//objSeleniumUtils.defaultWait(ProjectVariables.TImeout_2_Seconds);
		//sGoBack
		objSeleniumUtils.clickGivenXpath(oPresentationProfileValidations.sGoBack);
		//Enter Value
		Random randomGenerator = new Random();  
		int randomInt = randomGenerator.nextInt(1000);
		 sPPName="AutoTestPM"+randomInt;
		//Enter Presentation Name under 'Presentation Name (Required)' section
		oGenericUtils.setValue(By.xpath(oPresentationProfileValidations.sPresentationName), sPPName);
		//Verify 'OK' button Enabled
		oGenericUtils.isElementExist(oPresentationProfileValidations.sOkEnable);
		//Click on 'OK' button
		oGenericUtils.clickButton(By.xpath(oPresentationProfileValidations.sOkEnable));
		objSeleniumUtils.waitForContentLoad();
		oGenericUtils.isElementExist(StringUtils.replace(oPresentationProfileValidations.sChangeOppPres, "value", sPPName));
	}
	
	@Step
	public void verifygivenfunctionalityinEditTopicPopup(String arg1) 
	{
		refPresentationProfile.userVerifyEditandDeletefunctionalityinTopicDesc(arg1);		
	}
		
	@Step
	public void userValidatesDPAssignmentforProfileWithDPType(String sProfile, String sDPType) throws InterruptedException {
		refPresentationProfile.userValidatesDPAssignmentforProfileWithDPType(sProfile,sDPType);
	}

	@Step
	public void userValidtesFunctionalityForDPType(String sValidation, String sDPType,String sFunctionality) throws InterruptedException {		
		refPresentationProfile.userValidtesFunctionalityForDPType(sValidation,sDPType,sFunctionality);
	}
	
	public void validate_edit_topic_description() throws InterruptedException {
		refPresentationProfile.validate_edit_topic_description();
		
	}

	@Step
	public void capture_decision_to_one_PPS_combination() throws InterruptedException {
		
		refSeleniumUtils.defaultWait(ProjectVariables.TImeout_3_Seconds);
		
		//oGenericUtils.gfn_Click_String_object_Xpath("Presenation tab is clicked","//span[@class='pres_pro_name'][contains(text(),'AutoTestPM627')]");
		
		/*refSeleniumUtils.gfn_Verify_String_Object_Exist("Dp is displayed", DP+"[1]");
		
        String sDP=refSeleniumUtils.get_TextFrom_Locator(DP+"[1]");
        
        GenericUtils.Verify("DP:"+sDP, true);*/
        
		objSeleniumUtils.gfn_Click_String_object_Xpath("",(StringUtils.replace(refPresentationProfile.Label_With_Contains, "sValue", "DP Type") + "/..//span"));

		GenericUtils.Verify("clicking on Information Only dropdown option",
				oGenericUtils.clickOnElement(StringUtils.replace(oCPWPage.Div_contains_Class, "value", "transformPanel")
						+ "//span[contains(text(),'Information Only')]"));

		GenericUtils.Verify("clicking on DP Type dropdown", oGenericUtils.clickOnElement(StringUtils.replace(refPresentationProfile.Label_With_Contains, "sValue", "DP Type") + "/..//span"));

		GenericUtils.Verify("clicking on Rules Only dropdown option",
				oGenericUtils.clickOnElement(StringUtils.replace(oCPWPage.Div_contains_Class, "value", "transformPanel")
						+ "//span[contains(text(),'Rules')]"));

		refSeleniumUtils.defaultWait(ProjectVariables.TImeout_5_Seconds);
			
		oGenericUtils.gfn_Click_String_object_Xpath("clicking on DP",refPresentationProfile.DP+"[1]");
			
		objSeleniumUtils.gfn_Verify_String_Object_Exist("Ready For Presentation checkbox is not selected", "//span[contains(text(),'Ready For Presentation')] /..//div//input[@aria-checked='false']");
	
		refSeleniumUtils.defaultWait(ProjectVariables.TImeout_5_Seconds);
		
		oGenericUtils.gfn_Click_String_object_Xpath("PPS is selected",refPresentationProfile.sPPSSelection+"[1]");
		
		refSeleniumUtils.defaultWait(ProjectVariables.TImeout_5_Seconds);
		
	    oGenericUtils.gfn_Click_String_object_Xpath("clicking on DP Level assign icon",StringUtils.replace(oCPWPage.ButtonContainsClass, "value", "captureIcon"));
		
	    refSeleniumUtils.defaultWait(ProjectVariables.TImeout_5_Seconds);
	    
		refSeleniumUtils.gfn_Click_String_object_Xpath("Approve Radio button is selected", "//div[contains(text(),'Approve')]//preceding-sibling::div");
		
		//refSeleniumUtils.gfn_Click_String_object_Xpath("Test only checkbox is selected","(//div[@class='mat-checkbox-inner-container'])[2]");

		oGenericUtils.setValue(By.xpath(PresentationProfile.sApproveNotes),"Test Approve Automation Notes");
		
		refSeleniumUtils.gfn_Click_On_Object("Capture button is clicked","button", "Capture");

		refSeleniumUtils.defaultWait(ProjectVariables.TImeout_2_Seconds);
		
		oGenericUtils.gfn_Click_String_object_Xpath("Yes button is clicked",StringUtils.replace(oCPWPage.CaptureConfirmationButton, "value", "Yes"));	
		
	    oGenericUtils.defaultWait(ProjectVariables.TImeout_5_Seconds);
	    
		oGenericUtils.gfn_Click_String_object_Xpath("Ready For Presentation checkbox is in selected state and clicked","//span[contains(text(),'Ready For Presentation')] /..//div//input[@aria-checked='true']/..");
		
		objSeleniumUtils.gfn_Verify_String_Object_Exist("Msg: The opportunities under this profile will be auto updated. Are you sure you want to unmark this profile from Ready for Presentation? is displayed","//label[contains(text(),'The opportunities under this profile will be auto updated. Are you sure you want to unmark this profile from Ready for Presentation?')]");
		
		oGenericUtils.gfn_Click_String_object_Xpath("No button is clicked","(//button[contains(text(),'No')])[position()=last()-1]");
		
		objSeleniumUtils.gfn_Verify_String_Object_Exist("Ready For Presentation checkbox is in selected state","//span[contains(text(),'Ready For Presentation')] /..//div//input[@aria-checked='true']");
		
		oGenericUtils.gfn_Click_String_object_Xpath("Ready For Presentation checkbox is in selected state and clicked","//span[contains(text(),'Ready For Presentation')] /..//div//input[@aria-checked='true']/..");
		
		oGenericUtils.gfn_Click_String_object_Xpath("Yes button is clicked","(//button[contains(text(),'Yes')])[position()=last()-1]");
		
		oGenericUtils.defaultWait(ProjectVariables.TImeout_5_Seconds);
		
		objSeleniumUtils.gfn_Verify_String_Object_Exist("Ready For Presentation checkbox is not selected", "//span[contains(text(),'Ready For Presentation')] /..//div//input[@aria-checked='false']");
		
		oGenericUtils.gfn_Click_String_object_Xpath("PPS is selected",refPresentationProfile.sPPSSelection+"[1]");
		
		oGenericUtils.defaultWait(ProjectVariables.TImeout_5_Seconds);
		
		oGenericUtils.gfn_Click_On_Object("FINALIZE btn is clicked","button", "FINALIZE");
	
		oGenericUtils.defaultWait(ProjectVariables.TImeout_8_Seconds);
		
		objSeleniumUtils.gfn_Verify_String_Object_Exist("Msg: Finalized decisions can not be edited. Are you sure you want to proceed? is displayed","//label[contains(text(),' Finalized decisions can not be edited. Are you sure you want to proceed?')]");
		
		oGenericUtils.gfn_Click_String_object_Xpath("No button is clicked","(//button[contains(text(),'No')])[position()=last()]");
		
		oGenericUtils.defaultWait(ProjectVariables.TImeout_5_Seconds);
		
		oGenericUtils.gfn_Click_On_Object("FINALIZE btn is clicked","button", "FINALIZE");
        
        oGenericUtils.defaultWait(ProjectVariables.TImeout_8_Seconds);
		
        objSeleniumUtils.gfn_Verify_String_Object_Exist("Msg: Finalized decisions can not be edited. Are you sure you want to proceed? is displayed","//label[contains(text(),' Finalized decisions can not be edited. Are you sure you want to proceed?')]");
		
        oGenericUtils.gfn_Click_String_object_Xpath("Yes button is clicked","(//button[contains(text(),'Yes')])[position()=last()]");
		
		oGenericUtils.defaultWait(ProjectVariables.TImeout_5_Seconds);
		
		boolean bl=!objSeleniumUtils.is_WebElement_enabled("//span[contains(text(),'Ready For Presentation')] /..//div//input[@aria-checked='true']");
		
		GenericUtils.Verify("Ready For Presentation is readyonly mode",bl);
	}
	
	@Step
	public void userValidatesChangeSummaryData(String sValidationType,String sCriteria){ 
		if(sCriteria.isEmpty())
			refPresentationProfile.userValidatesChangeSummaryData(sValidationType);
		else
			refPresentationProfile.userValidatesChangeSummaryData(sValidationType,sCriteria);
	}

	@Step
	public void userValidatesDpForProfile(String validationType, String Grid) {
		refPresentationProfile.userValidatesDpForProfile(validationType,Grid);
		
	}
	
}
	

