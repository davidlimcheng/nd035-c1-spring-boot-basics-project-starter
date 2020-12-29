package com.udacity.jwdnd.course1.cloudstorage;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.*;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class CloudStorageApplicationTests {

	@LocalServerPort
	private int port;

	private WebDriver driver;
	private HomePage homePage;
	private LoginPage loginPage;
	private SignupPage signupPage;
	private final String firstName = "Tom";
	private final String lastName = "Ato";
	private final String username = "HappyGardener82";
	private final String password = "i secretly eat glue";

	@BeforeAll
	static void beforeAll() {
		WebDriverManager.chromedriver().setup();
	}

	@BeforeEach
	public void beforeEach() {
		this.driver = new ChromeDriver();
	}

	@AfterEach
	public void afterEach() {
		if (this.driver != null) {
			driver.quit();
		}
	}

	// verifies an unauthorized user can only access login and signup pages
	@Test
	public void authorization() {
		driver.get("http://localhost:" + this.port + "/signup");
		assertEquals("Sign Up", driver.getTitle());

		driver.get("http://localhost:" + this.port + "/login");
		assertEquals("Login", driver.getTitle());

		driver.get("http://localhost:" + this.port + "/home");
		assertNotEquals("Home", driver.getTitle());

		driver.get("http://localhost:" + this.port + "/cowabunga");
		assertNotEquals("Cowabunga", driver.getTitle());
	}

	// logs in as a new user, verifies the home page is accessible, then
	// logs out, and verifies the home page is inaccessible
	@Test
	public void newUserAccess() throws InterruptedException {
		driver.get("http://localhost:" + this.port + "/signup");

		signupPage = new SignupPage(driver);
		signupPage.fillAndSubmitForm(firstName, lastName, username, password);
		Thread.sleep(500);

		String successMessage = signupPage.getSuccessMessage();
		assertTrue(successMessage.contains("You successfully signed up!"));

		driver.get("http://localhost:" + this.port + "/login");
		loginPage = new LoginPage(driver);

		loginPage.fillAndSubmitForm(username, password);
		Thread.sleep(500);

		homePage = new HomePage(driver);
		Thread.sleep(500);

		assertEquals("Home", driver.getTitle());
		homePage.logoutButton.click();

		driver.get("http://localhost:" + this.port + "/home");
		assertNotEquals("Home", driver.getTitle());
	}

	// note actions
	@Test
	public void noteActions() throws InterruptedException {
		driver.get("http://localhost:" + this.port + "/signup");
		signupPage = new SignupPage(driver);
		signupPage.fillAndSubmitForm(firstName, lastName, username, password);
		Thread.sleep(500);

		signupPage.goToLogin();
		Thread.sleep(500);

		loginPage = new LoginPage(driver);
		loginPage.fillAndSubmitForm(username, password);
		Thread.sleep(500);

		homePage = new HomePage(driver);
		Thread.sleep(500);

		final String noteTitle = "reminder";
		final String noteDescription = "feed the zebras";
		final String newNoteTitle = "different reminder";
		final String newNoteDescription = "feed the crocodiles";

		// adding a new note
		homePage.addNewNote(noteTitle, noteDescription);
		Thread.sleep(500);

		List<String> noteTitles = homePage.getAllNoteTitles();
		List<String> noteDescriptions = homePage.getAllNoteDescriptions();
		assertEquals(noteTitle, noteTitles.get(0));
		assertEquals(noteDescription, noteDescriptions.get(0));

		// editing an existing note
		homePage.editNote(newNoteTitle, newNoteDescription);
		Thread.sleep(500);

		noteTitles = homePage.getAllNoteTitles();
		noteDescriptions = homePage.getAllNoteDescriptions();
		assertEquals(newNoteTitle, noteTitles.get(0));
		assertEquals(newNoteDescription, noteDescriptions.get(0));

		// deleting a note
		homePage.deleteNote();
		Thread.sleep(500);

		List<WebElement> notes = homePage.getAllNotes();
		assertEquals(0, notes.size());
	}

	// credential actions
	@Test
	public void credentialActions() throws InterruptedException {
		driver.get("http://localhost:" + this.port + "/signup");
		signupPage = new SignupPage(driver);
		signupPage.fillAndSubmitForm(firstName, lastName, username, password);
		Thread.sleep(500);

		signupPage.goToLogin();
		Thread.sleep(500);

		loginPage = new LoginPage(driver);
		loginPage.fillAndSubmitForm(username, password);
		Thread.sleep(500);

		homePage = new HomePage(driver);
		Thread.sleep(500);

		final String credentialUrl = "http://www.backslashinforth_the_third.com";
		final String credentialUsername = "HappyGardener";
		final String credentialPassword = "donkey";
		final String newCredentialUrl = "http://www.newsite.com";
		final String newCredentialUsername = "SadGardener";
		final String newCredentialPassword = "horsey";

		// add a new credential
		homePage.addNewCredential(credentialUrl, credentialUsername, credentialPassword);
		Thread.sleep(500);

		List<String> credentialUrls = homePage.getAllCredentialUrls();
		List<String> credentialUsernames = homePage.getAllCredentialUsernames();
		List<String> credentialPasswords = homePage.getAllEncryptedCredentialPasswords();

		assertEquals(credentialUrl, credentialUrls.get(0));
		assertEquals(credentialUsername, credentialUsernames.get(0));
		assertNotEquals(credentialPassword, credentialPasswords.get(0));

		// verify decrypted password
		String decryptedPassword = homePage.getDecryptedPassword();
		assertEquals(credentialPassword, decryptedPassword);
		homePage.closeEditCredentialModal();

		// editing a credential
		homePage.editCredential(newCredentialUrl, newCredentialUsername, newCredentialPassword);
		Thread.sleep(500);

		credentialUrls = homePage.getAllCredentialUrls();
		credentialUsernames = homePage.getAllCredentialUsernames();
		credentialPasswords = homePage.getAllEncryptedCredentialPasswords();

		assertEquals(newCredentialUrl, credentialUrls.get(0));
		assertEquals(newCredentialUsername, credentialUsernames.get(0));
		assertNotEquals(newCredentialPassword, credentialPasswords.get(0));

		decryptedPassword = homePage.getDecryptedPassword();
		assertEquals(newCredentialPassword, decryptedPassword);
		homePage.closeEditCredentialModal();

		// deleting a credential
		homePage.deleteCredential();
		Thread.sleep(500);

		List<WebElement> credentials = homePage.getAllCredentials();
		assertEquals(0, credentials.size());
	}

}
