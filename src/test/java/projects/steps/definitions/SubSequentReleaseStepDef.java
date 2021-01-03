package projects.steps.definitions;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.bson.conversions.Bson;
import org.json.simple.parser.ParseException;
import org.junit.Assert;

import com.mongodb.client.model.Filters;

import io.restassured.response.Response;
import net.serenitybdd.core.Serenity;
import net.serenitybdd.core.environment.EnvironmentSpecificConfiguration;
import net.thucydides.core.annotations.Step;
import net.thucydides.core.steps.ScenarioSteps;
import net.thucydides.core.util.EnvironmentVariables;
import net.thucydides.core.util.SystemEnvironmentVariables;
import project.pageobjects.CPWPage;
import project.pageobjects.FilterDrawer;
import project.pageobjects.LoginPage;
import project.pageobjects.OppurtunityDeck;
import project.pageobjects.PresentationDeck;
import project.utilities.AppUtils;
import project.utilities.ExcelUtils;
import project.utilities.GenericUtils;
import project.utilities.MongoDBUtils;
import project.utilities.OracleDBUtils;
import project.utilities.RestServiceUtils;
import project.utilities.SeleniumUtils;
import project.variables.JsonBody;
import project.variables.OracleDBQueries;
import project.variables.ProjectVariables;
import project.pageobjects.PresentationProfile;
import project.pageobjects.SubsequentPage;

public class SubSequentReleaseStepDef extends ScenarioSteps {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	LoginPage onLoginPage;
	FilterDrawer onFilterDrawer;
	ProjectVariables projectVariables;
	AppUtils refAppUtils;
	SeleniumUtils  objSeleniumUtils;
	CPWPage oCPWPage;
	GenericUtils oGenericUtils;
	OppurtunityDeck oOppurtunityDeck;
	PresentationProfile oPresentationProfile;
	PresentationDeck oPresentationDeck;
	SubsequentPage oSubsequentPage;
	MongoDBUtils oMongoDBUtils = new MongoDBUtils();
	RestServiceUtils oRestServiceUtils = new RestServiceUtils();
	
	//############################# To set up the data in cpw and PM for the given subsequent release change ###############################################
	@Step
	public void setUpDataforELLChanges(String user,String disposition,String UIchangetype,String dptype) throws Throwable {
		int i =1;
		boolean blnRows=false;
		Serenity.setSessionVariable("user").to(user);
		
		//To load the excel data
		ExcelUtils.LoadEllData();
		HashMap<Integer, HashMap<String, String>> sEllData = ProjectVariables.sSubsequentEllData;
		
		int iRowCount = sEllData.size();
		try
		{
			for (i=1;i<=iRowCount;i++)
			{
				String Change =sEllData.get(i).get("i_Change");
				String sExecution = sEllData.get(i).get("Execution");
				String sdptype = sEllData.get(i).get("i_DP Type");
				String sdisposition = sEllData.get(i).get("Disposition_captured");
				if (sExecution.equalsIgnoreCase("Y"))
				{
					blnRows = true;
					switch(sdptype)
					{
					case "eLL":
						//Method To set up the data in CPW
						user_setup_the_data_for_CPW(i,Change);
						break;
					case "eLL with out oppty":
						oCPWPage.geteLLDPsnotpartofoppty(i,user,sdisposition);
						
						break;
					case "eLL with oppty":
						oCPWPage.geteLLDPspartofoppty(i,user,sdisposition);
						break;
						default:
						Assert.assertTrue("case not found::"+dptype, false);
						break;
					}
					
					String sMPKey = Serenity.sessionVariableCalled("mpKey");
					String sTpcKey = Serenity.sessionVariableCalled("TopicKey");
					String sRlsLogKey = Serenity.sessionVariableCalled("releaseLogKey");
					String sMidRuleKey = Serenity.sessionVariableCalled("midRuleKey");
					
					ExcelUtils.setELLCellData(i, "MP_KEY", sMPKey);
					ExcelUtils.setELLCellData(i, "TOPIC_KEY", sTpcKey);					
					ExcelUtils.setELLCellData(i, "ReleaseLogKey", sRlsLogKey);
					ExcelUtils.setELLCellData(i, "Mid_RuleKey", sMidRuleKey);
					
					if (ExcelUtils.getELLCellData(i,"Disposition_captured").equalsIgnoreCase("Present")){
						//Method To set up the data in PM
						user_setup_the_data_for_PM(i,ExcelUtils.getELLCellData(i, "i_DP Key"));	
					}
					
					updateELLOracledb(i);
					
					//Method to set up excel data
					ExcelUtils.setELLCellData(i,"Status","PASSED");
					ExcelUtils.setELLCellData(i,"Execution","N");
				}
			}
		} 
		catch (Exception e) 
		{
			ExcelUtils.setELLCellData(i,"Status","FAILED");
			ExcelUtils.setELLCellData(i,"Execution","N");
			ExcelUtils.setELLCellData(i,"Reason",e.getMessage()+"at line number"+e.getStackTrace()[1].getLineNumber());
			//GenericUtils.Verify("Object not found , Failed due to :=" + e.getMessage(), "PASSED");			
		}
		finally 
		{	
			
			if(blnRows&&i<= iRowCount)
			{
				//ExcelUtils.setELLCellData(i,"Status","Failed");
				ExcelUtils.setELLCellData(i,"Execution","N");
				setUpDataforELLChanges(user,disposition,UIchangetype,dptype);
			}
		}

		Path report = Paths.get("src//test//resources//ELLHierarchy.xlsx");
		Serenity.recordReportData().asEvidence().withTitle("ELL Hierarachy").downloadable().fromFile(report);
			
	}
	
