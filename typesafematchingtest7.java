package org.mockito.internal.invocation;

import org.junit.Rule;
import org.junit.Test;
import org.mockito.ArgumentMatcher;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import java.util.Date;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.internal.invocation.TypeSafeMatching.matchesTypeSafe;

/**
 * Tests for {@link TypeSafeMatching}.
 */
public class TypeSafeMatchingTest {

    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule();

    @Test
    public void shouldInvokeCorrectMatchesMethod_whenMatcherHasOverloadedMatchesMethods() {
        // given
        // A custom matcher with multiple methods named 'matches'. The goal is to verify
        // that TypeSafeMatching correctly identifies and invokes the one defined by the
        // ArgumentMatcher<Integer> interface, not the other overloads.
        class MatcherWithOverloads implements ArgumentMatcher<Integer> {
            @Override
            public boolean matches(Integer argument) {
                // This is the correct method that should be invoked.
                return false;
            }

            // This overload has a different parameter type. It should be ignored.
            // If called, it will throw an exception and fail the test.
            @SuppressWarnings("unused")
            public boolean matches(Date argument) {
                throw new UnsupportedOperationException("This overloaded 'matches' method should not be called.");
            }

            // This overload has a different number of parameters. It should also be ignored.
            @SuppressWarnings("unused")
            public boolean matches(Integer argument, Void v) {
                throw new UnsupportedOperationException("This overloaded 'matches' method should not be called.");
            }
        }
        ArgumentMatcher<Integer> matcher = new MatcherWithOverloads();
        Integer argument = 123;

        // when
        // The TypeSafeMatching action is applied. It must find the correct 'matches(Integer)' method.
        boolean result = matchesTypeSafe().apply(matcher, argument);

        // then
        // The result must be 'false', as returned by the correct method.
        // The test also implicitly verifies that no exception was thrown, proving
        // that the other 'matches' methods were correctly ignored.
        assertThat(result).isFalse();
    }
}