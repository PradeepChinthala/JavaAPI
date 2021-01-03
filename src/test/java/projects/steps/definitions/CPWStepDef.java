package projects.steps.definitions;



import java.io.IOException;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang3.StringUtils;
import org.bson.Document;
import org.junit.Assert;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;

import com.mongodb.client.AggregateIterable;
import com.sun.xml.bind.v2.schemagen.xmlschema.Occurs;

import net.serenitybdd.core.Serenity;
import net.thucydides.core.annotations.Step;
import net.thucydides.core.steps.ScenarioSteps;
import project.pageobjects.CPWPage;
import project.pageobjects.FilterDrawer;
import project.pageobjects.OppurtunityDeck;
import project.pageobjects.PresentationProfileValidations;
import project.utilities.ExcelUtils;
import project.utilities.GenericUtils;
import project.utilities.MongoDBUtils;
import project.utilities.SeleniumUtils;
import project.variables.MonGoDBQueries;
import project.variables.ProjectVariables;

public class CPWStepDef extends ScenarioSteps {

	OppurtunityDeck oOppurtunityDeck;
	PresentationProfileValidations  oPresentationProfile;
	CPWPage oCPWPage;
	SeleniumUtils objSeleniumUtils;
	GenericUtils oGenericUtils;
	FilterDrawer oFilterDrawer;



	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Step
	public void the_is_logged_into_the_CPW_application(String arg1) throws Throwable {
		oCPWPage.Login_CPW(arg1);			
	}

	@Step
	public void User_click_on_client_with_release_in_the_Opportunity_Dashboard(String client, String release) throws IOException {

		//To retrieve the client key from the given client
		String sClientkey=CPWPage.RetrieveTheClientkeyfromgivenClientthroughservice(client);
		
		String exactRelese=StringUtils.substringAfter(release, " ")+GenericUtils.RetrivetheExactMonth(release);
		Serenity.setSessionVariable("client").to(client);
		Serenity.setSessionVariable("release").to(exactRelese);
		Serenity.setSessionVariable("clientkey").to(sClientkey);

		oCPWPage.SelectPayer(release, client);
	}



	//==================================================================================================================================================//

	@Step
	public void user_select_Medical_Policy_from_the_policy_selection_through_MongoDB() throws Throwable {

		String DBMedicalpolicy =MongoDBUtils.Retrieve_the_medicalpolicy_based_on_client_and_release(Serenity.sessionVariableCalled("clientkey"), Serenity.sessionVariableCalled("release"), "");

		if(DBMedicalpolicy==null)
		{
			Assert.assertTrue("Retrieved Medical policy is 'null' from the mongoDB for the client ===>"+Serenity.sessionVariableCalled("client")+",Release ===>"+Serenity.sessionVariableCalled("release"), false);
		}

		Serenity.setSessionVariable("Medicalpolicy").to(DBMedicalpolicy);

		oCPWPage.SelectPolicySelectionAndApplyFilters(DBMedicalpolicy);

		Assert.assertFalse("'No results found that meet the search criteria.' message is displayed in the AWB Grid for the Medical policy ==>"+DBMedicalpolicy,objSeleniumUtils.is_WebElement_Displayed(StringUtils.replace(oCPWPage.Div_contains_text, "value", "No results found that meet the search criteria.")));
	}

	@Step
	public void Validate_the_capture_and_update_disposition_functionality_with_MongoDB_for(String gridcriteria, String criteriatype, String disposition,String updateddisposition) throws InterruptedException {

		oCPWPage.capture_the_data_at_DP_level(criteriatype,disposition,updateddisposition);				
	}
	@Step
	public void logout_CPW_application() throws Throwable {
		oCPWPage.Logout_CPW();
	}


	@Step
	public void validateHomePage() 
	{
		oCPWPage.DynamicWaitfortheLoadingIconWithCount(20);

		if( objSeleniumUtils.is_WebElement_Displayed(oCPWPage.CPWOppRunsLabel))
		{
			Assert.assertTrue("CPW HomePage displayed",true);
		}
		else
		{
			Assert.assertTrue("CPW HomePage not  displayed",false);
		}
	}
	@Step
	public void user_retriev_opportunity_data_from_MongoDB(String arg1, String arg2) throws Throwable {

		MongoDBUtils.GetDBValuesBasedonAggregation(MonGoDBQueries.FilterMongoDBQuery(arg1),arg2);


	}	


	@Step
	public void user_select_Medical_Policy_from_the_policy_selection_through_MongoDB_for_the_given_latest_client_decision(String arg1) throws Throwable {


		// MongoDBUtils.GetGroupByDPKeyValues(oMonGoDBQueries.FilterMongoDBQuery(arg1));

		oCPWPage.SelectPolicySelectionAndApplyFilters(Serenity.sessionVariableCalled("Medicalpolicy"));

		Assert.assertTrue("Prior Disposition filterhead checkbox is unable to checked in the AWB Page",oCPWPage.ApplyFilters("Prior Disposition","","CHECK",""));
		Assert.assertTrue("Savings Status filterhead checkbox is unable to checked in the AWB Page",oCPWPage.ApplyFilters("Savings Status","","UNCHECK",""));
		Assert.assertTrue("Opportunity Savings Status checkbox is unable to checked in the AWB Page",oCPWPage.ApplyFilters("Savings Status","Opportunity","CHECK",StringUtils.replace(oCPWPage.ApplyResetfiltersbutton, "value", "Apply Filters")));

		Assert.assertTrue("Latest Client Decision filterhead checkbox is unable to checked in the AWB Page",oCPWPage.ApplyFilters("Latest Client Decision","","UNCHECK",""));
		Assert.assertTrue(arg1 +"::filterhead checkbox is unable to checked in the AWB Page",oCPWPage.ApplyFilters("Latest Client Decision",arg1,"CHECK",StringUtils.replace(oCPWPage.ApplyResetfiltersbutton, "value", "Apply Filters")));

		//Enter DP key on Search item
		Assert.assertTrue("Unable to enter the DP Key in the search field of AWB,DP Key ===>"+Serenity.sessionVariableCalled("DPKey"), objSeleniumUtils.Enter_given_Text_Element(oCPWPage.sSearchField_AWB,Serenity.sessionVariableCalled("DPKey")));

		Assert.assertTrue("Unable to click the search icon beside search box in the AWB page", objSeleniumUtils.clickGivenXpath(oCPWPage.AWBgriSearchbutton));

		objSeleniumUtils.defaultWait(ProjectVariables.TImeout_2_Seconds);

		boolean bstatus=objSeleniumUtils.is_WebElement_Displayed(StringUtils.replace(oCPWPage.ButtonContainsText, "value",Serenity.sessionVariableCalled("DPKey")));




	}

	public void user_selects(String arg1, String arg2, String arg3, String arg4) 
	{
		
		Assert.assertTrue("Unable to click the given filters in the given page ==>"+arg4, oCPWPage.ApplyFilters(arg2,arg1,arg3,""));
		
		Assert.assertTrue("Unable to click the Apply filters button in the given page ==>"+arg4, objSeleniumUtils.clickGivenXpath(StringUtils.replace(oCPWPage.ApplyResetfiltersbutton, "value", "Apply Filters")));

	}

	@Step
	public void validate_the_work_todo_count_on_the_count_with_DB() {

		System.out.println(Serenity.sessionVariableCalled("DPkey").toString());
		String value = Serenity.sessionVariableCalled("DPkey").toString().replace("[", "");
		String DpKey = value.replace("]", "");

		Serenity.setSessionVariable("DPkey").to(DpKey);

		oCPWPage.validate_the_work_todo_count_on_the_given_DPCard(Serenity.sessionVariableCalled("DPkey"));


	}

	//==================================================================================================================================================//

	@Step
	public void Move_the_data_from_CPW_to_PM_through_the_service(String User,String Client, String DPkeyCriteria) throws IOException 
	{
		Serenity.setSessionVariable("user").to(User);
		Serenity.setSessionVariable("client").to(Client);
		boolean bStatus=false;

		//To retrieve the client key from the given client
		String sClientkey=CPWPage.RetrieveTheClientkeyfromgivenClientthroughservice(Client);


		if(DPkeyCriteria.contains("Same RVA")||DPkeyCriteria.contains("Single-")||DPkeyCriteria.contains("Single Multiple LOB-")||DPkeyCriteria.contains("Single LOB-")||DPkeyCriteria.equalsIgnoreCase("Present"))
		{
			//Method to capture the disposition through service from MongoDB
			oCPWPage.Capture_the_disposition_through_service_from_MongoDBData(sClientkey,DPkeyCriteria,User);

		}
		else
		{
			bStatus=MongoDBUtils.GetAvailableDPKeyfromAvailableOpportunityDeck(sClientkey, DPkeyCriteria, 2, 1, 1);
			if(!bStatus)
			{
				//Method to capture the disposition through service from MongoDB
				oCPWPage.Capture_the_disposition_through_service_from_MongoDBData(sClientkey,DPkeyCriteria,User);

			}
		}

	}  


