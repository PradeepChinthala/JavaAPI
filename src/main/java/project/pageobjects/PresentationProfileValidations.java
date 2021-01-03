package project.pageobjects;

import java.util.List;
import java.util.Random;

import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Assert;
import org.openqa.selenium.WebElement;

import net.serenitybdd.core.Serenity;
import net.serenitybdd.core.annotations.findby.By;
import net.serenitybdd.core.pages.PageObject;
import project.utilities.GenericUtils;
import project.utilities.MongoDBUtils;
import project.utilities.SeleniumUtils;
import project.variables.ProjectVariables;


public class PresentationProfileValidations extends PageObject {
	SeleniumUtils oSeleniumUtils;

	//Instance creation
	GenericUtils oGenericUtils=new GenericUtils();
	//Locators for Presentation section
	public String sCreatePP="//div[contains(text(),'Create Presentation Profile')]";
	public String sPresentationName="//input[@placeholder='Presentation Name']";
	public String sCancelEnable="//div[text()='Create Presentation Profile']/..//button[.='Okay']/..//following-sibling::jqxbutton//button[.='Cancel'][@role='button'][@aria-disabled='false']";
	public String sGoBackCancelEnable="//div[text()='Create Presentation Profile']/..//button[.='Go Back']/..//following-sibling::jqxbutton//button[.='Cancel'][@role='button'][@aria-disabled='false']";
	public String sOkDisable="//div[contains(text(),'Presentation Profile')]/..//button[.='Okay'][@role='button'][@aria-disabled='true']";
	public String sOkEnable="//div[contains(text(),'Presentation Profile')]/..//button[.='Okay'][@role='button'][@aria-disabled='false']";
	public String sSucessOk="//span[contains(text(),'Okay')]";
	public String sOutpatient="//div[contains(text(),'Outpatient')]";
	public String sThreshold="//div[contains(text(),'High')]";
	public String sNotes="(//div[contains(text(),'Note')])/..//textarea";
	public String sDatePicker="//*[contains(@class,'mat-datepicker-toggle-default')]";
	public String sConfirmation="//div[contains(text(),'Confirmation')]";
	public String sConfirmation_Yes="//span[@class='mat-button-wrapper'][contains(text(),'Yes')]";
	public String sConfirmation_No="//span[@class='mat-button-wrapper'][contains(text(),'No')]";
	public String sAvailableExpand="//mat-expansion-panel-header[contains(@class,'ng-star-inserted mat-expanded')]";
	public String sAvailableCollapse="(//mat-expansion-panel-header[contains(@class,'indicator-after ng-star-inserted')])[1]";
	public String sGoBack="//button[.='Go Back'][@role='button'][@aria-disabled='false']";
	public String sCancel="//div[contains(text(),' Are you sure you want to cancel? Changes will not be saved')]";
	public String sGetPres="//span[@class='pres_pro_name']";
	public String sDeletePres="//div[.='Delete Presentation'][last()]";
	public String sAvailOP="//span[contains(@class,'pres-tab-active')]//span[contains(text(),'NPP Opportunities')]/span";
	public String sEditPres="//button[@mattooltip='Edit Presentation Profile'][@cdk-describedby-host='']";
	public String sEditSearch="//button[@mattooltip='Search for DPs'][@cdk-describedby-host='']";
	public String sFilterOP="//button[@mattooltip='Filter Opportunities']";
	public String sHierarchy="//button[@mattooltip='View Presentation Hierarchy']";
	public String sFinalaze="//button[.='FINALIZE']";
	public String sAddicon="//button[@class='add-presentation']";
	public String sChangeOppPres="//span[contains(text(),'value')][contains(@class,'pres_pro_name')]";


