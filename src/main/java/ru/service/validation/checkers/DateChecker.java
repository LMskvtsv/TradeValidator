package ru.service.validation.checkers;

import org.apache.log4j.Logger;
import ru.service.enums.OptionStyle;
import ru.service.enums.StatusCode;
import ru.service.messages.CheckResponse;
import ru.domain.Trade;
import ru.service.utils.ErrorMassages;
import ru.service.utils.HolidaysParser;
import ru.service.utils.TradeTypeHelper;

import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;

/**
 * Checker for date fields. At the moment they are: ccyPair, payCcy, premiumCcy.
 */
public class DateChecker implements Checker {
    private final Logger logger = Logger.getLogger(DateChecker.class);
    private final HolidaysParser holidaysParser = new HolidaysParser();
    private final HashSet<Calendar> holidays = new HashSet<>();
    private final int spotValueDate = 2;
    private final int forwardValueDate = 5;

    /**
     * Get holidays from resources folder.
     */
    @Override
    public void init() {
        holidays.addAll(holidaysParser.parse("holidays.xml"));
    }

    /**
     * Sets common check flow.
     *
     * @param trade    to check.
     * @param response response where to store result.
     * @return
     */
    @Override
    public CheckResponse check(Trade trade, CheckResponse response) {
        checkValueDateBeforeTradeDate(trade, response);
        checkValueDateIsWorking(trade, response);
        if (trade.getType() == null) {
            response.setStatusCode(StatusCode.ERROR);
            response.addMessage(ErrorMassages.EMPTY_INSTRUMENT_TYPE);
            logger.info(ErrorMassages.EMPTY_INSTRUMENT_TYPE);
        } else if (TradeTypeHelper.isVanilla(trade.getType())) {
            checkExpiryPremiumDate(trade, response);
            if (trade.getStyle() == null) {
                response.setStatusCode(StatusCode.ERROR);
                response.addMessage(ErrorMassages.EMPTY_STYLE);
                logger.info(ErrorMassages.EMPTY_STYLE);
            } else if (trade.getStyle().toUpperCase().equals(OptionStyle.AMERICAN.name())) {
                checkExcerciseStartDate(trade, response);
            }
        } else if (TradeTypeHelper.isSpot(trade.getType())) {
            checkValueDateWorkingDays(trade, response, spotValueDate);
        } else if (TradeTypeHelper.isForward(trade.getType())) {
            checkValueDateWorkingDays(trade, response, forwardValueDate);
        }
        return response;
    }

    /**
     * Sets error message to the response in case Trade date is not exists.
     *
     * @param tradeDate
     * @param response
     * @param needToCheck
     * @return
     */
    private boolean isTradeDateExists(Date tradeDate, CheckResponse response, Boolean needToCheck) {
        if (tradeDate == null) {
            response.setStatusCode(StatusCode.ERROR);
            response.addMessage(ErrorMassages.EMPTY_TRADE_DATE);
            logger.info(ErrorMassages.EMPTY_TRADE_DATE);
            needToCheck = false;
        }
        return needToCheck;
    }

    /**
     * Sets error message to the response in case Value date is not exists.
     *
     * @param valueDate
     * @param response
     * @param needToCheck
     * @return
     */
    private boolean isValueDateExists(Date valueDate, CheckResponse response, Boolean needToCheck) {
        if (valueDate == null) {
            response.setStatusCode(StatusCode.ERROR);
            response.addMessage(ErrorMassages.EMPTY_VALUE_DATE);
            logger.info(ErrorMassages.EMPTY_VALUE_DATE);
            needToCheck = false;
        }
        return needToCheck;
    }

    /**
     * Sets error message to the response in case Delivery date is not exists.
     *
     * @param deliveryDate
     * @param response
     * @param needToCheck
     * @return
     */
    private boolean isDeliveryDateExists(Date deliveryDate, CheckResponse response, Boolean needToCheck) {
        if (deliveryDate == null) {
            response.setStatusCode(StatusCode.ERROR);
            response.addMessage(ErrorMassages.EMPTY_DELIVERY_DATE);
            logger.info(ErrorMassages.EMPTY_DELIVERY_DATE);
            needToCheck = false;
        }
        return needToCheck;
    }

