package Tests;

import Pages.ComparePage;
import Pages.SmartphonePage;
import Pages.YandexMarketPage;
import org.junit.Before;
import org.junit.Test;

public class YandexMarketTest extends BaseTest {
    private YandexMarketPage yandexMarketPage;
    private ComparePage comparePage;
    private SmartphonePage smartphonePage;

    @Before
    public void initPage(){
        yandexMarketPage = new YandexMarketPage(driver);
        smartphonePage = new SmartphonePage(driver);
        comparePage = new ComparePage(driver);
    }

    @Test
    public void testComparisionTwoItems(){
        yandexMarketPage.openYandexMarket();
        yandexMarketPage.openElectronicsPage();
        yandexMarketPage.openSmartphonePage();
        smartphonePage.filterByManufacturer("xiaomi",smartphonePage.XIAOMI_CHECKBOX);
        smartphonePage.sortByPrice();
        smartphonePage.addFirstItem();
        smartphonePage.clearFilterByManufacturer("xiaomi",smartphonePage.XIAOMI_CHECKBOX);
        smartphonePage.filterByManufacturer("huawei",smartphonePage.HUAWEI_CHECKBOX);
        smartphonePage.addFirstItem();
        smartphonePage.openCompareLink();
        comparePage.assertThatCountOfItemIs(2);
        comparePage.openAllCharacteristics();
        comparePage.assertThatOSCharacteristicShown();
        comparePage.openDiffCharacteristics();
        comparePage.assertThatOSCharacteristicSNotShown();
    }

}
