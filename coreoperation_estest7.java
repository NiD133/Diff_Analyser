package org.apache.commons.jxpath.ri.compiler;

import org.apache.commons.jxpath.JXPathException;
import org.junit.Test;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

/**
 * Contains tests for the computation logic within CoreOperation subclasses,
 * particularly focusing on exception handling for invalid expressions.
 */
public class CoreOperationTest {

    /**
     * Tests that computing a complex expression tree throws a JXPathException
     * when a nested function is called with an incorrect number of arguments.
     *
     * This test verifies that the JXPath engine correctly identifies an invalid
     * function call (string-length() with 9 arguments instead of 0 or 1)
     * deep within a nested expression and reports it with a descriptive error message.
     */
    @Test
    public void computeShouldThrowExceptionForNestedFunctionWithIncorrectArgumentCount() {
        // Arrange: Build a complex expression tree containing an invalid function call.
        // The expression structure is effectively:
        // (string-length(...) * string-length(...)) + null + null + ...
        // where the string-length function itself is invalid.

        // 1. The 'string-length' function expects 0 or 1 arguments, but we provide 9.
        Expression[] invalidArgsForStringLength = new Expression[9];

        // 2. Create the 'string-length' function call. We use the Function interface
        // constant instead of the magic number 15 for clarity.
        CoreFunction stringLengthFunction = new CoreFunction(
            Function.CORE_FUNCTION_STRING_LENGTH,
            invalidArgsForStringLength
        );

        // 3. Nest the invalid function inside a multiplication operation.
        CoreOperationMultiply multiplyOperation = new CoreOperationMultiply(
            stringLengthFunction,
            stringLengthFunction
        );

        // 4. Create a self-referential expression to test the string representation
        // in the exception message. The function's first argument is the operation
        // that contains the function itself.
        invalidArgsForStringLength[0] = multiplyOperation;

        // 5. Create a top-level 'add' operation. Its computation will trigger the
        // evaluation of its arguments, including the invalid nested function.
        CoreOperationAdd rootOperation = new CoreOperationAdd(invalidArgsForStringLength);

        // Act & Assert
        try {
            // The compute call is expected to fail when it evaluates the stringLengthFunction.
            // The EvalContext can be null as the error occurs before it's used.
            rootOperation.compute(null);
            fail("Expected a JXPathException because string-length() was called with too many arguments.");
        } catch (JXPathException e) {
            // Verify that the exception message clearly indicates the problem.
            String errorMessage = e.getMessage();
            assertTrue(
                "Error message should identify the incorrect argument count.",
                errorMessage.startsWith("Incorrect number of arguments:")
            );
            assertTrue(
                "Error message should name the function 'string-length'.",
                errorMessage.contains("string-length")
            );
            assertTrue(
                "Error message should include the stringified nested expression.",
                errorMessage.contains(" * ") // Verifies the multiplication was part of the string representation.
            );
        }
    }
}