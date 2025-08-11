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
 * Tests for TypeSafeMatching functionality.
 * 
 * TypeSafeMatching ensures that ArgumentMatchers are applied safely without
 * throwing ClassCastException or NullPointerException when argument types
 * don't match the matcher's expected type.
 */
public class TypeSafeMatchingTest {

    private static final Object NON_COMPARABLE_OBJECT = new Object();

    @Rule 
    public MockitoRule mockitoRule = MockitoJUnit.rule();

    @Mock 
    public IMethods mock;

    // ========== Null Argument Handling Tests ==========

    /**
     * Verifies that comparing null arguments doesn't throw NullPointerException.
     * 
     * @see <a href="https://github.com/mockito/mockito/issues/457">Bug 457</a>
     */
    @Test
    public void shouldHandleNullArgumentWithoutException() {
        // Given: A LessOrEqual matcher expecting an Integer
        LessOrEqual<Integer> integerMatcher = new LessOrEqual<>(5);
        
        // When: Matching against null
        boolean matchResult = matchesTypeSafe().apply(integerMatcher, null);
        
        // Then: Should return false without throwing exception
        assertThat(matchResult).isFalse();
    }

    /**
     * Verifies that Null matcher correctly handles null arguments.
     */
    @Test
    public void shouldMatchNullWithNullMatcher() {
        // Given: A Null matcher
        // When: Matching against null
        boolean matchResult = matchesTypeSafe().apply(Null.NULL, null);
        
        // Then: Should return true
        assertThat(matchResult).isTrue();
    }

    /**
     * Verifies that matcher with null expected value handles null argument safely.
     */
    @Test
    public void shouldHandleNullMatcherValueAgainstNullArgument() {
        // Given: A LessOrEqual matcher with null expected value
        LessOrEqual<Integer> nullValueMatcher = new LessOrEqual<>(null);
        
        // When: Matching against null
        boolean matchResult = matchesTypeSafe().apply(nullValueMatcher, null);
        
        // Then: Should return false without throwing exception
        assertThat(matchResult).isFalse();
    }

    // ========== Type Mismatch Handling Tests ==========

    /**
     * Verifies that type-incompatible arguments don't cause ClassCastException.
     */
    @Test
    public void shouldHandleNonComparableObjectWithoutException() {
        // Given: A LessOrEqual matcher expecting Integer
        LessOrEqual<Integer> integerMatcher = new LessOrEqual<>(5);
        
        // When: Matching against non-comparable object
        boolean matchResult = matchesTypeSafe().apply(integerMatcher, NON_COMPARABLE_OBJECT);
        
        // Then: Should return false without throwing exception
        assertThat(matchResult).isFalse();
    }

    /**
     * Verifies that string matcher against integer argument is handled safely.
     */
    @Test
    public void shouldHandleStringMatcherAgainstIntegerArgument() {
        // Given: A StartsWith matcher expecting String
        StartsWith stringMatcher = new StartsWith("Hello");
        
        // When: Matching against integer
        boolean matchResult = matchesTypeSafe().apply(stringMatcher, 123);
        
        // Then: Should return false without throwing exception
        assertThat(matchResult).isFalse();
    }

    /**
     * Verifies that integer matcher against string argument is handled safely.
     */
    @Test
    public void shouldHandleIntegerMatcherAgainstStringArgument() {
        // Given: A LessOrEqual matcher expecting Integer
        LessOrEqual<Integer> integerMatcher = new LessOrEqual<>(5);
        
        // When: Matching against string
        boolean matchResult = matchesTypeSafe().apply(integerMatcher, "Hello");
        
        // Then: Should return false without throwing exception
        assertThat(matchResult).isFalse();
    }

    // ========== Method Overloading Tests ==========

