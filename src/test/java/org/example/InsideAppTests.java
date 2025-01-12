package org.example;

import org.junit.jupiter.api.*;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;

import static junit.framework.Assert.*;

public class InsideAppTests {

    private static WebDriver webDriver;
    private static String baseUrl;

    @BeforeAll
    public static void setUp() throws InterruptedException {
        System.setProperty("webdriver.chrome.driver", "/home/pelda/Downloads/chromedriver-linux64/chromedriver");
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--remote-allow-origins=*");
        webDriver = new ChromeDriver(options);
        baseUrl = "https://3.basecamp.com";
        webDriver.manage().window().maximize();

        login();
    }

    private static void login() throws InterruptedException {
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
        password.sendKeys("AlejnaEldin12345");

        WebElement login = webDriver.findElement(By.xpath("//input[@name='commit']"));
        login.click();

        Thread.sleep(5000);
    }

    @AfterAll
    public static void tearDown() {
        if (webDriver != null) {
            webDriver.quit();
        }
    }

    @Test
    void testActivity() throws InterruptedException {

        WebElement activity = webDriver.findElement(By.xpath("//a[@href='/5894319/reports/progress']"));
        activity.click();

        Thread.sleep(2000);

        WebElement summary = webDriver.findElement(By.xpath("//summary"));
        summary.click();
        Thread.sleep(500);

        WebElement specific = webDriver.findElement(By.xpath("//a[contains(@href, 'users')]"));
        specific.click();
        Thread.sleep(500);

        WebElement person = webDriver.findElement(By.xpath("//*[@id='person_47402743']"));
        person.click();
        Thread.sleep(500);

        WebElement title = webDriver.findElement(By.xpath("//h1[@class='perma__title']"));
        String expectedTitle = "Eldin Guzin’s activity";
        assertEquals(expectedTitle, title.getText());
    }

    // This test makes a new project Todo and makes it possible to add todos and comments later on
    @Test
    void testProjectToDo() throws InterruptedException {
        String randomWord = generateRandomWord();
        String randomNumber = String.valueOf(new Random().nextInt(1000));
        String testMessage = randomWord + " " + randomNumber;

        Thread.sleep(500);

        WebElement project = webDriver.findElement(By.xpath("/html/body/main/div[4]/section[1]/turbo-frame/div[1]/span/article/a/div"));
        project.click();

        Thread.sleep(1000);

        WebElement todoCard = webDriver.findElement(By.xpath("//a[contains(@class, 'card__link')]//span[@class='card__title'][text()='To-dos']/ancestor::a"));
        todoCard.click();
        Thread.sleep(2000);

        WebElement add = webDriver.findElement(By.xpath("//a[contains(@class, 'btn--primary')]"));
        add.click();
        Thread.sleep(500);

        WebElement input = webDriver.findElement(By.xpath("//*[@id='todolist_name']"));
        input.sendKeys(testMessage);

        WebElement post = webDriver.findElement(By.xpath("//input[@type='submit']"));
        post.click();

        Thread.sleep(1000);

        WebElement titleFound = webDriver.findElement(By.xpath("//h3[@class='todolist__title']//a[contains(@data-behavior, 'prevent_link_action')]"));

        String titleFoundText = titleFound.getText();

        assertEquals(testMessage, titleFoundText);

    }

