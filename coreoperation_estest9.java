package org.apache.commons.jxpath.ri.compiler;

import org.junit.Test;

/**
 * Tests for {@link CoreOperation} focusing on exception handling with invalid operand types.
 */
public class CoreOperationTest {

    /**
     * Verifies that a {@link CoreOperationMod} (modulo) throws an {@link ArithmeticException}
     * when its operands evaluate to non-numeric values.
     */
    @Test(expected = ArithmeticException.class)
    public void modOperationWithBooleanOperandsThrowsArithmeticException() {
        // Arrange: Create a boolean expression. An 'and' operation with no arguments
        // evaluates to a boolean, which is an invalid type for the 'mod' operator.
        Expression booleanExpression = new CoreOperationAnd(new Expression[0]);

        // Create a 'mod' operation with the boolean expression as both left and right operands.
        // This is equivalent to an expression like 'true mod true'.
        CoreOperation modOperation = new CoreOperationMod(booleanExpression, booleanExpression);

        // Act & Assert: Attempting to compute the result should throw an ArithmeticException
        // because the 'mod' operator cannot be applied to boolean values.
        // The expected exception is declared in the @Test annotation, making a try-catch block unnecessary.
        modOperation.compute(null); // The EvalContext is not relevant for this failure path.
    }
}