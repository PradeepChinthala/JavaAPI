package projects.steps.definitions;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.bson.BsonDocument;
import org.bson.conversions.Bson;
import org.junit.Assert;

import com.google.gson.JsonObject;
import com.mongodb.client.model.DBCollectionUpdateOptions;
import com.mongodb.client.model.Filters;

import net.serenitybdd.core.Serenity;
import net.thucydides.core.annotations.Step;
import net.thucydides.core.steps.ScenarioSteps;
import project.pageobjects.PresentationDeck;
import project.utilities.ExcelUtils;
import project.utilities.GenericUtils;
import project.utilities.MongoDBUtils;
import project.utilities.OracleDBUtils;
import project.variables.MonGoDBQueries;
import project.variables.OracleDBQueries;
import project.variables.ProjectVariables;

public class DBValidationsStepDef extends ScenarioSteps {

	private static final long serialVersionUID = 1L;

	PresentationDeck oPresentationDeck;

	@Step
	public void EllDataVerificationwithOracleandMongoDB() {

		String sELLDBQuery = OracleDBQueries.getOracleDBQuery("ELL QUERY");
		ArrayList<String> sOracleDBResult = OracleDBUtils.db_GetEllDatafromOracle(sELLDBQuery);

		sOracleDBResult.add("ItemnotPResent");
		sOracleDBResult.add(
				"MID RULE:20197;TOPIC KEY:3223;TOPIC DESC:Behavioral HealthMP KEY:63MP DESC:Healthplan PolicyMP SORTORDER:207DP KEY:11646DP DESC:Decision Point for Behavioral HealthDP SORTORDER:1REF KEY:null");
		sOracleDBResult.add(
				"MID RULE:20193;TOPIC KEY:1983;TOPIC DESC:Physical Therapy/Physical MedicineMP KEY:63MP DESC:Healthplan PolicyMP SORTORDER:207DP KEY:6525DP DESC:Decision Point for Physical Therapy/Physical MedicineDP SORTORDER:1REF KEY:null");
		sOracleDBResult.add(
				"MID RULE:20193;TOPIC KEY:1983;TOPIC DESC:Physical Therapy/Physica MedicineMP KEY:63MP DESC:Healthplan PolicyMP SORTORDER:207DP KEY:6525DP DESC:Decision Point for Physical Therapy/Physical MedicineDP SORTORDER:1REF KEY:null");
		sOracleDBResult.add(
				"MID RULE:2019;TOPIC KEY:3223;TOPIC DESC:Behavioral HealthMP KEY:63MP DESC:Healthplan PolicyMP SORTORDER:207DP KEY:11646DP DESC:Decision Point for Behavioral HealthDP SORTORDER:1REF KEY:null");

		Collections.sort(sOracleDBResult);
		ArrayList<Object> sValue = Serenity.sessionVariableCalled("MidRules");

		ArrayList<String> sMongoDBResult = MongoDBUtils.getEllDatafromMongoDB(sValue);
		Collections.sort(sMongoDBResult);

		for (int i = 0; i < sOracleDBResult.size(); i++) {
			boolean blnVal = sMongoDBResult.contains(sOracleDBResult.get(i));
			if (!blnVal) {
				ELLDataMismatched("Mismatched " + sOracleDBResult.get(i));
			}
		}

	}

	@Step
	public void ELLDataMismatched(String sValue) {
		System.out.println(sValue);

	}

	@Step
	public void Print(String sValue) {
		System.out.println(sValue);

	}

	@Step
	public void TopicLongDescVerificationwithEllandPolicyHierarchy() {

		ArrayList<Object> sDisctictTopicKeys = MongoDBUtils.getTopicKeysHavingchange();

		String sTopicKeys = StringUtils.join(sDisctictTopicKeys, ",");
		System.out.println(sTopicKeys);

		HashMap<String, String> sListELL = MongoDBUtils.getChangesforTopicKeysinEllData(sDisctictTopicKeys);

		HashMap<String, String> sListPolicy = MongoDBUtils.getTopicKeysfromPolicyHierarchy(sDisctictTopicKeys);

		sListELL.size();
		sListPolicy.size();

		Iterator<Map.Entry<String, String>> itr = sListPolicy.entrySet().iterator();

		while (itr.hasNext()) {
			try {
				Map.Entry<String, String> entry = itr.next();
				String sPolicyDesc = entry.getValue().trim();
				String EllDesc = sListELL.get(entry.getKey()).trim();
				if (entry.getKey().toString().equalsIgnoreCase("2454;8517")) {
					EllDesc = EllDesc + "Test";
				}

				if (!EllDesc.equalsIgnoreCase(sPolicyDesc)) {
					TopicLongDescMismatched("Mismatched" + entry.getKey());
					TopicLongDescMismatched("Mismatched values are" + sPolicyDesc + " and " + EllDesc);
				}

			} catch (Exception e) {

			}

		}
	}

	@Step
	public void TopicLongDescMismatched(String sValue) {
		System.out.println(sValue);

	}

	@Step
	public void CompareELLDatawithOpptyCollection() {
		ArrayList<Object> sAllMidRules = MongoDBUtils.getDistinctMidRuleKeysfromEllCollection();
		HashMap<String, String> sELLData = MongoDBUtils.getEllDataforGivenMidRules(sAllMidRules);
		MongoDBUtils.verifyEllDatainOpptyforGivenMidRules(sELLData);
	}

