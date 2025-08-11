package org.mockito.internal.invocation;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatcher;
import org.mockito.internal.invocation.ArgumentMatcherAction;
import org.mockito.internal.matchers.CapturingMatcher;
import org.mockito.internal.matchers.CompareEqual;
import org.mockito.internal.matchers.GreaterOrEqual;
import org.mockito.internal.matchers.Not;
import org.mockito.internal.matchers.NotNull;

@RunWith(EvoRunner.class)
@EvoRunnerParameters(
    mockJVMNonDeterminism = true,
    useVFS = true,
    useVNET = true,
    resetStaticState = true,
    separateClassLoader = true
)
public class TypeSafeMatching_ESTest {

    @Test(timeout = 4000)
    public void apply_WithNullMatcher_ThrowsNullPointerException() throws Throwable {
        // Test that applying a null matcher throws expected exception
        ArgumentMatcherAction action = TypeSafeMatching.matchesTypeSafe();
        
        try {
            action.apply(null, "any-argument");
            fail("Expected NullPointerException");
        } catch (NullPointerException e) {
            // Verify exception originates from TypeSafeMatching
            verifyException("org.mockito.internal.invocation.TypeSafeMatching", e);
        }
    }

    @Test(timeout = 4000)
    public void apply_WithIncompatibleMatcherType_ThrowsClassCastException() throws Throwable {
        // Test matcher type incompatibility handling
        ArgumentMatcherAction action = TypeSafeMatching.matchesTypeSafe();
        ArgumentMatcher<Integer> baseMatcher = new CompareEqual<>(null);
        Not incompatibleMatcher = new Not(baseMatcher);
        
        try {
            action.apply(incompatibleMatcher, incompatibleMatcher);
            fail("Expected ClassCastException");
        } catch (ClassCastException e) {
            // Verify exception contains relevant type information
            assertTrue(e.getMessage().contains("org.mockito.internal.matchers.Not"));
            assertTrue(e.getMessage().contains("java.lang.Comparable"));
        }
    }

    @Test(timeout = 4000)
    public void apply_WithCapturingMatcherAndMatchingArgument_ReturnsTrue() throws Throwable {
        // Test successful matching with compatible types
        ArgumentMatcherAction action = TypeSafeMatching.matchesTypeSafe();
        CapturingMatcher<Object> matcher = new CapturingMatcher<>(Object.class);
        Object argument = new Object();
        
        boolean result = action.apply(matcher, argument);
        
        assertTrue("Should match when types are compatible", result);
    }

    @Test(timeout = 4000)
    public void apply_WithNotNullMatcherAndNullArgument_ReturnsFalse() throws Throwable {
        // Test null argument handling with NotNull matcher
        ArgumentMatcherAction action = TypeSafeMatching.matchesTypeSafe();
        NotNull<Integer> notNullMatcher = new NotNull<>(Integer.class);
        
        boolean result = action.apply(notNullMatcher, null);
        
        assertFalse("Should not match null argument", result);
    }

    @Test(timeout = 4000)
    public void apply_WithTypeIncompatibleArgument_ReturnsFalse() throws Throwable {
        // Test type safety with incompatible argument type
        ArgumentMatcherAction action = TypeSafeMatching.matchesTypeSafe();
        GreaterOrEqual<Integer> intMatcher = new GreaterOrEqual<>(100);
        Object incompatibleArgument = new Object();
        
        boolean result = action.apply(intMatcher, incompatibleArgument);
        
        assertFalse("Should not match when types are incompatible", result);
    }
}