package org.apache.commons.jxpath.ri.axes;

import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.QName;
import org.apache.commons.jxpath.ri.compiler.NodeNameTest;
import org.apache.commons.jxpath.ri.compiler.Step;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.junit.Test;

import java.util.Locale;

import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;

/**
 * Contains tests for the static utility methods in {@link SimplePathInterpreter}.
 */
public class SimplePathInterpreterTest {

    /**
     * Tests that createNullPointer throws a NullPointerException when the
     * provided EvalContext is null, as the method relies on the context
     * for its operations.
     */
    @Test(expected = NullPointerException.class)
    public void createNullPointerShouldThrowNPEForNullContext() {
        // Arrange: Set up the arguments for the createNullPointer method.
        // A mock Step is needed to construct the parent pointer and the steps array.
        Step mockStep = mock(Step.class);
        NodeNameTest nodeNameTest = new NodeNameTest(new QName("", ""), "<<unknown namespace>>");
        doReturn(5).when(mockStep).getAxis();
        doReturn(nodeNameTest).when(mockStep).getNodeTest();
        doReturn(null).when(mockStep).getPredicates();

        // The method requires a non-null parent pointer.
        NodePointer parentPointer = NodePointer.newNodePointer(
                new QName("", "parent"), mockStep, Locale.JAPANESE);

        // The method requires an array of steps.
        Step[] steps = new Step[5];
        int currentStepIndex = 2;
        steps[currentStepIndex] = mockStep;

        // Act: Call the method with a null EvalContext.
        // The assertion is handled by the @Test(expected=...) annotation,
        // which verifies that a NullPointerException is thrown.
        SimplePathInterpreter.createNullPointer(null, parentPointer, steps, currentStepIndex);
    }
}