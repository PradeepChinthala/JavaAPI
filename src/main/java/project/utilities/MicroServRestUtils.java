package project.utilities;


import java.io.IOException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.junit.Assert;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.response.ValidatableResponse;
import project.variables.ProjectVariables;
import net.serenitybdd.core.Serenity;
import net.serenitybdd.rest.SerenityRest;



public class MicroServRestUtils {

	private static MicroServRestUtils instance = null;

	private MicroServRestUtils() {
		// Exists only to defeat instantiation.
	}

	public static MicroServRestUtils getInstance() {
		if(instance == null) {
			instance = new MicroServRestUtils();
		}
		return instance;
	}

	static List<List<String>> responseCollection = new ArrayList<List<String>>();

	
    //Global Setup Variables
    public static String path;
    
   	public static ArrayList<String> Post_the_Data_with_Rest_Assured_And_Fetch_Clients(String jsondata, String ServiceUrl) throws IOException
   	
      	{
       	
       	ProjectVariables.ResponseOutput=null;
       	ArrayList<String> list = new ArrayList<>();
       	int statusCode = 0;
   	
       	Response response;
       	response = RestAssured.given().contentType(ContentType.JSON).accept(ContentType.JSON).body(jsondata).when().post(ServiceUrl);
       
       	String jsonResponseStr = response.getBody().asString();
       	statusCode = response.getStatusCode();
    	String colData=StringUtils.substringBetween(jsonResponseStr, "[", "]");
       	String[] jsonData = colData.split("},");
       
      
       	for(int i=0;i<jsonData.length;i++){
       		String ExactData=jsonData[i].replaceAll("\"","");
       		
       		String ClientKey=StringUtils.substringBetween(ExactData, "clientId:", ",clientName");	   
       		
       		
       		String strClient=StringUtils.substringAfter(jsonData[i],  "clientName");
       		String ClientName=StringUtils.substringBetween(strClient, ":\"", "\"");	      				
       				list.add(ClientName+"="+ClientKey);
       		ProjectVariables.clientKeysList.add(ClientKey);
       	}
   			
    
   		System.out.println("POST Response Code :  " + statusCode);
   		
   		Serenity.setSessionVariable("ResponseCode").to(statusCode);
   		
   		ProjectVariables.clientNamesList=list;
   	
   		
   		
   	  if(statusCode==201||statusCode==200)
   	    {
   	    	GenericUtils.logMessage("Json data was inserted successsully through service url ======>"+ServiceUrl);
   	    	
   	    }
   	    else
   	    {
   	    	GenericUtils.logMessage("Json data was not inserted through service url ======>"+ServiceUrl+",Response code was ======>"+statusCode);
   	    	
   	    }
   	
   	  	if(list.isEmpty())
   	  	{
   	  		Assert.assertTrue("Unable to retrieve the clientkeys from the service", false);
   	  	}
   	  
   	  return list;
   	
      	}

    
    public static boolean RetrievetheClientdatafromtheResponse(String username, String serviceurl) throws IOException {
		int j=0;
		String  ClientKey=null;
		String  Clientname=null;
		ArrayList<String> Clientlist=new ArrayList<>();
		boolean bstatus=false;

		String ServiceRequestBody= "{\r\n" +"	\"userName\": \""+username+"\"\r\n" +"}\r\n";

		//method to post the given data in the given service
		bstatus=MicroServRestUtils.Post_the_Data_with_Rest_Assured(ServiceRequestBody, serviceurl);

		List<String> OutputList=Arrays.asList(ProjectVariables.ResponseOutput.split("}"));

		for (int i = 0; i < OutputList.size(); i++) {
			j=j+1;
			String Exactdata=StringUtils.substringAfter(OutputList.get(i), "{");
			ClientKey=StringUtils.substringBetween(Exactdata, "clientId:",",clientName");
			Clientname=StringUtils.substringAfterLast(Exactdata, "clientName:");
			Clientlist.add(Clientname.trim());
			ProjectVariables.Clientkeylist.add(Long.valueOf(ClientKey));

		}


		System.out.println("Total Assigned CLients ==>"+j);
		System.out.println("Total Clients ==>"+Clientlist);
		System.out.println("Total ClientKeys ==>"+ProjectVariables.Clientkeylist);
		return bstatus;
	}

	public static boolean Post_the_Data_with_Rest_Assured(String jsondata, String ServiceUrl) throws IOException
	{

		ProjectVariables.ResponseOutput=null;

		int statusCode = 0;

		Response response;
		response = RestAssured.given().contentType(ContentType.JSON).accept(ContentType.JSON).body(jsondata).when().post(ServiceUrl);

		//response = RestAssured.given().auth().basic("TestAuto", "Ihealth@123").post(ServiceUrl);
		String jsonResponseStr = response.getBody().asString();
		statusCode = response.getStatusCode();

		String Output=jsonResponseStr.replaceAll("\"","");

		//System.out.println(Output);

		String ExactOutput=StringUtils.substringBetween(Output, "result:[", "],isError");
		//System.out.println("Original Posted Response BOdy ===>"+ExactOutput);
		System.out.println("POST Response Code :  " + statusCode);

		Serenity.setSessionVariable("ResponseCode").to(statusCode);

		ProjectVariables.ResponseOutput=ExactOutput;



		if(statusCode==201||statusCode==200)
		{
			System.out.println("Json data was posted successsully through service url ======>"+ServiceUrl);

			if(ExactOutput.isEmpty())
			{

				return false;

			}


			return true;
		}
		else
		{
			System.out.println("Json data was not posted through service url ======>"+ServiceUrl+",Response code was ======>"+statusCode);

			return false;
		}



	}

	public static ValidatableResponse getResponseBody(String sEndPointURL){

		Map<String, String> headers = new HashMap<String, String>();
		headers.put("Content-Type", "application/json");
		headers.put("X-Session-Id", Serenity.sessionVariableCalled("sessionID"));

		ValidatableResponse sResponse = 
				SerenityRest.given().headers(headers).when().get(sEndPointURL).then().statusCode(200);

		return sResponse;

	}
	
	public static boolean Post_the_Data__through_service_using_HTTP_Client(String jsondata, String ServiceUrl) throws IOException
	{

		ProjectVariables.ResponseOutput=null;

		int statusCode = 0;

		Response response;
		response = RestAssured.given().contentType(ContentType.JSON).accept(ContentType.JSON).body(jsondata).when().post(ServiceUrl);

		//response = RestAssured.given().auth().basic("TestAuto", "Ihealth@123").post(ServiceUrl);
		String jsonResponseStr = response.getBody().asString();
		statusCode = response.getStatusCode();

		String Output=jsonResponseStr.replaceAll("\"","");

		System.out.println("ResponseBody::"+Output);
		System.out.println("POSTResponseCode:" + statusCode);

		Serenity.setSessionVariable("ResponseCode").to(statusCode);

		ProjectVariables.ResponseOutput=Output;
		if(statusCode==201||statusCode==200)
		{
			System.out.println("Json data was posted successsully through service url ======>"+ServiceUrl);

			return true;
		}
		else
		{
			System.out.println("Json data was not posted through service url ======>"+ServiceUrl+",Response code was ======>"+statusCode);

			return false;
		}



	}
	
}