	@Step
	public void Validate_the_PPS_between_and_MongoDB_for_monthly_policy_release(String sOracleDBName,
			String sMongoDBCollection) {
		ArrayList<String> sOracleMidrulecount = new ArrayList<>();
		ArrayList<String> sOraclPPS = null;
		ArrayList<String> sNotAvailableOraclPPS = new ArrayList<>();
		ArrayList<String> sNotAvailableMidrules = new ArrayList<>();
		Long MongoDBPPSCount = 0l;
		String sQuery = null;
		boolean bStatus = false;
		int startnum = 0;
		int lastnum = 0;

		startnum = Integer.valueOf(StringUtils.substringBetween(sOracleDBName, "Oracele-", "to").trim());
		lastnum = Integer.valueOf(StringUtils.substringAfter(sOracleDBName, "to").trim());

		sQuery = OracleDBQueries.getOracleDBQuery("PROD MIDRULE COUNT");
		System.out.println("Required Oracle Query==>" + sQuery);

		// Oracle Method to retrieve the policysets count
		sOracleMidrulecount = OracleDBUtils.executeSQLQuery(sQuery, ProjectVariables.DB_CONNECTION_URL_PMPRD2, "Count");

		if (sOracleMidrulecount.isEmpty()) {
			Assert.assertTrue("Unable to retrieve the policy records count from PMPRD2 Database", false);
		}

		// Mongo Method to retrieve the policysets count
		MongoDBPPSCount = MongoDBUtils.GetThePPSforthegivenMidruleKey("", "Count");

		if (sOracleMidrulecount.size() == MongoDBPPSCount) {
			System.out.println("Policysets records count is matching b/w oracle and mongoDb,oracle recordcount==>"
					+ sOracleMidrulecount.size() + ",mongoDb recordcount==>" + MongoDBPPSCount);
		} else {
			System.out.println("Policysets records count is not matching b/w oracle and mongoDb,oracle recordcount==>"
					+ sOracleMidrulecount.size() + ",mongoDb recordcount==>" + MongoDBPPSCount);
		}

		for (int i = startnum; i < lastnum; i++) {
			String smidRule = sOracleMidrulecount.get(i).trim();
			sQuery = StringUtils.replace(OracleDBQueries.getOracleDBQuery("PROD MIDRULE PPS"), "midrule", smidRule);
			System.out.println("Required Oracle Query==>" + sQuery);
			Verify(i + ".MidRuleKey==>" + smidRule + ",Total MidRules==>" + sOracleMidrulecount.size(), true);

			// Oracle Method to retrieve the data for the given midrule
			sOraclPPS = OracleDBUtils.executeSQLQuery(sQuery, ProjectVariables.DB_CONNECTION_URL_PMPRD2, "All Rows");

			// Mongo Method to retrieve the data for the given midrule
			Long recordsCount = MongoDBUtils.GetThePPSforthegivenMidruleKey(smidRule, "");

			if (recordsCount == 0) {
				sNotAvailableMidrules.add(smidRule);
				// Assert.assertTrue("Record count was zero for the given
				// midrulekey==>"+smidRule, false);
			} else {
				for (int j = 0; j < sOraclPPS.size(); j++) {
					if (ProjectVariables.PPSList.contains(sOraclPPS.get(j))) {
						System.out
						.println("Oracle PPS record is available in MongoDB,Oracle PPS==>" + sOraclPPS.get(j));
					} else {
						System.out.println("Oracle PPS record is not available in MongoDB,Oracle PPS==>"
								+ sOraclPPS.get(j) + ",for the MidRule==>" + smidRule);
						sNotAvailableOraclPPS.add(sOraclPPS.get(j) + ";Midrule==>" + smidRule);
					}
				}

			}

		}

		System.out.println("NotAvailableOraclPPS ==>" + sNotAvailableOraclPPS);

		if (sNotAvailableOraclPPS.isEmpty()) {
			bStatus = sNotAvailableMidrules.isEmpty();
			Verify("Record count was zero in the MongoDB for the given midrulekeys==>" + sNotAvailableMidrules,
					bStatus);
		} else {
			Verify("Record count was zero in the MongoDB for the given midrulekeys==>" + sNotAvailableMidrules, true);
			Verify("Not available PPS in mongoDb from Oracle==>" + sNotAvailableOraclPPS, false);
		}

	}

	@Step
	public void Verify(String StepDetails, boolean sStatus) {

		if (sStatus) {
			System.out.println(StepDetails);
			Assert.assertTrue(StepDetails, true);
		} else {
			System.out.println(StepDetails);
			Assert.assertTrue(StepDetails, false);

		}
	}

	@Step
	public void CompareSortOrderWithOracleandMongoDB(String sLevel) {

		String sQuery = null;

		switch (sLevel.toUpperCase()) {

		case "DP":
			sQuery = "GET DPS FOR SORT ORDER DIFFERENCE";
			break;

		case "TOPIC":
			sQuery = "GET TOPICS FOR SORT ORDER DIFFERENCE";
			break;

		case "MEDICAL POLICY":
			sQuery = "GET MPS FOR SORT ORDER DIFFERENCE";
			break;

		case "DPS RETIRE":
			sQuery = "GET DPS FOR RETIRE";
			break;

		case "MPS RETIRE":
			sQuery = "GET MPS FOR RETIRE";
			break;

		case "TPS RETIRE":
			sQuery = "GET TPS FOR RETIRE";
			break;

		default:
			Assert.assertTrue("No critirea matched", false);

		}

		ArrayList<String> sListELL = OracleDBUtils.db_GetAllFirstColumnValues(OracleDBQueries.getOracleDBQuery(sQuery));
		ArrayList<Integer> sVal = new ArrayList<Integer>();

		for (String isListELL : sListELL) {
			sVal.add(Integer.valueOf(isListELL));
		}

		System.out.println(sVal.size());
		MongoDBUtils.validateSortOrder(sVal, sLevel, "ellData");

	}

	@Step
	public void trackActiveInactiveStatus(String sRulesType, String sFields) {

		ArrayList<String> sOracleDBResult = OracleDBUtils.db_GetRuleStatus(OracleDBQueries.getOracleDBQuery(sRulesType),
				sFields);

		System.out.println(sOracleDBResult.size());

		ArrayList<Integer> sSubRules = Serenity.sessionVariableCalled("slstSubRules");

		ArrayList<String> sMongoDBResult = MongoDBUtils.getRuleStatus(sSubRules, sFields);

		Collections.sort(sOracleDBResult);

		Collections.sort(sMongoDBResult);

		for (int i = 0; i < sOracleDBResult.size(); i++) {
			boolean blnVal = sMongoDBResult.contains(sOracleDBResult.get(i));
			if (!blnVal) {
				ELLDataMismatched("Mismatched " + sOracleDBResult.get(i));
			}
		}
	}

	@Step
	public void updateOpportunitiesbetweenElltoOpptyCollection(String sLevel) {

		ArrayList<Integer> sSubRules = MongoDBUtils.getRetiredMidRulesFromEll(sLevel);

		if (sSubRules.size() == 0) {
			System.out.println("No MidRules Present for given criteria");
		} else {
			MongoDBUtils.validateSortOrder(sSubRules, sLevel, "oppty");
		}

	}

