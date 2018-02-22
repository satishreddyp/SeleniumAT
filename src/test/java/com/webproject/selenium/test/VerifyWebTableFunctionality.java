package com.webproject.selenium.test;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.apache.log4j.Logger;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import com.webproject.selenium.baseutility.BaseUtility;
import com.webproject.selenium.constants.DataConstants;
import com.webproject.selenium.constants.RegularExpressionConstants;
import com.webproject.selenium.constants.XPathConstants;

/**
 * This test class is to verify the functionality of table by editing, saving
 * new number in the forum and then saving the details in xml by searching the
 * number belonged row in the table
 * 
 * @author satish
 *
 */
public class VerifyWebTableFunctionality extends BaseUtility {

	private ArrayList<String> listToWrite;
	private static final Logger LOGGER = Logger.getLogger(VerifyWebTableFunctionality.class);
	private XSSFWorkbook workbook;
	private FileOutputStream outputStream;

	/**
	 * This method to edit, save new number in the forum and then saving the details
	 * in xml by searching the number belonged row in the table
	 * 
	 * @param number
	 *            This parameter provides the number to be input in the field
	 */
	@Test(dataProvider = "getFormDetails")
	public void webTableFunctionalityVerification(String number) {

		// Clicking on the edit button
		explicitlyWait(60, XPathConstants.EDIT_BUTTON_XPATH);
		doubleClickOnElement(XPathConstants.EDIT_BUTTON_XPATH);

		// Entering the values in the number field
		List<WebElement> listOfFields = inspectElements(XPathConstants.LIST_OF_INPUT_FIELDS_XPATH);
		for (WebElement webElement : listOfFields) {
			if (inspectElementAndGetAttribute(webElement, "value")
					.matches(RegularExpressionConstants.MOBILE_NUMBER_REGULAR_EXPRESSION)) {
				webElement.clear();
				webElement.sendKeys(number);
				LOGGER.info(number + " is entered in the text field");
				inspectAndClickOnElement(XPathConstants.SAVE_BUTTON_XPATH);
			}
		}

		Select select = new Select(inspectElement(XPathConstants.ITEMS_PER_PAGE_SELECTION_XPATH));
		select.selectByIndex(2);
		LOGGER.info("Changed the value in the dropdown to - " + select.getFirstSelectedOption().getText());

		// Search for the saved number row by clicking on next if not found in current
		// page
		boolean numberIsFound = inspectElements(String.format(XPathConstants.DYNAMIC_XPATH, number)).size() > 0;

		while (!numberIsFound) {
			boolean isNextEnabled = inspectElements(XPathConstants.BUTTON_NEXT_XPATH).size() > 0;
			if (isNextEnabled) {
				inspectAndClickOnElement(XPathConstants.BUTTON_NEXT_XPATH);
				explicitlyWait(60, XPathConstants.EDIT_BUTTON_XPATH);
				try {
					scrollToElement(XPathConstants.DYNAMIC_XPATH, number);
					numberIsFound = true;
					break;
				} catch (NoSuchElementException nse) {
					numberIsFound = false;
				}
			} else {
				Assert.fail("Number not saved");
			}
		}

		if (!numberIsFound) {
			Assert.fail("Number not saved");
		}

		// Writing the details read from row to the excel sheet
		listToWrite = new ArrayList<String>();
		listOfFields = inspectElements(String.format(XPathConstants.ROW_CONTENTS, number));
		for (WebElement webElement : listOfFields) {
			listToWrite.add(inspectElementAndGetText(webElement));
		}
		writingDataToExcel();
		LOGGER.info("Data write done successfully");
	}

	/**
	 * This method is to write data read from form to excel sheet
	 */
	private void writingDataToExcel() {
		workbook = new XSSFWorkbook();
		XSSFSheet sheet = workbook.createSheet("FormDetails");

		Row r = sheet.createRow(0);
		for (int i = 0; i < listToWrite.size(); i++) {
			r.createCell(i).setCellValue(listToWrite.get(i));
		}
		try {
			outputStream = new FileOutputStream("FormDetails.xlsx");
			workbook.write(outputStream);
		} catch (Exception e) {
			LOGGER.error("Problem in writing data" + e);
		} finally {
			try {
				workbook.close();
				outputStream.close();
			} catch (IOException e) {
				LOGGER.error(e);
			}
		}
	}

	/**
	 * This method provides the data to the testcase
	 * 
	 * @return data
	 */
	@DataProvider
	private Object[][] getFormDetails() {
		final Object[][] data = { { DataConstants.MOBILE_NUMBER } };
		return data;
	}
}