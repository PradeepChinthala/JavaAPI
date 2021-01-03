package project.utilities;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.junit.Assert;

import net.serenitybdd.core.Serenity;
import net.serenitybdd.core.environment.EnvironmentSpecificConfiguration;
import net.thucydides.core.util.EnvironmentVariables;
import net.thucydides.core.util.SystemEnvironmentVariables;
import project.variables.ProjectVariables;

public  class OracleDBUtils {	

	public static void environmentSpecificConnection() {
		EnvironmentVariables environmentVariables = SystemEnvironmentVariables.createEnvironmentVariables(); 
		ProjectVariables.DB_CONNECTION_URL = EnvironmentSpecificConfiguration.from(environmentVariables).getProperty("DB_CONNECTION_URL");
		System.out.println("DBConnectionUrl::"+ProjectVariables.DB_CONNECTION_URL);		
	}
	
	public static Connection getDBConnection(String DBToConnect)
	{
		
		Connection con=null;
		try {
						Class.forName(ProjectVariables.DB_DRIVER_NAME);						
						switch(DBToConnect.toUpperCase())
						{
						
						   case  "PMPRD2":							
							   con = DriverManager.getConnection(ProjectVariables.DB_CONNECTION_URL_PMPRD2,ProjectVariables.DB_USERNAME,ProjectVariables.DB_PASSWORD);						
							   break;  	
						   case  "VPMTST1":      
							   System.out.println(ProjectVariables.DB_CONNECTION_URL);
                               con = DriverManager.getConnection(ProjectVariables.DB_CONNECTION_URL,ProjectVariables.DB_USERNAME,ProjectVariables.DB_PASSWORD);                       
                               break;
                               default:
                            	   Assert.assertTrue("Case not found==>"+DBToConnect,false);
                            	   break;
						 
						}
	}
	catch (SQLException e) {
		 System.out.println("SQL Exception occurred::"+e.getLocalizedMessage());		 
	}		
		
		catch (ClassNotFoundException e) {
			 System.out.println("ClassNotFoundException occurred::"+e.getLocalizedMessage());		 
		}		
		
		if(con!=null)
		 {
			 System.out.println("Connection success to the DB::"+DBToConnect);				 
		 }
		
		return con;
		
	}
	
	
	
	public static String executeSQLQuery(String sqlQuery)
	{
		String queryResultValue= "";
		Connection con=null;

		try {
			Class.forName(ProjectVariables.DB_DRIVER_NAME);			
			con = DriverManager.getConnection(ProjectVariables.DB_CONNECTION_URL_PMPRD2,ProjectVariables.DB_USERNAME,ProjectVariables.DB_PASSWORD);

			if(con!=null) {
				System.out.println("Connected to the Database...");
			}else {
				System.out.println("Database connection failed ");
			}

			Statement st = con.createStatement();
			st.setQueryTimeout(30);
			ResultSet rs =st.executeQuery(sqlQuery);        	

			while(rs.next())
			{
				queryResultValue = rs.getString(1).toString();   
				break;
			} 

			System.out.println("DB Result: "+queryResultValue);

			con.close();

		}catch (SQLException e) {
			e.printStackTrace();}

		catch (NullPointerException err) {
			System.out.println("No Records obtained for this specific query");
			err.getMessage();              }        

		catch (ClassNotFoundException e1) {     		  		
			e1.getMessage();	}

		finally{
			try {
				if(con != null)
					con.close();

			}catch(SQLException e)  {           
				e.getMessage();         
			} 
		}

		return queryResultValue;

	}   
	
	public static String executeUpdateSQLQuery(String sqlQuery)
	{
		String queryResultValue= "";
		Connection con=null;

		try {
			Class.forName(ProjectVariables.DB_DRIVER_NAME);			
			con = DriverManager.getConnection(ProjectVariables.DB_CONNECTION_URL,ProjectVariables.elluser,ProjectVariables.ellpassword);

			if(con!=null) {
				System.out.println("Connected to the Database...");
			}else {
				System.out.println("Database connection failed ");
			}

			Statement st = con.createStatement();
			st.setQueryTimeout(30);
			int rcount  = st.executeUpdate(sqlQuery);        	

			if (rcount > 0){
				System.out.println("Number of records updated: "+rcount);				
			}else{
				Assert.assertTrue("No Records updated: "+rcount, false);
			}

			System.out.println("DB Result: "+rcount);
			
			con.commit();
			
			con.close();

		}catch (SQLException e) {
			e.printStackTrace();}

		catch (NullPointerException err) {
			System.out.println("No Records obtained for this specific query");
			err.getMessage();              }        

		catch (ClassNotFoundException e1) {     		  		
			e1.getMessage();	}

		finally{
			try {
				if(con != null)
					con.close();

			}catch(SQLException e)  {           
				e.getMessage();         
			} 
		}

		return queryResultValue;

	}  
	// ####################################################################################################

