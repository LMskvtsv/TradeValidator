package ru.service.validation.checkers;

import org.junit.BeforeClass;
import org.junit.Test;
import ru.domain.Trade;
import ru.service.enums.InstrumentType;
import ru.service.enums.OptionStyle;
import ru.service.enums.StatusCode;
import ru.service.messages.CheckResponse;
import ru.service.utils.ErrorMassages;

import java.util.Calendar;
import java.util.Date;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;


public class DateCheckerTest {

    private static final DateChecker CHECKER = new DateChecker();

    @BeforeClass
    public static void init() {
        CHECKER.init();
    }

    @Test
    public void whenIsOptionAndAllDatesAreMissingThen6Errors() {
        Trade trade = new Trade();
        CheckResponse resp = new CheckResponse();
        trade.setType(InstrumentType.VANILLAOPTION.name());
        trade.setStyle(OptionStyle.AMERICAN.name());
        resp = CHECKER.check(trade, resp);
        assertThat(resp.getStatusCode(), is(StatusCode.ERROR));
        assertThat(resp.getMessages().contains(ErrorMassages.EMPTY_TRADE_DATE), is(true));
        assertThat(resp.getMessages().contains(ErrorMassages.EMPTY_VALUE_DATE), is(true));
        assertThat(resp.getMessages().contains(ErrorMassages.EMPTY_EXERCISE_DATE), is(true));
        assertThat(resp.getMessages().contains(ErrorMassages.EMPTY_EXPIRY_DATE), is(true));
        assertThat(resp.getMessages().contains(ErrorMassages.EMPTY_PREMIUM_DATE), is(true));
        assertThat(resp.getMessages().contains(ErrorMassages.EMPTY_DELIVERY_DATE), is(true));
        assertThat(resp.getMessages().size(), is(6));
    }

    @Test
    public void whenIsOptionAnd5DatesAreMissingThen5Errors() {
        Trade trade = new Trade();
        CheckResponse resp = new CheckResponse();
        trade.setType(InstrumentType.VANILLAOPTION.name());
        trade.setTradeDate(new Date());
        resp = CHECKER.check(trade, resp);
        assertThat(resp.getStatusCode(), is(StatusCode.ERROR));
        assertThat(resp.getMessages().contains(ErrorMassages.EMPTY_VALUE_DATE), is(true));
        assertThat(resp.getMessages().contains(ErrorMassages.EMPTY_STYLE), is(true));
        assertThat(resp.getMessages().contains(ErrorMassages.EMPTY_EXPIRY_DATE), is(true));
        assertThat(resp.getMessages().contains(ErrorMassages.EMPTY_PREMIUM_DATE), is(true));
        assertThat(resp.getMessages().contains(ErrorMassages.EMPTY_DELIVERY_DATE), is(true));
        assertThat(resp.getMessages().size(), is(5));
    }

    @Test
    public void whenIsOptionAnd4DatesAreMissingThen4Errors() {
        Trade trade = new Trade();
        CheckResponse resp = new CheckResponse();
        trade.setType(InstrumentType.VANILLAOPTION.name());
        trade.setTradeDate(new Date());
        Calendar calendar = Calendar.getInstance();
        calendar.set(2018, Calendar.AUGUST, 14);
        trade.setValueDate(calendar.getTime());
        resp = CHECKER.check(trade, resp);
        assertThat(resp.getStatusCode(), is(StatusCode.ERROR));
        assertThat(resp.getMessages().contains(ErrorMassages.EMPTY_STYLE), is(true));
        assertThat(resp.getMessages().contains(ErrorMassages.EMPTY_EXPIRY_DATE), is(true));
        assertThat(resp.getMessages().contains(ErrorMassages.EMPTY_PREMIUM_DATE), is(true));
        assertThat(resp.getMessages().contains(ErrorMassages.EMPTY_DELIVERY_DATE), is(true));
        assertThat(resp.getMessages().size(), is(4));
    }

