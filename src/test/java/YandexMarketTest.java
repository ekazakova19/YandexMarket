import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Assert;
import org.junit.Test;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;

public class YandexMarketTest extends BaseTest {
    private static final Logger logger = LogManager.getLogger(YandexMarketTest.class);

    @Test
    public void testComparisionTwoItems()
    {
        openYandexMarket();
        clickOn(By.id("27903767-tab"));
        clickOn(By.linkText("Электроника"));
        clickOn(By.linkText("Смартфоны"));
        filterByManufacturer("xiaomi","7893318_7701962");
        sortByPrice();
        addFirstItem();
        checkItemAddedToComparision();
        clearFilterByManufacturer("xiaomi","7893318_7701962");
        filterByManufacturer("huawei","7893318_459710");
        addFirstItem();
        checkItemAddedToComparision();
        openComparePage();

        logger.info("Checking items in comparision basket...");
        assertThatCountOfItemIs(2);
        logger.info("Check completed successfully");

        logger.info("Checking that OS option displayed on All Characteristics page...");
        clickOn(By.cssSelector("div.n-compare-toolbar>div.n-compare-show-controls span.link.n-compare-show-controls__all"));
        assertThatOSCharacteristicShown();
        logger.info("Check completed successfully");

        logger.info("Checking that OS option not displayed on Different Characteristics page...");
        clickOn(By.cssSelector("div.n-compare-toolbar>div.n-compare-show-controls span.link.n-compare-show-controls__diff"));
        assertThatOSCharacteristicSNotShown();
        logger.info("Check completed successfully");
    }

    public void clickOn(By by){
        try {
            WebElement element = driver.findElement(by);
            wait.until(ExpectedConditions.elementToBeClickable(by));
            element.click();
            logger.info("Successfully clicked on element with locator {}", by);
        } catch (NoSuchElementException e) {
           logger.error("Could not find element. Error {} ", e.getMessage());
        }
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


    public void assertThatCountOfItemIs(int expectedCount){
        Assert.assertEquals(expectedCount,driver.findElements(By.cssSelector("div.n-compare-content__line > div.n-compare-cell")).size());
    }

    public void openComparePage(){
        if(isComparePopupShown()){
            WebElement compareButton = driver.findElement(By.cssSelector("a.link.header2-menu__item.header2-menu__item_type_compare"));
            JavascriptExecutor js = (JavascriptExecutor) driver;
            js.executeScript("arguments[0].scrollIntoView();", compareButton);
            wait.until(ExpectedConditions.elementToBeClickable(compareButton));
            clickOn(By.cssSelector("a.link.header2-menu__item.header2-menu__item_type_compare"));
        }
    }

    public boolean isOSHidden(){
        try {
            driver.findElement(By.xpath("//div[contains(@class,'n-compare-row_hidden_yes')]/div/div[contains(@class,'n-compare-row-name') and contains(text(),'Операционная система')]"));
            return true;
        } catch (NoSuchElementException e) {
            return false;
        }
    }

    public void filterByManufacturer(String manufacturer, String checkboxIdLocator){
        WebElement searchPrepack = driver.findElement(By.cssSelector("#search-prepack"));
        searchPrepack.findElement(By.cssSelector("div._2Hue1bCg-N > fieldset > footer > button")).click();
        searchPrepack.findElement(By.id("7893318-suggester")).sendKeys(manufacturer);
        actions.moveToElement(searchPrepack.findElement(By.id(checkboxIdLocator))).click().build().perform();
        logger.info("Filter by {} is applied",manufacturer);
        waitForFilterResultApply();
        searchPrepack.findElement(By.cssSelector("div._2Hue1bCg-N > fieldset > footer > button")).click();
    }

    public void clearFilterByManufacturer(String manufacturer, String checkboxIdLocator){
        WebElement searchPrepack = driver.findElement(By.cssSelector("#search-prepack"));
        searchPrepack.findElement(By.cssSelector("div._2Hue1bCg-N > fieldset > footer > button")).click();
        searchPrepack.findElement(By.id("7893318-suggester")).sendKeys(manufacturer);

        WebElement checkbox = searchPrepack.findElement(By.id(checkboxIdLocator));
        if(checkbox.getAttribute("checked").equalsIgnoreCase("true")){
            actions.moveToElement(searchPrepack.findElement(By.id(checkboxIdLocator))).click().build().perform();
            logger.info("Filter by {} is cleared", manufacturer);
            waitForFilterResultApply();
        }
        else {
            System.out.print("unchecked already");
            logger.warn("Filter by {} not applied already",manufacturer);
        }
        searchPrepack.findElement(By.cssSelector("div._2Hue1bCg-N > fieldset > footer > button")).click();
    }

    public void sortByPrice(){
        actions.moveToElement(driver.findElement(By.xpath("//div[contains(@class,n-filter-panel-dropdown__item)]"))).perform();
        driver.findElement(By.linkText("по цене")).click();
        logger.info("Sorting by price is applied");
        waitForFilterResultApply();
    }


    public void checkItemAddedToComparision(){
        if(isComparePopupShown()){
            By closeButton= By.cssSelector("div.popup-informer__content > div.popup-informer__close");
            WebElement popup =  driver.findElement(By.cssSelector("div.popup-informer"));
            wait.until(ExpectedConditions.and(ExpectedConditions.visibilityOf(popup),ExpectedConditions.elementToBeClickable(popup)));
            wait.until(ExpectedConditions.and(ExpectedConditions.visibilityOfElementLocated(closeButton),ExpectedConditions.elementToBeClickable(closeButton)));
            clickOn(closeButton);
            try {
                wait.until(ExpectedConditions.invisibilityOf(popup));
            } catch (TimeoutException e) {
                logger.warn("Popup not closed");
            }
            logger.info("Item has been added to comparision");
        }
        logger.warn("Popup not appears");
    }

    public boolean isComparePopupShown(){
        try {
            driver.findElement(By.cssSelector("div.popup-informer"));
            logger.info("Popup appears");
            return true;
        } catch (NoSuchElementException e) {
           return false;
        }
    }

    public void waitForFilterResultApply(){
        logger.info("Waiting for page reload");
        wait.until(ExpectedConditions.invisibilityOfElementLocated(By.cssSelector("div.n-filter-applied-results > div.preloadable__preloader")));
        logger.info("Page reloaded");
    }

    public void addFirstItem(){
        actions.moveToElement(driver.findElement(By.cssSelector(":nth-child(1) > .n-snippet-cell2__hover > div > div > div"))).perform();
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(":nth-child(1) > .n-snippet-cell2__hover > div > div > div")));
        driver.findElement(By.cssSelector(":nth-child(1) > .n-snippet-cell2__hover > div > div > div")).click();
        logger.info("Add the smartphone to comparision - completed");
    }

    public void openYandexMarket(){
        driver.get("https://ya.ru/");
        driver.get("https://market.yandex.ru/");
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("27903767-tab")));
        logger.info("https://market.yandex.ru/ is opened");
    }
}
