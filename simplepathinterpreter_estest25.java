package org.apache.commons.jxpath.ri.axes;

import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.compiler.Step;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.junit.Test;

// The original test class name and inheritance are kept to show a direct
// improvement of the provided code. In a real-world scenario, the class
// name would likely be improved to e.g., SimplePathInterpreterTest.
public class SimplePathInterpreter_ESTestTest25 extends SimplePathInterpreter_ESTest_scaffolding {

    /**
     * Verifies that createNullPointer throws an ArrayIndexOutOfBoundsException
     * when called with a negative step index. The method attempts to access
     * the 'steps' array using this index, which is an invalid operation.
     */
    @Test(expected = ArrayIndexOutOfBoundsException.class)
    public void createNullPointerThrowsExceptionForNegativeStepIndex() {
        // Arrange: Prepare the arguments for the method under test.
        // A negative index is chosen to trigger the expected exception.
        int negativeStepIndex = -183;
        Step[] steps = new Step[0];
        EvalContext context = null;
        NodePointer parentPointer = null;

        // Act: Execute the method with the invalid argument.
        SimplePathInterpreter.createNullPointer(context, parentPointer, steps, negativeStepIndex);

        // Assert: The test framework verifies that an ArrayIndexOutOfBoundsException
        // is thrown, as declared in the @Test annotation. No further assertions
        // are needed.
    }
}