package ru.service.validation.checkers;

import org.junit.BeforeClass;
import org.junit.Test;
import ru.domain.Trade;
import ru.service.enums.StatusCode;
import ru.service.messages.CheckResponse;
import ru.service.utils.ErrorMassages;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;


public class CCYCheckerTest {
    private static final CCYChecker CHECKER = new CCYChecker();

    @BeforeClass
    public static void init() {
        CHECKER.init();
    }

    @Test
    public void whenMissingCcyPairThenError() {
        Trade trade = new Trade();
        CheckResponse resp = new CheckResponse();
        resp.setStatusCode(StatusCode.SUCCESS);
        resp = CHECKER.check(trade, resp);
        assertThat(resp.getStatusCode(), is(StatusCode.ERROR));
        assertThat(resp.getMessages().contains(ErrorMassages.EMPTY_CCY_PAIR), is(true));
        assertThat(resp.getMessages().size(), is(1));
    }

    @Test
    public void whenEmptyCcyPairThenError() {
        Trade trade = new Trade();
        trade.setCcyPair("");
        CheckResponse resp = new CheckResponse();
        resp.setStatusCode(StatusCode.SUCCESS);
        resp = CHECKER.check(trade, resp);
        assertThat(resp.getStatusCode(), is(StatusCode.ERROR));
        assertThat(resp.getMessages().contains("Invalid ccy pair: "), is(true));
        assertThat(resp.getMessages().size(), is(1));
    }

    /**
     * Assumption: six symbols allowed for ccy pair. Three symbols for each ccy.
     */
    @Test
    public void when7SymbolsCcyPairThenError() {
        Trade trade = new Trade();
        trade.setCcyPair("USDEURO");
        CheckResponse resp = new CheckResponse();
        resp.setStatusCode(StatusCode.SUCCESS);
        resp = CHECKER.check(trade, resp);
        assertThat(resp.getStatusCode(), is(StatusCode.ERROR));
        assertThat(resp.getMessages().contains("Invalid ccy pair: USDEURO"), is(true));
        assertThat(resp.getMessages().size(), is(1));
    }

    @Test
    public void when1SymbolsCcyPairThenError() {
        Trade trade = new Trade();
        trade.setCcyPair("U");
        CheckResponse resp = new CheckResponse();
        resp.setStatusCode(StatusCode.SUCCESS);
        resp = CHECKER.check(trade, resp);
        assertThat(resp.getStatusCode(), is(StatusCode.ERROR));
        assertThat(resp.getMessages().contains("Invalid ccy pair: U"), is(true));
        assertThat(resp.getMessages().size(), is(1));
    }


    @Test
    public void whenInvalidBaseCcyThenError() {
        Trade trade = new Trade();
        trade.setCcyPair("USQEUR");
        CheckResponse resp = new CheckResponse();
        resp.setStatusCode(StatusCode.SUCCESS);
        resp = CHECKER.check(trade, resp);
        assertThat(resp.getStatusCode(), is(StatusCode.ERROR));
        assertThat(resp.getMessages().contains("Bad baseCCy currency code 'USQ'. Please, compare with ISO 4217."), is(true));
        assertThat(resp.getMessages().size(), is(1));
    }

    @Test
    public void whenInvalidCounterCcyThenError() {
        Trade trade = new Trade();
        trade.setCcyPair("EURUSQ");
        CheckResponse resp = new CheckResponse();
        resp.setStatusCode(StatusCode.SUCCESS);
        resp = CHECKER.check(trade, resp);
        assertThat(resp.getStatusCode(), is(StatusCode.ERROR));
        assertThat(resp.getMessages().contains("Bad counterCCy currency code 'USQ'. Please, compare with ISO 4217."), is(true));
        assertThat(resp.getMessages().size(), is(1));
    }

    /**
     * Assumption: lower case is allowed for ccy pair.
     */
    @Test
    public void whenLowerCaseCcyPairThenSuccess() {
        Trade trade = new Trade();
        trade.setCcyPair("eurusd");
        CheckResponse resp = new CheckResponse();
        resp.setStatusCode(StatusCode.SUCCESS);
        resp = CHECKER.check(trade, resp);
        assertThat(resp.getStatusCode(), is(StatusCode.SUCCESS));
        assertThat(resp.getMessages().size(), is(0));
    }

    @Test
    public void whenGoodCcyPairThenSuccess() {
        Trade trade = new Trade();
        trade.setCcyPair("EURUSD");
        trade.setPayCcy("USD");
        trade.setPremiumCcy("EUR");
        CheckResponse resp = new CheckResponse();
        resp.setStatusCode(StatusCode.SUCCESS);
        resp = CHECKER.check(trade, resp);
        assertThat(resp.getStatusCode(), is(StatusCode.SUCCESS));
        assertThat(resp.getMessages().size(), is(0));
    }