	@Step
	public void user_setup_the_data_for_PM(int i,String sDPKey) throws Throwable{
			
		String spps =Serenity.sessionVariableCalled("pps").toString();
		String[] sPPS = spps.split(";");

		createandAssignSubSequentPresentations(i,sDPKey,sPPS[0], "Prstn_NotSet_RFP");
		
		createandAssignSubSequentPresentations(i,sDPKey, sPPS[1], "Prstn_Set_RFP");

	}
		
	@Step
	public void createandAssignSubSequentPresentations(int i,String sDPKey,String sPPS,String sPresType) throws Throwable{
		
		String sClient = Serenity.sessionVariableCalled("client").toString();
		String sPresentation=oPresentationProfile.createPresentationThroughService(Serenity.sessionVariableCalled("user").toString(),sClient,"","","","");
		String sMedicalPolicy = ExcelUtils.getELLCellData(i,"Medical_Policy");
		String sTopic = ExcelUtils.getELLCellData(i,"Topic");
		
		Serenity.setSessionVariable("profileId").to(StringUtils.substringBefore(sPresentation, "-"));
		Serenity.setSessionVariable("PresentationName").to(StringUtils.substringAfter(sPresentation, "-"));
		Serenity.setSessionVariable("DPkey").to(sDPKey);
		System.out.println("ProfileID====>"+Serenity.sessionVariableCalled("profileId"));
		System.out.println("PresentationName====>"+Serenity.sessionVariableCalled("PresentationName"));		
		ExcelUtils.setELLCellData(i, sPresType, Serenity.sessionVariableCalled("PresentationName"));
		if (sPresType.equalsIgnoreCase("Prstn_NotSet_RFP"))
		{
			oPresentationDeck.assignSubsequentDPstoCreatedProfile(sDPKey,sClient, sMedicalPolicy, sTopic,sPPS, Serenity.sessionVariableCalled("PresentationName"));
			ExcelUtils.setELLCellData(i,"Prstn_NotSet_RFP_PPS",sPPS);
		}
		else{
			oPresentationDeck.assignSubsequentDPstoCreatedProfile(sDPKey,sClient, sMedicalPolicy, sTopic,sPPS, Serenity.sessionVariableCalled("PresentationName"));
			captureDisposition(i);
			getRawSavingsforPPS(i,sPPS,sDPKey);
			ExcelUtils.setELLCellData(i,"O_Savings_NotSet_RFP",Serenity.sessionVariableCalled("RawSavings"));
			ExcelUtils.setELLCellData(i,"Prstn_Set_RFP_PPS",sPPS);
			}
		}
	
