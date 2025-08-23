package org.apache.ibatis.builder;

import org.junit.Test;

/**
 * Tests for the {@link ParameterExpression} class.
 */
public class ParameterExpressionTest {

    /**
     * Verifies that the constructor throws a NullPointerException
     * when the input expression string is null. This is expected behavior
     * as the parser requires a non-null string to operate on.
     */
    @Test(expected = NullPointerException.class)
    public void constructorShouldThrowNullPointerExceptionForNullExpression() {
        // Attempt to create a ParameterExpression with a null string.
        // The @Test(expected = ...) annotation asserts that this action
        // must throw a NullPointerException for the test to pass.
        new ParameterExpression(null);
    }
}