	public static ArrayList<String> db_GetAllFirstColumnValues(String sqlQuery){

		Connection con=null;
		String result;
		ArrayList<String> resultList = new ArrayList<String>();

		try {

			Class.forName(ProjectVariables.DB_DRIVER_NAME);
			con = DriverManager.getConnection(ProjectVariables.DB_CONNECTION_URL,ProjectVariables.DB_USERNAME,ProjectVariables.DB_PASSWORD);

			if(con!=null) {
				System.out.println("Connected to the Database...");
			}else {
				System.out.println("Database connection failed ");
			}  

			Statement st = con.createStatement();	        		
			ResultSet rs =st.executeQuery(sqlQuery);  

			while (rs.next()) {
				result = rs.getString(1).toString();
				resultList.add(result);
			}

			con.close();

		}catch (SQLException e) {
			e.printStackTrace();}

		catch (NullPointerException err) {
			System.out.println("No Records obtained for this specific query");
			err.getMessage();              }        

		catch (ClassNotFoundException e1) {     		  		
			e1.getMessage();	}

		finally{
			try {
				if(con != null)
					con.close();

			}catch(SQLException e)  {           
				e.getMessage();         
			} 
		}

		return resultList;

	}   

	//####################################################################################################

	public static ArrayList<String> db_GetEllDatafromOracle(String sqlQuery) {

		Connection con=null;
		ArrayList<String> resultList = new ArrayList<String>();
		String result;
		ArrayList<Object> AllMidRules = new ArrayList<Object>();

		try {

			Class.forName(ProjectVariables.DB_DRIVER_NAME);
			con = DriverManager.getConnection(ProjectVariables.DB_CONNECTION_URL,
					ProjectVariables.DB_USERNAME, ProjectVariables.DB_PASSWORD);			
		

			if (con != null) {
				System.out.println("Connected to the Database...");
			} else {
				System.out.println("Database connection failed ");
			}

			//Statement st = con.createStatement();
			Statement st = con.prepareStatement(sqlQuery.trim());
			st.setQueryTimeout(1000);
			ResultSet rs = st.executeQuery(sqlQuery);
			ResultSetMetaData rsmd = rs.getMetaData();

			int iColCount = rsmd.getColumnCount();
			System.out.println("Column"+iColCount);

			while (rs.next()) {

				String sMidRule;
				String sTopicKey;
				String sTopicDesc;
				String sPrevLongTopicDesc;
				String sCurrentLongTopicDesc;
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

				try{
					sMidRule = rs.getString("MID_RULE_MOVE_TO").toString();	
				}catch (Exception e){
					sMidRule = null;
				}

				try{
					sPreviousTopicKey = rs.getString("PREV_TOPIC").toString();
				}catch (Exception e){
					sPreviousTopicKey = null;
				}

				try{
					sTopicKey = rs.getString("CUR_TOPIC").toString();
				}catch (Exception e){
					sTopicKey = null;
				}

				try{
					sPrevTopicDesc = rs.getString("PREV_TOPIC_TITLE").toString();					
				}catch (Exception e){
					sPrevTopicDesc = null;
				}

				try{
					sTopicDesc = rs.getString("CUR_TOPIC_TITLE").toString();
				}catch (Exception e){
					sTopicDesc = null;
				}
				
				try{
					sPrevLongTopicDesc = rs.getString("PREV_TOPIC_LONG_DESC").toString();
					sPrevLongTopicDesc = sPrevLongTopicDesc.replaceAll("[\\n\\t ]", "");
				}catch (Exception e){
					sPrevLongTopicDesc = null;
				}

				try{
					sCurrentLongTopicDesc = rs.getString("CUR_TOPIC_LONG_DESC").toString();
					sCurrentLongTopicDesc = sCurrentLongTopicDesc.replaceAll("[\\n\\t ]", "");
				}catch (Exception e){
					sCurrentLongTopicDesc = null;
				}
				
				try{
					sPrevMPKey = rs.getString("PREV_MP").toString();
				}catch (Exception e){
					sPrevMPKey = null;
				}
				
				try{
					sMPKey = rs.getString("CUR_MP").toString();
				}catch (Exception e){
					sMPKey = null;
				}

				try{
					sPrevMPDesc = rs.getString("PREV_MP_TITLE").toString();
				}catch (Exception e){
					sPrevMPDesc = null;
				}
				
				try{
					sMPDesc = rs.getString("CUR_MP_TITLE").toString();
				}catch (Exception e){
					sMPDesc = null;
				}
				
				try{
					sPrevMPSORTORDER = rs.getString("PREV_MP_SORT_ORDER").toString();
				}catch (Exception e){
					sPrevMPSORTORDER = null;
				}
				
				try{
					sMPSORTORDER = rs.getString("CUR_MP_SORT_ORDER").toString();
				}catch (Exception e){
					sMPSORTORDER = null;
				}
				
				try{
					sPrevDPKEY = rs.getString("PREV_DP").toString();
				}catch (Exception e){
					sPrevDPKEY = null;
				}
				
				try{
					sDPKEY = rs.getString("CUR_DP").toString();
				}catch (Exception e){
					sDPKEY = null;
				}
				
				try{
					sPrevDPDESC = rs.getString("PREV_DP_DESC").toString();
				}catch (Exception e){
					sPrevDPDESC = null;
				}
				
				try{
					sDPDESC = rs.getString("CUR_DP_DESC").toString();
				}catch (Exception e){
					sDPDESC = null;
				}
				
				try{
					sPrevDPSORTORDER = rs.getString("PREV_DP_SORT_ORDER").toString();
				}catch (Exception e){
					sPrevDPSORTORDER = null;
				}
				
				try{
					sDPSORTORDER = rs.getString("CUR_DP_SORT_ORDER").toString();
				}catch (Exception e){
					sDPSORTORDER = null;
				}
				
				try{
					sPrevREFDESC = rs.getString("PREV_REF_SOURCE_DESC").toString();
				}catch (Exception e){
					sPrevREFDESC = null;
				}
				
				try{
					sREFDESC = rs.getString("CUR_REF_SOURCE_DESC").toString();
				}catch (Exception e){
					sREFDESC = null;
				}

				result = "MID RULE:"+sMidRule+";Previous Topic Key:"+sPreviousTopicKey+";TOPIC KEY:"+sTopicKey+";Previous Topic Desc:"+sPrevTopicDesc+";TOPIC DESC:"+sTopicDesc+
						 ";Previous TP Long Desc:"+sPrevLongTopicDesc+";TP Long Desc:"+sCurrentLongTopicDesc+
						 ";Previous MP Key:"+sPrevMPKey+";MP KEY:"+sMPKey+";Previous MP Desc:"+sPrevMPDesc+";MP DESC:"+sMPDesc+";Previous MP Sort Order"+sPrevMPSORTORDER+";MP SORTORDER:"+sMPSORTORDER+
						 ";Previous DP Key"+sPrevDPKEY+";DP KEY:"+sDPKEY+";Previous DP Desc:"+sPrevDPDESC+";DP DESC:"+sDPDESC+";Previous DP SortOrder:"+sPrevDPSORTORDER+";DP SORTORDER:"+sDPSORTORDER+
						 ";Previous Ref Key:"+sPrevREFDESC+";REF KEY:"+sREFDESC;
				System.out.println("Column data "+result);
				resultList.add(result);		
				AllMidRules.add(Long.valueOf(sMidRule));
				Serenity.setSessionVariable("MidRules").to(AllMidRules);
			}
			con.close();

		} catch (SQLException e) {
			e.getMessage();
		}

		catch (NullPointerException err) {
			System.out.println("No Records obtained for this specific query");
			err.getMessage();
		}

		catch (ClassNotFoundException e1) {
			e1.getMessage();
		}

		finally{
			try {
				if(con != null)
					con.close();

			}catch(SQLException e)  {           
				e.getMessage();         
			} 
		}

		return resultList;

	}

	
	public static ArrayList<String> db_DPDatafromOracle(String sqlQuery) {

		Connection con=null;
		ArrayList<String> resultList = new ArrayList<String>();
		String result;
		ArrayList<Object> AllMidRules = new ArrayList<Object>();

		try {

			Class.forName(ProjectVariables.DB_DRIVER_NAME);
			con = DriverManager.getConnection(ProjectVariables.DB_CONNECTION_URL,
					ProjectVariables.DB_USERNAME, ProjectVariables.DB_PASSWORD);			
		

			if (con != null) {
				System.out.println("Connected to the Database...");
			} else {
				System.out.println("Database connection failed ");
			}

			//Statement st = con.createStatement();
			Statement st = con.prepareStatement(sqlQuery.trim());
			st.setQueryTimeout(1000);
			ResultSet rs = st.executeQuery(sqlQuery);
			ResultSetMetaData rsmd = rs.getMetaData();

			int iColCount = rsmd.getColumnCount();
			System.out.println("Column"+iColCount);

			while (rs.next()) {

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

				try{
					sMPKey = rs.getString("MED_POL_KEY").toString().trim();
				}catch (Exception e){
					sMPKey = null;
				}

				try{
					sMPTitle = rs.getString("MED_POL_TITLE").toString().trim();
				}catch (Exception e){
					sMPTitle = null;
				}

				try{
					sTopicKey = rs.getString("TOPIC_KEY").toString().trim();					
				}catch (Exception e){
					sTopicKey = null;
				}

				try{
					sTopicTitle = rs.getString("TOPIC_TITLE").toString().trim();
				}catch (Exception e){
					sTopicTitle = null;
				}
				
				try{
					sTOPICDesc = rs.getString("TOPIC_DESC").toString().trim();
					sTOPICDesc = sTOPICDesc.replaceAll("[\\\r\\\n]+", "");
					sTOPICDesc = sTOPICDesc.replaceAll("[\\n\\t ]", "");
				}catch (Exception e){
					sTOPICDesc = null;
				}

				try{
					sDPKey = rs.getString("DP_KEY").toString().trim();
				}catch (Exception e){
					sDPKey = null;
				}
				
				try{
					sDPDesc = rs.getString("DP_DESC").toString().trim();
					sDPDesc = sDPDesc.replaceAll("[\\n\\t ]", "");
				}catch (Exception e){
					sDPDesc = null;
				}
				
				try{
					sSORTORDER = rs.getString("SORT_ORDER").toString().trim();
				}catch (Exception e){
					sSORTORDER = null;
				}
				
				try{
					sMidRule = rs.getString("MID_RULE_KEY").toString().trim();
				}catch (Exception e){
					sMidRule = null;
				}

				
				try{
					sSubRule = rs.getString("SUB_RULE_KEY").toString().trim();
				}catch (Exception e){
					sSubRule = null;
				}
				


				result = "MID RULE:"+sMidRule+";MP KEY:"+sMPKey+";MP Title:"+sMPTitle+";TOPIC KEY:"+sTopicKey+";Topic Title:"+sTopicTitle+";TOPIC DESC:"+sTOPICDesc+
						 ";DP KEY:"+sDPKey+";DP DESC:"+sDPDesc+";SORTORDER:"+sSORTORDER+
						 ";Sub Rule Key:"+sSubRule;
				
				//System.out.println("Oracle Column data "+result);
				/*if (sMidRule.equalsIgnoreCase("19703")){
				resultList.add(result);		
				}*/
				
				
				resultList.add(result);		
					
				AllMidRules.add(Long.valueOf(sMidRule));
				Serenity.setSessionVariable("MidRules").to(AllMidRules);
			}
			con.close();

		} catch (SQLException e) {
			e.getMessage();
		}

		catch (NullPointerException err) {
			System.out.println("No Records obtained for this specific query");
			err.getMessage();
		}

		catch (ClassNotFoundException e1) {
			e1.getMessage();
		}

		finally{
			try {
				if(con != null)
					con.close();

			}catch(SQLException e)  {           
				e.getMessage();         
			} 
		}

		return resultList;

	}
	//####################################################################################################

	
	public static ArrayList<String> db_LatestCollectionDatafromOracle(String sqlQuery) {

		Connection con=null;
		ArrayList<String> resultList = new ArrayList<String>();
		String result;
		ArrayList<Object> AllMidRules = new ArrayList<Object>();

		try {

			Class.forName(ProjectVariables.DB_DRIVER_NAME);
			con = DriverManager.getConnection(ProjectVariables.DB_CONNECTION_URL,
					ProjectVariables.DB_USERNAME, ProjectVariables.DB_PASSWORD);			
		

			if (con != null) {
				System.out.println("Connected to the Database...");
			} else {
				System.out.println("Database connection failed ");
			}

			//Statement st = con.createStatement();
			Statement st = con.prepareStatement(sqlQuery.trim());
			st.setQueryTimeout(30000);
			ResultSet rs = st.executeQuery(sqlQuery);
			ResultSetMetaData rsmd = rs.getMetaData();

			int iColCount = rsmd.getColumnCount();
			System.out.println("Column"+iColCount);

			while (rs.next()) {

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
				String sRuleVersion;
				String sSubRuleDesc;
				String sLibKey;
				String sReasonCode;
				String sBWReasonCode;
				String sRuleHeaderKey;
				String sCoreKey;
				String sCoreDesc;
				String sRefKey;
				String sRefDesc;
				String sTitleKey;
				String sTitleDesc;
				String sRef;
				String sCatKey;

				try{
					sMPKey = rs.getString("MED_POL_KEY").toString().trim();
				}catch (Exception e){
					sMPKey = null;
				}

				try{
					sMPTitle = rs.getString("MED_POL_TITLE").toString().trim();
				}catch (Exception e){
					sMPTitle = null;
				}

				try{
					sTopicKey = rs.getString("TOPIC_KEY").toString().trim();					
				}catch (Exception e){
					sTopicKey = null;
				}

				try{
					sTopicTitle = rs.getString("TOPIC_TITLE").toString().trim();
				}catch (Exception e){
					sTopicTitle = null;
				}
				
				try{
					sTOPICDesc = rs.getString("TOPIC_DESC").toString().trim();
					sTOPICDesc = sTOPICDesc.replaceAll("[\\\r\\\n]+", "");
					sTOPICDesc = sTOPICDesc.replaceAll("[\\n\\t ]", "");
				}catch (Exception e){
					sTOPICDesc = null;
				}

				try{
					sDPKey = rs.getString("DP_KEY").toString().trim();
				}catch (Exception e){
					sDPKey = null;
				}
				
				try{
					sDPDesc = rs.getString("DP_DESC").toString().trim();
					sDPDesc = sDPDesc.replaceAll("[\\n\\t ]", "");
				}catch (Exception e){
					sDPDesc = null;
				}
				
				try{
					sSORTORDER = rs.getString("DP_SORT_ORDER").toString().trim();
				}catch (Exception e){
					sSORTORDER = null;
				}
				
				try{
					sMidRule = rs.getString("MID_RULE_KEY").toString().trim();
				}catch (Exception e){
					sMidRule = null;
				}
				
				try{
					sRuleVersion = rs.getString("RULE_VERSION").toString().trim();
				}catch (Exception e){
					sRuleVersion = null;
				}
				
				try{
					sSubRule = rs.getString("SUB_RULE_KEY").toString().trim();
				}catch (Exception e){
					sSubRule = null;
				}
				
				try{
					sSubRuleDesc = rs.getString("SUB_RULE_DESC").toString().trim();
				}catch (Exception e){
					sSubRuleDesc = null;
				}
				
				try{
					sLibKey = rs.getString("LIBRARY_STATUS_KEY").toString().trim();
				}catch (Exception e){
					sLibKey = null;
				}
				
				try{
					sReasonCode = rs.getString("REASON_CODE").toString().trim();
				}catch (Exception e){
					sReasonCode = null;
				}
				
				try{
					sBWReasonCode = rs.getString("BW_REASON_CODE").toString().trim();
				}catch (Exception e){
					sBWReasonCode = null;
				}
				
				try{
					sRuleHeaderKey = rs.getString("RULE_HEADER_KEY").toString().trim();
				}catch (Exception e){
					sRuleHeaderKey = null;
				}
				
				try{
					sCoreKey = rs.getString("PRIM_CORE_ENHANCED_KEY").toString().trim();
				}catch (Exception e){
					sCoreKey = null;
				}
				
				try{
					sCoreDesc = rs.getString("PRIM_CORE_ENHANCED_DESC").toString().trim();
				}catch (Exception e){
					sCoreDesc = null;
				}
				
				try{
					sRefKey = rs.getString("PRIM_REF_SOURCE_KEY").toString().trim();
				}catch (Exception e){
					sRefKey = null;
				}
				
				try{
					sRefDesc = rs.getString("PRIM_REF_SOURCE_DESC").toString().trim();
				}catch (Exception e){
					sRefDesc = null;
				}
				
				try{
					sTitleKey = rs.getString("PRIM_REF_TITLE_KEY").toString().trim();
				}catch (Exception e){
					sTitleKey = null;
				}
				
				try{
					sTitleDesc = rs.getString("PRIM_REF_TITLE_DESC").toString().trim();
				}catch (Exception e){
					sTitleDesc = null;
				}
				
				try{
					sRef = rs.getString("REFERENCE").toString().trim();
				}catch (Exception e){
					sRef = null;
				}
				
				try{
					sCatKey = rs.getString("GENDER_CAT_KEY").toString().trim();
				}catch (Exception e){
					sCatKey = null;
				}
				
				
				result = "MID RULE:"+sMidRule+";RULE VERSION:"+sRuleVersion+";MP KEY:"+sMPKey+";MP Title:"+sMPTitle+";TOPIC KEY:"+sTopicKey+";Topic Title:"+sTopicTitle+";TOPIC DESC:"+sTOPICDesc+
						 ";DP KEY:"+sDPKey+";DP DESC:"+sDPDesc+";SORTORDER:"+sSORTORDER+
						 ";Sub Rule Key:"+sSubRule+";SUB RULE DESC:"+sSubRuleDesc+";LIB KEY:"+sLibKey+";REASON CODE:"+sReasonCode+";BW REASON CODE:"+sBWReasonCode+";RULE HEADER KEY:"+sRuleHeaderKey+
						 ";CORE KEY:"+sCoreKey+";CORE DESC:"+sCoreDesc+";REF KEY:"+sRefKey+";REF DESC:"+sRefDesc+"TITLE KEY:"+sTitleKey+"TITLE DESC:"+sTitleDesc+
						 ";REFERENCE:"+sRef+";CAT KEY:"+sCatKey;
				
				//System.out.println("Oracle Column data "+result);
				/*if (sMidRule.equalsIgnoreCase("19703")){
				resultList.add(result);		
				}*/
				
				
				resultList.add(result);		
				
			}
			con.close();

		} catch (SQLException e) {
			System.out.println("Sytax Error "+e.getMessage());
		
		}

		catch (NullPointerException err) {
			System.out.println("No Records obtained for this specific query");
			err.getMessage();
		}

		catch (ClassNotFoundException e1) {
			e1.getMessage();
		}

		finally{
			try {
				if(con != null)
					con.close();

			}catch(SQLException e)  {           
				e.getMessage();         
			} 
		}

		return resultList;

	}
	public static String db_GetFirstValueforColumn(String sQuery,String dbColumn) throws Exception{

		Connection conn=null;      
		String sVal="";

		try {    	   

			Class.forName("oracle.jdbc.OracleDriver");

			conn = DriverManager.getConnection(ProjectVariables.DB_CONNECTION_URL,
					ProjectVariables.DB_USERNAME, ProjectVariables.DB_PASSWORD);

			Statement stmt=conn.createStatement();     
			stmt.setQueryTimeout(40);
			ResultSet rs=stmt.executeQuery(sQuery);

			while (rs.next()) {
				sVal=rs.getString(dbColumn);
			}

			System.out.println("Stored Data in DB:" +" " +sVal);

			if (conn != null) {
				conn.close();}

		}catch(Exception e) {       
			System.out.println("Exception "+e.getMessage());
		}

		finally{
			try {
				if(conn != null)
					conn.close();

			}catch(SQLException e)  {           
				e.printStackTrace();         
			} 
		}

		return sVal;

	}

