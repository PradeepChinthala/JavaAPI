package project.utilities;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.concurrent.TimeUnit;

import org.bson.BsonDocument;
import org.bson.conversions.Bson;
import org.junit.Assert;
import org.openqa.selenium.Cookie;
import org.openqa.selenium.ElementNotVisibleException;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Action;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.Wait;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.mongodb.client.model.Filters;

import net.serenitybdd.core.annotations.findby.By;
import net.serenitybdd.core.pages.PageObject;
import net.serenitybdd.core.pages.WebElementFacade;
import net.serenitybdd.core.pages.WebElementState;
import net.thucydides.core.webdriver.javascript.JavascriptExecutorFacade;
import project.exceptions.ElementNotFoundException;
import project.variables.ProjectVariables;

public class SeleniumUtils extends PageObject {



	// #####################################################################################################################

	// NOTE : JAVA SCRIPT HELPER METHODS WILL BE SPECIFIC TO THE PROJECT

	// #####################################################################################################################

	// ####################### OBJECT SYNCHRONIZATION METHODS
	// ###################################################################

	public static void logMessage(String message) {
		GenericUtils.logMessage(message);
	}



	// ####################################################################################################
	public void switchHandleToPMBrowser() {
		WebDriver driver = getDriver();
		ProjectVariables.parentWindow = driver.getWindowHandle();
		Set<String> handles =  driver.getWindowHandles();
		for(String windowHandle  : handles)
		{
			System.out.println("driver: " +windowHandle);
			if(!windowHandle.equals(ProjectVariables.parentWindow))
			{
				ProjectVariables.childWindow = windowHandle;
				driver.switchTo().window(windowHandle);
				driver.manage().window().maximize();
				//		          driver.manage().window().fullscreen();
				//		          driver.manage().window().maximize();
			}
		}

	}
	public  void waitForElement(String sXPath, String sCondition,int iWaitTime)
	{		
		WebDriverWait wait= new WebDriverWait(getDriver(),iWaitTime,ProjectVariables.ExplicitWait_PollTime);
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(sXPath)));		

	}


	public void Click_given_Xpath(String sXpath) {
		//highlightElement(sXpath);
		$(sXpath).withTimeoutOf(ProjectVariables.TImeout_10_Seconds ,ChronoUnit.SECONDS).waitUntilVisible().click();
	}

	public List<String>  getWebElementValuesAsList(String XPath) 
	{
		List<WebElement>  ListWebElement  = new  ArrayList<WebElement>(); 
		List<String>  ElementsValues =   new  ArrayList();

		try{    

			try {
				ListWebElement =  getElementsList("XPATH",XPath) ;
			} catch (ElementNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			for(int j=0;j<ListWebElement.size();j++)
			{
				String  sVal = ListWebElement.get(j).getText().trim();
				ElementsValues.add(sVal);		        	 
			}
		}catch (ElementNotVisibleException e) { }

		return ElementsValues;

	}

	public void highlightElement(String Xpath) {

		WebElement element = getDriver().findElement(By.xpath(Xpath));
		for (int i = 0; i < ProjectVariables.HIGHLIGHT_COUNT; i++) {
			WebDriver driver = getDriver();
			JavascriptExecutor js = (JavascriptExecutor) driver;
			js.executeScript("arguments[0].setAttribute('style', arguments[1]);", element,
					"color: black; border: 6px solid black;");
			js.executeScript("arguments[0].setAttribute('style', arguments[1]);", element, "");
		}

	}

	public void removehighlightElement(String Xpath) {
		WebElement element = getDriver().findElement(By.xpath(Xpath));
		for (int i = 0; i < ProjectVariables.HIGHLIGHT_COUNT; i++) {

			evaluateJavascript("arguments[0].style.border=''", element);

		}
	}


	public void highlightElement(WebElement element) {

		for (int i = 0; i < ProjectVariables.HIGHLIGHT_COUNT; i++) {
			WebDriver driver = getDriver();
			JavascriptExecutor js = (JavascriptExecutor) driver;
			js.executeScript("arguments[0].setAttribute('style', arguments[1]);", element,
					"color: black; border: 6px solid black;");
			js.executeScript("arguments[0].setAttribute('style', arguments[1]);", element, "");
		}

	}

	public void highlightWebElement(WebElement  We) {

		for (int i = 0; i < ProjectVariables.HIGHLIGHT_COUNT; i++) {
			WebDriver driver = getDriver();
			JavascriptExecutor js = (JavascriptExecutor) driver;
			js.executeScript("arguments[0].setAttribute('style', arguments[1]);", We,
					"color: black; border: 6px solid black;");
			js.executeScript("arguments[0].setAttribute('style', arguments[1]);", We, "");
		}

	}

	public String getTexFfromLocator(String Xpath) {
		String text = "";
		try{
			WebElement element = getDriver().findElement(By.xpath(Xpath));
			text = element.getText();			
		}
		catch(Exception e){ }
		return text;
	}

	public void switchHandleToCPWBrowser() {
		WebDriver driver = getDriver();
		driver.switchTo().window(ProjectVariables.parentWindow);

	}


	public void switchHangleToCPW() {
		getDriver().switchTo().window(ProjectVariables.parentWindow);

	}


	public void waitForPageLoad() throws NoSuchElementException {
		System.out.println("Page loaded in");
		long start = System.currentTimeMillis();
		Wait<WebDriver> wait = new FluentWait<WebDriver>(getDriver())
				.withTimeout(ProjectVariables.Page_Load_Timeout, TimeUnit.SECONDS).pollingEvery(5, TimeUnit.SECONDS)
				.ignoring(NoSuchElementException.class);
		ExpectedCondition<Boolean> pageLoadCondition = new ExpectedCondition<Boolean>() {
			public Boolean apply(WebDriver driver) {
				WebElement element = driver.findElement(By.id("mxui_widget_Progress_0"));
				if (element.getAttribute("class").equals("mx-progress mx-progress-empty mx-progress-hidden")) {
					return true;
				} else {
					return false;
				}
			}
		};wait.until(pageLoadCondition);
		long end = System.currentTimeMillis();
		System.out.println("ms: " + (end - start));
	}


	public void waitForContentLoad() throws NoSuchElementException {
		boolean bstatus=false;
		int j = 1;
		System.out.println("Loading is in progress");
		
		do {
			defaultWait(ProjectVariables.TImeout_2_Seconds);
			j = j + 1;
			
			if (j == 200) {
				break;
			}
			
			bstatus=is_WebElement_Displayed("//p[contains(text(),'Presentation Loading') or contains(text(),'Please wait. Content loading')]");
			
		} while (bstatus);
		
		if(bstatus)
		{
			Assert.assertTrue("Loading is taking more time....,more than '6' minutes", false);
		}
		

		
	}



	public void writeCookie(){

		try{			

			File file = new File(ProjectVariables.filePathforCookie);							
			FileReader fileReader = new FileReader(file);							
			BufferedReader Buffreader = new BufferedReader(fileReader);							
			String strline;			
			while((strline=Buffreader.readLine())!=null){									
				StringTokenizer token = new StringTokenizer(strline,";");									
				while(token.hasMoreTokens()){					
					String name = token.nextToken();					
					String value = token.nextToken();					
					String domain = token.nextToken();					
					String path = token.nextToken();					
					Date expiry = null;					

					String val;			
					if(!(val=token.nextToken()).equals("null"))
					{		
						expiry = new Date(val);					
					}		
					Boolean isSecure = new Boolean(token.nextToken()).								
							booleanValue();		
					Cookie ck = new Cookie(name,value,domain,path,expiry,isSecure);			
					System.out.println(ck);
					getDriver().manage().addCookie(ck); // This will add the stored cookie to your current session					
				}		
			}		
		}catch(Exception ex){					
			ex.printStackTrace();			
		}		
	}

	public String capatureCookie(String arg1){
		String path = ProjectVariables.filePathforCookie;
		File file = new File(path);							

		try		
		{	  
			System.out.println("Tile: "+getDriver().getTitle());
			// Delete old file if exists
			file.delete();		
			file.createNewFile();			
			FileWriter fileWrite = new FileWriter(file);							
			BufferedWriter Bwrite = new BufferedWriter(fileWrite);							
			for(Cookie ck : getDriver().manage().getCookies())							
			{			
				System.out.print("recodring cookies in file");
				Bwrite.write((ck.getName()+";"+ck.getValue()+";"+ck.getDomain()+";"+ck.getPath()+";"+ck.getExpiry()+";"+ck.isSecure()));																									
				Bwrite.newLine();             
			}			
			Bwrite.close();			
			fileWrite.close();	

		}
		catch(Exception ex)					
		{		
			ex.printStackTrace();			
		}		
		return path;
	}


	public boolean waitUntillGivenWindowPresent(int Time, int NoofWindows) {
		boolean flag = false;
		Set<String> windows = getDriver().getWindowHandles();
		int windowCount = windows.size();
		for (int i = 0; i < Time; i++) {
			if (windowCount >= NoofWindows) {
				flag = true;
				GenericUtils.logMessage("No wait required as windowCount >= NoofWindows");
				break;

			} else {
				try {
					defaultWait(ProjectVariables.TImeout_2_Seconds);
					GenericUtils.logMessage("Waited for Sleep :" + ProjectVariables.TImeout_2_Seconds);
					flag = true;
				} catch (Exception e) {
					GenericUtils.logMessage("Exception Thrown" + e.toString());
					flag = false;

				}

			}
		}
		return flag;

	}
	public List<WebElement> getElementsList(String locatorType, String locatorValue) throws ElementNotFoundException {

		List<WebElement> elements = null;
		try {

			switch (locatorType) {
			case "NAME":

				elements = getDriver().findElements(By.name(locatorValue));

				break;
			case "XPATH":			
				elements = getDriver().findElements(By.xpath(locatorValue));

				break;
			case "ID":			

				elements = getDriver().findElements(By.id(locatorValue));
				break;
			case "CSS":	

				elements = getDriver().findElements(By.cssSelector(locatorValue));
				break;
			case "CLASS":
				elements = getDriver().findElements(By.className(locatorValue));
				break;
			case "LINK_TEXT":
				elements = getDriver().findElements(By.linkText(locatorValue));

				break;
			case "PARTIAL_LINK_TEXT":				

				elements = getDriver().findElements(By.partialLinkText(locatorValue));

				break;
			case "TAG_NAME":

				elements = getDriver().findElements(By.tagName(locatorValue));

				break;
			default:

				break;
			}	


		}catch (Exception e) {
			GenericUtils.logMessage(
					"Elements not present:  of Locator Type :" + locatorType + "   of Value :  " + locatorValue);
			throw new ElementNotFoundException("No Locator found for : " + locatorValue);
		}
		return  elements;

	}
	public boolean clickGivenWebElement(WebElement element) {
		boolean flag = false;	
		try{
			element.click();
			flag = true;
		} catch (Exception ignored) {
			GenericUtils.logMessage("Element :" + element.toString() + " Not Clicked");
			flag = false;
		}
		return flag;
	}


	public void resetImplicitWaitToDefault() {

		resetImplicitTimeout();
		GenericUtils.logMessage("Resetted Implicit Timeout");
	}

	public void waitForPresenceOfElement(WebElementFacade element, int TimeoutinSeconds) {
		element.withTimeoutOf(TimeoutinSeconds, TimeUnit.SECONDS).waitUntilPresent();
		GenericUtils.logMessage(
				"Waited for Presence Of Element : " + element.toString() + "for seconds:" + TimeoutinSeconds);

	}

	public void waitForVisibilityOfElement(WebElementFacade element, int TimeoutinSeconds) {
		element.withTimeoutOf(TimeoutinSeconds, TimeUnit.SECONDS).waitUntilVisible();
		GenericUtils.logMessage(
				"Waited for Visibility Of Element :" + element.toString() + "for seconds:" + TimeoutinSeconds);
	}

	public void waitForElementNoTVisible(WebElementFacade element, int TimeoutinSeconds) {
		element.withTimeoutOf(TimeoutinSeconds, TimeUnit.SECONDS).waitUntilNotVisible();
		GenericUtils.logMessage(
				"Waited for Element Not Visible :" + element.toString() + "for seconds:" + TimeoutinSeconds);
	}

	public void waitForElementToEnable(WebElementFacade element, int TimeoutinSeconds) {
		element.withTimeoutOf(TimeoutinSeconds, TimeUnit.SECONDS).waitUntilEnabled();
		GenericUtils
		.logMessage("Waited for Element To Enable :" + element.toString() + "for seconds:" + TimeoutinSeconds);
	}

	public void waitForElementToBeClickable(WebElementFacade element, int TimeoutinSeconds) {
		element.withTimeoutOf(TimeoutinSeconds, TimeUnit.SECONDS).waitUntilClickable();
		GenericUtils.logMessage(
				"waited For Element To Be Clickable :" + element.toString() + "for seconds:" + TimeoutinSeconds);
	}

	public void waitForWebElements(List<WebElementFacade> elements, int TimeOutinseconds) {

		withTimeoutOf(TimeOutinseconds, TimeUnit.SECONDS).waitFor(elements);
		GenericUtils.logMessage("waited For WebElements in sec :" + TimeOutinseconds);
	}

	// added for CPW
	public void waitForTextByWebElementClass(WebElementFacade webElement, int TimeOutInSeconds, String textStr) {
		WebDriverWait wait = new WebDriverWait(getDriver(), TimeOutInSeconds);
		wait.until(ExpectedConditions.textToBePresentInElement(webElement, textStr));

		GenericUtils.logMessage(
				"Waited for Presence Of Text : " + webElement.getText() + " for seconds: " + TimeOutInSeconds);
	}

	public void defaultWait(long i) {
		try {

			Thread.sleep(TimeUnit.SECONDS.toMillis(i));
			// GenericUtils.logMessage("Waited for " + i + " seconds");

		} catch (InterruptedException e) {

			e.printStackTrace();
		}
	}

	public void setGivenInmplicitWait(int TimeoutinSeconds) {

		setImplicitTimeout(TimeoutinSeconds, ChronoUnit.SECONDS);
		GenericUtils.logMessage("Implicit wait setted for " + TimeoutinSeconds + " seconds");
	}

	// added for CPW
	public void waitUntilStalenessOfElementIsRemoved(WebElementFacade webElem, int TimeoutinSeconds) {
		WebDriverWait wait = new WebDriverWait(this.getDriver(), TimeoutinSeconds);
		wait.until(ExpectedConditions.stalenessOf(webElem));
		wait.until(ExpectedConditions.elementToBeClickable(webElem));
	}

	// ####################### OBJECT HIGHLIGHT METHODS
	// ###################################################################

	public void highlightRemoveElement(WebElementFacade element) {

		for (int i = 0; i < ProjectVariables.HIGHLIGHT_COUNT; i++) {

			evaluateJavascript("arguments[0].style.border='2px solid green'", element);
			// GenericUtils.logMessage("Highlighted Element");

			evaluateJavascript("arguments[0].style.border=''", element);
			// GenericUtils.logMessage(" Removed Highlight on Element");
		}
	}

	public void removehighlightElement(WebElementFacade element) {

		for (int i = 0; i < ProjectVariables.HIGHLIGHT_COUNT; i++) {

			evaluateJavascript("arguments[0].style.border=''", element);
			// GenericUtils.logMessage(" Removed Highlight on Element");
		}
	}

	public void highlightElements(List<WebElementFacade> elements) {

		for (int i = 0; i < elements.size(); i++) {

			evaluateJavascript("arguments[0].style.border='2px solid green'", elements.get(i));
			evaluateJavascript("arguments[0].style.border=''", elements.get(i));
			defaultWait(ProjectVariables.TImeout_2_Seconds);
		}

	}

	// added for CPW
	// ####################### WEBPAGE MANIPULATION METHODS
	// ################################################################

	public boolean jseScrollOnPage(String direction) {
		boolean flag = true;
		JavascriptExecutorFacade jseFacade = new JavascriptExecutorFacade(getDriver());
		switch (direction) {
		case "up":
			GenericUtils.logMessage("Scrolling up...");
			jseFacade.executeScript("window.scrollBy(0,-250)", "");
			GenericUtils.logMessage("Scrolled up...");
			flag = flag && true;
			break;
		case "down":
			GenericUtils.logMessage("Scrolling down...");
			jseFacade.executeScript("window.scrollBy(0,250)", "");
			GenericUtils.logMessage("Scrolled down...");
			flag = flag && true;
			break;
		case "right":
			GenericUtils.logMessage("Scrolling right...");
			jseFacade.executeScript("arguments[0].scrollRight += 250", "");
			GenericUtils.logMessage("Scrolled right...");
			flag = flag && true;
			break;
		case "left":
			GenericUtils.logMessage("Scrolling left...");
			jseFacade.executeScript("arguments[0].scrollLeft += 250", "");
			GenericUtils.logMessage("Scrolled left...");
			flag = flag && true;
			break;
		default:
			GenericUtils.logErrorMesage("Incorrect direction to scroll in..." + direction);
			flag = false;
		}
		return flag;
	}

	// added for CPW
	public boolean scrollUsingScrollBar(String webElementXPathStr, int scrollByOffset) {
		boolean flag = false;
		WebElement scrollBar = getDriver().findElement(By.xpath(webElementXPathStr));
		Actions builder = new Actions(getDriver());
		builder.moveToElement(scrollBar);
		builder.clickAndHold();
		// builder.sendKeys(Keys.DOWN);
		builder.moveByOffset(0, scrollByOffset);
		builder.release();
		builder.build().perform();

		return flag;
	}

	public boolean slideAlongTheSlider(String webElementXPathStr, int scrollByOffset) {
		boolean flag = false;
		WebElement scrollBar = getDriver().findElement(By.xpath(webElementXPathStr));
		Actions move = new Actions(getDriver());
		Action action = move.dragAndDropBy(scrollBar, scrollByOffset, 0).build();
		action.perform();
		return flag;
	}

	public boolean scrollHorizontalUsingScrollBar(String webElementXPathStr, int scrollByOffset) {
		boolean flag = false;
		WebElement scrollBar = getDriver().findElement(By.xpath(webElementXPathStr));
		Actions builder = new Actions(getDriver());
		builder.moveToElement(scrollBar);
		builder.clickAndHold();
		// builder.sendKeys(Keys.DOWN);
		builder.moveByOffset(scrollByOffset, 0);
		builder.release();
		builder.build().perform();

		return flag;
	}

	// ####################### OBJECT OPERATION METHODS
	// ###################################################################

	public boolean switchWindowUsingWindowCount(int waitTimer, int windowNum) {
		boolean flag = false;
		try {
			waitUntillGivenWindowPresent(waitTimer, windowNum);
			ArrayList<String> windowHandles = new ArrayList<String>(getDriver().getWindowHandles());
			GenericUtils.logMessage("Window Size :" + windowHandles.size());
			getDriver().switchTo().window(windowHandles.get(windowNum - 1).toString());
			flag = true;
			GenericUtils.logMessage(" switched To WindowCount :" + windowNum);
		} catch (Exception e) {
			GenericUtils.logMessage(" Not able to Switch To window count:" + windowNum);
			flag = false;
		}
		return flag;
	}

	public boolean isFileDownloaded(String dirPath, String fileName, String ext) {
		boolean flag = false;
		File dir = new File(dirPath);
		File[] files = dir.listFiles();
		if (files == null || files.length == 0) {
			flag = false;
			GenericUtils.logMessage("No File downloaded");
		}

		for (int i = 0; i < files.length; i++) {

			if (files[i].getName().contains(ext) && files[i].getName().equals(fileName)) {
				flag = true;
				GenericUtils.logMessage("File Downloaded");
			}
		}
		return flag;
	}

	public File getLatestFilefromDir(String dirPath) {
		File dir = new File(dirPath);
		File[] files = dir.listFiles();
		if (files == null || files.length == 0) {
			return null;
		}

		File lastModifiedFile = files[0];
		for (int i = 1; i < files.length; i++) {
			if (lastModifiedFile.lastModified() < files[i].lastModified()) {
				lastModifiedFile = files[i];
			}
		}
		return lastModifiedFile;
	}

	public WebElementFacade getElement(String locatorType, String locatorValue, int TimeoutinSeconds)
			throws ElementNotFoundException {

		WebElementFacade element = null;
		try {

			switch (locatorType) {
			case "NAME":

				element = find(By.name(locatorValue)).withTimeoutOf(TimeoutinSeconds, TimeUnit.SECONDS)
				.waitUntilVisible();

				break;
			case "XPATH":
				element = find(By.xpath(locatorValue)).withTimeoutOf(TimeoutinSeconds, TimeUnit.SECONDS)
				.waitUntilVisible();

				break;
			case "ID":
				element = find(By.id(locatorValue)).withTimeoutOf(TimeoutinSeconds, TimeUnit.SECONDS)
				.waitUntilVisible();

				break;
			case "CSS":
				element = find(By.cssSelector(locatorValue)).withTimeoutOf(TimeoutinSeconds, TimeUnit.SECONDS)
				.waitUntilVisible();

				break;
			case "CLASS":
				element = find(By.className(locatorValue)).withTimeoutOf(TimeoutinSeconds, TimeUnit.SECONDS)
				.waitUntilVisible();

				break;
			case "LINK_TEXT":
				element = find(By.linkText(locatorValue)).withTimeoutOf(TimeoutinSeconds, TimeUnit.SECONDS)
				.waitUntilVisible();

				break;
			case "PARTIAL_LINK_TEXT":
				element = find(By.partialLinkText(locatorValue)).withTimeoutOf(TimeoutinSeconds, TimeUnit.SECONDS)
				.waitUntilVisible();

				break;
			case "TAG_NAME":
				element = find(By.tagName(locatorValue)).withTimeoutOf(TimeoutinSeconds, TimeUnit.SECONDS)
				.waitUntilVisible();

				break;
			default:
				GenericUtils.logMessage("Invalid Locator Choice of Type :" + locatorType);

				break;
			}

		} catch (Exception e) {
			GenericUtils.logMessage(
					"Elements not present:  of Locator Type :" + locatorType + "   of Value :  " + locatorValue);
			throw new ElementNotFoundException("No Locator found for : " + locatorValue);
		}

		return element;
	}

	public WebElementFacade getElement(String locatorType, String locatorValue) {

		WebElementFacade element = null;

		switch (locatorType) {
		case "NAME":
			element = find(By.name(locatorValue));
			break;
		case "XPATH":
			element = find(By.xpath(locatorValue));
			break;
		case "ID":
			element = find(By.id(locatorValue));
			break;
		case "CSS":
			element = find(By.cssSelector(locatorValue));
			break;
		case "CLASS":
			element = find(By.className(locatorValue));
			break;
		case "LINK_TEXT":
			element = find(By.linkText(locatorValue));
			break;
		case "PARTIAL_LINK_TEXT":
			element = find(By.partialLinkText(locatorValue));
			break;
		case "TAG_NAME":
			element = find(By.tagName(locatorValue));
			break;
		default:
			GenericUtils.logMessage("Invalid Locator Choice of Type :" + locatorType);
			break;
		}
		return element;
	}

	public List<WebElementFacade> getElements(String locatorType, String locatorValue) throws ElementNotFoundException {

		List<WebElementFacade> elements = null;
		try {

			switch (locatorType) {
			case "NAME":

				elements = findAll(By.name(locatorValue));

				break;
			case "XPATH":
				elements = findAll(By.xpath(locatorValue));

				break;
			case "ID":
				elements = findAll(By.id(locatorValue));

				break;
			case "CSS":
				elements = findAll(By.cssSelector(locatorValue));

				break;
			case "CLASS":
				elements = findAll(By.className(locatorValue));

				break;
			case "LINK_TEXT":
				elements = findAll(By.linkText(locatorValue));

				break;
			case "PARTIAL_LINK_TEXT":
				elements = findAll(By.partialLinkText(locatorValue));

				break;
			case "TAG_NAME":
				elements = findAll(By.tagName(locatorValue));

				break;
			default:

				break;
			}

		} catch (Exception e) {
			GenericUtils.logMessage(
					"Elements not present:  of Locator Type :" + locatorType + "   of Value :  " + locatorValue);
			throw new ElementNotFoundException("No Locator found for : " + locatorValue);
		}

		return elements;
	}

	public List<WebElementFacade> getElements(String locatorType, String locatorValue, int TimeoutinSeconds)
			throws ElementNotFoundException {

		List<WebElementFacade> elements = null;
		try {

			switch (locatorType) {
			case "NAME":

				elements = withTimeoutOf(TimeoutinSeconds, TimeUnit.SECONDS).findAll(By.name(locatorValue));

				break;
			case "XPATH":
				elements = findAll(By.xpath(locatorValue));

				break;
			case "ID":
				elements = findAll(By.id(locatorValue));

				break;
			case "CSS":
				elements = findAll(By.cssSelector(locatorValue));

				break;
			case "CLASS":
				elements = findAll(By.className(locatorValue));

				break;
			case "LINK_TEXT":
				elements = findAll(By.linkText(locatorValue));

				break;
			case "PARTIAL_LINK_TEXT":
				elements = findAll(By.partialLinkText(locatorValue));

				break;
			case "TAG_NAME":
				elements = findAll(By.tagName(locatorValue));

				break;
			default:
				GenericUtils.logMessage("Invalid Locator Choice of Type :" + locatorType);

				break;
			}

		} catch (Exception e) {
			GenericUtils.logMessage(
					"Elements not present:  of Locator Type :" + locatorType + "   of Value :  " + locatorValue);
			throw new ElementNotFoundException("No Locator found for : " + locatorValue);
		}

		return elements;
	}

	public WebElementState getWebElementState(String locatorType, String locatorValue) throws ElementNotFoundException {
		GenericUtils.logMessage("Returned State of Element :" + locatorType + "   of Value :  " + locatorValue);
		return element(getElement(locatorType, locatorValue));
	}

	public String[] getTextFromWebElements(List<WebElementFacade> elements) {

		String[] Text = new String[elements.size()];
		for (int i = 0; i < elements.size(); i++) {

			Text[i] = elements.get(i).getText();
			GenericUtils.logMessage("Element text Added to list is :" + Text[i]);

		}
		return Text;
	}


	public String getTextFromWebElement(WebElement element) {

		String text = "";
		try{
			text = "";
			text = element.getText();  
		}
		catch (Exception ignored) {
			GenericUtils.logMessage("Element :" + element.toString() + " text not retrieved from element");		
		}

		return text;
	}



	// ####################### OBJECT ACTIONS METHODS
	// ###################################################################

	public boolean clickGivenElement(WebElementFacade element) {
		boolean flag = false;


		element.click();

		flag = true;

		return flag;
	}

	public boolean enterTextToElement(WebElementFacade element, String text) {

		boolean flag = false;
		try {
			element.clear();
			element.sendKeys(text);

			flag = true;
		} catch (Exception ignored) {
			GenericUtils.logMessage("Element :" + element.toString() + " Not Enterd text");
			flag = false;
		}
		return flag;
	}

	public boolean enterTextinCotrol(String sLocatorType , String sLocatorValue, String sTextToEnter) {

		boolean flag = false;
		try {	
			switch (sLocatorType)	
			{
			case  "XPATH":
				getDriver().findElement(By.xpath(sLocatorValue)).clear();
				getDriver().findElement(By.xpath(sLocatorValue)).sendKeys(sTextToEnter);	
				flag = true;
				break;		
			}	

		} catch (Exception e) {
			GenericUtils.logMessage(e.getMessage());
			flag = false;
		}
		return flag;
	}

	public boolean moveToElementAndClick(WebElementFacade element) {

		boolean flag = false;
		try {
			withAction().moveToElement(element).click().perform();

			flag = true;
		} catch (Exception ignored) {
			GenericUtils.logMessage("Element :" + element.toString() + " Not Clicked");
			flag = false;
		}
		return flag;
	}

	public boolean rightClickOnElement(WebElementFacade element) {

		boolean flag = false;
		try {
			withAction().contextClick(element).click().perform();

			flag = true;
		} catch (Exception ignored) {
			GenericUtils.logMessage("Element :" + element.toString() + " Not Clicked");
			flag = false;
		}
		return flag;

	}

	public boolean clickAndHoldElement(WebElementFacade element) {

		boolean flag = false;
		try {
			withAction().clickAndHold(element).click().perform();

			flag = true;
		} catch (Exception ignored) {
			GenericUtils.logMessage("Element :" + element.toString() + " Not able to Click and Hold");
			flag = false;
		}
		return flag;
	}

	public boolean dragAndDropByElement(WebElementFacade source, WebElementFacade target) {

		boolean flag = false;
		try {
			withAction().dragAndDrop(source, target).click().perform();

			flag = true;
		} catch (Exception ignored) {
			GenericUtils.logMessage("Not able to dragAndDrop given  :" + source + target);
			flag = false;
		}
		return flag;
	}

	public boolean SelectDropDownByVisibleText(WebElementFacade element, String text) {

		boolean flag = false;
		try {
			element.selectByVisibleText(text);

			flag = true;
		} catch (Exception ignored) {
			GenericUtils.logMessage("Not able to selectByVisibleText  :" + element + "::" + text);
			flag = false;
		}
		return flag;
	}

	public boolean SelectDropDownByValue(WebElementFacade element, String value) {

		boolean flag = false;
		try {
			element.selectByValue(value);

			flag = true;
		} catch (Exception ignored) {
			GenericUtils.logMessage("Not able to SelectDropDownByValue  :" + element + "::" + value);
			flag = false;
		}
		return flag;

	}

	public boolean SelectDropDownByIndex(WebElementFacade element, int indexValue) {
		boolean flag = false;
		try {
			element.selectByIndex(indexValue);

			flag = true;
		} catch (Exception ignored) {
			GenericUtils.logMessage("Not able to SelectDropDownByIndex  :" + element + "::" + indexValue);
			flag = false;
		}
		return flag;

	}

	public boolean UploadFile(WebElementFacade element, String filepath) {
		boolean flag = false;
		try {
			upload(filepath).to(element);
			withAction().clickAndHold(element).click().perform();

			flag = true;
		} catch (Exception ignored) {
			GenericUtils.logMessage("Not able to UploadFile  :" + element + "::" + filepath);
			flag = false;
		}
		return flag;

	}

	public boolean clickOnGivenElemntByJavaScript(WebElementFacade element) {
		boolean flag = false;
		try {
			evaluateJavascript("arguments[0].click();", element);
			flag = true;
		} catch (Exception ignore) {
			GenericUtils.logMessage("Element :" + element.toString() + " Not Clicked");
			flag = false;
		}
		return flag;

	}
	
	public boolean clickOnGivenElemntByJavaScript(String Xpath) {
		boolean flag = false;
		try {
			WebElement element = getDriver().findElement(By.xpath(Xpath));
			JavascriptExecutor executor = (JavascriptExecutor)getDriver();
			executor.executeScript("arguments[0].click();", element);
			flag = true;
		}catch (Exception ignore) {
			GenericUtils.logMessage("Element :" + Xpath + " Not Clicked");
			flag = false;
		}
		return flag;
		}
	
	public boolean moveToElement(WebElementFacade element) {
		boolean flag = false;
		try {
			withAction().moveToElement(element).build().perform();
			flag = true;
		} catch (Exception e) {
			GenericUtils.logMessage("Element :" + element.toString() + " Not displayed");
			flag = false;
		}
		return flag;
	}

	public boolean moveToElement(WebElement element) {
		boolean flag = false;
		try {
			withAction().moveToElement(element).build().perform();
			flag = true;
		} catch (Exception e) {
			GenericUtils.logMessage("Element :" + element.toString() + " Not displayed");
			flag = false;
		}
		return flag;
	}

	public boolean navigateBack() {
		boolean flag = false;
		try {
			getDriver().navigate().back();
			flag = true;
		} catch (Exception e) {
			GenericUtils.logMessage("Failed to navigate back");
			flag = false;
		}
		return flag;
	}

	// ####################### OBJECT VALIDATORS METHODS
	// ###################################################################

	public boolean isElementDisplayed(WebElementFacade element) {
		return element.isDisplayed();

	}

	public boolean isElementCurrentlyVisible(WebElementFacade element) {

		return element.isCurrentlyVisible();
	}

	public boolean isElementPresent(WebElementFacade element, String arg1) {
		if (element.isPresent()) {
			GenericUtils.logMessage("Element \"" + arg1 + "\" is Present");
			return true;
		} else {
			GenericUtils.logMessage("Element \"" + arg1 + "\" is Not Present");
			return false;
		}
	}

	public boolean isElementEnabled(WebElementFacade element) {
		try {
			return element.isEnabled();

		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;

	}

	public boolean isElementSelected(WebElementFacade element) {

		return element.isSelected();
	}

	public boolean isElementCurrentlyEnabled(WebElementFacade element) {

		return element.isCurrentlyEnabled();
	}

	public boolean isElementHasFocus(WebElementFacade element) {

		return element.hasFocus();
	}

	// added for CPW
	public boolean isElemPresent(String webElName) {
		List<WebElement> appButtons = new ArrayList<WebElement>();
		try {
			appButtons = getDriver().findElements(org.openqa.selenium.By.xpath(webElName));
			if (appButtons.size() > 0) {
				return true;
			} else {
				return false;
			}
		} catch (Exception e) {
			return false;
		}
	}

	public boolean isElemPresent(WebElementFacade webElName) {
		List<WebElement> appButtons = new ArrayList<WebElement>();
		try {
			appButtons = getDriver().findElements((org.openqa.selenium.By) webElName);
			if (appButtons.size() > 0) {
				return true;
			} else {
				return false;
			}
		} catch (Exception e) {
			return false;
		}
	}

	// ####################### OBJECT VALIDATION METHODS
	// ###################################################################

	public void elementShouldBeVisible(WebElementFacade element) {

		element.shouldBeVisible();
		GenericUtils.logMessage("Element  :" + element.getText() + "- is visible on the Page");
	}

	public void elementShouldBePresent(WebElementFacade element) {

		GenericUtils.logMessage("Element  :" + element.getText() + " - is present on the Page");
		element.shouldBePresent();
	}

	public void elementShouldBeEnabled(WebElementFacade element) {
		GenericUtils.logMessage("Element   :" + element.getText() + "- is Enabled on the Page");
		element.shouldBeEnabled();
	}

	// ############################################################################################################################

	public boolean openApplication(String sURL) {

		try {
			getDriver().navigate().to(sURL);
			GenericUtils.logMessage("Page is Opened Successfully");
			return true;
		} catch (Exception e) {
			GenericUtils.logMessage("Page is not Opened");
			return false;
		}

	}

	public boolean closeCurrentWindow() {

		try {
			getDriver().close();
			return true;
		} catch (Exception ignore) {
			return false;
		}

	}

	public boolean isElementNotPresent(String locatorType, String locatorValue, int TimeoutinSeconds)

			throws ElementNotFoundException {
		try {
			getElement(locatorType, locatorValue, TimeoutinSeconds);
			return false;
		} catch (ElementNotFoundException ignore) {
			return true;
		}

	}

	public String getTextFromWebElement(WebElementFacade element) {

		String text = element.getText().trim();
		GenericUtils.logMessage("Element text retrieved:" + text);

		return text;
	}

	public boolean isElementHyperLink(WebElementFacade element) {
		boolean flag = false;
		if (element.getAttribute("onClick") != null) {
			flag = true;
		}
		return flag;
	}

	public String[] get_All_Text_from_Locator(String Xpath) {
		List<WebElement> elements = getDriver().findElements(By.xpath(Xpath));
		String[] text = new String[elements.size()];
		for (int i = 0; i < elements.size(); i++) {

			text[i] = elements.get(i).getText();
			System.out.println(text[i]);
		}
		return text;
	}

	public String[] get_All_Text_from_Locator_Without_Null_Values(String Xpath) {
		List<WebElement> elements = getDriver().findElements(By.xpath(Xpath));
		String[] text = new String[elements.size()];
		for (int i = 0; i < elements.size(); i++) {
			text[i] = elements.get(i).getText();
			System.out.println(text[i]);
		}
		return Arrays.stream(text).filter(value -> value != null && value.length() > 0).toArray(size -> new String[size]);
	}
	// ############################################## chaitanya
	// ######################################################
	public int get_Matching_WebElement_count(String xpath) {
		return getDriver().findElements(By.xpath(xpath)).size();
	}

	public boolean is_WebElement_Visible(String Xpath) {
		try {
			return $(Xpath).withTimeoutOf(ProjectVariables.TImeout_5_Seconds, TimeUnit.SECONDS).isVisible();
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	public boolean is_WebElement_enabled(String Xpath) {
		try {


			return $(Xpath).withTimeoutOf(ProjectVariables.TImeout_5_Seconds, ChronoUnit.SECONDS).isEnabled();
		} catch (Exception e) {
			e.printStackTrace();

			System.out.println("Given element not in enabled mode,Xpath==>"+Xpath+",Exception=>"+e);
			return false;
		}
	}

	public boolean is_WebElement_Displayed(String Xpath) 
	{
		boolean bstatus=false;
		try {
			//highlightElement(Xpath);
			//removehighlightElement(Xpath);
			bstatus=$(Xpath).withTimeoutOf(ProjectVariables.TImeout_5_Seconds, ChronoUnit.SECONDS).isDisplayed();
			//System.out.println("webelement '"+Xpath+"' displayed staus==>"+bstatus);
			return bstatus;
		} catch (Exception e) 
		{
			//System.out.println("webelement '"+Xpath+"' displayed staus==>"+bstatus+",Exception=>"+e);
			return false;
		}
	}

	public String get_TextFrom_Locator(String Xpath) {
		try {
			//highlightElement(Xpath);
			//removehighlightElement(Xpath);

			return  $(Xpath).withTimeoutOf(ProjectVariables.TImeout_5_Seconds, ChronoUnit.SECONDS).waitUntilVisible()
					.getTextValue();
		} catch (Exception e) {

			Assert.assertTrue("Unable to retrieve the text from the given xpath ==>"+Xpath+",Exception==>"+e, false);

			return null;
		}
	}

	public void Click_given_Locator(String xpath) {
		// highlightElement(xpath);
		$(xpath).withTimeoutOf(ProjectVariables.TImeout_5_Seconds, ChronoUnit.SECONDS).waitUntilVisible().click();
	}

	public String Get_Value_By_given_attribute(String attribute, String Xpath) {
		try {
			String value = $(Xpath).withTimeoutOf(ProjectVariables.TImeout_5_Seconds, TimeUnit.SECONDS)
					.getAttribute(attribute);

			return value;
		} catch (Exception e) {
			return null;
		}
	}

	public String getWebElementAttributeVal(String attribute, WebElement webl) {
		try {
			String value = $(webl).withTimeoutOf(ProjectVariables.TImeout_5_Seconds, TimeUnit.SECONDS)
					.getAttribute(attribute);
			return value;
		} catch (Exception e) {
			return null;
		}
	}

	public String getElementAttributeValue(String attribute, WebElement  elmnt) {

		try {
			String value = $(elmnt).withTimeoutOf(ProjectVariables.TImeout_5_Seconds, TimeUnit.SECONDS)
					.getAttribute(attribute);
			return value;

		} catch (Exception e) {
			return null;
		}
	}

	public boolean highlightElement(WebElementFacade element) {
		boolean flag = false;

		try {

			for (int i = 0; i < ProjectVariables.HIGHLIGHT_COUNT; i++) {
				evaluateJavascript("arguments[0].style.border='2px orange'", element);
				evaluateJavascript("arguments[0].style.border=''", element);
			}

		} catch (Exception e) {
			return flag;
		}

		return true;

	}

	public boolean clickGivenXpath(String xpath) {
		boolean flag =false;
		try{

			$(xpath).withTimeoutOf(ProjectVariables.TImeout_5_Seconds, ChronoUnit.SECONDS).waitUntilVisible().click();

			flag =true;
			System.out.println("Element Clicked=>"+xpath);
		}
		catch(Exception ignored){

			Assert.assertTrue("Element Not Clicked,xpath==>"+xpath+",Exception ==>"+ignored,false);

			flag =false;
		}
		return flag;
	}

	@SuppressWarnings("deprecation")
	public boolean Enter_given_Text_Element(String xpath, String text) {

		try {
			$(xpath).withTimeoutOf(ProjectVariables.TImeout_5_Seconds, TimeUnit.SECONDS).waitUntilVisible().clear();
			$(xpath).withTimeoutOf(ProjectVariables.TImeout_5_Seconds, TimeUnit.SECONDS).waitUntilVisible().sendKeys(text);
			return true;
		}
		catch (Exception e) {
			System.out.println(text+" was unable to enter in the given Xpath:"+xpath);
			Assert.assertTrue("'"+text+"' text was unable to enter in the given Xpath:"+xpath,false);
			return false;
		}
	}




	public int getCountOfCharInAString(String s, char c) 
	{ 
		int res = 0; 

		for (int i=0; i<s.length(); i++) 
		{ 
			// checking character in string 
			if (s.charAt(i) == c) 
				res++; 
		}  
		return res; 
	} 

	public static void scrollingToGivenElement(WebDriver driver,String string) {

		WebElement element = driver.findElement(By.xpath(string));
		((JavascriptExecutor) driver).executeScript(
				"arguments[0].scrollIntoView(true);", element);
	}


	public boolean Wait_Untill_Element_is_displayed(int waitcount,String Locator) 
	{			
		int scount=0;
		do
		{
			defaultWait(ProjectVariables.TImeout_1_Seconds);
			//System.out.println("scount:"+scount);
			scount=scount+1;
			//System.out.println(!(is_WebElement_Displayed( Locator)||(scount==waitcount)));
		}while(!(is_WebElement_Displayed( Locator)||(scount==waitcount)));

		if (is_WebElement_Displayed(Locator) == false) 
		{
			System.out.println(Locator+ " is not displayed in UI");

			//Assert.assertTrue(Locator+ " is not displayed in UI", false);
		}
		return is_WebElement_Displayed( Locator);
	}

	/*
		public static void ReloadBrowser()
	  	{
			 // WebDriver driver = getDriver();		
		   	WebDriver driver = PageObject.getDriver();		  		
	  	}
	 */

	public boolean moveToElementAndClick(String xpath) {

		boolean flag = false;
		try {
			withAction().moveToElement(findBy(xpath)).click().build().perform();

			flag = true;
		} catch (Exception ignored) {
			GenericUtils.logMessage("Element :" + xpath.toString() + " Not Clicked");
			flag = false;
		}
		return flag;
	}

	public boolean is_WebElement_NotDisplayed(String Xpath) {
		try {
			boolean blnFlag = false;
			int iTimes =0;
			do {
				boolean sDisplayed = is_WebElement_Displayed(Xpath);
				if(!sDisplayed){
					return true;
				}
				iTimes = iTimes+1;
			}while(iTimes<60);
			return blnFlag;
		} catch (Exception e) {
			return false;
		}
	}

	
	public boolean DynamicWaitfortheLoadingIconWithCount(int count) {
		boolean bstatus=false;
		int j = 1;
		System.out.println("Loading is in progress");
		
		do {
			defaultWait(ProjectVariables.TImeout_2_Seconds);
			j = j + 1;
			
			if (j == count) {
				break;
			}
			
			bstatus=is_WebElement_Displayed("//p[contains(text(),'Loading')]");
			
		} while (bstatus);
		
		
		

		return bstatus;
		
	}
	

	public boolean is_WebElement_Selected(String Xpath) {
		try {
			//highlightElement(Xpath);
			//removehighlightElement(Xpath);
			return $(Xpath).withTimeoutOf(ProjectVariables.TImeout_2_Seconds, TimeUnit.SECONDS).isSelected();
		} catch (Exception e) {
			return false;
		}
	}

	public void Doubleclick(String xpath)
	{
		Actions action = new Actions(getDriver());
		action.moveToElement(getDriver().findElement(By.xpath(xpath))).doubleClick().build().perform();
	}

    
	public boolean gfn_Verify_Object_Exist(String Desription,String sTagName, String sText) throws InterruptedException {

		boolean blnFlag = false;
		int iTimer = 0;
		String strXpath = "//" + sTagName + "[text()='" + sText + "']";
		
		System.out.println("Locator   "+strXpath);

		try {
			do {

				List<WebElement> sList = getDriver().findElements(By.xpath(strXpath));

				if (sList.size() > 0) {
					for (int i = 0; i < sList.size(); i++) {
						if (sList.get(i).isDisplayed()) {
							highlightElement(sList.get(i));
							blnFlag = true;
							GenericUtils.Verify(Desription, blnFlag);
							break;
						}
					}
				}

				if (!blnFlag) {
					Thread.sleep(ProjectVariables.TImeout_2_Seconds);
				}
				iTimer = iTimer + 1;

			} while ((blnFlag != true) && (iTimer != ProjectVariables.TimerCount));

			if (blnFlag != true) {
				blnFlag = false;
				GenericUtils.Verify(Desription+"Element Not Found"+"Locator Used:"+strXpath, false);
			}

		} catch (Exception e) {
			System.err.println(e);
		}

		return blnFlag;

	}

	public boolean gfn_Click_On_Object(String Desription,String sTagName, String sText) throws InterruptedException {

		boolean blnFlag = false;
		int iTimer = 0;
		String strXpath = "//" + sTagName + "[text()= '" + sText + "']";

		try {
			do {

				List<WebElement> sList = getDriver().findElements(By.xpath(strXpath));

				if (sList.size() > 0) {
					for (int i = 0; i < sList.size(); i++) {
						if (sList.get(i).isDisplayed() && sList.get(i).isEnabled()) {
							highlightElement(sList.get(i));
							sList.get(i).click();
							blnFlag = true;
							GenericUtils.Verify(Desription, blnFlag);
							break;
						}
					}
				}

				if (!blnFlag) {
					Thread.sleep(ProjectVariables.TImeout_2_Seconds);
				}
				iTimer = iTimer + 1;
			} while ((blnFlag != true) && (iTimer != ProjectVariables.TimerCount));

			if (blnFlag != true) {
				blnFlag = false;
				GenericUtils.Verify(Desription+"Element Not Found"+"Locator Used:"+strXpath, false);
			}

		} catch (Exception e) {
			System.err.println(e);

		}

		return blnFlag;

	}

	public boolean gfn_Click_String_object_Xpath(String Desription,String sXpath) {

		boolean blnFlag = false;
		int iTimer = 0;
		try {
			do {

				List<WebElement> sList = getDriver().findElements(By.xpath(sXpath));

				if (sList.size() > 0) {
					for (int i = 0; i < sList.size(); i++) {
						if (sList.get(i).isDisplayed()) {
							highlightElement(sList.get(i));
							sList.get(i).click();
							blnFlag = true;
							GenericUtils.Verify(Desription, blnFlag);
							break;
						}
					}
				}

				if (!blnFlag) {
					Thread.sleep(ProjectVariables.TImeout_5_Seconds);
				}
				iTimer = iTimer + 1;

			} while ((blnFlag != true) && (iTimer != ProjectVariables.TimerCount));

			if (blnFlag != true) {
				GenericUtils.Verify(Desription+"Element Not Found"+"Locator Used:"+sXpath, false);
				blnFlag = false;
			}

		} catch (Exception e) {
			System.err.println(e);

		}

		return blnFlag;

	}

	public boolean gfn_Verify_String_Object_Exist(String Desription,String strXpath) throws InterruptedException {

		boolean blnFlag = false;
		int iTimer = 0;
		// String strXpath = "//"+sTagName+"[text()='"+sText+"']";

		try {
			do {

				List<WebElement> sList = getDriver().findElements(By.xpath(strXpath));

				if (sList.size() > 0) {
					for (int i = 0; i < sList.size(); i++) {
						if (sList.get(i).isDisplayed()) {
							highlightElement(sList.get(i));
							blnFlag = true;
							GenericUtils.Verify(Desription, blnFlag);
							break;
						}
					}
				}

				if (!blnFlag) {
					Thread.sleep(ProjectVariables.TImeout_2_Seconds);
				}
				iTimer = iTimer + 1;

			} while ((blnFlag != true) && (iTimer != ProjectVariables.TimerCount));

			if (blnFlag != true) {
				blnFlag = false;
				GenericUtils.Verify(Desription+"Element Not Found"+"Locator Used:"+strXpath, false);
			}

		} catch (Exception e) {
			System.err.println(e);

		}

		return blnFlag;

	}

	

	public boolean is_WebElement_Enabled(String Xpath) {
		try {
			//highlightElement_CheckBox(Xpath);
			return $(Xpath).withTimeoutOf(ProjectVariables.TImeout_2_Seconds, TimeUnit.MILLISECONDS).isEnabled();
		} catch (Exception e) {
			return false;
		}
	}

}
