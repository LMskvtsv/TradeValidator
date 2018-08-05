package ru.service.validation.checkers;

import org.junit.BeforeClass;
import org.junit.Test;
import ru.domain.Trade;
import ru.service.enums.StatusCode;
import ru.service.messages.CheckResponse;
import ru.service.utils.ErrorMassages;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.*;

public class CounterpartyCheckerTest {
    private static final CounterpartyChecker CHECKER = new CounterpartyChecker();

    @BeforeClass
    public static void init() {
        CHECKER.init();
    }

    @Test
    public void whenMissingCounterpartyThenError() {
        Trade trade = new Trade();
        CheckResponse resp = new CheckResponse();
        resp.setStatusCode(StatusCode.SUCCESS);
        resp = CHECKER.check(trade, resp);
        assertThat(resp.getStatusCode(), is(StatusCode.ERROR));
        assertThat(resp.getMessages().contains(ErrorMassages.EMPTY_COUNTERPARTY), is(true));
        assertThat(resp.getMessages().size(), is(1));
    }

    @Test
    public void whenCounterpartyIsNotSupportedThenError() {
        Trade trade = new Trade();
        trade.setCustomer("");
        CheckResponse resp = new CheckResponse();
        resp.setStatusCode(StatusCode.SUCCESS);
        resp = CHECKER.check(trade, resp);
        assertThat(resp.getStatusCode(), is(StatusCode.ERROR));
        assertThat(resp.getMessages().contains("Counterparty '' is not supported by the system."), is(true));
        assertThat(resp.getMessages().size(), is(1));
    }

    /**
     * Assumption: lower case is allowed for counterparty.
     */
    @Test
    public void whenLowerCaseCounterpartyThenSuccess() {
        Trade trade = new Trade();
        trade.setCustomer("pluto1");
        CheckResponse resp = new CheckResponse();
        resp.setStatusCode(StatusCode.SUCCESS);
        resp = CHECKER.check(trade, resp);
        assertThat(resp.getStatusCode(), is(StatusCode.SUCCESS));
        assertThat(resp.getMessages().size(), is(0));
    }

    @Test
    public void whenGoodCounterpartyThenSuccess() {
        Trade trade = new Trade();
        trade.setCustomer("PLUTO1");
        CheckResponse resp = new CheckResponse();
        resp.setStatusCode(StatusCode.SUCCESS);
        resp = CHECKER.check(trade, resp);
        assertThat(resp.getStatusCode(), is(StatusCode.SUCCESS));
        assertThat(resp.getMessages().size(), is(0));
    }

}