    /**
     * Sets error message to the response in case Premium date is not exists.
     *
     * @param premiumDate
     * @param response
     * @param needToCheck
     * @return
     */
    private boolean isPremiumDateExists(Date premiumDate, CheckResponse response, Boolean needToCheck) {
        if (premiumDate == null) {
            response.setStatusCode(StatusCode.ERROR);
            response.addMessage(ErrorMassages.EMPTY_PREMIUM_DATE);
            logger.info(ErrorMassages.EMPTY_PREMIUM_DATE);
            needToCheck = false;
        }
        return needToCheck;
    }

    /**
     * Sets error message to the response in case Expiry date is not exists.
     *
     * @param expiryDate
     * @param response
     * @param needToCheck
     * @return
     */
    private boolean isExpiryDateExists(Date expiryDate, CheckResponse response, Boolean needToCheck) {
        if (expiryDate == null) {
            response.setStatusCode(StatusCode.ERROR);
            response.addMessage(ErrorMassages.EMPTY_EXPIRY_DATE);
            logger.info(ErrorMassages.EMPTY_EXPIRY_DATE);
            needToCheck = false;
        }
        return needToCheck;
    }

    /**
     * Sets error message to the response in case Exercise date is not exists.
     *
     * @param exerciseDate
     * @param response
     * @param needToCheck
     * @return
     */
    private boolean isExerciseDateExists(Date exerciseDate, CheckResponse response, Boolean needToCheck) {
        if (exerciseDate == null) {
            response.setStatusCode(StatusCode.ERROR);
            response.addMessage(ErrorMassages.EMPTY_EXERCISE_DATE);
            logger.info(ErrorMassages.EMPTY_EXERCISE_DATE);
            needToCheck = false;
        }
        return needToCheck;
    }

    /**
     * Calculates valueDate to be and compares it with actual. If they are no equal - error.
     *
     * @param trade    - trade to check.
     * @param response - response to add checking result.
     * @param days     - working days.
     */
    private void checkValueDateWorkingDays(Trade trade, CheckResponse response, int days) {
        Boolean isNeedToCheck = true;
        Date tradeDate = trade.getTradeDate();
        Date valueDate = trade.getValueDate();
        isNeedToCheck = isTradeDateExists(tradeDate, response, isNeedToCheck);
        isNeedToCheck = isValueDateExists(valueDate, response, isNeedToCheck);
        if (isNeedToCheck) {
            Calendar tradeDateCal = Calendar.getInstance();
            tradeDateCal.setTime(tradeDate);
            Calendar valueDateToBeCal = calculateEndDate(tradeDateCal, days);
            Calendar valueDateActual = Calendar.getInstance();
            valueDateActual.setTime(valueDate);
            logger.info("Value date year should be: " + valueDateToBeCal.get(Calendar.YEAR));
            logger.info("Value date month should be: " + valueDateToBeCal.get(Calendar.MONTH));
            logger.info("Value date day should be: " + valueDateToBeCal.get(Calendar.DAY_OF_MONTH));
            if (valueDateActual.get(Calendar.YEAR) != valueDateToBeCal.get(Calendar.YEAR)
                    || valueDateActual.get(Calendar.MONTH) != valueDateToBeCal.get(Calendar.MONTH)
                    || valueDateActual.get(Calendar.DAY_OF_MONTH) != valueDateToBeCal.get(Calendar.DAY_OF_MONTH)) {
                response.setStatusCode(StatusCode.ERROR);
                String message = String.format("Value date is not %d working days from Trade date.", days);
                response.addMessage(message);
                logger.info(message);
            }
        }
    }


    /**
     * Calculates working days from trade date.
     *
     * @param tradeDate where to start from.
     * @param duration  - working days.
     * @return date after calculation.
     */
    public Calendar calculateEndDate(Calendar tradeDate, int duration) {
        for (int i = 0; i < duration; i++) {
            tradeDate.add(Calendar.DAY_OF_MONTH, 1);
            while (!isWorking(tradeDate)) {
                tradeDate.add(Calendar.DAY_OF_MONTH, 1);
            }
        }
        return tradeDate;
    }

    /**
     * Value date cannot be before trade date.
     *
     * @param trade    - trade to check.
     * @param response - response to add checking result.
     */
    private void checkValueDateBeforeTradeDate(Trade trade, CheckResponse response) {
        Boolean isNeedToCheck = true;
        Date tradeDate = trade.getTradeDate();
        Date valueDate = trade.getValueDate();
        isNeedToCheck = isTradeDateExists(tradeDate, response, isNeedToCheck);
        isNeedToCheck = isValueDateExists(valueDate, response, isNeedToCheck);
        if (isNeedToCheck) {
            if (valueDate.before(tradeDate)) {
                response.setStatusCode(StatusCode.ERROR);
                String message = "Value date cannot be before Trade date.";
                response.addMessage(message);
                logger.info(message);
            }
        }
    }

