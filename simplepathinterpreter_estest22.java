package org.apache.commons.jxpath.ri.axes;

import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.QName;
import org.apache.commons.jxpath.ri.compiler.Compiler;
import org.apache.commons.jxpath.ri.compiler.NodeNameTest;
import org.apache.commons.jxpath.ri.compiler.Step;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.apache.commons.jxpath.ri.model.beans.NullPointer;
import org.junit.Test;

import java.util.Locale;

import static org.junit.Assert.fail;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;

public class SimplePathInterpreter_ESTestTest22 extends SimplePathInterpreter_ESTest_scaffolding {

    /**
     * Tests that SimplePathInterpreter.createNullPointer throws a NullPointerException
     * when passed a null EvalContext.
     */
    @Test(timeout = 4000)
    public void createNullPointerThrowsNPEForNullContext() {
        // ARRANGE: Set up the arguments for the method call.
        // A parent pointer is the starting point for path interpretation.
        NodePointer parentPointer = new NullPointer(Locale.ENGLISH, "testId");

        // A mock Step simulates a part of an XPath expression. The details are configured
        // to ensure the execution path reaches the point where the null context is used.
        Step mockStep = mock(Step.class);
        doReturn(Compiler.AXIS_CHILD).when(mockStep).getAxis(); // Use constant for clarity
        doReturn(new NodeNameTest(new QName("anyName"))).when(mockStep).getNodeTest();
        doReturn(null).when(mockStep).getPredicates(); // No predicates for this step

        Step[] steps = {mockStep};
        int currentStepIndex = 0;

        // The EvalContext is intentionally null to trigger the expected exception.
        EvalContext nullEvalContext = null;

        // ACT & ASSERT: Call the method and verify that it throws the correct exception.
        try {
            SimplePathInterpreter.createNullPointer(nullEvalContext, parentPointer, steps, currentStepIndex);
            fail("Expected a NullPointerException, but no exception was thrown.");
        } catch (NullPointerException e) {
            // SUCCESS: The expected exception was caught.
        }
    }
}