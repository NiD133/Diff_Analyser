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

    @Rule public MockitoRule mockitoRule = MockitoJUnit.rule();
    @Mock public IMethods mock;

    @Test
    public void lessOrEqualMatcher_withNullArgument_returnsFalse() {
        // Given
        ArgumentMatcher<Integer> matcher = new LessOrEqual<>(5);
        
        // When
        boolean match = matchesTypeSafe().apply(matcher, null);
        
        // Then
        assertThat(match).isFalse();
    }

    @Test
    public void lessOrEqualMatcher_withNonComparableArgument_returnsFalse() {
        // Given
        ArgumentMatcher<Integer> matcher = new LessOrEqual<>(5);
        
        // When
        boolean match = matchesTypeSafe().apply(matcher, NON_COMPARABLE_OBJECT);
        
        // Then
        assertThat(match).isFalse();
    }

    @Test
    public void lessOrEqualMatcher_withNullWantedValueAndNullArgument_returnsFalse() {
        // Given
        ArgumentMatcher<Integer> matcher = new LessOrEqual<>(null);
        
        // When
        boolean match = matchesTypeSafe().apply(matcher, null);
        
        // Then
        assertThat(match).isFalse();
    }

    @Test
    public void nullMatcher_withNullArgument_returnsTrue() {
        // Given
        ArgumentMatcher<Object> matcher = Null.NULL;
        
        // When
        boolean match = matchesTypeSafe().apply(matcher, null);
        
        // Then
        assertThat(match).isTrue();
    }

    @Test
    public void startsWithMatcher_withNonStringArgument_returnsFalse() {
        // Given
        ArgumentMatcher<String> matcher = new StartsWith("Hello");
        
        // When
        boolean match = matchesTypeSafe().apply(matcher, 123);
        
        // Then
        assertThat(match).isFalse();
    }

    @Test
    public void lessOrEqualMatcher_withNonIntegerArgument_returnsFalse() {
        // Given
        ArgumentMatcher<Integer> matcher = new LessOrEqual<>(5);
        
        // When
        boolean match = matchesTypeSafe().apply(matcher, "Hello");
        
        // Then
        assertThat(match).isFalse();
    }

    @Test
    public void matcherWithOverloadedMethods_onlyUsesArgumentMatcherSignature() {
        // Given
        ArgumentMatcher<Integer> matcher = new OverloadedMatcher();
        Integer argument = 123;
        
        // When
        boolean match = matchesTypeSafe().apply(matcher, argument);
        
        // Then
        assertThat(match).isFalse();
    }

    private static class OverloadedMatcher implements ArgumentMatcher<Integer> {
        @Override
        public boolean matches(Integer arg) {
            return false;
        }

        // These should be ignored by TypeSafeMatching
        public boolean matches(Date arg) {
            throw new UnsupportedOperationException();
        }

        public boolean matches(Integer arg, Void v) {
            throw new UnsupportedOperationException();
        }
    }

    @Test
    public void matcherExtendingGenericClass_withMatchingArgument_returnsTrue() {
        // Given
        ArgumentMatcher<Integer> matcher = new IntegerMatcher();
        Integer argument = 123;
        
        // When
        boolean match = matchesTypeSafe().apply(matcher, argument);
        
        // Then
        assertThat(match).isTrue();
    }

    private abstract static class GenericMatcher<T> implements ArgumentMatcher<T> {}
    
    private static class IntegerMatcher extends GenericMatcher<Integer> {
        @Override
        public boolean matches(Integer argument) {
            return true;
        }
    }

    @Test
    public void matcherExtendingGenericClass_withMismatchedArgument_doesNotCallMatchesMethod() {
        // Given
        CallTrackerMatcher matcher = new CallTrackerMatcher();
        String mismatchedArgument = "Hello";
        
        // When
        matchesTypeSafe().apply(matcher, mismatchedArgument);
        
        // Then
        assertThat(matcher.wasCalled()).isFalse();
    }

    @Test
    public void matcherExtendingGenericClass_withMatchingArgument_callsMatchesMethod() {
        // Given
        CallTrackerMatcher matcher = new CallTrackerMatcher();
        Integer matchingArgument = 123;
        
        // When
        matchesTypeSafe().apply(matcher, matchingArgument);
        
        // Then
        assertThat(matcher.wasCalled()).isTrue();
    }

    private static class CallTrackerMatcher extends GenericMatcher<Integer> {
        private final AtomicBoolean called = new AtomicBoolean(false);
        
        @Override
        public boolean matches(Integer argument) {
            called.set(true);
            return true;
        }
        
        boolean wasCalled() {
            return called.get();
        }
    }

    @Test
    public void genericMatcherWithoutBridgeMethod_callsMatchesMethodForAnyArgumentType() {
        // Given
        GenericMatcherWithoutBridge matcher = new GenericMatcherWithoutBridge();
        
        // When & Then: Integer argument
        boolean calledForInteger = callAndCheck(matcher, 123);
        assertThat(calledForInteger).isTrue();
        
        // When & Then: String argument
        boolean calledForString = callAndCheck(matcher, "Hello");
        assertThat(calledForString).isTrue();
    }

    private boolean callAndCheck(GenericMatcherWithoutBridge matcher, Object argument) {
        AtomicBoolean called = new AtomicBoolean(false);
        matcher.setCalledFlag(called);
        matchesTypeSafe().apply(matcher, argument);
        return called.get();
    }

    private static class GenericMatcherWithoutBridge<T> implements ArgumentMatcher<T> {
        private AtomicBoolean calledFlag;

        void setCalledFlag(AtomicBoolean calledFlag) {
            this.calledFlag = calledFlag;
        }

        @Override
        public boolean matches(T argument) {
            calledFlag.set(true);
            return true;
        }
    }
}