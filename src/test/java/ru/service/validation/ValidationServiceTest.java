package ru.service.validation;

import org.junit.Test;
import ru.domain.Trades;
import ru.service.enums.StatusCode;
import ru.service.messages.CheckResponse;

import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.*;

public class ValidationServiceTest {
    private final ValidationService validationService = new ValidationService();

    @Test
    public void whenNullTradesThenError() {
        HashMap<Integer, CheckResponse> expected = new HashMap<>();
        CheckResponse response = new CheckResponse();
        response.setStatusCode(StatusCode.ERROR);
        String message = "Trades object is null. Please, check json format.";
        response.addMessage(message);
        expected.put(0, response);
        Map<Integer, CheckResponse> actual = validationService.validateArray(null);
        assertThat(actual.get(0).getStatusCode(), is(StatusCode.ERROR));
        assertThat(actual.get(0).getMessages().contains(message), is(true));
        assertThat(actual.get(0).getMessages().size(), is(1));
    }

    @Test
    public void whenTradesArrayHasNotAnyTradesThenError() {
        HashMap<Integer, CheckResponse> expected = new HashMap<>();
        CheckResponse response = new CheckResponse();
        response.setStatusCode(StatusCode.ERROR);
        String message = "There are no trades in 'tests' array. Please, check the name of trades array or add trades into array.";
        response.addMessage(message);
        expected.put(0, response);
        Trades trades = new Trades();
        Map<Integer, CheckResponse> actual = validationService.validateArray(trades);
        assertThat(actual.get(0).getStatusCode(), is(StatusCode.ERROR));
        assertThat(actual.get(0).getMessages().contains(message), is(true));
        assertThat(actual.get(0).getMessages().size(), is(1));
    }
}