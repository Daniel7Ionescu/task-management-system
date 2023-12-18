package com.teamrocket.tms.utils.calculators;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Map;

public class CompletionCalculator {

    private static final int TWO_DECIMALS = 2;

    private CompletionCalculator() {
    }

    public static double getPercentageComplete(Map<String, Boolean> itemsMap) {
        List<Boolean> valueList = itemsMap.values().stream().toList();
        int completedObjectives = (int) valueList.stream()
                .filter(item -> item)
                .count();
        double result = (double) completedObjectives / itemsMap.size() * 100;

        return roundUp(result, TWO_DECIMALS);
    }

    public static boolean checkCompleteBasedOnProgress(double progress) {
        return progress == 100;
    }

    public static double roundUp(double value, int numOfDecimals) {
        BigDecimal bigDecimal = BigDecimal.valueOf(value);
        bigDecimal = bigDecimal.setScale(numOfDecimals, RoundingMode.DOWN);

        return bigDecimal.doubleValue();
    }
}