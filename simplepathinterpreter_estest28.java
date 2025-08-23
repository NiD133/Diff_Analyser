package org.apache.commons.jxpath.ri.axes;

import org.apache.commons.jxpath.JXPathContext;
import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.JXPathContextReferenceImpl;
import org.apache.commons.jxpath.ri.QName;
import org.apache.commons.jxpath.ri.compiler.Expression;
import org.apache.commons.jxpath.ri.compiler.Step;
import org.apache.commons.jxpath.ri.compiler.VariableReference;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.junit.Test;

import java.util.Locale;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

// The test class name is kept as is from the original file.
public class SimplePathInterpreter_ESTestTest28 extends SimplePathInterpreter_ESTest_scaffolding {

    /**
     * Tests that interpretSimpleExpressionPath throws a RuntimeException when an expression
     * contains a reference to a variable that is not defined in the JXPathContext.
     */
    @Test(timeout = 4000)
    public void interpretSimpleExpressionPathWithUndefinedVariableThrowsRuntimeException() {
        // ARRANGE: Set up a context and an expression path with a predicate
        // that references an undefined variable.

        // 1. Define the name for the variable that will be undefined.
        String undefinedVariableName = "<<unknown namespace>>";
        QName undefinedVariableQName = new QName(undefinedVariableName);

        // 2. Create a basic JXPath context. The root object is arbitrary as it's not
        // relevant to the variable resolution logic being tested.
        NodePointer rootPointer = NodePointer.newNodePointer(new QName("root"), new Object(), Locale.getDefault());
        JXPathContextReferenceImpl jxpathContext = new JXPathContextReferenceImpl(null, rootPointer, rootPointer);
        EvalContext evalContext = new RootContext(jxpathContext, rootPointer);

        // 3. Create a predicate array containing only the reference to the undefined variable.
        // This focuses the test on the specific failure condition.
        Expression undefinedVariableRef = new VariableReference(undefinedVariableQName);
        Expression[] predicates = {undefinedVariableRef};
        Step[] steps = null; // The path has no steps, only predicates on the root.

        // ACT & ASSERT: Attempting to interpret the path should throw a RuntimeException.
        try {
            SimplePathInterpreter.interpretSimpleExpressionPath(evalContext, rootPointer, predicates, steps);
            fail("Expected a RuntimeException for an undefined variable, but none was thrown.");
        } catch (RuntimeException e) {
            // Verify that the exception message clearly indicates the undefined variable.
            String expectedMessage = "Undefined variable: " + undefinedVariableName;
            assertEquals(expectedMessage, e.getMessage());
        }
    }
}