	@Step
	public void captureDisposition(int i) throws Throwable{
		
		String sClient = ExcelUtils.getELLCellData(i, "Client");		
		String sUserName = Serenity.sessionVariableCalled("user");
		userLogsinCPDPMApplication(sUserName,"PM");
		onFilterDrawer.user_selects_given_value_from_Client_drop_down_list(sClient);
		oPresentationProfile.clickPresentationProfile();		
		oGenericUtils.clickOnElement("u", "Expand All");		
		oPresentationProfile.selectDPsatGivenLevelatPresView("DP ALL");		
		Serenity.setSessionVariable("Decision").to("Approve");
		oPresentationProfile.captureDecision("Approve");	
	}
	
	@Step
	public void userLogsinCPDPMApplication(String sUserName, String sApplication) 

	{	
		Serenity.setSessionVariable("user").to(sUserName);		
		onLoginPage.launchCPDApplication();			
		System.out.println("CPD application is launched successfully.............");
		try {
			
		if(sUserName.isEmpty())
		{	
			waitFor(ProjectVariables.TImeout_2_Seconds);
			onLoginPage.enterUserName(projectVariables.USER_NAME);		
			onLoginPage.enterPassword(GenericUtils.decode(projectVariables.PASSWORD));		
			onLoginPage.selectApplicationFromDrowpdown("PM");
			onLoginPage.clickLoginButton();
			objSeleniumUtils.waitForContentLoad();	
			}

			waitFor(ProjectVariables.TImeout_2_Seconds);
			onLoginPage.enterUserName(sUserName);		
			String sPassword = GenericUtils.gfnReadDataFromPropertyfile(sUserName, ProjectVariables.sTestUsers);
			onLoginPage.enterPassword(GenericUtils.decode(sPassword));		
			onLoginPage.selectApplicationFromDrowpdown(sApplication);
			onLoginPage.clickLoginButton();
			objSeleniumUtils.waitForContentLoad();	
			
		} catch (Exception e) {
			Assert.assertTrue("LoginPage details not entered "+e.getMessage(),false);
		}
	}
	
	@Step
	public void getRawSavingsforPPS(int i,String sPPS, String DPKey){
		
		String sClientKey = Serenity.sessionVariableCalled("clientkey");
		String[] pps = StringUtils.split(sPPS, "-");
		String payershort= pps[0];	
		String claimtype=pps[2];
		String Insurance=pps[1];
		
		Bson MatchFilter = Filters.and(Filters.eq("_id.clientKey", Long.valueOf(sClientKey)),
				Filters.eq("disposition.desc", "Present"),Filters.eq("subRule.hierarchy.dpKey", Long.valueOf(DPKey)),
				Filters.eq("_id.payerShort",payershort),Filters.eq("insuranceDesc",Insurance),
				Filters.eq("_id.claimType", claimtype));
		MongoDBUtils.GetDBValuesBasedonAggregation(MatchFilter, "RAW SAVINGS BASED ON DP");
		
	}
		
	@Step
	public void user_setup_the_data_for_CPW( int rowno, String sChange) throws Exception
	{
		List<String> PPSList= new ArrayList<>();
		
		String disposition=ProjectVariables.sSubsequentEllData.get(rowno).get("Disposition_captured");
		String dpKey=ProjectVariables.sSubsequentEllData.get(rowno).get("i_DP Key");
		String Src = "ell-gen-oppty";
		String MedicalPolicy=ProjectVariables.sSubsequentEllData.get(rowno).get("Medical_Policy");
		String Topic =ProjectVariables.sSubsequentEllData.get(rowno).get("Topic");
		
		String User = Serenity.sessionVariableCalled("user").toString();
		Serenity.setSessionVariable("Medicalpolicy").to(MedicalPolicy);
		Serenity.setSessionVariable("user").to(User);
		Serenity.setSessionVariable("Topic").to(Topic);
		Serenity.setSessionVariable("DPkey").to(dpKey);
		Serenity.setSessionVariable("Disposition").to(disposition);
		
		
		//Mongo method to get mp,topic and pps
		PPSList=oCPWPage.getPPSforthegiveneLLDP(dpKey,User);
		
		//To retrieve the exact PPS
		CPWPage.getPPSfromthegiven(PPSList);
		
		String Client=Serenity.sessionVariableCalled("client").toString();
		String spps =Serenity.sessionVariableCalled("pps").toString();
		String sInsuranceKeys=Serenity.sessionVariableCalled("Insurancekeys").toString();
		
	
		//Capture method through service
		oCPWPage.Capture_the_data_for_the_pipeline(disposition,sInsuranceKeys,User,dpKey,Src,Client);
		
		//To set data into excel
		ExcelUtils.setELLCellData(rowno, "PPS", spps);
		ExcelUtils.setELLCellData(rowno, "Client", Client);
	}
	
