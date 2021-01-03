
package project.utilities;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.bson.BsonDocument;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;
import org.json.JSONArray;
import org.json.simple.parser.JSONParser;
import org.junit.Assert;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.mongodb.BasicDBObject;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.AggregateIterable;
import com.mongodb.client.DistinctIterable;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Accumulators;
import com.mongodb.client.model.Aggregates;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Projections;
import com.mongodb.client.model.Sorts;
import com.mongodb.client.result.DeleteResult;

import net.serenitybdd.core.Serenity;
import net.serenitybdd.core.environment.EnvironmentSpecificConfiguration;
import net.thucydides.core.util.EnvironmentVariables;
import net.thucydides.core.util.SystemEnvironmentVariables;
import project.variables.OracleDBQueries;
import project.variables.ProjectVariables;

public class MongoDBUtils {

	static MongoClient mongoClient;
	static MongoDatabase db;
	static MongoCollection<Document> mColl;
	static FindIterable<Document> results;
	static MongoCursor<Document> cursor;
	static long recordsCount;
	static List<Document> resultsList = new ArrayList<Document>();
	static DistinctIterable<String> Distinctresults;
	static DistinctIterable<Double> DistinctresultsDouble;
	static DistinctIterable<Long> Distinctresults_with_long;

	private static MongoDBUtils instance = null;
	
	//private static EnvironmentVariables environmentVariables;
	
	
	public static void finalize2() {
		// Closing the Connection
		mongoClient.close();
	}

	public MongoDBUtils() {
		
		//EnvironmentVariables environmentVariables = null;
        //String MongoConURL = EnvironmentSpecificConfiguration.from(environmentVariables).getProperty("mongodb.connection.url");
		
		//mongoClient = new MongoClient(new MongoClientURI(MongoConURL));
		connectWithCredentials(ProjectVariables.MONGO_SERVER_PORT);
	}


	/*public static MongoDBUtils getInstance() {
		if (instance == null) {
			instance = new MongoDBUtils();
		}
		return instance;
	}*/

	// ########################CONNECTION RELATED
	// METHODS###############################################################

	// #### Validation Methods
	public static void connectWithCredentials(int port) {
		
		EnvironmentVariables environmentVariables = SystemEnvironmentVariables.createEnvironmentVariables();    
		
		String MongoConURL = EnvironmentSpecificConfiguration.from(environmentVariables).getProperty("mongodb.connection.url");
		
		mongoClient = new MongoClient(new MongoClientURI(MongoConURL));

		//mongoClient = new MongoClient(new MongoClientURI(
				//"mongodb+srv://cotiviti:MNE93Mh6@cpd-uat-pl-0-6oxiw.mongodb.net/?connectTimeoutMS=120000&ssl=true&authSource=admin&retryWrites=true&w=majority"));
		System.out.println(MongoConURL);
		System.out.println("############################# Connected Mongo DB successfully #####################");


	}
	
	public static void ConnectTogivenDBandCollection(String dbname,String collectionname) 
	{

		db = mongoClient.getDatabase(dbname);
		mColl = db.getCollection(collectionname);
	}

	public String getDPKeyDescriptionfromDB(String sClientKey, String dPKey, String sPresProfileName) {

		List<String> returnFields = new ArrayList<String>();
		List<Bson> returnFilters = new ArrayList<>();

		db = mongoClient.getDatabase("cpd");
		mColl = db.getCollection("oppty");
		cursor = mColl.find().iterator();
		Bson MatchFilter = new BsonDocument();

		if (sPresProfileName.isEmpty()) {
			MatchFilter = Filters.and(Filters.eq("_id.clientKey", Long.valueOf(sClientKey)),
					Filters.eq("subRule.hierarchy.dpKey", Long.valueOf(dPKey)));
		} else {
			MatchFilter = Filters.and(Filters.eq("_id.clientKey", Long.valueOf(sClientKey)),
					Filters.eq("subRule.hierarchy.dpKey", Long.valueOf(dPKey)),
					Filters.eq("disposition.desc", "Present"),
					Filters.eq("presentationProfiles.profileName", sPresProfileName));
		}

		// Get the DP Description
		DistinctIterable<String> outputDisp = mColl.distinct("subRule.hierarchy.dpDesc", MatchFilter, String.class);
		String outputDoc = outputDisp.first(); // Retrieve the First Value
		return outputDoc;

	}

	public List<String> getDPKeyPayershortsandLOBsfromDB(String sClientKey, String sDPKey, String sDataToRetrive,
			String sPlaceholderArg1, String sPlaceholderArg2) {
		List<String> returnFields = new ArrayList<String>();
		List<Bson> returnFilters = new ArrayList<>();

		db = mongoClient.getDatabase("cpd");
		mColl = db.getCollection("oppty");
		cursor = mColl.find().iterator();
		Bson MatchFilter = new BsonDocument();
		String sDistinctFilterValue = "";

		if (sDataToRetrive.equalsIgnoreCase("Payershorts")) {
			sDistinctFilterValue = "_id.payerShort";
		} else if (sDataToRetrive.equalsIgnoreCase("LOBs")) {
			sDistinctFilterValue = "insuranceDesc";
		}

		MatchFilter = Filters.and(Filters.eq("_id.clientKey", Long.valueOf(sClientKey)),
				Filters.eq("subRule.hierarchy.dpKey", Long.valueOf(sDPKey)), Filters.eq("disposition.desc", "Present"));
		DistinctIterable<String> outputDisp = mColl.distinct(sDistinctFilterValue, MatchFilter, String.class);

		MongoCursor<String> Itr = outputDisp.iterator();

		while (Itr.hasNext()) {
			returnFields.add(Itr.next());
		}
		return returnFields;

	}

	public static List<String> getPresentationProfileNamesForDPKey(String sClientKey, String sDPKey, String sDataToRetrive,
			String sPlaceholderArg1, String sPlaceholderArg2) {
		List<String> PresProfileNamesList = new ArrayList<String>();
		List<Bson> returnFilters = new ArrayList<>();

		db = mongoClient.getDatabase("cpd");
		mColl = db.getCollection("oppty");
		cursor = mColl.find().iterator();
		Bson MatchFilter = new BsonDocument();
		String sDistinctFilterValue = "";
		sDPKey = sDPKey.substring(2, sDPKey.length()).trim();

		sDistinctFilterValue = "presentationProfile.profileName";

		MatchFilter = Filters.and(Filters.eq("_id.clientKey", Long.valueOf(sClientKey)),
				Filters.eq("subRule.hierarchy.dpKey", Long.valueOf(sDPKey)));
		DistinctIterable<String> outputDisp = mColl.distinct(sDistinctFilterValue, MatchFilter, String.class);

		MongoCursor<String> Itr = outputDisp.iterator();

		while (Itr.hasNext()) {
			PresProfileNamesList.add(Itr.next());
		}
		return PresProfileNamesList;

	}

	public static String validateDPKeyPSLOBExistsinDB(String sDPKey, String sPayershort, String sLOB, String sPlaceholderArg1,
			String sPlaceholderArg2) {
		List<String> returnFields = new ArrayList<String>();
		List<Bson> returnFilters = new ArrayList<>();
		String sClientName = "";

		db = mongoClient.getDatabase("cpd");
		mColl = db.getCollection("oppty");
		cursor = mColl.find().iterator();
		Bson MatchFilter = new BsonDocument();
		String sDistinctFilterValue = "";

		MatchFilter = Filters.and(Filters.eq("insuranceDesc", sLOB), Filters.eq("_id.payerShort", sPayershort),
				Filters.eq("subRule.hierarchy.dpKey", Long.valueOf(sDPKey)), Filters.eq("disposition.desc", "Present"));
		DistinctIterable<String> outputDisp = mColl.distinct("clientDesc", MatchFilter, String.class);
		sClientName = outputDisp.first();
		return sClientName;

	}

	public static long retrieveAllDocuments(String dbname, String collectionname) {
		
		//connectWithCredentials(ProjectVariables.MONGO_SERVER_PORT);
		
		db = mongoClient.getDatabase(dbname);
		mColl = db.getCollection(collectionname);
		// cursor = mColl.find().iterator();
		results = mColl.find();
		recordsCount = mColl.count();

		System.out.println("Connected to Mongo DB successfully.....");
		System.out.println("Connected to Mongo DB,Time==>" + GenericUtils.getDateGivenFormat("dd/MM/yyyy h:mm:ss a"));

		return recordsCount;
	}

	public static long retrieveAllDocuments(String dbname, String collectionname, Bson sFilter) {
		
		//connectWithCredentials(ProjectVariables.MONGO_SERVER_PORT);

		db = mongoClient.getDatabase(dbname);
		mColl = db.getCollection(collectionname);
		// cursor = mColl.find().iterator();
		results = mColl.find(sFilter);
		recordsCount = mColl.count(sFilter);
		System.out.println("recordsCount::"+recordsCount);
		System.out.println("Connected to Mongo DB successfully.....");
		System.out.println("Connected to Mongo DB,Time==>" + GenericUtils.getDateGivenFormat("dd/MM/yyyy h:mm:ss a"));

		return recordsCount;
	}
	public static void retrieveFirstDocument(String dbname, String collectionname, Bson sFilter) {
		
		//connectWithCredentials(ProjectVariables.MONGO_SERVER_PORT);

		db = mongoClient.getDatabase(dbname);
		mColl = db.getCollection(collectionname);
		// cursor = mColl.find().iterator();
		results = mColl.find(sFilter).limit(1); 
		
		System.out.println("Connected to Mongo DB successfully.....");
		System.out.println("Connected to Mongo DB,Time==>" + GenericUtils.getDateGivenFormat("dd/MM/yyyy h:mm:ss a"));
	}
	
	public static void removeFirstDocument(String dbname, String collectionname, Bson sFilter) {
		
		//connectWithCredentials(ProjectVariables.MONGO_SERVER_PORT);

		db = mongoClient.getDatabase(dbname);
		mColl = db.getCollection(collectionname);
		DeleteResult result = mColl.deleteOne(sFilter);
		System.out.println("The Numbers of Deleted Document(s) : " + result.getDeletedCount());
		
	}
	
	public static String Retrieve_the_PayerKey_From_mongodb_with_the_given_Payershort(String fieldata,
			String fieldname) {

		BasicDBObject fields = new BasicDBObject();
		String outputresponse = null;

		Bson MatchFilter = new BsonDocument();

		if (fieldname.equalsIgnoreCase("Payershort")) {
			MatchFilter = Filters.and(
					Filters.eq("_id.clientKey", Long.valueOf(Serenity.sessionVariableCalled("clientkey"))),
					Filters.eq("payerShort", fieldata));

		} else if (fieldname.equalsIgnoreCase("Client")) {
			MatchFilter = Filters.eq("clientDesc", java.util.regex.Pattern.compile(fieldata));
		} else if (fieldname.equalsIgnoreCase("Insurance")) {
			MatchFilter = Filters.and(
					Filters.eq("_id.clientKey", Long.valueOf(Serenity.sessionVariableCalled("clientkey"))),
					Filters.eq("insuranceDesc", fieldata));
			// fields = new BasicDBObject("insuranceDesc", fieldata);
		} else if (fieldname.equalsIgnoreCase("PayerKey")) {
			MatchFilter = Filters.and(
					Filters.eq("_id.clientKey", Long.valueOf(Serenity.sessionVariableCalled("clientkey"))),
					Filters.eq("_id.payerKey", Long.valueOf(fieldata)));
			// fields = new
			// BasicDBObject("_id.payerKey",Long.valueOf(fieldata));
		} else if (fieldname.equalsIgnoreCase("Topic")) {
			MatchFilter = Filters.and(
					Filters.eq("_id.clientKey", Long.valueOf(Serenity.sessionVariableCalled("clientkey"))),
					Filters.eq("subRule.hierarchy.topicDesc", fieldata));
			// fields = new BasicDBObject("subRule.hierarchy.topicDesc",
			// fieldata);
		} else if (fieldname.equalsIgnoreCase("ClientKey")) {
			MatchFilter = Filters.and(Filters.eq("_id.clientKey", Long.valueOf(fieldata)));
			// fields = new BasicDBObject("subRule.hierarchy.topicDesc",
			// fieldata);
			
		}else if (fieldname.equalsIgnoreCase("Topic Based on DP")) {
			MatchFilter = Filters.and(
					Filters.eq("_id.clientKey", Long.valueOf(Serenity.sessionVariableCalled("clientkey"))),
					Filters.eq("subRule.hierarchy.topicDesc", fieldata),Filters.eq("subRule.hierarchy.dpKey", ProjectVariables.CapturedDPkey));
			// fields = new BasicDBObject("subRule.hierarchy.topicDesc",
			// fieldata);
		}
		else if (fieldname.equalsIgnoreCase("MP")) {
			MatchFilter = Filters.and(
					Filters.eq("_id.clientKey", Long.valueOf(Serenity.sessionVariableCalled("clientkey"))),
					Filters.eq("subRule.hierarchy.medPolicyDesc", fieldata));
			// fields = new BasicDBObject("subRule.hierarchy.topicDesc",
			// fieldata);
		}else {
			Assert.assertTrue("Given Field name was not availble in the method ===>" + fieldname, false);
		}

		retrieveFirstDocument("cpd", "oppty", MatchFilter);

		
		for (Document doc : results) {

			Document IDDoccument = doc.get("_id", Document.class);

			if (fieldname.equalsIgnoreCase("Payershort")) {

				outputresponse = String.valueOf(IDDoccument.get("payerKey"));
			} else if (fieldname.equalsIgnoreCase("Client")) {

				outputresponse = String.valueOf(IDDoccument.get("clientKey"));
			} else if (fieldname.equalsIgnoreCase("Insurance")) {

				outputresponse = String.valueOf(IDDoccument.get("insuranceKey"));

			} else if (fieldname.equalsIgnoreCase("PayerKey")) {

				outputresponse = String.valueOf(IDDoccument.get("payerShort"));

			} else if (fieldname.equalsIgnoreCase("ClientKey")) {

				outputresponse = String.valueOf(doc.get("clientDesc"));

			} else if (fieldname.equalsIgnoreCase("Topic")||fieldname.equalsIgnoreCase("Topic Based on DP")) {
				Document Ioccument = doc.get("subRule", Document.class);
				Document Iccument = Ioccument.get("hierarchy", Document.class);
				outputresponse = String.valueOf(Iccument.getLong("topicKey"));
			}
			else if (fieldname.equalsIgnoreCase("MP")) {
				Document Ioccument = doc.get("subRule", Document.class);
				Document Iccument = Ioccument.get("hierarchy", Document.class);
				outputresponse = String.valueOf(Iccument.getLong("medPolicyKey"));
			}

			break;

		}

		System.out.println("'" + fieldname + "' Key ===>" + outputresponse);

		if (outputresponse == null) 
		{
		Assert.assertTrue("Record count was 'zero' in the Mongo DB for the given MatchFilter ====>" + MatchFilter,false);
		}

		return outputresponse;

	}

	public static String getRawsavingsforDPCard(String sclientKey, String sDPKeyNo, List<String> selectedPayershorts,
			List<String> selectedLOBs, String sPresProfileName) {

		db = mongoClient.getDatabase("cpd");
		mColl = db.getCollection("oppty");
		cursor = mColl.find().iterator();
		Bson MatchFilter = new BsonDocument();

		MatchFilter = Filters.and(Filters.eq("_id.clientKey", Long.valueOf(sclientKey)),
				Filters.eq("subRule.hierarchy.dpKey", Long.valueOf(sDPKeyNo)),
				Filters.in("_id.payerShort", selectedPayershorts), Filters.in("insuranceDesc", selectedLOBs),
				Filters.eq("disposition.desc", "Present"),
				Filters.eq("presentationProfiles.profileName", sPresProfileName));
		Bson matchtext = Aggregates.match(Filters.and(MatchFilter));
		AggregateIterable<Document> output2 = mColl.aggregate(Arrays.asList(matchtext,
				Aggregates.group("$clientDesc", Accumulators.sum("Deck savings", "$annualSavings.raw"))));

		String rawsavings = "";
		String s = "";
		for (Document Doc : output2) {
			rawsavings = Doc.toString();
			s = Doc.get("Deck savings").toString();
		}

		System.out.println("RawSavings::" + s);

		return s;

	}

	// ########################PRINT RELATED
	// METHODS#####################################################################

	public static void printResultsCursor() {
		try {
			while (cursor.hasNext()) {
				Document document = cursor.next();
				GenericUtils.logMessage(document.toJson());

				Document something = document.get("value", Document.class);
				String nested = something.getString("AUTHOR");
				GenericUtils.logMessage(nested);
			}
		} finally {
			cursor.close();
		}
	}

	public static void printResultsCursor(String fieldName) {
		try {
			while (cursor.hasNext()) {
				Document document = cursor.next();
				GenericUtils.logMessage(document.toJson());

				Document something = document.get("value", Document.class);
				String nested = something.getString(fieldName);
				GenericUtils.logMessage(nested);
			}
		} finally {
			cursor.close();
		}
	}

	public static void printResultsDocument() {
		for (Document doc : results) {
			GenericUtils.logMessage(doc.toJson());

			Document something = doc.get("value", Document.class);
			String nested = something.getString("NOTE_TYPE");
			GenericUtils.logMessage(nested);

		}
	}

	public static void printResultsDocument(String fieldname) {
		for (Document doc : results) {
			GenericUtils.logMessage(doc.toJson());

			Document something = doc.get("value", Document.class);
			String nested = something.getString(fieldname);
			GenericUtils.logMessage(nested);

		}
	}

	public static void main(String[] args) {
		// connecting to the database
		// getRawsavingsforDPCard();

	}

	public static String Retrieve_the_medicalpolicy_based_on_client_and_release(String clientkey, String release,
			String priordisposition) {

		List<String> topicslist = null;
		Bson MatchFilter_2 = null;
		AggregateIterable<Document> output2 = null;
		ProjectVariables.DB_Medicalpolicylist.clear();

		HashSet<String> MecdicalpoliciesList = new HashSet<>();
		// Connection method for Mongo DB
		retrieveAllDocuments("cpd", "oppty");
		String DBMedicalpolicy = null;

		Bson MatchFilter = new BsonDocument();
		int i = 0;

		if (priordisposition.isEmpty()) {
			MatchFilter = Filters.and(Filters.eq("_id.clientKey", Long.valueOf(clientkey)),
					Filters.eq("disposition.desc", "No Disposition"),
					Filters.eq("_id.dataVersion", java.util.regex.Pattern.compile(release)),
					Filters.eq("ruleInBaseLine", 0), Filters.ne("annualSavings.lines", 0));
			// Filters.ne("annualSavings.lines", 0)

		} else {
			MatchFilter = Filters.and(Filters.eq("_id.clientKey", Long.valueOf(clientkey)),
					Filters.eq("disposition.desc", "No Disposition"),
					Filters.eq("_id.dataVersion", java.util.regex.Pattern.compile(release)),
					Filters.eq("disposition.priorDisposition", priordisposition), Filters.ne("annualSavings.lines", 0));
		}
		//
		results = mColl.find(MatchFilter);
		recordsCount = mColl.count(MatchFilter);

		System.out.println("Filtered Record Count ===>" + recordsCount);

		Bson matchtext = Aggregates.match(Filters.and(MatchFilter));

		if (priordisposition.isEmpty()) {
			// MatchFilter_2 = Filters.or(Filters.size("Topics", 4));

			output2 = mColl.aggregate(Arrays.asList(matchtext, Aggregates.group("$subRule.hierarchy.medPolicyDesc",
					Accumulators.addToSet("Topics", "$subRule.hierarchy.topicDesc"))));
			// Aggregates.match(MatchFilter_2)
		} else {
			output2 = mColl.aggregate(Arrays.asList(matchtext, Aggregates.group("$subRule.hierarchy.medPolicyDesc",
					Accumulators.addToSet("Topics", "$subRule.hierarchy.topicDesc"))));
		}

		// Aggregates.match(MatchFilter_2)

		for (Document document : output2) {

			String Topics = StringUtils.substringBetween(document.toString(), "Topics=[", "]}}");

			topicslist = Arrays.asList(Topics.split(","));

			DBMedicalpolicy = StringUtils.substringBetween(document.toString(), "_id=", ", Topics");

			if (topicslist.size() >= 4) {

				ProjectVariables.DB_Medicalpolicylist.add(DBMedicalpolicy);
			}

		}

		//////////////

		if (recordsCount == 0) {
			Assert.assertTrue("Record count is 'zero' in the Mongo DB for the given filtering data,client key==>"
					+ clientkey + ",Release ==>" + release + ",Disposition ==>No Disposition", false);
		}

		if (ProjectVariables.DB_Medicalpolicylist.isEmpty()) {
			Assert.assertTrue(
					"Mecial policies is 'null' in the Mongo DB for the given filtering data,client key==>" + clientkey
					+ ",Release ==>" + release + ",Disposition ==>No Disposition,for the topic count > 4",
					false);
		}

		System.out.println(ProjectVariables.DB_Medicalpolicylist);
		System.out.println("Medical Policies size ==>" + ProjectVariables.DB_Medicalpolicylist.size());

		for (String DBmedicalpolicy : ProjectVariables.DB_Medicalpolicylist) {
			return DBmedicalpolicy;
		}

		return null;

	}

	public static void Disposition_Records_in_Mongo_DB_For_the_given_PPS_in_AWB_Page(String clientkey,
			List<String> payerkeyList, String Dataversion, String medicalpolicyname, List<String> topicnameList,
			List<String> dpkeyList, String savingsstatus, String disposition, String requestcriteria) {
		/*if(Serenity.sessionVariableCalled("user").toString()=="nkumar")
		{
			Serenity.setSessionVariable("user").to("natuva");
		}*/
		Long ruleInBaseLine = 0l;
		Bson topicorquery = new BasicDBObject();
		ArrayList<Bson> topicORqueryList = new ArrayList<>();
		Bson dpKeycorquery = new BasicDBObject();
		Bson savingsstatusorquery = new BasicDBObject();
		ArrayList<Bson> dpKeyORqueryList = new ArrayList<>();
		ArrayList<Bson> savingsstatusORqueryList = new ArrayList<>();

		if (disposition.equalsIgnoreCase("No Disposition")) {
			ProjectVariables.DB_Nodisposition_insuranceList.clear();
			ProjectVariables.DB_Nodisposition_claimtypeList.clear();
			ProjectVariables.DB_Nodisposition_subRuleList.clear();
			ProjectVariables.DB_Nodisposition_DpkeyList.clear();

		} else {
			ProjectVariables.DB_insuranceList.clear();
			ProjectVariables.DB_claimtypeList.clear();
			ProjectVariables.DB_subRuleList.clear();
			ProjectVariables.DB_DpkeyList.clear();

		}

		Intialaize_the_insurance_claimtype_queries(payerkeyList);

		for (String topic : topicnameList) {

			topicORqueryList.add(Filters.eq("subRule.hierarchy.topicDesc", topic.trim()));

		}

		topicorquery = Filters.or(topicORqueryList);

		for (String dpkey : dpkeyList) {

			dpKeyORqueryList.add(Filters.eq("subRule.hierarchy.dpKey", Long.valueOf(dpkey.trim())));

		}

		dpKeycorquery = Filters.or(dpKeyORqueryList);

		if (savingsstatus.isEmpty()) {
			savingsstatusORqueryList.add(Filters.eq("ruleInBaseLine", Long.valueOf(0l)));
			savingsstatusORqueryList.add(Filters.eq("ruleInBaseLine", Long.valueOf(-1l)));

			savingsstatusorquery = Filters.or(savingsstatusORqueryList);
		} else {

			if (savingsstatus.equalsIgnoreCase("opportunity")) {
				savingsstatusORqueryList.add(Filters.eq("ruleInBaseLine", Long.valueOf(0l)));
			} else if (savingsstatus.equalsIgnoreCase("production")) {
				savingsstatusORqueryList.add(Filters.eq("ruleInBaseLine", Long.valueOf(-1l)));
			}

			savingsstatusorquery = Filters.or(savingsstatusORqueryList);
		}

		retrieveAllDocuments("cpd", "oppty");

		Bson MatchFilter = new BsonDocument();
		Bson MatchFilter_with_savingsstatus = new BsonDocument();

		switch (requestcriteria) {
		case "AWBGrid":
			if (topicnameList.isEmpty() && dpkeyList.isEmpty()) {
				MatchFilter = Filters.and(Filters.eq("_id.clientKey", Long.valueOf(clientkey)),
						Filters.eq("_id.dataVersion", java.util.regex.Pattern.compile(Dataversion)),
						Filters.eq("disposition.desc", disposition),
						Filters.eq("subRule.hierarchy.medPolicyDesc", medicalpolicyname), ProjectVariables.Payerorquery,
						ProjectVariables.Insuranceorquery, ProjectVariables.Claimtypeorquery, savingsstatusorquery);
			} else {
				if (dpkeyList.isEmpty()) {
					MatchFilter = Filters.and(Filters.eq("_id.clientKey", Long.valueOf(clientkey)),
							Filters.eq("_id.dataVersion", java.util.regex.Pattern.compile(Dataversion)),
							Filters.eq("disposition.desc", disposition), topicorquery,
							Filters.eq("subRule.hierarchy.medPolicyDesc", medicalpolicyname), savingsstatusorquery,
							ProjectVariables.Payerorquery, ProjectVariables.Insuranceorquery,
							ProjectVariables.Claimtypeorquery);
				} else {
					MatchFilter = Filters.and(Filters.eq("_id.clientKey", Long.valueOf(clientkey)),
							Filters.eq("_id.dataVersion", java.util.regex.Pattern.compile(Dataversion)),
							Filters.eq("disposition.desc", disposition), dpKeycorquery,
							Filters.eq("subRule.hierarchy.medPolicyDesc", medicalpolicyname),
							ProjectVariables.Payerorquery, ProjectVariables.Insuranceorquery,
							ProjectVariables.Claimtypeorquery, savingsstatusorquery);
				}
			}

			break;

		case "ReviewGrid":
			if (topicnameList.isEmpty() && dpkeyList.isEmpty()) {
				MatchFilter = Filters.and(Filters.eq("_id.clientKey", Long.valueOf(clientkey)),
						Filters.eq("_id.dataVersion", java.util.regex.Pattern.compile(Dataversion)),
						Filters.eq("disposition.desc", disposition),
						Filters.eq("disposition.userId", Serenity.sessionVariableCalled("user")),
						Filters.eq("disposition.notes", ProjectVariables.DispositionNotes),
						Filters.eq("subRule.hierarchy.medPolicyDesc", medicalpolicyname), ProjectVariables.Payerorquery,
						ProjectVariables.Insuranceorquery, ProjectVariables.Claimtypeorquery, savingsstatusorquery);
			} else {
				if (dpkeyList.isEmpty()) {
					MatchFilter = Filters.and(Filters.eq("_id.clientKey", Long.valueOf(clientkey)),
							Filters.eq("_id.dataVersion", java.util.regex.Pattern.compile(Dataversion)),
							Filters.eq("disposition.desc", disposition),
							Filters.eq("disposition.userId", Serenity.sessionVariableCalled("user")),
							Filters.eq("disposition.notes", ProjectVariables.DispositionNotes), topicorquery,
							Filters.eq("subRule.hierarchy.medPolicyDesc", medicalpolicyname),
							ProjectVariables.Insuranceorquery, ProjectVariables.Claimtypeorquery, savingsstatusorquery);
					// ProjectVariables.Payerorquery

				} else {
					MatchFilter = Filters.and(Filters.eq("_id.clientKey", Long.valueOf(clientkey)),
							Filters.eq("_id.dataVersion", java.util.regex.Pattern.compile(Dataversion)),
							Filters.eq("disposition.desc", disposition),
							Filters.eq("disposition.userId", Serenity.sessionVariableCalled("user")),
							Filters.eq("disposition.notes", ProjectVariables.DispositionNotes), dpKeycorquery,
							Filters.eq("subRule.hierarchy.medPolicyDesc", medicalpolicyname),
							ProjectVariables.Payerorquery, ProjectVariables.Insuranceorquery,
							ProjectVariables.Claimtypeorquery, savingsstatusorquery);
				}

			}

			break;

		default:
			Assert.assertTrue("Given selection was not found ==>" + requestcriteria, false);

			break;

		}

		results = mColl.find(MatchFilter);
		recordsCount = mColl.count(MatchFilter);

		System.out.println("Disposition_Count for the given PPS combination:" + recordsCount);

		if (recordsCount == 0) {
			Assert.assertTrue("Record count was 'zero' in the Mongo DB for the given filtering data,For clientkey ==>"
					+ clientkey + ",Dataversion ==>" + Dataversion + ",DPKey ==>" + dpkeyList
					+ ",Medicalpolicy Desc ==>" + medicalpolicyname + ",TopicDesc ==>" + topicnameList, false);
		}

		for (Document document : results) {
			Document ID_Doccument = document.get("_id", Document.class);
			Document Subrule_Doccument = document.get("subRule", Document.class);
			Document hierarchy_Doccument = Subrule_Doccument.get("hierarchy", Document.class);

			String claimtype = ID_Doccument.getString("claimType");
			String insurance = document.getString("insuranceDesc");
			String subrule = ID_Doccument.getString("subRuleId");
			Long DPkey = hierarchy_Doccument.getLong("dpKey");

			if (disposition.equalsIgnoreCase("No Disposition")) {
				ProjectVariables.DB_Nodisposition_insuranceList.add(insurance);
				ProjectVariables.DB_Nodisposition_claimtypeList.add(claimtype);
				ProjectVariables.DB_Nodisposition_subRuleList.add(subrule);
				ProjectVariables.DB_Nodisposition_DpkeyList.add(DPkey);
			} else {
				ProjectVariables.DB_insuranceList.add(insurance);
				ProjectVariables.DB_claimtypeList.add(claimtype);
				ProjectVariables.DB_subRuleList.add(subrule);
				ProjectVariables.DB_DpkeyList.add(DPkey);
			}

		}

		if (disposition.equalsIgnoreCase("No Disposition")) {
			System.out.println(
					"insuranceList for the given PPS combination:" + ProjectVariables.DB_Nodisposition_insuranceList);
			System.out.println(
					"claimtypeList for the given PPS combination:" + ProjectVariables.DB_Nodisposition_claimtypeList);
			System.out.println(
					"subRuleList for the given PPS combination:" + ProjectVariables.DB_Nodisposition_subRuleList);
			System.out
			.println("DpkeyList for the given PPS combination:" + ProjectVariables.DB_Nodisposition_DpkeyList);
			System.out.println("DpkeyList size for the given PPS combination:"
					+ ProjectVariables.DB_Nodisposition_DpkeyList.size());
			System.out.println("subRuleList size for the given PPS combination:"
					+ ProjectVariables.DB_Nodisposition_subRuleList.size());
		} else {
			System.out.println("insuranceList for the given PPS combination:" + ProjectVariables.DB_insuranceList);
			System.out.println("claimtypeList for the given PPS combination:" + ProjectVariables.DB_claimtypeList);
			System.out.println("subRuleList for the given PPS combination:" + ProjectVariables.DB_subRuleList);
			System.out.println("DpkeyList for the given PPS combination:" + ProjectVariables.DB_DpkeyList);
			System.out.println(
					"subRuleList size for the given PPS combination:" + ProjectVariables.DB_subRuleList.size());
			System.out.println("DpkeyList size for the given PPS combination:" + ProjectVariables.DB_DpkeyList.size());

		}
	}

	private static void Intialaize_the_insurance_claimtype_queries(List<String> payerkeyList) {

		ArrayList<Long> InsuranceKeyList = new ArrayList<>();
		ArrayList<String> ClaimtypeList = new ArrayList<>();
		ArrayList<String> LatestClientDecisionList = new ArrayList<>();
		ArrayList<String> PriorDispositionList = new ArrayList<>();
		ArrayList<String> CurrentDispositionList = new ArrayList<>();

		// Intialaizing the default values in the correponding arraylists
		InsuranceKeyList.add(1l);
		InsuranceKeyList.add(2l);
		InsuranceKeyList.add(3l);
		InsuranceKeyList.add(7l);
		InsuranceKeyList.add(8l);
		InsuranceKeyList.add(9l);

		ClaimtypeList.add("A");
		ClaimtypeList.add("F");
		ClaimtypeList.add("P");
		ClaimtypeList.add("I");
		ClaimtypeList.add("O");
		ClaimtypeList.add("S");

		LatestClientDecisionList.add("Absence of Decision");
		LatestClientDecisionList.add("Approve Library");
		LatestClientDecisionList.add("Approve with Modification");
		LatestClientDecisionList.add("No Decision");
		LatestClientDecisionList.add("Reject");
		LatestClientDecisionList.add("Suppress");

		CurrentDispositionList.add("Present");
		CurrentDispositionList.add("Do Not Present - CPM Review");
		CurrentDispositionList.add("Do Not Present");
		CurrentDispositionList.add("Not Reviewed");

		PriorDispositionList.add("Present");
		PriorDispositionList.add("Do Not Present - CPM Review");
		PriorDispositionList.add("Do Not Present");
		PriorDispositionList.add("Not Reviewed");

		ArrayList<Bson> payerORqueryList = new ArrayList<>();
		ArrayList<Bson> InsuranceORqueryList = new ArrayList<>();
		ArrayList<Bson> ClaimtypeORqueryList = new ArrayList<>();
		ArrayList<Bson> LatesCDMDecisionORqueryList = new ArrayList<>();
		ArrayList<Bson> CurrentDispositionORqueryList = new ArrayList<>();
		ArrayList<Bson> PriorDispositionORqueryList = new ArrayList<>();

		// Payershort Or filter
		for (String payer : payerkeyList) {

			Filters.eq("payerShort", payer.trim());

			// orquery=Filters.or(Filters.eq("payerShort", payer));
			payerORqueryList.add(Filters.eq("payerShort", payer.trim()));

		}

		ProjectVariables.Payerorquery = Filters.or(payerORqueryList);

		// Insurance Or filter
		for (Long INSurance : InsuranceKeyList) {

			Filters.eq("_id.insuranceKey", INSurance);

			// orquery=Filters.or(Filters.eq("payerShort", payer));
			InsuranceORqueryList.add(Filters.eq("_id.insuranceKey", INSurance));

		}

		ProjectVariables.Insuranceorquery = Filters.or(InsuranceORqueryList);

		// Claimtype Or filter
		for (String claimtype : ClaimtypeList) {

			Filters.eq("_id.claimType", claimtype);

			// orquery=Filters.or(Filters.eq("payerShort", payer));
			ClaimtypeORqueryList.add(Filters.eq("_id.claimType", claimtype));

		}

		ProjectVariables.Claimtypeorquery = Filters.or(ClaimtypeORqueryList);

		// LatestClientDecisoin Or filter
		for (String latestCDMDecision : LatestClientDecisionList) {

			Filters.eq("latestClientDecision.cdmDecision", latestCDMDecision);

			// orquery=Filters.or(Filters.eq("payerShort", payer));
			LatesCDMDecisionORqueryList.add(Filters.eq("latestClientDecision.cdmDecision", latestCDMDecision));

		}

		ProjectVariables.LatestCLientDecisionOrquery = Filters.or(LatesCDMDecisionORqueryList);

		// CurrentDisposition Or filter
		for (String currentdisposition : CurrentDispositionList) {

			Filters.eq("disposition.desc", currentdisposition);

			// orquery=Filters.or(Filters.eq("payerShort", payer));
			CurrentDispositionORqueryList.add(Filters.eq("disposition.desc", currentdisposition));

		}

		ProjectVariables.CurrentDispositionOrquery = Filters.or(CurrentDispositionORqueryList);

		// PriorDisposition Or filter
		for (String priordisposition : PriorDispositionList) {

			PriorDispositionORqueryList.add(Filters.eq("disposition.priorDisposition", priordisposition));

		}

		ProjectVariables.PriorDispositionOrquery = Filters.or(PriorDispositionORqueryList);

	}

	// ===============================Get Distinct payershort
	// details===================================>
	public static void Get_the_distinct_values_based_on_given(Bson MatchFilter, String sFeildName) {

		ProjectVariables.sGetDBList.clear();
		retrieveAllDocuments("cpd", "oppty");
		// Bson MatchFilter = new BsonDocument();
		int i = 0;
		MatchFilter = MatchFilter;
		Distinctresults = mColl.distinct(sFeildName, MatchFilter, String.class);
		for (String Payershort : Distinctresults) {
			i = i + 1;
			ProjectVariables.sGetDBList.add(Payershort);
			System.out.println(Payershort);
		}

		System.out.println("DB Results size ==>" + ProjectVariables.sGetDBList.size());
		System.out.println("DB Output ==>" + ProjectVariables.sGetDBList);
	}

	// ===============================Get Groupby DPKey
	// details===================================>
	public static void GetGroupByDPKeyValues(Bson MatchFilter) {

		String Medicalpolicy = null;
		String DPKey = null;
		String Topic = null;
		String Insurances = null;
		String ClientName = null;
		String Release = null;

		retrieveAllDocuments("cpd", "oppty");
		// Bson MatchFilter = new BsonDocument();
		int i = 0;

		results = mColl.find(MatchFilter);
		recordsCount = mColl.count(MatchFilter);

		System.out.println("Filtered Record Count ===>" + recordsCount);
		Bson matchtext = Aggregates.match(Filters.and(MatchFilter));

		AggregateIterable<Document> output2 = mColl.aggregate(Arrays.asList(matchtext,
				Aggregates.group("$subRule.hierarchy.dpKey",
						Accumulators.addToSet("Medicalpolicy", "$subRule.hierarchy.medPolicyDesc"),
						Accumulators.addToSet("Topic", "$subRule.hierarchy.topicDesc"),
						Accumulators.addToSet("Insurance", "$insuranceDesc"),
						Accumulators.addToSet("clientname", "$clientDesc"),
						Accumulators.addToSet("Release", "$_id.dataVersion"))));

		for (Document Doc : output2) {

			System.out.println(Doc);

			Medicalpolicy = StringUtils.substringBetween(String.valueOf(Doc), "Medicalpolicy=[", "], Topic");

			DPKey = StringUtils.substringBetween(String.valueOf(Doc), "id=", ", Medicalpolicy");

			Topic = StringUtils.substringBetween(String.valueOf(Doc), "Topic=[", "], Insurance");
			Insurances = StringUtils.substringBetween(String.valueOf(Doc), "Insurance=[", "], clientname");
			ClientName = StringUtils.substringBetween(String.valueOf(Doc), "clientname=[", "], Release");
			Release = StringUtils.substringBetween(String.valueOf(Doc), "Release=[PMPRD1_", "]}}");

			Serenity.setSessionVariable("Client").to(ClientName);
			Serenity.setSessionVariable("release").to(Release.substring(0, 6));
			Serenity.setSessionVariable("Medicalpolicy").to(Medicalpolicy);
			Serenity.setSessionVariable("Medicalpolicy").to(Medicalpolicy);
			Serenity.setSessionVariable("DPKey").to(DPKey);
			Serenity.setSessionVariable("Topic").to(Topic);
			Serenity.setSessionVariable("Insurances").to(Insurances);

			System.out.println("Client ==>" + ClientName);
			System.out.println("release ==>" + Release.substring(0, 6));
			System.out.println("Medicalpolicy ==>" + Medicalpolicy);
			System.out.println("DPKey ==>" + DPKey);
			System.out.println("Topic ==>" + Topic);
			System.out.println("Insurances ==>" + Insurances);

			break;

		}

	}

	// ==================================GETDBValues==================================================================>
	public static void GetDBValuesBasedonAggregation(Bson MatchFilter, String sInputFormat) {
		String rawsavings = null;
		String Medicalpolicy = null;
		String DPKey = null;
		String Topic = null;
		String Insurances = null;
		String ClientName = null;
		String Release = null;
		List<String> DPkeylist = null;
		List<String> Topicslist = null;
		AggregateIterable<Document> output = null;
		ProjectVariables.CapturedDPkeyList.clear();
		
		Medicalpolicy=Serenity.sessionVariableCalled("Medicalpolicy");
		Topic=Serenity.sessionVariableCalled("Topic");

		// To Connect to MongoDb
		retrieveAllDocuments("cpd", "oppty");
		results = mColl.find(MatchFilter);
		recordsCount = mColl.count(MatchFilter);

		System.out.println("Filtered Record Count ===>" + recordsCount);

		if (recordsCount == 0) {
			Assert.assertTrue("Record count was zero for the given match filter ==>" + MatchFilter, false);
		}

		Bson matchtext = Aggregates.match(Filters.and(MatchFilter));

		switch (sInputFormat.toUpperCase()) {

		case "MULTPLE_DP":
			output = mColl.aggregate(Arrays.asList(matchtext,
					Aggregates.group("$subRule.hierarchy.topicDesc",
							Accumulators.addToSet("Medicalpolicy", "$subRule.hierarchy.medPolicyDesc"),
							Accumulators.addToSet("DP", "$subRule.hierarchy.dpKey"))));
			break;
		case "CDM_DECISION_DATA":
			output = mColl.aggregate(Arrays.asList(matchtext,
					Aggregates.group("$subRule.hierarchy.dpKey",
							Accumulators.addToSet("Medicalpolicy", "$subRule.hierarchy.medPolicyDesc"),
							Accumulators.addToSet("Topic", "$subRule.hierarchy.topicDesc"),
							Accumulators.addToSet("Insurance", "$insuranceDesc"),
							Accumulators.addToSet("clientname", "$clientDesc"),
							Accumulators.addToSet("Release", "$_id.dataVersion"))));
			break;
		case "NOT STARTED ONLY WITH FILTERS":
		case "COMPLETED ONLY WITH FILTERS":
		case "PARTIALLY ASSIGNED ONLY WITH FILTERS":
		case "COMPLETED ONLY":
		case "PARTIALLY ASSIGNED ONLY":
		case "NOT STARTED ONLY":
		case "DP COUNT BASED ON TOPIC WITH FILTERS":
		case "DP COUNT BASED ON CLIENT WITH FILTERS":
		case "DPKEYS BASED ON MP":
		case "DPKEYS BASED ON TOPIC":
		case "DP COUNT BASED ON TOPIC":
		case "DP COUNT BASED ON CLIENT":
			output = mColl.aggregate(Arrays.asList(matchtext,
					Aggregates.group("$clientDesc", Accumulators.addToSet("DPKey", "$subRule.hierarchy.dpKey"))));

			break;
		case "DP COUNT BASED ON MP WITH FILTERS":
		case "DP COUNT BASED ON MP":
			output = mColl.aggregate(Arrays.asList(matchtext,
					Aggregates.group("$clientDesc", Accumulators.addToSet("DPKey", "$subRule.hierarchy.dpKey"),
							Accumulators.addToSet("Topics", "$subRule.hierarchy.topicDesc"))));
			break;
		case "RAW SAVINGS BASED ON DP":
		case "RAW SAVINGS BASED ON TOPIC":		
		case "RAW SAVINGS BASED ON MEDICALPOLICY":
		case "RAWSAVINGS FOR DP WITH INSURANCE":
		case "RAWSAVINGS FOR DP":
		case "ANNUAL RAW SAVINGS BASED ON CLIENT AND PAYERSHORT":
			output = mColl.aggregate(Arrays.asList(matchtext,
					Aggregates.group("$clientDesc", Accumulators.sum("Deck savings", "$annualSavings.raw"))));
			break;

		default:
			Assert.assertTrue("Case not found", false);
			break;

		}
		for (Document Doc : output) {

			switch (sInputFormat.toUpperCase()) {

			case "MULTPLE_DP":
				Medicalpolicy = StringUtils.substringBetween(String.valueOf(Doc), "Medicalpolicy=[", "], DP");
				DPKey = StringUtils.substringBetween(String.valueOf(Doc), "DP=[", "]}}");
				Topic = StringUtils.substringBetween(String.valueOf(Doc), "id=", ", Medicalpolicy");
				if (DPKey.contains(",")) {
					Serenity.setSessionVariable("Medicalpolicy").to(Medicalpolicy);
					Serenity.setSessionVariable("DPKey").to(DPKey);
					Serenity.setSessionVariable("Topic").to(Topic);
					break;
				}
				break;
			case "CDM_DECISION_DATA":

				Medicalpolicy = StringUtils.substringBetween(String.valueOf(Doc), "Medicalpolicy=[", "], Topic");
				DPKey = StringUtils.substringBetween(String.valueOf(Doc), "id=", ", Medicalpolicy");
				Topic = StringUtils.substringBetween(String.valueOf(Doc), "Topic=[", "], Insurance");
				Insurances = StringUtils.substringBetween(String.valueOf(Doc), "Insurance=[", "], clientname");
				ClientName = StringUtils.substringBetween(String.valueOf(Doc), "clientname=[", "], Release");
				Release = StringUtils.substringBetween(String.valueOf(Doc), "Release=[PMPRD1_", "]}}");
				Serenity.setSessionVariable("Client").to(ClientName);
				Serenity.setSessionVariable("release").to(Release.substring(0, 6));
				Serenity.setSessionVariable("DPkey").to(DPKey);

				break;

			case "DP COUNT BASED ON TOPIC WITH FILTERS":
			case "DP COUNT BASED ON CLIENT WITH FILTERS":
			case "DP COUNT BASED ON TOPIC":
			case "DP COUNT BASED ON CLIENT":
			case "DPKEYS BASED ON MP":
			case "DPKEYS BASED ON TOPIC":
				System.out.println(Doc);
				DPKey = StringUtils.substringBetween(String.valueOf(Doc), "DPKey=[", "]}}");
				DPkeylist = Arrays.asList(DPKey.split(","));
				Serenity.setSessionVariable("DPkeysize").to(String.valueOf(DPkeylist.size()));
				System.out.println("DPkeyssize ==>" + DPkeylist.size());
				ProjectVariables.CapturedDPkeyList.addAll(DPkeylist);
				break;
			case "DP COUNT BASED ON MP WITH FILTERS":
			case "DP COUNT BASED ON MP":
				DPKey = StringUtils.substringBetween(String.valueOf(Doc), "DPKey=[", "], Topics");
				Topic = StringUtils.substringBetween(String.valueOf(Doc), "Topics=[", "]}}");
				DPkeylist = Arrays.asList(DPKey.split(","));
				Topicslist = Arrays.asList(Topic.split(","));
				Serenity.setSessionVariable("DPkeysize").to(String.valueOf(DPkeylist.size()));
				Serenity.setSessionVariable("Topicsize").to(String.valueOf(Topicslist.size()));
				System.out.println("Topicssize ==>" + Topicslist.size());
				System.out.println("DPkeyssize ==>" + DPkeylist.size());
				break;
			case "RAW SAVINGS BASED ON DP":	
			case "RAW SAVINGS BASED ON TOPIC":		
			case "RAW SAVINGS BASED ON MEDICALPOLICY":
			case "RAWSAVINGS FOR DP WITH INSURANCE":
			case "RAWSAVINGS FOR DP":
			case "ANNUAL RAW SAVINGS BASED ON CLIENT AND PAYERSHORT":
				// String rawsavings = Doc.toString();
				rawsavings = Doc.get("Deck savings").toString();
				//Medicalpolicy = Serenity.sessionVariableCalled("Medicalpolicy").toString();
				//Topic = Serenity.sessionVariableCalled("Topic").toString();

				System.out.println("RawSavings::" + rawsavings);
				break;

			case "COMPLETED ONLY":
			case "NOT STARTED ONLY":
			case "PARTIALLY ASSIGNED ONLY":
				DPKey = StringUtils.substringBetween(String.valueOf(Doc), "DPKey=[", "]}}");
				DPkeylist = Arrays.asList(DPKey.split(","));
				Serenity.setSessionVariable("DPkeysize").to(String.valueOf(DPkeylist.size()));
				System.out.println("DPkeyssize ==>" + DPkeylist.size());
				for (int i = 0; i < DPkeylist.size(); i++) {
					if (sInputFormat.toUpperCase().contains("NOT STARTED")) {
						MatchFilter = Filters.and(
								Filters.eq("_id.clientKey", Long.valueOf(Serenity.sessionVariableCalled("clientkey"))),
								Filters.eq("ruleInBaseLine", 0), Filters.eq("disposition.desc", "Present"),
								Filters.ne("presentationProfile.profileName", null),
								Filters.eq("subRule.hierarchy.dpKey", Long.valueOf(DPkeylist.get(i).trim())));

					} else if (sInputFormat.toUpperCase().contains("PARTIALLY ASSIGNED")
							|| sInputFormat.toUpperCase().contains("COMPLETED")) {
						MatchFilter = Filters.and(
								Filters.eq("_id.clientKey", Long.valueOf(Serenity.sessionVariableCalled("clientkey"))),
								Filters.eq("ruleInBaseLine", 0), Filters.eq("disposition.desc", "Present"),
								Filters.eq("presentationProfile.profileName", null),
								Filters.eq("subRule.hierarchy.dpKey", Long.valueOf(DPkeylist.get(i).trim())));

					}

					recordsCount = mColl.count(MatchFilter);

					if (sInputFormat.toUpperCase().contains("NOT STARTED")
							|| sInputFormat.toUpperCase().contains("COMPLETED")) {
						if (recordsCount == 0) {
							ProjectVariables.CapturedDPkeyList.add(DPkeylist.get(i).trim());
						}
					} else {
						if (recordsCount > 0) {
							ProjectVariables.CapturedDPkeyList.add(DPkeylist.get(i).trim());
						}
					}

				}
				System.out.println("" + sInputFormat + " DPs=>" + ProjectVariables.CapturedDPkeyList);
				System.out.println("" + sInputFormat + " DPs Size=>" + ProjectVariables.CapturedDPkeyList.size());
				break;
			case "COMPLETED ONLY WITH FILTERS":
			case "NOT STARTED ONLY WITH FILTERS":
			case "PARTIALLY ASSIGNED ONLY WITH FILTERS":

				DPKey = StringUtils.substringBetween(String.valueOf(Doc), "DPKey=[", "]}}");
				DPkeylist = Arrays.asList(DPKey.split(","));
				Serenity.setSessionVariable("DPkeysize").to(String.valueOf(DPkeylist.size()));
				System.out.println("DPkeyssize ==>" + DPkeylist.size());
				for (int i = 0; i < DPkeylist.size(); i++) {
					if (sInputFormat.toUpperCase().contains("NOT STARTED")) {
						MatchFilter = Filters.and(
								Filters.eq("_id.clientKey", Long.valueOf(Serenity.sessionVariableCalled("clientkey"))),
								Filters.eq("ruleInBaseLine", 0), Filters.eq("disposition.desc", "Present"),
								Filters.ne("presentationProfile.profileName", null),
								Filters.eq("subRule.hierarchy.dpKey", Long.valueOf(DPkeylist.get(i).trim())),
								Filters.in("payerShort", ProjectVariables.CapturedPayershortList),
								Filters.in("insuranceDesc", ProjectVariables.CapturedInsuranceList),
								Filters.in("_id.claimType", ProjectVariables.CapturedClaimtypesList));

					} else if (sInputFormat.toUpperCase().contains("PARTIALLY ASSIGNED")
							|| sInputFormat.toUpperCase().contains("COMPLETED")) {
						MatchFilter = Filters.and(
								Filters.eq("_id.clientKey", Long.valueOf(Serenity.sessionVariableCalled("clientkey"))),
								Filters.eq("ruleInBaseLine", 0), Filters.eq("disposition.desc", "Present"),
								Filters.eq("presentationProfile.profileName", null),
								Filters.eq("subRule.hierarchy.dpKey", Long.valueOf(DPkeylist.get(i).trim())),
								Filters.in("payerShort", ProjectVariables.CapturedPayershortList),
								Filters.in("insuranceDesc", ProjectVariables.CapturedInsuranceList),
								Filters.in("_id.claimType", ProjectVariables.CapturedClaimtypesList));

					}

					recordsCount = mColl.count(MatchFilter);

					if (sInputFormat.toUpperCase().contains("NOT STARTED")
							|| sInputFormat.toUpperCase().contains("COMPLETED")) {
						if (recordsCount == 0) {
							ProjectVariables.CapturedDPkeyList.add(DPkeylist.get(i).trim());
						}
					} else {
						if (recordsCount > 0) {
							ProjectVariables.CapturedDPkeyList.add(DPkeylist.get(i).trim());
						}
					}

				}
				System.out.println("" + sInputFormat + " DPs=>" + ProjectVariables.CapturedDPkeyList);
				System.out.println("" + sInputFormat + " DPs Size=>" + ProjectVariables.CapturedDPkeyList.size());
				break;

			default:
				Assert.assertTrue("Case not found", false);
				break;
			}

			break;
		}

		Serenity.setSessionVariable("RawSavings").to(rawsavings);
		Serenity.setSessionVariable("Medicalpolicy").to(Medicalpolicy);
		Serenity.setSessionVariable("DPKey").to(DPKey);
		Serenity.setSessionVariable("Topic").to(Topic);
		Serenity.setSessionVariable("Insurances").to(Insurances);
		System.out.println("Medicalpolicy ==>" + Medicalpolicy);
		System.out.println("DPKey ==>" + DPKey);
		System.out.println("Topic ==>" + Topic);
		System.out.println("RawSavings ==>" + rawsavings);
	}

	// ====================================================================================================>
	public static void Get_the_distinct_values_based_on_given(String sClientName, String sFeildName) {
		ProjectVariables.sGetDBList.clear();
		retrieveAllDocuments("cpd", "oppty");
		Bson MatchFilter = new BsonDocument();
		int i = 0;
		MatchFilter = Filters.and(Filters.eq("_id.clientKey", Long.valueOf(Serenity.sessionVariableCalled("clientkey"))), Filters.ne("_id.dataVersion", ""));

		Distinctresults = mColl.distinct(sFeildName, MatchFilter, String.class);

		for (String Payershort : Distinctresults) {
			i = i + 1;
			ProjectVariables.sGetDBList.add(Payershort);
			System.out.println(Payershort);

		}

		System.out.println("DB Results size ==>" + ProjectVariables.sGetDBList.size());
		System.out.println("DB Output ==>" + ProjectVariables.sGetDBList);
	}

	// ====================================================================================================>
	public static int GettheCapturedDispositionPayerLOBsFromtheGiven(String medicalpolicyname, String disposition,
			String dpkey) {
		ProjectVariables.CapturedPayershortList.clear();
		ProjectVariables.CapturedInsuranceList.clear();
		String Insurance = null;
		String Payershort = null;
		String PayerKey = null;
		String claimtype=null;
		List<String> payers = null;

		// Method to connect mongoDB
		retrieveAllDocuments("cpd", "oppty");

		Bson MatchFilter = new BsonDocument();

		// Filter to form a match query based on inputs
		MatchFilter = Filters.and(
				Filters.eq("_id.clientKey", Long.valueOf(Serenity.sessionVariableCalled("clientkey"))),
				// Filters.eq("_id.dataVersion",
				// java.util.regex.Pattern.compile(Serenity.sessionVariableCalled("release"))),
				Filters.eq("disposition.desc", disposition),
				Filters.eq("disposition.userId", Serenity.sessionVariableCalled("user")),
				Filters.eq("presentationProfile", null), Filters.eq("subRule.hierarchy.dpKey", Long.valueOf(dpkey)),
				Filters.eq("subRule.hierarchy.medPolicyDesc", medicalpolicyname),
				// ProjectVariables.Payerorquery,
				// ProjectVariables.Insuranceorquery,ProjectVariables.Claimtypeorquery,
				Filters.eq("ruleInBaseLine", 0));

		recordsCount = mColl.count(MatchFilter);
		System.out.println("Filtered_Count:" + recordsCount);

		if (recordsCount == 0) {
			Assert.assertTrue("Record count was zero for the given Dpkey==>" + dpkey + ",Medicalpolicy==>"
					+ medicalpolicyname + ",Matchfilter===>" + MatchFilter, false);
		}

		Bson matchtext = Aggregates.match(Filters.and(MatchFilter));

		// Aggregate filter to retrieve the output
		AggregateIterable<Document> output = mColl.aggregate(Arrays.asList(matchtext,
				Aggregates.group(new Document().append("insurance", "$insuranceDesc").append("payershort",
						"$_id.payerShort").append("claimtype", "$_id.claimType")
						, Accumulators.addToSet("payerKey", "$_id.payerKey"),
						Accumulators.addToSet("payershort", "$_id.payerShort"),
						Accumulators.addToSet("claimtype", "$_id.claimType"))));
		
		int i = 0;

		// Loop to retrieve the output
		for (Document document : output) {

			// System.out.println(document);
			Insurance = StringUtils.substringBetween(document.toString(), "insurance=", ", payershort");
			PayerKey = StringUtils.substringBetween(document.toString(), "payerKey=[", "], payershort");
			Payershort = StringUtils.substringBetween(document.toString(), "payershort=[", "], claimtype");
			claimtype= StringUtils.substringBetween(document.toString(), "claimtype=[", "]}}");
			System.out.println(Payershort+"-"+Insurance+"-"+claimtype+"-"+PayerKey);
			payers = Arrays.asList(Payershort.split(","));
			ProjectVariables.CapturedInsuranceList.add(Payershort+"-"+Insurance+"-"+claimtype+"-"+PayerKey);
			ProjectVariables.CapturedPayershortList.addAll(payers);
			ProjectVariables.CapturedPayerLOBList.add(Insurance+"-" +Payershort);
			i = payers.size() + i;

		}
		System.out.println(ProjectVariables.CapturedInsuranceList);
		System.out.println(ProjectVariables.CapturedInsuranceList.size());
		System.out.println(ProjectVariables.CapturedInsuranceList.get(0));
		System.out.println(ProjectVariables.CapturedPayerLOBList);
		return i;

	}

	// ====================================================================================================>

	public static void DPKeysWithNoDispositionBasedOnClient(String Client, String DPKeyCriteria) {
		GetAvailableDPKeyfromCPW(Client, DPKeyCriteria, 0);
	}

	// To retrieve the DPkeys with NoDisposition
	public static void GetAvailableDPKeyfromCPW(String Clientkey, String DPKeyCriteria, int NoofInsuranceKeys) {
		boolean bstatus = false;
		String Payerkeys = null;
		String Release = null;
		String Medicalpolicy = null;
		String Topic = null;
		String DPKey = null;
		// String Clientkey=null;
		String InsuranceKeys = null;
		List<String> InsuranceKeysList = null;
		List<String> DPkeysList = null;
		List<String> payerkeysList = null;
		AggregateIterable<Document> output = null;

		// Method to connect mongoDB
		retrieveAllDocuments("cpd", "oppty");

		Bson MatchFilter = new BsonDocument();

		// Filter to form a match query based on inputs
		MatchFilter = Filters.and(Filters.eq("_id.clientKey", Long.valueOf(Clientkey)),
				Filters.eq("disposition.desc", "No Disposition"), Filters.ne("annualSavings.lines", 0),
				Filters.eq("ruleInBaseLine", 0));

		recordsCount = mColl.count(MatchFilter);
		System.out.println("Filtered_Count for the client key '" + Clientkey + "':" + recordsCount);
		System.out.println("Filtered_Count for the client key '" + Clientkey + "':" + recordsCount + ",Time==>"
				+ GenericUtils.getDateGivenFormat("dd/MM/yyyy h:mm:ss a"));

		if (recordsCount == 0) {
			Assert.assertTrue(
					"Record count was zero for the given Clientkey==>" + Clientkey + ",Disposition==>No Disposition",
					false);
		}

		Bson matchtext = Aggregates.match(Filters.and(MatchFilter));

		if (DPKeyCriteria.contains("Single")) {
			// Aggregate filter to retrieve the output
			output = mColl.aggregate(Arrays.asList(matchtext,
					Aggregates.group(
							new Document().append("DPKey", "$subRule.hierarchy.dpKey").append("Release",
									"$_id.dataVersion"),
							Accumulators.addToSet("Payerkeys", "$_id.payerKey"),
							Accumulators.addToSet("Release", "$_id.dataVersion"),
							Accumulators.addToSet("Medicalpolicy", "$subRule.hierarchy.medPolicyDesc"),
							Accumulators.addToSet("Topic", "$subRule.hierarchy.topicDesc"),
							Accumulators.addToSet("Clientkey", "$_id.clientKey"),
							Accumulators.addToSet("InsuranceKeys", "$_id.insuranceKey"))));

		} else if (DPKeyCriteria.equalsIgnoreCase("Multiple")) {
			// Aggregate filter to retrieve the output
			output = mColl.aggregate(Arrays.asList(matchtext,
					Aggregates.group(
							new Document().append("Topic", "$subRule.hierarchy.topicDesc")
							.append("Medicalpolicy", "$subRule.hierarchy.medPolicyDesc").append("Release",
									"$_id.dataVersion"),
							Accumulators.addToSet("Payerkeys", "$_id.payerKey"),
							Accumulators.addToSet("Release", "$_id.dataVersion"),
							Accumulators.addToSet("Medicalpolicy", "$subRule.hierarchy.medPolicyDesc"),
							Accumulators.addToSet("DPkeys", "$subRule.hierarchy.dpKey"),
							Accumulators.addToSet("Clientkey", "$_id.clientKey"),
							Accumulators.addToSet("InsuranceKeys", "$_id.insuranceKey"))));

		} else {
			Assert.assertTrue("Case not found -->" + DPKeyCriteria, false);
		}

		// Loop to retrieve the output
		for (Document document : output) {

			Payerkeys = StringUtils.substringBetween(String.valueOf(document), "Payerkeys=[", "], Release");
			Release = StringUtils.substringBetween(String.valueOf(document), "Release=[", "], Medicalpolicy");
			Medicalpolicy = StringUtils.substringBetween(String.valueOf(document), "Medicalpolicy=[", "], Topic");
			Clientkey = StringUtils.substringBetween(String.valueOf(document), "Clientkey=[", "], InsuranceKeys");
			InsuranceKeys = StringUtils.substringBetween(String.valueOf(document), "InsuranceKeys=[", "]}}");

			payerkeysList = Arrays.asList(Payerkeys.split(","));
			InsuranceKeysList = Arrays.asList(InsuranceKeys.split(","));

			if (payerkeysList.size() > 2 && InsuranceKeysList.size() > NoofInsuranceKeys) {
				if (DPKeyCriteria.contains("Single")) {
					System.out.println(document);
					DPKey = StringUtils.substringBetween(String.valueOf(document), "DPKey=", ", Release");
					if (!DPKey.contains("-")) {
						Topic = StringUtils.substringBetween(String.valueOf(document), "Topic=[", "], Clientkey");
						Medicalpolicy = StringUtils.substringBetween(String.valueOf(document), "Medicalpolicy=[",
								"], Topic");

						Serenity.setSessionVariable("clientkey").to(Clientkey);
						Serenity.setSessionVariable("DPkey").to(DPKey);
						Serenity.setSessionVariable("release").to(Release);
						Serenity.setSessionVariable("Medicalpolicy").to(Medicalpolicy);
						Serenity.setSessionVariable("Topic").to(Topic);
						Serenity.setSessionVariable("Payerkeys").to(Payerkeys);
						Serenity.setSessionVariable("InsuranceKeys").to(InsuranceKeys);
						bstatus = true;
						// System.out.println(document);
						break;
					}

				} else if (DPKeyCriteria.equalsIgnoreCase("Multiple")) {

					DPKey = StringUtils.substringBetween(String.valueOf(document), "DPkeys=[", "], Clientkey");
					if (!DPKey.contains("-")) {
						Topic = StringUtils.substringBetween(String.valueOf(document), "Topic=", ", Medicalpolicy");
						Medicalpolicy = StringUtils.substringBetween(String.valueOf(document), "Medicalpolicy=",
								", Release");

						DPkeysList = Arrays.asList(DPKey.split(","));

						if (DPkeysList.size() > 2) {
							System.out.println(document);
							Serenity.setSessionVariable("clientkey").to(Clientkey);
							Serenity.setSessionVariable("DPkey").to(DPKey);
							Serenity.setSessionVariable("release").to(Release);
							Serenity.setSessionVariable("Medicalpolicy").to(Medicalpolicy);
							Serenity.setSessionVariable("Topic").to(Topic);
							Serenity.setSessionVariable("Payerkeys").to(Payerkeys);
							Serenity.setSessionVariable("InsuranceKeys").to(InsuranceKeys);
							bstatus = true;
							// System.out.println(document);
							break;
						}

					}
				}
			}

		}

		if (Release.contains(",")) {
			Assert.assertTrue("Reteieved Multiple 'Dataversions' instead of single,Dataversion ==>" + Release
					+ ",Clientkey==>" + Serenity.sessionVariableCalled("clientkey") + ",Medicalpolicy==>"
					+ Serenity.sessionVariableCalled("Medicalpolicy") + ",Topic==>"
					+ Serenity.sessionVariableCalled("Topic") + ",DPkey==>" + Serenity.sessionVariableCalled("DPkey"),
					false);
		}
		if (!bstatus) {

			Assert.assertTrue("Unable to find the records from Mongo DB,for the clientkey=>" + Clientkey
					+ ",DPKeyCriteria=>" + DPKeyCriteria
					+ ",Disposition=>No Disposition,PayerKeys count > 2 and Insurancekeys count > " + NoofInsuranceKeys
					+ "", false);

		}

		System.out.println("Clientkey ==>" + Clientkey);
		System.out.println("DPKeys ==>" + DPKey);
		System.out.println("Payerkeys ==>" + Payerkeys);
		System.out.println("Release ==>" + Release);
		System.out.println("Medicalpolicy ==>" + Medicalpolicy);
		System.out.println("Topic ==>" + Topic);
		System.out.println("InsuranceKeys ==>" + InsuranceKeys);

		System.out.println("Output got at the Time==>" + GenericUtils.getDateGivenFormat("dd/MM/yyyy h:mm:ss a"));

	}

	// To retrieve the DPkeys with NoDisposition
	public static void GetAvailableDPKeyfromCPW(String Client, String DPKeyCriteria, int NoofInsuranceKeys,
			int NoofpayerKeys) {
		boolean bstatus = false;
		String Payerkeys = null;
		String Release = null;
		String Medicalpolicy = null;
		String Topic = null;
		String DPKey = null;
		String Clientkey = null;
		String InsuranceKeys = null;
		List<String> InsuranceKeysList = null;
		List<String> DPkeysList = null;
		List<String> payerkeysList = null;
		AggregateIterable<Document> output = null;

		// Method to connect mongoDB
		retrieveAllDocuments("cpd", "oppty");

		Bson MatchFilter = new BsonDocument();

		// Filter to form a match query based on inputs
		MatchFilter = Filters.and(Filters.eq("_id.clientKey", Long.valueOf(Clientkey)),
				Filters.eq("disposition.desc", "No Disposition"), Filters.ne("annualSavings.lines", 0),
				Filters.eq("ruleInBaseLine", 0));

		recordsCount = mColl.count(MatchFilter);
		System.out.println("Filtered_Count for the client '" + Client + "':" + recordsCount);

		if (recordsCount == 0) {
			Assert.assertTrue(
					"Record count was zero for the given Client==>" + Client + ",Disposition==>No Disposition", false);
		}

		Bson matchtext = Aggregates.match(Filters.and(MatchFilter));

		if (DPKeyCriteria.contains("Single")) {
			// Aggregate filter to retrieve the output
			output = mColl.aggregate(Arrays.asList(matchtext,
					Aggregates.group(
							new Document().append("DPKey", "$subRule.hierarchy.dpKey").append("Release",
									"$_id.dataVersion"),
							Accumulators.addToSet("Payerkeys", "$_id.payerKey"),
							Accumulators.addToSet("Release", "$_id.dataVersion"),
							Accumulators.addToSet("Medicalpolicy", "$subRule.hierarchy.medPolicyDesc"),
							Accumulators.addToSet("Topic", "$subRule.hierarchy.topicDesc"),
							Accumulators.addToSet("Clientkey", "$_id.clientKey"),
							Accumulators.addToSet("InsuranceKeys", "$_id.insuranceKey"))));

		} else if (DPKeyCriteria.equalsIgnoreCase("Multiple")) {
			// Aggregate filter to retrieve the output
			output = mColl.aggregate(Arrays.asList(matchtext,
					Aggregates.group(
							new Document().append("Topic", "$subRule.hierarchy.topicDesc")
							.append("Medicalpolicy", "$subRule.hierarchy.medPolicyDesc").append("Release",
									"$_id.dataVersion"),
							Accumulators.addToSet("Payerkeys", "$_id.payerKey"),
							Accumulators.addToSet("Release", "$_id.dataVersion"),
							Accumulators.addToSet("Medicalpolicy", "$subRule.hierarchy.medPolicyDesc"),
							Accumulators.addToSet("DPkeys", "$subRule.hierarchy.dpKey"),
							Accumulators.addToSet("Clientkey", "$_id.clientKey"),
							Accumulators.addToSet("InsuranceKeys", "$_id.insuranceKey"))));

		} else {
			Assert.assertTrue("Case not found -->" + DPKeyCriteria, false);
		}

		// Loop to retrieve the output
		for (Document document : output) {

			Payerkeys = StringUtils.substringBetween(String.valueOf(document), "Payerkeys=[", "], Release");
			Release = StringUtils.substringBetween(String.valueOf(document), "Release=[", "], Medicalpolicy");
			Medicalpolicy = StringUtils.substringBetween(String.valueOf(document), "Medicalpolicy=[", "], Topic");
			Clientkey = StringUtils.substringBetween(String.valueOf(document), "Clientkey=[", "], InsuranceKeys");
			InsuranceKeys = StringUtils.substringBetween(String.valueOf(document), "InsuranceKeys=[", "]}}");

			payerkeysList = Arrays.asList(Payerkeys.split(","));
			InsuranceKeysList = Arrays.asList(InsuranceKeys.split(","));

			if (payerkeysList.size() > NoofpayerKeys && InsuranceKeysList.size() > NoofInsuranceKeys) {
				if (DPKeyCriteria.contains("Single")) {
					System.out.println(document);

					DPKey = StringUtils.substringBetween(String.valueOf(document), "DPKey=", ", Release");
					Topic = StringUtils.substringBetween(String.valueOf(document), "Topic=[", "], Clientkey");
					Medicalpolicy = StringUtils.substringBetween(String.valueOf(document), "Medicalpolicy=[",
							"], Topic");

					Serenity.setSessionVariable("clientkey").to(Clientkey);
					Serenity.setSessionVariable("DPkey").to(DPKey);
					Serenity.setSessionVariable("release").to(Release);
					Serenity.setSessionVariable("Medicalpolicy").to(Medicalpolicy);
					Serenity.setSessionVariable("Topic").to(Topic);
					Serenity.setSessionVariable("Payerkeys").to(Payerkeys);
					Serenity.setSessionVariable("InsuranceKeys").to(InsuranceKeys);
					bstatus = true;
					// System.out.println(document);
					break;
				} else if (DPKeyCriteria.equalsIgnoreCase("Multiple")) {

					DPKey = StringUtils.substringBetween(String.valueOf(document), "DPkeys=[", "], Clientkey");
					Topic = StringUtils.substringBetween(String.valueOf(document), "Topic=", ", Medicalpolicy");
					Medicalpolicy = StringUtils.substringBetween(String.valueOf(document), "Medicalpolicy=",
							", Release");

					DPkeysList = Arrays.asList(DPKey.split(","));

					if (DPkeysList.size() > 2) {
						System.out.println(document);
						Serenity.setSessionVariable("clientkey").to(Clientkey);
						Serenity.setSessionVariable("DPkey").to(DPKey);
						Serenity.setSessionVariable("release").to(Release);
						Serenity.setSessionVariable("Medicalpolicy").to(Medicalpolicy);
						Serenity.setSessionVariable("Topic").to(Topic);
						Serenity.setSessionVariable("Payerkeys").to(Payerkeys);
						Serenity.setSessionVariable("InsuranceKeys").to(InsuranceKeys);
						bstatus = true;
						// System.out.println(document);
						break;
					}

				}
			}

		}

		if (Release.contains(",")) {
			Assert.assertTrue("Reteieved Multiple 'Dataversions' instead of single,Dataversion ==>" + Release
					+ ",Clientkey==>" + Serenity.sessionVariableCalled("clientkey") + ",Medicalpolicy==>"
					+ Serenity.sessionVariableCalled("Medicalpolicy") + ",Topic==>"
					+ Serenity.sessionVariableCalled("Topic") + ",DPkey==>" + Serenity.sessionVariableCalled("DPkey"),
					false);
		}
		if (!bstatus) {
			Assert.assertTrue("Unable to find the records from Mongo DB,for the client=>" + Client + ",DPKeyCriteria=>"
					+ DPKeyCriteria + ",Disposition=>No Disposition,PayerKeys count > " + NoofpayerKeys
					+ " and Insurancekeys count > " + NoofInsuranceKeys + "", false);
		}

		System.out.println("Clientkey ==>" + Clientkey);
		System.out.println("DPKeys ==>" + DPKey);
		System.out.println("Payerkeys ==>" + Payerkeys);
		System.out.println("Release ==>" + Release);
		System.out.println("Medicalpolicy ==>" + Medicalpolicy);
		System.out.println("Topic ==>" + Topic);
		System.out.println("InsuranceKeys ==>" + InsuranceKeys);

	}

	// ==================================================================================================================================================>
	public static void Check_the_captured_record_exists_or_not() {

		/*if(Serenity.sessionVariableCalled("user").toString()=="nkumar")
		{
			Serenity.setSessionVariable("user").to("natuva");
		}*/
		// Method to connect mongoDB
		retrieveAllDocuments("cpd", "oppty");

		Bson MatchFilter = new BsonDocument();

		// Filter to form a match query based on inputs
		MatchFilter = Filters.and(
				Filters.eq("_id.clientKey", Long.valueOf(Serenity.sessionVariableCalled("clientkey"))),
				//Filters.eq("_id.dataVersion", java.util.regex.Pattern.compile(ProjectVariables.DataVersion)),
				Filters.eq("ruleInBaseLine", 0),
				Filters.eq("disposition.desc", Serenity.sessionVariableCalled("Disposition")),
				Filters.eq("subRule.hierarchy.medPolicyDesc", Serenity.sessionVariableCalled("Medicalpolicy")),
				Filters.eq("subRule.hierarchy.topicDesc", Serenity.sessionVariableCalled("Topic")),
				Filters.eq("subRule.hierarchy.dpKey", ProjectVariables.CapturedDPkey),
				Filters.eq("disposition.userId", Serenity.sessionVariableCalled("user")));
		// ,Filters.ne("annualSavings.lines",0)
		recordsCount = mColl.count(MatchFilter);
		System.out.println("Filtered_Count:" + recordsCount);

		if (recordsCount == 0) {
			System.out.println(MatchFilter);
			Assert.assertTrue(
					"Captured record was not available in DB from service,MtchFilter==>"+MatchFilter,false);
		} else {
			System.out.println("Captured record was available in DB from service,Clientkey==>"
					+ Serenity.sessionVariableCalled("clientkey") + ",release==>" + ProjectVariables.DataVersion
					+ ",Medicalpolicy==>" + Serenity.sessionVariableCalled("Medicalpolicy") + ",Topic==>"
					+ Serenity.sessionVariableCalled("Topic") + ",DPkey==>" + ProjectVariables.CapturedDPkey);
		}

	}

	public static ArrayList<String> getEllDatafromMongoDB(ArrayList<Object> sMidRules) {

		ArrayList<String> resultList = new ArrayList<String>();
		String result;
		// Method to connect mongoDB
		retrieveAllDocuments("cpd", "ellData");

		Bson MatchFilter = new BsonDocument();

		// Filter to form a match query based on inputs
		MatchFilter = Filters.in("midRuleKey", sMidRules);
		results = mColl.find(MatchFilter);
		recordsCount = mColl.count(MatchFilter);
		System.out.println("Filtered_Count:" + recordsCount);

		if (recordsCount == 0) {
			System.out.println(MatchFilter);
			Assert.assertTrue("No records displayed", false);
		} else {
			System.out.println("");
		}

		for (Document rs : results) {

			String sMidRule;
			String sTopicKey;
			String sTopicDesc;
			String sCurrentLongTopicDesc;
			String sPrevLongTopicDesc;
			String sMPKey;
			String sMPDesc;
			String sMPSORTORDER;
			String sDPKEY;
			String sDPDESC;
			String sDPSORTORDER;
			String sREFDESC;
			String sPreviousTopicKey;
			String sPrevTopicDesc;
			String sPrevMPKey;
			String sPrevMPDesc;
			String sPrevMPSORTORDER;
			String sPrevDPKEY;
			String sPrevDPDESC;
			String sPrevDPSORTORDER;
			String sPrevREFDESC;

			try {
				sMidRule = rs.get("midRuleKey").toString();
			} catch (Exception e) {
				sMidRule = null;
			}

			try {
				Document Ioccument = rs.get("topicKey", Document.class);
				Object oPrevTopic = Ioccument.get("previous");
				sPreviousTopicKey = String.valueOf(oPrevTopic);
			} catch (Exception e) {
				sPreviousTopicKey = null;
			}

			try {
				Document Ioccument = rs.get("topicKey", Document.class);
				Object sTopic = Ioccument.get("current");
				sTopicKey = String.valueOf(sTopic);
			} catch (Exception e) {
				sTopicKey = null;
			}

			try {
				Document Ioccument = rs.get("topicDesc", Document.class);
				Object oPreviousTopicDesc = Ioccument.get("previous");
				sPrevTopicDesc = String.valueOf(oPreviousTopicDesc);
			} catch (Exception e) {
				sPrevTopicDesc = null;
			}

			try {
				Document Ioccument = rs.get("topicDesc", Document.class);
				Object oTopicDesc = Ioccument.get("current");
				sTopicDesc = String.valueOf(oTopicDesc);
			} catch (Exception e) {
				sTopicDesc = null;
			}

			try {
				Document Ioccument = rs.get("topicLongDesc", Document.class);
				Object oPreviousLongTopicDesc = Ioccument.get("previous");
				sPrevLongTopicDesc = String.valueOf(oPreviousLongTopicDesc);
				sPrevLongTopicDesc = sPrevLongTopicDesc.replaceAll("[\\n\\t ]", "");
			} catch (Exception e) {
				sPrevLongTopicDesc = null;
			}

			try {
				Document Ioccument = rs.get("topicLongDesc", Document.class);
				Object oCurrentLongTopicDesc = Ioccument.get("current");
				sCurrentLongTopicDesc = String.valueOf(oCurrentLongTopicDesc);
				sCurrentLongTopicDesc = sCurrentLongTopicDesc.replaceAll("[\\n\\t ]", "");
			} catch (Exception e) {
				sCurrentLongTopicDesc = null;
			}

			try {
				Document Ioccument = rs.get("medPolicyKey", Document.class);
				Object oPrevMPKey = Ioccument.get("previous");
				sPrevMPKey = String.valueOf(oPrevMPKey);
			} catch (Exception e) {
				sPrevMPKey = null;
			}

			try {
				Document Ioccument = rs.get("medPolicyKey", Document.class);
				Object oMPKey = Ioccument.get("current");
				sMPKey = String.valueOf(oMPKey);
			} catch (Exception e) {
				sMPKey = null;
			}

			try {
				Document Ioccument = rs.get("medPolicyDesc", Document.class);
				Object oPrevMPDesc = Ioccument.get("previous");
				sPrevMPDesc = String.valueOf(oPrevMPDesc);
			} catch (Exception e) {
				sPrevMPDesc = null;
			}

			try {
				Document Ioccument = rs.get("medPolicyDesc", Document.class);
				Object oMPDesc = Ioccument.get("current");
				sMPDesc = String.valueOf(oMPDesc);
			} catch (Exception e) {
				sMPDesc = null;
			}

			try {
				Document Ioccument = rs.get("mpSortOrder", Document.class);
				Object oPrevMPSortOrder = Ioccument.get("previous");
				sPrevMPSORTORDER = String.valueOf(oPrevMPSortOrder);
			} catch (Exception e) {
				sPrevMPSORTORDER = null;
			}

			try {
				Document Ioccument = rs.get("mpSortOrder", Document.class);
				Object oMPSortOrder = Ioccument.get("current");
				sMPSORTORDER = String.valueOf(oMPSortOrder);
			} catch (Exception e) {
				sMPSORTORDER = null;
			}

			try {
				Document Ioccument = rs.get("dpKey", Document.class);
				Object oPrevDPKey = Ioccument.get("previous");
				sPrevDPKEY = String.valueOf(oPrevDPKey);
			} catch (Exception e) {
				sPrevDPKEY = null;
			}

			try {
				Document Ioccument = rs.get("dpKey", Document.class);
				Object oDPKey = Ioccument.get("current");
				sDPKEY = String.valueOf(oDPKey);
			} catch (Exception e) {
				sDPKEY = null;
			}

			try {
				Document Ioccument = rs.get("dpDesc", Document.class);
				Object oPrevDPDesc = Ioccument.get("previous");
				sPrevDPDESC = String.valueOf(oPrevDPDesc);
			} catch (Exception e) {
				sPrevDPDESC = null;
			}

			try {
				Document Ioccument = rs.get("dpDesc", Document.class);
				Object oDPDesc = Ioccument.get("current");
				sDPDESC = String.valueOf(oDPDesc);
			} catch (Exception e) {
				sDPDESC = null;
			}

			try {
				Document Ioccument = rs.get("dpSortOrder", Document.class);
				Object oPrevDPSortOrder = Ioccument.get("previous");
				sPrevDPSORTORDER = String.valueOf(oPrevDPSortOrder);
			} catch (Exception e) {
				sPrevDPSORTORDER = null;
			}

			try {
				Document Ioccument = rs.get("dpSortOrder", Document.class);
				Object oDPSortOrder = Ioccument.get("current");
				sDPSORTORDER = String.valueOf(oDPSortOrder);
			} catch (Exception e) {
				sDPSORTORDER = null;
			}

			try {
				Document Ioccument = rs.get("refSourceDesc", Document.class);
				Object oPrevRefDesc = Ioccument.get("previous");
				sPrevREFDESC = String.valueOf(oPrevRefDesc);
			} catch (Exception e) {
				sPrevREFDESC = null;
			}

			try {
				Document Ioccument = rs.get("refSourceDesc", Document.class);
				Object oRefDesc = Ioccument.get("current");
				sREFDESC = String.valueOf(oRefDesc);
			} catch (Exception e) {
				sREFDESC = null;
			}

			result = "MID RULE:" + sMidRule + ";Previous Topic Key:" + sPreviousTopicKey + ";TOPIC KEY:" + sTopicKey
					+ ";Previous Topic Desc:" + sPrevTopicDesc + ";TOPIC DESC:" + sTopicDesc + ";Previous TP Long Desc:"
					+ sPrevLongTopicDesc + ";TP Long Desc:" + sCurrentLongTopicDesc + ";Previous MP Key:" + sPrevMPKey
					+ ";MP KEY:" + sMPKey + ";Previous MP Desc:" + sPrevMPDesc + ";MP DESC:" + sMPDesc
					+ ";Previous MP Sort Order" + sPrevMPSORTORDER + ";MP SORTORDER:" + sMPSORTORDER
					+ ";Previous DP Key" + sPrevDPKEY + ";DP KEY:" + sDPKEY + ";Previous DP Desc:" + sPrevDPDESC
					+ ";DP DESC:" + sDPDESC + ";Previous DP SortOrder:" + sPrevDPSORTORDER + ";DP SORTORDER:"
					+ sDPSORTORDER + ";Previous Ref Key:" + sPrevREFDESC + ";REF KEY:" + sREFDESC;
			System.out.println("Column data " + result);
			resultList.add(result);

		}

		return resultList;
	}
	
	
	public static ArrayList<String> getDPDatafromMongoDB(ArrayList<Object> sMidRules) {

		ArrayList<String> resultList = new ArrayList<String>();
		String result;
		// Method to connect mongoDB
		retrieveAllDocuments("cpd", "ellHierarchy");

		Bson MatchFilter = new BsonDocument();

		// Filter to form a match query based on inputs
		MatchFilter = Filters.and(Filters.in("subRule.hierarchy.midRuleKey", sMidRules),Filters.eq("_id.releaseLogKey",2019),Filters.eq("subRule.hierarchy.dpType",1),Filters.eq("subRule.hierarchy.cvKey",1));
		results = mColl.find(MatchFilter);
		recordsCount = mColl.count(MatchFilter);
		System.out.println("Filtered_Count:" + recordsCount);

		if (recordsCount == 0) {
			System.out.println(MatchFilter);
			Assert.assertTrue("No records displayed", false);
		} else {
			System.out.println("");
		}

		for (Document rs : results) {

			String sMidRule;
			String sTopicKey;
			String sMPKey;
			String sSubRule;
			String sMPTitle;
			String sTopicTitle;
			String sTOPICDesc;
			String sDPKey;
			String sDPDesc;
			String sSORTORDER;
			
			Document idDocument = rs.get("_id", Document.class);
			Document Ioccument = rs.get("subRule", Document.class);
			Document oHeiracrhy = Ioccument.get("hierarchy",Document.class);
			
			try {
				sMidRule = oHeiracrhy.get("midRuleKey").toString().trim();
			} catch (Exception e) {
				sMidRule = null;
			}


			try {
				Object oMpKey = oHeiracrhy.get("medPolicyKey");
				sMPKey = String.valueOf(oMpKey).trim();
			} catch (Exception e) {
				sMPKey = null;
			}
			
			try {
				Object oMPTitle = oHeiracrhy.get("medPolicyDesc");
				sMPTitle = String.valueOf(oMPTitle).trim();
			} catch (Exception e) {
				sMPTitle = null;
			}
			
			try {				
				Object sTopic = oHeiracrhy.get("topicKey");
				sTopicKey = String.valueOf(sTopic).trim();
			} catch (Exception e) {
				sTopicKey = null;
			}

			try {
				Object sTopic = oHeiracrhy.get("topicDesc");
				sTopicTitle = String.valueOf(sTopic).trim();
			} catch (Exception e) {
				sTopicTitle = null;
			}

			try {
		
				Object oTopicDesc = oHeiracrhy.get("topicDescLong");
				sTOPICDesc = String.valueOf(oTopicDesc).trim();
				sTOPICDesc = sTOPICDesc.replaceAll("[\\\r\\\n]+", "");
				sTOPICDesc = sTOPICDesc.replaceAll("[\\n\\t ]", "");
			} catch (Exception e) {
				sTOPICDesc = null;
			}
		
			try {
				Object oDPKey = oHeiracrhy.get("dpKey");
				sDPKey = String.valueOf(oDPKey).trim();
			} catch (Exception e) {
				sDPKey = null;
			}

		
			try {
				Object oDPKeyDesc = oHeiracrhy.get("dpDesc");
				sDPDesc = String.valueOf(oDPKeyDesc).trim();
				sDPDesc = sDPDesc.replaceAll("[\\n\\t ]", "");
			} catch (Exception e) {
				sDPDesc = null;
			}

			try {
				Object oDPSortOrder = oHeiracrhy.get("dpSortOrder");
				sSORTORDER = String.valueOf(oDPSortOrder).trim();
			} catch (Exception e) {
				sSORTORDER = null;
			}


			try {
				Object oSubRule = idDocument.get("subRuleKey");
				sSubRule = String.valueOf(oSubRule).trim();
			} catch (Exception e) {
				sSubRule = null;
			}


			result = "MID RULE:"+sMidRule+";MP KEY:"+sMPKey+";MP Title:"+sMPTitle+";TOPIC KEY:"+sTopicKey+";Topic Title:"+sTopicTitle+";TOPIC DESC:"+sTOPICDesc+
					 ";DP KEY:"+sDPKey+";DP DESC:"+sDPDesc+";SORTORDER:"+sSORTORDER+
					 ";Sub Rule Key:"+sSubRule;
			//System.out.println("Mongo Column data " + result);
			/*if (sMidRule.equalsIgnoreCase("19703")){
				resultList.add(result);		
				}*/
			
			
			resultList.add(result);		
			

		}

		return resultList;
	}
	
	public static ArrayList<String> getLatestCollectionDataDatafromMongoDB(String sType) throws org.json.simple.parser.ParseException {

		ArrayList<String> resultList = new ArrayList<String>();
		String result = null;
		// Method to connect mongoDB
		retrieveAllDocuments("cdm", "latestDecision");

		Bson MatchFilter = new BsonDocument();
		switch (sType.toUpperCase()){
			
		case "LATEST COLLECTION":
			MatchFilter = Filters.eq("clientKey", 25);
		break;
		case "INFORMATIONAL DP":
			MatchFilter = Filters.and(Filters.eq("_id.dpKey", 5478),Filters.eq("decKey", 1281205));
		break;
		case "CONFIGURATION DP":
			MatchFilter = Filters.and(Filters.eq("_id.dpKey", 4725),Filters.eq("decKey", 1254405));
		break;
	    }
		
		results = mColl.find(MatchFilter);
		recordsCount = mColl.count(MatchFilter);
		System.out.println("Filtered_Count:" + recordsCount);

		if (recordsCount == 0) {
			System.out.println(MatchFilter);
			Assert.assertTrue("No records displayed", false);
		} else {
			System.out.println("");
		}

		for (Document rs : results) {

			String sMidRule = null;
			String sTopicKey = null;
			String sMPKey = null;
			String sSubRule = null;
			String sMPTitle = null;
			String sTopicTitle = null;
			String sTOPICDesc = null;
			String sDPKey = null;
			String sDPDesc =null;
			String sSORTORDER = null;
			String sRuleVersion = null;
			String sSubRuleDesc= null;
			String sLibKey = null ;
			String sReasonCode= null ;
			String sBWReasonCode= null;
			String sRuleHeaderKey = null ;
			String sCoreKey = null;
			String sCoreDesc= null;
			String sRefKey = null;
			String sRefDesc = null;
			String sTitleKey = null;
			String sTitleDesc = null;
			String sRef = null ;
			String sCatKey= null;
			
			Document idDocument = rs.get("_id", Document.class);
			Document oHeiracrhy = rs.get("hierarchy",Document.class);

			try {
				Object oMpKey = oHeiracrhy.get("medPolicyKey");
				sMPKey = String.valueOf(oMpKey).trim();
			} catch (Exception e) {
				sMPKey = null;
			}
			
			try {
				Object oMPTitle = oHeiracrhy.get("medPolicyDesc");
				sMPTitle = String.valueOf(oMPTitle).trim();
			} catch (Exception e) {
				sMPTitle = null;
			}
			
			try {				
				Object sTopic = oHeiracrhy.get("topicKey");
				sTopicKey = String.valueOf(sTopic).trim();
			} catch (Exception e) {
				sTopicKey = null;
			}

			try {
				Object sTopic = oHeiracrhy.get("topicTitle");
				sTopicTitle = String.valueOf(sTopic).trim();
			} catch (Exception e) {
				sTopicTitle = null;
			}

			try {
		
				Object oTopicDesc = oHeiracrhy.get("topicDesc");
				sTOPICDesc = String.valueOf(oTopicDesc).trim();
				sTOPICDesc = sTOPICDesc.replaceAll("[\\\r\\\n]+", "");
				sTOPICDesc = sTOPICDesc.replaceAll("[\\n\\t ]", "");
			} catch (Exception e) {
				sTOPICDesc = null;
			}
		
			try {
				Object oDPKey = oHeiracrhy.get("dpKey");
				sDPKey = String.valueOf(oDPKey).trim();
			} catch (Exception e) {
				sDPKey = null;
			}

		
			try {
				Object oDPKeyDesc = oHeiracrhy.get("dpDesc");
				sDPDesc = String.valueOf(oDPKeyDesc).trim();
				sDPDesc = sDPDesc.replaceAll("[\\n\\t ]", "");
			} catch (Exception e) {
				sDPDesc = null;
			}

			try {
				Object oDPSortOrder = oHeiracrhy.get("dpSortOrder");
				sSORTORDER = String.valueOf(oDPSortOrder).trim();

			} catch (Exception e) {
				sSORTORDER = null;
			}
						
			ArrayList ColumnValue = null;
			try{
			 ColumnValue = (ArrayList) oHeiracrhy.get("rules");
			}catch(Exception e){
							
			}
			
			if (ColumnValue!=null ){
			for (int i = 0; i < ColumnValue.size(); i++) {

				Document oDocRules = (Document) ColumnValue.get(i);
				System.out.println(oDocRules.get("midRuleKey").toString());
				
				try {
					sMidRule = oDocRules.get("midRuleKey").toString().trim();
				} catch (Exception e) {
					sMidRule = null;
				}
				
				try {
					sRuleVersion = oDocRules.get("ruleVersion").toString().trim();
				} catch (Exception e) {
					sRuleVersion = null;
				}
				
				try {
					Object oSubRule = oDocRules.get("subRuleKey");
					sSubRule = String.valueOf(oSubRule).trim();
				} catch (Exception e) {
					sSubRule = null;
				}
				
				try {
					Object oSubRuleDesc = oDocRules.get("desc");
					sSubRuleDesc = String.valueOf(oSubRuleDesc).trim();
				} catch (Exception e) {
					sSubRuleDesc = null;
				}
				
				try {
					Object oLibKey = oDocRules.get("libStatusKey");
					sLibKey = String.valueOf(oLibKey).trim();
				} catch (Exception e) {
					sLibKey = null;
				}
				
				Document oDocReason = (Document) oDocRules.get("reason");
				
				try {					
					Object oCodeDesc = oDocReason.get("code");
					sReasonCode = String.valueOf(oCodeDesc).trim();
				} catch (Exception e) {
					sReasonCode = null;
				}
				
				try {					
					Object obwCode = oDocReason.get("bwCode");
					sBWReasonCode = String.valueOf(obwCode).trim();
				} catch (Exception e) {
					sBWReasonCode = null;
				}
				
				Document oDocheader = (Document) oDocRules.get("header");
				
				try {					
					Object oRuleHeaderKey = oDocheader.get("key");
					sRuleHeaderKey = String.valueOf(oRuleHeaderKey).trim();
				} catch (Exception e) {
					sRuleHeaderKey = null;
				}
				
				try {
					Object oCoreKey = oDocRules.get("coreEnhancedKey");
					sCoreKey = String.valueOf(oCoreKey).trim();
				} catch (Exception e) {
					sCoreKey = null;
					
				}try {
					Object oCoreDesc = oDocRules.get("coreEnhancedDesc");
					sCoreDesc = String.valueOf(oCoreDesc).trim();
				} catch (Exception e) {
					sCoreDesc = null;
				}
				
                Document oDocRef = (Document) oDocRules.get("refSource");
				
				try {					
					Object oRefKey = oDocRef.get("key");
					sRefKey = String.valueOf(oRefKey).trim();
				} catch (Exception e) {
					sRefKey = null;
				}
				
				
				try {					
					Object oRefDesc = oDocRef.get("desc");
					sRefDesc = String.valueOf(oRefDesc).trim();
				} catch (Exception e) {
					sRefDesc = null;
				}
				
			  Document oDocRefTitle = (Document) oDocRules.get("refTitle");
				
				try {					
					Object oRefKey = oDocRefTitle.get("key");
					sTitleKey = String.valueOf(oRefKey).trim();
				} catch (Exception e) {
					sTitleKey = null;
				}
				
				
				try {					
					Object oRefDesc = oDocRefTitle.get("desc");
					sTitleDesc = String.valueOf(oRefDesc).trim();
				} catch (Exception e) {
					sTitleDesc = null;
				}
				
				try {
					Object oRef = oDocRules.get("reference");
					sRef = String.valueOf(oRef).trim();
				} catch (Exception e) {
					sRef = null;
				}
				
				try {
					Object oCatKey = oDocRules.get("genderCatKey");
					sCatKey = String.valueOf(oCatKey).trim();
				} catch (Exception e) {
					sCatKey = null;}
				
				result = "MID RULE:"+sMidRule+";RULE VERSION:"+sRuleVersion+";MP KEY:"+sMPKey+";MP Title:"+sMPTitle+";TOPIC KEY:"+sTopicKey+";Topic Title:"+sTopicTitle+";TOPIC DESC:"+sTOPICDesc+
						 ";DP KEY:"+sDPKey+";DP DESC:"+sDPDesc+";SORTORDER:"+sSORTORDER+
						 ";Sub Rule Key:"+sSubRule+";SUB RULE DESC:"+sSubRuleDesc+";LIB KEY:"+sLibKey+";REASON CODE:"+sReasonCode+";BW REASON CODE:"+sBWReasonCode+";RULE HEADER KEY:"+sRuleHeaderKey+
						 ";CORE KEY:"+sCoreKey+";CORE DESC:"+sCoreDesc+";REF KEY:"+sRefKey+";REF DESC:"+sRefDesc+"TITLE KEY:"+sTitleKey+"TITLE DESC:"+sTitleDesc+
						 ";REFERENCE:"+sRef+";CAT KEY:"+sCatKey;	
			
				resultList.add(result);	
				}
			}else{
				result = "MID RULE:"+sMidRule+";RULE VERSION:"+sRuleVersion+";MP KEY:"+sMPKey+";MP Title:"+sMPTitle+";TOPIC KEY:"+sTopicKey+";Topic Title:"+sTopicTitle+";TOPIC DESC:"+sTOPICDesc+
						 ";DP KEY:"+sDPKey+";DP DESC:"+sDPDesc+";SORTORDER:"+sSORTORDER+
						 ";Sub Rule Key:"+sSubRule+";SUB RULE DESC:"+sSubRuleDesc+";LIB KEY:"+sLibKey+";REASON CODE:"+sReasonCode+";BW REASON CODE:"+sBWReasonCode+";RULE HEADER KEY:"+sRuleHeaderKey+
						 ";CORE KEY:"+sCoreKey+";CORE DESC:"+sCoreDesc+";REF KEY:"+sRefKey+";REF DESC:"+sRefDesc+"TITLE KEY:"+sTitleKey+"TITLE DESC:"+sTitleDesc+
						 ";REFERENCE:"+sRef+";CAT KEY:"+sCatKey;	
				
				resultList.add(result);	
				
				}
			}

		return resultList;
	}

	public static void DPKeysFromSubsequentReleases() {
		boolean bstatus = false;
		String Payerkeys = null;
		String Release = null;
		String Medicalpolicy = null;
		String Topic = null;
		String DPKey = null;
		String Disposition = null;
		String Clientkey = null;
		String Clientname = null;
		String Rule = null;
		List<String> ReleaseList = null;

		List<String> RulesList = null;
		AggregateIterable<Document> output = null;

		// Method to connect mongoDB
		retrieveAllDocuments("cpd", "oppty");

		Bson MatchFilter = new BsonDocument();

		for (int i = 0; i < ProjectVariables.clientKeysList.size(); i++) {

			// Filter to form a match query based on inputs
			MatchFilter = Filters.and(Filters.eq("_id.clientKey", Long.valueOf(ProjectVariables.clientKeysList.get(i))),
					Filters.eq("ruleInBaseLine", 0), Filters.eq("disposition.desc", "No Disposition"));
			// Filters.eq("disposition.desc", "No
			// Disposition"),Filters.ne("_id.dataVersion",
			// ""),Filters.ne("annualSavings.raw",
			// 0),Filters.ne("annualSavings.raw", 0)
			recordsCount = mColl.count(MatchFilter);

			System.out.println("Filtered_Count for the clientkey '" + ProjectVariables.clientKeysList.get(i) + "'==>"
					+ recordsCount);

			Bson matchtext = Aggregates.match(Filters.and(MatchFilter));
			// Aggregate filter to retrieve the output
			output = mColl.aggregate(Arrays.asList(matchtext,
					Aggregates.group("$subRule.hierarchy.dpKey", Accumulators.addToSet("Release", "$_id.dataVersion"),
							Accumulators.addToSet("Medicalpolicy", "$subRule.hierarchy.medPolicyDesc"),
							Accumulators.addToSet("Topic", "$subRule.hierarchy.topicDesc"),
							Accumulators.addToSet("Payerkeys", "$_id.payerKey"),
							Accumulators.addToSet("Clientname", "$clientDesc"),
							Accumulators.addToSet("ClientKey", "$_id.clientKey"),
							Accumulators.addToSet("Rules", "$_id.subRuleId"),
							Accumulators.addToSet("Dispositions", "$disposition.desc"))));

			// Loop to retrieve the output
			for (Document document : output) {
				DPKey = StringUtils.substringBetween(String.valueOf(document), "_id=", ", Release");
				Payerkeys = StringUtils.substringBetween(String.valueOf(document), "Payerkeys=[", "], Clientname");
				Medicalpolicy = StringUtils.substringBetween(String.valueOf(document), "Medicalpolicy=[", "], Topic");
				Topic = StringUtils.substringBetween(String.valueOf(document), "Topic=[", "], Payerkeys");
				Release = StringUtils.substringBetween(String.valueOf(document), "Release=[", "], Medicalpolicy");
				Clientkey = StringUtils.substringBetween(String.valueOf(document), "ClientKey=[", "], Rules");
				Rule = StringUtils.substringBetween(String.valueOf(document), ", Rules=[", "], Dispositions");
				Clientname = StringUtils.substringBetween(String.valueOf(document), ", Clientname=[", "], ClientKey");
				Disposition = StringUtils.substringBetween(String.valueOf(document), ", Dispositions=[", "]}}");
				ReleaseList = Arrays.asList(Release.split(","));
				RulesList = Arrays.asList(Rule.split(","));

				// &&CLientList.size()==1
				if (ReleaseList.size() > 1 && RulesList.size() > 1) {
					System.out.println(document);
					bstatus = true;
					break;
				}

			}

			if (bstatus) {
				break;
			}
		}

		if (!bstatus) {
			Assert.assertTrue(
					"There is no DP in the common releases in the clienkeys ==>" + ProjectVariables.clientKeysList,
					false);
		}

		Serenity.setSessionVariable("Medicalpolicy").to(Medicalpolicy);
		Serenity.setSessionVariable("Topic").to(Topic);
		Serenity.setSessionVariable("clientkey").to(Clientkey);
		Serenity.setSessionVariable("Client").to(Clientname);
		Serenity.setSessionVariable("DPkey").to(DPKey);
		Serenity.setSessionVariable("Payerkeys").to(Payerkeys);
		Serenity.setSessionVariable("release").to(ReleaseList);
		Serenity.setSessionVariable("Disposition").to(Disposition);

		System.out.println("Medicalpolicy ==>" + Serenity.sessionVariableCalled("Medicalpolicy"));
		System.out.println("Topic ==>" + Serenity.sessionVariableCalled("Topic"));
		System.out.println("Clientkey ==>" + Serenity.sessionVariableCalled("clientkey"));
		System.out.println("Client ==>" + Serenity.sessionVariableCalled("Client"));
		System.out.println("Disposition ==>" + Serenity.sessionVariableCalled("Disposition"));
		System.out.println("DPkey ==>" + Serenity.sessionVariableCalled("DPkey"));
		System.out.println("Payerkeys ==>" + Serenity.sessionVariableCalled("Payerkeys"));
		System.out.println("Releases ==>" + Serenity.sessionVariableCalled("release"));

	}

	public static void DPKeysFromSubsequentReleasesWithMultipleGroupings() {
		boolean bstatus = false;
		String Payerkeys = null;
		String Release = null;
		String Medicalpolicy = null;
		String Topic = null;
		String DPKey = null;
		String Clientkey = null;
		List<String> ReleaseList = null;
		List<String> CLientList = null;
		AggregateIterable<Document> output = null;

		// Method to connect mongoDB
		retrieveAllDocuments("cpd", "oppty");

		Bson MatchFilter = new BsonDocument();

		for (int i = 0; i < ProjectVariables.clientKeysList.size(); i++) {

			// Filter to form a match query based on inputs
			MatchFilter = Filters.and(Filters.ne("_id.dataVersion", ""),
					Filters.eq("_id.clientKey", Long.valueOf(ProjectVariables.clientKeysList.get(i))),
					Filters.eq("ruleInBaseLine", 0));
			// Filters.eq("disposition.desc", "No
			// Disposition")Filters.eq("disposition.desc", "No
			// Disposition"),,Filters.ne("annualSavings.raw",
			// 0),Filters.eq("disposition.desc", "No Disposition")
			recordsCount = mColl.count(MatchFilter);
			System.out.println("Filtered_Count:" + recordsCount);

			// if(recordsCount==0)
			{
				// Assert.assertTrue("Record count was zero for the
				// given,Disposition==>No Disposition,CLientkey
				// ==>"+ProjectVariables.clientKeysList.get(i)+",Dataversion is
				// subsequent releases of 3", false);
			}

			Bson matchtext = Aggregates.match(Filters.and(MatchFilter));
			// Aggregate filter to retrieve the output
			output = mColl.aggregate(Arrays.asList(matchtext, Aggregates.group(
					new Document().append("DPKey", "$subRule.hierarchy.dpKey").append("InsuranceDesc", "$insuranceDesc")
					.append("ClaimType", "$_id.claimType").append("Payershort", "$_id.payerShort"),
					Accumulators.addToSet("Release", "$_id.dataVersion"),
					Accumulators.addToSet("Medicalpolicy", "$subRule.hierarchy.medPolicyDesc"),
					Accumulators.addToSet("Topic", "$subRule.hierarchy.topicDesc"),
					Accumulators.addToSet("Payerkeys", "$_id.payerKey"),
					Accumulators.addToSet("Clientname", "$clientDesc"),
					Accumulators.addToSet("ClientKey", "$_id.clientKey"),
					Accumulators.sum("RawSavings", "$annualSavings.raw"),
					Accumulators.addToSet("Disposition", "$disposition.desc"),
					Accumulators.addToSet("Insurance", "$insuranceDesc"),
					Accumulators.addToSet("Claimtype", "$_id.claimType")))).allowDiskUse(true);

			// ,"$insuranceDesc","$_id.claimType","$_id.payerKey"

			// Loop to retrieve the output
			for (Document document : output) {

				Release = StringUtils.substringBetween(String.valueOf(document), "Release=[", "], Medicalpolicy");
				Clientkey = StringUtils.substringBetween(String.valueOf(document), "Clientkey=[", "]}}");
				ReleaseList = Arrays.asList(Release.split(","));
				// &&CLientList.size()==1
				if (ReleaseList.size() > 1) {
					System.out.println(document);
					bstatus = true;
					break;
				}

			}

			if (bstatus) {
				break;
			}
		}

	}

	public static void UpdateTheDispositionForthegivenClient(String Client, String Disposition, String criteria) {

		boolean bstatus = false;
		String Payerkeys = null;
		String Release = null;
		String Medicalpolicy = null;
		String Topic = null;
		String DPKey = null;
		/// String Disposition=null;
		String Clientkey = null;
		String Clientname = null;
		String Rule = null;
		List<String> ReleaseList = null;

		List<String> RulesList = null;
		AggregateIterable<Document> output = null;

		// Method to connect mongoDB
		retrieveAllDocuments("cpd", "oppty");

		Bson MatchFilter = new BsonDocument();

		// for (int i = 0; i < ProjectVariables.clientKeysList.size(); i++) {

		// Filter to form a match query based on inputs
		MatchFilter = Filters.and(Filters.ne("_id.dataVersion", ""),
				Filters.eq("clientDesc", java.util.regex.Pattern.compile(Client)), Filters.eq("ruleInBaseLine", 0),
				Filters.ne("annualSavings.raw", 0), Filters.eq("disposition.desc", Disposition));
		// Filters.eq("disposition.desc", "No Disposition")
		recordsCount = mColl.count(MatchFilter);
		System.out.println("Filtered_Count for the client '" + Client + "'==>" + recordsCount);

		Bson matchtext = Aggregates.match(Filters.and(MatchFilter));
		// Aggregate filter to retrieve the output
		output = mColl.aggregate(Arrays.asList(matchtext, Aggregates.group("$subRule.hierarchy.dpKey",
				Accumulators.addToSet("Release", "$_id.dataVersion"),
				Accumulators.addToSet("Medicalpolicy", "$subRule.hierarchy.medPolicyDesc"),
				Accumulators.addToSet("Topic", "$subRule.hierarchy.topicDesc"),
				Accumulators.addToSet("Payerkeys", "$_id.payerKey"), Accumulators.addToSet("Clientname", "$clientDesc"),
				Accumulators.addToSet("ClientKey", "$_id.clientKey"), Accumulators.addToSet("Rules", "$_id.subRuleId"),
				Accumulators.addToSet("Dispositions", "$disposition.desc"))));

		// Loop to retrieve the output
		for (Document document : output) {
			DPKey = StringUtils.substringBetween(String.valueOf(document), "_id=", ", Release");
			Payerkeys = StringUtils.substringBetween(String.valueOf(document), "Payerkeys=[", "], Clientname");
			Medicalpolicy = StringUtils.substringBetween(String.valueOf(document), "Medicalpolicy=[", "], Topic");
			Topic = StringUtils.substringBetween(String.valueOf(document), "Topic=[", "], Payerkeys");
			Release = StringUtils.substringBetween(String.valueOf(document), "Release=[", "], Medicalpolicy");
			Clientkey = StringUtils.substringBetween(String.valueOf(document), "ClientKey=[", "], Rules");
			Rule = StringUtils.substringBetween(String.valueOf(document), ", Rules=[", "], Dispositions");
			Clientname = StringUtils.substringBetween(String.valueOf(document), ", Clientname=[", "], ClientKey");
			Disposition = StringUtils.substringBetween(String.valueOf(document), ", Dispositions=[", "]}}");
			ReleaseList = Arrays.asList(Release.split(","));
			RulesList = Arrays.asList(Rule.split(","));

			// &&CLientList.size()==1
			if (criteria.equalsIgnoreCase("Single")) {
				// if(ReleaseList.size()>1&&RulesList.size()>1)
				{
					System.out.println(document);
					bstatus = true;
					break;
				}
			} else {
				if (RulesList.size() > 1) {
					System.out.println(document);
					bstatus = true;
					break;
				}
			}

		}

		// }

		if (!bstatus) {
			Assert.assertTrue("There is no DP in the common releases in the client ==>" + Client, false);
		}

		Serenity.setSessionVariable("Medicalpolicy").to(Medicalpolicy);
		Serenity.setSessionVariable("Topic").to(Topic);
		Serenity.setSessionVariable("clientkey").to(Clientkey);
		Serenity.setSessionVariable("Client").to(Clientname);
		Serenity.setSessionVariable("DPkey").to(DPKey);
		Serenity.setSessionVariable("Payerkeys").to(Payerkeys);
		Serenity.setSessionVariable("release").to(ReleaseList);
		Serenity.setSessionVariable("Disposition").to(Disposition);

		System.out.println("Medicalpolicy ==>" + Serenity.sessionVariableCalled("Medicalpolicy"));
		System.out.println("Topic ==>" + Serenity.sessionVariableCalled("Topic"));
		System.out.println("Clientkey ==>" + Serenity.sessionVariableCalled("clientkey"));
		System.out.println("Client ==>" + Serenity.sessionVariableCalled("Client"));
		System.out.println("Disposition ==>" + Serenity.sessionVariableCalled("Disposition"));
		System.out.println("DPkey ==>" + Serenity.sessionVariableCalled("DPkey"));
		System.out.println("Payerkeys ==>" + Serenity.sessionVariableCalled("Payerkeys"));
		System.out.println("Releases ==>" + Serenity.sessionVariableCalled("release"));

	}

	public static ArrayList<Object> getTopicKeysHavingchange() {

		ArrayList<Object> AllTopicKeys = new ArrayList<Object>();
		retrieveAllDocuments("cpd", "ellData");
		Bson MatchFilter = new BsonDocument();

		MatchFilter = Filters.and(Filters.eq("topicLongDesc.change", true),
				Filters.eq("releaseLogKey.current", Long.valueOf("1855")),
				Filters.eq("releaseLogKey.previous", Long.valueOf("1854")));
		DistinctIterable<Long> outputDisp = mColl.distinct("topicKey.current", MatchFilter, Long.class);

		for (Long iTP : outputDisp) {
			AllTopicKeys.add(iTP);
		}

		System.out.println(AllTopicKeys.size());
		return AllTopicKeys;
	}

	public static HashMap<String, String> getChangesforTopicKeysinEllData(ArrayList<Object> sTopicKeys) {
		HashMap<String, String> sMPTopicLongDescData = new HashMap<String, String>();
		ArrayList<String> sTopicLongDescData = new ArrayList<String>();
		String result;
		retrieveAllDocuments("cpd", "ellData");
		Bson MatchFilter = new BsonDocument();

		MatchFilter = Filters.and(Filters.in("topicKey.current", sTopicKeys),
				Filters.eq("releaseLogKey.current", Long.valueOf("1855")),
				Filters.eq("releaseLogKey.previous", Long.valueOf("1854")));

		Bson matchtext = Aggregates.match(Filters.and(MatchFilter));

		AggregateIterable<Document> sValues = mColl.aggregate(Arrays.asList(matchtext,
				Aggregates.group("$_id", Accumulators.addToSet("midrule", "$midRuleKey"),
						Accumulators.addToSet("dpKey", "$dpKey.current"),
						Accumulators.addToSet("topickey", "$topicKey.current"),
						Accumulators.addToSet("topic_prev", "$topicLongDesc.previous"),
						Accumulators.addToSet("TopicCurrent", "$topicLongDesc.current"))));

		recordsCount = mColl.count(MatchFilter);
		System.out.println(recordsCount);

		for (Document ido : sValues) {

			String sTopicKey;
			String sCurrentTPLongDesc;
			String sDPKey;
			Object oDPKey = ido.get("dpKey");
			sDPKey = StringUtils.substringBetween(String.valueOf(oDPKey), "[", "]");
			Object oTopicKey = ido.get("topickey");
			sTopicKey = StringUtils.substringBetween(String.valueOf(oTopicKey), "[", "]");

			try {
				Object oCurrentTPLongDesc = ido.get("TopicCurrent");
				char[] sValue = String.valueOf(oCurrentTPLongDesc).toCharArray();
				sCurrentTPLongDesc = String.valueOf(oCurrentTPLongDesc).substring(1, sValue.length - 1);
				sCurrentTPLongDesc = sCurrentTPLongDesc.replaceAll("[\\n\\t ]", "");
			} catch (Exception e) {
				sCurrentTPLongDesc = null;
			}

			result = "TopicKey:" + sTopicKey + ";DPKey:" + sDPKey + ";Long Desc:" + sCurrentTPLongDesc;
			sMPTopicLongDescData.put(sTopicKey + ";" + sDPKey, sCurrentTPLongDesc);
			sTopicLongDescData.add(result);
		}

		return sMPTopicLongDescData;

	}

	public static HashMap<String, String> getTopicKeysfromPolicyHierarchy(ArrayList<Object> sTopicKeys) {

		HashMap<String, String> sMPTopicLongDescData = new HashMap<String, String>();
		ArrayList<String> sTopicLongDescData = new ArrayList<String>();
		String result;
		retrieveAllDocuments("policyHierarchy", "policyHierarchy");
		Bson MatchFilter = new BsonDocument();

		MatchFilter = Filters.and(Filters.in("_id.topicKey", sTopicKeys),
				Filters.eq("_id.dataVersion", java.util.regex.Pattern.compile("PMPRD1_20190821_215306")));

		results = mColl.find(MatchFilter);

		recordsCount = mColl.count(MatchFilter);
		System.out.println(recordsCount);

		for (Document ido : results) {

			String sTopicKey;
			String sCurrentTPLongDesc;
			String sDPKey;
			Document Ioccument = ido.get("_id", Document.class);
			Object oDPKey = Ioccument.get("dpKey");
			sDPKey = String.valueOf(oDPKey);

			Object oTPKey = Ioccument.get("topicKey");
			sTopicKey = String.valueOf(oTPKey);

			try {
				Object oCurrentTPLongDesc = ido.get("topicDescLong");
				sCurrentTPLongDesc = String.valueOf(oCurrentTPLongDesc);
				sCurrentTPLongDesc = sCurrentTPLongDesc.replaceAll("[\\n\\t ]", "");
			} catch (Exception e) {
				sCurrentTPLongDesc = null;
			}

			result = "TopicKey:" + sTopicKey + ";DPKey:" + sDPKey + ";Long Desc:" + sCurrentTPLongDesc;
			sMPTopicLongDescData.put(sTopicKey + ";" + sDPKey, sCurrentTPLongDesc);
			sTopicLongDescData.add(result);
		}

		return sMPTopicLongDescData;

	}

	public static HashMap<String, Object> verifyStatusinDBforGivenDP(String sDP) {

		HashMap<String, Object> sDPData = new HashMap<String, Object>();
		String sDOSFrom = null;
		String sDOSTO = null;
		String sProcessingFromTs = null;
		String sProcessingToTS = null;
		String sReason = null;
		String sNote = null;
		String sFollowUpOwner = null;
		Long sOpportunityStatus = 0l;

		retrieveAllDocuments("presentations", "decision");
		Bson MatchFilter = new BsonDocument();

		MatchFilter = Filters.and(Filters.eq("_id.dpKey", Long.valueOf(sDP)),
				Filters.eq("_id.clientKey", Long.valueOf(Serenity.sessionVariableCalled("clientkey"))));

		results = mColl.find(MatchFilter);

		for (Document ido : results) {
			// System.out.println(ido);
			String sStatus = ido.get("status").toString();

			switch (sStatus) {
			case "Approve":
			case "Approve with Mod":
				sDOSFrom = ido.get("dosFromTs").toString();
				sDOSFrom = GenericUtils.ConvertEpochtoDate(sDOSFrom);

				sDOSTO = ido.get("dosToTs").toString();
				sDOSTO = GenericUtils.ConvertEpochtoDate(sDOSTO);

				sProcessingFromTs = ido.get("processingFromTs").toString();
				sProcessingFromTs = GenericUtils.ConvertEpochtoDate(sProcessingFromTs);

				sProcessingToTS = ido.get("processingToTs").toString();
				sProcessingToTS = GenericUtils.ConvertEpochtoDate(sProcessingToTS);

				sOpportunityStatus = ido.getLong("isProd");
				break;
			case "Reject":

				sReason = ido.getString("reason");
				break;
			case "Defer":

				sProcessingToTS = ido.get("processingToTs").toString();
				sProcessingToTS = GenericUtils.ConvertEpochtoDate(sProcessingToTS);
				break;
			case "Follow up":
				sFollowUpOwner = ido.getString("followUpOwner");
				if (ido.get("processingToTs") != null) {
					sProcessingToTS = ido.get("processingToTs").toString();
					sProcessingToTS = GenericUtils.ConvertEpochtoDate(sProcessingToTS);

				}

				break;
			default:
				Assert.assertTrue("Decision not found as expected in Db==>" + sStatus + ",for the DP==>" + sDP, false);
				break;
			}
			sNote = ido.getString("note");

			sDPData.put("Status", sStatus);
			sDPData.put("DOS FROM", sDOSFrom);
			sDPData.put("DOS TO", sDOSTO);
			sDPData.put("PROCESSING FROM", sProcessingFromTs);
			sDPData.put("PROCESSING TO", sProcessingToTS);
			sDPData.put("Reason", sReason);
			sDPData.put("Note", sNote);
			sDPData.put("FollowUpOwner", sFollowUpOwner);
			sDPData.put("OpportunityStatus", sOpportunityStatus);
			// return sDPData;

		}

		return sDPData;
	}

	public static ArrayList<Object> getDistinctMidRuleKeysfromEllCollection() {

		ArrayList<Object> distinctMidRules = new ArrayList<Object>();
		retrieveAllDocuments("cpd", "ellData");
		Bson MatchFilter = new BsonDocument();

		results = mColl.find(MatchFilter);
		recordsCount = mColl.count(MatchFilter);

		for (Document rs : results) {

			Object sMidRule = rs.get("midRuleKey");

			distinctMidRules.add(sMidRule);
		}

		System.out.println(distinctMidRules.size());
		return distinctMidRules;
	}

	public static HashMap<String, String> getEllDataforGivenMidRules(ArrayList<Object> sMidRules) {

		ArrayList<Object> resultList = new ArrayList<Object>();
		HashMap<String, String> sResultMap = new HashMap<String, String>();

		retrieveAllDocuments("cpd", "ellData");
		Bson MatchFilter = new BsonDocument();

		MatchFilter = Filters.in("midRuleKey", sMidRules);

		results = mColl.find(MatchFilter);
		recordsCount = mColl.count(MatchFilter);

		for (Document rs : results) {

			String sMidRule;
			String sTopicKey;
			String sTopicDesc;
			String sMPKey;
			String sMPDesc;
			String sDPKEY;
			String sDPDESC;
			String sDPSORTORDER;
			String sREFDESC;
			String sREFKEY;

			try {
				sMidRule = rs.get("midRuleKey").toString();
			} catch (Exception e) {
				sMidRule = null;
			}

			try {
				Document Ioccument = rs.get("topicKey", Document.class);
				Object sTopic = Ioccument.get("current");
				sTopicKey = String.valueOf(sTopic);
			} catch (Exception e) {
				sTopicKey = null;
			}

			try {
				Document Ioccument = rs.get("topicDesc", Document.class);
				Object oTopicDesc = Ioccument.get("current");
				sTopicDesc = String.valueOf(oTopicDesc);
			} catch (Exception e) {
				sTopicDesc = null;
			}

			try {
				Document Ioccument = rs.get("medPolicyKey", Document.class);
				Object oMPKey = Ioccument.get("current");
				sMPKey = String.valueOf(oMPKey);
			} catch (Exception e) {
				sMPKey = null;
			}

			try {
				Document Ioccument = rs.get("medPolicyDesc", Document.class);
				Object oMPDesc = Ioccument.get("current");
				sMPDesc = String.valueOf(oMPDesc);
			} catch (Exception e) {
				sMPDesc = null;
			}

			try {
				Document Ioccument = rs.get("dpKey", Document.class);
				Object oDPKey = Ioccument.get("current");
				sDPKEY = String.valueOf(oDPKey);
			} catch (Exception e) {
				sDPKEY = null;
			}

			try {
				Document Ioccument = rs.get("dpDesc", Document.class);
				Object oDPDesc = Ioccument.get("current");
				sDPDESC = String.valueOf(oDPDesc);
			} catch (Exception e) {
				sDPDESC = null;
			}

			try {
				Document Ioccument = rs.get("dpSortOrder", Document.class);
				Object oDPSortOrder = Ioccument.get("current");
				sDPSORTORDER = String.valueOf(oDPSortOrder);
			} catch (Exception e) {
				sDPSORTORDER = null;
			}

			try {
				Document Ioccument = rs.get("refSourceKey", Document.class);
				Object oRefKey = Ioccument.get("current");
				sREFKEY = String.valueOf(oRefKey);
			} catch (Exception e) {
				sREFKEY = null;
			}

			try {
				Document Ioccument = rs.get("refSourceDesc", Document.class);
				Object oRefDesc = Ioccument.get("current");
				sREFDESC = String.valueOf(oRefDesc);
			} catch (Exception e) {
				sREFDESC = null;
			}

			String sFormattedResult = sTopicKey + ";" + sTopicDesc + ";" + sMPKey + ";" + sMPDesc + ";" + sDPKEY + ";"
					+ sDPDESC + ";" + sDPSORTORDER + ";" + sREFKEY + ";" + sREFDESC;

			sResultMap.put(sMidRule, sFormattedResult);

			System.out.println("Column data " + sFormattedResult);
			resultList.add(sFormattedResult);
		}

		System.out.println(resultList.size());
		return sResultMap;
	}

	public static ArrayList<Object> getEllDatainOpptyforGivenMidRulesV2(ArrayList<Object> sMidRules,
			HashMap<String, String> sMap) {

		ArrayList<Object> resultList = new ArrayList<Object>();

		HashMap<String, Object> sResultMap = new HashMap<String, Object>();

		retrieveAllDocuments("cpd", "oppty");
		Bson MatchFilter = new BsonDocument();

		MatchFilter = Filters.in("subRule.hierarchy.midRuleKey", sMidRules);

		results = mColl.find(MatchFilter);
		recordsCount = mColl.count(MatchFilter);

		for (Document rs : results) {

			String sMidRule;
			String sTopicKey;
			String sTopicDesc;
			String sMPKey;
			String sMPDesc;
			String sDPKEY;
			String sDPDESC;
			String sDPSORTORDER;
			String sREFKEY;
			String sREFDESC;

			Document Ioccument = rs.get("subRule", Document.class);
			Document oHierachyDoc = Ioccument.get("hierarchy", Document.class);
			try {
				sMidRule = oHierachyDoc.get("midRuleKey").toString();
			} catch (Exception e) {
				sMidRule = null;
			}

			try {
				sTopicKey = oHierachyDoc.get("topicKey").toString();
			} catch (Exception e) {
				sTopicKey = null;
			}

			try {
				sTopicDesc = oHierachyDoc.get("topicDesc").toString();
			} catch (Exception e) {
				sTopicDesc = null;
			}

			try {
				sMPKey = oHierachyDoc.get("medPolicyKey").toString();
			} catch (Exception e) {
				sMPKey = null;
			}

			try {
				sMPDesc = oHierachyDoc.get("medPolicyDesc").toString();
			} catch (Exception e) {
				sMPDesc = null;
			}

			try {
				sDPKEY = oHierachyDoc.get("dpKey").toString();
			} catch (Exception e) {
				sDPKEY = null;
			}

			try {
				sDPDESC = oHierachyDoc.get("dpDesc").toString();
			} catch (Exception e) {
				sDPDESC = null;
			}

			try {
				sDPSORTORDER = oHierachyDoc.get("dpSortOrder").toString();
			} catch (Exception e) {
				sDPSORTORDER = null;
			}

			Document refSourceDoc = Ioccument.get("refSource", Document.class);

			try {
				sREFKEY = refSourceDoc.get("key").toString();
			} catch (Exception e) {
				sREFKEY = null;
			}

			try {
				sREFDESC = refSourceDoc.get("desc").toString();
			} catch (Exception e) {
				sREFDESC = null;
			}

			String sFormattedResult = sTopicKey + ";" + sTopicDesc + ";" + sMPKey + ";" + sMPDesc + ";" + sDPKEY + ";"
					+ sDPDESC + ";" + sDPSORTORDER + ";" + sREFKEY + ";" + sREFDESC;

			resultList.add(sFormattedResult);

			sResultMap.put(sMidRule, resultList);

		}

		System.out.println(resultList.size());
		return resultList;
	}

	public static ArrayList<Object> verifyEllDatainOpptyforGivenMidRules(HashMap<String, String> sMap) {

		ArrayList<Object> resultList = new ArrayList<Object>();

		retrieveAllDocuments("cpd", "oppty");
		Bson MatchFilter = new BsonDocument();

		Iterator<Map.Entry<String, String>> itr = sMap.entrySet().iterator();

		int iRecord = 0;

		while (itr.hasNext()) {
			try {
				Map.Entry<String, String> entry = itr.next();
				String sMidRuleKey = entry.getKey().trim();
				String sEllMidRuleDetails = entry.getValue().trim();

				MatchFilter = Filters.and(Filters.in("subRule.hierarchy.midRuleKey", Long.valueOf(sMidRuleKey)),
						Filters.eq("_id.dataVersion", java.util.regex.Pattern.compile("PMPRD1_20190821_215306")),
						Filters.in("_id.clientKey", Long.valueOf("51"), Long.valueOf("53")));

				results = mColl.find(MatchFilter);
				recordsCount = mColl.count(MatchFilter);

				System.out.println(iRecord);
				System.out.println("Processing " + sMidRuleKey);
				for (Document rs : results) {

					String sMidRule;
					String sTopicKey;
					String sTopicDesc;
					String sMPKey;
					String sMPDesc;
					String sDPKEY;
					String sDPDESC;
					String sDPSORTORDER;
					String sREFKEY;
					String sREFDESC;
					String sRVAKEY;

					Document Ioccument = rs.get("subRule", Document.class);
					Document oHierachyDoc = Ioccument.get("hierarchy", Document.class);
					try {
						sMidRule = oHierachyDoc.get("midRuleKey").toString();
					} catch (Exception e) {
						sMidRule = null;
					}

					try {
						sTopicKey = oHierachyDoc.get("topicKey").toString();
					} catch (Exception e) {
						sTopicKey = null;
					}

					try {
						sTopicDesc = oHierachyDoc.get("topicDesc").toString();
					} catch (Exception e) {
						sTopicDesc = null;
					}

					try {
						sMPKey = oHierachyDoc.get("medPolicyKey").toString();
					} catch (Exception e) {
						sMPKey = null;
					}

					try {
						sMPDesc = oHierachyDoc.get("medPolicyDesc").toString();
					} catch (Exception e) {
						sMPDesc = null;
					}

					try {
						sDPKEY = oHierachyDoc.get("dpKey").toString();
					} catch (Exception e) {
						sDPKEY = null;
					}

					try {
						sDPDESC = oHierachyDoc.get("dpDesc").toString();
					} catch (Exception e) {
						sDPDESC = null;
					}

					try {
						sDPSORTORDER = oHierachyDoc.get("dpSortOrder").toString();
					} catch (Exception e) {
						sDPSORTORDER = null;
					}

					Document refSourceDoc = Ioccument.get("refSource", Document.class);

					try {
						sREFKEY = refSourceDoc.get("key").toString();
					} catch (Exception e) {
						sREFKEY = null;
					}

					try {
						sREFDESC = refSourceDoc.get("desc").toString();
					} catch (Exception e) {
						sREFDESC = null;
					}

					try {
						sRVAKEY = rs.get("rvaKey").toString();
					} catch (Exception e) {
						sRVAKEY = null;
					}

					String sFormattedResult = sTopicKey + ";" + sTopicDesc + ";" + sMPKey + ";" + sMPDesc + ";" + sDPKEY
							+ ";" + sDPDESC + ";" + sDPSORTORDER + ";" + sREFKEY + ";" + sREFDESC;

					if (!sEllMidRuleDetails.trim().equalsIgnoreCase(sFormattedResult.trim())) {
						System.out.println("Values mismatched for " + sMidRule + " and RVA Key " + sRVAKEY);
						System.out.println("Expected : " + sEllMidRuleDetails + " and actual is " + sFormattedResult);
						// Serenity.recordReportData().withTitle(sMidRule).andContents(sMidRule);
						// Serenity.recordReportData().withTitle(sMidRule).andContents("Expected:
						// "+sEllMidRuleDetails+" \nActual: "+sFormattedResult);
						break;
					}

					resultList.add(sFormattedResult);

				}

			} catch (Exception e) {

			}

			iRecord = iRecord + 1;

		}

		System.out.println(resultList.size());
		return resultList;
	}

	public static HashMap<String, Object> verifyStatusinDBforGivenDP(String sDP, String sPayerKey, String sClaimType) {
		HashMap<String, Object> sDPData = new HashMap<String, Object>();
		String sDOSFrom = null;
		String sDOSTO = null;
		String sProcessingFromTs = null;
		String sProcessingToTS = null;
		String sReason = null;
		String sNote = null;
		String sFollowUpOwner = null;
		Long sOpportunityStatus = 0l;
		retrieveAllDocuments("presentations", "decision");
		Bson MatchFilter = new BsonDocument();
		Long DPKey = Long.valueOf(sDP);
		// CapturedInsuranceKey
		MatchFilter = Filters.and(Filters.eq("_id.dpKey", DPKey), Filters.eq("_id.payerKey", Long.valueOf(sPayerKey)),
				Filters.eq("_id.insuranceKey", Long.valueOf(Serenity.sessionVariableCalled("CapturedInsuranceKey"))),
				Filters.eq("_id.claimType", sClaimType),
				Filters.eq("_id.clientKey", Long.valueOf(Serenity.sessionVariableCalled("clientkey"))));
		Bson matchtext = Aggregates.match(Filters.and(MatchFilter));
		recordsCount = mColl.count(MatchFilter);
		System.out.println("Filtered_Count for the DPKEY '" + DPKey + "':" + recordsCount);

		if (recordsCount == 0) {
			Assert.assertTrue("Record count is zero for the given filter==>" + MatchFilter, false);
		}

		AggregateIterable<Document> output2 = mColl
				.aggregate(Arrays.asList(matchtext, Aggregates.sort(Sorts.ascending("_id"))));
		// results = mColl.find(MatchFilter);

		for (Document ido : output2) {
			// System.out.println(ido);
			String sStatus = ido.get("status").toString();

			switch (sStatus) {
			case "Approve":
			case "Approve with Mod":
				sDOSFrom = ido.get("dosFromTs").toString();
				sDOSFrom = GenericUtils.ConvertEpochtoDate(sDOSFrom);

				sDOSTO = ido.get("dosToTs").toString();
				sDOSTO = GenericUtils.ConvertEpochtoDate(sDOSTO);

				sProcessingFromTs = ido.get("processingFromTs").toString();
				sProcessingFromTs = GenericUtils.ConvertEpochtoDate(sProcessingFromTs);

				sProcessingToTS = ido.get("processingToTs").toString();
				sProcessingToTS = GenericUtils.ConvertEpochtoDate(sProcessingToTS);

				sOpportunityStatus = ido.getLong("isProd");
				break;
			case "Reject":

				sReason = ido.getString("reason");
				break;
			case "Defer":

				sProcessingToTS = ido.get("processingToTs").toString();
				sProcessingToTS = GenericUtils.ConvertEpochtoDate(sProcessingToTS);
				break;
			case "Follow up":
				sFollowUpOwner = ido.getString("followUpOwner");
				if (ido.get("processingToTs") != null) {
					sProcessingToTS = ido.get("processingToTs").toString();
					sProcessingToTS = GenericUtils.ConvertEpochtoDate(sProcessingToTS);

				}

				break;
			default:
				Assert.assertTrue("Decision not found as expected in Db==>" + sStatus + ",for the DP==>" + sDP, false);
				break;
			}
			sNote = ido.getString("note");

			sDPData.put("Status", sStatus);
			sDPData.put("DOS FROM", sDOSFrom);
			sDPData.put("DOS TO", sDOSTO);
			sDPData.put("PROCESSING FROM", sProcessingFromTs);
			sDPData.put("PROCESSING TO", sProcessingToTS);
			sDPData.put("Reason", sReason);
			sDPData.put("Note", sNote);
			sDPData.put("FollowUpOwner", sFollowUpOwner);
			sDPData.put("OpportunityStatus", sOpportunityStatus);
			// return sDPData;

		}
		System.out.println(sDPData);
		return sDPData;
	}

	public static String retrieveEditTopicDescrDBValues(String ClientName, String PresentationProfileName,
			String EditedTopicTitle, String ValueToFetch, String placeholderArg2) {

		retrieveAllDocuments("cpd", "oppty");
		Bson MatchFilter = new BsonDocument();
		int i = 0;
		String PresProfileID = "";
		Long TopicKey;
		String TopicDescr = "";

		String ValueToFetchFromDB = "";
		MongoCursor<String> Itr = null;
		String RetunVal = "";

		ValueToFetchFromDB = (ValueToFetch.split("-"))[1].trim();
		ValueToFetchFromDB = ValueToFetchFromDB.toUpperCase();

		switch (ValueToFetchFromDB) {

		case "TOPICDESCRIPTION":

			// Retrieve PresProfileId from DB using ClientName,PresProfileNam -
			// OPPTY collection
			MatchFilter = Filters.and(Filters.eq("_id.clientKey", Long.valueOf(Serenity.sessionVariableCalled("clientkey"))),
					Filters.eq("presentationProfile.profileName", PresentationProfileName));
			Distinctresults = mColl.distinct("presentationProfile.profileId", MatchFilter, String.class);

			Itr = Distinctresults.iterator();
			while (Itr.hasNext()) {
				PresProfileID = Itr.next();
			}
			System.out.println("PresProfileID::" + PresProfileID);

			// Retrieve TopicKey from DB using ClientName,PresProfileName and
			// TopicTitile - OPPTY collection
			MatchFilter = Filters.and(Filters.eq("_id.clientKey", Long.valueOf(Serenity.sessionVariableCalled("clientkey"))),
					Filters.eq("presentationProfile.profileName", PresentationProfileName),
					Filters.eq("subRule.hierarchy.topicDesc", EditedTopicTitle));
			DistinctIterable<Long> Distinctresults2 = mColl.distinct("subRule.hierarchy.topicKey", MatchFilter,
					Long.class);
			TopicKey = Distinctresults2.first();
			System.out.println("TopicKey::" + TopicKey);

			// Retrieve EditedTopicDesc from DB using PresProfileId and TopicKey
			// - profileTopics collection
			retrieveAllDocuments("presentations", "profileTopics");
			db = mongoClient.getDatabase("presentations");
			MatchFilter = Filters.and(Filters.eq("_id.profileId", PresProfileID), Filters.eq("_id.topicKey", TopicKey));
			DistinctIterable<String> Distinctresults3 = mColl.distinct("editedTopicDescLong", MatchFilter,
					String.class);
			MongoCursor<String> Itr2 = Distinctresults3.iterator();
			while (Itr2.hasNext()) {
				TopicDescr = Itr2.next();
			}

			RetunVal = TopicDescr;
		 System.out.println("Topic Desc::"+TopicDescr);
			break;

		case "PRESENTATIONPROFILEID":
			// Retrieve PresProfileId from DB using ClientName,PresProfileNam -
			// OPPTY collection
			MatchFilter = Filters.and(Filters.eq("clientDesc", ClientName),
					Filters.eq("presentationProfile.profileName", PresentationProfileName));
			Distinctresults = mColl.distinct("presentationProfile.profileId", MatchFilter, String.class);

			Itr = Distinctresults.iterator();
			while (Itr.hasNext()) {
				PresProfileID = Itr.next();
			}
			System.out.println("PresProfileID::" + PresProfileID);
			RetunVal = PresProfileID;

			break;
		}
		return RetunVal;
	}

	public static List<String> retrievePresentationProfileCollectionValues(String ClientName, String valuetoRetrieve,
			String Criteria) {

		MongoCursor<String> Itr = null;
		MongoCursor<String> Itr2 = null;
		int Resultsize = 0;

		// Retrieve the ClientKey for the ClientName
		//String clientKey = Retrieve_the_PayerKey_From_mongodb_with_the_given_Payershort(ClientName, "Client");
		String clientKey=Serenity.sessionVariableCalled("clientkey");
		// Method to connect mongoDB
		// db = mongoClient.getDatabase("presentations");
		retrieveAllDocuments("presentations", "profile");
		Bson MatchFilter = new BsonDocument();
		List<String> ResultData = new ArrayList<String>();

		switch (valuetoRetrieve) {
		case "PresentationProfiles":

			if (Criteria.equalsIgnoreCase("AllProfiles")) {
				MatchFilter = Filters.and(Filters.eq("clientKey", Long.valueOf(clientKey)),
								Filters.ne("presentationView", "Change Opportunities"));
				Distinctresults = mColl.distinct("profileName", MatchFilter, String.class);

				Itr = Distinctresults.iterator();

				while (Itr.hasNext()) {
					Resultsize += 1;
					Itr.next();
				}

				if (Resultsize == 0) {
					Assert.assertTrue("MongoDB not returned any values for the Query", false);
				}

				Itr2 = Distinctresults.iterator();
				int k = 0;
				while (Itr2.hasNext()) {
					ResultData.add(Itr2.next());
					System.out.println("Profile Name::" + ResultData.get(k));
					k = k + 1;
				}
			}
			break;

		case "PresentationProfileStatus":

			String PresName = Serenity.sessionVariableCalled("PresentationName");
			MatchFilter = Filters.and(Filters.eq("clientKey", Long.valueOf(clientKey)));
			MatchFilter = Filters.and(Filters.eq("profileName", PresName));
			Distinctresults = mColl.distinct("profileStatus", MatchFilter, String.class);
			Itr = Distinctresults.iterator();

			while (Itr.hasNext()) {
				Resultsize += 1;
				Itr.next();
			}

			if (Resultsize == 0) {
				Assert.assertTrue("MongoDB not returned any values for the Query", false);
			}
			Itr2 = Distinctresults.iterator();
			int k = 0;
			while (Itr2.hasNext()) {
				ResultData.add(Itr2.next());
				System.out.println("Profile Status::" + ResultData.get(k));
				k = k + 1;

			}

			break;

		}

		System.out.println("Profilenames==>" + ResultData);

		System.out.println("Profile size==>" + ResultData.size());
		return ResultData;
	}

	// To retrieve the Unassigned DPkeys with Present Disposition
	public static void GetUnavailableAvailableDPKeysfromPM(String Client, String DPKeyCriteria, int NoofInsuranceKeys) {
		boolean bstatus = false;
		String Payerkeys = null;
		String Release = null;
		String Medicalpolicy = null;
		String Topic = null;
		String DPKey = null;
		String Clientkey = null;
		String InsuranceKeys = null;
		List<String> InsuranceKeysList = null;
		List<String> DPkeysList = null;
		ArrayList<String> RequiredDPkeysList = new ArrayList<>();
		List<String> payerkeysList = null;
		AggregateIterable<Document> output = null;
		Bson matchtext = null;

		// Method to connect mongoDB
		retrieveAllDocuments("cpd", "oppty");

		Bson MatchFilter = new BsonDocument();

		// Filter to form a match query based on inputs
		MatchFilter = Filters.and(Filters.eq("_id.clientKey", Long.valueOf(Client)),
				// Filters.and(Filters.eq("clientDesc",
				// java.util.regex.Pattern.compile(Client)),
				Filters.eq("disposition.desc", "Present"), Filters.eq("presentationProfile.profileName", null),
				Filters.eq("ruleInBaseLine", 0));
		// Filters.ne("annualSavings.lines", 0),
		recordsCount = mColl.count(MatchFilter);
		System.out.println("Filtered_Count for the client '" + Client + "':" + recordsCount);

		if (recordsCount == 0) {
			Assert.assertTrue("Record count was zero for the given Client==>" + Client + ",Disposition==>Present",
					false);
		}

		matchtext = Aggregates.match(Filters.and(MatchFilter));

		if (DPKeyCriteria.contains("Single")) {
			// Aggregate filter to retrieve the output
			output = mColl.aggregate(Arrays.asList(matchtext,
					Aggregates.group("$_id.clientKey", Accumulators.addToSet("DPKey", "$subRule.hierarchy.dpKey"),
							Accumulators.addToSet("Payerkeys", "$_id.payerKey"),
							Accumulators.addToSet("Release", "$_id.dataVersion"),
							Accumulators.addToSet("Medicalpolicy", "$subRule.hierarchy.medPolicyDesc"),
							Accumulators.addToSet("Topic", "$subRule.hierarchy.topicDesc"),
							Accumulators.addToSet("Clientkey", "$_id.clientKey"),
							Accumulators.addToSet("InsuranceKeys", "$_id.insuranceKey"))));

		} else if (DPKeyCriteria.equalsIgnoreCase("Multiple")) {
			// Aggregate filter to retrieve the output
			output = mColl.aggregate(Arrays.asList(matchtext, Aggregates.group(
					new Document().append("Topic", "$subRule.hierarchy.topicDesc").append("Medicalpolicy",
							"$subRule.hierarchy.medPolicyDesc"),
					Accumulators.addToSet("Payerkeys", "$_id.payerKey"),
					Accumulators.addToSet("Release", "$_id.dataVersion"),
					Accumulators.addToSet("Medicalpolicy", "$subRule.hierarchy.medPolicyDesc"),
					Accumulators.addToSet("DPkeys", "$subRule.hierarchy.dpKey"),
					Accumulators.addToSet("Clientkey", "$_id.clientKey"),
					Accumulators.addToSet("InsuranceKeys", "$_id.insuranceKey"))));

		} else {
			Assert.assertTrue("Case not found -->" + DPKeyCriteria, false);
		}

		// Loop to retrieve the output
		for (Document document : output) {
			System.out.println(document);
			if (DPKeyCriteria.equalsIgnoreCase("Single")) {
				DPKey = StringUtils.substringBetween(String.valueOf(document), "DPKey=[", "], Payerkeys");
			} else {
				DPKey = StringUtils.substringBetween(String.valueOf(document), "DPkeys=[", "], Clientkey");
			}
			/*
			 * Payerkeys=StringUtils.substringBetween(String.valueOf(document),
			 * "Payerkeys=[","], Release");
			 * Release=StringUtils.substringBetween(String.valueOf(document),
			 * "Release=[","], Medicalpolicy");
			 * Medicalpolicy=StringUtils.substringBetween(String.valueOf(
			 * document), "Medicalpolicy=[","], Topic");
			 * Clientkey=StringUtils.substringBetween(String.valueOf(document),
			 * "Clientkey=[","], InsuranceKeys");
			 * InsuranceKeys=StringUtils.substringBetween(String.valueOf(
			 * document), "InsuranceKeys=[","]}}");
			 * 
			 * payerkeysList=Arrays.asList(Payerkeys.split(","));
			 * InsuranceKeysList=Arrays.asList(InsuranceKeys.split(","));
			 */

			DPkeysList = Arrays.asList(DPKey.split(","));

			for (int i = 0; i < DPkeysList.size(); i++) {
				MatchFilter = Filters.and(Filters.eq("_id.clientKey", Long.valueOf(Client)),
						Filters.eq("subRule.hierarchy.dpKey", Long.valueOf(DPkeysList.get(i).trim())),
						Filters.eq("disposition.desc", "Present"), Filters.ne("presentationProfile.profileName", null),
						Filters.eq("ruleInBaseLine", 0));
				recordsCount = mColl.count(MatchFilter);

				if (recordsCount == 0) {
					System.out.println("Required DPkey==>" + DPkeysList.get(i).trim());
					MatchFilter = Filters.and(Filters.eq("_id.clientKey", Long.valueOf(Client)),
							Filters.eq("subRule.hierarchy.dpKey", Long.valueOf(DPkeysList.get(i).trim())),
							Filters.eq("disposition.desc", "Present"), Filters.eq("ruleInBaseLine", 0));

					matchtext = Aggregates.match(Filters.and(MatchFilter));

					// Aggregate filter to retrieve the output
					output = mColl.aggregate(Arrays.asList(matchtext,
							Aggregates.group("$_id.clientKey",
									Accumulators.addToSet("DPKey", "$subRule.hierarchy.dpKey"),
									Accumulators.addToSet("Payerkeys", "$_id.payerKey"),
									Accumulators.addToSet("Release", "$_id.dataVersion"),
									Accumulators.addToSet("Medicalpolicy", "$subRule.hierarchy.medPolicyDesc"),
									Accumulators.addToSet("Topic", "$subRule.hierarchy.topicDesc"),
									Accumulators.addToSet("Clientkey", "$_id.clientKey"),
									Accumulators.addToSet("InsuranceKeys", "$_id.insuranceKey"))));

					for (Document reqDocument : output) {
						System.out.println(reqDocument);
					}
					RequiredDPkeysList.add(DPkeysList.get(i).trim());

					if (DPKeyCriteria.equalsIgnoreCase("Single")) {
						System.out.println(RequiredDPkeysList);
						bstatus = true;
						break;
					} else if (DPKeyCriteria.equalsIgnoreCase("Multiple") && RequiredDPkeysList.size() > 1) {
						System.out.println(RequiredDPkeysList);
						bstatus = true;
						break;
					}
				}

			}

			if (bstatus) {
				break;
			}

		}

		/*
		 * System.out.println("Clientkey ==>"+Clientkey); System.out.println(
		 * "DPKeys ==>"+DPKey); System.out.println("Payerkeys ==>"+Payerkeys);
		 * System.out.println("Release ==>"+Release); System.out.println(
		 * "Medicalpolicy ==>"+Medicalpolicy); System.out.println("Topic ==>"
		 * +Topic); System.out.println("InsuranceKeys ==>"+InsuranceKeys);
		 */

	}

	// ====================================================================================================>

	public static void GettheCapturedDispositionPayerLOBClaimTypesFromtheGiven(String dpkey) {
		ProjectVariables.PPSList.clear();
		String Insurance = null;
		String Payershort = null;
		String Claimtype = null;
		long sInsuranceKey = 0l;
		long sPayerKey = 0l;
		String Medicalpolicy = null;
		String Topic = null;
		List<String> payers = null;

		// Method to connect mongoDB
		retrieveAllDocuments("cpd", "oppty");

		Bson MatchFilter = new BsonDocument();

		// Filter to form a match query based on inputs
		MatchFilter = Filters.and(
				Filters.eq("_id.clientKey", Long.valueOf(Serenity.sessionVariableCalled("clientkey"))),
				Filters.eq("disposition.desc", "Present"), Filters.eq("subRule.hierarchy.dpKey", Long.valueOf(dpkey)),
				Filters.eq("ruleInBaseLine", 0));

		results = mColl.find(MatchFilter);
		recordsCount = mColl.count(MatchFilter);
		System.out.println("Filtered_Count:" + recordsCount);

		if (recordsCount == 0) {
			Assert.assertTrue("Record count was zero for the given Dpkey==>" + dpkey, false);
		}

		// Loop to retrieve the output
		for (Document document : results) {
			Document IDdocuumment = document.get("_id", Document.class);
			Document Subruledocuumment = document.get("subRule", Document.class);
			Document Hirerachydocuumment = Subruledocuumment.get("hierarchy", Document.class);
			Insurance = document.getString("insuranceDesc");
			Payershort = IDdocuumment.getString("payerShort");
			Claimtype = IDdocuumment.getString("claimType");
			Medicalpolicy = Hirerachydocuumment.getString("medPolicyDesc");
			Topic = Hirerachydocuumment.getString("topicDesc");
			sPayerKey = IDdocuumment.getLong("payerKey");
			sInsuranceKey = IDdocuumment.getLong("insuranceKey");

			ProjectVariables.PPSList.add(Payershort + "-" + Insurance + "-" + Claimtype);
			ProjectVariables.PPSKeyList.add(sPayerKey + "-" + sInsuranceKey + "-" + Claimtype);
			// System.out.println(Payershort+"-"+Insurance+"-"+Claimtype);
			ProjectVariables.DB_PayershortList.add(Payershort);
			ProjectVariables.DB_insuranceList.add(Insurance);
			ProjectVariables.DB_claimtypeList.add(Claimtype);
			Serenity.setSessionVariable("Medicalpolicy").to(Medicalpolicy);
			Serenity.setSessionVariable("Topic").to(Topic);
		}
		System.out.println(ProjectVariables.PPSList);
		System.out.println(ProjectVariables.DB_PayershortList);
		System.out.println(ProjectVariables.DB_insuranceList);
		System.out.println(ProjectVariables.DB_claimtypeList);
		System.out.println("Medicalpolicy==>" + Serenity.sessionVariableCalled("Medicalpolicy"));
		System.out.println("Topic==>" + Serenity.sessionVariableCalled("Topic"));
		System.out.println(ProjectVariables.PPSKeyList);
	}

	// ====================================================================================================>
	public static void GetThePriorityandSavings(String DPkey) {
		AggregateIterable<Document> output = null;
		String Priortiy = null;
		String PriorityReasons = null;
		String Rawsavings = null;

		// Method to connect mongoDB
		retrieveAllDocuments("cpd", "oppty");

		Bson MatchFilter = new BsonDocument();

		// Filter to form a match query based on inputs
		MatchFilter = Filters.and(
				Filters.eq("_id.clientKey", Long.valueOf(Serenity.sessionVariableCalled("clientkey"))),
				Filters.eq("disposition.desc", "Present"), Filters.eq("subRule.hierarchy.dpKey", Long.valueOf(DPkey)),
				Filters.eq("ruleInBaseLine", 0));

		Bson matchtext = Aggregates.match(Filters.and(MatchFilter));

		output = mColl.aggregate(Arrays.asList(matchtext,
				Aggregates.group("$subRule.hierarchy.dpKey",
						Accumulators.addToSet("PriorityReasons", "$disposition.reasons"),
						Accumulators.addToSet("Priority", "$disposition.priority"),
						Accumulators.sum("Rawsavings", "$annualSavings.raw"))));

		results = mColl.find(MatchFilter);
		recordsCount = mColl.count(MatchFilter);
		System.out.println("Filtered_Count:" + recordsCount);
		System.out.println("Filtered_Count,Time==>" + GenericUtils.getDateGivenFormat("dd/MM/yyyy h:mm:ss a"));

		if (recordsCount == 0) {
			Assert.assertTrue("Record count was zero for the given DPkey==>" + DPkey, false);
		}

		// Loop to retrieve the output
		for (Document document : output) {
			System.out.println(document);
			Priortiy = StringUtils.substringBetween(String.valueOf(document), "Priority=[", "], Rawsavings");
			PriorityReasons = StringUtils.substringBetween(String.valueOf(document), "PriorityReasons=[[",
					"]], Priority");
			Rawsavings = StringUtils.substringBetween(String.valueOf(document), "Rawsavings=", "}}");
		}

		System.out.println("Priority==>" + Priortiy);
		System.out.println("Priorityreasons==>" + PriorityReasons);
		System.out.println("Rawsavings==>" + Rawsavings);
		Serenity.setSessionVariable("Priority").to(Priortiy);
		Serenity.setSessionVariable("Priorityreasons").to(PriorityReasons);
		Serenity.setSessionVariable("Rawsavings").to(Rawsavings);

	}

	// ====================================================================================================

	public static ArrayList<Object> validateSortOrder(ArrayList<Integer> sValues, String sLevel, String sCollection) {
		ArrayList<Object> resultList = new ArrayList<Object>();

		retrieveAllDocuments("cpd", sCollection);
		Bson MatchFilter = new BsonDocument();

		switch (sLevel.toUpperCase()) {

		case "DP":
		case "DPS RETIRE":
			MatchFilter = Filters.in("dpKey.previous", sValues);
			break;

		case "MPS RETIRE":
			MatchFilter = Filters.in("medPolicyKey.previous", sValues);
			break;

		case "TPS RETIRE":
			MatchFilter = Filters.in("topicKey.previous", sValues);
			break;

		case "TOPIC":
			MatchFilter = Filters.and(Filters.in("topicKey.previous", sValues),
					Filters.eq("releaseLogKey.current", 1857));
			break;

		case "MEDICAL POLICY":
			MatchFilter = Filters.in("medPolicyKey.previous", sValues);
			break;

		case "DPS MIDRULES RETIRE":
		case "MPS MIDRULES RETIRE":
		case "TPS MIDRULES RETIRE":
			MatchFilter = Filters.in("subRule.hierarchy.midRuleKey", sValues);
			break;

		default:
			Assert.assertTrue("No critirea matched", false);

		}

		results = mColl.find(MatchFilter);
		recordsCount = mColl.count(MatchFilter);
		System.out.println(recordsCount);

		for (Document rs : results) {

			String sStatus;
			Document Ioccument = null;
			Document oSubRuleKey;
			Document oHierarchy;

			switch (sLevel.toUpperCase()) {

			case "DP":
				Ioccument = rs.get("dpSortOrder", Document.class);
				break;

			case "TOPIC":
				Ioccument = rs.get("topicSortOrder", Document.class);
				break;

			case "MEDICAL POLICY":
				Ioccument = rs.get("mpSortOrder", Document.class);
				break;

			case "DPS RETIRE":
				Ioccument = rs.get("dpRetire", Document.class);
				break;

			case "MPS RETIRE":
				Ioccument = rs.get("mpRetire", Document.class);
				break;

			case "TPS RETIRE":
				Ioccument = rs.get("topicRetire", Document.class);
				break;

			case "DPS MIDRULES RETIRE":
				oSubRuleKey = rs.get("subRule", Document.class);
				oHierarchy = oSubRuleKey.get("hierarchy", Document.class);
				Ioccument = oHierarchy.get("dpRetire", Document.class);
				break;

			case "MPS MIDRULES RETIRE":
				oSubRuleKey = rs.get("subRule", Document.class);
				oHierarchy = oSubRuleKey.get("hierarchy", Document.class);
				Ioccument = oHierarchy.get("mpRetire", Document.class);
				break;

			case "TPS MIDRULES RETIRE":
				oSubRuleKey = rs.get("subRule", Document.class);
				oHierarchy = oSubRuleKey.get("hierarchy", Document.class);
				Ioccument = oHierarchy.get("topicRetire", Document.class);
				break;

			default:
				Assert.assertTrue("No critirea matched", false);

			}

			try {
				sStatus = Ioccument.get("change").toString();
			} catch (Exception e) {
				sStatus = null;
			}

			if (!sStatus.equalsIgnoreCase("true")) {
				Assert.assertTrue("Values are as not as expected", false);
			}

		}

		return resultList;
	}

	// ====================================================================================================>
	public static long GetThePPSforthegivenMidruleKey(String midruleKey, String criteria) {
		ProjectVariables.PPSList.clear();
		String sMidRulekey = null;
		String sRuleversion = null;
		String sSubruleKey = null;
		String sPayerkey = null;
		String sInsuranceKey = null;
		String sClaimtype = null;
		String sProd_10 = null;
		String sTest_10 = null;
		String sPayershort = null;
		String sInsurance = null;
		List<String> DispositionreasonsList = null;

		// Method to connect mongoDB
		retrieveAllDocuments("cpd", "monthlyRelease");

		Bson MatchFilter = new BsonDocument();

		// Filter to form a match query based on inputs
		if (criteria.equalsIgnoreCase("Count")) {
			MatchFilter = Filters.and(Filters.exists("policySets", true));
		} else {
			MatchFilter = Filters.and(Filters.eq("_id.midRuleKey", Long.valueOf(midruleKey)),
					Filters.exists("policySets", true));
		}

		results = mColl.find(MatchFilter);
		recordsCount = mColl.count(MatchFilter);
		System.out.println("Filtered_Count:" + recordsCount);

		/*
		 * if(recordsCount==0) { Assert.assertTrue(
		 * "Record count was zero for the given midrulekey==>"+midruleKey,
		 * false); }
		 */
		if (!criteria.equalsIgnoreCase("Count")) {
			// Loop to retrieve the output
			for (Document document : results) {

				DispositionreasonsList = (List<String>) document.get("policySets");
				System.out.println("PPS Size==>" + DispositionreasonsList.size());
				for (int i = 0; i < DispositionreasonsList.size(); i++) {
					// System.out.println(String.valueOf(DispositionreasonsList.get(i)));
					sPayerkey = StringUtils.substringBetween(String.valueOf(DispositionreasonsList.get(i)), "payerKey=",
							", insuranceKey");
					sInsuranceKey = StringUtils.substringBetween(String.valueOf(DispositionreasonsList.get(i)),
							"insuranceKey=", ", claimType");
					sClaimtype = StringUtils.substringBetween(String.valueOf(DispositionreasonsList.get(i)),
							"claimType=", ", prod_10");
					sProd_10 = StringUtils.substringBetween(String.valueOf(DispositionreasonsList.get(i)), "prod_10=",
							", test_10");
					sTest_10 = StringUtils.substringBetween(String.valueOf(DispositionreasonsList.get(i)), "test_10=",
							", payer_short");
					sPayershort = StringUtils.substringBetween(String.valueOf(DispositionreasonsList.get(i)),
							"payer_short=", ", insuranceDesc");
					sInsurance = StringUtils.substringBetween(String.valueOf(DispositionreasonsList.get(i)),
							"insuranceDesc=", ", rule");
					sRuleversion = StringUtils.substringBetween(String.valueOf(DispositionreasonsList.get(i)), ".",
							", switchkey");

					ProjectVariables.PPSList.add(sPayerkey + ";" + sInsuranceKey + ";" + sClaimtype + ";" + sProd_10
							+ ";" + sTest_10 + ";" + sPayershort + ";" + sInsurance + ";" + sRuleversion);
					System.out.println(sPayerkey + ";" + sInsuranceKey + ";" + sClaimtype + ";" + sProd_10 + ";"
							+ sTest_10 + ";" + sPayershort + ";" + sInsurance + ";" + sRuleversion);
				}
			}

		}

		return recordsCount;

	}

	public static void GetRequiredCountOfDPKeysfromCPW(String Client, String DPKeyCriteria, int NoofInsuranceKeys) {
		boolean bstatus = false;
		String Payerkeys = null;
		String Release = null;
		String Medicalpolicy = null;
		String Topic = null;
		String DPKey = null;
		String Clientkey = null;
		String InsuranceKeys = null;
		List<String> InsuranceKeysList = null;
		List<String> DPkeysList = null;
		List<String> payerkeysList = null;
		AggregateIterable<Document> output = null;

		// Method to connect mongoDB
		retrieveAllDocuments("cpd", "oppty");

		Bson MatchFilter = new BsonDocument();

		// Filter to form a match query based on inputs
		MatchFilter = Filters.and(Filters.eq("_id.clientKey", Long.valueOf(Client)),
				Filters.eq("disposition.desc", "No Disposition"), Filters.eq("ruleInBaseLine", 0));
		// Filters.ne("annualSavings.lines", 0)
		recordsCount = mColl.count(MatchFilter);
		System.out.println("Filtered_Count for the client '" + Client + "':" + recordsCount);

		if (recordsCount == 0) {
			Assert.assertTrue(
					"Record count was zero for the given Client==>" + Client + ",Disposition==>No Disposition", false);
		}

		Bson matchtext = Aggregates.match(Filters.and(MatchFilter));

		// Aggregate filter to retrieve the output
		output = mColl.aggregate(Arrays.asList(matchtext,
				Aggregates.group(
						new Document().append("Topic", "$subRule.hierarchy.topicDesc")
						.append("Medicalpolicy", "$subRule.hierarchy.medPolicyDesc").append("Release",
								"$_id.dataVersion"),
						Accumulators.addToSet("Payerkeys", "$_id.payerKey"),
						Accumulators.addToSet("Release", "$_id.dataVersion"),
						Accumulators.addToSet("Medicalpolicy", "$subRule.hierarchy.medPolicyDesc"),
						Accumulators.addToSet("DPkeys", "$subRule.hierarchy.dpKey"),
						Accumulators.addToSet("Clientkey", "$_id.clientKey"),
						Accumulators.addToSet("InsuranceKeys", "$_id.insuranceKey"))));

		// Loop to retrieve the output
		for (Document document : output) {

			Payerkeys = StringUtils.substringBetween(String.valueOf(document), "Payerkeys=[", "], Release");
			Release = StringUtils.substringBetween(String.valueOf(document), "Release=[", "], Medicalpolicy");
			Medicalpolicy = StringUtils.substringBetween(String.valueOf(document), "Medicalpolicy=[", "], Topic");
			Clientkey = StringUtils.substringBetween(String.valueOf(document), "Clientkey=[", "], InsuranceKeys");
			InsuranceKeys = StringUtils.substringBetween(String.valueOf(document), "InsuranceKeys=[", "]}}");

			payerkeysList = Arrays.asList(Payerkeys.split(","));
			InsuranceKeysList = Arrays.asList(InsuranceKeys.split(","));

			if (payerkeysList.size() > 2 && InsuranceKeysList.size() > NoofInsuranceKeys) {

				if (DPKeyCriteria.contains("RequiredCount")) {

					// Split this
					int count = 7;

					DPKey = StringUtils.substringBetween(String.valueOf(document), "DPkeys=[", "], Clientkey");
					Topic = StringUtils.substringBetween(String.valueOf(document), "Topic=", ", Medicalpolicy");
					Medicalpolicy = StringUtils.substringBetween(String.valueOf(document), "Medicalpolicy=",
							", Release");

					DPkeysList = Arrays.asList(DPKey.split(","));

					if (DPkeysList.size() >= count) {
						System.out.println(document);
						Serenity.setSessionVariable("clientkey").to(Clientkey);
						Serenity.setSessionVariable("DPkey").to(DPKey);
						Serenity.setSessionVariable("release").to(Release);
						Serenity.setSessionVariable("Medicalpolicy").to(Medicalpolicy);
						Serenity.setSessionVariable("Topic").to(Topic);
						Serenity.setSessionVariable("Payerkeys").to(Payerkeys);
						Serenity.setSessionVariable("InsuranceKeys").to(InsuranceKeys);
						bstatus = true;
						// System.out.println(document);
						break;
					}

				}
			}

		}

		if (Release.contains(",")) {
			Assert.assertTrue("Reteieved Multiple 'Dataversions' instead of single,Dataversion ==>" + Release
					+ ",Clientkey==>" + Serenity.sessionVariableCalled("clientkey") + ",Medicalpolicy==>"
					+ Serenity.sessionVariableCalled("Medicalpolicy") + ",Topic==>"
					+ Serenity.sessionVariableCalled("Topic") + ",DPkey==>" + Serenity.sessionVariableCalled("DPkey"),
					false);
		}
		if (!bstatus) {
			Assert.assertTrue("Unable to find the records from Mongo DB,for the client=>" + Client + ",DPKeyCriteria=>"
					+ DPKeyCriteria + ",Disposition=>No Disposition,PayerKeys count > 2 and Insurancekeys count > "
					+ NoofInsuranceKeys + "", false);
		}

		System.out.println("Clientkey ==>" + Clientkey);
		System.out.println("DPKeys ==>" + DPKey);
		System.out.println("Payerkeys ==>" + Payerkeys);
		System.out.println("Release ==>" + Release);
		System.out.println("Medicalpolicy ==>" + Medicalpolicy);
		System.out.println("Topic ==>" + Topic);
		System.out.println("InsuranceKeys ==>" + InsuranceKeys);

	}

	// ====================================================================================================>

	public static ArrayList<String> getRuleStatus(ArrayList<Integer> sValues, String sColumns) {

		ArrayList<String> resultList = new ArrayList<String>();
		retrieveAllDocuments("cpd", "monthlyRelease");
		Bson MatchFilter = new BsonDocument();

		MatchFilter = Filters.and(Filters.in("_id.subRuleKey", sValues), Filters.eq("_id.releaseDate", "20191126"));

		results = mColl.find(MatchFilter);
		recordsCount = mColl.count(MatchFilter);
		System.out.println(recordsCount);

		for (Document rs : results) {

			String sColumnValue = "";
			String result = "";
			Document Ioccument = null;

			Ioccument = rs.get("_id", Document.class);

			String[] sColumnList = sColumns.split(";");

			for (int i = 0; i < sColumnList.length; i++) {

				switch (sColumnList[i].toUpperCase()) {

				case "SUB_RULE_KEY":
					try {
						sColumnValue = Ioccument.get("subRuleKey").toString();
					} catch (Exception e) {
						sColumnValue = null;
					}
					break;
				case "DOS_TO":
					try {
						sColumnValue = rs.get("retiredDate").toString();
					} catch (Exception e) {
						sColumnValue = null;
					}
					break;
				case "DEACTIVATED_10":
					try {
						sColumnValue = rs.get("deactivate").toString();
					} catch (Exception e) {
						sColumnValue = null;
					}
					break;

				case "DISABLED_10":
					try {
						sColumnValue = rs.get("disabled").toString();
					} catch (Exception e) {
						sColumnValue = null;
					}
					break;
				}

				result = result + sColumnList[i] + ":" + sColumnValue;

			}

			System.out.println(result);

			resultList.add(result);

		}

		return resultList;
	}

	public static ArrayList<Integer> getRetiredMidRulesFromEll(String sKey) {

		ArrayList<Integer> AllMidRules = new ArrayList<Integer>();
		retrieveAllDocuments("cpd", "ellData");
		Bson MatchFilter = new BsonDocument();

		switch (sKey.toUpperCase()) {

		case "DPS MIDRULES RETIRE":
			MatchFilter = Filters.eq("dpRetire.change", true);
			break;
		case "MPS MIDRULES RETIRE":
			MatchFilter = Filters.eq("mpRetire.change", true);
			break;
		case "TPS MIDRULES RETIRE":
			MatchFilter = Filters.eq("topicRetire.change", true);
			break;
		}

		results = mColl.find(MatchFilter);
		recordsCount = mColl.count(MatchFilter);
		System.out.println(recordsCount);

		for (Document rs : results) {

			Document Ioccument = rs.get("_id", Document.class);

			String sMidRule = Ioccument.get("midRuleKey").toString();

			AllMidRules.add(Integer.parseInt(sMidRule));

		}

		Serenity.setSessionVariable("MidRules").to(AllMidRules);
		return AllMidRules;
	}

	public static ArrayList<Integer> getSubRuleKeyhavingStatus(String sDisabled, String sDeactivate,
			String sRetiredStatus, String sCollection) {

		ArrayList<Integer> resultList = new ArrayList<Integer>();
		retrieveAllDocuments("cpd", sCollection);
		Bson MatchFilter = new BsonDocument();

		MatchFilter = Filters.and(Filters.eq("disabled", Integer.parseInt(sDisabled)),
				Filters.eq("deactivate", Integer.parseInt(sDeactivate)),
				Filters.eq("retired", Integer.parseInt(sRetiredStatus)));

		results = mColl.find(MatchFilter);
		recordsCount = mColl.count(MatchFilter);
		System.out.println(recordsCount);

		for (Document rs : results) {

			Document Ioccument = rs.get("_id", Document.class);

			String sMidRule = Ioccument.get("subRuleKey").toString();

			resultList.add(Integer.parseInt(sMidRule));

		}

		Serenity.setSessionVariable("slstSubRules").to(resultList);

		return resultList;
	}

	public static boolean ValidateStatusforSubRules(ArrayList<Integer> sValues, String sExpected, String sCollection) {

		retrieveAllDocuments("cpd", sCollection);
		Bson MatchFilter = new BsonDocument();

		MatchFilter = Filters.and(Filters.in("_id.subRuleKey", sValues), Filters.eq("_id.dataVersion", ""));

		results = mColl.find(MatchFilter);
		recordsCount = mColl.count(MatchFilter);
		System.out.println(recordsCount);

		for (Document rs : results) {

			Document Ioccument = rs.get("subRule", Document.class);

			String sStatus = Ioccument.get("ruleStatus").toString();

			if (!sStatus.equalsIgnoreCase(sExpected)) {
				GenericUtils.Verify("Values are as not as expected", false);
			}

		}

		return true;

	}

	public static ArrayList<String> getNewVersionRulesfromMongo(ArrayList<Integer> sValues, String sCollection) {

		retrieveAllDocuments("cpd", sCollection);

		ArrayList<String> resultList = new ArrayList<String>();

		Bson MatchFilter = new BsonDocument();

		MatchFilter = Filters.and(Filters.in("_id.midRuleKey", sValues));

		results = mColl.find(MatchFilter);
		recordsCount = mColl.count(MatchFilter);
		System.out.println(recordsCount);

		for (Document rs : results) {
			String result = "";
			Document oIDccument = rs.get("_id", Document.class);

			String sMidRule = oIDccument.get("midRuleKey").toString();

			String sChange = rs.get("ellChange").toString();

			if (!sChange.contains("New Version")) {
				GenericUtils.Verify("New Version is not displayed for " + sMidRule, false);
			}

			Document Ioccument = rs.get("version", Document.class);

			String sCurrent = Ioccument.get("current").toString();
			String sPrevious = Ioccument.get("previous").toString();

			result = "NEW_MID_RULE:" + sMidRule + "OLD_VERSION:" + sPrevious + "NEW_VERSION:" + sCurrent;

			System.out.println(result);

			resultList.add(result);

		}

		return resultList;

	}

	public static void getRuleRelationshipDetails(String dPKeyCount, String valToRetrieve,
			String RuleRelationshipCombination, String ClientKey) {

		// Method to connect mongoDB
		retrieveAllDocuments("cpd", "oppty");
		Bson MatchFilter = new BsonDocument();
		Bson MatchFilter2 = new BsonDocument();

		String RelationShipRegex = "";
		// ** pass Client name as one of the argument
		RuleRelationshipCombination = "AnyOneOfRelationships-Companion-Counterpart-OutofSequence";

		switch (RuleRelationshipCombination) {

		case "AnyOneOfRelationships-Companion-Counterpart-OutofSequence":
			RelationShipRegex = "COUNTERPART | COMPANION |OUT OF SEQUENCE";

			break;

		case "OnlyMutuallyExclusiveRelationship":
			RelationShipRegex = "MUTUALLY EXCLUSIVE";
			break;

		case "MutuallyExclusiveRelationshipWithAnyOtherRelation":
			RelationShipRegex = "COUNTERPART | COMPANION|MUTUALLY EXCLUSIVE";

			break;

		default:
			System.out.println("No input provided for Switch case");

		}

		MatchFilter = Filters.and(Filters.eq("disposition", "No Disposition"),
				Filters.eq("_id.clientKey", Long.valueOf(ClientKey)), Filters.eq("ruleRelations.activeRules",
						java.util.regex.Pattern.compile("(" + RelationShipRegex + ")")));

		results = mColl.find(MatchFilter);
		Long DPkey = 0L;
		String PayerShort = "";
		String MedPolicy = "";

		for (Document document : results) {
			Document ID_Doccument = document.get("_id", Document.class);
			Document Subrule_Doccument = document.get("subrule", Document.class);
			Document hierarchy_Doccument = Subrule_Doccument.get("hierarchy", Document.class);
			MedPolicy = hierarchy_Doccument.getString("medPolicyDesc").trim();
			DPkey = hierarchy_Doccument.getLong("dpkey");
			PayerShort = ID_Doccument.getString("payerShort").trim();
			break;
		}

		Serenity.setSessionVariable("DPKey").to(DPkey);
		Serenity.setSessionVariable("MedicalPolicy").to(MedPolicy);
		Serenity.setSessionVariable("PayerShort").to(PayerShort);

	}

	// ====================================================================================================>

	public static void GetTheConfiguredPayerLOBsforThegivenClientKey(String ClientKey) {
		ProjectVariables.PPSList.clear();
		String Insurance = null;
		String Payershort = null;
		// Method to connect mongoDB
		retrieveAllDocuments("cpd", "oppty");

		Bson MatchFilter = new BsonDocument();

		// Filter to form a match query based on inputs
		MatchFilter = Filters.and(Filters.eq("_id.clientKey", Long.valueOf(ClientKey)));

		results = mColl.find(MatchFilter);
		recordsCount = mColl.count(MatchFilter);
		System.out.println("Filtered_Count:" + recordsCount);

		if (recordsCount == 0) {
			Assert.assertTrue("Record count was zero for the given ClientKey==>" + ClientKey, false);
		}

		// Loop to retrieve the output
		for (Document document : results) {

			Insurance = document.getString("insuranceDesc");
			Payershort = document.getString("payerShort");
			ProjectVariables.DB_PayershortList.add(Payershort);
			ProjectVariables.DB_insuranceList.add(Insurance);

		}

		System.out.println(ProjectVariables.DB_PayershortList);
		System.out.println(ProjectVariables.DB_insuranceList);

	}

	// ====================================================================================================>

	public static void GetTheConfiguredPresentationsforThegivenClientKey(String ClientKey) {
		ProjectVariables.PPSList.clear();
		String sProfilename = null;
		Long sCreatedtime = 0l;
		String sExactCreatedtime = null;
		int i = 1;
		// Method to connect mongoDB
		retrieveAllDocuments("presentations", "profile");

		Bson MatchFilter = new BsonDocument();

		// Filter to form a match query based on inputs
		MatchFilter = Filters.and(Filters.eq("clientKey", Long.valueOf(ClientKey)));

		results = mColl.find(MatchFilter);
		recordsCount = mColl.count(MatchFilter);
		System.out.println("Filtered_Count:" + recordsCount);

		if (recordsCount == 0) {
			Assert.assertTrue("Record count was zero for the given ClientKey==>" + ClientKey, false);
		}

		// Loop to retrieve the output
		for (Document document : results) {

			sProfilename = document.getString("profileName");
			sCreatedtime = document.getLong("createdTime");
			sExactCreatedtime = GenericUtils.ConvertEpochtoDate(String.valueOf(sCreatedtime), "dd/MM/yyyy h:mm:ss a");
			ProjectVariables.ProfilewithCreatedTime.add(sProfilename + ";" + sExactCreatedtime);
			// System.out.println(i+"."+sProfilename+";"+sExactCreatedtime);
			i = i + 1;

		}

		System.out.println(ProjectVariables.ProfilewithCreatedTime);

	}

	// ====================================================================================================>

	public static void GettheMPTopicsAndDPsBasedOnPayerLOBClaimTypes(List<String> Payershorts, List<String> LOBs,
			List<String> Claimtypes, String Criteria) {
		String Rawsavings = null;
		String Medicalpolicy = null;
		String Topic = null;
		String DPkey = null;
		String sPayerhshort = null;
		String sInsurance = null;
		String sClaimtype = null;
		AggregateIterable<Document> output = null;
		ArrayList<String> MPWithSavings = new ArrayList<>();
		ArrayList<String> TopicWithSavings = new ArrayList<>();
		ArrayList<String> DPDetails = new ArrayList<>();

		// Method to connect mongoDB
		retrieveAllDocuments("cpd", "oppty");

		Bson MatchFilter = new BsonDocument();

		if (Payershorts == null) {
			// Filter to form a match query based on inputs
			MatchFilter = Filters.and(
					Filters.eq("_id.clientKey", Long.valueOf(Serenity.sessionVariableCalled("clientkey"))),
					Filters.eq("disposition.desc", "Present"), Filters.eq("ruleInBaseLine", 0),
					Filters.eq("opptyFinalize", null));
		} else {
			// Filter to form a match query based on inputs
			MatchFilter = Filters.and(
					Filters.eq("_id.clientKey", Long.valueOf(Serenity.sessionVariableCalled("clientkey"))),
					Filters.eq("disposition.desc", "Present"), Filters.in("_id.payerShort", Payershorts),
					Filters.in("insuranceDesc", LOBs), Filters.in("_id.claimType", Claimtypes),
					Filters.eq("ruleInBaseLine", 0), Filters.eq("opptyFinalize", null));
		}

		Bson matchtext = Aggregates.match(Filters.and(MatchFilter));

		switch (Criteria) {
		case "MP":
			output = mColl.aggregate(Arrays.asList(matchtext, Aggregates.group("$subRule.hierarchy.medPolicyDesc",
					Accumulators.sum("Rawsavings", "$annualSavings.raw"))));
			break;
		case "Topic":
			output = mColl.aggregate(Arrays.asList(matchtext, Aggregates.group("$subRule.hierarchy.topicDesc",
					Accumulators.sum("Rawsavings", "$annualSavings.raw"))));
			break;
		case "DPKey":
			output = mColl.aggregate(Arrays.asList(matchtext,
					Aggregates.group("$subRule.hierarchy.dpKey", Accumulators.sum("Rawsavings", "$annualSavings.raw"),
							Accumulators.addToSet("MP", "$subRule.hierarchy.medPolicyDesc"),
							Accumulators.addToSet("Topic", "$subRule.hierarchy.topicDesc"),
							Accumulators.addToSet("Payershort", "$payerShort"),
							Accumulators.addToSet("Claimtype", "$_id.claimType"),
							Accumulators.addToSet("Insurance", "$insuranceDesc"))));
			break;
		case "PPS":
			// Filter to form a match query based on inputs
			MatchFilter = Filters.and(
					Filters.eq("_id.clientKey", Long.valueOf(Serenity.sessionVariableCalled("clientkey"))),
					Filters.eq("disposition.desc", "Present"),
					Filters.eq("subRule.hierarchy.dpKey", Long.valueOf(Serenity.sessionVariableCalled("DPkey"))));
			matchtext = Aggregates.match(Filters.and(MatchFilter));
			output = mColl.aggregate(Arrays.asList(matchtext,
					Aggregates.group("$subRule.hierarchy.dpKey", Accumulators.sum("Rawsavings", "$annualSavings.raw"),
							Accumulators.addToSet("MP", "$subRule.hierarchy.medPolicyDesc"),
							Accumulators.addToSet("Topic", "$subRule.hierarchy.topicDesc"),
							Accumulators.addToSet("Payershort", "$payerShort"),
							Accumulators.addToSet("Claimtype", "$_id.claimType"),
							Accumulators.addToSet("Insurance", "$insuranceDesc"))));
			break;
		default:
			Assert.assertTrue("Case not found===>" + Criteria, false);
			break;
		}

		results = mColl.find(MatchFilter);
		recordsCount = mColl.count(MatchFilter);
		System.out.println("Filtered_Count:" + recordsCount);

		if (recordsCount == 0) {
			Assert.assertTrue("Record count was zero for the given filer==>" + MatchFilter, false);
		}

		// Loop to retrieve the output
		for (Document Doc : output) {

			switch (Criteria) {
			case "MP":
				Medicalpolicy = StringUtils.substringBetween(String.valueOf(Doc), "_id=", ", Rawsavings");
				Rawsavings = StringUtils.substringBetween(String.valueOf(Doc), "Rawsavings=", "}}");
				MPWithSavings.add(Medicalpolicy + ";" + Rawsavings + "=");
				break;
			case "Topic":
				Topic = StringUtils.substringBetween(String.valueOf(Doc), "_id=", ", Rawsavings");
				Rawsavings = StringUtils.substringBetween(String.valueOf(Doc), "Rawsavings=", "}}");
				TopicWithSavings.add(Topic + ";" + Rawsavings + "=");
				break;
			case "PPS":
			case "DPKey":
				// System.out.println(Doc);
				Medicalpolicy = StringUtils.substringBetween(String.valueOf(Doc), "MP=[", "], Topic");
				Topic = StringUtils.substringBetween(String.valueOf(Doc), "Topic=[", "], Payershort");
				DPkey = StringUtils.substringBetween(String.valueOf(Doc), "_id=", ", Rawsavings");
				Rawsavings = StringUtils.substringBetween(String.valueOf(Doc), "Rawsavings=", ", MP");
				sPayerhshort = StringUtils.substringBetween(String.valueOf(Doc), "Payershort=[", "], Claimtype");
				sInsurance = StringUtils.substringBetween(String.valueOf(Doc), "Insurance=[", "]}}");
				sClaimtype = StringUtils.substringBetween(String.valueOf(Doc), "Claimtype=[", "], Insurance");

				DPDetails.add(DPkey + ";" + Medicalpolicy + ";" + Topic + ";" + sPayerhshort + ";" + sInsurance + ";"
						+ sClaimtype + ";" + Rawsavings);
				break;

			default:
				Assert.assertTrue("Case not found===>" + Criteria, false);
				break;
			}

			if (Criteria.equalsIgnoreCase("DPKey")) {
				break;
			}

		}
		Serenity.setSessionVariable("MPwithsavings").to(MPWithSavings);
		Serenity.setSessionVariable("Topicwithsavings").to(TopicWithSavings);
		Serenity.setSessionVariable("DPDetails").to(DPDetails);
		System.out.println("Topic with savings==>" + Serenity.sessionVariableCalled("Topicwithsavings"));
		System.out.println("MP with savings==>" + Serenity.sessionVariableCalled("MPwithsavings"));
		System.out.println("DPDetails==>" + Serenity.sessionVariableCalled("DPDetails"));

	}

	// ====================================================================================================>

	// To retrieve the DPkeys with NoDisposition
	public static void GetAvailableDPKeyfromCPW(String Clientkey, String DPKeyCriteria, int NoofInsuranceKeys,
			int NoofpayerKeys, int NoofClaimtypes) {
		boolean bstatus = false;
		String Payerkeys = null;
		String Release = null;
		String Medicalpolicy = null;
		String Topic = null;
		String DPKey = null;
		String Claimtype = null;
		String InsuranceKeys = null;
		List<String> InsuranceKeysList = null;
		List<String> DPkeysList = null;
		List<String> payerkeysList = null;
		List<String> ClaimtypeList = null;
		AggregateIterable<Document> output = null;

		// Method to connect mongoDB
		retrieveAllDocuments("cpd", "oppty");

		Bson MatchFilter = new BsonDocument();

		// Filter to form a match query based on inputs
		MatchFilter = Filters.and(Filters.eq("_id.clientKey", Long.valueOf(Clientkey)),
				Filters.eq("disposition.desc", "No Disposition"), Filters.ne("annualSavings.lines", 0),
				Filters.eq("ruleInBaseLine", 0));

		recordsCount = mColl.count(MatchFilter);
		System.out.println("Filtered_Count for the client key '" + Clientkey + "':" + recordsCount);
		System.out.println("Filtered_Count for the client key '" + Clientkey + "':" + recordsCount + ",Time==>"
				+ GenericUtils.getDateGivenFormat("dd/MM/yyyy h:mm:ss a"));

		if (recordsCount == 0) {
			Assert.assertTrue(
					"Record count was zero for the given Clientkey==>" + Clientkey + ",Disposition==>No Disposition",
					false);
		}

		Bson matchtext = Aggregates.match(Filters.and(MatchFilter));

		if (DPKeyCriteria.contains("Single")) {
			// Aggregate filter to retrieve the output
			output = mColl.aggregate(Arrays.asList(matchtext,
					Aggregates.group(
							new Document().append("DPKey", "$subRule.hierarchy.dpKey").append("Release",
									"$_id.dataVersion"),
							Accumulators.addToSet("Payerkeys", "$_id.payerKey"),
							Accumulators.addToSet("Release", "$_id.dataVersion"),
							Accumulators.addToSet("Medicalpolicy", "$subRule.hierarchy.medPolicyDesc"),
							Accumulators.addToSet("Topic", "$subRule.hierarchy.topicDesc"),
							Accumulators.addToSet("Clientkey", "$_id.clientKey"),
							Accumulators.addToSet("InsuranceKeys", "$_id.insuranceKey"),
							Accumulators.addToSet("Claimtypes", "$_id.claimType"))));
		} else if (DPKeyCriteria.equalsIgnoreCase("Multiple")) {
			// Aggregate filter to retrieve the output
			output = mColl.aggregate(Arrays.asList(matchtext,
					Aggregates.group(
							new Document().append("Topic", "$subRule.hierarchy.topicDesc")
							.append("Medicalpolicy", "$subRule.hierarchy.medPolicyDesc").append("Release",
									"$_id.dataVersion"),
							Accumulators.addToSet("Payerkeys", "$_id.payerKey"),
							Accumulators.addToSet("Release", "$_id.dataVersion"),
							Accumulators.addToSet("Medicalpolicy", "$subRule.hierarchy.medPolicyDesc"),
							Accumulators.addToSet("DPkeys", "$subRule.hierarchy.dpKey"),
							Accumulators.addToSet("Clientkey", "$_id.clientKey"),
							Accumulators.addToSet("InsuranceKeys", "$_id.insuranceKey"),
							Accumulators.addToSet("Claimtypes", "$_id.claimType"))));

		} else {
			Assert.assertTrue("Case not found -->" + DPKeyCriteria, false);
		}

		// Loop to retrieve the output
		for (Document document : output) {

			Payerkeys = StringUtils.substringBetween(String.valueOf(document), "Payerkeys=[", "], Release");
			Release = StringUtils.substringBetween(String.valueOf(document), "Release=[", "], Medicalpolicy");
			Medicalpolicy = StringUtils.substringBetween(String.valueOf(document), "Medicalpolicy=[", "], Topic");
			Clientkey = StringUtils.substringBetween(String.valueOf(document), "Clientkey=[", "], InsuranceKeys");
			InsuranceKeys = StringUtils.substringBetween(String.valueOf(document), "InsuranceKeys=[", "], Claimtypes");
			Claimtype = StringUtils.substringBetween(String.valueOf(document), "Claimtypes=[", "]}}");

			payerkeysList = Arrays.asList(Payerkeys.split(","));
			InsuranceKeysList = Arrays.asList(InsuranceKeys.split(","));
			ClaimtypeList = Arrays.asList(Claimtype.split(","));

			if (payerkeysList.size() > NoofpayerKeys && InsuranceKeysList.size() > NoofInsuranceKeys
					&& ClaimtypeList.size() > NoofClaimtypes) {
				if (DPKeyCriteria.contains("Single")) {
					System.out.println(document);
					DPKey = StringUtils.substringBetween(String.valueOf(document), "DPKey=", ", Release");
					if (!DPKey.contains("-")) {
						Topic = StringUtils.substringBetween(String.valueOf(document), "Topic=[", "], Clientkey");
						Medicalpolicy = StringUtils.substringBetween(String.valueOf(document), "Medicalpolicy=[",
								"], Topic");

						Serenity.setSessionVariable("clientkey").to(Clientkey);
						Serenity.setSessionVariable("DPkey").to(DPKey);
						Serenity.setSessionVariable("release").to(Release);
						Serenity.setSessionVariable("Medicalpolicy").to(Medicalpolicy);
						Serenity.setSessionVariable("Topic").to(Topic);
						Serenity.setSessionVariable("Payerkeys").to(Payerkeys);
						Serenity.setSessionVariable("InsuranceKeys").to(InsuranceKeys);
						Serenity.setSessionVariable("Claimtypes").to(Claimtype);
						bstatus = true;
						// System.out.println(document);
						break;
					}

				} else if (DPKeyCriteria.equalsIgnoreCase("Multiple")) {

					DPKey = StringUtils.substringBetween(String.valueOf(document), "DPkeys=[", "], Clientkey");
					if (!DPKey.contains("-")) {
						Topic = StringUtils.substringBetween(String.valueOf(document), "Topic=", ", Medicalpolicy");
						Medicalpolicy = StringUtils.substringBetween(String.valueOf(document), "Medicalpolicy=",
								", Release");

						DPkeysList = Arrays.asList(DPKey.split(","));

						if (DPkeysList.size() > 2) {
							System.out.println(document);
							Serenity.setSessionVariable("clientkey").to(Clientkey);
							Serenity.setSessionVariable("DPkey").to(DPKey);
							Serenity.setSessionVariable("release").to(Release);
							Serenity.setSessionVariable("Medicalpolicy").to(Medicalpolicy);
							Serenity.setSessionVariable("Topic").to(Topic);
							Serenity.setSessionVariable("Payerkeys").to(Payerkeys);
							Serenity.setSessionVariable("InsuranceKeys").to(InsuranceKeys);
							Serenity.setSessionVariable("Claimtypes").to(Claimtype);
							bstatus = true;
							// System.out.println(document);
							break;
						}

					}
				}
			}

		}

		if (Release.contains(",")) {
			Assert.assertTrue("Reteieved Multiple 'Dataversions' instead of single,Dataversion ==>" + Release
					+ ",Clientkey==>" + Serenity.sessionVariableCalled("clientkey") + ",Medicalpolicy==>"
					+ Serenity.sessionVariableCalled("Medicalpolicy") + ",Topic==>"
					+ Serenity.sessionVariableCalled("Topic") + ",DPkey==>" + Serenity.sessionVariableCalled("DPkey"),
					false);
		}
		if (!bstatus) {
			Assert.assertTrue("Unable to find the records from Mongo DB,for the clientkey=>" + Clientkey
					+ ",DPKeyCriteria=>Single,Disposition=>No Disposition,PayerKeys count > " + NoofpayerKeys
					+ " ,Insurancekeys count >" + NoofInsuranceKeys + " and Claimtypes count >" + NoofClaimtypes + "",
					false);

		}

		System.out.println("Clientkey ==>" + Clientkey);
		System.out.println("DPKeys ==>" + DPKey);
		System.out.println("Payerkeys ==>" + Payerkeys);
		System.out.println("Release ==>" + Release);
		System.out.println("Medicalpolicy ==>" + Medicalpolicy);
		System.out.println("Topic ==>" + Topic);
		System.out.println("InsuranceKeys ==>" + InsuranceKeys);
		System.out.println("Claimtypes ==>" + InsuranceKeys);

		System.out.println("Output got at the Time==>" + GenericUtils.getDateGivenFormat("dd/MM/yyyy h:mm:ss a"));

	}

	// ====================================================================================================>

	// To retrieve the DPkeys From Available Opportunity Deck,which dont have
	// decisions
	public static boolean GetAvailableDPKeyfromAvailableOpportunityDeck(String Clientkey, String DPKeyCriteria,
			int NoofpayerKeys, int NoofInsuranceKeys, int NoofClaimtypes) {

		/*if(Serenity.sessionVariableCalled("user").toString()=="nkumar")
		{
			Serenity.setSessionVariable("user").to("natuva");
		}*/
		
		boolean bstatus = false;
		String Payerkeys = null;
		String Release = null;
		String Medicalpolicy = null;
		String Topic = null;
		String DPKey = null;
		String Claimtype = null;
		String InsuranceKeys = null;
		List<String> InsuranceKeysList = null;
		List<String> DPkeysList = null;
		List<String> payerkeysList = null;
		List<String> ClaimtypeList = null;
		AggregateIterable<Document> output = null;

		// Method to connect mongoDB
		Bson MatchFilter = new BsonDocument();

		// Filter to form a match query based on inputs
		MatchFilter = Filters.and(Filters.eq("_id.clientKey", Long.valueOf(Clientkey)),
				Filters.eq("disposition.desc", "Present"), Filters.ne("annualSavings.lines", 0),
				Filters.eq("ruleInBaseLine", 0),
				Filters.eq("disposition.userId", Serenity.sessionVariableCalled("user")),
				Filters.eq("presentationProfile.profileName", null));

		retrieveAllDocuments("cpd", "oppty",MatchFilter);
		
		//recordsCount = mColl.count(MatchFilter);
		System.out.println(
				"Filtered_Count for the client key '" + Clientkey + "' for the disposition Present:" + recordsCount);
		System.out.println("Filtered_Count for the client key '" + Clientkey + "' for the disposition Present:"
				+ recordsCount + ",Time==>" + GenericUtils.getDateGivenFormat("dd/MM/yyyy h:mm:ss a"));

		
		Bson matchtext = Aggregates.match(Filters.and(MatchFilter));

		if (DPKeyCriteria.contains("Single")) {
			// Aggregate filter to retrieve the output
			output = mColl.aggregate(Arrays.asList(matchtext,
					Aggregates.group(
							new Document().append("DPKey", "$subRule.hierarchy.dpKey")
							.append("Release", "$_id.dataVersion").append("Username", "$disposition.userId")
							.append("PresentationName", "$presentationProfile.profileName"),
							Accumulators.addToSet("Payerkeys", "$_id.payerKey"),
							Accumulators.addToSet("Release", "$_id.dataVersion"),
							Accumulators.addToSet("Medicalpolicy", "$subRule.hierarchy.medPolicyDesc"),
							Accumulators.addToSet("Topic", "$subRule.hierarchy.topicDesc"),
							Accumulators.addToSet("Clientkey", "$_id.clientKey"),
							Accumulators.addToSet("InsuranceKeys", "$_id.insuranceKey"),
							Accumulators.addToSet("Claimtypes", "$_id.claimType"))));
		} else if (DPKeyCriteria.equalsIgnoreCase("Multiple")) {
			// Aggregate filter to retrieve the output
			output = mColl.aggregate(Arrays.asList(matchtext,
					Aggregates.group(
							new Document().append("Topic", "$subRule.hierarchy.topicDesc")
							.append("Medicalpolicy", "$subRule.hierarchy.medPolicyDesc")
							.append("Release", "$_id.dataVersion").append("Username", "$disposition.userId")
							.append("PresentationName", "$presentationProfile.profileName"),
							Accumulators.addToSet("Payerkeys", "$_id.payerKey"),
							Accumulators.addToSet("Release", "$_id.dataVersion"),
							Accumulators.addToSet("Medicalpolicy", "$subRule.hierarchy.medPolicyDesc"),
							Accumulators.addToSet("DPkeys", "$subRule.hierarchy.dpKey"),
							Accumulators.addToSet("Clientkey", "$_id.clientKey"),
							Accumulators.addToSet("InsuranceKeys", "$_id.insuranceKey"),
							Accumulators.addToSet("Claimtypes", "$_id.claimType"))));

		} else {
			Assert.assertTrue("Case not found -->" + DPKeyCriteria, false);
		}

		// Loop to retrieve the output
		for (Document document : output) {

			Payerkeys = StringUtils.substringBetween(String.valueOf(document), "Payerkeys=[", "], Release");
			Release = StringUtils.substringBetween(String.valueOf(document), "Release=[", "], Medicalpolicy");
			Medicalpolicy = StringUtils.substringBetween(String.valueOf(document), "Medicalpolicy=[", "], Topic");
			Clientkey = StringUtils.substringBetween(String.valueOf(document), "Clientkey=[", "], InsuranceKeys");
			InsuranceKeys = StringUtils.substringBetween(String.valueOf(document), "InsuranceKeys=[", "], Claimtypes");
			Claimtype = StringUtils.substringBetween(String.valueOf(document), "Claimtypes=[", "]}}");

			payerkeysList = Arrays.asList(Payerkeys.split(","));
			InsuranceKeysList = Arrays.asList(InsuranceKeys.split(","));
			ClaimtypeList = Arrays.asList(Claimtype.split(","));

			if (payerkeysList.size() > NoofpayerKeys && InsuranceKeysList.size() > NoofInsuranceKeys
					&& ClaimtypeList.size() > NoofClaimtypes) {
				if (DPKeyCriteria.contains("Single")) {
					System.out.println(document);
					DPKey = StringUtils.substringBetween(String.valueOf(document), "DPKey=", ", Release");
					if (!DPKey.contains("-")) {
						Topic = StringUtils.substringBetween(String.valueOf(document), "Topic=[", "], Clientkey");
						Medicalpolicy = StringUtils.substringBetween(String.valueOf(document), "Medicalpolicy=[",
								"], Topic");

						Serenity.setSessionVariable("clientkey").to(Clientkey);
						Serenity.setSessionVariable("DPkey").to(DPKey);
						Serenity.setSessionVariable("release").to(Release);
						Serenity.setSessionVariable("Medicalpolicy").to(Medicalpolicy);
						Serenity.setSessionVariable("Topic").to(Topic);
						Serenity.setSessionVariable("Payerkeys").to(Payerkeys);
						Serenity.setSessionVariable("InsuranceKeys").to(InsuranceKeys);
						Serenity.setSessionVariable("Claimtypes").to(Claimtype);
						ProjectVariables.DataVersion = Release;
						ProjectVariables.CapturedDPkey = Long.valueOf(DPKey);
						bstatus = true;
						// System.out.println(document);
						break;
					}

				} else if (DPKeyCriteria.equalsIgnoreCase("Multiple")) {

					DPKey = StringUtils.substringBetween(String.valueOf(document), "DPkeys=[", "], Clientkey");
					if (!DPKey.contains("-")) {
						Topic = StringUtils.substringBetween(String.valueOf(document), "Topic=", ", Medicalpolicy");
						Medicalpolicy = StringUtils.substringBetween(String.valueOf(document), "Medicalpolicy=",
								", Release");

						DPkeysList = Arrays.asList(DPKey.split(","));

						if (DPkeysList.size() > 2) {
							System.out.println(document);
							Serenity.setSessionVariable("clientkey").to(Clientkey);
							Serenity.setSessionVariable("DPkey").to(DPKey);
							Serenity.setSessionVariable("release").to(Release);
							Serenity.setSessionVariable("Medicalpolicy").to(Medicalpolicy);
							Serenity.setSessionVariable("Topic").to(Topic);
							Serenity.setSessionVariable("Payerkeys").to(Payerkeys);
							Serenity.setSessionVariable("InsuranceKeys").to(InsuranceKeys);
							Serenity.setSessionVariable("Claimtypes").to(Claimtype);
							ProjectVariables.DataVersion = Release;
							bstatus = true;
							// System.out.println(document);
							break;
						}

					}
				}
			}

		}

		 if (!bstatus) {
			 System.out.println("Unable to find the records from Mongo DB,for the clientkey=>" + Clientkey
					 + ",DPKeyCriteria=>Single,Disposition=>Present,PayerKeys count > " + NoofpayerKeys
					 + " ,Insurancekeys count >" + NoofInsuranceKeys + " and Claimtypes count >" + NoofClaimtypes + "");

		 }
		 if (bstatus) {
			 System.out.println("Clientkey ==>" + Clientkey);
			 System.out.println("DPKeys ==>" + DPKey);
			 System.out.println("Payerkeys ==>" + Payerkeys);
			 System.out.println("Release ==>" + Release);
			 System.out.println("Medicalpolicy ==>" + Medicalpolicy);
			 System.out.println("Topic ==>" + Topic);
			 System.out.println("InsuranceKeys ==>" + InsuranceKeys);
			 System.out.println("Claimtypes ==>" + InsuranceKeys);

		 }

		 System.out.println("Output got at the Time==>" + GenericUtils.getDateGivenFormat("dd/MM/yyyy h:mm:ss a"));
		 return bstatus;
	}

	public static ArrayList<String> getDPKey(String sClientKey, String Release, String RuleStatus) {

		ArrayList<String> sOutPut = new ArrayList<String>();
		retrieveAllDocuments("cpd", "oppty");
		cursor = mColl.find().iterator();
		Bson MatchFilter = new BsonDocument();

		MatchFilter = Filters.and(Filters.eq("_id.clientKey", Long.valueOf(sClientKey)),
				//Filters.eq("_id.dataVersion", java.util.regex.Pattern.compile(Release)),
				Filters.nin("disposition.desc", Arrays.asList("No Disposition,Prior Approval".split(","))), Filters.eq("subRule.ruleStatus", RuleStatus.toUpperCase()),
				Filters.eq("ruleInBaseLine", 0),Filters.eq("presentationProfile.profileName", null));
		results = mColl.find(MatchFilter);
		recordsCount = mColl.count(MatchFilter);
		System.out.println("Filtered_Count:" + recordsCount);

		Bson matchtext = Aggregates.match(Filters.and(MatchFilter));

		AggregateIterable<Document> output = null;
			// Aggregate filter to retrieve the output
		output = mColl.aggregate(Arrays.asList(matchtext,
					Aggregates.group(
							new Document().append("DPKey", "$subRule.hierarchy.dpKey"),							
							Accumulators.addToSet("SubRules", "$_id.subRuleId"))));	
		for (Document document : output) {
			String DPKey=StringUtils.substringBetween(String.valueOf(document), "DPKey=", "}},");
			String SubRules=StringUtils.substringBetween(String.valueOf(document), "SubRules=[", "]");
			sOutPut.add(DPKey+"-"+SubRules);
		}
		return  sOutPut;
	}

	public static String getDPAndTopicRetire(String disposition) {
		retrieveAllDocuments("cpd", "retiredOppty");
		HashSet<String> MPList = new HashSet<>();

		cursor = mColl.find().iterator();
		Bson MatchFilter = new BsonDocument();

		switch (disposition) {

		case "No Disposition":
			MatchFilter = Filters.and(
					Filters.eq("_id.clientKey", Long.valueOf(Serenity.sessionVariableCalled("clientkey"))),
					//Filters.eq("_id.dataVersion",java.util.regex.Pattern.compile(Serenity.sessionVariableCalled("release"))),
					Filters.eq("disposition.desc", disposition), Filters.ne("annualSavings.lines", 0));
			break;
		case "Present":
			MatchFilter = Filters.and(
					Filters.eq("_id.clientKey", Long.valueOf(Serenity.sessionVariableCalled("clientkey"))),
					Filters.eq("disposition.desc", disposition), Filters.ne("annualSavings.lines", 0));
			break;
		case "Captured Disposition":
			MatchFilter = Filters.and(
					Filters.eq("_id.clientKey", Long.valueOf(Serenity.sessionVariableCalled("clientkey"))),
					Filters.eq("_id.dataVersion",
							java.util.regex.Pattern.compile(Serenity.sessionVariableCalled("release"))),
					Filters.ne("disposition.desc", "No Disposition"), Filters.ne("annualSavings.lines", 0));
			break;
		}
		results = mColl.find(MatchFilter);
		recordsCount = mColl.count(MatchFilter);
		System.out.println("Filtered_Count:" + recordsCount);

		for (Document document : results) {
			Document Subrule_Doccument = document.get("subRule", Document.class);
			Document hierarchy_Doccument = Subrule_Doccument.get("hierarchy", Document.class);
			Long dpKey = hierarchy_Doccument.getLong("dpKey");
			String MP = hierarchy_Doccument.getString("medPolicyDesc");
			String topicDescription = hierarchy_Doccument.getString("topicDesc");
			Serenity.setSessionVariable("DPkey").to(dpKey);
			Serenity.setSessionVariable("Medicalpolicy").to(MP);
			Serenity.setSessionVariable("Topic").to(topicDescription);
			MPList.add(MP + "::" + dpKey);
		}

		System.out.println("MPList==>" + MPList);
		System.out.println("MPsSize=====>" + MPList.size());
		return null;
	}

	public static void getNotificationChangeData(String sChange, String sColumnName) {

		Connection con = null;
		Long iClientKey;
		String sqlQuery = OracleDBQueries.getOracleDBQuery(sChange);
		boolean blnAvlDeck = false;
		boolean blnPresDeck = false;
		boolean blnCPWDeck = false;
		boolean blnCheck = false;
		String sMessage;
		boolean blnComplete = false;

		try {

			switch (sChange.toUpperCase()) {

			case "SAVINGS":
				Class.forName(ProjectVariables.DB_DRIVER_NAME);
				con = DriverManager.getConnection(ProjectVariables.DB_CONNECTION_RVAPRD1, ProjectVariables.DB_USERNAME,
						ProjectVariables.DB_PASSWORD);
				break;
			default:
				Class.forName(ProjectVariables.DB_DRIVER_NAME);
				con = DriverManager.getConnection(ProjectVariables.DB_CONNECTION_URL, ProjectVariables.DB_USERNAME,
						ProjectVariables.DB_PASSWORD);

			}

			if (con != null) {
				System.out.println("Connected to the Database...");
			} else {
				System.out.println("Database connection failed ");
			}

			Statement st = con.createStatement();
			st.setQueryTimeout(120);
			ResultSet rs = st.executeQuery(sqlQuery);

			while (rs.next()) {

				blnComplete = false;

				switch (sChange.toUpperCase()) {

				case "DP DESC CHANGE":
					String sDPKey = rs.getString("CUR_DP_KEY");
					blnComplete = setNotificationData(sDPKey, sChange, "DPDESC");
					break;

				case "TOPIC LONG DESC CHANGE":
					String sTPKey = rs.getString("TOPIC_KEY");
					blnComplete = setNotificationData(sTPKey, sChange, "TOPICLONGDESC");
					break;

				case "DP SORT ORDER CHANGE":
					String sDPSortKey = rs.getString("DP_KEY");
					blnComplete = setNotificationData(sDPSortKey, sChange, "DPSORTORDER");
					break;

				case "TOPIC DESC CHANGE":
					Long sTPDescKey = null;
					String sMidRuleKey = rs.getString("MID_RULE_KEY");
					retrieveAllDocuments("cpd", "ellData");
					Bson filterMatchFilter = Filters.and(Filters.eq("_id.midRuleKey", Integer.valueOf(sMidRuleKey)));
					FindIterable<Document> sData = ProjectVariables.mColl.find(filterMatchFilter);
					// Filter to form a match query based on inputs
					for (Document document : sData) {
						Document ID_Dcument = document.get("topicKey", Document.class);
						sTPDescKey = ID_Dcument.getLong("current");
						break;
					}
					blnComplete = setNotificationData(String.valueOf(sTPDescKey), sChange, "TOPICDESC");
					break;

				case "RULE RECAT":
					String sCurrentRule = rs.getString("MID_RULE_KEY");
					blnComplete = setNotificationData(sCurrentRule, sChange, "RULERECAT");
					break;

				case "DP RECAT":
					String sCurrentTpic = rs.getString("DP_KEY");
					blnComplete = setNotificationData(sCurrentTpic, sChange, "DPRECAT");
					break;

				case "TOPIC RECAT":
					String sCurrentMPKey = rs.getString("TOPIC_KEY");
					blnComplete = setNotificationData(sCurrentMPKey, sChange, "TOPICRECAT");
					break;

				case "TOPIC SORT ORDER":
					String sTopicKey = rs.getString("TOPIC_KEY");
					blnComplete = setNotificationData(sTopicKey, sChange, "TOPICSORTORDER");
					break;

				case "MP SORT ORDER":
					String sMPKey = rs.getString("MED_POL_KEY");
					blnComplete = setNotificationData(sMPKey, sChange, "MPSORTORDER");
					break;

				case "DP RETIRE":
					String sDPRetireKey = rs.getString("DP_KEY");
					blnComplete = setNotificationData(sDPRetireKey, sChange, "DPRETIRE");
					break;

				case "TOPIC RETIRE":

					String sTPRetireKey = rs.getString("TOPIC_KEY");
					blnComplete = setNotificationData(sTPRetireKey, sChange, "TOPICRETIRE");
					break;

				case "MP RETIRE":

					String sMPRetireKey = rs.getString("MED_POL_KEY");
					blnComplete = setNotificationData(sMPRetireKey, sChange, "MPRETIRE");
					break;

				case "RULE DISABLED":
					String sdisabledKey = rs.getString("SUB_RULE_KEY");
					blnComplete = setNotificationData(sdisabledKey, sChange, "RULEDISABLED");
					break;

				case "RULE DEACTIVATE":
					String sdeactivateKey = rs.getString("SUB_RULE_KEY");
					blnComplete = setNotificationData(sdeactivateKey, sChange, "RULEDEACTIVATED");
					break;

				case "NEW MIDRULE":

					String sNewVersionKey = rs.getString("MID_RULE_KEY");
					blnComplete = setNotificationData(sNewVersionKey, sChange, "NEWMIDRULE");
					break;

				case "NEW MIDRULE VERSION":

					String sMidRule = rs.getString("SUB_RULE_KEY");
					blnComplete = setNotificationData(sMidRule, sChange, "NEWVERSION");
					break;

				case "SWITCH":
					String sSwichRule = rs.getString("SUB_RULE_KEY");
					blnComplete = setNotificationData(sSwichRule, sChange, "SWITCH");

					break;

				case "PRIMARY SOURCE KEY":
					System.out.println("Data not available");

					break;

				case "PRIMARY SOURCE DESCRIPTION":
					System.out.println("Data not available");

					break;

				case "SAVINGS":
					System.out.println("Data not available");

					break;

				}

				if (blnComplete) {
					break;
				}

			}

			if (con != null) {
				con.close();
			}

		} catch (Exception e) {
			System.out.println("Exception " + e.getMessage());
		}

		finally {
			try {
				if (con != null)
					con.close();

			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

	}

	public static String getPresentationwithID(String sProfileID) {

		retrieveAllDocuments("presentations", "profile");
		Bson MatchFilter = new BsonDocument();
		MatchFilter = Filters.and(Filters.eq("_id", new ObjectId(sProfileID)));
		FindIterable<Document> sresults = mColl.find(MatchFilter);
		String sProfileName = null;
		for (Document document : sresults) {
			return document.getString("profileName");
		}
		return sProfileName;

	}

	public static boolean setNotificationData(String sChangeKey, String sChange, String sChangeType) throws Exception {

		boolean blnCheck = false;
		String sMessage = null;
		// Method to connect mongoDB
		retrieveAllDocuments("notifications", "notification");
		Bson filterMatchFilter = Filters.and(Filters.eq("_id.changeKey", Integer.valueOf(sChangeKey)),
				Filters.eq("_id.changeType", sChangeType), Filters.or(Filters.ne("cpwStatus", "invalid"),
						Filters.ne("availableDeckStatus", "invalid"), Filters.ne("presentationDeckStatus", "invalid")));
		results = mColl.find(filterMatchFilter);
		recordsCount = mColl.count(filterMatchFilter);
		System.out.println("Filtered_Count:" + recordsCount);

		// Filter to form a match query based on inputs
		for (Document document : results) {

			switch (sChange.toUpperCase()) {
			case "DP DESC CHANGE":
				sMessage = "DP description for DP " + sChangeKey + " has been updated";
				break;

			case "TOPIC LONG DESC CHANGE":
			case "TOPIC DESC CHANGE":
				String sTopicChange = document.getString("changeDesc");
				sMessage = "Topic description for '" + sTopicChange + "' has been updated";
				break;

			case "DP SORT ORDER CHANGE":
				String sOldValue = document.getString("oldValue");
				String sNewValue = document.getString("newValue");

				sMessage = "DP sort order for DP " + sChangeKey + " is updated from " + sOldValue + " to " + sNewValue;
				break;

			case "RULE RECAT":
				String sOldDPValue = document.getString("oldValue");
				String sNewDPValue = document.getString("newValue");
				sMessage = "Rule " + sChangeKey + " has been recategorized from " + sOldDPValue + " to " + sNewDPValue;
				break;

			case "DP RECAT":
				String sOldMPChg = document.getString("oldValue");
				String sNewMPChg = document.getString("newValue");
				sMessage = "DP " + sChangeKey + " has been recategorized from '" + sOldMPChg + "' to '" + sNewMPChg
						+ "'";
				break;

			case "TOPIC RECAT":
				String sOldTpValue = document.getString("oldValue");
				String sNewTPValue = document.getString("newValue");
				String sNewChangeChg = document.getString("changeDesc");
				sMessage = "Topic " + sNewChangeChg + " has been recategorized from '" + sOldTpValue + "' to '"
						+ sNewTPValue + "'";
				break;

			case "TOPIC SORT ORDER":
				String sOldTopicValue = document.getString("oldValue");
				String sNewTopicValue = document.getString("newValue");
				sMessage = "Topic sort order for Topic " + sChangeKey + " is updated from " + sOldTopicValue + " to "
						+ sNewTopicValue;
				break;

			case "MP SORT ORDER":
				String sOldMPValue = document.getString("oldValue");
				String sNewMPValue = document.getString("newValue");
				sMessage = "Topic sort order for Medical Policy " + sChangeKey + " is updated from " + sOldMPValue
						+ " to " + sNewMPValue;
				break;

			case "DP RETIRE":
				sMessage = "DP " + sChangeKey + " has been retired";
				break;

			case "TOPIC RETIRE":
				sMessage = "Topic " + sChangeKey + " has been retired";
				break;

			case "MP RETIRE":
				sMessage = "MP " + sChangeKey + " has been retired";
				break;

			case "RULE DEACTIVATE":
				String sRuleChange = document.getString("changeDesc");
				String sOldRuleValue = document.getString("oldValue");
				sMessage = "Rule " + sRuleChange + " under DP " + sOldRuleValue + " has been deactivated";
				break;

			case "RULE DISABLED":
				String sDisabledRuleChange = document.getString("changeDesc");
				String sTRule = sDisabledRuleChange.split(".")[0];
				sMessage = "Rule " + sTRule + " under DP " + sChangeKey + " has been disabled";
				break;

			case "NEW MIDRULE":
				sMessage = "NEW " + sChangeKey + " MID RULE";
				break;

			case "NEW MIDRULE VERSION":
				String sVersionChange = document.getString("changeDesc");
				String sNewVersionValue = document.getString("newValue");
				sMessage = "New rule version " + sVersionChange + " is added as an opportunity under DP "
						+ sNewVersionValue;
				break;

			case "SWITCH":
				String sSwictChange = document.getString("changeDesc");
				String sDPKey = document.getString("newValue");
				ArrayList idPPS = (ArrayList) (document.get("pps"));
				String sdesc = "";
				for (int i = idPPS.size() - 1; i >= 0; i--) {
					Map<Object, Object> svalues = (Map<Object, Object>) idPPS.get(i);
					String strdesc = svalues.get("policySetDesc").toString();
					sdesc = sdesc + "," + strdesc;
				}

				System.out.println(sdesc.substring(1));
				String sProdMessage = "Production switch is created on Rule " + sSwictChange + " under DP " + sDPKey
						+ " for " + sdesc.substring(1);

				boolean blnProdmessage = addMessage(document, sProdMessage, sChange);

				String sTestMessage = "Test switch is created on Rule " + sSwictChange + " under DP " + sDPKey + " for "
						+ sdesc.substring(1);

				boolean blnTestMessage = addMessage(document, sTestMessage, sChange);

				return (blnProdmessage && blnTestMessage);

			case "PRIMARY SOURCE KEY":
				System.out.println("Data not available for primary source key");
				break;

			case "PRIMARY SOURCE DESCRIPTION":
				System.out.println("Data not available for Primary Source desc");
				break;

			case "SAVINGS":
				sMessage = "No Message available";
				break;

			}

			blnCheck = addMessage(document, sMessage, sChange);

		}

		return blnCheck;
	}

	public static boolean addMessage(Document document, String sMessage, String sChange) throws Exception {

		Long iClientKey;
		boolean blnUpdate = false;

		String sPath = "src//test//resources//Notifications.xlsx";

		Document ID_Doccument = document.get("_id", Document.class);
		iClientKey = ID_Doccument.getLong("clientKey");

		if (verifyDispositionStatus(iClientKey)) {
			String sClientName = Retrieve_the_PayerKey_From_mongodb_with_the_given_Payershort(
					String.valueOf(iClientKey), "ClientKey");
			System.out.println(sClientName);

			String sAvlblDeckStatus = document.getString("availableDeckStatus");
			if (!sAvlblDeckStatus.equalsIgnoreCase("invalid")) {
				ExcelUtils.generateNotificationData("Available Deck", sClientName, "", sMessage, sChange, sPath, "");
				blnUpdate = true;

			}

			String spresentationDeckStatus = document.getString("presentationDeckStatus");

			if (!spresentationDeckStatus.equalsIgnoreCase("invalid")) {
				String sPrfID = ID_Doccument.getString("profileId");
				String Presentation = getPresentationwithID(sPrfID);
				ExcelUtils.generateNotificationData("Presentation Deck", sClientName, Presentation, sMessage, sChange,
						sPath, "");
				blnUpdate = true;

			}

			String scpwStatus = document.getString("cpwStatus");

			if (!scpwStatus.equalsIgnoreCase("invalid")) {
				ExcelUtils.generateNotificationData("CPW Deck", sClientName, "", sMessage, sChange, sPath, "");
				blnUpdate = true;

			}
		}

		return blnUpdate;
	}

	public static boolean verifyDispositionStatus(Long ClientKey) {

		retrieveAllDocuments("cpd", "oppty");

		Bson MatchFilter = new BsonDocument();

		List<String> release = Arrays.asList(ProjectVariables.sNotificationDataVersions);

		MatchFilter = Filters.and(Filters.eq("_id.clientKey", Long.valueOf(ClientKey)),
				Filters.eq("disposition.desc", "Present"), Filters.in("_id.dataVersion", release));

		results = mColl.find(MatchFilter);
		recordsCount = mColl.count(MatchFilter);
		System.out.println("Disposition present count" + recordsCount);

		return (recordsCount > 0);

	}

	public static void getTopicsByPolicyName(String clientKey, String policyName, String groupName) {
		retrieveAllDocuments("cpd", "oppty");
		Bson MatchFilter = new BsonDocument();
		List<String> returnFields = new ArrayList<String>();

		MatchFilter = Filters.and(Filters.eq("disposition.desc", "No Disposition"),
				Filters.eq("subRule.ruleRelations.groupName", java.util.regex.Pattern.compile("^" + groupName)),
				Filters.eq("_id.clientKey", Long.valueOf(clientKey)),
				Filters.eq("subRule.libStatusKey", Long.valueOf(1)),
				Filters.eq("subRule.hierarchy.medPolicyDesc", policyName),
				Filters.eq("ruleInBaseLine", Long.valueOf(0)), Filters.ne("annualSavings.lines", 0));

		Distinctresults = mColl.distinct("subRule.hierarchy.topicDesc", MatchFilter, String.class);
		MongoCursor<String> Itr = Distinctresults.iterator();
		while (Itr.hasNext())
			returnFields.add(Itr.next().toString());
		Serenity.setSessionVariable("Topics").to(returnFields);
	}

	public static List<String> getSubRuleKeys(String clientKey) {
		retrieveAllDocuments("cpd", "oppty");
		Bson MatchFilter = new BsonDocument();
		List<String> returnFields = new ArrayList<String>();

		MatchFilter = Filters.and(Filters.eq("_id.clientKey", Long.valueOf(clientKey)));
		Distinctresults = mColl.distinct("_id.subRuleKey", MatchFilter, String.class);
		MongoCursor<String> Itr = Distinctresults.iterator();
		while (Itr.hasNext())
			returnFields.add(Itr.next().toString());
		List<String> returnFields1 = returnFields;
		List<String> returnFields2 = returnFields1;
		return returnFields;
	}

	public static void getMedicalPolicyByClientName(String clientKey) {
		retrieveAllDocuments("cpd", "oppty");
		Bson MatchFilter = new BsonDocument();

		MatchFilter = Filters.and(Filters.eq("_id.clientKey", Long.valueOf(clientKey)),
				Filters.eq("disposition.desc", "No Disposition"), Filters.eq("subRule.libStatusKey", Long.valueOf(1)),
				Filters.eq("subRule.ruleRelations.groupName", java.util.regex.Pattern.compile("^COUNTERPART")),
				Filters.eq("ruleInBaseLine", 0), Filters.ne("annualSavings.lines", 0));

		Distinctresults = mColl.distinct("subRule.hierarchy.medPolicyDesc", MatchFilter, String.class);
		Serenity.setSessionVariable("MedicalPolicy").to(Distinctresults.first());
	}

	public static void getValuesForRuleRelationship(String valtoRetrieve, String ClientKey, String dispositionVal,
			String mDBCollectionName, String reqRelationship) {

		ArrayList<String> CapturedGroupNames = new ArrayList<String>();
		ArrayList<String> DPKeysList = new ArrayList<String>();
		List<Document> GroupNames = new ArrayList<Document>();

		List<String> DPPayershortDetails = new ArrayList<String>();

		HashMap<String, List<String>> DPKeyDetails = new HashMap<String, List<String>>();
		boolean DataFoundinDB = false;

		// Method to connect mongoDB
		retrieveAllDocuments("cpd", "oppty");
		Bson MatchFilter = new BsonDocument();

		String RelationShipRegex = "";

		switch (reqRelationship) {
		case "Non-MutuallyExclusiveRelationship":
			RelationShipRegex = "COUNTERPART | COMPANION |OUT OF SEQUENCE";
			break;

		case "MutuallyExclusiveRelationship":
			RelationShipRegex = "MUTUALLY EXCLUSIVE";
			break;

		case "AnyRuleRelationship":
		case "MutuallyExclusiveRelationshipWithAnyOtherRelation":
			RelationShipRegex = "COUNTERPART | COMPANION|OUT OF SEQUENCE|MUTUALLY EXCLUSIVE";
			break;

		default:
			System.out.println("No input provided for Switch case");

		}

		MatchFilter = Filters.and(Filters.eq("disposition.desc", dispositionVal),
				Filters.eq("_id.clientKey", Long.valueOf(ClientKey)), Filters.eq("prod_10", null),
				Filters.eq("subRule.ruleRelations.groupName",
						java.util.regex.Pattern.compile("^(" + RelationShipRegex + ")")),
				Filters.eq("subRule.libStatusKey", 1), Filters.ne("annualSavings.lines", 0),
				Filters.eq("ruleInBaseLine", 0), Filters.eq("presentationProfile.profileName", null));

		results = mColl.find(MatchFilter);
		recordsCount = mColl.count(MatchFilter);
		if (recordsCount == 0) {
			Assert.assertTrue("There are no records on MongoDB for provided query", false);
		} else {
			System.out.println("Records fetched from MongoDB::" + recordsCount);
		}

		Long DPkey = 0L;
		String PayerShort = "";
		String MedPolicy = "";
		String GroupName = "";
		String ActiveRules = "";
		String TopicName = "";

		for (Document document : results) {
			Document ID_Doccument = document.get("_id", Document.class);
			Document Subrule_Doccument = document.get("subRule", Document.class);
			Document hierarchy_Doccument = Subrule_Doccument.get("hierarchy", Document.class);
			GroupNames = Subrule_Doccument.get("ruleRelations", List.class);
			CapturedGroupNames = new ArrayList<String>();
			for (Document d : GroupNames) {
				GroupName = d.getString("groupName");
				CapturedGroupNames.add(GroupName);
			}

			if (reqRelationship.equalsIgnoreCase("MutuallyExclusiveRelationship")) {
				if (CapturedGroupNames.contains("COUNTERPART : NA") || CapturedGroupNames.contains("COMPANION : NA")
						|| CapturedGroupNames.contains("OUT OF SEQUENCE : Secondary")
						|| CapturedGroupNames.contains("OUT OF SEQUENCE : Primary")) {
					System.out.println("Only ME not there");
					CapturedGroupNames = null;
				} else if (CapturedGroupNames.contains("MUTUALLY EXCLUSIVE : CMS Only")
						|| CapturedGroupNames.contains("MUTUALLY EXCLUSIVE : CMS + Cotiviti")) {
					MedPolicy = hierarchy_Doccument.getString("medPolicyDesc").trim();
					DPkey = hierarchy_Doccument.getLong("dpKey");
					DPKeysList.add(DPkey.toString());
					TopicName = hierarchy_Doccument.getString("topicDesc").trim();
					PayerShort = ID_Doccument.getString("payerShort").trim();
					DataFoundinDB = true;
					break;
				}
			}

			else if (reqRelationship.equalsIgnoreCase("Non-MutuallyExclusiveRelationship")) {
				if (CapturedGroupNames.contains("MUTUALLY EXCLUSIVE : CMS Only")
						|| CapturedGroupNames.contains("MUTUALLY EXCLUSIVE : CMS + Cotiviti")) {
					System.out.println("Only ME is there,so not valid");
					CapturedGroupNames = null;
				} else {
					MedPolicy = hierarchy_Doccument.getString("medPolicyDesc").trim();
					DPkey = hierarchy_Doccument.getLong("dpKey");
					DPKeysList.add(DPkey.toString());
					TopicName = hierarchy_Doccument.getString("topicDesc").trim();
					PayerShort = ID_Doccument.getString("payerShort").trim();
					DataFoundinDB = true;

				}
			}

			else if (reqRelationship.equalsIgnoreCase("MutuallyExclusiveRelationshipWithAnyOtherRelation")) {
				if ((CapturedGroupNames.contains("MUTUALLY EXCLUSIVE : CMS Only")
						|| CapturedGroupNames.contains("MUTUALLY EXCLUSIVE : CMS + Cotiviti"))
						&& (CapturedGroupNames.contains("COUNTERPART : NA")
								|| CapturedGroupNames.contains("COMPANION : NA")
								|| CapturedGroupNames.contains("OUT OF SEQUENCE : Secondary")
								|| CapturedGroupNames.contains("OUT OF SEQUENCE : Primary"))) {

					MedPolicy = hierarchy_Doccument.getString("medPolicyDesc").trim();
					DPkey = hierarchy_Doccument.getLong("dpKey");
					TopicName = hierarchy_Doccument.getString("topicDesc").trim();
					PayerShort = ID_Doccument.getString("payerShort").trim();
					DataFoundinDB = true;
					break;
				}
			}

			else if (reqRelationship.equalsIgnoreCase("AnyRuleRelationship")) {
				if (CapturedGroupNames.contains("MUTUALLY EXCLUSIVE : CMS Only")
						|| CapturedGroupNames.contains("MUTUALLY EXCLUSIVE : CMS + Cotiviti")
						|| (CapturedGroupNames.contains("COUNTERPART : NA")
								|| CapturedGroupNames.contains("COMPANION : NA")
								|| CapturedGroupNames.contains("OUT OF SEQUENCE : Secondary")
								|| CapturedGroupNames.contains("OUT OF SEQUENCE : Primary"))) {
					MedPolicy = hierarchy_Doccument.getString("medPolicyDesc").trim();
					DPkey = hierarchy_Doccument.getLong("dpKey");
					DPKeysList.add(DPkey.toString());
					TopicName = hierarchy_Doccument.getString("topicDesc").trim();
					PayerShort = ID_Doccument.getString("payerShort").trim();
					DataFoundinDB = true;
					DPPayershortDetails.add(MedPolicy);
					DPPayershortDetails.add(PayerShort);
					DPPayershortDetails.add(TopicName);
					DPKeyDetails.put(DPkey.toString(), DPPayershortDetails);

				}
			}
		}
		if (DataFoundinDB == false) {
			Assert.assertTrue(
					"There are no required data in the MongoDB result  for provided criteria::" + reqRelationship,
					false);
		}

		Serenity.setSessionVariable("DPKeyList").to(DPKeysList);
		Serenity.setSessionVariable("DPKeyDetails").to(DPKeyDetails);
		Serenity.setSessionVariable("Topic").to(TopicName);
		Serenity.setSessionVariable("Payershort").to(PayerShort);
		Serenity.setSessionVariable("DPKey").to(DPKeysList);

	}

	public static ArrayList<String> getMongoMonthlyColData(String sCollection, String sfields, String sPrimaryKey) {

		ArrayList<String> resultList = new ArrayList<String>();

		ArrayList<Long> iKeyValues = new ArrayList<Long>();

		String sKeyVal = null;

		retrieveAllDocuments("cpd", sCollection);
		Bson MatchFilter = new BsonDocument();

		for (Document rs : results) {

			String sColumnValue = "";
			String result = "";
			Document Ioccument = null;

			/*
			 * try{ sKeyVal = rs.get(sPrimaryKey).toString(); }catch (Exception
			 * e){ sKeyVal = null; }
			 */

			String[] sColumnList = sfields.split(";");

			for (int i = 0; i < sColumnList.length; i++) {

				try {
					sColumnValue = rs.get(sColumnList[i]).toString();
				} catch (Exception e) {
					sColumnValue = null;

				}

				result = result + sColumnList[i] + ":" + sColumnValue;

			}

			System.out.println(result);

			// iKeyValues.add(Long.valueOf(sKeyVal));

			resultList.add(result);

		}

		// Serenity.setSessionVariable("sKeyValues").to(iKeyValues);

		return resultList;
	}

	public static ArrayList<String> getMongoOpptyColData(String sCollection, String sfields) {

		ArrayList<String> resultList = new ArrayList<String>();

		ArrayList<Integer> AllMidRules = new ArrayList<Integer>();

		AggregateIterable<Document> output = null;

		String sColumnValue = null;
		retrieveAllDocuments("cpd", "oppty");
		Bson MatchFilter = new BsonDocument();

		switch (sCollection.toUpperCase()) {

		case "ELL_OT_DP":
			output = mColl.aggregate(Arrays.asList(Aggregates.group(new Document()
					.append("DP_KEY", "$subRule.hierarchy.dpKey").append("TOPIC_KEY", "$subRule.hierarchy.topicKey")
					.append("DP_DESC", "$subRule.hierarchy.dpDesc")
					.append("DP_SORT_ORDER", "$subRule.hierarchy.dpSortOrder"))));
			break;

		case "ELL_OT_MP":
			output = mColl.aggregate(Arrays.asList(
					Aggregates.group(new Document().append("MED_POL_KEY", "$subRule.hierarchy.medPolicyKey")
							.append("MP_SORT_ORDER", "$subRule.hierarchy.mpSortOrder").append("MP_TITLE",
									"$subRule.hierarchy.medPolicyDesc"))));
			break;

		case "ELL_OT_TOPIC":
			output = mColl.aggregate(
					Arrays.asList(Aggregates.group(new Document().append("TOPIC_KEY", "$subRule.hierarchy.topicKey")
							.append("MED_POL_KEY", "$subRule.hierarchy.medPolicyKey")
							.append("TOPIC_SORT_ORDER", "$subRule.hierarchy.topicSortOrder")
							.append("TOPIC_TITLE", "$subRule.hierarchy.topicDesc"))));
			break;
		}

		String[] sColumnList = sfields.split(";");

		for (Document rsd : output) {

			Document rs = rsd.get("_id", Document.class);
			String result = "";

			for (int i = 0; i < sColumnList.length; i++) {

				try {
					sColumnValue = rs.get(sColumnList[i]).toString();
				} catch (Exception e) {
					sColumnValue = null;
				}

				result = result + sColumnList[i] + ":" + sColumnValue;

			}

			resultList.add(result);

			System.out.println(result);

		}

		return resultList;

	}

	public static ArrayList<JsonObject> getMongoSwitchColData(String sClientKey, String s, String sPrimaryKey) {

		ArrayList<JsonObject> resultList = new ArrayList<JsonObject>();

		ArrayList<Long> iKeyValues = new ArrayList<Long>();

		String sKeyVal = null;

		retrieveAllDocuments("cpd", "monthly_ot_midrule");

		Bson filterMatchFilter = Filters.and(Filters.eq("CLIENT_KEY", Integer.valueOf(sClientKey)),
				Filters.ne("SWITCH_INFO", null));
		results = mColl.find(filterMatchFilter);
		recordsCount = mColl.count(filterMatchFilter);
		System.out.println("Filtered_Count:" + recordsCount);

		Bson MatchFilter = new BsonDocument();

		for (Document rs : results) {

			JsonObject sColumnValue = null;
			String result = "";
			Document Ioccument = null;

			Object object = null;
			JSONArray arrayObj = null;
			JSONParser jsonParser = new JSONParser();

			try {
				String ColumnValue = rs.get("SWITCH_INFO").toString();
				object = jsonParser.parse(ColumnValue);
				JsonArray jsonArray = new JsonParser().parse(ColumnValue).getAsJsonArray();

				for (int i = 0; i <= jsonArray.size(); i++) {

					sColumnValue = (JsonObject) jsonArray.get(i);

					sKeyVal = sColumnValue.get("rule").toString().split("\\.")[0];

					// sKeyVal = sColumnValue.get("subRuleKey").toString();

					/*
					 * System.out.println(sColumnValue.get("prod_10"));
					 * System.out.println(sColumnValue.get("test_10"));
					 * System.out.println(sColumnValue.get("subRuleKey"));
					 * System.out.println(sColumnValue.get("payerKey"));
					 * System.out.println(sColumnValue.get("insuranceKey"));
					 * System.out.println(sColumnValue.get("claimType"));
					 */

					resultList.add(sColumnValue);

					iKeyValues.add(Long.valueOf(sKeyVal));

					System.out.println(sColumnValue);
				}

			} catch (Exception e) {
				sColumnValue = null;

			}
		}

		Serenity.setSessionVariable("sKeyValues").to(iKeyValues);

		return resultList;

	}

	public static ArrayList<String> ValMongoSwitchinOppty(String sClientKey, ArrayList<JsonObject> sLstValues) {

		ArrayList<String> resultList = new ArrayList<String>();

		ArrayList<Long> iKeyValues = new ArrayList<Long>();

		String sKeyVal = null;

		retrieveAllDocuments("cpd", "oppty");
		String sProd;
		String sTest;
		String sSubRuleKey = null;
		String sPayerKey = null;
		String sInsuranceKey = null;
		String sClaimType = null;

		ArrayList<Integer> sSubRules = Serenity.sessionVariableCalled("sKeyValues");
		// Bson filterMatchFilter = Filters.and(Filters.eq("_id.subRuleKey",
		// Long.valueOf(sSubRuleKey)),Filters.eq("_id.clientKey",
		// Long.valueOf(sClientKey)),Filters.eq("_id.payerKey",
		// Integer.valueOf(sPayerKey)),Filters.eq("_id.insuranceKey",
		// Integer.valueOf(sInsuranceKey)),Filters.eq("_id.claimType",
		// sClaimType));
		Bson filterMatchFilter = Filters.and(Filters.eq("_id.clientKey", Integer.valueOf(sClientKey)),
				Filters.in("subRule.hierarchy.midRuleKey", sSubRules));
		results = mColl.find(filterMatchFilter);
		recordsCount = mColl.count(filterMatchFilter);
		System.out.println("Filtered_Count:" + recordsCount);

		ArrayList<String> sMidPPS = Serenity.sessionVariableCalled("sPayerKeys");

		for (Document rs : results) {

			String result = "";
			String strProd = "";
			String strtest = "";
			String PPS = "";
			Document rsid = rs.get("_id", Document.class);

			String strPayerKey = rsid.get("payerKey").toString().trim();

			String strClientKey = rsid.get("clientKey").toString().trim();
			String strInsKey = rsid.get("insuranceKey").toString().trim();

			String strSubRuleKey = rsid.get("subRuleKey").toString().trim();
			String strClaimType = rsid.get("claimType").toString().trim();
			Document Subrule_Doccument = rs.get("subRule", Document.class);
			Document hierarchy_Doccument = Subrule_Doccument.get("hierarchy", Document.class);

			String sMidRule = hierarchy_Doccument.get("midRuleKey").toString().trim();

			PPS = "Client Key:" + strClientKey + ";Insurance Key:" + strInsKey + ";Payer Key:" + strPayerKey
					+ ";Sub Rule Key:" + strSubRuleKey + ";Claim Type:" + strClaimType + ";Mid Rule:" + sMidRule;
			if (sMidPPS.contains(PPS.trim())) {

				try {
					strProd = rs.get("prod_10").toString();
				} catch (Exception e) {
					strProd = null;
				}

				try {
					strtest = rs.get("test_10").toString();
				} catch (Exception e) {
					strtest = null;
				}

				result = "Client Key:" + strClientKey + ";Insurance Key:" + strInsKey + ";Payer Key:" + strPayerKey
						+ ";Sub Rule Key:" + strSubRuleKey + ";Claim Type:" + strClaimType + ";Prod 10:" + strProd
						+ ";Test 10:" + strtest + ";Mid Rule:" + sMidRule;

				resultList.add(result);

				System.out.println(result);
			}
		}

		return resultList;

	}

	public void getRuleRelationshipPopupDetails(String RequiredVal, String dPKey, String sPresProfileName) {

		String RuleID = "";
		String RuleSavingStatus = "";

		db = mongoClient.getDatabase("cpd");
		mColl = db.getCollection("oppty");
		cursor = mColl.find().iterator();
		Bson MatchFilter = new BsonDocument();

		// Get the ClientKey from the Session variable
		Long ClientKey = Serenity.sessionVariableCalled("ClientKey");
		String DPkey = Serenity.sessionVariableCalled("DPKey");
		String MedPolicy = Serenity.sessionVariableCalled("MedicalPolicy");
		String Payershort = Serenity.sessionVariableCalled("PayerShort");
		String TopicName = Serenity.sessionVariableCalled("TopicName");
		String InsuranceDesc = Serenity.sessionVariableCalled("InsuranceDesc");
		String SubRuleID = Serenity.sessionVariableCalled("SubRule");
		String ClaimType = Serenity.sessionVariableCalled("ClaimType");

		switch (RequiredVal) {

		case "RuleID":
			MatchFilter = Filters.and(Filters.eq("_id.clientKey", Long.valueOf(ClientKey)),
					Filters.eq("subRule.hierarchy.dpKey", Long.valueOf(dPKey)),
					Filters.eq("_id.payerShort", Payershort), Filters.eq("_id.claimType", "O"),
					Filters.eq("insuranceDesc", InsuranceDesc));
			// Get the RuleID for the PPS combination
			DistinctIterable<String> outputDisp = mColl.distinct("_id.subRuleId", MatchFilter, String.class);
			RuleID = outputDisp.first(); // Retrieve the First Value
			Serenity.setSessionVariable("RuleID").to(RuleID);
			break;

		case "SavingStatus":

			MatchFilter = Filters.and(Filters.eq("_id.clientKey", Long.valueOf(ClientKey)),
					Filters.eq("_id.subRuleId", RuleID), Filters.eq("_id.payerShort", Payershort),
					Filters.eq("_id.claimType", ClaimType), Filters.eq("insuranceDesc", InsuranceDesc));

			// Get the RuleID for the PPS combination
			Distinctresults_with_long = mColl.distinct("ruleInBaseLine", MatchFilter, Long.class);
			Long RuleinBaseLine = Distinctresults_with_long.first(); // Retrieve
			// the
			// First
			// Value

			if (RuleinBaseLine == 0) {
				RuleSavingStatus = "Opportunity";
			} else if (RuleinBaseLine == -1) {
				RuleSavingStatus = "Production";
			}
			Serenity.setSessionVariable("SavingStatus").to(RuleSavingStatus);

			break;

		case "RawProdSaving":
			MatchFilter = Filters.and(Filters.eq("_id.clientKey", ClientKey), Filters.eq("_id.subRuleId", SubRuleID),
					Filters.in("_id.payerShort", Payershort), Filters.in("insuranceDesc", InsuranceDesc),
					Filters.eq("disposition.desc", "Present"));
			Bson matchtext = Aggregates.match(Filters.and(MatchFilter));
			AggregateIterable<Document> output2 = mColl.aggregate(Arrays.asList(matchtext,
					Aggregates.group("$clientDesc", Accumulators.sum("Deck savings", "$annualSavings.raw"))));

			int RawSavings = 0;
			String s = "";

			for (Document Doc : output2) {
				RawSavings = Integer.parseInt(Doc.toString());
				s = Doc.get("Deck savings").toString();
			}

			Serenity.setSessionVariable("RuleRawSavings").to(RawSavings);
			break;

		case "RuleRelatioshipGroupDetails":

			// Get all the MidRuleKeys for the DP and corresponding relations
			HashMap<String, ArrayList<String>> GroupDetails = new HashMap<String, ArrayList<String>>();

			Long DPkey1 = 0L;
			String PayerShort = "";
			String GroupName = "";
			String ActiveRules = "";

			MatchFilter = Filters.and(Filters.eq("_id.clientKey", ClientKey),
					Filters.eq("subRule.hierarchy.dpKey", Serenity.sessionVariableCalled("DPKey")),
					Filters.eq("_id.payerShort", Serenity.sessionVariableCalled("Payershort")),
					Filters.eq("insuranceDesc", Serenity.sessionVariableCalled("InsuranceDesc")),
					Filters.eq("disposition.desc", "Present"));
			results = mColl.find(MatchFilter);

			for (Document document : results) {
				Document ID_Doccument = document.get("_id", Document.class);
				Document Subrule_Doccument = document.get("subRule", Document.class);
				Document hierarchy_Doccument = Subrule_Doccument.get("hierarchy", Document.class);

				ArrayList<String> MidRules = new ArrayList<String>();

				Set<String> RelationsSet = GroupDetails.keySet();
			}
		}
	}

	public static void getValuesForRuleRelationshipForMidRules(String mDBCollectionName) {

		MongoCursor<Long> Itr = null;
		MongoCursor<String> Itr2 = null;
		int Resultsize = 0;

		Map<String, List<String>> MidRulesAssociatedActiveRulesMap_MDB = new HashMap<String, List<String>>();
		Map<String, List<String>> MidRulesAssociatedActiveRulesMap_Oracle = new HashMap<String, List<String>>();
		Map<String, String> MidRulesAssociatedActiveRulesRelationMap = new HashMap<String, String>();
		List<String> AssociatedActiveRules_MDB = null;
		List<String> AssociatedActiveRules_Oracle = new ArrayList<String>();
		;

		// Method to connect mongoDB
		retrieveAllDocuments("cpd", "OPPTY");
		Bson MatchFilter = new BsonDocument();
		List<String> ResultData = new ArrayList<String>();
		int MidRuleKey_Oracle = 0;
		boolean ActiveRulesMatched = false;

		BasicDBObject fields = new BasicDBObject();
		String outputresponse = null;

		List<Long> MidRuleMDB_Lng = new ArrayList<Long>();
		List<String> MidRule_Str = new ArrayList<String>();
		List<String> MidRulesOracle = new ArrayList<String>();
		MidRulesOracle = Serenity.sessionVariableCalled("AllMidRules");
		System.out.println("Total Oracle Mid Rules ::" + MidRulesOracle.size());
		results = mColl.find().projection(Projections.include("_id.midRuleKey"));
		cursor = results.iterator();

		int k = 0;
		while (cursor.hasNext()) {

			String MidRule = StringUtils.substringBetween(cursor.next().toString(), "midRuleKey=", ",");
			MidRuleMDB_Lng.add(Long.valueOf(MidRule.trim()));
			k = k + 1;
		}
		System.out.println("Total MidRules in MongoDB:: " + k);

		boolean flag = false;
		int count = 0;

		for (int j = 0; j < MidRuleMDB_Lng.size(); j++) {
			flag = false;

			Long mdRule = Long.valueOf(MidRuleMDB_Lng.get(j));
			for (int m = 0; m < MidRulesOracle.size(); m++) {
				Long mdRule_Oracle = Long.valueOf(MidRulesOracle.get(m));
				if (mdRule.equals(mdRule_Oracle)) {
					flag = true;
					count = count + 1;
					break;
				}
			}
			// System.out.println("MatchedRuleCount"+count);
			if (flag == false) {
				System.out.println("!!!!!!@@@@MidRule in MDB but not in Oracle::" + mdRule);
			}
		}

		System.out.println("All MidRules in MongoDB are availabale in Oracle ");

	}

	public static boolean validateDPDisposition(String requiredDisposition) {

		retrieveAllDocuments("cpd", "oppty");
		Bson MatchFilter = new BsonDocument();

		String DPKey = Serenity.sessionVariableCalled("DPKey");
		String MedPolicy = Serenity.sessionVariableCalled("MedicalPolicy");
		String PayerShort = Serenity.sessionVariableCalled("PayerShort");
		Long ClientKey = Serenity.sessionVariableCalled("ClientKey");

		HashSet<String> DPKeysToValidate = Serenity.sessionVariableCalled("CapturedDPKeys");
		List<String> DPKeysToValidateList = new ArrayList<String>(DPKeysToValidate);

		boolean DispositionUpdatedAsPresent = false;

		for (int d = 0; d < DPKeysToValidateList.size(); d++) {
			MatchFilter = new BsonDocument();

			MatchFilter = Filters.and(Filters.eq("_id.payerShort", PayerShort),
					Filters.eq("disposition.desc", requiredDisposition),
					Filters.eq("subRule.hierarchy.dpKey", Long.valueOf(DPKeysToValidateList.get(d))),
					Filters.eq("_id.clientKey", ClientKey), Filters.eq("prod_10", null),
					Filters.eq("subRule.libStatusKey", 1), Filters.eq("ruleInBaseLine", 0));
			recordsCount = mColl.count(MatchFilter);

			if (recordsCount == 0) {
				Assert.assertTrue("There are no records on MongoDB for provided query", false);
			} else {
				System.out.println("Records fetched from MongoDB::" + recordsCount);
			}

			Distinctresults = mColl.distinct("disposition.desc", MatchFilter, String.class);
			String DipositioninDB = Distinctresults.first();
			if (requiredDisposition.equalsIgnoreCase(DipositioninDB)) {
				DispositionUpdatedAsPresent = true;
			} else {
				DispositionUpdatedAsPresent = false;
			}

		}

		return DispositionUpdatedAsPresent;

	}

	public static boolean GetAvailableDPKeyWithRelations(String clientName, String Criteria, String dispositionVal,
			String mDBCollectionName) {

		ArrayList<String> ImpactedRules = new ArrayList<String>();

		List<String> rela2 = new ArrayList<String>();
		Set<String> AllDPKeys = new HashSet<String>();
		HashMap<String, ArrayList<String>> ImpactedRulesforDPKey = new HashMap<String, ArrayList<String>>();

		String DPKey = Serenity.sessionVariableCalled("DPKey");
		String MedPolicy = Serenity.sessionVariableCalled("MedicalPolicy");
		String PayerShort = Serenity.sessionVariableCalled("PayerShort");

		retrieveAllDocuments("cpd", mDBCollectionName);
		Bson MatchFilter = new BsonDocument();
		Bson MatchFilter2 = new BsonDocument();

		ArrayList<String> DispositionreasonsList = null;
		Long ClientKey = Serenity.sessionVariableCalled("ClientKey");

		MatchFilter = Filters.and(Filters.eq("_id.payerShort", PayerShort),
				Filters.eq("disposition.desc", dispositionVal),
				Filters.eq("subRule.hierarchy.dpKey", Long.valueOf(DPKey)), Filters.eq("_id.clientKey", ClientKey),
				Filters.eq("prod_10", null), Filters.eq("subRule.libStatusKey", 1), Filters.eq("ruleInBaseLine", 0));
		recordsCount = mColl.count(MatchFilter);

		if (recordsCount == 0) {
			Assert.assertTrue("There are no records on MongoDB for provided query", false);
		} else {
			System.out.println("Records fetched from MongoDB::" + recordsCount);
		}

		Distinctresults_with_long = mColl.distinct("subRule.ruleRelations.activeRules", MatchFilter, Long.class);
		MongoCursor<Long> Itr1 = Distinctresults_with_long.iterator();
		while (Itr1.hasNext()) {
			String Impactedrules = Itr1.next().toString();
			ImpactedRules.add(Impactedrules);
			System.out.println(Impactedrules);
		}

		Long ImpactedRule = 0L;
		String DPKey2 = "";

		boolean AssociatedRuleDPkeyExists = false;

		// Store the Main DP also for which disposition taken as "Present"
		AllDPKeys.add(DPKey.toString());

		for (int k = 0; k < ImpactedRules.size(); k++) {
			Distinctresults_with_long = null;
			ImpactedRule = Long.valueOf(ImpactedRules.get(k));
			MatchFilter2 = Filters.and(Filters.eq("_id.payerShort", PayerShort), Filters.eq("_id.clientKey", ClientKey),
					Filters.eq("subRule.hierarchy.midRuleKey", ImpactedRule),
					Filters.eq("disposition.desc", dispositionVal), Filters.eq("prod_10", null),
					Filters.eq("subRule.libStatusKey", 1), Filters.eq("ruleInBaseLine", 0));
			Distinctresults_with_long = mColl.distinct("subRule.hierarchy.dpKey", MatchFilter2, Long.class);

			MongoCursor<Long> Itr2 = Distinctresults_with_long.iterator();

			if (!Itr2.hasNext()) {
				System.out.println("******@@There is no PPS combination for Impacted Rule(MidRule)::" + ImpactedRule
						+ " in MongoDB");
			} else {
				while (Itr2.hasNext()) {
					AllDPKeys.add(Itr2.next().toString());
					AssociatedRuleDPkeyExists = true;
				}

			}
		} // end for

		Serenity.setSessionVariable("CapturedDPKeys").to(AllDPKeys);
		return AssociatedRuleDPkeyExists;

	}

	public static void getRuleRelationshipDetailsForWarningMessageValidation(String dPKeyCount, String valToRetrieve,
			String RuleRelationshipCombination, String ClientKey) {

		// Method to connect mongoDB
		retrieveAllDocuments("cpd", "oppty");
		Bson MatchFilter = new BsonDocument();
		List<Document> GroupNames = new ArrayList<Document>();
		ArrayList<String> CapturedGroupNames = new ArrayList<String>();
		boolean DataFoundinDB = false;

		String RelationShipRegex = "";

		switch (RuleRelationshipCombination) {

		case "AnyOneOfRelationships-Companion-Counterpart-OutofSequence":
			RelationShipRegex = "COUNTERPART|COMPANION|OUT OF SEQUENCE";

			break;

		case "OnlyMutuallyExclusiveRelationship":
			RelationShipRegex = "MUTUALLY EXCLUSIVE";
			break;

		case "MutuallyExclusiveRelationshipWithAnyOtherRelation":
			RelationShipRegex = "COUNTERPART|COMPANION|OUT OF SEQUENCE|MUTUALLY EXCLUSIVE";

			break;

		default:
			System.out.println("No input provided for Switch case");

		}

		MatchFilter = Filters.and(Filters.eq("disposition.desc", "No Disposition"),
				Filters.eq("_id.clientKey", Long.valueOf(ClientKey)), Filters.eq("prod_10", null),
				Filters.eq("subRule.ruleRelations.groupName",
						java.util.regex.Pattern.compile("^(" + RelationShipRegex + ")")),
				Filters.eq("subRule.libStatusKey", 1), Filters.ne("annualSavings.lines", 0),
				Filters.eq("ruleInBaseLine", 0));

		results = mColl.find(MatchFilter);
		Iterator itr = results.iterator();
		int RecordsCount = 0;
		while (itr.hasNext()) {
			RecordsCount = RecordsCount + 1;
			itr.next();
		}

		if (RecordsCount == 0) {
			Assert.assertTrue("There are no records on MongoDB for provided query", false);
		} else {
			System.out.println("Records fetched from MongoDB::" + RecordsCount);
		}
		Long DPkey = 0L;
		String PayerShort = "";
		String MedPolicy = "";
		String GroupName = "";
		String ActiveRules = "";
		String TopicName = "";

		for (Document document : results) {
			Document ID_Doccument = document.get("_id", Document.class);
			Document Subrule_Doccument = document.get("subRule", Document.class);
			Document hierarchy_Doccument = Subrule_Doccument.get("hierarchy", Document.class);
			GroupNames = Subrule_Doccument.get("ruleRelations", List.class);
			CapturedGroupNames = new ArrayList<String>();
			for (Document d : GroupNames) {
				System.out.println("GroupName: " + d.getString("groupName"));
				// System.out.println("ActiveRules: " + d.get("activeRules"));
				GroupName = d.getString("groupName");
				CapturedGroupNames.add(GroupName);
			}

			if (RuleRelationshipCombination.equalsIgnoreCase("OnlyMutuallyExclusiveRelationship")) {
				if (CapturedGroupNames.contains("COUNTERPART : NA") || CapturedGroupNames.contains("COMPANION : NA")
						|| CapturedGroupNames.contains("OUT OF SEQUENCE : Secondary")
						|| CapturedGroupNames.contains("OUT OF SEQUENCE : Primary")) {
					System.out.println("Only ME not there");
					CapturedGroupNames = null;
				} else if (CapturedGroupNames.contains("MUTUALLY EXCLUSIVE : CMS Only")
						|| CapturedGroupNames.contains("MUTUALLY EXCLUSIVE : CMS + Cotiviti")) {

					MedPolicy = hierarchy_Doccument.getString("medPolicyDesc").trim();
					DPkey = hierarchy_Doccument.getLong("dpKey");
					TopicName = hierarchy_Doccument.getString("topicDesc").trim();
					PayerShort = ID_Doccument.getString("payerShort").trim();
					DataFoundinDB = true;
					break;
				}
			}

			else if (RuleRelationshipCombination
					.equalsIgnoreCase("AnyOneOfRelationships-Companion-Counterpart-OutofSequence")) {
				if (CapturedGroupNames.contains("MUTUALLY EXCLUSIVE : CMS Only")
						|| CapturedGroupNames.contains("MUTUALLY EXCLUSIVE : CMS + Cotiviti")) {
					System.out.println("Only ME is there,so not valid");
					CapturedGroupNames = null;
				} else {
					MedPolicy = hierarchy_Doccument.getString("medPolicyDesc").trim();
					DPkey = hierarchy_Doccument.getLong("dpKey");
					TopicName = hierarchy_Doccument.getString("topicDesc").trim();
					PayerShort = ID_Doccument.getString("payerShort").trim();
					DataFoundinDB = true;
					break;
				}
			}

			else if (RuleRelationshipCombination
					.equalsIgnoreCase("MutuallyExclusiveRelationshipWithAnyOtherRelation")) {

				if (CapturedGroupNames.size() >= 2) {
					if ((CapturedGroupNames.contains("MUTUALLY EXCLUSIVE : CMS Only")
							|| CapturedGroupNames.contains("MUTUALLY EXCLUSIVE : CMS + Cotiviti"))
							&& (CapturedGroupNames.contains("COUNTERPART : NA")
									|| CapturedGroupNames.contains("COMPANION : NA")
									|| CapturedGroupNames.contains("OUT OF SEQUENCE : Secondary")
									|| CapturedGroupNames.contains("OUT OF SEQUENCE : Primary"))) {

						MedPolicy = hierarchy_Doccument.getString("medPolicyDesc").trim();
						DPkey = hierarchy_Doccument.getLong("dpKey");
						TopicName = hierarchy_Doccument.getString("topicDesc").trim();
						PayerShort = ID_Doccument.getString("payerShort").trim();
						DataFoundinDB = true;
						break;
					}
				}
			}

		}

		if (DataFoundinDB == false) {
			Assert.assertTrue("There are no required data in the MongoDB result  for provided criteria::"
					+ RuleRelationshipCombination, false);
		}

		// CapturePayershort also
		Serenity.setSessionVariable("DPKey").to(DPkey);
		Serenity.setSessionVariable("MedicalPolicy").to(MedPolicy);
		Serenity.setSessionVariable("PayerShort").to(PayerShort);
		Serenity.setSessionVariable("TopicName").to(TopicName);

		System.out.println(DPkey);
		System.out.println(MedPolicy);
		System.out.println(PayerShort);
		System.out.println(TopicName);

	}

	public static List<String> validateMonthlyReleaseCollection(String ValidationCiteria, String valuetoRetrieve) {

		MongoCursor<Long> Itr = null;
		MongoCursor<String> Itr2 = null;
		int Resultsize = 0;

		Map<String, List<String>> MidRulesAssociatedActiveRulesMap_MDB = new HashMap<String, List<String>>();
		Map<String, List<String>> MidRulesAssociatedActiveRulesMap_Oracle = new HashMap<String, List<String>>();
		Map<String, String> MidRulesAssociatedActiveRulesRelationMap = new HashMap<String, String>();
		List<String> AssociatedActiveRules_MDB = null;
		List<String> AssociatedActiveRules_Oracle = new ArrayList<String>();
		;

		// Method to connect mongoDB
		retrieveAllDocuments("cpd", "monthlyRelease_onetime");
		Bson MatchFilter = new BsonDocument();
		List<String> ResultData = new ArrayList<String>();
		int MidRuleKey_Oracle = 0;
		boolean ActiveRulesMatched = false;

		switch (ValidationCiteria) {

		case "RuleRelationshipsValidationfromMDBtoOracle":
			HashMap<String, String> AssociatedActiveRulesRelationMapMDB = new HashMap<String, String>();
			HashMap<String, String> AssociatedActiveRulesRelationMapOracle = new HashMap<String, String>();
			ArrayList<String> RuleRelationActiveRule = new ArrayList<String>();
			ArrayList<String> RuleRelationInactiveRule = new ArrayList<String>();
			HashMap<String, ArrayList<String>> RelationMapMDB = new HashMap<String, ArrayList<String>>();
			HashMap<String, ArrayList<String>> InactiveRelationMapMDB = new HashMap<String, ArrayList<String>>();
			HashMap<String, ArrayList<String>> MidruleRelationMapOracle = null;
			HashMap<String, ArrayList<String>> MidruleInactiveRelationMapOracle = null;
			List<String> RuleRelationActiveRulesOracle = new ArrayList<String>();
			List<String> RuleRelationInactiveRulesOracle = new ArrayList<String>();
			String GroupCat = "";
			int ActiveRuleRelationCount = 0;
			int InactiveRuleRelationCount = 0;

			List<Long> midrules = null;
			List<Object> GrpList = new ArrayList<Object>();
			Object[] relation = new Object[10];
			List<String> rela2 = new ArrayList<String>();

			MidruleRelationMapOracle = Serenity.sessionVariableCalled("MidRulesRelationMapping");
			MidruleInactiveRelationMapOracle = Serenity.sessionVariableCalled("MidRulesInactiveRelationMapping");

			MidRulesAssociatedActiveRulesMap_Oracle = Serenity
					.sessionVariableCalled("MidRulesActiveAndInactiveRulesMapOracle");
			for (String key : MidRulesAssociatedActiveRulesMap_Oracle.keySet()) {
				ActiveRuleRelationCount = 0;
				InactiveRuleRelationCount = 0;
				MidRuleKey_Oracle = Integer.parseInt(key);

				midrules = new ArrayList<Long>();
				midrules.add(Long.valueOf(MidRuleKey_Oracle));

				MatchFilter = Filters.in("_id.midRuleKey", midrules);
				results = mColl.find(MatchFilter);
				recordsCount = mColl.count(MatchFilter);

				Bson matchtext = Aggregates.match(Filters.and(MatchFilter));
				AggregateIterable<Document> sValues = null;

				sValues = mColl.aggregate(Arrays.asList(matchtext,
						Aggregates.group("$_id", Accumulators.addToSet("Relations", "$ruleRelations"))));
				rela2 = new ArrayList<String>();

				for (Document Doc : sValues) {

					String Relns = StringUtils.substringBetween(Doc.toString(), "Relations=[[", "}}]]");
					rela2 = Arrays.asList(Relns.split("}},"));

					break;
				}

				RuleRelationActiveRule = new ArrayList<String>();
				RelationMapMDB = new HashMap<String, ArrayList<String>>();

				String RuleTemp = "";
				AssociatedActiveRulesRelationMapMDB = new HashMap<String, String>();

				if (!(rela2.isEmpty())) {
					for (int j = 0; j < rela2.size(); j++) {
						RuleRelationActiveRule = new ArrayList<String>();
						RuleRelationInactiveRule = new ArrayList<String>();
						RelationMapMDB = new HashMap<String, ArrayList<String>>();
						InactiveRelationMapMDB = new HashMap<String, ArrayList<String>>();

						String GrpName = StringUtils.substringBetween(rela2.get(j), "groupName=", ",");

						if (!rela2.get(j).contains("inActiveRules")) {
							ActiveRuleRelationCount = ActiveRuleRelationCount + 1;

							RuleTemp = StringUtils.substringBetween(rela2.get(j), "activeRules=[", "]").trim();

							if (RuleTemp.contains(",")) {
								// New Code
								String[] AllRules = RuleTemp.split(",");
								for (int n = 0; n < AllRules.length; n++) {
									RuleRelationActiveRule.add(AllRules[n].trim());
								}
							}

							else {
								RuleRelationActiveRule.add(RuleTemp.trim());
							}
							RelationMapMDB.put(GrpName, RuleRelationActiveRule);
						} else {
							InactiveRuleRelationCount = InactiveRuleRelationCount + 1;
							RuleTemp = StringUtils.substringBetween(rela2.get(j), "inActiveRules=[", "]").trim();
							if (RuleTemp.contains(",")) {
								String[] AllRules = RuleTemp.split(",");
								for (int n = 0; n < AllRules.length; n++) {
									RuleRelationInactiveRule.add(AllRules[n].trim());
								}
							} else {
								RuleRelationInactiveRule.add(RuleTemp.trim());
							}
							InactiveRelationMapMDB.put(GrpName, RuleRelationInactiveRule);
						}

						// } //End of for loop for different string in the
						// Documents

						// Cheking for Active and Inactive Rules
						ArrayList<String> GropupNames = null;
						// @SuppressWarnings("unchecked")
						if (!(RelationMapMDB.isEmpty())) {
							GropupNames = new ArrayList<String>(RelationMapMDB.keySet());
						}

						if (!(InactiveRelationMapMDB.isEmpty())) {
							GropupNames = new ArrayList<String>(InactiveRelationMapMDB.keySet());
						}

						for (int s = 0; s < GropupNames.size(); s++) {
							// Get Value from MDB Map ,Get Value from Oracle
							// Map,Compare both array lists

							switch (GropupNames.get(s).trim()) {
							case "OUT OF SEQUENCE : Primary":
								GroupCat = "A";
								break;

							case "OUT OF SEQUENCE : Secondary":
								GroupCat = "B";
								break;

							case "COUNTERPART : NA":
								GroupCat = "C";
								break;

							case "MUTUALLY EXCLUSIVE : CMS Only":
								GroupCat = "D";
								break;

							case "COMPANION : NA":
								GroupCat = "E";
								break;

							case "MUTUALLY EXCLUSIVE : CMS + Cotiviti":
								GroupCat = "F";
								break;
							}

							if (!(RelationMapMDB.isEmpty())) {
								RuleRelationActiveRulesOracle = MidruleRelationMapOracle
										.get(String.valueOf(MidRuleKey_Oracle) + ":" + GroupCat);
								List<String> RuleRelationActiveRulesMDB = RelationMapMDB.get(GropupNames.get(s));

								if ((RuleRelationActiveRulesMDB == null) || (RuleRelationActiveRulesOracle == null)) {
									System.out.println(
											"Object is  NULL-->$$$$$$RuleRelations Not Matched from Oracle to MDB for MidRule::"
													+ MidRuleKey_Oracle + "and ActiveRule"
													+ RuleRelationActiveRulesMDB.get(0));
								}

								else {

									// Validating the size
									if (RuleRelationActiveRulesOracle.size() == RuleRelationActiveRulesMDB.size()) {
										Assert.assertTrue(
												"ActiveRules RuleRelations count Matched from Oracle to MDB for MidRule::"
														+ MidRuleKey_Oracle,
														true);
									} else {
										Assert.assertTrue(
												"ActiveRules RuleRelations count Not Matched from Oracle to MDB for MidRule::"
														+ MidRuleKey_Oracle,
														false);
									}

									if (RuleRelationActiveRulesOracle.equals(RuleRelationActiveRulesMDB)) {

										Assert.assertTrue("RuleRelations Matched from Oracle to MDB for MidRule::"
												+ MidRuleKey_Oracle, true);
									} else {
										System.out.println("RuleRelations Not Matched from Oracle to MDB for MidRule::"
												+ MidRuleKey_Oracle + "and ActiveRule"
												+ RuleRelationActiveRulesMDB.get(0));
										Assert.assertTrue("RuleRelations Not Matched from Oracle to MDB for MidRule::"
												+ MidRuleKey_Oracle + "and ActiveRule"
												+ RuleRelationActiveRulesMDB.get(0), false);
									}
								} // end of else
							}
							if (!(InactiveRelationMapMDB.isEmpty())) {

								RuleRelationInactiveRulesOracle = MidruleInactiveRelationMapOracle
										.get(String.valueOf(MidRuleKey_Oracle) + ":" + GroupCat);
								List<String> RuleRelatioInactiveRulesMDB = InactiveRelationMapMDB
										.get(GropupNames.get(s));
								if ((RuleRelatioInactiveRulesMDB == null)
										|| (RuleRelationInactiveRulesOracle == null)) {
									System.out.println(
											"Object is  NULL-->$$$$$$Inactive RuleRelations Not Matched from Oracle to MDB for MidRule::"
													+ MidRuleKey_Oracle + "and ActiveRule"
													+ RuleRelatioInactiveRulesMDB.get(0));
								}

								else {
									// Validating the size
									if (RuleRelationInactiveRulesOracle.size() == RuleRelatioInactiveRulesMDB.size()) {
										Assert.assertTrue(
												"InactiveRules RuleRelations count Matched from Oracle to MDB for MidRule::"
														+ MidRuleKey_Oracle,
														true);
									} else {
										Assert.assertTrue(
												"InactiveRules RuleRelations count Not Matched from Oracle to MDB for MidRule::"
														+ MidRuleKey_Oracle,
														false);
									}
									if (RuleRelationInactiveRulesOracle.equals(RuleRelatioInactiveRulesMDB)) {
										System.out.println(
												"Inactive RuleRelations Matched from Oracle to MDB for MidRule::"
														+ MidRuleKey_Oracle);
										Assert.assertTrue(
												"Inactive RuleRelations Matched from Oracle to MDB for MidRule::"
														+ MidRuleKey_Oracle,
														true);
									} else {
										System.out.println(
												"Inactive RuleRelations Not Matched from Oracle to MDB for MidRule::"
														+ MidRuleKey_Oracle + "and ActiveRule"
														+ RuleRelatioInactiveRulesMDB.get(0));
										Assert.assertTrue(
												"Inactive RuleRelations Not Matched from Oracle to MDB for MidRule::"
														+ MidRuleKey_Oracle + "and ActiveRule"
														+ RuleRelatioInactiveRulesMDB.get(0),
														false);
									}
								} // end of else

							}
						} // End of GroupNames For loop

					} // End of for loop for different string in the Documents

				} else {
					if ((RelationMapMDB.isEmpty()) && (InactiveRelationMapMDB.isEmpty())) {
						System.out.println("@@@@##RuleRelations are there in Oracle but not in MDB for MidRule::"
								+ MidRuleKey_Oracle);
					} else {
						RuleRelationActiveRulesOracle = MidruleRelationMapOracle
								.get(String.valueOf(MidRuleKey_Oracle) + ":" + GroupCat);
						RuleRelationInactiveRulesOracle = MidruleInactiveRelationMapOracle
								.get(String.valueOf(MidRuleKey_Oracle) + ":" + GroupCat);
						if ((RuleRelationActiveRulesOracle.isEmpty()) && (RuleRelationInactiveRulesOracle.isEmpty())) {
							System.out.println("@@@@##RuleRelations are there in MDB but not in Oracle for MidRule::"
									+ MidRuleKey_Oracle);
						}

					}
				}

				// System.out.println("Total Active RuleRelations for
				// MidRule::"+MidRuleKey_Oracle+"are::"+ActiveRuleRelationCount
				// );
				// System.out.println("Total Inactive RuleRelations for
				// MidRule::"+MidRuleKey_Oracle+"are::"+InactiveRuleRelationCount
				// );

			} // End Oracle All MidRules For loop

			break;

		case "MidRulesCountValidationMDBtoOracle":

			BasicDBObject fields = new BasicDBObject();
			String outputresponse = null;

			List<Long> MidRuleMDB_Lng = new ArrayList<Long>();
			List<String> MidRule_Str = new ArrayList<String>();
			List<String> MidRulesOracle = new ArrayList<String>();
			MidRulesOracle = Serenity.sessionVariableCalled("AllMidRules");
			System.out.println("Total Oracle Mid Rules ::" + MidRulesOracle.size());
			results = mColl.find().projection(Projections.include("_id.midRuleKey"));
			cursor = results.iterator();

			int k = 0;
			while (cursor.hasNext()) {

				String MidRule = StringUtils.substringBetween(cursor.next().toString(), "midRuleKey=", ",");
				MidRuleMDB_Lng.add(Long.valueOf(MidRule.trim()));
				k = k + 1;
			}
			System.out.println("Total MidRules in MongoDB:: " + k);

			if (MidRulesOracle.size() == k) {
				Assert.assertTrue("Total MidRules in MongoDB matched with Oracle::", true);
			} else {
				System.out.println("****Total MidRules in MongoDB not matched with Oracle::" + "Oracle Count::"
						+ MidRulesOracle.size() + "MDB Count::" + k);
				Assert.assertTrue("Total MidRules in MongoDB not matched with Oracle::", false);
			}

			boolean flag = false;
			int count = 0;

			for (int j = 0; j < MidRuleMDB_Lng.size(); j++) {
				flag = false;

				Long mdRule = Long.valueOf(MidRuleMDB_Lng.get(j));
				for (int m = 0; m < MidRulesOracle.size(); m++) {
					Long mdRule_Oracle = Long.valueOf(MidRulesOracle.get(m));
					if (mdRule.equals(mdRule_Oracle)) {
						flag = true;
						count = count + 1;
						break;
					}
				}

				if (flag == false) {
					System.out.println("!!!!!!@@@@MidRule in MDB but not in Oracle::" + mdRule);
				}
			}

			flag = false;
			count = 0;

			for (int j = 0; j < MidRulesOracle.size(); j++) {
				flag = false;

				Long mdRule = Long.valueOf(MidRulesOracle.get(j));
				for (int m = 0; m < MidRuleMDB_Lng.size(); m++) {
					Long mdRule_MidRuleMongo = Long.valueOf(MidRuleMDB_Lng.get(m));
					if (mdRule.equals(mdRule_MidRuleMongo)) {
						flag = true;
						count = count + 1;
						break;
					}
				}

				if (flag == false) {
					System.out.println("!!!!!!@@@@MidRule in Oracle but not in MDB::" + mdRule);
				}
			}

			break;

		}
		return ResultData;

	}

	public static ArrayList<Long> checkForMidRuleKeyForNewVersionCreation()
	{
		ArrayList<Long> sValues =new ArrayList<Long>();
		retrieveAllDocuments("cpd", "ellData");
		results = mColl.find();
		recordsCount = mColl.count();
		System.out.println("Count of Records "+recordsCount);
		Distinctresults_with_long  = mColl.distinct("_id.midRuleKey", Filters.and(Filters.eq("ellChange", "New Version"),Filters.eq("releaseLogKey.previous", 1859),Filters.eq("releaseLogKey.current", 2005)),Long.class);
		for (Long document : Distinctresults_with_long) 
		{
			sValues.add(document);
		}

		//System.out.println(sValues);
		System.out.println(sValues.size());
		return sValues;
	}
	
	public static boolean checkForClientKeyDetailsForMidRuleKey(Long midRuleKey,String ellChange) throws Exception 
	{ 
		boolean bStatus=false;
		int unAvailableMidRules=0;
		
		Bson MatchFilter = new BsonDocument();
		MatchFilter = Filters.and(Filters.eq("_id.midRuleKey", midRuleKey),
				Filters.eq("ellChange", ellChange));
		retrieveAllDocuments("cpd", "ellData",MatchFilter);
		if( recordsCount ==0)
		{
			Assert.assertTrue("There are no records on MongoDB for provided query",false);    
		}
		for (Document document : results) 
		{
			//For 3808 Story
			Document dpKeyC=document.get("dpKey", Document.class);
			Long dpKeyCurrent=dpKeyC.getLong("current");
			Serenity.setSessionVariable("DPKey").to(dpKeyCurrent);

			Document ruleVersion=document.get("version", Document.class);
			Long previous=ruleVersion.getLong("previous");
			String current=ruleVersion.getLong("current").toString();
			Serenity.setSessionVariable("NewVersion").to(current);
			break;
		}
		retrieveAllDocuments("cpd", "oppty");
		Distinctresults_with_long  = mColl.distinct("_id.clientKey", Filters.eq("_id.subRuleId", java.util.regex.Pattern.compile(String.valueOf(midRuleKey))),Long.class);
		for (Long document : Distinctresults_with_long) 
		{
			unAvailableMidRules++;
			ProjectVariables.Clientkeylist.add(document);
		}
		if(unAvailableMidRules==0)
		{
			ProjectVariables.unAvailableMidRuleKeys.add(String.valueOf(midRuleKey));
			return bStatus;
		}
		else
		{ 
			bStatus=true;
		}

		return bStatus;
	}
	
	public static void getLatestDataVersionFromClientKey(Long clientKey)throws Exception
	{
		String release="";
		ArrayList<String> clientInfo=new ArrayList<String>();
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMM");
		List<Date> releaseDates = new ArrayList<Date>();

		Distinctresults  = mColl.distinct("_id.dataVersion", Filters.eq("_id.clientKey", clientKey),String.class);
		//Data Version latest 
		for(String cDataVersion:Distinctresults)
		{
			release=StringUtils.substringAfter(String.valueOf(cDataVersion),"PMPRD1_").substring(0, 6);
			releaseDates.add(dateFormat.parse(release));
			clientInfo.add(release);
		}
		dateFormat.format(Collections.max(releaseDates));
		Collections.sort(releaseDates);
		String latestDataVersion=dateFormat.format(releaseDates.get(releaseDates.size() - 1));
		System.out.println("latestDataVersion ****************** "+latestDataVersion);
		//Store latest date and clientKey in serenity variables.
		Serenity.setSessionVariable("LatestReleaseDate").to(latestDataVersion);
		Serenity.setSessionVariable("ClientKey").to(clientKey);
	}
	
	public static void checkForPPSCombinationHavingSwitch(Long clientKey,String release,Long midRuleKey,String Subrule,String dpKey) 
	{
		String payerKey=null;
		String claimType=null;
		String InsuranceKeys=null;
		String disposition=null;
		List<String> ClaimtypeList=null;
		AggregateIterable<Document> output=null;
		List<String> PayerKeysList=null; 
		List<String> InsuranceList=null;
		ProjectVariables.PayerKeysList.clear();
		ProjectVariables.CapturedInsuranceList.clear();
		ProjectVariables.CapturedClaimtypesList.clear();

		Bson MatchFilter = new BsonDocument();
		if(dpKey.isEmpty())
		{
			MatchFilter = Filters.and(Filters.eq("_id.clientKey", clientKey),
					Filters.eq("_id.dataVersion", java.util.regex.Pattern.compile(release)),
					Filters.eq("subRule.hierarchy.midRuleKey", midRuleKey),
					Filters.ne("_id.subRuleId", Subrule),
					Filters.eq("ruleInBaseLine", 0));
		}
		else
		{
			MatchFilter = Filters.and(Filters.eq("_id.clientKey", clientKey),
					Filters.eq("_id.dataVersion", java.util.regex.Pattern.compile(release)),
					//For 3808 Story
					//Filters.eq("subRule.hierarchy.dpKey", String.valueOf(Serenity.sessionVariableCalled("eDPKey"))),
					Filters.eq("subRule.hierarchy.dpKey", dpKey),
					Filters.ne("_id.subRuleId", Subrule),
					Filters.eq("ruleInBaseLine", 0));
		}

		retrieveAllDocuments("cpd", "oppty",MatchFilter); 
		
		if( recordsCount ==0)
		{
			Assert.assertTrue("There are no records on MongoDB for provided query "+ MatchFilter,false);    
		}
		System.out.println("Record count for the filter==>"+recordsCount+",Matchfilter==>"+MatchFilter);
		Bson matchtext = Aggregates.match(Filters.and(MatchFilter));
		//Aggregate filter to retrieve the output
		output = mColl.aggregate(Arrays.asList(matchtext,Aggregates.group("_id.clientKey", Accumulators.addToSet("Payerkey", "$_id.payerKey"),Accumulators.addToSet("Release", "$_id.dataVersion"),Accumulators.addToSet("Insurance", "$insuranceDesc"),Accumulators.addToSet("ClaimType", "$_id.claimType"),Accumulators.addToSet("Dispositions", "$disposition.desc"))));
		for (Document document : output)
		{
			System.out.println(document);
			payerKey=StringUtils.substringBetween(String.valueOf(document), "Payerkey=[","], Release");
			release=StringUtils.substringBetween(String.valueOf(document), "Release=[","], Insurance");
			InsuranceKeys=StringUtils.substringBetween(String.valueOf(document), "Insurance=[","], ClaimType");
			claimType=StringUtils.substringBetween(String.valueOf(document), "ClaimType=[","], Dispositions");
			disposition=StringUtils.substringBetween(String.valueOf(document), ", Dispositions=[","]}}");

			PayerKeysList=Arrays.asList(payerKey.split(","));
			ClaimtypeList=Arrays.asList(claimType.split(","));
			InsuranceList=Arrays.asList(InsuranceKeys.split(","));
			for (int i = 0; i < PayerKeysList.size(); i++) 
			{
				ProjectVariables.PayerKeysList.add(Long.valueOf(PayerKeysList.get(i).trim()));
			}
			for (int i = 0; i < ClaimtypeList.size(); i++) 
			{
				ProjectVariables.CapturedClaimtypesList.add(ClaimtypeList.get(i).trim());
			}

			for (int i = 0; i < InsuranceList.size(); i++) 
			{
				ProjectVariables.CapturedInsuranceList.add(InsuranceList.get(i).trim());
			}
		}
		System.out.println("Payer Key List ==>"+ProjectVariables.PayerKeysList);
		System.out.println("CapturedInsuranceList ==>"+ProjectVariables.CapturedInsuranceList);
		System.out.println("Captured Claimtypes List ==>"+ProjectVariables.CapturedClaimtypesList);
	}

	public static void PPSCombinationSwitchVerificationForNewVersion(Long clientKey,String release,String subrule) 
	{
		String rawSavings=null;
		String aggSavings=null;
		String conSavings=null;
		AggregateIterable<Document> output=null;

		
		Bson MatchFilter = new BsonDocument();
		MatchFilter = Filters.and(Filters.eq("_id.clientKey", clientKey),
				Filters.eq("_id.dataVersion", java.util.regex.Pattern.compile(release)),
				Filters.in("insuranceDesc", ProjectVariables.CapturedInsuranceList),
				Filters.in("_id.claimType", ProjectVariables.CapturedClaimtypesList),
				Filters.in("_id.payerKey", ProjectVariables.PayerKeysList),
				//Filters.eq("_id.subRuleId", java.util.regex.Pattern.compile(subrule)));
				Filters.eq("_id.subRuleId",subrule));
		
		retrieveAllDocuments("cpd", "oppty",MatchFilter); 
		if( recordsCount ==0)
		{
			Assert.assertTrue("There are no records on MongoDB for provided query===>"+MatchFilter,false);
			System.out.println("Client Key '"+clientKey+"' was not available in Oppty for New Version "+MatchFilter);
		}
		Bson matchtext = Aggregates.match(Filters.and(MatchFilter));
		//Aggregate filter to retrieve the output
		output = mColl.aggregate(Arrays.asList(matchtext,Aggregates.group("_id.clientKey",Accumulators.sum("RawSavings", "$annualSavings.raw"),Accumulators.sum("AggSavings", "$annualSavings.agg"),Accumulators.sum("ConSavings", "$annualSavings.con"),Accumulators.addToSet("Dispositions", "$disposition.desc"))));
		for (Document document : output)
		{
			rawSavings=StringUtils.substringBetween(String.valueOf(document), "RawSavings=",", AggSavings");
			aggSavings=StringUtils.substringBetween(String.valueOf(document), "AggSavings=",", ConSavings");
			conSavings=StringUtils.substringBetween(String.valueOf(document), "ConSavings=",", Dispositions");
			Integer rawSavingsValue=Integer.valueOf(rawSavings);
			Integer aggSavingsValue=Integer.valueOf(aggSavings);
			Integer conSavingsValue=Integer.valueOf(conSavings);
			//System.out.println(document);
			if((rawSavingsValue==0) && (aggSavingsValue==0) && (conSavingsValue==0))
			{
				System.out.println("Raw_AggSavings and Agg_ConSavings Are Zero as exepcted for Client Key is " + clientKey + ", SubRule is "+ subrule +" And Release is "+release);
				Assert.assertTrue("Raw_AggSavings and Agg_ConSavings Are Zero===>",true);
			}
			else
			{
				Assert.assertTrue("Raw_AggSavings and Agg_ConSavings===>",false);
			}
		}
	}

	public static void verifyPPSSwitchNotAddedToNewVersion(Long clientKey,String release,String subrule) 
	{
		AggregateIterable<Document> output = null;
		
		Bson MatchFilter = new BsonDocument();
		MatchFilter = Filters.and(Filters.eq("_id.clientKey", clientKey),
				Filters.eq("_id.dataVersion", java.util.regex.Pattern.compile(release)),
				Filters.in("insuranceDesc", ProjectVariables.CapturedInsuranceList),
				Filters.in("_id.claimType", ProjectVariables.CapturedClaimtypesList),
				Filters.in("_id.payerKey", ProjectVariables.PayerKeysList),
				Filters.eq("_id.subRuleId",subrule));
		
		retrieveAllDocuments("cpd", "oppty",MatchFilter);

		if (recordsCount == 0) 
			System.out.println("Switch was created in oracle, PPS Count is empty as Expected "+MatchFilter);
		else
			Assert.assertTrue("Switch was created in oracle but still New Version is seen in "+MatchFilter, false); 
	}

	public static ArrayList<Long> checkForMidRuleKeysForNewMidRuleCreation()
	{
		ArrayList<Long> sValues =new ArrayList<Long>();
		retrieveAllDocuments("cpd", "ellData");
		Distinctresults_with_long  = mColl.distinct("_id.midRuleKey", Filters.eq("ellChange", "New MidRule"),Long.class);
		for (Long document : Distinctresults_with_long) 
		{
			sValues.add(document);
		}
		//System.out.println(sValues);
		System.out.println(sValues.size());
		return sValues;
	} 
	public static void cdmDataInsertionIntoDecisionCollection()
	{
		String dpKey=Serenity.sessionVariableCalled("latestDecisionDPKey");
		String payerKey=Serenity.sessionVariableCalled("latestDecisionPayerKey");
		Bson matchFilter = new BsonDocument();
		matchFilter = Filters.and(Filters.eq("_id.payerKey",Long.valueOf(payerKey)),
				Filters.eq("_id.dpKey", Long.valueOf(dpKey)));
		retrieveAllDocuments("cdm", "latestDecision",matchFilter);
		if( recordsCount ==0)
			Assert.assertTrue("There are no records on MongoDB for provided query",false);    
		else
			Assert.assertTrue("New Decision Service Inserted data In latestDecision Collection",true);  
	}


	//========================================== Sorting ==============================================================================>
	
	public static String RetrieveThePriorityofthegiven(String data,String typeofdata)
	{
		String sPriority=null;
		AggregateIterable<Document> output=null;

		//Method to connect mongoDB
		retrieveAllDocuments("cpd", "oppty");

		Bson MatchFilter = new BsonDocument();
		
		switch(typeofdata)
		{
		case "Medicalpolicy":
			//Filter to form a match query based on inputs
			MatchFilter = Filters.and(Filters.eq("_id.clientKey", Long.valueOf(Serenity.sessionVariableCalled("clientkey"))),
					Filters.eq("disposition.desc", "Present"),Filters.eq("subRule.hierarchy.medPolicyDesc", data),
					Filters.eq("ruleInBaseLine", 0));

		break;
		case "Topic":
			//Filter to form a match query based on inputs
			MatchFilter = Filters.and(Filters.eq("_id.clientKey", Long.valueOf(Serenity.sessionVariableCalled("clientkey"))),
					Filters.eq("disposition.desc", "Present"),Filters.eq("subRule.hierarchy.topicDesc", data),
					Filters.eq("ruleInBaseLine", 0));

		break;
		case "DP":
			//Filter to form a match query based on inputs
			MatchFilter = Filters.and(Filters.eq("_id.clientKey", Long.valueOf(Serenity.sessionVariableCalled("clientkey"))),
					Filters.eq("disposition.desc", "Present"),Filters.eq("subRule.hierarchy.dpKey", Long.valueOf(data)),
					Filters.eq("ruleInBaseLine", 0));

		break;

		default:
			Assert.assertTrue("Case not found==>"+typeofdata, false);
		break;
		}

		

		recordsCount = mColl.count(MatchFilter);
		

		/*if(recordsCount==0)
			{
				Assert.assertTrue("Record count was zero for the given Clientkey==>"+Clientkey+",Disposition==>Present", false);
			}
		 */
		Bson matchtext = Aggregates.match(Filters.and(MatchFilter));

		//Aggregate filter to retrieve the output
		output = mColl.aggregate(Arrays.asList(matchtext,Aggregates.group("_id.clientKey", Accumulators.addToSet("Priority", "$disposition.priority"))));
		
		
		for (Document doc:output) 
		{
			System.out.println(doc);
			sPriority=StringUtils.substringBetween(String.valueOf(doc), "Priority=[","]}}");
		}
		
		return sPriority;
		
	}

	public static ArrayList<Long> RetrieveTheSortOrderofthegivenDP(ArrayList<String> DPlist)
	{
		String DPsortOrder=null;
		ArrayList<Long> DPSortorderlist=new ArrayList<>();
		AggregateIterable<Document> output=null;

		//Method to connect mongoDB
		retrieveAllDocuments("cpd", "oppty");
		Bson MatchFilter = new BsonDocument();
		
		for (int i= 0; i < DPlist.size(); i++) 
		{
			//Filter to form a match query based on inputs
			MatchFilter = Filters.and(Filters.eq("_id.clientKey", Long.valueOf(Serenity.sessionVariableCalled("clientkey"))),
					Filters.eq("disposition.desc", "Present"),Filters.eq("subRule.hierarchy.dpKey", Long.valueOf(DPlist.get(i))),
					Filters.eq("ruleInBaseLine", 0));

			recordsCount = mColl.count(MatchFilter);

			if(recordsCount==0)
				{
					Assert.assertTrue("Record count was zero for the given filter==>"+MatchFilter, false);
				}
			 
			Bson matchtext = Aggregates.match(Filters.and(MatchFilter));

			//Aggregate filter to retrieve the output
			output = mColl.aggregate(Arrays.asList(matchtext,Aggregates.group("_id.clientKey", Accumulators.addToSet("DPsortOrder", "$subRule.hierarchy.dpSortOrder"))));
			
			
			for (Document doc:output) 
			{
				//System.out.println(doc);
				DPsortOrder=StringUtils.substringBetween(String.valueOf(doc), "DPsortOrder=[","]}}");
				DPSortorderlist.add(Long.valueOf(DPsortOrder));
			}
		}

		
		
		return DPSortorderlist;
		
	}
	
	public static boolean getThePresentationStaus(String clientkey,String profileID)
	{
		boolean bstatus=false;
		//Method to connect mongoDB
		retrieveAllDocuments("presentations", "decision");
	
		Bson MatchFilter = new BsonDocument();
				
		//Filter to form a match query based on inputs
		MatchFilter = Filters.and(Filters.eq("_id.clientKey", Long.valueOf(clientkey)),
				Filters.eq("profileId", profileID));
		
		recordsCount = mColl.count(MatchFilter);
		
		System.out.println("RecordCount::"+recordsCount+",for matchfilter==>"+MatchFilter);
		if(recordsCount==0)
		{
			bstatus=true;
			return bstatus;
		}
		else
		{
			return bstatus;
		}
	}
	
	//####################### Duplicate DP+PPS #################################################//
	
	public static void getDuplicateDataversionforPPS(String clientkey,String collection,String Disposition) throws ParseException
	{
		boolean bstatus=false;
		int i=0;
		String sdataversion=null;
		AggregateIterable<Document> output=null;
		List<String> dataVersionList=null;
		String sDPkey=null;
		String sPayershort=null;
		String sClaimtype=null;
		String sinsurance=null;
		String sDisposition=null;
		String sLatestdataversion=null;
		String sSubrule=null;
		String sPresentation=null;
		ArrayList<String> dates=new ArrayList();
		
		//Method to connect mongoDB
		ConnectTogivenDBandCollection("cpd", collection);
	
		Bson MatchFilter = new BsonDocument();
				
		//Filter to form a match query based on inputs
		MatchFilter = Filters.and(Filters.eq("_id.clientKey", Long.valueOf(clientkey)),
				Filters.eq("disposition.desc", Disposition));
		
		recordsCount = mColl.count(MatchFilter);
		
		System.out.println("RecordCount::"+recordsCount+",for matchfilter==>"+MatchFilter);
		Bson matchtext = Aggregates.match(Filters.and(MatchFilter));

		//Aggregate filter to retrieve the output
		if(Disposition.equalsIgnoreCase("Present")||Disposition.equalsIgnoreCase("Complete")||Disposition.equalsIgnoreCase("Defer"))
		{
			output = mColl.aggregate(Arrays.asList(matchtext,Aggregates.group(new Document().append("DPKey", "$subRule.hierarchy.dpKey").append("PayerShort", "$_id.payerShort").append("Claimtype", "$_id.claimType").append("Insurance", "$insuranceDesc").append("Disposition", "$disposition.desc").append("Subrule", "$_id.subRuleId").append("Presentation", "$presentationProfile.profileName"), Accumulators.addToSet("Dataversion", "$_id.dataVersion")))
					).allowDiskUse(true);
			
			
		}
		else
		{
			output = mColl.aggregate(Arrays.asList(matchtext,Aggregates.group(new Document().append("DPKey", "$subRule.hierarchy.dpKey").append("PayerShort", "$_id.payerShort").append("Claimtype", "$_id.claimType").append("Insurance", "$insuranceDesc").append("Disposition", "$disposition.desc").append("Subrule", "$_id.subRuleId"), Accumulators.addToSet("Dataversion", "$_id.dataVersion")))
				).allowDiskUse(true);
			
				
		}
		
		for (Document document : output) 
		{
			
			sdataversion=StringUtils.substringBetween(String.valueOf(document), "Dataversion=[", "]}}");
			dataVersionList=Arrays.asList(sdataversion.split(","));
			
			if(dataVersionList.size()>1)
			{
				i=i+1;
				//System.out.println(String.valueOf(document));	
				sDPkey=StringUtils.substringBetween(String.valueOf(document), "DPKey=", ", PayerShort");
				sPayershort=StringUtils.substringBetween(String.valueOf(document), "PayerShort=", ", Claimtype");
				sClaimtype=StringUtils.substringBetween(String.valueOf(document), "Claimtype=", ", Insurance");
				sinsurance=StringUtils.substringBetween(String.valueOf(document), "Insurance=", ", Disposition");
				sDisposition=StringUtils.substringBetween(String.valueOf(document), "Disposition=", ", Subrule");
				
				if(Disposition.equalsIgnoreCase("Present")||Disposition.equalsIgnoreCase("Complete")||Disposition.equalsIgnoreCase("Defer")&&String.valueOf(document).contains("Presentation"))
				{
					sSubrule=StringUtils.substringBetween(String.valueOf(document), "Subrule=", ", Presentation");
					sPresentation=StringUtils.substringBetween(String.valueOf(document), "Presentation=", "}}, Dataversion");
				}
				else
				{
					sSubrule=StringUtils.substringBetween(String.valueOf(document), "Subrule=", "}}, Dataversion");	
				}
				
				
				
				
				//To get the latest dataversion
				for (int j = 0; j < dataVersionList.size(); j++) 
				{
					dates.add(StringUtils.substringAfter(dataVersionList.get(j), "PMPRD1_").substring(0, 6));
				}
				sLatestdataversion=GenericUtils.getTheLatestDate(dates).trim();
				if(Disposition.equalsIgnoreCase("Present")||Disposition.equalsIgnoreCase("Complete")||Disposition.equalsIgnoreCase("Defer")&&String.valueOf(document).contains("Presentation"))
				{
					ProjectVariables.DuplicatePPS.add("DP-"+sDPkey+";Payershort-"+sPayershort+";Insurance-"+sinsurance+";Claimtype-"+sClaimtype+";Disposition-"+sDisposition+";Dataversion-"+sLatestdataversion+";Subrule-"+sSubrule+";Presentation-"+sPresentation);
				}
				else
				{
					ProjectVariables.DuplicatePPS.add("DP-"+sDPkey+";Payershort-"+sPayershort+";Insurance-"+sinsurance+";Claimtype-"+sClaimtype+";Disposition-"+sDisposition+";Dataversion-"+sLatestdataversion+";Subrule-"+sSubrule);	
				}
				
				dates.clear();
			}
		}
		
		
		GenericUtils.Verify("PPS Count is '"+i+"',which are having duplicate dataversions in '"+collection+"' collection for disposition==>"+Disposition+",ClientKey==>"+clientkey,true);
		//System.out.println("PPS Count is '"+i+"',which are having duplicate dataversions in '"+collection+"' collection for disposition==>"+Disposition+",ClientKey==>"+clientkey);
		
		Serenity.recordReportData().asEvidence().withTitle("PPS Count is '"+i+"',which are having duplicate dataversions in '"+collection+"' collection for disposition==>"+Disposition+",ClientKey==>"+clientkey).andContents("Passed");
	}
	
	public static void verifyTheDuplicatePPS(String clientkey,String collection)
	{
		boolean bstatus=false;
		int i=0;
		String sdataversion=null;
		AggregateIterable<Document> output=null;
		List<String> dataVersionList=null;
		String sDPkey=null;
		String sPayershort=null;
		String sClaimtype=null;
		String sinsurance=null;
		String sDisposition=null;
		String sDataversion=null;
		String sSubrule=null;
		String sPresentation=null;
		//Method to connect mongoDB
		ConnectTogivenDBandCollection("cpd", collection);
	
		Bson MatchFilter = new BsonDocument();
		
		for (int j = 0; j < ProjectVariables.DuplicatePPS.size(); j++) 
		{
			i=i+1;
			//System.out.println(i+".PPS=>"+ProjectVariables.DuplicatePPS.get(j));
			GenericUtils.Verify(i+".PPS=>"+ProjectVariables.DuplicatePPS.get(j)+",Total PPS=>"+ProjectVariables.DuplicatePPS.size(),true);
			sDPkey=StringUtils.substringBetween(ProjectVariables.DuplicatePPS.get(j), "DP-", ";Payershort");
			sPayershort=StringUtils.substringBetween(ProjectVariables.DuplicatePPS.get(j), "Payershort-", ";Insurance");
			sClaimtype=StringUtils.substringBetween(ProjectVariables.DuplicatePPS.get(j), "Claimtype-", ";Disposition");
			sinsurance=StringUtils.substringBetween(ProjectVariables.DuplicatePPS.get(j), "Insurance-", ";Claimtype");
			sDisposition=StringUtils.substringBetween(ProjectVariables.DuplicatePPS.get(j), "Disposition-",";Dataversion");
			sDataversion=StringUtils.substringBetween(ProjectVariables.DuplicatePPS.get(j), "Dataversion-",";Subrule");
			sPresentation=StringUtils.substringAfter(ProjectVariables.DuplicatePPS.get(j), "Presentation-");
			
			if(!sPresentation.isEmpty())
			{
				sSubrule=StringUtils.substringBetween(ProjectVariables.DuplicatePPS.get(j), "Subrule-",";Presentation");
				
			}
			else
			{
				sSubrule=StringUtils.substringAfter(ProjectVariables.DuplicatePPS.get(j), "Subrule-");
				sPresentation=null;
			}
			
			if(sDisposition.equalsIgnoreCase("Present")||sDisposition.equalsIgnoreCase("Complete")||sDisposition.equalsIgnoreCase("Defer"))
			{
				//Filter to form a match query based on inputs
				MatchFilter = Filters.and(Filters.eq("_id.clientKey", Long.valueOf(clientkey)),
								Filters.eq("disposition.desc", sDisposition),
								Filters.eq("subRule.hierarchy.dpKey", Long.valueOf(sDPkey)),
								Filters.eq("_id.payerShort", sPayershort),
								Filters.eq("_id.claimType", sClaimtype),
								Filters.eq("insuranceDesc", sinsurance),
								Filters.eq("_id.subRuleId", sSubrule),
								Filters.eq("presentationProfile.profileName", sPresentation)
								);
			}
			else
			{
				//Filter to form a match query based on inputs
				MatchFilter = Filters.and(Filters.eq("_id.clientKey", Long.valueOf(clientkey)),
								Filters.eq("disposition.desc", sDisposition),
								Filters.eq("subRule.hierarchy.dpKey", Long.valueOf(sDPkey)),
								Filters.eq("_id.payerShort", sPayershort),
								Filters.eq("_id.claimType", sClaimtype),
								Filters.eq("insuranceDesc", sinsurance),
								Filters.eq("_id.subRuleId", sSubrule)
								);
			}
			
			
			recordsCount = mColl.count(MatchFilter);
			
			System.out.println("RecordCount::"+recordsCount+",for matchfilter==>"+MatchFilter);
			if(recordsCount==1)
			{
				GenericUtils.Verify("Recordcount is '1' as Duplicate dataversion has been removed for PPS",true);
				//System.out.println("PPS count is '1' as Duplicate dataversion has been removed");
			}
			else
			{
				Serenity.recordReportData().asEvidence().withTitle("Recordcount is '"+recordsCount+"' in collection '"+collection+"',it should be '1' as duplicate dataversion is removed,PPS=>"+ProjectVariables.DuplicatePPS.get(j)).andContents("Passed");
				//Assert.assertTrue("Recordcount is '"+recordsCount+"' in collection '"+collection+"',it should be '1' as duplicate dataversion is removed,PPS=>"+ProjectVariables.DuplicatePPS.get(j),false);
				//GenericUtils.Verify("Recordcount is '"+recordsCount+"' in collection '"+collection+"',it should be '1' as duplicate dataversion is removed,PPS=>"+ProjectVariables.DuplicatePPS.get(j),false);
			}
			
			if(sDisposition.equalsIgnoreCase("Present")||sDisposition.equalsIgnoreCase("Complete")||sDisposition.equalsIgnoreCase("Defer"))
			{
				//Filter to form a match query based on inputs
				MatchFilter = Filters.and(Filters.eq("_id.clientKey", Long.valueOf(clientkey)),
								Filters.eq("disposition.desc", sDisposition),
								Filters.eq("subRule.hierarchy.dpKey", Long.valueOf(sDPkey)),
								Filters.eq("_id.payerShort", sPayershort),
								Filters.eq("_id.claimType", sClaimtype),
								Filters.eq("insuranceDesc", sinsurance),
								Filters.eq("_id.dataVersion", java.util.regex.Pattern.compile(sDataversion)),
								Filters.eq("_id.subRuleId", sSubrule),
								Filters.eq("presentationProfile.profileName", sPresentation)
								);
			}
			else
			{
				//Filter to form a match query based on inputs
				MatchFilter = Filters.and(Filters.eq("_id.clientKey", Long.valueOf(clientkey)),
								Filters.eq("disposition.desc", sDisposition),
								Filters.eq("subRule.hierarchy.dpKey", Long.valueOf(sDPkey)),
								Filters.eq("_id.payerShort", sPayershort),
								Filters.eq("_id.claimType", sClaimtype),
								Filters.eq("insuranceDesc", sinsurance),
								Filters.eq("_id.dataVersion", java.util.regex.Pattern.compile(sDataversion)),
								Filters.eq("_id.subRuleId", sSubrule)
								);
			}
			
			
			recordsCount = mColl.count(MatchFilter);
			
			System.out.println("RecordCount::"+recordsCount+",for matchfilter==>"+MatchFilter);
			if(recordsCount==1)
			{
				//System.out.println("RecordCount is '1' as Latest dataversion has been added in '"+collection+"' collection");
				GenericUtils.Verify("RecordCount is '1' as Latest dataversion has been added in '"+collection+"' collection",true);
				
			}
			else
			{
				//Assert.assertTrue("Latest dataversion has not added in '"+collection+"' collection,PPS count is '"+recordsCount+"',PPS=>"+ProjectVariables.DuplicatePPS.get(j),false);
				Serenity.recordReportData().asEvidence().withTitle("Latest dataversion has not added,PPS count is '"+recordsCount+"',PPS=>"+ProjectVariables.DuplicatePPS.get(j)).andContents("Passed");
				//GenericUtils.Verify("Latest dataversion has not added in '"+collection+"' collection,PPS count is '"+recordsCount+"',PPS=>"+ProjectVariables.DuplicatePPS.get(j),false);
			}
		}
		
	}
	
	public static ArrayList<String> getTheDistinctDispositionsbasedOnClient(String clientkey,String collectionname)
	{
		int i=1;
		ArrayList<Long> clinetKeyList=new ArrayList<>();
		ArrayList<String> DispositionList=new ArrayList<>();
		//Method to connect mongoDB
		ConnectTogivenDBandCollection("cpd", collectionname);
		
		/*Distinctresults_with_long  = mColl.distinct("_id.clientKey",Long.class);
		for (Long document : Distinctresults_with_long) 
		{
			clinetKeyList.add(document);
		}
		System.out.println("Total Clients===>"+clinetKeyList.size());*/
		//for (Long clientKey : clinetKeyList) 
		{
			Distinctresults  = mColl.distinct("disposition.desc",Filters.eq("_id.clientKey", Long.valueOf(clientkey)),String.class);
			for (String disp:Distinctresults) 
			{
				DispositionList.add(disp);
			}
			
			System.out.println(i+".clinetKey '"+clientkey+"'::"+DispositionList);
			//DispositionList.clear();
			i=i+1;
		}
		return DispositionList;
	}
	
	public static void getDuplicateDataversionforDPPayrshort(String clientkey,String collection,String Disposition) throws ParseException
	{
		boolean bstatus=false;
		int i=0;
		String sdataversion=null;
		AggregateIterable<Document> output=null;
		List<String> dataVersionList=null;
		String sDPkey=null;
		String sPayershort=null;
		String sClaimtype=null;
		String sinsurance=null;
		String sDisposition=null;
		String sLatestdataversion=null;
		String sSubrule=null;
		String sPresentation=null;
		ArrayList<String> dates=new ArrayList();
		
		//Method to connect mongoDB
		ConnectTogivenDBandCollection("cpd", collection);
	
		Bson MatchFilter = new BsonDocument();
				
		//Filter to form a match query based on inputs
		MatchFilter = Filters.and(Filters.eq("_id.clientKey", Long.valueOf(clientkey)),
				Filters.eq("disposition.desc", Disposition));
		
		recordsCount = mColl.count(MatchFilter);
		
		//System.out.println("RecordCount::"+recordsCount+",for matchfilter==>"+MatchFilter);
		Bson matchtext = Aggregates.match(Filters.and(MatchFilter));

		//Aggregate filter to retrieve the output
		if(Disposition.equalsIgnoreCase("Present")||Disposition.equalsIgnoreCase("Complete")||Disposition.equalsIgnoreCase("Defer"))
		{
			output = mColl.aggregate(Arrays.asList(matchtext,Aggregates.group(new Document().append("DPKey", "$subRule.hierarchy.dpKey").append("PayerShort", "$_id.payerShort").append("Disposition", "$disposition.desc").append("Presentation", "$presentationProfile.profileName").append("Insurance", "$insuranceDesc").append("Claimtype", "$_id.claimType").append("subruleid", "$_id.subRuleId"), Accumulators.addToSet("Dataversion", "$_id.dataVersion")))
					).allowDiskUse(true);
		}
		else
		{
			
			output = mColl.aggregate(Arrays.asList(matchtext,Aggregates.group(new Document().append("DPKey", "$subRule.hierarchy.dpKey").append("PayerShort", "$_id.payerShort").append("Disposition", "$disposition.desc").append("Insurance", "$insuranceDesc").append("Claimtype", "$_id.claimType").append("subruleid", "$_id.subRuleId"), Accumulators.addToSet("Dataversion", "$_id.dataVersion")))
					).allowDiskUse(true);
		}
		
		for (Document document : output) 
		{
			//System.out.println(document);
			sdataversion=StringUtils.substringBetween(String.valueOf(document), "Dataversion=[", "]}}");
			dataVersionList=Arrays.asList(sdataversion.split(","));
			sDPkey=StringUtils.substringBetween(String.valueOf(document), "DPKey=", ", PayerShort");
			/*if(sDPkey.equalsIgnoreCase("12020"))
			{
				System.out.println(String.valueOf(document));
			}*/
			if(dataVersionList.size()>1)
			{
				i=i+1;
				//System.out.println(String.valueOf(document));	
				sDPkey=StringUtils.substringBetween(String.valueOf(document), "DPKey=", ", PayerShort");
				sPayershort=StringUtils.substringBetween(String.valueOf(document), "PayerShort=", ", Disposition");
				sinsurance=StringUtils.substringBetween(String.valueOf(document), "Insurance=", ", Claimtype");
				sClaimtype=StringUtils.substringBetween(String.valueOf(document), "Claimtype=", ", subruleid");
				sSubrule=StringUtils.substringBetween(String.valueOf(document), "subruleid=", "}}, Dataversion");
				
				if(Disposition.equalsIgnoreCase("Present")||Disposition.equalsIgnoreCase("Complete")||Disposition.equalsIgnoreCase("Defer"))
				{
					sDisposition=StringUtils.substringBetween(String.valueOf(document), "Disposition=", ", Presentation");
					sPresentation=StringUtils.substringBetween(String.valueOf(document), "Presentation=", "}}, Dataversion");
					
				}
				else
				{
					sDisposition=StringUtils.substringBetween(String.valueOf(document), "Disposition=", ", Insurance");	
					
				}
				
				
				
				//To get the latest dataversion
				for (int j = 0; j < dataVersionList.size(); j++) 
				{
					dates.add(StringUtils.substringAfter(dataVersionList.get(j), "PMPRD1_").substring(0, 6));
				}
				sLatestdataversion=GenericUtils.getTheLatestDate(dates).trim();
				if(Disposition.equalsIgnoreCase("Present")||Disposition.equalsIgnoreCase("Complete")||Disposition.equalsIgnoreCase("Defer")&&String.valueOf(document).contains("Presentation"))
				{
					ProjectVariables.DuplicatePPS.add("Clientkey-"+clientkey+";DP-"+sDPkey+";Payershort-"+sPayershort+";Insurance-"+sinsurance+";Claimtype:"+sClaimtype+";Subrule-"+sSubrule+";Disposition-"+sDisposition+";Dataversion-"+sLatestdataversion+";Presentation"+sPresentation);
				}
				else
				{
					ProjectVariables.DuplicatePPS.add("Clientkey-"+clientkey+";DP-"+sDPkey+";Payershort-"+sPayershort+";Insurance-"+sinsurance+";Claimtype:"+sClaimtype+";Subrule-"+sSubrule+";Disposition-"+sDisposition+";Dataversion-"+sLatestdataversion);	
				}
				
				dates.clear();
			}
		}
		
		
		GenericUtils.Verify("PPS Count is '"+i+"',which are having duplicate dataversions in '"+collection+"' collection for disposition==>"+Disposition+",ClientKey==>"+clientkey,true);
		//System.out.println("PPS Count is '"+i+"',which are having duplicate dataversions in '"+collection+"' collection for disposition==>"+Disposition+",ClientKey==>"+clientkey);
		
		Serenity.recordReportData().asEvidence().withTitle("PPS Count is '"+i+"',which are having duplicate dataversions in '"+collection+"' collection for disposition==>"+Disposition+",ClientKey==>"+clientkey).andContents("Passed");
		
		for (String dppps : ProjectVariables.DuplicatePPS) 
		{
			Serenity.recordReportData().asEvidence().withTitle("Duplicate DP+PPS::"+dppps).andContents("Passed");
		}
		
	}
	
	public static String retrieveTheeLLMPsbasedonPPSforFilterPanel(String payershort,String insurance,String claimtype,String LCD)
	{
		HashSet<Long> dpKeys=new HashSet<>();
		HashSet<String> MPs=new HashSet<>();
		HashSet<String> ExactMPswithDPs=new HashSet<>();
		HashSet<String> Topics=new HashSet<>();
		Bson MatchFilter = new BsonDocument();
		String medicalpolicy=null;
		AggregateIterable<Document> output=null;
		String sDPkey=null;;
		ArrayList<String> dpKeysList=new ArrayList<>();
		HashSet<Long> rvapayerSwitchList=new HashSet<>();
		List<String> MPList = new ArrayList<>();
		List<String> DpKeys= new ArrayList<>();
		HashSet<String> MPswithDPs=new HashSet<>();
		HashSet<String> RequiredMPs=new HashSet<>();
		int i=0;	
		String dps=null;
		
		//To load the pps and LCD in arraylist
		loadThePPSandLCDinList(payershort,insurance,claimtype,LCD);
	//################ To retrieve the DPkeys for the pps #######################################				
		ConnectTogivenDBandCollection("cdm", "latestDecision");
		MatchFilter = Filters.and(Filters.in("_id.payerKey", ProjectVariables.payerKeys),
				  Filters.in("_id.insuranceKey", ProjectVariables.insuranceKeyslist),
				  Filters.in("_id.claimType", ProjectVariables.ClaimtypeList),
				  Filters.in("decStatusDesc", ProjectVariables.lCDlist));
		results=mColl.find(MatchFilter);
		recordsCount = mColl.count(MatchFilter);
		if(recordsCount==0)
		{
			Assert.assertTrue("RecordCount is '0' for the given matchfilter===>"+MatchFilter, false);
		}
		for (Document doc : results) 
		{
			Document iddoc=doc.get("_id",Document.class);
			dpKeys.add(iddoc.getLong("dpKey"));
		}
		System.out.println("DPKeysize::"+dpKeys.size());		
		
	//################ To retrieve the MP and Topics for the DPkeys #######################################
		ConnectTogivenDBandCollection("cpd", "ellHierarchy");
		MatchFilter = Filters.and(Filters.in("_id.dpKey", dpKeys),Filters.eq("rvaPayerSwitch", 0),Filters.eq("subRule.hierarchy.dpTypeDesc", "Rules"));
		results=mColl.find(MatchFilter);
		recordsCount = mColl.count(MatchFilter);
		if(recordsCount==0)
		{
			Assert.assertTrue("RecordCount is '0' for the given matchfilter===>"+MatchFilter, false);
		}
		for (Document doc : results) 
		{
			Document idDoc=doc.get("_id",Document.class);
			Document suRuledoc=doc.get("subRule",Document.class);
			Document hierarchydoc=suRuledoc.get("hierarchy",Document.class);
			medicalpolicy=hierarchydoc.getString("medPolicyDesc");
			MPs.add(medicalpolicy);
			Topics.add(hierarchydoc.getString("topicDesc")+";"+medicalpolicy);
		}
	//################ To retrieve the MPs with corresponding DPkeys #######################################
		for (String sTopic : Topics) 
		{
			String exactTopic=StringUtils.substringBefore(sTopic, ";");
			MatchFilter = Filters.and(
						  Filters.eq("subRule.hierarchy.topicDesc", exactTopic),
						  Filters.eq("rvaPayerSwitch", 0),
						  Filters.eq("subRule.hierarchy.dpTypeDesc", "Rules")
						  );
			Bson matchtext = Aggregates.match(Filters.and(MatchFilter));
			output = mColl.aggregate(Arrays.asList(matchtext,Aggregates.group(new Document().append("Topic", "$subRule.hierarchy.topicDesc").append("MP",
					"$subRule.hierarchy.medPolicyDesc").append("dpKey", "$subRule.hierarchy.dpKey").append("rvapayerSwitch", "$rvaPayerSwitch"),
					Accumulators.addToSet("DPKeys", "$subRule.hierarchy.dpKey"),
					Accumulators.addToSet("rvaPayerswitch", "$rvaPayerSwitch"),
					Accumulators.addToSet("Medicalpolicy", "$subRule.hierarchy.medPolicyDesc")
					))).allowDiskUse(true);
			for (Document document : output) 
			{
				medicalpolicy=StringUtils.substringBetween(String.valueOf(document), "Medicalpolicy=[", "]}}");
				sDPkey=StringUtils.substringBetween(String.valueOf(document),"DPKeys=[","], rvaPayerswitch").trim();
				MPList=Arrays.asList(medicalpolicy.split(","));
				for (int j = 0; j < MPList.size(); j++) 
				{
					ExactMPswithDPs.add(MPList.get(j)+"::"+sDPkey);
					i=i+1;
				}
			}
		}
	//####################### Verify the dpkey is having 'None' decision or not #########################################
		ConnectTogivenDBandCollection("cdm", "latestDecision");
		
		for (String mP : ExactMPswithDPs) 
		{
			medicalpolicy=StringUtils.substringBefore(mP,"::");
			sDPkey=StringUtils.substringAfter(mP,"::");
			Serenity.setSessionVariable("DPkey").to(StringUtils.substringAfter(mP,"::"));
			String row=medicalpolicy+";Raw-0;Agg-0;Con-0";
			MatchFilter = Filters.and(Filters.in("_id.payerKey", ProjectVariables.payerKeys),
					  Filters.in("_id.insuranceKey", ProjectVariables.insuranceKeyslist),
					  Filters.in("_id.claimType", ProjectVariables.ClaimtypeList),
					  Filters.in("decStatusDesc", ProjectVariables.lCDlist),
						  Filters.eq("_id.dpKey", Long.valueOf(sDPkey))
						  );
			recordsCount = mColl.count(MatchFilter);
			if(recordsCount!=0)
			{
				RequiredMPs.add(medicalpolicy.trim());
				MPswithDPs.add(medicalpolicy+"::"+sDPkey);
			}
		}
 //####################### Verify the dpkey is having rvaPayerswitch is '0' only #########################################
		ExactMPswithDPs.clear();
		ConnectTogivenDBandCollection("cpd", "ellHierarchy");
		for (String MP : RequiredMPs) 
		{
			for (String mpwithdp : MPswithDPs) 
			{
				if(MP.equalsIgnoreCase(StringUtils.substringBefore(mpwithdp,"::").trim()))
				{
					
					//Filter to form a match query based on inputs
					MatchFilter = Filters.and(
								  Filters.eq("subRule.hierarchy.medPolicyDesc", StringUtils.substringBefore(mpwithdp,"::").trim()),
								  Filters.eq("subRule.hierarchy.dpKey", Long.valueOf(StringUtils.substringAfter(mpwithdp,"::").trim()))
								  );
					Distinctresults_with_long  = mColl.distinct("rvaPayerSwitch",MatchFilter,Long.class);
					for (Long rvapayeswitch:Distinctresults_with_long) 
					{
						rvapayerSwitchList.add(rvapayeswitch);
					}
					if(rvapayerSwitchList.size()==1&&rvapayerSwitchList.contains(0l))
					{
						dpKeysList.add(StringUtils.substringAfter(mpwithdp,"::").trim());
					}
					rvapayerSwitchList.clear();
				}
			
			}
			 dps=String.join(",", dpKeysList);
			if(dpKeysList.size()!=0)
			{
				ExactMPswithDPs.add(MP+"::"+dps);
			}
			dpKeysList.clear();
		}
//####################### Verify the dpkey is captured or not #########################################	
		ConnectTogivenDBandCollection("cpd", "oppty");
		for (String eLLdata : ExactMPswithDPs) 
		{
			medicalpolicy=StringUtils.substringBefore(eLLdata, "::").trim();
			DpKeys=Arrays.asList(StringUtils.substringAfter(eLLdata, "::").split(","));
			for (int j = 0; j < DpKeys.size(); j++)
			{
				//Filter to form a match query based on inputs
				MatchFilter = Filters.and(
							Filters.eq("_id.clientKey", Long.valueOf(Serenity.sessionVariableCalled("clientkey"))),
							  Filters.eq("subRule.hierarchy.medPolicyDesc", medicalpolicy),
							  Filters.eq("disposition.desc", "No Disposition"),
							  Filters.eq("subRule.hierarchy.dpKey", Long.valueOf(DpKeys.get(j).trim()))
							  );
				recordsCount = mColl.count(MatchFilter);
				dps=String.join(",", DpKeys);
				if(recordsCount!=0)
				{
					//System.out.println(medicalpolicy+"::"+DpKeys.size()+"::"+dps);
					ProjectVariables.Medicalpolicy_PolicySelectiondrawer.add(medicalpolicy);
					ProjectVariables.DB_MPlist.add(medicalpolicy+"::"+dps);
				}
			}
		}
		
		for (String eLLdata : ProjectVariables.DB_MPlist) {
			DpKeys=Arrays.asList(StringUtils.substringAfter(eLLdata, "::").split(","));
			if(DpKeys.size()>5)
			{
				medicalpolicy=StringUtils.substringBefore(eLLdata, "::");
			}
			System.out.println(StringUtils.substringBefore(eLLdata, "::")+"::"+DpKeys.size()+"::"+DpKeys);
		}
		
		System.out.println("eLL MPsize::"+ProjectVariables.Medicalpolicy_PolicySelectiondrawer.size());
		System.out.println("eLL Topicsize::"+Topics.size());
		if(ProjectVariables.Medicalpolicy_PolicySelectiondrawer.isEmpty())
		{
			Assert.assertTrue("No eLL MPs are availabl for the client/PPS", false);
		}
		return medicalpolicy;
	
	}
	
	private static void loadThePPSandLCDinList(String payershort,
			String insurance,String claimtype,String LCD) {
		
		
		//To load the given pps into lists
		if(!payershort.isEmpty())
		{
			ProjectVariables.lCDlist=Arrays.asList(LCD.split(","));
			ProjectVariables.ClaimtypeList=Arrays.asList(claimtype.split(","));
			ProjectVariables.insuranceList=Arrays.asList(insurance.split(","));
			for (int j = 0; j < ProjectVariables.insuranceList.size(); j++) {
				ProjectVariables.insuranceKeyslist.add(Long.valueOf(GenericUtils.Retrieve_the_insuranceKey_from_insurance(ProjectVariables.insuranceList.get(j).trim())));
			}
			
			ProjectVariables.payerShortList=Arrays.asList(payershort.split(","));
			for (int j = 0; j < ProjectVariables.payerShortList.size(); j++) {
				//To retrieve the payerkeys based on clientkey
				ProjectVariables.payerKeys.add(Long.valueOf(Retrieve_the_PayerKey_From_mongodb_with_the_given_Payershort(ProjectVariables.payerShortList.get(j).trim(), "Payershort")));
					
			}
		}
		else
		{
			ProjectVariables.lCDlist=Arrays.asList(ProjectVariables.LatestClientDecisionFilterOptions.split(","));
			ProjectVariables.ClaimtypeList=Arrays.asList(ProjectVariables.ProductFilterOptions.split(","));
			ProjectVariables.insuranceKeyslist=Arrays.asList(1l,2l,3l,7l,8l,9l);
			//To retrieve the payerkeys based on clientkey
			ProjectVariables.payerKeys.addAll(getTheDistinctPayerkeysBasedOnClient(Serenity.sessionVariableCalled("clientkey")));
		}
		
	}
	
	public static ArrayList<Long> getTheDistinctPayerkeysBasedOnClient(String clientkey)
	{
		ArrayList<Long> Payershortlist=new ArrayList<>();
		//Method to connect mongoDB
		ConnectTogivenDBandCollection("cpd", "oppty");
		
		AggregateIterable<Document> output=null;
		
		Bson MatchFilter = new BsonDocument();
		//Filter to form a match query based on inputs
		MatchFilter = Filters.and(Filters.eq("_id.clientKey", Long.valueOf(clientkey)));
		recordsCount = mColl.count(MatchFilter);
		System.out.println("RecordCount::"+recordsCount+",for matchfilter==>"+MatchFilter);
		Distinctresults_with_long  = mColl.distinct("_id.payerKey",Filters.eq("_id.clientKey", Long.valueOf(clientkey)),Long.class);
		for (Long payerKey:Distinctresults_with_long) 
		{
			Payershortlist.add(payerKey);
		}
		System.out.println("PayerKeys::"+Payershortlist);
		return Payershortlist;
	}

	public static ArrayList<String> getELLPPSData(String sDPKey,String MedicalPolicy, String Topic, String Src){
		ArrayList<String> arPPS = new ArrayList<String>(); 
	       retrieveAllDocuments("cpd", "oppty");
	       
	       List<String> returnFields = new ArrayList<String>();
	       
	       Bson MatchFilter = new BsonDocument();
	       int i = 0;
	       MatchFilter = Filters.and(Filters.eq("ruleInBaseLine", 0),
	               Filters.eq("disposition.desc", "No Disposition"),
	               Filters.eq("source",Src),
	               Filters.eq("subRule.hierarchy.dpKey", Long.valueOf(sDPKey)));

	       DistinctIterable<Integer> outputDisp = mColl.distinct("_id.clientKey", MatchFilter, Integer.class);

	       MongoCursor<Integer> Itr = outputDisp.iterator();

	       while (Itr.hasNext()) {

	           int iClientKey = Itr.next();
	           
	           System.out.println(iClientKey);
	           Bson subMatchFilter = Filters.and(
	           Filters.eq("disposition.desc", "No Disposition"),
	       Filters.eq("_id.clientKey",Long.valueOf(iClientKey)),
	           Filters.eq("subRule.hierarchy.dpKey", Long.valueOf(sDPKey)),
	           Filters.eq("subRule.hierarchy.medPolicyDesc",MedicalPolicy),
	       Filters.eq("subRule.hierarchy.topicDesc",Topic),
	       Filters.eq("source",Src),
	           Filters.eq("ruleInBaseLine", 0));

	           // cursor = mColl.find().iterator();
	           System.out.println(subMatchFilter);
	           FindIterable<Document> subresults = mColl.find(subMatchFilter);
	           long subrecordsCount = mColl.count(subMatchFilter);
	            
	           if(subrecordsCount>=3){
	               
	           for (Document document : subresults) {
	               Document ID_Doccument = document.get("_id", Document.class);
	               Document Subrule_Doccument = document.get("subRule", Document.class);
	               Document hierarchy_Doccument = Subrule_Doccument.get("hierarchy", Document.class);
	               
	               String sPayerShort = ID_Doccument.get("payerShort").toString();
	               String sClient = document.get("clientDesc").toString();
	               String sClaimType = ID_Doccument.get("claimType").toString();
	               String sInsuranceDesc = document.get("insuranceDesc").toString();
	               
	               String strPPS = sPayerShort+"-"+sInsuranceDesc+"-"+sClaimType;
	               
	               arPPS.add(strPPS);
	               
	               if (arPPS.size()==3){
	            	   Serenity.setSessionVariable("clientkey").to(String.valueOf(iClientKey));
	            	   Serenity.setSessionVariable("client").to(String.valueOf(sClient));
	            	   Serenity.setSessionVariable("pps").to(String.join(";", arPPS));
	            	   System.out.println(String.join(";", arPPS));
	                  return arPPS;
	               }
	           }
	         }            
	       }
	       return null;
	    }

	//Chaitanya eLL methods ######################################################################################
	
		//2nd type of eLL dps
		public static boolean rvaDPsnotpartofoppty(String clientkey)
		{
			boolean bstatus=false;
			ArrayList<Long> idpKeyList=new ArrayList<>();
		ArrayList<Long> finaldpKeyList=new ArrayList<>();
		ArrayList<Long> payerKeyList=new ArrayList<>();
		//Method to connect mongoDB
		ConnectTogivenDBandCollection("cpd", "oppty");
		Bson MatchFilter = new BsonDocument();
		//Filter to form a match query based on inputs
		MatchFilter = Filters.and(Filters.eq("_id.clientKey", Long.valueOf(clientkey)));
		recordsCount = mColl.count(MatchFilter);
		System.out.println("RecordCount::"+recordsCount+",for matchfilter==>"+MatchFilter);
		Distinctresults_with_long=mColl.distinct("subRule.hierarchy.dpKey", MatchFilter,Long.class);
		for (Long dpkey : Distinctresults_with_long) 
		{
			idpKeyList.add(dpkey);
		}
		System.out.println("dpKeySize::"+idpKeyList.size());
		//Method to connect mongoDB
		ConnectTogivenDBandCollection("cpd", "ellHierarchy");
		//Filter to form a match query based on inputs
		MatchFilter = Filters.and(Filters.eq("rvaPayerSwitch", -1),
				Filters.in("subRule.hierarchy.midRuleClaimTypes", ProjectVariables.DB_claimtypeList),
				Filters.nin("_id.dpKey", idpKeyList));
		Distinctresults_with_long=mColl.distinct("_id.dpKey", MatchFilter,Long.class);
		for (Long dpkey : Distinctresults_with_long) 
		{
			finaldpKeyList.add(dpkey);
		}
		System.out.println("2nd type of dpkeys count::"+finaldpKeyList.size()+",clientkey::"+clientkey);
		if(!finaldpKeyList.isEmpty())
		{
			for (Long dp : finaldpKeyList) 
			{
				//To get pps from the dpkey,clientkey
				bstatus=getPPSfromthecdm(String.valueOf(dp), clientkey);
				if(bstatus)
				{
					break;
				}
			}
			
			System.out.println("client::"+Serenity.sessionVariableCalled("client"));
			System.out.println("DPkey::"+Serenity.sessionVariableCalled("DPkey"));
			System.out.println("Medicalpolicy::"+Serenity.sessionVariableCalled("Medicalpolicy"));
			System.out.println("Topic::"+Serenity.sessionVariableCalled("Topic"));
			System.out.println("PPS::"+Serenity.sessionVariableCalled("pps"));
			
		}
		
		return bstatus;
		}
		
		//3rd type of eLL dps
		public static void rvaDPsPartofeLLandOppty()
		{
			boolean bstatus=false;
			String client=null;
			String clientkey=null;
			String Medicalpolicy=null;
			String Topic=null;
			String payershorts=null;
			String insurance=null;
			String claimtypes=null;
			String payerkeys=null;
			String insurancekeys=null;
			String dpkey=null;
			String rvaPayerswitch=null;
			AggregateIterable<Document> output=null;
			ArrayList<Long> idpKeyList=new ArrayList<>();
			List<String> rvaPayerswitchList=new ArrayList<>();
			List<String> payershortList=new ArrayList<>();
			List<String> claimtypelist=new ArrayList<>();
			Bson MatchFilter = new BsonDocument();
			
			ArrayList<String> arPPS=new ArrayList<>();
			
			//Method to connect mongoDB
			ConnectTogivenDBandCollection("cpd", "ellHierarchy");
			//Bson projectionFilter = Filters.and(projectionfields);
			//FindIterable<Document> outputDisp = mColl.find().projection(projectionFilter);
			output = mColl.aggregate(
					Arrays.asList(
					//Aggregates.project(Projections.fields(Projections.excludeId(),Projections.include("subRule.hierarchy.dpKey","rvaPayerSwitch"))),
					Aggregates.group("$subRule.hierarchy.dpKey",
					Accumulators.addToSet("rvaPayerswitch", "$rvaPayerSwitch")
					))).allowDiskUse(true);
			
			for (Document document : output) {
				
				rvaPayerswitch=StringUtils.substringBetween(String.valueOf(document), "rvaPayerswitch=[", "]}}");
				dpkey=StringUtils.substringBetween(String.valueOf(document), "_id=", ", rvaPayerswitch");
				rvaPayerswitchList=Arrays.asList(rvaPayerswitch.split(","));
				if(rvaPayerswitchList.size()>1)
				{
					idpKeyList.add(Long.valueOf(dpkey));
				}
						
			}
			
			System.out.println("3rd type of DPkeysize::"+idpKeyList.size());
			
			//#################### get the client data from the dplist ##############################################################
			
			//Method to connect mongoDB
			ConnectTogivenDBandCollection("cpd", "oppty");
			for (Long idpkey : idpKeyList) 
			{
				//Filter to form a match query based on inputs
				MatchFilter = Filters.and(Filters.eq("subRule.hierarchy.dpKey", idpkey),
						Filters.eq("disposition.desc", "No Disposition"),
						Filters.eq("ruleInBaseLine", 0)
						//Filters.eq("source",Src)
						);
				Bson matchtext = Aggregates.match(Filters.and(MatchFilter));
				output = mColl.aggregate(
						Arrays.asList(
						matchtext,
						Aggregates.group(
								new Document().append("DPKey", "$subRule.hierarchy.dpKey").append("client", "$clientDesc"),
						Accumulators.addToSet("clientkey", "$_id.clientKey"),
						Accumulators.addToSet("payershort", "$_id.payerShort"),
						Accumulators.addToSet("Medicalpolicy", "$subRule.hierarchy.medPolicyDesc"),
						Accumulators.addToSet("Topic", "$subRule.hierarchy.topicDesc")
						))).allowDiskUse(true);
				for (Document doc : output) 
				{
					
				   client=StringUtils.substringBetween(String.valueOf(doc), "client=", "}}, clientkey");
				   dpkey=StringUtils.substringBetween(String.valueOf(doc), "DPKey=", ", client");
				   clientkey=StringUtils.substringBetween(String.valueOf(doc), "clientkey=[", "], payershort");
				   payershorts=StringUtils.substringBetween(String.valueOf(doc), "payershort=[", "], Medicalpolicy");
				  Medicalpolicy=StringUtils.substringBetween(String.valueOf(doc), "Medicalpolicy=[", "], Topic");
				   Topic=StringUtils.substringBetween(String.valueOf(doc), "Topic=[", "]}}");
				   payershortList=Arrays.asList(payershorts.split(","));
				   if(payershortList.size()>2)
				   {
					   bstatus=true;
					   Serenity.setSessionVariable("client").to(client);
					   Serenity.setSessionVariable("clientkey").to(clientkey);
					   Serenity.setSessionVariable("Medicalpolicy").to(Medicalpolicy);
					   Serenity.setSessionVariable("Topic").to(Topic);
					   Serenity.setSessionVariable("DPkey").to(dpkey);
					   break;
					  
				   }
				}
				if(bstatus)
				{
					break;
				}
				
			}
			//####################################### To retrieve the exact PPS ###########################################3
			
			//To retrieve the PPS from the given dpkey and clientkey
			getPPSfromthegiven(dpkey,clientkey);
			
			System.out.println("client::"+client);
			System.out.println("DPkey::"+dpkey);
			System.out.println("Medicalpolicy::"+Medicalpolicy);
			System.out.println("Topic::"+Topic);
			System.out.println("PPS::"+Serenity.sessionVariableCalled("pps"));
			
			
			
			
		}

		private static void getPPSfromthegiven(String dpkey, String clientkey) 
		{
			Bson MatchFilter = new BsonDocument();
			
			HashSet<String> arPPS=new HashSet<String>();
			
			//Method to connect mongoDB
			ConnectTogivenDBandCollection("cpd", "oppty");
			//Filter to form a match query based on inputs
			MatchFilter = Filters.and(Filters.eq("_id.clientKey", Long.valueOf(clientkey)),
					Filters.eq("disposition.desc", "No Disposition"),
					Filters.eq("subRule.hierarchy.dpKey", Long.valueOf(dpkey)),
					Filters.eq("ruleInBaseLine", 0)
					//Filters.eq("source",Src)
					);
			results= mColl.find(MatchFilter);
			recordsCount = mColl.count(MatchFilter);
			System.out.println("RecordCount::"+recordsCount+",for matchfilter==>"+MatchFilter);
			for (Document doc : results) 
			{
				 Document ID_Doccument = doc.get("_id", Document.class);
	             Document Subrule_Doccument = doc.get("subRule", Document.class);
	             Document hierarchy_Doccument = Subrule_Doccument.get("hierarchy", Document.class);
	             
	             String sPayerShort = ID_Doccument.get("payerShort").toString();
	             String sClient = doc.get("clientDesc").toString();
	             String sClaimType = ID_Doccument.get("claimType").toString();
	             String sInsuranceDesc = doc.get("insuranceDesc").toString();
	             Long sMedKey = hierarchy_Doccument.getLong("medPolicyKey");
	             Long sTopicKey = hierarchy_Doccument.getLong("topicKey");
	             Long sReleaseKey = doc.getLong("releaseLogKey");
	             Long sMidRuleKey = hierarchy_Doccument.getLong("midRuleKey");
	             
	             String strPPS = sPayerShort+"-"+sInsuranceDesc+"-"+sClaimType;
	            
	             arPPS.add(strPPS);
	             
	             if (arPPS.size()==3){
	          	   Serenity.setSessionVariable("clientkey").to(String.valueOf(clientkey));
	          	   Serenity.setSessionVariable("client").to(String.valueOf(sClient));
	          	   Serenity.setSessionVariable("TopicKey").to(String.valueOf(sTopicKey));
	          	   Serenity.setSessionVariable("mpKey").to(String.valueOf(sMedKey));
	          	   Serenity.setSessionVariable("releaseLogKey").to(String.valueOf(sReleaseKey));
	          	   Serenity.setSessionVariable("midRuleKey").to(String.valueOf(sMidRuleKey));
	          	   Serenity.setSessionVariable("pps").to(String.join(";", arPPS));
	          	   System.out.println(String.join(";", arPPS));
	          	   break;
	             }
			}
			
		}
		
		public static boolean getPPSfromthecdm(String dpkey, String clientkey) 
		{
			boolean bstatus=false;
			Bson MatchFilter = new BsonDocument();
			Serenity.setSessionVariable("clientkey").to(String.valueOf(clientkey));
			
			ArrayList<String> arPPS=new ArrayList<>();
			ArrayList<Long> payerkeys=new ArrayList<>();
			List<String> cdmdecisionOpportunity=Arrays.asList(ProjectVariables.OpportunityLCDFilterOptions.split(","));
			
			payerkeys.addAll(getTheDistinctPayerkeysBasedOnClient(clientkey));
			
			//Method to connect mongoDB
			ConnectTogivenDBandCollection("cdm", "latestDecision");
			
			//Filter to form a match query based on inputs
			MatchFilter = Filters.and(Filters.in("_id.payerKey", payerkeys),
					Filters.eq("_id.dpKey", Long.valueOf(dpkey)),
					Filters.in("decStatusDesc", cdmdecisionOpportunity)
					);
			results= mColl.find(MatchFilter);
			recordsCount = mColl.count(MatchFilter);
			System.out.println("RecordCount::"+recordsCount+",for matchfilter==>"+MatchFilter);
			if(recordsCount==0)
			{
				return false;
			}
			else
			{
				for (Document doc : results) 
				{
					 Document ID_Doccument = doc.get("_id", Document.class);
					 
					 Long payerkey=ID_Doccument.getLong("payerKey");
					 Long inskey=ID_Doccument.getLong("insuranceKey");
		            
		             String sPayerShort = Retrieve_the_PayerKey_From_mongodb_with_the_given_Payershort(String.valueOf(payerkey), "PayerKey");
		             String sClaimType = ID_Doccument.get("claimType").toString();
		             String sInsuranceDesc = GenericUtils.Retrieve_the_insuranceDesc_from_insuranceKey(String.valueOf(inskey));
		             
		             String strPPS = sPayerShort+"-"+sInsuranceDesc+"-"+sClaimType;
		            
		             arPPS.add(strPPS);
		             
		             if (arPPS.size()==3)
		             {
		            	 Serenity.setSessionVariable("DPkey").to(dpkey);
		            	 Serenity.setSessionVariable("client").to(Retrieve_the_PayerKey_From_mongodb_with_the_given_Payershort(String.valueOf(clientkey), "ClientKey"));
		            	 Serenity.setSessionVariable("pps").to(String.join(";", arPPS));
		          	   System.out.println(String.join(";", arPPS));
		          	   
			          	//Method to connect mongoDB
			       		ConnectTogivenDBandCollection("cpd", "ellHierarchy");
			       		//Filter to form a match query based on inputs
			       		MatchFilter = Filters.and(Filters.eq("_id.dpKey", Long.valueOf(dpkey)));
			       		results= mColl.find(MatchFilter);
			       		for (Document doc2 : results) 
			       		{
			       		 Document iD_Doccument = doc2.get("_id", Document.class);
			       		 Document subdoc = doc2.get("subRule", Document.class);
			       		 Document hierardoc = subdoc.get("hierarchy", Document.class);
			       		
						 String MP=hierardoc.getString("medPolicyDesc");
						 String Topic=hierardoc.getString("topicDesc");
						 String MPKey=String.valueOf(hierardoc.getLong("medPolicyKey"));
						 String TpKey= String.valueOf(hierardoc.getLong("topicKey"));
						 String MidRule= String.valueOf(hierardoc.getLong("midRuleKey"));
						 String sReleaseLog = String.valueOf(iD_Doccument.getLong("releaseLogKey"));
						 Serenity.setSessionVariable("Medicalpolicy").to(MP);
						 Serenity.setSessionVariable("Topic").to(Topic);
						 Serenity.setSessionVariable("releaseLogKey").to(sReleaseLog);
						 Serenity.setSessionVariable("midRuleKey").to(MidRule);
						 Serenity.setSessionVariable("mpKey").to(MPKey);
						 Serenity.setSessionVariable("TopicKey").to(TpKey);
						 }
		          	   return true;
		             }
				}
			}
			
			return false;
			
		}
public static ArrayList<String> userRetrievesChangeSummaryData(Bson MatchFilter,String sValidationType) {
			
			ArrayList<String> changeData = new ArrayList<String>();
			AggregateIterable<Document> output = null;
			String dpKey;
			// To Connect to MongoDb
			retrieveAllDocuments("cpd", "changeOpportunity");
			results = mColl.find(MatchFilter).sort(new BasicDBObject("dpKey",1));
			recordsCount = mColl.count(MatchFilter);

			System.out.println("Filtered Record Count ===>" + recordsCount);

			if (recordsCount == 0) {
				Assert.assertTrue("Record count was zero for the given match filter ==>" + MatchFilter, false);
			}
						
			Bson matchtext = Aggregates.match(Filters.and(MatchFilter));

			switch (sValidationType.toUpperCase()) {

			case "ALL":
				output = mColl.aggregate(Arrays.asList(matchtext,
						Aggregates.group("$dpKey",
								Accumulators.addToSet("dpStatus", "$ellChange"),
								Accumulators.addToSet("dpRecat", "$dpChange.recategorized"),
								Accumulators.addToSet("topicChange", "$topicChange"),
								Accumulators.addToSet("descrChange", "$dpChange.description"),
								Accumulators.addToSet("ctChange", "$ruleChange.claimTypes"),
								Accumulators.addToSet("refChange", "$ruleChange.reference"),
								Accumulators.addToSet("newRules", "$newRules"),
								Accumulators.addToSet("recatRules", "$ruleChange.recategorized"),
								Accumulators.addToSet("verRules", "$ruleChange.version"),
								Accumulators.addToSet ("ruleChange","$ruleChange.status"),
								Accumulators.addToSet ("midRule","$_id.midRuleKey"),
								Accumulators.addToSet("deactRules", "$deactRules"))));

				for (Document Doc : output) {
					System.out.println(String.valueOf(Doc));
		               dpKey = StringUtils.substringBetween(Doc.toString(), "_id=", ", dpStatus");
		               String dpStatus = StringUtils.substringBetween(Doc.toString(), "dpStatus=[", "],");
		               String dpRecat = String.valueOf(StringUtils.substringBetween(Doc.toString(), "dpRecat=[", "], topicChange").toString().isEmpty() ? false : true) ;
		               String topicChange = String.valueOf(StringUtils.substringBetween(Doc.toString(), "topicChange=[", "], descrChange").toString().isEmpty() ? false : true) ;
		               String descrChange = String.valueOf(StringUtils.substringBetween(Doc.toString(), "descrChange=[", "], ctChange").toString().isEmpty() ? false : true);
		               String ctChange = String.valueOf(StringUtils.substringBetween(Doc.toString(), "ctChange=[", "], refChange").toString().isEmpty() ? false : true);
		               String refChange = String.valueOf(StringUtils.substringBetween(Doc.toString(), "refChange=[", "], newRules").toString().isEmpty() ? false : true);
		               String newRules= String.valueOf(StringUtils.substringBetween(Doc.toString(), "ruleChange=[", "], midRule").toString().equalsIgnoreCase("NEW") ? 
		            		   StringUtils.substringBetween(Doc.toString(), "midRule=[", "], deactRules") : "".length());
		               String recatRules= String.valueOf(StringUtils.substringBetween(Doc.toString(), "recatRules=[", "], verRules").length());
		               String verRules= String.valueOf(StringUtils.countMatches(Doc.get("verRules").toString(), "Document"));
		               String deactRules= String.valueOf(StringUtils.substringBetween(Doc.toString(), "ruleChange=[", "], midRule").toString().equalsIgnoreCase("DEACTIVATED") ? 
		            		   StringUtils.substringBetween(Doc.toString(), "midRule=[", "], deactRules") : "".length());
		               
		               String strPPS = dpKey+"-"+dpStatus+"-"+dpRecat+"-"+topicChange+"-"+descrChange+"-"+ctChange+"-"+refChange+"-"+newRules+"-"+recatRules+"-"+verRules+"-"+deactRules;
		               System.out.println(strPPS);
		               changeData.add(strPPS);
				}
				break;
			case "NEW RULE":
				output = mColl.aggregate(Arrays.asList(matchtext,
						Aggregates.group("$dpKey",
								Accumulators.addToSet("clientKey", "$_id.clientKey"),
								Accumulators.addToSet("newRules", "$newRules"),
								Accumulators.addToSet ("ruleChange","$ruleChange.status"),
								Accumulators.addToSet ("midRule","$_id.midRuleKey"))));
				for (Document Doc : output) {
					System.out.println(String.valueOf(Doc));
					dpKey = StringUtils.substringBetween(Doc.toString(), "_id=", ", clientKey");
					changeData.add(dpKey);
				}
				break;
			case "NEW MP":
				output = mColl.aggregate(Arrays.asList(matchtext,
						Aggregates.group("$dpKey",
								Accumulators.addToSet("clientKey", "$_id.clientKey"),
								Accumulators.addToSet("MPKey", "$policyChange.medPolicyKey"))));
				for (Document Doc : output) {
					System.out.println(String.valueOf(Doc));
					dpKey = StringUtils.substringBetween(Doc.toString(), "_id=", ", clientKey");
					changeData.add(dpKey);
				}
				break;
			case "NEW TOPIC":
				output = mColl.aggregate(Arrays.asList(matchtext,
						Aggregates.group("$dpKey",
								Accumulators.addToSet("clientKey", "$_id.clientKey"),
								Accumulators.addToSet("topicKey", "$topicChange.topicKey"))));
				for (Document Doc : output) {
					System.out.println(String.valueOf(Doc));
					dpKey = StringUtils.substringBetween(Doc.toString(), "_id=", ", clientKey");
					changeData.add(dpKey);
				}
				break;
			case "RECAT TOPIC":
				output = mColl.aggregate(Arrays.asList(matchtext,
						Aggregates.group("$dpKey",
								Accumulators.addToSet("clientKey", "$_id.clientKey"),
								Accumulators.addToSet("topicKey", "$topicChange.recategorized"))));
				for (Document Doc : output) {
					System.out.println(String.valueOf(Doc));
					dpKey = StringUtils.substringBetween(Doc.toString(), "_id=", ", clientKey");
					changeData.add(dpKey);
				}
				break;
			case "REFERENCE CHANGE":
				output = mColl.aggregate(Arrays.asList(matchtext,
						Aggregates.group("$dpKey",
								Accumulators.addToSet("clientKey", "$_id.clientKey"),
								Accumulators.addToSet("reference", "$ruleChange.reference"))));
				for (Document Doc : output) {
					System.out.println(String.valueOf(Doc));
					dpKey = StringUtils.substringBetween(Doc.toString(), "_id=", ", clientKey");
					changeData.add(dpKey);
				}
				break;
			case "RECATEGORIZED RULE":
				output = mColl.aggregate(Arrays.asList(matchtext,
						Aggregates.group("$dpKey",
								Accumulators.addToSet("clientKey", "$_id.clientKey"),
								Accumulators.addToSet("reference", "$ruleChange.recategorized"))));	
				for (Document Doc : output) {
					System.out.println(String.valueOf(Doc));
					dpKey = StringUtils.substringBetween(Doc.toString(), "_id=", ", clientKey");
					changeData.add(dpKey);
				}
				break;
			case "CLAIM TYPE ADDED":
			case "CLAIM TYPE REMOVED":
				output = mColl.aggregate(Arrays.asList(matchtext,
						Aggregates.group("$dpKey",
								Accumulators.addToSet("clientKey", "$_id.clientKey"),
								Accumulators.addToSet("claimType", "$ruleChange.claimTypes"))));	
				for (Document Doc : output) {
					System.out.println(String.valueOf(Doc));
					dpKey = StringUtils.substringBetween(Doc.toString(), "_id=", ", clientKey");
					changeData.add(dpKey);
				}
				break;
			case "NEW DP":
			case "DEACT DP":
			case "CHANGE DP DESC":
					output = mColl.aggregate(Arrays.asList(matchtext,
							Aggregates.group("$dpKey",
									Accumulators.addToSet("clientKey", "$_id.clientKey"))));							
				for (Document Doc : output) {
					System.out.println(String.valueOf(Doc));
					dpKey = StringUtils.substringBetween(Doc.toString(), "_id=", ", clientKey");
					changeData.add(dpKey);
				}				
				break;
			case "RECAT DP":
				output = mColl.aggregate(Arrays.asList(matchtext,
						Aggregates.group("$dpKey",
								Accumulators.addToSet("dpRecat", "$dpChange.recategorized"))));
				for (Document Doc : output) {
				   dpKey = StringUtils.substringBetween(Doc.toString(), "_id=", ", dpRecat");
				   String dpRecat = String.valueOf(StringUtils.substringBetween(Doc.toString(), "dpRecat=[", "]").toString().isEmpty() ? false : true) ;
				   Serenity.setSessionVariable(dpKey+sValidationType).to(dpRecat);
	               changeData.add(dpKey);
				}
				break;
			case "DEACTIVATED POLICY":
				output = mColl.aggregate(Arrays.asList(matchtext,
						Aggregates.group("$dpKey",
								Accumulators.addToSet ("ruleChange","$ruleChange.status"),
								Accumulators.addToSet ("midRule","$_id.midRuleKey"),
								Accumulators.addToSet("deactRules", "$deactRules"))));
				for (Document Doc : output) {
					System.out.println(String.valueOf(Doc));
					dpKey = StringUtils.substringBetween(Doc.toString(), "_id=", ", ruleChange");
					String deactRules= String.valueOf(StringUtils.substringBetween(Doc.toString(), "ruleChange=[", "], midRule").toString().equalsIgnoreCase("DEACTIVATED") ? 
							StringUtils.substringBetween(Doc.toString(), "midRule=[", "], deactRules").split(",").length : "".length());
					Serenity.setSessionVariable(dpKey+sValidationType).to(deactRules);
					changeData.add(dpKey);
				}
				break;
			case "VERSION CHANGE":
				output = mColl.aggregate(Arrays.asList(matchtext,
						Aggregates.group("$dpKey",
								Accumulators.addToSet("clientKey", "$_id.clientKey"),
								Accumulators.addToSet ("midRule","$_id.midRuleKey"),
								Accumulators.addToSet("version", "$ruleChange.version"))));
				for (Document Doc : output) {
					System.out.println(String.valueOf(Doc));
					dpKey = StringUtils.substringBetween(Doc.toString(), "_id=", ", clientKey");
					String verRules= String.valueOf(StringUtils.substringBetween(Doc.toString(), "midRule=[", "], version").split(",").length);
					Serenity.setSessionVariable(dpKey+sValidationType).to(verRules);
					changeData.add(dpKey);
				}
				
				break;
					
			
			
		}
			return changeData;
		}	

public static boolean getChangePresentationProfileStatus(String dpKey,String clientkey,String profileID)
{
	boolean bstatus=false;
	//Method to connect mongoDB
	retrieveAllDocuments("cpd", "changeOpportunity");

	Bson MatchFilter = new BsonDocument();
			
	//Filter to form a match query based on inputs
	if(dpKey.isEmpty()){
		MatchFilter = Filters.and(Filters.eq("_id.dpKey", dpKey),
				Filters.eq("_id.clientKey", Long.valueOf(clientkey)),
				Filters.eq("presentationProfile.profileName", profileID),
				Filters.eq("opptyFinalize.isFinalized", true));
	}else{
		MatchFilter = Filters.and(Filters.eq("_id.clientKey", Long.valueOf(clientkey)),
				Filters.eq("presentationProfile.profileName", profileID),
				Filters.eq("opptyFinalize.isFinalized", true));
	}
	
	
	recordsCount = mColl.count(MatchFilter);
	
	System.out.println("RecordCount::"+recordsCount+",for matchfilter==>"+MatchFilter);
	if(recordsCount==1)
	{
		bstatus=true;
		return bstatus;
	}
	else
	{
		return bstatus;
	}
}

	//get Duplicate DP/MP/Topic Desc records
	public static void getDuplicateDesc(String clientkey,String collection,String Disposition,String criteria) throws ParseException
	{
		boolean bstatus=false;
		int i=0;
		String sDesc=null;
		AggregateIterable<Document> output=null;
		List<String> descList=null;
		String sDPkey=null;
		String sPayershort=null;
		String sClaimtype=null;
		String sinsurance=null;
		String sDisposition=null;
		String sLatestdataversion=null;
		String sSubrule=null;
		String sPresentation=null;
		ArrayList<String> dates=new ArrayList();
		
		//Method to connect mongoDB
		ConnectTogivenDBandCollection("cpd", collection);
	
		Bson MatchFilter = new BsonDocument();
				
		//Filter to form a match query based on inputs
		MatchFilter = Filters.and(Filters.eq("_id.clientKey", Long.valueOf(clientkey)),
				Filters.eq("disposition.desc", Disposition));
		
		recordsCount = mColl.count(MatchFilter);
		
		//System.out.println("RecordCount::"+recordsCount+",for matchfilter==>"+MatchFilter);
		Bson matchtext = Aggregates.match(Filters.and(MatchFilter));

		//Aggregate filter to retrieve the output
		if(Disposition.equalsIgnoreCase("Present")||Disposition.equalsIgnoreCase("Complete")||Disposition.equalsIgnoreCase("Defer"))
		{
			output = mColl.aggregate(Arrays.asList(matchtext,Aggregates.group(new Document().
					append("DPKey", "$subRule.hierarchy.dpKey").
					append("PayerShort", "$_id.payerShort").
					append("Disposition", "$disposition.desc").
					append("Presentation", "$presentationProfile.profileName").
					append("Insurance", "$insuranceDesc").
					append("Claimtype", "$_id.claimType").
					append("subruleid", "$_id.subRuleId"), 
					Accumulators.addToSet(criteria, "$subRule.hierarchy."+criteria+"")))
					).allowDiskUse(true);
		}
		else
		{
			output = mColl.aggregate(Arrays.asList(matchtext,Aggregates.group(new Document().
					append("DPKey", "$subRule.hierarchy.dpKey").
					append("PayerShort", "$_id.payerShort").
					append("Disposition", "$disposition.desc").
					append("Insurance", "$insuranceDesc").
					append("Claimtype", "$_id.claimType").
					append("subruleid", "$_id.subRuleId"),
					Accumulators.addToSet(criteria, "$subRule.hierarchy."+criteria+"")))
					).allowDiskUse(true);
		}
		
		for (Document document : output) 
		{
			//System.out.println(document);
			sDesc=StringUtils.substringBetween(String.valueOf(document), ""+criteria+"=[", "]}}");
			descList=Arrays.asList(sDesc.split(","));
			sDPkey=StringUtils.substringBetween(String.valueOf(document), "DPKey=", ", PayerShort");
			
			if(descList.size()>1)
			{
				i=i+1;
				//System.out.println(String.valueOf(document));	
				sDPkey=StringUtils.substringBetween(String.valueOf(document), "DPKey=", ", PayerShort");
				sPayershort=StringUtils.substringBetween(String.valueOf(document), "PayerShort=", ", Disposition");
				sinsurance=StringUtils.substringBetween(String.valueOf(document), "Insurance=", ", Claimtype");
				sClaimtype=StringUtils.substringBetween(String.valueOf(document), "Claimtype=", ", subruleid");
				sSubrule=StringUtils.substringBetween(String.valueOf(document), "subruleid=", "}}, Dataversion");
				
				if(Disposition.equalsIgnoreCase("Present")||Disposition.equalsIgnoreCase("Complete")||Disposition.equalsIgnoreCase("Defer"))
				{
					sDisposition=StringUtils.substringBetween(String.valueOf(document), "Disposition=", ", Presentation");
					sPresentation=StringUtils.substringBetween(String.valueOf(document), "Presentation=", "}}, Dataversion");
					
				}
				else
				{
					sDisposition=StringUtils.substringBetween(String.valueOf(document), "Disposition=", ", Insurance");	
					
				}
				
				
				if(Disposition.equalsIgnoreCase("Present")||Disposition.equalsIgnoreCase("Complete")||Disposition.equalsIgnoreCase("Defer")&&String.valueOf(document).contains("Presentation"))
				{
					ProjectVariables.DuplicatePPS.add("Clientkey-"+clientkey+";DP-"+sDPkey+";Payershort-"+sPayershort+";Insurance-"+sinsurance+";Claimtype:"+sClaimtype+";Subrule-"+sSubrule+";Disposition-"+sDisposition+";Presentation"+sPresentation);
				}
				else
				{
					ProjectVariables.DuplicatePPS.add("Clientkey-"+clientkey+";DP-"+sDPkey+";Payershort-"+sPayershort+";Insurance-"+sinsurance+";Claimtype:"+sClaimtype+";Subrule-"+sSubrule+";Disposition-"+sDisposition);	
				}
				
				dates.clear();
			}
		}
		
		
		GenericUtils.Verify("PPS Count is '"+i+"',which are having duplicate "+criteria+" in '"+collection+"' collection for disposition::"+Disposition+",ClientKey::"+clientkey,true);
		//System.out.println("PPS Count is '"+i+"',which are having duplicate dataversions in '"+collection+"' collection for disposition==>"+Disposition+",ClientKey==>"+clientkey);
		
		Serenity.recordReportData().asEvidence().withTitle("PPS Count is '"+i+"',which are having duplicate "+criteria+" in '"+collection+"' collection for disposition::"+Disposition+",ClientKey::"+clientkey).andContents("Passed");
		
		/*for (String dppps : ProjectVariables.DuplicatePPS) 
		{
			Serenity.recordReportData().asEvidence().withTitle("Duplicate DP+PPS::"+dppps).andContents("Passed");
		}*/
		
	}


	//#####################################################################################################################################################################
	//Method to set data for Change Opportunities
	public static boolean getDataforgivenChangeoppty(String clientkey,String changeoppty)
	{
		int i=0;
	boolean bstatus=false;
	String dpkey=null;
	String subrulekey=null;
	String MP=null;
	String topic=null;
	String dpdesc=null;
	String currentdpdesc=null;
	String midrulekey=null;
	String topicKey=null;
	String mpKey=null;
	String clientDesc=null;
	AggregateIterable<Document> output=null;
	ArrayList<String> ellList = new ArrayList<String>();
	
	//Method to connect mongoDB
	ConnectTogivenDBandCollection("cpd", "changeOpportunity");
	Bson MatchFilter = new BsonDocument();
	if(changeoppty.equalsIgnoreCase("RULE CHANGE"))
	{
		//Filter to form a match query based on inputs
		MatchFilter = Filters.and(Filters.eq("_id.clientKey", Long.valueOf(clientkey)),
					//Filters.eq("ellChange", changeoppty),
					//Filters.nin("releaseInfo.releaseDate", "20200825"),
					Filters.exists("ruleChange.version", true),
					Filters.eq("presentationProfile", null));
	}
	else if(changeoppty.equalsIgnoreCase("CLAIM TYPE REMOVED")||changeoppty.equalsIgnoreCase("CLAIM TYPE ADDED"))
	{
		//Filter to form a match query based on inputs
				MatchFilter = Filters.and(Filters.eq("_id.clientKey", Long.valueOf(clientkey)),
							//Filters.in("ellChange", changeoppty),
							//Filters.nin("releaseInfo.releaseDate", "20200825"),
							Filters.eq("presentationProfile", null));
	}
	else
	{
		//Filter to form a match query based on inputs
		MatchFilter = Filters.and(Filters.eq("_id.clientKey", Long.valueOf(clientkey)),
					//Filters.eq("ellChange", changeoppty),
					//Filters.eq("dpKey", 4178l),
					//Filters.ne("releaseInfo.releaseName", "August 2020"),
					Filters.eq("presentationProfile", null));
	}
	
	recordsCount = mColl.count(MatchFilter);
	
	if(recordsCount==0)
	{
		System.out.println("No Dpkey is available for the given changetype '"+changeoppty+"',for clientkey '"+clientkey+"'");
		//System.out.println("RecordCount::"+recordsCount+",for matchfilter==>"+MatchFilter);
		return bstatus;
	}
	else
	{
		//System.out.println(MatchFilter);
		
		Bson matchtext = Aggregates.match(Filters.and(MatchFilter));
		
			output = mColl.aggregate(Arrays.asList(matchtext,Aggregates.group(
					new Document().append("DPKey", "$dpKey").append("subRuleKey", "$subRule.subRuleKey"),
					Accumulators.addToSet("MP","$medPolicyDesc"),
					Accumulators.addToSet("Topic","$topicDesc"),
					Accumulators.addToSet("PreviousDPDesc","$dpDescr"),
					Accumulators.addToSet("midRuleKey","$subRule.midRuleKey"),
					Accumulators.addToSet("mpKey","$medPolicyKey"),
					Accumulators.addToSet("topicKey","$topicKey"),
					Accumulators.addToSet("releaseName","$releaseInfo.releaseName"),
					Accumulators.addToSet("ellChange","$ellChange")
					))
					).allowDiskUse(true);
		
		
		for (Document doc : output) 
		{
			i=i+1;
			//System.out.println(doc);
			dpkey=StringUtils.substringBetween(String.valueOf(doc), "DPKey=", ", subRuleKey");
			subrulekey=StringUtils.substringBetween(String.valueOf(doc), "subRuleKey=", "}}, MP");
			MP=StringUtils.substringBetween(String.valueOf(doc), "MP=[", "], Topic=");
			topic=StringUtils.substringBetween(String.valueOf(doc), "Topic=[", "], PreviousDPDesc");
			dpdesc=StringUtils.substringBetween(String.valueOf(doc), "PreviousDPDesc=[", "], midRuleKey");
			midrulekey=StringUtils.substringBetween(String.valueOf(doc), "midRuleKey=[", "], mpKey");
			mpKey=StringUtils.substringBetween(String.valueOf(doc), "mpKey=[", "], topicKey");
			topicKey=StringUtils.substringBetween(String.valueOf(doc), "topicKey=[", "], releaseName");
			String release=StringUtils.substringBetween(String.valueOf(doc), "releaseName=[", "], ellChange").trim();
			String ellChange=StringUtils.substringBetween(String.valueOf(doc), "ellChange=[", "]}}").trim();
			
			if(!release.contains("August 2020")&&ellChange.contains(changeoppty))
			{
				System.out.println("Release::"+release);
				System.out.println("ellChange::"+ellChange);
				System.out.println("dpkey::"+dpkey);
				System.out.println("subrulekey::"+subrulekey);
				/*MatchFilter = Filters.and(Filters.eq("_id.clientKey", Long.valueOf(clientkey)),
						Filters.eq("_id.dpKey", Long.valueOf(dpkey)),
						Filters.eq("topicDesc", topic),
						Filters.nin("releaseInfo.releaseName", "August 2020"),
						Filters.eq("subRule.subRuleKey", Long.valueOf(subrulekey))
						);
				 matchtext = Aggregates.match(Filters.and(MatchFilter));
				
				 AggregateIterable<Document> output2 = mColl.aggregate(Arrays.asList(matchtext,Aggregates.group("$ellChange"))
						).allowDiskUse(true);
				
				
				for (Document doc2:output2) 
				{
					//System.out.println(doc);
					String ellchange=StringUtils.substringBetween(String.valueOf(doc2), "_id=", "}}");
					ellList.add(ellchange);
				}
				//System.out.println();
				System.out.println("ellchanges '"+ellList+"' for DPkey::"+dpkey+",subRuleKey::"+subrulekey);
				System.out.println("eLLChangesize::"+ellList.size());
				if(ellList.size()==1&&ellList.get(0).equalsIgnoreCase(changeoppty))*/
				{
					
					Serenity.setSessionVariable("clientkey").to(clientkey);
					Serenity.setSessionVariable("PREV_MP_TITLE").to(MP);
					Serenity.setSessionVariable("PREV_TOPIC_TITLE").to(topic);
					Serenity.setSessionVariable("PREV_DP_DESC").to(dpdesc.trim());
					Serenity.setSessionVariable("CUR_MID_RULE").to(midrulekey);
					Serenity.setSessionVariable("CUR_SUB_RULE_KEY").to(subrulekey);
					Serenity.setSessionVariable("PREV_DP").to(dpkey);
					Serenity.setSessionVariable("PREV_MP").to(mpKey);//mpkey
					Serenity.setSessionVariable("PREV_TOPIC").to(topicKey);//topickey
					bstatus=true;
					System.out.println("clientkey::"+Serenity.sessionVariableCalled("clientkey"));
					System.out.println("MP::"+Serenity.sessionVariableCalled("PREV_MP_TITLE"));
					System.out.println("Topic::"+Serenity.sessionVariableCalled("PREV_TOPIC_TITLE"));
					System.out.println("Dpkey::"+Serenity.sessionVariableCalled("PREV_DP"));
					System.out.println("DPDesc::"+Serenity.sessionVariableCalled("PREV_DP_DESC"));
					System.out.println("Midrulekey::"+Serenity.sessionVariableCalled("CUR_MID_RULE"));
					System.out.println("Subrulekey::"+Serenity.sessionVariableCalled("CUR_SUB_RULE_KEY"));
					System.out.println("MPKey::"+Serenity.sessionVariableCalled("PREV_MP"));
					System.out.println("TopicKey::"+Serenity.sessionVariableCalled("PREV_TOPIC"));
					System.out.println("clientdesc::"+Serenity.sessionVariableCalled("CLIENT_DESC"));
					
					MatchFilter = Filters.and(Filters.eq("_id.clientKey", Long.valueOf(clientkey)),
							Filters.eq("_id.dpKey", Long.valueOf(dpkey)),
							Filters.eq("ellChange", ellChange),
							Filters.eq("subRule.midRuleKey", Long.valueOf(midrulekey)),
							Filters.eq("subRule.subRuleKey", Long.valueOf(subrulekey)),
							Filters.eq("medPolicyDesc", MP),
							Filters.eq("topicDesc", topic),
							Filters.eq("presentationProfile", null));
				   recordsCount = mColl.count(MatchFilter);
				   System.out.println("PPS Count::"+recordsCount);
				   Serenity.setSessionVariable("PPScount").to(recordsCount);
					break;
				}
				
				//ellList.clear();
			
			}
			else
			{

				System.out.println("Getting releasename as '"+release+"'and ellchange as '"+ellChange+"',No Dpkey is available for the given changetype '"+changeoppty+"',for clientkey '"+clientkey+"'");
				//return bstatus;
				
			}
			
			
		}
	if(!bstatus)
	{
		System.out.println("No Dpkey is available for the given changetype '"+changeoppty+"',for clientkey '"+clientkey+"'");
	}
	
   //Serenity.setSessionVariable("Matchfilter").to(MatchFilter);
	return bstatus;
	
	}
	}
	
	//Method to verify data for Change Opportunities post pipeline 
	public static long verifyDataforgivenChangeoppty(String clientkey,String changeoppty,String criteria)
	{
		int i=0;
		boolean bstatus=false;
		String dpkey=Serenity.sessionVariableCalled("PREV_DP");
		Serenity.recordReportData().withTitle("dpKey").andContents(dpkey);
		String subrulekey=Serenity.sessionVariableCalled("CUR_SUB_RULE_KEY");
		String MP=Serenity.sessionVariableCalled("PREV_MP_TITLE");
		String topic=Serenity.sessionVariableCalled("PREV_TOPIC_TITLE");
		String Changeddpdesc=Serenity.sessionVariableCalled("CUR_DP_DESC");
		String midrulekey=Serenity.sessionVariableCalled("CUR_MID_RULE");
		String Prevdpdesc=Serenity.sessionVariableCalled("PREV_DP_DESC");
		 
		AggregateIterable<Document> output=null;
		
		//Method to connect mongoDB
		ConnectTogivenDBandCollection("cpd", "changeOpportunity");
		Bson MatchFilter = new BsonDocument();
		switch(criteria)
		{
		case "Unassigned":
			//Filter to form a match query based on inputs
			MatchFilter = Filters.and(Filters.eq("_id.clientKey", Long.valueOf(clientkey)),
					Filters.eq("_id.dpKey", Long.valueOf(dpkey)),
					Filters.eq("ellChange", changeoppty),
					Filters.eq("releaseInfo.releaseDate", Serenity.sessionVariableCalled("CUR_RELEASE_DATE")),
					Filters.eq("subRule.midRuleKey", Long.valueOf(midrulekey)),
					Filters.eq("subRule.subRuleKey", Long.valueOf(subrulekey)),
					Filters.eq("medPolicyDesc", MP),
					Filters.eq("topicDesc", topic),
					Filters.eq("dpChange.description.current", Changeddpdesc),
					Filters.eq("dpChange.description.previous", Prevdpdesc),
					Filters.eq("presentationProfile", null));
		break;
		case "Without RFP":
		case "With RFP":
			String presName=Serenity.sessionVariableCalled("PresentationName");
			//Filter to form a match query based on inputs
			MatchFilter = Filters.and(Filters.eq("_id.clientKey", Long.valueOf(clientkey)),
					Filters.eq("_id.dpKey", Long.valueOf(dpkey)),
					Filters.eq("ellChange", changeoppty),
					Filters.eq("releaseInfo.releaseDate", Serenity.sessionVariableCalled("CUR_RELEASE_DATE")),
					Filters.eq("subRule.midRuleKey", Long.valueOf(midrulekey)),
					Filters.eq("subRule.subRuleKey", Long.valueOf(subrulekey)),
					Filters.eq("medPolicyDesc", MP),
					Filters.eq("topicDesc", topic),
					Filters.eq("dpChange.description.current", Changeddpdesc),
					Filters.eq("dpChange.description.previous", Prevdpdesc),
					Filters.eq("presentationProfile.profileName", presName));
		break;
		default:
			GenericUtils.Verify("case not found::"+criteria, false);
			
		break;
		}
		
		
		recordsCount = mColl.count(MatchFilter);
		System.out.println("RecordCount::"+recordsCount+",for matchfilter==>"+MatchFilter);
		Serenity.setSessionVariable("Matchfilter").to(MatchFilter);
		
		return recordsCount;
		
		
	}
		
	public static boolean getTargetDataforgivenchange(String clientkey,String changeoppty)
	{
		int i=0;
		boolean bstatus=false;
		String dpkey=null;
		String subrulekey=null;
		String MP=null;
		String topic=null;
		String dpdesc=null;
		String currentdpdesc=null;
		String midrulekey=null;
		String topicKey=null;
		String mpKey=null;
		AggregateIterable<Document> output=null;
		
	//Method to connect mongoDB
	ConnectTogivenDBandCollection("cpd", "changeOpportunity");
	Bson MatchFilter = new BsonDocument();
	//Filter to form a match query based on inputs
	MatchFilter = Filters.and(Filters.eq("_id.clientKey", Long.valueOf(clientkey)),
				//Filters.eq("ellChange", changeoppty),
				//Filters.nin("releaseInfo.releaseName", "August 2020"),
				Filters.nin("topicKey", Long.valueOf(Serenity.sessionVariableCalled("PREV_TOPIC").toString())),
				Filters.eq("presentationProfile", null));
	recordsCount = mColl.count(MatchFilter);
	System.out.println("RecordCount::"+recordsCount+",for matchfilter==>"+MatchFilter);
	if(recordsCount==0)
	{
		System.out.println("No records found for given '"+changeoppty+"' ,MatchFilter::"+MatchFilter);
		return bstatus;
	}
	else
	{
		Bson matchtext = Aggregates.match(Filters.and(MatchFilter));
		
			output = mColl.aggregate(Arrays.asList(matchtext,Aggregates.group(
					new Document().append("DPKey", "$_id.dpKey").append("subRuleKey", "$subRule.subRuleKey"),
					Accumulators.addToSet("MP","$medPolicyDesc"),
					Accumulators.addToSet("Topic","$topicDesc"),
					Accumulators.addToSet("DPDesc","$dpDescr"),
					Accumulators.addToSet("midRuleKey","$subRule.midRuleKey"),
					Accumulators.addToSet("mpKey","$medPolicyKey"),
					Accumulators.addToSet("topicKey","$topicKey"),
					Accumulators.addToSet("releaseName","$releaseInfo.releaseName"),
					Accumulators.addToSet("ellChange","$ellChange")
					))
					).allowDiskUse(true);
		
		
		for (Document doc : output) 
		{
			i=i+1;
			//System.out.println(doc);
			dpkey=StringUtils.substringBetween(String.valueOf(doc), "DPKey=", ", subRuleKey");
			subrulekey=StringUtils.substringBetween(String.valueOf(doc), "subRuleKey=", "}}, MP");
			MP=StringUtils.substringBetween(String.valueOf(doc), "MP=[", "], Topic=");
			topic=StringUtils.substringBetween(String.valueOf(doc), "Topic=[", "], DPDesc");
			dpdesc=StringUtils.substringBetween(String.valueOf(doc), "DPDesc=[", "], midRuleKey");
			mpKey=StringUtils.substringBetween(String.valueOf(doc), "mpKey=[", "], topicKey");
			topicKey=StringUtils.substringBetween(String.valueOf(doc), "topicKey=[", "], releaseName");
			String release=StringUtils.substringBetween(String.valueOf(doc), "releaseName=[", "], ellChange").trim();
			String ellChange=StringUtils.substringBetween(String.valueOf(doc), "ellChange=[", "]}}").trim();
			
			if(!release.contains("August 2020")&&ellChange.contains(changeoppty))
			{
				System.out.println("Release::"+release);
				System.out.println("ellChange::"+ellChange);
				System.out.println("dpkey::"+dpkey);
				System.out.println("subrulekey::"+subrulekey);
				Serenity.setSessionVariable("clientkey").to(clientkey);
				Serenity.setSessionVariable("CUR_DP").to(dpkey);
				Serenity.setSessionVariable("CUR_DP_DESC").to(dpdesc);
				Serenity.setSessionVariable("CUR_TOPIC").to(topicKey);
				Serenity.setSessionVariable("CUR_TOPIC_TITLE").to(topic);
				
				bstatus=true;
				break;
			}
			
			
		}
		System.out.println("clientkey::"+Serenity.sessionVariableCalled("clientkey"));
		System.out.println("CUR_DP::"+Serenity.sessionVariableCalled("CUR_DP"));
		System.out.println("CUR_DP_DESC::"+Serenity.sessionVariableCalled("CUR_DP_DESC"));
		System.out.println("CUR_DP ellChange::"+changeoppty);
		System.out.println("CUR_TOPIC::"+Serenity.sessionVariableCalled("CUR_TOPIC"));
		System.out.println("CUR_TOPIC_TITLE::"+Serenity.sessionVariableCalled("CUR_TOPIC_TITLE"));
		return bstatus;
		
	}
	}

	//get distinct ellChanges based on clientkey
	public static ArrayList<String> getdistincteLlChanges(String clientkey)
	{
		ArrayList<String> ellList=new ArrayList<>();
		AggregateIterable<Document> output=null;
		//Method to connect mongoDB
		ConnectTogivenDBandCollection("cpd", "changeOpportunity");
		Bson MatchFilter = new BsonDocument();
		//Filter to form a match query based on inputs
		MatchFilter = Filters.and(Filters.eq("_id.clientKey", Long.valueOf(clientkey)),
					Filters.eq("presentationProfile", null));
		
		Bson matchtext = Aggregates.match(Filters.and(MatchFilter));
		
		output = mColl.aggregate(Arrays.asList(matchtext,Aggregates.group("$ellChange"))
				).allowDiskUse(true);
		
		
		for (Document doc:output) 
		{
			//System.out.println(doc);
			String ellchange=StringUtils.substringBetween(String.valueOf(doc), "_id=", "}}");
			ellList.add(ellchange);
		}
	System.out.println("ellList::"+ellList);
		return ellList;
	}

	//verify Target & Source DPs and corresponding PPS post DP DeActivation
	public static void verifyDPDeactiveChangePostpipeline(String dpkey,String criteria,String dptype,String expeLlchange)
	{
		AggregateIterable<Document> output=null;
		boolean bstatus=false;
		String deactDPkey=null;
		String deactDPStatus=null;
		String ruleChangestatus=null;
		String ruleChangePreviousDPkey=null;
		String ruleChangeCurrentDPkey=null;
		String ruleChangePreviousDPDesc=null;
		String ruleChangeCurrentDPDesc=null;
		String deactDPDesc=null;
		String ellChange=null;
		String presName=null;
		
		String clientkey=Serenity.sessionVariableCalled("clientkey");
		String targetDP=Serenity.sessionVariableCalled("CUR_DP");
		String midrulekey=Serenity.sessionVariableCalled("CUR_MID_RULE");
		String subrulekey=Serenity.sessionVariableCalled("CUR_SUB_RULE_KEY");
		
		//expected values
		String expdeactDPStatus="DEACTIVATED";
		String expdeactDPkey=Serenity.sessionVariableCalled("PREV_DP");
		String expruleChangestatus="CHANGED";
		String expruleChangePreviousDPkey=Serenity.sessionVariableCalled("PREV_DP");
		String expruleChangePreviousDPDesc=Serenity.sessionVariableCalled("PREV_DP_DESC");
		String expruleChangeCurrentDPDesc=Serenity.sessionVariableCalled("CUR_DP_DESC");
		//String expellChange="RECATEGORIZED RULE , DEACT DP";
		
		
		//Method to connect mongoDB
		ConnectTogivenDBandCollection("cpd", "changeOpportunity");
		Bson MatchFilter = new BsonDocument();
		
		//Matchfilter Code based on given criteria ####################################################
		switch(criteria)
		{
		
		case "Unassigned":
			//Filter to form a match query based on inputs
			MatchFilter = Filters.and(Filters.eq("_id.clientKey", Long.valueOf(clientkey)),
					Filters.eq("_id.midRuleKey", Long.valueOf(midrulekey)),
					Filters.eq("subRule.subRuleKey", Long.valueOf(subrulekey)),
					Filters.eq("releaseInfo.releaseDate", Serenity.sessionVariableCalled("CUR_RELEASE_DATE")),
					Filters.eq("dpKey", Long.valueOf(dpkey)),
						Filters.eq("presentationProfile", null));
		break;
		case "Without RFP":
			 presName=Serenity.sessionVariableCalled("PresentationName");
			 if(dptype.equalsIgnoreCase("Target"))
				{
					presName=null;
				}
				else
				{
					presName=presName;
				}
			//Filter to form a match query based on inputs
			MatchFilter = Filters.and(Filters.eq("_id.clientKey", Long.valueOf(clientkey)),
					Filters.eq("_id.midRuleKey", Long.valueOf(midrulekey)),
					Filters.eq("subRule.subRuleKey", Long.valueOf(subrulekey)),
					Filters.eq("dpKey", Long.valueOf(dpkey)),
					Filters.eq("releaseInfo.releaseDate", Serenity.sessionVariableCalled("CUR_RELEASE_DATE")),
						Filters.eq("presentationProfile.profileName", presName));
			
		break;
		case "With RFP":
			 presName=Serenity.sessionVariableCalled("PresentationName");
			//Filter to form a match query based on inputs
			MatchFilter = Filters.and(Filters.eq("_id.clientKey", Long.valueOf(clientkey)),
					Filters.eq("_id.midRuleKey", Long.valueOf(midrulekey)),
					Filters.eq("subRule.subRuleKey", Long.valueOf(subrulekey)),
					Filters.eq("dpKey", Long.valueOf(dpkey)),
					Filters.eq("ellChange", expeLlchange),
					Filters.eq("releaseInfo.releaseDate", Serenity.sessionVariableCalled("CUR_RELEASE_DATE")),
					Filters.eq("dpChange.status", expdeactDPStatus),
					Filters.eq("dpChange.dpKey", Long.valueOf(expdeactDPkey)),
					Filters.eq("ruleChange.recategorized.previous.dpKey", Long.valueOf(expruleChangePreviousDPkey)),
					Filters.eq("ruleChange.recategorized.current.dpKey", Long.valueOf(targetDP)),
						Filters.eq("presentationProfile.profileName", presName));
		break;
		default:
			GenericUtils.Verify("case not found::"+criteria, false);
			
		break;
		}
		
		//Aggregation Code based on given criteria ####################################################
		switch(criteria)
		{
		case "Unassigned":
		case "Without RFP":
			//results = mColl.find(MatchFilter);
			recordsCount = mColl.count(MatchFilter);
			//System.out.println(!(recordsCount==0));
			GenericUtils.Verify("Source DP validation,recordcount is '"+recordsCount+"',Matchfilter::"+MatchFilter, !(recordsCount==0));
			
			Bson matchtext = Aggregates.match(Filters.and(MatchFilter));
			
			output = mColl.aggregate(Arrays.asList(matchtext,Aggregates.group("$dpKey",
					Accumulators.addToSet("ellChange","$ellChange"),
					Accumulators.addToSet("dpStatus","$dpChange.status"),
					Accumulators.addToSet("deactDPkey","$dpChange.dpKey"),
					Accumulators.addToSet("ruleChangestatus","$ruleChange.status"),
					Accumulators.addToSet("ruleChangepreviosdpKey","$ruleChange.recategorized.previous.dpKey"),
					Accumulators.addToSet("ruleChangecurrentdpKey","$ruleChange.recategorized.current.dpKey"),
					Accumulators.addToSet("deactDPDesc","$dpChange.dpDesc"),
					Accumulators.addToSet("ruleChangepreviosdpDesc","$ruleChange.recategorized.previous.dpDesc"),
					Accumulators.addToSet("ruleChangecurrentdpDesc","$ruleChange.recategorized.current.dpDesc")
					))
					).allowDiskUse(true);
			
			
			for (Document doc:output) 
			{
				System.out.println(doc);
				deactDPkey=StringUtils.substringBetween(String.valueOf(doc), "deactDPkey=[", "], ruleChangestatus");
				deactDPStatus=StringUtils.substringBetween(String.valueOf(doc), "dpStatus=[", "], deactDPkey");
				ruleChangestatus=StringUtils.substringBetween(String.valueOf(doc), "ruleChangestatus=[", "], ruleChangepreviosdpKey");
				ruleChangePreviousDPkey=StringUtils.substringBetween(String.valueOf(doc), "ruleChangepreviosdpKey=[", "], ruleChangecurrentdpKey");
				ruleChangeCurrentDPkey=StringUtils.substringBetween(String.valueOf(doc), "ruleChangecurrentdpKey=[", "], deactDPDesc");
				ruleChangePreviousDPDesc=StringUtils.substringBetween(String.valueOf(doc), "ruleChangepreviosdpDesc=[", "], ruleChangecurrentdpDesc");
				ruleChangeCurrentDPDesc=StringUtils.substringBetween(String.valueOf(doc), "ruleChangecurrentdpDesc=[", "]}}");
				deactDPDesc=StringUtils.substringBetween(String.valueOf(doc), "deactDPDesc=[", "], ruleChangepreviosdpDesc");
				ellChange=StringUtils.substringBetween(String.valueOf(doc), "ellChange=[", "], dpStatus");
			}
		
			GenericUtils.Verify(""+dptype+" DP validation,Expected ellChange::"+expeLlchange+",Actual::"+ellChange+",post DPdeact,Matchfilter::"+MatchFilter, expeLlchange.equalsIgnoreCase(ellChange));
			GenericUtils.Verify(""+dptype+" DP validation,Expected Deactivation DPkey::"+expdeactDPkey+",Actual::"+deactDPkey+",post DPdeact,Matchfilter::"+MatchFilter, deactDPkey.equalsIgnoreCase(expdeactDPkey));
			GenericUtils.Verify(""+dptype+" DP validation,Expected Deactivation Status::"+expdeactDPStatus+",Actual::"+deactDPStatus+",post DPdeact,Matchfilter::"+MatchFilter, expdeactDPStatus.equalsIgnoreCase(deactDPStatus));
			GenericUtils.Verify(""+dptype+" DP validation,Expected RuleChange Status::"+expruleChangestatus+",Actual::"+ruleChangestatus+",post DPdeact,Matchfilter::"+MatchFilter, expruleChangestatus.equalsIgnoreCase(ruleChangestatus));
			GenericUtils.Verify(""+dptype+" DP validation,Expected Previous RulechangeDPkey::"+expruleChangePreviousDPkey+",Actual::"+ruleChangePreviousDPkey+",post DPdeact,Matchfilter::"+MatchFilter, expruleChangePreviousDPkey.equalsIgnoreCase(ruleChangePreviousDPkey));
			GenericUtils.Verify(""+dptype+" DP validation,Expected Current RulechangeDPkey::"+targetDP+",Actual::"+ruleChangeCurrentDPkey+",post DPdeact,Matchfilter::"+MatchFilter, targetDP.equalsIgnoreCase(ruleChangeCurrentDPkey));
			GenericUtils.Verify(""+dptype+" DP validation,Expected Previous RulechangeDPDesc::"+expruleChangePreviousDPDesc+",Actual::"+ruleChangePreviousDPDesc+",post DPdeact,Matchfilter::"+MatchFilter, expruleChangePreviousDPDesc.equalsIgnoreCase(ruleChangePreviousDPDesc));
			GenericUtils.Verify(""+dptype+" DP validation,Expected Current RulechangeDPDesc::"+expruleChangeCurrentDPDesc+",Actual::"+ruleChangeCurrentDPDesc+",post DPdeact,Matchfilter::"+MatchFilter, expruleChangeCurrentDPDesc.equalsIgnoreCase(ruleChangeCurrentDPDesc));
			GenericUtils.Verify(""+dptype+" DP validation,Expected Deactivation DPDesc::"+expruleChangePreviousDPDesc+",Actual::"+deactDPDesc+",post DPdeact,Matchfilter::"+MatchFilter, expruleChangePreviousDPDesc.equalsIgnoreCase(deactDPDesc));
			
			//verify data post pipeline for DPDeactivation change for Source DPKey ########################################################
			/*if(dptype.equalsIgnoreCase("Source"))
			{
				if(criteria.equalsIgnoreCase("Unassigned"))
				{
					presName=null;
				}
				else
				{
					presName=presName;
				}
				//Filter to form a match query based on inputs
				MatchFilter = Filters.and(Filters.eq("_id.clientKey", Long.valueOf(clientkey)),
						Filters.eq("_id.midRuleKey", Long.valueOf(midrulekey)),
						Filters.eq("subRule.subRuleKey", Long.valueOf(subrulekey)),
						Filters.eq("dpKey", Long.valueOf(dpkey)),
						Filters.eq("ellChange", expeLlchange),
						Filters.eq("releaseInfo.releaseDate", Serenity.sessionVariableCalled("CUR_RELEASE_DATE")),
						Filters.eq("dpChange.status", expdeactDPStatus),
						Filters.eq("ruleChange.status", expruleChangestatus),
							Filters.eq("presentationProfile.profileName", presName));
				recordsCount = mColl.count(MatchFilter);
				long expPPScount=Long.valueOf(Serenity.sessionVariableCalled("PPScount").toString());
				bstatus=recordsCount==expPPScount;
				GenericUtils.Verify("Source DP validation,Expected PPSCount:"+expPPScount+",Actual::"+recordsCount+",post DPdeact,Matchfilter::"+MatchFilter, bstatus);
				
			}*/
			
		break;
		case "With RFP":
			recordsCount = mColl.count(MatchFilter);
			long expPPScount=Long.valueOf(Serenity.sessionVariableCalled("PPScount").toString());
			bstatus=recordsCount==0;
			GenericUtils.Verify("Source DP validation,Expected PPSCount:0,Actual::"+recordsCount+",post DPdeact,Matchfilter::"+MatchFilter, bstatus);
			
		break;
		default:
			GenericUtils.Verify("case not found::"+criteria, false);
			
		break;
		}
	
	}	

	//verify New DP change post PIPELINE
	public static void verifyNewDPChangePostpipeline(String dpkey,String criteria,String dptype,String expeLlchange)
	{
		AggregateIterable<Document> output=null;
		boolean bstatus=false;
		String newDPkey=null;
		String newDPStatus=null;
		String ruleChangestatus=null;
		String ruleChangePreviousDPkey=null;
		String ruleChangeCurrentDPkey=null;
		String ruleChangePreviousDPDesc=null;
		String ruleChangeCurrentDPDesc=null;
		String newDPDesc=null;
		String ellChange=null;
		String presName=null;
		
		String clientkey=Serenity.sessionVariableCalled("clientkey");
		String newDP=Serenity.sessionVariableCalled("CUR_DP");
		String midrulekey=Serenity.sessionVariableCalled("CUR_MID_RULE");
		String subrulekey=Serenity.sessionVariableCalled("CUR_SUB_RULE_KEY");
		
		//expected values
		String expOLDDPkey=Serenity.sessionVariableCalled("PREV_DP");
		String expruleChangePreviousDPDesc=Serenity.sessionVariableCalled("PREV_DP_DESC");
		String expruleChangeCurrentDPDesc=Serenity.sessionVariableCalled("CUR_DP_DESC");
		
		
		//Method to connect mongoDB
		ConnectTogivenDBandCollection("cpd", "changeOpportunity");
		Bson MatchFilter = new BsonDocument();
		
		//Matchfilter Code based on given criteria ####################################################
		switch(criteria)
		{
		
		case "Unassigned":
			//Filter to form a match query based on inputs
			MatchFilter = Filters.and(Filters.eq("_id.clientKey", Long.valueOf(clientkey)),
					Filters.eq("_id.midRuleKey", Long.valueOf(midrulekey)),
					Filters.eq("subRule.subRuleKey", Long.valueOf(subrulekey)),
					Filters.eq("dpKey", Long.valueOf(dpkey)),
					Filters.eq("releaseInfo.releaseDate", Serenity.sessionVariableCalled("CUR_RELEASE_DATE")),
						Filters.eq("presentationProfile", null));
		break;
		case "Without RFP":
			 presName=Serenity.sessionVariableCalled("PresentationName");
			 
			//Filter to form a match query based on inputs
			MatchFilter = Filters.and(Filters.eq("_id.clientKey", Long.valueOf(clientkey)),
					Filters.eq("_id.midRuleKey", Long.valueOf(midrulekey)),
					Filters.eq("subRule.subRuleKey", Long.valueOf(subrulekey)),
					Filters.eq("dpKey", Long.valueOf(dpkey)),
					Filters.eq("releaseInfo.releaseDate", Serenity.sessionVariableCalled("CUR_RELEASE_DATE")),
						Filters.eq("presentationProfile.profileName", presName));
			
		break;
		case "With RFP":
			 presName=Serenity.sessionVariableCalled("PresentationName");
			//Filter to form a match query based on inputs
			MatchFilter = Filters.and(Filters.eq("_id.clientKey", Long.valueOf(clientkey)),
					Filters.eq("_id.midRuleKey", Long.valueOf(midrulekey)),
					Filters.eq("subRule.subRuleKey", Long.valueOf(subrulekey)),
					Filters.eq("dpKey", Long.valueOf(dpkey)),
					Filters.eq("ellChange", expeLlchange),
					Filters.eq("dpChange.status", "NEW"),
					Filters.eq("dpChange.dpKey", Long.valueOf(dpkey)),
					Filters.eq("releaseInfo.releaseDate", Serenity.sessionVariableCalled("CUR_RELEASE_DATE")),
					Filters.eq("ruleChange.recategorized.previous.dpKey", Long.valueOf(expOLDDPkey)),
					Filters.eq("ruleChange.recategorized.current.dpKey", Long.valueOf(dpkey)),
						Filters.eq("presentationProfile.profileName", presName));
		break;
		default:
			GenericUtils.Verify("case not found::"+criteria, false);
			
		break;
		}
		
		//Aggregation Code based on given criteria ####################################################
		switch(criteria)
		{
		case "Unassigned":
		case "Without RFP":
			//results = mColl.find(MatchFilter);
			recordsCount = mColl.count(MatchFilter);
			//System.out.println(!(recordsCount==0));
			GenericUtils.Verify("Source DP validation,recordcount is '"+recordsCount+"',Matchfilter::"+MatchFilter, !(recordsCount==0));
			
			Bson matchtext = Aggregates.match(Filters.and(MatchFilter));
			
			output = mColl.aggregate(Arrays.asList(matchtext,Aggregates.group("$dpKey",
					Accumulators.addToSet("ellChange","$ellChange"),
					Accumulators.addToSet("dpStatus","$dpChange.status"),
					Accumulators.addToSet("newDPkey","$dpChange.dpKey"),
					Accumulators.addToSet("ruleChangestatus","$ruleChange.status"),
					Accumulators.addToSet("ruleChangepreviosdpKey","$ruleChange.recategorized.previous.dpKey"),
					Accumulators.addToSet("ruleChangecurrentdpKey","$ruleChange.recategorized.current.dpKey"),
					Accumulators.addToSet("newDPDesc","$dpChange.dpDesc"),
					Accumulators.addToSet("ruleChangepreviosdpDesc","$ruleChange.recategorized.previous.dpDesc"),
					Accumulators.addToSet("ruleChangecurrentdpDesc","$ruleChange.recategorized.current.dpDesc")
					))
					).allowDiskUse(true);
			
			
			for (Document doc:output) 
			{
				System.out.println(doc);
				newDPkey=StringUtils.substringBetween(String.valueOf(doc), "newDPkey=[", "], ruleChangestatus");
				newDPStatus=StringUtils.substringBetween(String.valueOf(doc), "dpStatus=[", "], newDPkey");
				ruleChangestatus=StringUtils.substringBetween(String.valueOf(doc), "ruleChangestatus=[", "], ruleChangepreviosdpKey");
				ruleChangePreviousDPkey=StringUtils.substringBetween(String.valueOf(doc), "ruleChangepreviosdpKey=[", "], ruleChangecurrentdpKey");
				ruleChangeCurrentDPkey=StringUtils.substringBetween(String.valueOf(doc), "ruleChangecurrentdpKey=[", "], newDPDesc");
				ruleChangePreviousDPDesc=StringUtils.substringBetween(String.valueOf(doc), "ruleChangepreviosdpDesc=[", "], ruleChangecurrentdpDesc");
				ruleChangeCurrentDPDesc=StringUtils.substringBetween(String.valueOf(doc), "ruleChangecurrentdpDesc=[", "]}}");
				newDPDesc=StringUtils.substringBetween(String.valueOf(doc), "newDPDesc=[", "], ruleChangepreviosdpDesc");
				ellChange=StringUtils.substringBetween(String.valueOf(doc), "ellChange=[", "], dpStatus");
			}
		
			GenericUtils.Verify(""+dptype+" DP validation,Expected ellChange::"+expeLlchange+",Actual::"+ellChange+",post NEWDP,Matchfilter::"+MatchFilter, expeLlchange.equalsIgnoreCase(ellChange));
			GenericUtils.Verify(""+dptype+" DP validation,Expected DPkey::"+dpkey+",Actual::"+newDPkey+",post NEWDP,Matchfilter::"+MatchFilter, dpkey.equalsIgnoreCase(newDPkey));
			GenericUtils.Verify(""+dptype+" DP validation,Expected dpStatus::NEW,Actual::"+newDPStatus+",post NEWDP,Matchfilter::"+MatchFilter, newDPStatus.equalsIgnoreCase("NEW"));
			GenericUtils.Verify(""+dptype+" DP validation,Expected RuleChange Status::CHANGED,Actual::"+ruleChangestatus+",post NEWDP,Matchfilter::"+MatchFilter, "CHANGED".equalsIgnoreCase(ruleChangestatus));
			GenericUtils.Verify(""+dptype+" DP validation,Expected Previous RulechangeDPkey::"+expOLDDPkey+",Actual::"+ruleChangePreviousDPkey+",post NEWDP,Matchfilter::"+MatchFilter, expOLDDPkey.equalsIgnoreCase(ruleChangePreviousDPkey));
			GenericUtils.Verify(""+dptype+" DP validation,Expected Current RulechangeDPkey::"+newDPkey+",Actual::"+ruleChangeCurrentDPkey+",post NEWDP,Matchfilter::"+MatchFilter, newDPkey.equalsIgnoreCase(ruleChangeCurrentDPkey));
			GenericUtils.Verify(""+dptype+" DP validation,Expected Previous RulechangeDPDesc::"+expruleChangePreviousDPDesc+",Actual::"+ruleChangePreviousDPDesc+",post NEWDP,Matchfilter::"+MatchFilter, expruleChangePreviousDPDesc.equalsIgnoreCase(ruleChangePreviousDPDesc));
			GenericUtils.Verify(""+dptype+" DP validation,Expected Current RulechangeDPDesc::"+expruleChangeCurrentDPDesc+",Actual::"+ruleChangeCurrentDPDesc+",post NEWDP,Matchfilter::"+MatchFilter, expruleChangeCurrentDPDesc.equalsIgnoreCase(ruleChangeCurrentDPDesc));
			GenericUtils.Verify(""+dptype+" DP validation,Expected NEW DPDesc::"+expruleChangeCurrentDPDesc+",Actual::"+newDPDesc+",post NEWDP,Matchfilter::"+MatchFilter, expruleChangeCurrentDPDesc.equalsIgnoreCase(newDPDesc));
			
			//verify data post pipeline for DPDeactivation change for Source DPKey ########################################################
			/*if(dptype.equalsIgnoreCase("Source"))
			{
				if(criteria.equalsIgnoreCase("Unassigned"))
				{
					presName=null;
				}
				else
				{
					presName=presName;
				}
				//Filter to form a match query based on inputs
				MatchFilter = Filters.and(Filters.eq("_id.clientKey", Long.valueOf(clientkey)),
						Filters.eq("_id.midRuleKey", Long.valueOf(midrulekey)),
						Filters.eq("subRule.subRuleKey", Long.valueOf(subrulekey)),
						Filters.eq("dpKey", Long.valueOf(dpkey)),
						Filters.eq("ellChange", expeLlchange),
						Filters.eq("dpChange.status", expdeactDPStatus),
						Filters.eq("ruleChange.status", expruleChangestatus),
							Filters.eq("presentationProfile.profileName", presName));
				recordsCount = mColl.count(MatchFilter);
				long expPPScount=Long.valueOf(Serenity.sessionVariableCalled("PPScount").toString());
				bstatus=recordsCount==expPPScount;
				GenericUtils.Verify("Source DP validation,Expected PPSCount:"+expPPScount+",Actual::"+recordsCount+",post DPdeact,Matchfilter::"+MatchFilter, bstatus);
				
			}*/
			
		break;
		case "With RFP":
			recordsCount = mColl.count(MatchFilter);
			long expPPScount=Long.valueOf(Serenity.sessionVariableCalled("PPScount").toString());
			bstatus=recordsCount==0;
			GenericUtils.Verify("Source DP validation,Expected PPSCount:0,Actual::"+expPPScount+",post DPdeact,Matchfilter::"+MatchFilter, bstatus);
			
		break;
		default:
			GenericUtils.Verify("case not found::"+criteria, false);
			
		break;
		}
	
	}
	
	//verify OLD DP change post PIPELINE
	public static void verifyOldDPChangePostpipeline(String dpkey,String criteria,String dptype,String expeLlchange)
	{
		AggregateIterable<Document> output=null;
		boolean bstatus=false;
		String newDPkey=null;
		String newDPStatus=null;
		String ruleChangestatus=null;
		String ruleChangePreviousDPkey=null;
		String ruleChangeCurrentDPkey=null;
		String ruleChangePreviousDPDesc=null;
		String ruleChangeCurrentDPDesc=null;
		String newDPDesc=null;
		String ellChange=null;
		String presName=null;
		
		String clientkey=Serenity.sessionVariableCalled("clientkey");
		String newDP=Serenity.sessionVariableCalled("CUR_DP");
		String midrulekey=Serenity.sessionVariableCalled("CUR_MID_RULE");
		String subrulekey=Serenity.sessionVariableCalled("CUR_SUB_RULE_KEY");
		
		//expected values
		String expOLDDPkey=Serenity.sessionVariableCalled("PREV_DP");
		String expruleChangePreviousDPDesc=Serenity.sessionVariableCalled("PREV_DP_DESC");
		
		
		//Method to connect mongoDB
		ConnectTogivenDBandCollection("cpd", "changeOpportunity");
		Bson MatchFilter = new BsonDocument();
		
		//Matchfilter Code based on given criteria ####################################################
		switch(criteria)
		{
		
		case "Unassigned":
			//Filter to form a match query based on inputs
			MatchFilter = Filters.and(Filters.eq("_id.clientKey", Long.valueOf(clientkey)),
					Filters.eq("_id.midRuleKey", Long.valueOf(midrulekey)),
					Filters.eq("subRule.subRuleKey", Long.valueOf(subrulekey)),
					Filters.eq("dpKey", Long.valueOf(dpkey)),
					Filters.eq("releaseInfo.releaseDate", Serenity.sessionVariableCalled("PREV_RELEASE_DATE")),
						Filters.eq("presentationProfile", null));
		break;
		case "Without RFP":
			 presName=Serenity.sessionVariableCalled("PresentationName");
			 
			//Filter to form a match query based on inputs
			MatchFilter = Filters.and(Filters.eq("_id.clientKey", Long.valueOf(clientkey)),
					Filters.eq("_id.midRuleKey", Long.valueOf(midrulekey)),
					Filters.eq("subRule.subRuleKey", Long.valueOf(subrulekey)),
					Filters.eq("dpKey", Long.valueOf(dpkey)),
					Filters.eq("releaseInfo.releaseDate", Serenity.sessionVariableCalled("PREV_RELEASE_DATE")),
						Filters.eq("presentationProfile.profileName", presName));
			
		break;
		case "With RFP":
			 presName=Serenity.sessionVariableCalled("PresentationName");
			//Filter to form a match query based on inputs
			MatchFilter = Filters.and(Filters.eq("_id.clientKey", Long.valueOf(clientkey)),
					Filters.eq("_id.midRuleKey", Long.valueOf(midrulekey)),
					Filters.eq("subRule.subRuleKey", Long.valueOf(subrulekey)),
					Filters.eq("dpKey", Long.valueOf(dpkey)),
					Filters.eq("ellChange", expeLlchange),
					Filters.eq("releaseInfo.releaseDate", Serenity.sessionVariableCalled("PREV_RELEASE_DATE")),
					Filters.eq("presentationProfile.profileName", presName));
		break;
		default:
			GenericUtils.Verify("case not found::"+criteria, false);
			
		break;
		}
		
		//Aggregation Code based on given criteria ####################################################
		switch(criteria)
		{
		case "Unassigned":
		case "Without RFP":
			//results = mColl.find(MatchFilter);
			recordsCount = mColl.count(MatchFilter);
			//System.out.println(!(recordsCount==0));
			GenericUtils.Verify("Source DP validation,recordcount is '"+recordsCount+"',Matchfilter::"+MatchFilter, !(recordsCount==0));
			
			Bson matchtext = Aggregates.match(Filters.and(MatchFilter));
			
			output = mColl.aggregate(Arrays.asList(matchtext,Aggregates.group("$dpKey",
					Accumulators.addToSet("ellChange","$ellChange"),
					Accumulators.addToSet("ruleChangestatus","$ruleChange.status"),
					Accumulators.addToSet("DPDesc","$dpDescr")
					))
					).allowDiskUse(true);
			
			
			for (Document doc:output) 
			{
				//System.out.println(doc);
				newDPkey=StringUtils.substringBetween(String.valueOf(doc), "_id=", ", ellChange");
				ruleChangestatus=StringUtils.substringBetween(String.valueOf(doc), "ruleChangestatus=[", "], DPDesc");
				newDPDesc=StringUtils.substringBetween(String.valueOf(doc), "DPDesc=[", "]}}");
				ellChange=StringUtils.substringBetween(String.valueOf(doc), "ellChange=[", "], ruleChangestatus");
			}
		
			GenericUtils.Verify(""+dptype+" DP validation,Expected ellChange::"+expeLlchange+",Actual::"+ellChange+",post NEWDP,Matchfilter::"+MatchFilter, expeLlchange.equalsIgnoreCase(ellChange));
			GenericUtils.Verify(""+dptype+" DP validation,Expected DPkey::"+dpkey+",Actual::"+newDPkey+",post NEWDP,Matchfilter::"+MatchFilter, dpkey.equalsIgnoreCase(newDPkey));
			GenericUtils.Verify(""+dptype+" DP validation,Expected RuleChange Status::CHANGED,Actual::"+ruleChangestatus+",post NEWDP,Matchfilter::"+MatchFilter, "CHANGED".equalsIgnoreCase(ruleChangestatus));
			GenericUtils.Verify(""+dptype+" DP validation,Expected NEW DPDesc::"+expruleChangePreviousDPDesc+",Actual::"+newDPDesc+",post NEWDP,Matchfilter::"+MatchFilter, expruleChangePreviousDPDesc.equalsIgnoreCase(newDPDesc));
			
			
		break;
		case "With RFP":
			recordsCount = mColl.count(MatchFilter);
			long expPPScount=Long.valueOf(Serenity.sessionVariableCalled("PPScount").toString());
			bstatus=recordsCount==0;
			GenericUtils.Verify("Source DP validation,Expected PPSCount:0,Actual::"+expPPScount+",post DPdeact,Matchfilter::"+MatchFilter, bstatus);
			
		break;
		default:
			GenericUtils.Verify("case not found::"+criteria, false);
			
		break;
		}
	
	}

	//verify RECAT DP change post PIPELINE
	public static void verifyRECATDPChangePostpipeline(String dpkey,String criteria,String dptype,String expeLlchange)
	{
		AggregateIterable<Document> output=null;
		boolean bstatus=false;
		String newDPkey=null;
		String newDPStatus=null;
		String ruleChangestatus=null;
		String ruleChangePrevioustopicKey=null;
		String ruleChangeCurrenttopicKey=null;
		String ruleChangePreviousTopicDesc=null;
		String ruleChangeCurrentTopicDesc=null;
		String newDPDesc=null;
		String ellChange=null;
		String presName=null;
		
		String clientkey=Serenity.sessionVariableCalled("clientkey");
		String newDP=Serenity.sessionVariableCalled("CUR_DP");
		String midrulekey=Serenity.sessionVariableCalled("CUR_MID_RULE");
		String subrulekey=Serenity.sessionVariableCalled("CUR_SUB_RULE_KEY");
		String curTopickey=Serenity.sessionVariableCalled("CUR_TOPIC");
		
		//expected values
		String expOLDTopickey=Serenity.sessionVariableCalled("PREV_TOPIC");
		String expruleChangePreviousTopicDesc=Serenity.sessionVariableCalled("PREV_TOPIC_TITLE");
		String expruleChangeCurrentTopicDesc=Serenity.sessionVariableCalled("CUR_TOPIC_TITLE");
		String dpDesc=Serenity.sessionVariableCalled("PREV_DP_DESC");
		
		
		//Method to connect mongoDB
		ConnectTogivenDBandCollection("cpd", "changeOpportunity");
		Bson MatchFilter = new BsonDocument();
		
		//Matchfilter Code based on given criteria ####################################################
		switch(criteria)
		{
		
		case "Unassigned":
			//Filter to form a match query based on inputs
			MatchFilter = Filters.and(Filters.eq("_id.clientKey", Long.valueOf(clientkey)),
					Filters.eq("_id.midRuleKey", Long.valueOf(midrulekey)),
					Filters.eq("subRule.subRuleKey", Long.valueOf(subrulekey)),
					Filters.eq("dpKey", Long.valueOf(dpkey)),
					Filters.eq("topicKey", Long.valueOf(curTopickey)),
					Filters.eq("releaseInfo.releaseDate", Serenity.sessionVariableCalled("CUR_RELEASE_DATE")),
						Filters.eq("presentationProfile", null));
		break;
		case "Without RFP":
			 presName=Serenity.sessionVariableCalled("PresentationName");
			 
			//Filter to form a match query based on inputs
			MatchFilter = Filters.and(Filters.eq("_id.clientKey", Long.valueOf(clientkey)),
					Filters.eq("_id.midRuleKey", Long.valueOf(midrulekey)),
					Filters.eq("subRule.subRuleKey", Long.valueOf(subrulekey)),
					Filters.eq("dpKey", Long.valueOf(dpkey)),
					Filters.eq("topicKey", Long.valueOf(curTopickey)),
					Filters.eq("releaseInfo.releaseDate", Serenity.sessionVariableCalled("CUR_RELEASE_DATE")),
						Filters.eq("presentationProfile.profileName", presName));
			
		break;
		case "With RFP":
			 presName=Serenity.sessionVariableCalled("PresentationName");
			//Filter to form a match query based on inputs
			MatchFilter = Filters.and(Filters.eq("_id.clientKey", Long.valueOf(clientkey)),
					Filters.eq("_id.midRuleKey", Long.valueOf(midrulekey)),
					Filters.eq("subRule.subRuleKey", Long.valueOf(subrulekey)),
					Filters.eq("dpKey", Long.valueOf(dpkey)),
					Filters.eq("ellChange", expeLlchange),
					Filters.eq("dpChange.status", "CHANGED"),
					Filters.eq("dpChange.dpKey", Long.valueOf(dpkey)),
					Filters.eq("releaseInfo.releaseDate", Serenity.sessionVariableCalled("CUR_RELEASE_DATE")),
					Filters.eq("ruleChange.recategorized.previous.topicKey", Long.valueOf(expOLDTopickey)),
					Filters.eq("ruleChange.recategorized.current.topicKey", Long.valueOf(curTopickey)),
						Filters.eq("presentationProfile.profileName", presName));
		break;
		default:
			GenericUtils.Verify("case not found::"+criteria, false);
			
		break;
		}
		
		//Aggregation Code based on given criteria ####################################################
		switch(criteria)
		{
		case "Unassigned":
		case "Without RFP":
			//results = mColl.find(MatchFilter);
			recordsCount = mColl.count(MatchFilter);
			//System.out.println(!(recordsCount==0));
			GenericUtils.Verify("Source DP validation,recordcount is '"+recordsCount+"',Matchfilter::"+MatchFilter, !(recordsCount==0));
			
			Bson matchtext = Aggregates.match(Filters.and(MatchFilter));
			
			output = mColl.aggregate(Arrays.asList(matchtext,Aggregates.group("$dpKey",
					Accumulators.addToSet("ellChange","$ellChange"),
					Accumulators.addToSet("dpStatus","$dpChange.status"),
					Accumulators.addToSet("newDPkey","$dpChange.dpKey"),
					Accumulators.addToSet("ruleChangepreviostopicKey","$dpChange.recategorized.previous.topicKey"),
					Accumulators.addToSet("ruleChangecurrenttopicKey","$dpChange.recategorized.current.topicKey"),
					Accumulators.addToSet("newDPDesc","$dpChange.dpDesc"),
					Accumulators.addToSet("ruleChangepreviostopicDesc","$dpChange.recategorized.previous.topicDesc"),
					Accumulators.addToSet("ruleChangecurrenttopicDesc","$dpChange.recategorized.current.topicDesc")
					))
					).allowDiskUse(true);
			
			
			for (Document doc:output) 
			{
				System.out.println(doc);
				newDPkey=StringUtils.substringBetween(String.valueOf(doc), "_id=", ", ellChange");
				newDPStatus=StringUtils.substringBetween(String.valueOf(doc), "dpStatus=[", "], newDPkey");
				//ruleChangestatus=StringUtils.substringBetween(String.valueOf(doc), "ruleChangestatus=[", "], ruleChangepreviosdpKey");
				ruleChangePrevioustopicKey=StringUtils.substringBetween(String.valueOf(doc), "ruleChangepreviostopicKey=[", "], ruleChangecurrenttopicKey");
				ruleChangeCurrenttopicKey=StringUtils.substringBetween(String.valueOf(doc), "ruleChangecurrenttopicKey=[", "], newDPDesc");
				ruleChangePreviousTopicDesc=StringUtils.substringBetween(String.valueOf(doc), "ruleChangepreviostopicDesc=[", "], ruleChangecurrenttopicDesc");
				ruleChangeCurrentTopicDesc=StringUtils.substringBetween(String.valueOf(doc), "ruleChangecurrenttopicDesc=[", "]}}");
				newDPDesc=StringUtils.substringBetween(String.valueOf(doc), "newDPDesc=[", "], ruleChangepreviostopicDesc");
				ellChange=StringUtils.substringBetween(String.valueOf(doc), "ellChange=[", "], dpStatus");
			}
		
			GenericUtils.Verify(""+dptype+" DP validation,Expected ellChange::"+expeLlchange+",Actual::"+ellChange+",post NEWDP,Matchfilter::"+MatchFilter, expeLlchange.equalsIgnoreCase(ellChange));
			GenericUtils.Verify(""+dptype+" DP validation,Expected DPkey::"+dpkey+",Actual::"+newDPkey+",post NEWDP,Matchfilter::"+MatchFilter, dpkey.equalsIgnoreCase(newDPkey));
			GenericUtils.Verify(""+dptype+" DP validation,Expected dpStatus::CHANGED,Actual::"+newDPStatus+",post NEWDP,Matchfilter::"+MatchFilter, newDPStatus.equalsIgnoreCase("CHANGED"));
			//GenericUtils.Verify(""+dptype+" DP validation,Expected RuleChange Status::CHANGED,Actual::"+ruleChangestatus+",post NEWDP,Matchfilter::"+MatchFilter, "CHANGED".equalsIgnoreCase(ruleChangestatus));
			GenericUtils.Verify(""+dptype+" DP validation,Expected Previous RulechangeTopickey::"+expOLDTopickey+",Actual::"+ruleChangePrevioustopicKey+",post NEWDP,Matchfilter::"+MatchFilter, expOLDTopickey.equalsIgnoreCase(ruleChangePrevioustopicKey));
			GenericUtils.Verify(""+dptype+" DP validation,Expected Current RulechangeTopickey::"+curTopickey+",Actual::"+ruleChangeCurrenttopicKey+",post NEWDP,Matchfilter::"+MatchFilter, curTopickey.equalsIgnoreCase(ruleChangeCurrenttopicKey));
			GenericUtils.Verify(""+dptype+" DP validation,Expected Previous RulechangeTopicDesc::"+expruleChangePreviousTopicDesc+",Actual::"+ruleChangePreviousTopicDesc+",post NEWDP,Matchfilter::"+MatchFilter, expruleChangePreviousTopicDesc.equalsIgnoreCase(ruleChangePreviousTopicDesc));
			GenericUtils.Verify(""+dptype+" DP validation,Expected Current RulechangeTopicDesc::"+expruleChangeCurrentTopicDesc+",Actual::"+ruleChangeCurrentTopicDesc+",post NEWDP,Matchfilter::"+MatchFilter, expruleChangeCurrentTopicDesc.equalsIgnoreCase(ruleChangeCurrentTopicDesc));
			GenericUtils.Verify(""+dptype+" DP validation,Expected RECAT DPDesc::"+dpDesc+",Actual::"+newDPDesc+",post NEWDP,Matchfilter::"+MatchFilter, dpDesc.equalsIgnoreCase(newDPDesc));
			
			//verify data post pipeline for DPDeactivation change for Source DPKey ########################################################
			/*if(dptype.equalsIgnoreCase("Source"))
			{
				if(criteria.equalsIgnoreCase("Unassigned"))
				{
					presName=null;
				}
				else
				{
					presName=presName;
				}
				//Filter to form a match query based on inputs
				MatchFilter = Filters.and(Filters.eq("_id.clientKey", Long.valueOf(clientkey)),
						Filters.eq("_id.midRuleKey", Long.valueOf(midrulekey)),
						Filters.eq("subRule.subRuleKey", Long.valueOf(subrulekey)),
						Filters.eq("dpKey", Long.valueOf(dpkey)),
						Filters.eq("ellChange", expeLlchange),
						Filters.eq("dpChange.status", expdeactDPStatus),
						Filters.eq("ruleChange.status", expruleChangestatus),
							Filters.eq("presentationProfile.profileName", presName));
				recordsCount = mColl.count(MatchFilter);
				long expPPScount=Long.valueOf(Serenity.sessionVariableCalled("PPScount").toString());
				bstatus=recordsCount==expPPScount;
				GenericUtils.Verify("Source DP validation,Expected PPSCount:"+expPPScount+",Actual::"+recordsCount+",post DPdeact,Matchfilter::"+MatchFilter, bstatus);
				
			}*/
			
		break;
		case "With RFP":
			recordsCount = mColl.count(MatchFilter);
			long expPPScount=Long.valueOf(Serenity.sessionVariableCalled("PPScount").toString());
			bstatus=recordsCount==0;
			GenericUtils.Verify("Source DP validation,Expected PPSCount:0,Actual::"+expPPScount+",post DPdeact,Matchfilter::"+MatchFilter, bstatus);
			
		break;
		default:
			GenericUtils.Verify("case not found::"+criteria, false);
			
		break;
		}
	
	}
	
	//Method to set data for Rule Recat Change 
	public static boolean getDataforRuleRecatChange(String clientkey,String changeoppty)
	{
		int i=0;
	boolean bstatus=false;
	String prevversion=null;
	String curversion=null;
	String dpkey=null;
	String subrulekey=null;
	String MP=null;
	String topic=null;
	String dpdesc=null;
	String currentdpdesc=null;
	String midrulekey=null;
	String topicKey=null;
	String mpKey=null;
	String clientDesc=null;
	AggregateIterable<Document> output=null;
	ArrayList<String> ellList = new ArrayList<String>();
	ArrayList<Long> dpKeys = new ArrayList<Long>();
	List<String> dpKeyList = null;;
	
	//Method to connect mongoDB
	ConnectTogivenDBandCollection("cpd", "changeOpportunity");
	Bson MatchFilter = new BsonDocument();
	if(changeoppty.equalsIgnoreCase("RULE CHANGE"))
	{
		//Filter to form a match query based on inputs
		MatchFilter = Filters.and(Filters.eq("_id.clientKey", Long.valueOf(clientkey)),
					//Filters.eq("ellChange", changeoppty),
					//Filters.ne("releaseInfo.releaseDate", "20200825"),
					Filters.exists("ruleChange.version", true),
					Filters.eq("presentationProfile", null));
	}
	else
	{
		//Filter to form a match query based on inputs
		MatchFilter = Filters.and(Filters.eq("_id.clientKey", Long.valueOf(clientkey)),
					//Filters.eq("ellChange", changeoppty),
					//Filters.ne("releaseInfo.releaseDate", "20200825"),
					Filters.eq("presentationProfile", null));
	}
	
	recordsCount = mColl.count(MatchFilter);
	
	if(recordsCount==0)
	{
		System.out.println("No Dpkey is available for the given changetype '"+changeoppty+"',for clientkey '"+clientkey+"'");
		//System.out.println("RecordCount::"+recordsCount+",for matchfilter==>"+MatchFilter);
		return bstatus;
	}
	else
	{
		
		
		Bson matchtext = Aggregates.match(Filters.and(MatchFilter));
		
			
		output = mColl.aggregate(Arrays.asList(matchtext,Aggregates.group(
				new Document().append("Topic", "$topicKey"),
				Accumulators.addToSet("MP","$medPolicyDesc"),
				Accumulators.addToSet("Topic","$topicDesc"),
				Accumulators.addToSet("PreviousDPDesc","$dpDescr"),
				Accumulators.addToSet("midRuleKey","$subRule.midRuleKey"),
				Accumulators.addToSet("mpKey","$medPolicyKey"),
				Accumulators.addToSet("topicKey","$topicKey"),
				Accumulators.addToSet("dpKeys","$dpKey")
				))
				).allowDiskUse(true);
		
		for (Document doc3 : output) 
		{
			//System.out.println(doc3);
			topicKey=StringUtils.substringBetween(String.valueOf(doc3), "Topic=", "}}, MP");
			dpkey=StringUtils.substringBetween(String.valueOf(doc3), "dpKeys=[","]}}");
			dpKeyList=Arrays.asList(dpkey.split(","));
			
		//if they are multiple dpkeys,then only it will be going to loop
		if(dpKeyList.size()>1)
		{
			System.out.println("dpKeyList::"+dpKeyList);
			System.out.println("DpKey Size::"+dpKeyList.size());
			MatchFilter = Filters.and(Filters.eq("_id.clientKey", Long.valueOf(clientkey)),
					Filters.eq("topicKey", Long.valueOf(topicKey)),
					Filters.eq("presentationProfile", null));
			
				 matchtext = Aggregates.match(Filters.and(MatchFilter));
				
				output = mColl.aggregate(Arrays.asList(matchtext,Aggregates.group(
						new Document().append("DPKey", "$dpKey").append("subRuleKey", "$subRule.subRuleKey"),
						Accumulators.addToSet("MP","$medPolicyDesc"),
						Accumulators.addToSet("Topic","$topicDesc"),
						Accumulators.addToSet("PreviousDPDesc","$dpDescr"),
						Accumulators.addToSet("midRuleKey","$subRule.midRuleKey"),
						Accumulators.addToSet("mpKey","$medPolicyKey"),
						Accumulators.addToSet("topicKey","$topicKey"),
						Accumulators.addToSet("subRuleId","$subRule.subRuleId"),
						Accumulators.addToSet("preversion","$ruleChange.version.previous"),
						Accumulators.addToSet("curversion","$ruleChange.version.current"),
						Accumulators.addToSet("releaseName","$releaseInfo.releaseName"),
						Accumulators.addToSet("ellChange","$ellChange")
						))
						).allowDiskUse(true);
			
			
			for (Document doc : output) 
			{
				i=i+1;
				//System.out.println(doc);
				dpkey=StringUtils.substringBetween(String.valueOf(doc), "DPKey=", ", subRuleKey");
				subrulekey=StringUtils.substringBetween(String.valueOf(doc), "subRuleKey=", "}}, MP");
				MP=StringUtils.substringBetween(String.valueOf(doc), "MP=[", "], Topic=");
				topic=StringUtils.substringBetween(String.valueOf(doc), "Topic=[", "], PreviousDPDesc");
				dpdesc=StringUtils.substringBetween(String.valueOf(doc), "PreviousDPDesc=[", "], midRuleKey").trim();
				midrulekey=StringUtils.substringBetween(String.valueOf(doc), "midRuleKey=[", "], mpKey");
				mpKey=StringUtils.substringBetween(String.valueOf(doc), "mpKey=[", "], topicKey");
				topicKey=StringUtils.substringBetween(String.valueOf(doc), "topicKey=[", "], subRuleId");
				
				String release=StringUtils.substringBetween(String.valueOf(doc), "releaseName=[", "], ellChange").trim();
				String ellChange=StringUtils.substringBetween(String.valueOf(doc), "ellChange=[", "]}}").trim();
				
				if(!release.contains("August 2020")&&ellChange.contains(changeoppty))
				{
					System.out.println("Release::"+release);
					System.out.println("ellChange::"+ellChange);
					System.out.println("dpkey::"+dpkey);
					System.out.println("subrulekey::"+subrulekey);
					if(changeoppty.equalsIgnoreCase("RULE CHANGE"))
					{
						prevversion=StringUtils.substringBetween(String.valueOf(doc), "preversion=[", "], curversion");
						if(prevversion.contains(","))
						{
							prevversion=StringUtils.substringBefore(prevversion, ",").trim();
						}
						curversion=StringUtils.substringBetween(String.valueOf(doc), "curversion=[", "], releaseName");
						if(curversion.contains(","))
						{
							curversion=StringUtils.substringBefore(curversion, ",").trim();
						}
					}
					else
					{
						prevversion=StringUtils.substringAfter(StringUtils.substringBetween(String.valueOf(doc), "subRuleId=[", "], preversion"),".");
						curversion=StringUtils.substringAfter(StringUtils.substringBetween(String.valueOf(doc), "subRuleId=[", "], preversion"),".");
					}
					
					/*MatchFilter = Filters.and(Filters.eq("_id.clientKey", Long.valueOf(clientkey)),
							Filters.eq("_id.dpKey", Long.valueOf(dpkey)),
							Filters.eq("topicDesc", topic),
							Filters.eq("subRule.subRuleKey", Long.valueOf(subrulekey))
							);
					 matchtext = Aggregates.match(Filters.and(MatchFilter));
					
					 AggregateIterable<Document> output2 = mColl.aggregate(Arrays.asList(matchtext,Aggregates.group("$ellChange"))
							).allowDiskUse(true);
					
					
					for (Document doc2:output2) 
					{
						//System.out.println(doc);
						String ellchange=StringUtils.substringBetween(String.valueOf(doc2), "_id=", "}}");
						ellList.add(ellchange);
					}
					//System.out.println();
					System.out.println("ellchanges '"+ellList+"' for DPkey::"+dpkey+",subRuleKey::"+subrulekey);
					System.out.println("eLLChangesize::"+ellList.size());
					if(ellList.size()==1&&ellList.get(0).equalsIgnoreCase(changeoppty))*/
					{
						
						Serenity.setSessionVariable("clientkey").to(clientkey);
						Serenity.setSessionVariable("PREV_MP_TITLE").to(MP);
						Serenity.setSessionVariable("PREV_TOPIC_TITLE").to(topic);
						Serenity.setSessionVariable("PREV_DP_DESC").to(dpdesc);
						Serenity.setSessionVariable("CUR_MID_RULE").to(midrulekey);
						Serenity.setSessionVariable("CUR_SUB_RULE_KEY").to(subrulekey);
						Serenity.setSessionVariable("PREV_DP").to(dpkey);
						Serenity.setSessionVariable("PREV_MP").to(mpKey);//mpkey
						Serenity.setSessionVariable("PREV_TOPIC").to(topicKey);//topickey
						Serenity.setSessionVariable("CUR_VERSION").to(curversion);
						Serenity.setSessionVariable("PREV_VERSION").to(prevversion);
						bstatus=true;
						System.out.println("clientkey::"+Serenity.sessionVariableCalled("clientkey"));
						System.out.println("MP::"+Serenity.sessionVariableCalled("PREV_MP_TITLE"));
						System.out.println("Topic::"+Serenity.sessionVariableCalled("PREV_TOPIC_TITLE"));
						System.out.println("Dpkey::"+Serenity.sessionVariableCalled("PREV_DP"));
						System.out.println("DPDesc::"+Serenity.sessionVariableCalled("PREV_DP_DESC"));
						System.out.println("Midrulekey::"+Serenity.sessionVariableCalled("CUR_MID_RULE"));
						System.out.println("Subrulekey::"+Serenity.sessionVariableCalled("CUR_SUB_RULE_KEY"));
						System.out.println("MPKey::"+Serenity.sessionVariableCalled("PREV_MP"));
						System.out.println("TopicKey::"+Serenity.sessionVariableCalled("PREV_TOPIC"));
						System.out.println("CUR_VERSION::"+Serenity.sessionVariableCalled("CUR_VERSION"));
						System.out.println("PREV_VERSION::"+Serenity.sessionVariableCalled("PREV_VERSION"));
						//System.out.println("clientdesc::"+Serenity.sessionVariableCalled("CLIENT_DESC"));
						
						MatchFilter = Filters.and(Filters.eq("_id.clientKey", Long.valueOf(clientkey)),
								Filters.eq("_id.dpKey", Long.valueOf(dpkey)),
								Filters.eq("ellChange", ellChange),
								Filters.eq("subRule.midRuleKey", Long.valueOf(midrulekey)),
								Filters.eq("subRule.subRuleKey", Long.valueOf(subrulekey)),
								Filters.eq("medPolicyDesc", MP),
								Filters.eq("topicDesc", topic),
								Filters.eq("presentationProfile", null));
					   recordsCount = mColl.count(MatchFilter);
					   System.out.println("PPS Count::"+recordsCount);
					   Serenity.setSessionVariable("PPScount").to(recordsCount);
						break;
					}
					
					//ellList.clear();
				}
				
				
				
				
			}
		}
		if(bstatus)
		{
			//bstatus=false;
			MatchFilter = Filters.and(Filters.eq("_id.clientKey", Long.valueOf(clientkey)),
					Filters.ne("_id.dpKey", Long.valueOf(Serenity.sessionVariableCalled("PREV_DP"))),
					//Filters.eq("ellChange", changeoppty),
					Filters.eq("medPolicyDesc", MP),
					Filters.eq("topicDesc", topic),
					Filters.eq("presentationProfile", null));
			recordsCount = mColl.count(MatchFilter);
			GenericUtils.Verify("Target dpkey count is zero in the same topic,MatchFilter::"+MatchFilter, !(recordsCount==0));
			 matchtext = Aggregates.match(Filters.and(MatchFilter));
			output = mColl.aggregate(Arrays.asList(matchtext,Aggregates.group(
					new Document().append("DPKey", "$dpKey"),
					Accumulators.addToSet("DPDesc","$dpDescr"),
					Accumulators.addToSet("ellChange","$ellChange")
					))
					).allowDiskUse(true);
			for (Document doc : output)
			{
				System.out.println("###################### Target data ###################");
				dpkey=StringUtils.substringBetween(String.valueOf(doc), "DPKey=", "}}, DPDesc");
				dpdesc=StringUtils.substringBetween(String.valueOf(doc), "DPDesc=[", "], ellChange");
				Serenity.setSessionVariable("CUR_DP").to(dpkey);
				Serenity.setSessionVariable("CUR_DP_DESC").to(dpdesc);
				bstatus=true;
				System.out.println("CUR_DP::"+Serenity.sessionVariableCalled("CUR_DP"));
				System.out.println("CUR_DP_DESC::"+Serenity.sessionVariableCalled("CUR_DP_DESC"));
				break;
			}
			
			break;
		}
			
		}
			
	}
	if(Serenity.sessionVariableCalled("CUR_DP")==null)
	{
		System.out.println("No Target Dpkey is available for the given changetype '"+changeoppty+"',for clientkey '"+clientkey+"'");
		return false;
	}
	if(!bstatus)
	{
		System.out.println("No Dpkey is available for the given changetype '"+changeoppty+"',for clientkey '"+clientkey+"'");
	}
	
   //Serenity.setSessionVariable("Matchfilter").to(MatchFilter);
	return bstatus;
	
	}
	
	//verify Rule RECAT post post pipeline
	public static void verifyRuleRecatChangePostpipeline(String dpkey,String criteria,String dptype,String expeLlchange)
	{
		AggregateIterable<Document> output=null;
		boolean bstatus=false;
		String deactDPkey=null;
		String deactDPStatus=null;
		String ruleChangestatus=null;
		String ruleChangePreviousDPkey=null;
		String ruleChangeCurrentDPkey=null;
		String ruleChangePreviousDPDesc=null;
		String ruleChangeCurrentDPDesc=null;
		String deactDPDesc=null;
		String ellChange=null;
		String presName=null;
		
		String clientkey=Serenity.sessionVariableCalled("clientkey");
		String targetDP=Serenity.sessionVariableCalled("CUR_DP");
		String midrulekey=Serenity.sessionVariableCalled("CUR_MID_RULE");
		String subrulekey=Serenity.sessionVariableCalled("CUR_SUB_RULE_KEY");
		
		//expected values
		
		String expdeactDPkey=Serenity.sessionVariableCalled("PREV_DP");
		String expruleChangestatus="CHANGED";
		String expruleChangePreviousDPkey=Serenity.sessionVariableCalled("PREV_DP");
		String expruleChangePreviousDPDesc=Serenity.sessionVariableCalled("PREV_DP_DESC");
		String expruleChangeCurrentDPDesc=Serenity.sessionVariableCalled("CUR_DP_DESC");
		//String expellChange="RECATEGORIZED RULE , DEACT DP";
		
		
		//Method to connect mongoDB
		ConnectTogivenDBandCollection("cpd", "changeOpportunity");
		Bson MatchFilter = new BsonDocument();
		
		//Matchfilter Code based on given criteria ####################################################
		switch(criteria)
		{
		
		case "Unassigned":
			//Filter to form a match query based on inputs
			MatchFilter = Filters.and(Filters.eq("_id.clientKey", Long.valueOf(clientkey)),
					Filters.eq("_id.midRuleKey", Long.valueOf(midrulekey)),
					Filters.eq("subRule.subRuleKey", Long.valueOf(subrulekey)),
					Filters.eq("releaseInfo.releaseDate", Serenity.sessionVariableCalled("CUR_RELEASE_DATE")),
					Filters.eq("dpKey", Long.valueOf(dpkey)),
						Filters.eq("presentationProfile", null));
		break;
		case "Without RFP":
			if(dptype.equalsIgnoreCase("Target"))
			{
				presName=null;
			}
			else
			{
				presName=Serenity.sessionVariableCalled("PresentationName");
			}
			 
			
			//Filter to form a match query based on inputs
			MatchFilter = Filters.and(Filters.eq("_id.clientKey", Long.valueOf(clientkey)),
					Filters.eq("_id.midRuleKey", Long.valueOf(midrulekey)),
					Filters.eq("subRule.subRuleKey", Long.valueOf(subrulekey)),
					Filters.eq("releaseInfo.releaseDate", Serenity.sessionVariableCalled("CUR_RELEASE_DATE")),
					Filters.eq("dpKey", Long.valueOf(dpkey)),
						Filters.eq("presentationProfile.profileName", presName));
			
		break;
		case "With RFP":
			 presName=Serenity.sessionVariableCalled("PresentationName");
			//Filter to form a match query based on inputs
			MatchFilter = Filters.and(Filters.eq("_id.clientKey", Long.valueOf(clientkey)),
					Filters.eq("_id.midRuleKey", Long.valueOf(midrulekey)),
					Filters.eq("subRule.subRuleKey", Long.valueOf(subrulekey)),
					Filters.eq("dpKey", Long.valueOf(dpkey)),
					Filters.eq("ellChange", expeLlchange),
					Filters.eq("releaseInfo.releaseDate", Serenity.sessionVariableCalled("CUR_RELEASE_DATE")),
					Filters.eq("ruleChange.status", "CHANGED"),
					Filters.eq("ruleChange.recategorized.previous.dpKey", Long.valueOf(expruleChangePreviousDPkey)),
					Filters.eq("ruleChange.recategorized.current.dpKey", Long.valueOf(targetDP)),
						Filters.eq("presentationProfile.profileName", presName));
		break;
		default:
			GenericUtils.Verify("case not found::"+criteria, false);
			
		break;
		}
		
		//Aggregation Code based on given criteria ####################################################
		switch(criteria)
		{
		case "Unassigned":
		case "Without RFP":
			//results = mColl.find(MatchFilter);
			recordsCount = mColl.count(MatchFilter);
			//System.out.println(!(recordsCount==0));
			GenericUtils.Verify("Source DP validation,recordcount is '"+recordsCount+"',Matchfilter::"+MatchFilter, !(recordsCount==0));
			
			Bson matchtext = Aggregates.match(Filters.and(MatchFilter));
			
			output = mColl.aggregate(Arrays.asList(matchtext,Aggregates.group("$dpKey",
					Accumulators.addToSet("ellChange","$ellChange"),
					Accumulators.addToSet("ruleChangestatus","$ruleChange.status"),
					Accumulators.addToSet("ruleChangepreviosdpKey","$ruleChange.recategorized.previous.dpKey"),
					Accumulators.addToSet("ruleChangecurrentdpKey","$ruleChange.recategorized.current.dpKey"),
					Accumulators.addToSet("ruleChangepreviosdpDesc","$ruleChange.recategorized.previous.dpDesc"),
					Accumulators.addToSet("ruleChangecurrentdpDesc","$ruleChange.recategorized.current.dpDesc")
					))
					).allowDiskUse(true);
			
			
			for (Document doc:output) 
			{
				System.out.println(doc);
				ruleChangestatus=StringUtils.substringBetween(String.valueOf(doc), "ruleChangestatus=[", "], ruleChangepreviosdpKey");
				ruleChangePreviousDPkey=StringUtils.substringBetween(String.valueOf(doc), "ruleChangepreviosdpKey=[", "], ruleChangecurrentdpKey");
				ruleChangeCurrentDPkey=StringUtils.substringBetween(String.valueOf(doc), "ruleChangecurrentdpKey=[", "], ruleChangepreviosdpDesc");
				ruleChangePreviousDPDesc=StringUtils.substringBetween(String.valueOf(doc), "ruleChangepreviosdpDesc=[", "], ruleChangecurrentdpDesc");
				ruleChangeCurrentDPDesc=StringUtils.substringBetween(String.valueOf(doc), "ruleChangecurrentdpDesc=[", "]}}");
				ellChange=StringUtils.substringBetween(String.valueOf(doc), "ellChange=[", "], ruleChangestatus");
			}
		
			GenericUtils.Verify(""+dptype+" DP validation,Expected ellChange::"+expeLlchange+",Actual::"+ellChange+",post PIPELINE,Matchfilter::"+MatchFilter, expeLlchange.equalsIgnoreCase(ellChange));
			GenericUtils.Verify(""+dptype+" DP validation,Expected RuleChange Status::"+expruleChangestatus+",Actual::"+ruleChangestatus+",post PIPELINE,Matchfilter::"+MatchFilter, expruleChangestatus.equalsIgnoreCase(ruleChangestatus));
			GenericUtils.Verify(""+dptype+" DP validation,Expected Previous RulechangeDPkey::"+expruleChangePreviousDPkey+",Actual::"+ruleChangePreviousDPkey+",post PIPELINE,Matchfilter::"+MatchFilter, expruleChangePreviousDPkey.equalsIgnoreCase(ruleChangePreviousDPkey));
			GenericUtils.Verify(""+dptype+" DP validation,Expected Current RulechangeDPkey::"+targetDP+",Actual::"+ruleChangeCurrentDPkey+",post PIPELINE,Matchfilter::"+MatchFilter, targetDP.equalsIgnoreCase(ruleChangeCurrentDPkey));
			GenericUtils.Verify(""+dptype+" DP validation,Expected Previous RulechangeDPDesc::"+expruleChangePreviousDPDesc+",Actual::"+ruleChangePreviousDPDesc+",post PIPELINE,Matchfilter::"+MatchFilter, expruleChangePreviousDPDesc.equalsIgnoreCase(ruleChangePreviousDPDesc));
			GenericUtils.Verify(""+dptype+" DP validation,Expected Current RulechangeDPDesc::"+expruleChangeCurrentDPDesc+",Actual::"+ruleChangeCurrentDPDesc+",post PIPELINE,Matchfilter::"+MatchFilter, expruleChangeCurrentDPDesc.equalsIgnoreCase(ruleChangeCurrentDPDesc));
						
			
		break;
		case "With RFP":
			recordsCount = mColl.count(MatchFilter);
			long expPPScount=Long.valueOf(Serenity.sessionVariableCalled("PPScount").toString());
			bstatus=recordsCount==0;
			GenericUtils.Verify("Source DP validation,Expected PPSCount:0,Actual::"+expPPScount+",post DPdeact,Matchfilter::"+MatchFilter, bstatus);
			
		break;
		default:
			GenericUtils.Verify("case not found::"+criteria, false);
			
		break;
		}
	
	}	
	
	//Method to set data for Claimtype added/Removed Change Opportunities
	public static boolean getDataClaimTypeChangeoppty(String clientkey,String changeoppty,String criteria)
	{
		int i=0;
	boolean bstatus=false;
	String dpkey=null;
	String subrulekey=null;
	String MP=null;
	String topic=null;
	String dpdesc=null;
	String currentdpdesc=null;
	String midrulekey=null;
	String topicKey=null;
	String mpKey=null;
	String clientDesc=null;
	String midruleClaimtypes=null;
	List<String> claimTypelist=null;
	AggregateIterable<Document> output=null;
	ArrayList<String> ellList = new ArrayList<String>();
	
	//Method to connect mongoDB
	ConnectTogivenDBandCollection("cpd", "changeOpportunity");
	Bson MatchFilter = new BsonDocument();
	
	//Filter to form a match query based on inputs
	MatchFilter = Filters.and(Filters.eq("_id.clientKey", Long.valueOf(clientkey)),
				//Filters.eq("ellChange", changeoppty),
				//Filters.ne("releaseInfo.releaseDate", "20200825"),
				Filters.eq("presentationProfile", null));
	
	
	recordsCount = mColl.count(MatchFilter);
	
	if(recordsCount==0)
	{
		System.out.println("No Dpkey is available for the given changetype '"+changeoppty+"',for clientkey '"+clientkey+"'");
		//System.out.println("RecordCount::"+recordsCount+",for matchfilter==>"+MatchFilter);
		return bstatus;
	}
	else
	{
		//System.out.println(MatchFilter);
		
		Bson matchtext = Aggregates.match(Filters.and(MatchFilter));
		
			output = mColl.aggregate(Arrays.asList(matchtext,Aggregates.group(
					new Document().append("DPKey", "$dpKey").append("subRuleKey", "$subRule.subRuleKey").append("midRuleKey","$subRule.midRuleKey"),
					Accumulators.addToSet("MP","$medPolicyDesc"),
					Accumulators.addToSet("Topic","$topicDesc"),
					Accumulators.addToSet("PreviousDPDesc","$dpDescr"),
					Accumulators.addToSet("midRuleKey","$subRule.midRuleKey"),
					Accumulators.addToSet("mpKey","$medPolicyKey"),
					Accumulators.addToSet("topicKey","$topicKey"),
					Accumulators.addToSet("midRuleClaimtypes","$subRule.ruleClaimTypes"),
					Accumulators.addToSet("subRuleId","$subRule.subRuleId"),
					Accumulators.addToSet("releaseName","$releaseInfo.releaseName"),
					Accumulators.addToSet("ellChange","$ellChange")
					))
					).allowDiskUse(true);
		
		
		for (Document doc : output) 
		{
			i=i+1;
			bstatus=true;
			//System.out.println(doc);
			dpkey=StringUtils.substringBetween(String.valueOf(doc), "DPKey=", ", subRuleKey");
			subrulekey=StringUtils.substringBetween(String.valueOf(doc), "subRuleKey=", ", midRuleKey");
			MP=StringUtils.substringBetween(String.valueOf(doc), "MP=[", "], Topic=");
			topic=StringUtils.substringBetween(String.valueOf(doc), "Topic=[", "], PreviousDPDesc");
			dpdesc=StringUtils.substringBetween(String.valueOf(doc), "PreviousDPDesc=[", "], midRuleKey").trim();
			midrulekey=StringUtils.substringBetween(String.valueOf(doc), "midRuleKey=[", "], mpKey");
			mpKey=StringUtils.substringBetween(String.valueOf(doc), "mpKey=[", "], topicKey");
			topicKey=StringUtils.substringBetween(String.valueOf(doc), "topicKey=[", "], midRuleClaimtypes");
			midruleClaimtypes=StringUtils.substringBetween(String.valueOf(doc), "midRuleClaimtypes=[", "], subRuleId");
			String version=StringUtils.substringAfter(StringUtils.substringBetween(String.valueOf(doc), "subRuleId=[", "], releaseName"),".");
			String release=StringUtils.substringBetween(String.valueOf(doc), "releaseName=[", "], ellChange").trim();
			String ellChange=StringUtils.substringBetween(String.valueOf(doc), "ellChange=[", "]}}").trim();
			
			if(!release.contains("August 2020")&&ellChange.contains(changeoppty))
			{
				System.out.println("Release::"+release);
				System.out.println("ellChange::"+ellChange);
				System.out.println("dpkey::"+dpkey);
				System.out.println("subrulekey::"+subrulekey);
				
				claimTypelist=Arrays.asList(midruleClaimtypes.split(","));
				System.out.println(claimTypelist);
				if(criteria.equalsIgnoreCase("CLAIM TYPE ADDED"))
				{
					bstatus=claimTypelist.size()<6;
				}
				else if(criteria.equalsIgnoreCase("CLAIM TYPE REMOVED"))
				{
					bstatus=claimTypelist.size()>1;
					
					/*for (String ct : claimTypelist) 
					{
						if(ct.trim().equalsIgnoreCase("P"))
						{
							bstatus=false;
							break;
						}
					}*/
					
				}
				else
				{
					GenericUtils.Verify("criteria not found::"+criteria, false);
				}
				
				if(bstatus)
				{
					/*MatchFilter = Filters.and(Filters.eq("_id.clientKey", Long.valueOf(clientkey)),
							Filters.eq("_id.dpKey", Long.valueOf(dpkey)),
							Filters.eq("topicDesc", topic),
							Filters.eq("subRule.subRuleKey", Long.valueOf(subrulekey))
							);
					 matchtext = Aggregates.match(Filters.and(MatchFilter));
					
					 AggregateIterable<Document> output2 = mColl.aggregate(Arrays.asList(matchtext,Aggregates.group("$ellChange"))
							).allowDiskUse(true);
					
					
					for (Document doc2:output2) 
					{
						//System.out.println(doc);
						String ellchange=StringUtils.substringBetween(String.valueOf(doc2), "_id=", "}}");
						ellList.add(ellchange);
					}
					//System.out.println();
					System.out.println("ellchanges '"+ellList+"' for DPkey::"+dpkey+",subRuleKey::"+subrulekey);
					System.out.println("eLLChangesize::"+ellList.size());
					if(ellList.size()==1&&ellList.get(0).equalsIgnoreCase(changeoppty))*/
					{
						
						Serenity.setSessionVariable("clientkey").to(clientkey);
						Serenity.setSessionVariable("PREV_MP_TITLE").to(MP);
						Serenity.setSessionVariable("PREV_TOPIC_TITLE").to(topic);
						Serenity.setSessionVariable("PREV_DP_DESC").to(dpdesc);
						Serenity.setSessionVariable("CUR_MID_RULE").to(midrulekey);
						Serenity.setSessionVariable("CUR_SUB_RULE_KEY").to(subrulekey);
						Serenity.setSessionVariable("PREV_DP").to(dpkey);
						Serenity.setSessionVariable("PREV_MP").to(mpKey);//mpkey
						Serenity.setSessionVariable("PREV_TOPIC").to(topicKey);//topickey
						
						Serenity.setSessionVariable("PREV_MID_RULE_CLAIM_TYPES").to(midruleClaimtypes.replaceAll(" ", ""));
						
						Serenity.setSessionVariable("CUR_VERSION").to(version);
						bstatus=true;
						System.out.println("clientkey::"+Serenity.sessionVariableCalled("clientkey"));
						System.out.println("MP::"+Serenity.sessionVariableCalled("PREV_MP_TITLE"));
						System.out.println("Topic::"+Serenity.sessionVariableCalled("PREV_TOPIC_TITLE"));
						System.out.println("Dpkey::"+Serenity.sessionVariableCalled("PREV_DP"));
						System.out.println("DPDesc::"+Serenity.sessionVariableCalled("PREV_DP_DESC"));
						System.out.println("Midrulekey::"+Serenity.sessionVariableCalled("CUR_MID_RULE"));
						System.out.println("Subrulekey::"+Serenity.sessionVariableCalled("CUR_SUB_RULE_KEY"));
						System.out.println("MPKey::"+Serenity.sessionVariableCalled("PREV_MP"));
						System.out.println("TopicKey::"+Serenity.sessionVariableCalled("PREV_TOPIC"));
						System.out.println("clientdesc::"+Serenity.sessionVariableCalled("CLIENT_DESC"));
						System.out.println("PREV_MID_RULE_CLAIM_TYPES::"+Serenity.sessionVariableCalled("PREV_MID_RULE_CLAIM_TYPES"));
						System.out.println("Version::"+Serenity.sessionVariableCalled("CUR_VERSION"));
						
						MatchFilter = Filters.and(Filters.eq("_id.clientKey", Long.valueOf(clientkey)),
								Filters.eq("_id.dpKey", Long.valueOf(dpkey)),
								Filters.eq("ellChange", ellChange),
								Filters.eq("subRule.midRuleKey", Long.valueOf(midrulekey)),
								Filters.eq("subRule.subRuleKey", Long.valueOf(subrulekey)),
								Filters.eq("medPolicyDesc", MP),
								Filters.eq("topicDesc", topic),
								Filters.eq("presentationProfile", null));
					   recordsCount = mColl.count(MatchFilter);
					   System.out.println("PPS Count::"+recordsCount);
					   Serenity.setSessionVariable("PPScount").to(recordsCount);
					   
					   //To get clamitypes for the given type 'Claim Type Added/Removed'
					   getClaimtypeBasedon(Serenity.sessionVariableCalled("PREV_MID_RULE_CLAIM_TYPES"),criteria);
					   
						break;
					}
					
					//ellList.clear();
					
				}
			}
			
			
			
			
			
		}
	if(!bstatus)
	{
		System.out.println("No Dpkey is available for the given changetype '"+changeoppty+"',for clientkey '"+clientkey+"'");
	}
	
   //Serenity.setSessionVariable("Matchfilter").to(MatchFilter);
	return bstatus;
	
	}
	}

	private static void getClaimtypeBasedon(String claimtypes, String criteria)
	{
		String reqCTs=null;
		ArrayList<String> CTs=new ArrayList<>();
		List<String> claimtypeList=Arrays.asList(claimtypes.split(","));
		List<String> expclaimtypeList=Arrays.asList("A,F,P,I,O,S".split(","));
		
		
		switch(criteria)
		{
			case "CLAIM TYPE ADDED":
				CTs.addAll(claimtypeList);
				for (int i = 0; i < expclaimtypeList.size(); i++) 
				{
					if(!claimtypeList.contains(expclaimtypeList.get(i).trim()))
					{
						CTs.add(expclaimtypeList.get(i).trim());
						reqCTs=String.join(",",CTs);
						Serenity.setSessionVariable("CUR_MID_RULE_CLAIM_TYPES").to(reqCTs);
						break;
					}
				}
			break;
			case "CLAIM TYPE REMOVED":
				for (int i = 1; i < claimtypeList.size(); i++) 
				{
					CTs.add(expclaimtypeList.get(i).trim());
				}
				reqCTs=String.join(",",CTs);
				Serenity.setSessionVariable("CUR_MID_RULE_CLAIM_TYPES").to(reqCTs.replaceAll(" ", ""));
			
				break;
			default:
				GenericUtils.Verify("case not found::"+criteria, false);
				break;
			
		
		}
		System.out.println("PREV_MID_RULE_CLAIM_TYPES::"+Serenity.sessionVariableCalled("PREV_MID_RULE_CLAIM_TYPES"));
		System.out.println("CUR_MID_RULE_CLAIM_TYPES::"+Serenity.sessionVariableCalled("CUR_MID_RULE_CLAIM_TYPES"));
		
		
	}
		
	//verify Rule CLaimType Added/Removed post post pipeline
	public static void verifyRuleClaimTypeChangePostpipeline(String dpkey,String criteria,String dptype,String expeLlchange)
	{
		AggregateIterable<Document> output=null;
		boolean bstatus=false;
		String deactDPkey=null;
		String deactDPStatus=null;
		String ruleChangestatus=null;
		String ruleChangePreviousCT=null;
		String ruleChangeCurrentCT=null;
		String ruleChangePreviousDPDesc=null;
		String ruleChangeCurrentDPDesc=null;
		String deactDPDesc=null;
		String ellChange=null;
		String presName=null;
		
		String clientkey=Serenity.sessionVariableCalled("clientkey");
		String targetDP=Serenity.sessionVariableCalled("CUR_DP");
		String midrulekey=Serenity.sessionVariableCalled("CUR_MID_RULE");
		String subrulekey=Serenity.sessionVariableCalled("CUR_SUB_RULE_KEY");
		
		//expected values
		
		String DPkey=Serenity.sessionVariableCalled("PREV_DP");
		String expruleChangestatus="CHANGED";
		String expruleChangePreviousCT=Serenity.sessionVariableCalled("PREV_MID_RULE_CLAIM_TYPES");
		String expruleChangeCurrentCT=Serenity.sessionVariableCalled("CUR_MID_RULE_CLAIM_TYPES");
		//String expellChange="RECATEGORIZED RULE , DEACT DP";
		
		
		//Method to connect mongoDB
		ConnectTogivenDBandCollection("cpd", "changeOpportunity");
		Bson MatchFilter = new BsonDocument();
		
		//Matchfilter Code based on given criteria ####################################################
		switch(criteria)
		{
		
		case "Unassigned":
			//Filter to form a match query based on inputs
			MatchFilter = Filters.and(Filters.eq("_id.clientKey", Long.valueOf(clientkey)),
					Filters.eq("_id.midRuleKey", Long.valueOf(midrulekey)),
					Filters.eq("subRule.subRuleKey", Long.valueOf(subrulekey)),
					Filters.eq("releaseInfo.releaseDate", Serenity.sessionVariableCalled("CUR_RELEASE_DATE")),
					Filters.eq("dpKey", Long.valueOf(dpkey)),
						Filters.eq("presentationProfile", null));
		break;
		case "Without RFP":
			 presName=Serenity.sessionVariableCalled("PresentationName");
			
			//Filter to form a match query based on inputs
			MatchFilter = Filters.and(Filters.eq("_id.clientKey", Long.valueOf(clientkey)),
					Filters.eq("_id.midRuleKey", Long.valueOf(midrulekey)),
					Filters.eq("subRule.subRuleKey", Long.valueOf(subrulekey)),
					Filters.eq("releaseInfo.releaseDate", Serenity.sessionVariableCalled("CUR_RELEASE_DATE")),
					Filters.eq("dpKey", Long.valueOf(dpkey)),
						Filters.eq("presentationProfile.profileName", presName));
			
		break;
		case "With RFP":
			 presName=Serenity.sessionVariableCalled("PresentationName");
			//Filter to form a match query based on inputs
			MatchFilter = Filters.and(Filters.eq("_id.clientKey", Long.valueOf(clientkey)),
					Filters.eq("_id.midRuleKey", Long.valueOf(midrulekey)),
					Filters.eq("subRule.subRuleKey", Long.valueOf(subrulekey)),
					Filters.eq("dpKey", Long.valueOf(dpkey)),
					Filters.eq("ellChange", expeLlchange),
					Filters.eq("releaseInfo.releaseDate", Serenity.sessionVariableCalled("CUR_RELEASE_DATE")),
					Filters.eq("ruleChange.status", "CHANGED"),
					Filters.eq("ruleChange.claimTypes.current", expruleChangeCurrentCT),
					Filters.eq("ruleChange.claimTypes.previous", expruleChangePreviousCT),
						Filters.eq("presentationProfile.profileName", presName));
		break;
		default:
			GenericUtils.Verify("case not found::"+criteria, false);
			
		break;
		}
		
		//Aggregation Code based on given criteria ####################################################
		switch(criteria)
		{
		case "Unassigned":
		case "Without RFP":
			//results = mColl.find(MatchFilter);
			recordsCount = mColl.count(MatchFilter);
			//System.out.println(!(recordsCount==0));
			GenericUtils.Verify("Source DP validation,recordcount is '"+recordsCount+"',Matchfilter::"+MatchFilter, !(recordsCount==0));
			
			Bson matchtext = Aggregates.match(Filters.and(MatchFilter));
			
			output = mColl.aggregate(Arrays.asList(matchtext,Aggregates.group("$dpKey",
					Accumulators.addToSet("ellChange","$ellChange"),
					Accumulators.addToSet("ruleChangestatus","$ruleChange.status"),
					Accumulators.addToSet("ruleChangecurrentClaimtypes","$ruleChange.claimTypes.current"),
					Accumulators.addToSet("ruleChangepreviosClaimtypes","$ruleChange.claimTypes.previous")
					))
					).allowDiskUse(true);
			
			
			for (Document doc:output) 
			{
				System.out.println(doc);
				ruleChangestatus=StringUtils.substringBetween(String.valueOf(doc), "ruleChangestatus=[", "], ruleChangecurrentClaimtypes");
				ruleChangePreviousCT=StringUtils.substringBetween(String.valueOf(doc), "ruleChangepreviosClaimtypes=[", "]}}");
				ruleChangeCurrentCT=StringUtils.substringBetween(String.valueOf(doc), "ruleChangecurrentClaimtypes=[", "], ruleChangepreviosClaimtypes");
				ellChange=StringUtils.substringBetween(String.valueOf(doc), "ellChange=[", "], ruleChangestatus");
			}
		
			GenericUtils.Verify(""+dptype+" DP validation,Expected ellChange::"+expeLlchange+",Actual::"+ellChange+",post PIPELINE,Matchfilter::"+MatchFilter, expeLlchange.equalsIgnoreCase(ellChange));
			GenericUtils.Verify(""+dptype+" DP validation,Expected RuleChange Status::"+expruleChangestatus+",Actual::"+ruleChangestatus+",post PIPELINE,Matchfilter::"+MatchFilter, expruleChangestatus.equalsIgnoreCase(ruleChangestatus));
			GenericUtils.Verify(""+dptype+" DP validation,Expected Previous RulechangeCTs::"+expruleChangePreviousCT+",Actual::"+ruleChangePreviousCT+",post PIPELINE,Matchfilter::"+MatchFilter, expruleChangePreviousCT.equalsIgnoreCase(ruleChangePreviousCT));
			GenericUtils.Verify(""+dptype+" DP validation,Expected Previous RulechangeDPDesc::"+expruleChangeCurrentCT+",Actual::"+ruleChangeCurrentCT+",post PIPELINE,Matchfilter::"+MatchFilter, expruleChangeCurrentCT.equalsIgnoreCase(ruleChangeCurrentCT));
						
			
		break;
		case "With RFP":
			recordsCount = mColl.count(MatchFilter);
			long expPPScount=Long.valueOf(Serenity.sessionVariableCalled("PPScount").toString());
			bstatus=recordsCount==0;
			GenericUtils.Verify("Source DP validation,Expected PPSCount:0,Actual::"+expPPScount+",post DPdeact,Matchfilter::"+MatchFilter, bstatus);
			
		break;
		default:
			GenericUtils.Verify("case not found::"+criteria, false);
			
		break;
		}
	
	}	
	
	/*//Method to set data for claimType added/removed change
	public static boolean getDataforclamiTypeChangeoppty(String clientkey,String changeoppty)
	{
		int i=0;
	boolean bstatus=false;
	String dpkey=null;
	String subrulekey=null;
	String MP=null;
	String topic=null;
	String dpdesc=null;
	String currentdpdesc=null;
	String midrulekey=null;
	String topicKey=null;
	String mpKey=null;
	String clientDesc=null;
	AggregateIterable<Document> output=null;
	ArrayList<String> ellList = new ArrayList<String>();
	
	//Method to connect mongoDB
	ConnectTogivenDBandCollection("cpd", "changeOpportunity");
	Bson MatchFilter = new BsonDocument();
	
	//Filter to form a match query based on inputs
	MatchFilter = Filters.and(Filters.eq("_id.clientKey", Long.valueOf(clientkey)),
				Filters.in("ellChange", changeoppty),
				Filters.ne("releaseInfo.releaseDate", "20200825"),
				Filters.eq("presentationProfile", null));

	
	recordsCount = mColl.count(MatchFilter);
	
	if(recordsCount==0)
	{
		System.out.println("No Dpkey is available for the given changetype '"+changeoppty+"',for clientkey '"+clientkey+"'");
		//System.out.println("RecordCount::"+recordsCount+",for matchfilter==>"+MatchFilter);
		return bstatus;
	}
	else
	{
		//System.out.println(MatchFilter);
		
		Bson matchtext = Aggregates.match(Filters.and(MatchFilter));
		
			output = mColl.aggregate(Arrays.asList(matchtext,Aggregates.group(
					new Document().append("DPKey", "$dpKey").append("subRuleKey", "$subRule.subRuleKey"),
					Accumulators.addToSet("MP","$medPolicyDesc"),
					Accumulators.addToSet("Topic","$topicDesc"),
					Accumulators.addToSet("PreviousDPDesc","$dpDescr"),
					Accumulators.addToSet("midRuleKey","$subRule.midRuleKey"),
					Accumulators.addToSet("mpKey","$medPolicyKey"),
					Accumulators.addToSet("topicKey","$topicKey")
					))
					).allowDiskUse(true);
		
		
		for (Document doc : output) 
		{
			i=i+1;
			//System.out.println(doc);
			dpkey=StringUtils.substringBetween(String.valueOf(doc), "DPKey=", ", subRuleKey");
			subrulekey=StringUtils.substringBetween(String.valueOf(doc), "subRuleKey=", "}}, MP");
			MP=StringUtils.substringBetween(String.valueOf(doc), "MP=[", "], Topic=");
			topic=StringUtils.substringBetween(String.valueOf(doc), "Topic=[", "], PreviousDPDesc");
			dpdesc=StringUtils.substringBetween(String.valueOf(doc), "PreviousDPDesc=[", "], midRuleKey");
			midrulekey=StringUtils.substringBetween(String.valueOf(doc), "midRuleKey=[", "], mpKey");
			mpKey=StringUtils.substringBetween(String.valueOf(doc), "mpKey=[", "], topicKey");
			topicKey=StringUtils.substringBetween(String.valueOf(doc), "topicKey=[", "]}}");
			
			MatchFilter = Filters.and(Filters.eq("_id.clientKey", Long.valueOf(clientkey)),
					Filters.eq("_id.dpKey", Long.valueOf(dpkey)),
					Filters.eq("topicDesc", topic),
					Filters.eq("subRule.subRuleKey", Long.valueOf(subrulekey))
					);
			 matchtext = Aggregates.match(Filters.and(MatchFilter));
			
			 AggregateIterable<Document> output2 = mColl.aggregate(Arrays.asList(matchtext,Aggregates.group("$ellChange"))
					).allowDiskUse(true);
			
			
			for (Document doc2:output2) 
			{
				//System.out.println(doc);
				String ellchange=StringUtils.substringBetween(String.valueOf(doc2), "_id=", "}}");
				ellList.add(ellchange);
			}
			//System.out.println();
			System.out.println("ellchanges '"+ellList+"' for DPkey::"+dpkey+",subRuleKey::"+subrulekey);
			System.out.println("eLLChangesize::"+ellList.size());
			if(ellList.size()==1&&ellList.get(0).equalsIgnoreCase(changeoppty))
			{
				
				Serenity.setSessionVariable("clientkey").to(clientkey);
				Serenity.setSessionVariable("PREV_MP_TITLE").to(MP);
				Serenity.setSessionVariable("PREV_TOPIC_TITLE").to(topic);
				Serenity.setSessionVariable("PREV_DP_DESC").to(dpdesc);
				Serenity.setSessionVariable("CUR_MID_RULE").to(midrulekey);
				Serenity.setSessionVariable("CUR_SUB_RULE_KEY").to(subrulekey);
				Serenity.setSessionVariable("PREV_DP").to(dpkey);
				Serenity.setSessionVariable("PREV_MP").to(mpKey);//mpkey
				Serenity.setSessionVariable("PREV_TOPIC").to(topicKey);//topickey
				bstatus=true;
				System.out.println("clientkey::"+Serenity.sessionVariableCalled("clientkey"));
				System.out.println("MP::"+Serenity.sessionVariableCalled("PREV_MP_TITLE"));
				System.out.println("Topic::"+Serenity.sessionVariableCalled("PREV_TOPIC_TITLE"));
				System.out.println("Dpkey::"+Serenity.sessionVariableCalled("PREV_DP"));
				System.out.println("DPDesc::"+Serenity.sessionVariableCalled("PREV_DP_DESC"));
				System.out.println("Midrulekey::"+Serenity.sessionVariableCalled("CUR_MID_RULE"));
				System.out.println("Subrulekey::"+Serenity.sessionVariableCalled("CUR_SUB_RULE_KEY"));
				System.out.println("MPKey::"+Serenity.sessionVariableCalled("PREV_MP"));
				System.out.println("TopicKey::"+Serenity.sessionVariableCalled("PREV_TOPIC"));
				System.out.println("clientdesc::"+Serenity.sessionVariableCalled("CLIENT_DESC"));
				
				MatchFilter = Filters.and(Filters.eq("_id.clientKey", Long.valueOf(clientkey)),
						Filters.eq("_id.dpKey", Long.valueOf(dpkey)),
						Filters.eq("ellChange", changeoppty),
						Filters.eq("subRule.midRuleKey", Long.valueOf(midrulekey)),
						Filters.eq("subRule.subRuleKey", Long.valueOf(subrulekey)),
						Filters.eq("medPolicyDesc", MP),
						Filters.eq("topicDesc", topic),
						Filters.eq("presentationProfile", null));
			   recordsCount = mColl.count(MatchFilter);
			   System.out.println("PPS Count::"+recordsCount);
			   Serenity.setSessionVariable("PPScount").to(recordsCount);
				break;
			}
			
			//ellList.clear();
			
		}
	if(!bstatus)
	{
		System.out.println("No Dpkey is available for the given changetype '"+changeoppty+"',for clientkey '"+clientkey+"'");
	}
	
   //Serenity.setSessionVariable("Matchfilter").to(MatchFilter);
	return bstatus;
	
	}
	}
	*/
	public static boolean getDataforgivenChangeoppty(String clientkey,String changeType,String changeoppty) throws IOException
	{
		int i=0;
	boolean bstatus=false;
	String dpkey=null;
	String subrulekey=null;
	String MP=null;
	String topic=null;
	String dpdesc=null;
	String currentdpdesc=null;
	String midrulekey=null;
	String topicKey=null;
	String mpKey=null;
	String claimType=null;
	AggregateIterable<Document> output=null;
	ArrayList<String> ellList = new ArrayList<String>();
	
	//Method to connect mongoDB
	ConnectTogivenDBandCollection("cpd", "changeOpportunity");
	Bson MatchFilter = new BsonDocument();
	//Filter to form a match query based on inputs
	
    switch(changeType.toUpperCase()){
	case "NEW RULE":
	case "RULE REFERENCE CHANGE":
	case "RULE VERSION CHANGE":
	case "RULE DEACT CHANGE":
	case "NEW MP TOPIC RECAT":
		if(changeoppty.equals("VERSION CHANGE"))
			MatchFilter = Filters.and(Filters.eq("_id.clientKey", Long.valueOf(clientkey)),
					Filters.exists("ruleChange.version", true),
					Filters.eq("presentationProfile", null));
		else if (changeoppty.equals("NEW RULE"))
			MatchFilter = Filters.and(Filters.eq("_id.clientKey", Long.valueOf(clientkey)),
					Filters.eq("ruleChange.status", "NEW"),
					Filters.eq("presentationProfile", null));
		else if(changeoppty.equals("RECATEGORIZED RULE"))
			MatchFilter = Filters.and(Filters.eq("_id.clientKey", Long.valueOf(clientkey)),
					Filters.exists("ruleChange.recategorized", true),
					Filters.eq("presentationProfile", null));
		else
			MatchFilter = Filters.and(Filters.eq("_id.clientKey", Long.valueOf(clientkey)),
					Filters.eq("ellChange", changeoppty),
					Filters.eq("presentationProfile", null));		
		break;
	default: MatchFilter = Filters.and(Filters.eq("_id.clientKey", Long.valueOf(clientkey)),
				Filters.eq("ellChange", changeoppty),
				Filters.eq("presentationProfile", null));
	}
  	recordsCount = mColl.count(MatchFilter);
	
	if(recordsCount==0)
	{
		System.out.println("RecordCount::"+recordsCount+",for matchfilter==>"+MatchFilter);
		return bstatus;
	}
	else
	{
		//System.out.println(MatchFilter);
		
		Bson matchtext = Aggregates.match(Filters.and(MatchFilter));
		
			output = mColl.aggregate(Arrays.asList(matchtext,Aggregates.group(
					new Document().append("DPKey", "$dpKey").append("subRuleKey", "$subRule.subRuleKey"),
					Accumulators.addToSet("MP","$medPolicyDesc"),
					Accumulators.addToSet("Topic","$topicDesc"),
					Accumulators.addToSet("PreviousDPDesc","$dpDescr"),
					Accumulators.addToSet("midRuleKey","$subRule.midRuleKey"),
					Accumulators.addToSet("mpKey","$medPolicyKey"),
					Accumulators.addToSet("topicKey","$topicKey"),
					Accumulators.addToSet("releaseName","$releaseInfo.releaseName"),
                    Accumulators.addToSet("ellChange","$ellChange")

					))
					).allowDiskUse(true);
		
		
		for (Document doc : output) 
		{
			i=i+1;
			//System.out.println(doc);
			dpkey=StringUtils.substringBetween(String.valueOf(doc), "DPKey=", ", subRuleKey");
			subrulekey=StringUtils.substringBetween(String.valueOf(doc), "subRuleKey=", "}}, MP");
			MP=StringUtils.substringBetween(String.valueOf(doc), "MP=[", "], Topic=");
			topic=StringUtils.substringBetween(String.valueOf(doc), "Topic=[", "], PreviousDPDesc");
			dpdesc=StringUtils.substringBetween(String.valueOf(doc), "PreviousDPDesc=[", "], midRuleKey");
			midrulekey=StringUtils.substringBetween(String.valueOf(doc), "midRuleKey=[", "], mpKey");
			mpKey=StringUtils.substringBetween(String.valueOf(doc), "mpKey=[", "], topicKey");
            topicKey=StringUtils.substringBetween(String.valueOf(doc), "topicKey=[", "], releaseName");
            String release=StringUtils.substringBetween(String.valueOf(doc), "releaseName=[", "], ellChange").trim();
            String ellChange=StringUtils.substringBetween(String.valueOf(doc), "ellChange=[", "]}}").trim();
           
            if(!release.contains("August 2020")) {

            	Serenity.setSessionVariable("clientkey").to(clientkey);
            	Serenity.recordReportData().withTitle("clientKey").andContents(clientkey);
				Serenity.setSessionVariable("PREV_MP_TITLE").to(MP);
				Serenity.setSessionVariable("PREV_TOPIC_TITLE").to(topic);
				Serenity.setSessionVariable("PREV_DP_DESC").to(dpdesc);
				Serenity.setSessionVariable("CUR_MID_RULE").to(midrulekey);
				Serenity.setSessionVariable("CUR_SUB_RULE_KEY").to(subrulekey);
				Serenity.setSessionVariable("PREV_DP").to(dpkey);
				Serenity.recordReportData().withTitle("dpKey").andContents(dpkey);
				Serenity.setSessionVariable("PREV_MP").to(mpKey);//mpkey
				Serenity.setSessionVariable("PREV_TOPIC").to(topicKey);
				Serenity.setSessionVariable("CLAIM_TYPE").to(claimType);//topickey
				bstatus=true;
				System.out.println("clientkey::"+Serenity.sessionVariableCalled("clientkey"));
				System.out.println("MP::"+Serenity.sessionVariableCalled("PREV_MP_TITLE"));
				System.out.println("Topic::"+Serenity.sessionVariableCalled("PREV_TOPIC_TITLE"));
				System.out.println("Dpkey::"+Serenity.sessionVariableCalled("PREV_DP"));
				System.out.println("DPDesc::"+Serenity.sessionVariableCalled("PREV_DP_DESC"));
				System.out.println("Midrulekey::"+Serenity.sessionVariableCalled("CUR_MID_RULE"));
				System.out.println("Subrulekey::"+Serenity.sessionVariableCalled("CUR_SUB_RULE_KEY"));
				System.out.println("MPKey::"+Serenity.sessionVariableCalled("PREV_MP"));
				System.out.println("TopicKey::"+Serenity.sessionVariableCalled("PREV_TOPIC"));
				System.out.println("claimType::"+Serenity.sessionVariableCalled("CLAIM_TYPE"));
				MatchFilter = Filters.and(Filters.eq("_id.clientKey", Long.valueOf(clientkey)),
							Filters.eq("_id.dpKey", Long.valueOf(dpkey)),
							Filters.eq("ellChange", ellChange),
							Filters.eq("subRule.midRuleKey", Long.valueOf(midrulekey)),
							Filters.eq("subRule.subRuleKey", Long.valueOf(subrulekey)),
							Filters.eq("medPolicyDesc", MP),
							Filters.eq("topicDesc", topic),
							Filters.ne("releaseInfo.releaseDate", "20200825"),
							Filters.eq("presentationProfile", null));
			   recordsCount = mColl.count(MatchFilter);
			   System.out.println("PPS Count::"+recordsCount);
			   Serenity.setSessionVariable("PPScount").to(recordsCount);
			break;
			
		}else
        {
            System.out.println("Getting releasename as '"+release+"'and ellchange as '"+ellChange+"',No Dpkey is available for the given changetype '"+changeoppty+"',for clientkey '"+clientkey+"'");
            
        }				
		}
		return bstatus;
	}
	}
	
	public static long verifyRuleDataforgivenChangeoppty(String clientkey,String changeType,String changeoppty,String criteria)
	{
		int i=0;
		boolean bstatus=false;
		String presName=null;
		String dpkey=Serenity.sessionVariableCalled("PREV_DP");
		Serenity.recordReportData().withTitle("dpKey").andContents(dpkey);
		String MP=Serenity.sessionVariableCalled("PREV_MP_TITLE");
		String topic=Serenity.sessionVariableCalled("PREV_TOPIC_TITLE");
		String midrulekey=Serenity.sessionVariableCalled("CUR_MID_RULE");
		String releaseDate= StringUtils.substringBetween(Serenity.sessionVariableCalled("CUR_RELEASE_DATE"), "\"", "\"");
		AggregateIterable<Document> output=null;
		
		//Method to connect mongoDB
		ConnectTogivenDBandCollection("cpd", "changeOpportunity");
		Bson MatchFilter = new BsonDocument();
		switch(changeType) {
		case "NEW RULE":
			if(criteria.equals("Without RFP"))
				presName=Serenity.sessionVariableCalled("PresentationName");			
			switch(criteria)
			{
			case "Unassigned":
			case "With RFP":
			case "Without RFP"://Filter to form a match query based on inputs
				MatchFilter = Filters.and(Filters.eq("_id.clientKey", Long.valueOf(clientkey)),
						Filters.eq("_id.dpKey", Long.valueOf(dpkey)),
						Filters.eq("ellChange", "NEW RULE"),
						Filters.eq("subRule.midRuleKey", Long.valueOf(midrulekey)),
						Filters.eq("medPolicyDesc", MP),
						Filters.eq("topicDesc", topic),
						Filters.eq("ruleChange.status","NEW"),
						Filters.eq("releaseInfo.releaseDate",releaseDate),
						Filters.eq("presentationProfile.profileName", presName));
			break;
			}
			break;
		case "RULE REFERENCE CHANGE":
			if(criteria.contains("Without RFP"))
				presName=Serenity.sessionVariableCalled("PresentationName");			
			switch(criteria)
			{
			case "Unassigned":
			case "With RFP":
			case "Without RFP":
				MatchFilter = Filters.and(Filters.eq("_id.clientKey", Long.valueOf(clientkey)),
						Filters.eq("_id.dpKey", Long.valueOf(dpkey)),
						//Filters.eq("ellChange", changeoppty),
						Filters.eq("subRule.midRuleKey", Long.valueOf(midrulekey)),
						Filters.eq("medPolicyDesc", MP),
						Filters.eq("topicDesc", topic),
						Filters.eq("releaseInfo.releaseDate",releaseDate),
						Filters.exists("ruleChange.version.previous", true),
						Filters.exists("ruleChange.version.current", true),
						Filters.exists("ruleChange.reference.previous", true),
						Filters.exists("ruleChange.reference.current", true),
						Filters.eq("presentationProfile.profileName", presName));
				break;
			}
			break;
		case "RULE VERSION CHANGE":
			if(criteria.contains("Without RFP"))
				presName=Serenity.sessionVariableCalled("PresentationName");			
			switch(criteria)
			{
			case "Unassigned":
			case "With RFP":
			case "Without RFP":
				MatchFilter = Filters.and(Filters.eq("_id.clientKey", Long.valueOf(clientkey)),
						Filters.eq("_id.dpKey", Long.valueOf(dpkey)),
						Filters.eq("ellChange", changeoppty),
						Filters.eq("subRule.midRuleKey", Long.valueOf(midrulekey)),
						Filters.eq("medPolicyDesc", MP),
						Filters.eq("topicDesc", topic),
						Filters.eq("releaseInfo.releaseDate",releaseDate),
						Filters.exists("ruleChange.version.previous", true),
						Filters.exists("ruleChange.version.current", true),
						Filters.eq("presentationProfile.profileName", presName));
				break;
			}
			break;
		case "RULE DESC CHANGE":
			if(criteria.contains("Without RFP"))
				presName=Serenity.sessionVariableCalled("PresentationName");			
			switch(criteria)
			{
			case "Unassigned":
			case "With RFP":
				MatchFilter = Filters.and(Filters.eq("_id.clientKey", Long.valueOf(clientkey)),
						Filters.eq("_id.dpKey", Long.valueOf(dpkey)),
						//Filters.eq("ellChange", changeoppty),
						Filters.eq("subRule.midRuleKey", Long.valueOf(midrulekey)),
						Filters.eq("medPolicyDesc", MP),
						Filters.eq("topicDesc", topic),
						Filters.eq("releaseInfo.releaseDate",releaseDate),
						Filters.exists("ruleChange.version.previous", true),
						Filters.exists("ruleChange.version.current", true),
						Filters.exists("ruleChange.description.previous", true),
						Filters.exists("ruleChange.description.current", true),
						Filters.eq("presentationProfile.profileName", presName));
				break;
			case "Without RFP":
				MatchFilter = Filters.and(Filters.eq("_id.clientKey", Long.valueOf(clientkey)),
						Filters.eq("_id.dpKey", Long.valueOf(dpkey)),
						Filters.eq("ellChange", changeoppty),
						Filters.eq("subRule.midRuleKey", Long.valueOf(midrulekey)),
						Filters.eq("medPolicyDesc", MP),
						Filters.eq("topicDesc", topic),
						Filters.eq("releaseInfo.releaseDate",releaseDate),
						Filters.exists("ruleChange.version.previous", true),
						Filters.exists("ruleChange.version.current", true),
						Filters.eq("presentationProfile.profileName", presName));
				break;			
			}
			break;
		case "RULE DEACT CHANGE":
			if(criteria.equals("Without RFP"))
				presName=Serenity.sessionVariableCalled("PresentationName");			
			switch(criteria)
			{
			case "Unassigned":
			case "Without RFP":
			case "With RFP":
				//Filter to form a match query based on inputs
				MatchFilter = Filters.and(Filters.eq("_id.clientKey", Long.valueOf(clientkey)),
						Filters.eq("_id.dpKey", Long.valueOf(dpkey)),
						Filters.eq("ellChange", changeoppty),
						Filters.eq("subRule.midRuleKey", Long.valueOf(midrulekey)),
						Filters.eq("medPolicyDesc", MP),
						Filters.eq("topicDesc", topic),
						Filters.eq("ruleChange.status","DEACTIVATED"),
						Filters.eq("releaseInfo.releaseDate",releaseDate),
						Filters.eq("presentationProfile.profileName", presName));
			break;
			}
			break;
		case "NEW MP TOPIC RECAT":
			if(criteria.equals("Without RFP"))
				presName=Serenity.sessionVariableCalled("PresentationName");			
			switch(criteria)
			{
			case "Unassigned":
			case "Without RFP":
			case "With RFP":
				//Filter to form a match query based on inputs
				MatchFilter = Filters.and(Filters.eq("_id.clientKey", Long.valueOf(clientkey)),
						Filters.eq("_id.dpKey", Long.valueOf(dpkey)),
						Filters.eq("ellChange", changeoppty),
						Filters.eq("policyChange.status","NEW"),
						Filters.exists("topicChange.recategorized", true),
						Filters.eq("releaseInfo.releaseDate",releaseDate),
						Filters.eq("presentationProfile.profileName", presName));
			break;
			}
			break;
		case "NEW TOPIC":
			if(criteria.equals("Without RFP"))
				presName=Serenity.sessionVariableCalled("PresentationName");			
			switch(criteria)
			{
			case "Unassigned":
			case "Without RFP":
			case "With RFP":
				//Filter to form a match query based on inputs
				MatchFilter = Filters.and(Filters.eq("_id.clientKey", Long.valueOf(clientkey)),
						Filters.eq("_id.dpKey", Long.valueOf(dpkey)),
						Filters.eq("ellChange", changeoppty),
						Filters.eq("subRule.midRuleKey", Long.valueOf(midrulekey)),
						Filters.eq("medPolicyDesc", MP),
						Filters.eq("topicDesc", topic),
						Filters.eq("topicChange.status","NEW"),
						Filters.exists("dpChange.recategorized.previous", true),
						Filters.exists("dpChange.recategorized.current", true),
						Filters.eq("releaseInfo.releaseDate",releaseDate),
						Filters.eq("presentationProfile.profileName", presName));
			break;
			}
			break;
		default:
			GenericUtils.Verify("case not found::"+criteria, false);
			
		
		}
		recordsCount = mColl.count(MatchFilter);
		System.out.println("RecordCount::"+recordsCount+",for matchfilter==>"+MatchFilter);
		Serenity.setSessionVariable("Matchfilter").to(MatchFilter);
		
		return recordsCount;
		
		
	}
}