package org.apache.commons.jxpath.ri.compiler;

import org.apache.commons.jxpath.ri.QName;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Contains tests for the {@link ExtensionFunction#toString()} method.
 */
public class ExtensionFunctionToStringTest {

    /**
     * Tests that the toString() method correctly formats its output
     * when the function name and its arguments are null.
     *
     * The expected format is "functionName(arg1, arg2, ...)", where null
     * values are rendered as the string "null".
     */
    @Test
    public void testToStringWithNullFunctionNameAndArguments() {
        // Arrange: Create an ExtensionFunction with a null function name and an
        // array of two null expressions as arguments.
        QName nullFunctionName = null;
        Expression[] nullArguments = new Expression[2]; // Array elements are null by default
        ExtensionFunction extensionFunction = new ExtensionFunction(nullFunctionName, nullArguments);

        // Act: Call the toString() method.
        String result = extensionFunction.toString();

        // Assert: Verify that the output string is formatted as expected.
        assertEquals("The toString() output for null function and args is incorrect",
                "null(null, null)", result);
    }
}