	@Step
	public void accessCPMInNewTab()
	{
		((JavascriptExecutor) getDriver()).executeScript("window.open()");
		ArrayList<String> tabs = new ArrayList<String>(getDriver().getWindowHandles());
		getDriver().switchTo().window(tabs.get(1)).get("https://cpd-qa.cotiviti.com/");
		try{
			oCPWPage.DynamicWaitfortheLoadingIconWithCount(20);
			System.out.println(getDriver().getTitle());
			if(getDriver().getTitle().equalsIgnoreCase("CPW"))
			{
				Assert.assertTrue("Redirection to CPD page is successful in new window", true);
				oCPWPage.DynamicWaitfortheLoadingIconWithCount(60);

				if(objSeleniumUtils.is_WebElement_Displayed(CPWPage.sGeneric09))
				{
					Assert.assertTrue("Opportunity Dashboard Page is displayed after redirecting to CPW Page", true);
				}else{
					Assert.assertTrue("Opportunity Dashboard Page is not displayed after redirecting to CPW Page", false);	
				}
			}else{
				Assert.assertTrue("Redirection to CPD page is not successful in new window", false); 
			}


		}catch(Exception e){
			GenericUtils.Verify("Object not clicked Successfully , Failed due to :="+e.getMessage(),"FAILED");

		}
	}

	//==================================================================================================================================================//

	public void Move_the_data_from_CPW_to_PM_through_the_service_for_the_given_priority(String user,String Client, String DPkeyCriteria,String Priority) throws IOException {

		String Requestbody=null;
		List<String> PriorityList=Arrays.asList(Priority.split(","));

		Serenity.setSessionVariable("user").to(user);
		Serenity.setSessionVariable("Disposition").to("Present");

		//To retrieve the client key from the given client
		String sClientkey=CPWPage.RetrieveTheClientkeyfromgivenClientthroughservice(Client);
		Serenity.setSessionVariable("client").to(Client);
		
		//Method to retrieve the data from DB based on the client
		MongoDBUtils.DPKeysWithNoDispositionBasedOnClient(sClientkey, DPkeyCriteria);

		ProjectVariables.DataVersion=Serenity.sessionVariableCalled("release");

		//Single DPKey,Single Priority
		if(!Priority.contains(",")&&DPkeyCriteria.equalsIgnoreCase("Single"))
		{
			ProjectVariables.CapturedDPkey=Long.valueOf(Serenity.sessionVariableCalled("DPkey"));
			//Requestbody
			//Requestbody ="{\"data_version\":\""+Serenity.sessionVariableCalled("release")+"\",\"disposition\":\"Present\",\"decision_points\":[{\"decision_point_id\":"+Serenity.sessionVariableCalled("DPkey")+",\"payer_ids\":["+Serenity.sessionVariableCalled("Payerkeys")+"],\"lob_ids\":[1,2,3,7,8,9],\"claim_type_ids\":[\"A\",\"F\",\"P\",\"I\",\"O\",\"S\"]}],\"reasons\":[\"High savings\",\"Business Reason\",\"Client Requested\"],\"do_not_present_until_next_run\":false,\"operation\":\"insert\",\"userId\":\""+user+"@ihtech.com\",\"userName\":\""+user+"\",\"client_key\":"+Serenity.sessionVariableCalled("clientkey")+",\"priority\":\""+Priority+"\"}\r\n";
			
			Requestbody = "{\r\n" +
					"	\"client_key\":"+Serenity.sessionVariableCalled("clientkey")+",\r\n" +
					"	\"clientDesc\": \""+Serenity.sessionVariableCalled("client")+"\",\r\n" +
					"	\"decision_points\":[\r\n" +
					"		{\"decision_point_id\":"+Serenity.sessionVariableCalled("DPkey")+",\r\n" +
					"		\"payerPolicySet\":[],\r\n" +
					"		\"opptySource\":\"rva\",\"payer_ids\":["+Serenity.sessionVariableCalled("Payerkeys")+"],\r\n" +
					"		\"lob_ids\":[1,2,3,7,8,9],\r\n" +
					"		\"claim_type_ids\":[\"A\",\"F\",\"P\",\"I\",\"O\",\"S\"]}],\r\n" +
					"	\"do_not_present_until_next_run\":false,\r\n" +
					"	\"operation\":\"insert\",\r\n" +
					"	\"userId\":\""+user+"\",\r\n" +
					"	\"userName\":\""+user+"@ihtech.com\",\r\n" +
					"	\"disposition\":\"Present\",\r\n" +
					"	\"page_id\":\"Analysis\",\r\n" +
					"	\"note\":\"\",\r\n" +
					"	\"reasons\":[\"High savings\",\"Business Reason\",\"Client Requested\"],\r\n" +
					"	\"priority\":\""+Priority+"\"\r\n" +
					"}\r\n";
			
			//Method to capture the disposition from CPW through service
			oCPWPage.Capture_the_disposition_through_service(Requestbody);
		}
		//Multiple DPKey's,Single Priority
		else if(!Priority.contains(",")&&DPkeyCriteria.equalsIgnoreCase("Multiple"))
		{
			List<String> DPKeysList=Arrays.asList(Serenity.sessionVariableCalled("DPkey").toString().split(","));

			for (int i = 0; i < DPKeysList.size(); i++)
			{
				ProjectVariables.CapturedDPkey=Long.valueOf(DPKeysList.get(i).trim());
				//Requestbody
				//Requestbody = "{\"data_version\":\""+Serenity.sessionVariableCalled("release")+"\",\"disposition\":\"Present\",\"decision_points\":[{\"decision_point_id\":"+DPKeysList.get(i).trim()+",\"payer_ids\":["+Serenity.sessionVariableCalled("Payerkeys")+"],\"lob_ids\":[1,2,3,7,8,9],\"claim_type_ids\":[\"A\",\"F\",\"P\",\"I\",\"O\",\"S\"]}],\"reasons\":[\"High savings\",\"Business Reason\",\"Client Requested\"],\"do_not_present_until_next_run\":false,\"operation\":\"insert\",\"userId\":\""+user+"@ihtech.com\",\"userName\":\""+user+"\",\"client_key\":"+Serenity.sessionVariableCalled("clientkey")+",\"priority\":\""+Priority.trim()+"\"}\r\n";
				Requestbody = "{\r\n" +
						"	\"client_key\":"+Serenity.sessionVariableCalled("clientkey")+",\r\n" +
						"	\"clientDesc\": \""+Serenity.sessionVariableCalled("client")+"\",\r\n" +
						"	\"decision_points\":[\r\n" +
						"		{\"decision_point_id\":"+DPKeysList.get(i).trim()+",\r\n" +
						"		\"payerPolicySet\":[],\r\n" +
						"		\"opptySource\":\"rva\",\"payer_ids\":["+Serenity.sessionVariableCalled("Payerkeys")+"],\r\n" +
						"		\"lob_ids\":[1,2,3,7,8,9],\r\n" +
						"		\"claim_type_ids\":[\"A\",\"F\",\"P\",\"I\",\"O\",\"S\"]}],\r\n" +
						"	\"do_not_present_until_next_run\":false,\r\n" +
						"	\"operation\":\"insert\",\r\n" +
						"	\"userId\":\""+user+"\",\r\n" +
						"	\"userName\":\""+user+"@ihtech.com\",\r\n" +
						"	\"disposition\":\"Present\",\r\n" +
						"	\"page_id\":\"Analysis\",\r\n" +
						"	\"note\":\"\",\r\n" +
						"	\"reasons\":[\"High savings\",\"Business Reason\",\"Client Requested\"],\r\n" +
						"	\"priority\":\""+Priority+"\"\r\n" +
						"}\r\n";
				//Method to capture the disposition from CPW through service
				oCPWPage.Capture_the_disposition_through_service(Requestbody);
			}
		}
		//Single DPKey,Multiple Priorities
		else if(Priority.contains(",")&&DPkeyCriteria.equalsIgnoreCase("Single"))
		{
			List<String> PayerKeysList=Arrays.asList(Serenity.sessionVariableCalled("Payerkeys").toString().split(","));

			for (int i = 0; i < PriorityList.size(); i++)
			{
				ProjectVariables.CapturedDPkey=Long.valueOf(Serenity.sessionVariableCalled("DPkey"));
				//Requestbody
				//Requestbody = "{\"data_version\":\""+Serenity.sessionVariableCalled("release")+"\",\"disposition\":\"Present\",\"decision_points\":[{\"decision_point_id\":"+Serenity.sessionVariableCalled("DPkey")+",\"payer_ids\":["+PayerKeysList.get(i).trim()+"],\"lob_ids\":[1,2,3,7,8,9],\"claim_type_ids\":[\"A\",\"F\",\"P\",\"I\",\"O\",\"S\"]}],\"reasons\":[\"High savings\",\"Business Reason\",\"Client Requested\"],\"do_not_present_until_next_run\":false,\"operation\":\"insert\",\"userId\":\""+user+"@ihtech.com\",\"userName\":\""+user+"\",\"client_key\":"+Serenity.sessionVariableCalled("clientkey")+",\"priority\":\""+PriorityList.get(i).trim()+"\"}\r\n";
				Requestbody = "{\r\n" +
						"	\"client_key\":"+Serenity.sessionVariableCalled("clientkey")+",\r\n" +
						"	\"clientDesc\": \""+Serenity.sessionVariableCalled("client")+"\",\r\n" +
						"	\"decision_points\":[\r\n" +
						"		{\"decision_point_id\":"+Serenity.sessionVariableCalled("DPkey")+",\r\n" +
						"		\"payerPolicySet\":[],\r\n" +
						"		\"opptySource\":\"rva\",\"payer_ids\":["+Serenity.sessionVariableCalled("Payerkeys")+"],\r\n" +
						"		\"lob_ids\":[1,2,3,7,8,9],\r\n" +
						"		\"claim_type_ids\":[\"A\",\"F\",\"P\",\"I\",\"O\",\"S\"]}],\r\n" +
						"	\"do_not_present_until_next_run\":false,\r\n" +
						"	\"operation\":\"insert\",\r\n" +
						"	\"userId\":\""+user+"\",\r\n" +
						"	\"userName\":\""+user+"@ihtech.com\",\r\n" +
						"	\"disposition\":\"Present\",\r\n" +
						"	\"page_id\":\"Analysis\",\r\n" +
						"	\"note\":\"\",\r\n" +
						"	\"reasons\":[\"High savings\",\"Business Reason\",\"Client Requested\"],\r\n" +
						"	\"priority\":\""+PriorityList.get(i).trim()+"\"\r\n" +
						"}\r\n";
				//Method to capture the disposition from CPW through service
				oCPWPage.Capture_the_disposition_through_service(Requestbody);
			}
		}
		//Multiple DPKeys,Multiple Priorities
		else if(Priority.contains(",")&&DPkeyCriteria.equalsIgnoreCase("Multiple"))
		{
			List<String> DPKeysList=Arrays.asList(Serenity.sessionVariableCalled("DPkey").toString().split(","));



			for (int i = 0; i < PriorityList.size(); i++)
			{
				ProjectVariables.CapturedDPkey=Long.valueOf(DPKeysList.get(i).trim());
				ProjectVariables.CapturedDPkeyList.add(DPKeysList.get(i).trim());
				//Requestbody
				//Requestbody = "{\"data_version\":\""+Serenity.sessionVariableCalled("release")+"\",\"disposition\":\"Present\",\"decision_points\":[{\"decision_point_id\":"+DPKeysList.get(i).trim()+",\"payer_ids\":["+Serenity.sessionVariableCalled("Payerkeys")+"],\"lob_ids\":[1,2,3,7,8,9],\"claim_type_ids\":[\"A\",\"F\",\"P\",\"I\",\"O\",\"S\"]}],\"reasons\":[\"High savings\",\"Business Reason\",\"Client Requested\"],\"do_not_present_until_next_run\":false,\"operation\":\"insert\",\"userId\":\""+user+"@ihtech.com\",\"userName\":\""+user+"\",\"client_key\":"+Serenity.sessionVariableCalled("clientkey")+",\"priority\":\""+PriorityList.get(i).trim()+"\"}\r\n";
				Requestbody = "{\r\n" +
						"	\"client_key\":"+Serenity.sessionVariableCalled("clientkey")+",\r\n" +
						"	\"clientDesc\": \""+Serenity.sessionVariableCalled("client")+"\",\r\n" +
						"	\"decision_points\":[\r\n" +
						"		{\"decision_point_id\":"+DPKeysList.get(i).trim()+",\r\n" +
						"		\"payerPolicySet\":[],\r\n" +
						"		\"opptySource\":\"rva\",\"payer_ids\":["+Serenity.sessionVariableCalled("Payerkeys")+"],\r\n" +
						"		\"lob_ids\":[1,2,3,7,8,9],\r\n" +
						"		\"claim_type_ids\":[\"A\",\"F\",\"P\",\"I\",\"O\",\"S\"]}],\r\n" +
						"	\"do_not_present_until_next_run\":false,\r\n" +
						"	\"operation\":\"insert\",\r\n" +
						"	\"userId\":\""+user+"\",\r\n" +
						"	\"userName\":\""+user+"@ihtech.com\",\r\n" +
						"	\"disposition\":\"Present\",\r\n" +
						"	\"page_id\":\"Analysis\",\r\n" +
						"	\"note\":\"\",\r\n" +
						"	\"reasons\":[\"High savings\",\"Business Reason\",\"Client Requested\"],\r\n" +
						"	\"priority\":\""+PriorityList.get(i).trim()+"\"\r\n" +
						"}\r\n";
				//Method to capture the disposition from CPW through service
				oCPWPage.Capture_the_disposition_through_service(Requestbody);
			}
			
			
			Serenity.setSessionVariable("DPkey").to(ProjectVariables.CapturedDPkeyList);
			
			System.out.println(Serenity.sessionVariableCalled("DPkey").toString());
			String DPkey=Serenity.sessionVariableCalled("DPkey").toString().replace("[", "");
			Serenity.setSessionVariable("DPkey").to(DPkey.replace("]", ""));
			
			System.out.println(Serenity.sessionVariableCalled("DPkey").toString());
			
		}
		else
		{
			Assert.assertTrue("Case not found ==>"+DPkeyCriteria+","+Priority, false);
		}
	}

