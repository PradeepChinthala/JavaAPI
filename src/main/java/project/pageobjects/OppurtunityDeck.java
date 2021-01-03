package project.pageobjects;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;

import org.junit.Assert;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import net.serenitybdd.core.Serenity;
import net.serenitybdd.core.pages.PageObject;
import project.utilities.MongoDBUtils;

import project.exceptions.ElementNotFoundException;
import project.utilities.GenericUtils;
import project.utilities.SeleniumUtils;
import project.variables.MonGoDBQueries;
import project.variables.ProjectVariables;

public class OppurtunityDeck  extends PageObject
{
	SeleniumUtils refSeleniumUtils;	
	 PresentationDeck  refPresentationDeck ;
	 GenericUtils  oGenericUtils = new  GenericUtils();
	 FilterDrawer  refFilterDrawer ;  
	 PresentationProfile   oPresProfile;
	 MonGoDBQueries oMonGoDBQueries = new MonGoDBQueries();
	 
	
	public String FirstPayershortxpath =  "(//tr[@role='row'][contains(@class,'mat-row')])[1]/td[1]//span";  
	
    public String FirstClaimType =  "(//tr[@role='row'][contains(@class,'mat-row')])[1]/td[1]//span/span";   
    
    public String InsuranceDesc =  "//tr[@role='row']//th[2]//label"; 
    
    public String sPresentationAssignmentCheckbox = "//div[@class='asign-popover']//span[normalize-space()='val']/parent::div/preceding-sibling::div";
    
	//public String  sMedPolicyAssignmentIcons =  "//app-cpd-opp-deck//mat-panel-title[@class='mat-expansion-panel-header-title']//div/label[starts-with(text(),'Medical Policy:')]/following::div[@class='container3']//i[@class='fa fa-arrow-circle-o-right assignIcon']";	
	
	public String  sMedPolicyAssignmentIcons =  "//app-cpd-opp-deck//mat-panel-title[contains(@class,'mat-expansion-panel-header-title')]//div/label[starts-with(text(),'Medical Policy:')]/..//following-sibling::div//i";
	
	public String  AllMedPolicies  =  "//app-cpd-opp-deck//mat-panel-title[contains(@class,'mat-expansion-panel-header-title')]//div/label[starts-with(text(),'Medical Policy:')]";
	
	public String  AllTopics  =   	"//app-cpd-opp-deck//mat-panel-title[contains(@class,'mat-expansion-panel-header-title')]//div/label[starts-with(text(),'Topic:')]";
	
	public String AssignOppsPopup =   "//div[contains(@class,'jqx-popover right jqx-widget jqx-widget-content jqx-rc-all jqx-popover-arrow-right jqx-selectable')]";
	
	//public String  sPresentationAssignmentCheckbox  = "//div[@class='jqx-popover-content']//div[contains(@id,'listBoxContentjqxWidget')]//span[text()='val']//preceding-sibling::div";
	
	//public String  sPresentationAssignmentCheckbox  = "//div[contains(@class,'jqx-popover right jqx-widget jqx-widget-content jqx-rc-all jqx-popover-arrow-right jqx-selectable')]//div[@class='jqx-popover-content']//span[text()='val']";
	
	//public String  AssignPopupPresNames = "//div[contains(@class,'jqx-popover right jqx-widget jqx-widget-content jqx-rc-all jqx-popover-arrow-right jqx-selectable')]//div[@class='jqx-popover-content']//span[@class='jqx-listitem-state-normal jqx-item checkboxes jqx-rc-all']";

     public String AssignPopupPresNames = "//div[@class='asign-popover']//mat-radio-button[contains(@id, '-radio')]//div[@class='mat-radio-label-content']";
	
      
      public String sPresentationAssignmentOKBtn=   "//div[@class='jqx-popover right jqx-widget jqx-widget-content jqx-rc-all jqx-popover-arrow-right jqx-selectable']//div[contains(text(),'Assign')]//parent::div//following-sibling::div[@class='jqx-popover-content']//button[@role='button' and text()='Okay']";

  	public String sPresentationAssignmentCancelBtn =  "//div[@class='jqx-popover right jqx-widget jqx-widget-content jqx-rc-all jqx-popover-arrow-right jqx-selectable']//div[contains(text(),'Assign')]//parent::div//following-sibling::div[@class='jqx-popover-content']//button[@role='button' and text()='Cancel']" ;

	
	public String  sOppDeckAllDPs =   "//app-cpd-opp-deck//app-cpd-dp-details-container//mat-card-header//mat-checkbox[contains(@class,'dpIdLabel')]//span/label";		
	
	public String  sOppDeckAllDPCards  = "//app-cpd-opp-deck//app-cpd-dp-details-container//mat-card-header//mat-checkbox";
		//	+ "[contains(@class,'dpIdLabel')]";
	
	public String  sOpDeckHeader  =  "//app-cpd-opp-deck//mat-expansion-panel-header//app-cpd-opp-deck-header/parent::span/parent::mat-expansion-panel-header[contains(@class,'mat-expansion-panel-header')]";
	
	public String sOppDeckHeaderTitle = "//app-cpd-opp-deck//mat-expansion-panel-header//app-cpd-opp-deck-header/div/div";

	public String sOppDeckExpansion  =  "//app-cpd-opp-deck//mat-expansion-panel-header//app-cpd-opp-deck-header/parent::span/parent::mat-expansion-panel-header/following-sibling::div[@class='mat-expansion-panel-content ng-trigger ng-trigger-bodyExpansion']";	
	
	
	
	public String sAllDPCheckboxes  =   "//app-cpd-opp-deck//app-cpd-dp-details-container//mat-card-header//mat-checkbox[contains(@class,'dpIdLabel')]//span/label[starts-with(text(),'arg')]/ancestor::mat-checkbox//div[@class='mat-checkbox-inner-container']";
	
	public String  CheckboxState = "/input[@type='checkbox']";
	
	public String sDPCheckboxesProp  =   "//app-cpd-opp-deck//app-cpd-dp-details-container//mat-card-header//mat-checkbox[contains(@class,'dpIdLabel')]//span/label[starts-with(text(),'arg')]/ancestor::mat-checkbox//div[@class='mat-checkbox-inner-container']//input[@type='checkbox']";
	
	public  String sFirstDPCardWorkToDoCountLabel  =  "(//app-cpd-opp-deck//app-cpd-opp-topic//app-cpd-dp-details-container)[1]//mat-card-header/span/label";
	
	public  String sAllDPCardsWorkToDoCountLabel  =    "(//app-cpd-opp-deck//app-cpd-opp-topic//app-cpd-dp-details-container)//mat-card-header/span/label";
	
	public String sToDoCountPopupTitle =    "//div[contains(@class,'jqx-popover-title')]";
	
	public String  sPopupContent  =   "//div[contains(@class,'jqx-popover')]/div[@class='jqx-popover-content']";

	 public String sWorkToDoAllRows =  "//app-cpd-dp-todo-dialog//table/tbody/tr";
	 
	 public String  sWorkToDoPresAssignmentCol = "//app-cpd-dp-todo-dialog//table/tbody/tr[RowNoArg]/td[ColNoArg]";
	 
	 public String  sWorkToDoPresAssgnVals =   "//app-cpd-dp-todo-dialog//table//tbody//tr/td[@class='assignmentEllipse']";
	 
	 public String  sWorkToDoPresAssgnValsRows  =  "//app-cpd-dp-todo-dialog//table//tbody//td[@class='assignmentEllipse']/parent::tr";
	 
	 public String  sWorkToDoPayerVals =   "//app-cpd-dp-todo-dialog//table//tbody//tr/td[@class='ng-star-inserted'][1]";	
	 
	 public String  sWorkToDoPopupCloseBtn  =  "//div[contains(@class,'jqx-popover-title')]/div[contains(@class,'jqx-window-close-button-background jqx-popover-close-button')]";
	 
	 public String  sWorkToDoPopupCloseBtn1  =  "(//div[contains(@class,'jqx-popover-title')]/div[contains(@class,'jqx-window-close-button-background jqx-popover-close-button')] )[2]";
	 
	 public String  sAllLOBs    =  	"//app-cpd-dp-todo-dialog//table//tbody//tr/td[@class='ng-star-inserted'][2]";
	 
	 public String  sAllLOBsRows    =  	"//app-cpd-dp-todo-dialog//table//tbody//td[@class='ng-star-inserted'][2]/parent::tr";
	 			 
	 public String  sDPAssignmentBtns     =  "//app-cpd-opp-deck//app-cpd-opp-topic//app-cpd-dp-details-container//mat-card-header//span[2]";
	 
	 public String  sLOBForPS     =   "//app-cpd-dp-todo-dialog//table//tbody//tr/td[@class='ng-star-inserted'][1][text()='PSArg']/following-sibling::td[@class='ng-star-inserted']";
	 
	 public String  sTopicAssignmentBtns  = "//app-cpd-opp-deck//mat-panel-title[contains(@class,'mat-expansion-panel-header-title')]//div/label[starts-with(text(),'Topic:')]/..//following-sibling::div//i";
	 
	//app-cpd-opp-deck//app-cpd-opp-topic//mat-expansion-panel-header//label[text()='Topic: ICD-10-CM Laterality Policy (1 DP(s))']/parent::div/following-sibling::div[@class='containerTopic']
	 
	 public String  PresProfileChkBox  = "//div[contains(@class,'jqx-popover right jqx-widget jqx-widget-content jqx-rc-all jqx-popover-arrow-right jqx-selectable')]//div[@class='jqx-popover-content']//span[text()='ProfileNameArg']/parent::div/div";

      public String  AssignPopupPresNamesPartialXpath = "//following-sibling::span";
      
      //public String  DeckFilter = "(//app-cpd-aval-opp-tool-bar//div[@class='mat-select-trigger'])[2]";     

      public String  DeckFilter =  "(//mat-form-field[contains(@class,'sort-dropdown')])[3]//div/div";
      
       public String  filterOption =   "//div[@class='cdk-overlay-pane']//mat-option/span[contains(text(),'ValueToSelect')]";
       
       public String  HeaderLevelAssignIcon =  "//*[@class='assignPopover']/i[contains(@class,'assignIcon')]";

	    public String sMessage_No_Oppurtunities = "//div[contains(@id,'contenttable')]//div[@role='gridcell']//span[text()='No opportunities match your selections']";
	      
	      public String sMedicalPolicyCheckbox = "//div[contains(@id,'content')]//div[contains(@id,'contenttable')]//div[@class='aw-checkbox-cell']";
	      
	      public String sExpandAllLnk = "//ngx-spinner[@name='oppDeckSpinner']//following-sibling::app-cpd-aval-opp-tool-bar//a[text()='Expand All']";
	 	 
	 	 public String sCollapseAllLnk ="//ngx-spinner[@name='oppDeckSpinner']//following-sibling::app-cpd-aval-opp-tool-bar//a[text()='Collapse All']";
	 	 
	 	 public String sMedicalPolicyExpColIcon = "//label[starts-with(text(), 'Medical Policy')]//parent::div/parent::mat-panel-title//mat-icon";
	 	 
	 	 public String sTopicExpColIcon = "//label[starts-with(text(), 'Topic')]//parent::div/parent::mat-panel-title//mat-icon";
	 	 
	 	 public String sMedPolicies = "//label[starts-with(text(), 'Medical Policy')]";
	 	 
	 	 public String sTopics = "//label[starts-with(text(), 'Topic')]";
	      
	      public String sAllDPCards = "//mat-card[@class='cardMain mat-card']";
	      
	      public String sFlipCards ="//mat-card[@class='cardMain mat-card']/descendant::ngx-flip";
	      
	      public String sPresentationsView = "//div[@id='assignContainer']/descendant::mat-card[@class='cardMain mat-card']/descendant::ngx-flip/div[@class = 'flipper rotate']";
	      
	      public String sSavingsView = "//div[@id='assignContainer']/descendant::mat-card[@class='cardMain mat-card']/descendant::ngx-flip/div[@class = 'flipper']";      
     
	      
	      public String sPresNameOnDPCard = "/../../../../..//li[text()='sval']";
	       
	       public String sSavingsValueOnDP = "/../../../../following-sibling::mat-card-content//mat-grid-tile[@class='mat-grid-tile dpSavings']//div[2]";
	       
	       public String sWorkToDoOfDP ="/../../../following-sibling::span/label";
	       
	       public String sRawSavingsOnDPCard = "//mat-card-header//label[text()='DP "+Serenity.sessionVariableCalled("DPkey")+"']/../../../../following-sibling::mat-card-content//div[@class='front']//div[@class='savings-val']";

	public String  PresProfileChkBoxNotChecked  =  "//div[contains(@class,'jqx-popover right jqx-widget jqx-widget-content jqx-rc-all jqx-popover-arrow-right jqx-selectable')]//div[@class='jqx-popover-content']//span[text()='ProfileNameArg']/parent::div/div[@checked='false']";

	public String  PresProfileChkBoxPartiallySelected  =  "//div[@class='jqx-popover-content']//span[text()='ProfileNameArg']/parent::div/div[@checked='null']";

	public String   DPCardFlip   =  "//app-cpd-opp-deck//app-cpd-opp-topic//app-cpd-dp-details-container//mat-card-header//mat-checkbox[contains(@class,'dpIdLabel')]//span/label[text()='DPKeyArg']/ancestor::mat-card-header/following-sibling::mat-card-content/ngx-flip/div";

	public String   AssignedPresentationsforDPCard   =  "//app-cpd-opp-deck//app-cpd-opp-topic//app-cpd-dp-details-container//mat-card//mat-checkbox[contains(@class,'dpIdLabel')]//span/label[text()='DPKeyArg']/ancestor::mat-card-header/following-sibling::mat-card-content//div[@class='presList']//li[@class='ng-star-inserted']";

	public String   DPCardAssignIcon   =    "//app-cpd-opp-deck//app-cpd-opp-topic//app-cpd-dp-details-container//mat-card-header//mat-checkbox[contains(@class,'dpIdLabel')]//label[text()='DPKeyArg']/ancestor::mat-card-header//span[2]";

	//public String   AllDPshavingSummaryView = "//app-cpd-opp-deck//mat-card//ngx-flip[@ng-reflect-flip='false']";

	public  String TopicAssignIconforMedPolicy =  "//app-cpd-opp-deck//mat-panel-title[@class='mat-expansion-panel-header-title']//div/label[starts-with(text(),'MedPolicyArg')]/parent::div/parent::mat-panel-title/parent::span/parent::mat-expansion-panel-header/following-sibling::div//app-cpd-opp-topic//mat-expansion-panel-header//div[@class='containerTopic']";

	public  String DPAssignIconforTopic =  "//app-cpd-opp-deck//app-cpd-opp-topic//mat-expansion-panel-header//label[text()='TopicNameArg']/parent::div/ancestor::app-cpd-opp-topic//app-cpd-dp-details-container//mat-card-header//span[2]";

	public  String TopicAssignIconforTopic = "//app-cpd-opp-deck//app-cpd-opp-topic//mat-expansion-panel-header//label[text()='TopicNameArg']/parent::div/following-sibling::div[@class='containerTopic']";

	public  String AssignIconforMedPolicy =  "//app-cpd-opp-deck//mat-panel-title[@class='mat-expansion-panel-header-title']//div/label[starts-with(text(),'MedPolicyArg')]/following::div[@class='container3']";

	public String  AssignIconforMedPolicyBasedOnTopic =  "//app-cpd-opp-deck//app-cpd-opp-topic//mat-expansion-panel-header//label[text()='TopicNameArg']//ancestor::app-cpd-opp-policy//div[@class='container3']";

	public String   AllDPshavingSummaryView = "//app-cpd-opp-deck//mat-card//ngx-flip//div[@class='flipper']";

	public String   AvaialbleLOBForDPPartialXpath =    "//div[@class='available filtered']";

	public String  DPKeyForAvailableLOBsPartialXpath  =  "//ancestor::app-cpd-dp-details-container//span[1]/label";

	public String    DPKeyXpath =  "//mat-card-content/ancestor::mat-card//label[contains(text(),'DP')]";
	//public String    DPKeyXpath =  "//mat-card-content//div[@class='flipper']/ancestor::mat-card//label[contains(text(),'DP')]";

	public String   AssignPopupVisibleCheckboxes = "//div[contains(@class,'jqx-popover right jqx-widget jqx-widget-content jqx-rc-all jqx-popover-arrow-right jqx-selectable')]//div[@class='jqx-popover-content']//div[@class='jqx-checkbox chkbox']";	

  public String  DPCardAgeingCaption   =    "(//app-cpd-dp-details-container//mat-card-content)[1]//div[@class='pendingDaysSection']//b";

  public String  SortbyDropdown   =   "//label[contains(text(),'Sort by:')]/..//mat-select";
  
  public String   DropdownValues = "//mat-option//span[@class='mat-option-text']";

