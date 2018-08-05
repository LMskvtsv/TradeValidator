package ru.service.validation.checkers;

import org.junit.BeforeClass;
import org.junit.Test;
import ru.domain.Trade;
import ru.service.enums.StatusCode;
import ru.service.messages.CheckResponse;
import ru.service.utils.ErrorMassages;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.*;

public class LegalEntityCheckerTest {

    private static final LegalEntityChecker CHECKER = new LegalEntityChecker();

    @BeforeClass
    public static void init() {
        CHECKER.init();
    }

    @Test
    public void whenMissingLEThenError() {
        Trade trade = new Trade();
        CheckResponse resp = new CheckResponse();
        resp.setStatusCode(StatusCode.SUCCESS);
        resp = CHECKER.check(trade, resp);
        assertThat(resp.getStatusCode(), is(StatusCode.ERROR));
        assertThat(resp.getMessages().contains(ErrorMassages.EMPTY_LE), is(true));
        assertThat(resp.getMessages().size(), is(1));
    }

    @Test
    public void whenLeIsNotSupportedThenError() {
        Trade trade = new Trade();
        trade.setLegalEntity("");
        CheckResponse resp = new CheckResponse();
        resp.setStatusCode(StatusCode.SUCCESS);
        resp = CHECKER.check(trade, resp);
        assertThat(resp.getStatusCode(), is(StatusCode.ERROR));
        assertThat(resp.getMessages().contains("Legal entity '' is not supported by the system."), is(true));
        assertThat(resp.getMessages().size(), is(1));
    }

    /**
     * Assumption: lower case is allowed for LE. Spaces are allowed for LE.
     */
    @Test
    public void whenLowerCaseAndSpaceLEThenSuccess() {
        Trade trade = new Trade();
        trade.setLegalEntity("cs zurich");
        CheckResponse resp = new CheckResponse();
        resp.setStatusCode(StatusCode.SUCCESS);
        resp = CHECKER.check(trade, resp);
        assertThat(resp.getStatusCode(), is(StatusCode.SUCCESS));
        assertThat(resp.getMessages().size(), is(0));
    }

    @Test
    public void whenGoodLEThenSuccess() {
        Trade trade = new Trade();
        trade.setLegalEntity("CS Zurich");
        CheckResponse resp = new CheckResponse();
        resp.setStatusCode(StatusCode.SUCCESS);
        resp = CHECKER.check(trade, resp);
        assertThat(resp.getStatusCode(), is(StatusCode.SUCCESS));
        assertThat(resp.getMessages().size(), is(0));
    }
}