	@Step
	public void CompareEllDatatoOpptyCollectionforStatus(String sDisabled, String sDeactivate, String sRetiredStatus,
			String sValue) {

		ArrayList<Integer> sSubRules = MongoDBUtils.getSubRuleKeyhavingStatus(sDisabled, sDeactivate, sRetiredStatus,
				"monthly_ot");

		MongoDBUtils.ValidateStatusforSubRules(sSubRules, sValue, "oppty");

	}

	@Step
	public void abilityToTrackNewVersionCreated() throws SQLException {

		ArrayList<String> sOracleMidRulesData = OracleDBUtils.getNewVersionCreatedRules(
				OracleDBQueries.getOracleDBQuery("NEW RULE VERSION RULES"), "NEW_MID_RULE;OLD_VERSION;NEW_VERSION");

		ArrayList<Integer> sMidRules = Serenity.sessionVariableCalled("slstMidRules");

		System.out.println(sMidRules.size());

		ArrayList<String> sMongoMidRulesData = MongoDBUtils.getNewVersionRulesfromMongo(sMidRules, "ellData");

		Collections.sort(sOracleMidRulesData);

		Collections.sort(sMongoMidRulesData);

		for (int i = 0; i < sOracleMidRulesData.size(); i++) {
			boolean blnVal = sMongoMidRulesData.contains(sOracleMidRulesData.get(i));
			if (!blnVal) {
				ELLDataMismatched("Mismatched " + sOracleMidRulesData.get(i));
			}
		}

	}

	@Step
	public void notifyEllChangesOnPresenationProfile(String sChange) {

		MongoDBUtils.getNotificationChangeData(sChange, "");

	}

	@Step
	public void CleanDataSheet(String sPath) throws IOException, InterruptedException {
		ExcelUtils.cleanSheet(sPath);

	}

	@Step
	public void validateNofications(String sWorkbookPath) throws Throwable {
		oPresentationDeck.verifyNotifications(sWorkbookPath);

	}

	@Step
	public void retrieveRuleRelationDetailsFromOracle(String ValuesToFetch, String DBInstanceName, String ClientName,
			String InputValues) {

		ArrayList<String> DPKeysVals = new ArrayList<String>();
		ArrayList<String> MidRules = new ArrayList<String>();

		Connection con = null;
		ResultSet rs;
		String sMidRuleKey = "";
		Map<String, ArrayList<String>> MidRulesAssociatedActiveRulesMap = new HashMap<String, ArrayList<String>>();
		Map<String, ArrayList<String>> MidRulesRelationAssociatedRulesMap = new HashMap<String, ArrayList<String>>();
		Map<String, ArrayList<String>> MidRulesInactiveRelationsAssociatedRulesMap = new HashMap<String, ArrayList<String>>();

		con = OracleDBUtils.getDBConnection(DBInstanceName);
		String ClientKey = MongoDBUtils.Retrieve_the_PayerKey_From_mongodb_with_the_given_Payershort(ClientName,
				"Client");
		int ToalMidRulesCount = 0;

		switch (ValuesToFetch) {

		case "TotalRecordsForClient":
			try {
				Statement Stmt1 = con.createStatement();
				Statement Stmt2 = con.createStatement();

				String qry = "Select count(*) from rva_ia.vw_rva_cpd WHERE client_key = " + ClientKey
						+ " and data_version = 'PMPRD1_20191023_185426'";
				rs = Stmt1.executeQuery(qry);
				String TotalrecordsforClient = rs.getString(1);

				// Get all the MidRules
				String qry2 = "Select distinct MID_RULE_KEY from rva_ia.vw_rva_cpd WHERE client_key = " + ClientKey
						+ " and data_version = 'PMPRD1_20191023_185426' order by mid_rule_key";
				ResultSet rs2 = Stmt2.executeQuery(qry2);

				while (rs.next()) {
					sMidRuleKey = rs.getString("MID_RULE_KEY").toString();
					MidRules.add(sMidRuleKey.trim());
					ToalMidRulesCount = ToalMidRulesCount + 1;
				}

			} catch (Exception e) {
				System.out.println(e.getMessage());
			}
			break;

		case "MidRulesDetails":
			try {
				Statement Stmt1 = con.createStatement();

				// Get all the MidRules
				String qry2 = "Select distinct MID_RULE_KEY from rva_ia.vw_rva_cpd WHERE client_key = " + ClientKey
						+ " and data_version = 'PMPRD1_20191023_185426' order by mid_rule_key";
				rs = Stmt1.executeQuery(qry2);

				while (rs.next()) {
					sMidRuleKey = rs.getString("MID_RULE_KEY").toString();
					MidRules.add(sMidRuleKey.trim());
					ToalMidRulesCount = ToalMidRulesCount + 1;
				}

			} catch (Exception e) {
				System.out.println(e.getMessage());
			}

			Serenity.setSessionVariable("ClientMidRules").to(MidRules);

			break;

		case "DistinctRuleRelationships":
			try {
				Statement Stmt1 = con.createStatement();
				Serenity.sessionVariableCalled("ClientMidRules");

				// For each mid rule find the RuleRelations using below query

				// Get all the MidRules
				String qry2 = "  select * from RULE_UPKEEP.VW_MR_RELATIONS where mid_rule_key = 14411";
				rs = Stmt1.executeQuery(qry2);

				while (rs.next()) {
					sMidRuleKey = rs.getString("MID_RULE_KEY").toString();
					MidRules.add(sMidRuleKey.trim());
					ToalMidRulesCount = ToalMidRulesCount + 1;
				}

			} catch (Exception e) {
				System.out.println(e.getMessage());
			}

			break;
		}

	}

	@Step
	public void validateRuleRelationDetailsinMDB(String mDBCollectionName) {
		MongoDBUtils.getValuesForRuleRelationshipForMidRules(mDBCollectionName);

	}

