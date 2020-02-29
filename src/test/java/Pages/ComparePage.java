package Pages;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Assert;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;

public class ComparePage {
    private WebDriver driver;
    private final By COMPARE_ITEM = By.cssSelector("div.n-compare-content__line > div.n-compare-cell");
    private final By HIDDEN_OS =By.xpath("//div[contains(@class,'n-compare-row_hidden_yes')]/div/div[contains(@class,'n-compare-row-name') and contains(text(),'Операционная система')]");
    private final By ALL_CHARACTERISTIC_LINK = By.cssSelector("div.n-compare-toolbar>div.n-compare-show-controls span.link.n-compare-show-controls__all");
    private final By DIFF_CHARACTERISTIC_LINK =By.cssSelector("div.n-compare-toolbar>div.n-compare-show-controls span.link.n-compare-show-controls__diff");
    private static final Logger logger = LogManager.getLogger(ComparePage.class);

    public ComparePage(WebDriver driver) {
        this.driver = driver;
    }
    public void openAllCharacteristics(){
        logger.info("Open all characteristics");
        driver.findElement(ALL_CHARACTERISTIC_LINK).click();
    }
    public void openDiffCharacteristics(){
        logger.info("Open different characteristics");
        driver.findElement(DIFF_CHARACTERISTIC_LINK).click();
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