	//####################################################################################################

	public static HashMap<String, String> db_GetAllFirstRowValues (String sQuery) throws Exception{

		Connection conn=null;      
		boolean blnFlag = false;
		HashMap<String, String> sRowValues = new HashMap<String, String>();

		try {    	   

			sRowValues.clear();
			Class.forName("oracle.jdbc.OracleDriver");

			conn = DriverManager.getConnection(ProjectVariables.DB_CONNECTION_URL,
					ProjectVariables.DB_USERNAME, ProjectVariables.DB_PASSWORD);

			Statement stmt=conn.createStatement();     
			stmt.setQueryTimeout(60);
			ResultSet rs=stmt.executeQuery(sQuery);

			int iColCount = rs.getMetaData().getColumnCount();
			while (rs.next()) {
				for (int i = 1 ;i<= iColCount;i++){
					blnFlag = true;
					String sColumnName = rs.getMetaData().getColumnName(i);
					String sColumnVal = rs.getString(sColumnName);
					sRowValues.put(sColumnName, sColumnVal);
				}

				if (blnFlag){break;}
			}

			if (conn != null) {
				conn.close();}

		}catch(Exception e) {       
			System.out.println("Exception "+e.getMessage());
		}

		finally{
			try {
				if(conn != null)
					conn.close();

			}catch(SQLException e)  {           
				e.printStackTrace();         
			} 
		}

		return sRowValues;

	}
	
