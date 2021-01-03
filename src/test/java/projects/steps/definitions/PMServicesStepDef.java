package projects.steps.definitions;

import static com.jayway.jsonpath.matchers.JsonPathMatchers.hasJsonPath;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.junit.Assert;

import com.google.gson.Gson;
import com.jayway.jsonpath.Configuration;
import com.jayway.jsonpath.Criteria;
import com.jayway.jsonpath.Filter;

import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.response.ValidatableResponse;
import net.serenitybdd.core.Serenity;
import net.thucydides.core.annotations.Step;
import net.thucydides.core.steps.ScenarioSteps;
import project.pageobjects.CPWPage;
import project.pageobjects.PresentationDeck;
import project.pageobjects.PresentationProfile;
import project.utilities.ExcelUtils;
import project.utilities.GenericUtils;
import project.utilities.MongoDBUtils;
import project.utilities.RestServiceUtils;
import project.variables.JsonBody;
import project.variables.ProjectVariables;



public class PMServicesStepDef extends ScenarioSteps {

	private static final long serialVersionUID = 1L;
	JsonBody oJsonBody;
	PresentationProfile oPresentationProfile;
	RestServiceUtils oRestServiceUtils = new RestServiceUtils();
	CPWStepDef oCPWStepDef;
	CPWPage oCPWPage;
	PresentationDeck oPresentationDeck;


	@Step
	public void logintoApplication(String sUserName, String sPassword) throws Exception {
		Serenity.setSessionVariable("User").to(sUserName);
		//To load the services from excel into hashmap
		ExcelUtils.loadServices();

		String sRequiredPassword=oCPWPage.Get_Password_For_the_given_url(sUserName);

		HashMap<String,Object> parametersMap = new HashMap<String,Object>();
		parametersMap.put("userName", sUserName);
		parametersMap.put("password", sRequiredPassword);
		String sBody = JsonBody.getRequestPayload("getLogin",parametersMap);
		String sEndPoint = ProjectVariables.sServices.get("getLogin").get("EndpointURL");

		//io.restassured.path.json.JsonPath oResponseBody = oRestServiceUtils.getResponseBodyPostService(sEndPoint,sBody);
		
		io.restassured.path.json.JsonPath oResponseBody = oRestServiceUtils.getResponseBodyPostService(sEndPoint,sBody);
		Serenity.setSessionVariable("oLoginResponse").to(oResponseBody);
		String sSessionID = oResponseBody.get("sessionId");
		Serenity.setSessionVariable("sessionID").to(sSessionID);
		Serenity.setSessionVariable("User").to(sUserName);

		System.out.println("Session ID ==>"+sSessionID);
		
		getPayershots();

	}
	
	public void getPayershots(){
		
		/*String sEndPoint = ProjectVariables.sServices.get("getpayershots").get("EndpointURL");
		
		String sid= sEndPoint.replace("{id}", "43");
		
		oRestServiceUtils.getResponseBody(sid);*/
		
		System.out.println();
//		HashMap<String, String> headers = new HashMap<String, String>();
//		
//		headers.put("id", "43");
			
		//io.restassured.path.json.JsonPath  sResp=oRestServiceUtils.getRequestWithPathParams(sEndPoint,headers).jsonPath();
		
		//System.out.println(sResp);
		
		String POST_PARAMS =  "{\"clientKey\":43,\"clientDesc\":\"Dean Health Plan\",\"sourcePPS\":{\"payerKey\":1318,\"insuranceKey\":7,\"claimType\":\"A\",\"payerShort\":\"DHPMP\",\"policySetKey\":1948},\"targetPPS\":{\"payerKey\":1058,\"insuranceKey\":7,\"claimType\":\"A\",\"payerShort\":\"DHPWI\",\"policySetKey\":480},\"prm\":\"\",\"decisions\":[\"Approve\"],\"user\":\"iht_ittest09\"}\r\n";
		
		Response sResponse = oRestServiceUtils.PostServiceWithSessionID("https://cpd-gateway-qa1.cotiviti.com/crossover-opportunities/v1/search/decisions", POST_PARAMS);
		
		System.out.println(sResponse);
	}

	@Step
	public void verifyResponse(Object sExpected , Object sActual){

		Assert.assertEquals("Status Check", sExpected, sActual);

	}

	@Step
	public void verifyFirstNameAndLastName(String sfirstname, String sLastname) {

		io.restassured.path.json.JsonPath sLoginResponseBody = Serenity.sessionVariableCalled("oLoginResponse");
		Assert.assertEquals("First name validation",sLoginResponseBody.get("firstName").toString(),sfirstname);
		Assert.assertEquals("Last name validation",sLoginResponseBody.get("lastName").toString(),sLastname);

	}

	@Step
	public void verifythePayershortsAssignedforGivenUser(String sUser) throws ParseException, IOException {

		String sEndpoint = ProjectVariables.sServices.get("getClientTeamData").get("EndpointURL");
		HashMap<String,Object> parametersMap = new HashMap<String,Object>();
		parametersMap.put("userName", sUser);
		String sClientsBody = JsonBody.getRequestPayload("getClientTeamData", parametersMap);
		oRestServiceUtils.getResponseBodyforPostServiceWithSessionID(sEndpoint, sClientsBody);

	}

	@Step
	public void verifytheClientsassignedforgivenuser(String sUser) throws ParseException, IOException {

		String sEndpoint = ProjectVariables.sServices.get("getClients").get("EndpointURL");
		HashMap<String,Object> parametersMap = new HashMap<String,Object>();
		parametersMap.put("userName", sUser);
		String sClientsBody = JsonBody.getRequestPayload("getClients", parametersMap);
		oRestServiceUtils.getResponseBodyforPostServiceWithSessionID(sEndpoint, sClientsBody);

	}

	@Step
	public void gettheDPandSavingsCountforGivenClient(String sClient) {
		String sClientKey = MongoDBUtils.Retrieve_the_PayerKey_From_mongodb_with_the_given_Payershort(sClient, "Client");
		String sEndpoint = ProjectVariables.sServices.get("getSavingsAndDPCount").get("EndpointURL");
		HashMap<String,Object> parametersMap = new HashMap<String,Object>();
		parametersMap.put("clientKey", sClientKey);
		oRestServiceUtils.ValidategetRequestWithParams(parametersMap, sEndpoint);

	}

