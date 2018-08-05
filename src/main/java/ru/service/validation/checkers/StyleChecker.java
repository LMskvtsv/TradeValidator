package ru.service.validation.checkers;

import com.google.common.base.Enums;
import org.apache.log4j.Logger;
import ru.domain.Trade;
import ru.service.enums.InstrumentType;
import ru.service.enums.OptionStyle;
import ru.service.enums.StatusCode;
import ru.service.messages.CheckResponse;
import ru.service.utils.ErrorMassages;
import ru.service.utils.TradeTypeHelper;

/**
 * Checker for style field.
 */
public class StyleChecker implements Checker {
    private final Logger logger = Logger.getLogger(StyleChecker.class);


    /**
     * The option style can be in pre-defined values (OptionStyle.class).
     * In case instrument type is missing - there will be an error.
     * In case instrument type is not in pre-defined value - there will be an error.
     * In case instrument type is not option checking is just skipping.
     *
     * @param trade    to check.
     * @param response response where to store result.
     * @return
     */
    @Override
    public CheckResponse check(Trade trade, CheckResponse response) {
        logger.info(String.format("Trade id = %d. InstrumentType to check: %s", trade.getId(), trade.getType()));
        if (trade.getType() == null) {
            response.setStatusCode(StatusCode.ERROR);
            response.addMessage(ErrorMassages.EMPTY_INSTRUMENT_TYPE);
            logger.info(ErrorMassages.EMPTY_INSTRUMENT_TYPE);
        } else if (!Enums.getIfPresent(InstrumentType.class, trade.getType().toUpperCase()).isPresent()) {
            response.setStatusCode(StatusCode.ERROR);
            String message = String.format("Instrument type '%s' is not supported by system.", trade.getType());
            response.addMessage(message);
            logger.info(message);
        } else if (!TradeTypeHelper.isVanilla(trade.getType())) {
            logger.info("Instrument type is not supported by checker. Skipping....");
        } else {
            if (trade.getStyle() == null) {
                response.setStatusCode(StatusCode.ERROR);
                response.addMessage(ErrorMassages.EMPTY_STYLE);
                logger.info(ErrorMassages.EMPTY_STYLE);
            } else if (!Enums.getIfPresent(OptionStyle.class, trade.getStyle().toUpperCase()).isPresent()) {
                logger.info(String.format("Trade id = %d. Style to check: %s", trade.getId(), trade.getStyle()));
                response.setStatusCode(StatusCode.ERROR);
                String message = String.format("Option style '%s' is not supported.", trade.getStyle());
                response.addMessage(message);
                logger.info(message);
            }
        }
        return response;
    }

    @Override
    public void init() {

    }
}
