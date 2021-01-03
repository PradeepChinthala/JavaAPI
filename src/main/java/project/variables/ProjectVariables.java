package project.variables;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

import org.bson.Document;
import org.bson.conversions.Bson;
import org.json.simple.JSONObject;
import static com.mongodb.client.model.Filters.eq;

import com.mongodb.BasicDBObject;
import com.mongodb.MongoClient;
import com.mongodb.client.DistinctIterable;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;

public class ProjectVariables {

	public static final int WEBELEMENTS_TIME_IN_SECONDS = 20;
	
	public static int APP_RENDER_WAIT_TIME = 20;
	
	public static final int TImeout_1_Seconds = 1;
	
	
	public static final int TImeout_2_Seconds = 2;
	
	public static final int TImeout_5_Seconds = 5;
	
	public static final int TImeout_6_Seconds = 6;
	
	public static final int TImeout_8_Seconds = 8;
	
	public static final int TImeout_10_Seconds = 10;
	
	public static final int TImeout_12_Seconds = 12;
	
	public static final int TImeout_15_Seconds = 15;
	
	public static final int TImeout_20_Seconds = 20;
	
	public static final int TImeout_25_Seconds = 25;
	
	public static final int TImeout_30_Seconds = 30;
	
	public static final int TImeout_40_Seconds = 40;

	public static final int TImeout_1_hour = 3600;
	
	public static final int HIGHLIGHT_COUNT = 5;

	public static final int SCROLL_BY_OFFSET = 100;
	
	public static final int SCROLL_BY_SMALL_OFFSET = 10;
	
	public static final int SCROLL_BY_HORIZONTAL_OFFSET = 1000;
	
	public static int High_MIN_SLEEP = 1000;

	public static final long Page_Load_Timeout = 50;
	
	public static final int ExplicitWait_PollTime = 500;
	
	public static long CapturedDPkey;
	
	//public static final String USER_NAME = "iht_ittest09";
	
//	public static final String USER_NAME = "ulanka";	
//	public static final String PASSWORD = "TW90b0AxMjM=";

	public static final String USER_NAME = "iht_ittest09";	
	public static final String PASSWORD = "SWhlYWx0aDEyMw==";
	public static String sTestUsers = "src//test//resources//TestUsers.properties";
	
	public static String  sDirectory = System.getProperty("user.dir");
	
	public static String parentWindow =null;
	
	public static String childWindow = null;

	public static String sSystemsUserName = System.getProperty("user.name");

	public static String MP_Labels="Medical Policy,DP Dispositions Captured,Raw Production Savings,Filtered Raw Opportunity Savings,Filtered Aggressive Opportunity Savings";

	public static String sTestDataFilePath = "C:\\Users\\" + sSystemsUserName + "\\Downloads\\";

	public static String Filter_Savings_Xpath_Args="opp-raw-prod-sav,opp-raw-opp-sav,opp-agg-opp-sav";

	public static String InsuranceTypes[] = {"Medicare","Medicaid","Commercial","Dual Eligible","BlueCard","Federal Employee Program"};

	public static String ClientDecesionValues[] = {"Absence of Decision","Approve Library","Approve with Modification","No Decision","None","Reject","Suppress"};
	
	public static String CurrentDispositionValues[] = {"Present","Do Not Present - CPM Review","Do Not Present","Not Reviewed","Prior Approval"};
	
	public static String PriorDispositionValues[] = {"Present","Do Not Present - CPM Review","Do Not Present","No Disposition"};

	public static String[] SavingsStatuFilteOptionsValues={"Opportunity","Production"};
	
	public static String sheet1="Present and DNP-CPM";
	
	public static String sheet2="DNP-CPM";
	
	public static String sheet3="Prior Approvals";

	public static String AWBFilterOptions="Latest Client Decision;Prior Disposition;Savings Status";

	public static String AWBWorkedOpportunityFilterOptions="Payer Short;Insurance;Product;Latest Client Decision;Current Disposition";


	public static int TimerCount=60;

	public static String filters="Flag,Prior Disposition,Savings Status";
	