	public void gettheDPandLobinfo(String sClient, String sPayershort, String sMedicalpolicies) throws ParseException, IOException {

		int[] arPayerKeys = new int[]{1020,1024,1022,1021,1023,1202,321,1110,1434};
		int[] arTopicKeys = new int[]{1186,2557,2399,109,1973,1446,2287,2351};				

		HashMap<String,Object> sFields = new HashMap<String,Object>();
		sFields.put("payerKeys", arPayerKeys);
		sFields.put("topicKeys", arTopicKeys);

		int iClientKey = 19;
		String sDpLobBody = JsonBody.getRequestPayload("getDpLobInfo", sFields);
		String sLobEndpoint = ProjectVariables.sServices.get("getDpLobInfo").get("EndpointURL");
		oRestServiceUtils.postWithPathParams(iClientKey,sLobEndpoint,sDpLobBody);

	}

	@Step
	public void createPresentationProfilewithGivenRequest(String sUser, String sClient,String sPayershorts,String sLobs,String sProduct,String sPriority) throws ParseException, IOException {

		Serenity.setSessionVariable("Client").to(sClient);
		if(sUser.isEmpty()||sUser.contains("User"))
		{
			Assert.assertTrue("User is not given from the gherkin::"+sUser, false);
		}
		String sPresentation=oPresentationProfile.createPresentationThroughService(sUser,sClient,sPayershorts,sLobs,sProduct,sPriority);

		Serenity.setSessionVariable("profileId").to(StringUtils.substringBefore(sPresentation, "-"));
		Serenity.setSessionVariable("PresentationName").to(StringUtils.substringAfter(sPresentation, "-"));


		System.out.println("ProfileID====>"+Serenity.sessionVariableCalled("profileId"));
		System.out.println("PresentationName====>"+Serenity.sessionVariableCalled("PresentationName"));
	}
	//Not using
	public void getDataforGivenClient(String sClient,String sPayerkey) throws IOException {

		String sClientKey = MongoDBUtils.Retrieve_the_PayerKey_From_mongodb_with_the_given_Payershort(sClient, "Client");	
		String sEndpoint = ProjectVariables.sServices.get("getPMSummaryInformation").get("EndpointURL");
		HashMap<String,String> sValue = new HashMap<String,String>();
		sValue.put("id", sClientKey);

		InputStream sResposeBody = oRestServiceUtils.getRequestWithPathParams(sEndpoint,sValue).then().extract().asInputStream();

		Filter filter = Filter.filter(Criteria.where("payerKey").is(1344));

		List<Object> lstObjects = com.jayway.jsonpath.JsonPath.read(sResposeBody, "$.[?]", filter);
		
		JSONArray jsonObj = new JSONArray(lstObjects);

		List<JSONObject> jsonItems = IntStream.range(0, jsonObj.length()).mapToObj(index -> (JSONObject) jsonObj.get(index)).collect(Collectors.toList());	

		System.out.println(jsonItems);

	}
	
	public void userLogsOutFromThenApplication() {

		String sEndpoint = ProjectVariables.sServices.get("logout").get("EndpointURL");
		Response sResponse = oRestServiceUtils.PostServiceWithSessionID(sEndpoint, "");
		Object sVal = sResponse.jsonPath().get("isError");
		System.out.println(String.valueOf(sVal));
		Assert.assertTrue("Log out",String.valueOf(sVal).equalsIgnoreCase("false"));

	}

	@SuppressWarnings("unchecked")
	public void getAllPayersforClientforUser(String sClient, String sUser) throws IOException {
		String sEndPoint = ProjectVariables.sServices.get("getfilterService").get("EndpointURL");
		HashMap<String,Object> parametersMap = new HashMap<String,Object>();
		parametersMap.put("userId", sUser);
		InputStream sResposeBody = oRestServiceUtils.getRequestWithParams(parametersMap, sEndPoint).then().extract().asInputStream();

		Filter filter = Filter.filter(
				Criteria.where("clientDesc")
				.is(sClient));

		List<Object> lstObjects = com.jayway.jsonpath.JsonPath.read(sResposeBody, "$.[?]", filter);
		JSONArray jsonObj = new JSONArray(lstObjects);

		List<JSONObject> jsonItems = IntStream.range(0, jsonObj.length()).mapToObj(index -> (JSONObject) jsonObj.get(index)).collect(Collectors.toList());		


		Object sPayerKeys = RestServiceUtils.getValuefromJson(jsonItems.get(0),"payerKeys");
		Serenity.setSessionVariable("AllPayerKeys").to(sPayerKeys);

		Object sInsuranceDesc = RestServiceUtils.getValuefromJson(jsonItems.get(0),"insuranceDesc");
		Serenity.setSessionVariable("AllInsuranceDesc").to(sInsuranceDesc);

		Object sTopicKeys = RestServiceUtils.getValuefromJson(jsonItems.get(0),"topicKey");
		Serenity.setSessionVariable("AlltopicKey").to(sTopicKeys);

	}

	public static Map<String, Object> toFlatMap(final JSONObject object) throws JSONException {
		final Map<String, Object> map = new HashMap<String, Object>();
		final Iterator<String> keysItr = object.keys();
		while (keysItr.hasNext()) {
			final String key = keysItr.next();
			final Object value = object.get(key);
			if (!(value instanceof String)) {
				continue;
			}
			map.put(key, value);
		}

		return map;
	}

