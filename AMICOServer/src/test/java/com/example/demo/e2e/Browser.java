package com.example.demo.e2e;

import static java.lang.invoke.MethodHandles.lookup;
import static org.slf4j.LoggerFactory.getLogger;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.WebDriverWait;

import org.slf4j.Logger;

public class Browser {
	
	private WebDriver driver;
	private String URL;
	
	final static Logger log = getLogger(lookup().lookupClass());
	
	public Browser(WebDriver driver, String URL) {
		this.driver = driver;
		this.URL = URL;
	}
	
	public void goToPage() {
		this.driver.get(URL);
	}
	
	public void goToPage(String page) {
		this.driver.get(URL + page);
	}
	
	public WebDriver getDriver() {
		return this.driver;
	}
	
	public void waitUntil(ExpectedCondition<WebElement> expectedCondition, String errorMessage, int seconds) {
		WebDriverWait waiter = new WebDriverWait(this.driver, seconds);
		
		try {
			waiter.until(expectedCondition);
		} catch(org.openqa.selenium.TimeoutException timeout) {
			log.error(errorMessage);
			throw new org.openqa.selenium.TimeoutException("\"" + errorMessage + "\" (checked with condition) > " + timeout.getMessage());
		}
	}
	
}
