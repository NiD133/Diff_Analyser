package org.apache.commons.jxpath.ri.compiler;

import org.apache.commons.jxpath.Function;
import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.JXPathContextReferenceImpl;
import org.apache.commons.jxpath.ri.QName;
import org.apache.commons.jxpath.ri.axes.RootContext;
import org.apache.commons.jxpath.ri.model.VariablePointer;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Test suite for the {@link ExtensionFunction} class.
 */
public class ExtensionFunctionTest {

    /**
     * Tests that computeValue() throws a RuntimeException when the JXPathContext
     * cannot find the specified function. This simulates a scenario where a function
     * is called in an XPath expression but is not registered with the context.
     */
    @Test
    public void computeValueShouldThrowExceptionWhenFunctionIsNotFound() {
        // Arrange: Set up an ExtensionFunction and a mock context that
        // simulates the function not being found.
        Expression[] noArguments = new Expression[0];
        // The function name is intentionally null to match the original test's scenario,
        // which leads to the specific "null[]" message.
        QName functionName = null;
        ExtensionFunction extensionFunction = new ExtensionFunction(functionName, noArguments);

        // Mock the JXPathContext to always return null when getFunction is called.
        JXPathContextReferenceImpl mockJXPathContext = mock(JXPathContextReferenceImpl.class);
        when(mockJXPathContext.getFunction(any(QName.class), any(Object[].class)))
                .thenReturn(null);

        // Create the evaluation context required to call computeValue.
        VariablePointer rootPointer = new VariablePointer(new QName("root"));
        EvalContext evalContext = new RootContext(mockJXPathContext, rootPointer);

        // Act & Assert: Verify that calling computeValue throws a RuntimeException
        // with a specific message indicating the function was not found.
        try {
            extensionFunction.computeValue(evalContext);
            fail("Expected a RuntimeException to be thrown because the function does not exist.");
        } catch (RuntimeException e) {
            // The expected message format is "No such function: <functionName>[]"
            // In this specific test case, the function name is null.
            assertEquals("No such function: null[]", e.getMessage());
        }
    }
}