	//==================================================================================================================================================//

	@Step
	public void CaptureSingleDPToNewPayerShort(String Client, String DPkeyCriteria) throws IOException{
		String Requestbody=null;
		String Payershorts = null;
		String newPayershort = null;
		Serenity.setSessionVariable("Disposition").to("Present");
		Serenity.setSessionVariable("user").to("nkumar");
		//Serenity.setSessionVariable("client").to(Client);

		String sClientkey=CPWPage.RetrieveTheClientkeyfromgivenClientthroughservice(Client);

		if(DPkeyCriteria.equalsIgnoreCase("Single"))
		{
			//Method to retrieve the data from DB based on the client
			MongoDBUtils.DPKeysWithNoDispositionBasedOnClient(sClientkey, DPkeyCriteria);			
			Payershorts = Serenity.sessionVariableCalled("Payerkeys");
			newPayershort =  Payershorts.split(",")[0];
			System.out.println("First new payer short is "+newPayershort);		
		}
		if(DPkeyCriteria.equalsIgnoreCase("Same"))
		{
			Payershorts = Serenity.sessionVariableCalled("Payerkeys");
			newPayershort =  Payershorts.split(",")[1].trim();
			System.out.println("Second new payer short is "+newPayershort);
		}
		ProjectVariables.CapturedDPkey=Long.valueOf(Serenity.sessionVariableCalled("DPkey"));
		ProjectVariables.DataVersion=Serenity.sessionVariableCalled("release");

		//Requestbody
		Requestbody = "{\r\n" +
				"	\"client_key\":"+Serenity.sessionVariableCalled("clientkey")+",\r\n" +
				"	\"clientDesc\": \""+Serenity.sessionVariableCalled("client")+"\",\r\n" +
				"	\"decision_points\":[\r\n" +
				"	{\"decision_point_id\":"+Serenity.sessionVariableCalled("DPkey")+",\r\n" +
				"	\"payerPolicySet\":[],\r\n" +
				"	\"opptySource\":\"rva\",\"payer_ids\":["+newPayershort+"],\r\n" +
				"	\"lob_ids\":[1,2,3,7,8,9],\r\n" +
				"	\"claim_type_ids\":[\"A\",\"F\",\"P\",\"I\",\"O\",\"S\"]}],\r\n" +
				"	\"do_not_present_until_next_run\":false,\r\n" +
				"	\"operation\":\"insert\",\r\n" +
				"	\"userId\":\"nkumar@ihtech.com\",\r\n" +
				"	\"userName\":\"natuva.kumar\",\r\n" +
				"	\"disposition\":\"Present\",\r\n" +
				"	\"page_id\":\"Analysis\",\r\n" +
				"	\"note\":\"\",\r\n" +
				"	\"reasons\":[\"High savings\",\"Business Reason\",\"Client Requested\"],\r\n" +
				"	\"priority\":\"High\"\r\n" +
				"}\r\n";
		/*Requestbody = "{\"data_version\":\""+Serenity.sessionVariableCalled("release")+"\",\"disposition\":\"Present\",\"decision_points\":[{\"decision_point_id\":"+Serenity.sessionVariableCalled("DPkey")+","
				+ "\"payer_ids\":["+newPayershort+"],\"lob_ids\":[1,2,3,7,8,9],\"claim_type_ids\":[\"A\",\"F\",\"P\",\"I\",\"O\",\"S\"]}],\"reasons\":[],\"do_not_present_until_next_run\":false,\"operation\":\"insert\",\"userId\":\"nkumar@ihtech.com\",\"userName\":\"natuva.kumar\",\"client_key\":"+Serenity.sessionVariableCalled("clientkey")+",\"priority\":\"High\"}\r\n";*/

		//Method to capture the disposition from CPW through service
		oCPWPage.Capture_the_disposition_through_service(Requestbody);
	}

	//==================================================================================================================================================//

	@Step
	public void validateHomePage_CPQ_CPW(String sAppName) throws InterruptedException 
	{
		switch(sAppName.toUpperCase()){
		case "CPQ" :
			oCPWPage.DynamicWaitfortheCPQLoadingIconWithCount(20);

			if(objSeleniumUtils.is_WebElement_Displayed(oCPWPage.CPQHeaderLabel))
			{
				Assert.assertTrue("CPQ HomePage is displayed",true);
			}
			else
			{
				Assert.assertTrue("CPQ HomePage is not displayed",false);
			}
			break;

		case "CPW" :
			oCPWPage.DynamicWaitfortheLoadingIconWithCount(20);

			if( objSeleniumUtils.is_WebElement_Displayed(oCPWPage.CPWOppRunsLabel))
			{
				Assert.assertTrue("CPW HomePage displayed",true);
			}
			else
			{
				Assert.assertTrue("CPW HomePage not  displayed",false);
			}
			break;
		default:
			Assert.assertTrue("Case not found =>"+sAppName, false);
			break;

		}


	}