	public static String FlagFilterOptions="All References Sources for Rule are Expert Panel,Bilateral Type Not For My Payer,Clinical Appropriateness,CMS+ Cotiviti DP,Companion,Counterpart,Cross Product across products for Cross Product Payer,Cross Product across products for Non-Cross Product Payer,Cross Product different products for Cross Product Payer,Cross Product different products for Non-Cross Product Payer,Disproportionate Savings Distribution,DP in Open Presentation,Expert Panel Ref - Primary with Other Ref Sources,Expert Panel Ref - Secondary or in Ref Details	,Hard Denial Rule for Hard Denial Payer,LCD Rule,Mother/Baby Rule for Mother/Baby Payer,Mutually Exclusive,OOS,Payer has DPs in this Topic,Payer has rules in this DP,Payer Sensitivity,Prior Auth DP,Quirky Diag Rule for Quirky Diag Payer,Real Time Payer - Mid Rule Not Real Time,Recode Logic for Deny versus Recode Payer,Rule in Specialty MP with Specialty Society Reference,Rule with no Conservative Savings defined,Test Switch for my Payer Policy Set for Mid Rule,Test Switch for RVA PPS";

	public static String LatestClientDecisionFilterOptions="Absence of Decision,Approve Library,Approve with Modification,No Decision,None,Reject,Suppress";
	
	public static String PriorDispositionFilterOptions="Present,Do Not Present - CPM Review,Do Not Present,No Disposition";
	
	public static String SavingStatusFilterOptions="Opportunity,Production";
	
	public static String InsuranceFilterOptions="Medicare,Medicaid,Commercial,Dual Eligible,BlueCard,Federal Employee Program";
	
	public static String ProductFilterOptions="ICM,A,F,P,ICMO,I,O,S";
	
	public static String FiltersData="Approve Library,Present,Opportunity";

	public static String Year;

	public static String month;

	public static List<JSONObject> execResultList;

	public static int ElementVisibleTimeout = 10;
	
	public static String filePathforCookie = "H:\\cookie\\Cookies.data";
	
	public static final String ROOT_URI = "http://10.170.32.20:10036";
	public static final String CLIENT_TEAM_DATA_ENDPOINT ="/micro/teammanagement/1/clients";

	public static final int MIN_COUNT = 10;

	public static final long TImeout_3_Seconds = 3;

	public static long CapturedPayerkey;

	
	public static String uniquePresentationProfile = "";
	
	public static List<String> DB_Medicalpolicylist=new ArrayList<>();
	//**  MongoDB variables
	public static String CPW_QA_URL="https://cpd-qa1.cotiviti.com/index3.html";
	
	public static String CPW_QA_AngularJSURL="https://cpw-bfqa.cotiviti.com/";
	
	public static int MONGO_SERVER_PORT = 15;
	
	public static String MONGODB_USER = "cotiviti-readonly";
	
	public static String MONGODB_PWD = "UQgMW8LP";
	
	public static String MONGODB = "admin";
	
	public static String MONGO_SERVER_URL = "pl-0-us-east-1-6oxiw.mongodb.net";
	
	public static HashSet<String> DB_Nodisposition_insuranceList = new HashSet<String>();
	public static HashSet<String> DB_Nodisposition_claimtypeList = new HashSet<String>();
	public static HashSet<String> DB_Nodisposition_subRuleList = new HashSet<String>();
	public static HashSet<Long> DB_Nodisposition_DpkeyList = new HashSet<Long>();
	public static HashSet<String> DB_insuranceList = new HashSet<String>();
	public static HashSet<String> DB_claimtypeList = new HashSet<String>();
	public static HashSet<String> DB_subRuleList = new HashSet<String>();
	public static HashSet<Long> DB_DpkeyList = new HashSet<Long>();
	public static HashSet<String> DB_PayershortList = new HashSet<String>();
	
	public static Bson Payerorquery = new BasicDBObject();
	public static Bson Insuranceorquery = new BasicDBObject();
	public static Bson Claimtypeorquery = new BasicDBObject();
	public static String DispositionNotes="Testing by chaitanya";
	public static Bson LatestCLientDecisionOrquery = new BasicDBObject();
	public static Bson PriorDispositionOrquery = new BasicDBObject();
	public static Bson CurrentDispositionOrquery = new BasicDBObject();
	
	public static String Disposition;

	public static String DispositionReasons;
	
	public static String Priority="High";
	public static ArrayList<String> sGetPayershorts=new ArrayList<>();
	public static ArrayList<String> sGetDBList = new ArrayList<>();
	 
