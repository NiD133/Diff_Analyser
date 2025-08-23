package org.apache.commons.jxpath.ri.compiler;

import org.apache.commons.jxpath.JXPathContext;
import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.JXPathContextReferenceImpl;
import org.apache.commons.jxpath.ri.QName;
import org.apache.commons.jxpath.ri.axes.RootContext;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.junit.Test;

import java.util.Locale;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

/**
 * Contains tests for the {@link ExtensionFunction} class.
 * This improved version focuses on clarity and maintainability.
 */
public class ExtensionFunctionTest {

    /**
     * Tests that computeValue() throws a RuntimeException when the specified
     * extension function cannot be found or invoked.
     * <p>
     * This scenario is simulated by providing a QName that corresponds to a
     * class name ("org.apache.commons.jxpath.ri.Parser") rather than a
     * valid, invokable function.
     */
    @Test
    public void computeValue_whenFunctionCannotBeInvoked_throwsRuntimeException() {
        // Arrange: Set up an extension function with a name that cannot be resolved
        // to an actual function, and create the necessary evaluation context.
        QName unresolvableFunctionName = new QName("org.apache.commons.jxpath.ri.Parser");
        Expression[] noArguments = null;
        ExtensionFunction extensionFunction = new ExtensionFunction(unresolvableFunctionName, noArguments);

        // The computeValue method requires a valid EvalContext. We create a minimal
        // RootContext, which is a common type of EvalContext used by the JXPath engine.
        JXPathContext jxpathContext = JXPathContext.newContext(new Object());
        NodePointer rootPointer = NodePointer.newNodePointer(new QName("root"), jxpathContext.getContextBean(), Locale.getDefault());
        EvalContext evalContext = new RootContext((JXPathContextReferenceImpl) jxpathContext, rootPointer);

        // Act & Assert: Verify that calling computeValue throws the expected exception
        // with a descriptive message.
        try {
            extensionFunction.computeValue(evalContext);
            fail("Expected a RuntimeException because the extension function is not invokable.");
        } catch (RuntimeException e) {
            // The JXPath engine wraps the underlying error in a JXPathException (a subclass of RuntimeException).
            // We verify the message to ensure it's the correct error.
            String expectedMessage = "Cannot invoke extension function " + unresolvableFunctionName;
            assertEquals(expectedMessage, e.getMessage());
        }
    }
}