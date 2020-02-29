package Pages;

import Tests.YandexMarketTest;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class YandexMarketPage  {
    private WebDriver driver;
    private WebDriverWait wait;
    private static final Logger logger = LogManager.getLogger(YandexMarketTest.class);

    public YandexMarketPage(WebDriver driver) {
        this.driver = driver;
        wait = new WebDriverWait(driver,7);
    }

    public void openYandexMarket(){
        driver.get("https://ya.ru/");
        driver.get("https://market.yandex.ru/");
        logger.info("https://market.yandex.ru/ is opened");
    }
    public void clickOnTab(String name){
        driver.findElement(By.linkText(name)).click();
        logger.info("Clicked on tab {} ", name);
    }

    public void openElectronicsPage(){
        clickOnTab("Электроника");
        wait.until(ExpectedConditions.titleContains("Электроника"));
        logger.info("Electronics page is opened");
    }
    public void openSmartphonePage(){
        clickOnTab("Смартфоны");
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("div.n-filter-applied-results__content.preloadable")));
        logger.info("Smartphone page is opened");
    }

}
