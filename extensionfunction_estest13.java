package org.apache.commons.jxpath.ri.compiler;

import org.apache.commons.jxpath.ri.QName;
import org.junit.Test;

import static org.junit.Assert.assertSame;

/**
 * Unit tests for the {@link ExtensionFunction} class.
 */
public class ExtensionFunctionTest {

    /**
     * Verifies that getFunctionName() returns the same QName instance
     * that was provided to the constructor.
     */
    @Test
    public void getFunctionNameShouldReturnTheNameSetInConstructor() {
        // Arrange: Create a QName for the function and instantiate ExtensionFunction.
        QName expectedFunctionName = new QName("test:my-function");
        Expression[] noArguments = null; // The function has no arguments for this test.
        ExtensionFunction extensionFunction = new ExtensionFunction(expectedFunctionName, noArguments);

        // Act: Call the method under test.
        QName actualFunctionName = extensionFunction.getFunctionName();

        // Assert: Verify that the returned QName is the exact same instance.
        assertSame("The getter should return the identical QName instance passed to the constructor.",
                expectedFunctionName, actualFunctionName);
    }
}