 public String  SortbyDropdownArrow   =   "//label[contains(text(),'Sort by')]/..//mat-select";
 
 public String  SortbyDropdownValues   =  "//mat-option//span[@class='mat-option-text' and contains(text(), 'DropDownVal')]";
 
public String    DPKeyDynamicXpath =   "//div[@class='available filtered']//ancestor::app-cpd-dp-details-container//span[1]/label[contains(text(),'DPKeyArg')]";

 public String DPPayer = "//span[text()='DP DPKeyArg']//ancestor::div[@class='dp_view_main']//table[@role='grid']//span[text()='Payer ']";

 public String DPClaimType = "//span[text()='DP DPKeyArg']//ancestor::div[@class='dp_view_main']//table[@role='grid']//span[text()=' [ClaimType]']";
 
 public String DPLOB = "//span[text()='DP DPKeyArg']//ancestor::div[@class='dp_view_main']//table[@role='grid']//label[text()='Insurance ']";
 
 public String DP_ALL_chk = "//span[text()='DP DPKeyArg']//ancestor::div//table[@role='grid']//label[text()='ALL ']//span";
 
 public String DP_AssignIcon = "//span[text()='DP DPKeyArg']//parent::li/following-sibling::li/span/button";
 
 public String DP_LOBStatus ="(//div[text()='LobValue']//parent::div)[1]";
 
 //public String DP_LOBRows = "//span[text()='DP DPKeyArg']//ancestor::div[@class='dp_view_main']//table[@role='grid']//label[contains(text(),'Insurance')]//ancestor::div//tbody//tr";
 public String DP_LOBRows="(//span[text()='DP DPKeyArg']/ancestor::section/following-sibling::table//td[contains(@class,'Insurance') and not (contains(@class,'greyOut'))]//span[@class='cb_grid_font'])";
 
 public String DP_Presentationlabel = "//label[text()='DP DPKeyArg']/../../../../..//div[@class='back']//li[text()='sval']";
 
 public String Toolbar_icons = "(//ul[@class='toolkit'])[1]//*[@mattooltip='sval']";
 
 public String Chk_AllPPS = "//label[contains(text(), 'ALL')]//span";
 
 public String AssignIcon = "//ul[@class='dp_view_lt_block']//button";
 
 public String Btn_Cancel_AssignPopup="//div[@class='asign-popover_button']//button[text()='Cancel']";
 
 public String DPLabel = "//label[text()='DP sval']";
 
 public String DP_LOB_Bold = "//div[@class='lob_tiles']//div[@class='available filtered']//div[text()=' Insurance ']";
 
 public String DP_LOB_All = "//div[@class='lob_tiles']//div[@class='lob_tiles_ft'][2]";
 
 public String DP_LOB_Reject_Decision = "//div[@class='lob_tiles']//div[text() = ' sval ']//following-sibling::div/i";
 
 public String DP_LOB_Approved_Decision = "//div[@class='lob_tiles']//div[text() = ' sval ']//preceding-sibling::div/i";
 
 public String DP_LOB_Grey = "//div[@class='lob_tiles']//div[@class='available filtered']//div[text()=' Insurance ']";
 
 
 //### New XPaths
 
 public String   RulerelatioIconxpath =  "//mat-card-header//label[text()='DPKey']//ancestor::app-cpd-dp-details-container//mat-icon[contains(@class,'ruleRelationIcon')]";
 public String DPKeyHeaderXpath = "//mat-card-header//label[text()='DPKey']"; 
 public String MedPolicy  = "(//mat-card-header//label[text()='DPKey']//ancestor::app-cpd-med-policy//mat-expansion-panel-header)[1]//div[contains(@class,'container3')]/span/i";               
 public String Topic  =  "//mat-card-header//label[text()='DPKey']//ancestor::app-cpd-topic//div[contains(@class,'containerTopic')]/span/i";
 public String DPAssignArrow  =  "//span[.='DPKeyArg']/following::button[contains(@class,'arrow')]";
 public String DPLevelAllPayerLOBCheckbox  = "//span[text()='DPKeyArg']//ancestor::div//table[@role='grid']//label[text()='ALL ']//span";
 public String  FirstPayershort = "((//tr[@role='row'][contains(@class,'mat-row')])[1]/td[1]//span)[3]";
 public String DPGridCloseIcon = "//span[text()='DPKeyArg']//ancestor::div//table[@role='grid']//ancestor::mat-card-content//i[@class='material-icons close-icon']";
 public String InfoMessageXpath =  "//simple-snack-bar//span[@class='mat-button-wrapper']";
 
 
 public String FirstDPInMPInPres="(//label[contains(text(),'MP')]/ancestor::app-cpd-pres-policy//label[contains(text(),'DP') and not (contains(text(),'DP(s)'))])[1]";
 public String FirstDPInMP="(//label[contains(text(),'MP')]/ancestor::app-cpd-opp-policy//label[contains(text(),'DP') and not (contains(text(),'DP(s)'))])[1]";
 public String FirstDPInTopic="(//label[contains(text(),'value')]/ancestor::app-cpd-opp-topic//label[contains(text(),'DP') and not (contains(text(),'DP(s)'))])[1]";
  
 public String NPPDrodown="//span[contains(@class,'select-value')]/span[contains(text(),'NPP Opportunities')]";
  
 
	//=================================================================== PAGE METHODS ==============================================================================//

	public static String getDynamicXpath(String sXpath,Object sVal){

	  		String sFormattedXpath = null;

	  		switch (sXpath.toUpperCase()){

	  		case "PRESENTATION IN DP CARDS":
	  			sFormattedXpath  = "//mat-card[@class='cardMain mat-card']/descendant::li[text()= '"+sVal+"']";
	  			break;	
	  		
	  		}	
	  		return sFormattedXpath;
	  	}
	
 	public boolean clickOpportunityDeck()
	{	
		
		try{				
				  if(refSeleniumUtils.is_WebElement_Visible(sOpDeckHeader))
				  {
					  refSeleniumUtils.highlightElement(sOpDeckHeader);
					  refSeleniumUtils.Click_given_Locator(sOpDeckHeader);
					  refSeleniumUtils.defaultWait(ProjectVariables.TImeout_10_Seconds);				
				  }
		    }	
			catch (Exception e) 
			{			
				System.out.println("Exception Message::"+e.getMessage());
				getDriver().quit();
			}

		
		return true;
	}
		
	public  void clickWorkTodoCountLink(String sDPKey)
	{
		
		refSeleniumUtils.defaultWait(ProjectVariables.TImeout_3_Seconds);		
		if(sDPKey.isEmpty())
		{	
				try{				
					  if(refSeleniumUtils.is_WebElement_Visible(sFirstDPCardWorkToDoCountLabel))
					  {
						  refSeleniumUtils.highlightElement(sFirstDPCardWorkToDoCountLabel);
						  refSeleniumUtils.Click_given_Locator(sFirstDPCardWorkToDoCountLabel);
						  refSeleniumUtils.defaultWait(ProjectVariables.TImeout_1_Seconds);				
					  }
				    }		
					catch (Exception e) {			
					System.out.println("Exception Message::"+e.getMessage());
					getDriver().quit();
					}
		   }			
			
					
		else   //if a DPKey has been passed to the method,click on the particular DPKey Worktodo count
		{

			String sXWorkToDOXPath = "//label[text() = '"+sDPKey+"']/ancestor::mat-checkbox/following-sibling::span/label";
			oGenericUtils.clickOnElement(sXWorkToDOXPath);
		}

	}
		
	public boolean AssignPopupBtnclick(String sBtnName)	
	{
		boolean bClicked = false;
		
		try{			
	
			  if(sBtnName.equalsIgnoreCase("OK"))
			  {
				  refSeleniumUtils.highlightElement(sPresentationAssignmentOKBtn);			  
				  refSeleniumUtils.clickGivenXpath(sPresentationAssignmentOKBtn);
				  bClicked = true;
			  }
				  else if(sBtnName.equalsIgnoreCase("Cancel"))
				{
					  refSeleniumUtils.highlightElement(sPresentationAssignmentCancelBtn);
					  refSeleniumUtils.clickGivenXpath(sPresentationAssignmentCancelBtn);							
					  bClicked = true;
				}
				  
		}catch (Exception e) {	
			
			System.out.println("Error::"+e.getMessage());
		}   
		
		return  bClicked;			
	}
	
	
	public void verifyOppurtunitiesAndValidateNoOppurtunityMessageDisappeared(String sMessage)
	{
		refSeleniumUtils.defaultWait(ProjectVariables.TImeout_10_Seconds);
		if(!refSeleniumUtils.isElemPresent(sMessage_No_Oppurtunities))
		{
			Assert.assertTrue(sMessage+" disappeared is verified successfully in Oppurtunity grid", true);
		}else{
			Assert.assertTrue(sMessage+" still exists in Oppurtunity grid after changing payershort", false);
			
		}
		int count =  findAll(sMedicalPolicyCheckbox).size();
		
		
		if(count>=1)
		{
			Assert.assertTrue("Existence of Medical policies is verified successfully in Oppurtunity grid", true);
		}else{
			Assert.assertTrue("Medical Policies are not displayed in Oppurtunity grid after changing payershort", false);
		}
		
	}
	
	public void validateOppDeck_PresContainerState(String sContainer,String sExpectedState) throws ElementNotFoundException
	{
	
	  boolean bFlag = false;	 
		switch( sContainer)
		{
		case "OpportunityDeck":					    
								
				try{
							WebElement wl = getDriver().findElement(By.xpath(this.sOpDeckHeader));								
						
								String sActualOppDeckExpandProperty   = wl.getAttribute("aria-expanded");				
					     
					           if(sExpectedState.equalsIgnoreCase("Expanded"))
					        	 {		        	   
					        	   			if(sActualOppDeckExpandProperty.equalsIgnoreCase("true"))
					        	   			{
					        	   				Assert.assertTrue("The container ::"+sContainer +" is in expected state :"+sExpectedState,true);
					        	   			}		
					        	   			else
					        	   			{
					        	   				Assert.assertTrue("The container ::"+sContainer +" is not in expected state :"+sExpectedState,false);
					        	   			}
					        	  }		           
		
					           if(sExpectedState.equalsIgnoreCase("Collapsed"))
					        	 {		        	   
					        	   			if(sActualOppDeckExpandProperty.equalsIgnoreCase("false"))
					        	   			{
					        	   				Assert.assertTrue("The container ::"+sContainer +" is in expected state :"+sExpectedState,true);
					        	   			}	
					        	   			else
					        	   			{
					        	   				Assert.assertTrue("The container ::"+sContainer +" is not in expected state :"+sExpectedState,false);
					        	   			}
					        	  }		
			         }

				catch (Exception e) {			
						System.out.println("Exception Message::"+e.getMessage());
						getDriver().quit();
			}
				
			break;			
			
		case "PresentationProfile":
				try{	
					WebElement wl2 = getDriver().findElement(By.xpath(refPresentationDeck.sPresDeckHeader));
					String sActualPresDeckExpandProperty   = wl2.getAttribute("aria-expanded");
		
			           if(sExpectedState.equalsIgnoreCase("Expanded"))
			        	 {		        	   
			        	   			if(sActualPresDeckExpandProperty.equalsIgnoreCase("true"))
			        	   			{
			        	   				Assert.assertTrue("The container ::"+sContainer +" is in expected state :"+sExpectedState,true);
			        	   			}	
			        	   			else
			        	   			{
			        	   				Assert.assertTrue("The container ::"+sContainer +" is not in expected state :"+sExpectedState,false);
			        	   			}
			        	  }		           
		
			           if(sExpectedState.equalsIgnoreCase("Collapsed"))
			        	 {		        	   
			        	   			if(sActualPresDeckExpandProperty.equalsIgnoreCase("false"))
			        	   			{
			        	   				bFlag = true; 
			        	   			}		      	   
			        	  }		
				}
				catch (Exception e)
				{			
					System.out.println("Exception Message::"+e.getMessage());
					getDriver().quit();
				}			
			
			break;
			
		case "MedicalPolicy" :
			
		             List<WebElement>   MedPolicyExpanderElmnts = refSeleniumUtils.getElementsList("XPATH",refPresentationDeck.sMedPolicyExpandPanel);
		             
		              if( MedPolicyExpanderElmnts.size() ==0 )
		              {
		            	  Assert.assertTrue("No Medical Policies displayed",false);
		              }
		              
		                for(int i=0;i<MedPolicyExpanderElmnts.size();i++)
		                {
		                	if(MedPolicyExpanderElmnts.get(i).getAttribute("aria-expanded").equalsIgnoreCase("true"))
		                			{
		                		refSeleniumUtils.highlightElement(MedPolicyExpanderElmnts.get(i));
		                					bFlag = true; 
		                			}
		                 }			     
			
			break;
			
			
			case "Topics" :
				
			             List<WebElement>   TopicExpanderElmnts = refSeleniumUtils.getElementsList("XPATH",refPresentationDeck.sTopicExpandPanel);
			             
			              if( TopicExpanderElmnts.size() ==0 )
			              {
			            	  Assert.assertTrue("No Medical Policies displayed",false);
			              }
			              
			                for(int i=0;i<TopicExpanderElmnts.size();i++)
			                {
			                	if(TopicExpanderElmnts.get(i).getAttribute("aria-expanded").equalsIgnoreCase("true"))
			                			{
			                		refSeleniumUtils.highlightElement(TopicExpanderElmnts.get(i));
			                					bFlag = true; 
			                			}
			                 }			     
				
				break;
		}	
		
		
	}
		
	public boolean validateDeckCheckboxes(String sDeckName) throws ElementNotFoundException
	{		
		
	       boolean bFlag = false;
	       int iLoopCounter=0;
	       
	       switch(sDeckName)
	       {	       
	          case "AvailableOpportunities":
						  //Store all available  DPs names in a List 
						  List<String> DPs = refSeleniumUtils.getWebElementValuesAsList(this.sOppDeckAllDPCards);		
							List<WebElement>  ListWebElement  = new  ArrayList<WebElement>(); 
							 List<String>  ElementsValues =   new  ArrayList();	
							 
							ListWebElement =  refSeleniumUtils.getElementsList("XPATH",this.sOppDeckAllDPCards) ;						      
					         for(int j=0;j<ListWebElement.size();j++)
					         {
					        	  String  sVal = ListWebElement.get(j).getText().trim();
					        	  ElementsValues.add(sVal);		        	 
					         }
						  
						   List<WebElement> DPElements = refSeleniumUtils.getElementsList("XPATH",this.sOppDeckAllDPCards); 
						   
						   //Limit the Checbox count for selection to 3 if there are more than 3 checkboxes in the UI		              
					          if( DPs.size()>3)
					          {
					        	  iLoopCounter = 3;
					          } else {   iLoopCounter = DPs.size(); }
					
						   
						  
						  //For all the DPs check if the  Check boxes available		  
						  for(int k=0;k<iLoopCounter;k++)
						  {		
							    bFlag = false;
								//evaluateJavascript("arguments[0].scrollIntoView(true);", DPElements.get(k));			
								String  sChkboxXPath = StringUtils.replace(this.sAllDPCheckboxes,"arg",DPs.get(k));			  						
								
								if(refSeleniumUtils.is_WebElement_Displayed(sChkboxXPath))
								{	
									refSeleniumUtils.highlightElement(sChkboxXPath);			       
							     	bFlag = true;
							    }
								else
								{					
									GenericUtils.logErrorMesage("The Checkbox is not displayed for the DP Card in the oppDeck ::"+DPs.get(k));
									break;				
								}
						  }	
						  
		  break;
	      case "PresentationDeck":
	    	  
	    	  //Store all available  DPs names in a List 
			  DPs = refSeleniumUtils.getWebElementValuesAsList(refPresentationDeck.sAllDPs); 
			  DPElements = refSeleniumUtils.getElementsList("XPATH",refPresentationDeck.sAllDPs); 
			  
			   
			   //Limit the Checbox count for selection to 3 if there are more than 3 checkboxes in the UI		              
		          if( DPs.size()>3)
		          {
		        	  iLoopCounter = 3;
		          } else {   iLoopCounter = DPs.size(); }
		
			  
			  //For all the DPs check if the  Check boxes available		  
			  for(int k=0;k<iLoopCounter;k++)
			  {		
				    bFlag = false;
				//	evaluateJavascript("arguments[0].scrollIntoView(true);", DPElements.get(k));			
					String  sChkboxXPath = StringUtils.replace(refPresentationDeck.sAllDPCheckboxes,"arg",DPs.get(k));			  						
					
					if(refSeleniumUtils.is_WebElement_Displayed(sChkboxXPath))
					{	
						refSeleniumUtils.highlightElement(sChkboxXPath);			       
				     	bFlag = true;
				    }
					else
					{	
						bFlag = false;
						GenericUtils.logErrorMesage("The Checkbox is not displayed for the DP Card in the Presentation Deck ::"+DPs.get(k));
						break;				
					}
			  }		    	  
	    	  
	       break;	  
	        	  
    }//End case	  
		  
		return bFlag;
		
	}	