	//===========================================================================================================================================>
	public void PresentationProfileValidations(String sValidation,String sPayershort,String sLOB) throws InterruptedException{
      SeleniumUtils oSeleniumUtils=this.switchToPage(SeleniumUtils.class);
      GenericUtils oGenericUtils=this.switchToPage(GenericUtils.class);
		String sPPName=null;
		//try{
			//Verify 'Presentation Deck' section
			//Click on '+' button
			System.out.println(getDriver().findElement(By.xpath("//button[@class='add-presentation']")).isDisplayed());
			Thread.sleep(1000);
			//oGenericUtils.getDriver().findElement(By.xpath("//mat-icon[text()= 'add']")).click();
			oSeleniumUtils.clickGivenXpath("//button[@class='add-presentation']");
			
			Thread.sleep(2000);
			//oGenericUtils.clickOnElement("mat-icon", "add");
			//Verify 'Presentation Name (Required)' screen
			oGenericUtils.isElementExist(sCreatePP);
			//Verify 'Cancel' button enabled
			oGenericUtils.isElementExist(sCancelEnable);
			//Verify 'OK ' button disabled
			Assert.assertTrue("okay button was not disabled,it should be disabled,withour entering pres name", oSeleniumUtils.is_WebElement_Displayed(sOkDisable));;
			
			//oGenericUtils.isElementExist(sOkDisable);
			//Enter Value
			Random randomGenerator = new Random();  
			int randomInt = randomGenerator.nextInt(1000);
			 sPPName="AutoTestPM"+randomInt;
			

			//PPreesentation Name with 35 character limit
			if(sValidation.equalsIgnoreCase("CREATE_35_CHAR")){
				 sPPName="AutoTest"+RandomStringUtils.randomAlphanumeric(30).toUpperCase();
			}

			Serenity.setSessionVariable("sPPName").to(sPPName);
			switch(sValidation.toUpperCase()){
			
			case "CREATE":
			case "CREATE_ALLFEILDS":
			case "CREATE_ALLFEILDS_CANCEL":	
			case "CREATE_35_CHAR":	
				//Enter Presentation Name under 'Presentation Name (Required)' section
				oGenericUtils.setValue(By.xpath(sPresentationName), sPPName);
			 	
				//==================Enter All fields===========================================>
				if(sValidation.equalsIgnoreCase("CREATE_ALLFEILDS") || sValidation.equalsIgnoreCase("CREATE_ALLFEILDS_CANCEL")){
						//Click 'DatePicker' icon
						  oGenericUtils.clickButton(By.xpath(sDatePicker));
						//Select Target date  				  
						 String sGetDay= oGenericUtils.SystemTime_in_the_given_format("d");
						 oGenericUtils.clickButton(By.xpath("//div[@class='mat-calendar-body-cell-content mat-calendar-body-selected mat-calendar-body-today'][contains(.,'"+sGetDay+"')]"));
						 //Select Payershort & LOB
						 //oGenericUtils.clickButton(By.xpath("(//div[@class='ft_lt']//*[contains(text(),'"+sPayershort+"')]/..//span)[1]/.."));
						 oGenericUtils.clickButton(By.xpath("(//div[@class='ft_lt']//*[contains(text(),'"+sLOB+"')]/..//span)[1]/.."));
						
						//Select 'Product item'
						oGenericUtils.clickButton(By.xpath(sOutpatient));
						//Select Priority Threshold
						oGenericUtils.clickButton(By.xpath(sThreshold));
						//Enter Notes
						oGenericUtils.setValue(By.xpath(sNotes), sPPName);
				}				
				//=============================================================================>
				//Verify 'OK' button Enabled
				oGenericUtils.isElementExist(sOkEnable);
				if(sValidation.equalsIgnoreCase("CREATE_ALLFEILDS_CANCEL")){
					//Click on 'Cancel' button
					oGenericUtils.clickButton(By.xpath(sCancelEnable));
					//Verify 'confirmation' POPUP message
					//oGenericUtils.isElementExist(sConfirmation);
					//Verify PopUp text message "Are you sure you want to cancel?"
					oGenericUtils.isElementExist(sCancel,10);
					//Click on 'No' button
					//oGenericUtils.clickButton(By.xpath(sConfirmation_No));
					//Verify 'GoBack'  message
					oGenericUtils.isElementExist(sGoBack);
					//Click on 'Cancel' button
					oGenericUtils.clickButton(By.xpath(sGoBackCancelEnable));
					Thread.sleep(2000);
				}else{
					//Click on 'OK' button
					oGenericUtils.clickButton(By.xpath(sOkEnable));
					
					Thread.sleep(3000);

					//Verify Newly created Presentation profile under PP deck
					if(!sValidation.equalsIgnoreCase("CREATE_35_CHAR")){
						oGenericUtils.isElementExist("//span[contains(text(),'"+sPPName+"')]");
					}
				}	
				break;
			case "CANCEL":
				//Click on 'Cancel' button
				oGenericUtils.clickButton(By.xpath(sCancelEnable));
				Thread.sleep(2000);
				
				break;
			case "VERIFY_PAYERSHORTS_LOB_DATA":
				validatePayershortData(sPayershort,sLOB);
				oGenericUtils.clickButton(By.xpath(sCancelEnable));
				break;
			case "CREATE_CLICK":
				oGenericUtils.setValue(By.xpath(sPresentationName), sPPName);
				//Click on 'OK' button
				oGenericUtils.clickButton(By.xpath(sOkEnable));				
				Thread.sleep(5000);
				
				//Verify 'Available Opportunities tab ' is active
				//oGenericUtils.isElementExist(sAvailOP);
				//Select Newly created presentation profile
				oGenericUtils.clickButton(By.xpath("//span[contains(text(),'"+sPPName+"')]"));
				//Verify Selected Presentation highlighted
				oGenericUtils.isElementExist("//span[contains(@class,'pres-tab-active')]//span[contains(text(),'"+sPPName+"')]");
				//Search  and Edit icon fields are enable
				oGenericUtils.isElementExist(sEditPres);
				oGenericUtils.isElementExist(sEditSearch);
				//Filter,Presentation Hierarchy,FINALIZE buttons are disabled
				if(!getDriver().findElement(By.xpath(sFilterOP)).isEnabled() && !getDriver().findElement(By.xpath(sHierarchy)).isEnabled() && !getDriver().findElement(By.xpath(sFinalaze)).isEnabled()){
					GenericUtils.Verify("Sucessfully verified Filter,Presentation Hierarchy,FINALIZE buttons are disabled","PASSED");
				}else{
					GenericUtils.Verify("Filter,Presentation Hierarchy,FINALIZE buttons are enabled","FAILED");
				};
				
				break;
			
			}
			
			//Validation of Presentation Name with 35 character limit(New Presentation Name)
			if(sValidation.equalsIgnoreCase("CREATE_35_CHAR")){
				List<WebElement> sPPList=getDriver().findElements(By.xpath(sGetPres));
				if(sPPList.size()>0){
					String sGetPPName=sPPList.get(0).getText();
					int sPresentationLen=sGetPPName.replace("...", "").trim().length();
					if(sPresentationLen==35){
						GenericUtils.Verify("Presentattion count matched :=35","PASSED");
					}else{
						GenericUtils.Verify("Presentattion count not matched with 35:::"+sPresentationLen,"FAILED");
					}
				}	
			}
			
		//}catch(Exception e){
			//GenericUtils.Verify("Object not found , Failed due to :="+e.getMessage(),"FAILED");
		//}
	}
	//==================================Edit_Cancel Presentation profile validations==========================================================>
	public void Edit_Delete_PresentationProfile(String sValidation){
		try{
			
			switch(sValidation.toUpperCase()){
				
			case "EDIT":
			case  "EDIT_35_CHAR":	
					Random randomGenerator = new Random();  
					int randomInt = randomGenerator.nextInt(1000);
					String sPPNameEdit="Auto Test Notes Edit "+randomInt;
					
					List<WebElement> sPPList=getDriver().findElements(By.xpath(sGetPres));
					if(sPPList.size()>0){
					
						//Verify 'Presentation Profile' name
						String sPPName=sPPList.get(0).getText();
						//Select Presentation profile
						oGenericUtils.clickButton(By.xpath("//span[contains(text(),'"+sPPName+"')]"));
						//Click on 'Edit icon'
						oGenericUtils.clickButton(By.xpath("//button[@mattooltip='Edit Presentation Profile']"));
						//Condition for 35 characters for Presentation Name
						if(sValidation.equalsIgnoreCase("EDIT_35_CHAR")){
							sPPNameEdit="AutoTest"+RandomStringUtils.randomAlphanumeric(30).toUpperCase();
							oGenericUtils.setValue(By.xpath(sPresentationName), sPPNameEdit);
						}
						
						oGenericUtils.clickButton(By.xpath(sOutpatient));
						//Select Priority Threshold
						oGenericUtils.clickButton(By.xpath(sThreshold));
						//Enter Notes
						oGenericUtils.setValue(By.xpath(sNotes), sPPNameEdit);
						//Click on 'OK' button
						oGenericUtils.clickButton(By.xpath(sOkEnable));
						Thread.sleep(3000);
					}else{
						GenericUtils.Verify("No Records for Presentation Profile","FAILED");	
					}	
				break;
				case "DELETE":
					List<WebElement> sPPDelete=getDriver().findElements(By.xpath(sGetPres));
					if(sPPDelete.size()>0){
						String sPPName=sPPDelete.get(0).getText();
						//Verify 'Presentation Profile' name
						String sGetText=getDriver().findElement(By.xpath(sPresentationName)).getAttribute("value");
						//Click on 'Edit icon'
						oGenericUtils.clickButton(By.xpath("(//mat-icon[text()='clear'])[1]"));						
						//Verify 'Delete Presentation.' message
						oGenericUtils.isElementExist(sDeletePres);
						Thread.sleep(3000);
						//Click on 'OK' button
						oGenericUtils.clickButton(By.xpath("//span[@class='mat-button-wrapper'][.='Yes']"));
						Thread.sleep(2000);
						//Verify After deleted records count
						List<WebElement> sPPAfterDelete=getDriver().findElements(By.xpath(sGetPres));
						if(sPPDelete.size()!=sPPAfterDelete.size()){
							GenericUtils.Verify("Presentation records deleted sucessfully:="+sPPDelete.size()+":::"+sPPAfterDelete.size(),"PASSED");	
						}else{
							GenericUtils.Verify("Failed to delete Presentation records "+sPPDelete.size()+":::"+sPPAfterDelete.size(),"FAILED");	
						}
					}else{
						GenericUtils.Verify("No Records for Presentation Profil","FAILED");	
					}	
					break;
			}
			
			//Validation of Presentation Name with 35 character limit(Edit Presentation Name)
			if(sValidation.equalsIgnoreCase("EDIT_35_CHAR")){
				List<WebElement> sPPList=getDriver().findElements(By.xpath(sGetPres));
				if(sPPList.size()>0){
					String sGetPPName=sPPList.get(0).getText();
					int sPresentationLen=sGetPPName.replace("...", "").trim().length();
					if(sPresentationLen==35){
						GenericUtils.Verify("Presentattion count matched :=35","PASSED");
					}else{
						GenericUtils.Verify("Presentattion count not matched with 35:::"+sPresentationLen,"FAILED");
					}
				}	
			}
			
		}catch(Exception e){
			GenericUtils.Verify("Object not found , Failed due to :="+e.getMessage(),"FAILED");
		}
	}
	
