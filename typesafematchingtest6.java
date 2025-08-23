package org.mockito.internal.invocation;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.internal.invocation.TypeSafeMatching.matchesTypeSafe;

import org.junit.Test;
import org.mockito.ArgumentMatcher;
import org.mockito.internal.matchers.LessOrEqual;

/**
 * Tests for {@link TypeSafeMatching}.
 */
public class TypeSafeMatchingTest {

    @Test
    public void shouldReturnFalseWhenArgumentTypeIsIncompatibleWithMatcherType() {
        // This test verifies that type-safe matching correctly identifies when an argument's
        // type is incompatible with the matcher's declared generic type.
        // Here, the matcher expects an Integer, but is given a String.

        // Arrange
        ArgumentMatcher<Integer> integerMatcher = new LessOrEqual<>(5);
        Object incompatibleArgument = "a string";

        // Act
        boolean isMatch = matchesTypeSafe().apply(integerMatcher, incompatibleArgument);

        // Assert
        assertThat(isMatch).isFalse();
    }
}