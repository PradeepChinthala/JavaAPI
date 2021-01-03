package project.utilities;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import net.serenitybdd.core.Serenity;
import project.variables.ProjectVariables;

public class ExcelUtils {


	public static int SetCellDataXlsm(String sTcName, String sTcStatus) throws Exception {

		int rowcount;
		int rowNum = 0;
		try {

			int iTcNum = 1;
			int iTcName = 2;
			int iTcStatus = 3;
			int iTcTime = 4;                                                           
			String path = "P:\\IT\\PCAAutomation\\Mail\\Mail.xlsm";

			DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
			Date date = new Date();
			String sDate = dateFormat.format(date).toString();

			File src = new File(path);
			FileInputStream finput = new FileInputStream(src);
			XSSFWorkbook workbook = new XSSFWorkbook(finput);
			XSSFSheet sheet = workbook.getSheetAt(0);                   
			rowcount = sheet.getPhysicalNumberOfRows();

			int iRowCount = rowcount;
			rowNum = iRowCount+1;
			String sTcNo = "TC_"+(iRowCount-9);

			XSSFRow row = sheet.getRow(0);          
			row = sheet.getRow(rowNum - 1);
			if (row == null)
				row = sheet.createRow(rowNum - 1);

			sheet.autoSizeColumn(iTcNum);
			XSSFCell sTcNumCell = row.getCell(iTcNum);
			if (sTcNumCell == null)
				sTcNumCell = row.createCell(iTcNum);
			sTcNumCell.setCellValue(sTcNo);

			sheet.autoSizeColumn(iTcName);
			XSSFCell sTsNamecell = row.getCell(iTcName);
			if (sTsNamecell == null)
				sTsNamecell = row.createCell(iTcName);
			sTsNamecell.setCellValue(sTcName);

			sheet.autoSizeColumn(iTcStatus);
			XSSFCell sTcStatusCell = row.getCell(iTcStatus);
			if (sTcStatusCell == null)
				sTcStatusCell = row.createCell(iTcStatus);
			sTcStatusCell.setCellValue(sTcStatus);

			sheet.autoSizeColumn(iTcTime);
			XSSFCell sTcDateCell = row.getCell(iTcTime);
			if (sTcDateCell == null)
				sTcDateCell = row.createCell(iTcTime);
			sTcDateCell.setCellValue(sDate);

			FileOutputStream fileOut = new FileOutputStream(path);
			workbook.write(fileOut);

			fileOut.close();


		} catch (Exception e) {
			e.printStackTrace();
		}

		return rowNum;

	}