	public static ArrayList<String> executeSQLQueryMultipleRows(String sqlQuery) throws Exception
	{
				
		ArrayList<String> resultList = new ArrayList<String>();
	
	try {					        		
        	Class.forName(ProjectVariables.DB_DRIVER_NAME);
        	
        	Connection conn = DriverManager.getConnection(ProjectVariables.DB_CONNECTION_URL,
					ProjectVariables.DB_USERNAME, ProjectVariables.DB_PASSWORD);
        	
        	  if(conn!=null) {
                  System.out.println("Connected to the Database...");
              }else {
                  System.out.println("Database connection failed ");
              }
        	        	  
        	  String result;        	  
        	  Statement st = conn.createStatement();	        		
        	  ResultSet rs =st.executeQuery(sqlQuery);  
        	  
        	  int i = 0;
        	  while (rs.next() && i != 500) {
        	          result = rs.getString(1).toString();
        	          resultList.add(result);
        	          i = i + 1;
        	      }        	
        	           
        	  if (conn != null) {
      	    	conn.close();} 
   
            
        }catch (SQLException e) {
            e.printStackTrace();}
        
        catch (NullPointerException err) {
            System.out.println("No Records obtained for this specific query");
            err.printStackTrace();              }        
		
	     catch (ClassNotFoundException e1) {     		  		
				e1.printStackTrace();	}
	  
	  return resultList;
        
	}   

