package project.runner;

import static org.hamcrest.Matchers.containsString;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.MultiHashMap;
import org.apache.commons.lang3.StringUtils;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import com.google.gson.Gson;

import net.serenitybdd.core.Serenity;
import project.pageobjects.CPWPage;
import project.utilities.ExcelUtils;
import project.utilities.GenericUtils;
import project.utilities.MicroServRestUtils;
import project.utilities.MongoDBUtils;
import project.utilities.RestServiceUtils;
import project.variables.JsonBody;
import project.variables.MonGoDBQueries;
import project.variables.ProjectVariables;

public class test {

	public static void main(String[] args) throws ParseException, IOException, org.json.simple.parser.ParseException {

		
		RestServiceUtils oRestServiceUtils = new RestServiceUtils();
		List<String> Payers = null;
		List<String> LOBS=null;
		List<String> Claimtypes=null;
		List<String> Output=null;
		
		Serenity.setSessionVariable("clientkey").to("11");
		Serenity.setSessionVariable("release").to("201911");
		
		Serenity.setSessionVariable("user").to("natuva");
		Serenity.setSessionVariable("Medicalpolicy").to("Add-on Code Policy");
		Serenity.setSessionVariable("SelectedPayerShort").to("UHCAZ");
		Serenity.setSessionVariable("Topic").to("Properties of Add-on Codes");
		
		
		//MongoDBUtils.connectWithCredentials(ProjectVariables.MONGO_SERVER_PORT);
		
		System.out.println(GenericUtils.ConvertEpochtoDate("1573583795822", "dd/MM/yyyy h:mm:ss a"));
		
		//MongoDBUtils.GettheCapturedDispositionPayerLOBsFromtheGiven("Medicaid - Texas State Policy", "Present", "10633");
		
		
		


	}

}