	public static boolean SetPath(int rowNum, int colName, String data, String sPath) {

		try {

			String path = sPath;
			FileInputStream fis = new FileInputStream(path);
			XSSFWorkbook workbook = new XSSFWorkbook(fis);
			if (rowNum <= 0)
				return false;
			XSSFSheet sheet = workbook.getSheetAt(0);
			XSSFRow row = sheet.getRow(0);
			int colNum = colName;
			sheet.autoSizeColumn(colNum);
			row = sheet.getRow(rowNum - 1);
			if (row == null)
				row = sheet.createRow(rowNum - 1);
			XSSFCell cell = row.getCell(colNum);
			if (cell == null)
				cell = row.createCell(colNum);
			cell.setCellValue(data);
			FileOutputStream fileOut = new FileOutputStream(path);
			workbook.write(fileOut);
			fileOut.close();

		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	public static int SetCellDataXlsm(String sFeaturename , String sTcName, String sTcStatus, String sPath, String arg1, String arg2) throws Exception {

		int rowcount;
		int rowNum = 0;
		try {

			int iTcNum = 0;
			int iFeatureName = 1;
			int iTcName = 2;
			int iTcStatus = 3;
			int iTcTime = 4;                                                           
			String path = sPath;

			DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
			Date date = new Date();
			String sDate = dateFormat.format(date).toString();

			File src = new File(path);
			FileInputStream finput = new FileInputStream(src);
			XSSFWorkbook workbook = new XSSFWorkbook(finput);
			XSSFSheet sheet = workbook.getSheetAt(0);                   
			rowcount = sheet.getPhysicalNumberOfRows();

			int iRowCount = rowcount;
			rowNum = iRowCount+1;
			String sTcNo = "TC_"+(iRowCount-9);

			XSSFRow row = sheet.getRow(0);          
			row = sheet.getRow(rowNum - 1);
			if (row == null)
				row = sheet.createRow(rowNum - 1);

			sheet.autoSizeColumn(iTcNum);
			XSSFCell sTcNumCell = row.getCell(iTcNum);
			if (sTcNumCell == null)
				sTcNumCell = row.createCell(iTcNum);
			sTcNumCell.setCellValue(sTcNo);

			sheet.autoSizeColumn(iFeatureName);
			XSSFCell sFeatureCell = row.getCell(iFeatureName);
			if (sFeatureCell == null)
				sFeatureCell = row.createCell(iFeatureName);
			sFeatureCell.setCellValue(sFeaturename);

			sheet.autoSizeColumn(iTcName);
			XSSFCell sTsNamecell = row.getCell(iTcName);
			if (sTsNamecell == null)
				sTsNamecell = row.createCell(iTcName);
			sTsNamecell.setCellValue(sTcName);

			sheet.autoSizeColumn(iTcStatus);
			XSSFCell sTcStatusCell = row.getCell(iTcStatus);
			if (sTcStatusCell == null)
				sTcStatusCell = row.createCell(iTcStatus);
			sTcStatusCell.setCellValue(sTcStatus);

			sheet.autoSizeColumn(iTcTime);
			XSSFCell sTcDateCell = row.getCell(iTcTime);
			if (sTcDateCell == null)
				sTcDateCell = row.createCell(iTcTime);
			sTcDateCell.setCellValue(sDate);

			FileOutputStream fileOut = new FileOutputStream(path);
			workbook.write(fileOut);

			fileOut.close();

		} catch (Exception e) {
			e.printStackTrace();
		}

		return rowNum;

	}
	public static int SetCellDataXlsm(int RowNum,int ColNum,int sheet_No,String sStatus,String Path) throws Exception {
		int rowNum = 0;
		try {

			                                                        
			File src = new File(Path);
			FileInputStream finput = new FileInputStream(src);
			XSSFWorkbook workbook = new XSSFWorkbook(finput);
			XSSFSheet sheet = workbook.getSheetAt(sheet_No);
			
			XSSFRow row = sheet.getRow(RowNum);
			XSSFCell sTcDateCell = row.getCell(ColNum);
			if (sTcDateCell == null)
				sTcDateCell = row.createCell(ColNum);
			sTcDateCell.setCellValue(sStatus);
			FileOutputStream fileOut = new FileOutputStream(Path);
			workbook.write(fileOut);

			fileOut.close();

		} catch (Exception e) {
			e.printStackTrace();
		}

		return rowNum;

	}

	public static int GetSheetCount() throws Exception {
		int sheetcount;
		try {
			File src = new File(ProjectVariables.sTestDataFilePath);
			FileInputStream finput = new FileInputStream(src);
			XSSFWorkbook workbook = new XSSFWorkbook(finput);
			sheetcount = workbook.getNumberOfSheets();
		} catch (Exception e) {

			throw (e);

		}

		System.out.println("sheet Count:=" + sheetcount);
		return sheetcount;

	}

	/**
	 * @param sheet_No
	 * @return
	 * @throws Exception
	 */
	public static int GetRowCount(int sheet_No) throws Exception {

		int rowcount;
		try {

			// Open the Excel file

			File src = new File(ProjectVariables.sTestDataFilePath);
			FileInputStream finput = new FileInputStream(src);
			XSSFWorkbook workbook = new XSSFWorkbook(finput);

			XSSFSheet ExcelWSheet = workbook.getSheetAt(sheet_No);

			rowcount = ExcelWSheet.getLastRowNum();

		} catch (Exception e) {

			throw (e);

		}

		System.out.println("Row Count:=" + rowcount);
		return rowcount;
	}
	public static int GetRowCount(int sheet_No,String sTestDataFilePath) throws Exception {

		int rowcount;
		try {

			// Open the Excel file

			File src = new File(sTestDataFilePath);
			FileInputStream finput = new FileInputStream(src);
			XSSFWorkbook workbook = new XSSFWorkbook(finput);

			XSSFSheet ExcelWSheet = workbook.getSheetAt(sheet_No);

			rowcount = ExcelWSheet.getLastRowNum();

		} catch (Exception e) {

			throw (e);

		}

		System.out.println("Row Count:=" + rowcount);
		return rowcount;
	}

	public static int GetColumnCount(int sheet_No) throws Exception {

		int columncount;
		try {

			// Open the Excel file

			File src = new File(ProjectVariables.sTestDataFilePath);
			FileInputStream finput = new FileInputStream(src);
			XSSFWorkbook workbook = new XSSFWorkbook(finput);

			XSSFSheet ExcelWSheet = workbook.getSheetAt(sheet_No);
			XSSFRow row = ExcelWSheet.getRow(0);
			columncount = row.getLastCellNum();

		} catch (Exception e) {

			throw (e);

		}

		System.out.println("Column Count:=" + columncount);
		return columncount;
	}

	//////////////////////////////////////////////
	public static String getCellData(int RowNum, int ColNum, int sheet_No,String Path) throws Exception {
		String cellText = null;

		try {

			File src = new File(Path);
			FileInputStream finput = new FileInputStream(src);
			XSSFWorkbook workbook = new XSSFWorkbook(finput);
			XSSFSheet sheet = workbook.getSheetAt(sheet_No);
			XSSFCell cell = sheet.getRow(RowNum).getCell(ColNum);

			if (cell.getCellType() == Cell.CELL_TYPE_STRING) {
				cellText = cell.getStringCellValue();
			} else if (cell.getCellType() == Cell.CELL_TYPE_NUMERIC || cell.getCellType() == Cell.CELL_TYPE_FORMULA) {
				cellText = String.valueOf(cell.getDateCellValue());
			} else if (cell.getCellType() == Cell.CELL_TYPE_BLANK)
				cellText = null;
			else
				cellText = "";
		}

		catch (Exception e) {
			// e.printStackTrace();
			System.out.println("row " + RowNum + " or column " + ColNum + " does not exist  in xlsx");
			cellText = null;
		}

		return cellText;
	}

	public static  String  getSheetName(int sheetNO){

		String sName=null;

		File getLatestFile = new SeleniumUtils().getLatestFilefromDir(ProjectVariables.sTestDataFilePath);

		String fileName = getLatestFile.getName();

		String sNewFileName=ProjectVariables.sTestDataFilePath+fileName;

		//String sSheetName=ExcelUtils.GetSheetName(1,sNewFileName);

		System.out.println("Sheet Name"+sNewFileName);


		FileInputStream fileInputStream = null;
		try {
			fileInputStream = new FileInputStream(sNewFileName);

			HSSFWorkbook workbook = new HSSFWorkbook(fileInputStream);

			// for each sheet in the workbook
			//for (int i = 0; i < workbook.getNumberOfSheets(); i++) {

			//System.out.println("Sheet name: " + workbook.getSheetName(sheetNO));
			sName=workbook.getSheetName(sheetNO);
			//}

		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (fileInputStream != null) {
				try {
					fileInputStream.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return sName;
	}

	public static void loadServices() throws IOException{

		File src = new File("src//test//resources//RestServices.xlsx");
		FileInputStream finput = new FileInputStream(src);
		XSSFWorkbook workbook = new XSSFWorkbook(finput);

		XSSFSheet ExcelWSheet = workbook.getSheetAt(0);

		int rowcount = ExcelWSheet.getLastRowNum();

		for (int i = 1 ;i<=rowcount;i++){
			XSSFRow iRow = ExcelWSheet.getRow(i);

			String sEndPointName = iRow.getCell(0).getStringCellValue();
			String sEndPointURI = iRow.getCell(1).getStringCellValue();
			String sEndPointBody = iRow.getCell(2).getStringCellValue();
			HashMap<String,String> sURI = new HashMap<String,String>();

			sURI.put("EndpointURL", sEndPointURI);
			sURI.put("RequestBody", sEndPointBody);
			ProjectVariables.sServices.put(sEndPointName, sURI);

		}

	}

	public static int generateNotificationData(String sTypeofDeck,String sClient, String sPresentation, String sMessage ,String sChange, String sPath, String arg2) throws Exception {

		int rowcount;
		int rowNum = 0;
		try {

			int iDeck = 0;
			int iClient = 1;
			int iPresentation = 2;
			int iMessage = 3;
			int iChange = 4;                                                           
			String path = sPath;

			DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
			Date date = new Date();
			String sDate = dateFormat.format(date).toString();

			File src = new File(path);
			FileInputStream finput = new FileInputStream(src);
			XSSFWorkbook workbook = new XSSFWorkbook(finput);
			XSSFSheet sheet = workbook.getSheetAt(0);                   
			rowcount = sheet.getPhysicalNumberOfRows();

			int iRowCount = rowcount;
			rowNum = iRowCount+1;

			XSSFRow row = sheet.getRow(0);          
			row = sheet.getRow(rowNum - 1);
			if (row == null)
				row = sheet.createRow(rowNum - 1);

			sheet.autoSizeColumn(iDeck);
			XSSFCell sTcNumCell = row.getCell(iDeck);
			if (sTcNumCell == null)
				sTcNumCell = row.createCell(iDeck);
			sTcNumCell.setCellValue(sTypeofDeck);

			sheet.autoSizeColumn(iClient);
			XSSFCell sFeatureCell = row.getCell(iClient);
			if (sFeatureCell == null)
				sFeatureCell = row.createCell(iClient);
			sFeatureCell.setCellValue(sClient);

			sheet.autoSizeColumn(iPresentation);
			XSSFCell sTsNamecell = row.getCell(iPresentation);
			if (sTsNamecell == null)
				sTsNamecell = row.createCell(iPresentation);
			sTsNamecell.setCellValue(sPresentation);

			sheet.autoSizeColumn(iMessage);
			XSSFCell sTcStatusCell = row.getCell(iMessage);
			if (sTcStatusCell == null)
				sTcStatusCell = row.createCell(iMessage);
			sTcStatusCell.setCellValue(sMessage);

			sheet.autoSizeColumn(iChange);
			XSSFCell sTcDateCell = row.getCell(iChange);
			if (sTcDateCell == null)
				sTcDateCell = row.createCell(iChange);
			sTcDateCell.setCellValue(sChange);

			FileOutputStream fileOut = new FileOutputStream(path);
			workbook.write(fileOut);

			fileOut.close();

		} catch (Exception e) {
			e.printStackTrace();
		}

		return rowNum;

	}

	public static void cleanSheet( String sPath) throws IOException, InterruptedException {
		
		File src = new File(sPath);
		FileInputStream finput = new FileInputStream(src);
		XSSFWorkbook workbook = new XSSFWorkbook(finput);
		XSSFSheet sheet = workbook.getSheetAt(0);
		
		try{

			int numberOfRows = sheet.getPhysicalNumberOfRows();

			if(numberOfRows > 0) {
				for (int i = 1; i <= sheet.getLastRowNum(); i++) {
					if(sheet.getRow(i) != null) {
						sheet.removeRow( sheet.getRow(i));
					} else {
						System.out.println("Info: clean sheet='" + sheet.getSheetName() + "' ... skip line: " + i);
					}
				}               
			} else {
				System.out.println("Info: clean sheet='" + sheet.getSheetName() + "' ... is empty");
			}   
			
			finput.close();
			
		} catch (Exception e) {

		} finally {
			if (finput != null) {
				try {
					finput.close();
				} catch (IOException e) {
				}
			}
		}

	}
	
	public static void LoadEllData() throws IOException{

		File src = new File("src//test//resources//ELLHierarchy.xlsx");
		FileInputStream finput = new FileInputStream(src);
		XSSFWorkbook workbook = new XSSFWorkbook(finput);
		XSSFSheet ExcelWSheet = workbook.getSheetAt(0);
		int rowCount = ExcelWSheet.getPhysicalNumberOfRows();
		
		//Set Header Columns Numbers 
		int colcount = ExcelWSheet.getRow(0).getPhysicalNumberOfCells();		
		HashMap<String,Integer> sColumnNos = new HashMap<String,Integer>();
		for (int itrCols = 0 ;itrCols<colcount;itrCols++){			
			XSSFRow iRow = ExcelWSheet.getRow(0);
			String sColunmName = iRow.getCell(itrCols).getStringCellValue().trim();
			sColumnNos.put(sColunmName, itrCols);

		}
		Serenity.setSessionVariable("ELLHeaders").to(sColumnNos);
		
		
		//Add Data to HashMap
		HashMap<Integer, HashMap<String, String>> EllData = new HashMap<Integer,HashMap<String, String>>();
		
		for (int i = 1 ;i<rowCount;i++){
			
			XSSFRow iRow = ExcelWSheet.getRow(i);
			
			HashMap<String, String> sValues = new HashMap<String,String>();
			for (int j = 0 ;j<colcount;j++){	
				XSSFRow iHeaderRow = ExcelWSheet.getRow(0);
				String sColName = iHeaderRow.getCell(j).getStringCellValue().trim();
				String cellText;
				
				try{
				if (iRow.getCell(j).getCellType() == Cell.CELL_TYPE_STRING) {
					cellText = iRow.getCell(j).getStringCellValue();
				} else if (iRow.getCell(j).getCellType() == iRow.getCell(j).CELL_TYPE_NUMERIC || iRow.getCell(j).getCellType() == Cell.CELL_TYPE_FORMULA) {
					cellText = String.valueOf(iRow.getCell(j).getRawValue());
				} else if (iRow.getCell(j).getCellType() == Cell.CELL_TYPE_BLANK)
					cellText = null;
				else
					cellText = "";
				
				}catch(Exception e){
					cellText = null;
				}
				sValues.put(sColName, cellText);	
				}
							
			EllData.put(i,sValues);
			}
			
		
		Serenity.setSessionVariable("SubSequentELLData").to(EllData);
		
		ProjectVariables.sSubsequentEllData=EllData;
	}
	
	@SuppressWarnings("unchecked")
	public static int getELLRowno(String strChange,String strDPType,String strDisposition) throws IOException{
		
		HashMap<Integer, HashMap<String, String>> sHeaders = Serenity.sessionVariableCalled("SubSequentELLData");		
		Iterator iter = sHeaders.entrySet().iterator();
		while (iter.hasNext()) {
			Map.Entry entry = (Map.Entry) iter.next();			
			System.out.println("[Key] : " + entry.getKey() + " [Value] : " + entry.getValue());			
			Map<String, String> sValues = (Map<String, String>) entry.getValue();			
			List<String> valueList = sValues.values().stream().collect(Collectors.toList());		
			if (valueList.contains(strDisposition) && 
					valueList.contains(strChange) && valueList.contains(strDPType)){
				  System.out.println(entry.getKey());
				  return (int) entry.getKey();
			}
		}
		
		return -1;
		
	}
	
	public static int getEllColno(String sColumnName) throws IOException{

		HashMap<String, Integer> getColno = Serenity.sessionVariableCalled("ELLHeaders");
		
		return getColno.get(sColumnName);
	}
	

	public static void setELLCellData(int iRowno,String sColumnName,String sValue) throws IOException, InterruptedException{
		
		int col = getEllColno(sColumnName);
		
		File src = new File("src//test//resources//ELLHierarchy.xlsx");
		FileInputStream finput = new FileInputStream(src);
		XSSFWorkbook workbook = new XSSFWorkbook(finput);
		
		XSSFSheet ExcelWSheet = workbook.getSheetAt(0);
		XSSFRow row = ExcelWSheet.getRow(iRowno);
		
		ExcelWSheet.autoSizeColumn(col);
		XSSFCell sTcStatusCell = row.getCell(col);
		if (sTcStatusCell == null)
			sTcStatusCell = row.createCell(col);
		sTcStatusCell.setCellValue(sValue);
		
		FileOutputStream fileOut = new FileOutputStream(src);
		workbook.write(fileOut);
	
		fileOut.close();
		Thread.sleep(1000);
	}
	
public static String getELLCellData(int iRowno,String sColumnName) throws IOException{
		
		int col = getEllColno(sColumnName);
		String cellText = null;
		try{
			
			File src = new File("src//test//resources//ELLHierarchy.xlsx");
			FileInputStream finput = new FileInputStream(src);
			XSSFWorkbook workbook = new XSSFWorkbook(finput);
			
			XSSFSheet ExcelWSheet = workbook.getSheetAt(0);
			
			XSSFCell cell = ExcelWSheet.getRow(iRowno).getCell(col);
	
			if (cell.getCellType() == Cell.CELL_TYPE_STRING) {
				cellText = cell.getStringCellValue();
			} else if (cell.getCellType() == Cell.CELL_TYPE_NUMERIC || cell.getCellType() == Cell.CELL_TYPE_FORMULA) {
				cellText = String.valueOf(cell.getDateCellValue());
			} else if (cell.getCellType() == Cell.CELL_TYPE_BLANK)
				cellText = null;
			else
				cellText = "";
			
			}catch (Exception e) {
				// e.printStackTrace();
				System.out.println("row " + iRowno + " or column " + col + " does not exist  in xlsx");
				cellText = null;
			}

		return cellText;
	}

}