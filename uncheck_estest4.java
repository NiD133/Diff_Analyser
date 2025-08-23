package org.apache.commons.io.function;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

/**
 * Tests for the {@link Uncheck} utility class.
 */
public class UncheckTest {

    /**
     * Tests that {@link Uncheck#test(IOPredicate, Object)} correctly returns true
     * when the provided predicate evaluates to true and does not throw an exception.
     */
    @Test
    public void testShouldReturnTrueWhenPredicateIsTrue() {
        // Arrange: Create a predicate that always returns true.
        // The specific input value does not matter for this test.
        final IOPredicate<String> alwaysTruePredicate = IOPredicate.alwaysTrue();
        final String dummyInput = "any-string";

        // Act: Call the method under test.
        final boolean result = Uncheck.test(alwaysTruePredicate, dummyInput);

        // Assert: Verify that the result is true.
        assertTrue("Expected true for a predicate that always returns true", result);
    }
}