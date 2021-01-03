package project.utilities;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import project.pageobjects.*;
import project.variables.ProjectVariables;

public class AppUtils 
{

	 SeleniumUtils objSeleniumUtils = new  SeleniumUtils();
	 FilterDrawer  refFilterDrawer =  new  FilterDrawer() ; 
	
	 

	public static List<String> service_call_to_get_all_assigned_clients() throws ParseException {
		ArrayList<String> clientList = new ArrayList<String>();
		String response = RestAssured.given().contentType(ContentType.JSON).accept(ContentType.JSON)
				.body("{\"userName\": \"iht_ittest09\"}").when()
				.post(ProjectVariables.ROOT_URI + ProjectVariables.CLIENT_TEAM_DATA_ENDPOINT).asString();
		JSONParser parser = new JSONParser();
		JSONObject jo = (JSONObject) parser.parse(response);
		JSONArray ja = (JSONArray) jo.get("result");
		Iterator<Map.Entry> itr1;
		Iterator itr2 = ja.iterator();
		while (itr2.hasNext()) {
			itr1 = ((Map) itr2.next()).entrySet().iterator();
			while (itr1.hasNext()) {
				Map.Entry pair = itr1.next();
				if (pair.getKey().equals("clientName")) {
					clientList.add(pair.getValue().toString());
				}
			}
		}
		Collections.sort(clientList);
		return clientList;
	}

		
	
	
	
	
}
