package com.zsoltfabok.blog;

import static org.junit.Assert.assertEquals;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;

public class IntegrationTestWithJetty {

  private static EmbeddedJetty jetty = new EmbeddedJetty();
  private static WebDriver browser;

  @BeforeClass
  public static void setUp() {
    jetty.start("spike");
    browser = new HtmlUnitDriver();
  }

  @Test
  public void test() {
    browser.get(jetty.getApplicationUrl("spike"));
    assertEquals("App", browser.findElement(By.id("name")).getText());
  }

  @AfterClass
  public static void tearDown() {
    browser.close();
    jetty.stop();
  }
}
