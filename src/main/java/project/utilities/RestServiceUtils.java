package project.utilities;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.simple.parser.ParseException;

import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.response.ValidatableResponse;
import net.serenitybdd.core.Serenity;
import net.serenitybdd.core.environment.EnvironmentSpecificConfiguration;
import net.serenitybdd.rest.SerenityRest;
import net.thucydides.core.util.EnvironmentVariables;
import net.thucydides.core.util.SystemEnvironmentVariables;
import project.variables.JsonBody;
import project.variables.ProjectVariables;

import static org.hamcrest.Matchers.*;

public class RestServiceUtils {

	public static String sBase;
	//private EnvironmentVariables environmentVariables;

	public RestServiceUtils(){
		EnvironmentVariables environmentVariables = SystemEnvironmentVariables.createEnvironmentVariables();       
		//String myCustomProperty = variables.getProperty("restapi.baseurl");*/
		
		String APP_URL = EnvironmentSpecificConfiguration.from(environmentVariables).getProperty("restapi.baseuri");
		sBase = APP_URL;

	} 

	public Response getPostResponse(String sEndPointURL ,String sBody){

		Map<String, String> headers = new HashMap<String, String>();
		headers.put("Content-Type", "application/json");
		System.out.println("BaseURI::"+sBase);

		Response sResponse =  SerenityRest.given().baseUri(sBase)
				.contentType("application/json")
				.headers(headers)
				.body(sBody)
				.when()
				.post(sEndPointURL);

		return sResponse;

	}

	public Response getPostResponsewithSessionID(String sEndPointURL ,String sBody){

		Map<String, String> headers = new HashMap<String, String>();
		headers.put("Content-Type", "application/json");
		headers.put("X-Session-Id", Serenity.sessionVariableCalled("sessionID"));

		Response sResponse =  SerenityRest.given().baseUri(sBase)
				.contentType("application/json")
				.headers(headers)
				.body(sBody)
				.when()
				.post(sEndPointURL);

		return sResponse;

	}
	
	public JsonPath getResponseBodyPostService(String sEndPointURL ,String sBody){
		JsonPath sJsonExtract = getPostResponse(sEndPointURL,sBody).then().assertThat().statusCode(200).extract().jsonPath();
		return sJsonExtract;

	}

	public Response getService(String sEndPointURL){
		Response oResponse =  SerenityRest.given().get(sEndPointURL);
		return oResponse;
	}


	public ValidatableResponse ValidategetRequestWithParams(HashMap<String,Object> parametersMap, String sEndpointURL){

		ValidatableResponse sResponse = getRequestWithParams(parametersMap,sEndpointURL).then().assertThat().statusCode(200);

		return sResponse;

	}

	public Response getRequestWithParams(HashMap<String,Object> parametersMap, String sEndpointURL){

		Map<String, String> headers = new HashMap<String, String>();
		headers.put("Content-Type", "application/json");
		headers.put("X-Session-Id", Serenity.sessionVariableCalled("sessionID"));

		Response sResponse =  SerenityRest.given()
				.headers(headers)
				.baseUri(sBase)
				.parameters(parametersMap)
				.when()
				.get(sEndpointURL);

		return sResponse;

	}


	public JsonPath getResponseBodyFromGetRequestWithParams(HashMap<String,Object> parametersMap, String sEndpointURL){

		JsonPath sResponse = getRequestWithParams(parametersMap,sEndpointURL).getBody().jsonPath();

		return sResponse;

	}

	public ValidatableResponse postWithParams(HashMap<String,Object> parametersMap, String sEndPointURL){

		Map<String, String> headers = new HashMap<String, String>();
		headers.put("Content-Type", "application/json");
		headers.put("X-Session-Id", Serenity.sessionVariableCalled("sessionID"));

		ValidatableResponse sResponse = 
				 SerenityRest.given()
		.headers(headers)
		.baseUri(sBase)
		.parameters(parametersMap)
		.when()
		.post(sEndPointURL)
		.then().assertThat().statusCode(200);

		return sResponse;

	}

	public Response getResponsePostWithParams(HashMap<String,Object> parametersMap, String sEndPointURL){

		Map<String, String> headers = new HashMap<String, String>();
		headers.put("Content-Type", "application/json");
		headers.put("X-Session-Id", Serenity.sessionVariableCalled("sessionID"));

		Response sResponse =  SerenityRest.given()
				.headers(headers)
				.baseUri(sBase)
				.parameters(parametersMap)
				.when()
				.post(sEndPointURL);

		return sResponse;

	}

