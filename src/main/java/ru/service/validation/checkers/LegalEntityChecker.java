package ru.service.validation.checkers;

import com.google.common.base.Enums;
import org.apache.log4j.Logger;
import ru.domain.Trade;
import ru.service.enums.Counterparty;
import ru.service.enums.LegalEntity;
import ru.service.enums.StatusCode;
import ru.service.messages.CheckResponse;
import ru.service.utils.ErrorMassages;

/**
 * Checker for customer field.
 */
public class LegalEntityChecker implements Checker {
    private final Logger logger = Logger.getLogger(LegalEntityChecker.class);

    /**
     * Checks that LegalEntity field has one of the pre-defined values (LegalEntity.class)
     * In case field is not exist - there will be missing field error.
     * Assumption: it is allowed for LE value to be in lower case and to have spaces.
     *
     * @param trade    to be checked.
     * @param response to store the bad result.
     * @return same response.
     */
    @Override
    public CheckResponse check(Trade trade, CheckResponse response) {
        if (trade.getLegalEntity() == null) {
            response.setStatusCode(StatusCode.ERROR);
            response.addMessage(ErrorMassages.EMPTY_LE);
            logger.info(ErrorMassages.EMPTY_LE);
        } else {
            logger.info(String.format("Trade id = %d. LE to check: %s", trade.getId(), trade.getLegalEntity()));
            if (!Enums.getIfPresent(LegalEntity.class, trade.getLegalEntity().replaceAll("\\s", "").toUpperCase()).isPresent()) {
                response.setStatusCode(StatusCode.ERROR);
                String message = String.format("Legal entity '%s' is not supported by the system.", trade.getLegalEntity());
                response.addMessage(message);
                logger.info(message);
            }
        }
        return response;
    }

    /**
     * Nothing to to yet.
     */
    @Override
    public void init() {

    }
}