	public boolean validateDeckCheckboxesState(String sChkboxTobeState, String sDeckName) throws ElementNotFoundException 
	{		
		  String  sChkboxXPath = "";	
		  List<WebElement>   WlChkboxes = new ArrayList();
		  List<WebElement>   DPChkBoxes = new ArrayList();
		  List<String>  DPs = new ArrayList();
		  boolean bFlag = false;
		  String sAcutalChkboxProp = "";
		  int iLoopCounter = 0;
		  String  sChkboxXPathPro = "";
		  
		 //For each check box element check the check boxes is selected /de selected
	
		switch(sDeckName)
		{
		    case "AvailableOpportunities":
		    			sChkboxXPath = StringUtils.replace(this.sAllDPCheckboxes,"arg","DP");	
		    			WlChkboxes  = refSeleniumUtils.getElementsList("XPATH",sChkboxXPath);
		    			DPs = refSeleniumUtils.getWebElementValuesAsList(this.sOppDeckAllDPs); 
		    			sChkboxXPathPro = StringUtils.replace(this.sDPCheckboxesProp,"arg","DP");	
		    			DPChkBoxes  = refSeleniumUtils.getElementsList("XPATH",sChkboxXPathPro);
		    			
		    break;
		    
		    case "PresentationDeck":
		    		  sChkboxXPath = StringUtils.replace(refPresentationDeck.sAllDPCheckboxes,"arg","DP");	
		    		  WlChkboxes  = refSeleniumUtils.getElementsList("XPATH",sChkboxXPath);
		    		  DPs = refSeleniumUtils.getWebElementValuesAsList(refPresentationDeck.sAllDPs); 
		    		  sChkboxXPathPro = StringUtils.replace(refPresentationDeck.sDPCheckboxesProp,"arg","DP");	
		    		  DPChkBoxes  = refSeleniumUtils.getElementsList("XPATH",sChkboxXPathPro);
			break;
			 
			default:    Assert.assertTrue("No option provided in the switch case",false);		
		
		}			
		         //Limit the Checbox count for selection to 3 if there are more than 3 checkboxes in the UI		              
		          if( WlChkboxes.size()>3)
		          {
		        	  iLoopCounter = 3;
		          } else {   iLoopCounter = WlChkboxes.size(); }
		
		          
				  for(int m =0;m<iLoopCounter;m++)
				  {		
					 	 bFlag = false; 					    
							   
						           if(sChkboxTobeState.equalsIgnoreCase("Select"))
						        	 {	
						        	        //Select Check box
						        		  WlChkboxes  = refSeleniumUtils.getElementsList("XPATH",sChkboxXPath);
						        		  refSeleniumUtils.clickGivenWebElement(WlChkboxes.get(m));	
						        	    	DPChkBoxes  = refSeleniumUtils.getElementsList("XPATH",sChkboxXPathPro);
						        	    	sAcutalChkboxProp   = DPChkBoxes.get(m).getAttribute("aria-checked");	
						        	   			if(sAcutalChkboxProp.equalsIgnoreCase("true"))
						        	   			{
						        	   				bFlag = true; 
						        	   			}	
						        	   			else
						        	   			{
						        	   				bFlag = false;
						        	   				GenericUtils.logErrorMesage("The Checkbox  not selected for the DP Card after clicking on Checkbox in the "+sDeckName+"::"+DPs.get(m));
						        	   				break;
						        	   			}	
						        	  }		           
					
						           if(sChkboxTobeState.equalsIgnoreCase("DeSelect"))
						        	 {		
						        	   //DeSselect Check box						        	
						        		WlChkboxes  = refSeleniumUtils.getElementsList("XPATH",sChkboxXPath);
						        		refSeleniumUtils.clickGivenWebElement(WlChkboxes.get(m));				        	 
						        	   DPChkBoxes  = refSeleniumUtils.getElementsList("XPATH",sChkboxXPathPro);
						        	   sAcutalChkboxProp   = DPChkBoxes.get(m).getAttribute("aria-checked");	
						        	   
						        	   			if(sAcutalChkboxProp.equalsIgnoreCase("false"))
						        	   			{
						        	   				bFlag = true; 
						        	   			}	
						        	   			else
						        	   			{
						        	   				bFlag = false;
						        	   				GenericUtils.logErrorMesage("The Checkbox  not Deselected for the DP Card  after checkbox deselection in the "+sDeckName+"::"+DPs.get(m));
						        	   				break;
						        	   			}	
						        	  }		
					}		
		 
		 return bFlag;
	}
	
	public boolean applyAvailableDPsFilter(String sFilterValue) throws InterruptedException
	{
			boolean valueSelected = false;
		
			Thread.sleep(5000);
			//Click on the Filter Drop down
			refSeleniumUtils.highlightElement(DeckFilter);
			refSeleniumUtils.clickGivenXpath(DeckFilter);
			
		
	try{	
				switch(sFilterValue)
				{
				case "All":	
					refSeleniumUtils.clickGivenXpath(StringUtils.replace(filterOption, "ValueToSelect", "All"));
					refSeleniumUtils.defaultWait(ProjectVariables.TImeout_3_Seconds);					
							valueSelected = true;
					break;
					
				case "NotStartedOnly":
					refSeleniumUtils.clickGivenXpath(StringUtils.replace(filterOption, "ValueToSelect", "Not Started Only"));
					refSeleniumUtils.defaultWait(ProjectVariables.TImeout_3_Seconds);	
					
						valueSelected = true;
					break;
					
				case "PartiallyAssignedOnly":
					refSeleniumUtils.clickGivenXpath(StringUtils.replace(filterOption, "ValueToSelect", "Partially Assigned Only"));
					refSeleniumUtils.defaultWait(ProjectVariables.TImeout_3_Seconds);	
					
						valueSelected = true;
					break;
				
				case "CompletedOnly" :
					refSeleniumUtils.clickGivenXpath(StringUtils.replace(filterOption, "ValueToSelect", "Completed Only"));	
					refSeleniumUtils.defaultWait(ProjectVariables.TImeout_3_Seconds);					
						valueSelected = true;
					break;
					
					 default:  Assert.assertTrue("No option provided from Gherkin for Switch",false);	
				
				}
	  }	catch (Exception e){ }	
	
		
		return valueSelected;
	}
		
	public void flipAllDPCards(String sDPView) {
		
		String sXPath = null;
		switch  (sDPView.toUpperCase()){
		
		case "PRESENTATION VIEW":
			sXPath = sSavingsView;
			break;
		case "SAVINGS VIEW":
			sXPath = sPresentationsView;
			break;
		default :
			Assert.assertTrue("In correct Entry provided for flipping cards", false);
		}
		
		List<WebElement> oFlipCards = getDriver().findElements(By.xpath(sXPath));
		
		for (int i = 0 ;i < oFlipCards.size() ; i++){
		
			oFlipCards.get(i).click();
		}
	
	}
	
	public void verifyExpandAndCollapseAllFunctionality(String arg1) throws ElementNotFoundException{
		
		
		switch(arg1.toUpperCase()){
		case "EXPAND ALL" :
			refSeleniumUtils.Click_given_Locator(sExpandAllLnk);
			break;
		case "COLLAPSE ALL" :
			refSeleniumUtils.Click_given_Locator(sCollapseAllLnk);
			break;
		}
			
		int countOfMP = refSeleniumUtils.getElementsList("XPATH", sMedicalPolicyExpColIcon).size();
		System.out.println("Medical Policies count is "+countOfMP);
		
		List<String> MedPolicyList= refSeleniumUtils.getWebElementValuesAsList(sMedPolicies);
		for(int i=0;i<countOfMP;i++)
		{
			String MP = MedPolicyList.get(i);
			String dynamicMP = StringUtils.replace(sMedicalPolicyExpColIcon,"Medical Policy",MP);
			refSeleniumUtils.highlightElement(dynamicMP);
			String MPtext = refSeleniumUtils.get_TextFrom_Locator(dynamicMP);
			switch(arg1.toUpperCase()){
			case "EXPAND ALL" :
				if(MPtext.equalsIgnoreCase("arrow_drop_down"))
				{
					Assert.assertTrue("Medical policy "+MP+" expanded verification is successful", true);
					if(i==countOfMP){
						Assert.assertTrue("All medical policies expanded verification is successful", true);
					}
				}else{
					Assert.assertTrue("Medical policy "+MP+" is not expanded", false);
				}
				break;
			case "COLLAPSE ALL" :
				if(MPtext.equalsIgnoreCase("arrow_right"))
				{
					Assert.assertTrue("Medical policy "+MP+" colapsed verification is successful", true);
					if(i==countOfMP){
						Assert.assertTrue("All medical policies collapsed verification is successful", true);
					}
				}else{
					Assert.assertTrue("Medical policy "+MP+" is not collapsed", false);
				}
				break;
			}
				
			
		}
		
		int countOfTopics = refSeleniumUtils.getElementsList("XPATH", sTopics).size();
		System.out.println("Topics count is "+countOfTopics);
				switch(arg1.toUpperCase()){
				case "EXPAND ALL" :				
					List<String> TopicsList= refSeleniumUtils.getWebElementValuesAsList(sTopics);
					Serenity.setSessionVariable("ListOfTopics").to(TopicsList);
					for(int j=0;j<countOfTopics;j++)
					{
						String Topic = TopicsList.get(j);
						//if(Topic.contains("Topic: Major Surgery: 90-Day Procedures")||Topic.contains("Topic: Major Surgery: 0-Day Procedures"))
						if(Topic.contains("Major Surgery:")||Topic.contains("Minor Surgery:"))
						{
						String[] t = StringUtils.split(Topic, ":");
						Topic = t[0]+":"+t[1]+": "+t[2];
						}						
						String[] topicText = StringUtils.split(Topic, "(");
						String dynamicTopic = StringUtils.replace(sTopicExpColIcon,"Topic",topicText[0]);
						String TPtext = refSeleniumUtils.get_TextFrom_Locator(dynamicTopic);
						if(TPtext.equalsIgnoreCase("arrow_drop_down"))
						{
							Assert.assertTrue("Topic "+Topic+" expanded verification is successful", true);
							if(j==countOfTopics){
								Assert.assertTrue("All topics expanded verification is successful", true);
							}
						}else{
							Assert.assertTrue("Topic "+Topic+" is not expanded", false);
						}
					}
					break;
				case "COLLAPSE ALL" :
					////label[starts-with(text(), 'Topic')]//parent::div/parent::mat-panel-title/parent::span/parent::mat-expansion-panel-header
					for(int j=0;j<countOfTopics;j++)
					{
						List<String> TopicList = Serenity.sessionVariableCalled("ListOfTopics");
						String Topic = TopicList.get(j);
						if(Topic.contains("Topic: Major Surgery: 90-Day Procedures"))
						{
						String[] t = StringUtils.split(Topic, ":");
						Topic = t[0]+":"+t[1]+": "+t[2];
						}						
						String[] topicText = StringUtils.split(Topic, "(");
						String dynamicTopic = StringUtils.replace(sTopicExpColIcon,"Topic",topicText[0]);				
						Assert.assertFalse("Topic "+Topic+" collapsed verification is successful", refSeleniumUtils.is_WebElement_Displayed(dynamicTopic.concat("[text()='arrow_right')]")));					
				}
					break;
			}
		
	}
				 
	public void verifyExpandAndCollapseAtMedicalPolicyLevel(String arg1) throws ElementNotFoundException{
		
		
			int countOfMP = refSeleniumUtils.getElementsList("XPATH", sMedicalPolicyExpColIcon).size();
			System.out.println("Medical Policies count is "+countOfMP);
			
			List<String> MedPolicyList= refSeleniumUtils.getWebElementValuesAsList(sMedPolicies);
			switch(arg1.toUpperCase()){
			case "EXPAND" :
			for(int i=0;i<countOfMP;i++)
				{
					String MP = MedPolicyList.get(i);
					String dynamicMP = StringUtils.replace(sMedicalPolicyExpColIcon,"Medical Policy",MP);
					refSeleniumUtils.highlightElement(dynamicMP);
					refSeleniumUtils.Click_given_Locator(dynamicMP);
					Assert.assertTrue("Medical Policy "+dynamicMP+"expanded verification is successful", refSeleniumUtils.get_TextFrom_Locator(dynamicMP).equalsIgnoreCase("arrow_drop_down"));
					for(i=i+1;i<countOfMP;i++){
						String collapsedMP = MedPolicyList.get(i);
						String collapsedMPPath = StringUtils.replace(sMedicalPolicyExpColIcon,"Medical Policy",collapsedMP);
						refSeleniumUtils.highlightElement(collapsedMPPath);
						Assert.assertTrue("Medical Policy "+dynamicMP+"collapsed verification is successful", refSeleniumUtils.get_TextFrom_Locator(collapsedMPPath).equalsIgnoreCase("arrow_right"));
					}						
				}
			break;
			case "COLLAPSE" :
				for(int i=0;i<countOfMP;i++)
					{
						String MP = MedPolicyList.get(i);
						String dynamicMP = StringUtils.replace(sMedicalPolicyExpColIcon,"Medical Policy",MP);
						refSeleniumUtils.highlightElement(dynamicMP);
						refSeleniumUtils.Click_given_Locator(dynamicMP);
						Assert.assertTrue("Medical Policy "+dynamicMP+"collapsed verification is successful", refSeleniumUtils.get_TextFrom_Locator(dynamicMP).equalsIgnoreCase("arrow_right"));
						for(i=i+1;i<countOfMP;i++){
							String collapsedMP = MedPolicyList.get(i);
							String collapsedMPPath = StringUtils.replace(sMedicalPolicyExpColIcon,"Medical Policy",collapsedMP);
							refSeleniumUtils.highlightElement(collapsedMPPath);
							Assert.assertTrue("Medical Policy "+dynamicMP+"expanded verification is successful", refSeleniumUtils.get_TextFrom_Locator(collapsedMPPath).equalsIgnoreCase("arrow_drop_down"));
						}						
					}
				break;
		}
		
	}
	

	public void verifyExpandAndCollapseAtTopicLevel(String arg1) throws ElementNotFoundException{


		int countOfMP = refSeleniumUtils.getElementsList("XPATH", sMedicalPolicyExpColIcon).size();
		System.out.println("Medical Policies count is "+countOfMP);

		int countOfTopics = refSeleniumUtils.getElementsList("XPATH", sTopics).size();
		System.out.println("Topics count is "+countOfTopics);

		List<String> MedPolicyList= refSeleniumUtils.getWebElementValuesAsList(sMedPolicies);
		switch(arg1.toUpperCase()){
		case "EXPAND" :
			for(int i=0;i<countOfMP;i++)
			{
				String MP = MedPolicyList.get(i);
				String dynamicMP = StringUtils.replace(sMedicalPolicyExpColIcon,"Medical Policy",MP);
				refSeleniumUtils.highlightElement(dynamicMP);
				refSeleniumUtils.Click_given_Locator(dynamicMP);
				Assert.assertTrue("Medical Policy "+dynamicMP+"expanded verification is successful", refSeleniumUtils.get_TextFrom_Locator(dynamicMP).equalsIgnoreCase("arrow_drop_down"));
			}

			List<String> TopicsList= refSeleniumUtils.getWebElementValuesAsList(sTopics);
			Serenity.setSessionVariable("ListOfTopics").to(TopicsList);
			for(int j=0;j<countOfTopics;j++)
			{
				String Topic = TopicsList.get(j);
				if(Topic.contains("Topic: Major Surgery: 90-Day Procedures"))
				{
					String[] t = StringUtils.split(Topic, ":");
					Topic = t[0]+":"+t[1]+": "+t[2];
				}						
				String[] topicText = StringUtils.split(Topic, "(");
				String dynamicTopic = StringUtils.replace(sTopicExpColIcon,"Topic",topicText[0]);
				refSeleniumUtils.highlightElement(dynamicTopic);
				refSeleniumUtils.Click_given_Locator(dynamicTopic);
				Assert.assertTrue("Topic "+dynamicTopic+"expanded verification is successful", refSeleniumUtils.get_TextFrom_Locator(dynamicTopic).equalsIgnoreCase("arrow_drop_down"));
				for(j=j+1;j<countOfTopics;j++){
					String collapsedTopic = TopicsList.get(j);
					if(collapsedTopic.contains("Topic: Major Surgery: 90-Day Procedures"))
					{
						String[] t = StringUtils.split(collapsedTopic, ":");
						collapsedTopic = t[0]+":"+t[1]+": "+t[2];
					}						
					String[] collapsedTopicText = StringUtils.split(collapsedTopic, "(");
					String collapsedTopicPath = StringUtils.replace(sTopicExpColIcon,"Topic",collapsedTopicText[0]);						
					refSeleniumUtils.highlightElement(collapsedTopicPath);
					Assert.assertTrue("Topic "+collapsedTopicPath+"collapsed verification is successful", refSeleniumUtils.get_TextFrom_Locator(collapsedTopicPath).equalsIgnoreCase("arrow_right"));
				}
			}


			break;
		case "COLLAPSE" :

			List<String> ListTopics= refSeleniumUtils.getWebElementValuesAsList(sTopics);
			Serenity.setSessionVariable("ListOfTopics").to(ListTopics);
			for(int j=0;j<countOfTopics;j++)
			{
				String Topic = ListTopics.get(j);
				if(Topic.contains("Topic: Major Surgery: 90-Day Procedures"))
				{
					String[] t = StringUtils.split(Topic, ":");
					Topic = t[0]+":"+t[1]+": "+t[2];
				}						
				String[] topicText = StringUtils.split(Topic, "(");
				String dynamicTopic = StringUtils.replace(sTopicExpColIcon,"Topic",topicText[0]);
				refSeleniumUtils.highlightElement(dynamicTopic);
				refSeleniumUtils.Click_given_Locator(dynamicTopic);
				Assert.assertTrue("Topic "+dynamicTopic+"collapsed verification is successful", refSeleniumUtils.get_TextFrom_Locator(dynamicTopic).equalsIgnoreCase("arrow_right"));
				for(j=j+1;j<countOfTopics;j++){
					String collapsedTopic = ListTopics.get(j);
					if(collapsedTopic.contains("Topic: Major Surgery: 90-Day Procedures"))
					{
						String[] t = StringUtils.split(collapsedTopic, ":");
						collapsedTopic = t[0]+":"+t[1]+": "+t[2];
					}						
					String[] collapsedTopicText = StringUtils.split(collapsedTopic, "(");
					String collapsedTopicPath = StringUtils.replace(sTopicExpColIcon,"Topic",collapsedTopicText[0]);						
					refSeleniumUtils.highlightElement(collapsedTopicPath);
					Assert.assertTrue("Topic "+collapsedTopicPath+"expanded verification is successful", refSeleniumUtils.get_TextFrom_Locator(collapsedTopicPath).equalsIgnoreCase("arrow_drop_down"));
				}
			}
			break;
		}
	}