    @Test
    public void whenIsOptionAnd3DatesAreMissingThen3Errors() {
        Trade trade = new Trade();
        CheckResponse resp = new CheckResponse();
        trade.setType(InstrumentType.VANILLAOPTION.name());
        trade.setTradeDate(new Date());
        trade.setDeliveryDate(new Date());
        Calendar calendar = Calendar.getInstance();
        calendar.set(2018, Calendar.AUGUST, 14);
        trade.setValueDate(calendar.getTime());
        trade.setExcerciseStartDate(new Date());
        resp = CHECKER.check(trade, resp);
        assertThat(resp.getStatusCode(), is(StatusCode.ERROR));
        assertThat(resp.getMessages().contains(ErrorMassages.EMPTY_STYLE), is(true));
        assertThat(resp.getMessages().contains(ErrorMassages.EMPTY_EXPIRY_DATE), is(true));
        assertThat(resp.getMessages().contains(ErrorMassages.EMPTY_PREMIUM_DATE), is(true));
        assertThat(resp.getMessages().size(), is(3));
    }

    @Test
    public void whenIsOptionAnd2DateIsMissingThen2Errors() {
        Trade trade = new Trade();
        CheckResponse resp = new CheckResponse();
        trade.setType(InstrumentType.VANILLAOPTION.name());
        trade.setTradeDate(new Date());
        trade.setDeliveryDate(new Date());
        Calendar calendar = Calendar.getInstance();
        calendar.set(2018, Calendar.AUGUST, 14);
        trade.setValueDate(calendar.getTime());
        trade.setExcerciseStartDate(new Date());
        trade.setExpiryDate(new Date());
        resp = CHECKER.check(trade, resp);
        assertThat(resp.getStatusCode(), is(StatusCode.ERROR));
        assertThat(resp.getMessages().contains(ErrorMassages.EMPTY_STYLE), is(true));
        assertThat(resp.getMessages().contains(ErrorMassages.EMPTY_PREMIUM_DATE), is(true));
        assertThat(resp.getMessages().size(), is(2));
    }

    @Test
    public void whenDeliveryDateMissingThenError() {
        Trade trade = new Trade();
        CheckResponse resp = new CheckResponse();
        trade.setType(InstrumentType.VANILLAOPTION.name());
        trade.setTradeDate(new Date());
        Calendar calendar = Calendar.getInstance();
        calendar.set(2018, Calendar.AUGUST, 14);
        trade.setValueDate(calendar.getTime());
        trade.setExcerciseStartDate(new Date());
        trade.setExpiryDate(new Date());
        trade.setPremiumDate(new Date());
        resp = CHECKER.check(trade, resp);
        assertThat(resp.getStatusCode(), is(StatusCode.ERROR));
        assertThat(resp.getMessages().contains(ErrorMassages.EMPTY_STYLE), is(true));
        assertThat(resp.getMessages().contains(ErrorMassages.EMPTY_DELIVERY_DATE), is(true));
        assertThat(resp.getMessages().size(), is(2));
    }

    @Test
    public void whenIsNOTOptionAndAllDatesAreMissingThen2Errors() {
        Trade trade = new Trade();
        CheckResponse resp = new CheckResponse();
        trade.setType(InstrumentType.SPOT.name());
        resp = CHECKER.check(trade, resp);
        assertThat(resp.getStatusCode(), is(StatusCode.ERROR));
        assertThat(resp.getMessages().contains(ErrorMassages.EMPTY_TRADE_DATE), is(true));
        assertThat(resp.getMessages().contains(ErrorMassages.EMPTY_VALUE_DATE), is(true));
        assertThat(resp.getMessages().size(), is(2));
    }

