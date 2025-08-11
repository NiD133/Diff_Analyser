/*
 * Copyright (c) 2017 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.invocation;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.internal.invocation.TypeSafeMatching.matchesTypeSafe;

import java.util.Date;
import java.util.concurrent.atomic.AtomicBoolean;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.ArgumentMatcher;
import org.mockito.Mock;
import org.mockito.internal.matchers.LessOrEqual;
import org.mockito.internal.matchers.Null;
import org.mockito.internal.matchers.StartsWith;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import org.mockitousage.IMethods;

/**
 * Tests for {@link TypeSafeMatching}.
 *
 * This suite verifies that {@code TypeSafeMatching} correctly identifies whether an argument's type
 * is compatible with an {@link ArgumentMatcher}'s expected type, preventing {@link ClassCastException}
 * and other runtime errors.
 */
public class TypeSafeMatchingTest {

    private static final Object INCOMPATIBLE_ARGUMENT = new Object();

    @Rule public MockitoRule mockitoRule = MockitoJUnit.rule();

    @Mock public IMethods mock;

    // --- Basic Type Incompatibility Tests ---

    /**
     * @see <a href="https://github.com/mockito/mockito/issues/457">Bug 457</a>
     */
    @Test
    public void shouldReturnFalseAndNotThrowNPEWhenArgumentIsNull() {
        // when
        boolean match = matchesTypeSafe().apply(new LessOrEqual<>(5), null);

        // then
        assertThat(match).isFalse();
    }

    @Test
    public void shouldReturnFalseAndNotThrowCCEWhenArgumentHasIncompatibleType() {
        // when
        boolean match = matchesTypeSafe().apply(new LessOrEqual<>(5), INCOMPATIBLE_ARGUMENT);

        // then
        assertThat(match).isFalse();
    }

    @Test
    public void shouldReturnFalseWhenMatcherExpectsIntegerAndArgumentIsString() {
        // when
        boolean match = matchesTypeSafe().apply(new LessOrEqual<>(5), "a string");

        // then
        assertThat(match).isFalse();
    }

    @Test
    public void shouldReturnFalseWhenMatcherExpectsStringAndArgumentIsInteger() {
        // when
        boolean match = matchesTypeSafe().apply(new StartsWith("Hello"), 123);

        // then
        assertThat(match).isFalse();
    }

    @Test
    public void shouldReturnTrueWhenUsingIsNullMatcherWithNullArgument() {
        // when
        boolean match = matchesTypeSafe().apply(Null.NULL, null);

        // then
        assertThat(match).isTrue();
    }

    // --- Advanced Generic and Reflection Tests ---

    @Test
    public void shouldCorrectlyIdentifyMatchesMethodAndIgnoreOverloads() {
        // given
        class MatcherWithOverloads implements ArgumentMatcher<Integer> {
            @Override
            public boolean matches(Integer argument) {
                return true; // This is the correct method to be called.
            }

            @SuppressWarnings("unused")
            public boolean matches(Date argument) {
                throw new UnsupportedOperationException("Should not be called");
            }

            @SuppressWarnings("unused")
            public boolean matches(Integer argument, Void v) {
                throw new UnsupportedOperationException("Should not be called");
            }
        }

        // when
        boolean match = matchesTypeSafe().apply(new MatcherWithOverloads(), 123);

        // then
        assertThat(match).isTrue();
    }

    @Test
    public void shouldFindArgumentTypeWhenMatcherInheritsFromGenericClass() {
        // given
        abstract class GenericMatcher<T> implements ArgumentMatcher<T> {}

        class IntegerMatcher extends GenericMatcher<Integer> {
            @Override
            public boolean matches(Integer argument) {
                return true;
            }
        }

        // when
        boolean match = matchesTypeSafe().apply(new IntegerMatcher(), 123);

        // then
        assertThat(match).isTrue();
    }

    @Test
    public void shouldInvokeMatcherForCompatibleTypeAndNotForIncompatibleType() {
        // given
        final AtomicBoolean wasCalled = new AtomicBoolean(false);

        abstract class GenericMatcher<T> implements ArgumentMatcher<T> {}
        class IntegerMatcher extends GenericMatcher<Integer> {
            @Override
            public boolean matches(Integer argument) {
                wasCalled.set(true);
                return true;
            }
        }
        ArgumentMatcher<Integer> integerMatcher = new IntegerMatcher();

        // when a compatible argument is provided
        wasCalled.set(false);
        matchesTypeSafe().apply(integerMatcher, 123);
        // then the matcher is invoked
        assertThat(wasCalled.get()).isTrue();

        // when an incompatible argument is provided
        wasCalled.set(false);
        matchesTypeSafe().apply(integerMatcher, "a string");
        // then the matcher is not invoked
        assertThat(wasCalled.get()).isFalse();
    }

    @Test
    public void shouldTreatRawGenericMatcherAsAcceptingAnyObject() {
        // given
        final AtomicBoolean wasCalled = new AtomicBoolean(false);
        // A matcher with a raw generic type, which erases to Object.
        class RawGenericMatcher<T> implements ArgumentMatcher<T> {
            @Override
            public boolean matches(T argument) {
                wasCalled.set(true);
                return true;
            }
        }
        ArgumentMatcher<Integer> rawMatcher = new RawGenericMatcher<>();

        // when called with an Integer
        wasCalled.set(false);
        matchesTypeSafe().apply(rawMatcher, 123);
        // then the matcher is invoked because the type check passes against Object
        assertThat(wasCalled.get()).isTrue();

        // when called with a String
        wasCalled.set(false);
        matchesTypeSafe().apply(rawMatcher, "a string");
        // then the matcher is also invoked because the type check passes against Object
        assertThat(wasCalled.get()).isTrue();
    }
}