	@Step
	public static boolean  updateELLOracledb(int iRow) throws IOException, InterruptedException{
	       
	       String Change = ProjectVariables.sSubsequentEllData.get(iRow).get("i_Change");
	       String ReleaseLogKey = ExcelUtils.getELLCellData(iRow,"ReleaseLogKey");
	       
	       String sPrvsQuery = null;    
	       String sUpdateQuery = null;
	       String sPrvsChangeDesc = null;
	       String sCurrentChangeDesc = null;
	       
	       switch (Change.toUpperCase()){
	       
	       case "DP DESCRIPTION":
	           String DpKey = ExcelUtils.getELLCellData(iRow,"i_DP Key");
	           sPrvsQuery = OracleDBQueries.getOracleDBQuery("PREVIOUS DP DESC CHANGE", DpKey, ReleaseLogKey, "");
	           sUpdateQuery = OracleDBQueries.getOracleDBQuery("UPDATE DP DESC CHANGE", DpKey, ReleaseLogKey, "");

	           sPrvsChangeDesc = OracleDBUtils.executeSQLQuery(sPrvsQuery,ProjectVariables.DB_CONNECTION_URL,ProjectVariables.elluser,ProjectVariables.ellpassword);
		       ExcelUtils.setELLCellData(iRow, "Previous_Change", sPrvsChangeDesc);
		       
		       OracleDBUtils.executeUpdateSQLQuery(sUpdateQuery);
		       Thread.sleep(3000);
		       sCurrentChangeDesc = OracleDBUtils.executeSQLQuery(sPrvsQuery,ProjectVariables.DB_CONNECTION_URL,ProjectVariables.elluser,ProjectVariables.ellpassword);
		       ExcelUtils.setELLCellData(iRow, "Current_Change", sCurrentChangeDesc);
		       
	           break;
	           
	       case "TOPIC TITLE":
	           String TPKey = ExcelUtils.getELLCellData(iRow,"TOPIC_KEY");
	           sPrvsQuery = OracleDBQueries.getOracleDBQuery("PREVIOUS TOPIC TITLE CHANGE", TPKey, ReleaseLogKey, "");
	           sUpdateQuery = OracleDBQueries.getOracleDBQuery("UPDATE TOPIC TITLE CHANGE", TPKey, ReleaseLogKey, "");

	           sPrvsChangeDesc = OracleDBUtils.executeSQLQuery(sPrvsQuery,ProjectVariables.DB_CONNECTION_URL,ProjectVariables.elluser,ProjectVariables.ellpassword);
		       ExcelUtils.setELLCellData(iRow, "Previous_Change", sPrvsChangeDesc);
		       
		       OracleDBUtils.executeUpdateSQLQuery(sUpdateQuery);

		       sCurrentChangeDesc = OracleDBUtils.executeSQLQuery(sPrvsQuery,ProjectVariables.DB_CONNECTION_URL,ProjectVariables.elluser,ProjectVariables.ellpassword);
		       ExcelUtils.setELLCellData(iRow, "Current_Change", sCurrentChangeDesc);
		       
	           break;
	           
	       case "MP TITLE":
	           String MPKey = ExcelUtils.getELLCellData(iRow,"MP_KEY");
	           sPrvsQuery = OracleDBQueries.getOracleDBQuery("PREVIOUS MP TITLE CHANGE", MPKey, ReleaseLogKey, "");
	           sUpdateQuery = OracleDBQueries.getOracleDBQuery("UPDATE MP TITLE CHANGE", MPKey, ReleaseLogKey, "");

	           sPrvsChangeDesc = OracleDBUtils.executeSQLQuery(sPrvsQuery,ProjectVariables.DB_CONNECTION_URL,ProjectVariables.elluser,ProjectVariables.ellpassword);
		       ExcelUtils.setELLCellData(iRow, "Previous_Change", sPrvsChangeDesc);
		       
		       OracleDBUtils.executeUpdateSQLQuery(sUpdateQuery);

		       sCurrentChangeDesc = OracleDBUtils.executeSQLQuery(sPrvsQuery,ProjectVariables.DB_CONNECTION_URL,ProjectVariables.elluser,ProjectVariables.ellpassword);
		       ExcelUtils.setELLCellData(iRow, "Current_Change", sCurrentChangeDesc);
	           break;
	           
	       case "DP SORTORDER":
	           String sDpKey = ExcelUtils.getELLCellData(iRow,"i_DP Key");
	           sPrvsQuery = OracleDBQueries.getOracleDBQuery("PREVIOUS DP SORTORDER CHANGE", sDpKey, ReleaseLogKey, "");

	           sPrvsChangeDesc = OracleDBUtils.executeSQLQuery(sPrvsQuery,ProjectVariables.DB_CONNECTION_URL,ProjectVariables.elluser,ProjectVariables.ellpassword);
	           ExcelUtils.setELLCellData(iRow, "Previous_Change", sPrvsChangeDesc);
	           
	           String sSortOrder = null;
	           if (sPrvsChangeDesc.equalsIgnoreCase("1")){
	                  sSortOrder = "2";
	               }else{
	               sSortOrder = "1";
	           }
	           sUpdateQuery = OracleDBQueries.getOracleDBQuery("UPDATE DP SORTORDER CHANGE",sDpKey, ReleaseLogKey, sSortOrder);
	           
	           OracleDBUtils.executeUpdateSQLQuery(sUpdateQuery);
	    
	           sCurrentChangeDesc = OracleDBUtils.executeSQLQuery(sPrvsQuery,ProjectVariables.DB_CONNECTION_URL,ProjectVariables.elluser,ProjectVariables.ellpassword);
	           ExcelUtils.setELLCellData(iRow, "Current_Change", sCurrentChangeDesc);

	           break;
	           
	       case "PRIMARY REFERENCE":
	           String sMidRule = ExcelUtils.getELLCellData(iRow, "Mid_RuleKey");
	           sPrvsQuery = OracleDBQueries.getOracleDBQuery("PREVIOUS PRIMARY REFERENCE CHANGE", sMidRule, ReleaseLogKey, "");
	           sUpdateQuery = OracleDBQueries.getOracleDBQuery("UPDATE PRIMARY REFRENCE CHANGE", sMidRule, ReleaseLogKey, "");
	           
	           sPrvsChangeDesc = OracleDBUtils.executeSQLQuery(sPrvsQuery,ProjectVariables.DB_CONNECTION_URL,ProjectVariables.elluser,ProjectVariables.ellpassword);
		       ExcelUtils.setELLCellData(iRow, "Previous_Change", sPrvsChangeDesc);
		       
		       OracleDBUtils.executeUpdateSQLQuery(sUpdateQuery);

		       sCurrentChangeDesc = OracleDBUtils.executeSQLQuery(sPrvsQuery,ProjectVariables.DB_CONNECTION_URL,ProjectVariables.elluser,ProjectVariables.ellpassword);
		       ExcelUtils.setELLCellData(iRow, "Current_Change", sCurrentChangeDesc);
	           break;
	           
	       case "TOPIC DESCRIPTION":
	    	   String sTPKey = ExcelUtils.getELLCellData(iRow,"TOPIC_KEY");
	           sPrvsQuery = OracleDBQueries.getOracleDBQuery("PREVIOUS TOPIC DESC CHANGE", sTPKey, ReleaseLogKey, "");
	           sUpdateQuery = OracleDBQueries.getOracleDBQuery("UPDATE TOPIC DESC CHANGE", sTPKey, ReleaseLogKey, "");

	           sPrvsChangeDesc = OracleDBUtils.executeSQLQuery(sPrvsQuery,ProjectVariables.DB_CONNECTION_URL,ProjectVariables.elluser,ProjectVariables.ellpassword);
		       ExcelUtils.setELLCellData(iRow, "Previous_Change", sPrvsChangeDesc);
		       
		       OracleDBUtils.executeUpdateSQLQuery(sUpdateQuery);

		       sCurrentChangeDesc = OracleDBUtils.executeSQLQuery(sPrvsQuery,ProjectVariables.DB_CONNECTION_URL,ProjectVariables.elluser,ProjectVariables.ellpassword);
		       ExcelUtils.setELLCellData(iRow, "Current_Change", sCurrentChangeDesc);
		       break;
		       
	       case "DP RECAT":
	    	   String strDPKey = ExcelUtils.getELLCellData(iRow,"i_DP Key");
	           sPrvsQuery = OracleDBQueries.getOracleDBQuery("PREVIOUS DP RECAT CHANGE", strDPKey, ReleaseLogKey, "");
	           
	           sPrvsChangeDesc = OracleDBUtils.executeSQLQuery(sPrvsQuery,ProjectVariables.DB_CONNECTION_URL,ProjectVariables.elluser,ProjectVariables.ellpassword);
		       ExcelUtils.setELLCellData(iRow, "Previous_Change", sPrvsChangeDesc);
		       
		       String sTopicKey = null;
	           if (sPrvsChangeDesc.equalsIgnoreCase("30")){
	        	   sTopicKey = "52";
	               }else{
	                sTopicKey = "30";
	           }
		       
		       sUpdateQuery = OracleDBQueries.getOracleDBQuery("UPDATE DP RECAT CHANGE", strDPKey, ReleaseLogKey, sTopicKey);
		       
		       OracleDBUtils.executeUpdateSQLQuery(sUpdateQuery);

		       sCurrentChangeDesc = OracleDBUtils.executeSQLQuery(sPrvsQuery,ProjectVariables.DB_CONNECTION_URL,ProjectVariables.elluser,ProjectVariables.ellpassword);
		       ExcelUtils.setELLCellData(iRow, "Current_Change", sCurrentChangeDesc);
		       break;
		       
	       case "TOPIC RECAT":
	    	   String strTPKey = ExcelUtils.getELLCellData(iRow,"TOPIC_KEY");
	           sPrvsQuery = OracleDBQueries.getOracleDBQuery("PREVIOUS TOPIC RECAT CHANGE", strTPKey, ReleaseLogKey, "");
	           sPrvsChangeDesc = OracleDBUtils.executeSQLQuery(sPrvsQuery,ProjectVariables.DB_CONNECTION_URL,ProjectVariables.elluser,ProjectVariables.ellpassword);
		       ExcelUtils.setELLCellData(iRow, "Previous_Change", sPrvsChangeDesc);
		       
	           String sMPKey = null;
	           if (sPrvsChangeDesc.equalsIgnoreCase("77")){
	        	   sMPKey = "48";
	               }else{
	               sMPKey = "77";
	           }

	           sUpdateQuery = OracleDBQueries.getOracleDBQuery("UPDATE TOPIC RECAT CHANGE", strTPKey, ReleaseLogKey, sMPKey);

		       OracleDBUtils.executeUpdateSQLQuery(sUpdateQuery);

		       sCurrentChangeDesc = OracleDBUtils.executeSQLQuery(sPrvsQuery,ProjectVariables.DB_CONNECTION_URL,ProjectVariables.elluser,ProjectVariables.ellpassword);
		       ExcelUtils.setELLCellData(iRow, "Current_Change", sCurrentChangeDesc);
		       break;
 
	       }

	       return true;
	    
	    }
	@Step
	public void userSetsDataForChangeOpportunity(String user, String changeType, String changeOpportunity) throws Exception {
		oSubsequentPage.setdataforgivenChangeOpportunity(user, changeType, changeOpportunity);
	}

	@Step
	public void postChangeOpportunityData(String changeType) throws ParseException, IOException {
		oSubsequentPage.postChangeOptyData(changeType);
	}

	@Step
	public void verifyDataForChangeOpportunity(String changeType, String changeOpportunity, String criteria) throws Exception {
		oSubsequentPage.verifydataforgivenChangeOpportunity(changeType, changeOpportunity, criteria);
	}

}
