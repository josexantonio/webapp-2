package com.example.demo.e2e;

import static java.lang.invoke.MethodHandles.lookup;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;

import static org.assertj.core.api.Assertions.assertThat;
import static org.slf4j.LoggerFactory.getLogger;

import java.util.List;

import org.slf4j.Logger;

public class TestE2E {

	final static Logger log = getLogger(lookup().lookupClass());

	public void loginUser(Browser browser, String name, String pass) {
		// Wait show form login
		browser.waitUntil(ExpectedConditions.visibilityOfElementLocated(By.name("username")), "No login page", 1);
		
		// Load form
		WebElement userField = browser.getDriver().findElement(By.name("username"));
		WebElement passField = browser.getDriver().findElement(By.name("password"));
		
		WebElement divSubmit = browser.getDriver().findElement(By.className("form-check"));
		WebElement submit = divSubmit.findElement(By.tagName("button"));
		
		// Write credentials
		userField.sendKeys(name);
		passField.sendKeys(pass);
		submit.click();
		
		// Check login
		browser.waitUntil(ExpectedConditions.visibilityOfElementLocated(By.className("masthead")), "Login failed", 2);
		
		WebElement menuBar = browser.getDriver().findElement(By.id("navbarResponsive"));
		List<WebElement> elementsBar = menuBar.findElements(By.tagName("a"));
		assertThat(elementsBar.get(elementsBar.size() - 1).getText()).isEqualToIgnoringCase("MY PROFILE");
		
		log.info("Loggin successful, user {}", name);
	}

	public void logout(Browser browser) {
		WebElement menuBar = browser.getDriver().findElement(By.id("navbarResponsive"));
		List<WebElement> elementsBar = menuBar.findElements(By.tagName("a"));
		
		WebElement buttonLogout;
		if(elementsBar.size() == 2) {
			buttonLogout = elementsBar.get(0);
		} else {
			buttonLogout = elementsBar.get(1);
		}
		
		// Click logout button
		buttonLogout.click();
		
		//Check logout
		menuBar = browser.getDriver().findElement(By.id("navbarResponsive"));
		elementsBar = menuBar.findElements(By.tagName("a"));
		
		assertThat(elementsBar.get(elementsBar.size() - 1).getText()).isEqualToIgnoringCase("LOG IN");
		
		log.info("Logout successful");
	}

}
