package com.jpbeta.tools.weibo.auth;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

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

    String _authUrl = getPropVal("authUrl");
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

    String title = driver.getTitle();
    System.out.println("Page title is: " + title);
    if (!"authOK".equals(title)) {
      sendMail();
    }
    String pageText = driver.findElement(By.tagName("body"))
      .getText();
    System.out.println(pageText);
    sleep(1000);
    // Close the browser
    driver.quit();
  }

  /**
   *
   */
  private static void sendMail() {
    try {
      Properties property = new Properties();
      // SMTPを使う場合
      property.put("mail.smtp.auth", "true");
      property.put("mail.smtp.starttls.enable", "true");
      property.put("mail.smtp.host", getPropVal("smtpHost"));
      property.put("mail.smtp.port", getPropVal("smtpPort"));
      property.put("mail.smtp.debug", "true");
      Session session = Session.getInstance(property, new javax.mail.Authenticator() {
        protected PasswordAuthentication getPasswordAuthentication() {
          return new PasswordAuthentication(getPropVal("mailAuthUser"), getPropVal("mailAuthPass"));
        }
      });
      MimeMessage mimeMessage = new MimeMessage(session);
      InternetAddress toAddress = new InternetAddress(getPropVal("toEmailAddr"));
      mimeMessage.setRecipient(Message.RecipientType.TO, toAddress);
      InternetAddress fromAddress = new InternetAddress(getPropVal("fromEmailAddr"));
      mimeMessage.setFrom(fromAddress);
      mimeMessage.setSubject("Auto Login Failure", "ISO-2022-JP");
      mimeMessage.setText("Auto Login Failure", "ISO-2022-JP");
      Transport.send(mimeMessage);
    } catch (Exception e) {
      e.printStackTrace();
    }
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
