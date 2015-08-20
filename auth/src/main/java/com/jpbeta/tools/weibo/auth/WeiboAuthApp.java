package com.jpbeta.tools.weibo.auth;


import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Wait;
import org.openqa.selenium.support.ui.WebDriverWait;


/**
 * WeiboAuthApp
 *
 */
public class WeiboAuthApp {
  public static final String PROPERTIES_FILE_PATH = "./resources/setting.properties";
  public static void main(String[] args) {

    String _authUrl =  getPropVal("authUrl");
    String _userId = getPropVal("userId");
    String _password = getPropVal("passWd");
    String _clickElement = getPropVal("clickElement");
    String _driverName = getPropVal("driverName");
    String _driverPath = getPropVal("driverPath");

    System.setProperty(_driverName, _driverPath);

    WebDriver driver = new ChromeDriver();
    driver.get(_authUrl);
    sleep(9000);
    Wait<WebDriver> wait = new WebDriverWait(driver, 30);
    WebElement userId = driver.findElement(By.name("userId"));
    userId.sendKeys(_userId);
    WebElement passwd = driver.findElement(By.name("passwd"));
    passwd.sendKeys(_password);
    WebElement button = wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector(_clickElement)));
    sleep(9000);
    button.click();
    sleep(5000);

    System.out.println("Page title is: " + driver.getTitle());
    String pageText = driver.findElement(By.tagName("body")).getText();
    System.out.println(pageText);
    sleep(1000);
    // Close the browser
    driver.quit();
  }

  private static void sleep(int microtime) {
    try {
      Thread.sleep(microtime);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
  }

  public static String getPropVal(String key) {

    Properties conf = new Properties();
    try {
      conf.load(new FileInputStream(PROPERTIES_FILE_PATH));
    } catch (IOException e) {
      System.err.println("Cannot open " + PROPERTIES_FILE_PATH + ".");
      e.printStackTrace();
      System.exit(-1); // プログラム終了
    }
    return conf.getProperty(key);
  }
}