	public void AssignDPtoProfile() throws ParseException, IOException {



		String sTopicKey = MongoDBUtils.Retrieve_the_PayerKey_From_mongodb_with_the_given_Payershort(Serenity.sessionVariableCalled("Topic").toString(), "Topic");
		int iTopicKey = Integer.parseInt(sTopicKey);
		int[] arTopicKeys = new int[]{iTopicKey};				

		HashMap<String,Object> sFields = new HashMap<String,Object>();
		sFields.put("topicKeys", arTopicKeys);

		int iClientKey = Integer.parseInt(Serenity.sessionVariableCalled("clientkey"));
		int iDPKey= Integer.parseInt(Serenity.sessionVariableCalled("DPkey"));
		int iInsuranceKey = Integer.parseInt(Serenity.sessionVariableCalled("InsuranceKeys").toString().split(",")[0]);
		String sDpLobBody = JsonBody.getRequestPayload("getDpLobInfo", sFields);
		String sLobEndpoint = ProjectVariables.sServices.get("getDpLobInfo").get("EndpointURL");
		io.restassured.path.json.JsonPath sResposeBody = oRestServiceUtils.postWithPathParams(iClientKey,sLobEndpoint,sDpLobBody).getBody().jsonPath();

		String sPayershort = sResposeBody.get("dpLobInfoList[0].ppsBasedcdmDecisions.pps.payerShort[0]");
		String sInsuranceDesc = sResposeBody.get("dpLobInfoList[0].ppsBasedcdmDecisions.pps.insuranceDesc[0]");
		String sClaimType = sResposeBody.get("dpLobInfoList[0].ppsBasedcdmDecisions.pps.claimType[0]");

		String sPayeKey = MongoDBUtils.Retrieve_the_PayerKey_From_mongodb_with_the_given_Payershort(sPayershort, "payerShort");

		HashMap<String,Object> sFieldsprof = new HashMap<String,Object>();
		sFieldsprof.put("profileId", Serenity.sessionVariableCalled("profileId"));
		sFieldsprof.put("profileName", Serenity.sessionVariableCalled("profileName"));

		HashMap<String,Object> sFields1 = new HashMap<String,Object>();
		List<Map<String , Object>> sFields21  = new ArrayList<Map<String,Object>>();
		sFields21.add(sFieldsprof);

		sFields1.put("clientKey", iClientKey);
		sFields1.put("topics", iTopicKey);
		sFields1.put("dPs", iDPKey);
		sFields1.put("presentationAddition", sFieldsprof);

		HashMap<String,Object> sProfileLobMap = new HashMap<String,Object>();
		int[] iDps = new int[]{iDPKey};
		sProfileLobMap.put("topicKey", iTopicKey);
		sProfileLobMap.put("dps", iDps);

		HashMap<String,Object> oPPs = new HashMap<String,Object>();
		oPPs.put("payerKey", Integer.parseInt(sPayeKey));
		oPPs.put("insuranceDesc", sInsuranceDesc);
		oPPs.put("claimType", sClaimType);
		oPPs.put("insuranceKey", iInsuranceKey);



		List<Map<String , Object>> lstpps  = new ArrayList<Map<String,Object>>();		
		lstpps.add(oPPs);

		sProfileLobMap.put("pps",lstpps);

		List<Map<String , Object>> lstProfile  = new ArrayList<Map<String,Object>>();		
		lstProfile.add(sProfileLobMap);

		sFields1.put("profileTopics", lstProfile);

		String sUpdateAssignmentBdy = JsonBody.getRequestPayload("updateAssignments", sFields1);
		String sUpdateAssignmentEndPnt= ProjectVariables.sServices.get("updateAssignments").get("EndpointURL");

		Response sResponse =  oRestServiceUtils.PostServiceWithSessionID(sUpdateAssignmentEndPnt, sUpdateAssignmentBdy);

		sResponse.then().assertThat().body(containsString("true"));

		System.out.println("profileName===>"+Serenity.sessionVariableCalled("profileName"));
		System.out.println("DPKeys===>"+iDPKey);

	}

	public void capturetheDecisionforGivenStatus(String sStatus) throws ParseException, IOException {


		String sPrfStateEndPoint = ProjectVariables.sServices.get("Profilepayerlob").get("EndpointURL");
		HashMap<String,String> sProfileState = new HashMap<String,String>();
		sProfileState.put("profileid", Serenity.sessionVariableCalled("profileId").toString());
		Response sResponse = oRestServiceUtils.getRequestWithPathParams(sPrfStateEndPoint, sProfileState);
		io.restassured.path.json.JsonPath sResponsePath = sResponse.getBody().jsonPath();

		int iClientKey = Integer.parseInt(Serenity.sessionVariableCalled("clientkey"));
		int iDPKey= Integer.parseInt(Serenity.sessionVariableCalled("DPkey"));


		String sEndpntSavePrfDescn = ProjectVariables.sServices.get("saveProfileDecision").get("EndpointURL");
		HashMap<String,Object> sInput = new HashMap<String,Object>();

		sInput.put("status", "Approve");
		sInput.put("isProd", "-1");
		sInput.put("note", "Capture Decision with Service");
		sInput.put("updatedBy", Serenity.sessionVariableCalled("User"));

		HashMap<String,Object> sProfleMap = new HashMap<String,Object>();
		int[] iDps = new int[]{iDPKey};
		sProfleMap.put("dpKeys", iDps);
		sProfleMap.put("insuranceKeys", sResponsePath.get("insuranceKey"));
		sProfleMap.put("payerKeys", sResponsePath.get("payerKey"));
		sProfleMap.put("clientKey", iClientKey);
		sProfleMap.put("profileId", Serenity.sessionVariableCalled("profileId").toString());

		sInput.put("dpPPS", sProfleMap);
		String sProfileBdy = JsonBody.getRequestPayload("saveProfileDecision", sInput);

		ValidatableResponse sValue = oRestServiceUtils.PostServiceWithSessionID(sEndpntSavePrfDescn, sProfileBdy).then().assertThat().statusCode(200);
		System.out.println();

	}

	public void getPresentationHavingOppurtunities(String sClient) throws IOException {

		String sSavingsandDPCount = ProjectVariables.sServices.get("getSavingsAndDPCount").get("EndpointURL");
		String sClientKey = MongoDBUtils.Retrieve_the_PayerKey_From_mongodb_with_the_given_Payershort(sClient, "Client");
		HashMap<String, Object> parametersMap = new HashMap<String, Object>();
		parametersMap.put("clientKey", sClientKey);

		InputStream sResposeBody = oRestServiceUtils.getRequestWithParams(parametersMap, sSavingsandDPCount).then().extract().asInputStream();

		Filter filter = Filter.filter(
				Criteria.where("dpCount")
				.gt(1));

		List<Object> lstObjects = com.jayway.jsonpath.JsonPath.read(sResposeBody, "$.[?]", filter);

		JSONArray jsonObj = new JSONArray(lstObjects);

		List<JSONObject> jsonItems = IntStream.range(0, jsonObj.length())
				.mapToObj(index -> (JSONObject) jsonObj.get(index))
				.collect(Collectors.toList());	

		Object sval = RestServiceUtils.getValuefromJson(jsonItems.get(0), "profileId");

		String sEpProfiles = ProjectVariables.sServices.get("getProfiles").get("EndpointURL");

		InputStream sResposeBody1 = oRestServiceUtils.getRequestWithParams(parametersMap, sEpProfiles).then().extract().asInputStream();

		Filter filter1 = Filter.filter(
				Criteria.where("id")
				.is(String.valueOf(sval)));

		List<Object> lstObjects2 = com.jayway.jsonpath.JsonPath.read(sResposeBody1, "$.[?]", filter1);

		JSONArray jsonObj2 = new JSONArray(lstObjects2);

		List<JSONObject> jsonItems2 = IntStream.range(0, jsonObj2.length())
				.mapToObj(index -> (JSONObject) jsonObj2.get(index))
				.collect(Collectors.toList());	

		Object sval2 = RestServiceUtils.getValuefromJson(jsonItems2.get(0), "profileName");

		Serenity.setSessionVariable("PresentationName").to(String.valueOf(sval2));
		Serenity.setSessionVariable("profileId").to(String.valueOf(sval));
		Serenity.setSessionVariable("clientkey").to(sClientKey);
		System.out.println("PresentationName with opportunities===>"+String.valueOf(sval2));

	}

