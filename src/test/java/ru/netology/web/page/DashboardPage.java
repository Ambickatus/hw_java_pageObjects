package ru.netology.web.page;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import ru.netology.web.data.DataHelper;

import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;

public class DashboardPage {
    private final SelenideElement heading = $("[data-test-id=dashboard]");
    private static final ElementsCollection cards = $$(".list__item div");
    private final String st = "баланс: ";
    private final String fin = " р.";
    private final SelenideElement reloadBut = $("[data-test-id=action-reload]");

    public DashboardPage() {
        heading.shouldBe(visible);
    }

    public  int getCardBalance(DataHelper.CardInfo info) {
        var balance = getCards(info).getText();
        return extractBalance(balance);
    }

    private int extractBalance(String balance) {
        var splitByDoubleDot = balance.split(":");
        return Integer.parseInt(splitByDoubleDot[1].substring(1, splitByDoubleDot[1].indexOf("р.")).trim());
    }

    public TransferPage selectCardToTransfer(DataHelper.CardInfo info) {
        getCards(info).$(".button").click();
        return new TransferPage();
    }

    private static SelenideElement getCards(DataHelper.CardInfo info) {
        return cards.findBy(Condition.attribute("data-test-id", info.getTestId()));
    }
    public void reloadDashboardPage(){
        reloadBut.click();
        heading.shouldBe(visible);
    }

}