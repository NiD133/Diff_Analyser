package org.mockito.internal.invocation;

import org.junit.Test;
import static org.junit.Assert.*;
import org.mockito.ArgumentMatcher;
import org.mockito.internal.invocation.ArgumentMatcherAction;
import org.mockito.internal.invocation.TypeSafeMatching;
import org.mockito.internal.matchers.CapturingMatcher;
import org.mockito.internal.matchers.CompareEqual;
import org.mockito.internal.matchers.GreaterOrEqual;
import org.mockito.internal.matchers.Not;
import org.mockito.internal.matchers.NotNull;

/**
 * Tests for TypeSafeMatching class which provides type-safe argument matching functionality.
 * This class ensures that argument matchers are applied safely without ClassCastExceptions.
 */
public class TypeSafeMatchingTest {

    private final ArgumentMatcherAction typeSafeMatching = TypeSafeMatching.matchesTypeSafe();

    @Test(expected = NullPointerException.class)
    public void shouldThrowNullPointerExceptionWhenMatcherIsNull() {
        // Given: null matcher and null argument
        ArgumentMatcher nullMatcher = null;
        Object nullArgument = null;
        
        // When: applying type-safe matching with null matcher
        // Then: should throw NullPointerException
        typeSafeMatching.apply(nullMatcher, nullArgument);
    }

    @Test(expected = ClassCastException.class)
    public void shouldThrowClassCastExceptionWhenMatcherAndArgumentTypesAreIncompatible() {
        // Given: a Not matcher wrapping a CompareEqual matcher for Integer
        CompareEqual<Integer> integerMatcher = new CompareEqual<>(null);
        Not notMatcher = new Not(integerMatcher);
        
        // When: applying the Not matcher to itself (incompatible types)
        // Then: should throw ClassCastException because Not cannot be cast to Comparable
        typeSafeMatching.apply(notMatcher, notMatcher);
    }

    @Test
    public void shouldReturnTrueWhenCapturingMatcherMatchesCompatibleObject() {
        // Given: a capturing matcher for Object type and a compatible object
        CapturingMatcher<Object> objectMatcher = new CapturingMatcher<>(Object.class);
        Object testObject = new Object();
        
        // When: applying the matcher to the compatible object
        boolean result = typeSafeMatching.apply(objectMatcher, testObject);
        
        // Then: should return true as the types are compatible
        assertTrue("CapturingMatcher should match compatible Object", result);
    }

    @Test
    public void shouldReturnFalseWhenNotNullMatcherReceivesNullArgument() {
        // Given: a NotNull matcher for Integer type and a null argument
        NotNull<Integer> notNullMatcher = new NotNull<>(Integer.class);
        Object nullArgument = null;
        
        // When: applying the NotNull matcher to null
        boolean result = typeSafeMatching.apply(notNullMatcher, nullArgument);
        
        // Then: should return false as null doesn't satisfy NotNull condition
        assertFalse("NotNull matcher should return false for null argument", result);
    }

    @Test
    public void shouldReturnFalseWhenIntegerMatcherReceivesIncompatibleObjectType() {
        // Given: a GreaterOrEqual matcher for Integer and an incompatible Object
        Integer threshold = 781;
        GreaterOrEqual<Integer> integerMatcher = new GreaterOrEqual<>(threshold);
        Object incompatibleObject = new Object();
        
        // When: applying the Integer matcher to a plain Object
        boolean result = typeSafeMatching.apply(integerMatcher, incompatibleObject);
        
        // Then: should return false due to type incompatibility
        assertFalse("Integer matcher should return false for incompatible Object type", result);
    }
}