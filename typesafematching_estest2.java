package org.mockito.internal.invocation;

import org.junit.Test;
import org.mockito.ArgumentMatcher;
import org.mockito.internal.matchers.CompareEqual;
import org.mockito.internal.matchers.Not;

import static org.junit.Assert.fail;

/**
 * Tests for {@link TypeSafeMatching}.
 */
public class TypeSafeMatchingTest {

    @Test
    public void apply_shouldPropagateClassCastException_whenThrownByMatcherImplementation() {
        // Arrange
        // We set up a scenario where a matcher's internal logic will cause a ClassCastException.
        // The goal is to verify that TypeSafeMatching.apply() does not suppress this exception
        // but lets it propagate, as the failure occurs inside the matcher, not due to a
        // direct type mismatch with the method signature.

        // 1. The `CompareEqual` matcher's implementation may attempt to cast its argument to Comparable.
        ArgumentMatcher<Integer> innerMatcher = new CompareEqual<>(null);
        ArgumentMatcher<Integer> matcherWithInternalCasting = new Not(innerMatcher);

        // 2. We use the matcher itself as the argument. A `Not` instance is not a `Comparable`,
        //    so this will trigger a ClassCastException deep inside the `CompareEqual` matcher's logic.
        Object incompatibleArgument = matcherWithInternalCasting;

        ArgumentMatcherAction action = TypeSafeMatching.matchesTypeSafe();

        // Act & Assert
        try {
            action.apply(matcherWithInternalCasting, incompatibleArgument);
            fail("Expected a ClassCastException to be thrown by the matcher's internal logic.");
        } catch (ClassCastException e) {
            // Success: The expected exception was caught and propagated correctly.
            // This confirms that `apply` does not hide exceptions from faulty matcher implementations.
        }
    }
}