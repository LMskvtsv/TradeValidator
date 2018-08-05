package ru.service.validation.checkers;

import org.junit.BeforeClass;
import org.junit.Test;
import ru.domain.Trade;
import ru.service.enums.InstrumentType;
import ru.service.enums.OptionStyle;
import ru.service.enums.StatusCode;
import ru.service.messages.CheckResponse;
import ru.service.utils.ErrorMassages;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.*;

public class StyleCheckerTest {
    private static final StyleChecker CHECKER = new StyleChecker();

    @BeforeClass
    public static void init() {
        CHECKER.init();
    }

    /**
     * Assumption: style is a mandatory field when instrument type is option only.
     */
    @Test
    public void whenIsNotOptionAndMissingStyleThenSuccess() {
        Trade trade = new Trade();
        trade.setType(InstrumentType.SPOT.name());
        CheckResponse resp = new CheckResponse();
        resp.setStatusCode(StatusCode.SUCCESS);
        resp = CHECKER.check(trade, resp);
        assertThat(resp.getStatusCode(), is(StatusCode.SUCCESS));
        assertThat(resp.getMessages().size(), is(0));
    }

    @Test
    public void whenOptionAndMissingStyleThenError() {
        Trade trade = new Trade();
        trade.setType(InstrumentType.VANILLAOPTION.name());
        CheckResponse resp = new CheckResponse();
        resp.setStatusCode(StatusCode.SUCCESS);
        resp = CHECKER.check(trade, resp);
        assertThat(resp.getStatusCode(), is(StatusCode.ERROR));
        assertThat(resp.getMessages().contains(ErrorMassages.EMPTY_STYLE), is(true));
        assertThat(resp.getMessages().size(), is(1));
    }

    /**
     * Assumption: instrument type must not be empty.
     */
    @Test
    public void whenMissingInstrumentTypeThenError() {
        Trade trade = new Trade();
        CheckResponse resp = new CheckResponse();
        resp.setStatusCode(StatusCode.SUCCESS);
        resp = CHECKER.check(trade, resp);
        assertThat(resp.getStatusCode(), is(StatusCode.ERROR));
        assertThat(resp.getMessages().contains(ErrorMassages.EMPTY_INSTRUMENT_TYPE), is(true));
        assertThat(resp.getMessages().size(), is(1));
    }


    @Test
    public void whenIsNotOptionAndWrongStyleExistsThenSuccess() {
        Trade trade = new Trade();
        trade.setType(InstrumentType.SPOT.name());
        trade.setStyle("Fake style");
        CheckResponse resp = new CheckResponse();
        resp.setStatusCode(StatusCode.SUCCESS);
        resp = CHECKER.check(trade, resp);
        assertThat(resp.getStatusCode(), is(StatusCode.SUCCESS));
        assertThat(resp.getMessages().size(), is(0));
    }

    @Test
    public void whenIsOptionAndWrongStyleExistsThenError() {
        Trade trade = new Trade();
        trade.setType(InstrumentType.VANILLAOPTION.name());
        trade.setStyle("Fake style");
        CheckResponse resp = new CheckResponse();
        resp.setStatusCode(StatusCode.SUCCESS);
        resp = CHECKER.check(trade, resp);
        assertThat(resp.getStatusCode(), is(StatusCode.ERROR));
        assertThat(resp.getMessages().contains("Option style 'Fake style' is not supported."), is(true));
        assertThat(resp.getMessages().size(), is(1));
    }

    @Test
    public void whenIsOptionAndGoodStyleExistsThenSuccess() {
        Trade trade = new Trade();
        trade.setType(InstrumentType.VANILLAOPTION.name());
        trade.setStyle(OptionStyle.AMERICAN.name());
        CheckResponse resp = new CheckResponse();
        resp.setStatusCode(StatusCode.SUCCESS);
        resp = CHECKER.check(trade, resp);
        assertThat(resp.getStatusCode(), is(StatusCode.SUCCESS));
        assertThat(resp.getMessages().size(), is(0));
    }
}