	//=================================================================== CHAITANYA ==============================================================================//		

	public void validatethegivenDatainOpportunityDeck(String capturedata,String capturedtypeofdata) {
		CPWPage oCPWPage=this.switchToPage(CPWPage.class);
        SeleniumUtils refSeleniumUtils=this.switchToPage(SeleniumUtils.class);
		boolean bstatus=false;

		switch(capturedtypeofdata)
		{
		case "Updated DPkey":
			bstatus=refSeleniumUtils.is_WebElement_Displayed(StringUtils.replace(oCPWPage.labelwithtext, "svalue", "DP "+capturedata+""));

			if(bstatus)
			{
				Assert.assertTrue("Captured DPkey is still displayed,even though disposition is '"+Serenity.sessionVariableCalled("Disposition")+"' in the Available opportunity deck of PM from CPW,DPKey==>"+capturedata+",Topic==>"+Serenity.sessionVariableCalled("Topic"), false);
			}
			else
			{
				System.out.println("Captured DPkey is not displayed as expetecd for the disposition is '"+Serenity.sessionVariableCalled("Disposition")+"' in the Available opportunity deck of PM from CPW,DPKey==>"+capturedata+",Topic==>"+Serenity.sessionVariableCalled("Topic"));
			}

			//Scrolling to captured DPkey
			//refSeleniumUtils.scrollingToGivenElement(getDriver(), StringUtils.replace(oCPWPage.labelwithtext, "svalue", "DP "+capturedata+""));

			//refSeleniumUtils.highlightElement(StringUtils.replace(oCPWPage.labelwithtext, "svalue", "DP "+capturedata+""));
			break;
		case "DPkey":
			bstatus=refSeleniumUtils.is_WebElement_Displayed(StringUtils.replace(oCPWPage.labelwithtext, "svalue", "DP "+capturedata+""));

			if(bstatus)
			{
				System.out.println("Captured DPkey is displayed in the Available opportunity deck of PM from CPW,DPKey==>"+capturedata+",Medicalpolicy==>"+Serenity.sessionVariableCalled("Medicalpolicy"));
			}
			else
			{
				Assert.assertTrue("Captured DPkey is not displayed in the Available opportunity deck of PM from CPW,DPKey==>"+capturedata+",Medicalpolicy==>"+Serenity.sessionVariableCalled("Medicalpolicy"), false);
			}

			//Scrolling to captured DPkey
			refSeleniumUtils.scrollingToGivenElement(getDriver(), StringUtils.replace(oCPWPage.labelwithtext, "svalue", "DP "+capturedata+""));

			refSeleniumUtils.highlightElement(StringUtils.replace(oCPWPage.labelwithtext, "svalue", "DP "+capturedata+""));
			break;
		case "Topic":
			bstatus=refSeleniumUtils.is_WebElement_Displayed(StringUtils.replace(oCPWPage.labelcontainstext, "svalue", "Topic: "+capturedata+""));

			if(bstatus)
			{
				System.out.println("Captured Topic is displayed in the Available opportunity deck of PM from CPW,Topic==>"+capturedata+",Medicalpolicy==>"+Serenity.sessionVariableCalled("Medicalpolicy"));
			}
			else
			{

				bstatus=refSeleniumUtils.is_WebElement_Displayed(StringUtils.replace(oCPWPage.labelcontainstext, "svalue", ""+capturedata+""));

				Assert.assertTrue("Captured Topic is not displayed in the Available opportunity deck of PM from CPW,Topic==>"+capturedata+",Medicalpolicy==>"+Serenity.sessionVariableCalled("Medicalpolicy"), bstatus);
			}


			//refSeleniumUtils.highlightElement(StringUtils.replace(oCPWPage.labelcontainstext, "svalue", "Topic: "+capturedata+""));
			break;
		case "Medicalpolicy":
			bstatus=refSeleniumUtils.is_WebElement_Displayed(StringUtils.replace(oCPWPage.labelcontainstext, "svalue", "Medical Policy: "+capturedata+""));

			if(bstatus)
			{
				System.out.println("Captured Medical Policy is displayed in the Available opportunity deck of PM from CPW,Medicalpolicy==>"+capturedata);
			}
			else
			{
				Assert.assertTrue("Captured Medical Policy is not displayed in the Available opportunity deck of PM from CPW,Medicalpolicy==>"+capturedata, false);
			}


			//refSeleniumUtils.highlightElement(StringUtils.replace(oCPWPage.labelcontainstext, "svalue", "Medical Policy: "+capturedata+""));
			break;
		default:
			Assert.assertTrue("case not found ==>"+capturedtypeofdata, false);
			break;
		}


	}


	public void Select_the_given_Topic_or_MP_in_filterDrawer(String filtereddata,String filteredcriteria) {
		FilterDrawer oFilterDrawer=this.switchToPage(FilterDrawer.class);

		//Enter the captured medicalpolicy in the search field 
		refSeleniumUtils.Enter_given_Text_Element("//input[@placeholder='Search for Medical Policy / Topic']", filtereddata);

		//click the search button of search field
		refSeleniumUtils.clickGivenXpath("//button[@class='searchbutton']");

		refSeleniumUtils.defaultWait(ProjectVariables.TImeout_3_Seconds);

		refSeleniumUtils.clickGivenXpath(StringUtils.replace(oFilterDrawer.Medicalpolicy_Checkbox, "value", filtereddata));


	}

	

	public void validate_the_avaliable_DPs_count_at(String AvailableDPsCriteria) {
		
		
		String Medicalpolicy=null;
		String Topic=null;
		CPWPage oCPWPage=this.switchToPage(CPWPage.class);

		String DPCount=null;

		switch(AvailableDPsCriteria)
		{
		case "DP COUNT BASED ON CLIENT WITH FILTERS":
		case "DP COUNT BASED ON CLIENT":
			//To retrive the Available DP count at header level
			MongoDBUtils.GetDBValuesBasedonAggregation(MonGoDBQueries.FilterMongoDBQuery(AvailableDPsCriteria),AvailableDPsCriteria);

			//Retrieving the DPCount from UI
			DPCount=StringUtils.substringBetween(refSeleniumUtils.get_TextFrom_Locator("//span[contains(text(),'NPP Opportunities')]/span"),"(",")");

			//Comparing the DPcount with DB
			GenericUtils.CompareTwoValues("Available DP count at Header Level", DPCount.trim(), Serenity.sessionVariableCalled("DPkeysize"));

			break;
		case "DP COUNT BASED ON MP WITH FILTERS":
		case "DP COUNT BASED ON MP":
			 Medicalpolicy=StringUtils.substringBetween(refSeleniumUtils.get_TextFrom_Locator(StringUtils.replace(oCPWPage.labelcontainstext, "svalue", "Medical Policy")+"[1]"),"Medical Policy:","(").trim();
			String Topiccount=StringUtils.substringBetween(refSeleniumUtils.get_TextFrom_Locator(StringUtils.replace(oCPWPage.labelcontainstext, "svalue", "Medical Policy")+"[1]"),"in","Topic").trim();

			Serenity.setSessionVariable("Medicalpolicy").to(Medicalpolicy);

			System.out.println(refSeleniumUtils.get_TextFrom_Locator(StringUtils.replace(oCPWPage.labelcontainstext, "svalue", Medicalpolicy)));

			System.out.println(Medicalpolicy+" (");



			//Retrieving the DPCount from UI
			DPCount=StringUtils.substringBetween(refSeleniumUtils.get_TextFrom_Locator(StringUtils.replace(oCPWPage.labelcontainstext, "svalue", Medicalpolicy)),Medicalpolicy+" (","DP").trim();
			//To retrive the Available DP count at header level
			MongoDBUtils.GetDBValuesBasedonAggregation(MonGoDBQueries.FilterMongoDBQuery(AvailableDPsCriteria),AvailableDPsCriteria);

			//Comparing the DPcount with DB
			GenericUtils.CompareTwoValues("Available DP count at Medicalpolicy Level,MP==>"+Medicalpolicy, DPCount, Serenity.sessionVariableCalled("DPkeysize"));
			GenericUtils.CompareTwoValues("Available Topic count at Medicalpolicy Level,MP==>"+Medicalpolicy, Topiccount, Serenity.sessionVariableCalled("Topicsize"));

			break;
		case "DP COUNT BASED ON TOPIC WITH FILTERS":
		case "DP COUNT BASED ON TOPIC":
			Topic=StringUtils.substringBetween(refSeleniumUtils.get_TextFrom_Locator(StringUtils.replace(oCPWPage.labelcontainstext, "svalue", "Topic:")+"[1]"),"Topic:","(").trim();
			Serenity.setSessionVariable("Topic").to(Topic);

			//Retrieving the DPCount from UI
			DPCount=StringUtils.substringBetween(refSeleniumUtils.get_TextFrom_Locator(StringUtils.replace(oCPWPage.labelcontainstext, "svalue", Topic)),Topic+" (","DP").trim();
			//To retrive the Available DP count at header level
			MongoDBUtils.GetDBValuesBasedonAggregation(MonGoDBQueries.FilterMongoDBQuery(AvailableDPsCriteria),AvailableDPsCriteria);

			//Comparing the DPcount with DB
			GenericUtils.CompareTwoValues("Available DP count at Topic Level,Topic==>"+Topic, DPCount, Serenity.sessionVariableCalled("DPkeysize"));
			break;
		case "ASSIGNEE POPUP DP COUNT BASED ON CLIENT WITH FILTERS":
		case "ASSIGNEE POPUP DP COUNT BASED ON CLIENT":
			
			refSeleniumUtils.clickGivenXpath(StringUtils.replace(oCPWPage.Span_with_Class, "value", "assignPopover")+"/i");
			refSeleniumUtils.defaultWait(ProjectVariables.TImeout_5_Seconds);
			//Retrieving the DPCount from UI
			DPCount=StringUtils.substringBetween(refSeleniumUtils.get_TextFrom_Locator(StringUtils.replace(oCPWPage.Div_Start_with_text, "value", "Assign")),"(",")");
			
			//To retrive the Available DP count at header level
			MongoDBUtils.GetDBValuesBasedonAggregation(MonGoDBQueries.FilterMongoDBQuery(StringUtils.substringAfter(AvailableDPsCriteria, "ASSIGNEE POPUP").trim()),StringUtils.substringAfter(AvailableDPsCriteria, "ASSIGNEE POPUP").trim());

			String[] Expected = Serenity.sessionVariableCalled("DPKey").toString().split(",");
			String[] Actual = refSeleniumUtils.get_All_Text_from_Locator("//span[@class='mat-checkbox-label']//label");
			Arrays.sort(Expected);
			Arrays.sort(Actual);
			for(int i=0;i<Expected.length;i++){
				boolean sStatus=Actual[i].contains(Expected[i]);
				if(!sStatus)
					GenericUtils.Verify("Expected:"+Expected[i]+"Actual:"+Actual[i], false);
			}
			
					
			//Comparing the DPcount with DB
			GenericUtils.CompareTwoValues("Available DP count at Header Level of assign popup", DPCount.trim(), Serenity.sessionVariableCalled("DPkeysize"));

			
			refSeleniumUtils.clickGivenXpath(StringUtils.replace(oCPWPage.AssignPopupbuttons, "value", "Cancel"));
			refSeleniumUtils.defaultWait(ProjectVariables.TImeout_2_Seconds);
			
			break;
		case "ASSIGNEE POPUP DP COUNT BASED ON MP WITH FILTERS":
		case "ASSIGNEE POPUP DP COUNT BASED ON MP":
			 Medicalpolicy=StringUtils.substringBetween(refSeleniumUtils.get_TextFrom_Locator(StringUtils.replace(oCPWPage.labelcontainstext, "svalue", "Medical Policy")+"[1]"),"Medical Policy:","(").trim();
			
			Serenity.setSessionVariable("Medicalpolicy").to(Medicalpolicy);

			refSeleniumUtils.clickGivenXpath(StringUtils.replace(oCPWPage.labelcontainstext, "svalue", "Medical Policy")+"[1]/../..//i");
			refSeleniumUtils.defaultWait(ProjectVariables.TImeout_5_Seconds);
			
			
			//Retrieving the DPCount from UI
			DPCount=StringUtils.substringBetween(refSeleniumUtils.get_TextFrom_Locator(StringUtils.replace(oCPWPage.Div_Start_with_text, "value", "Assign")),"(",")");
			//To retrive the Available DP count at header level
			MongoDBUtils.GetDBValuesBasedonAggregation(MonGoDBQueries.FilterMongoDBQuery(StringUtils.substringAfter(AvailableDPsCriteria, "ASSIGNEE POPUP").trim()),StringUtils.substringAfter(AvailableDPsCriteria, "ASSIGNEE POPUP").trim());

			//Comparing the DPcount with DB
			GenericUtils.CompareTwoValues("Available DP count at Medicalpolicy Level of assign popup,MP==>"+Medicalpolicy, DPCount, Serenity.sessionVariableCalled("DPkeysize"));
			refSeleniumUtils.clickGivenXpath(StringUtils.replace(oCPWPage.AssignPopupbuttons, "value", "Cancel"));
			refSeleniumUtils.defaultWait(ProjectVariables.TImeout_2_Seconds);

			break;
		case "ASSIGNEE POPUP DP COUNT BASED ON TOPIC WITH FILTERS":
		case "ASSIGNEE POPUP DP COUNT BASED ON TOPIC":
			 Topic=StringUtils.substringBetween(refSeleniumUtils.get_TextFrom_Locator(StringUtils.replace(oCPWPage.labelcontainstext, "svalue", "Topic:")+"[1]"),"Topic:","(").trim();
			
			Serenity.setSessionVariable("Topic").to(Topic);

			refSeleniumUtils.clickGivenXpath(StringUtils.replace(oCPWPage.labelcontainstext, "svalue", "Topic:")+"[1]/../..//i");
			refSeleniumUtils.defaultWait(ProjectVariables.TImeout_5_Seconds);
			//Retrieving the DPCount from UI
			DPCount=StringUtils.substringBetween(refSeleniumUtils.get_TextFrom_Locator(StringUtils.replace(oCPWPage.Div_Start_with_text, "value", "Assign")),"(",")");
			//To retrive the Available DP count at header level
			MongoDBUtils.GetDBValuesBasedonAggregation(MonGoDBQueries.FilterMongoDBQuery(StringUtils.substringAfter(AvailableDPsCriteria, "ASSIGNEE POPUP").trim()),StringUtils.substringAfter(AvailableDPsCriteria, "ASSIGNEE POPUP").trim());

			//Comparing the DPcount with DB
			GenericUtils.CompareTwoValues("Available DP count at Topic Level of assign popup,Topic==>"+Medicalpolicy, DPCount, Serenity.sessionVariableCalled("DPkeysize"));
			refSeleniumUtils.clickGivenXpath(StringUtils.replace(oCPWPage.AssignPopupbuttons, "value", "Cancel"));
			refSeleniumUtils.defaultWait(ProjectVariables.TImeout_2_Seconds);

			break;

		default:
			Assert.assertTrue("Case not found ==>"+AvailableDPsCriteria, false);
			break;
		}



	


	

	


	}