	public static ArrayList<String> executeSQLQueryMultipleRows(String sqlQuery,String DB_CONNECTION_URL,String DB_USERNAME,String DB_PASSWORD) throws Exception
	{
				
		ArrayList<String> resultList = new ArrayList<String>();
	
	try {					        		
        	Class.forName(ProjectVariables.DB_DRIVER_NAME);
        	
        	Connection conn = DriverManager.getConnection(DB_CONNECTION_URL,DB_USERNAME, DB_PASSWORD);
        	
        	  if(conn!=null) {
                  System.out.println("Connected to the Database...");
              }else {
                  System.out.println("Database connection failed ");
              }
        	        	  
        	  String result;        	  
        	  Statement st = conn.createStatement();	        		
        	  ResultSet rs =st.executeQuery(sqlQuery);  
        	  
        	  int i = 0;
        	  while (rs.next() && i != 500) {
        	          result = rs.getString(1).toString();
        	          resultList.add(result);
        	          i = i + 1;
        	      }        	
        	           
        	  if (conn != null) {
      	    	conn.close();} 
   
            
        }catch (SQLException e) {
            e.printStackTrace();}
        
        catch (NullPointerException err) {
            System.out.println("No Records obtained for this specific query");
            err.printStackTrace();              }        
		
	     catch (ClassNotFoundException e1) {     		  		
				e1.printStackTrace();	}
	  
	  return resultList;
        
	}  
//	================================================================================================================================>
	public static ArrayList<String> getQueryResults(String sQuery,String DB_CONNECTION_URL,String DB_USERNAME,String DB_PASSWORD,String[] dbColumn) throws Exception
    {
      Connection conn=null;      
      ArrayList<String> values = null;  
    
     try {    	   
         // Load the Driver class. 
          Class.forName("oracle.jdbc.OracleDriver");
          // Create the connection using the static getConnection method 
          conn=DriverManager.getConnection(DB_CONNECTION_URL,DB_USERNAME, DB_PASSWORD);
          //Create a Statement class to execute the SQL statement 
          Statement stmt=conn.createStatement();
          //Execute the SQL statement and get the results in a Resultset 
          ResultSet rs=stmt.executeQuery(sQuery);
          values = new ArrayList<String>();
			while (rs.next()) {
				for (int i = 0; i < dbColumn.length; i++) {					
					values.add(rs.getString(dbColumn[i]));										
				}	
			}
			System.out.println("Stored Data in DB:" +" " +values);
			 if (conn != null) {
			       	conn.close();}
      }
    
      catch(Exception e) {
             
            // GenericUtils.fn_ReportEvent("SQL Exception Occured","error is : "+e.toString(),"Failed");
      }
       finally{
              try {
                     if(conn != null)
                   conn.close();
              }
               catch(SQLException e)  {
               	 e.printStackTrace();
               	 //GenericUtils.fn_ReportEvent("SQL Connection issue","Connection Error is :"+e.toString(),"Failed");
               } 
       }
    
	return values;
                     
      }
	