	@Step
	public void getthePayershortandLOBhavingNofilterCriteria() throws ParseException, IOException {

		String sProfileID = Serenity.sessionVariableCalled("profileId").toString();
		String ClientKey = Serenity.sessionVariableCalled("clientkey");

		oRestServiceUtils.getAllinformationforGivenClientPresentionProfile(sProfileID);
		HashSet<Object> sPayerKeySet = Serenity.sessionVariableCalled("AllPresentationPayerKeys");
		HashSet<Object> sInsuranceSet = Serenity.sessionVariableCalled("AllPresentationInsuranceDesc");

		Object[] sValues = sPayerKeySet.toArray();
		Object[] sInsurance = sInsuranceSet.toArray();
		boolean blnFlag = false;
		for (int i=0 ;i<sValues.length; i++){

			String sPayeKey = String.valueOf(sValues[i]);

			for (int j = 0; j< sInsurance.length;j++){
				Response oRespone = oRestServiceUtils.getAvailableOppurtunitiesforPresenation(ClientKey,sProfileID,sPayeKey, String.valueOf(sInsurance[j]), null);
				ArrayList<Object> svale = oRespone.jsonPath().get();
				int iValues = svale.size();
				if(iValues == 0){
					String sPayerShort = MongoDBUtils.Retrieve_the_PayerKey_From_mongodb_with_the_given_Payershort(sPayeKey,"PayerKey");
					Serenity.setSessionVariable("NoFilterPayer").to(sPayerShort);
					Serenity.setSessionVariable("NoFilterInsurance").to(String.valueOf(sInsurance[j]));
					blnFlag =true;
					break;
				}
			}
			if (blnFlag){break;}
		}

		if (!blnFlag){
			GenericUtils.Verify("no criteria met to filter", false);

		}

	}

	@Step
	public void assignDPtoCreatedProfile() throws ParseException, IOException {

		String sClientKey = Serenity.sessionVariableCalled("clientkey");
		String sDPKey = Serenity.sessionVariableCalled("DPkey");
		String sTopicKey = Serenity.sessionVariableCalled("TopicKey");
		String sProfileID = Serenity.sessionVariableCalled("profileId");
		String sProfileName = Serenity.sessionVariableCalled("profileName");

		oRestServiceUtils.assignDPtoProfile(sClientKey,sDPKey,sTopicKey ,sProfileID ,sProfileName);

	}

	@Step
	public void getAvailableOppurtunitiesforGiven(String client, String payershort, String lob,
			String topics) throws ParseException, IOException {

		String sClientKey = MongoDBUtils.Retrieve_the_PayerKey_From_mongodb_with_the_given_Payershort(client, "Client");
		ArrayList<Long> sClientkeylist=new ArrayList<>();
		sClientkeylist.add(Long.parseLong(sClientKey));
		//ArrayList<String> sPayerKeys = MongoDBUtils.GettheDistinctDBResultsFromtheGiven("Payerkeys","",sClientkeylist);
		HashMap<String,Object> sFields = new HashMap<String,Object>();
		sFields.put("clientKey", sClientKey);
		sFields.put("insuranceDesc", lob.split(";"));
		sFields.put("payerKeys",payershort.split(";"));
		sFields.put("topicKeys",topics.split(";"));

		String sBody = JsonBody.getRequestPayload("getPresentationManagerOpportunites", sFields);
		String sEndpoint = ProjectVariables.sServices.get("getPresentationManagerOpportunites").get("EndpointURL");

		//String oReponse = oRestServiceUtils.PostServiceWithSessionID(sEndpoint, sBody).then().extract().asString();
		String oReponse = oRestServiceUtils.PostServiceWithSessionID(sEndpoint, sBody).then().extract().asString();	
		//System.out.println(oReponse.get('[23].unassignedOpp').toString());
		//JsonPath sjosn = oReponse.jsonPath();
		JSONArray jsonObj = new JSONArray(oReponse.trim());
		JSONObject jsonOb = new JSONObject(jsonObj.get(1).toString());

		Gson g = new Gson();		
		String str = g.toJson(oReponse);


		Object json = Configuration.defaultConfiguration().jsonProvider().parse(str);	

		Assert.assertThat(json, hasJsonPath("[3].unassignedOpp", equalTo(7)));
		//Object sVLaue = oReponse.get("[unassignedOpp");
		//System.out.println(sVLaue);
	}

	public void getSummaryinformationforGivenClient(String sClient) {

		oRestServiceUtils.getAllinformationforGivenClient(sClient);
	}

	@Step
	public void assignMultipleDPstoCreatedProfile() throws ParseException, IOException {


		oPresentationDeck.assignMultipleDPstoCreatedProfile();

	}

	@Step
	public void deletePresentationsforClient(String sClientKey) 
	{
		boolean bstatus=false;
		List<String> sClientKeysList=Arrays.asList(sClientKey.split(","));

		for (int j = 0; j < sClientKeysList.size(); j++) 
		{
			String sEpProfiles = ProjectVariables.sServices.get("getProfiles").get("EndpointURL");
			//String sClientKey = MongoDBUtils.Retrieve_the_PayerKey_From_mongodb_with_the_given_Payershort(sClient, "Client");
			HashMap<String, Object> parametersMap = new HashMap<String, Object>();
			parametersMap.put("clientKey", sClientKeysList.get(j));
			Response sResposeBody1 = oRestServiceUtils.getRequestWithParams(parametersMap, sEpProfiles);
			io.restassured.path.json.JsonPath sResponsePath = sResposeBody1.getBody().jsonPath();

			ArrayList<Object> oPayers = sResponsePath.get("id");
			ArrayList<Object> oProfileNames = sResponsePath.get("profileName");
			HashSet<Object> AllProfileIDs = new HashSet<Object>(oPayers);
			Serenity.setSessionVariable("AllPresentationProfileIDs").to(AllProfileIDs);

			for (int i=0;i< oPayers.size();i++)
			{
				String sProfileID = String.valueOf(oPayers.get(i));
				String sProfileName = String.valueOf(oProfileNames.get(i));

				if(sProfileName.contains("Auto")||sProfileName.contains("Robo")||sProfileName.contains("TestPres"))
				{
					bstatus=MongoDBUtils.getThePresentationStaus(sClientKeysList.get(j), sProfileID);
					if(bstatus)
					{
						String sService = "presentation-manager/v1/client/"+sClientKeysList.get(j)+"/profile/delete/"+sProfileID;			
						oRestServiceUtils.PostServiceWithSessionID(sService, "");
						System.out.println("Presentation '"+sProfileName+"' has Deleted");
					}
					else
					{
						System.out.println("Presentation '"+sProfileName+"' has DPs,which have capture decisions");
					}
				}

			}

			System.out.println("All the profiles contains 'Auto','Robo','TestPres' are deleted,which haven't captured for the clientkey==>"+sClientKeysList.get(j));
		}

		System.out.println("All the profiles contains 'Auto','Robo','TestPres' are deleted,which haven't captured for clientkeys==>"+sClientKeysList);


	}

