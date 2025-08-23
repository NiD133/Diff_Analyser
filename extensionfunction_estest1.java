package org.apache.commons.jxpath.ri.compiler;

import org.apache.commons.jxpath.ri.QName;
import org.junit.Test;
import static org.junit.Assert.assertNull;

/**
 * Tests for the {@link ExtensionFunction} class, focusing on its constructor and basic getters.
 */
public class ExtensionFunction_ESTestTest1 {

    /**
     * Verifies that getFunctionName() returns null if the ExtensionFunction
     * was constructed with a null function name.
     */
    @Test(timeout = 4000)
    public void getFunctionNameShouldReturnNullWhenConstructedWithNull() {
        // Arrange: Create an ExtensionFunction with a null QName.
        // The arguments are not relevant for this test.
        QName functionName = null;
        Expression[] dummyArguments = new Expression[3];
        ExtensionFunction extensionFunction = new ExtensionFunction(functionName, dummyArguments);

        // Act: Call the method under test.
        QName retrievedFunctionName = extensionFunction.getFunctionName();

        // Assert: Verify that the returned name is null, as expected.
        assertNull("getFunctionName() should return the null value passed to the constructor.", retrievedFunctionName);
    }
}