package project.utilities;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Field;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.json.simple.JSONObject;
import org.junit.Assert;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

import cucumber.api.Result;
import cucumber.api.Scenario;
import cucumber.runtime.junit.JUnitReporter;
import gherkin.ast.Step;
import io.restassured.response.Response;
import io.restassured.response.ValidatableResponse;
import net.serenitybdd.core.Serenity;
import net.serenitybdd.core.pages.PageObject;
import net.serenitybdd.rest.SerenityRest;
import net.thucydides.core.annotations.ManagedPages;
import net.thucydides.core.pages.Pages;
import project.utilities.SeleniumUtils;

public class ZTA extends SeleniumUtils {
	

	public static <ExecutionUnitRunner>  void failure_Categorization(WebDriver driver,Scenario sScenario) throws Exception{

	DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
	Date date = new Date();

	String sDate = dateFormat.format(date).toString();        	
	//StringUtils.substringBefore(StringUtils.substringAfterLast(sScenario.getSourceTagNames().toString(), "@"),"]");
	String sTagName=StringUtils.substringAfter(sScenario.getSourceTagNames().toArray()[0].toString(), "@");	
	String sStatus = sScenario.getStatus().toString();
	String sTCname = sScenario.getName();
	//System.out.println(StringUtils.substringBefore(sScenario.getId(), ".feature"));
	String sFeatureName = StringUtils.substringAfterLast(StringUtils.substringBefore(sScenario.getId(), ".feature"),"/");
	String sReason = "" ;
    String sReasonCode="";
    String sErrormessage="";
    String sDesc = "";
    String ScreenShotPath ="";
    String StepURL=null;
    ArrayList<Result> sResults = null;
    ArrayList<Step> sSteps = null;
    int size=0;
    System.out.println("#########################################################################");
    System.out.println(sScenario.getSourceTagNames());
    System.out.println("FeatureName::"+sFeatureName);
    System.out.println("TCname::"+sTCname);
    System.out.println("Tagname::"+sTagName);
    System.out.println("TCStatus::"+sStatus);
    System.out.println("#########################################################################");
    
    //Insert Tcs into TestPlan collection
    InsertTCsdataIntoDB(sTagName,sFeatureName,sTCname,sStatus,sDate,"","","","","Testplan");
    driver.quit();
	
	
	
	}	


	@SuppressWarnings({ "unchecked" })
	public static void InsertTCsdataIntoDB(String sTagName,String sFeaturename , String sTcName, String sTcStatus,String sDate,String sReasonCode,String sReason,String sDesc,String sPath,String criteria) throws Exception {
		String requestbody=null;
		JSONObject ExecutionDetails=new JSONObject() ;
		ExecutionDetails.put("featureNo", "");
		ExecutionDetails.put("featureName", sFeaturename);
		ExecutionDetails.put("testscriptName", sTcName);		
		ExecutionDetails.put("userstoryNo", "");
		ExecutionDetails.put("tagName", sTagName);
		ExecutionDetails.put("release", "APR 2019");
		ExecutionDetails.put("priority", "High");
		ExecutionDetails.put("testType", "Regression");
		switch(criteria)
		{
		case "Testplan":
			requestbody=ExecutionDetails.toString();
			//Method to insert through service
			MicroServRestUtils.Post_the_Data__through_service_using_HTTP_Client(requestbody, "http://uapcippm01:8080/SpringwithMongo-ZTAServices/testplan/adduniquerecord/pm");
			System.out.println("Insereted record into TestPlan collection::"+ExecutionDetails.toJSONString());
			
			break;
		case "Testlab":
			ExecutionDetails.put("testcaseStatus", sTcStatus);
			ExecutionDetails.put("failureReason", "");
			ExecutionDetails.put("FailureScreenshot","");
			ExecutionDetails.put("reasonCode", "");
			ExecutionDetails.put("executionTime",sDate);
			ExecutionDetails.put("stepDescription", "");
			ExecutionDetails.put("DefectNo", "");
			ExecutionDetails.put("defectStaus", "");
			requestbody=ExecutionDetails.toString();
			//Method to insert through service
			MicroServRestUtils.Post_the_Data__through_service_using_HTTP_Client(requestbody, "http://uapcippm01:8080/SpringwithMongo-ZTAServices/testlab/updatesinglerecord/pm");
			System.out.println("Insereted record into TestLab collection::"+ExecutionDetails.toJSONString());
			
			break;
		default:
			Assert.assertTrue("Case not found::"+criteria, false);
			break;
		}
		
      }
	}



   
   

