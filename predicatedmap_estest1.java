package org.apache.commons.collections4.map;

import org.apache.commons.collections4.Predicate;
import org.apache.commons.collections4.functors.TruePredicate;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

/**
 * Unit tests for {@link PredicatedMap}.
 */
public class PredicatedMapTest {

    /**
     * Tests that checkSetValue() throws a NullPointerException if the map
     * was constructed with a null valuePredicate. The method internally
     * calls evaluate() on the predicate without a null check.
     */
    @Test(expected = NullPointerException.class)
    public void checkSetValueShouldThrowNullPointerExceptionWhenValuePredicateIsNull() {
        // Arrange: Create a PredicatedMap with a null value predicate.
        // The key predicate can be any valid predicate; it's not relevant to this test.
        final Map<String, String> map = new HashMap<>();
        final Predicate<String> keyPredicate = TruePredicate.truePredicate();
        final Predicate<String> nullValuePredicate = null;

        // The constructor is protected, but this test is in the same package, so it's accessible.
        final PredicatedMap<String, String> predicatedMap =
            new PredicatedMap<>(map, keyPredicate, nullValuePredicate);

        // Act: Call the method under test. This is expected to throw a NullPointerException
        // because it will attempt to call a method on the null valuePredicate.
        predicatedMap.checkSetValue("anyValue");
    }
}