	//==================================================================================================================================================//
	@Step
	public void validate_the_captured_DPKey_in_PM_at(String DPkeyCriteria, String PMSection) throws InterruptedException 
	{

		switch(PMSection)
		{
		case "Available DPs Section":

			//validate the captured DPKeys in Available DP's Section
			oCPWPage.Validate_the_Available_DP_section_for_the_given(DPkeyCriteria);

			break;
		case "Presetation Profile Section":
			//validate the captured DPKeys in Available DP's Section
			oCPWPage.Validate_the_Presentation_profile_section_for_the_given(DPkeyCriteria);


			break;
		default:
			Assert.assertTrue("Case not found =>"+DPkeyCriteria, false);
			break;

		}


	}

	//==================================================================================================================================================//
	@Step
	public void User_should_not_view_the_given(String DPkeyCriteria, String PMSection) {boolean bstatus=false;
	List<String> DPKeysList=null;

	//To capture the disposition for retrieved Mongo Data through the service
	DPKeysList=Arrays.asList(Serenity.sessionVariableCalled("DPkey").toString().split(","));

	switch(PMSection)
	{
	case "Available DPs Section":

		//oCPWPage.validate_the_updated_DPkey_in_available_opportunity_deck();

		//Method to select the Captured Topic in subsequent RVA run from Presentation View
		oCPWPage.Select_the_Available_OpportunityDeck_from_Presentationview();

		bstatus=oCPWPage.Enter_the_given_MP_Topic_in_filter_Drawer(Serenity.sessionVariableCalled("Topic"));

		if(bstatus)
		{
			objSeleniumUtils.clickGivenXpath(StringUtils.replace(oFilterDrawer.Medicalpolicy_Checkbox, "value", Serenity.sessionVariableCalled("Topic")));

			oFilterDrawer.user_filters_by_clicking_on_Apply_for_Medical_Policy_Topic();

			if(DPkeyCriteria.equalsIgnoreCase("Updated LOB-DPKey"))
			{
				oCPWPage.validate_the_given_LOB_is_not_visible_On_DP(Serenity.sessionVariableCalled("DPkey"),Serenity.sessionVariableCalled("InvalidInsuranceKey"),PMSection);
			}

			for (int i = 0; i < DPKeysList.size(); i++)
			{
				oOppurtunityDeck.validatethegivenDatainOpportunityDeck(DPKeysList.get(i).trim(), "Updated DPkey");	
			}

		}
		else
		{

			System.out.println("Captured Topic,DPkey is not displayed as expetecd for the disposition is '"+Serenity.sessionVariableCalled("Disposition")+"' in the Available opportunity deck of PM from CPW,DPKey==>"+Serenity.sessionVariableCalled("DPkey")+",Topic==>"+Serenity.sessionVariableCalled("Topic"));
		}





		break;
	case "Presetation Profile Section":
		if(DPkeyCriteria.equalsIgnoreCase("Updated LOB-DPKey"))
		{
			//DB Method to retrieve the Raw savings for the captured DP
			MongoDBUtils.GetDBValuesBasedonAggregation(MonGoDBQueries.FilterMongoDBQuery("RAWSAVINGS FOR DP WITH INSURANCE"),"RAWSAVINGS FOR DP WITH INSURANCE");

			//To Validate the removed LOB is not diplayed in the assigned Presentation
			oCPWPage.validate_the_given_LOB_is_not_visible_On_DP(Serenity.sessionVariableCalled("DPkey"),Serenity.sessionVariableCalled("InvalidInsuranceKey"),PMSection);

			//To Validate the rawsavings for the DP in the assigned Presentation,after removing LOB 
			oCPWPage.Validate_the_rawsavings_for_the_given_DP_at_PresentaionDeck(Serenity.sessionVariableCalled("DPkey"),Serenity.sessionVariableCalled("sPPName"),Serenity.sessionVariableCalled("RawSavings"));
		}
		else
		{

			for (int i = 0; i < DPKeysList.size(); i++)
			{
				//validate the captured DPKeys in Available DP's Section
				bstatus= oCPWPage.select_the_given_DPkey_at_Presentation_view(ProjectVariables.CreatedPresentationList.get(i).trim(), DPKeysList.get(i).trim(),"");
				if(bstatus)
				{
					Assert.assertTrue("Captured Topic,DPkey is still displayed for the disposition '"+Serenity.sessionVariableCalled("Disposition")+"' in the Presentation View of PM from CPW,DPKey=>"+DPKeysList.get(i)+",Topic=>"+Serenity.sessionVariableCalled("Topic")+",Presentation=>"+ProjectVariables.CreatedPresentationList.get(i).trim(), false);
				}
				else
				{
					System.out.println("Captured Topic,DPkey is not displayed as expetecd for the disposition is '"+Serenity.sessionVariableCalled("Disposition")+"' in the Presentation View of PM from CPW,DPKey=>"+DPKeysList.get(i)+",Topic=>"+Serenity.sessionVariableCalled("Topic")+",Presentation=>"+ProjectVariables.CreatedPresentationList.get(i).trim());
				}
			}

		}

		break;
	default:
		Assert.assertTrue("Case not found =>"+DPkeyCriteria, false);
		break;

	}
	}

	@Step
	public void validate_the_DPcards_in_available_opportunity_deck_for_given_selection_of_dropdown(String ShowDropdownValues) {
		String selectedoption=null;
		List<String> ExpectedShowDropDownList=Arrays.asList(ProjectVariables.ShowDropdownValues.split(","));
		List<String> ActualShowDropDownList=Arrays.asList(ShowDropdownValues.split(","));

		for (int i = 0; i < ExpectedShowDropDownList.size(); i++) 
		{
			//To select the option in Show DropDown 
			if(selectedoption==null)
			{
				selectedoption="All";
			}
			objSeleniumUtils.clickGivenXpath(StringUtils.replace(oCPWPage.Span_with_text, "value", selectedoption));
			objSeleniumUtils.defaultWait(ProjectVariables.TImeout_1_Seconds);
			objSeleniumUtils.clickGivenXpath(StringUtils.replace(oCPWPage.ShowDropDownData, "value", ExpectedShowDropDownList.get(i)));
			objSeleniumUtils.defaultWait(ProjectVariables.TImeout_3_Seconds);

			switch(ActualShowDropDownList.get(i))
			{
			case "All":
				MongoDBUtils.GetDBValuesBasedonAggregation(MonGoDBQueries.FilterMongoDBQuery("DP COUNT BASED ON CLIENT"),"DP COUNT BASED ON CLIENT");
				break;
			case "All with filters":
				MongoDBUtils.GetDBValuesBasedonAggregation(MonGoDBQueries.FilterMongoDBQuery("DP COUNT BASED ON CLIENT WITH FILTERS"),"DP COUNT BASED ON CLIENT WITH FILTERS");
				break;
			default:
				MongoDBUtils.GetDBValuesBasedonAggregation(MonGoDBQueries.FilterMongoDBQuery(ActualShowDropDownList.get(i)),ActualShowDropDownList.get(i));
				break;
			}

			//To validate the DPcards in Avaialble opportunitydeck with DB for the selected dropdown
			oCPWPage.validate_the_DPcards_in_available_opportunity_deck_for_given_selection_of_dropdown(ExpectedShowDropDownList.get(i));

			selectedoption=ExpectedShowDropDownList.get(i);

		}

	}

	@Step
	public void retrieveValuesForRuleRelationshipCombination(String DPKeyCount, String valToRetrieve,String ruleRelationshipCombination,String ClientName, String OptionaParam) 
	{
		String  ClientKey =MongoDBUtils.Retrieve_the_PayerKey_From_mongodb_with_the_given_Payershort(ClientName,"Client");
		MongoDBUtils.getRuleRelationshipDetails(DPKeyCount,valToRetrieve,ruleRelationshipCombination,ClientKey);

	}

	@Step
	public void the_is_logged_into_the_NewCPW_application(String user) 
	{
		oCPWPage.Login_NewCPW(user);	

	}

	@Step
	public void captureDisposition(String dispositionTocapture, String dPKeyCount, String string)
	{

		oCPWPage.captureDispositioninAWB	(dispositionTocapture,dPKeyCount,string);
	}

	@Step
	public void navigateToAWBForAClientAndRelease(String clientName, String release) throws IOException 
	{				
		//To retrieve the client key from the given client
        String sClientkey=CPWPage.RetrieveTheClientkeyfromgivenClientthroughservice(clientName);
		//String  ClientKey =MongoDBUtils.Retrieve_the_PayerKey_From_mongodb_with_the_given_Payershort(clientName,"Client");
		Serenity.setSessionVariable("clientkey").to(sClientkey);
		System.out.println("clientkey==>"+Serenity.sessionVariableCalled("clientkey"));
		String exactRelese=StringUtils.substringAfter(release, " ")+GenericUtils.RetrivetheExactMonth(release);
		Serenity.setSessionVariable("release").to(exactRelese);
		//String ClinetToClick  =  "//table[@role='presentation']//td//span[contains(text(),'clientNameVal')]//ancestor::td//preceding-sibling::td//span[contains(text(),'ReleaseVal')]";
		String ClientXpath = StringUtils.replace(oCPWPage.ClinetToClick,"clientNameVal",clientName);
		String ClientXpath2 = StringUtils.replace(ClientXpath,"ReleaseVal",release);
		objSeleniumUtils.clickGivenXpath(ClientXpath2);  
		//Loading POPUP
		objSeleniumUtils.DynamicWaitfortheLoadingIconWithCount(20);
	  
	}

