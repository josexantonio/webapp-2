package com.example.demo.e2e;

import static java.lang.invoke.MethodHandles.lookup;
import static org.junit.Assert.assertThat;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.slf4j.LoggerFactory.getLogger;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.hamcrest.text.IsEqualIgnoringCase;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;
import org.junit.jupiter.api.extension.ExtendWith;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;

import io.github.bonigarcia.seljup.SeleniumExtension;

@ExtendWith(SeleniumExtension.class)
public class TestE2EFront extends ElastestBaseTest {

	final static Logger log = getLogger(lookup().lookupClass());

	final static String PATH_DOWNLOAD = Paths.get(System.getProperty("user.dir"), "download-temporal").toString();

	@Test
	public void checkCreateCourse(TestInfo testInfo) {
		// Login
		goToPage("login");
		this.loginUser("admin", "pass");

		// Create course
		goToPage("admin/addCourse");

		WebElement name = driver.findElement(By.name("newName"));
		WebElement startDate = driver.findElement(By.name("startDate"));
		WebElement endDate = driver.findElement(By.name("endDate"));
		WebElement newLanguage = driver.findElement(By.name("newLanguage"));
		WebElement newType = driver.findElement(By.name("newType"));
		WebElement newSkill = driver.findElement(By.name("newSkill1"));
		WebElement newDescription = driver.findElement(By.name("newDescription"));
		WebElement image = driver.findElement(By.name("courseImage"));

		WebElement divForm = driver.findElement(By.className("form-group"));
		WebElement submit = divForm.findElement(By.tagName("button"));

		String imageUpload = System.getProperty("user.dir") + "/src/test/resources/test.jpeg";

		name.sendKeys("test");
		startDate.sendKeys("10/02/2019");
		endDate.sendKeys("10/06/2019");
		newLanguage.sendKeys("Spanish");
		newType.sendKeys("test");
		newSkill.sendKeys("test");
		newDescription.sendKeys("test");
		
		try {
			image.sendKeys(imageUpload);
		} catch (Exception e) {
			log.info("Impossible to find the image");
			e.printStackTrace();
		}

		submit.click();

		waitUntil(ExpectedConditions.visibilityOfElementLocated(By.id("dataTable1")), "Failed creating course", 2);
		log.info("Course created correctly");

		// Go to last courses
		WebElement divTable = driver.findElement(By.className("table-responsive"));
		List<WebElement> pagesCourses = divTable.findElements(By.className("page-item"));
		WebElement lastPageCourses = pagesCourses.get(pagesCourses.size() - 2);
		lastPageCourses.click();

		// Select last course
		WebElement lastCourse = getLastCourse();
		WebElement inputName = lastCourse.findElement(By.name("newName"));
		WebElement buttonDeleteLastCourse = lastCourse.findElement(By.name("btnDelete"));
		String nameLastCourse = inputName.getAttribute("value");

		// Check if the last course is equals than course added and delete it
		assertThat("Failed adding course", nameLastCourse, IsEqualIgnoringCase.equalToIgnoringCase("test"));
		buttonDeleteLastCourse.click();

		// Wait remove course
		sleep(500);

		// Check if the course is deleted
		lastCourse = getLastCourse();
		inputName = lastCourse.findElement(By.name("newName"));
		nameLastCourse = inputName.getAttribute("value");

		assertThat("Failed deleting course", nameLastCourse, not(IsEqualIgnoringCase.equalToIgnoringCase("test")));
		log.info("Course deleted correctly");
		
		//Logout
		this.goToPage();
		this.logout();
	}

