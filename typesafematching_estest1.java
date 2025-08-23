package org.mockito.internal.invocation;

import org.junit.Test;
import org.mockito.ArgumentMatcher;

/**
 * Unit tests for the {@link TypeSafeMatching} class.
 */
public class TypeSafeMatchingTest {

    /**
     * Verifies that the 'apply' method throws a NullPointerException when the
     * provided ArgumentMatcher is null. This is the expected behavior because
     * the matcher is essential for the type-safe matching logic and cannot be null.
     */
    @Test(expected = NullPointerException.class)
    public void apply_shouldThrowNullPointerException_whenMatcherIsNull() {
        // Arrange
        ArgumentMatcherAction typeSafeMatchingAction = TypeSafeMatching.matchesTypeSafe();
        Object anyArgument = new Object(); // The actual argument is irrelevant for this test.

        // Act & Assert
        // The method call is expected to throw a NullPointerException because the matcher is null.
        typeSafeMatchingAction.apply(null, anyArgument);
    }
}