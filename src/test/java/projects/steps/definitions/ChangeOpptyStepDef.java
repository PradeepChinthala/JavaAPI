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
import project.utilities.SeleniumUtils;
import project.variables.JsonBody;
import project.variables.OracleDBQueries;
import project.variables.ProjectVariables;
import project.pageobjects.PresentationProfile;

public class ChangeOpptyStepDef extends ScenarioSteps {
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
	MongoDBUtils oMongoDBUtils = new MongoDBUtils();
	
	
	@Step
	public void CreateChngSetup(String sChange) throws Exception {
		
		setdataforgivenChangeOpportunity("iht_ittest05","CHANGE DP DESC","RULE CHANGE , REFERENCE CHANGE");
		postChangeOptyData("CHANGE DP DESC");
	}
	
	@Step
	public void setdataforgivenChangeOpportunity(String user,String Changetype,String Changeoppty) throws Exception
	{
		//List<String> PPSList= new ArrayList<>();
		//PresentationProfile oPresentationProfile=this.switchToPage(PresentationProfile.class);
		boolean bstatus=false;
		ProjectVariables.clientKeysList.clear();
		EnvironmentVariables environmentVariables = SystemEnvironmentVariables.createEnvironmentVariables();
		String restURI = EnvironmentSpecificConfiguration.from(environmentVariables).getProperty("restapi.baseuri");
		System.out.println("Rest API::"+restURI);
		
		//Method to get clientkeys for the given user
		CPWPage.RetrievetheClientdatafromtheResponse(user, restURI + ProjectVariables.CLIENT_TEAM_DATA_ENDPOINT);
		
		for (String clientkey : ProjectVariables.clientKeysList) 
		{
			//To get unique pps from service for the given clientkey
			oPresentationProfile.getUniquePPSfromClientconfigService(clientkey);
			switch(Changetype)
			{
				case "CHANGE DP DESC":
					//mongo method to set data for changeDPDesc
					bstatus=MongoDBUtils.getDataforgivenChangeoppty(clientkey,Changeoppty);
					//MongoDBUtils.getDataforgivenChangeoppty(clientkey,"RULE CHANGE , REFERENCE CHANGE");
					//MongoDBUtils.getDataforgivenChangeoppty(clientkey, "RECAT DP");
					//MongoDBUtils.getDataforgivenChangeoppty(clientkey, "CHANGE DP DESC");
				break;
				default:
					GenericUtils.Verify("Case not found::"+Changeoppty, false);
					
				break;
			
			}
		
		
			if(bstatus)
			{
				break;
			}
		}
		
		if(!bstatus)
		{
			Assert.assertTrue("unable to find data in DB for given changetype::"+Changeoppty, false);
		}
		
		
	}
	
	@Step
	public void postChangeOptyData(String sChange) throws ParseException, IOException {
		
		HashMap<String,Object> parametersMap1 = new HashMap<String,Object>();
		
		switch (sChange.toUpperCase()) {
		
		case "CHANGE DP DESC":
		parametersMap1.put("PREV_MP", Serenity.sessionVariableCalled("PREV_MP"));
		/*parametersMap1.put("PREV_MP_TITLE", Serenity.sessionVariableCalled("PREV_MP_TITLE"));
		parametersMap1.put("CUR_MP", Serenity.sessionVariableCalled("CUR_MP"));
		parametersMap1.put("CUR_MP_TITLE", Serenity.sessionVariableCalled("CUR_MP_TITLE"));
		parametersMap1.put("PREV_TOPIC", Serenity.sessionVariableCalled("PREV_TOPIC"));
		parametersMap1.put("PREV_TOPIC_TITLE", Serenity.sessionVariableCalled("PREV_TOPIC_TITLE"));
		parametersMap1.put("CUR_TOPIC", Serenity.sessionVariableCalled("CUR_TOPIC"));
		parametersMap1.put("CUR_TOPIC_TITLE", Serenity.sessionVariableCalled("CUR_TOPIC_TITLE"));
		parametersMap1.put("CUR_DP", Serenity.sessionVariableCalled("CUR_DP"));
		parametersMap1.put("PREV_DP", Serenity.sessionVariableCalled("PREV_DP"));
		parametersMap1.put("CUR_DP_DESC", Serenity.sessionVariableCalled("CUR_DP_DESC"));
		parametersMap1.put("PREV_DP_DESC", Serenity.sessionVariableCalled("PREV_DP_DESC"));
		parametersMap1.put("CUR_MID_RULE", Serenity.sessionVariableCalled("CUR_MID_RULE"));
		parametersMap1.put("PREV_MID_RULE", Serenity.sessionVariableCalled("PREV_MID_RULE"));
		parametersMap1.put("CUR_SUB_RULE_KEY", Serenity.sessionVariableCalled("CUR_SUB_RULE_KEY"));
		parametersMap1.put("PREV_SUB_RULE_KEY", Serenity.sessionVariableCalled("PREV_SUB_RULE_KEY"));*/
		
		String ssBody = JsonBody.getRequestPayload("getDescChange",parametersMap1);
		
		System.out.println("Session ID ==>"+ssBody);
		String ssEndPoint = ProjectVariables.sServices.get("getDescChange").get("EndpointURL");

		}
	
	}

}
