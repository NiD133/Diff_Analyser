package org.mockito.internal.invocation;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.internal.invocation.TypeSafeMatching.matchesTypeSafe;

import org.junit.Test;
import org.mockito.ArgumentMatcher;
import org.mockito.internal.matchers.StartsWith;

/**
 * Tests for {@link TypeSafeMatching}.
 */
public class TypeSafeMatchingTest {

    /**
     * This test verifies that the type-safe matching logic correctly identifies
     * an incompatibility between a matcher's expected type and an actual argument's type.
     * <p>
     * A {@code StartsWith} matcher expects a {@code String}, but it is provided with an
     * {@code Integer}. The expected behavior is for the matching to fail gracefully
     * by returning {@code false}, rather than throwing a {@code ClassCastException}.
     */
    @Test
    public void shouldReturnFalseWhenArgumentTypeIsIncompatibleWithMatcher() {
        // Arrange
        // A matcher that expects a String argument.
        ArgumentMatcher<String> stringMatcher = new StartsWith("Hello");
        // An argument of a type (Integer) that is not compatible with the matcher.
        Object incompatibleArgument = 123;

        // Act
        // The 'apply' method should perform a type-safe check.
        boolean isMatch = matchesTypeSafe().apply(stringMatcher, incompatibleArgument);

        // Assert
        // The result should be false, indicating the argument does not match
        // because its type is incompatible. No ClassCastException should be thrown.
        assertThat(isMatch).isFalse();
    }
}