	//####################################################################################################
	public static List<String> getRuleRelationshipDatafromOracle(String Query,String DBName,String placholder1)
	{   
		List<String> DPKeysVals =  new ArrayList<String>();
		Connection con =  null;
		
		try
		{
			Class.forName(ProjectVariables.DB_DRIVER_NAME);
			 con = DriverManager.getConnection(ProjectVariables.DB_CONNECTION_URL_PMPRD2,
					ProjectVariables.DB_USERNAME, ProjectVariables.DB_PASSWORD);
			 if(con!=null)
			 {
				 System.out.println("Connection success");				 
			 }
			 
			 String qry = "select r.mid_rule_key,count(1) from RULE_UPKEEP.VW_MR_RELATIONS r, ELL.VLIBRARY_VERSIONS s "
					 + " where r.last_updated_ts <= to_date('29/10/2019','dd/mm/yyyy') "
					 + " and r.deactivated_10 = 0 and r.mid_rule_key = s.mid_rule_key "
					 + " and s.release_log_key = 1857 "
					 + "  group by r.mid_rule_key";
			 
			   Statement Stmt = con.createStatement();
			   ResultSet  rs = Stmt.executeQuery(qry);
			   while(rs.next())
			   {
			       System.out.println("Value::"+rs.getString("mid_rule_key").toString());
			       break;  
			   }  
			   con.close();
		}
		catch(Exception e)
		{
			  System.out.println(e.getMessage());
		}
		
		return DPKeysVals;
	}
	
