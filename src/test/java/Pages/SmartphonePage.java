package Pages;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class SmartphonePage {
    private WebDriver driver;
    private WebDriverWait wait;
    private Actions actions;

    private final By SHOW_ALL_MANUF_BUTTON = By.cssSelector("fieldset[data-autotest-id=\"7893318\"] button");
    private final By MINIMAZE_ALL_MANUF_BUTTON = By.cssSelector("fieldset[data-autotest-id=\"7893318\"] button");
    private final By MANUF_INPUT_FIELD = By.id("7893318-suggester");
    private final By ADD_TO_COMPARE_BUTTON = By.cssSelector(":nth-child(1) > .n-snippet-cell2__hover > div > div > div");
    private final By COMPARE_POPUP = By.cssSelector("div.popup-informer");
    private final By FILTER_BY_PRICE = By.linkText("по цене");
    private final By SHOW_MORE_PAGES = By.cssSelector("div.n-pager-more");

    public final By HUAWEI_CHECKBOX = By.id("7893318_459710");
    public final By XIAOMI_CHECKBOX = By.id("7893318_7701962");

    private static final Logger logger = LogManager.getLogger(ComparePage.class);

    public SmartphonePage(WebDriver driver) {
        this.driver = driver;
        wait = new WebDriverWait(driver,10);
        actions = new Actions(driver);
    }


    public void filterByManufacturer(String manufacturer, By checkboxIdLocator){
        clickOnShowAllManufacturer();
        enterManufacturerName(manufacturer);
        actions.moveToElement(driver.findElement(checkboxIdLocator)).click().perform();
        waitForFilterResultApply();
        logger.info("Filter by {} is applied",manufacturer);
        clickOnMinimazeManufacturer();
    }

    public void clearFilterByManufacturer(String manufacturer, By checkboxIdLocator){
        clickOnShowAllManufacturer();
        enterManufacturerName(manufacturer);
        WebElement checkbox = driver.findElement(checkboxIdLocator);
        uncheckCheckbox(checkbox);
        waitForFilterResultApply();
        logger.info("Filter by {} is cleared",manufacturer);
        clickOnMinimazeManufacturer();
    }

    public void clickOnShowAllManufacturer(){
        driver.findElement(SHOW_ALL_MANUF_BUTTON).click();
        wait.until(ExpectedConditions.elementToBeClickable(MANUF_INPUT_FIELD));
    }

    public void enterManufacturerName(String manufacturer){
        driver.findElement(MANUF_INPUT_FIELD).sendKeys(manufacturer);
        wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("fieldset[data-autotest-id=\"7893318\"] input[type=checkbox]")));
    }

    public void clickOnMinimazeManufacturer(){
        driver.findElement(MINIMAZE_ALL_MANUF_BUTTON).click();
        wait.until(ExpectedConditions.invisibilityOfElementLocated(MANUF_INPUT_FIELD));
    }

    public void uncheckCheckbox(WebElement checkbox){
        if(checkbox.getAttribute("checked").equalsIgnoreCase("true")){
            actions.moveToElement(checkbox).click().perform();
            wait.until(ExpectedConditions.elementSelectionStateToBe(checkbox,false));
        }
        else {
            logger.warn("Checkbox not checked. Skip step");
        }
    }


    public void waitForFilterResultApply(){
        logger.info("Waiting for page reload");
        wait.until(ExpectedConditions.attributeToBe(By.cssSelector("div.n-filter-applied-results__content.preloadable"),"style","height: auto;"));
        logger.info("Page reloaded");
    }

    public void addFirstItem(){
        actions.moveToElement(driver.findElement(ADD_TO_COMPARE_BUTTON)).perform();
        wait.until(ExpectedConditions.visibilityOfElementLocated(ADD_TO_COMPARE_BUTTON));
        driver.findElement(ADD_TO_COMPARE_BUTTON).click();
        wait.until(ExpectedConditions.visibilityOfElementLocated(COMPARE_POPUP));
        logger.info("Smartphone has been added to compare");
    }

    public void sortByPrice(){
        driver.findElement(FILTER_BY_PRICE).click();
        wait.until(ExpectedConditions.elementToBeClickable(SHOW_MORE_PAGES));
        logger.info("Sorting by price is applied");
    }

    public void openCompareLink(){
        driver.get("https://market.yandex.ru/compare");
        wait.until(ExpectedConditions.titleContains("Сравнение"));
    }

}
