package org.apache.commons.collections4.map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.util.HashMap;
import java.util.Map;
import org.apache.commons.collections4.Predicate;
import org.apache.commons.collections4.functors.OrPredicate;
import org.junit.Test;

/**
 * Tests for {@link PredicatedMap} focusing on its behavior when predicates throw exceptions.
 */
public class PredicatedMapTest {

    /**
     * Tests that PredicatedMap.put() propagates a NullPointerException thrown by a faulty key predicate.
     */
    @Test
    public void putWithFaultyKeyPredicateShouldPropagateNPE() {
        // Arrange
        // An OrPredicate constructed with null sub-predicates will throw a NullPointerException
        // when its `evaluate` method is called. This simulates a faulty predicate.
        final Predicate<Object> faultyKeyPredicate = new OrPredicate<>(null, null);
        final Map<Object, Object> baseMap = new HashMap<>();

        // Create a PredicatedMap that uses the faulty predicate for key validation.
        // The value predicate is not relevant for this test, so it can be null.
        final Map<Object, Object> predicatedMap = new PredicatedMap<>(baseMap, faultyKeyPredicate, null);

        // Act & Assert
        try {
            // Attempting to add any entry should trigger the faulty key predicate.
            predicatedMap.put("any key", "any value");
            fail("A NullPointerException should have been thrown by the faulty key predicate.");
        } catch (final NullPointerException e) {
            // This is the expected outcome.
            // For a more robust test, we verify that the exception originates from the
            // OrPredicate, confirming the predicate's internal error is being propagated.
            // This check replaces the original test's non-standard `verifyException` call.
            final String throwingClassName = e.getStackTrace()[0].getClassName();
            assertEquals("org.apache.commons.collections4.functors.OrPredicate", throwingClassName);
        }
    }
}