	public static ArrayList<String> executeSQLQuery(String sqlQuery,String DBConnectionUrl,String Criteria)
	{
		String queryResultValue= "";
		ArrayList<String> ResultList=new ArrayList<>();
		Connection con=null;
		String sMidRulekey=null;
		String sRuleversion=null;
		String sSubruleKey=null;
		String sPayerkey=null;
		String sInsuranceKey=null;
		String sClaimtype=null;
		String sProd_10=null;
		String sTest_10=null;
		String sPayershort=null;
		String sInsurance=null;
		

		try {

			Class.forName(ProjectVariables.DB_DRIVER_NAME);
			con = DriverManager.getConnection(DBConnectionUrl,ProjectVariables.DB_USERNAME,ProjectVariables.DB_PASSWORD);

			if(con!=null) {
				System.out.println("Connected to the Database...");
			}else {
				System.out.println("Database connection failed ");
			}

			Statement st = con.createStatement();
			st.setQueryTimeout(30);
			ResultSet rs =st.executeQuery(sqlQuery);        	

			switch(Criteria)
			{
			case "Count":
				while(rs.next())
				{
					queryResultValue = rs.getString(1).toString();   
					ResultList.add(queryResultValue);
					//break;
				} 
			break;
			case "All Rows":
				while(rs.next())
				{
					sMidRulekey=rs.getString(1);
					sRuleversion=rs.getString(2);
					sSubruleKey=rs.getString(3);
					sPayerkey=rs.getString(4);
					sInsuranceKey=rs.getString(5);
					sClaimtype=rs.getString(6);
					sProd_10=rs.getString(7);
					sTest_10=rs.getString(8);
					sPayershort=rs.getString(9);
					sInsurance=rs.getString(10);
					
					
					ResultList.add(sPayerkey+";"+sInsuranceKey+";"+sClaimtype+";"+sProd_10+";"+sTest_10+";"+sPayershort+";"+sInsurance+";"+sRuleversion);
					
				} 
				break;
			
			default:
				Assert.assertTrue("Case not found==>"+Criteria, false);
				break;
			}
			
			

			System.out.println("Oracle DBResultCount===>"+ResultList.size());
			System.out.println("Oracle DBResult===>"+ResultList);

			con.close();

		}catch (SQLException e) {
			e.printStackTrace();}

		catch (NullPointerException err) {
			System.out.println("No Records obtained for this specific query ==>"+sqlQuery);
			err.getMessage();              }        

		catch (ClassNotFoundException e1) {     		  		
			e1.getMessage();	}

		finally{
			try {
				if(con != null)
					con.close();

			}catch(SQLException e)  {           
				e.getMessage();         
			} 
		}
		
		

		return ResultList;

	}  
	
