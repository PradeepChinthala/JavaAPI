package project.pageobjects;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import org.apache.commons.lang3.StringUtils;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;
import org.junit.Assert;

import io.restassured.response.Response;
import net.serenitybdd.core.Serenity;
import net.serenitybdd.core.environment.EnvironmentSpecificConfiguration;
import net.serenitybdd.core.pages.PageObject;
import net.thucydides.core.annotations.Step;
import net.thucydides.core.util.EnvironmentVariables;
import net.thucydides.core.util.SystemEnvironmentVariables;
import project.utilities.GenericUtils;
import project.utilities.MongoDBUtils;
import project.utilities.RestServiceUtils;
import project.variables.JsonBody;
import project.variables.ProjectVariables;

public class SubsequentPage extends PageObject {

	RestServiceUtils oRestServiceUtils = new RestServiceUtils();
	CPWPage oCPWPage = new CPWPage();

	// Method to set data for Change Opportunities
	public void setdataforgivenChangeOpportunity(String user, String Changetype, String Changeoppty) throws Exception {
		// List<String> PPSList= new ArrayList<>();
		PresentationProfile oPresentationProfile = this.switchToPage(PresentationProfile.class);
		boolean bstatus = false;
		ProjectVariables.clientKeysList.clear();
		EnvironmentVariables environmentVariables = SystemEnvironmentVariables.createEnvironmentVariables();
		String restURI = EnvironmentSpecificConfiguration.from(environmentVariables).getProperty("restapi.baseuri");
		System.out.println("Rest API::" + restURI);

		// Method to get clientkeys for the given user
		CPWPage.RetrievetheClientdatafromtheResponse(user, restURI + ProjectVariables.CLIENT_TEAM_DATA_ENDPOINT);

		for (String clientkey : ProjectVariables.clientKeysList) {
			switch (Changetype) {
			case "RECAT RULE":
				bstatus=MongoDBUtils.getDataforRuleRecatChange(clientkey,Changeoppty);
				break;
			case "CHANGE DP DESC":
				// mongo method to set data for changeDPDesc
				bstatus = MongoDBUtils.getDataforgivenChangeoppty(clientkey, Changeoppty);
				
				break;
			case "RECAT DP":
			case "DEACT DP":
				bstatus=MongoDBUtils.getDataforgivenChangeoppty(clientkey,Changeoppty);//CHANGE DP DESC
				ArrayList<String> ellList = new ArrayList<String>();
				if(bstatus)
				{
					
					//To get distinct ellchanges from the given clientkey
					ellList.addAll(MongoDBUtils.getdistincteLlChanges(clientkey));
					for (String ellchange : ellList) 
					{
						if(!ellchange.contains("DEACT")&&!ellchange.contains("RECAT DP"))
						{
							//To retrieve the cur dpkey and dpdesc to set target dp in DPDeact change
							bstatus=MongoDBUtils.getTargetDataforgivenchange(clientkey,ellchange);
						}
						if(bstatus)
						{
							break;
						}
					}
					
					if(Serenity.sessionVariableCalled("CUR_DP")==null)
					{
						GenericUtils.Verify("Unable to find target DPkey for 'DP DEACT' change", false);
					}
					
				}
				
			break;
			case "CLAIM TYPE ADDED":
			case "CLAIM TYPE REMOVED":
				// mongo method to set data for claimtype added/removed
				bstatus = MongoDBUtils.getDataClaimTypeChangeoppty(clientkey, Changeoppty,Changetype);
				
			break;
			case "NEW DP":
				//bstatus=MongoDBUtils.getDataforclamiTypeChangeoppty(clientkey, Changeoppty);
				bstatus = MongoDBUtils.getDataforgivenChangeoppty(clientkey, Changeoppty);
				
				break;
			case "NEW RULE":
			case "RULE REFERENCE CHANGE" :
			case "RULE VERSION CHANGE":
			case "RULE DESC CHANGE":
			case "RULE DEACT CHANGE":
			case "NEW MP TOPIC RECAT":
			case "NEW TOPIC":
				
				// mongo method to set data for changeDPDesc
				bstatus = MongoDBUtils.getDataforgivenChangeoppty(clientkey,Changetype, Changeoppty);
				// MongoDBUtils.getDataforgivenChangeoppty(clientkey,"RULE CHANGE , REFERENCE
				// CHANGE");
				// MongoDBUtils.getDataforgivenChangeoppty(clientkey, "RECAT DP");
				// MongoDBUtils.getDataforgivenChangeoppty(clientkey, "CHANGE DP DESC");
				break;
			default:
				GenericUtils.Verify("Case not found::" + Changetype, false);
				break;

			}
			if (bstatus) {
				break;
			}
		}

		if (!bstatus) {
			Assert.assertTrue("unable to find data in DB for given changetype::" + Changeoppty, false);
		}

	}

