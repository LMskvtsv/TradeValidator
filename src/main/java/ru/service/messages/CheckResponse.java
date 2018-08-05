package ru.service.messages;

import ru.service.enums.StatusCode;
import ru.domain.Trade;

import java.util.HashSet;

/**
 * Web service response.
 */
public class CheckResponse {

    private StatusCode statusCode;
    /**
     * All the errors for the trade. Hash set prevents same messages.
     */
    private HashSet<String> messages = new HashSet();

    /**
     * Trade for link to the error message.
     */
    private Trade trade;


    public Trade getTrade() {
        return trade;
    }

    public void setTrade(Trade trade) {
        this.trade = trade;
    }

    public StatusCode getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(StatusCode statusCode) {
        this.statusCode = statusCode;
    }

    public HashSet<String> getMessages() {
        return messages;
    }

    /**
     * Adds message to set.
     *
     * @param message - error message.
     */
    public void addMessage(String message) {
        messages.add(message);
    }
}