	//===================================Validate payershort data based on selected client========================================================>
	public void validatePayershortData(String sClientName,String sFieldName){
			boolean blnResult=false;
		try{
		MongoDBUtils.Get_the_distinct_values_based_on_given(sClientName,sFieldName);
		 System.out.println("Total Payershort items:==>"+ProjectVariables.sGetDBList.size());
		 int sTotalItems=ProjectVariables.sGetDBList.size();
		 for(int i=0;i<sTotalItems;i++){
			 String sGetItem=ProjectVariables.sGetDBList.get(i);
			 String sPayerLOB="//div[@class='Payer_block']//span[.='"+sGetItem+"']|//div[@class='ft_lt ']//span[.='"+sGetItem+"']";
			 List<WebElement> sList=getDriver().findElements(By.xpath(sPayerLOB));
			 if(sList.size()>0 ){
				 oGenericUtils.isElementExist(sPayerLOB);
				 blnResult=true;
				 
			 }
			//Verify Flag status
			 if(!blnResult){
				 GenericUtils.Verify("UI and MongoDB values not matched=","FAILED");
			 }
			 
		 }
	}catch(Exception e){
		GenericUtils.Verify("Object not found , Failed due to :="+e.getMessage(),"FAILED");
	}	
	}
 //==================================Presentation Profile deck closed===========================================================================>
	public void presentationdeck_closemark_closeicon(){
		try{
			//Verify 'Available Deck ' expanded status
			boolean sAvialbeStatus=oGenericUtils.isElementExist(sAvailableExpand, 5);
			if(sAvialbeStatus){
				oGenericUtils.clickButton(By.xpath(sAvailableExpand));
			}
			//Verify 'Available' collapse item
			oGenericUtils.isElementExist(sAvailableCollapse);
			//Click on any 'Presentation' item
			List<WebElement> sList=getDriver().findElements(By.xpath(sGetPres));
			if(sList.size()>0){
				String sGetPresentation=sList.get(0).getText();
				oGenericUtils.clickButton(By.xpath("//label[@id='lblName']"));
				//Verify Presentation displayed under Available deck
				oGenericUtils.isElementExist("//div[contains(text(),'"+sGetPresentation+"')]");
				//Verify 'X' icon
				oGenericUtils.isElementExist("//div[contains(text(),'"+sGetPresentation+"')]/..//span[.='X']");
				//Click on 'X' icon then presentation deck will close
				oGenericUtils.clickButton(By.xpath("//div[contains(text(),'"+sGetPresentation+"')]/..//span[.='X']"));
			}else{
				GenericUtils.Verify("No Presentaions found under presentation deck","FAILED");
			}
			
		}catch(Exception e){
			GenericUtils.Verify("Object not found , Failed due to :="+e.getMessage(),"FAILED");
		}
	}
//==============================================================================================================================================>	
 //====================================Verify DP data===========================================================================================>
	public void verifyTopicDPData(String sDPKey,String sInsuranceStatus,String sValidation){
		
		try{
			//mat-card//label[.='DP 11157']/ancestor::mat-card//div[@id='text'][contains(text(),'MCD')]
			String sStatus=null;
			boolean bstatus=false;
			
			switch(sValidation.toUpperCase()){
			
				case "SUPPRESS":
					
					 sStatus="//mat-card//label[.='DP "+sDPKey+"']/ancestor::mat-card//div[contains(text(),'"+sInsuranceStatus+"')]/..//i[@class='fa fa-caret-down down arrow ng-star-inserted'][contains(@title,'Suppress')]";
					break;
				case "APPROVE LIBRARY":
					
					 sStatus="//mat-card//label[.='DP "+sDPKey+"']/ancestor::mat-card//div[contains(text(),'"+sInsuranceStatus+"')]/..//i[@class='fa fa-caret-up up arrow ng-star-inserted'][contains(@title,'Approve Library')]";
					break;
				case "APPROVE WITH MODIFICATION":
					 sStatus="//mat-card//label[.='DP "+sDPKey+"']/ancestor::mat-card//div[contains(text(),'"+sInsuranceStatus+"')]/..//i[@class='fa fa-caret-up up arrow ng-star-inserted'][contains(@title,'Approve With Modifications')]";
					break;
				case "APPROVE":
					 sStatus="//mat-card//label[.='DP "+sDPKey+"']/ancestor::mat-card//div[contains(text(),'"+sInsuranceStatus+"')]/..//i[@class='fa fa-caret-up up arrow ng-star-inserted'][contains(@title,'Approve')]";
					break;
				case "REJECT":
					 sStatus="//mat-card//label[.='DP "+sDPKey+"']/ancestor::mat-card//div[contains(text(),'"+sInsuranceStatus+"')]/..//i[@class='fa fa-caret-down down arrow ng-star-inserted'][contains(@title,'Reject')]";
					break;
				case "NO DECISION":
					 sStatus="//mat-card//label[.='DP "+sDPKey+"']/ancestor::mat-card//div[contains(text(),'"+sInsuranceStatus+"')]";
					break;
				
				
			}
			//Verify Insurance status for respective DP Key
			bstatus=oSeleniumUtils.is_WebElement_Displayed(sStatus);
			
			GenericUtils.Verify("Captured DPKey with latestClientdecision on DPCard,DPkey=>"+sDPKey+",Topic=>"+Serenity.sessionVariableCalled("Topic")+",latestClientdecision==>"+sInsuranceStatus, bstatus);
			
			
		}catch(Exception e){
			GenericUtils.Verify("Object not found , Failed due to :="+e.getMessage(),"FAILED");
		}
	
	}
   //=============================Verify captured DPs data under Avaialable deck==============================================================>
	public void verifycapturedDPsData(){
		try{
			// Serenity.setSessionVariable("DPKey").to(DPKey);
			String sGetDPData=Serenity.sessionVariableCalled("DPKey").toString();
			String[] sDPKey=sGetDPData.split(",");
			for (int i=0;i<=sDPKey.length;i++){
				 String sStatus="//mat-card//label[.='DP "+sDPKey[i]+"']";
				//Verify captured Disposition data under Available deck
					oGenericUtils.isElementExist(sStatus);
			}
			
		}catch(Exception e){
			GenericUtils.Verify("Object not found , Failed due to :="+e.getMessage(),"FAILED");
		}
	}
	
