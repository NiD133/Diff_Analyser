package org.apache.commons.collections4.map;

import org.apache.commons.collections4.Predicate;
import org.apache.commons.collections4.functors.NullPredicate;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertNull;

/**
 * Contains tests for the {@link PredicatedMap#checkSetValue(Object)} method.
 */
public class PredicatedMapTest {

    /**
     * Tests that checkSetValue returns the provided value (null in this case)
     * when the value predicate evaluates to true.
     */
    @Test
    public void checkSetValueShouldReturnNullWhenPredicateAcceptsNull() {
        // Arrange: Create a PredicatedMap with a predicate that allows null values.
        // The key predicate is irrelevant for this test and can be null.
        final Map<String, Integer> emptyMap = new HashMap<>();
        final Predicate<Integer> acceptsNullPredicate = NullPredicate.nullPredicate();
        final PredicatedMap<String, Integer> predicatedMap = new PredicatedMap<>(emptyMap, null, acceptsNullPredicate);

        // Act: Check a null value against the predicate.
        final Integer validatedValue = predicatedMap.checkSetValue(null);

        // Assert: The method should return the original null value, as the predicate passed.
        assertNull("The returned value should be null because the predicate allows it.", validatedValue);
    }
}