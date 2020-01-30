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
        driver.findElement(By.id("27903768-tab")).click();
        driver.findElement(By.linkText("Электроника")).click();
        driver.findElement(By.linkText("Смартфоны")).click();
        filterByManufacturer("xiaomi","7893318_7701962");
        sortByPrice();
        addFirstItem();
        checkItemAddedToComparision();
        clearFilterByManufacturer("xiaomi","7893318_7701962");
        filterByManufacturer("huawei","7893318_459710");
        addFirstItem();
        checkItemAddedToComparision();

        logger.info("Checking items in comparision basket...");
        driver.findElement(By.cssSelector("a.link.header2-menu__item.header2-menu__item_type_compare")).click();
        Assert.assertEquals(2,driver.findElements(By.cssSelector("div.n-compare-content__line > div.n-compare-cell")).size());
        logger.info("Check completed successfully");

        logger.info("Checking common OS option...");
        driver.findElement(By.cssSelector("div.n-compare-toolbar>div.n-compare-show-controls span.link.n-compare-show-controls__all")).click();
        Assert.assertTrue("Operation System characteristic is not shown",isOSCharacteristicShown());
        logger.info("Check completed successfully");

        logger.info("Checking different OS option...");
        driver.findElement(By.cssSelector("div.n-compare-toolbar>div.n-compare-show-controls span.link.n-compare-show-controls__diff")).click();
        Assert.assertFalse("Operation System characteristic is shown",isOSCharacteristicShown());
        logger.info("Check completed successfully");
    }

    public boolean isOSCharacteristicShown(){
        try {
            driver.findElement(By.xpath("//div[contains(@class,'n-compare-row_hidden_yes')]/div/div[contains(@class,'n-compare-row-name') and contains(text(),'Операционная система')]"));
            return false;
        } catch (NoSuchElementException e) {
            return true;
        } catch (Exception e){
            logger.error(e);
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
        driver.findElement(By.cssSelector("div.popup-informer__pane .popup-informer__title"));
        logger.info("Item has been added to comparision");
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
    }

    public void openYandexMarket(){
        driver.get("https://ya.ru/");
        Cookie cookie1 = driver.manage().getCookieNamed("yandex_gid");
        Cookie cookie2 = driver.manage().getCookieNamed("yandexuid");
        driver.get("https://market.yandex.ru/");
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//button[contains(@id, '27903768-tab')]")));
        driver.manage().addCookie(new Cookie("yandex_gid", cookie1.getValue()));
        driver.manage().addCookie(new Cookie("yandexuid", cookie2.getValue()));
        logger.info("https://market.yandex.ru/ is opened");
    }


}