    @Test
    public void whenValueDateBeforeTradeDateThenError() {
        Trade trade = new Trade();
        trade.setType(InstrumentType.SPOT.name());
        CheckResponse resp = new CheckResponse();
        Calendar tradeDate = Calendar.getInstance();
        tradeDate.set(2018, Calendar.AUGUST, 02);
        trade.setTradeDate(tradeDate.getTime());
        Calendar valueDate = Calendar.getInstance();
        valueDate.set(2018, Calendar.AUGUST, 01);
        trade.setValueDate(valueDate.getTime());
        resp = CHECKER.check(trade, resp);
        assertThat(resp.getStatusCode(), is(StatusCode.ERROR));
        assertThat(resp.getMessages().contains("Value date cannot be before Trade date."), is(true));
        assertThat(resp.getMessages().contains("Value date is not 2 working days from Trade date."), is(true));
        assertThat(resp.getMessages().size(), is(2));
    }

    @Test
    public void whenValueDateSameAsTradeDateThen1Error() {
        Trade trade = new Trade();
        trade.setType(InstrumentType.SPOT.name());
        CheckResponse resp = new CheckResponse();
        Calendar tradeDate = Calendar.getInstance();
        tradeDate.set(2018, Calendar.AUGUST, 02);
        trade.setTradeDate(tradeDate.getTime());
        Calendar valueDate = Calendar.getInstance();
        valueDate.set(2018, Calendar.AUGUST, 02);
        trade.setValueDate(valueDate.getTime());
        resp = CHECKER.check(trade, resp);
        assertThat(resp.getStatusCode(), is(StatusCode.ERROR));
        assertThat(resp.getMessages().contains("Value date is not 2 working days from Trade date."), is(true));
        assertThat(resp.getMessages().size(), is(1));
    }

    @Test
    public void whenValueDateAfterTradeDate1WorkingDayThen1Error() {
        Trade trade = new Trade();
        trade.setType(InstrumentType.SPOT.name());
        CheckResponse resp = new CheckResponse();
        Calendar tradeDate = Calendar.getInstance();
        tradeDate.set(2018, Calendar.AUGUST, 02);
        trade.setTradeDate(tradeDate.getTime());
        Calendar valueDate = Calendar.getInstance();
        valueDate.set(2018, Calendar.AUGUST, 03);
        trade.setValueDate(valueDate.getTime());
        resp = CHECKER.check(trade, resp);
        assertThat(resp.getStatusCode(), is(StatusCode.ERROR));
        assertThat(resp.getMessages().contains("Value date is not 2 working days from Trade date."), is(true));
        assertThat(resp.getMessages().size(), is(1));
    }

    @Test
    public void whenValueDateAfterTradeDate2WorkingDayThenSuccess() {
        Trade trade = new Trade();
        trade.setType(InstrumentType.SPOT.name());
        CheckResponse resp = new CheckResponse();
        resp.setStatusCode(StatusCode.SUCCESS);
        Calendar tradeDate = Calendar.getInstance();
        tradeDate.set(2018, Calendar.AUGUST, 02);
        trade.setTradeDate(tradeDate.getTime());
        Calendar valueDate = Calendar.getInstance();
        valueDate.set(2018, Calendar.AUGUST, 06);
        trade.setValueDate(valueDate.getTime());
        resp = CHECKER.check(trade, resp);
        assertThat(resp.getStatusCode(), is(StatusCode.SUCCESS));
        assertThat(resp.getMessages().size(), is(0));
    }

    @Test
    public void checkValueDateWhenHolidayAndWeekendsInBetweenSuccess() {
        Trade trade = new Trade();
        trade.setType(InstrumentType.FORWARD.name());
        CheckResponse resp = new CheckResponse();
        resp.setStatusCode(StatusCode.SUCCESS);
        Calendar tradeDate = Calendar.getInstance();
        tradeDate.set(2018, Calendar.AUGUST, 14);
        trade.setTradeDate(tradeDate.getTime());
        Calendar valueDate = Calendar.getInstance();
        valueDate.set(2018, Calendar.AUGUST, 22);
        trade.setValueDate(valueDate.getTime());
        resp = CHECKER.check(trade, resp);
        assertThat(resp.getStatusCode(), is(StatusCode.SUCCESS));
        assertThat(resp.getMessages().size(), is(0));
    }

