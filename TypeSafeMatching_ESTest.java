package org.mockito.internal.invocation;

import org.junit.Test;
import static org.junit.Assert.*;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatcher;
import org.mockito.internal.invocation.ArgumentMatcherAction;
import org.mockito.internal.invocation.TypeSafeMatching;
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
public class TypeSafeMatching_ESTest extends TypeSafeMatching_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void testNullArgumentMatcherThrowsNullPointerException() throws Throwable {
        ArgumentMatcherAction action = TypeSafeMatching.matchesTypeSafe();
        try {
            action.apply(null, null);
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            verifyException("org.mockito.internal.invocation.TypeSafeMatching", e);
        }
    }

    @Test(timeout = 4000)
    public void testIncompatibleMatcherThrowsClassCastException() throws Throwable {
        ArgumentMatcherAction action = TypeSafeMatching.matchesTypeSafe();
        CompareEqual<Integer> compareEqual = new CompareEqual<>(null);
        Not notMatcher = new Not(compareEqual);
        try {
            action.apply(notMatcher, notMatcher);
            fail("Expecting exception: ClassCastException");
        } catch (ClassCastException e) {
            verifyException("org.mockito.internal.matchers.CompareTo", e);
        }
    }

    @Test(timeout = 4000)
    public void testCapturingMatcherReturnsTrueForAnyObject() throws Throwable {
        ArgumentMatcherAction action = TypeSafeMatching.matchesTypeSafe();
        CapturingMatcher<Object> capturingMatcher = new CapturingMatcher<>(Object.class);
        Object testObject = new Object();
        boolean result = action.apply(capturingMatcher, testObject);
        assertTrue(result);
    }

    @Test(timeout = 4000)
    public void testNotNullMatcherReturnsFalseForNullArgument() throws Throwable {
        ArgumentMatcherAction action = TypeSafeMatching.matchesTypeSafe();
        NotNull<Integer> notNullMatcher = new NotNull<>(Integer.class);
        boolean result = action.apply(notNullMatcher, null);
        assertFalse(result);
    }

    @Test(timeout = 4000)
    public void testGreaterOrEqualMatcherReturnsFalseForNonComparableObject() throws Throwable {
        ArgumentMatcherAction action = TypeSafeMatching.matchesTypeSafe();
        Integer referenceValue = 781;
        GreaterOrEqual<Integer> greaterOrEqualMatcher = new GreaterOrEqual<>(referenceValue);
        Object nonComparableObject = new Object();
        boolean result = action.apply(greaterOrEqualMatcher, nonComparableObject);
        assertFalse(result);
    }
}