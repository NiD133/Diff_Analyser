package org.apache.commons.jxpath.ri.axes;

import org.apache.commons.jxpath.ri.compiler.Step;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.apache.commons.jxpath.ri.model.beans.NullPointer;
import org.junit.Test;

import java.util.Locale;

import static org.junit.Assert.assertSame;

/**
 * Contains tests for the {@link SimplePathInterpreter} class, focusing on specific path interpretation scenarios.
 */
public class SimplePathInterpreterTest {

    /**
     * Tests that interpretSimpleLocationPath returns the original root pointer
     * unmodified when the path (steps array) is empty.
     */
    @Test
    public void interpretSimpleLocationPathWithEmptyStepsReturnsRootPointer() {
        // Arrange
        // Create a root pointer with a specific state (an index) to ensure it's returned unchanged.
        NodePointer rootPointer = new NullPointer(Locale.JAPANESE, "testID");
        rootPointer.setIndex(133);

        // An empty array of steps represents an empty path.
        Step[] emptySteps = new Step[0];

        // Act
        // The EvalContext is not used in this specific code path, so null is acceptable.
        NodePointer resultPointer = SimplePathInterpreter.interpretSimpleLocationPath(null, rootPointer, emptySteps);

        // Assert
        // The method should return the exact same object instance it was given.
        assertSame("When the steps array is empty, the original root pointer should be returned.", rootPointer, resultPointer);
    }
}