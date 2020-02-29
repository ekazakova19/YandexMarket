package Pages;

import Tests.YandexMarketTest;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class YandexMarketPage  {
    private WebDriver driver;
    private WebDriverWait wait;
    private Actions actions;
    public final By CATEGORIES_TAB = By.id("27903767-tab");
    public final By ELECTRONICS = By.linkText("Электроника");
    public final By SMARTPHONE = By.linkText("Смартфоны");
    public final By XIAOMI_CHECKBOX = By.id("7893318_7701962");
    public final By HUAWEI_CHECKBOX = By.id("7893318_459710");
    private final By SHOW_ALL_MANUF_BUTTON = By.cssSelector("fieldset[data-autotest-id=\"7893318\"] button");
    private final By MINIMAZE_ALL_MANUF_BUTTON = By.cssSelector("fieldset[data-autotest-id=\"7893318\"] button");
    private final By MANUF_INPUT_FIELD = By.id("7893318-suggester");
    private final By PRELOADER =By.cssSelector("div.n-filter-applied-results > div.preloadable__preloader");
    private final By FILTER_BY_PRICE = By.linkText("по цене");
    private final By ADD_TO_COMPARE_BUTTON = By.cssSelector(":nth-child(1) > .n-snippet-cell2__hover > div > div > div");
    private final By COMPARE_POPUP = By.cssSelector(("div.popup-informer__content"));

    private final By CLOSE_COMPARE_POPUP = By.cssSelector("div.popup-informer__content > div.popup-informer__close");
    private final By COMPARE_BUTTON = By.cssSelector("a.link.header2-menu__item.header2-menu__item_type_compare");
    private static final Logger logger = LogManager.getLogger(YandexMarketTest.class);

    public YandexMarketPage(WebDriver driver) {
        this.driver = driver;
        wait = new WebDriverWait(driver,7);
        actions = new Actions(driver);
    }

    public void clickOn(By by){
        try {
            WebElement element = driver.findElement(by);
            wait.until(ExpectedConditions.elementToBeClickable(by));
            element.click();
            logger.info("Successfully clicked on element with locator {}", by);
        } catch (NoSuchElementException e) {
            logger.error("Could not find element. Error {} ", e.getMessage());
            throw e;
        }
    }

     public void filterByManufacturer(String manufacturer, By checkboxIdLocator){
        clickOn(SHOW_ALL_MANUF_BUTTON);
        driver.findElement(MANUF_INPUT_FIELD).sendKeys(manufacturer);
        actions.moveToElement(driver.findElement(checkboxIdLocator)).click().perform();
        logger.info("Filter by {} is applied",manufacturer);
        waitForFilterResultApply();
        clickOn(SHOW_ALL_MANUF_BUTTON);
    }
    public void waitForFilterResultApply(){
        logger.info("Waiting for page reload");
        wait.until(ExpectedConditions.attributeToBe(By.cssSelector("div.n-filter-applied-results__content.preloadable"),"style","height: auto;"));
        logger.info("Page reloaded");
    }

    public void clearFilterByManufacturer(String manufacturer, By checkboxIdLocator){
        clickOn(SHOW_ALL_MANUF_BUTTON);
        driver.findElement(MANUF_INPUT_FIELD).sendKeys(manufacturer);
        WebElement checkbox = driver.findElement(checkboxIdLocator);
        if(checkbox.getAttribute("checked").equalsIgnoreCase("true")){
            actions.moveToElement(checkbox).click().perform();
            logger.info("Filter by {} is cleared", manufacturer);
            waitForFilterResultApply();
        }
        else {
            logger.warn("Filter by {} had not been applied ",manufacturer);
        }
        clickOn(MINIMAZE_ALL_MANUF_BUTTON);
    }

    public void sortByPrice(){
        clickOn(FILTER_BY_PRICE);
        logger.info("Sorting by price is applied");
        waitForFilterResultApply();
    }
    public void addFirstItem(){
        actions.moveToElement(driver.findElement(ADD_TO_COMPARE_BUTTON)).perform();
        wait.until(ExpectedConditions.visibilityOfElementLocated(ADD_TO_COMPARE_BUTTON));
        clickOn(ADD_TO_COMPARE_BUTTON);
    }


    public void checkItemAddedToComparision(){
        if(isComparePopupShown()){
            wait.until(ExpectedConditions.and(ExpectedConditions.visibilityOfElementLocated(COMPARE_POPUP),ExpectedConditions.elementToBeClickable(COMPARE_POPUP)));
            closeComparePopup();
            logger.info("Item has been added to comparision");
        }
        else {
            logger.warn("Popup not appears");
        }
    }
    public boolean isComparePopupShown(){
        try {
            driver.findElement(By.cssSelector("div.popup-informer"));
            logger.info("Popup exists on the page");
            return true;
        } catch (NoSuchElementException e) {
            return false;
        }
    }

    public void closeComparePopup(){
        wait.until(ExpectedConditions.and(ExpectedConditions.visibilityOfElementLocated(CLOSE_COMPARE_POPUP),ExpectedConditions.elementToBeClickable(CLOSE_COMPARE_POPUP)));
        clickOn(CLOSE_COMPARE_POPUP);
        try {
            wait.until(ExpectedConditions.invisibilityOfElementLocated(COMPARE_POPUP));
            logger.info("Popup closed");
        } catch (TimeoutException e) {
            logger.warn("Popup not closed");
            clickOn(CLOSE_COMPARE_POPUP);
        }
    }

    public void openComparePage(){
        WebElement compareButton = driver.findElement(COMPARE_BUTTON);
        if(isComparePopupShown()){
            JavascriptExecutor js = (JavascriptExecutor) driver;
            js.executeScript("arguments[0].scrollIntoView();", compareButton);
        }
        wait.until(ExpectedConditions.elementToBeClickable(compareButton));
        clickOn(COMPARE_BUTTON);
    }

    public void openCompareLink(){
        driver.get("https://market.yandex.ru/compare");
    }

    public void openYandexMarket(){
        driver.get("https://ya.ru/");
        driver.get("https://market.yandex.ru/");
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("27903767-tab")));
        logger.info("https://market.yandex.ru/ is opened");
    }

}
