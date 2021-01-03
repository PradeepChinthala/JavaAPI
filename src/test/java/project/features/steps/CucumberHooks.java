package project.features.steps;


import cucumber.api.Scenario;
import cucumber.api.java.After;

import net.thucydides.core.annotations.ManagedPages;
import net.thucydides.core.pages.Pages;
import project.utilities.ZTA;

public class CucumberHooks {
	
	ZTA oZTA;
	
	@ManagedPages
	private Pages pages;

	
	
        @After 
        public void cleanUp(Scenario sScenario) throws Exception{
        	
        	//ZTA.failure_Categorization(pages.getDriver(), sScenario);
     
        }
      
      
}
