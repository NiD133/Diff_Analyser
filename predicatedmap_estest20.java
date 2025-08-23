package org.apache.commons.collections4.map;

import org.apache.commons.collections4.Predicate;
import org.apache.commons.collections4.functors.UniquePredicate;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

/**
 * Tests for {@link PredicatedMap} constructor validation.
 */
public class PredicatedMapConstructorTest {

    @Test
    public void constructorShouldThrowExceptionWhenExistingMapValueFailsValidation() {
        // Arrange: Create a predicate that only accepts an object once.
        // We will use the same instance for both key and value validation.
        Predicate<Object> uniquePredicate = new UniquePredicate<>();

        // Arrange: Create a map with an entry where the key and value are the same object.
        Map<Object, Object> initialMap = new HashMap<>();
        initialMap.put(uniquePredicate, uniquePredicate);

        // Act & Assert: Attempt to create a PredicatedMap.
        // The constructor validates existing entries. The process is as follows:
        // 1. The key (uniquePredicate) is validated first. This succeeds, and the
        //    predicate internally records that it has seen this object.
        // 2. The value (the same uniquePredicate instance) is validated second. This fails
        //    because the UniquePredicate has already seen this object.
        try {
            new PredicatedMap<>(initialMap, uniquePredicate, uniquePredicate);
            fail("Expected an IllegalArgumentException because the value predicate should fail.");
        } catch (final IllegalArgumentException e) {
            // Assert: Verify the exception message is correct.
            assertEquals("Cannot add value - Predicate rejected it", e.getMessage());
        }
    }
}