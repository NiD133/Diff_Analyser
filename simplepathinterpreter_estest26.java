package org.apache.commons.jxpath.ri.axes;

import org.apache.commons.jxpath.ri.Compiler;
import org.apache.commons.jxpath.ri.QName;
import org.apache.commons.jxpath.ri.compiler.NodeNameTest;
import org.apache.commons.jxpath.ri.compiler.Step;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.apache.commons.jxpath.ri.model.beans.NullPointer;
import org.junit.Test;

import java.util.Locale;

import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;

/*
 * Note: The original test class structure, including its name and parent class,
 * has been preserved as it may be part of a larger, auto-generated test suite.
 * The focus of the improvement is on the test case itself.
 */
public class SimplePathInterpreter_ESTestTest26 extends SimplePathInterpreter_ESTest_scaffolding {

    /**
     * Tests that interpretSimpleLocationPath returns null for a path containing a non-child axis.
     * <p>
     * The {@link SimplePathInterpreter} is an optimization for simple paths consisting
     * exclusively of 'child::' axes. This test verifies that when it encounters a
     * different axis, such as 'attribute::', it correctly abstains from processing
     * by returning null, signaling that a more general interpreter should be used.
     */
    @Test(timeout = 4000)
    public void interpretSimpleLocationPathShouldReturnNullForNonChildAxis() {
        // ARRANGE
        // Create a mock Step representing an 'attribute::' axis, which the simple
        // interpreter is not designed to handle.
        Step attributeStep = mock(Step.class);
        doReturn(Compiler.AXIS_ATTRIBUTE).when(attributeStep).getAxis();
        doReturn(new NodeNameTest(new QName(null, "anyAttribute"))).when(attributeStep).getNodeTest();
        doReturn(null).when(attributeStep).getPredicates();

        Step[] steps = {attributeStep};
        NodePointer rootPointer = new NullPointer(Locale.JAPANESE, "root");

        // ACT
        // Attempt to interpret the path. The EvalContext is null because it's not
        // needed for the interpreter to identify the unsupported axis.
        NodePointer resultPointer = SimplePathInterpreter.interpretSimpleLocationPath(null, rootPointer, steps);

        // ASSERT
        assertNull("Interpreter should return null for paths with non-child axes.", resultPointer);
    }
}