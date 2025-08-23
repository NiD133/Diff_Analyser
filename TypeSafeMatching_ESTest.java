package org.mockito.internal.invocation;

import org.junit.Test;
import org.mockito.ArgumentMatcher;
import org.mockito.internal.matchers.CapturingMatcher;
import org.mockito.internal.matchers.CompareEqual;
import org.mockito.internal.matchers.GreaterOrEqual;
import org.mockito.internal.matchers.Not;
import org.mockito.internal.matchers.NotNull;

import static org.junit.Assert.*;

public class TypeSafeMatchingTest {

    @Test
    public void apply_throwsNPE_whenMatcherIsNull() {
        ArgumentMatcherAction action = TypeSafeMatching.matchesTypeSafe();

        // Null matcher is not allowed
        assertThrows(NullPointerException.class, () -> action.apply(null, null));
    }

    @Test
    public void apply_throwsClassCastException_whenArgumentIsNotComparableForCompareEqual() {
        ArgumentMatcherAction action = TypeSafeMatching.matchesTypeSafe();

        // CompareEqual expects a Comparable; by wrapping with Not and passing the matcher itself
        // as the argument, we end up providing a non-Comparable value to a Comparable-based matcher.
        CompareEqual<Integer> eq = new CompareEqual<>(null);
        Not not = new Not(eq);

        assertThrows(ClassCastException.class, () -> action.apply(not, not));
    }

    @Test
    public void apply_returnsTrue_whenTypesAreCompatible_andMatcherAccepts() {
        ArgumentMatcherAction action = TypeSafeMatching.matchesTypeSafe();

        // CapturingMatcher<Object> accepts any Object, so this should be a safe match.
        CapturingMatcher<Object> capturing = new CapturingMatcher<>(Object.class);
        Object value = new Object();

        assertTrue(action.apply(capturing, value));
    }

    @Test
    public void apply_returnsFalse_whenArgumentIsNull_andMatcherRequiresNonNull() {
        ArgumentMatcherAction action = TypeSafeMatching.matchesTypeSafe();

        // NotNull<Integer> does not accept null.
        NotNull<Integer> nonNullInt = new NotNull<>(Integer.class);

        assertFalse(action.apply(nonNullInt, null));
    }

    @Test
    public void apply_returnsFalse_insteadOfThrowing_whenArgumentTypeIsIncompatible() {
        ArgumentMatcherAction action = TypeSafeMatching.matchesTypeSafe();

        // GreaterOrEqual<Integer> cannot safely accept an arbitrary Object instance.
        // TypeSafeMatching should deem it incompatible and return false (no ClassCastException).
        GreaterOrEqual<Integer> ge = new GreaterOrEqual<>(781);
        Object notAnInteger = new Object();

        assertFalse(action.apply(ge, notAnInteger));
    }
}