	@Step
	public void ValidateMonthlyToOpptyCollections(String sCollection, String sfields, String sPrimaryKey) {

		ArrayList<String> sMongoOpptyColData = MongoDBUtils.getMongoOpptyColData(sCollection, sfields);

		Print("Distinct Oppty count for Collection " + sCollection + " : " + sMongoOpptyColData.size());

		ArrayList<String> sMongoColData = MongoDBUtils.getMongoMonthlyColData(sCollection, sfields, sPrimaryKey);

		Print(sCollection + " count : " + sMongoColData.size());

		Collections.sort(sMongoOpptyColData);

		Collections.sort(sMongoColData);

		for (int i = 0; i < sMongoOpptyColData.size(); i++) {
			boolean blnVal = sMongoColData.contains(sMongoOpptyColData.get(i));
			if (!blnVal) {
				ELLDataMismatched("Mismatched " + sMongoOpptyColData.get(i));
			}
		}

	}

	@Step
	public void SwitchKey(String strClientKey, String sfields, String sPrimaryKey) {

		ArrayList<JsonObject> sLstValues = MongoDBUtils.getMongoSwitchColData(strClientKey, sfields, sPrimaryKey);

		Print(strClientKey + " PPS count: " + sLstValues.size());

		ArrayList<Integer> sMidRules = Serenity.sessionVariableCalled("sKeyValues");

		ArrayList<String> iPayerKeys = new ArrayList<String>();

		System.out.println(sMidRules.size());

		ArrayList<String> resultList = new ArrayList<String>();

		for (int i = 0; i <= sLstValues.size(); i++) {
			String sProd;
			String sTest;
			String strSubRuleKey = null;
			String strPayerKey = null;
			String strInsKey = null;
			String strClaimType = null;
			String result = null;
			String sMidRule = null;
			String PPS = null;

			try {
				sProd = sLstValues.get(i).get("prod_10").toString().trim();
				if (sProd.equalsIgnoreCase("0")) {
					sProd = null;
				}
			} catch (Exception e) {
				sProd = null;
			}

			try {
				sTest = sLstValues.get(i).get("test_10").toString().trim();
				if (sTest.equalsIgnoreCase("0")) {
					sTest = null;
				}
			} catch (Exception e) {
				sTest = null;
			}

			try {
				strSubRuleKey = sLstValues.get(i).get("subRuleKey").toString().trim();
			} catch (Exception e) {
				strSubRuleKey = null;
			}

			try {
				strPayerKey = sLstValues.get(i).get("payerKey").toString().trim();
			} catch (Exception e) {
				strPayerKey = null;
			}
			try {
				strInsKey = sLstValues.get(i).get("insuranceKey").toString().trim();
			} catch (Exception e) {
				strInsKey = null;
			}
			try {
				strClaimType = sLstValues.get(i).get("claimType").toString().trim().replaceAll("^\"+|\"+$", "");
			} catch (Exception e) {
				strClaimType = null;
			}

			try {
				sMidRule = sLstValues.get(i).get("rule").toString().split("\\.")[0].trim();
			} catch (Exception e) {
				sMidRule = null;
			}

			PPS = "Client Key:" + strClientKey + ";Insurance Key:" + strInsKey + ";Payer Key:" + strPayerKey
					+ ";Sub Rule Key:" + strSubRuleKey + ";Claim Type:" + strClaimType + ";Mid Rule:" + sMidRule;

			iPayerKeys.add(PPS.trim());

			result = "Client Key:" + strClientKey + ";Insurance Key:" + strInsKey + ";Payer Key:" + strPayerKey
					+ ";Sub Rule Key:" + strSubRuleKey + ";Claim Type:" + strClaimType + ";Prod 10:" + sProd
					+ ";Test 10:" + sTest + ";Mid Rule:" + sMidRule;

			resultList.add(result);

		}

		Serenity.setSessionVariable("sPayerKeys").to(iPayerKeys);

		ArrayList<String> sMongoOpptyData = MongoDBUtils.ValMongoSwitchinOppty(strClientKey, sLstValues);

		Print("Matched Count in Oppty" + sMongoOpptyData.size());

		Collections.sort(sMongoOpptyData);

		Collections.sort(resultList);

		for (int i = 0; i < sMongoOpptyData.size(); i++) {

			boolean blnVal = resultList.contains(sMongoOpptyData.get(i));
			if (!blnVal) {
				ELLDataMismatched("Mismatched " + sMongoOpptyData.get(i));
			}
		}
	}