	public boolean verifyPresentationatGivenDP(String sDPKey,String sPresentation){


		String sXGetAllPrestation = "//label[text() = '"+sDPKey+"']/ancestor::mat-card-header/following-sibling::mat-card-content/descendant::li";
		List<WebElement> sValues = getDriver().findElements(By.xpath(sXGetAllPrestation));

		for(int i=0;i<sValues.size();i++){
			String sPresn = sValues.get(i).getText();
			if(sPresentation.equalsIgnoreCase(sPresn)){
				return true;
			}
		}
		return false;
	}


	public String getWorktoDoCountforDP(String sDPKey) {
		String sXGetToDo = "//label[contains(text(),'"+sDPKey+"')]/ancestor::mat-card-header/descendant::label[@class = 'toDo']";
		return getDriver().findElement(By.xpath(sXGetToDo)).getText().trim();
			
		}
				
		public void verifyTextColor(String sDescription, String sXpathValue, String sExpectedValue) throws Exception {
			String colour=null;
			String ExpectedColour=null;
			
		switch(sExpectedValue)
		{
		case "High":
			ExpectedColour="#a3d55f";
			break;
		case "Medium":
		case "MED":
        case "Med":

			ExpectedColour="#29abe2";
			break;
		case "Low":
			ExpectedColour="#808080";
			break;

			
		default:
			Assert.assertTrue("Case not found ==>"+sExpectedValue, false);
		break;
		}
				
			if(!sDescription.contains("background"))
			{
				colour = getDriver().findElement(By.xpath(sXpathValue)).getCssValue("color");
				
			}
			else
			{
				colour = getDriver().findElement(By.xpath(sXpathValue)).getCssValue("background-color");
			}
			
			//System.out.println("color:" + colour);

			String[] hexValue = colour.replace("rgba(", "").replace(")", "").split(",");

			int hexValue1 = Integer.parseInt(hexValue[0]);

			hexValue[1] = hexValue[1].trim();

			int hexValue2 = Integer.parseInt(hexValue[1]);

			hexValue[2] = hexValue[2].trim();

			int hexValue3 = Integer.parseInt(hexValue[2]);

			String actualColor = String.format("#%02x%02x%02x", hexValue1, hexValue2, hexValue3);

			//System.out.println(actualColor);
			
			//Comparing the DPcount with DB
			GenericUtils.CompareTwoValues(sDescription+" for the priority ==>"+sExpectedValue, actualColor, ExpectedColour);
			
		}

		public void Validate_the_priority_and_reasons_for_the_captured_data(String dPKey, String DBPriority) throws Exception 
		{
			CPWPage oCPWPage=this.switchToPage(CPWPage.class);
			boolean bstatus=false;
			String PriorityOnDP=null;
			String Priorityreason=null;
			List<String> PriorityReasonsOrderList=Arrays.asList(ProjectVariables.StaticOrderofPriorityReasons.split(","));
			
			if(DBPriority.equalsIgnoreCase("Medium"))
			{
				DBPriority="MED";
			}
			
			bstatus=refSeleniumUtils.is_WebElement_Displayed(StringUtils.replace(oCPWPage.labelwithtext, "svalue", "DP "+dPKey+""));
			
			if(bstatus)
			{
				System.out.println("Captured DP Key is displayed in the 'Available Opportunity' deck,DPKey==>"+dPKey+",Topic==>"+Serenity.sessionVariableCalled("Topic"));
			}
			else
			{
				Assert.assertTrue("Captured DP Key is not displayed in the 'Available Opportunity' deck,DPKey==>"+dPKey+",Topic==>"+Serenity.sessionVariableCalled("Topic"),false);
			}
			
			PriorityOnDP=StringUtils.substringAfter(refSeleniumUtils.get_TextFrom_Locator(StringUtils.replace(StringUtils.replace(oCPWPage.Priority_Savings_On_DP, "value", dPKey),"data","priority")),":").trim();
			
			//Comparing the DPcount with DB
			GenericUtils.CompareTwoValues("Priority on DPCard==>"+dPKey+",Topic==>"+Serenity.sessionVariableCalled("Topic"), PriorityOnDP, DBPriority);
		
			//Method to verify the colour of the priority on DPCard
			verifyTextColor("Priority colour on DPCard==>"+dPKey+",Topic==>"+Serenity.sessionVariableCalled("Topic"), StringUtils.replace(StringUtils.replace(oCPWPage.Priority_Savings_On_DP, "value", dPKey),"data","priority"), DBPriority);
			
			//Validating the Priority reasons on the given DPCard
			int PriorityReasonsSize=refSeleniumUtils.get_Matching_WebElement_count(StringUtils.replace(oCPWPage.PriorityReasons_On_DP, "value", dPKey));
			for (int i = 1; i <=PriorityReasonsSize; i++) 
			{
				Priorityreason=refSeleniumUtils.get_TextFrom_Locator(StringUtils.replace(oCPWPage.PriorityReasons_On_DP, "value", dPKey)+"["+i+"]");
				//Comparing the DPcount with DB
				GenericUtils.CompareTwoValues("PriorityReason on DPCard==>"+dPKey+",Topic==>"+Serenity.sessionVariableCalled("Topic"), Priorityreason, PriorityReasonsOrderList.get(i-1));
				
				//Method to verify the background colour of the priorityreasons on DPCard
				verifyTextColor("PriorityReason background colour on DPCard==>"+dPKey+",Topic==>"+Serenity.sessionVariableCalled("Topic"), StringUtils.replace(oCPWPage.PriorityReasons_On_DP, "value", dPKey)+"["+i+"]", DBPriority);
				
				
			}
			
		}
		
		public void Validate_the_priority_and_reasons_for_the_captured_Topic_MP(String Topic_MP,String captureddatatype) throws Exception 
		{
			CPWPage oCPWPage=this.switchToPage(CPWPage.class);
			boolean bstatus=false;
			String PriorityOnDP=null;
			String Priorityreason=null;
			ArrayList<String> Prioritylist=new ArrayList<>();
			List<String> PriorityReasonsOrderList=Arrays.asList(ProjectVariables.StaticOrderofPriorityReasons.split(","));
			
			bstatus=refSeleniumUtils.is_WebElement_Displayed(StringUtils.replace(oCPWPage.labelcontainstext, "svalue",Topic_MP));
			
			if(bstatus)
			{
				System.out.println("Captured "+captureddatatype+" is displayed in the 'Available Opportunity' deck,"+captureddatatype+"==>"+Topic_MP);
			}
			else
			{
				Assert.assertTrue("Captured "+captureddatatype+" is not displayed in the 'Available Opportunity' deck,"+captureddatatype+"==>"+Topic_MP,false);
			}
			
			int Prioritysize=refSeleniumUtils.get_Matching_WebElement_count(StringUtils.replace(oCPWPage.PrioritiesUnderMP_Topic, "value", Topic_MP));
			
			//Retrieving the all Priorities under the given MP or Topic
			for (int i = 1; i <= Prioritysize; i++) 
			{
				PriorityOnDP=StringUtils.substringAfter(refSeleniumUtils.get_TextFrom_Locator(StringUtils.replace(oCPWPage.PrioritiesUnderMP_Topic, "value", Topic_MP)+"["+i+"]"),":").trim();
				Prioritylist.add(PriorityOnDP);
				
			}
			
			
			
			//Validating the Priority reasons on the given Topic or MP
			int PriorityReasonsSize=refSeleniumUtils.get_Matching_WebElement_count(StringUtils.replace(oCPWPage.PriorityReasonsUnderMP_Topic, "value", Topic_MP));
			for (int i = 1; i <=PriorityReasonsSize; i++) 
			{
				Priorityreason=refSeleniumUtils.get_TextFrom_Locator(StringUtils.replace(oCPWPage.PriorityReasonsUnderMP_Topic, "value", Topic_MP)+"["+i+"]");
				
				//Comparing the PriorityReasons on MP or Topic with DB
				GenericUtils.CompareTwoValues("PriorityReason on "+captureddatatype+"==>"+Topic_MP, Priorityreason, PriorityReasonsOrderList.get(i-1));
				
				if(Prioritylist.contains("HIGH"))
				{
					//Method to verify the background colour of the priorityreasons on MP or Topic
					verifyTextColor("PriorityReason background colour on "+captureddatatype+"==>"+Topic_MP, StringUtils.replace(oCPWPage.PriorityReasonsUnderMP_Topic, "value", Topic_MP)+"["+i+"]", "High");
					
				}
				else if(Prioritylist.contains("MED"))
				{
					//Method to verify the background colour of the priorityreasons on MP or Topic
					verifyTextColor("PriorityReason background colour on "+captureddatatype+"==>"+Topic_MP, StringUtils.replace(oCPWPage.PriorityReasonsUnderMP_Topic, "value", Topic_MP)+"["+i+"]", "Medium");
					
				}
				else if(Prioritylist.contains("LOW"))
				{
					//Method to verify the background colour of the priorityreasons on MP or Topic
					verifyTextColor("PriorityReason background colour on "+captureddatatype+"==>"+Topic_MP, StringUtils.replace(oCPWPage.PriorityReasonsUnderMP_Topic, "value", Topic_MP)+"["+i+"]", "Low");
					
				}
				
				
			}
			
			if(PriorityReasonsSize==0)
			{
				Assert.assertTrue("Priority reasons are showing as empty beside "+captureddatatype+"==>"+Topic_MP+",even though the DP is captured,DP==>"+Serenity.sessionVariableCalled("DPkey"), false);
			}
			
		}

		public void Validate_the_priority_and_reasons_on_the_given_DPCard(String capturedata, String Priorities) throws Exception {
			
			String Priority=null;
			short DPkeysize=3;
			
			List<String> DPKeyList=Arrays.asList(Serenity.sessionVariableCalled("DPkey").toString().split(","));
			List<String> PriortyList=Arrays.asList(Priorities.split(","));
			
			// TODO Auto-generated method stub
			if(capturedata.contains("Single")&&!Priorities.contains(","))
			{
				Priority=RetrieveTheExactPriority(Priorities);
				
				//Validate the priority and reasons on the given DP card
				Validate_the_priority_and_reasons_for_the_captured_data(Serenity.sessionVariableCalled("DPkey"),Priority);
				
			}
			else if(capturedata.contains("Single")&&Priorities.contains(","))
			{
				if(Priorities.contains("High"))
				{
					Priority="High";
				}
				else if(Priorities.contains("Medium"))
				{
					Priority="Med";
				}
				else if(Priorities.contains("Low"))
				{
					Priority="Low";
				}
				else
				{
					Assert.assertTrue("Case not found ==>"+Priorities, false);
				}
				//Validate the priority and reasons on the given DP card
				Validate_the_priority_and_reasons_for_the_captured_data(Serenity.sessionVariableCalled("DPkey"),Priority);
				
			}
			else if(capturedata.contains("Multiple")&&!Priorities.contains(","))
			{
				for (int i = 0; i < DPkeysize; i++) 
				{
					//Validate the priority and reasons on the given DP card
					Validate_the_priority_and_reasons_for_the_captured_data(DPKeyList.get(i).trim(),Priorities);
						
				}
			}
			else if(capturedata.contains("Multiple")&&Priorities.contains(","))
			{
				
				
				for (int i = 0; i < DPkeysize; i++) 
				{
					//Validate the priority and reasons on the given DP card
					Validate_the_priority_and_reasons_for_the_captured_data(DPKeyList.get(i).trim(),PriortyList.get(i).trim());
						
				}
			}
			else
			{
				Assert.assertTrue("Case not found ==>"+Priorities+","+capturedata, false);
			}
				
			
		}
		
		public String getLOB(String LOBValue){
			
			HashMap<String, String> oHashMap = new HashMap<String, String>();
			
			oHashMap.put("Commercial", " COM ");
			oHashMap.put("Medicaid", " MCD ");
			oHashMap.put("Medicare", " MCR ");
			oHashMap.put("Dual Eligible", " DUA ");
			oHashMap.put("BlueCard", " BLU ");
			oHashMap.put("Federal Employee", " FED ");
			return oHashMap.get(LOBValue);
			
		}

		public String RetrieveTheExactPriority(String Priorities)
		{
			String Priority=null;
			if(Priorities.contains("High"))
			{
				Priority="High";
			}
			else if(Priorities.contains("Medium"))
			{
				Priority="Med";
			}
			else if(Priorities.contains("Low"))
			{
				Priority="Low";
			}
			else
			{
				Assert.assertTrue("Case not found ==>"+Priorities, false);
			}
			
			return Priority;
		}



		public void assignToPresentation(String dPKeySource, String presentation, String assignmentLevel) throws ElementNotFoundException 
		{
			   String DPKey = "";
			   if(dPKeySource.equalsIgnoreCase("CapturedDPkey"))
			   {
				   
				   if(Serenity.sessionVariableCalled("DPKey")!=null)
				   {
				      DPKey = Serenity.sessionVariableCalled("DPKey").toString();
				   }
				   else
				   {
					   DPKey = Serenity.sessionVariableCalled("DPkey").toString();
					   DPKey =  "DP "+DPKey;
				   }				   
				   
			   }
		
			switch  (assignmentLevel.toUpperCase()){

			case "MEDICALPOLICYLEVEL":						
				 				String MedPolicyXpath = StringUtils.replace(MedPolicy,"DPKey",DPKey);
				 				oGenericUtils.clickOnElement(MedPolicyXpath);
				 				break;

			case "TOPIC LEVEL":			
				//oGenericUtils.clickOnElement(sTopicLevel);
								String TopicXpath = StringUtils.replace(Topic,"DPKey",DPKey);
				 				oGenericUtils.clickOnElement(TopicXpath);
				 				break;

			case "DPKEYLEVEL":
							String DPXpath = StringUtils.replace(DPKeyDynamicXpath, "DPKeyArg", DPKey);	
							//Close the Expanded DP grid by clicking close icon
							String DPGridCloseXPath = StringUtils.replace(DPGridCloseIcon, "DPKeyArg",DPKey);								
							if(refSeleniumUtils.is_WebElement_Displayed(DPGridCloseXPath))
							{
								refSeleniumUtils.clickGivenXpath(DPGridCloseXPath);
							}
							refSeleniumUtils.clickGivenXpath(DPXpath);
							String DPCheckboxXpath = StringUtils.replace(DPLevelAllPayerLOBCheckbox, "DPKeyArg", DPKey);	
							refSeleniumUtils.clickGivenXpath(DPCheckboxXpath);
							String AssignIcon = StringUtils.replace(DPAssignArrow, "DPKeyArg", DPKey);
							oGenericUtils.clickOnElement(AssignIcon);
							break;
			case "PAYERLOBLEVEL":				
			case "SINGLE FIRST PAYERSHORT":
				
				                     //click available path
									DPXpath = StringUtils.replace(DPKeyDynamicXpath, "DPKeyArg",DPKey);				
									refSeleniumUtils.clickGivenXpath(DPXpath);
								    oGenericUtils.clickOnElement(FirstPayershort);
									//Capture Payershort, InsuranceDesc,ClaimType
									String Payershort = refSeleniumUtils.get_TextFrom_Locator(FirstPayershortxpath).trim();
									String Payershort1 =        (Payershort.split("\\["))[0].trim();									       
									String ClaimType = refSeleniumUtils.get_TextFrom_Locator(FirstClaimType).trim();
									String ClaimType1 =  ClaimType.substring(1, 2).trim();
									String InsuranceDesc1 = refSeleniumUtils.get_TextFrom_Locator(InsuranceDesc).trim();																	    
								    AssignIcon = StringUtils.replace(DPAssignArrow, "DPKeyArg",DPKey);
									oGenericUtils.clickOnElement(AssignIcon);
									System.out.println("Values ::"+Payershort1+ClaimType1+InsuranceDesc1);
									
									Serenity.setSessionVariable("AssignedPayershort").to(Payershort1);
									Serenity.setSessionVariable("AssignedClaimtype").to(ClaimType1);
									Serenity.setSessionVariable("AssigneInsuranceDesc").to(InsuranceDesc1);	
									
									AssignIcon = StringUtils.replace(DPAssignArrow, "DPKeyArg", DPKey);
									oGenericUtils.clickOnElement(AssignIcon);
								break;
								
			case "UNASSIGNED PPS":						    
				
				      	DPXpath = StringUtils.replace(DPKeyDynamicXpath, "DPKeyArg",DPKey);		
				    	 evaluateJavascript("arguments[0].scrollIntoView(true);",refSeleniumUtils.findBy(DPXpath) );	
						refSeleniumUtils.clickGivenXpath(DPXpath);					
						
					      String allchkboxes = "(//tr[@role='row'][contains(@class,'mat-row')])/td[1]//span[contains(@class,'checkmark')]";
				          List<WebElement>  AllChkBoxElements =  refSeleniumUtils.getElementsList("XPATH", allchkboxes);
				          
				          for(int k=1;k<=AllChkBoxElements.size();k++)
				          {							        	  
				        	  	 //evaluateJavascript("arguments[0].scrollIntoView(true);", AllChkBoxElements.get(k));
				        	  	String  AssignedPresentation = "";
				        	  	 try{
				        	 
				        	  		 String AssignedPresXPath = "("+allchkboxes+")["+k+"]//ancestor::td//following-sibling::td//input[@type='checkbox']//ancestor::td//span";
				        	  		 
				        	  		if(refSeleniumUtils.is_WebElement_Displayed(AssignedPresXPath)) 
				        	  		{
				        	  		AssignedPresentation = refSeleniumUtils.get_TextFrom_Locator(AssignedPresXPath).trim();
				        	  		}
				        	        		
				        	        
				        	  	 }
				        	  	 catch (Exception e) { System.out.println(e.getMessage());}
				        	  	
					    
					        		  if((AssignedPresentation.isEmpty()))
					        	  {
					        		  refSeleniumUtils.clickGivenWebElement(AllChkBoxElements.get(k));	
					        		  String Payershort2 = refSeleniumUtils.get_TextFrom_Locator("("+allchkboxes+")["+k+"]//ancestor::td//span").trim();
					        		  String ClaimType3 = refSeleniumUtils.get_TextFrom_Locator("("+allchkboxes+")["+k+"]//ancestor::td//span/span").trim();
					        		
					        		  //Capture values
					        		  String Payershort3 =        (Payershort2.split("\\["))[0].trim();		
					        		  String ClaimType4 = refSeleniumUtils.get_TextFrom_Locator(FirstClaimType).trim();
					        		  String ClaimType5 =  ClaimType4.substring(1, 2).trim();
																		  
									  //Get the Insurance Desc name								
									  String InsuranceDescAttrib = refSeleniumUtils.Get_Value_By_given_attribute("class","("+allchkboxes+")["+k+"]//ancestor::td//following-sibling::td//input[@type='checkbox']//ancestor::td");	
									  String insurance = StringUtils.substringBetween(InsuranceDescAttrib, "mat-cell cdk-column-","mat-column-");
									  
									  Serenity.setSessionVariable("AssignedPayershort").to(Payershort3);
									  Serenity.setSessionVariable("AssignedClaimtype").to(ClaimType5);
									  Serenity.setSessionVariable("AssignedInsuranceDesc").to(insurance);
									  
									  AssignIcon = StringUtils.replace(DPAssignArrow, "DPKeyArg", DPKey);
									   oGenericUtils.clickOnElement(AssignIcon);
									  
					        		  break;
					        	  }				        	  
					        	  
					        	  
				          }
				          
				
				
				break;
								
								
			case "DP MULTIPLE":
			case "DP ALL":			
				break;

			default:
				Assert.assertTrue("Invalid selection", false);

			}			
			//Code for assigning to presentation			
			 String sAssignChkBox =   StringUtils.replace(sPresentationAssignmentCheckbox,"val",Serenity.sessionVariableCalled("PresentationName"));				    
			refSeleniumUtils.Click_given_Locator(sAssignChkBox);		    
		    refSeleniumUtils.highlightElement(oPresProfile.sPresentationAssignmentOKBtn);
		    refSeleniumUtils.Click_given_Locator(oPresProfile.sPresentationAssignmentOKBtn);
		    refSeleniumUtils.defaultWait(ProjectVariables.TImeout_1_Seconds);
		}
				

		

		