    @Test
    public void checkValueDateWhenHolidayAndWeekendsInBetweenError() {
        Trade trade = new Trade();
        trade.setType(InstrumentType.FORWARD.name());
        CheckResponse resp = new CheckResponse();
        resp.setStatusCode(StatusCode.SUCCESS);
        Calendar tradeDate = Calendar.getInstance();
        tradeDate.set(2018, Calendar.AUGUST, 14);
        trade.setTradeDate(tradeDate.getTime());
        Calendar valueDate = Calendar.getInstance();
        valueDate.set(2018, Calendar.AUGUST, 21);
        trade.setValueDate(valueDate.getTime());
        resp = CHECKER.check(trade, resp);
        assertThat(resp.getStatusCode(), is(StatusCode.ERROR));
        assertThat(resp.getMessages().contains("Value date is not 5 working days from Trade date."), is(true));
        assertThat(resp.getMessages().size(), is(1));
    }

    @Test
    public void whenValueDateWeekendThenError() {
        Trade trade = new Trade();
        trade.setType(InstrumentType.SPOT.name());
        CheckResponse resp = new CheckResponse();
        resp.setStatusCode(StatusCode.SUCCESS);
        Calendar tradeDate = Calendar.getInstance();
        tradeDate.set(2018, Calendar.AUGUST, 02);
        trade.setTradeDate(tradeDate.getTime());
        Calendar valueDate = Calendar.getInstance();
        valueDate.set(2018, Calendar.AUGUST, 04);
        trade.setValueDate(valueDate.getTime());
        resp = CHECKER.check(trade, resp);
        assertThat(resp.getStatusCode(), is(StatusCode.ERROR));
        assertThat(resp.getMessages().contains("Value date is not 2 working days from Trade date."), is(true));
        assertThat(resp.getMessages().contains("Value date cannot be weekend or holiday."), is(true));
        assertThat(resp.getMessages().size(), is(2));
    }

    @Test
    public void whenValueDateHolidayThenError() {
        Trade trade = new Trade();
        trade.setType(InstrumentType.SPOT.name());
        CheckResponse resp = new CheckResponse();
        resp.setStatusCode(StatusCode.SUCCESS);
        Calendar tradeDate = Calendar.getInstance();
        tradeDate.set(2018, Calendar.AUGUST, 02);
        trade.setTradeDate(tradeDate.getTime());
        Calendar valueDate = Calendar.getInstance();
        valueDate.set(2018, Calendar.AUGUST, 15);
        trade.setValueDate(valueDate.getTime());
        resp = CHECKER.check(trade, resp);
        assertThat(resp.getStatusCode(), is(StatusCode.ERROR));
        assertThat(resp.getMessages().contains("Value date is not 2 working days from Trade date."), is(true));
        assertThat(resp.getMessages().contains("Value date cannot be weekend or holiday."), is(true));
        assertThat(resp.getMessages().size(), is(2));
    }