     public static String ResponseOutput;
     public static ArrayList<String> clientNamesList=new ArrayList<>();
     public static String userData ="{\"userName\" : \"iht_ittest09\"}\r\n";
	
	public static HashMap<String,HashMap<String,String>> sServices= new HashMap<String,HashMap<String,String>>();
	
	public static ArrayList<String> CapturedPayershortList=new ArrayList<>();
	public static ArrayList<String> CapturedDPkeyList = new ArrayList<>();
	public static ArrayList<String> CapturedClaimtypesList=new ArrayList<>();
	public static ArrayList<String> CapturedInsuranceList=new ArrayList<>();
	public static ArrayList<String> CapturedPayerLOBList=new ArrayList<>();
	public static ArrayList<String> PPSList=new ArrayList<>();
	public static ArrayList<String> PPSKeyList=new ArrayList<>();
	
    
	public static String[] sNotificationDataVersions={"PMPRD1_20191120_191234","PMPRD1_20190821_215306","PMPRD1_20191023_185426","PMPRD1_20190925_190018"};
	
	public static String RWO_filters="Payer Short,Insurance,Product,Latest Client Decision,Current Disposition";

	public static String CaptureDispositionServiceUrl="cpd-opportunities/v1/capturedisposition";
	
	public static ArrayList<String> clientKeysList=new ArrayList<>();

	public static String StaticOrderofPriorityReasons="CR,BR,HS";
	
	public static ArrayList<Long> Clientkeylist=new ArrayList<>();
	
	public static String DB_USERNAME = "USER_MASTER_SELECT";
	public static String DB_PASSWORD = "USER_MASTER_SELECT";
	public static String DB_CONNECTION_URL = "jdbc:Oracle:thin:@vpmtst1.ihtech.com:1521/VPMTST1.iht.com";
	public static String DB_CONNECTION_URL_VPMUAT1 = "jdbc:Oracle:thin:@usadelphix03.ihtech.com:1521/VPMUAT1.iht.com";
	public static String DB_CONNECTION_URL_PMPRD2 = "jdbc:Oracle:thin:@pmprd2.ihtech.com:1521/PMPRD2.iht.com";
	public static String CSI_URL = "https://qaportal.ihealthtechnologies.com/CustomerServiceInterface/";
	public static String DB_CONNECTION_URL1 = "jdbc:Oracle:thin:@host:1521/database.iht.com";
	public static String DB_CONNECTION_RVAPRD1 = "jdbc:Oracle:thin:@usdprvaprd01:1521/RVAPRD1.iht.com";
	
	public static String DB_DRIVER_NAME = "oracle.jdbc.OracleDriver";


	public static String DataVersion;
	
	public static ArrayList<String> CreatedPresentationList=new ArrayList<>();
	
	public static String StaticInsurnaces="1,2,3,7,8,9";



    public static String DOSFROM = "11/11/2000";
    public static String DOSTO =  "12/12/2021";
    public static String PROCESSIONINGFROM = "11/11/2000";
    public static String PROCESSIONINGTO = "12/12/2021";

    public static String ShowDropdownValues="All,Not Started Only,Partially Assigned Only,Completed Only";
    public static String nkumarPassword="Q2F0MTAwNUA=";
    public static String ulankaPassword="TGVub3ZvMTIj";


	public static String RejectReasons="Provider contracts or network issues";
	
	public static String ModifiedRejectReasons="Payment Methodology conflict";
	
	public static String RejectReasonsList="Provider contracts or network issues;Payment Methodology conflict;Client internal policy;Client system limitations;Pre-auth process;Vendor management/vendor carve out (i.e. another vendor manages D&B or radiology, etc);State regulations/Line of Business Conflict;Plan already accepted an alternative Cotiviti policy (e.g. CMS vs CMS + Cotiviti Bilateral Rule Sets);Policy Reference Issues;Client Sensitivity/risk of provider noise;Little/No Savings;Clinically Disagree with Policy;Member Liability Issues";

	public static String DecisionCapturePopup_ReasonDropdownValues = "Provider contracts or network issues;Payment Methodology conflict;Client internal policy;Client system limitations;Pre-auth process;Vendor management/vendor carve out (i.e. another vendor manages D&B or radiology, etc);State regulations/Line of Business Conflict;Plan already accepted an alternative Cotiviti policy (e.g. CMS vs CMS + Cotiviti Bilateral Rule Sets);Policy Reference Issues;Client Sensitivity/risk of provider noise;Little/No Savings;Clinically Disagree with Policy;Member Liability Issues";
	
