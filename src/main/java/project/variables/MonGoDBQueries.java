package project.variables;

import java.util.ArrayList;
import java.util.Arrays;

import org.apache.commons.lang3.StringUtils;
import org.bson.BsonDocument;
import org.bson.conversions.Bson;

import com.mongodb.client.model.Accumulators;
import com.mongodb.client.model.Aggregates;
import com.mongodb.client.model.Filters;

import junit.framework.Assert;
import net.serenitybdd.core.Serenity;
import project.utilities.OracleDBUtils;

public class MonGoDBQueries {

	public Bson GetDistinctPayershortLOB(String sInputs) {

		Bson MatchFilter = new BsonDocument();
		// Spliting Input
		String[] sFinalInput = sInputs.split(":");
		MatchFilter = Filters.and(Filters.eq("clientDesc", sFinalInput[0]), Filters.ne("_id.dataVersion", ""),
				Filters.eq("disposition.desc", "Present"));
		return MatchFilter;
	}

	// ===============================================================================================================================================>
	public static Bson FilterMongoDBQuery(String sInputs) {
		Bson MatchFilter = new BsonDocument();

		switch (sInputs.toUpperCase()) {
		case "SUPPRESS":
		case "APPROVE WITH MODIFICATION":
		case "APPROVE":
		case "REJECT":
		case "NO DECISION":
		case "APPROVE LIBRARY":
			MatchFilter = Filters.and(
					Filters.eq("_id.clientKey", Long.valueOf(Serenity.sessionVariableCalled("clientkey"))),
					Filters.eq("_id.dataVersion",
							java.util.regex.Pattern.compile(Serenity.sessionVariableCalled("release"))),
					Filters.eq("latestClientDecision.cdmDecision", sInputs), Filters.eq("ruleInBaseLine", 0),
					Filters.eq("disposition.desc", "No Disposition"), Filters.ne("annualSavings.lines", 0));
			break;
		case "APPROVE LIBRARY WITHOUT RELEASE":
			MatchFilter = Filters.and(
					Filters.eq("latestClientDecision.cdmDecision", StringUtils.substringBefore(sInputs, "without release").trim()), Filters.eq("ruleInBaseLine", 0),
					Filters.eq("disposition.desc", "No Disposition"), Filters.ne("annualSavings.lines", 0));
			break;
		case "MULTIPLE_DP":
			MatchFilter = Filters.and(
					Filters.eq("_id.clientKey", Long.valueOf(Serenity.sessionVariableCalled("clientkey"))),
					Filters.eq("_id.dataVersion",
							java.util.regex.Pattern.compile(Serenity.sessionVariableCalled("release"))),
					Filters.eq("ruleInBaseLine", 0), Filters.eq("disposition.desc", "No Disposition"),
					Filters.ne("annualSavings.lines", 0));
			break;
		case "DPKEYS BASED ON TOPIC":
			MatchFilter = Filters.and(
					Filters.eq("_id.clientKey", Long.valueOf(Serenity.sessionVariableCalled("clientkey"))),
					Filters.eq("_id.dataVersion",
							java.util.regex.Pattern.compile(Serenity.sessionVariableCalled("release"))),
					Filters.eq("ruleInBaseLine", 0), Filters.eq("disposition.desc", "Present"),
					Filters.eq("subRule.hierarchy.topicDesc", Serenity.sessionVariableCalled("Topic")));
			break;
		case "DPKEYS BASED ON MP":
			MatchFilter = Filters.and(
					Filters.eq("_id.clientKey", Long.valueOf(Serenity.sessionVariableCalled("clientkey"))),
					Filters.eq("_id.dataVersion",
							java.util.regex.Pattern.compile(Serenity.sessionVariableCalled("release"))),
					Filters.eq("ruleInBaseLine", 0), Filters.eq("disposition.desc", "Present"),
					Filters.eq("subRule.hierarchy.medPolicyDesc", Serenity.sessionVariableCalled("Medicalpolicy")));
			break;

		case "DP COUNT BASED ON CLIENT":
			MatchFilter = Filters.and(
					Filters.eq("_id.clientKey", Long.valueOf(Serenity.sessionVariableCalled("clientkey"))),Filters.eq("opptyFinalize", null),
					Filters.eq("ruleInBaseLine", 0), Filters.eq("disposition.desc", "Present"));
			break;

		case "DP COUNT BASED ON TOPIC":
			MatchFilter = Filters.and(
					Filters.eq("_id.clientKey", Long.valueOf(Serenity.sessionVariableCalled("clientkey"))),Filters.eq("opptyFinalize", null),
					Filters.eq("ruleInBaseLine", 0), Filters.eq("disposition.desc", "Present"),
					Filters.eq("subRule.hierarchy.topicDesc", Serenity.sessionVariableCalled("Topic")));
			break;
		case "DP COUNT BASED ON MP":
			MatchFilter = Filters.and(
					Filters.eq("_id.clientKey", Long.valueOf(Serenity.sessionVariableCalled("clientkey"))),Filters.eq("opptyFinalize", null),
					Filters.eq("ruleInBaseLine", 0), Filters.eq("disposition.desc", "Present"),
					Filters.eq("subRule.hierarchy.medPolicyDesc", java.util.regex.Pattern.compile(Serenity.sessionVariableCalled("Medicalpolicy"))));
			break;
		case "DP COUNT BASED ON CLIENT WITH FILTERS":
			if(ProjectVariables.CapturedClaimtypesList.isEmpty())
			{
				MatchFilter = Filters.and(
						Filters.eq("_id.clientKey", Long.valueOf(Serenity.sessionVariableCalled("clientkey"))),
						Filters.eq("ruleInBaseLine", 0), Filters.eq("disposition.desc", "Present"),Filters.eq("opptyFinalize", null),
						Filters.in("payerShort", ProjectVariables.CapturedPayershortList),
						Filters.in("insuranceDesc", ProjectVariables.CapturedInsuranceList));
					
			}
			else
			{
				MatchFilter = Filters.and(
						Filters.eq("_id.clientKey", Long.valueOf(Serenity.sessionVariableCalled("clientkey"))),Filters.in("_id.claimType", ProjectVariables.CapturedClaimtypesList),
						Filters.eq("ruleInBaseLine", 0), Filters.eq("disposition.desc", "Present"),Filters.eq("opptyFinalize", null),
						Filters.in("payerShort", ProjectVariables.CapturedPayershortList),
						Filters.in("insuranceDesc", ProjectVariables.CapturedInsuranceList));
				
			}
			break;

		case "DP COUNT BASED ON TOPIC WITH FILTERS":
			MatchFilter = Filters.and(
					Filters.eq("_id.clientKey", Long.valueOf(Serenity.sessionVariableCalled("clientkey"))),Filters.eq("opptyFinalize", null),
					Filters.eq("ruleInBaseLine", 0), Filters.eq("disposition.desc", "Present"),
					Filters.eq("subRule.hierarchy.topicDesc", Serenity.sessionVariableCalled("Topic")),
					Filters.in("payerShort", ProjectVariables.CapturedPayershortList),
					Filters.in("insuranceDesc", ProjectVariables.CapturedInsuranceList));
			break;
		case "DP COUNT BASED ON MP WITH FILTERS":
			MatchFilter = Filters
					.and(Filters.eq("_id.clientKey", Long.valueOf(Serenity.sessionVariableCalled("clientkey"))),Filters.eq("opptyFinalize", null),
							Filters.eq("ruleInBaseLine", 0), Filters.eq("disposition.desc", "Present"),
							Filters.eq("subRule.hierarchy.medPolicyDesc",
									Serenity.sessionVariableCalled("Medicalpolicy")),
							Filters.in("payerShort", ProjectVariables.CapturedPayershortList),
							Filters.in("insuranceDesc", ProjectVariables.CapturedInsuranceList));
			break;

		case "RAWSAVINGS FOR DP":
			MatchFilter = Filters
					.and(Filters.eq("_id.clientKey", Long.valueOf(Serenity.sessionVariableCalled("clientkey"))),Filters.eq("opptyFinalize", null),
							//Filters.eq("_id.dataVersion",
								//	java.util.regex.Pattern.compile(ProjectVariables.DataVersion)),
							Filters.eq("ruleInBaseLine", 0), Filters.eq("disposition.desc", "Present"),
							Filters.eq("subRule.hierarchy.medPolicyDesc",
									Serenity.sessionVariableCalled("Medicalpolicy")),
							Filters.eq("subRule.hierarchy.topicDesc", Serenity.sessionVariableCalled("Topic")),
							Filters.eq("subRule.hierarchy.dpKey", ProjectVariables.CapturedDPkey));
			break;
		case "RAWSAVINGS FOR DP WITH INSURANCE":
			MatchFilter = Filters.and(
					Filters.eq("_id.clientKey", Long.valueOf(Serenity.sessionVariableCalled("clientkey"))),
					Filters.ne("_id.insuranceKey", Long.valueOf(Serenity.sessionVariableCalled("InvalidInsuranceKey"))),
					Filters.eq("_id.dataVersion", java.util.regex.Pattern.compile(ProjectVariables.DataVersion)),
					Filters.eq("ruleInBaseLine", 0), Filters.eq("disposition.desc", "Present"),
					Filters.eq("subRule.hierarchy.medPolicyDesc", Serenity.sessionVariableCalled("Medicalpolicy")),
					Filters.eq("subRule.hierarchy.topicDesc", Serenity.sessionVariableCalled("Topic")),
					Filters.eq("subRule.hierarchy.dpKey", ProjectVariables.CapturedDPkey)
					);
			//Filters.ne("annualSavings.lines", 0)
			break;
		case "ANNUAL RAW SAVINGS BASED ON CLIENT AND PAYERSHORT":
			MatchFilter = Filters
					.and(Filters.eq("_id.clientKey", Long.valueOf(Serenity.sessionVariableCalled("clientkey"))),Filters.eq("opptyFinalize", null),
							Filters.eq("subRule.hierarchy.dpKey",
									Long.valueOf(Serenity.sessionVariableCalled("DPkey"))),
							Filters.eq("_id.payerShort", Serenity.sessionVariableCalled("SelectedPayerShort")),
							Filters.eq("disposition.desc", "Present"));
			break;
		case "ANNUAL RAW SAVINGS BASED ON CLIENT AND MEDICALPOLICY":
			MatchFilter = Filters
					.and(Filters.eq("_id.clientKey", Long.valueOf(Serenity.sessionVariableCalled("clientkey"))),Filters.eq("opptyFinalize", null),
							Filters.eq("subRule.hierarchy.medPolicyDesc",
									Serenity.sessionVariableCalled("Medicalpolicy")),
							Filters.eq("_id.payerShort", Serenity.sessionVariableCalled("SelectedPayerShort")),
							Filters.eq("disposition.desc", "Present"));

			break;     
            		
		case "ANNUAL RAW SAVINGS BASED ON CLIENT AND TOPIC":
			MatchFilter = Filters
					.and(Filters.eq("_id.clientKey", Long.valueOf(Serenity.sessionVariableCalled("clientkey"))),
							Filters.eq("subRule.hierarchy.topicDesc",
									Serenity.sessionVariableCalled("Topic")),
							Filters.eq("_id.payerShort", Serenity.sessionVariableCalled("SelectedPayerShort")),
							Filters.eq("disposition.desc", "Present"));

			break;	
		case "COMPLETED ONLY":
			MatchFilter = Filters.and(
					Filters.eq("_id.clientKey", Long.valueOf(Serenity.sessionVariableCalled("clientkey"))),
					Filters.eq("ruleInBaseLine", 0), Filters.eq("disposition.desc", "Present"),Filters.eq("opptyFinalize", null),Filters.ne("presentationProfile.profileName", null));
			break;
		case "PARTIALLY ASSIGNED ONLY":
			MatchFilter = Filters.and(
					Filters.eq("_id.clientKey", Long.valueOf(Serenity.sessionVariableCalled("clientkey"))),
					Filters.eq("ruleInBaseLine", 0), Filters.eq("disposition.desc", "Present"),Filters.ne("presentationProfile.profileName", null));
			break;
		case "NOT STARTED ONLY":
			MatchFilter = Filters.and(
					Filters.eq("_id.clientKey", Long.valueOf(Serenity.sessionVariableCalled("clientkey"))),
					Filters.eq("ruleInBaseLine", 0), Filters.eq("disposition.desc", "Present"),Filters.eq("presentationProfile.profileName", null));
			break;
		case "PARTIALLY ASSIGNED ONLY WITH FILTERS":
		case "COMPLETED ONLY WITH FILTERS":
			MatchFilter = Filters.and(
					Filters.eq("_id.clientKey", Long.valueOf(Serenity.sessionVariableCalled("clientkey"))),
					Filters.eq("ruleInBaseLine", 0), Filters.eq("disposition.desc", "Present"),Filters.ne("presentationProfile.profileName", null),Filters.in("payerShort", ProjectVariables.CapturedPayershortList),Filters.in("_id.claimType", ProjectVariables.CapturedClaimtypesList),
					Filters.in("insuranceDesc", ProjectVariables.CapturedInsuranceList),Filters.eq("opptyFinalize", null));
		
			break;
		case "NOT STARTED ONLY WITH FILTERS":
			MatchFilter = Filters.and(
					Filters.eq("_id.clientKey", Long.valueOf(Serenity.sessionVariableCalled("clientkey"))),
					Filters.eq("ruleInBaseLine", 0), Filters.eq("disposition.desc", "Present"),Filters.eq("presentationProfile.profileName", null),Filters.in("payerShort", ProjectVariables.CapturedPayershortList),Filters.in("_id.claimType", ProjectVariables.CapturedClaimtypesList),
					Filters.in("insuranceDesc", ProjectVariables.CapturedInsuranceList));
			break;
		case "RAW SAVINGS BASED ON MEDICALPOLICY":
			MatchFilter = Filters
					.and(Filters.eq("_id.clientKey", Long.valueOf(Serenity.sessionVariableCalled("clientkey"))),Filters.eq("opptyFinalize", null),
							Filters.eq("subRule.hierarchy.medPolicyDesc",
									Serenity.sessionVariableCalled("Medicalpolicy")),
								Filters.eq("disposition.desc", "Present"));

			break;     
            		
		case "RAW SAVINGS BASED ON TOPIC":
			MatchFilter = Filters
					.and(Filters.eq("_id.clientKey", Long.valueOf(Serenity.sessionVariableCalled("clientkey"))),Filters.eq("opptyFinalize", null),
							Filters.eq("subRule.hierarchy.topicDesc",
									Serenity.sessionVariableCalled("Topic")),
								Filters.eq("disposition.desc", "Present"));

			break;	
		case "RAW SAVINGS BASED ON DP":
		case "RAWSAVINGS BASED ON DP":
			MatchFilter = Filters
					.and(Filters.eq("_id.clientKey", Long.valueOf(Serenity.sessionVariableCalled("clientkey"))),Filters.eq("opptyFinalize", null),
							Filters.eq("ruleInBaseLine", 0), Filters.eq("disposition.desc", "Present"),
							Filters.eq("subRule.hierarchy.dpKey", Long.valueOf(Serenity.sessionVariableCalled("DPkey"))));
			break;
		case "RAW SAVINGS BASED ON MEDICALPOLICY WITH PPS":
			MatchFilter = Filters
					.and(Filters.eq("_id.clientKey", Long.valueOf(Serenity.sessionVariableCalled("clientkey"))),
							Filters.eq("subRule.hierarchy.medPolicyDesc",
									Serenity.sessionVariableCalled("Medicalpolicy")),
								Filters.eq("disposition.desc", "Present"),Filters.in("payerShort", ProjectVariables.CapturedPayershortList),
								Filters.in("insuranceDesc", ProjectVariables.CapturedInsuranceList),Filters.in("_id.claimType", ProjectVariables.CapturedClaimtypesList));

			break;     
            		
		case "RAW SAVINGS BASED ON TOPIC WITH PPS":
			MatchFilter = Filters
					.and(Filters.eq("_id.clientKey", Long.valueOf(Serenity.sessionVariableCalled("clientkey"))),
							Filters.eq("subRule.hierarchy.topicDesc",
									Serenity.sessionVariableCalled("Topic")),
								Filters.eq("disposition.desc", "Present"),Filters.in("payerShort", ProjectVariables.CapturedPayershortList),
								Filters.in("insuranceDesc", ProjectVariables.CapturedInsuranceList),Filters.in("_id.claimType", ProjectVariables.CapturedClaimtypesList));

			break;	
		case "RAWSAVINGS BASED ON DP WITH PPS":
			MatchFilter = Filters
					.and(Filters.eq("_id.clientKey", Long.valueOf(Serenity.sessionVariableCalled("clientkey"))),
							Filters.eq("ruleInBaseLine", 0), Filters.eq("disposition.desc", "Present"),
							Filters.eq("subRule.hierarchy.dpKey", Long.valueOf(Serenity.sessionVariableCalled("DPkey"))),Filters.in("payerShort", ProjectVariables.CapturedPayershortList),
							Filters.in("insuranceDesc", ProjectVariables.CapturedInsuranceList),Filters.in("_id.claimType", ProjectVariables.CapturedClaimtypesList));
			break;
		case "DP WITH CONFIGURATION AND INFORMATIONAL":
			try {
				MatchFilter = Filters.and(Filters.in("_id.dpKey",ProjectVariables.DP), Filters.eq("_id.releaseLogKey", Long.parseLong(OracleDBUtils.db_GetFirstValueforColumn(OracleDBQueries.getOracleDBQuery("RELEASE LOG KEY"),"RELEASE_LOG_KEY"))),
						Filters.in("subRule.hierarchy.dpType",ProjectVariables.DPType),Filters.eq("releaseInfo.releaseName", OracleDBUtils.db_GetFirstValueforColumn(OracleDBQueries.getOracleDBQuery("RELEASE LOG KEY"),"RELEASE_NAME")));		
			} catch (Exception e) {
				e.printStackTrace();
			}
			break;
		case "DP WITH ALL":
			try {
				MatchFilter = Filters.and(Filters.eq("_id.releaseLogKey", Long.parseLong(OracleDBUtils.db_GetFirstValueforColumn(OracleDBQueries.getOracleDBQuery("RELEASE LOG KEY"),"RELEASE_LOG_KEY"))),
						Filters.eq("subRule.hierarchy.dpType", 3l),Filters.ne("subRule.hierarchy.midRuleKey",0l),Filters.eq("releaseInfo.releaseName", OracleDBUtils.db_GetFirstValueforColumn(OracleDBQueries.getOracleDBQuery("RELEASE LOG KEY"),"RELEASE_NAME")));
			} catch (Exception e) {
				e.printStackTrace();
			}
			break;
		case "SORT ORDER WITH 1":
			try {
				MatchFilter = Filters.and(Filters.eq("_id.releaseLogKey", Long.parseLong(OracleDBUtils.db_GetFirstValueforColumn(OracleDBQueries.getOracleDBQuery("RELEASE LOG KEY"),"RELEASE_LOG_KEY"))),
						Filters.in("subRule.hierarchy.dpType", ProjectVariables.DPType),Filters.eq("subRule.hierarchy.dpSortOrder",1l),Filters.eq("releaseInfo.releaseName",OracleDBUtils.db_GetFirstValueforColumn(OracleDBQueries.getOracleDBQuery("RELEASE LOG KEY"),"RELEASE_NAME")));
			} catch (Exception e) {
				e.printStackTrace();
			}
			break;
		case "SORT ORDER WITHOUT 1":
			try {
				MatchFilter = Filters.and(Filters.eq("_id.releaseLogKey", Long.parseLong(OracleDBUtils.db_GetFirstValueforColumn(OracleDBQueries.getOracleDBQuery("RELEASE LOG KEY"),"RELEASE_LOG_KEY"))),
						Filters.in("subRule.hierarchy.dpType", ProjectVariables.DPType),Filters.ne("subRule.hierarchy.dpSortOrder",1l),Filters.eq("releaseInfo.releaseName", OracleDBUtils.db_GetFirstValueforColumn(OracleDBQueries.getOracleDBQuery("RELEASE LOG KEY"),"RELEASE_NAME")));
			} catch (Exception e) {
				e.printStackTrace();
			}
			break;
		case "APPROVE LIB":
			MatchFilter= Filters.and(Filters.eq("_id.dpKey", Long.parseLong(ProjectVariables.getDPBasedonDecision(sInputs))),
					Filters.in("_id.insuranceKey",Arrays.asList("1l,7l".split(","))),Filters.eq("_id.payerKey", 26l),Filters.eq("_id.claimType", "O"));
			break;
		case "SUPRESS":
			MatchFilter= Filters.and(Filters.eq("_id.dpKey", Long.parseLong(ProjectVariables.getDPBasedonDecision(sInputs))),
					Filters.in("_id.insuranceKey",Arrays.asList("1l,2l,7l".split(","))),Filters.eq("_id.payerKey", 26l),Filters.eq("_id.claimType", "A"));
		case "SUPRESS AND NO DECISION":
			MatchFilter= Filters.and(Filters.eq("_id.dpKey", Long.parseLong(ProjectVariables.getDPBasedonDecision(sInputs))),
					Filters.in("_id.insuranceKey",1l),Filters.eq("_id.payerKey", 26l),Filters.in("_id.claimType", Arrays.asList("A,O".split(","))));
			break;
		case "APPROVE LIB AND NO DECISION":
			MatchFilter= Filters.and(Filters.eq("_id.dpKey", Long.parseLong(ProjectVariables.getDPBasedonDecision(sInputs))),
					Filters.in("_id.insuranceKey",1l),Filters.eq("_id.payerKey", 26l),Filters.in("_id.claimType", Arrays.asList("A,O".split(","))));
			break;
		case "DELETE PRESENTATION":
			MatchFilter = Filters.and(Filters.eq("presentationProfiles.profileName", Serenity.sessionVariableCalled("sPPName").toString()));
			break;
		case "ALL":
			MatchFilter = Filters.and(Filters.eq("_id.clientKey", Long.valueOf(Serenity.sessionVariableCalled("clientkey"))),
					Filters.eq("presentationProfile.profileName", null));
			break;
		case "NEW MP":
			MatchFilter = Filters.and(Filters.eq("policyChange.status", "NEW"),Filters.eq("_id.clientKey", Long.valueOf(Serenity.sessionVariableCalled("clientkey"))),
					Filters.eq("presentationProfile.profileName", null));
			break;
		case "NEW TOPIC":
			MatchFilter = Filters.and(Filters.eq("topicChange.status", "NEW"),Filters.eq("_id.clientKey", Long.valueOf(Serenity.sessionVariableCalled("clientkey"))),
					Filters.eq("presentationProfile.profileName", null));
			break;
		case "RECAT TOPIC":
			MatchFilter = Filters.and(Filters.exists("topicChange.recategorized", true),Filters.eq("_id.clientKey", Long.valueOf(Serenity.sessionVariableCalled("clientkey"))),
					Filters.eq("presentationProfile.profileName", null));
			break;
		case "NEW DP":
			MatchFilter = Filters.and(Filters.eq("dpChange.status", "NEW"),Filters.eq("_id.clientKey", Long.valueOf(Serenity.sessionVariableCalled("clientkey"))),
					Filters.eq("presentationProfile.profileName", null));
			break;
		case "RECAT DP":
			MatchFilter = Filters.and(Filters.exists("dpChange.recategorized", true),Filters.eq("_id.clientKey", Long.valueOf(Serenity.sessionVariableCalled("clientkey"))),
					Filters.eq("presentationProfile.profileName", null));
			break;
		case "RECATEGORIZED RULE":
			MatchFilter = Filters.and(Filters.exists("ruleChange.recategorized", true),Filters.eq("_id.clientKey", Long.valueOf(Serenity.sessionVariableCalled("clientkey"))),
					Filters.eq("presentationProfile.profileName", null));
			break;
		case "DEACT DP":
		case "CHANGE DP DESC":
		case "CLAIM TYPE ADDED":
		case "CLAIM TYPE REMOVED":
			MatchFilter = Filters.and(Filters.eq("ellChange", java.util.regex.Pattern.compile(sInputs.toUpperCase())),Filters.eq("_id.clientKey", Long.valueOf(Serenity.sessionVariableCalled("clientkey"))),
					Filters.eq("presentationProfile.profileName", null));
			break;
		case "NEW RULE":
			MatchFilter = Filters.and(Filters.eq("ruleChange.status", "NEW"),Filters.eq("_id.clientKey", Long.valueOf(Serenity.sessionVariableCalled("clientkey"))),
					Filters.eq("presentationProfile.profileName", null));
			break;
		case "DEACTIVATED POLICY":
			MatchFilter = Filters.and(Filters.eq("ruleChange.status", "DEACTIVATED"),Filters.eq("_id.clientKey", Long.valueOf(Serenity.sessionVariableCalled("clientkey"))),
					Filters.eq("presentationProfile.profileName", null));
			break;
		case "VERSION CHANGE":
			MatchFilter = Filters.and(Filters.exists("ruleChange.version", true),Filters.eq("_id.clientKey", Long.valueOf(Serenity.sessionVariableCalled("clientkey"))),
					Filters.eq("presentationProfile.profileName", null));
			break;
		case "REFERENCE CHANGE":
			MatchFilter = Filters.and(Filters.exists("ruleChange.reference", true),Filters.eq("_id.clientKey", Long.valueOf(Serenity.sessionVariableCalled("clientkey"))),
					Filters.eq("presentationProfile.profileName", null));
			break;
		default:
			Assert.assertTrue("Given selection was not found ==>" + sInputs, false);
			break;
		}

		System.out.println(MatchFilter);
		return MatchFilter;
	}

	// ===============================================================================================================================================>
	
	public static Bson getDPType(String sInput) {

		Bson MatchFilter = new BsonDocument();
		MatchFilter = Filters.and(Filters.eq("_id.dpKey", Long.parseLong(sInput)), Filters.eq("subRule.hierarchy.dpTypeDesc", "Information Only"));				
		return MatchFilter;
	}

}


