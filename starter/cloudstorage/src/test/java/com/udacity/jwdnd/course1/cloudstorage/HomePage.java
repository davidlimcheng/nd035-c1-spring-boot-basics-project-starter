package com.udacity.jwdnd.course1.cloudstorage;

import org.apache.tomcat.util.bcel.Const;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class HomePage {
    private final WebDriver driver;

    @FindBy(id = "logout-button")
    public WebElement logoutButton;

    @FindBy(id = "nav-notes-tab")
    public WebElement notesTab;

    @FindBy(id = "nav-credentials-tab")
    public WebElement credentialsTab;

    public HomePage(WebDriver driver) {
        this.driver = driver;
        PageFactory.initElements(driver, this);
    }

    public void addNewNote(String title, String description) throws InterruptedException {
        WebDriverWait wait = new WebDriverWait(driver, Constants.WAIT_TIMEOUT);

        notesTab.click();
        wait.until(ExpectedConditions.elementToBeClickable(By.id("add-note-button"))).click();
        Thread.sleep(500);

        driver.findElement(By.xpath("//*[@id=\"note-title\"]")).sendKeys(title);
        driver.findElement(By.xpath("//*[@id=\"note-description\"]")).sendKeys(description);
        wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//*[@id=\"noteModal\"]/div/div/div[3]/button[2]"))).click();
        Thread.sleep(500);
    }

    public void editNote(String newTitle, String newDescription) throws InterruptedException {
        WebDriverWait wait = new WebDriverWait(driver, Constants.WAIT_TIMEOUT);

        notesTab.click();
        wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//*[@id=\"userTable\"]/tbody/tr/td[1]/button"))).click();
        Thread.sleep(500);

        WebElement inputNoteTitle = driver.findElement(By.xpath("//*[@id=\"note-title\"]"));
        inputNoteTitle.clear();
        inputNoteTitle.sendKeys(newTitle);

        WebElement inputNoteDescription = driver.findElement(By.xpath("//*[@id=\"note-description\"]"));
        inputNoteDescription.clear();
        inputNoteDescription.sendKeys(newDescription);

        wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//*[@id=\"noteModal\"]/div/div/div[3]/button[2]"))).click();
        Thread.sleep(500);
    }

    public void deleteNote() throws InterruptedException {
        WebDriverWait wait = new WebDriverWait(driver, Constants.WAIT_TIMEOUT);

        notesTab.click();
        wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//*[@id=\"userTable\"]/tbody/tr/td[1]/form/button"))).click();
        Thread.sleep(500);
    }

    public List<WebElement> getAllNotes() {
        WebDriverWait wait = new WebDriverWait(driver, Constants.WAIT_TIMEOUT);

        notesTab.click();
        wait.until(ExpectedConditions.elementToBeClickable(By.id("add-note-button")));
        return driver.findElements(By.id("note-row"));
    }

    public List<String> getAllNoteTitles() {
        WebDriverWait wait = new WebDriverWait(driver, Constants.WAIT_TIMEOUT);

        notesTab.click();
        wait.until(ExpectedConditions.elementToBeClickable(By.id("add-note-button")));
        List<String> allNoteTitles = new ArrayList<>();
        List<WebElement> allTitles = wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(By.id("note-title-text")));

        for (WebElement title : allTitles) {
            allNoteTitles.add(title.getText());
        }

        return allNoteTitles;
    }

    public List<String> getAllNoteDescriptions() {
        WebDriverWait wait = new WebDriverWait(driver, Constants.WAIT_TIMEOUT);

        notesTab.click();
        wait.until(ExpectedConditions.elementToBeClickable(By.id("add-note-button")));
        List<String> allNoteDescriptions = new ArrayList<>();
        List<WebElement> allDescriptions = wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(By.id("note-description-text")));

        for (WebElement title : allDescriptions) {
            allNoteDescriptions.add(title.getText());
        }

        return allNoteDescriptions;
    }

    public void addNewCredential(String url, String username, String password) throws InterruptedException {
        WebDriverWait wait = new WebDriverWait(driver, Constants.WAIT_TIMEOUT);

        credentialsTab.click();
        wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//*[@id=\"add-credential-button\"]"))).click();
        Thread.sleep(500);

        driver.findElement(By.xpath("//*[@id=\"credential-url\"]")).sendKeys(url);
        driver.findElement(By.xpath("//*[@id=\"credential-username\"]")).sendKeys(username);
        driver.findElement(By.xpath("//*[@id=\"credential-password\"]")).sendKeys(password);
        wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//*[@id=\"credentialModal\"]/div/div/div[3]/button[2]"))).click();
        Thread.sleep(500);
    }

    public List<String> getAllCredentialUrls() {
        WebDriverWait wait = new WebDriverWait(driver, Constants.WAIT_TIMEOUT);

        credentialsTab.click();
        wait.until(ExpectedConditions.elementToBeClickable(By.id("add-credential-button")));
        List<String> allCredentialUrls = new ArrayList<>();
        List<WebElement> allUrls = wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(By.id("credential-url-text")));

        for (WebElement url : allUrls) {
            allCredentialUrls.add(url.getText());
        }

        return allCredentialUrls;
    }

    public List<String> getAllCredentialUsernames() {
        WebDriverWait wait = new WebDriverWait(driver, Constants.WAIT_TIMEOUT);

        credentialsTab.click();
        wait.until(ExpectedConditions.elementToBeClickable(By.id("add-credential-button")));
        List<String> allCredentialUsernames = new ArrayList<>();
        List<WebElement> allUsernames = wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(By.id("credential-username-text")));

        for (WebElement username : allUsernames) {
            allCredentialUsernames.add(username.getText());
        }

        return allCredentialUsernames;
    }

    public List<String> getAllEncryptedCredentialPasswords() {
        WebDriverWait wait = new WebDriverWait(driver, Constants.WAIT_TIMEOUT);

        credentialsTab.click();
        wait.until(ExpectedConditions.elementToBeClickable(By.id("add-credential-button")));
        List<String> allEncryptedCredentialPasswords = new ArrayList<>();
        List<WebElement> allEncryptedPasswords = wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(By.id("credential-password-text")));

        for (WebElement username : allEncryptedPasswords) {
            allEncryptedCredentialPasswords.add(username.getText());
        }

        return allEncryptedCredentialPasswords;
    }

    public String getDecryptedPassword() throws InterruptedException {
        WebDriverWait wait = new WebDriverWait(driver, Constants.WAIT_TIMEOUT);

        credentialsTab.click();
        wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//*[@id=\"credentialTable\"]/tbody/tr/td[1]/button"))).click();
        Thread.sleep(500);

        return driver.findElement(By.xpath("//*[@id=\"credential-password\"]")).getAttribute("value");
    }

    public void closeEditCredentialModal() throws InterruptedException {
        WebDriverWait wait = new WebDriverWait(driver, Constants.WAIT_TIMEOUT);
        wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//*[@id=\"credentialModal\"]/div/div/div[3]/button[1]"))).click();
        Thread.sleep(500);
    }

    public void editCredential(String newUrl, String newUsername, String newPassword) throws InterruptedException {
        WebDriverWait wait = new WebDriverWait(driver, Constants.WAIT_TIMEOUT);

        credentialsTab.click();
        wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//*[@id=\"credentialTable\"]/tbody/tr/td[1]/button"))).click();
        Thread.sleep(500);

        WebElement inputCredentialUrl = driver.findElement(By.xpath("//*[@id=\"credential-url\"]"));
        inputCredentialUrl.clear();
        inputCredentialUrl.sendKeys(newUrl);

        WebElement inputCredentialUsername = driver.findElement(By.xpath("//*[@id=\"credential-username\"]"));
        inputCredentialUsername.clear();
        inputCredentialUsername.sendKeys(newUsername);

        WebElement inputCredentialPassword = driver.findElement(By.xpath("//*[@id=\"credential-password\"]"));
        inputCredentialPassword.clear();
        inputCredentialPassword.sendKeys(newPassword);

        wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//*[@id=\"credentialModal\"]/div/div/div[3]/button[2]"))).click();
        Thread.sleep(500);
    }

    public void deleteCredential() throws InterruptedException {
        WebDriverWait wait = new WebDriverWait(driver, Constants.WAIT_TIMEOUT);

        credentialsTab.click();
        wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//*[@id=\"credentialTable\"]/tbody/tr/td[1]/form/button"))).click();
        Thread.sleep(500);
    }

    public List<WebElement> getAllCredentials() {
        WebDriverWait wait = new WebDriverWait(driver, Constants.WAIT_TIMEOUT);

        credentialsTab.click();
        wait.until(ExpectedConditions.elementToBeClickable(By.id("add-credential-button")));
        return driver.findElements(By.id("credential-row"));
    }
}
