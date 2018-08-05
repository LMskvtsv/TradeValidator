package ru.service.validation.checkers;

import ru.service.messages.CheckResponse;
import ru.domain.Trade;

/**
 * Interface for checkers.
 */
public interface Checker {
    /**
     * Check something you would like.
     *
     * @param trade    to check.
     * @param response response where to store result.
     * @return updated or not response.
     */
    CheckResponse check(Trade trade, CheckResponse response);

    /**
     * Initialisation.
     */
    void init();
}