	@Step
	public void compareRuleRelationshipData() {

		ArrayList<String> DPKeysVals = new ArrayList<String>();
		ArrayList<String> MidRules = new ArrayList<String>();

		ArrayList<String> OOSPrimary = new ArrayList<String>();
		ArrayList<String> OOSSecondary = new ArrayList<String>();
		ArrayList<String> CounterpartNA = new ArrayList<String>();
		ArrayList<String> MECMS = new ArrayList<String>();
		ArrayList<String> CompanionNA = new ArrayList<String>();
		ArrayList<String> MECMSCotvt = new ArrayList<String>();

		ArrayList<String> OOSPrimaryInActive = new ArrayList<String>();
		ArrayList<String> OOSSecondaryInActive = new ArrayList<String>();
		ArrayList<String> CounterpartNAInActive = new ArrayList<String>();
		ArrayList<String> MECMSInActive = new ArrayList<String>();
		ArrayList<String> CompanionNAInActive = new ArrayList<String>();
		ArrayList<String> MECMSCotvtInActive = new ArrayList<String>();

		Connection con = null;
		String sMidRuleKey = "";

		boolean OOSPrimaryRelationKeysExists = false;
		boolean OOSSecondaryRelationKeysExists = false;
		boolean CounterpartNARelationKeysExists = false;
		boolean MECMSRelationKeysExists = false;
		boolean CompanionNARelationKeysExists = false;
		boolean MECMSCotvtRelationKeysExists = false;

		Map<String, ArrayList<String>> MidRulesAssociatedActiveRulesMap = new HashMap<String, ArrayList<String>>();
		Map<String, ArrayList<String>> MidRulesRelationAssociatedRulesMap = new HashMap<String, ArrayList<String>>();
		Map<String, ArrayList<String>> MidRulesInactiveRelationsAssociatedRulesMap = new HashMap<String, ArrayList<String>>();

		HashMap<String, String> AssociatedActiveAndInactiveRulesRelationsMap = new HashMap<String, String>();

		Map<String, HashMap<String, String>> MidRulesAssociatedActiveAndInactiveRulesRelationMap = new HashMap<String, HashMap<String, String>>();

		ArrayList<String> AssociatedActiveAndInactiveRules = null;

		// con = OracleDBUtils.getDBConnection("PMPRD2"); // For previous
		// monthly release collection it was PMPRD2,but for latest release they
		// refreshed data from VPMTST1

		con = OracleDBUtils.getDBConnection("VPMTST1");

		try {

			String qry = "Select r.mid_rule_key,count(1) from RULE_UPKEEP.VW_MR_RELATIONS r " + " ,rules.sub_rules sr "
					+ "  where r.last_updated_ts <= to_date('27/05/2020','dd/mm/yyyy') "
					+ "  and sr.mid_rule_key = r.mid_rule_key " + "  and sr.deactivated_10 = 0 "
					+ "  and sr.library_status_key = 1 " + " group by r.mid_rule_key";

			Statement Stmt1 = con.createStatement();
			Statement Stmt2 = con.createStatement();
			ResultSet rs = Stmt1.executeQuery(qry);
			int i = 1;
			int DeactivatedFlag = 2;

			while (rs.next()) {
				System.out.println("Value::" + rs.getString("MID_RULE_KEY").toString());
				sMidRuleKey = rs.getString("MID_RULE_KEY").toString();

				// Storing all Midrules in Array to compare the count with
				// MongoDB collection
				MidRules.add(sMidRuleKey.trim());

				int MidRuleKey = rs.getInt("MID_RULE_KEY");
				String qry2 = "select r.mid_rule_key,S.SUB_RULE_KEY,R.GROUP_NAME,R.TRAIT_A_NAME,R.ASSOCIATE_MID_RULE_KEY,r.deactivated_10 from  RULE_UPKEEP.VW_MR_RELATIONS r,RULES.SUB_RULES s "
						+ " where r.last_updated_ts <= to_date('27/05/2020','dd/mm/yyyy') "
						+ " and r.mid_rule_key = s.mid_rule_key " + " and s.library_status_key = 1 "
						+ " AND S.DEACTIVATED_10=0 " + " and r.mid_rule_key =" + MidRuleKey;

				ResultSet rs2 = Stmt2.executeQuery(qry2);

				AssociatedActiveAndInactiveRules = new ArrayList<String>();

				OOSPrimary = new ArrayList<String>();
				OOSSecondary = new ArrayList<String>();
				CounterpartNA = new ArrayList<String>();
				MECMS = new ArrayList<String>();
				CompanionNA = new ArrayList<String>();
				MECMSCotvt = new ArrayList<String>();

				OOSPrimaryInActive = new ArrayList<String>();
				OOSSecondaryInActive = new ArrayList<String>();
				CounterpartNAInActive = new ArrayList<String>();
				MECMSInActive = new ArrayList<String>();
				CompanionNAInActive = new ArrayList<String>();
				MECMSCotvtInActive = new ArrayList<String>();

				OOSPrimaryRelationKeysExists = false;
				OOSSecondaryRelationKeysExists = false;
				CounterpartNARelationKeysExists = false;
				MECMSRelationKeysExists = false;
				CompanionNARelationKeysExists = false;
				MECMSCotvtRelationKeysExists = false;

				while (rs2.next()) {

					String Rule = rs2.getString("ASSOCIATE_MID_RULE_KEY").toString().trim();
					AssociatedActiveAndInactiveRules.add(Rule);
					String RuleRelation = rs2.getString("GROUP_NAME").toString().trim() + " : "
							+ rs2.getString("TRAIT_A_NAME").toString().trim();
					DeactivatedFlag = Integer.parseInt(rs2.getString("DEACTIVATED_10").toString());

					switch (RuleRelation) {
					case "OUT OF SEQUENCE : Primary":

						if (DeactivatedFlag == 0) {
							OOSPrimary.add(Rule);
						} else if (DeactivatedFlag == -1) {
							OOSPrimaryInActive.add(Rule);
						}

						OOSPrimaryRelationKeysExists = true;
						break;

					case "OUT OF SEQUENCE : Secondary":

						if (DeactivatedFlag == 0) {
							OOSSecondary.add(Rule);
						} else if (DeactivatedFlag == -1) {
							OOSSecondaryInActive.add(Rule);
						}

						OOSSecondaryRelationKeysExists = true;
						break;

					case "COUNTERPART : NA":

						if (DeactivatedFlag == 0) {
							CounterpartNA.add(Rule);
						} else if (DeactivatedFlag == -1) {
							CounterpartNAInActive.add(Rule);
						}

						CounterpartNARelationKeysExists = true;
						break;

					case "MUTUALLY EXCLUSIVE : CMS Only":
						if (DeactivatedFlag == 0) {
							MECMS.add(Rule);
						} else if (DeactivatedFlag == -1) {
							MECMSInActive.add(Rule);
						}

						MECMSRelationKeysExists = true;
						break;

					case "COMPANION : NA":

						if (DeactivatedFlag == 0) {
							CompanionNA.add(Rule);
						} else if (DeactivatedFlag == -1) {
							CompanionNAInActive.add(Rule);
						}

						CompanionNARelationKeysExists = true;
						break;

					case "MUTUALLY EXCLUSIVE : CMS + Cotiviti":

						if (DeactivatedFlag == 0) {
							MECMSCotvt.add(Rule);
						} else if (DeactivatedFlag == -1) {
							MECMSCotvtInActive.add(Rule);
						}

						MECMSCotvtRelationKeysExists = true;
						break;
					}

					AssociatedActiveAndInactiveRulesRelationsMap.put(Rule, RuleRelation);

				}
				rs2.close();
				String GroupCat = "";

				if (OOSPrimaryRelationKeysExists == true) {
					GroupCat = "A";

					if (!OOSPrimary.isEmpty()) {
						MidRulesRelationAssociatedRulesMap.put(sMidRuleKey + ":" + GroupCat, OOSPrimary);
					}

					if ((!OOSPrimaryInActive.isEmpty())) {
						MidRulesInactiveRelationsAssociatedRulesMap.put(sMidRuleKey + ":" + GroupCat,
								OOSPrimaryInActive);
					}

				}
				if (OOSSecondaryRelationKeysExists == true) {
					GroupCat = "B";

					if (!OOSSecondary.isEmpty()) {
						MidRulesRelationAssociatedRulesMap.put(sMidRuleKey + ":" + GroupCat, OOSSecondary);
					}
					if (!OOSSecondaryInActive.isEmpty()) {
						MidRulesInactiveRelationsAssociatedRulesMap.put(sMidRuleKey + ":" + GroupCat,
								OOSSecondaryInActive);
					}

				}
				if (CounterpartNARelationKeysExists == true) {
					GroupCat = "C";

					if (!CounterpartNA.isEmpty()) {
						MidRulesRelationAssociatedRulesMap.put(sMidRuleKey + ":" + GroupCat, CounterpartNA);
					}
					if (!CounterpartNAInActive.isEmpty()) {
						MidRulesInactiveRelationsAssociatedRulesMap.put(sMidRuleKey + ":" + GroupCat,
								CounterpartNAInActive);
					}

				}
				if (MECMSRelationKeysExists == true) {
					GroupCat = "D";
					if (!MECMS.isEmpty()) {
						MidRulesRelationAssociatedRulesMap.put(sMidRuleKey + ":" + GroupCat, MECMS);
					}
					if (!MECMSInActive.isEmpty()) {
						MidRulesInactiveRelationsAssociatedRulesMap.put(sMidRuleKey + ":" + GroupCat, MECMSInActive);
					}
				}
				if (CompanionNARelationKeysExists == true) {
					GroupCat = "E";

					if (!CompanionNA.isEmpty()) {
						MidRulesRelationAssociatedRulesMap.put(sMidRuleKey + ":" + GroupCat, CompanionNA);
					}
					if (!CompanionNAInActive.isEmpty()) {
						MidRulesInactiveRelationsAssociatedRulesMap.put(sMidRuleKey + ":" + GroupCat,
								CompanionNAInActive);
					}
				}
				if (MECMSCotvtRelationKeysExists == true) {
					GroupCat = "F";

					if (!MECMSCotvt.isEmpty()) {
						MidRulesRelationAssociatedRulesMap.put(sMidRuleKey + ":" + GroupCat, MECMSCotvt);
					}
					if (!MECMSCotvtInActive.isEmpty()) {
						MidRulesInactiveRelationsAssociatedRulesMap.put(sMidRuleKey + ":" + GroupCat,
								MECMSCotvtInActive);
					}

				}
				MidRulesAssociatedActiveRulesMap.put(sMidRuleKey, AssociatedActiveAndInactiveRules);
				MidRulesAssociatedActiveAndInactiveRulesRelationMap.put(sMidRuleKey,
						AssociatedActiveAndInactiveRulesRelationsMap);

			}

			Serenity.setSessionVariable("MidRulesActiveAndInactiveRulesMapOracle").to(MidRulesAssociatedActiveRulesMap);
			Serenity.setSessionVariable("MidRulesAssociatedActiveAndInactiveRulesRelationsMap")
			.to(MidRulesAssociatedActiveAndInactiveRulesRelationMap);

			Serenity.setSessionVariable("ActiveRulesAndInactiveRuleRelationMap")
			.to(AssociatedActiveAndInactiveRulesRelationsMap);
			Serenity.setSessionVariable("AllMidRules").to(MidRules);
			Serenity.setSessionVariable("MidRulesRelationMapping").to(MidRulesRelationAssociatedRulesMap);

			// InActiveRules --Storing the MidRules and associated Relationship
			// representation Inactive MidRules
			Serenity.setSessionVariable("MidRulesInactiveRelationMapping")
			.to(MidRulesInactiveRelationsAssociatedRulesMap);

			rs.close();
			con.close();
		}

		catch (Exception e) {
			System.out.println(e.getMessage());
		}
		MongoDBUtils.validateMonthlyReleaseCollection("MidRulesCountValidationMDBtoOracle", "");
		MongoDBUtils.validateMonthlyReleaseCollection("RuleRelationshipsValidationfromMDBtoOracle", "");

	}