	@Step
	public void verify_Approve_with_MOD_DB_and_Services_count_for(String arg1) throws Throwable {
		switch(arg1.toUpperCase()){

		case "CPT CODE":
			oPresentationDeck.validations_approvewithMOD_DB_SERVICES_COUNT("APPROVE WITH MOD CPT CODE COUNT","approveWithModCPTDBCount");
			break;
		case "ICD CODE":
			oPresentationDeck.validations_approvewithMOD_DB_SERVICES_COUNT("APPROVE WITH MOD ICD CODE COUNT","approveWithModICDDBCount");
			break;
		case "REASON CODE":
			oPresentationDeck.validations_approvewithMOD_DB_SERVICES_COUNT("APPROVE WITH MOD REASON CODE COUNT","approveWithModREASONDBCount");
			break;	   
		}

	}

	@Step
	public void verify_Approve_with_MOD_DB_and_Services_data_for(String arg1) throws Throwable {
		switch(arg1.toUpperCase()){

		case "CPT CODE":
			oPresentationDeck.validations_approvewithMOD_DB_SERVICES_DATA("APPROVE WITH MOD CPT CODE DATA", "approveWithModCPTData");
			break;
		case "ICD CODE":
			oPresentationDeck.validations_approvewithMOD_DB_SERVICES_DATA("APPROVE WITH MOD ICD CODE DATA","approveWithModICDData");
			break;
		case "REASON CODE":
			oPresentationDeck.validations_approvewithMOD_DB_SERVICES_DATA("APPROVE WITH MOD REASON CODE DATA","approveWithModREASONData");
			break;
		case "AGE FILTER-1":
			oPresentationDeck.validations_approvewithMOD_DB_SERVICES_DATA("APPROVE WITH MOD AGE CODE DATA","approveWithModAGEFILETER-1");
			break;
		case "AGE FILTER0":
			oPresentationDeck.validations_approvewithMOD_DB_SERVICES_DATA("APPROVE WITH MOD AGE CODE DATA0","approveWithModAGEFILETER0");
			break;   
		}
	}

	public void validateSavings(String savingsType, String section, String client) throws IOException
	{


		//To load the services from excel into HasHMap
		ExcelUtils.loadServices();		

		String DPKey = Serenity.sessionVariableCalled("DPkey").toString();
		String OppSavings =Serenity.sessionVariableCalled("OppSavings");		
		Integer AssignedDPkey = Integer.valueOf(DPKey);

		ArrayList<Integer> DPKeys =  new ArrayList<Integer>();
		ArrayList<Object> AllData =  new ArrayList<Object>();
		HashMap<Object,Object> AllData1 =  new HashMap<Object,Object>();
		String eReviewData = ProjectVariables.sServices.get("getCPWPresentDispositionData").get("EndpointURL");
		String ePayLoad = ProjectVariables.sServices.get("getCPWPresentDispositionData").get("RequestBody");

		io.restassured.path.json.JsonPath sval = oRestServiceUtils.getResponseBodyPostService(eReviewData,ePayLoad);		

		boolean RawSavingsUpdated  =false;

		Long Actualsavings =  sval.get("result.rawOppSavings");	

		if((Actualsavings.equals(OppSavings)))
		{

			RawSavingsUpdated  =true;

		}					

		if(RawSavingsUpdated == true)
		{	    	  
			Assert.assertTrue("RawSavings updated as per the disposition" , true);
			getDriver().quit();
		}
		else
		{ 
			Assert.assertTrue("RawSavings not updated as per the dispositione ", false);
		}

	}	

	@Step
	public void validateOppInRWOPage(String pageName, String client) throws IOException 
	{

		//To load the services from excel into HasHMap
		ExcelUtils.loadServices();		

		String DPKey = Serenity.sessionVariableCalled("DPkey").toString();
		String AssignedPayershort =Serenity.sessionVariableCalled("AssignedPayershort");
		String AssignedClaimtype = Serenity.sessionVariableCalled("AssignedClaimtype");
		String AssigneInsuranceDesc = Serenity.sessionVariableCalled("AssignedInsuranceDesc");			  


		Integer AssignedDPkey = Integer.valueOf(DPKey);

		ArrayList<Integer> DPKeys =  new ArrayList<Integer>();
		ArrayList<Object> AllData =  new ArrayList<Object>();
		HashMap<Object,Object> AllData1 =  new HashMap<Object,Object>();
		String eReviewData = ProjectVariables.sServices.get("getReviewWorkedOppData").get("EndpointURL");
		String ePayLoad = ProjectVariables.sServices.get("getReviewWorkedOppData").get("RequestBody");

		io.restassured.path.json.JsonPath sval = oRestServiceUtils.getResponseBodyPostService(eReviewData,ePayLoad);		

		DPKeys =  sval.get("result.dpKey");			    		 
		System.out.println("Total DPKeys::" +DPKeys.size());    		 


		boolean AssignedPPSCombinationToPresentationFound = false;

		for(int m=0;m<DPKeys.size();m++)
		{	    		
			Integer dp;
			dp =  sval.get("result["+m+"].dpKey");	
			System.out.println("Srvc DP::"+dp);
			if(AssignedDPkey.equals(dp) )
			{								
				String idesc =  sval.get("result["+m+"].insuranceDesc");	
				String payershort =  sval.get("result["+m+"].payerShort");					
				String ClaimType =  sval.get("result["+m+"].claimType");	

				if((idesc.equalsIgnoreCase(AssigneInsuranceDesc)) && (payershort.equalsIgnoreCase(AssignedPayershort)) && (ClaimType.equalsIgnoreCase(AssignedClaimtype)))
				{
					System.out.println("dp::"+dp);
					System.out.println(idesc);
					System.out.println(payershort);									
					AssignedPPSCombinationToPresentationFound  =true;
					break;
				}					
			}//End if
		}//End for

		if(AssignedPPSCombinationToPresentationFound == true)
		{	    	  
			Assert.assertTrue("PPS combination which is assigned to Presentation is displayed in CPWReviewWorkedOppornity(RWO) page" , false);
			getDriver().quit();
		}
		else
		{ 
			Assert.assertTrue("PPS combination which is assigned to Presentation is displayed in CPWReviewWorkedOppornity(RWO) page ", true);
		}




	}