	@Step
	public void captureValuesForDispositionUpdate(String clientName, String dispositionVal, String MDBCollectionName) 
	{
		//String  ClientKey =MongoDBUtils.Retrieve_the_PayerKey_From_mongodb_with_the_given_Payershort(clientName,"Client");
		MongoDBUtils.getRuleRelationshipDetails(clientName,dispositionVal,MDBCollectionName,"");
	}

	@Step
	public void captureValuesforDPKeyafterUpdateDisposition(String valtoRetrieve, String criteria,String dispositionVal, String mDBCollectionName) 
	{

		MongoDBUtils.GetAvailableDPKeyWithRelations(valtoRetrieve,criteria,dispositionVal,mDBCollectionName);

	}

	@Step
	public void validateTooltipInformationInAWB(String validationCriteria, String capturedOrFromDB, String DPorTopicorMPLevel)
	{		

		WebElement   TopicIcon ;
		WebElement   DPIcon ;

		switch (validationCriteria)
		{				
		case "AllRuleRelationshipsCount": 

			if(DPorTopicorMPLevel.equalsIgnoreCase("TopicLevel"))
			{
				TopicIcon = getDriver().findElement(By.xpath(oCPWPage.TopicRuleRelationshipIcon));		
				//Validate if RuleRelationshipIcon  Tool tip  is present
				objSeleniumUtils.highlightElement(oCPWPage.TopicRuleRelationshipIcon);
				objSeleniumUtils.moveToElement(TopicIcon);					  
				String UITooltipText = objSeleniumUtils.Get_Value_By_given_attribute("title", oCPWPage.TopicRuleRelationshipIcon);					  
				if(UITooltipText.equalsIgnoreCase("Rule Relationship Alert 1 DP(s)"))
				{							      	
					Assert.assertTrue("Tooltip::"+UITooltipText+" is displayed when User hovers mouse for  Topic::",true);	
				}
				else
				{
					Assert.assertTrue("Tooltip is not displayed when User hovers mouse for  Topic::",false);
				}		
			}	  
			break;

		case "RuleRelationshipAlert":												   

			if(DPorTopicorMPLevel.equalsIgnoreCase("DPlevel"))
			{									
				DPIcon = getDriver().findElement(By.xpath(oCPWPage.DPRuleRelationshipIcon));										
				//Validate if RuleRelationshipIcon  Tool tip  is present
				objSeleniumUtils.highlightElement(DPIcon);
				objSeleniumUtils.moveToElement(DPIcon);					  
				String UITooltipTextDP = objSeleniumUtils.Get_Value_By_given_attribute("title", oCPWPage.DPRuleRelationshipIcon);					  
				if(UITooltipTextDP.equalsIgnoreCase("Rule Relationship Alert"))
				{							      	
					Assert.assertTrue("Tooltip::"+UITooltipTextDP+" is displayed when User hovers mouse for  DP::",true);	
				}
				else
				{
					Assert.assertTrue("Tooltip is not displayed when User hovers mouse for  DP::",false);
				}
			}	  

			break;

		default:  System.out.println("No switch case input provided");				

		}

	}

	public void retrieveValuesForRuleRelationshipCombinationForMessages(String dPKeyCount, String valToRetrieve,String ruleRelationshipCombination, String clientName, String OptionalParam) 
	{		
		String  ClientKey =MongoDBUtils.Retrieve_the_PayerKey_From_mongodb_with_the_given_Payershort(clientName,"Client");
		MongoDBUtils.getRuleRelationshipDetailsForWarningMessageValidation(dPKeyCount,valToRetrieve,ruleRelationshipCombination,ClientKey);
	}


	@Step
	public void searchValueinAWB(String searchValue, String pageName)
	{
		oCPWPage.findValueinAWB(searchValue, pageName);
	}

	@Step
	public void validateRuleRelatioshipIcon(String dPorTopicLevel) 
	{		
		oCPWPage.validateRuleRelationshipIcons(dPorTopicLevel);
	}

	@Step
	public void validate_the_rule_status_based_on(String ruleStatus)throws Exception {
		ArrayList<String> output = MongoDBUtils.getDPKey(Serenity.sessionVariableCalled("clientkey"),Serenity.sessionVariableCalled("release"), ruleStatus); 
		if(output.size()>0){
			objSeleniumUtils.clickGivenXpath("//span[contains(text(),'Review Worked Opportunities')]");
			objSeleniumUtils.defaultWait(ProjectVariables.TImeout_6_Seconds);
			ruleStatus=ruleStatus.toLowerCase();				
			for(int i=0;i<output.size();i++){
				String DPKey=output.get(i).split("-")[0];
				String[] SubRules= output.get(i).split("-")[1].split(",");
				objSeleniumUtils.Enter_given_Text_Element("//input[@placeholder='Search Policy, Topic, DP, or DP Description']", DPKey);
				objSeleniumUtils.clickGivenXpath("//button[contains(@class,'search-button')]");
				objSeleniumUtils.defaultWait(ProjectVariables.TImeout_3_Seconds);
				Assert.assertTrue("Click status dispostion description", objSeleniumUtils.clickGivenXpath(StringUtils.replace(oCPWPage.DispositionDesclocator, "value", DPKey)));
				objSeleniumUtils.DynamicWaitfortheLoadingIconWithCount(20);
				for(int j=0;j<SubRules.length;j++){
					GenericUtils.Verify("displaying midRule:"+SubRules[j]+" as "+ruleStatus, oGenericUtils.isElementExist("//p[@class ='midrule-name'][contains(text(),'"+SubRules[j]+"')]/span[text()='("+ruleStatus.substring(0, 1).toUpperCase() + ruleStatus.substring(1)+")']"));
				}
				objSeleniumUtils.clickGivenXpath("//fa-icon[@icon='times']");
				objSeleniumUtils.defaultWait(ProjectVariables.TImeout_3_Seconds);
				
			}
		}else{
			GenericUtils.Verify("no rules are found with status:"+ruleStatus+" for clientKey:"+Serenity.sessionVariableCalled("clientkey"),true); 
		}
		
	}

	@Step
	public void selectDPKeyBasedOnGivenMedicalPolicy(String medPolicy,Long dpKey)throws Exception {
		// TODO Auto-generated method stub

		//MongoDBUtils.getDPKey(Serenity.sessionVariableCalled("clientkey"),Serenity.sessionVariableCalled("release"), ruleStatus);		
		dpKey = Serenity.sessionVariableCalled("DPkey");
		//String MedPolicy = Serenity.sessionVariableCalled("Medicalpolicy");	
		medPolicy = Serenity.sessionVariableCalled("Medicalpolicy");	
		String MedPolicyXpath = StringUtils.replace(oCPWPage.MedPolicyAfterSearch, "MedPolicyValue", medPolicy);

		// Click PolicySelectionDrawerButton		
		// Enter DP in the Opps Search box
		objSeleniumUtils.clickGivenXpath(oCPWPage.PolicySelectionDrawerButton);
		// Enter MedicalPolicy in the search Box
		objSeleniumUtils.Enter_given_Text_Element(oCPWPage.MedPolicySearchBox, medPolicy);
		// Click Search Button
		objSeleniumUtils.clickGivenXpath(oCPWPage.MedPolicySearchButton);
		// Click the MedPolicy Name so that ApplyToOpportunityGrid Button is
		// enabled
		objSeleniumUtils.clickGivenXpath(MedPolicyXpath);
		// Click ApplyToOpportunityGrid Button
		objSeleniumUtils.clickGivenXpath(oCPWPage.ApplyToOpportunityGridBtn);
		objSeleniumUtils.Enter_given_Text_Element(oCPWPage.SearchFileldXpath, String.valueOf(dpKey));
		objSeleniumUtils.clickGivenXpath(oCPWPage.SearchButtonXpath);
		objSeleniumUtils.defaultWait(ProjectVariables.TImeout_6_Seconds);
		//objSeleniumUtils.clickGivenXpath(oCPWPage.TopicExpandButton);		
		objSeleniumUtils.defaultWait(ProjectVariables.TImeout_6_Seconds);

	}

	@Step
	public void clicks_on_link_of_DPDescription(String Captureddata, String capturedtypeofdata) {
		// TODO Auto-generated method stub

		String dpKey = Serenity.sessionVariableCalled("DPkey").toString();
		String dpKeyDescXpath = StringUtils.replace(oCPWPage.DispositionDesclocator, "value", dpKey);
		if(capturedtypeofdata.equalsIgnoreCase("DP Description"))
		{
		Assert.assertTrue("Click status "+dpKeyDescXpath+"Dispostion Description", objSeleniumUtils.clickGivenXpath(dpKeyDescXpath));
		objSeleniumUtils.DynamicWaitfortheLoadingIconWithCount(20);
		boolean statusValue=oGenericUtils.isElementExist(oCPWPage.midRuleStatusXPath);
		Assert.assertTrue("Status xPath "+oCPWPage.midRuleStatusXPath+"Status",statusValue);}
					
	}


