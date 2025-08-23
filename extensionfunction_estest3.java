package org.apache.commons.jxpath.ri.compiler;

import org.apache.commons.jxpath.ri.QName;
import org.junit.Test;

/**
 * Contains improved tests for the {@link ExtensionFunction} class, focusing on understandability.
 * This class demonstrates how to refactor an auto-generated test for clarity and maintainability.
 */
// The original test class name `ExtensionFunction_ESTestTest3` and its scaffolding are kept
// to maintain the context of the refactoring task. In a real project, this would likely be
// part of a single, well-named `ExtensionFunctionTest` class.
public class ExtensionFunction_ESTestTest3 extends ExtensionFunction_ESTest_scaffolding {

    /**
     * Verifies that the {@code toString()} method throws a {@code NullPointerException}
     * when the {@code ExtensionFunction} is constructed with a null function name.
     * <p>
     * The {@code toString()} method is expected to access the function name to build its
     * string representation. Providing a null name should therefore result in an NPE.
     */
    @Test(expected = NullPointerException.class)
    public void toStringShouldThrowNullPointerExceptionWhenFunctionNameIsNull() {
        // Arrange: Create an ExtensionFunction with a null QName for the function name.
        // The arguments are not relevant to this specific failure condition, so we can
        // provide an empty array for simplicity.
        QName nullFunctionName = null;
        Expression[] arguments = new Expression[0];
        ExtensionFunction extensionFunction = new ExtensionFunction(nullFunctionName, arguments);

        // Act & Assert: Calling toString() should trigger the NullPointerException.
        // The @Test(expected) annotation handles the assertion, causing the test
        // to pass if and only if an NPE is thrown.
        extensionFunction.toString();
    }
}