	/*
	 * @Test public void checkDownload(TestInfo testInfo) { // Login
	 * goToPage("login"); loginUser("amico", "pass");
	 * 
	 * // Profile goToPage("users/amico/profile");
	 * 
	 * // Go to course WebElement inscribedCourses =
	 * driver.findElement(By.id("inscribed-courses")); List<WebElement>
	 * buttonsCourses = inscribedCourses.findElements(By.tagName("a")); WebElement
	 * firstCourse = buttonsCourses.get(0); firstCourse.click();
	 * 
	 * // Go to first subject
	 * waitUntil(ExpectedConditions.visibilityOfElementLocated(By.tagName("section")
	 * ), "Failed opening course", 2); WebElement subjects =
	 * driver.findElement(By.tagName("section")); List<WebElement> buttonsSubjects =
	 * subjects.findElements(By.tagName("a")); WebElement firstSubject =
	 * buttonsSubjects.get(0); firstSubject.click();
	 * 
	 * // Download
	 * waitUntil(ExpectedConditions.visibilityOfElementLocated(By.className(
	 * "container-fluid")), "Failed opening subject", 2); List<WebElement> files =
	 * driver.findElements(By.className("item-content")); WebElement firstFile =
	 * files.get(0); firstFile.click();
	 * 
	 * // Check download sleep(500); checkDownloadFile(); }
	 */
	
	@Test
	public void checkShowProfile() {
		//Go to profile 
		goToPage("users/amico/profile");
		
		//Check if not show profile
		WebElement errorPage = driver.findElement(By.tagName("h2"));
		assertThat("Failed test, show profile when it shouldn't be seen", errorPage.getText(), IsEqualIgnoringCase.equalToIgnoringCase("404"));
		log.info("The profile not show correctly");
	}

	public void loginUser(String name, String pass) {
		// Wait show form login
		waitUntil(ExpectedConditions.visibilityOfElementLocated(By.name("username")), "No login page", 1);

		// Load form
		WebElement userField = driver.findElement(By.name("username"));
		WebElement passField = driver.findElement(By.name("password"));

		WebElement divSubmit = driver.findElement(By.className("form-check"));
		WebElement submit = divSubmit.findElement(By.tagName("button"));

		// Write credentials
		userField.sendKeys(name);
		passField.sendKeys(pass);
		submit.click();

		// Check login
		waitUntil(ExpectedConditions.visibilityOfElementLocated(By.className("masthead")), "Login failed", 2);

		WebElement menuBar = driver.findElement(By.id("navbarResponsive"));
		List<WebElement> elementsBar = menuBar.findElements(By.tagName("a"));
		assertThat(elementsBar.get(elementsBar.size() - 1).getText()).isEqualToIgnoringCase("MY PROFILE");

		log.info("Loggin successful, user {}", name);
	}

	public void logout() {
		WebElement menuBar = driver.findElement(By.id("navbarResponsive"));
		List<WebElement> elementsBar = menuBar.findElements(By.tagName("a"));

		WebElement buttonLogout;
		if (elementsBar.size() == 2) {
			buttonLogout = elementsBar.get(0);
		} else {
			buttonLogout = elementsBar.get(1);
		}

		// Click logout button
		buttonLogout.click();

		// Check logout
		menuBar = driver.findElement(By.id("navbarResponsive"));
		elementsBar = menuBar.findElements(By.tagName("a"));

		assertThat(elementsBar.get(elementsBar.size() - 1).getText()).isEqualToIgnoringCase("LOG IN");

		log.info("Logout successful");
	}

	public void goToPage() {
		String url = sutUrl + "new/";

		this.driver.get(url);
	}

	public void goToPage(String page) {
		String url = sutUrl + "new/";

		this.driver.get(url + page);
	}

	public void waitUntil(ExpectedCondition<WebElement> expectedCondition, String errorMessage, int seconds) {
		WebDriverWait waiter = new WebDriverWait(driver, seconds);

		try {
			waiter.until(expectedCondition);
		} catch (org.openqa.selenium.TimeoutException timeout) {
			log.error(errorMessage);
			throw new org.openqa.selenium.TimeoutException(
					"\"" + errorMessage + "\" (checked with condition) > " + timeout.getMessage());
		}
	}

	private void checkDownloadFile() {
		File folder = new File(PATH_DOWNLOAD);
		boolean found = false;

		if (folder.exists() && folder.listFiles().length > 0) {
			found = true;

			try {
				FileUtils.deleteDirectory(folder);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		assertThat("Download failed", true, is(found));
	}

	private WebElement getLastCourse() {
		WebElement divTable = driver.findElement(By.id("dataTable1"));
		List<WebElement> trsTable = divTable.findElements(By.tagName("tr"));
		WebElement lastCourse = trsTable.get(trsTable.size() - 1);

		return lastCourse;
	}

	private void sleep(int time) {
		try {
			Thread.sleep(time);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}
