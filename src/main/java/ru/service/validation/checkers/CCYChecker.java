package ru.service.validation.checkers;

import org.apache.log4j.Logger;
import ru.domain.Trade;
import ru.service.enums.StatusCode;
import ru.service.messages.CheckResponse;
import ru.service.utils.CCYParser;
import ru.service.utils.ErrorMassages;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Checker for currency fields. At the moment they are: ccyPair, payCcy, premiumCcy.
 */
public class CCYChecker implements Checker {
    private final Logger logger = Logger.getLogger(CCYChecker.class);
    /**
     * File name from resources folder.
     */
    private final String ccyFileName = "currencies.xml";
    HashSet<String> ccyCodes;
    /**
     * Regex to split ccyPair into two currencies.
     */
    private final Pattern regexFormat = Pattern.compile("([A-Z]{3})([A-Z]{3})");

    /**
     * Get ISO ccyCodes.
     */
    public void init() {
        CCYParser parser = new CCYParser();
        ccyCodes = parser.parse(ccyFileName);
    }

    /**
     * Currently implemented following checks:
     * 1. CcyPair consists of valid ISO codes only.
     * 2. PremiumCCY is valid ISO code.
     * 3. PayCCY is valid ISO code
     * In case ccyPare is absent where will be error.
     * In case payCCY or premiumCCY is missing, check will be skipped and there will non be error.
     * Not all instruments should have payCCY, premiumCCY fields filled.
     *
     * @param trade    to check.
     * @param response response where to store result.
     * @return
     */
    @Override
    public CheckResponse check(Trade trade, CheckResponse response) {
        HashMap<String, String> fieldsToCheck = new HashMap<>();
        if (trade.getCcyPair() == null) {
            response.setStatusCode(StatusCode.ERROR);
            response.addMessage(ErrorMassages.EMPTY_CCY_PAIR);
            logger.info(ErrorMassages.EMPTY_CCY_PAIR);
        } else {
            Matcher matcher = regexFormat.matcher(trade.getCcyPair().toUpperCase(Locale.ENGLISH));
            if (!matcher.matches()) {
                response.setStatusCode(StatusCode.ERROR);
                String message = String.format("Invalid ccy pair: %s", trade.getCcyPair());
                response.addMessage(message);
                logger.info(message);
            } else {
                String base = matcher.group(1);
                String counter = matcher.group(2);
                fieldsToCheck.put("baseCCy", base);
                fieldsToCheck.put("counterCCy", counter);
            }
        }
        fieldsToCheck.put("payCCy", trade.getPayCcy());
        fieldsToCheck.put("premCCy", trade.getPremiumCcy());
        fieldsToCheck.forEach((k, v) -> {
            if (v == null) {
                logger.info(String.format("Tag '%s' is missing. Skipping check...", k));
            } else if (!ccyCodes.contains(v)) {
                response.setStatusCode(StatusCode.ERROR);
                String message = String.format("Bad %s currency code '%s'. Please, compare with ISO 4217.", k, v);
                response.addMessage(message);
                logger.info(message);
            }
        });
        return response;
    }
}