	@Step
	public void getSummaryFromCPWOpportunities(String serviceName,String data) throws IOException{
		ExcelUtils.loadServices();                   
		io.restassured.path.json.JsonPath sval;
		if(serviceName.toLowerCase().contains("saving")){
			HashMap<Object,Object> AllData =  new HashMap<Object,Object>();
			String eReviewData = ProjectVariables.sServices.get("getPresentSavings").get("EndpointURL");
			String ePayLoad = ProjectVariables.sServices.get("getPresentSavings").get("RequestBody").replace("%clientkey%", Serenity.sessionVariableCalled("clientkey"));

			sval = oRestServiceUtils.getResponseBodyPostService(eReviewData,ePayLoad);                  

			Integer rawOppSavings = sval.get("result.rawOppSavings"); 
			Integer aggOppSavings = sval.get("result.aggOppSavings"); 
			Integer conOppSavings = sval.get("result.conOppSavings"); 
			Integer presentDPDispositions = sval.get("result.presentDPDispositions"); 
			AllData.put("RawOppSavings", rawOppSavings);
			AllData.put("AggOppSavings", aggOppSavings);
			AllData.put("ConOppSavings", conOppSavings);
			AllData.put("PresentDPDispositions", presentDPDispositions);
			Serenity.setSessionVariable(data).to(AllData);
		}
		else if(serviceName.toLowerCase().contains("policy")){
			HashMap<Object,Object> AllData =  new HashMap<Object,Object>();
			String eReviewData = ProjectVariables.sServices.get("getPolicySummary").get("EndpointURL");
			String ePayLoad = ProjectVariables.sServices.get("getPolicySummary").get("RequestBody").replace("%clientkey%", Serenity.sessionVariableCalled("clientkey"));

			sval = oRestServiceUtils.getResponseBodyPostService(eReviewData,ePayLoad);                  

			ArrayList<Object>  resultData =  sval.get("result");         
			System.out.println("Total elements::" +resultData.size()); 
			ArrayList<Integer> aggOppSavings = sval.get("result.aggOppSavings"); 
			ArrayList<Integer> conOppSavings = sval.get("result.conOppSavings"); 
			ArrayList<Integer> totalOppEdits = sval.get("result.totalOppEdits");               

			AllData.put("AggOppSavings", aggOppSavings);
			AllData.put("ConOppSavings", conOppSavings);
			AllData.put("TotalOppEdits", totalOppEdits);
			Serenity.setSessionVariable(data).to(AllData);
		}

	}

