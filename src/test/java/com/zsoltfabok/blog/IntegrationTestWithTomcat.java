package com.zsoltfabok.blog;
import static org.junit.Assert.assertEquals;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;

public class IntegrationTestWithTomcat {
  private static EmbeddedTomcat tomcat = new EmbeddedTomcat();
  private static WebDriver browser;

  @BeforeClass
  public static void setUp() {
    tomcat.start();
    tomcat.deploy("spike");
    browser = new HtmlUnitDriver();
  }

  @Test
  public void test() {
    browser.get(tomcat.getApplicationUrl("spike"));
    assertEquals("App", browser.findElement(By.id("name")).getText());
  }

  @AfterClass
  public static void tearDown() {
    browser.close();
    tomcat.stop();
  }
}