    @Test
    public void whenExpiryAndPremiumDateAfterDeliveryDateThenError() {
        Trade trade = new Trade();
        trade.setStyle(OptionStyle.EUROPEAN.name());
        trade.setType(InstrumentType.VANILLAOPTION.name());
        CheckResponse resp = new CheckResponse();

        Calendar tradeDate = Calendar.getInstance();
        tradeDate.set(2018, Calendar.AUGUST, 02);
        trade.setTradeDate(tradeDate.getTime());

        Calendar valueDate = Calendar.getInstance();
        valueDate.set(2018, Calendar.AUGUST, 14);
        trade.setValueDate(valueDate.getTime());

        Calendar exersiceDate = Calendar.getInstance();
        exersiceDate.set(2018, Calendar.AUGUST, 15);
        trade.setExcerciseStartDate(exersiceDate.getTime());

        Calendar deliveryDate = Calendar.getInstance();
        deliveryDate.set(2018, Calendar.AUGUST, 02);
        trade.setDeliveryDate(deliveryDate.getTime());

        Calendar expDate = Calendar.getInstance();
        expDate.set(2018, Calendar.AUGUST, 03);
        trade.setExpiryDate(expDate.getTime());

        Calendar premiumDate = Calendar.getInstance();
        premiumDate.set(2018, Calendar.AUGUST, 03);
        trade.setPremiumDate(premiumDate.getTime());

        resp = CHECKER.check(trade, resp);
        assertThat(resp.getStatusCode(), is(StatusCode.ERROR));
        assertThat(resp.getMessages().contains("'Expiry date' and 'Premium date' shall be before 'Delivery date'."), is(true));
        assertThat(resp.getMessages().size(), is(1));
    }

    @Test
    public void whenExpiryAndPremiumDateSameAsDeliveryDateThenError() {
        Trade trade = new Trade();
        trade.setStyle(OptionStyle.EUROPEAN.name());
        trade.setType(InstrumentType.VANILLAOPTION.name());
        CheckResponse resp = new CheckResponse();

        Calendar tradeDate = Calendar.getInstance();
        tradeDate.set(2018, Calendar.AUGUST, 02);
        trade.setTradeDate(tradeDate.getTime());

        Calendar valueDate = Calendar.getInstance();
        valueDate.set(2018, Calendar.AUGUST, 14);
        trade.setValueDate(valueDate.getTime());

        Calendar exersiceDate = Calendar.getInstance();
        exersiceDate.set(2018, Calendar.AUGUST, 15);
        trade.setExcerciseStartDate(exersiceDate.getTime());

        Calendar deliveryDate = Calendar.getInstance();
        deliveryDate.set(2018, Calendar.AUGUST, 02);
        trade.setDeliveryDate(deliveryDate.getTime());

        Calendar expDate = Calendar.getInstance();
        expDate.set(2018, Calendar.AUGUST, 02);
        trade.setExpiryDate(expDate.getTime());

        Calendar premiumDate = Calendar.getInstance();
        premiumDate.set(2018, Calendar.AUGUST, 02);
        trade.setPremiumDate(premiumDate.getTime());

        resp = CHECKER.check(trade, resp);
        assertThat(resp.getStatusCode(), is(StatusCode.ERROR));
        assertThat(resp.getMessages().contains("'Expiry date' and 'Premium date' shall be before 'Delivery date'."), is(true));
        assertThat(resp.getMessages().size(), is(1));
    }

    @Test
    public void whenExpiryAndPremiumDateBeforeDeliveryDateThenSuccess() {
        Trade trade = new Trade();
        trade.setStyle(OptionStyle.EUROPEAN.name());
        trade.setType(InstrumentType.VANILLAOPTION.name());
        CheckResponse resp = new CheckResponse();

        Calendar tradeDate = Calendar.getInstance();
        tradeDate.set(2018, Calendar.AUGUST, 02);
        trade.setTradeDate(tradeDate.getTime());

        Calendar valueDate = Calendar.getInstance();
        valueDate.set(2018, Calendar.AUGUST, 14);
        trade.setValueDate(valueDate.getTime());

        Calendar exersiceDate = Calendar.getInstance();
        exersiceDate.set(2018, Calendar.AUGUST, 15);
        trade.setExcerciseStartDate(exersiceDate.getTime());

        Calendar deliveryDate = Calendar.getInstance();
        deliveryDate.set(2018, Calendar.AUGUST, 02);
        trade.setDeliveryDate(deliveryDate.getTime());

        Calendar expDate = Calendar.getInstance();
        expDate.set(2018, Calendar.AUGUST, 01);
        trade.setExpiryDate(expDate.getTime());

        Calendar premiumDate = Calendar.getInstance();
        premiumDate.set(2018, Calendar.AUGUST, 01);
        trade.setPremiumDate(premiumDate.getTime());

        resp.setStatusCode(StatusCode.SUCCESS);
        resp = CHECKER.check(trade, resp);
        assertThat(resp.getStatusCode(), is(StatusCode.SUCCESS));
        assertThat(resp.getMessages().size(), is(0));
    }

