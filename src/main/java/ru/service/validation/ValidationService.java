package ru.service.validation;

import ru.service.enums.StatusCode;
import ru.service.messages.CheckResponse;
import ru.domain.Trade;
import ru.domain.Trades;
import org.apache.log4j.Logger;
import ru.service.validation.checkers.Checker;
import ru.service.validation.checkers.Checkers;

import java.util.HashMap;
import java.util.Map;

/**
 * Validation layer.
 */
public class ValidationService {
    private final Logger logger = Logger.getLogger(ValidationService.class);

    /**
     * Creates response object for each trade. Sets status for SUCCESS.
     * Passes response object to every checker. In case there is an error, each checker
     * adds error message to the response, and sets status to ERROR.
     *
     * @param trades
     * @return response mapped to inner trade id (counter). Inner trade id resets with the new request.
     */
    public Map<Integer, CheckResponse> validateArray(Trades trades) {
        int counter = 1;
        final Map<Integer, CheckResponse> responses = new HashMap<>();
        if (trades != null) {
            if (trades.getTests().size() == 0) {
                String message = "There are no trades in 'tests' array. Please, check the name of trades array or add trades into array.";
                logger.error(message);
                CheckResponse response = new CheckResponse();
                response.setStatusCode(StatusCode.ERROR);
                response.addMessage(message);
                responses.put(0, response);
            } else {
                for (Trade trade : trades.getTests()) {
                    logger.info("Check trade: " + trade);
                    CheckResponse response = new CheckResponse();
                    response.setStatusCode(StatusCode.SUCCESS);
                    response.setTrade(trade);
                    trade.setId(counter++);
                    for (Checker checker : Checkers.getCheckers()) {
                        responses.put(trade.getId(), checker.check(trade, response));
                    }
                }
            }
        } else {
            String message = "Trades object is null. Please, check json format.";
            logger.error(message);
            CheckResponse response = new CheckResponse();
            response.setStatusCode(StatusCode.ERROR);
            response.addMessage(message);
            responses.put(0, response);
        }
        return responses;
    }
}
