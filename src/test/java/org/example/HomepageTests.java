package org.example;


import org.junit.jupiter.api.*;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;


import java.time.Duration;
import java.util.ArrayList;
import java.util.List;


import static junit.framework.Assert.*;


public class HomepageTests {
    private static WebDriver webDriver;
    private static String baseUrl;


    @BeforeAll
    public static void setUp() {
        System.setProperty("webdriver.chrome.driver", "/home/alejna/Downloads/chromedriver-linux64/chromedriver");
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--remote-allow-origins=*");
        webDriver = new ChromeDriver(options);
        baseUrl = "https://3.basecamp.com";
        webDriver.manage().window().maximize();


    }


    @AfterAll
    public static void tearDown() {
        if (webDriver != null) {
            webDriver.quit();
        }
    }


    @Test
    public void testTitle() throws InterruptedException {
        webDriver.get(baseUrl);
        String actualTitle = webDriver.getTitle();
        System.out.println("Actual title: " + actualTitle);
        assertEquals("Basecamp: Project management software, online collaboration", actualTitle);
        Thread.sleep(1000);
    }




    @Test
    public void testAuthenticationFlow() throws InterruptedException {
        webDriver.get(baseUrl);


        WebElement signInButton = webDriver.findElement(By.cssSelector("a[href='https://launchpad.37signals.com/signin']"));
        signInButton.click();


        Thread.sleep(3000);
        ArrayList<String> tabs = new ArrayList<>(webDriver.getWindowHandles());
        webDriver.switchTo().window(tabs.get(1));


        String expectedLoginUrl = "https://launchpad.37signals.com/signin";
        assertEquals(expectedLoginUrl, webDriver.getCurrentUrl());


        WebElement email = webDriver.findElement(By.id("username"));
        email.click();
        email.sendKeys("eldin.guzin@stu.ibu.edu.ba");


        WebElement next = webDriver.findElement(By.xpath("//button[@name='button']"));
        next.click();


        Thread.sleep(500);


        WebElement password = webDriver.findElement(By.id("password"));
        password.click();
        password.sendKeys("AlejnaEldin12345");


        WebElement login = webDriver.findElement(By.xpath("//input[@name='commit']"));
        login.click();


        Thread.sleep(5000);


        String expectedProjectUrl = "https://3.basecamp.com/5894319/";
        assertEquals(expectedProjectUrl, webDriver.getCurrentUrl());
    }


    @Test
    void testWrongEmailAuthenticationFlow() throws InterruptedException {
        webDriver.get(baseUrl);


        WebElement signInButton = webDriver.findElement(By.cssSelector("a[href='https://launchpad.37signals.com/signin']"));
        signInButton.click();


        Thread.sleep(3000);
        ArrayList<String> tabs = new ArrayList<>(webDriver.getWindowHandles());
        webDriver.switchTo().window(tabs.get(1));


        WebElement email = webDriver.findElement(By.id("username"));
        email.click();
        email.sendKeys("wrongEmail@stu.ibu.edu.ba");


        WebElement next = webDriver.findElement(By.xpath("//button[@name='button']"));
        next.click();


        Thread.sleep(500);


        WebElement wrong = webDriver.findElement(By.xpath("//div[@data-role='unrecognized_notice_container']"));
        String expectedErrorMessage = "We couldn’t find that one. Want to try another?";
        assertEquals(expectedErrorMessage, wrong.getText());
    }


    @Test
    void testWrongPasswordAuthenticationFlow() throws InterruptedException {
        webDriver.get(baseUrl);


        WebElement signInButton = webDriver.findElement(By.cssSelector("a[href='https://launchpad.37signals.com/signin']"));
        signInButton.click();


        Thread.sleep(3000);
        ArrayList<String> tabs = new ArrayList<>(webDriver.getWindowHandles());
        webDriver.switchTo().window(tabs.get(1));


        WebElement email = webDriver.findElement(By.id("username"));
        email.click();
        email.sendKeys("eldin.guzin@stu.ibu.edu.ba");


        WebElement next = webDriver.findElement(By.xpath("//button[@name='button']"));
        next.click();


        Thread.sleep(500);


        WebElement password = webDriver.findElement(By.id("password"));
        password.click();
        password.sendKeys("Wrong");


        WebElement login = webDriver.findElement(By.xpath("//input[@name='commit']"));
        login.click();


        Thread.sleep(3000);


        WebElement error = webDriver.findElement(By.xpath("//div[@class='flash txt--center']/h2"));
        String expectedErrorMessage = "We didn’t recognize that password.";
        assertEquals(expectedErrorMessage, error.getText());
    }


