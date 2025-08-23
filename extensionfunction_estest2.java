package org.apache.commons.jxpath.ri.compiler;

import org.apache.commons.jxpath.ri.QName;
import org.junit.Test;
import static org.junit.Assert.assertTrue;

/**
 * Contains tests for the {@link ExtensionFunction} class.
 */
public class ExtensionFunctionTest {

    /**
     * Verifies that an ExtensionFunction is always considered context-dependent.
     *
     * According to its design, an extension function receives the current evaluation
     * context to perform its work. Therefore, the computeContextDependent() method
     * should consistently return true, regardless of the function's name or arguments.
     */
    @Test
    public void computeContextDependentShouldAlwaysReturnTrue() {
        // Arrange: Create an instance of ExtensionFunction.
        // The specific function name and arguments are not relevant for this method's
        // behavior, so we can use simple, minimal values.
        QName dummyFunctionName = new QName("ns", "any-function");
        Expression[] noArguments = new Expression[0];
        ExtensionFunction extensionFunction = new ExtensionFunction(dummyFunctionName, noArguments);

        // Act: Call the method under test.
        boolean isContextDependent = extensionFunction.computeContextDependent();

        // Assert: The result must be true.
        assertTrue("An ExtensionFunction is expected to always be context-dependent.", isContextDependent);
    }
}