package org.mockito.internal.invocation;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.internal.invocation.TypeSafeMatching.matchesTypeSafe;

import java.util.concurrent.atomic.AtomicBoolean;
import org.junit.Test;
import org.mockito.ArgumentMatcher;

/**
 * Tests for {@link TypeSafeMatching}.
 */
public class TypeSafeMatchingTest {

    /**
     * This test verifies the behavior of {@code TypeSafeMatching} when it cannot
     * determine the generic type of an {@link ArgumentMatcher} at runtime. This can
     * happen with certain constructs like non-static inner classes due to Java's
     * type erasure.
     *
     * <p>In such cases, {@code TypeSafeMatching} should safely default to treating
     * the matcher's expected type as {@code Object}. This means the matcher is
     * always invoked, regardless of the argument's type, preventing potential
     * {@code ClassCastException}s that might otherwise occur within the matcher itself.
     */
    @Test
    public void shouldAlwaysInvokeMatcherWhenItsGenericTypeIsIndeterminate() {
        // given: A matcher implemented as a non-static inner class, whose generic
        // type <T> is difficult to resolve at runtime.
        class NonStaticGenericMatcher<T> implements ArgumentMatcher<T> {
            private final AtomicBoolean wasCalled = new AtomicBoolean(false);

            @Override
            public boolean matches(T argument) {
                wasCalled.set(true);
                return true; // Return value is not relevant for this test
            }

            void reset() {
                wasCalled.set(false);
            }

            boolean wasCalled() {
                return wasCalled.get();
            }
        }
        NonStaticGenericMatcher<Integer> matcher = new NonStaticGenericMatcher<>();

        // when: The matcher is applied to an argument of the correct type.
        matchesTypeSafe().apply(matcher, 123);
        // then: The matcher should be invoked as a baseline confirmation.
        assertThat(matcher.wasCalled())
            .withFailMessage("Sanity check failed: Matcher should be called for a compatible type.")
            .isTrue();

        // and given: The matcher's state is reset.
        matcher.reset();
        assertThat(matcher.wasCalled()).isFalse(); // Verify reset worked

        // when: The matcher is applied to an argument of an incompatible type.
        matchesTypeSafe().apply(matcher, "a string");
        // then: The matcher should still be invoked due to the fallback mechanism.
        assertThat(matcher.wasCalled())
            .withFailMessage(
                "Matcher should be called even for an incompatible type "
                    + "as a fallback when its generic type cannot be determined.")
            .isTrue();
    }
}