    @Test
    void createAnnoucement() throws InterruptedException {
        String randomWord = generateRandomWord();
        String randomNumber = String.valueOf(new Random().nextInt(1000));
        String testMessage = randomWord + " " + randomNumber;
        Thread.sleep(500);

        WebElement project = webDriver.findElement(By.xpath("/html/body/main/div[4]/section[1]/turbo-frame/div[1]/span/article/a/div"));
        project.click();

        Thread.sleep(2000);

        WebElement messageBoard = webDriver.findElement(By.xpath("//div[@class='card__content'][.//span[contains(@style, 'var(--color-blue-50);')]]"));
        messageBoard.click();
        Thread.sleep(500);

        WebElement addMessage = webDriver.findElement(By.xpath("//a[contains(@class, 'btn--primary')]"));
        addMessage.click();
        Thread.sleep(500);

        WebElement title = webDriver.findElement(By.xpath("//*[@id='message_subject']"));
        title.click();
        title.sendKeys(testMessage);

        WebElement content = webDriver.findElement(By.xpath("//*[@id='message_content']"));
        content.click();
        content.sendKeys(testMessage);
        Thread.sleep(500);

        //This selects the no one checkbox which tells it to not notify users when this is created
        WebElement checkbox = webDriver.findElement(By.xpath("/html/body/main/div[2]/form/footer/div[1]/div/div[3]/label/div/span"));
        checkbox.click();

        WebElement post = webDriver.findElement(By.xpath("//button[@value='active']"));
        post.click();
        Thread.sleep(2000);

        WebElement titleTest = webDriver.findElement(By.xpath("//a[@data-turbo-method='get']"));

        String titleTestText = titleTest.getText();

        assertEquals(testMessage, titleTestText);
    }

    @Test
    void testChat() throws InterruptedException {
        String randomWord = generateRandomWord();
        String randomNumber = String.valueOf(new Random().nextInt(1000));
        String testMessage = randomWord + " " + randomNumber;
        Thread.sleep(500);

        WebElement project = webDriver.findElement(By.xpath("/html/body/main/div[4]/section[1]/turbo-frame/div[1]/span/article/a/div"));
        project.click();

        Thread.sleep(1000);

        WebElement chat = webDriver.findElement(By.xpath("//div[contains(@class, 'card__content')]//span[@class='card__title'][text()='Chat']"));
        chat.click();

        Thread.sleep(1000);

        WebElement chatInput = webDriver.findElement(By.cssSelector("trix-editor.formatted_content.chat__mic"));
        chatInput.click();
        chatInput.sendKeys(testMessage);

        Actions actions = new Actions(webDriver);
        actions.sendKeys(Keys.RETURN).perform();

        Thread.sleep(2000);

        WebElement messages = webDriver.findElement(By.xpath("//bc-infinite-page"));

        String messagesText = messages.getText();

        try {
            assertTrue("Message not found in chat", messagesText.contains("testing"));
        } catch (AssertionError e) {
            System.out.println("Test failed: Message 'testing' was not found in chat");
            throw e;
        } catch (Exception e) {
            System.out.println("Test failed: " + e.getMessage());
            throw e;
        }
    }

    private String generateRandomWord() {
        String[] words = {"test", "hello", "chat", "message", "selenium", "automated"};
        return words[new Random().nextInt(words.length)];
    }

    @Test
    void testDocsAndFiles() throws InterruptedException {
        String randomWord = generateRandomWord();
        String randomNumber = String.valueOf(new Random().nextInt(1000));
        String testMessage = randomWord + " " + randomNumber;
        Thread.sleep(500);
        WebElement project = webDriver.findElement(By.xpath("/html/body/main/div[4]/section[1]/turbo-frame/div[1]/span/article/a/div"));
        project.click();
        Thread.sleep(1000);

        WebElement docs = webDriver.findElement(By.xpath("//section[contains(@class, 'centered')]"));
        docs.click();
        Thread.sleep(1000);

        WebElement add = webDriver.findElement(By.xpath("//button[contains(@title, 'a')]"));
        add.click();
        Thread.sleep(500);

        WebElement newFile = webDriver.findElement(By.linkText("Start a new doc"));
        newFile.click();
        Thread.sleep(2000);

        WebElement title = webDriver.findElement(By.xpath("//*[@id='document_title']"));
        title.sendKeys(testMessage);

        WebElement checkbox = webDriver.findElement(By.xpath("/html/body/main/div[2]/form/footer/div[1]/div/div[3]/label/div/span"));
        checkbox.click();
        Thread.sleep(2000);

        WebElement post = webDriver.findElement(By.xpath("//button[@name='status']"));
        post.click();
        Thread.sleep(2000);

        WebElement docTitle = webDriver.findElement(By.cssSelector("h1.message__subject .inline-edit-form a"));

        try {
            assertEquals(testMessage, docTitle.getText());
        } catch (AssertionError e) {
            System.out.println("Expected title: " + testMessage);
            System.out.println("Actual title: " + docTitle.getText());
            throw e;
        }
    }

