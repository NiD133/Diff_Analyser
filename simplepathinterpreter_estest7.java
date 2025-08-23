package org.apache.commons.jxpath.ri.axes;

import org.apache.commons.jxpath.JXPathContext;
import org.apache.commons.jxpath.ri.Compiler;
import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.QName;
import org.apache.commons.jxpath.ri.compiler.Expression;
import org.apache.commons.jxpath.ri.compiler.Step;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.junit.Test;

import static org.evosuite.shaded.org.mockito.Mockito.mock;
import static org.evosuite.shaded.org.mockito.Mockito.when;
import static org.junit.Assert.*;

/**
 * Contains tests for the {@link SimplePathInterpreter} class.
 * This class provides a refactored test case from the original generated suite.
 */
public class SimplePathInterpreter_ESTestTest7 extends SimplePathInterpreter_ESTest_scaffolding {

    /**
     * Tests that interpreting a simple path consisting only of "self::" axis steps
     * correctly returns the original root node pointer.
     *
     * <p>The original test was auto-generated and difficult to understand. This version
     * clarifies the intent by using descriptive names, simplifying the setup, and adding
     * more specific assertions to verify the behavior.</p>
     */
    @Test(timeout = 4000)
    public void interpretPathWithOnlySelfAxisStepsShouldReturnOriginalRootPointer() {
        // Arrange: Set up the context, root pointer, and path steps.

        // A simple evaluation context is sufficient as it's not deeply used for this path.
        JXPathContext context = JXPathContext.newContext(null);
        EvalContext evalContext = context.getAbsoluteRootContext();

        // Create a non-null root pointer to start the path interpretation.
        // A BeanPointer is a concrete implementation suitable for this test.
        QName rootName = new QName("root");
        Object bean = "some-bean-object";
        NodePointer rootPointer = NodePointer.newChildNodePointer(null, rootName, bean);

        // Define a path step representing "self::node()".
        // The "self" axis is represented by Compiler.AXIS_SELF (value 0).
        // The step has no predicates.
        Step selfAxisStep = mock(Step.class);
        when(selfAxisStep.getAxis()).thenReturn(Compiler.AXIS_SELF);
        when(selfAxisStep.getPredicates()).thenReturn(new Expression[0]);

        // Create a path consisting of multiple "self::" steps.
        Step[] pathSteps = {selfAxisStep, selfAxisStep, selfAxisStep, selfAxisStep};

        // The overall expression path has no top-level predicates.
        Expression[] noPredicates = new Expression[0];

        // Act: Execute the method under test.
        NodePointer resultPointer = SimplePathInterpreter.interpretSimpleExpressionPath(
                evalContext, rootPointer, noPredicates, pathSteps);

        // Assert: The result should be identical to the original root pointer.
        assertNotNull("The result pointer should not be null", resultPointer);
        assertSame("The result pointer should be the same instance as the root pointer",
                rootPointer, resultPointer);

        // A BeanPointer for a whole object has an index of WHOLE_COLLECTION.
        // This confirms the returned pointer is the original, unmodified pointer.
        assertEquals("The pointer's index should be unchanged",
                NodePointer.WHOLE_COLLECTION, resultPointer.getIndex());
    }
}