		public void Validate_filter_drawer_section_with_DB(String DBData, String criteria) 
		{
			CPWPage  oCPWPage=this.switchToPage(CPWPage.class);
			String DBValue=null;
			String DBSavings=null;
			String UISavings=null;
			String DBTopic=null;
			String DBDPkey=null;
			List<String> DBDataList=Arrays.asList(DBData.replace("]", "").split(";"));;
			
			boolean bStatus=false;
			int i=1;
			
			
			switch(criteria)
			{
			case "MP":
			case "Topic":
				DBDataList=Arrays.asList(DBData.split("=,"));
				for (String DBMPDetails: DBDataList) 
				{
					DBValue=StringUtils.substringBefore(DBMPDetails, ";").trim();
					DBSavings=StringUtils.substringAfter(DBMPDetails, ";").trim();
					if(DBValue.substring(0,1).equalsIgnoreCase("["))
					{
						DBValue=StringUtils.substringAfter(DBValue,"[");
					}
					if(DBSavings.contains("="))
					{
						DBSavings=StringUtils.substringBefore(DBSavings,"=");
					}
					
					bStatus=oCPWPage.Enter_the_given_MP_Topic_in_filter_Drawer(DBValue);
					if(bStatus)
					{
						
						GenericUtils.Verify(i+"."+criteria+" '"+DBValue+"' is displayed as per the DB in filterdrawer section,"+criteria+" size==>"+DBDataList.size(), true);
					}
					else
					{
						
						GenericUtils.Verify(i+"."+criteria+" '"+DBValue+"' is not displayed as per the DB in filterdrawer section,"+criteria+" size==>"+DBDataList.size(), false);
					}
					
					i=i+1;
					
					
					if(!refSeleniumUtils.is_WebElement_Displayed(StringUtils.replace(oCPWPage.MP_TopicSavings, "value", DBValue))){
						UISavings=StringUtils.substringAfter(refSeleniumUtils.get_TextFrom_Locator(StringUtils.replace(oCPWPage.MP_TopicSavings_Contains, "value", DBValue)),"$").replaceAll(",", "").trim();	
					}
					else{
						UISavings=StringUtils.substringAfter(refSeleniumUtils.get_TextFrom_Locator(StringUtils.replace(oCPWPage.MP_TopicSavings, "value", DBValue)),"$").replaceAll(",", "").trim();
					}
					GenericUtils.CompareTwoValues("Rawsavings for "+criteria+" in filterdrawer,Expected value=>"+DBSavings+",Actual value=>"+UISavings+","+criteria+"=>"+DBValue, UISavings, DBSavings);
					
					
					
					
				}
			break;
			case "DPKey":
				DBData=DBData.replace("[", "");
				DBDataList=Arrays.asList(DBData.replace("]", "").split(";"));;
				DBDPkey=DBDataList.get(0).trim();
				DBTopic=DBDataList.get(2).trim();
				refSeleniumUtils.clickGivenXpath(StringUtils.replace(oCPWPage.ButtonContainsText, "value", "Reset"));
				
				 bStatus=oCPWPage.Enter_the_given_MP_Topic_in_filter_Drawer(DBTopic);
				 
				 if(bStatus)
					{
						GenericUtils.Verify(criteria+" '"+DBTopic+"' is displayed as per the DB in filterdrawer section", true);
					}
					else
					{
						GenericUtils.Verify(criteria+" '"+DBTopic+"' is not displayed as per the DB in filterdrawer section", false);
					}
					
				refSeleniumUtils.clickGivenXpath(StringUtils.replace(refFilterDrawer.Medicalpolicy_Checkbox, "value", DBTopic));
					 
				refFilterDrawer.user_filters_by_clicking_on_Apply_for_Medical_Policy_Topic();
					 
				validatethegivenDatainOpportunityDeck(DBDPkey, "DPkey");
				refSeleniumUtils.defaultWait(ProjectVariables.TImeout_3_Seconds);
				refSeleniumUtils.clickGivenXpath(StringUtils.replace(oCPWPage.labelwithtext, "svalue", "DP "+DBDPkey+""));
				refSeleniumUtils.waitForContentLoad();
				//Validating payerLOB grid with Db
				validateThePayerLOBGridwithDBIn(DBData.replace("]", ""),"Available Opportunity Deck");
				
				break;
			case "PayerLOBGrid in presentationview":
				DBData=DBData.replace("[", "");
				DBDataList=Arrays.asList(DBData.replace("]", "").split(";"));;
				DBDPkey=DBDataList.get(0).trim();
				
				refSeleniumUtils.clickGivenXpath(StringUtils.replace(oCPWPage.PresentationViewDPKey, "value", DBDPkey));
				refSeleniumUtils.defaultWait(ProjectVariables.TImeout_3_Seconds);
				refSeleniumUtils.waitForContentLoad();
				
				//Validating payerLOB grid with Db
				validateThePayerLOBGridwithDBIn(DBData.replace("]", ""),"PresentationView");
				
			break;
			default:
				Assert.assertTrue("Case not found==>"+criteria, false);
			break;
			}
			
			
			
			
		}

		private void validateThePayerLOBGridwithDBIn(String DBData,String section)
		{
			CPWPage  oCPWPage=this.switchToPage(CPWPage.class);
			List<String> DBDataList=null;
			String DBpayershort=null;
			String DBInsurance=null;
			String DBClaimtype=null;
			String DBTopic=null;
			String UISavings=null;
			String DBDPkey=null;
			String DBSavings=null;
			String sUIInsurance=null;
			DBDataList=Arrays.asList(DBData.split(";"));
			DBTopic=DBDataList.get(2).trim();
			DBDPkey=DBDataList.get(0).trim();
			DBpayershort=DBDataList.get(3).trim();
			DBInsurance=DBDataList.get(4).trim();
			DBClaimtype=DBDataList.get(5).trim();
			DBSavings=DBDataList.get(6).trim();
			
			ArrayList<String> UIPayershortlist=new ArrayList<>();
			ArrayList<String> UIClaimtpelist=new ArrayList<>();
			ArrayList<String> UIInsurancelist=new ArrayList<>();
			
			List<String> DBPayershortList=Arrays.asList(DBpayershort.split(","));
			List<String> DBInsuranceList=Arrays.asList(DBInsurance.split(","));
			List<String> DBClaimtypeList=Arrays.asList(DBClaimtype.split(","));
			
			/*if(section.equalsIgnoreCase("PresentationView"))
			{
				ValdaiteTheFiltersectiondataInPresentationview(DBPayershortList,"Payershort");
				ValdaiteTheFiltersectiondataInPresentationview(DBPayershortList,"Insurance");
				ValdaiteTheFiltersectiondataInPresentationview(DBPayershortList,"Claimtype");
			}*/
			
			Assert.assertTrue("DP Decision view is not displayed,after clicking on the DP=>"+DBDPkey+" in "+section+" for the Topic=>"+DBTopic, refSeleniumUtils.is_WebElement_Displayed(StringUtils.replace(oCPWPage.Span_with_text, "value", "DP "+DBDPkey+"")));
			
			UISavings=StringUtils.substringAfter(refSeleniumUtils.get_TextFrom_Locator(StringUtils.replace(oCPWPage.DPSavingsInAvailableDeck,"value",DBDPkey)),"$").replaceAll(",", "").trim();
			
			int iPayerssize=refSeleniumUtils.get_Matching_WebElement_count(StringUtils.replace(oCPWPage.PayerClaimtypeInAvailableDeck,"value",DBDPkey));
			
			for (int i = 1; i<=iPayerssize; i++) 
			{
				String sUIPayershort=StringUtils.substringBefore(refSeleniumUtils.get_TextFrom_Locator(StringUtils.replace(oCPWPage.PayerClaimtypeInAvailableDeck,"value",DBDPkey)+"["+i+"]/ancestor::span"),"[").trim();
				String sUIClaimtype=StringUtils.substringBetween(refSeleniumUtils.get_TextFrom_Locator(StringUtils.replace(oCPWPage.PayerClaimtypeInAvailableDeck,"value",DBDPkey)+"["+i+"]"),"[","]").trim();
				UIPayershortlist.add(sUIPayershort);
				UIClaimtpelist.add(sUIClaimtype);
			}
			
			GenericUtils.CompareTwoValues("UIPayershorts=>"+UIPayershortlist+",DBPayershort=>"+DBPayershortList+" in "+section+" for DP=>"+DBDPkey+",Topic=>"+DBTopic, UIPayershortlist, DBPayershortList);
			GenericUtils.CompareTwoValues("UIClaimtypes=>"+UIClaimtpelist+",DBClaimtypes=>"+DBClaimtypeList+" in "+section+" for DP=>"+DBDPkey+",Topic=>"+DBTopic, UIClaimtpelist, DBClaimtypeList);
			
			int iInsurancesize=refSeleniumUtils.get_Matching_WebElement_count(StringUtils.replace(oCPWPage.InsurancesInAvailableDeck,"value",DBDPkey));
			
			for (int i = 1; i<=iInsurancesize; i++) 
			{
				if(section.equalsIgnoreCase("PresentationView"))
				{
					sUIInsurance=refSeleniumUtils.get_TextFrom_Locator(StringUtils.replace(oCPWPage.InsurancesInAvailableDeck,"value",DBDPkey)+"["+i+"]//span[not (contains(@class,'checkmark'))]").trim();	
				}
				else
				{
					sUIInsurance=refSeleniumUtils.get_TextFrom_Locator(StringUtils.replace(oCPWPage.InsurancesInAvailableDeck,"value",DBDPkey)+"["+i+"]//label").trim();
					 
				}
				UIInsurancelist.add(sUIInsurance);
			}
			GenericUtils.CompareTwoValues("UIInsurances=>"+UIInsurancelist+",DBInsurances=>"+DBInsuranceList+" in "+section+" for DP=>"+DBDPkey+",Topic=>"+DBTopic, UIInsurancelist, DBInsuranceList);
	
			GenericUtils.CompareTwoValues("UISavings=>"+UISavings+",DBSavings=>"+DBSavings+" in "+section+" for DP=>"+DBDPkey+",Topic=>"+DBTopic, UISavings, DBSavings);
			
		}

		public void validateRuleRelationshipIconDetails(String validteForDPCardorLOBGrid)
		{
		
			String CapturedDPKey =  Serenity.sessionVariableCalled("DPKey").toString().trim();
			String   RulerelatioIconxpath =  "//mat-card-header//label[text()='DPKey']//ancestor::app-cpd-dp-details-container//mat-icon[contains(@class,'ruleRelationIcon')]";
			String DPKeyXpath = "//mat-card-header//label[text()='DPKey']";
			String TooltipText = "//div[@class='cdk-overlay-container']//div[@class='cdk-overlay-pane mat-tooltip-panel']//div[contains(@class,'mat-tooltip')]";
			String LOBGridRelationIcons =   "//div[@class='dp_view_main']//tbody[@role='rowgroup']//i[@class='fa fa-bookmark txt-red']";
			String RuleRelatioShipIcon = StringUtils.replace(RulerelatioIconxpath,"DPKey",CapturedDPKey);
			String DPKey = StringUtils.replace(DPKeyXpath,"DPKey",CapturedDPKey);
			
			switch(validteForDPCardorLOBGrid)
			{
						case "DPCardHeader":
						
								if(refSeleniumUtils.is_WebElement_Displayed(RuleRelatioShipIcon))
								{
									Assert.assertTrue("RuleRelatioShipIcon is displayed for the Captured DPKey::"+CapturedDPKey +"in the PM OpportunityDeck on DPCardHeader", true);								
								}	
								else
								{
									Assert.assertTrue("RuleRelatioShipIcon is displayed for the Captured DPKey::"+CapturedDPKey +"in the PM OpportunityDeck on DPCardHeader", false);	
									getDriver().quit();
								}
							
								 WebElement RelationIcon = getDriver().findElement(By.xpath(RuleRelatioShipIcon));																								
								 refSeleniumUtils.moveToElement(RelationIcon);			
								 String sTooltip = getDriver().findElement(By.xpath(TooltipText)).getText().trim();									 
								  boolean TooltipStatus = sTooltip.equalsIgnoreCase("Rule Relationship Alert");;
								  GenericUtils.Verify("Tooltip::"+sTooltip+" is displayed when User hovers mouse for  DPKey::", TooltipStatus);							
						break;		

						case "DPPayerLOBGrid":
								refSeleniumUtils.highlightElement(DPKey);
							     refSeleniumUtils.clickGivenXpath(DPKey);
							     List<WebElement> GridRelationIconElements = null;	
								try {
									 GridRelationIconElements = refSeleniumUtils.getElementsList("XPATH", LOBGridRelationIcons);
								
								} catch (ElementNotFoundException e) 
								{							      
									Assert.assertTrue("RuleRelatioShipIcons not displayed"+e.getMessage(), false);
									getDriver().quit();								
								}
							   
								boolean flag = false;
								if(GridRelationIconElements.size() == 0)  {  flag = false;}
								else {  flag = true;   }
								
								//Check if RuleRelationship icons are displayed or not in the DPKey LOB Grid,if not throw error
								 GenericUtils.Verify("RuleRelationshipicons displayed in the PayershortLOBGrid for the given DPKey", flag);	
									
								 	//Check if the Tooltip is displayed for the first Rulerelationship icon
									 refSeleniumUtils.moveToElement(GridRelationIconElements.get(1));	
									 
									 sTooltip=refSeleniumUtils.getWebElementAttributeVal("title", GridRelationIconElements.get(1));															 
									 TooltipStatus = sTooltip.equalsIgnoreCase("Rule Relationship Alert");;
									 GenericUtils.Verify("Validate Tooltip::"+sTooltip+" display when User hovers mouse on the first Payershort in the LOBGrid::", TooltipStatus);								
						break;			
			
			}		
					
		}	

