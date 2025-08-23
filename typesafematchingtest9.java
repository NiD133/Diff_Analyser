package org.mockito.internal.invocation;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.internal.invocation.TypeSafeMatching.matchesTypeSafe;

import java.util.concurrent.atomic.AtomicBoolean;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.ArgumentMatcher;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

/**
 * Tests for {@link TypeSafeMatching}.
 */
public class TypeSafeMatchingTest {

    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule();

    // A generic base class to test type resolution through inheritance.
    private abstract static class GenericMatcher<T> implements ArgumentMatcher<T> {
        // No implementation needed, just for establishing a generic type hierarchy.
    }

    // A concrete matcher implementation that inherits its argument type (Integer)
    // from the generic superclass.
    private static class IntegerMatcher extends GenericMatcher<Integer> {
        private final AtomicBoolean wasInvoked;

        IntegerMatcher(AtomicBoolean wasInvoked) {
            this.wasInvoked = wasInvoked;
        }

        @Override
        public boolean matches(Integer argument) {
            wasInvoked.set(true);
            // The return value is not important for this test.
            return true;
        }
    }

    @Test
    public void shouldCorrectlyMatchTypeWhenArgumentTypeIsDefinedInGenericSuperclass() {
        // This test verifies that TypeSafeMatching can correctly determine an
        // ArgumentMatcher's type even when it's defined in a generic superclass.

        // Arrange
        final AtomicBoolean matcherInvoked = new AtomicBoolean(false);
        final ArgumentMatcher<Integer> integerMatcher = new IntegerMatcher(matcherInvoked);

        // Act: Apply the matcher with a compatible argument (Integer).
        matchesTypeSafe().apply(integerMatcher, 123);

        // Assert: The matcher should be invoked.
        assertThat(matcherInvoked.get())
            .as("Matcher should be invoked for a type-compatible argument")
            .isTrue();

        // --- Second scenario: incorrect type ---

        // Arrange: Reset the flag for the next check.
        matcherInvoked.set(false);

        // Act: Apply the matcher with an incompatible argument (String).
        matchesTypeSafe().apply(integerMatcher, "a string");

        // Assert: The matcher should NOT be invoked due to the type mismatch.
        assertThat(matcherInvoked.get())
            .as("Matcher should not be invoked for a type-incompatible argument")
            .isFalse();
    }
}