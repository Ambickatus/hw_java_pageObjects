package ru.netology.web.test;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.netology.web.data.DataHelper;
import ru.netology.web.page.DashboardPage;
import ru.netology.web.page.LoginPage;
import ru.netology.web.page.TransferPage;

import static com.codeborne.selenide.Selenide.open;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

class MoneyTransferTest {
    int initialBalanceCard1;
    int initialBalanceCard2;
    DashboardPage dashBoardPage;
    DataHelper.CardInfo selectedCardForTransfer;
    DataHelper.CardInfo cardFromTransfer;
    DataHelper.CardInfo invalidCard;


    @BeforeEach
    void setup() {
        open("http://localhost:9999");
        var loginPage = new LoginPage();
        var authInfo = DataHelper.getAuthInfo();
        var verificationPage = loginPage.validLogin(authInfo);
        var verificationCode = DataHelper.getVerificationCodeFor(authInfo);
        selectedCardForTransfer = DataHelper.getFirstCardInfo();
        cardFromTransfer = DataHelper.getSecondCardInfo();
        invalidCard = DataHelper.getWrongCardInfo();
        dashBoardPage = verificationPage.validVerify(verificationCode);
        initialBalanceCard1 = dashBoardPage.getCardBalance(selectedCardForTransfer);
        initialBalanceCard2 = dashBoardPage.getCardBalance(cardFromTransfer);

    }

    @Test
    void shouldTransferMoneyFromSecondToFirstCard() {
        int transferedAmount = DataHelper.validAmountForTransfer(initialBalanceCard2);
        var transferPage = dashBoardPage.selectCardToTransfer(selectedCardForTransfer);
        transferPage.validTransferMoney(cardFromTransfer, String.valueOf(transferedAmount));
        dashBoardPage.reloadDashboardPage();
        assertAll(() -> assertEquals(initialBalanceCard1 + transferedAmount, dashBoardPage.getCardBalance(selectedCardForTransfer)),
                () -> assertEquals(initialBalanceCard2 - transferedAmount, dashBoardPage.getCardBalance(cardFromTransfer)));

    }

    @Test
    void shouldNotTransferMoneyFromCardWithLowMoney() {
        String errorMesText = "Ошибка! Невозможно перевести сумму, превышающую остаток на счёте";
        int transferedAmount = DataHelper.invalidAmountForTransfer(initialBalanceCard2);
        var transferPage = dashBoardPage.selectCardToTransfer(selectedCardForTransfer);
        transferPage.transferMoney(cardFromTransfer, String.valueOf(transferedAmount));
        dashBoardPage.reloadDashboardPage();
        assertAll(() -> transferPage.checkErrorMessage(errorMesText));

    }


    @Test
    void shouldNotTransferMoneyFromInvalidCard() {
        String errorMesText = "Ошибка! Произошла ошибка";
        int transferedAmount = DataHelper.validAmountForTransfer(initialBalanceCard2);
        var transferPage = dashBoardPage.selectCardToTransfer(selectedCardForTransfer);
        transferPage.transferMoneyFromInvalidCard(invalidCard, String.valueOf(transferedAmount));
        assertAll(() -> transferPage.checkErrorMessage(errorMesText));
    }
}