	public void capturetheDecisionWithOneorMoreStatus(String sStatus,String sClient)throws ParseException, IOException {
		// TODO Auto-generated method stub

		HashMap<String,String> sProfileState = new HashMap<String,String>();
		sProfileState.put("profileid", Serenity.sessionVariableCalled("profileId").toString());
		ArrayList<Map<String , Object>> ppslist  = new ArrayList<Map<String,Object>>();
		
		String  InsuranceKey=null;
		String payerkey=null;
		String claimtype=null;
		String insuranceDesc=null;
		String RequestBody=null;

		int iClientKey = Integer.parseInt(Serenity.sessionVariableCalled("clientkey"));     
		List<String> DPKeysList=Arrays.asList(Serenity.sessionVariableCalled("DPkey").toString().split(","));
		HashMap<String,String> sValue = new HashMap<String,String>();
		sValue.put("id", String.valueOf(iClientKey));

		for (int p = 0; p < DPKeysList.size(); p++)
		{         
			String sDPKey = DPKeysList.get(p).trim();
			int iDPKey= Integer.parseInt(sDPKey);
			MongoDBUtils.GettheCapturedDispositionPayerLOBClaimTypesFromtheGiven(sDPKey);
			for (int i = 0; i < ProjectVariables.PPSKeyList.size(); i++) 
			{
				HashMap<String,Object> oPPS = new HashMap<String,Object>();
				InsuranceKey=StringUtils.substringBetween(ProjectVariables.PPSKeyList.get(i), "-","-");
				payerkey=StringUtils.substringBefore(ProjectVariables.PPSKeyList.get(i), "-"+InsuranceKey);
				claimtype=StringUtils.substringAfterLast(ProjectVariables.PPSKeyList.get(i), "-");
				insuranceDesc=GenericUtils.Retrieve_the_insuranceDesc_from_insuranceKey(InsuranceKey);                     

				oPPS.put("\"payerKey\"", Integer.valueOf(payerkey));
				oPPS.put("\"insuranceKey\"", InsuranceKey);
				oPPS.put("\"dpKey\"", iDPKey);
				oPPS.put("\"insuranceDesc\"", "\""+insuranceDesc+"\"");
				oPPS.put("\"claimType\"", "\""+claimtype+"\"");
				ppslist.add(oPPS);
			}
				switch(sStatus)
				{
				case "Approve":
					RequestBody="{\"status\":\""+sStatus+"\",\"isProd\":-1,\"note\":\"Test Notes\",\"reason\":null,\"processingFromTs\":-6847826008000,"
							+ "\"processingToTs\":253402194600000,\"dOSFromTs\":-6847826008000,\"dOSToTs\":253402194600000,\"followUpOwner\":null,"
							+ "\"updatedBy\":\""+Serenity.sessionVariableCalled("User")+"\",\"dpPPS\":{\"dpKeys\":["+iDPKey+"],\"insuranceKeys\":null,\"payerKeys\":null,\"clientKey\":\""+Serenity.sessionVariableCalled("clientkey")+"\","
							+ "\"profileId\":\""+Serenity.sessionVariableCalled("profileId")+"\",\"dpPayerLobs\":"+ppslist+"},\"modifications\":{\"maxAgeFilter\":null,\"minAgeFilter\":null}}\r\n";
					
					//String sbody="{\"status\":\"Approve\",\"isProd\":-1,\"note\":null,\"reason\":null,\"processingFromTs\":-6847826008000,\"processingToTs\":253402194600000,\"dOSFromTs\":-6847826008000,\"dOSToTs\":253402194600000,\"followUpOwner\":null,\"updatedBy\":\"nkumar\",\"dpPPS\":{\"dpKeys\":[2312],\"insuranceKeys\":null,\"payerKeys\":null,\"clientKey\":11,\"profileId\":\"5e84554013686c0001b5840b\",\"dpPayerLobs\":[{\"dpKey\":2312,\"payerKey\":23,\"insuranceKey\":2,\"insuranceDesc\":\"Medicaid\",\"claimType\":\"P\"},{\"dpKey\":2312,\"payerKey\":1420,\"insuranceKey\":2,\"insuranceDesc\":\"Medicaid\",\"claimType\":\"P\"},{\"dpKey\":2312,\"payerKey\":251,\"insuranceKey\":1,\"insuranceDesc\":\"Medicare\",\"claimType\":\"P\"}]},\"modifications\":{\"maxAgeFilter\":null,\"minAgeFilter\":null}}\r\n";
					
					System.out.println(RequestBody);
					
					break;
				case "Approve with Mod":
					RequestBody="{\"status\":\""+sStatus+"\",\"isProd\":-1,\"note\":\"Test Notes\",\"reason\":null,\"processingFromTs\":-6847826008000,"
							+ "\"processingToTs\":253402194600000,\"dOSFromTs\":-6847826008000,\"dOSToTs\":253402194600000,\"followUpOwner\":null,"
							+ "\"updatedBy\":\""+Serenity.sessionVariableCalled("User")+"\",\"dpPPS\":{\"dpKeys\":["+iDPKey+"],\"insuranceKeys\":null,\"payerKeys\":null,\"clientKey\":\""+Serenity.sessionVariableCalled("clientkey")+"\","
							+ "\"profileId\":\""+Serenity.sessionVariableCalled("profileId")+"\",\"dpPayerLobs\":"+ppslist+"},\"modifications\":{\"maxAgeFilter\":null,\"minAgeFilter\":null,\"other\":\"TestMod\"}}\r\n}";
					break;
				case "Reject":
					RequestBody="{\"status\":\""+sStatus+"\",\"note\":\"Test Notes\",\"reason\":\"Client internal policy\",\"processingFromTs\":-6847826008000,"
							+ "\"processingToTs\":253402194600000,"+ "\"updatedBy\":\""+Serenity.sessionVariableCalled("User")+"\",\"dpPPS\":{\"dpKeys\":["+iDPKey+"],\"insuranceKeys\":null,\"payerKeys\":null,\"clientKey\":\""+Serenity.sessionVariableCalled("clientkey")+"\","
							+ "\"profileId\":\""+Serenity.sessionVariableCalled("profileId")+"\",\"dpPayerLobs\":"+ppslist+"}}\r\n";
					break;
				case "Defer":
					RequestBody="{\"status\":\""+sStatus+"\",\"note\":\"Defer Notes\","
							+ "\"processingToTs\":253402194600000,"+ "\"updatedBy\":\""+Serenity.sessionVariableCalled("User")+"\",\"dpPPS\":{\"dpKeys\":["+iDPKey+"],\"insuranceKeys\":null,\"payerKeys\":null,\"clientKey\":\""+Serenity.sessionVariableCalled("clientkey")+"\","
							+ "\"profileId\":\""+Serenity.sessionVariableCalled("profileId")+"\",\"dpPayerLobs\":"+ppslist+"}}\r\n";
					break;
				case "Follow up":
					RequestBody="{\"status\":\""+sStatus+"\",\"isProd\":-1,\"note\":\"Test Notes\",\"reason\":null,\"processingFromTs\":-6847826008000,"
							+ "\"processingToTs\":253402194600000,\"dOSFromTs\":-6847826008000,\"dOSToTs\":253402194600000,\"followUpOwner\":null,"
							+ "\"updatedBy\":\""+Serenity.sessionVariableCalled("User")+"\",\"dpPPS\":{\"dpKeys\":["+iDPKey+"],\"insuranceKeys\":null,\"payerKeys\":null,\"clientKey\":\""+Serenity.sessionVariableCalled("clientkey")+"\","
							+ "\"profileId\":\""+Serenity.sessionVariableCalled("profileId")+"\",\"dpPayerLobs\":"+ppslist+"}}\r\n";
					break;

				default:
					Assert.assertTrue("Given Status is not available in the switch case", false);
				}
				
			
				String sEndpntSavePrfDescn = ProjectVariables.sServices.get("saveProfileDecision").get("EndpointURL");
				System.out.println("RequestBody==>"+RequestBody.replaceAll("=", ":"));
				Response cResponse = oRestServiceUtils.PostServiceWithSessionID(sEndpntSavePrfDescn, RequestBody.replaceAll("=", ":"));
				System.out.println(cResponse.getStatusCode());
				if(cResponse.getStatusCode()==200||cResponse.getStatusCode()==201)
				{
					System.out.println("capturetheDecisionWithOneorMoreStatus");
				}
				else
				{
					Assert.assertTrue("requestBody=>"+RequestBody.replaceAll("=", ":")+",Response=>"+cResponse.getStatusCode(), false);
				}
			}
		
	
	}

	@Step
	public void logintoApplicationForFinalize(String sUserName) throws Exception {

		String  InsuranceKey=null;
		String payerkey=null;
		String claimtype=null;
		ArrayList<Map<String , Object>> ppslist  = new ArrayList<Map<String,Object>>();
		
		List<String> DPKeysList=Arrays.asList(Serenity.sessionVariableCalled("DPkey").toString().split(","));
		int iClientKey = Integer.parseInt(Serenity.sessionVariableCalled("clientkey"));
		

		for (int p = 0; p < DPKeysList.size(); p++)
		{         
			String sDPKey = DPKeysList.get(p).trim();
			int iDPKey= Integer.parseInt(sDPKey);
			//To Retrieve the payer and Lobs from the captured DP
			MongoDBUtils.GettheCapturedDispositionPayerLOBClaimTypesFromtheGiven(sDPKey);
			for (int i = 0; i < ProjectVariables.PPSKeyList.size(); i++) 
			{	
				HashMap<String,Object> oPPS = new HashMap<String,Object>();
				InsuranceKey=StringUtils.substringBetween(ProjectVariables.PPSKeyList.get(i), "-","-");
				payerkey=StringUtils.substringBefore(ProjectVariables.PPSKeyList.get(i), "-"+InsuranceKey);
				claimtype=StringUtils.substringAfterLast(ProjectVariables.PPSKeyList.get(i), "-");
				oPPS.put("\"payerKey\"", Integer.valueOf(payerkey));
				oPPS.put("\"insuranceKey\"", InsuranceKey);
				oPPS.put("\"dpKey\"", iDPKey);
				oPPS.put("\"claimType\"", "\""+claimtype+"\"");
				ppslist.add(oPPS);
			}
				String RequestBody="{\"dpKeys\":["+iDPKey+"],\"insuranceKeys\":null,\"payerKeys\":null,\"clientKey\":"+iClientKey+","
						+ "\"profileId\":\""+Serenity.sessionVariableCalled("profileId")+"\",\"decisionFilter\":\"All\",\"updatedBy\":\""+Serenity.sessionVariableCalled("User")+"\",\"dpPayerLobs\":"+ppslist+"}\r\n";
				System.out.println("RequestBody==>"+RequestBody.replaceAll("=", ":"));
				String sDecisionsFinalizeEndPnt= ProjectVariables.sServices.get("getDecisionsFinalize").get("EndpointURL");
				Response sResponse = oRestServiceUtils.PostServiceWithSessionID(sDecisionsFinalizeEndPnt, RequestBody.replaceAll("=", ":"));
				System.out.println(sResponse.getStatusCode());
				if(sResponse.getStatusCode()==200||sResponse.getStatusCode()==201)
				{
					System.out.println("InsuranceKey:"+InsuranceKey+", PayerKey:"+payerkey+", DPKey:"+iDPKey+", claimtype:"+claimtype+" was finalized successfully, Presentation:"+Serenity.sessionVariableCalled("PresentationName"));
				}
				else
				{
					Assert.assertTrue("InsuranceKey:"+InsuranceKey+", PayerKey:"+payerkey+", DPKey:"+iDPKey+", claimtype:"+claimtype+" was unable to finalized+,requestBody=>"+RequestBody.replaceAll("=", ":")+",Response=>"+sResponse.getStatusCode(), false);
				} 
	
			}
		}