	public ValidatableResponse ValidatepostWithPathParams(Object parameter, String sEndPointURL,String sBody){

		ValidatableResponse sResponse = postWithPathParams( parameter,  sEndPointURL, sBody).then().assertThat().statusCode(200);

		return sResponse;

	}

	public Response postWithPathParams(Object parameter, String sEndPointURL,String sBody){

		Map<String, String> headers = new HashMap<String, String>();
		headers.put("Content-Type", "application/json");
		headers.put("X-Session-Id", Serenity.sessionVariableCalled("sessionID"));

		Response sResponse = 
				 SerenityRest.given()
		.headers(headers)
		.baseUri(sBase)
		.body(sBody)
		.when()
		.post(sEndPointURL,parameter);

		return sResponse;

	}

	public ValidatableResponse getResponseBodyWithPathParams(String sEndpointURL, HashMap<String,String> sValue){

		ValidatableResponse sResponse = getRequestWithPathParams(sEndpointURL,sValue).then().assertThat().statusCode(200);

		return sResponse;

	}

	public Response getRequestWithPathParams(String sEndpointURL, HashMap<String,String> sValue){

		Map<String, String> headers = new HashMap<String, String>();
		headers.put("Content-Type", "application/json");
		headers.put("X-Session-Id", Serenity.sessionVariableCalled("sessionID"));
		Response sResponse =  SerenityRest.given().headers(headers).baseUri(sBase).when().get(sEndpointURL,sValue);

		return sResponse;

	}

	public Response PostServiceWithSessionID(String sEndPointURL ,String sBody){

		Map<String, String> headers = new HashMap<String, String>();
		headers.put("Content-Type", "application/json");
		headers.put("X-Session-Id", Serenity.sessionVariableCalled("sessionID"));

		Response sResponse =  SerenityRest.given().baseUri(sBase)
				.contentType("application/json")
				.headers(headers)
				.body(sBody)
				.when()
				.post(sEndPointURL);

		return sResponse;

	}

	public JsonPath getResponseBodyforPostServiceWithSessionID(String sEndPointURL ,String sBody){
		Response sReponse = PostServiceWithSessionID(sEndPointURL,sBody);
		sReponse.then().assertThat().statusCode(200);
		JsonPath sResponse = sReponse.jsonPath();
		return sResponse;

	}

	public static Response getResponseBody(String sEndPointURL){
//asdas
		Map<String, String> headers = new HashMap<String, String>();
		
		Response oResponse =  SerenityRest.given().get("https://cpd-gateway-qa1.cotiviti.com/mdm-data/v1/policyset/43");
		
		System.out.println(RestAssured.get("https://cpd-gateway-qa1.cotiviti.com/mdm-data/v1/policyset/43").getBody());
		
		System.out.println(oResponse.jsonPath().get("payer_id").toString());
		
		/*System.out.println(SerenityRest.get("https://cpd-gateway-qa1.cotiviti.com/mdm-data/v1/policyset/43").getBody());
		
		;
		
		
		
		headers.put("Content-Type", "application/json");
		headers.put("X-Session-Id", Serenity.sessionVariableCalled("sessionID"));

		ValidatableResponse sResponse = 
				 SerenityRest.given().headers(headers).baseUri(sBase).when().get(sEndPointURL).then().statusCode(200);
		
		 
				 Response sResponse = SerenityRest.given().headers(headers).baseUri(sBase).when().get(sEndPointURL);

				 SerenityRest.get("https://cpd-gateway-qa1.cotiviti.com/mdm-data/v1/policyset/43").getBody();
				 
				 System.out.println(SerenityRest.get("https://cpd-gateway-qa1.cotiviti.com/mdm-data/v1/policyset/43").getBody());
				*/
				 
		return oResponse;

	}

	public static Object getValuefromJson(JSONObject x,String item) throws JSONException
	{

		Object finalresult = null;	
		JSONArray keys =  x.names();
		for(int i=0;i<keys.length();i++)
		{


			if(finalresult!=null)
			{
				return finalresult;                    
			}

			String current_key = keys.get(i).toString();

			if(current_key.equals(item))
			{
				finalresult=x.get(current_key);
				return finalresult;
			}

			if(x.get(current_key).getClass().getName().equals("org.json.JSONObject"))
			{
				return getValuefromJson((JSONObject) x.get(current_key),item);
			}
			else if(x.get(current_key).getClass().getName().equals("org.json.JSONArray"))
			{
				for(int j=0;j<((JSONArray) x.get(current_key)).length();j++)
				{
					if(((JSONArray) x.get(current_key)).get(j).getClass().getName().equals("org.json.JSONObject"))
					{
						getValuefromJson((JSONObject)((JSONArray) x.get(current_key)).get(j),item);
					}
				}
			}
		}
		return null;
	}

