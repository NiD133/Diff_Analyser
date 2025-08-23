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

public class TypeSafeMatchingTest {

    private static final Object NON_COMPARABLE_OBJECT = new Object();

    @Rule 
    public MockitoRule mockitoRule = MockitoJUnit.rule();

    @Mock 
    public IMethods mock;

    /**
     * Test to ensure that a null argument does not throw a NullPointerException.
     * Related issue: https://github.com/mockito/mockito/issues/457
     */
    @Test
    public void shouldNotThrowExceptionForNullArgument() {
        boolean isMatch = matchesTypeSafe().apply(new LessOrEqual<Integer>(5), null);
        assertThat(isMatch).isFalse();
    }

    /**
     * Test to ensure that a non-comparable object does not throw a ClassCastException.
     */
    @Test
    public void shouldNotThrowExceptionForNonComparableObject() {
        boolean isMatch = matchesTypeSafe().apply(new LessOrEqual<Integer>(5), NON_COMPARABLE_OBJECT);
        assertThat(isMatch).isFalse();
    }

    /**
     * Test to ensure that comparing null values does not throw a ClassCastException.
     */
    @Test
    public void shouldNotThrowExceptionForNullComparison() {
        boolean isMatch = matchesTypeSafe().apply(new LessOrEqual<Integer>(null), null);
        assertThat(isMatch).isFalse();
    }

    /**
     * Test to ensure that comparing null with a Null matcher returns true.
     */
    @Test
    public void shouldMatchNullWithNullMatcher() {
        boolean isMatch = matchesTypeSafe().apply(Null.NULL, null);
        assertThat(isMatch).isTrue();
    }

    /**
     * Test to ensure that comparing a string matcher with an integer does not throw a ClassCastException.
     */
    @Test
    public void shouldNotMatchStringMatcherWithInteger() {
        boolean isMatch = matchesTypeSafe().apply(new StartsWith("Hello"), 123);
        assertThat(isMatch).isFalse();
    }

    /**
     * Test to ensure that comparing an integer matcher with a string does not throw a ClassCastException.
     */
    @Test
    public void shouldNotMatchIntegerMatcherWithString() {
        boolean isMatch = matchesTypeSafe().apply(new LessOrEqual<Integer>(5), "Hello");
        assertThat(isMatch).isFalse();
    }

    /**
     * Test to ensure that overloaded matches methods are ignored.
     */
    @Test
    public void shouldIgnoreOverloadedMatchesMethods() {
        class TestMatcher implements ArgumentMatcher<Integer> {
            @Override
            public boolean matches(Integer arg) {
                return false;
            }

            @SuppressWarnings("unused")
            public boolean matches(Date arg) {
                throw new UnsupportedOperationException();
            }

            @SuppressWarnings("unused")
            public boolean matches(Integer arg, Void v) {
                throw new UnsupportedOperationException();
            }
        }

        boolean isMatch = matchesTypeSafe().apply(new TestMatcher(), 123);
        assertThat(isMatch).isFalse();
    }

    /**
     * Test to ensure that a matcher with a subtype extending a generic class matches correctly.
     */
    @Test
    public void shouldMatchWithSubtypeExtendingGenericClass() {
        abstract class GenericMatcher<T> implements ArgumentMatcher<T> {}
        class TestMatcher extends GenericMatcher<Integer> {
            @Override
            public boolean matches(Integer argument) {
                return true;
            }
        }
        boolean isMatch = matchesTypeSafe().apply(new TestMatcher(), 123);
        assertThat(isMatch).isTrue();
    }

    /**
     * Test to ensure that a matcher with a subtype extending a generic class does not match incompatible types.
     */
    @Test
    public void shouldNotMatchWithSubtypeExtendingGenericClassForIncompatibleTypes() {
        final AtomicBoolean wasCalled = new AtomicBoolean();

        abstract class GenericMatcher<T> implements ArgumentMatcher<T> {}
        class TestMatcher extends GenericMatcher<Integer> {
            @Override
            public boolean matches(Integer argument) {
                wasCalled.set(true);
                return true;
            }
        }

        wasCalled.set(false);
        matchesTypeSafe().apply(new TestMatcher(), 123);
        assertThat(wasCalled.get()).isTrue();

        wasCalled.set(false);
        matchesTypeSafe().apply(new TestMatcher(), "");
        assertThat(wasCalled.get()).isFalse();
    }

    /**
     * Test to ensure that every argument type is passed if no bridge method was generated.
     */
    @Test
    public void shouldPassEveryArgumentTypeIfNoBridgeMethodGenerated() {
        final AtomicBoolean wasCalled = new AtomicBoolean();
        class GenericMatcher<T> implements ArgumentMatcher<T> {
            @Override
            public boolean matches(T argument) {
                wasCalled.set(true);
                return true;
            }
        }

        wasCalled.set(false);
        matchesTypeSafe().apply(new GenericMatcher<Integer>(), 123);
        assertThat(wasCalled.get()).isTrue();

        wasCalled.set(false);
        matchesTypeSafe().apply(new GenericMatcher<Integer>(), "");
        assertThat(wasCalled.get()).isTrue();
    }
}