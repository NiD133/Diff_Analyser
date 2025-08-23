package org.mockito.internal.invocation;

import org.junit.Test;
import org.mockito.ArgumentMatcher;
import org.mockito.internal.matchers.GreaterOrEqual;

import static org.junit.Assert.assertFalse;

/**
 * Tests for {@link TypeSafeMatching}.
 */
public class TypeSafeMatchingTest {

    /**
     * Verifies that the TypeSafeMatching action correctly identifies when an argument's
     * type is incompatible with the matcher's expected generic type.
     *
     * The `apply` method should return `false` in this scenario, preventing a potential
     * ClassCastException that would occur if the matcher's `matches` method were called directly.
     */
    @Test
    public void shouldReturnFalseWhenArgumentTypeIsIncompatibleWithMatcherType() {
        // Arrange
        // The action under test, which performs a type-safe check.
        ArgumentMatcherAction typeSafeMatching = TypeSafeMatching.matchesTypeSafe();

        // A matcher that is parameterized to only accept Integers.
        ArgumentMatcher<Integer> integerMatcher = new GreaterOrEqual<>(100);

        // An argument of a type (Object) that is not an Integer.
        Object incompatibleArgument = new Object();

        // Act
        // Apply the type-safe matching logic.
        boolean isMatch = typeSafeMatching.apply(integerMatcher, incompatibleArgument);

        // Assert
        // The result must be false because a plain Object cannot be safely passed to a matcher for Integers.
        assertFalse("Expected type-safe matching to fail for an incompatible argument type", isMatch);
    }
}