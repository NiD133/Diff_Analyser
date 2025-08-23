package org.apache.commons.jxpath.ri.axes;

import org.apache.commons.jxpath.ri.Compiler;
import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.compiler.Expression;
import org.apache.commons.jxpath.ri.compiler.Step;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.junit.Test;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Contains tests for the {@link SimplePathInterpreter} class.
 */
public class SimplePathInterpreterTest {

    /**
     * Verifies that interpretSimpleLocationPath throws a NullPointerException
     * if it encounters a null element in the steps array.
     */
    @Test(expected = NullPointerException.class)
    public void interpretSimpleLocationPathWithNullStepThrowsException() {
        // ARRANGE
        // A root node pointer to start the path interpretation from. A mock is sufficient.
        NodePointer rootPointer = mock(NodePointer.class);

        // To reach the problematic null step, the interpreter must first process a valid step.
        // A "self::node()" step is the simplest, causing the interpreter to advance
        // to the next step in the path.
        Step selfStep = mock(Step.class);
        when(selfStep.getAxis()).thenReturn(Compiler.AXIS_SELF);
        when(selfStep.getPredicates()).thenReturn(new Expression[0]);

        // Create a path where the second step is null.
        Step[] stepsWithNull = new Step[]{selfStep, null};

        // ACT
        // The interpreter will process the first step successfully, then throw a
        // NullPointerException when it tries to access the second, null step.
        SimplePathInterpreter.interpretSimpleLocationPath(null, rootPointer, stepsWithNull);

        // ASSERT
        // The @Test(expected) annotation handles the exception assertion.
    }
}