	@Step
	public void user_search_for_in_AWB(String clientKey) {
		//Retrieve Session Variables
		Long DPKey = Serenity.sessionVariableCalled("DPKey");
		String MedPolicy = Serenity.sessionVariableCalled("MedicalPolicy");	


		String PolicySelectionDrawerButton = "//button[@class='policy-selection-button mat-flat-button mat-button-base mat-accent']";

		String MedPolicySearchBox =  "//div[@class='policy-drawer']//div[@class='mat-form-field-infix']//input[contains(@class,'mat-input-element')]";

		String MedPolicySearchButton  =  "//div[@class='policy-drawer']//button[@class='search-button mat-flat-button mat-button-base']";

		String MedPolicyAfterSearch  = "//div[@class='policy-drawer']//table[@class='k-grid-table']//div[contains(text(),'MedPolicyValue')]";

		String   ApplyToOpportunityGridBtn =   "//div[@class='policy-drawer']//button/span[contains(text(), 'Apply To Opportunity Grid')]/parent::button";


		//**************************************************	 	 

		//Click   PolicySelectionDrawerButton
		objSeleniumUtils.clickGivenXpath(PolicySelectionDrawerButton);

		//String  ClientKey =MongoDBUtils.Retrieve_the_PayerKey_From_mongodb_with_the_given_Payershort("Aetna Medicaid","Client");

		MongoDBUtils.getMedicalPolicyByClientName(clientKey);

		//Enter MedicalPolicy in the search Box
		objSeleniumUtils.Enter_given_Text_Element(MedPolicySearchBox,Serenity.sessionVariableCalled("MedicalPolicy"));

		//Click   Search Button
		objSeleniumUtils.clickGivenXpath(MedPolicySearchButton);

		String MedPolicyXpath  = StringUtils.replace(MedPolicyAfterSearch,"MedPolicyValue",Serenity.sessionVariableCalled("MedicalPolicy"));
		//Click   the MedPolicy Name so that ApplyToOpportunityGrid Button is enabled
		objSeleniumUtils.clickGivenXpath(MedPolicyXpath);

		//Click   ApplyToOpportunityGrid Button
		objSeleniumUtils.clickGivenXpath(ApplyToOpportunityGridBtn);

		objSeleniumUtils.defaultWait(ProjectVariables.TImeout_6_Seconds);
	}

	@Step
	public void user_should_see_filter_options_from_flag(String arg1) {		
		List<WebElement> list = getDriver().findElements(By.xpath("//div[@class='filter']/*[@title='Flag']//div[@class='mat-list-text']"));
		for(WebElement e : list)
			Assert.assertTrue(e.isEnabled());
		String[] flags = objSeleniumUtils.get_All_Text_from_Locator("//div[@class='filter']/*[@title='Flag']//div[@class='mat-list-text']");
		flags = Arrays.stream(flags).map(String::trim).toArray(String[]::new);
		String[] expected =  Arrays.stream(arg1.split(",")).map(String::trim).toArray(String[]::new);
		if(!Arrays.toString(flags).contains(Arrays.toString(expected))){
			System.out.println("flag options not matched : "+Arrays.toString(flags));
			Assert.fail("flag options not matched : Actual =>"+Arrays.toString(flags)+ " Expected => "+ arg1);
		}
	}

	@Step
	public void user_validate_apply_filter_on_flag_section_for_client(String arg1,String clientKey) {
		String resetbtn = "//div[@class='reset-apply-buttons']/button[1]";
		String applyFilterbtn = "//div[@class='reset-apply-buttons']/button[2]";
		String flagChk = "//div[@class='filter']/*[@title='Flag']/div[1]/mat-checkbox/label/div/div[3]/*/*";
		String production_Chk = "//app-checklist[@title='Savings Status']//div[contains(text(),'Production')]/../mat-pseudo-checkbox[@class='mat-pseudo-checkbox mat-pseudo-checkbox-checked']";
		String topicDesc = "(//h3[text()='Opportunities']/..//table)[2]//div[contains(@class,'topic-desc')]//label";

		String flagOptionChk  = "//div[@class='filter']/*[@title='Flag']//div[contains(text(),'arg')]/../child::*[2]";
		String expandLink = "((//h3[text()='Opportunities']/..//table)[2]//a[@class='k-icon ng-star-inserted k-i-expand' or @class='k-icon k-i-expand ng-star-inserted'])[1]";
		String individualFlag = "(//h3[text()='Opportunities']/..//table)[2]//tr//i[@class='fa fa-bookmark ng-star-inserted']";
		String flagText = "//mat-table/mat-row[not(mat-cell/span/i)]/mat-cell[position()=5 or position()=6 or position()=7 or position()=8][normalize-space()]";
		String ruleRelationshipMsg = "//div[contains(@id,'mat-dialog-title')]/span";
		String closeX = "//mat-dialog-container//*[@role='img']";

		String[] flags =  Arrays.stream(arg1.split(",")).map(String::trim).toArray(String[]::new);
		for(String flag:flags){
			System.out.println("********************  Validation =>  "+flag+" ********************");
			objSeleniumUtils.defaultWait(ProjectVariables.TImeout_2_Seconds);			
			objSeleniumUtils.moveToElementAndClick(resetbtn);
			objSeleniumUtils.moveToElementAndClick(flagChk);
			objSeleniumUtils.moveToElementAndClick(flagOptionChk.replace("arg", flag));			
			if(objSeleniumUtils.is_WebElement_Displayed(production_Chk))
				objSeleniumUtils.moveToElementAndClick(production_Chk);	
			objSeleniumUtils.clickGivenXpath(applyFilterbtn);
			String[] topics = objSeleniumUtils.get_All_Text_from_Locator(topicDesc);			
			if(topics.length>0){
				if(flag.contains("None")){
					String tableFlags = "(//h3[text()='Opportunities']/..//table)[2]//tr/td[3]/div";
					Assert.assertFalse(objSeleniumUtils.is_WebElement_Displayed(tableFlags));
					System.out.println("Flags does not exist for => 'None' filter");
				}
				else{
					// DB Validation
					MongoDBUtils.getTopicsByPolicyName(clientKey,Serenity.sessionVariableCalled("MedicalPolicy"),flag.toUpperCase());

					List<String> dbTopics = Serenity.sessionVariableCalled("Topics");
					// VALIDATION PENDING
					Arrays.equals(dbTopics.toArray(), Arrays.asList(topics).toArray());
					System.out.println("Topics were matched for '"+flag+"'");

					do{	
						objSeleniumUtils.moveToElementAndClick(expandLink);		
					}while(objSeleniumUtils.is_WebElement_Displayed(expandLink));	
					List<WebElement> flagElements = getDriver().findElements(By.xpath(individualFlag));
					for(int i=1;i<=flagElements.size();i++){
						objSeleniumUtils.moveToElementAndClick("("+individualFlag+")["+i+"]");
						objSeleniumUtils.defaultWait(ProjectVariables.TImeout_1_Seconds);
						System.out.println("Relationship : "+objSeleniumUtils.get_TextFrom_Locator(ruleRelationshipMsg));	
						String[] text = objSeleniumUtils.get_All_Text_from_Locator(flagText);	
						Assert.assertTrue(Arrays.asList(text).contains(flag));
						objSeleniumUtils.moveToElementAndClick(closeX);
						objSeleniumUtils.defaultWait(ProjectVariables.TImeout_1_Seconds);				
					}	
				}
				objSeleniumUtils.$(resetbtn).withTimeoutOf(ProjectVariables.TImeout_5_Seconds, ChronoUnit.SECONDS).waitUntilVisible().sendKeys(Keys.PAGE_UP);
			}
			else
				System.out.println("No topics displayed for '"+flag+"'");				
		}

	}

	@Step
	public void captureDispositionForProvidedDetails(String dispositionToTake, String dPKeyCount,String medicalPolicyCount)  
	{
		oCPWPage.captureDispositionForProvidedDetailsinAWB( dispositionToTake,  dPKeyCount, medicalPolicyCount)  ;		
	}

	@Step
	public void validateruleRelationiconAndAlertinAWB(String topicLevel, String dPLevel)
	{
		oCPWPage.validateRuleRelationAlertinAWB( topicLevel, dPLevel);
	}

	@Step
	public void validateWarningMessage(String expectedWarningMessage) 
	{				   
		objSeleniumUtils.highlightElement(oCPWPage.WarningMessageXpath);		  
		String  UIWarningMessage =   objSeleniumUtils.get_TextFrom_Locator(oCPWPage.WarningMessageXpath);	      

		if( expectedWarningMessage.trim().equalsIgnoreCase(UIWarningMessage.trim()))
		{
			Assert.assertTrue("Warning message is displayed",true);
			System.out.println("UI Warning Message:"+UIWarningMessage);
			System.out.println("Expected  Warning Message:"+expectedWarningMessage);
		}
		else
		{
			System.out.println("UI Warning Message:"+UIWarningMessage);
			System.out.println("Expected  Warning Message:"+expectedWarningMessage);
			Assert.assertTrue("Warning message is not displayed",false);
		}

	}


