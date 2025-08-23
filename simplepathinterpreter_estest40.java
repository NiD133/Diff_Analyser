package org.apache.commons.jxpath.ri.axes;

import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.QName;
import org.apache.commons.jxpath.ri.compiler.Expression;
import org.apache.commons.jxpath.ri.compiler.NodeNameTest;
import org.apache.commons.jxpath.ri.compiler.Step;
import org.junit.Test;

import static org.evosuite.shaded.org.mockito.Mockito.mock;
import static org.evosuite.shaded.org.mockito.Mockito.when;
import static org.junit.Assert.fail;

public class SimplePathInterpreter_ESTestTest40 extends SimplePathInterpreter_ESTest_scaffolding {

    /**
     * Tests that createNullPointer throws a NullPointerException when a step in the
     * path has a null predicate array.
     * <p>
     * This scenario is simulated by using a single mock Step instance for multiple
     * path steps. The mock is configured to return a valid (empty) predicate array
     * on the first invocation, but null on the second. The method is expected to
     * process the first step and then fail when it encounters the null predicates
     * from the second step.
     */
    @Test(timeout = 4000)
    public void createNullPointerThrowsNPEWhenStepPredicatesAreNull() {
        // ARRANGE
        // A single mock Step instance is used for all steps in the path.
        // It's configured to return different values on subsequent calls to simulate
        // traversing a path where a subsequent step is malformed.
        Step mockStep = mock(Step.class);

        // Configure the mock to return a valid empty predicate array for the first step,
        // and null for the second step, which is expected to cause the NPE.
        Expression[] emptyPredicates = new Expression[0];
        when(mockStep.getPredicates()).thenReturn(emptyPredicates, null);

        // The axis values are configured to allow the method to proceed to the predicate check.
        // The specific values are not critical to triggering the NPE.
        when(mockStep.getAxis()).thenReturn(6, 0);

        // A path with two steps is sufficient to trigger the behavior. Both steps
        // use the same mock instance.
        Step[] steps = {mockStep, mockStep};

        // A minimal, non-null evaluation context is required for the method call.
        QName qName = new QName("", "test");
        NodeNameTest nodeNameTest = new NodeNameTest(qName);
        EvalContext context = new SelfContext(null, nodeNameTest);

        // ACT & ASSERT
        try {
            // The method starts processing at the first step (index 0).
            // It should process steps[0] successfully and then throw an NPE on steps[1].
            SimplePathInterpreter.createNullPointer(context, null, steps, 0);
            fail("Expected a NullPointerException because the second step's predicates are null.");
        } catch (NullPointerException e) {
            // This is the expected outcome. The test passes.
        }
    }
}