	// Method to verify data for Change Opportunities post pipeline
	public void verifydataforgivenChangeOpportunity(String Changetype, String Changeoppty, String criteria)
			throws Exception {
		 boolean bstatus=false;
		 long expPPScount=0l;
		 String NEWDP=null;
		 String OLDDP=null;
         Long iPPSCount=0l;
         String clientkey=Serenity.sessionVariableCalled("clientkey");
          expPPScount=Long.valueOf(Serenity.sessionVariableCalled("PPScount").toString());
         
         switch(Changetype)
         {
             case "CHANGE DP DESC":
                  //DB method verify change opportunity data post pipeline
                  iPPSCount=MongoDBUtils.verifyDataforgivenChangeoppty(clientkey, Changeoppty,criteria);
                  String Matchfilter=Serenity.sessionVariableCalled("Matchfilter").toString();
                  switch(criteria)
                  {
                  case "Without RFP":
                  case "Unassigned":
                       bstatus=expPPScount==iPPSCount;
                       GenericUtils.Verify("Expected PPSCount::"+expPPScount+",Actual PPSCount::"+iPPSCount+",Post pipeline for changetype::"+Changetype+",PrecondtionChanges::"+Changeoppty+",Matchfilter::"+Matchfilter, bstatus);
                       break;
                  case "With RFP":
                       bstatus=iPPSCount==0;
                       GenericUtils.Verify("PPSCount::"+iPPSCount+",Post pipeline for changetype::"+Changetype+",PrecondtionChanges::"+Changeoppty+",Matchfilter::"+Matchfilter, bstatus);
                       break;
                  default:
                       GenericUtils.Verify("case not found::"+criteria, false);            
                       break;
                  }                                    
             break;
             case "DEACT DP":
                  String targetDP=Serenity.sessionVariableCalled("CUR_DP");
                  String sourceDP=Serenity.sessionVariableCalled("PREV_DP");
                  switch(criteria)
                  {
                  case "Without RFP":
                  case "Unassigned":
                       //DB method verify De Activation change opportunity data post pipeline
                       MongoDBUtils.verifyDPDeactiveChangePostpipeline(sourceDP, criteria,"Source",Changeoppty);//Changeoppty --"RECATEGORIZED RULE , DEACT DP"
                       MongoDBUtils.verifyDPDeactiveChangePostpipeline(targetDP, criteria,"Target",Changeoppty);//Changeoppty --"RECATEGORIZED RULE , DEACT DP"
                       break;
                       
                  case "With RFP":
                       //DB method verify De Activation change opportunity data post pipeline
                       MongoDBUtils.verifyDPDeactiveChangePostpipeline(sourceDP, criteria,"Source",Changeoppty);
                       break;
                  default:
                       GenericUtils.Verify("case not found::"+criteria, false);            
                       break;
                  }         
                  
             break;
             case "NEW DP":
            	 System.out.println(Serenity.sessionVariableCalled("CUR_DP").toString());
            	  NEWDP=Serenity.sessionVariableCalled("CUR_DP").toString();
                  OLDDP=Serenity.sessionVariableCalled("PREV_DP");
                 //NEW DP VERIFICATION POST PIPELINE
            	 MongoDBUtils.verifyNewDPChangePostpipeline(NEWDP, criteria, "NEW", "RECATEGORIZED RULE , NEW DP");
         		//oLD DP VERIFICATION POST PIPELINE
         		MongoDBUtils.verifyOldDPChangePostpipeline(OLDDP, criteria, "NEW", "CLAIM TYPE ADDED");
            break;
             case "RECAT DP":
            	 String dpkey=Serenity.sessionVariableCalled("PREV_DP");
            	 //Verify at target topic side,post pipeline
            	 MongoDBUtils.verifyRECATDPChangePostpipeline(dpkey, criteria, "Source", "RECAT DP");
            	 break;
             case "RECAT RULE":
            	  NEWDP=Serenity.sessionVariableCalled("CUR_DP");
                  OLDDP=Serenity.sessionVariableCalled("PREV_DP");
            	MongoDBUtils.verifyRuleRecatChangePostpipeline(NEWDP, criteria, "Target", "RECATEGORIZED RULE");
         		MongoDBUtils.verifyRuleRecatChangePostpipeline(OLDDP, criteria, "Source", "RECATEGORIZED RULE");

            	 break;
             case "CLAIM TYPE ADDED":
 			case "CLAIM TYPE REMOVED":
 				OLDDP=Serenity.sessionVariableCalled("PREV_DP");
 				MongoDBUtils.verifyRuleClaimTypeChangePostpipeline(OLDDP, criteria, "Target", "RULE CHANGE , "+Changetype+"");
 				break;
 			case "NEW RULE":
            case "RULE REFERENCE CHANGE":
            case "RULE VERSION CHANGE":
            case "RULE DESC CHANGE":
            case "RULE DEACT CHANGE":
            case "NEW MP TOPIC RECAT":
            case "NEW TOPIC":
           	 iPPSCount=MongoDBUtils.verifyRuleDataforgivenChangeoppty(clientkey,Changetype, Changeoppty,criteria);
                Matchfilter=Serenity.sessionVariableCalled("Matchfilter").toString();
           	 switch(criteria)
                {
                case "Without RFP":
                case "Unassigned":
                case "With RFP":
                     bstatus=iPPSCount>0;
                     GenericUtils.Verify("Actual PPSCount::"+iPPSCount+",Post pipeline for changetype::"+Changetype+",PrecondtionChanges::"+Changeoppty+",Matchfilter::"+Matchfilter, bstatus);
                     break;                
                default:
                     GenericUtils.Verify("case not found::"+criteria, false);            
                     break;
                } 
           	 break;
             default:
                  GenericUtils.Verify("case not found::"+Changetype, false);
             break;
             
         }


	}

