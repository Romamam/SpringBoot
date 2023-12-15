package com.example.springboot.util;

import java.util.ArrayList;
import java.util.List;

public class CombinationGenerator {

    public static <T> List<List<T>> generateCombinations(List<T> elements, int combinationSize) {
        List<List<T>> result = new ArrayList<>();
        generateCombinations(elements, combinationSize, 0, new ArrayList<>(), result);
        return result;
    }

    private static <T> void generateCombinations(List<T> elements, int combinationSize, int index,
                                                 List<T> currentCombination, List<List<T>> allCombinations) {
        if (currentCombination.size() == combinationSize) {
            allCombinations.add(new ArrayList<>(currentCombination));
            return;
        }

        for (int i = index; i < elements.size(); i++) {
            currentCombination.add(elements.get(i));
            generateCombinations(elements, combinationSize, i + 1, currentCombination, allCombinations);
            currentCombination.remove(currentCombination.size() - 1);
        }
    }
}
