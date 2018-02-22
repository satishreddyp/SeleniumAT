package com.webproject.selenium.constants;

/**
 * This class provides the xpath constants
 * 
 * @author Satish
 */
public class XPathConstants {
	public static final String EDIT_BUTTON_XPATH = "(//i[contains(@class,'pencil')])[1]";
	public static final String LIST_OF_INPUT_FIELDS_XPATH = "//input[@type='text']";
	public static final String SAVE_BUTTON_XPATH = "(//button[text()='Save'])[1]";
	public static final String ITEMS_PER_PAGE_SELECTION_XPATH = "//select[@class='ng-pristine ng-untouched ng-valid']";
	public static final String BUTTON_NEXT_XPATH = "//button[@class='ui-grid-pager-next']";
	public static final String DYNAMIC_XPATH = "//*[text()='%s']";
	public static final String ROW_CONTENTS = "//*[text()='%s']/ancestor::*[@role='row']//*[@class='ui-grid-cell-contents ng-binding ng-scope']";
}