		public void captureRelatedDPsandRules(String dPKey) 
		{
		
			List<String> DPKeysVals =  new ArrayList<String>();
			//List<String> DPKeysForValidation =  new ArrayList<String>();		
			Set<String> DPKeysForValidation =  new HashSet<String>();		
			String DPKey = Serenity.sessionVariableCalled("DPKey").toString();
								
			String AllDPkeysinRuleRelationPopup    =  "//mat-dialog-content[contains(@class,'ruleReltionsDialog')]//mat-cell[contains(@class,' cdk-column-DP')]";					
			String RuleRelationPopupCloseBtn  =   "//app-cpd-rule-relationship-popup//i[@class='fa fa-close closeIcon']";
			
		
				//Click on the DP Card
				  String DPXpath = StringUtils.replace(DPKeyDynamicXpath, "DPKeyArg", DPKey);				
			 	  refSeleniumUtils.clickGivenXpath(DPXpath);
			  	  String PayershortRuleRelationiconXPath = "//mat-card-header//label[text()='DPKeyArg']//ancestor::app-cpd-dp-details-container//i[@class='fa fa-bookmark txt-red']";
			 	 
			 	 //Click on the available first rule relationship icon	
			       String PayershortRuleRelationicon =  StringUtils.replace(PayershortRuleRelationiconXPath, "DPKeyArg", DPKey);	
			 	   refSeleniumUtils.clickGivenXpath(PayershortRuleRelationicon);	 	
			 	   
			 	  ///Get all DPs from RuleRelationship popup	 with Status "Opportunity" only which is defined in the xpath	
			 	    DPKeysVals = refSeleniumUtils.getWebElementValuesAsList(AllDPkeysinRuleRelationPopup);
			 	    
			 	  //Take second value from the list onwards to validate in Presentation Profile assignment as 1st value is the DPKey already assigned.	 	    
			 	    for(int j=1;j<DPKeysVals.size();j++)
			 	    {
			 	    	DPKeysForValidation.add(DPKeysVals.get(j));	 	    	
			 	    }
			 		 	  
			 	      Serenity.setSessionVariable("RuleRelationDPKeys").to(DPKeysForValidation);  		 	      
			 	   
			 	      //Close the RuleRelationpopup 
			 	      refSeleniumUtils.clickGivenXpath(RuleRelationPopupCloseBtn);	 
			 	      
			
		}
		
		
		public void verifyNoOppurtunityMessage(String sMessage)
		{
			if(refSeleniumUtils.isElemPresent(sMessage_No_Oppurtunities))
			{
				//refSeleniumUtils.highlightElement(sMessage_No_Oppurtunities);
				Assert.assertTrue(sMessage+" is verified successfully in Oppurtunity grid", true);
			}else{
				Assert.assertTrue(sMessage+" is not displayed in Oppurtunity grid", false);
			}
			
		}
		

		 //************************ Sorting		
				
			public void validateSortingfunctionalityinAvailableOpportunitydeck(String SortbyOptions) 
			{
				CPWPage oCPWPage=this.switchToPage(CPWPage.class);
				
				ArrayList<String> MPlist=new ArrayList<>();
				ArrayList<String> Topiclist=new ArrayList<>();
				ArrayList<String> DPlist=new ArrayList<>();
				ArrayList<Long> DPLonglist=new ArrayList<>();
				List<String> SortbyOptionsList=Arrays.asList(SortbyOptions.split(","));
			
				
				
				for (int i = 0; i < SortbyOptionsList.size(); i++) 
				{
					GenericUtils.Verify("clicking on sortby dropdown", oGenericUtils.clickOnElement(SortbyDropdown));
					GenericUtils.Verify("clicking on sortby option '"+SortbyOptionsList.get(i)+"' in dropdown", oGenericUtils.clickOnElement(StringUtils.replace(SortbyDropdownValues, "DropDownVal", SortbyOptionsList.get(i).trim())));
					refSeleniumUtils.defaultWait(ProjectVariables.TImeout_3_Seconds);
					GenericUtils.Verify("clicking on Exapnd all link", oGenericUtils.clickOnElement(StringUtils.replace(oCPWPage.anchorTag_with_text, "value", "Expand All")));
					refSeleniumUtils.defaultWait(ProjectVariables.TImeout_2_Seconds);
					switch(SortbyOptionsList.get(i))
					{
					case "Alphanumeric":
						//Retrieve the MPs from Available Deck
						MPlist.addAll(RetrieveTheDatafromAvaialbleDeck("MP",SortbyOptionsList.get(i)));
						GenericUtils.validate_the_sorting_funtionality_String(MPlist, "Sort Ascending", "Medicalpolicies in Available Opportunity");
						
						//Retrieve the Topics from Available Deck
						Topiclist.addAll(RetrieveTheDatafromAvaialbleDeck("Topic",SortbyOptionsList.get(i)));
						GenericUtils.validate_the_sorting_funtionality_String(Topiclist, "Sort Ascending", "Topics in Available Opportunity");
						
						
						//Retrieve the DPs from Available Deck
						DPlist.addAll(RetrieveTheDatafromAvaialbleDeck("DP",SortbyOptionsList.get(i)));
						for (int j = 0; j <DPlist.size(); j++) 
						{
							DPLonglist.add(Long.valueOf(DPlist.get(j)));
						}
						
						GenericUtils.validate_the_sorting_funtionality_Integer(DPLonglist, "Sort Ascending", "DPs in Available Opportunity");
										
					break;
					
					case "Savings by Highest Dollar Policy":
						
						//Retrieve the MPs from Available Deck
						MPlist.addAll(RetrieveTheDatafromAvaialbleDeck("MP",SortbyOptionsList.get(i)));
						//Method to validate the sorting for the option 'savings'
						validateSortingbasedonSavingsinAvailabledeck(MPlist,"Medicalpolicy");
						
						//Retrieve the Topics from Available Deck
						Topiclist.addAll(RetrieveTheDatafromAvaialbleDeck("Topic",SortbyOptionsList.get(i)));
						//Method to validate the sorting for the option 'savings'
						validateSortingbasedonSavingsinAvailabledeck(Topiclist,"Topic");
						
						//Retrieve the Topics from Available Deck
						DPlist.addAll(RetrieveTheDatafromAvaialbleDeck("DP",SortbyOptionsList.get(i)));
						//Method to validate the sorting for the option 'savings'
						validateSortingbasedonSavingsinAvailabledeck(DPlist,"DP");
										
					break;
					case "Priority":
						//Retrieve the MPs from Available Deck
						MPlist.addAll(RetrieveTheDatafromAvaialbleDeck("MP",SortbyOptionsList.get(i)));
						//Method to validate the sorting for the option 'Priority'
						validateTheSortbyPriorityforthegiven(MPlist,"Medicalpolicy");
						
						//Retrieve the Topics from Available Deck
						Topiclist.addAll(RetrieveTheDatafromAvaialbleDeck("Topic",SortbyOptionsList.get(i)));
						//Method to validate the sorting for the option 'Priority'
						validateTheSortbyPriorityforthegiven(Topiclist,"Topic");
						
						//Retrieve the Topics from Available Deck
						DPlist.addAll(RetrieveTheDatafromAvaialbleDeck("DP",SortbyOptionsList.get(i)));
						//Method to validate the sorting for the option 'Priority'
						validateTheSortbyPriorityforthegiven(DPlist,"DP");
						
					break;
					case "eLL Order":
						//Retrieve the MPs from Available Deck
						MPlist.addAll(RetrieveTheDatafromAvaialbleDeck("MP",SortbyOptionsList.get(i)));
						//Method to validate the sorting for the option 'Priority'
						validateTheSortbyeLlOrderforthegiven(MPlist,"Medicalpolicy");
						
						//Retrieve the Topics from Available Deck
						Topiclist.addAll(RetrieveTheDatafromAvaialbleDeck("Topic",SortbyOptionsList.get(i)));
						//Method to validate the sorting for the option 'Priority'
						validateTheSortbyeLlOrderforthegiven(Topiclist,"Topic");
						
						//Retrieve the Topics from Available Deck
						DPlist.addAll(RetrieveTheDatafromAvaialbleDeck("DP",SortbyOptionsList.get(i)));
						//Method to validate the sorting for the option 'Priority'
						validateTheSortbyeLlOrderforthegiven(DPlist,"DP");
					break;
					case "Savings by Highest Dollar DP":
						//Retrieve the MPs from Available Deck
						MPlist.addAll(RetrieveTheDatafromAvaialbleDeck("MP",SortbyOptionsList.get(i)));
						//Method to validate the sorting for the option 'Savings by DP'
						validateTheSortbySavingsDPforthegiven(MPlist,"Medicalpolicy");
						
						
						//Retrieve the Topics from Available Deck
						Topiclist.addAll(RetrieveTheDatafromAvaialbleDeck("Topic",SortbyOptionsList.get(i)));
						//Method to validate the sorting for the option 'Savings by DP'
						validateTheSortbySavingsDPforthegiven(Topiclist,"Topic");
						
						//Retrieve the Topics from Available Deck
						DPlist.addAll(RetrieveTheDatafromAvaialbleDeck("DP",SortbyOptionsList.get(i)));
						//Method to validate the sorting for the option 'Savings by DP'
						validateTheSortbySavingsDPforthegiven(DPlist,"DP");
						break;
					default:
						Assert.assertTrue("Case not found===>"+SortbyOptionsList.get(i), false);
					break;
					}
					
					//method to select all MPs in filterdrawer section
					refFilterDrawer.user_selects_all_MedicalPolicies();
					GenericUtils.Verify("clicking on Applyfilters button",oGenericUtils.clickButton(By.xpath(refFilterDrawer.sBtn_Apply)));
					MPlist.clear();
					DPlist.clear();
					Topiclist.clear();
				}
				
			}

			private void validateTheSortbyeLlOrderforthegiven(ArrayList<String> Datalist,String typeofdata) 
			{
				String sDPKey=null;
				ArrayList<String> DPlist=new ArrayList<>();
				ArrayList<Long> DPSortorderlist=new ArrayList<>();
				ArrayList<String> ReqSortorderlist=new ArrayList<>();
				switch(typeofdata)
				{
				case "Topic":
				case "Medicalpolicy":
					for (int i = 0; i < Datalist.size(); i++) 
					{
						if(typeofdata.equalsIgnoreCase("Topic"))
						{
							sDPKey=StringUtils.substringAfter(oGenericUtils.get_TextFrom_Locator(StringUtils.replace(FirstDPInTopic, "value", Datalist.get(i))),"DP").trim();
						}
						else
						{
							if(Serenity.sessionVariableCalled("PresentationName")==null)
							{
								sDPKey=StringUtils.substringAfter(oGenericUtils.get_TextFrom_Locator(StringUtils.replace(FirstDPInMP, "MP", Datalist.get(i))),"DP").trim();	
							}
							else
							{
								sDPKey=StringUtils.substringAfter(oGenericUtils.get_TextFrom_Locator(StringUtils.replace(FirstDPInMPInPres, "MP", Datalist.get(i))),"DP").trim();
							}
						}
						
						DPlist.add(sDPKey);
						System.out.println(i+"."+Datalist.get(i)+",DP==>"+sDPKey);
					}
					//DBMethod to reprieve the DPsortOrder for the DPlist
					DPSortorderlist.addAll(MongoDBUtils.RetrieveTheSortOrderofthegivenDP(DPlist));
					
					//Method to validate the sorting for the option 'eLl Order'
					GenericUtils.validate_the_sorting_funtionality_Integer(DPSortorderlist, "Sort Ascending", "DPSortorderList in Available Opportunity for "+typeofdata+"=>"+DPlist);
					
					for (int i = 0; i < DPSortorderlist.size()-1; i++) 
					{
						if(DPSortorderlist.get(i)==(DPSortorderlist.get(i+1)))
						{
							System.out.println("MPs DPsortorderlist=>"+DPSortorderlist.size());
							System.out.println("MPs Size=>"+Datalist.size());
							System.out.println(i+"."+DPSortorderlist.get(i)+","+i+1+"."+DPSortorderlist.get(i+1));
							System.out.println(i+"."+Datalist.get(i)+","+i+1+"."+Datalist.get(i+1));
							ReqSortorderlist.add(Datalist.get(i));
							ReqSortorderlist.add(Datalist.get(i+1));
							//Method to validate the sorting for the option 'eLl Order'
							GenericUtils.validate_the_sorting_funtionality(ReqSortorderlist, "Sort Ascending", ""+typeofdata+" in Available Opportunity");
							ReqSortorderlist.clear();
						}
					}
					
				break;
				case "DP":
					
					//DBMethod to reprieve the DPsortOrder for the DPlist
					DPSortorderlist.addAll(MongoDBUtils.RetrieveTheSortOrderofthegivenDP(Datalist));
					
					//Method to validate the sorting for the option 'eLl Order'
					GenericUtils.validate_the_sorting_funtionality_Integer(DPSortorderlist, "Sort Ascending", "DPSortorderList in Available Opportunity for "+typeofdata+"=>"+Datalist);
					
					/*for (int i = 0; i < DPSortorderlist.size()-1; i++) 
					{
						if(DPSortorderlist.get(i)==(DPSortorderlist.get(i+1)))
						{
							ReqSortorderlist.add(Datalist.get(i));
							ReqSortorderlist.add(Datalist.get(i+1));
							//Method to validate the sorting for the option 'eLl Order'
							GenericUtils.validate_the_sorting_funtionality(ReqSortorderlist, "Sort Ascending", ""+typeofdata+" in Available Opportunity");
						}
					}*/
					
				break;
				default:
					Assert.assertTrue("Case not found===>"+typeofdata, false);
				break;
				}
				
			}

			private void validateTheSortbySavingsDPforthegiven(ArrayList<String> Datalist,String typeofdata) 
			{
				String sDPKey=null;
				ArrayList<String> DPlist=new ArrayList<>();
				switch(typeofdata)
				{
				case "Topic":
				case "Medicalpolicy":
					for (int i = 0; i < Datalist.size(); i++) 
					{
						if(typeofdata.equalsIgnoreCase("Topic"))
						{
							sDPKey=StringUtils.substringAfter(oGenericUtils.get_TextFrom_Locator(StringUtils.replace(FirstDPInTopic, "value", Datalist.get(i))),"DP").trim();
						}
						else
						{
							if(Serenity.sessionVariableCalled("PresentationName")==null)
							{
								sDPKey=StringUtils.substringAfter(oGenericUtils.get_TextFrom_Locator(StringUtils.replace(FirstDPInMP, "MP", "Medical Policy: "+Datalist.get(i))),"DP").trim();	
							}
							else
							{
								sDPKey=StringUtils.substringAfter(oGenericUtils.get_TextFrom_Locator(StringUtils.replace(FirstDPInMPInPres, "MP", "Medical Policy: "+Datalist.get(i))),"DP").trim();
							}
						}
						
						DPlist.add(sDPKey);
						System.out.println(i+"."+Datalist.get(i)+",DP==>"+sDPKey);
					}
					
					//Validate the sorting based on the sort by highestsavings DP
					validateSortingbasedonSavingsinAvailabledeck(DPlist, "DP");
					System.out.println(typeofdata+" are displayed in sortby highest dollor DP..........");
					
				break;
				case "DP":
					//Validate the sorting based on the sort by highestsavings DP
					validateSortingbasedonSavingsinAvailabledeck(Datalist, typeofdata);
					System.out.println(typeofdata+" are displayed in sortby highest dollor DP..........");
					
				break;
				default:
					Assert.assertTrue("Case not found===>"+typeofdata, false);
				break;
				}
				
			}
			
			
			private void validateTheSortbyPriorityforthegiven(ArrayList<String> Datalist,String typeofdata) 
			{
				String sPriority=null;
				
				ArrayList<String> Highlist=new ArrayList<>();
				ArrayList<String> Medlist=new ArrayList<>();
				ArrayList<String> Lowlist=new ArrayList<>();
				
				for (int k = 0; k < Datalist.size(); k++) 
				{
					//To retrieve the priority for the given data
					sPriority=MongoDBUtils.RetrieveThePriorityofthegiven(Datalist.get(k), typeofdata);
					
					
					if(sPriority.contains("High"))
					{
						if(Medlist.isEmpty()&&Lowlist.isEmpty())
						{
							Highlist.add(Datalist.get(k));	
						}
						else
						{
							Assert.assertTrue("Sortby priority was not working in availabledeck for '"+typeofdata+"'=>"+Datalist.get(k), false);
						}
							
					}
					else if(!sPriority.contains("High")&&sPriority.contains("Medium"))
					{
						
						Medlist.add(Datalist.get(k));
					}
					else if(!sPriority.contains("High")&&!sPriority.contains("Medium")&&sPriority.contains("Low"))
					{
						
						Lowlist.add(Datalist.get(k));
					}
					
				}
				
				GenericUtils.validate_the_sorting_funtionality(Highlist, "Sort Ascending", ""+typeofdata+" in Available Opportunity");
				GenericUtils.validate_the_sorting_funtionality(Medlist, "Sort Ascending", ""+typeofdata+" in Available Opportunity");
				GenericUtils.validate_the_sorting_funtionality(Lowlist, "Sort Ascending", ""+typeofdata+" in Available Opportunity");
				
			
			}

			
			
