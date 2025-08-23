package org.mockito.internal.invocation;

import org.junit.Test;
import org.mockito.ArgumentMatcher;
import org.mockito.internal.matchers.NotNull;

import static org.junit.Assert.assertFalse;

/**
 * Tests for {@link TypeSafeMatching}.
 */
public class TypeSafeMatchingTest {

    /**
     * This test verifies that the TypeSafeMatching action correctly handles null arguments.
     * <p>
     * A null argument is considered type-compatible with any matcher. Therefore, the action
     * should proceed to call the underlying matcher's 'matches' method. In this case,
     * the {@link NotNull} matcher should correctly return false for a null input.
     */
    @Test
    public void shouldReturnFalseWhenNotNullMatcherIsAppliedToNullArgument() {
        // Arrange
        ArgumentMatcherAction typeSafeMatching = TypeSafeMatching.matchesTypeSafe();
        ArgumentMatcher<Object> notNullMatcher = new NotNull<>();

        // Act
        boolean isMatch = typeSafeMatching.apply(notNullMatcher, null);

        // Assert
        assertFalse("A NotNull matcher should return false for a null argument", isMatch);
    }
}