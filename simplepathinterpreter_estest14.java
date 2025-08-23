package org.apache.commons.jxpath.ri.axes;

import org.apache.commons.jxpath.ri.QName;
import org.apache.commons.jxpath.ri.compiler.Compiler;
import org.apache.commons.jxpath.ri.compiler.NodeNameTest;
import org.apache.commons.jxpath.ri.compiler.ProcessingInstructionTest;
import org.apache.commons.jxpath.ri.compiler.Step;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.junit.Test;

import java.util.Locale;

import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;

/**
 * Contains improved tests for the {@link SimplePathInterpreter} class.
 */
public class SimplePathInterpreter_ESTestTest14 {

    /**
     * Tests that interpretSimpleLocationPath throws a ClassCastException when a step
     * contains a NodeTest that is not a NodeNameTest.
     * <p>
     * The SimplePathInterpreter is an optimization that only supports a limited
     * set of node tests (specifically NodeNameTest). This test ensures it fails
     * correctly when given an unsupported type like ProcessingInstructionTest.
     */
    @Test(expected = ClassCastException.class)
    public void interpretSimpleLocationPathThrowsExceptionForUnsupportedNodeTest() {
        // ARRANGE
        // 1. Define a starting node for the path interpretation. Its specific value is not
        //    relevant for this test, as the failure occurs during step processing.
        NodePointer startNode = NodePointer.newNodePointer(new QName("root"), new Object(), Locale.ENGLISH);

        // 2. Create a mock Step that uses an unsupported NodeTest type.
        //    The SimplePathInterpreter expects a NodeNameTest but gets a ProcessingInstructionTest.
        ProcessingInstructionTest unsupportedNodeTest = new ProcessingInstructionTest("unsupported-target");
        Step stepWithUnsupportedNodeTest = mock(Step.class);

        // 3. Configure the mock step. The axis must be one that the interpreter handles,
        //    but the NodeTest is deliberately of an unsupported type.
        doReturn(Compiler.AXIS_FOLLOWING_SIBLING).when(stepWithUnsupportedNodeTest).getAxis();
        doReturn(unsupportedNodeTest).when(stepWithUnsupportedNodeTest).getNodeTest();
        doReturn(null).when(stepWithUnsupportedNodeTest).getPredicates();

        Step[] steps = {stepWithUnsupportedNodeTest};

        // ACT
        // Attempt to interpret the path. This should fail with a ClassCastException
        // when the interpreter tries to cast ProcessingInstructionTest to NodeNameTest.
        // The EvalContext is not used in the code path leading to the exception, so it can be null.
        SimplePathInterpreter.interpretSimpleLocationPath(null, startNode, steps);

        // ASSERT
        // The @Test(expected) annotation asserts that a ClassCastException is thrown.
    }
}