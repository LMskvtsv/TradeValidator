package ru.service.validation.checkers;

import com.google.common.base.Enums;
import org.apache.log4j.Logger;
import ru.domain.Trade;
import ru.service.enums.Counterparty;
import ru.service.enums.StatusCode;
import ru.service.messages.CheckResponse;
import ru.service.utils.ErrorMassages;

/**
 * Checker for customer field.
 */
public class CounterpartyChecker implements Checker {
    private final Logger logger = Logger.getLogger(CounterpartyChecker.class);

    /**
     * Checks that customer field has one of the pre-defined values (Counterparty.class)
     * In case field is not exist - there will be missing field error.
     * Assumption: it is allowed for customer value to be in lower case.
     *
     * @param trade    to be checked.
     * @param response to store the bad result.
     * @return same response.
     */
    @Override
    public CheckResponse check(Trade trade, CheckResponse response) {
        if (trade.getCustomer() == null) {
            response.setStatusCode(StatusCode.ERROR);
            response.addMessage(ErrorMassages.EMPTY_COUNTERPARTY);
            logger.info(ErrorMassages.EMPTY_COUNTERPARTY);
        } else {
            logger.info(String.format("Trade id = %d. Counterparty to check: %s", trade.getId(), trade.getCustomer()));
            if (!Enums.getIfPresent(Counterparty.class, trade.getCustomer().toUpperCase()).isPresent()) {
                response.setStatusCode(StatusCode.ERROR);
                String message = String.format("Counterparty '%s' is not supported by the system.", trade.getCustomer());
                response.addMessage(message);
                logger.info(message);
            }
        }
        return response;
    }

    /**
     * Nothing to do yet.
     */
    @Override
    public void init() {

    }
}
