package project.runner;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.json.simple.parser.ParseException;
import org.junit.Assert;

import net.serenitybdd.core.Serenity;
import project.pageobjects.CPWPage;
import project.pageobjects.PresentationDeck;
import project.pageobjects.PresentationProfile;
import project.utilities.ExcelUtils;
import project.utilities.GenericUtils;
import project.utilities.MongoDBUtils;
import project.utilities.OracleDBUtils;
import project.variables.MonGoDBQueries;
import project.variables.OracleDBQueries;
import project.variables.ProjectVariables;
import projects.steps.definitions.PMServicesStepDef;

public class TestDebug {
	static int i;
	
	public static void main(String[] args) throws Exception {
		
		
	}
	
	public static void sett(){
		
		
		try{
		for( int j =1;j<8;j++){
			i  = i+1;
			
			if (i==3){
				//int num=Integer.parseInt ("XYZ");
				fail();
			}
			System.out.println(i);
		}
		
		}catch(Exception e){
			
			Assert.assertTrue(false);
		}
		finally{
			if (i<8){
			sett();
		}
		}
		
		
		}
	
	public static void fail(){
		
		Assert.assertTrue(false);
		
	}

}