	public static ArrayList<String> ProfilewithCreatedTime=new ArrayList<>();
	
	public static MongoClient mongoClient;
	
	public static MongoDatabase db;
	public static MongoCollection<Document> mColl;
	public static FindIterable<Document> results;
	public static MongoCursor<Document> cursor;
	public static long recordsCount;
	public static List<Document> resultsList = new ArrayList<Document>();
	public static DistinctIterable<String> Distinctresults;
	public static DistinctIterable<Double> DistinctresultsDouble;
	public static DistinctIterable<Long> Distinctresults_with_long;

	public static ArrayList<String> DBTopicslist=new ArrayList<>();
	
	public static ArrayList<Long> PayerKeysList=new ArrayList<>();
	public static ArrayList<String> unAvailableMidRuleKeys=new ArrayList<String>();

	public static ArrayList<String> DuplicatePPS=new ArrayList<>();

	public static String DPTypeOptions = "Rules,Information Only,Configuration Only";

	public static ArrayList<Long> DP= new ArrayList<Long>(Arrays.asList(1184l,1185l,1186l,1192l,1194l,3120l,3121l,3122l,3123l,3124l,3125l,3127l,4456l,4471l,4472l,4474l,4476l,4477l,4478l,4602l,4646l,4724l,4725l,4982l,4983l,4984l,4985l,4986l,4987l,4988l,4989l,4998l,4999l,5000l,5001l,5002l,5003l,5004l,5005l,5006l,5007l,5008l,5009l,5012l,5438l,5478l,5507l,5552l,5578l,5599l,5600l,5601l,5606l,5723l,5754l,5755l,5761l,5769l,5778l,5779l,5780l,6104l,6805l,7230l,7987l,8659l,11559l));
	
	public static ArrayList<Long> DPType=new ArrayList<Long>(Arrays.asList(2l,3l));

	public static String MockPresentation = "Test Cnf Information";

	public static String InformationMockDP = "DP 5478";
	
	public static String ConfigurationMockDP = "DP 1194,DP 4724,DP 4725";

	public static String DecisionGridHeader = ",DP,Decision on Source,Decision on Target,Comments,Notification";

	public static String getDPBasedonDecision(String sDecision){
		HashMap<String,String> DP =new HashMap<String,String>();
		DP.put("APPROVE LIB", "5780");
		DP.put("SUPRESS", "5780");
		DP.put("SUPRESS AND NO DECISION","5761");
		DP.put("APPROVE LIB AND NO DECISION", "5779");
		return DP.get(sDecision);		
	}
	
	public static String changeOpportunitiesCol[]={"DP","DP Status/Change","DP Recategorized","Topic (Changed)","Description","Claim Type","Reference","Mid-Rules","Added","Recategorized","Versioned","Deactivated","Previous Decision"};
	public static String ChangePresAlert="This presentation name has been used, please enter a unique presentation name";
	// 
	public static String cancelPresAlert="Are you sure you want to cancel? Changes will not be saved";
	public static HashSet<String> UIDPKeylist = new HashSet<>();
	
	public static List<String> lCDlist = new ArrayList<String>();
	public static List<String> ClaimtypeList = new ArrayList<>();
	public static List<String> insuranceList=new ArrayList<>();
	public static List<Long> insuranceKeyslist = new ArrayList<>();
	public static ArrayList<Long> payerKeys=new ArrayList<>();
	public static List<String> payerShortList=new ArrayList<>();
	public static HashSet<String> Medicalpolicy_PolicySelectiondrawer=new HashSet();
	public static HashSet<String> DB_MPlist=new HashSet<>();
	public static String eLLtabcolnames="DP,Flags,Status,Latest Client Decision,Prior Disposition";
	public static String RVAtabcolnames="DP,Flag,Raw Savings,Aggressive Savings,Edits,Savings Status,Latest Client Decision,Prior Disposition";
	public static HashMap<Integer, HashMap<String, String>> sSubsequentEllData = new HashMap<Integer,HashMap<String,String>>();
	
	public static String elluser="ell";
	public static String ellpassword="ell";
	
	public static String OpportunityLCDFilterOptions="Absence of Decision,No Decision,Reject,Suppress";


}

