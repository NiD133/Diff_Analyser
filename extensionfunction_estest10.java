package org.apache.commons.jxpath.ri.compiler;

import org.apache.commons.jxpath.ri.QName;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Tests the string representation of the {@link ExtensionFunction} class.
 */
public class ExtensionFunctionToStringTest {

    /**
     * Verifies that the toString() method correctly formats an ExtensionFunction
     * that was created with no arguments. The expected format is the function's
     * qualified name followed by empty parentheses "()".
     */
    @Test
    public void toString_shouldRenderNameAndEmptyParentheses_whenNoArguments() {
        // Arrange: Create an ExtensionFunction with a qualified name and no arguments (null array).
        QName functionQName = new QName("org.apache.commons.jxpath.ri.Parser");
        ExtensionFunction extensionFunction = new ExtensionFunction(functionQName, null);
        String expectedString = "org.apache.commons.jxpath.ri.Parser()";

        // Act: Call the toString() method.
        String actualString = extensionFunction.toString();

        // Assert: Verify that the output matches the expected format.
        assertEquals("The string representation of a function with no arguments is incorrect.",
                expectedString, actualString);
    }
}