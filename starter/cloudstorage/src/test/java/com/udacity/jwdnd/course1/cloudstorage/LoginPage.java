package com.udacity.jwdnd.course1.cloudstorage;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class LoginPage {
    private final WebDriver driver;

    public LoginPage(WebDriver driver) {
        this.driver = driver;
        PageFactory.initElements(driver, this);
    }

    public void fillAndSubmitForm(String username, String password) {
        driver.findElement(By.xpath("//*[@id=\"inputUsername\"]")).sendKeys(username);
        driver.findElement(By.xpath("//*[@id=\"inputPassword\"]")).sendKeys(password);
        driver.findElement(By.xpath("//*[@id=\"submit-button\"]")).click();
    }

    public String getSuccessMessage() {
        WebDriverWait wait = new WebDriverWait(driver, Constants.WAIT_TIMEOUT);
        WebElement marker = wait.until(webDriver -> webDriver.findElement(By.id("success-msg")));

        return marker.getText();
    }

}
