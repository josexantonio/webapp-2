package com.example.demo.e2e;

import static java.lang.invoke.MethodHandles.lookup;
import static org.junit.Assert.assertThat;
import static org.hamcrest.CoreMatchers.not;
import static org.slf4j.LoggerFactory.getLogger;

import java.util.List;

import org.hamcrest.text.IsEqualIgnoringCase;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.slf4j.Logger;

import io.github.bonigarcia.seljup.SeleniumExtension;

@ExtendWith(SeleniumExtension.class)
public class TestE2EFront extends TestE2E{
	
	final static Logger log = getLogger(lookup().lookupClass());
	
	private Browser browser;
	
    public TestE2EFront(ChromeDriver driver) {
    	driver.manage().window().setSize(new Dimension(1024, 724));
    	this.browser = new Browser(driver, "http://localhost:4200/");
    }
    
    @Test
    public void checkCreateCourse() {
    	//Login
    	this.browser.goToPage("login");
    	this.loginUser(this.browser, "admin", "pass");
    		
    	//Create course
    	this.browser.goToPage("admin/addCourse");
    	
		WebElement name = browser.getDriver().findElement(By.name("newName"));
		WebElement startDate = browser.getDriver().findElement(By.name("startDate"));
		WebElement endDate = browser.getDriver().findElement(By.name("endDate"));
		WebElement newLanguage = browser.getDriver().findElement(By.name("newLanguage"));
		WebElement newType = browser.getDriver().findElement(By.name("newType"));
		WebElement newSkill = browser.getDriver().findElement(By.name("newSkill1"));
		WebElement newDescription = browser.getDriver().findElement(By.name("newDescription"));
		WebElement image = browser.getDriver().findElement(By.name("courseImage"));
		
		WebElement divForm = browser.getDriver().findElement(By.className("form-group"));
		WebElement submit = divForm.findElement(By.tagName("button"));
		
		String imageUpload = System.getProperty("user.dir") + "/src/test/resources/test.jpeg";
		
		name.sendKeys("test");
		startDate.sendKeys("10/02/2019");
		endDate.sendKeys("10/06/2019");
		newLanguage.sendKeys("Spanish");
		newType.sendKeys("test");
		newSkill.sendKeys("test");
		newDescription.sendKeys("test");
		image.sendKeys(imageUpload);
		submit.click();
		
		browser.waitUntil(ExpectedConditions.visibilityOfElementLocated(By.className("table-responsive")), "Failed creating course", 2);
		log.info("Course created correctly");
		
		//Go to last courses
		WebElement divTable = browser.getDriver().findElement(By.className("table-responsive"));
		List<WebElement> pagesCourses = divTable.findElements(By.className("page-item"));
		WebElement lastPageCourses = pagesCourses.get(pagesCourses.size() - 2);
		lastPageCourses.click();
		
		//Select last course
		divTable = browser.getDriver().findElement(By.className("table-responsive"));
		List<WebElement> trsTable = divTable.findElements(By.tagName("tr"));
		WebElement lastCourse = trsTable.get(trsTable.size() - 1);
		
		List<WebElement> inputsLastCourse = lastCourse.findElements(By.tagName("input")); 
		List<WebElement> buttonsLastCourse = lastCourse.findElements(By.tagName("button"));
		WebElement buttonDeleteLastCourse = buttonsLastCourse.get(buttonsLastCourse.size() - 1);
		String nameLastCourse = inputsLastCourse.get(0).getAttribute("value");
		
		//Check if the last course is equals than course added and delete it
		assertThat("Failed adding course", nameLastCourse, IsEqualIgnoringCase.equalToIgnoringCase("test"));
		buttonDeleteLastCourse.click();
		
		//Check if the course is deleted
		divTable = browser.getDriver().findElement(By.className("table-responsive"));
		trsTable = divTable.findElements(By.tagName("tr"));
		lastCourse = trsTable.get(trsTable.size() - 1);
		inputsLastCourse = lastCourse.findElements(By.tagName("input"));
		nameLastCourse = inputsLastCourse.get(0).getAttribute("value");
		
		assertThat("Failed deleting course", nameLastCourse, not(IsEqualIgnoringCase.equalToIgnoringCase("test")));
		log.info("Course deleted correctly");
		
		this.browser.goToPage();
    	this.logout(browser);
    }
}