	@Step
	public void abilityToUpdateAndNotifyNewVersionCreated() throws Exception {
		boolean bStatus=false;
		ArrayList<Long> smidRules=MongoDBUtils.checkForMidRuleKeyForNewVersionCreation();
		Collections.sort(smidRules);
		int i=1;
		int j=1;
		for(Long mMidRuleKey:smidRules)
		{
			System.out.println("Total MidRules===>"+smidRules);
			System.out.println("TotalMidRules Count===>"+smidRules.size());
			System.out.println(i+".MidRule====>"+mMidRuleKey);
			bStatus=MongoDBUtils.checkForClientKeyDetailsForMidRuleKey(mMidRuleKey,"New Version");
			if(bStatus)
			{
				for(Long iClientKey:ProjectVariables.Clientkeylist)
				{
					MongoDBUtils.getLatestDataVersionFromClientKey(iClientKey);
					System.out.println("Release====>"+Serenity.sessionVariableCalled("LatestReleaseDate").toString());
					String subrule=String.valueOf(mMidRuleKey)+"."+Serenity.sessionVariableCalled("NewVersion").toString();
					MongoDBUtils.checkForPPSCombinationHavingSwitch(Serenity.sessionVariableCalled("ClientKey"), Serenity.sessionVariableCalled("LatestReleaseDate").toString(), mMidRuleKey,subrule,"");
					Serenity.setSessionVariable("MidRuleKey").to(mMidRuleKey);
					String oracleOutput=OracleDBUtils.executeSQLQuery(OracleDBQueries.getOracleDBQuery("NEW RULE VERSION UPDATE AND NOTIFY"));
					System.out.println("**********OracleOutput"+oracleOutput);
					if(oracleOutput.isEmpty())
					{
						MongoDBUtils.PPSCombinationSwitchVerificationForNewVersion(Serenity.sessionVariableCalled("ClientKey"), Serenity.sessionVariableCalled("LatestReleaseDate").toString(), subrule);
						System.out.println("Switch Records Are Empty");
					}
					else
					{
						MongoDBUtils.verifyPPSSwitchNotAddedToNewVersion(Serenity.sessionVariableCalled("ClientKey"), Serenity.sessionVariableCalled("LatestReleaseDate").toString(), subrule);
					}
					j=j+1;
				}
			}
			else
			{
				System.out.println("MidRule '"+mMidRuleKey+"' is not available in oppty collection");
			}
			i=i+1;
		}
	}

