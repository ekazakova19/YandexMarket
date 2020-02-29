package Pages;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Assert;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class ComparePage {
    private WebDriver driver;
    private WebDriverWait wait;
    private Actions actions;

    private final By COMPARE_ITEM = By.cssSelector("div.n-compare-content__line > div.n-compare-cell");
    private final By HIDDEN_OS =By.xpath("//div[contains(@class,'n-compare-row_hidden_yes')]/div/div[contains(@class,'n-compare-row-name') and contains(text(),'Операционная система')]");
    private final By ALL_CHARACTERISTIC_LINK = By.cssSelector("div.n-compare-toolbar>div.n-compare-show-controls span.link.n-compare-show-controls__all > span");
    private final By DIFF_CHARACTERISTIC_LINK =By.cssSelector("div.n-compare-toolbar>div.n-compare-show-controls span.link.n-compare-show-controls__diff");
    private static final Logger logger = LogManager.getLogger(ComparePage.class);

    public ComparePage(WebDriver driver) {
        this.driver = driver;
        wait = new WebDriverWait(driver,5);
        actions = new Actions(driver);
    }

    public void openAllCharacteristics(){
        wait.until(ExpectedConditions.elementToBeClickable(ALL_CHARACTERISTIC_LINK));
        driver.findElement(ALL_CHARACTERISTIC_LINK).click();
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("div.n-compare-toolbar>div.n-compare-show-controls span.link.n-compare-show-controls__all.n-compare-show-controls__all_active_yes")));
        logger.info("All characteristics tab is opened");
    }

    public void openDiffCharacteristics(){
        wait.until(ExpectedConditions.elementToBeClickable(DIFF_CHARACTERISTIC_LINK));
        driver.findElement(DIFF_CHARACTERISTIC_LINK).click();
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("div.n-compare-toolbar>div.n-compare-show-controls span.link.n-compare-show-controls__diff.n-compare-show-controls__diff_active_yes")));
        logger.info("Different characteristics tab is opened");
    }

    public void assertThatCountOfItemIs(int expectedCount){
        Assert.assertEquals(expectedCount,driver.findElements(COMPARE_ITEM).size());
    }
    public void assertThatOSCharacteristicShown(){
        if(isOSHidden()){
            logger.error("Actual result - OS not shown,  but expected - shown");
            Assert.fail();
        }
        logger.info("OS is shown");

    }
    public void assertThatOSCharacteristicSNotShown(){
        if(!isOSHidden()){
            logger.error("Actual result - OS shown, but expected - not shown");
            Assert.fail();
        }
        logger.info("OS is not shown");
    }

    public boolean isOSHidden(){
        try {
            driver.findElement(HIDDEN_OS);
            return true;
        } catch (NoSuchElementException e) {
            return false;
        }
    }

}
