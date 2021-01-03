package project.runner;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.runner.RunWith;

import cucumber.api.CucumberOptions;
import net.serenitybdd.cucumber.CucumberWithSerenity;
import project.utilities.MongoDBUtils;
import project.variables.ProjectVariables;

@RunWith(CucumberWithSerenity.class)

@CucumberOptions( plugin = {"pretty", "html:target/cucumber-html-report"}, 
tags={"@ChangeService"},
monochrome = true,
features=".", glue="project.features.steps")


public class TestRunner 
{


	@BeforeClass
	public static void dbConnections(){

		MongoDBUtils.connectWithCredentials(ProjectVariables.MONGO_SERVER_PORT);
	}


	@AfterClass
	public static void closeConnection(){
		MongoDBUtils.finalize2();
	}
}

