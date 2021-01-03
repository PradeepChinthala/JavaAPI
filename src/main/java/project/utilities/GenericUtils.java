
package project.utilities;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Properties;
import java.util.Random;
import java.util.Set;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.sql.Timestamp;
import org.apache.commons.lang3.StringUtils;

import org.json.simple.JSONObject;
import org.junit.Assert;
import org.openqa.selenium.By;
import org.openqa.selenium.ElementNotVisibleException;
import org.openqa.selenium.InvalidElementStateException;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.WebElement;

import net.serenitybdd.core.Serenity;
import project.variables.ProjectVariables;

public class GenericUtils extends SeleniumUtils {



	static org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(GenericUtils.class);

	// ####################### GENERIC METHODS
	// ###################################################################

	// ####################################################################################################

	public static String getDateGivenFormat() {
		// String element = DA_PROJ_OR.LAST_SEARCH_TIME;
		String sExpectedTime = new SimpleDateFormat("MM/dd/yyyy").format(Calendar.getInstance().getTime());
		String[] words = sExpectedTime.split("\\s");
		System.out.println("System Date-->:- " + words[0]);

		String sExpectedDate = words[0];

		return sExpectedDate;
	}

	public static String ConvertEpochtoDate(String sData) {

		Date date = new Date(Long.valueOf(sData));
		DateFormat format = new SimpleDateFormat("MM/dd/yyyy");
		format.setTimeZone(TimeZone.getTimeZone("GMT"));
		String formatted = format.format(date);
		System.out.println(formatted);

		return formatted;
	}

	public static String getAddedDate(String sDate) {

		// Given Date in String format
		String oldDate = sDate;
		System.out.println("Date before Addition: " + oldDate);
		// Specifying date format that matches the given date
		SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
		sdf.setTimeZone(TimeZone.getTimeZone("GMT"));
		Calendar c = Calendar.getInstance();
		try {
			// Setting the date to the given date
			c.setTime(sdf.parse(oldDate));
		} catch (ParseException e) {
			e.printStackTrace();
		}

		// Number of Days to add
		c.add(Calendar.DAY_OF_MONTH, 7);
		// Date after adding the days to the given date
		String newDate = sdf.format(c.getTime());
		// Displaying the new Date after addition of Days
		System.out.println("Date after Addition: " + newDate);

		return newDate;

	}

	public static String getDateGivenFormat(String format) {
		// String element = DA_PROJ_OR.LAST_SEARCH_TIME;
		/*String sExpectedTime = new SimpleDateFormat(format).format(Calendar.getInstance().getTime());
		String[] words = sExpectedTime.split("\\s");
		System.out.println("System Date-->:- " + words[0]);

		String sExpectedDate = words[0];
*/
		
		DateFormat dateFormat = new SimpleDateFormat(format);
		Date date = new Date();
		//System.out.println(dateFormat.format(date)); //2016/11/16 12:08:43
		
		return dateFormat.format(date);
	}

	// ####################################################################################################

	public static int generateRandomNumberforGivenRange(int range) {
		Random rand = new Random();
		int value = rand.nextInt(range);
		return value;
	}

	// ####################################################################################################

	public static int GetRandomNumber() {
		Random r = new Random(System.currentTimeMillis());
		return ((1 + r.nextInt(2)) * 10000 + r.nextInt(10000));
	}

	// ####################################################################################################

