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

    /**
     * Verifies that a null argument does not cause a NullPointerException
     * and correctly results in a non-match for a type-safe matcher.
     *
     * @see <a href="https://github.com/mockito/mockito/issues/457">Bug 457</a>
     */
    @Test
    public void shouldReturnFalseWhenArgumentIsNull() {
        // Arrange
        ArgumentMatcher<Integer> matcher = new LessOrEqual<>(5);

        // Act
        boolean isMatch = matchesTypeSafe().apply(matcher, null);

        // Assert
        assertThat(isMatch).isFalse();
    }
}