    /**
     * Verifies that overloaded matches() methods are ignored and only the 
     * correct ArgumentMatcher.matches(T) method is called.
     */
    @Test
    public void shouldIgnoreOverloadedMatchesMethods() {
        // Given: A matcher with overloaded matches methods
        ArgumentMatcher<Integer> matcherWithOverloads = new ArgumentMatcher<Integer>() {
            @Override
            public boolean matches(Integer arg) {
                return false; // This should be called
            }

            @SuppressWarnings("unused")
            public boolean matches(Date arg) {
                throw new UnsupportedOperationException("This overload should not be called");
            }

            @SuppressWarnings("unused")
            public boolean matches(Integer arg, Void v) {
                throw new UnsupportedOperationException("This overload should not be called");
            }
        };
        
        // When: Matching with integer argument
        boolean matchResult = matchesTypeSafe().apply(matcherWithOverloads, 123);
        
        // Then: Should call correct method and return false
        assertThat(matchResult).isFalse();
    }

    // ========== Generic Type Handling Tests ==========

    /**
     * Verifies that matchers extending generic classes work correctly with matching types.
     */
    @Test
    public void shouldMatchWithGenericSubclassAndCompatibleType() {
        // Given: A matcher extending generic class
        abstract class GenericMatcher<T> implements ArgumentMatcher<T> {}
        
        ArgumentMatcher<Integer> integerMatcher = new GenericMatcher<Integer>() {
            @Override
            public boolean matches(Integer argument) {
                return true;
            }
        };
        
        // When: Matching with compatible type
        boolean matchResult = matchesTypeSafe().apply(integerMatcher, 123);
        
        // Then: Should return true
        assertThat(matchResult).isTrue();
    }

    /**
     * Verifies that matchers extending generic classes handle incompatible types correctly.
     * The matcher method should not be called for incompatible types.
     */
    @Test
    public void shouldNotCallMatcherWithIncompatibleTypeInGenericSubclass() {
        // Given: A matcher extending generic class with call tracking
        final AtomicBoolean matcherWasCalled = new AtomicBoolean(false);
        
        abstract class GenericMatcher<T> implements ArgumentMatcher<T> {}
        
        ArgumentMatcher<Integer> integerMatcher = new GenericMatcher<Integer>() {
            @Override
            public boolean matches(Integer argument) {
                matcherWasCalled.set(true);
                return true;
            }
        };
        
        // When: Matching with compatible type (Integer)
        matcherWasCalled.set(false);
        matchesTypeSafe().apply(integerMatcher, 123);
        
        // Then: Matcher should be called
        assertThat(matcherWasCalled.get()).isTrue();
        
        // When: Matching with incompatible type (String)
        matcherWasCalled.set(false);
        matchesTypeSafe().apply(integerMatcher, "incompatible string");
        
        // Then: Matcher should not be called
        assertThat(matcherWasCalled.get()).isFalse();
    }

    /**
     * Verifies behavior when no bridge method is generated for generic matchers.
     * In this case, all argument types are passed through to the matcher.
     */
    @Test
    public void shouldPassAllArgumentTypesWhenNoBridgeMethodExists() {
        // Given: A generic matcher without bridge method generation
        final AtomicBoolean matcherWasCalled = new AtomicBoolean(false);
        
        ArgumentMatcher<Integer> genericMatcher = new ArgumentMatcher<Integer>() {
            @Override
            public boolean matches(Integer argument) {
                matcherWasCalled.set(true);
                return true;
            }
        };
        
        // When: Matching with declared generic type (Integer)
        matcherWasCalled.set(false);
        matchesTypeSafe().apply(genericMatcher, 123);
        
        // Then: Matcher should be called
        assertThat(matcherWasCalled.get()).isTrue();
        
        // When: Matching with different type (String)
        matcherWasCalled.set(false);
        matchesTypeSafe().apply(genericMatcher, "different type");
        
        // Then: Matcher should still be called (no bridge method to prevent it)
        assertThat(matcherWasCalled.get()).isTrue();
    }
}