    @Test
    void testSchedule() throws InterruptedException {
        String randomWord = generateRandomWord();
        String randomNumber = String.valueOf(new Random().nextInt(1000));
        String testMessage = randomWord + " " + randomNumber;

        Thread.sleep(1000);
        WebElement project = webDriver.findElement(By.xpath("/html/body/main/div[4]/section[1]/turbo-frame/div[1]/span/article/a/div"));
        project.click();
        Thread.sleep(1000);

        WebElement scheduleCard = webDriver.findElement(By.xpath("//a[contains(@class, 'card__link')]//span[@class='card__title'][text()='Schedule']/ancestor::a"));
        scheduleCard.click();
        Thread.sleep(1000);

        WebElement add = webDriver.findElement(By.xpath("//a[contains(@class, 'app-mobile__hide')]"));
        add.click();
        Thread.sleep(1000);

        WebElement eventName = webDriver.findElement(By.xpath("//*[@id='schedule_entry_summary']"));
        eventName.sendKeys(testMessage);

        WebElement label = webDriver.findElement(By.xpath("//label[@class='binary-toggle']"));
        label.click();

        WebElement button = webDriver.findElement(By.xpath("//button[@name='status']"));
        button.click();
        Thread.sleep(2000);

        WebElement events = webDriver.findElement(By.xpath("//div[@class='schedule-day__events']"));
        String eventsText = events.getText();

        if(eventsText.contains(testMessage)) {
            System.out.println("test passed");
        }
        else{
            fail("test failed");
        }
    }

    @Test
    void testingPings() throws InterruptedException {
        String randomWord = generateRandomWord();
        String randomNumber = String.valueOf(new Random().nextInt(1000));
        String testMessage = randomWord + " " + randomNumber;
        Thread.sleep(1000);

        WebElement pings = webDriver.findElement(By.xpath("//a[@href='/5894319/circles']"));
        pings.click();
        Thread.sleep(1000);

        WebElement alejna = webDriver.findElement(By.xpath("//a[contains(@class, 'centered')]"));
        alejna.click();
        Thread.sleep(2000);

        WebElement chatInput = webDriver.findElement(By.cssSelector("trix-editor.formatted_content.chat__mic"));
        chatInput.click();
        chatInput.sendKeys(testMessage);

        Actions actions = new Actions(webDriver);
        actions.sendKeys(Keys.RETURN).perform();

        Thread.sleep(2000);

        WebElement messages = webDriver.findElement(By.xpath("//bc-infinite-page"));

        String messagesText = messages.getText();

        try {
            assertTrue("Message not found in chat", messagesText.contains(testMessage));
        } catch (AssertionError e) {
            System.out.println("Test failed: Message 'testing' was not found in chat");
            throw e;
        } catch (Exception e) {
            System.out.println("Test failed: " + e.getMessage());
            throw e;
        }
    }

    @Test
    void testChangeBio() throws InterruptedException {
        String randomWord = generateRandomWord();
        String randomNumber = String.valueOf(new Random().nextInt(1000));
        String testMessage = randomWord + " " + randomNumber;
        Thread.sleep(1000);

        WebElement myProfile = webDriver.findElement(By.xpath("//a[@class='nav__link--me']"));
        myProfile.click();
        Thread.sleep(1000);

        WebElement profileLink = webDriver.findElement(By.xpath("/html/body/div[1]/nav/div[2]/div/div/div/section[2]/ul/li[1]/a"));
        profileLink.click();
        Thread.sleep(2000);

        WebElement bio = webDriver.findElement(By.xpath("//*[@id='person_bio']"));
        bio.clear();
        bio.sendKeys(testMessage);

        WebElement submit = webDriver.findElement(By.xpath("//input[@type='submit']"));
        submit.click();
        Thread.sleep(2000);
    }
}
