package org.mockito.internal.invocation;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.internal.invocation.TypeSafeMatching.matchesTypeSafe;

import org.junit.Test;
import org.mockito.ArgumentMatcher;

/**
 * Tests for {@link TypeSafeMatching}.
 * This class focuses on scenarios involving generic types.
 */
public class TypeSafeMatchingTest {

    // A helper abstract class that provides a generic base for argument matchers.
    // This mimics a common user pattern of creating custom, reusable matchers.
    private abstract static class BaseGenericMatcher<T> implements ArgumentMatcher<T> {
        // The 'matches' method is left for the concrete implementation.
    }

    // A concrete implementation for matching Integers. The generic type is defined
    // by extending the base class, which is the scenario under test.
    private static class IntegerMatcher extends BaseGenericMatcher<Integer> {
        @Override
        public boolean matches(Integer argument) {
            // The return value is not important for this test. We are only verifying
            // that TypeSafeMatching correctly identifies the argument type and calls this method.
            return true;
        }
    }

    /**
     * Verifies that TypeSafeMatching can correctly determine the argument type
     * for an ArgumentMatcher that specifies its generic type through a superclass.
     *
     * <p>The System Under Test must inspect the class hierarchy to find the concrete
     * type {@code Integer} which replaces the generic type {@code T} from the superclass.
     */
    @Test
    public void shouldInferArgumentTypeFromGenericSuperclass() {
        // Arrange
        ArgumentMatcher<Integer> integerMatcher = new IntegerMatcher();
        Integer validArgument = 123;

        // Act
        // The 'apply' method should correctly identify that 'integerMatcher' expects an Integer
        // and that 'validArgument' is a compatible instance, leading to a successful match.
        boolean isMatch = matchesTypeSafe().apply(integerMatcher, validArgument);

        // Assert
        assertThat(isMatch).isTrue();
    }
}