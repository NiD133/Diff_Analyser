package org.apache.commons.jxpath.ri.compiler;

import org.apache.commons.jxpath.ri.QName;
import org.junit.Test;

/**
 * Contains tests for the {@link ExtensionFunction} class.
 */
public class ExtensionFunctionTest {

    /**
     * Tests that calling compute() on an ExtensionFunction initialized with a null
     * function name throws a NullPointerException.
     */
    @Test(expected = NullPointerException.class)
    public void computeShouldThrowNullPointerExceptionWhenFunctionNameIsNull() {
        // Arrange: Create an ExtensionFunction with a null function name.
        // The arguments array is required by the constructor but its content is irrelevant for this test.
        Expression[] args = new Expression[1];
        ExtensionFunction extensionFunction = new ExtensionFunction(null, args);

        // Act: Attempt to compute the function's value.
        // This is expected to throw a NullPointerException because the internal
        // functionName field is null and is likely dereferenced within compute().
        // The context can also be null as it's not relevant to this specific failure.
        extensionFunction.compute(null);

        // Assert: The @Test(expected) annotation handles the exception assertion.
    }
}