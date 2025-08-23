package org.apache.commons.io.function;

import static org.junit.Assert.assertFalse;

import org.junit.Test;

/**
 * Tests for the {@link Uncheck} utility class.
 */
public class UncheckTest {

    /**
     * Tests that {@link Uncheck#test(IOPredicate, Object)} correctly returns false
     * when the provided predicate evaluates to false.
     */
    @Test
    public void testWithPredicateReturningFalse() {
        // Arrange: Create a predicate that always returns false.
        final IOPredicate<String> alwaysFalsePredicate = IOPredicate.alwaysFalse();
        final String irrelevantInput = "test-input";

        // Act: Call the Uncheck.test method with the predicate.
        final boolean result = Uncheck.test(alwaysFalsePredicate, irrelevantInput);

        // Assert: The result should be false, matching the predicate's behavior.
        assertFalse("The result should be false when the predicate is always false.", result);
    }
}