	public void postChangeOptyData(String sChange) throws ParseException, IOException {

		HashMap<String, Object> parametersMap1 = new HashMap<String, Object>();

		String sBody = null;
		String sEndPoint = null;
		JSONObject json=null;
		switch (sChange.toUpperCase()) {

		case "CHANGE DP DESC":

			parametersMap1.put("PREV_MP", Integer.valueOf(Serenity.sessionVariableCalled("PREV_MP")));
			parametersMap1.put("PREV_MP_TITLE", Serenity.sessionVariableCalled("PREV_MP_TITLE").toString());
			parametersMap1.put("CUR_MP", Integer.valueOf(Serenity.sessionVariableCalled("PREV_MP")));
			parametersMap1.put("CUR_MP_TITLE", Serenity.sessionVariableCalled("PREV_MP_TITLE").toString());
			parametersMap1.put("PREV_TOPIC", Integer.valueOf(Serenity.sessionVariableCalled("PREV_TOPIC")));
			parametersMap1.put("PREV_TOPIC_TITLE", Serenity.sessionVariableCalled("PREV_TOPIC_TITLE"));
			parametersMap1.put("CUR_TOPIC", Integer.valueOf(Serenity.sessionVariableCalled("PREV_TOPIC")));
			parametersMap1.put("CUR_TOPIC_TITLE", Serenity.sessionVariableCalled("PREV_TOPIC_TITLE"));
			parametersMap1.put("CUR_DP", Integer.valueOf(Serenity.sessionVariableCalled("PREV_DP")));
			parametersMap1.put("PREV_DP", Integer.valueOf(Serenity.sessionVariableCalled("PREV_DP")));
			parametersMap1.put("CUR_DP_DESC", "Automation Change - " + Serenity.sessionVariableCalled("PREV_DP_DESC"));
			parametersMap1.put("PREV_DP_DESC", Serenity.sessionVariableCalled("PREV_DP_DESC"));
			parametersMap1.put("CUR_MID_RULE", Integer.valueOf(Serenity.sessionVariableCalled("CUR_MID_RULE")));
			parametersMap1.put("PREV_MID_RULE", Integer.valueOf(Serenity.sessionVariableCalled("CUR_MID_RULE")));
			parametersMap1.put("CUR_SUB_RULE_KEY", Integer.valueOf(Serenity.sessionVariableCalled("CUR_SUB_RULE_KEY")));
			parametersMap1.put("PREV_SUB_RULE_KEY",
					Integer.valueOf(Serenity.sessionVariableCalled("CUR_SUB_RULE_KEY")));

			Serenity.setSessionVariable("CUR_DP_DESC").to((parametersMap1.get("CUR_DP_DESC")));
			sBody = JsonBody.getChangeRequestPayload("getDescChange", parametersMap1);
			System.out.println("Request Body ==>" + sBody);
			sEndPoint = ProjectVariables.sServices.get("getDescChange").get("EndpointURL");
			json=JsonBody.getChangeRequestPayload("getDescChange");
			Serenity.setSessionVariable("CUR_RELEASE_DATE").to(json.get("CUR_RELEASE_DATE"));
			Serenity.setSessionVariable("PREV_RELEASE_DATE").to(json.get("PREV_RELEASE_DATE"));

			break;

		case "DEACT DP":

			parametersMap1.put("PREV_MP", Integer.valueOf(Serenity.sessionVariableCalled("PREV_MP")));
			parametersMap1.put("PREV_MP_TITLE", Serenity.sessionVariableCalled("PREV_MP_TITLE").toString());
			parametersMap1.put("CUR_MP", Integer.valueOf(Serenity.sessionVariableCalled("PREV_MP")));
			parametersMap1.put("CUR_MP_TITLE", Serenity.sessionVariableCalled("PREV_MP_TITLE").toString());
			parametersMap1.put("PREV_TOPIC", Integer.valueOf(Serenity.sessionVariableCalled("PREV_TOPIC")));
			parametersMap1.put("PREV_TOPIC_TITLE", Serenity.sessionVariableCalled("PREV_TOPIC_TITLE"));
			parametersMap1.put("CUR_TOPIC", Integer.valueOf(Serenity.sessionVariableCalled("PREV_TOPIC")));
			parametersMap1.put("CUR_TOPIC_TITLE", Serenity.sessionVariableCalled("PREV_TOPIC_TITLE"));
			parametersMap1.put("CUR_DP", Integer.valueOf(Serenity.sessionVariableCalled("CUR_DP")));
			parametersMap1.put("PREV_DP", Integer.valueOf(Serenity.sessionVariableCalled("PREV_DP")));
			parametersMap1.put("CUR_DP_DESC", Serenity.sessionVariableCalled("CUR_DP_DESC"));
			parametersMap1.put("PREV_DP_DESC", Serenity.sessionVariableCalled("PREV_DP_DESC"));
			parametersMap1.put("CUR_MID_RULE", Integer.valueOf(Serenity.sessionVariableCalled("CUR_MID_RULE")));
			parametersMap1.put("PREV_MID_RULE", Integer.valueOf(Serenity.sessionVariableCalled("CUR_MID_RULE")));
			parametersMap1.put("CUR_SUB_RULE_KEY", Integer.valueOf(Serenity.sessionVariableCalled("CUR_SUB_RULE_KEY")));
			parametersMap1.put("PREV_SUB_RULE_KEY",
					Integer.valueOf(Serenity.sessionVariableCalled("CUR_SUB_RULE_KEY")));
			
			sBody = JsonBody.getChangeRequestPayload("getDeactivateDPChange", parametersMap1);

			System.out.println("Request Body ==>" + sBody);
			sEndPoint = ProjectVariables.sServices.get("getDeactivateDPChange").get("EndpointURL");
			json=JsonBody.getChangeRequestPayload("getDeactivateDPChange");
			Serenity.setSessionVariable("CUR_RELEASE_DATE").to(json.get("CUR_RELEASE_DATE"));
			Serenity.setSessionVariable("PREV_RELEASE_DATE").to(json.get("PREV_RELEASE_DATE"));
			break;
        case "NEW DP":

            parametersMap1.put("PREV_MP", Integer.valueOf(Serenity.sessionVariableCalled("PREV_MP")));
            parametersMap1.put("PREV_MP_TITLE", Serenity.sessionVariableCalled("PREV_MP_TITLE").toString());
            parametersMap1.put("CUR_MP", Integer.valueOf(Serenity.sessionVariableCalled("PREV_MP")));
            parametersMap1.put("CUR_MP_TITLE", Serenity.sessionVariableCalled("PREV_MP_TITLE").toString());
            parametersMap1.put("PREV_TOPIC", Integer.valueOf(Serenity.sessionVariableCalled("PREV_TOPIC")));
            parametersMap1.put("PREV_TOPIC_TITLE", Serenity.sessionVariableCalled("PREV_TOPIC_TITLE"));
            parametersMap1.put("CUR_TOPIC", Integer.valueOf(Serenity.sessionVariableCalled("PREV_TOPIC")));
            parametersMap1.put("CUR_TOPIC_TITLE", Serenity.sessionVariableCalled("PREV_TOPIC_TITLE"));
            parametersMap1.put("PREV_DP", Integer.valueOf(Serenity.sessionVariableCalled("PREV_DP")));
            parametersMap1.put("PREV_DP_DESC", Serenity.sessionVariableCalled("PREV_DP_DESC"));
            parametersMap1.put("CUR_MID_RULE", Integer.valueOf(Serenity.sessionVariableCalled("CUR_MID_RULE")));
            parametersMap1.put("PREV_MID_RULE", Integer.valueOf(Serenity.sessionVariableCalled("CUR_MID_RULE")));
            parametersMap1.put("CUR_SUB_RULE_KEY", Integer.valueOf(Serenity.sessionVariableCalled("CUR_SUB_RULE_KEY")));
            parametersMap1.put("PREV_SUB_RULE_KEY",
                      Integer.valueOf(Serenity.sessionVariableCalled("CUR_SUB_RULE_KEY")));
            
            sBody = JsonBody.getChangeRequestPayload("getNewDPChange", parametersMap1);

            System.out.println("Request Body ==>" + sBody);
            sEndPoint = ProjectVariables.sServices.get("getNewDPChange").get("EndpointURL");
            json=JsonBody.getChangeRequestPayload("getNewDPChange");
            Serenity.setSessionVariable("CUR_DP").to(json.get("CUR_DP"));
            Serenity.setSessionVariable("CUR_DP_DESC").to(json.get("CUR_DP_DESC"));
			Serenity.setSessionVariable("CUR_RELEASE_DATE").to(json.get("CUR_RELEASE_DATE"));
			Serenity.setSessionVariable("PREV_RELEASE_DATE").to(json.get("PREV_RELEASE_DATE"));
			System.out.println("CUR_DP::"+Serenity.sessionVariableCalled("CUR_DP").toString());
			System.out.println("CUR_DP_DESC::"+Serenity.sessionVariableCalled("CUR_DP_DESC").toString());
            break;

			
       case "RECAT DP":

            parametersMap1.put("PREV_MP", Integer.valueOf(Serenity.sessionVariableCalled("PREV_MP")));
            parametersMap1.put("PREV_MP_TITLE", Serenity.sessionVariableCalled("PREV_MP_TITLE").toString());
            parametersMap1.put("CUR_MP", Integer.valueOf(Serenity.sessionVariableCalled("PREV_MP")));
            parametersMap1.put("CUR_MP_TITLE", Serenity.sessionVariableCalled("PREV_MP_TITLE").toString());
            parametersMap1.put("PREV_TOPIC", Integer.valueOf(Serenity.sessionVariableCalled("PREV_TOPIC")));
            parametersMap1.put("PREV_TOPIC_TITLE", Serenity.sessionVariableCalled("PREV_TOPIC_TITLE"));
            parametersMap1.put("CUR_TOPIC", Integer.valueOf(Serenity.sessionVariableCalled("CUR_TOPIC")));
            parametersMap1.put("CUR_TOPIC_TITLE", Serenity.sessionVariableCalled("CUR_TOPIC_TITLE"));
            parametersMap1.put("CUR_DP", Integer.valueOf(Serenity.sessionVariableCalled("PREV_DP")));
            parametersMap1.put("PREV_DP", Integer.valueOf(Serenity.sessionVariableCalled("PREV_DP")));
            parametersMap1.put("CUR_DP_DESC", Serenity.sessionVariableCalled("PREV_DP_DESC"));
            parametersMap1.put("PREV_DP_DESC", Serenity.sessionVariableCalled("PREV_DP_DESC"));
            parametersMap1.put("CUR_MID_RULE", Integer.valueOf(Serenity.sessionVariableCalled("CUR_MID_RULE")));
            parametersMap1.put("PREV_MID_RULE", Integer.valueOf(Serenity.sessionVariableCalled("CUR_MID_RULE")));
            parametersMap1.put("CUR_SUB_RULE_KEY", Integer.valueOf(Serenity.sessionVariableCalled("CUR_SUB_RULE_KEY")));
            parametersMap1.put("PREV_SUB_RULE_KEY",
                      Integer.valueOf(Serenity.sessionVariableCalled("CUR_SUB_RULE_KEY")));
            
            sBody = JsonBody.getChangeRequestPayload("getDPRecatChange", parametersMap1);

            System.out.println("Request Body ==>" + sBody);
            sEndPoint = ProjectVariables.sServices.get("getDPRecatChange").get("EndpointURL");
            json=JsonBody.getChangeRequestPayload("getDPRecatChange");
           
			Serenity.setSessionVariable("CUR_RELEASE_DATE").to(json.get("CUR_RELEASE_DATE"));
			Serenity.setSessionVariable("PREV_RELEASE_DATE").to(json.get("PREV_RELEASE_DATE"));
            break;
       case "RECAT RULE":
           
           parametersMap1.put("PREV_MP", Integer.valueOf(Serenity.sessionVariableCalled("PREV_MP")));
           parametersMap1.put("PREV_MP_TITLE", Serenity.sessionVariableCalled("PREV_MP_TITLE").toString());
           parametersMap1.put("CUR_MP", Integer.valueOf(Serenity.sessionVariableCalled("PREV_MP")));
           parametersMap1.put("CUR_MP_TITLE", Serenity.sessionVariableCalled("PREV_MP_TITLE").toString());
           parametersMap1.put("PREV_TOPIC", Integer.valueOf(Serenity.sessionVariableCalled("PREV_TOPIC")));
           parametersMap1.put("PREV_TOPIC_TITLE", Serenity.sessionVariableCalled("PREV_TOPIC_TITLE"));
           parametersMap1.put("CUR_TOPIC", Integer.valueOf(Serenity.sessionVariableCalled("PREV_TOPIC")));
           parametersMap1.put("CUR_TOPIC_TITLE", Serenity.sessionVariableCalled("PREV_TOPIC_TITLE"));
           parametersMap1.put("CUR_DP", Integer.valueOf(Serenity.sessionVariableCalled("CUR_DP")));
           parametersMap1.put("PREV_DP", Integer.valueOf(Serenity.sessionVariableCalled("PREV_DP")));
           parametersMap1.put("CUR_DP_DESC", Serenity.sessionVariableCalled("CUR_DP_DESC"));
           parametersMap1.put("PREV_DP_DESC", Serenity.sessionVariableCalled("PREV_DP_DESC"));
           parametersMap1.put("CUR_MID_RULE", Integer.valueOf(Serenity.sessionVariableCalled("CUR_MID_RULE")));
           parametersMap1.put("PREV_MID_RULE", Integer.valueOf(Serenity.sessionVariableCalled("CUR_MID_RULE")));
           parametersMap1.put("CUR_VERSION", Integer.valueOf(Serenity.sessionVariableCalled("CUR_VERSION")));
           parametersMap1.put("PREV_VERSION", Integer.valueOf(Serenity.sessionVariableCalled("PREV_VERSION")));
           parametersMap1.put("CUR_SUB_RULE_KEY", Integer.valueOf(Serenity.sessionVariableCalled("CUR_SUB_RULE_KEY")));
           parametersMap1.put("PREV_SUB_RULE_KEY",
                     Integer.valueOf(Serenity.sessionVariableCalled("CUR_SUB_RULE_KEY")));
           
           sBody = JsonBody.getChangeRequestPayload("getRuleRecatChange", parametersMap1);

           System.out.println("Request Body ==>" + sBody);
           sEndPoint = ProjectVariables.sServices.get("getRuleRecatChange").get("EndpointURL");
           json=JsonBody.getChangeRequestPayload("getRuleRecatChange");
          
			Serenity.setSessionVariable("CUR_RELEASE_DATE").to(json.get("CUR_RELEASE_DATE"));
			Serenity.setSessionVariable("PREV_RELEASE_DATE").to(json.get("PREV_RELEASE_DATE"));
           break;
       case "CLAIM TYPE ADDED":
           
           parametersMap1.put("PREV_MP", Integer.valueOf(Serenity.sessionVariableCalled("PREV_MP")));
           parametersMap1.put("PREV_MP_TITLE", Serenity.sessionVariableCalled("PREV_MP_TITLE").toString());
           parametersMap1.put("CUR_MP", Integer.valueOf(Serenity.sessionVariableCalled("PREV_MP")));
           parametersMap1.put("CUR_MP_TITLE", Serenity.sessionVariableCalled("PREV_MP_TITLE").toString());
           parametersMap1.put("PREV_TOPIC", Integer.valueOf(Serenity.sessionVariableCalled("PREV_TOPIC")));
           parametersMap1.put("PREV_TOPIC_TITLE", Serenity.sessionVariableCalled("PREV_TOPIC_TITLE"));
           parametersMap1.put("CUR_TOPIC", Integer.valueOf(Serenity.sessionVariableCalled("PREV_TOPIC")));
           parametersMap1.put("CUR_TOPIC_TITLE", Serenity.sessionVariableCalled("PREV_TOPIC_TITLE"));
           parametersMap1.put("CUR_DP", Integer.valueOf(Serenity.sessionVariableCalled("PREV_DP")));
           parametersMap1.put("PREV_DP", Integer.valueOf(Serenity.sessionVariableCalled("PREV_DP")));
           parametersMap1.put("CUR_DP_DESC", Serenity.sessionVariableCalled("PREV_DP_DESC"));
           parametersMap1.put("PREV_DP_DESC", Serenity.sessionVariableCalled("PREV_DP_DESC"));
           parametersMap1.put("CUR_MID_RULE", Integer.valueOf(Serenity.sessionVariableCalled("CUR_MID_RULE")));
           parametersMap1.put("PREV_MID_RULE", Integer.valueOf(Serenity.sessionVariableCalled("CUR_MID_RULE")));
           parametersMap1.put("CUR_MID_RULE_CLAIM_TYPES", Serenity.sessionVariableCalled("CUR_MID_RULE_CLAIM_TYPES"));
           parametersMap1.put("PREV_MID_RULE_CLAIM_TYPES", Serenity.sessionVariableCalled("PREV_MID_RULE_CLAIM_TYPES"));
           parametersMap1.put("CUR_VERSION", Integer.valueOf(Serenity.sessionVariableCalled("CUR_VERSION")));
           parametersMap1.put("PREV_VERSION", Integer.valueOf(Serenity.sessionVariableCalled("CUR_VERSION")));
           parametersMap1.put("CUR_SUB_RULE_KEY", Integer.valueOf(Serenity.sessionVariableCalled("CUR_SUB_RULE_KEY")));
           parametersMap1.put("PREV_SUB_RULE_KEY",
                     Integer.valueOf(Serenity.sessionVariableCalled("CUR_SUB_RULE_KEY")));
           
           sBody = JsonBody.getChangeRequestPayload("getClaimTypeAddedChange", parametersMap1);

           System.out.println("Request Body ==>" + sBody);
           sEndPoint = ProjectVariables.sServices.get("getClaimTypeAddedChange").get("EndpointURL");
           json=JsonBody.getChangeRequestPayload("getClaimTypeAddedChange");
           
			Serenity.setSessionVariable("CUR_RELEASE_DATE").to(json.get("CUR_RELEASE_DATE"));
			Serenity.setSessionVariable("PREV_RELEASE_DATE").to(json.get("PREV_RELEASE_DATE"));
           break;
           
      case "CLAIM TYPE REMOVED":
           
    	  parametersMap1.put("PREV_MP", Integer.valueOf(Serenity.sessionVariableCalled("PREV_MP")));
          parametersMap1.put("PREV_MP_TITLE", Serenity.sessionVariableCalled("PREV_MP_TITLE").toString());
          parametersMap1.put("CUR_MP", Integer.valueOf(Serenity.sessionVariableCalled("PREV_MP")));
          parametersMap1.put("CUR_MP_TITLE", Serenity.sessionVariableCalled("PREV_MP_TITLE").toString());
          parametersMap1.put("PREV_TOPIC", Integer.valueOf(Serenity.sessionVariableCalled("PREV_TOPIC")));
          parametersMap1.put("PREV_TOPIC_TITLE", Serenity.sessionVariableCalled("PREV_TOPIC_TITLE"));
          parametersMap1.put("CUR_TOPIC", Integer.valueOf(Serenity.sessionVariableCalled("PREV_TOPIC")));
          parametersMap1.put("CUR_TOPIC_TITLE", Serenity.sessionVariableCalled("PREV_TOPIC_TITLE"));
          parametersMap1.put("CUR_DP", Integer.valueOf(Serenity.sessionVariableCalled("PREV_DP")));
          parametersMap1.put("PREV_DP", Integer.valueOf(Serenity.sessionVariableCalled("PREV_DP")));
          parametersMap1.put("CUR_DP_DESC", Serenity.sessionVariableCalled("PREV_DP_DESC"));
          parametersMap1.put("PREV_DP_DESC", Serenity.sessionVariableCalled("PREV_DP_DESC"));
          parametersMap1.put("CUR_MID_RULE", Integer.valueOf(Serenity.sessionVariableCalled("CUR_MID_RULE")));
          parametersMap1.put("PREV_MID_RULE", Integer.valueOf(Serenity.sessionVariableCalled("CUR_MID_RULE")));
          parametersMap1.put("CUR_MID_RULE_CLAIM_TYPES", Serenity.sessionVariableCalled("CUR_MID_RULE_CLAIM_TYPES"));
          parametersMap1.put("PREV_MID_RULE_CLAIM_TYPES", Serenity.sessionVariableCalled("PREV_MID_RULE_CLAIM_TYPES"));
          parametersMap1.put("CUR_VERSION", Integer.valueOf(Serenity.sessionVariableCalled("CUR_VERSION")));
          parametersMap1.put("PREV_VERSION", Integer.valueOf(Serenity.sessionVariableCalled("CUR_VERSION")));
          parametersMap1.put("CUR_SUB_RULE_KEY", Integer.valueOf(Serenity.sessionVariableCalled("CUR_SUB_RULE_KEY")));
          parametersMap1.put("PREV_SUB_RULE_KEY",
                    Integer.valueOf(Serenity.sessionVariableCalled("CUR_SUB_RULE_KEY")));
           
           sBody = JsonBody.getChangeRequestPayload("getClaimTypeRemovedChange", parametersMap1);

           System.out.println("Request Body ==>" + sBody);
           sEndPoint = ProjectVariables.sServices.get("getClaimTypeRemovedChange").get("EndpointURL");
           json=JsonBody.getChangeRequestPayload("getClaimTypeRemovedChange");
           
			Serenity.setSessionVariable("CUR_RELEASE_DATE").to(json.get("CUR_RELEASE_DATE"));
			Serenity.setSessionVariable("PREV_RELEASE_DATE").to(json.get("PREV_RELEASE_DATE"));
           break;
   
      case "NEW RULE":
          parametersMap1.put("CUR_MP", Integer.valueOf(Serenity.sessionVariableCalled("PREV_MP")));
          parametersMap1.put("CUR_MP_TITLE", Serenity.sessionVariableCalled("PREV_MP_TITLE").toString());
          parametersMap1.put("CUR_TOPIC", Integer.valueOf(Serenity.sessionVariableCalled("PREV_TOPIC")));
          parametersMap1.put("CUR_TOPIC_TITLE", Serenity.sessionVariableCalled("PREV_TOPIC_TITLE"));
          parametersMap1.put("CUR_DP", Integer.valueOf(Serenity.sessionVariableCalled("PREV_DP")));            
          parametersMap1.put("CUR_DP_DESC", Serenity.sessionVariableCalled("PREV_DP_DESC"));
          Serenity.setSessionVariable("CUR_MID_RULE").to(StringUtils.substringBetween(ProjectVariables.sServices.get("getNewRuleChange").get("RequestBody"), "\"CUR_MID_RULE\": ", ","));
          Serenity.setSessionVariable("CUR_RELEASE_DATE").to(StringUtils.substringBetween(ProjectVariables.sServices.get("getNewRuleChange").get("RequestBody"), "\"CUR_RELEASE_DATE\": ", ","));
          sBody = JsonBody.getChangeRequestPayload("getNewRuleChange", parametersMap1);
          System.out.println("Request Body ==>" + sBody);
          sEndPoint = ProjectVariables.sServices.get("getNewRuleChange").get("EndpointURL");
          break;
           
      case "RULE REFERENCE CHANGE":
           
           parametersMap1.put("PREV_MP", Integer.valueOf(Serenity.sessionVariableCalled("PREV_MP")));
           parametersMap1.put("PREV_MP_TITLE", Serenity.sessionVariableCalled("PREV_MP_TITLE").toString());
           parametersMap1.put("CUR_MP", Integer.valueOf(Serenity.sessionVariableCalled("PREV_MP")));
           parametersMap1.put("CUR_MP_TITLE", Serenity.sessionVariableCalled("PREV_MP_TITLE").toString());
           parametersMap1.put("PREV_TOPIC", Integer.valueOf(Serenity.sessionVariableCalled("PREV_TOPIC")));
           parametersMap1.put("PREV_TOPIC_TITLE", Serenity.sessionVariableCalled("PREV_TOPIC_TITLE"));
           parametersMap1.put("CUR_TOPIC", Integer.valueOf(Serenity.sessionVariableCalled("PREV_TOPIC")));
           parametersMap1.put("CUR_TOPIC_TITLE", Serenity.sessionVariableCalled("PREV_TOPIC_TITLE"));
           parametersMap1.put("CUR_DP", Integer.valueOf(Serenity.sessionVariableCalled("PREV_DP")));
           parametersMap1.put("PREV_DP", Integer.valueOf(Serenity.sessionVariableCalled("PREV_DP")));
           parametersMap1.put("CUR_DP_DESC", Serenity.sessionVariableCalled("CUR_DP_DESC"));
           parametersMap1.put("PREV_DP_DESC", Serenity.sessionVariableCalled("PREV_DP_DESC"));
           parametersMap1.put("CUR_MID_RULE", Integer.valueOf(Serenity.sessionVariableCalled("CUR_MID_RULE")));
           parametersMap1.put("PREV_MID_RULE", Integer.valueOf(Serenity.sessionVariableCalled("CUR_MID_RULE")));
           parametersMap1.put("CUR_VERSION", Integer.valueOf(Serenity.sessionVariableCalled("CUR_MID_RULE"))+2);
           parametersMap1.put("PREV_VERSION", Integer.valueOf(Serenity.sessionVariableCalled("CUR_MID_RULE")));
           parametersMap1.put("CUR_SUB_RULE_KEY", Integer.valueOf(Serenity.sessionVariableCalled("CUR_SUB_RULE_KEY")));
           parametersMap1.put("PREV_SUB_RULE_KEY",
                     Integer.valueOf(Serenity.sessionVariableCalled("CUR_SUB_RULE_KEY")));
           Serenity.setSessionVariable("CUR_RELEASE_DATE").to(StringUtils.substringBetween(ProjectVariables.sServices.get("getRuleRefChange").get("RequestBody"), "\"CUR_RELEASE_DATE\": ", ","));
           sBody = JsonBody.getChangeRequestPayload("getRuleRefChange", parametersMap1);

           System.out.println("Request Body ==>" + sBody);
           sEndPoint = ProjectVariables.sServices.get("getRuleRefChange").get("EndpointURL");
           break;
           
      case "RULE VERSION CHANGE":
           
           parametersMap1.put("PREV_MP", Integer.valueOf(Serenity.sessionVariableCalled("PREV_MP")));
           parametersMap1.put("PREV_MP_TITLE", Serenity.sessionVariableCalled("PREV_MP_TITLE").toString());
           parametersMap1.put("CUR_MP", Integer.valueOf(Serenity.sessionVariableCalled("PREV_MP")));
           parametersMap1.put("CUR_MP_TITLE", Serenity.sessionVariableCalled("PREV_MP_TITLE").toString());
           parametersMap1.put("PREV_TOPIC", Integer.valueOf(Serenity.sessionVariableCalled("PREV_TOPIC")));
           parametersMap1.put("PREV_TOPIC_TITLE", Serenity.sessionVariableCalled("PREV_TOPIC_TITLE"));
           parametersMap1.put("CUR_TOPIC", Integer.valueOf(Serenity.sessionVariableCalled("PREV_TOPIC")));
           parametersMap1.put("CUR_TOPIC_TITLE", Serenity.sessionVariableCalled("PREV_TOPIC_TITLE"));
           parametersMap1.put("CUR_DP", Integer.valueOf(Serenity.sessionVariableCalled("PREV_DP")));
           parametersMap1.put("PREV_DP", Integer.valueOf(Serenity.sessionVariableCalled("PREV_DP")));
           parametersMap1.put("CUR_DP_DESC", Serenity.sessionVariableCalled("CUR_DP_DESC"));
           parametersMap1.put("PREV_DP_DESC", Serenity.sessionVariableCalled("PREV_DP_DESC"));
           parametersMap1.put("CUR_MID_RULE", Integer.valueOf(Serenity.sessionVariableCalled("CUR_MID_RULE")));
           parametersMap1.put("PREV_MID_RULE", Integer.valueOf(Serenity.sessionVariableCalled("CUR_MID_RULE")));
           parametersMap1.put("CUR_VERSION", Integer.valueOf(Serenity.sessionVariableCalled("CUR_MID_RULE"))+2);
           parametersMap1.put("PREV_VERSION", Integer.valueOf(Serenity.sessionVariableCalled("CUR_MID_RULE")));
           parametersMap1.put("CUR_SUB_RULE_KEY", Integer.valueOf(Serenity.sessionVariableCalled("CUR_SUB_RULE_KEY")));
           parametersMap1.put("PREV_SUB_RULE_KEY",
                     Integer.valueOf(Serenity.sessionVariableCalled("CUR_SUB_RULE_KEY")));
           Serenity.setSessionVariable("CUR_RELEASE_DATE").to(StringUtils.substringBetween(ProjectVariables.sServices.get("getRuleVersionChange").get("RequestBody"), "\"CUR_RELEASE_DATE\": ", ","));
           sBody = JsonBody.getChangeRequestPayload("getRuleVersionChange", parametersMap1);

           System.out.println("Request Body ==>" + sBody);
           sEndPoint = ProjectVariables.sServices.get("getRuleVersionChange").get("EndpointURL");
           break;
           
      case "RULE DESC CHANGE":
           
           parametersMap1.put("PREV_MP", Integer.valueOf(Serenity.sessionVariableCalled("PREV_MP")));
           parametersMap1.put("PREV_MP_TITLE", Serenity.sessionVariableCalled("PREV_MP_TITLE").toString());
           parametersMap1.put("CUR_MP", Integer.valueOf(Serenity.sessionVariableCalled("PREV_MP")));
           parametersMap1.put("CUR_MP_TITLE", Serenity.sessionVariableCalled("PREV_MP_TITLE").toString());
           parametersMap1.put("PREV_TOPIC", Integer.valueOf(Serenity.sessionVariableCalled("PREV_TOPIC")));
           parametersMap1.put("PREV_TOPIC_TITLE", Serenity.sessionVariableCalled("PREV_TOPIC_TITLE"));
           parametersMap1.put("CUR_TOPIC", Integer.valueOf(Serenity.sessionVariableCalled("PREV_TOPIC")));
           parametersMap1.put("CUR_TOPIC_TITLE", Serenity.sessionVariableCalled("PREV_TOPIC_TITLE"));
           parametersMap1.put("CUR_DP", Integer.valueOf(Serenity.sessionVariableCalled("PREV_DP")));
           parametersMap1.put("PREV_DP", Integer.valueOf(Serenity.sessionVariableCalled("PREV_DP")));
           parametersMap1.put("CUR_DP_DESC", Serenity.sessionVariableCalled("CUR_DP_DESC"));
           parametersMap1.put("PREV_DP_DESC", Serenity.sessionVariableCalled("PREV_DP_DESC"));
           parametersMap1.put("CUR_MID_RULE", Integer.valueOf(Serenity.sessionVariableCalled("CUR_MID_RULE")));
           parametersMap1.put("PREV_MID_RULE", Integer.valueOf(Serenity.sessionVariableCalled("CUR_MID_RULE")));
           parametersMap1.put("CUR_VERSION", Integer.valueOf(Serenity.sessionVariableCalled("CUR_MID_RULE"))+2);
           parametersMap1.put("PREV_VERSION", Integer.valueOf(Serenity.sessionVariableCalled("CUR_MID_RULE")));
           parametersMap1.put("CUR_SUB_RULE_KEY", Integer.valueOf(Serenity.sessionVariableCalled("CUR_SUB_RULE_KEY")));
           parametersMap1.put("PREV_SUB_RULE_KEY",Integer.valueOf(Serenity.sessionVariableCalled("CUR_SUB_RULE_KEY")));
           
           Serenity.setSessionVariable("CUR_RELEASE_DATE").to(StringUtils.substringBetween(ProjectVariables.sServices.get("getRuleVersionChange").get("RequestBody"), "\"CUR_RELEASE_DATE\": ", ","));
           sBody = JsonBody.getChangeRequestPayload("getRuleDescChange", parametersMap1);

           System.out.println("Request Body ==>" + sBody);
           sEndPoint = ProjectVariables.sServices.get("getRuleDescChange").get("EndpointURL");
           break;
      case "RULE DEACT CHANGE":
          
          parametersMap1.put("PREV_MP", Integer.valueOf(Serenity.sessionVariableCalled("PREV_MP")));
          parametersMap1.put("PREV_MP_TITLE", Serenity.sessionVariableCalled("PREV_MP_TITLE").toString());
          parametersMap1.put("CUR_MP", Integer.valueOf(Serenity.sessionVariableCalled("PREV_MP")));
          parametersMap1.put("CUR_MP_TITLE", Serenity.sessionVariableCalled("PREV_MP_TITLE").toString());
          parametersMap1.put("PREV_TOPIC", Integer.valueOf(Serenity.sessionVariableCalled("PREV_TOPIC")));
          parametersMap1.put("PREV_TOPIC_TITLE", Serenity.sessionVariableCalled("PREV_TOPIC_TITLE"));
          parametersMap1.put("CUR_TOPIC", Integer.valueOf(Serenity.sessionVariableCalled("PREV_TOPIC")));
          parametersMap1.put("CUR_TOPIC_TITLE", Serenity.sessionVariableCalled("PREV_TOPIC_TITLE"));
          parametersMap1.put("CUR_DP", Integer.valueOf(Serenity.sessionVariableCalled("PREV_DP")));
          parametersMap1.put("PREV_DP", Integer.valueOf(Serenity.sessionVariableCalled("PREV_DP")));
          parametersMap1.put("CUR_DP_DESC", Serenity.sessionVariableCalled("CUR_DP_DESC"));
          parametersMap1.put("PREV_DP_DESC", Serenity.sessionVariableCalled("PREV_DP_DESC"));
          parametersMap1.put("CUR_MID_RULE", Integer.valueOf(Serenity.sessionVariableCalled("CUR_MID_RULE")));
          parametersMap1.put("PREV_MID_RULE", Integer.valueOf(Serenity.sessionVariableCalled("CUR_MID_RULE")));
          parametersMap1.put("PREV_SUB_RULE_KEY",
                    Integer.valueOf(Serenity.sessionVariableCalled("CUR_SUB_RULE_KEY")));
          Serenity.setSessionVariable("CUR_RELEASE_DATE").to(StringUtils.substringBetween(ProjectVariables.sServices.get("getRuleDeactChange").get("RequestBody"), "\"CUR_RELEASE_DATE\": ", ","));
          sBody = JsonBody.getChangeRequestPayload("getRuleDeactChange", parametersMap1);

          System.out.println("Request Body ==>" + sBody);
          sEndPoint = ProjectVariables.sServices.get("getRuleDeactChange").get("EndpointURL");
          break;
          
     
     case "NEW MP TOPIC RECAT":
          
          parametersMap1.put("PREV_MP", Integer.valueOf(Serenity.sessionVariableCalled("PREV_MP")));
          parametersMap1.put("PREV_MP_TITLE", Serenity.sessionVariableCalled("PREV_MP_TITLE").toString());
          parametersMap1.put("PREV_TOPIC", Integer.valueOf(Serenity.sessionVariableCalled("PREV_TOPIC")));
          parametersMap1.put("PREV_TOPIC_TITLE", Serenity.sessionVariableCalled("PREV_TOPIC_TITLE"));
          parametersMap1.put("CUR_TOPIC", Integer.valueOf(Serenity.sessionVariableCalled("PREV_TOPIC")));
          parametersMap1.put("CUR_TOPIC_TITLE", Serenity.sessionVariableCalled("PREV_TOPIC_TITLE"));
          parametersMap1.put("CUR_DP", Integer.valueOf(Serenity.sessionVariableCalled("PREV_DP")));
          parametersMap1.put("PREV_DP", Integer.valueOf(Serenity.sessionVariableCalled("PREV_DP")));
          parametersMap1.put("CUR_DP_DESC", Serenity.sessionVariableCalled("CUR_DP_DESC"));
          parametersMap1.put("PREV_DP_DESC", Serenity.sessionVariableCalled("PREV_DP_DESC"));
          parametersMap1.put("CUR_MID_RULE", Integer.valueOf(Serenity.sessionVariableCalled("CUR_MID_RULE")));
          parametersMap1.put("PREV_MID_RULE", Integer.valueOf(Serenity.sessionVariableCalled("CUR_MID_RULE")));
          parametersMap1.put("CUR_SUB_RULE_KEY", Integer.valueOf(Serenity.sessionVariableCalled("CUR_SUB_RULE_KEY")));
          parametersMap1.put("PREV_SUB_RULE_KEY",
                    Integer.valueOf(Serenity.sessionVariableCalled("CUR_SUB_RULE_KEY")));
          Serenity.setSessionVariable("CUR_RELEASE_DATE").to(StringUtils.substringBetween(ProjectVariables.sServices.get("getNewMPTopicChange").get("RequestBody"), "\"CUR_RELEASE_DATE\": ", ","));
          sBody = JsonBody.getChangeRequestPayload("getNewMPTopicChange", parametersMap1);

          System.out.println("Request Body ==>" + sBody);
          sEndPoint = ProjectVariables.sServices.get("getNewMPTopicChange").get("EndpointURL");
          break;
          
     case "NEW TOPIC":
          
          parametersMap1.put("PREV_MP", Integer.valueOf(Serenity.sessionVariableCalled("PREV_MP")));
          parametersMap1.put("PREV_MP_TITLE", Serenity.sessionVariableCalled("PREV_MP_TITLE").toString());
          parametersMap1.put("CUR_MP", Integer.valueOf(Serenity.sessionVariableCalled("PREV_MP")));
          parametersMap1.put("CUR_MP_TITLE", Serenity.sessionVariableCalled("PREV_MP_TITLE").toString());
          parametersMap1.put("PREV_TOPIC", Integer.valueOf(Serenity.sessionVariableCalled("PREV_TOPIC")));
          parametersMap1.put("PREV_TOPIC_TITLE", Serenity.sessionVariableCalled("PREV_TOPIC_TITLE"));
          parametersMap1.put("CUR_TOPIC", Integer.valueOf(Serenity.sessionVariableCalled("PREV_TOPIC")));
          parametersMap1.put("CUR_TOPIC_TITLE", Serenity.sessionVariableCalled("PREV_TOPIC_TITLE"));
          parametersMap1.put("CUR_DP", Integer.valueOf(Serenity.sessionVariableCalled("PREV_DP")));
          parametersMap1.put("PREV_DP", Integer.valueOf(Serenity.sessionVariableCalled("PREV_DP")));
          parametersMap1.put("CUR_DP_DESC", Serenity.sessionVariableCalled("CUR_DP_DESC"));
          parametersMap1.put("PREV_DP_DESC", Serenity.sessionVariableCalled("PREV_DP_DESC"));
          parametersMap1.put("CUR_MID_RULE", Integer.valueOf(Serenity.sessionVariableCalled("CUR_MID_RULE")));
          parametersMap1.put("PREV_MID_RULE", Integer.valueOf(Serenity.sessionVariableCalled("CUR_MID_RULE")));
          parametersMap1.put("CUR_SUB_RULE_KEY", Integer.valueOf(Serenity.sessionVariableCalled("CUR_SUB_RULE_KEY")));
          parametersMap1.put("PREV_SUB_RULE_KEY",
                    Integer.valueOf(Serenity.sessionVariableCalled("CUR_SUB_RULE_KEY")));
          Serenity.setSessionVariable("CUR_RELEASE_DATE").to(StringUtils.substringBetween(ProjectVariables.sServices.get("getNewTopicChange").get("RequestBody"), "\"CUR_RELEASE_DATE\": ", ","));
          sBody = JsonBody.getChangeRequestPayload("getNewTopicChange", parametersMap1);

          System.out.println("Request Body ==>" + sBody);
          sEndPoint = ProjectVariables.sServices.get("getNewTopicChange").get("EndpointURL");
          break;
       default:
			GenericUtils.Verify("case not found::"+sChange, false);
		break;
		}
		System.out.println("CUR_RELEASE_DATE::"+Serenity.sessionVariableCalled("CUR_RELEASE_DATE"));
		System.out.println("PREV_RELEASE_DATE::"+Serenity.sessionVariableCalled("PREV_RELEASE_DATE"));

		Response oResponseBody = oRestServiceUtils.getPostResponse(sEndPoint, sBody);
		System.out.println("Response code ==>" + oResponseBody.getStatusCode());
		
		if(oResponseBody.getStatusCode()==200||oResponseBody.getStatusCode()==201)
		{
			System.out.println("Posted the given request successfully");
		}
		else
		{
			Assert.assertTrue("Unable to post the request,response code::"+oResponseBody.getStatusCode()+",request body::"+sBody,false);
		}

	}
}