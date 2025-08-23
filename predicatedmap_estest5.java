package org.apache.commons.collections4.map;

import org.apache.commons.collections4.Predicate;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertFalse;

/**
 * This class contains tests for the {@link PredicatedMap}.
 * The original test was auto-generated and has been refactored for clarity.
 */
public class PredicatedMap_ESTestTest5 extends PredicatedMap_ESTest_scaffolding {

    /**
     * Tests that isSetValueChecking() returns false when the map is constructed
     * with a null value predicate, indicating that no value validation will occur.
     */
    @Test
    public void isSetValueCheckingShouldReturnFalseWhenValuePredicateIsNull() {
        // Arrange
        final Map<String, Integer> decoratedMap = new HashMap<>();
        final Predicate<? super Integer> valuePredicate = null;

        // Create a PredicatedMap with a null value predicate. The key predicate is
        // also set to null as it is not relevant to this test.
        final PredicatedMap<String, Integer> predicatedMap =
                new PredicatedMap<>(decoratedMap, null, valuePredicate);

        // Act
        final boolean isChecking = predicatedMap.isSetValueChecking();

        // Assert
        assertFalse("isSetValueChecking() should be false when no value predicate is set.", isChecking);
    }
}