	public void createNewpresentationInChangeOpportunities(String sPresentationname)
	{
		SeleniumUtils oSeleniumUtils=this.switchToPage(SeleniumUtils.class);
		GenericUtils oGenericUtils=this.switchToPage(GenericUtils.class);
		oGenericUtils.isElementExist(sAddicon);
		oGenericUtils.clickOnElement(sAddicon);
		oSeleniumUtils.defaultWait(ProjectVariables.TImeout_3_Seconds);
		//Verify 'Presentation Name (Required)' screen
		oGenericUtils.isElementExist(sCreatePP);
		//Verify 'Cancel' button enabled
		oGenericUtils.isElementExist(sCancelEnable);
		//Verify 'OK ' button disabled
		Assert.assertTrue("okay button was not disabled,it should be disabled,withour entering pres name", oSeleniumUtils.is_WebElement_Displayed(sOkDisable));;
		//Enter Presentation Name under 'Presentation Name (Required)' section
		oGenericUtils.setValue(By.xpath(sPresentationName), sPresentationname);
		//Verify 'OK' button Enabled
		oGenericUtils.isElementExist(sOkEnable);
		//Click on 'OK' button
		oGenericUtils.clickButton(By.xpath(sOkEnable));
		oSeleniumUtils.waitForContentLoad();
		
	 	
	}
}
