package org.apache.commons.jxpath.ri.compiler;

import org.apache.commons.jxpath.JXPathContext;
import org.apache.commons.jxpath.JXPathFunctionNotFoundException;
import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.JXPathContextReferenceImpl;
import org.apache.commons.jxpath.ri.QName;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

/**
 * Contains tests for the {@link ExtensionFunction} class.
 */
public class ExtensionFunctionTest {

    /**
     * Tests that computing an ExtensionFunction with a name that is not registered
     * in the JXPathContext throws a JXPathFunctionNotFoundException.
     */
    @Test
    public void computeWithUndefinedFunctionThrowsException() {
        // Arrange: Set up the test conditions.

        // 1. Define a function name that is guaranteed to be undefined.
        // An empty prefix and an empty local name are used here.
        QName undefinedFunctionName = new QName("", "");

        // 2. Create an ExtensionFunction instance with the undefined name.
        // The arguments are not relevant for this test, so we provide an empty array.
        Expression[] functionArgs = new Expression[0];
        ExtensionFunction extensionFunction = new ExtensionFunction(undefinedFunctionName, functionArgs);

        // 3. Create a minimal, valid evaluation context.
        JXPathContextReferenceImpl jxpathContext = (JXPathContextReferenceImpl) JXPathContext.newContext(new Object());
        EvalContext evalContext = jxpathContext.getAbsoluteRootContext();

        // Act & Assert: Execute the method and verify the outcome.
        try {
            extensionFunction.compute(evalContext);
            fail("Expected a JXPathFunctionNotFoundException because the function is not defined.");
        } catch (JXPathFunctionNotFoundException e) {
            // Verify that the exception message correctly identifies the undefined function.
            // The expected format is "prefix:localName".
            assertEquals("Undefined function: :", e.getMessage());
        }
    }
}