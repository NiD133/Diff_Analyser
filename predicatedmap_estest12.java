package org.apache.commons.collections4.map;

import org.apache.commons.collections4.Predicate;
import org.apache.commons.collections4.functors.NonePredicate;
import org.apache.commons.collections4.functors.OnePredicate;
import org.apache.commons.collections4.functors.UniquePredicate;
import org.junit.Test;

import java.lang.reflect.Array;
import java.util.HashMap;
import java.util.Map;

/**
 * Contains tests for edge cases in {@link PredicatedMap}.
 */
public class PredicatedMap_ESTestTest12 {

    /**
     * Tests that a {@code put} operation throws a {@link StackOverflowError}
     * when the value predicate is defined recursively.
     *
     * <p>This scenario is engineered by creating a predicate that is a member of the
     * collection of predicates it is supposed to evaluate, leading to infinite recursion.
     */
    @Test(expected = StackOverflowError.class)
    public void putWithRecursiveValuePredicateShouldThrowStackOverflowError() {
        // Arrange: Set up a map with a value predicate designed to cause infinite recursion.

        // 1. Create an array to hold the predicates.
        @SuppressWarnings("unchecked")
        Predicate<Object>[] predicates = (Predicate<Object>[]) Array.newInstance(Predicate.class, 2);

        // 2. Create a predicate that evaluates the predicates in the array.
        OnePredicate<Object> recursivePredicate = new OnePredicate<>(predicates);

        // 3. Create the recursion: add the predicate to the very array it evaluates.
        // When recursivePredicate.evaluate() is called, it will eventually call itself.
        predicates[0] = recursivePredicate;

        // 4. The final value predicate will trigger the evaluation of the recursive predicate.
        Predicate<Object> valuePredicate = new NonePredicate<>(predicates);
        Predicate<Object> keyPredicate = new UniquePredicate<>();
        Map<Object, Integer> baseMap = new HashMap<>();
        Map<Object, Integer> predicatedMap = PredicatedMap.predicatedMap(baseMap, keyPredicate, valuePredicate);

        Integer testKeyAndValue = 4336;

        // Act & Assert: Attempting to put an entry will validate the value,
        // triggering the recursive predicate and causing a StackOverflowError.
        // The @Test(expected=...) annotation asserts that this error is thrown.
        predicatedMap.put(testKeyAndValue, testKeyAndValue);
    }
}