			private void validateSortingbasedonSavingsinAvailabledeck(ArrayList<String> Datalist,String typeofdata)
			{
				String sRawSavings=null;
				
				
					for (int k = 0; k < (Datalist.size()- 1); k++) 
					{
						
						if(typeofdata.equalsIgnoreCase("DP"))
						{
							Serenity.setSessionVariable("DPkey").to(Datalist.get(k).trim());
						}
						else
						{
							Serenity.setSessionVariable(typeofdata).to(Datalist.get(k).trim());	
						}
						
						//To retrive the Rawsavings for the given MP
						MongoDBUtils.GetDBValuesBasedonAggregation(MonGoDBQueries.FilterMongoDBQuery("RAW SAVINGS BASED ON "+typeofdata+""),"RAW SAVINGS BASED ON "+typeofdata+"");
						sRawSavings=Serenity.sessionVariableCalled("RawSavings").toString();

						
						if(typeofdata.equalsIgnoreCase("DP"))
						{
							Serenity.setSessionVariable("DPkey").to(Datalist.get(k+1).trim());
						}
						else
						{
							Serenity.setSessionVariable(typeofdata).to(Datalist.get(k+1).trim());	
						}
						
						//To retrive the Rawsavings for the given MP
						MongoDBUtils.GetDBValuesBasedonAggregation(MonGoDBQueries.FilterMongoDBQuery("RAW SAVINGS BASED ON "+typeofdata+""),"RAW SAVINGS BASED ON "+typeofdata+"");
						String SecondRawSavings=Serenity.sessionVariableCalled("RawSavings").toString();

						int compare = Double.valueOf(sRawSavings).compareTo(Double.valueOf(SecondRawSavings));

						if(compare==0||compare<0)
						{
							if(!sRawSavings.equalsIgnoreCase(SecondRawSavings))
							{
								Assert.assertTrue(""+typeofdata+" '"+Datalist.get(k).trim() + "," + Datalist.get(k + 1).trim()+"' are not displyed in Descending order by savings==>"+sRawSavings+","+SecondRawSavings, false);	
							}
							else
							{
								System.out.println(""+typeofdata+" '"+Datalist.get(k).trim() + "," + Datalist.get(k + 1).trim()+"' are displyed in Descending order by savings==>"+sRawSavings+","+SecondRawSavings);
							}
								
						}
						else
						{
							System.out.println(""+typeofdata+" '"+Datalist.get(k).trim() + "," + Datalist.get(k + 1).trim()+"' are displyed in Descending order by savings==>"+sRawSavings+","+SecondRawSavings);
						}
					}
					
				
				
			}

			
			
			private ArrayList<String> RetrieveTheDatafromAvaialbleDeck(String typeofData, String Sortoption) 
			{
			
				CPWPage oCPWPage=this.switchToPage(CPWPage.class);
				ArrayList<String> Datalist=new ArrayList<>();
				String sMedicapolicy=null;
				String sTopic=null;
				int DPsize=0;
				
				switch(typeofData)
				{
				case "MP":
				int MPsize=refSeleniumUtils.get_Matching_WebElement_count(StringUtils.replace(oCPWPage.labelcontainstext, "svalue", "Medical Policy:"));
				for (int j = 1; j <= MPsize; j++) 
				{
					String MP=StringUtils.substringBetween(refSeleniumUtils.get_TextFrom_Locator(StringUtils.replace(oCPWPage.labelcontainstext, "svalue", "Medical Policy:")+"["+j+"]"),":","DP(").trim();
					sMedicapolicy=StringUtils.substringBeforeLast(MP, "(").trim();
					String Topicsize=StringUtils.substringBetween(refSeleniumUtils.get_TextFrom_Locator(StringUtils.replace(oCPWPage.labelcontainstext, "svalue", "Medical Policy:")+"["+j+"]"),"DP(s) in","Topic(s)").trim();
					Datalist.add(sMedicapolicy);
					System.out.println(j+"."+sMedicapolicy);
					if(Serenity.sessionVariableCalled("ReqMedicalpolicy")==null)
					{
						if(Integer.valueOf(Topicsize)>2)
						{
							Serenity.setSessionVariable("ReqMedicalpolicy").to(sMedicapolicy);
							System.out.println("Medicapolicy with more than 2 topics==>"+sMedicapolicy);
						}
					}
					
					
				}
				int TopicSize=refSeleniumUtils.get_Matching_WebElement_count(StringUtils.replace(oCPWPage.labelcontainstext, "svalue", "Topic:"));
				for (int j = 1; j <= TopicSize; j++) 
				{
					String Topic=StringUtils.substringBetween(refSeleniumUtils.get_TextFrom_Locator(StringUtils.replace(oCPWPage.labelcontainstext, "svalue", "Topic:")+"["+j+"]"),":","DP(").trim();
					sTopic=StringUtils.substringBeforeLast(Topic, "(").trim();
					 DPsize=Integer.valueOf(StringUtils.substringAfterLast(Topic, "(").trim());
					if(DPsize>2)
					{
						Serenity.setSessionVariable("ReqTopic").to(sTopic);
						System.out.println("Topic with more than 2 DPs==>"+sTopic);
						System.out.println(Serenity.sessionVariableCalled("ReqTopic").toString());
						break;
					}
					
				}
				break;
				case "Topic":
					GenericUtils.Verify("clicking on Reset button", oGenericUtils.clickOnElement(StringUtils.replace(oCPWPage.ButtonContainsText, "value", "Reset")));
					GenericUtils.Verify("MP Dsplayed in filerdrawer==>"+Serenity.sessionVariableCalled("Medicalpolicy"),oCPWPage.Enter_the_given_MP_Topic_in_filter_Drawer(Serenity.sessionVariableCalled("ReqMedicalpolicy")));
					GenericUtils.Verify("clicking on MP Checkbox in filerdrawer==>"+Serenity.sessionVariableCalled("Medicalpolicy"),oGenericUtils.clickOnElement(StringUtils.replace(refFilterDrawer.Medicalpolicy_Checkbox, "value", Serenity.sessionVariableCalled("ReqMedicalpolicy"))));
					//click on 'Apply' button
					GenericUtils.Verify("clicking on the Applyfilters button",oGenericUtils.clickButton(By.xpath(refFilterDrawer.sBtn_Apply)));
					refSeleniumUtils.waitForContentLoad();
					GenericUtils.Verify("clicking on sortby dropdown", oGenericUtils.clickOnElement(SortbyDropdown));
					GenericUtils.Verify("clicking on sortby option '"+Sortoption+"' in dropdown", oGenericUtils.clickOnElement(StringUtils.replace(SortbyDropdownValues, "DropDownVal", Sortoption)));
					refSeleniumUtils.defaultWait(ProjectVariables.TImeout_3_Seconds);
					GenericUtils.Verify("clicking on Exapnd all link", oGenericUtils.clickOnElement(StringUtils.replace(oCPWPage.anchorTag_with_text, "value", "Expand All")));
					refSeleniumUtils.defaultWait(ProjectVariables.TImeout_2_Seconds);
					
					int Topicsize=refSeleniumUtils.get_Matching_WebElement_count(StringUtils.replace(oCPWPage.labelcontainstext, "svalue", "Topic:"));
					for (int j = 1; j <= Topicsize; j++) 
					{
						String MP=StringUtils.substringBetween(refSeleniumUtils.get_TextFrom_Locator(StringUtils.replace(oCPWPage.labelcontainstext, "svalue", "Topic:")+"["+j+"]"),":","DP(").trim();
						sTopic=StringUtils.substringBeforeLast(MP, "(").trim();
						Datalist.add(sTopic);
						System.out.println(j+"."+sTopic);
						
					}
				break;
				case "DP":
					GenericUtils.Verify("clicking on Reset button", oGenericUtils.clickOnElement(StringUtils.replace(oCPWPage.ButtonContainsText, "value", "Reset")));
					GenericUtils.Verify("Unable to see the Topic in filerdrawer==>"+Serenity.sessionVariableCalled("ReqTopic"),oCPWPage.Enter_the_given_MP_Topic_in_filter_Drawer(Serenity.sessionVariableCalled("ReqTopic")));
					GenericUtils.Verify("clicking on Topic Checkbox in filerdrawer==>"+Serenity.sessionVariableCalled("ReqTopic"),oGenericUtils.clickOnElement(StringUtils.replace(refFilterDrawer.Medicalpolicy_Checkbox, "value", Serenity.sessionVariableCalled("ReqTopic"))));
					//click on 'Apply' button
					GenericUtils.Verify("clicking on Applyfilters button",oGenericUtils.clickButton(By.xpath(refFilterDrawer.sBtn_Apply)));
					refSeleniumUtils.waitForContentLoad();
					GenericUtils.Verify("clicking on sortby dropdown", oGenericUtils.clickOnElement(SortbyDropdown));
					GenericUtils.Verify("clicking on sortby option '"+Sortoption+"' in dropdown", oGenericUtils.clickOnElement(StringUtils.replace(SortbyDropdownValues, "DropDownVal", Sortoption)));
					refSeleniumUtils.defaultWait(ProjectVariables.TImeout_3_Seconds);
					GenericUtils.Verify("clicking on Exapnd all link", oGenericUtils.clickOnElement(StringUtils.replace(oCPWPage.anchorTag_with_text, "value", "Expand All")));
					refSeleniumUtils.defaultWait(ProjectVariables.TImeout_2_Seconds);
					
					 DPsize=refSeleniumUtils.get_Matching_WebElement_count(oCPWPage.DPCards_in_AvailableOpportunity);
					for (int j = 1; j <= DPsize; j++) 
					{
						String DP=StringUtils.substringAfter(refSeleniumUtils.get_TextFrom_Locator(oCPWPage.DPCards_in_AvailableOpportunity+"["+j+"]"),"DP").trim();
						Datalist.add(DP);
						System.out.println(j+"."+DP);
						
					}
				break;
				case "DPs in Presentation":
					GenericUtils.Verify("clicking on Expand all link", oGenericUtils.clickOnElement("//u[contains(text(),'Expand All')]"));
					DPsize=refSeleniumUtils.get_Matching_WebElement_count(StringUtils.replace(oCPWPage.DPCards_in_Presentation, "topic", Serenity.sessionVariableCalled("ReqTopic")));
					for (int j = 1; j <= DPsize; j++) 
					{
						String DP=StringUtils.substringAfter(refSeleniumUtils.get_TextFrom_Locator(StringUtils.replace(oCPWPage.DPCards_in_Presentation, "topic", Serenity.sessionVariableCalled("ReqTopic"))+"["+j+"]"),"DP").trim();
						Datalist.add(DP);
						System.out.println(j+"."+DP);
						
					}
				break;
				default:
					Assert.assertTrue("Case not found===>"+typeofData, false);
				break;
				}
				
				return Datalist;
				
				
			}

			
			
			public void validateSortingfunctionalityinPresentationdeck(String sortbyOptions) {
				
				
				ArrayList<String> MPlist=new ArrayList<>();
				
				ArrayList<String> DPlist=new ArrayList<>();
				ArrayList<Long> DPLonglist=new ArrayList<>();
				List<String> SortbyOptionsList=Arrays.asList(sortbyOptions.split(","));
			
				for (int i = 0; i < SortbyOptionsList.size(); i++) 
				{
					GenericUtils.Verify("clicking on sortby dropdown", oGenericUtils.clickOnElement(SortbyDropdown));
					GenericUtils.Verify("clicking on sortby option '"+SortbyOptionsList.get(i)+"' in dropdown", oGenericUtils.clickOnElement(StringUtils.replace(SortbyDropdownValues, "DropDownVal", SortbyOptionsList.get(i).trim())));
					refSeleniumUtils.defaultWait(ProjectVariables.TImeout_3_Seconds);
					GenericUtils.Verify("clicking on Expand all link", oGenericUtils.clickOnElement("//u[contains(text(),'Expand All')]"));
					refSeleniumUtils.defaultWait(ProjectVariables.TImeout_2_Seconds);
					switch(SortbyOptionsList.get(i))
					{
					case "Alphanumeric":
						//Retrieve the MPs from Available Deck
						MPlist.addAll(RetrieveTheDatafromAvaialbleDeck("MP",SortbyOptionsList.get(i)));
						GenericUtils.validate_the_sorting_funtionality_String(MPlist, "Sort Ascending", "Medicalpolicies in PresentationDeck");
										
						//Retrieve the DPs from Available Deck
						DPlist.addAll(RetrieveTheDatafromAvaialbleDeck("DPs in Presentation",SortbyOptionsList.get(i)));
						for (int j = 0; j <DPlist.size(); j++) 
						{
							DPLonglist.add(Long.valueOf(DPlist.get(j)));
						}
						
						
						//Retrieve the Topics from Available Deck
						//DPlist.addAll(RetrieveTheDatafromAvaialbleDeck("DPs in Presentation",SortbyOptionsList.get(i)));
						GenericUtils.validate_the_sorting_funtionality_Integer(DPLonglist, "Sort Ascending", "DPs in PresentationDeck");
										
					break;
					
					case "Savings by Highest Dollar Policy":
						
						//Retrieve the MPs from Available Deck
						MPlist.addAll(RetrieveTheDatafromAvaialbleDeck("MP",SortbyOptionsList.get(i)));
						//Method to validate the sorting for the option 'savings'
						validateSortingbasedonSavingsinAvailabledeck(MPlist,"Medicalpolicy");
						
						
						//Retrieve the Topics from Available Deck
						DPlist.addAll(RetrieveTheDatafromAvaialbleDeck("DPs in Presentation",SortbyOptionsList.get(i)));
						//Method to validate the sorting for the option 'savings'
						validateSortingbasedonSavingsinAvailabledeck(DPlist,"DP");
										
					break;
					case "Priority":
						//Retrieve the MPs from Available Deck
						MPlist.addAll(RetrieveTheDatafromAvaialbleDeck("MP",SortbyOptionsList.get(i)));
						//Method to validate the sorting for the option 'Priority'
						validateTheSortbyPriorityforthegiven(MPlist,"Medicalpolicy");
						
						
						//Retrieve the Topics from Available Deck
						DPlist.addAll(RetrieveTheDatafromAvaialbleDeck("DPs in Presentation",SortbyOptionsList.get(i)));
						//Method to validate the sorting for the option 'Priority'
						validateTheSortbyPriorityforthegiven(DPlist,"DP");
						
					break;
					case "eLL Order":
						//Retrieve the MPs from Available Deck
						MPlist.addAll(RetrieveTheDatafromAvaialbleDeck("MP",SortbyOptionsList.get(i)));
						//Method to validate the sorting for the option 'Priority'
						validateTheSortbyeLlOrderforthegiven(MPlist,"Medicalpolicy");
						
						
						//Retrieve the Topics from Available Deck
						DPlist.addAll(RetrieveTheDatafromAvaialbleDeck("DPs in Presentation",SortbyOptionsList.get(i)));
						//Method to validate the sorting for the option 'Priority'
						validateTheSortbyeLlOrderforthegiven(DPlist,"DP");
					break;
					case "Savings by Highest Dollar DP":
						//Retrieve the MPs from Available Deck
						MPlist.addAll(RetrieveTheDatafromAvaialbleDeck("MP",SortbyOptionsList.get(i)));
						//Method to validate the sorting for the option 'Savings by DP'
						validateTheSortbySavingsDPforthegiven(MPlist,"Medicalpolicy");
						
						
						//Retrieve the Topics from Available Deck
						DPlist.addAll(RetrieveTheDatafromAvaialbleDeck("DPs in Presentation",SortbyOptionsList.get(i)));
						//Method to validate the sorting for the option 'Savings by DP'
						validateTheSortbySavingsDPforthegiven(DPlist,"DP");
						break;
					default:
						Assert.assertTrue("Case not found===>"+SortbyOptionsList.get(i), false);
					break;
					}
					
					MPlist.clear();
					DPlist.clear();
				}
				
			}	

}

 