    @Test
    public void whenExerciseDateBetweenTradeDateAndExpiryDateThenSuccess() {
        Trade trade = new Trade();
        trade.setStyle(OptionStyle.AMERICAN.name());
        trade.setType(InstrumentType.VANILLAOPTION.name());
        CheckResponse resp = new CheckResponse();

        Calendar tradeDate = Calendar.getInstance();
        tradeDate.set(2018, Calendar.AUGUST, 02);
        trade.setTradeDate(tradeDate.getTime());

        Calendar valueDate = Calendar.getInstance();
        valueDate.set(2018, Calendar.AUGUST, 14);
        trade.setValueDate(valueDate.getTime());

        Calendar exersiceDate = Calendar.getInstance();
        exersiceDate.set(2018, Calendar.AUGUST, 03);
        trade.setExcerciseStartDate(exersiceDate.getTime());

        Calendar deliveryDate = Calendar.getInstance();
        deliveryDate.set(2018, Calendar.AUGUST, 10);
        trade.setDeliveryDate(deliveryDate.getTime());

        Calendar expDate = Calendar.getInstance();
        expDate.set(2018, Calendar.AUGUST, 06);
        trade.setExpiryDate(expDate.getTime());

        Calendar premiumDate = Calendar.getInstance();
        premiumDate.set(2018, Calendar.AUGUST, 01);
        trade.setPremiumDate(premiumDate.getTime());

        resp.setStatusCode(StatusCode.SUCCESS);
        resp = CHECKER.check(trade, resp);
        assertThat(resp.getStatusCode(), is(StatusCode.SUCCESS));
        assertThat(resp.getMessages().size(), is(0));
    }

    @Test
    public void whenExerciseDateBeforeTradeDateThenError() {
        Trade trade = new Trade();
        trade.setStyle(OptionStyle.AMERICAN.name());
        trade.setType(InstrumentType.VANILLAOPTION.name());
        CheckResponse resp = new CheckResponse();

        Calendar tradeDate = Calendar.getInstance();
        tradeDate.set(2018, Calendar.AUGUST, 02);
        trade.setTradeDate(tradeDate.getTime());

        Calendar valueDate = Calendar.getInstance();
        valueDate.set(2018, Calendar.AUGUST, 14);
        trade.setValueDate(valueDate.getTime());

        Calendar exersiceDate = Calendar.getInstance();
        exersiceDate.set(2018, Calendar.AUGUST, 01);
        trade.setExcerciseStartDate(exersiceDate.getTime());

        Calendar deliveryDate = Calendar.getInstance();
        deliveryDate.set(2018, Calendar.AUGUST, 10);
        trade.setDeliveryDate(deliveryDate.getTime());

        Calendar expDate = Calendar.getInstance();
        expDate.set(2018, Calendar.AUGUST, 06);
        trade.setExpiryDate(expDate.getTime());

        Calendar premiumDate = Calendar.getInstance();
        premiumDate.set(2018, Calendar.AUGUST, 01);
        trade.setPremiumDate(premiumDate.getTime());

        resp.setStatusCode(StatusCode.SUCCESS);
        resp = CHECKER.check(trade, resp);
        assertThat(resp.getStatusCode(), is(StatusCode.ERROR));
        assertThat(resp.getMessages().contains("'Exercise Start Date' has to be after the 'Trade date' but before the 'Expiry date'."), is(true));
        assertThat(resp.getMessages().size(), is(1));
    }
}