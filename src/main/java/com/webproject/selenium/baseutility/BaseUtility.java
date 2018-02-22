package com.webproject.selenium.baseutility;

import java.io.IOException;
import java.util.List;
import java.util.Properties;
import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import com.webproject.selenium.constants.GeneralConstants;

/**
 * This class maintains the application start and exit and contains some common
 * Functionalities like clicking on an element, getting text of particular,
 * getting attribute, actions to be performed element and wait conditions
 * 
 * @author Satish
 *
 */
public class BaseUtility {

	protected static WebDriver driver;
	private static final Logger LOGGER = Logger.getLogger(BaseUtility.class);
	private Properties projectConstants;

	/**
	 * This method creates driver instance for the driver and opens the provides url
	 * in the browser
	 *
	 * @throws IOException
	 */
	@BeforeMethod
	public void preConditions() throws IOException {

		// assigning the desired capabilities to the driver
		try {
			projectConstants = new Properties();
			projectConstants
					.load(getClass().getClassLoader().getResourceAsStream(GeneralConstants.PROJECT_CONSTANTS_FILE));
		} catch (NullPointerException npe) {
			Assert.fail("Please create the database and Email Report properties and "
					+ GeneralConstants.PROJECT_CONSTANTS_FILE + " before running the suite");
		}
		driver = new FirefoxDriver();
		driver.manage().window().maximize();
		driver.get(projectConstants.getProperty(GeneralConstants.URL_KEY));
	}

	/**
	 * This method is used to waits till the element is found
	 *
	 * @param seconds
	 *            This parameter provides the number of seconds
	 * @param xPath
	 *            This parameter provides the xpath of the element
	 */
	public void explicitlyWait(int seconds, String xPath) {
		new WebDriverWait(driver, seconds).until(ExpectedConditions.presenceOfElementLocated(By.xpath(xPath)));
	}

	/**
	 * This method is used to click on the element
	 *
	 * @param xPath
	 *            This parameter provides the xpath of the element
	 */
	public void inspectAndClickOnElement(String xPath) {
		WebElement element = inspectElement(xPath);
		String textOfElement = element.getText();
		element.click();
		LOGGER.info("Clicked on " + textOfElement);
	}

	/**
	 * This method provides the text of the given xpath
	 *
	 * @param xPath
	 *            This parameter provides the xpath
	 * @return the text of the element
	 */
	public String inspectElementAndGetText(String xPath) {
		return inspectElementAndGetText(inspectElement(xPath));
	}

	/**
	 * This method provides the text of the given webelement
	 *
	 * @param element
	 *            This parameter provides the webelement
	 * @return the text of the element
	 */
	public String inspectElementAndGetText(WebElement element) {
		return element.getText();
	}

	/**
	 * This method is to get the text from attribute type from given element
	 *
	 * @param element
	 *            This parameter provides the webelement
	 *
	 * @param attributeType
	 *            This parameter provides the attribute type
	 *
	 * @return attributeText of the element
	 */
	public String inspectElementAndGetAttribute(WebElement element, String attributeType) {
		return element.getAttribute(attributeType);
	}

	/**
	 * This method is to return the element
	 * 
	 * @param xPath
	 *            This parameter provides the xpath
	 * @return element
	 */
	public WebElement inspectElement(String xPath) {
		return driver.findElement(By.xpath(xPath));
	}

	/**
	 * This method is to double click on the xpath provided
	 * 
	 * @param xPath
	 *            This parameter provides the xpath
	 */
	public void doubleClickOnElement(String xPath) {
		Actions action = new Actions(driver);
		action.moveToElement(inspectElement(xPath)).doubleClick().build().perform();
	}

	/**
	 * This method is to inspect the set of elements
	 * 
	 * @param xPath
	 *            This parameter provides the list of elements xpath
	 * @return list of webelements
	 */
	public List<WebElement> inspectElements(String xPath) {
		return driver.findElements(By.xpath(xPath));
	}

	/**
	 * This method is to scroll page to the respective element
	 * 
	 * @param xPath
	 *            This parameter provides the xpath
	 * @param text
	 *            This parameter provides text to be places in xpath
	 */
	public void scrollToElement(String xPath, String text) {
		((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);",
				inspectElement(String.format(xPath, text)));
	}

	/**
	 * This method is to close the application completely.
	 */
	@AfterMethod
	public void closeApp() {
		if (driver != null) {
			driver.quit();
		}
	}
}