	public static String decode(String value) throws Exception {
		byte[] decodedValue = null;
		try {
			decodedValue = Base64.getDecoder().decode(value);
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		return new String(decodedValue, StandardCharsets.UTF_8);
	}

	// ####################################################################################################

	public static void logMessage(String message) {
		logger.info(message);
	}

	public static void logErrorMesage(String message) {
		logger.error(message);
	}

	// ####################################################################################################

	public boolean CompareLists(List<String> a, List<String> b) {

		Collections.sort(a);
		Collections.sort(b);

		if (a.size() != b.size()) {
			return false;
		}

		for (int i = 0; i < a.size(); i++) {
			if (!a.get(i).equals(b.get(i))) {
				return false;
			}
		}

		return true;

	}

	public void defaultWait(long i) {
		try {

			Thread.sleep(TimeUnit.SECONDS.toMillis(i));
			GenericUtils.logMessage("Waited for " + i + " seconds");

		} catch (InterruptedException e) {

			e.printStackTrace();
		}
	}

	// ####################################################################################################

	public static String ChangeDateFormat(String Sdate) throws ParseException {
		SimpleDateFormat sdf = new SimpleDateFormat("M/d/yyyy");
		Date Nd = sdf.parse(Sdate);
		String temp = sdf.format(Nd);
		return temp;
	}

	public static String ChangeDateFormat(String Sdate, String sFormat) throws ParseException {
		SimpleDateFormat sdf = new SimpleDateFormat(sFormat);
		Date Nd = sdf.parse(Sdate);
		String temp = sdf.format(Nd);
		return temp;
	}

	// ####################################################################################################

	public void gfnWritedatatoPropertyfile(String sKey, String sValue, String sFile) throws IOException {

		try {
			File configFile = new File(sFile);
			FileInputStream in = new FileInputStream(configFile);
			Properties props = new Properties();
			props.load(in);
			in.close();

			FileOutputStream out = new FileOutputStream(configFile);
			props.setProperty(sKey, sValue);
			props.store(out, null);
			out.close();
		}

		catch (Exception e) {
			System.err.println(e);

		}

	}

	// ####################################################################################################

	public static String gfnReadDataFromPropertyfile(String sKey, String sFile) throws IOException {

		String value = null;

		try {
			File file = new File(sFile);
			FileInputStream fileInput;
			fileInput = new FileInputStream(file);
			Properties prop = new Properties();
			prop.load(fileInput);
			value = prop.getProperty(sKey);
			System.out.println(prop.getProperty(sKey));
		}

		catch (Exception e) {
			System.err.println(e);

		}
		return value;

	}

	// ####################################################################################################

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

	public static String trimQuotedString(String strToBeExtracted) {
		String bareStr = null;
		Pattern p = Pattern.compile("\"([^\"]*)\"");
		Matcher m = p.matcher(strToBeExtracted);
		while (m.find()) {
			bareStr = m.group(1);
		}

		Pattern p1 = Pattern.compile("\\[\"([^\"]*)\"\\]");
		Matcher m1 = p1.matcher(bareStr);
		while (m1.find()) {
			bareStr = m1.group(1);
		}

		Pattern p2 = Pattern.compile("\\[\"([^\"]*)\"");
		Matcher m2 = p2.matcher(bareStr);
		while (m2.find()) {
			bareStr = m2.group(1);
		}

		Pattern p3 = Pattern.compile("\"([^\"]*)\"\\]");
		Matcher m3 = p3.matcher(bareStr);
		while (m3.find()) {
			bareStr = m3.group(1);
		}
		// System.out.println(bareStr);
		return bareStr;
	}

	@SuppressWarnings({ "unchecked" })
	public static void SetDataAsJsonFile(String sTagName, String sFeaturename, String sTcName, String sTcStatus,
			String sDate, String sReasonCode, String sReason) throws Exception {
		// First Employee

		JSONObject ExecutionDetails = new JSONObject();

		ExecutionDetails.put("Tag Name", sTagName);
		ExecutionDetails.put("Feature Name", sFeaturename);
		ExecutionDetails.put("TC Name", sTcName);
		ExecutionDetails.put("TC Status", sTcStatus);
		ExecutionDetails.put("Execution Time", sDate);
		ExecutionDetails.put("Reason Code", sReasonCode);
		ExecutionDetails.put("Failure Reason", sReason);

		ProjectVariables.execResultList.add(ExecutionDetails);

		// Write JSON file
		FileWriter file = new FileWriter(System.getProperty("user.dir") + "\\Execution.json", true);
		try {
			file.write(ExecutionDetails.toString());
			file.write("\n");
			// for clearing json file
			// ExecutionDetails.clear();
		} catch (Exception e) {
			System.out.println("Failed due to:" + e);
		} finally {
			file.flush();
			file.close();
		}

	}

	public static List<List<String>> getUniqueValues(List<List<String>> queryValues) {
		List<List<String>> uniqueElemList = new ArrayList<List<String>>();
		Set<List<String>> uniqueSet = new HashSet<List<String>>();
		List<String> singleRec = new ArrayList<String>();

		for (int j = 0; j < queryValues.size(); j++) {
			GenericUtils.logMessage(queryValues.get(j).toString());
		}

		for (int i = 0; i < queryValues.size(); i++) {
			singleRec = queryValues.get(i);
			if (!uniqueSet.contains(singleRec)) {
				System.out.println(singleRec);
				uniqueSet.add(singleRec);
			}
		}

		uniqueElemList = new ArrayList<List<String>>(uniqueSet);

		return uniqueElemList;
	}

	public static List<String> getUniqueListValues(List<String> queryValues) {
		List<String> uniqueElemList = new ArrayList<String>();
		Set<String> uniqueSet = new HashSet<String>();

		for (String recdValue : queryValues) {
			uniqueSet.add(recdValue);
		}

		uniqueElemList = new ArrayList<String>(uniqueSet);

		return uniqueElemList;
	}

	public static String RetrivetheExactMonth(String reuquiredSubsequentRelease) {
		String month = reuquiredSubsequentRelease.substring(0, 3);
		String monthname = null;

		switch (month) {
		case "JAN":
			monthname = "01";
			break;
		case "FEB":
			monthname = "02";
			break;
		case "MAR":
			monthname = "03";
			break;
		case "APR":
			monthname = "04";
			break;
		case "MAY":
			monthname = "05";
			break;
		case "JUN":
			monthname = "06";
			break;
		case "JUL":
			monthname = "07";
			break;
		case "AUG":
			monthname = "08";
			break;
		case "SEP":
			monthname = "09";
			break;
		case "OCT":
			monthname = "10";
			break;
		case "NOV":
			monthname = "11";
			break;
		case "DEC":
			monthname = "12";
			break;

		}

		return monthname;
	}

	// ################################################################################################################################################################

	public boolean isElementExist(String sXpath) {
		boolean blnFlag = false;
		int iTimer = 0;
		try {
			do {
				try {

					List<WebElement> sList = getDriver().findElements(By.xpath(sXpath));

					if (sList.size() > 0) {
						for (int i = 0; i < sList.size(); i++) {
							if (sList.get(i) != null && sList.get(i).isDisplayed() && sList.get(i).isEnabled()) {
								// oSeleniumUtils.highlightElement(sList.get(i));
								blnFlag = true;
								Verify("Value displayed sucessfully:=" + sXpath, "PASSED");
								Verify("Value displayed sucessfully:=" + sXpath, true);
								break;
							}
						}
					}

					Thread.sleep(ProjectVariables.High_MIN_SLEEP);
					iTimer = iTimer + 1;

				} catch (StaleElementReferenceException e) {
					if (!blnFlag) {
						Thread.sleep(ProjectVariables.High_MIN_SLEEP);
					}
					iTimer = iTimer + 1;

				} catch (WebDriverException se) {
					if (!blnFlag) {
						Thread.sleep(ProjectVariables.High_MIN_SLEEP);
					}
					iTimer = iTimer + 1;
				}

			} while ((blnFlag != true) && (iTimer != 10));

			if (blnFlag != true) {
				Verify("Object not found:=" + sXpath, "FAILED");
				blnFlag = false;
			}

		} catch (Exception e) {
			// System.err.println(e);
			System.out.println("Object not found failed due to Execption" + e.getMessage());
			Verify("Object not found failed due to Execption" + e.getMessage(), "FAILED");

			blnFlag = false;
		}

		return blnFlag;

	}

	// ################################################################################################################################################################
	public static void Verify(String StepDetails, String sStatus) {

		if (sStatus.equalsIgnoreCase("PASSED")) {
			System.out.println(StepDetails);
			Serenity.recordReportData().withTitle(StepDetails).andContents(sStatus);
			Assert.assertTrue(StepDetails, true);
		} else {
			Serenity.recordReportData().withTitle(StepDetails).andContents(sStatus);
			Serenity.takeScreenshot();
			System.out.println(StepDetails);
			Assert.assertTrue(StepDetails, false);

		}
	}

	// ################################################################################################################################################################

	public static void Verify(String StepDetails, boolean sStatus) {

		if (sStatus) {
			System.out.println("Validation successful for "+StepDetails);
			//Serenity.recordReportData().withTitle(StepDetails).andContents("Passed");
			Assert.assertTrue(StepDetails, true);
		} else {
			System.out.println("Validation not successful for "+StepDetails);
			Serenity.recordReportData().withTitle(StepDetails).andContents("Failed");
			Serenity.takeScreenshot();
			Assert.assertTrue(StepDetails, false);

		}
	}

	// ################################################################################################################################################################

	public boolean setValue(By sObjectType, String sInputVal) {
		boolean blnResult = false;
		int iTimer = 0;

		try {
			do {
				List<WebElement> sTxtElement = getDriver().findElements(sObjectType);
				// if size greater than zero
				if (sTxtElement.size() > 0) {

					JavascriptExecutor js = (JavascriptExecutor) getDriver();
					js.executeScript("arguments[0].value = '';", getDriver().findElement(sObjectType));
					getDriver().findElement(sObjectType).clear();
					getDriver().findElement(sObjectType).sendKeys("");
					getDriver().findElement(sObjectType).sendKeys(sInputVal);
					Thread.sleep(1000);
					blnResult = true;
					Verify("Value enterted sucessfully:=" + sInputVal, "PASSED");

				}

				iTimer = iTimer + 1;
				Thread.sleep(1000);

			} while ((blnResult != true) && (iTimer != 60));

			if (blnResult != true) {
				Verify("Object not found:=" + sObjectType, "FAILED");
				return false;
			}
		} catch (Exception e) {
			Verify("Object not entered Successfully , Failed due to :=" + e.getMessage(), "FAILED");
			return false;
		}
		return blnResult;
	}

	// ################################################################################################################################################################

	public boolean clickButton(By sObjectType) {
		boolean blnFlag = false;
		int iTimer = 0;
		try {
			do {

				try {
					List<WebElement> sList = getDriver().findElements(sObjectType);

					if (sList.size() > 0) {
						for (int i = 0; i < sList.size(); i++) {
							if (sList.get(i) != null && sList.get(i).isDisplayed() && sList.get(i).isEnabled()) {
								//// oSeleniumUtils.highlightElement(sList.get(i));
								sList.get(i).click();
								blnFlag = true;
								break;
							}
						}
					}

					if (!blnFlag) {
						Thread.sleep(ProjectVariables.High_MIN_SLEEP);
					}
					iTimer = iTimer + 1;

				} catch (InvalidElementStateException Ie) {
					if (!blnFlag) {
						Thread.sleep(ProjectVariables.High_MIN_SLEEP);
					}
					iTimer = iTimer + 1;

				} catch (StaleElementReferenceException e) {
					if (!blnFlag) {
						Thread.sleep(ProjectVariables.High_MIN_SLEEP);
					}
					iTimer = iTimer + 1;

				} catch (WebDriverException se) {
					if (!blnFlag) {
						Thread.sleep(ProjectVariables.High_MIN_SLEEP);
					}
					iTimer = iTimer + 1;

				}

			} while ((blnFlag != true) && (iTimer != 10));

			if (blnFlag != true) {
			 Verify("Object not found:="+sObjectType,"FAILED");
				blnFlag = false;
			}

		} catch (Exception e) {
			System.err.println(e);
			getDriver().quit();
		}

		return blnFlag;

	}

	// ################################################################################################################################################################

	public boolean isElementExist(String sXpath, int Time) {
		boolean blnFlag = false;
		int iTimer = 0;
		try {
			do {
				try {

					List<WebElement> sList = getDriver().findElements(By.xpath(sXpath));

					if (sList.size() > 0) {
						for (int i = 0; i < sList.size(); i++) {
							if (sList.get(i) != null && sList.get(i).isDisplayed() && sList.get(i).isEnabled()) {
								// oSeleniumUtils.highlightElement(sList.get(i));
								blnFlag = true;
								Verify("Element displayed sucessfully:=" + sXpath, "PASSED");
								break;
							}
						}
					}

					Thread.sleep(ProjectVariables.High_MIN_SLEEP);
					iTimer = iTimer + 1;
					
				} catch (InvalidElementStateException Ie) {
					if (!blnFlag) {
						Thread.sleep(ProjectVariables.High_MIN_SLEEP);
					}
					iTimer = iTimer + 1;

				} catch (StaleElementReferenceException e) {
					if (!blnFlag) {
						Thread.sleep(ProjectVariables.High_MIN_SLEEP);
					}
					iTimer = iTimer + 1;

				} catch (WebDriverException se) {
					if (!blnFlag) {
						Thread.sleep(ProjectVariables.High_MIN_SLEEP);
					}
					iTimer = iTimer + 1;
				}

			} while ((blnFlag != true) && (iTimer != Time));

			if (blnFlag != true) {
				 Verify("Object not found:="+sXpath,"FAILED");
				blnFlag = false;
			}

		} catch (Exception e) {
			System.out.println("Object not found failed due to Execption" + e.getMessage());
			Verify("Object not found failed due to Execption" + e.getMessage(), "FAILED");
			blnFlag = false;
		}

		return blnFlag;

	}

	// ################################################################################################################################################################

	public boolean clickOnElement(String sXpath) {
		boolean blnFlag = false;
		int iTimer = 0;
		try {
			do {

				try {
					List<WebElement> sList = getDriver().findElements(By.xpath(sXpath));

					if (sList.size() > 0) {
						for (int i = 0; i < sList.size(); i++) {
							if (sList.get(i) != null && sList.get(i).isDisplayed() && sList.get(i).isEnabled()) {
								//// oSeleniumUtils.highlightElement(sList.get(i));
								sList.get(i).click();
								Verify("Link clicked sucessfully:=" + sXpath, "PASSED");
								blnFlag = true;
								break;
							}
						}
					}

					if (!blnFlag) {
						Thread.sleep(ProjectVariables.High_MIN_SLEEP);
					}
					iTimer = iTimer + 1;

				} catch (InvalidElementStateException Ie) {
					if (!blnFlag) {
						Thread.sleep(ProjectVariables.High_MIN_SLEEP);
					}
					iTimer = iTimer + 1;

				} catch (StaleElementReferenceException e) {
					if (!blnFlag) {
						Thread.sleep(ProjectVariables.High_MIN_SLEEP);
					}
					iTimer = iTimer + 1;

				} catch (WebDriverException se) {
					if (!blnFlag) {
						Thread.sleep(ProjectVariables.High_MIN_SLEEP);
					}
					iTimer = iTimer + 1;

				}

			} while ((blnFlag != true) && (iTimer != 10));

			if (blnFlag != true) {
				 Verify("Object not found:="+sXpath,"FAILED");
				blnFlag = false;
			}

		} catch (Exception e) {
			System.err.println(e);
			Verify("Object not found failed due to Execption" + e.getMessage(), "FAILED");
		}

		return blnFlag;

	}

	// ################################################################################################################################################################

	public boolean ClickAllElements(String sXpath) {
		boolean blnFlag = false;
		int iTimer = 0;
		try {
			do {

				try {
					List<WebElement> sList = getDriver().findElements(By.xpath(sXpath));

					if (sList.size() > 0) {
						for (int i = 0; i < sList.size(); i++) {
							if (sList.get(i) != null && sList.get(i).isDisplayed() && sList.get(i).isEnabled()) {
								//// oSeleniumUtils.highlightElement(sList.get(i));
								sList.get(i).click();
								Verify("Link clicked sucessfully:=" + sXpath, "PASSED");
								blnFlag = true;
							}
						}
					}

					if (!blnFlag) {
						Thread.sleep(ProjectVariables.High_MIN_SLEEP);
					}
					iTimer = iTimer + 1;

				} catch (InvalidElementStateException Ie) {
					if (!blnFlag) {
						Thread.sleep(ProjectVariables.High_MIN_SLEEP);
					}
					iTimer = iTimer + 1;

				} catch (StaleElementReferenceException e) {
					if (!blnFlag) {
						Thread.sleep(ProjectVariables.High_MIN_SLEEP);
					}
					iTimer = iTimer + 1;

				} catch (WebDriverException se) {
					if (!blnFlag) {
						Thread.sleep(ProjectVariables.High_MIN_SLEEP);
					}
					iTimer = iTimer + 1;

				}

			} while ((blnFlag != true) && (iTimer != 30));

			if (blnFlag != true) {
				// Verify("Object not found:="+sXpath,"FAILED");
				blnFlag = false;
			}

		} catch (Exception e) {
			System.err.println(e);
			Verify("Object not found failed due to Execption" + e.getMessage(), "FAILED");
		}

		return blnFlag;

	}

	// ################################################################################################################################################################

	public boolean IsElementExistWithContains(String sTagName, String sText) {
		boolean blnFlag = false;
		int iTimer = 0;
		String strXpath = "//" + sTagName + "[contains(text(),'" + sText + "')]";

		try {
			do {

				List<WebElement> sList = getDriver().findElements(By.xpath(strXpath));

				if (sList.size() > 0) {
					for (int i = 0; i < sList.size(); i++) {
						if (sList.get(i).isDisplayed()) {
							// oSeleniumUtils.highlightElement(sList.get(i));
							blnFlag = true;
							Verify("Link displayed sucessfully:=" + sText, "PASSED");
							break;
						}
					}
				}

				if (!blnFlag) {
					Thread.sleep(ProjectVariables.High_MIN_SLEEP);
				}
				iTimer = iTimer + 1;

			} while ((blnFlag != true) && (iTimer != 10));

			if (blnFlag != true) {
				System.out.println(sText + " object not found");
				blnFlag = false;
			}

			if (!blnFlag) {
				Assert.assertTrue("Object not found ==>" + strXpath, false);
			}

		} catch (Exception e) {
			System.err.println(e);
			Verify("Object not clicked Successfully , Failed due to :=" + e.getMessage(), "FAILED");
			getDriver().quit();

		}

		return blnFlag;

	}
	//###################################################################################################################################
	public boolean IsElementExistWithText(String strXpath, String sText) {
		boolean blnFlag = false;
		int iTimer = 0;
		

		try {
			do {

				List<WebElement> sList = getDriver().findElements(By.xpath(strXpath));

				if (sList.size() > 0) {
					for (int i = 0; i < sList.size(); i++) {
						if (sList.get(i).isDisplayed()) {
							// oSeleniumUtils.highlightElement(sList.get(i));
							String sGetText=sList.get(i).getText();
							if(sGetText.equalsIgnoreCase(sText)){
								blnFlag = true;
								Verify("Link displayed sucessfully:=" + sText, "PASSED");
								break;
							}
						}
					}
				}

				if (!blnFlag) {
					Thread.sleep(ProjectVariables.High_MIN_SLEEP);
				}
				iTimer = iTimer + 1;

			} while ((blnFlag != true) && (iTimer != 10));

			if (blnFlag != true) {
				System.out.println(sText + " object not found");
				blnFlag = false;
			}

			/*if (!blnFlag) {
				Assert.assertTrue("Object not found ==>" + strXpath, false);
			}*/

		} catch (Exception e) {
			System.err.println(e);
			Verify("Object not clicked Successfully , Failed due to :=" + e.getMessage(), "FAILED");
			getDriver().quit();

		}

		return blnFlag;

	}

	public boolean IsElementExistWithContains(String sTagName, String sText, int Time) {
		boolean blnFlag = false;
		int iTimer = 0;
		String strXpath = "//" + sTagName + "[contains(text(),'" + sText + "')]";

		setImplicitTimeout(1, ChronoUnit.SECONDS);

		try {
			do {

				List<WebElement> sList = getDriver().findElements(By.xpath(strXpath));

				if (sList.size() > 0) {
					for (int i = 0; i < sList.size(); i++) {
						if (sList.get(i).isDisplayed()) {
							// oSeleniumUtils.highlightElement(sList.get(i));
							blnFlag = true;
							Verify("Link displayed sucessfully:=" + sText, "PASSED");
							break;
						}
					}
				}

				if (!blnFlag) {
					Thread.sleep(ProjectVariables.High_MIN_SLEEP);
				}
				iTimer = iTimer + 1;

			} while ((blnFlag != true) && (iTimer != Time));

			if (blnFlag != true) {
				System.out.println(sText + " object not found");
				blnFlag = false;
			}

			if (!blnFlag) {
				Assert.assertTrue("Object not found ==>" + strXpath, false);
			}

		} catch (Exception e) {
			System.err.println(e);
			Verify("Object not clicked Successfully , Failed due to :=" + e.getMessage(), "FAILED");
			getDriver().quit();

		}

		finally {
			resetImplicitTimeout();
		}

		return blnFlag;

	}
	// ################################################################################################################################################################

	public boolean clickOnElement(String sTagName, String sText) {
		boolean blnFlag = false;
		int iTimer = 0;
		String strXpath = "//" + sTagName + "[text()= '" + sText + "']";

		try {
			do {
				try {
					List<WebElement> sList = getDriver().findElements(By.xpath(strXpath));

					if (sList.size() > 0) {
						for (int i = 0; i < sList.size(); i++) {
							if (sList.get(i).isDisplayed() && sList.get(i).isEnabled()) {
								//// oSeleniumUtils.highlightElement(sList.get(i));
								sList.get(i).click();
								blnFlag = true;
								Verify("Link clicked sucessfully:=" + sText, "PASSED");
								break;
							}
						}
					}

					if (!blnFlag) {
						Thread.sleep(ProjectVariables.High_MIN_SLEEP);
					}
					iTimer = iTimer + 1;

				} catch (InvalidElementStateException Ie) {
					if (!blnFlag) {
						Thread.sleep(ProjectVariables.High_MIN_SLEEP);
					}
					iTimer = iTimer + 1;

				} catch (StaleElementReferenceException e) {
					if (!blnFlag) {
						Thread.sleep(ProjectVariables.High_MIN_SLEEP);
					}
					iTimer = iTimer + 1;

				} catch (WebDriverException se) {
					if (!blnFlag) {
						Thread.sleep(ProjectVariables.High_MIN_SLEEP);
					}
					iTimer = iTimer + 1;

				}
			} while ((blnFlag != true) && (iTimer != 15));

			if (blnFlag != true) {
				Verify("Object not found:=" + strXpath, "FAILED");
				blnFlag = false;
			}

		} catch (Exception e) {
			System.err.println(e);
			Verify("Object not found :" + strXpath + ",failed due to exception ==>" + e, "FAILED");
		}

		return blnFlag;

	}

	public boolean clickOnElementContainsText(String sTagName, String sText) {
		boolean blnFlag = false;
		int iTimer = 0;
		String strXpath = "//" + sTagName + "[contains(text(),'" + sText + "')]";

		try {
			do {
				try {
					List<WebElement> sList = getDriver().findElements(By.xpath(strXpath));

					if (sList.size() > 0) {
						for (int i = 0; i < sList.size(); i++) {
							if (sList.get(i).isDisplayed() && sList.get(i).isEnabled()) {
								//// oSeleniumUtils.highlightElement(sList.get(i));
								sList.get(i).click();
								blnFlag = true;
								Verify("Link clicked sucessfully:=" + sText, "PASSED");
								break;
							}
						}
					}

					if (!blnFlag) {
						Thread.sleep(ProjectVariables.High_MIN_SLEEP);
					}
					iTimer = iTimer + 1;

				} catch (InvalidElementStateException Ie) {
					if (!blnFlag) {
						Thread.sleep(ProjectVariables.High_MIN_SLEEP);
					}
					iTimer = iTimer + 1;

				} catch (StaleElementReferenceException e) {
					if (!blnFlag) {
						Thread.sleep(ProjectVariables.High_MIN_SLEEP);
					}
					iTimer = iTimer + 1;

				} catch (WebDriverException se) {
					if (!blnFlag) {
						Thread.sleep(ProjectVariables.High_MIN_SLEEP);
					}
					iTimer = iTimer + 1;

				}
			} while ((blnFlag != true) && (iTimer != 30));

			if (blnFlag != true) {
				Verify("Object not found:=" + strXpath, "FAILED");
				blnFlag = false;
			}

		} catch (Exception e) {
			System.err.println(e);
			Verify("Object not found :" + strXpath + ",failed due to exception ==>" + e, "FAILED");
		}

		return blnFlag;

	}

	// ################################################################################################################################################################

	public boolean isElementExist(String sTagName, String sText) {
		boolean blnFlag = false;
		int iTimer = 0;
		String strXpath = "//" + sTagName + "[text()='" + sText + "']";

		try {
			do {
				try {
					List<WebElement> sList = getDriver().findElements(By.xpath(strXpath));

					if (sList.size() > 0) {
						for (int i = 0; i < sList.size(); i++) {
							if (sList.get(i) != null && sList.get(i).isDisplayed() && sList.get(i).isEnabled()) {
								// oSeleniumUtils.highlightElement(sList.get(i));
								blnFlag = true;
								Verify("Link displayed sucessfully:=" + sText, "PASSED");
								break;
							}
						}
					}

					if (!blnFlag) {
						Thread.sleep(ProjectVariables.High_MIN_SLEEP);
					}
					iTimer = iTimer + 1;

				} catch (ElementNotVisibleException Ie) {
					if (!blnFlag) {
						Thread.sleep(ProjectVariables.High_MIN_SLEEP);
					}
					iTimer = iTimer + 1;

				} catch (StaleElementReferenceException e) {
					if (!blnFlag) {
						Thread.sleep(ProjectVariables.High_MIN_SLEEP);
					}
					iTimer = iTimer + 1;

				} catch (NoSuchElementException se) {
					if (!blnFlag) {
						Thread.sleep(ProjectVariables.High_MIN_SLEEP);
					}
					iTimer = iTimer + 1;

				}

			} while ((blnFlag != true) && (iTimer != 10));

			if (blnFlag != true) {
				Verify("Object not found:=" + strXpath, "FAILED");
				blnFlag = false;
			}

		} catch (Exception e) {
			System.err.println(e);
			Verify("Object not clicked, Failed due to exception:=" + e.getMessage(), "FAILED");
		}

		return blnFlag;

	}

	// ################################################################################################################################################################

	public boolean WaitUntilElmtDisappears(String sXpath) {
	
		int iTimer = 0;
		String strXpath = sXpath;
		boolean blnFlag = false;
		try {
			do {
				try {
					List<WebElement> sList = getDriver().findElements(By.xpath(strXpath));

					if (sList.size() > 0) {						
						 blnFlag = false;						
					}else{
						 blnFlag = true;
					}

					if (blnFlag) {
						Thread.sleep(ProjectVariables.High_MIN_SLEEP);
					}
					iTimer = iTimer + 1;

				} catch (ElementNotVisibleException Ie) {
					if (!blnFlag) {
						Thread.sleep(ProjectVariables.High_MIN_SLEEP);
					}
					iTimer = iTimer + 1;

				} catch (StaleElementReferenceException e) {
					if (!blnFlag) {
						Thread.sleep(ProjectVariables.High_MIN_SLEEP);
					}
					iTimer = iTimer + 1;

				} catch (NoSuchElementException se) {
					if (!blnFlag) {
						Thread.sleep(ProjectVariables.High_MIN_SLEEP);
					}
					
					iTimer = iTimer + 1;

				}

			} while ((blnFlag != true) && (iTimer != 120));

			if (blnFlag != true) {
				Verify("Object still visible:=" + strXpath, "FAILED");
				blnFlag = false;
			}

		} catch (Exception e) {
			System.err.println(e);
			Verify("Object still visible, Failed due to exception:=" + e.getMessage(), "FAILED");
		}

		return blnFlag;

	}

	
	public static String SystemTime_in_the_given_format(String Timeformat) 
	{
		//TimeFormat Example==> dd/MM/yyyy h:mm:ss a
		
		DateFormat dateFormat = new SimpleDateFormat(Timeformat);
		Date date = new Date();		
		dateFormat.setTimeZone(TimeZone.getTimeZone("America/Montreal"));
		return dateFormat.format(date);
		
		/*DateFormat df = new SimpleDateFormat(Timeformat);
		Date date = new Date();

		// Tell the DateFormat that you want the time in this timezone
		dateFormat.setTimeZone(TimeZone.getTimeZone("America/Montreal"));

		String whatIWant = df.format(date);*/


	}

	// ==============================================CompareTwoValues===============================================================================>
	public static void CompareTwoValues(String sFunctionality, String sInput1, String sInput2) {

		if (sInput1.equalsIgnoreCase(sInput2)) {
			GenericUtils.Verify("Both values matched:==" + sFunctionality + "::==UI Value ==>" + sInput1
					+ " & DB value==>" + sInput2, "PASSED");
		} else {
			GenericUtils.Verify("Both values not matched:==" + sFunctionality + "::==UI Value ==>" + sInput1
					+ " & DB value==>" + sInput2, "FAILED");
		}

	}

	public static String Retrieve_the_data_based_on_criteria(String sFunctionality, String sInput1, String sInput2) {

		String Smallnum = null;
		String Bignum = null;

		if (sInput1.compareToIgnoreCase(sInput2) > 0) {
			Bignum = sInput1;
			Smallnum = sInput2;
		} else {
			Bignum = sInput2;
			Smallnum = sInput1;
		}

		if (sFunctionality.equalsIgnoreCase("Small")) {
			return Smallnum;
		} else if (sFunctionality.equalsIgnoreCase("Big")) {
			return Bignum;
		} else {
			Assert.assertTrue("case not found ==>" + sFunctionality, false);
		}
		return null;

	}

	public static String getCurrentTimeStamp() {
		// Date object
		Date date = new Date();
		long time = date.getTime();
		Timestamp ts = new Timestamp(time);
		return StringUtils.replace(ts.toString(), ":", "-");
	}

	// ===============================================================================================================================================================================
	// //

	public String Retrieve_the_insurance_from_insuranceKey(String Insurancekey) {
		String InsuranceName = null;
		switch (Insurancekey) {
		case "Medicare":
		case "1":
			InsuranceName = "MCR";
			break;
		case "Medicaid":
		case "2":
			InsuranceName = "MCD";
			break;
		case "Commercial":
		case "7":
			InsuranceName = "COM";
			break;
		case "Dual Eligible":
		case "3":
			InsuranceName = "DUA";
			break;
		case "BlueCard":
		case "8":
			InsuranceName = "BLU";
			break;
		case "Federal Employee Program":
		case "9":
			InsuranceName = "FED";
			break;
		default:
			Assert.assertTrue("Case not found=>" + Insurancekey, false);
			break;
		}

		return InsuranceName;
	}

	// ===============================================================================================================================================================================
	// //

	public static String Retrieve_the_insuranceKey_from_insurance(String Insurancekey) {
		String InsuranceName = null;
		switch (Insurancekey.toUpperCase()) {
		case "MEDICARE":

			InsuranceName = "1";
			break;
		case "MEDICAID":

			InsuranceName = "2";
			break;
		case "COMMERCIAL":

			InsuranceName = "7";
			break;
		case "DUAL ELIGIBLE":

			InsuranceName = "3";
			break;
		case "BLUECARD":

			InsuranceName = "8";
			break;
		case "FEDERAL EMPLOYEE PROGRAM":

			InsuranceName = "9";
			break;
		default:
			Assert.assertTrue("Case not found=>" + Insurancekey.toUpperCase(), false);
			break;
		}
System.out.println(InsuranceName);
		return InsuranceName;
	}

	// ===============================================================================================================================================================================
	
		public static void CompareTwoValues(String sFunctionality,ArrayList<String> sInputarrylist,String sExpectedoutput){
		    
			for (String DBValue : sInputarrylist) {
				
					 if(sExpectedoutput.contains(DBValue.trim()))
					 {
				         GenericUtils.Verify("Both values matched:=="+sFunctionality+"::==UI Value "+sExpectedoutput+" & DB value==>"+DBValue,"PASSED");
					 }
					 else
					 {
				             GenericUtils.Verify("Both values not matched:=="+sFunctionality+"::==UI Value "+sExpectedoutput+" & DB value==>"+DBValue,"FAILED");
					 }
			}
			
		   
		    
		}
		
		// ===============================================================================================================================================================================
		
		public static void CompareTwoValues(String sFunctionality,HashSet<String> sInputarrylist,String sExpectedoutput){
		    
			for (String DBValue : sInputarrylist) {
				
					 if(sExpectedoutput.contains(DBValue.trim()))
					 {
				         GenericUtils.Verify("Both values matched:=="+sFunctionality+"::==UI Value "+sExpectedoutput+" & DB value==>"+DBValue,"PASSED");
					 }
					 else
					 {
				             GenericUtils.Verify("Both values not matched:=="+sFunctionality+"::==UI Value "+sExpectedoutput+" & DB value==>"+DBValue,"FAILED");
					 }
			}
			
		   
		    
		}
		
		// ===============================================================================================================================================================================
		
		public static String RetreiveTheUsenameFromthegivenUserID(String sUserID)
		{
			String sUsername=null;
			
			switch(sUserID)
			{
			case "nkumar":
				sUsername="natuva kumar";
			break;
			case "iht_ittest09":
			case "iht_ittest03":
			case "iht_ittest06":
			case "iht_ittest01":
			case "iht_ittest05":
				sUsername=sUserID;
			break;
			
			}
			
			return sUsername;
		}
		
		// ===============================================================================================================================================================================
		
		public static String Retrieve_the_insuranceDesc_from_insuranceKey(String Insurancekey) {
			String InsuranceName = null;
			switch (Insurancekey) {
			case "1":
				InsuranceName = "Medicare";
				break;
			case "2":
				InsuranceName = "Medicaid";
				break;
			case "7":
				InsuranceName = "Commercial";
				break;
			case "3":
				InsuranceName = "Dual Eligible";
				break;
			case "8":
				InsuranceName = "BlueCard";
				break;
			case "9":
				InsuranceName = "Federal Employee Program";
				break;
			default:
				Assert.assertTrue("Case not found=>" + Insurancekey, false);
				break;
			}

			return InsuranceName;
		}		
		
		// ===============================================================================================================================================================================
		
		public static String ConvertEpochtoDate(String sData,String Requiredformat) {
			//TimeFormat Example==> dd/MM/yyyy h:mm:ss a
			Date date = new Date(Long.valueOf(sData));
			DateFormat format = new SimpleDateFormat(Requiredformat);
			//format.setTimeZone(TimeZone.getTimeZone("GMT"));
			String formatted = format.format(date);
			//System.out.println(formatted);

			return formatted;
		}
	
		// ===============================================================================================================================================================================
		
		public static void ComapreTwoGivenDates(String d1,String d2) throws ParseException { 
	        SimpleDateFormat sdfo = new SimpleDateFormat("dd/MM/yyyy h:mm:ss a"); 
			  
	        // Get the two dates to be compared 
	        Date Date1 = sdfo.parse(d1); 
	        Date Date2 = sdfo.parse(d2); 
						
	        if(Date1.compareTo(Date2) > 0) 
	        {
	         System.out.println("Date 1 occurs after Date 2");
	        } else if(Date1.compareTo(Date2) < 0) 
	        {
	         System.out.println("Date 1 occurs before Date 2");
	        } else if(Date1.compareTo(Date2) == 0) 
	        {
	         System.out.println("Both dates are equal");
	        }
	      }

		// ===============================================================================================================================================================================
		
		public static void CompareTwoValues(String sFunctionality,ArrayList<String> sUIDataList,List<String> sDBDataList){
			    
					
			for (String DBData : sDBDataList) 
			{
				if(sUIDataList.contains(DBData.trim()))
				{
					 GenericUtils.Verify("Both values matched:=="+sFunctionality+"::==UI Value "+sUIDataList+" & DB value==>"+DBData,"PASSED");
				}
				else
				{
					GenericUtils.Verify("Both values not matched:=="+sFunctionality+"::==UI Value "+sUIDataList+" & DB value==>"+DBData,"FAILED");
				}
				
			}
			
			
				
			   
			    
			}

		// ===============================================================================================================================================================================

				public static void validate_the_sorting_funtionality(ArrayList<String> savingslist,String Order,String colname) {
					ArrayList<String> ActualList=new ArrayList<>();
					ActualList.addAll(savingslist);
					System.out.println("Actual Arraylist==>"+savingslist);
					Collections.sort(savingslist);
					System.out.println("AfterSorting Arraylist==>"+savingslist);
					if(ActualList.equals(savingslist))
					{
						System.out.println(colname+" data is displyed in ascending order as expected,Data==>"+ActualList);
					}
					else
					{
						Assert.assertTrue(colname+" data is not displyed in ascending order as expected,Data==>"+ActualList, false);
					}
				}

		//===============================================================================================================================================================================
				
				public static void validate_the_sorting_funtionality_Integer(ArrayList<Long> savingslist,String Order,String colname) {
					
					System.out.println("sorting list size ==>"+savingslist.size());
					
					for (int k = 0; k < (savingslist.size()- 1); k++) {
			
						int compare = savingslist.get(k).compareTo(savingslist.get(k + 1));
			
						System.out.println(savingslist.get(k) + "," + savingslist.get(k + 1));
			
						System.out.println(compare);
			
						
							if (Order.equalsIgnoreCase("Ascending order") || Order.equalsIgnoreCase("Sort Ascending")) {
								if (compare == 0 || compare < 0) {
									System.out.println( colname + " is displayed successfully in " + Order + ",for the data:"+savingslist.get(k)+ "," + savingslist.get(k + 1));
								} else {
									Assert.assertTrue(colname + " is not displayed in " + Order + ",for the data:"+savingslist.get(k) + "," + savingslist.get(k + 1), false);
								}
							} else {
								if (compare < 0) {
									Assert.assertTrue(colname + " is not displayed in " + Order + ",for the data:"+savingslist.get(k) + "," + savingslist.get(k + 1), false);
			
								} else {
									System.out.println(colname + "  is displayed succesfully in " + Order + ",for the data:"+savingslist.get(k) + "," + savingslist.get(k + 1));
								}
							}
			
						
					}
					
				}

   // ===============================================================================================================================================================================
				
		public static void validate_the_sorting_funtionality_String(ArrayList<String> savingslist,String Order,String colname) {
			
			System.out.println("sorting list size ==>"+savingslist.size());
			
			for (int k = 0; k < (savingslist.size()- 1); k++) {
	
				int compare = savingslist.get(k).compareToIgnoreCase(savingslist.get(k + 1));
	
				System.out.println(savingslist.get(k) + "," + savingslist.get(k + 1));
	
				System.out.println(compare);
	
				
					if (Order.equalsIgnoreCase("Ascending order") || Order.equalsIgnoreCase("Sort Ascending")) {
						if (compare == 0 || compare < 0) {
							System.out.println( colname + " is displayed successfully in " + Order + ",for the data:"+savingslist.get(k)+ "," + savingslist.get(k + 1));
						} else {
							Assert.assertTrue(colname + " is not displayed in " + Order + ",for the data:"+savingslist.get(k) + "," + savingslist.get(k + 1), false);
						}
					} else {
						if (compare < 0) {
							Assert.assertTrue(colname + " is not displayed in " + Order + ",for the data:"+savingslist.get(k) + "," + savingslist.get(k + 1), false);
	
						} else {
							System.out.println(colname + "  is displayed succesfully in " + Order + ",for the data:"+savingslist.get(k) + "," + savingslist.get(k + 1));
						}
					}
	
				
			}
			
		}

		// ===============================================================================================================================================================================
	
	public static String getTheLatestDate(ArrayList<String> dateList) throws ParseException
	{
		//System.out.println("size::"+dateList.size());
		// Using java.util.Collections
		 //System.out.println("Latest Date : " + Collections.max(dateList));

		return Collections.max(dateList);
	}
		

}

