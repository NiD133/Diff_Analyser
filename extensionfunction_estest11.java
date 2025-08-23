package org.apache.commons.jxpath.ri.compiler;

import org.apache.commons.jxpath.ri.QName;
import org.junit.Test;

/**
 * Contains tests for the {@link ExtensionFunction} class, focusing on its behavior
 * when constructed with invalid arguments.
 */
public class ExtensionFunctionTest {

    /**
     * Verifies that calling computeValue() on an ExtensionFunction initialized
     * with a null function name (QName) results in a NullPointerException.
     *
     * The method is expected to access the function name internally before
     * performing any other operations, leading to the exception.
     */
    @Test(expected = NullPointerException.class)
    public void computeValueShouldThrowNullPointerExceptionWhenFunctionNameIsNull() {
        // Arrange: Create an ExtensionFunction with a null function name.
        // The arguments are not relevant for this specific failure but are required
        // by the constructor.
        Expression[] dummyArguments = new Expression[]{ new Constant("arg1") };
        QName nullFunctionName = null;
        ExtensionFunction extensionFunction = new ExtensionFunction(nullFunctionName, dummyArguments);

        // Act: Attempt to compute the value. This should immediately fail because
        // the function name is null. The EvalContext can also be null as the
        // exception is thrown before it is used.
        extensionFunction.computeValue(null);

        // Assert: The @Test(expected) annotation handles the assertion that a
        // NullPointerException was thrown.
    }
}