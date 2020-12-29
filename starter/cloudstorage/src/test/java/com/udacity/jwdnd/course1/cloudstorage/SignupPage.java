package com.udacity.jwdnd.course1.cloudstorage;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class SignupPage {
    private final WebDriver driver;

    @FindBy(id = "inputFirstName")
    public WebElement inputFirstName;

    @FindBy(id = "inputLastName")
    public WebElement inputLastName;

    @FindBy(id = "inputUsername")
    public WebElement inputUsername;

    @FindBy(id = "inputPassword")
    public WebElement inputPassword;

    @FindBy(id = "submit-button")
    public WebElement submitButton;

    @FindBy(id = "login-link")
    public WebElement loginLink;

    public SignupPage(WebDriver driver) {
        this.driver = driver;
        PageFactory.initElements(driver, this);
    }

    public String getSuccessMessage() {
        WebDriverWait wait = new WebDriverWait(driver, Constants.WAIT_TIMEOUT);
        WebElement marker = wait.until(webDriver -> webDriver.findElement(By.id("success-msg")));

        return marker.getText();
    }

    public void goToLogin() {
        loginLink.click();
    }

    public void fillAndSubmitForm(String firstName, String lastName, String username, String password) {
        driver.findElement(By.xpath("//*[@id=\"inputFirstName\"]")).sendKeys(firstName);
        driver.findElement(By.xpath("//*[@id=\"inputLastName\"]")).sendKeys(lastName);
        driver.findElement(By.xpath("//*[@id=\"inputUsername\"]")).sendKeys(username);
        driver.findElement(By.xpath("//*[@id=\"inputPassword\"]")).sendKeys(password);
        driver.findElement(By.xpath("//*[@id=\"submit-button\"]")).click();
    }

}