	public Response getAvailableOppurtunitiesforPresenation(String sClientKey, String sPrfID , String payershort, String lob,
			String topics) throws ParseException, IOException {

		HashMap<String,Object> sFields = new HashMap<String,Object>();
		sFields.put("clientKey", sClientKey);
		sFields.put("insuranceDesc", lob.split(";"));
		sFields.put("payerKeys",payershort.split(";"));
		sFields.put("profileId",sPrfID);

		String sBody = JsonBody.getRequestPayload("getPresentationManagerOpportunites", sFields);
		String sEndpoint = ProjectVariables.sServices.get("getPresentationManagerOpportunites").get("EndpointURL");

		Response oReponse = PostServiceWithSessionID(sEndpoint, sBody);
		return oReponse;
	}

	public void getAllinformationforGivenClient(String sClient) {
		String sClientKey = MongoDBUtils.Retrieve_the_PayerKey_From_mongodb_with_the_given_Payershort(sClient, "Client");
		Serenity.setSessionVariable("clientkey").to(sClientKey);

		String sEndpoint = ProjectVariables.sServices.get("getPMSummaryInformation").get("EndpointURL");
		HashMap<String,String> sValue = new HashMap<String,String>();
		sValue.put("id", sClientKey);

		JsonPath savl = getRequestWithPathParams(sEndpoint,sValue).jsonPath();
		ArrayList<Object> sTopics = savl.get("opportunitySavings.hierarchy.topicKey");		   
		HashSet<Object> AllTopics = new HashSet<Object>(sTopics);
		Serenity.setSessionVariable("AllTopicKeys").to(AllTopics);

		ArrayList<Object> oPayers = savl.get("policySetConfig.payerKey");
		HashSet<Object> AllPayerKeys = new HashSet<Object>(oPayers);
		Serenity.setSessionVariable("AllPayerKeys").to(AllPayerKeys);

		ArrayList<Object> oPayerShorts = savl.get("policySetConfig.payerShort");
		HashSet<Object> AllPayerShorts = new HashSet<Object>(oPayerShorts);
		Serenity.setSessionVariable("AllPayerShorts").to(AllPayerShorts);

		ArrayList<Object> oInsuranceDesc = savl.get("policySetConfig.insuranceDesc");
		HashSet<Object> AllInsuranceDesc = new HashSet<Object>(oInsuranceDesc);
		Serenity.setSessionVariable("AllInsuranceDesc").to(AllInsuranceDesc);

		ArrayList<Object> oMedicalPolicyDesc = savl.get("opportunitySavings.hierarchy.medPolicyDesc");
		HashSet<Object> AllMPDesc = new HashSet<Object>(oMedicalPolicyDesc);
		Serenity.setSessionVariable("AllMPDesc").to(AllMPDesc);

	}

	public void getAllinformationforGivenClientPresentionProfile(String sPresentationID) {

		String sPrfStateEndPoint = ProjectVariables.sServices.get("Profilepayerlob").get("EndpointURL");
		HashMap<String,String> sProfileState = new HashMap<String,String>();
		sProfileState.put("profileid",sPresentationID);
		Response sResponse = getRequestWithPathParams(sPrfStateEndPoint, sProfileState);
		JsonPath sResponsePath = sResponse.getBody().jsonPath();

		ArrayList<Object> oPayers = sResponsePath.get("payerShort");
		HashSet<Object> AllPayerShorts = new HashSet<Object>(oPayers);
		Serenity.setSessionVariable("AllPresentationPayerShorts").to(AllPayerShorts);

		ArrayList<Object> oInsuranceDesc = sResponsePath.get("insuranceDesc");
		HashSet<Object> AllInsuranceDesc = new HashSet<Object>(oInsuranceDesc);
		Serenity.setSessionVariable("AllPresentationInsuranceDesc").to(AllInsuranceDesc);

		ArrayList<Object> oPayerKeys = sResponsePath.get("payerKey");
		HashSet<Object> AllPayerKeys = new HashSet<Object>(oPayerKeys);
		Serenity.setSessionVariable("AllPresentationPayerKeys").to(AllPayerKeys);

		ArrayList<Object> oinsuranceKeys = sResponsePath.get("insuranceKey");
		HashSet<Object> AllInsuranceKeys = new HashSet<Object>(oinsuranceKeys);
		Serenity.setSessionVariable("AllPresentationInsuranceKeys").to(AllInsuranceKeys);

	}

