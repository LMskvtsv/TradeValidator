package ru.domain;

import java.util.ArrayList;
import java.util.List;

/**
 * All tests in incoming json object should be stored in array. This object represents the array of tests.
 */
public class Trades {

    private List<Trade> tests = new ArrayList<Trade>();

    public List<Trade> getTests() {
        return tests;
    }

    public void setTests(List<Trade> tests) {
        this.tests = tests;
    }
}
