package ru.service.utils;

import ru.service.enums.InstrumentType;

/**
 * Util to define trade type from string.
 */
public class TradeTypeHelper {

    public static boolean isSpot(String instrType) {
        return instrType.toUpperCase().equals(InstrumentType.SPOT.name());
    }

    public static boolean isForward(String instrType) {
        return instrType.toUpperCase().equals(InstrumentType.FORWARD.name());
    }

    public static boolean isVanilla(String instrType) {
        return instrType.toUpperCase().equals(InstrumentType.VANILLAOPTION.name());
    }
}