    /**
     * Assumption: not every instrument should have pay currency and premium currency filled.
     */
    @Test
    public void whenMissingPayAndPremCCYThenSuccess() {
        Trade trade = new Trade();
        trade.setCcyPair("EURUSD");
        CheckResponse resp = new CheckResponse();
        resp.setStatusCode(StatusCode.SUCCESS);
        resp = CHECKER.check(trade, resp);
        assertThat(resp.getStatusCode(), is(StatusCode.SUCCESS));
        assertThat(resp.getMessages().size(), is(0));
    }

    @Test
    public void whenEmptyPremiumCCYThenerror() {
        Trade trade = new Trade();
        trade.setCcyPair("EURUSD");
        trade.setPremiumCcy("");
        CheckResponse resp = new CheckResponse();
        resp.setStatusCode(StatusCode.SUCCESS);
        resp = CHECKER.check(trade, resp);
        assertThat(resp.getStatusCode(), is(StatusCode.ERROR));
        assertThat(resp.getMessages().contains("Bad premCCy currency code ''. Please, compare with ISO 4217."), is(true));
        assertThat(resp.getMessages().size(), is(1));
    }

    @Test
    public void whenInvalidPremiumCCYThenError() {
        Trade trade = new Trade();
        trade.setCcyPair("EURUSD");
        trade.setPremiumCcy("USQ");
        CheckResponse resp = new CheckResponse();
        resp.setStatusCode(StatusCode.SUCCESS);
        resp = CHECKER.check(trade, resp);
        assertThat(resp.getStatusCode(), is(StatusCode.ERROR));
        assertThat(resp.getMessages().contains("Bad premCCy currency code 'USQ'. Please, compare with ISO 4217."), is(true));
        assertThat(resp.getMessages().size(), is(1));
    }

    /**
     * Assumption: lower case is allowed for premium ccy.
     */
    @Test
    public void whenLowerCasePremiumCcyThenSuccess() {
        Trade trade = new Trade();
        trade.setCcyPair("EURUSD");
        trade.setPremiumCcy("usd");
        CheckResponse resp = new CheckResponse();
        resp.setStatusCode(StatusCode.SUCCESS);
        resp = CHECKER.check(trade, resp);
        assertThat(resp.getStatusCode(), is(StatusCode.ERROR));
        assertThat(resp.getMessages().size(), is(1));
    }

    @Test
    public void whenGoodPremiumCcyThenSuccess() {
        Trade trade = new Trade();
        trade.setCcyPair("EURUSD");
        trade.setPremiumCcy("GBP");
        CheckResponse resp = new CheckResponse();
        resp.setStatusCode(StatusCode.SUCCESS);
        resp = CHECKER.check(trade, resp);
        assertThat(resp.getStatusCode(), is(StatusCode.SUCCESS));
        assertThat(resp.getMessages().size(), is(0));
    }

    @Test
    public void whenEmptyPayCCYThenError() {
        Trade trade = new Trade();
        trade.setCcyPair("EURUSD");
        trade.setPayCcy("");
        CheckResponse resp = new CheckResponse();
        resp.setStatusCode(StatusCode.SUCCESS);
        resp = CHECKER.check(trade, resp);
        assertThat(resp.getStatusCode(), is(StatusCode.ERROR));
        assertThat(resp.getMessages().contains("Bad payCCy currency code ''. Please, compare with ISO 4217."), is(true));
        assertThat(resp.getMessages().size(), is(1));
    }

    @Test
    public void whenInvalidPayCCYThenError() {
        Trade trade = new Trade();
        trade.setCcyPair("EURUSD");
        trade.setPayCcy("USQ");
        CheckResponse resp = new CheckResponse();
        resp.setStatusCode(StatusCode.SUCCESS);
        resp = CHECKER.check(trade, resp);
        assertThat(resp.getStatusCode(), is(StatusCode.ERROR));
        assertThat(resp.getMessages().contains("Bad payCCy currency code 'USQ'. Please, compare with ISO 4217."), is(true));
        assertThat(resp.getMessages().size(), is(1));
    }

    /**
     * Assumption: lower case is allowed for pay ccy.
     */
    @Test
    public void whenLowerCasePayCcyThenSuccess() {
        Trade trade = new Trade();
        trade.setCcyPair("EURUSD");
        trade.setPayCcy("usd");
        CheckResponse resp = new CheckResponse();
        resp.setStatusCode(StatusCode.SUCCESS);
        resp = CHECKER.check(trade, resp);
        assertThat(resp.getStatusCode(), is(StatusCode.ERROR));
        assertThat(resp.getMessages().size(), is(1));
    }

    @Test
    public void whenGoodPayCcyThenSuccess() {
        Trade trade = new Trade();
        trade.setCcyPair("EURUSD");
        trade.setPayCcy("GBP");
        CheckResponse resp = new CheckResponse();
        resp.setStatusCode(StatusCode.SUCCESS);
        resp = CHECKER.check(trade, resp);
        assertThat(resp.getStatusCode(), is(StatusCode.SUCCESS));
        assertThat(resp.getMessages().size(), is(0));
    }
}