	@Step
	public void verify_the_DP_Topic_of_retire_status(String Page)throws Exception {
		// TODO Auto-generated method stub

		Long dpKey=Serenity.sessionVariableCalled("DPkey"); 

		switch(Page){
		case "AWB": 
			MongoDBUtils.getDPAndTopicRetire("No Disposition");
			String medicalPolicy=Serenity.sessionVariableCalled("Medicalpolicy");
			String MedPolicyXpath = StringUtils.replace(oCPWPage.MedPolicyAfterSearch, "MedPolicyValue", medicalPolicy);
			objSeleniumUtils.clickGivenXpath(oCPWPage.PolicySelectionDrawerButton);
			objSeleniumUtils.defaultWait(ProjectVariables.TImeout_10_Seconds);
			objSeleniumUtils.Enter_given_Text_Element(oCPWPage.MedPolicySearchBox, medicalPolicy);

			objSeleniumUtils.clickGivenXpath(oCPWPage.MedPolicySearchButton);
			objSeleniumUtils.defaultWait(ProjectVariables.TImeout_3_Seconds);
			objSeleniumUtils.clickGivenXpath(MedPolicyXpath); 
			objSeleniumUtils.clickGivenXpath(oCPWPage.ApplyToOpportunityGridBtn);
			objSeleniumUtils.defaultWait(ProjectVariables.TImeout_6_Seconds);
			boolean statusValue=oGenericUtils.isElementExist(oCPWPage.nonRecordsOfMedicalPolicies);
			if(statusValue)
			{
				Assert.assertTrue("Non Medical Policy / Topic "+oCPWPage.nonRecordsOfMedicalPolicies+"Status",statusValue);
			}
			else
			{

				objSeleniumUtils.Enter_given_Text_Element(oCPWPage.SearchFileldXpath, String.valueOf(dpKey));
				objSeleniumUtils.clickGivenXpath(oCPWPage.SearchButtonXpath);
				objSeleniumUtils.defaultWait(ProjectVariables.TImeout_6_Seconds);
				Assert.assertTrue("Non Records Opportunities "+oCPWPage.nonRecordsOfOpportunities+"Status",oGenericUtils.isElementExist(oCPWPage.nonRecordsOfOpportunities));
			}
			break;
		case "RWO":
			MongoDBUtils.getDPAndTopicRetire("No Disposition");
			objSeleniumUtils.clickGivenXpath(oCPWPage.reviewWorkedOpportunities);
			objSeleniumUtils.defaultWait(ProjectVariables.TImeout_6_Seconds);
			objSeleniumUtils.Enter_given_Text_Element(oCPWPage.SearchFileldXpath, String.valueOf(dpKey));
			objSeleniumUtils.clickGivenXpath(oCPWPage.SearchButtonXpath);
			objSeleniumUtils.defaultWait(ProjectVariables.TImeout_6_Seconds);
			Assert.assertTrue("Non Records Opportunities "+oCPWPage.nonRecordsOfOpportunities+"Status",oGenericUtils.isElementExist(oCPWPage.nonRecordsOfOpportunities));
			break;
		case "PM":
			MongoDBUtils.getDPAndTopicRetire("Present"); 
			Thread.sleep(2000);
			//Click on 'Reset' button
			objSeleniumUtils.highlightElement(oFilterDrawer.sReset);
			oGenericUtils.clickButton(By.xpath(oFilterDrawer.sReset));
			Thread.sleep(2000);
			boolean bstatus=oCPWPage.Enter_the_given_MP_Topic_in_filter_Drawer(Serenity.sessionVariableCalled("Medicalpolicy"));

			if(bstatus)
			{
				objSeleniumUtils.clickGivenXpath(StringUtils.replace(oFilterDrawer.Medicalpolicy_Checkbox, "value", Serenity.sessionVariableCalled("Medicalpolicy")));
				oFilterDrawer.user_filters_by_clicking_on_Apply_for_Medical_Policy_Topic();
				oOppurtunityDeck.validatethegivenDatainOpportunityDeck(Serenity.sessionVariableCalled("DPkey").toString(), "Updated DPkey");
			}
			else
			{
				Assert.assertTrue("DPKey not displayed as its retired"+Serenity.sessionVariableCalled("DPkey"), true);
			}
			break;
		}

	}

	@Step
	public void validateRuleRelationIcon(String section) 
	{
		oCPWPage.navigateToRulerelationPopup(section);
				
	}
	
	@Step		
	public void validateSavingValueDifference(String arg1, String arg2, String arg3) {
		Integer savingVal = Integer.parseInt(Serenity.sessionVariableCalled(arg1).toString().trim().replace("$", "").replace(",", ""));
		HashMap<Object,Object> before = Serenity.sessionVariableCalled(arg2);
		HashMap<Object,Object> after = Serenity.sessionVariableCalled(arg3);
		
		Integer RawOppSavingsBefore = Integer.parseInt(before.get("RawOppSavings").toString());		
		Integer RawOppSavingsAfter = Integer.parseInt(after.get("RawOppSavings").toString());
		Assert.assertTrue("Saving value is different from ["+RawOppSavingsBefore+" - "+RawOppSavingsAfter+"]",(RawOppSavingsBefore-RawOppSavingsAfter)==savingVal);
		
		Integer AggOppSavingsBefore = Integer.parseInt(before.get("AggOppSavings").toString());		
		Integer AggOppSavingssAfter = Integer.parseInt(after.get("AggOppSavings").toString());
		Assert.assertTrue("Saving value is different from ["+AggOppSavingsBefore+" - "+AggOppSavingssAfter+"]",(AggOppSavingsBefore-AggOppSavingssAfter)==savingVal);
		
		Integer conOppSavingsBefore = Integer.parseInt(before.get("ConOppSavings").toString());
		Integer conOppSavingsAfter = Integer.parseInt(after.get("ConOppSavings").toString());
		Assert.assertTrue("Saving value is different from ["+conOppSavingsBefore+" - "+conOppSavingsAfter+"]",(conOppSavingsBefore-conOppSavingsAfter)==savingVal);
	}

	@Step
	public void userSelectsMedicalPolicyAs(String MP_Topic) {
		oCPWPage.Enter_the_given_MP_Topic_in_filter_Drawer(MP_Topic);
	}
	
	@Step
	public void user_select_Medical_Policy_from_the_policy_selection_through_MongoDB(String sTabname) throws Throwable {
		String DBMedicalpolicy = null;
		if (sTabname.equalsIgnoreCase("eLL")) {
			DBMedicalpolicy = MongoDBUtils.retrieveTheeLLMPsbasedonPPSforFilterPanel("", "", "", "");
		} else {
			DBMedicalpolicy = MongoDBUtils.Retrieve_the_medicalpolicy_based_on_client_and_release(
					Serenity.sessionVariableCalled("clientkey"), Serenity.sessionVariableCalled("release"), "");
		}

		if (DBMedicalpolicy == null) {
			Assert.assertTrue("Retrieved Medical policy is 'null' from the mongoDB for the client ===>"
					+ Serenity.sessionVariableCalled("client") + ",Release ===>"
					+ Serenity.sessionVariableCalled("release"), false);
		}

		Serenity.setSessionVariable("Medicalpolicy").to(DBMedicalpolicy);

		oCPWPage.SelectPolicySelectionAndApplyFilters(DBMedicalpolicy);

		}
	
	//################################### Method to verify cpw changes after subsequent pipeline #############################################################
	