    /**
     * Value date cannot fall on weekend or non-working day.
     *
     * @param trade    - trade to check.
     * @param response - response to add checking result.
     */
    private void checkValueDateIsWorking(Trade trade, CheckResponse response) {
        Boolean isNeedToCheck = true;
        Date valueDate = trade.getValueDate();
        isNeedToCheck = isValueDateExists(valueDate, response, isNeedToCheck);
        if (isNeedToCheck) {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(valueDate);
            if (!isWorking(calendar)) {
                response.setStatusCode(StatusCode.ERROR);
                String message = "Value date cannot be weekend or holiday.";
                response.addMessage(message);
                logger.info(message);
            }
        }
    }

    /**
     * Option specific: expiry date and premium date shall be before delivery date.
     *
     * @param trade    - trade to check.
     * @param response - response to add checking result.
     */
    private void checkExpiryPremiumDate(Trade trade, CheckResponse response) {
        Boolean isNeedToCheck = true;
        Date deliveryDate = trade.getDeliveryDate();
        Date premiumDate = trade.getPremiumDate();
        Date expiryDate = trade.getExpiryDate();
        isNeedToCheck = isDeliveryDateExists(deliveryDate, response, isNeedToCheck);
        isNeedToCheck = isPremiumDateExists(premiumDate, response, isNeedToCheck);
        isNeedToCheck = isExpiryDateExists(expiryDate, response, isNeedToCheck);
        if (isNeedToCheck) {
            if (!expiryDate.before(deliveryDate) || !premiumDate.before(deliveryDate)) {
                response.setStatusCode(StatusCode.ERROR);
                String message = "'Expiry date' and 'Premium date' shall be before 'Delivery date'.";
                response.addMessage(message);
                logger.info(message);
            }
        }
    }

    /**
     * American option specific: american option style will have in addition
     * the exercise Star tDate, which has to be after the trade date but before the expiry date.
     *
     * @param trade    - trade to check.
     * @param response - response to add checking result.
     */
    private void checkExcerciseStartDate(Trade trade, CheckResponse response) {
        Boolean isNeedToCheck = true;
        Date exerciseStartDate = trade.getExcerciseStartDate();
        Date tradeDate = trade.getTradeDate();
        Date expiryDate = trade.getExpiryDate();
        isNeedToCheck = isExerciseDateExists(exerciseStartDate, response, isNeedToCheck);
        isNeedToCheck = isTradeDateExists(tradeDate, response, isNeedToCheck);
        isNeedToCheck = isExpiryDateExists(expiryDate, response, isNeedToCheck);
        if (isNeedToCheck) {
            if (expiryDate.before(exerciseStartDate) || exerciseStartDate.before(tradeDate)) {
                response.setStatusCode(StatusCode.ERROR);
                String message = "'Exercise Start Date' has to be after the 'Trade date' but before the 'Expiry date'.";
                response.addMessage(message);
                logger.info(message);
            }
        }
    }

    /**
     * Checks id day is working day (should not be weekend and holiday).
     *
     * @param date to check.
     * @return true is day is working.
     */
    private boolean isWorking(Calendar date) {
        return !isHoliday(date, holidays) && !isWeekend(date);
    }

    /**
     * Checks if date is a holiday.
     *
     * @param date     to check.
     * @param holidays - collection of holidays.
     * @return true if date is holiday.
     */
    private boolean isHoliday(Calendar date, Collection<Calendar> holidays) {
        boolean result = false;
        for (Calendar cal : holidays) {
            if (cal.get(Calendar.MONTH) == date.get(Calendar.MONTH)
                    && cal.get(Calendar.DATE) == date.get(Calendar.DATE)
                    && cal.get(Calendar.YEAR) == date.get(Calendar.YEAR)) {
                result = true;
                break;
            }
        }
        return result;
    }

    /**
     * Checks if date is a saturday or sunday.
     *
     * @param date to check.
     * @return true if date is weekend.
     */
    private boolean isWeekend(Calendar date) {
        boolean result = false;
        if (date.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY || date.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY) {
            result = true;
        }
        return result;
    }
}
