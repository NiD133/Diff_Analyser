package org.mockito.internal.invocation;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.mockito.ArgumentMatcher;
import org.mockito.internal.matchers.CapturingMatcher;
import org.mockito.internal.matchers.CompareEqual;
import org.mockito.internal.matchers.GreaterOrEqual;
import org.mockito.internal.matchers.NotNull;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

/**
 * Tests for {@link TypeSafeMatching}.
 *
 * This class verifies that the type-safe matching action correctly identifies
 * whether an argument is type-compatible with an {@link ArgumentMatcher} before
 * applying it.
 */
public class TypeSafeMatchingTest {

    // A modern JUnit 4 approach for asserting exceptions.
    @Rule
    public ExpectedException thrown = ExpectedException.none();

    private final ArgumentMatcherAction typeSafeMatching = TypeSafeMatching.matchesTypeSafe();

    @Test
    public void shouldReturnFalse_whenArgumentIsOfIncompatibleType() {
        // Arrange
        ArgumentMatcher<Integer> integerMatcher = new GreaterOrEqual<>(10);
        Object incompatibleArgument = "a string"; // A String is not an Integer

        // Act
        boolean result = typeSafeMatching.apply(integerMatcher, incompatibleArgument);

        // Assert
        assertFalse("Should return false for a type-incompatible argument", result);
    }

    @Test
    public void shouldReturnTrue_whenArgumentIsOfCompatibleType() {
        // Arrange
        ArgumentMatcher<Object> anyObjectMatcher = new CapturingMatcher<>(Object.class);
        Object compatibleArgument = new Object();

        // Act
        boolean result = typeSafeMatching.apply(anyObjectMatcher, compatibleArgument);

        // Assert
        assertTrue("Should return true for a type-compatible argument", result);
    }

    @Test
    public void shouldLetMatcherDecide_whenArgumentIsNull() {
        // Arrange
        // A null argument is considered "compatible" by TypeSafeMatching,
        // allowing the matcher itself to handle the null check.
        ArgumentMatcher<Integer> notNullMatcher = new NotNull<>(Integer.class);

        // Act
        boolean result = typeSafeMatching.apply(notNullMatcher, null);

        // Assert
        assertFalse("Should delegate to the matcher, which returns false for null", result);
    }

    @Test
    public void shouldThrowNullPointerException_whenMatcherIsNull() {
        // Arrange
        Object anyArgument = new Object();

        // Assert
        thrown.expect(NullPointerException.class);

        // Act
        typeSafeMatching.apply(null, anyArgument);
    }

    @Test
    public void shouldNotPreventClassCastException_forMatcherWithBroadTypeAndInternalCasting() {
        // This test demonstrates a limitation: TypeSafeMatching cannot prevent a ClassCastException
        // if a matcher declares a broad argument type (e.g., Object) but then performs an
        // unsafe cast internally.
        
        // Arrange
        // CompareEqual's matches() method takes an Object, so TypeSafeMatching considers it safe.
        // However, its implementation internally casts the argument to Comparable.
        ArgumentMatcher<Integer> matcherWithInternalCast = new CompareEqual<>(10);
        Object incompatibleArgument = "a string";

        // Assert
        thrown.expect(ClassCastException.class);

        // Act
        typeSafeMatching.apply(matcherWithInternalCast, incompatibleArgument);
    }
}