	@Step
	public void verify_AWB_and_RWO_pages_after_pipeline_for_the_captured_data(String sChange, String sDisposition, String DpType,String user) throws Throwable{
		
		ExcelUtils.LoadEllData();
		

		HashMap<Integer, HashMap<String, String>> sEllData = ProjectVariables.sSubsequentEllData;
		
		Serenity.setSessionVariable("user").to(user);

		int iRowCount = sEllData.size();
		int i =1;
		boolean  blnRows = false;

		for (i=1;i<=iRowCount;i++){
			
			String sExecution = sEllData.get(i).get("Execution");
			
			if (sExecution.equalsIgnoreCase("Y")){
				Serenity.setSessionVariable("client").to(ProjectVariables.sSubsequentEllData.get(i).get("Client"));
				Serenity.setSessionVariable("user").to(user);
				Serenity.setSessionVariable("Medicalpolicy").to(ProjectVariables.sSubsequentEllData.get(i).get("Medical_Policy"));
				Serenity.setSessionVariable("Topic").to(ProjectVariables.sSubsequentEllData.get(i).get("Topic"));
				Serenity.setSessionVariable("DPkey").to(ProjectVariables.sSubsequentEllData.get(i).get("i_DP Key"));
				Serenity.setSessionVariable("Disposition").to(ProjectVariables.sSubsequentEllData.get(i).get("Disposition_captured"));	
				Serenity.setSessionVariable("RFP_Presentation").to(ProjectVariables.sSubsequentEllData.get(i).get("Prstn_Set_RFP"));
				Serenity.setSessionVariable("NotRFP_Presentation").to(ProjectVariables.sSubsequentEllData.get(i).get("Prstn_NotSet_RFP"));
				Serenity.setSessionVariable("RFP_Savings").to(ProjectVariables.sSubsequentEllData.get(i).get("O_Savings_NotSet_RFP"));
				String sClientkey=CPWPage.RetrieveTheClientkeyfromgivenClientthroughservice(Serenity.sessionVariableCalled("client").toString());
				Serenity.setSessionVariable("clientkey").to(sClientkey);
			}		
		}
		
		
		oCPWPage.SelectPayer("", Serenity.sessionVariableCalled("client").toString());
		switch(sChange.toUpperCase()){
		case "DP RETIRE":
			oCPWPage.SelectPolicySelectionAndApplyFilters(Serenity.sessionVariableCalled("Topic").toString(),"Topic");
			oCPWPage.verifygridcolumnsinAWBPae(DpType);
			Assert.assertFalse("'No results found that meet the search criteria.' message is displayed in the AWB Grid for the Medical policy ==>"+"Medicaid - New York State Policy",objSeleniumUtils.is_WebElement_Displayed(StringUtils.replace(oCPWPage.Div_contains_text, "value", "No results found that meet the search criteria.")));
			ArrayList<String> DPKeylist = new ArrayList();
			DPKeylist.add(Serenity.sessionVariableCalled("DPkey").toString());

			oCPWPage.verify_the_captured_data_is_not_displayed_in_the_given(DPKeylist, "DPkey", DpType);//mp no need
			///
			oCPWPage.Open_the_Review_Worked_Opportunity_Page();
			Assert.assertTrue("Unable to enter the DPkey in the search field of Reviewwoked Opportunity Page, DPkey ===>"+DPKeylist, objSeleniumUtils.Enter_given_Text_Element(oCPWPage.sSearchField_RWOpp,Serenity.sessionVariableCalled("DPkey").toString()));

			Assert.assertTrue("Unable to click the search icon beside search box in the Reviewwoked Opportunity Page", objSeleniumUtils.clickGivenXpath(oCPWPage.AWBgriSearchbutton));

			objSeleniumUtils.defaultWait(ProjectVariables.TImeout_5_Seconds);
			Assert.assertTrue("Captured DPKey is not displaying in the Review Worked Opp Grid,Captured  DPKey ===>"+DPKeylist+",Disposition ==>", objSeleniumUtils.is_WebElement_Displayed(StringUtils.replace(oCPWPage.ButtonContainsText, "value",Serenity.sessionVariableCalled("DPkey").toString())));
			break;
		case "MP RETIRE":
			oCPWPage.selectgivenFiltersMP_Topic(Serenity.sessionVariableCalled("Medicalpolicy").toString(),"MP");
			oCPWPage.verifygridcolumnsinAWBPae(DpType);
			oCPWPage.Open_the_Review_Worked_Opportunity_Page();
			Assert.assertTrue("Unable to enter the MP in the search field of Reviewwoked Opportunity Page, MP ===>"+Serenity.sessionVariableCalled("Medicalpolicy").toString(), objSeleniumUtils.Enter_given_Text_Element(oCPWPage.sSearchField_RWOpp,Serenity.sessionVariableCalled("Medicalpolicy").toString()));

			Assert.assertTrue("Unable to click the search icon beside search box in the Reviewwoked Opportunity Page", objSeleniumUtils.clickGivenXpath(oCPWPage.AWBgriSearchbutton));

			objSeleniumUtils.defaultWait(ProjectVariables.TImeout_5_Seconds);
			Assert.assertTrue("Captured DPKey is not displaying in the Review Worked Opp Grid,Captured  MP ===>"+Serenity.sessionVariableCalled("Medicalpolicy").toString()+",Disposition ==>", objSeleniumUtils.is_WebElement_Displayed(StringUtils.replace(oCPWPage.ButtonContainsText, "value",Serenity.sessionVariableCalled("Medicalpolicy").toString())));
			break;
		case "TOPIC RETIRE":
			oCPWPage.selectgivenFiltersMP_Topic(Serenity.sessionVariableCalled("Medicalpolicy").toString(),"Topic");
			oCPWPage.verifygridcolumnsinAWBPae(DpType);
			oCPWPage.Open_the_Review_Worked_Opportunity_Page();
			Assert.assertTrue("Unable to enter the Topic in the search field of Reviewwoked Opportunity Page, Topic ===>"+Serenity.sessionVariableCalled("Topic").toString(), objSeleniumUtils.Enter_given_Text_Element(oCPWPage.sSearchField_RWOpp,Serenity.sessionVariableCalled("Topic").toString()));

			Assert.assertTrue("Unable to click the search icon beside search box in the Reviewwoked Opportunity Page", objSeleniumUtils.clickGivenXpath(oCPWPage.AWBgriSearchbutton));

			objSeleniumUtils.defaultWait(ProjectVariables.TImeout_5_Seconds);
			Assert.assertTrue("Captured DPKey is not displaying in the Review Worked Opp Grid,Captured  Topic ===>"+Serenity.sessionVariableCalled("Topic").toString()+",Disposition ==>", objSeleniumUtils.is_WebElement_Displayed(StringUtils.replace(oCPWPage.ButtonContainsText, "value",Serenity.sessionVariableCalled("Topic").toString())));
			break;
		case "DP DESCRIPTION":
			oCPWPage.Open_the_Review_Worked_Opportunity_Page();
			Assert.assertTrue("Unable to enter the DPKey in the search field of Reviewwoked Opportunity Page, DPkey ===>"+Serenity.sessionVariableCalled("DPkey").toString(), objSeleniumUtils.Enter_given_Text_Element(oCPWPage.sSearchField_RWOpp,Serenity.sessionVariableCalled("DPkey").toString()));

			Assert.assertTrue("Unable to click the search icon beside search box in the Reviewwoked Opportunity Page", objSeleniumUtils.clickGivenXpath(oCPWPage.AWBgriSearchbutton));

			objSeleniumUtils.defaultWait(ProjectVariables.TImeout_5_Seconds);
			Assert.assertTrue("Captured DPKey is not displaying in the Review Worked Opp Grid,Captured  DPkey ===>"+Serenity.sessionVariableCalled("DPkey").toString()+",Disposition ==>", objSeleniumUtils.is_WebElement_Displayed(StringUtils.replace(oCPWPage.ButtonContainsText, "value",Serenity.sessionVariableCalled("DPkey").toString())));
			Assert.assertTrue("Captured DP description is not displaying in the Review Worked Opp Grid,Captured  discription ===>"+Serenity.sessionVariableCalled("DPDesc").toString()+",Disposition ==>", objSeleniumUtils.is_WebElement_Displayed(StringUtils.replace(oCPWPage.Span_contains_text, "value",Serenity.sessionVariableCalled("DPDesc").toString())));
			Assert.assertTrue("Unable to click the DPKey in Reviewwoked Opportunity Page", objSeleniumUtils.clickGivenXpath(StringUtils.replace(oCPWPage.ButtonContainsText, "value",Serenity.sessionVariableCalled("DPkey").toString())));
			Assert.assertTrue("Captured DP description is not displaying in the Review Worked Opp Grid,Captured  discription ===>"+Serenity.sessionVariableCalled("DPDesc").toString()+",Disposition ==>", objSeleniumUtils.is_WebElement_Displayed(StringUtils.replace(oCPWPage.Span_contains_text, "value",Serenity.sessionVariableCalled("DPDesc").toString())));
			break;
		case "TOPIC NAME":
			oCPWPage.selectgivenFiltersMP_Topic(Serenity.sessionVariableCalled("Topic").toString(),"Topic");
			oCPWPage.verifygridcolumnsinAWBPae(DpType);
			oCPWPage.Open_the_Review_Worked_Opportunity_Page();
			Assert.assertTrue("Unable to enter the Topic in the search field of Reviewwoked Opportunity Page, Topic ===>"+Serenity.sessionVariableCalled("Topic").toString(), objSeleniumUtils.Enter_given_Text_Element(oCPWPage.sSearchField_RWOpp,Serenity.sessionVariableCalled("Topic").toString()));

			Assert.assertTrue("Unable to click the search icon beside search box in the Reviewwoked Opportunity Page", objSeleniumUtils.clickGivenXpath(oCPWPage.AWBgriSearchbutton));

			objSeleniumUtils.defaultWait(ProjectVariables.TImeout_5_Seconds);			
			Assert.assertTrue("Captured Topic is not displaying in the Review Worked Opp Grid,Captured  Topic ===>"+Serenity.sessionVariableCalled("Topic").toString()+",Disposition ==>", objSeleniumUtils.is_WebElement_Displayed(StringUtils.replace(oCPWPage.labelcontainstext, "value",Serenity.sessionVariableCalled("Topic").toString())));
			break;
		case "MP NAME":
			oCPWPage.selectgivenFiltersMP_Topic(Serenity.sessionVariableCalled("Medicalpolicy").toString(),"MP");
			oCPWPage.verifygridcolumnsinAWBPae(DpType);
			oCPWPage.Open_the_Review_Worked_Opportunity_Page();
			Assert.assertTrue("Unable to enter the MP in the search field of Reviewwoked Opportunity Page, MP ===>"+Serenity.sessionVariableCalled("Medicalpolicy").toString(), objSeleniumUtils.Enter_given_Text_Element(oCPWPage.sSearchField_RWOpp,Serenity.sessionVariableCalled("Medicalpolicy").toString()));

			Assert.assertTrue("Unable to click the search icon beside search box in the Reviewwoked Opportunity Page", objSeleniumUtils.clickGivenXpath(oCPWPage.AWBgriSearchbutton));

			objSeleniumUtils.defaultWait(ProjectVariables.TImeout_5_Seconds);			
			Assert.assertTrue("Captured MP is not displaying in the Review Worked Opp Grid,Captured  MP ===>"+Serenity.sessionVariableCalled("Medicalpolicy").toString()+",Disposition ==>", objSeleniumUtils.is_WebElement_Displayed(StringUtils.replace(oCPWPage.labelcontainstext, "value",Serenity.sessionVariableCalled("Medicalpolicy").toString())));
			break;
		default:
			Assert.assertTrue("case not found::"+sChange, false);
		break;
			
		}
			
	}

}