	@Step
	public void abilityToTrackUpdateAndNotifyNewMidRuleisCreated() throws Exception
	{
		ArrayList<Long> smidRules=MongoDBUtils.checkForMidRuleKeysForNewMidRuleCreation();
		boolean bStatus=false;
		int i=1;
		for(Long mMidRuleKey:smidRules)
		{
			System.out.println("Total MidRules===>"+smidRules);
			System.out.println("TotalMidRules Count===>"+smidRules.size());
			System.out.println(i+".MidRule====>"+mMidRuleKey);
			bStatus=MongoDBUtils.checkForClientKeyDetailsForMidRuleKey(mMidRuleKey,"New MidRule");
			if(bStatus)
			{
				for(Long iClientKey:ProjectVariables.Clientkeylist)
				{
					MongoDBUtils.getLatestDataVersionFromClientKey(iClientKey);
					System.out.println("MidRule====>"+mMidRuleKey);
					System.out.println("ClientKey====>"+Serenity.sessionVariableCalled("ClientKey"));
					System.out.println("Release====>"+Serenity.sessionVariableCalled("LatestReleaseDate").toString());
					String subrule=String.valueOf(mMidRuleKey)+"."+Serenity.sessionVariableCalled("NewVersion").toString();
					MongoDBUtils.checkForPPSCombinationHavingSwitch(Serenity.sessionVariableCalled("ClientKey"), Serenity.sessionVariableCalled("LatestReleaseDate").toString(), mMidRuleKey,subrule,Serenity.sessionVariableCalled("DPKey").toString());
					Serenity.setSessionVariable("MidRuleKey").to(mMidRuleKey);
					System.out.println(OracleDBUtils.executeSQLQuery(OracleDBQueries.getOracleDBQuery("NEW RULE VERSION UPDATE AND NOTIFY")));
					String oracleOutput=OracleDBUtils.executeSQLQuery(OracleDBQueries.getOracleDBQuery("NEW RULE VERSION UPDATE AND NOTIFY"));
					System.out.println("**********   OracleOutput"+oracleOutput);
					if(oracleOutput.isEmpty())
					{
						MongoDBUtils.PPSCombinationSwitchVerificationForNewVersion(Serenity.sessionVariableCalled("ClientKey"), Serenity.sessionVariableCalled("LatestReleaseDate").toString(), subrule);
						System.out.println("Switch Records Are Empty");
					}
					else
					{
						MongoDBUtils.verifyPPSSwitchNotAddedToNewVersion(Serenity.sessionVariableCalled("ClientKey"), Serenity.sessionVariableCalled("LatestReleaseDate").toString(), subrule);
						Assert.assertTrue("Switch has been created for the given mid rule===>",false);
					}
				}
			}
			else
			{
				System.out.println("MidRule '"+mMidRuleKey+"' is not available in oppty collection");
			}
			i=i+1;
		}
	}
	
	//=================================================================================================

	@Step
	public void verifyTheDuplicateDPandPPSIn(String collection, String clientkey) throws ParseException 
	{
		ArrayList<String> dispList=new ArrayList<>();
		
			switch(collection)
			{
			case "oppty_prod":
				dispList.addAll(MongoDBUtils.getTheDistinctDispositionsbasedOnClient(clientkey,"oppty"));
				for (int i = 0; i < dispList.size(); i++) 
				{
					if(!dispList.get(i).equalsIgnoreCase("Prior Approval"))
					{//&&!dispList.get(i).equalsIgnoreCase("No Disposition")Prior Approval
						//To retrieve the Duplicate DP+PPs from oppty_prod collection
						//MongoDBUtils.getDuplicateDataversionforPPS(clientkey,"oppty",dispList.get(i));
						MongoDBUtils.getDuplicateDataversionforDPPayrshort(clientkey,"oppty",dispList.get(i));
						//To verify the Duplicate DP+PPs from oppty_prod collection
						//MongoDBUtils.verifyTheDuplicatePPS(clientkey,"oppty");
						
					}
				}
				break;
			case "profileoppty_prod":
				dispList.addAll(MongoDBUtils.getTheDistinctDispositionsbasedOnClient(clientkey,"profileOppty"));
				for (int i = 0; i < dispList.size(); i++) 
				{
					//if(!dispList.get(i).equalsIgnoreCase("Present")&&!dispList.get(i).equalsIgnoreCase("No Disposition"))
					{
						//To retrieve the Duplicate DP+PPs from oppty_prod collection
						//MongoDBUtils.getDuplicateDataversionforPPS(clientkey,"profileOppty",dispList.get(i));
						MongoDBUtils.getDuplicateDataversionforDPPayrshort(clientkey,"profileOppty",dispList.get(i));
						
						//To verify the Duplicate DP+PPs from oppty_prod collection
						//MongoDBUtils.verifyTheDuplicatePPS(clientkey,"profileOppty_automationTestSet");
					}
				}
				break;
			default:
				Assert.assertTrue("Case not found==>"+collection, false);
				break;
			}
		
		
	}

	@Step
	public void userComparesDataFromOracleToMongo(String sDataType) {
		switch(sDataType){
		case "DP's":
			int sExpectedCount;
			long sActualCount;
			String[] sValidations="DP WITH CONFIGURATION AND INFORMATIONAL,DP WITH ALL,SORT ORDER WITH 1,SORT ORDER WITHOUT 1".split(",");
			for(int i=0;i<sValidations.length;i++){
				sExpectedCount=Integer.parseInt(OracleDBUtils.executeSQLQuery(OracleDBQueries.getOracleDBQuery(sValidations[i])));
				sActualCount=MongoDBUtils.retrieveAllDocuments("cpd","ellHierarchy",MonGoDBQueries.FilterMongoDBQuery(sValidations[i]));
				GenericUtils.Verify("comparision between Mongo and Oracle for '"+sValidations[i].toLowerCase()+"' MongoCount:"+sActualCount+" OracleCount:"+sExpectedCount, sExpectedCount== (int) sActualCount);
			}
			break;
			default:Assert.assertTrue("Invalid case selection", false);
		}		
	}
	
