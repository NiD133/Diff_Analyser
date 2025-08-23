package org.apache.commons.jxpath.ri.axes;

import org.apache.commons.jxpath.ri.Compiler;
import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.QName;
import org.apache.commons.jxpath.ri.compiler.Step;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.junit.Test;

import java.util.Locale;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Test suite for {@link SimplePathInterpreter}.
 * This version focuses on providing clear, understandable, and maintainable tests.
 */
public class SimplePathInterpreterTest {

    /**
     * Tests that interpreting a simple path consisting of a single "self::node()" step
     * returns the starting node pointer itself. This is the simplest possible path traversal.
     */
    @Test
    public void interpretSimpleLocationPathWithSelfStepReturnsSameNode() {
        // Arrange: Create a starting node and a path with a single "self" step.
        QName qName = new QName("testNode");
        Object bean = new Object();
        NodePointer startNode = NodePointer.newNodePointer(qName, bean, Locale.ENGLISH);

        // A "self::node()" step is represented by a Step object with axis SELF
        // and no predicates. We use a mock to simulate this.
        Step selfStep = mock(Step.class);
        when(selfStep.getAxis()).thenReturn(Compiler.AXIS_SELF);
        when(selfStep.getPredicates()).thenReturn(null);

        Step[] steps = {selfStep};

        // Act: Interpret the simple location path.
        // The EvalContext can be null for this type of simple interpretation.
        NodePointer resultNode = SimplePathInterpreter.interpretSimpleLocationPath(null, startNode, steps);

        // Assert: The returned node pointer should be the exact same instance as the starting node.
        assertNotNull("The result node pointer should not be null", resultNode);
        assertSame("For a 'self::node()' step, the original node pointer should be returned",
                startNode, resultNode);
    }
}