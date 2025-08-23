package org.apache.commons.jxpath.ri.axes;

import org.apache.commons.jxpath.ri.Compiler;
import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.QName;
import org.apache.commons.jxpath.ri.compiler.Constant;
import org.apache.commons.jxpath.ri.compiler.Expression;
import org.apache.commons.jxpath.ri.compiler.Step;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.apache.commons.jxpath.ri.model.beans.NullPointer;
import org.junit.Test;

import java.util.Locale;

import static org.evosuite.shaded.org.mockito.Mockito.mock;
import static org.evosuite.shaded.org.mockito.Mockito.when;

public class SimplePathInterpreter_ESTestTest4 extends SimplePathInterpreter_ESTest_scaffolding {

    /**
     * Verifies that interpretSimpleLocationPath throws a NullPointerException when
     * passed a null EvalContext, under specific conditions established by a sequence
     * of interactions with a mocked Step object.
     *
     * <p>This test was automatically generated and has been refactored for clarity.
     * The original test used a complex, two-call setup where the first call's only
     * purpose was to change the state of the mock for the second call.</p>
     */
    @Test(timeout = 4000, expected = NullPointerException.class)
    public void interpretSimpleLocationPathThrowsNPEWhenContextIsNullAndMockStepIsInSpecificState() {
        // ARRANGE: Create a mock Step with a sequence of return values.
        // The same mock instance is used for both steps in the path.
        Step mockedStep = mock(Step.class);
        Step[] steps = { mockedStep, mockedStep };

        // Configure the mock to return different values on subsequent calls.
        // The first two axis values are intentionally invalid to control the code path
        // in the warm-up call, while the next two are valid (AXIS_SELF).
        final int INVALID_AXIS_1 = -440;
        final int INVALID_AXIS_2 = Integer.MIN_VALUE;
        when(mockedStep.getAxis()).thenReturn(INVALID_AXIS_1, INVALID_AXIS_2, Compiler.AXIS_SELF, Compiler.AXIS_SELF);

        Expression[] predicates = { new Constant("some-predicate") };
        // The first two calls to getPredicates() return a valid array; subsequent calls return null.
        when(mockedStep.getPredicates()).thenReturn(predicates, predicates, null, null, null);

        // ARRANGE MOCK STATE: This first method call is not the primary subject of the test.
        // Its purpose is to "consume" the initial set of responses from the mock,
        * advancing its internal state for the actual test call that follows.
        NodePointer warmUpPointer = NodePointer.newNodePointer(new QName("root"), "bean", Locale.getDefault());
        SimplePathInterpreter.interpretSimpleExpressionPath(null, warmUpPointer, predicates, steps);

        // ACT: Call the method under test with a null EvalContext.
        // At this point, the mock is in a state where getAxis() returns AXIS_SELF and
        // getPredicates() returns null. This specific state, combined with the null context,
        // is expected to trigger a NullPointerException within the interpreter.
        NodePointer rootPointerForTest = new NullPointer(Locale.getDefault(), "id");
        SimplePathInterpreter.interpretSimpleLocationPath(null, rootPointerForTest, steps);

        // ASSERT: The test expects a NullPointerException, which is declared in the @Test annotation.
    }
}