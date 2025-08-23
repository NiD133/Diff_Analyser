package org.apache.commons.jxpath.ri.axes;

import org.apache.commons.jxpath.ri.compiler.Expression;
import org.apache.commons.jxpath.ri.compiler.Step;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.junit.Test;

import static org.junit.Assert.assertNull;

/**
 * Contains tests for the static utility methods in {@link SimplePathInterpreter}.
 */
public class SimplePathInterpreterTest {

    /**
     * Tests that interpretSimpleExpressionPath returns null when provided with a null root node
     * and empty path components (predicates and steps). This verifies the method's handling of
     * null and empty inputs.
     */
    @Test
    public void interpretSimpleExpressionPath_withNullRootAndEmptyPath_shouldReturnNull() {
        // Arrange: Define the inputs for the edge case scenario.
        // The method is called with a null context, a null root node,
        // and empty arrays for path steps and predicates.
        NodePointer rootPointer = null;
        Step[] emptySteps = new Step[0];
        Expression[] emptyPredicates = new Expression[0];

        // Act: Execute the method under test.
        NodePointer result = SimplePathInterpreter.interpretSimpleExpressionPath(
                null, // EvalContext is null
                rootPointer,
                emptyPredicates,
                emptySteps);

        // Assert: Verify that the result is null as expected.
        assertNull("The method should return null for a null root and empty path", result);
    }
}