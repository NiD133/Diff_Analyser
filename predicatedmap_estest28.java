package org.apache.commons.collections4.map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.collections4.FunctorException;
import org.apache.commons.collections4.Predicate;
import org.apache.commons.collections4.functors.ExceptionPredicate;
import org.junit.Test;

/**
 * Contains tests for the exception-handling behavior of {@link PredicatedMap}.
 */
public class PredicatedMapExceptionTest {

    /**
     * Tests that if the key predicate throws an exception, the {@code put} method
     * propagates that exception.
     */
    @Test
    public void putShouldPropagateExceptionFromKeyPredicate() {
        // Arrange: Create a map decorated with a predicate that always throws an exception.
        final Predicate<String> exceptionPredicate = ExceptionPredicate.exceptionPredicate();
        final Map<String, String> baseMap = new HashMap<>();

        // The value predicate is irrelevant here, as the key predicate is evaluated first.
        final PredicatedMap<String, String> predicatedMap =
            PredicatedMap.predicatedMap(baseMap, exceptionPredicate, null);

        // Act & Assert: Verify that calling put() results in the expected exception.
        try {
            predicatedMap.put("anyKey", "anyValue");
            fail("Expected a FunctorException to be thrown because the key predicate is an ExceptionPredicate.");
        } catch (final FunctorException e) {
            // This is the expected behavior.
            // The ExceptionPredicate is documented to throw a FunctorException with this specific message.
            assertEquals("ExceptionPredicate invoked", e.getMessage());
        }
    }
}