	@Step
	public void verifyDataPersistedInMongoCollection() 
	{
		MongoDBUtils.cdmDataInsertionIntoDecisionCollection();
	}
	
	public void service_For_CDM_Decision_Data_Insertion_Into_Decision_Collection() throws ParseException, IOException {
		//To load the services from excel into HasHMap
		//ExcelUtils.loadServices();
		HashMap<String,Object> latestDecisionFields = new HashMap<String,Object>();
		        HashMap<String,Object> idFields = new HashMap<String,Object>();
		        latestDecisionFields.put("dpKey", GenericUtils.GetRandomNumber());
		        latestDecisionFields.put("payerKey", GenericUtils.GetRandomNumber());
		        latestDecisionFields.put("claimType", "A");
		        latestDecisionFields.put("lobKey", 3); 
		        
		        List<Map<String , Object>> sFields  = new ArrayList<Map<String,Object>>();        
		        sFields.add(latestDecisionFields);
		        idFields.put("id",latestDecisionFields);
		        
		        String dpKey=String.valueOf(latestDecisionFields.get("dpKey"));
		String payerKey=String.valueOf(latestDecisionFields.get("payerKey"));
		   Serenity.setSessionVariable("latestDecisionDPKey").to(dpKey);
		   Serenity.setSessionVariable("latestDecisionPayerKey").to(payerKey);
		String sBody = "["+JsonBody.getRequestPayload("cdmLatestDecision", idFields)+"]";
		String sEndPointLatestDecision = ProjectVariables.sServices.get("cdmLatestDecision").get("EndpointURL"); 
		//String sBody = ProjectVariables.sServices.get("cdmLatestDecision").get("RequestBody"); 
		io.restassured.response.Response oResponse = oRestServiceUtils.PostServiceWithSessionID(sEndPointLatestDecision,sBody);
		System.out.println("Status Code=>"+oResponse.getStatusCode());
		if (oResponse.getStatusCode() == 200 || oResponse.getStatusCode() == 201)
		System.out.println("Successfully saved latest decisions");
		else 
		Assert.assertTrue("requestBody=>" + sBody.replaceAll("=", ":") + ",Response Status Code=>" + oResponse.getStatusCode(),
		false);
		}
	
	@Step
	public void createChangePresentationProfile(String sUser, String sClient, String sPayershorts, String sLobs, String sProduct,
			String sPriority) throws ParseException, IOException {
		String sPresentation=oPresentationProfile.createChangePresentationThroughService(sUser, sClient, sPayershorts, sLobs, sProduct, sPriority);
		Serenity.setSessionVariable("profileId").to(StringUtils.substringBefore(sPresentation, "-"));
		Serenity.setSessionVariable("PresentationName").to(StringUtils.substringAfter(sPresentation, "-"));
		Serenity.recordReportData().withTitle("PresentationName").andContents(Serenity.sessionVariableCalled("PresentationName").toString());
		Serenity.recordReportData().withTitle("profileId").andContents(Serenity.setSessionVariable("profileId").toString());
		System.out.println("ProfileID====>"+Serenity.sessionVariableCalled("profileId"));
		System.out.println("PresentationName====>"+Serenity.sessionVariableCalled("PresentationName"));
		
	}

	@Step
	public void assignChangeDPsToProfile() throws ParseException, IOException {
		String getRequestBody = "{\"clientKey\":"+Serenity.sessionVariableCalled("clientkey")+",\"dpKeys\":["+Serenity.sessionVariableCalled("PREV_DP").toString()+"],\"action\":\"Assign\",\"presentationAddition\":{\"profileId\":\""+Serenity.sessionVariableCalled("profileId")+"\",\"profileName\":\""+Serenity.sessionVariableCalled("PresentationName")+"\"}}";
		System.out.println("RequestBody==>"+getRequestBody.replaceAll("=", ":"));
		String sEndpoint = ProjectVariables.sServices.get("assignChangeDPs").get("EndpointURL");
		oRestServiceUtils.getResponseBodyforPostServiceWithSessionID(sEndpoint, getRequestBody);
	}

	@Step
	public void userCaptureAndFinalizeDecisionOnChangeDP(String sDecision,String sFinalize) throws ParseException, IOException {
		String sBody;
		String sEndpoint;
		io.restassured.path.json.JsonPath oReponse;
		sBody = ProjectVariables.sServices.get("captureDecisionOnChangeDPs").get("RequestBody");
		sBody=sBody.replace("Approve",sDecision).replace("iht_ittest09",Serenity.sessionVariableCalled("User").toString()).replace("4289", Serenity.sessionVariableCalled("CUR_DP")).replace("48", Serenity.sessionVariableCalled("clientkey").toString())
				.replace("proID", Serenity.sessionVariableCalled("profileId").toString());
		sEndpoint = ProjectVariables.sServices.get("captureDecisionOnChangeDPs").get("EndpointURL");
		oReponse = oRestServiceUtils.getResponseBodyforPostServiceWithSessionID(sEndpoint, sBody);
		if(sFinalize.equals("Yes")){
			sBody = ProjectVariables.sServices.get("finalizeChangeDPs").get("RequestBody");
			sBody=sBody.replace("iht_ittest09",Serenity.sessionVariableCalled("User").toString()).replace("4166", "4170").replace("48", Serenity.sessionVariableCalled("clientkey").toString())
					.replace("test", Serenity.sessionVariableCalled("profileId").toString()).replace("Cigna", Serenity.sessionVariableCalled("client").toString());
			sEndpoint = ProjectVariables.sServices.get("finalizeChangeDPs").get("EndpointURL");
			oReponse = oRestServiceUtils.getResponseBodyforPostServiceWithSessionID(sEndpoint, sBody);
		}
	}


}