	@Step
	public void compareDpsFromOracleToMongo() {
		
		String sELLDBQuery = OracleDBQueries.getOracleDBQuery("TEST RULES");
		ArrayList<String> sOracleDBResult = OracleDBUtils.db_DPDatafromOracle(sELLDBQuery);
		
		GenericUtils.Verify("Oracle Count ="+sOracleDBResult.size(), true);

		/*sOracleDBResult.add("ItemnotPResent");
		sOracleDBResult.add(
				"MID RULE:20197;TOPIC KEY:3223;TOPIC DESC:Behavioral HealthMP KEY:63MP DESC:Healthplan PolicyMP SORTORDER:207DP KEY:11646DP DESC:Decision Point for Behavioral HealthDP SORTORDER:1REF KEY:null");
		sOracleDBResult.add(
				"MID RULE:20193;TOPIC KEY:1983;TOPIC DESC:Physical Therapy/Physical MedicineMP KEY:63MP DESC:Healthplan PolicyMP SORTORDER:207DP KEY:6525DP DESC:Decision Point for Physical Therapy/Physical MedicineDP SORTORDER:1REF KEY:null");
		sOracleDBResult.add(
				"MID RULE:20193;TOPIC KEY:1983;TOPIC DESC:Physical Therapy/Physica MedicineMP KEY:63MP DESC:Healthplan PolicyMP SORTORDER:207DP KEY:6525DP DESC:Decision Point for Physical Therapy/Physical MedicineDP SORTORDER:1REF KEY:null");
		sOracleDBResult.add(
				"MID RULE:2019;TOPIC KEY:3223;TOPIC DESC:Behavioral HealthMP KEY:63MP DESC:Healthplan PolicyMP SORTORDER:207DP KEY:11646DP DESC:Decision Point for Behavioral HealthDP SORTORDER:1REF KEY:null");
*/
		Collections.sort(sOracleDBResult);
		ArrayList<Object> sValue = Serenity.sessionVariableCalled("MidRules");

		ArrayList<String> sMongoDBResult = MongoDBUtils.getDPDatafromMongoDB(sValue);

		GenericUtils.Verify("Mongo DB Count ="+sMongoDBResult.size(), true);
		
		Collections.sort(sMongoDBResult);
		 int iMisMatchCount= 0;
		for (int i = 0; i < sOracleDBResult.size(); i++) {
			System.out.println("Mongo Mistmatch ="+sMongoDBResult.get(i));
			System.out.println("Oracle Mismatch ="+sOracleDBResult.get(i));
			boolean blnVal = sMongoDBResult.contains(sOracleDBResult.get(i));
			if (!blnVal) {
				Serenity.recordReportData().asEvidence().withTitle(sOracleDBResult.get(i)).andContents("Mismatched");
				iMisMatchCount = iMisMatchCount+1;
			}
		}
		
		GenericUtils.Verify("Mismatched Count ="+iMisMatchCount, true);

		
	}

	@Step
	public void compareLatestColctnFromOracleToMongo(String sDataType) throws org.json.simple.parser.ParseException {
	
		String sELLDBQuery = OracleDBQueries.getOracleDBQuery(sDataType);
		ArrayList<String> sOracleDBResult = OracleDBUtils.db_LatestCollectionDatafromOracle(sELLDBQuery);
		
		GenericUtils.Verify("Oracle Count ="+sOracleDBResult.size(), true);

		Collections.sort(sOracleDBResult);

		ArrayList<String> sMongoDBResult = MongoDBUtils.getLatestCollectionDataDatafromMongoDB(sDataType);

		GenericUtils.Verify("Mongo DB Count ="+sMongoDBResult.size(), true);
		
		Collections.sort(sMongoDBResult);
		 int iMisMatchCount= 0;
		for (int i = 0; i < sOracleDBResult.size(); i++) {			
			boolean blnVal = sMongoDBResult.contains(sOracleDBResult.get(i));			
			GenericUtils.Verify("Oracle Record ="+sOracleDBResult.get(i), true);
			if (!blnVal) {
				System.out.println("Mongo Mistmatch ="+sMongoDBResult.get(i));				
				System.out.println("Oracle Mismatch ="+sOracleDBResult.get(i));
				GenericUtils.Verify("Mongo Mistmatch ="+sMongoDBResult.get(i), true);
				GenericUtils.Verify("Oracle Mismatch  ="+sOracleDBResult.get(i), true);
				Serenity.recordReportData().asEvidence().withTitle(sOracleDBResult.get(i)).andContents("Mismatched");
				iMisMatchCount = iMisMatchCount+1;
			}
		}
		
		GenericUtils.Verify("Oracle to Mongo Mismatched="+iMisMatchCount, true);
		int iMongoMisMatchCount= 0;
		for (int i = 0; i < sMongoDBResult.size(); i++) {			
			boolean blnVal = sOracleDBResult.contains(sMongoDBResult.get(i));
			GenericUtils.Verify("Mongo Record ="+sMongoDBResult.get(i), true);
			if (!blnVal) {
				System.out.println("Mongo Mistmatch ="+sMongoDBResult.get(i));				
				System.out.println("Oracle Mismatch ="+sOracleDBResult.get(i));
				GenericUtils.Verify("Mongo Mistmatch ="+sMongoDBResult.get(i), true);
				GenericUtils.Verify("Oracle Mismatch  ="+sOracleDBResult.get(i), true);
				Serenity.recordReportData().asEvidence().withTitle(sMongoDBResult.get(i)).andContents("Mismatched");
				iMongoMisMatchCount = iMongoMisMatchCount+1;
			}
		}
		GenericUtils.Verify("Mongo to Oracle Mismatched count="+iMongoMisMatchCount, true);
		
	}

	//######################################### Duplicate Desc Method ##############################################################
	@Step
	public void verifyTheDuplicateDesc(String criteria,String collname,String clientkey) throws ParseException 
	{
		String sFieldname=null;
		ArrayList<String> dispList=new ArrayList<>();
		
			switch(criteria)
			{
			case "Medicalpolicy":
				sFieldname="medPolicyDesc";
				break;
			case "Topic":
				sFieldname="topicDesc";
				break;
			case "DP":
				sFieldname="dpDesc";
				break;
			default:
				Assert.assertTrue("Case not found==>"+criteria, false);
				break;
			}
			dispList.addAll(MongoDBUtils.getTheDistinctDispositionsbasedOnClient(clientkey,collname));
			for (int i = 0; i < dispList.size(); i++) 
			{
				if(!dispList.get(i).equalsIgnoreCase("Prior Approval"))
				{
					MongoDBUtils.getDuplicateDesc(clientkey, collname, dispList.get(i).trim(), sFieldname);
					
				}
			}
		
		
	}

}