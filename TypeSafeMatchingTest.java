/*
 * Copyright (c) 2017 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.invocation;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.internal.invocation.TypeSafeMatching.matchesTypeSafe;

import java.util.Date;
import java.util.concurrent.atomic.AtomicBoolean;

import org.junit.Test;
import org.mockito.ArgumentMatcher;
import org.mockito.internal.matchers.LessOrEqual;
import org.mockito.internal.matchers.Null;
import org.mockito.internal.matchers.StartsWith;

public class TypeSafeMatchingTest {

    // Test fixtures
    private static final Object NON_COMPARABLE = new Object();
    private static final Integer FIVE = 5;
    private static final String HELLO = "Hello";

    // Small helper to reduce noise in tests
    private static boolean applyMatch(ArgumentMatcher<?> matcher, Object argument) {
        return matchesTypeSafe().apply(matcher, argument);
    }

    // --- Null handling and non-comparable arguments ---------------------------------------------

    /**
     * Comparable-based matcher should not match null and must not throw NPE.
     */
    @Test
    public void nullArgumentIsNotMatchedByComparableMatcher() {
        boolean result = applyMatch(new LessOrEqual<Integer>(FIVE), null);
        assertThat(result).isFalse();
    }

    /**
     * Comparable-based matcher should not match a non-comparable argument and must not throw CCE.
     */
    @Test
    public void nonComparableArgumentIsNotMatchedByComparableMatcher() {
        boolean result = applyMatch(new LessOrEqual<Integer>(FIVE), NON_COMPARABLE);
        assertThat(result).isFalse();
    }

    /**
     * Comparable-based matcher created with a null reference should not match null and must not throw CCE.
     */
    @Test
    public void comparableMatcherWithNullReferenceDoesNotMatchNullArgument() {
        boolean result = applyMatch(new LessOrEqual<Integer>(null), null);
        assertThat(result).isFalse();
    }

    /**
     * Null matcher should match null.
     */
    @Test
    public void nullMatcherMatchesNullArgument() {
        boolean result = applyMatch(Null.NULL, null);
        assertThat(result).isTrue();
    }

    // --- Mixed type comparisons -----------------------------------------------------------------

    @Test
    public void stringMatcherDoesNotMatchIntegerArgument() {
        boolean result = applyMatch(new StartsWith(HELLO), 123);
        assertThat(result).isFalse();
    }

    @Test
    public void comparableMatcherDoesNotMatchStringArgument() {
        boolean result = applyMatch(new LessOrEqual<Integer>(FIVE), HELLO);
        assertThat(result).isFalse();
    }

    // --- Overloaded matches(...) methods are ignored --------------------------------------------

    /**
     * Only the single-argument matches(T) method should be considered; overloads must be ignored.
     */
    static class OverloadedMatchesMatcher implements ArgumentMatcher<Integer> {
        @Override
        public boolean matches(Integer arg) {
            return false;
        }

        @SuppressWarnings("unused")
        public boolean matches(Date arg) {
            throw new UnsupportedOperationException("Should be ignored");
        }

        @SuppressWarnings("unused")
        public boolean matches(Integer arg, Void v) {
            throw new UnsupportedOperationException("Should be ignored");
        }
    }

    @Test
    public void onlyTheArgumentMatchersSingleMatchesMethodIsConsideredEvenIfOverloadsExist() {
        boolean result = applyMatch(new OverloadedMatchesMatcher(), 123);
        assertThat(result).isFalse();
    }

    // --- Generic subclasses and bridge method behavior ------------------------------------------

    static abstract class GenericMatcher<T> implements ArgumentMatcher<T> {}

    static class AlwaysTrueIntegerGenericMatcher extends GenericMatcher<Integer> {
        @Override
        public boolean matches(Integer argument) {
            return true;
        }
    }

    @Test
    public void genericSubclassMatcherIsRecognized() {
        boolean result = applyMatch(new AlwaysTrueIntegerGenericMatcher(), 123);
        assertThat(result).isTrue();
    }

    static class RecordingIntegerGenericMatcher extends GenericMatcher<Integer> {
        private final AtomicBoolean called;

        RecordingIntegerGenericMatcher(AtomicBoolean called) {
            this.called = called;
        }

        @Override
        public boolean matches(Integer argument) {
            called.set(true);
            return true;
        }
    }

    @Test
    public void genericSubclassMatcherIsInvokedWhenArgumentTypeMatches() {
        AtomicBoolean called = new AtomicBoolean(false);
        applyMatch(new RecordingIntegerGenericMatcher(called), 123);
        assertThat(called.get()).isTrue();
    }

    @Test
    public void genericSubclassMatcherIsNotInvokedWhenArgumentTypeIsIncompatible() {
        AtomicBoolean called = new AtomicBoolean(false);
        applyMatch(new RecordingIntegerGenericMatcher(called), "");
        assertThat(called.get()).isFalse();
    }

    /**
     * If no bridge method is generated (matches(T) where T is erased), any argument type should be passed through.
     */
    static class NonBridgeGenericMatcher<T> implements ArgumentMatcher<T> {
        private final AtomicBoolean called;

        NonBridgeGenericMatcher(AtomicBoolean called) {
            this.called = called;
        }

        @Override
        public boolean matches(T argument) {
            called.set(true);
            return true;
        }
    }

    @Test
    public void nonBridgeGenericMatcherAcceptsEveryArgumentType() {
        AtomicBoolean called = new AtomicBoolean(false);
        applyMatch(new NonBridgeGenericMatcher<Integer>(called), 123);
        assertThat(called.get()).isTrue();

        called.set(false);
        applyMatch(new NonBridgeGenericMatcher<Integer>(called), HELLO);
        assertThat(called.get()).isTrue();
    }
}