	public static ArrayList<String> db_GetRuleStatus(String sqlQuery,String sColumns) {

		Connection con=null;
		ArrayList<String> resultList = new ArrayList<String>();
		
		ArrayList<Integer> AllSubRules = new ArrayList<Integer>();

		try {

			Class.forName(ProjectVariables.DB_DRIVER_NAME);
			con = DriverManager.getConnection("jdbc:Oracle:thin:@pmprd2.ihtech.com:1521/PMPRD2.iht.com",
					ProjectVariables.DB_USERNAME, ProjectVariables.DB_PASSWORD);

			if (con != null) {
				System.out.println("Connected to the Database...");
			} else {
				System.out.println("Database connection failed ");
			}

			//Statement st = con.createStatement();
			Statement st = con.prepareStatement(sqlQuery.trim());
			st.setQueryTimeout(1000);
			ResultSet rs = st.executeQuery(sqlQuery);
			ResultSetMetaData rsmd = rs.getMetaData();

			
			while (rs.next()) {

				String sColumnValue;
				String result = "";
				String sSubRuleKey;
				
				try{
					sSubRuleKey = rs.getString("SUB_RULE_KEY").toString();	
				}catch (Exception e){
					sSubRuleKey = null;
				}
				
				String[] sColumnList = sColumns.split(";");
				
				for (int i = 0 ;i<sColumnList.length;i++){
					
					try{
						sColumnValue = rs.getString(sColumnList[i]).toString();	
					}catch (Exception e){
						sColumnValue = null;
					}
					
					result = result+sColumnList[i]+":"+sColumnValue;
				}
				
				
				System.out.println("Column data "+result);
				resultList.add(result);
				AllSubRules.add(Integer.parseInt(sSubRuleKey));
				Serenity.setSessionVariable("slstSubRules").to(AllSubRules);
			}
			con.close();

		} catch (SQLException e) {
			e.getMessage();
		}

		catch (NullPointerException err) {
			System.out.println("No Records obtained for this specific query");
			err.getMessage();
		}

		catch (ClassNotFoundException e1) {
			e1.getMessage();
		}

		finally{
			try {
				if(con != null)
					con.close();

			}catch(SQLException e)  {           
				e.getMessage();         
			} 
		}

		return resultList;

	}

	public static ArrayList<String> getNewVersionCreatedRules(String sqlQuery,String sColumns) throws SQLException {
		
		Connection con=null;
		ArrayList<String> resultList = new ArrayList<String>();
		
		ArrayList<Integer> AllMidRules = new ArrayList<Integer>();

		try {

			Class.forName(ProjectVariables.DB_DRIVER_NAME);
			con = DriverManager.getConnection("jdbc:Oracle:thin:@usappmprd01:1521/PMPRD1.iht.com",
					ProjectVariables.DB_USERNAME, ProjectVariables.DB_PASSWORD);

			if (con != null) {
				System.out.println("Connected to the Database...");
			} else {
				System.out.println("Database connection failed ");
			}

			//Statement st = con.createStatement();
			Statement st = con.prepareStatement(sqlQuery.trim());
			st.setQueryTimeout(1000);
			ResultSet rs = st.executeQuery(sqlQuery);
			
			while (rs.next()) {

				String sColumnValue;
				String result = "";
				String sMidRule;
				
				try{
					sMidRule = rs.getString("NEW_MID_RULE").toString();	
				}catch (Exception e){
					sMidRule = null;
				}
				
				String[] sColumnList = sColumns.split(";");
				
				for (int i = 0 ;i<sColumnList.length;i++){
					
					try{
						sColumnValue = rs.getString(sColumnList[i]).toString();	
					}catch (Exception e){
						sColumnValue = null;
					}
					
					result = result+sColumnList[i]+":"+sColumnValue;
				}
				
				
				System.out.println("Column data "+result);
				resultList.add(result);
				AllMidRules.add(Integer.parseInt(sMidRule));
				Serenity.setSessionVariable("slstMidRules").to(AllMidRules);
			}
		}
		catch (NullPointerException err) {
			System.out.println("No Records obtained for this specific query");
			err.getMessage();
		}

		catch (ClassNotFoundException e1) {
			e1.getMessage();
		}

		finally{
			try {
				if(con != null)
					con.close();

			}catch(SQLException e)  {           
				e.getMessage();         
			} 
		}

		return resultList;
		
		
	}

	public static String executeSQLQuery(String sqlQuery,String dbconnurl,String username,String password)
	{
		String queryResultValue= "";
		Connection con=null;

		try {
			Class.forName(ProjectVariables.DB_DRIVER_NAME);			
			con = DriverManager.getConnection(dbconnurl,username,password);

			if(con!=null) {
				System.out.println("Connected to the Database...");
			}else {
				System.out.println("Database connection failed ");
			}

			Statement st = con.createStatement();
			st.setQueryTimeout(30);
			ResultSet rs =st.executeQuery(sqlQuery);        	

			while(rs.next())
			{
				queryResultValue = rs.getString(1).toString();   
				break;
			} 

			System.out.println("DB Result: "+queryResultValue);

			con.close();

		}catch (SQLException e) {
			e.printStackTrace();}

		catch (NullPointerException err) {
			System.out.println("No Records obtained for this specific query");
			err.getMessage();              }        

		catch (ClassNotFoundException e1) {     		  		
			e1.getMessage();	}

		finally{
			try {
				if(con != null)
					con.close();

			}catch(SQLException e)  {           
				e.getMessage();         
			} 
		}

		return queryResultValue;

	}   

	


}
