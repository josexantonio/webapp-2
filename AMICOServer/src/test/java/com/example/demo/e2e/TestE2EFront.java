package com.example.demo.e2e;

import static java.lang.invoke.MethodHandles.lookup;
import static org.junit.Assert.assertThat;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.slf4j.LoggerFactory.getLogger;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.hamcrest.text.IsEqualIgnoringCase;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.slf4j.Logger;

import io.github.bonigarcia.seljup.SeleniumExtension;
import io.github.bonigarcia.wdm.WebDriverManager;

@ExtendWith(SeleniumExtension.class)
public class TestE2EFront extends TestE2E {

	final static Logger log = getLogger(lookup().lookupClass());

	final static String PATH_DOWNLOAD = Paths.get(System.getProperty("user.dir"), "download-temporal").toString();

	private Browser browser;

	public TestE2EFront() {
		WebDriverManager.chromedriver().setup();

		HashMap<String, Object> chromePrefs = new HashMap<String, Object>();
		chromePrefs.put("profile.default_content_settings.popups", 0);
		chromePrefs.put("download.default_directory", PATH_DOWNLOAD);

		ChromeOptions options = new ChromeOptions();
		options.setExperimentalOption("prefs", chromePrefs);
		WebDriver driver = new ChromeDriver(options);

		driver.manage().window().setSize(new Dimension(1024, 724));
		this.browser = new Browser(driver, "http://localhost:4200/");
	}

	@AfterEach
	public void dispose() {
		this.browser.goToPage();
		this.logout(browser);
		
		this.browser.getDriver().quit();
	}

	@Test
	public void checkCreateCourse() {
		// Login
		this.browser.goToPage("login");
		this.loginUser(this.browser, "admin", "pass");

		// Create course
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

		browser.waitUntil(ExpectedConditions.visibilityOfElementLocated(By.id("dataTable1")), "Failed creating course",
				2);
		log.info("Course created correctly");

		// Go to last courses
		WebElement divTable = browser.getDriver().findElement(By.className("table-responsive"));
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
	}

	@Test
	public void checkDownload() {
		// Login
		this.browser.goToPage("login");
		this.loginUser(this.browser, "amico", "pass");

		// Profile
		this.browser.goToPage("users/amico/profile");

		// Go to course
		WebElement inscribedCourses = browser.getDriver().findElement(By.id("inscribed-courses"));
		List<WebElement> buttonsCourses = inscribedCourses.findElements(By.tagName("a"));
		WebElement firstCourse = buttonsCourses.get(0);
		firstCourse.click();

		// Go to first subject
		browser.waitUntil(ExpectedConditions.visibilityOfElementLocated(By.tagName("section")), "Failed opening course",
				2);
		WebElement subjects = browser.getDriver().findElement(By.tagName("section"));
		List<WebElement> buttonsSubjects = subjects.findElements(By.tagName("a"));
		WebElement firstSubject = buttonsSubjects.get(0);
		firstSubject.click();

		// Download
		browser.waitUntil(ExpectedConditions.visibilityOfElementLocated(By.className("container-fluid")),
				"Failed opening subject", 2);
		List<WebElement> files = browser.getDriver().findElements(By.className("item-content"));
		WebElement firstFile = files.get(0);
		firstFile.click();

		// Check download
		sleep(500);
		checkDownloadFile();
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
		WebElement divTable = browser.getDriver().findElement(By.id("dataTable1"));
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
