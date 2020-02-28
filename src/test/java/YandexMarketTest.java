import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Before;
import org.junit.Test;

public class YandexMarketTest extends BaseTest {
    private static final Logger logger = LogManager.getLogger(YandexMarketTest.class);
    private YandexMarketPage yandexMarketPage;
    private ComparePage comparePage;
    @Before
    public void initPage(){
        yandexMarketPage = new YandexMarketPage(driver);
        comparePage = new ComparePage(driver);
    }

    @Test
    public void testComparisionTwoItems2(){
        yandexMarketPage.openYandexMarket();
        yandexMarketPage.clickOn(yandexMarketPage.CATEGORIES_TAB);
        yandexMarketPage.clickOn(yandexMarketPage.ELECTRONICS);
        yandexMarketPage.clickOn(yandexMarketPage.SMARTPHONE);
        yandexMarketPage.filterByManufacturer("xiaomi",yandexMarketPage.XIAOMI_CHECKBOX);
        yandexMarketPage.sortByPrice();
        yandexMarketPage.addFirstItem();
        yandexMarketPage.checkItemAddedToComparision();
        yandexMarketPage.clearFilterByManufacturer("xiaomi",yandexMarketPage.XIAOMI_CHECKBOX);
        yandexMarketPage.filterByManufacturer("huawei",yandexMarketPage.HUAWEI_CHECKBOX);
        yandexMarketPage.addFirstItem();
        yandexMarketPage.checkItemAddedToComparision();
       // yandexMarketPage.openComparePage();
        yandexMarketPage.openCompareLink();
        comparePage.assertThatCountOfItemIs(2);
        comparePage.openAllCharacteristics();
        comparePage.assertThatOSCharacteristicShown();
        comparePage.openDiffCharacteristics();
        comparePage.assertThatOSCharacteristicSNotShown();
    }

}
