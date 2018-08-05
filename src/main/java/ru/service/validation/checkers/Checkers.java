package ru.service.validation.checkers;

import java.util.ArrayList;

/**
 * Store fo all CHECKERS.
 */
public class Checkers {
    private final static ArrayList<Checker> CHECKERS = new ArrayList<>();

    /**
     * You can add or delete checker here to control what checks should be performed.
     */
    public static void initCheckers() {
        CHECKERS.add(new CounterpartyChecker());
        CHECKERS.add(new StyleChecker());
        CHECKERS.add(new CCYChecker());
        CHECKERS.add(new DateChecker());
        CHECKERS.add(new LegalEntityChecker());
        CHECKERS.stream().forEach(Checker::init);
    }

    /**
     * Getter for all CHECKERS.
     *
     * @return
     */
    public static ArrayList<Checker> getCheckers() {
        return CHECKERS;
    }
}