	public Response getAvailableOppurtunitiesforGivenInput(String ClientKey,HashSet<Object> sPayerKeys,HashSet<Object> sInsuranceDesc,HashSet<Object> sTopicKeys) throws ParseException, IOException {

		HashMap<String,Object> sFields = new HashMap<String,Object>();
		sFields.put("clientKey", ClientKey);
		sFields.put("insuranceDesc", sInsuranceDesc);
		sFields.put("payerKeys",sPayerKeys);
		sFields.put("topicKeys",sTopicKeys);

		String sBody = JsonBody.getRequestPayload("getOppurtunities", sFields);
		String sEndpoint = ProjectVariables.sServices.get("getOppurtunities").get("EndpointURL");

		Response oReponse = PostServiceWithSessionID(sEndpoint, sBody);
		return oReponse;

	}

	public void assignDPtoProfile(String sClientKey,String sDPKey ,String sTopicKey ,String sProfileID , String sProfileName ) throws ParseException, IOException {

		
		int iTopicKey = Integer.parseInt(sTopicKey);
		int[] arTopicKeys = new int[]{iTopicKey};				

		HashMap<String,Object> sFields = new HashMap<String,Object>();
		sFields.put("topicKeys", arTopicKeys);
		
		int iClientKey = Integer.parseInt(sClientKey);
		int iDPKey= Integer.parseInt(sDPKey);
		//int iInsuranceKey = Integer.parseInt(Serenity.sessionVariableCalled("InsuranceKeys").toString().split(",")[0]);
		String sDpLobBody = JsonBody.getRequestPayload("getDpLobInfo", sFields);
		String sLobEndpoint = ProjectVariables.sServices.get("getDpLobInfo").get("EndpointURL");
		JsonPath sResposeBody = postWithPathParams(iClientKey,sLobEndpoint,sDpLobBody).getBody().jsonPath();

		String sPayershort = sResposeBody.get("dpLobInfoList[0].ppsBasedcdmDecisions.pps.payerShort[0]");
		String sInsuranceDesc = sResposeBody.get("dpLobInfoList[0].ppsBasedcdmDecisions.pps.insuranceDesc[0]");
		
		String sClaimType = sResposeBody.get("dpLobInfoList[0].ppsBasedcdmDecisions.pps.claimType[0]");

		String sPayeKey = MongoDBUtils.Retrieve_the_PayerKey_From_mongodb_with_the_given_Payershort(sPayershort, "payerShort");

		HashMap<String,Object> sFieldsprof = new HashMap<String,Object>();
		sFieldsprof.put("profileId", sProfileID);
		sFieldsprof.put("profileName", sProfileName);

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
		
		List<Map<String , Object>> lstpps  = new ArrayList<Map<String,Object>>();		
		lstpps.add(oPPs);
		
		sProfileLobMap.put("pps",lstpps);
		
		List<Map<String , Object>> lstProfile  = new ArrayList<Map<String,Object>>();		
		lstProfile.add(sProfileLobMap);

		sFields1.put("profileTopics", lstProfile);

		String sUpdateAssignmentBdy = JsonBody.getRequestPayload("updateAssignments", sFields1);
		String sUpdateAssignmentEndPnt= ProjectVariables.sServices.get("updateAssignments").get("EndpointURL");

		Response sResponse = PostServiceWithSessionID(sUpdateAssignmentEndPnt, sUpdateAssignmentBdy);

		sResponse.then().assertThat().body(containsString("true"));

	}

	public Response getRequestWithPathParams(String sEndpointURL){

		Map<String, String> headers = new HashMap<String, String>();
		headers.put("Content-Type", "application/json");
		headers.put("X-Session-Id", Serenity.sessionVariableCalled("sessionID"));
		Response sResponse =  SerenityRest.given().headers(headers).baseUri(sBase).when().get(sEndpointURL);

		return sResponse;

	}

	
}