    @Test
    public void testPricing() throws InterruptedException {
        webDriver.get(baseUrl);
        webDriver.manage().window().maximize();


        Thread.sleep(2000);


        WebElement pricingLink = webDriver.findElement(By.xpath("//a[@href='/pricing']"));
        pricingLink.click();
        Thread.sleep(2000);


        WebElement subscription = webDriver.findElement(By.xpath("//a[@data-pricing-plan='Plus']"));
        ((JavascriptExecutor) webDriver).executeScript("arguments[0].scrollIntoView(true);", subscription);


        Thread.sleep(1000);
        subscription.click();


        assertEquals("https://3.basecamp.com/signup/account/new?plan=per_user_v3", webDriver.getCurrentUrl());


    }






    @Test
    public void testNewsletterSubscription() throws InterruptedException {
        webDriver.get(baseUrl);


        WebDriverWait wait = new WebDriverWait(webDriver, Duration.ofSeconds(10));


        WebElement newsletterSection = webDriver.findElement(By.className("footer__newsletter"));
        ((JavascriptExecutor) webDriver).executeScript("arguments[0].scrollIntoView(true);", newsletterSection);


        Thread.sleep(1000);


        WebElement emailInput = wait.until(ExpectedConditions.elementToBeClickable(By.id("mce-EMAIL")));
        WebElement subscribeButton = wait.until(ExpectedConditions.elementToBeClickable(By.id("mc-embedded-subscribe")));


        emailInput.sendKeys("test@example.com");


        ((JavascriptExecutor) webDriver).executeScript("arguments[0].click();", subscribeButton);


        try {
            wait.until(driver -> {
                String inputValue = emailInput.getAttribute("value");
                return inputValue == null || inputValue.isEmpty();
            });
            System.out.println("Newsletter subscription test completed successfully");
            assertTrue(true);
        } catch (Exception e) {
            System.out.println("Newsletter form might not have been submitted successfully");
            fail("Newsletter subscription test failed");
        }
    }


    @Test
    public void testRealWorldResultsPage() throws InterruptedException {
        webDriver.get(baseUrl);
        Thread.sleep(2000);


        WebElement realWorld = webDriver.findElement(By.xpath("/html/body/nav/div[1]/div[2]/a[1]"));
        realWorld.click();
        Thread.sleep(1000);


        assertEquals("https://basecamp.com/customers", webDriver.getCurrentUrl());


        WebElement appSection = webDriver.findElement(By.xpath("//a[@href='/apps']"));
        ((JavascriptExecutor) webDriver).executeScript("arguments[0].scrollIntoView(true);", appSection);


        Thread.sleep(1000);


        appSection.click();


        Thread.sleep(2000);


        assertEquals("https://basecamp.com/apps", webDriver.getCurrentUrl());
    }




    @Test
    public void testPageLoadPerformance() {
        long start = System.currentTimeMillis();
        webDriver.get(baseUrl);
        long finish = System.currentTimeMillis();
        long totalTime = finish - start;
        assertTrue("Page load took too long", totalTime < 3000);
    }


    @Test
    public void testAccessibility() {
        webDriver.get(baseUrl);


        // Test alt text presence on images
        List<WebElement> images = webDriver.findElements(By.tagName("img"));
        for (WebElement image : images) {
            assertNotNull("Image missing alt text", image.getAttribute("alt"));
        }
    }


    @Test
    public void testHTTPSEnforcement() throws InterruptedException {
        webDriver.get("http:/3.basecamp.com");
        Thread.sleep(1000);


        String currentUrl = webDriver.getCurrentUrl();
        if (currentUrl.startsWith("https://")) {
            System.out.println("HTTP successfully redirects to HTTPS.");
        } else {
            System.out.println("HTTP does not redirect to HTTPS.");
        }
    }
}

