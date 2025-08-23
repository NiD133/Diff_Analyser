package org.apache.commons.jxpath.ri.axes;

import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.compiler.Compiler;
import org.apache.commons.jxpath.ri.compiler.Expression;
import org.apache.commons.jxpath.ri.compiler.NodeNameTest;
import org.apache.commons.jxpath.ri.compiler.NodeTypeTest;
import org.apache.commons.jxpath.ri.compiler.Step;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.junit.Test;

import static org.junit.Assert.assertThrows;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;

/**
 * Contains tests for the {@link SimplePathInterpreter} class.
 */
public class SimplePathInterpreterTest {

    /**
     * Tests that interpretSimpleExpressionPath throws a ClassCastException
     * when a step contains a NodeTypeTest instead of the expected NodeNameTest.
     * The interpreter is an optimization for simple paths and does not support
     * all node test types, directly attempting to cast to NodeNameTest.
     */
    @Test
    public void interpretSimpleExpressionPathWithUnsupportedNodeTypeTestThrowsException() {
        // Arrange: Create a step with a NodeTypeTest. The interpreter's fast path
        // expects a NodeNameTest to extract a QName and will fail to cast.
        NodeTypeTest unsupportedNodeTest = new NodeTypeTest(Compiler.NODE_TYPE_NODE);
        Step stepWithUnsupportedNodeTest = mock(Step.class);
        doReturn(Compiler.AXIS_CHILD).when(stepWithUnsupportedNodeTest).getAxis();
        doReturn(unsupportedNodeTest).when(stepWithUnsupportedNodeTest).getNodeTest();
        doReturn(new Expression[0]).when(stepWithUnsupportedNodeTest).getPredicates();

        Step[] steps = {stepWithUnsupportedNodeTest};

        // Arrange: Provide dummy context and pointer, as they are not relevant for this exception.
        EvalContext dummyContext = null;
        NodePointer dummyRootPointer = mock(NodePointer.class);
        Expression[] noPredicates = new Expression[0];

        // Act & Assert: The method should throw a ClassCastException because it cannot
        // cast NodeTypeTest to NodeNameTest.
        assertThrows(ClassCastException.class, () ->
                SimplePathInterpreter.interpretSimpleExpressionPath(dummyContext, dummyRootPointer, noPredicates, steps)
        );
    }
}