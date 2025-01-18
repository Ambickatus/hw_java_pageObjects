package ru.netology.web.page;

import com.codeborne.selenide.Selectors;
import com.codeborne.selenide.SelenideElement;
import ru.netology.web.data.DataHelper;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;


public class TransferPage {
    private final SelenideElement headOfTransferPage= $("[data-test-id=dashboard]");
    private final SelenideElement headOfTransferPageUnder = $(Selectors.byText("Пополнение карты"));
    private final SelenideElement transferedAmount = $("[data-test-id=amount] input");
    private final SelenideElement transferedFrom = $("[data-test-id=from] input");
    private final SelenideElement buttonAction = $("[data-test-id=action-transfer]");
    private final SelenideElement buttonCancel = $("[data-test-id=action-cancel]");
    private final SelenideElement errorMessage = $("[data-test-id=error-notification]");

    public TransferPage() {
        headOfTransferPage.shouldBe(visible);
        headOfTransferPageUnder.shouldBe(visible);
    }

    public DashboardPage validTransferMoney(DataHelper.CardInfo info, String amount){
        transferMoney(info, amount);
        return new DashboardPage();
    }

    public void transferMoney(DataHelper.CardInfo info, String amount){
        transferedAmount.setValue(amount);
        transferedFrom.setValue(info.getCardNumber());
        buttonAction.click();
    }

    public void transferMoneyFromInvalidCard(String amount){
        transferedAmount.setValue(amount);
        transferedFrom.setValue("1234556678890");
        buttonAction.click();
    }
    public void checkErrorMessage(String text) {
        errorMessage.shouldBe(visible).shouldHave(text(text));
    }


}
