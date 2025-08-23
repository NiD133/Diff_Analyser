package org.apache.commons.jxpath.ri.axes;

import org.apache.commons.jxpath.BasicVariables;
import org.apache.commons.jxpath.ri.QName;
import org.apache.commons.jxpath.ri.compiler.Step;
import org.apache.commons.jxpath.ri.model.VariablePointer;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

/**
 * Contains tests for exception scenarios in {@link SimplePathInterpreter}.
 */
public class SimplePathInterpreterExceptionTest {

    /**
     * Tests that {@code createNullPointer} throws an {@code IllegalArgumentException}
     * when the parent pointer refers to an undeclared variable. The method is expected
     * to fail early because it cannot resolve the variable.
     */
    @Test
    public void createNullPointer_whenVariableIsUndeclared_throwsIllegalArgumentException() {
        // Arrange: Set up a pointer to a variable that has not been declared.
        QName undeclaredVariableName = new QName("ns", "myVar");
        BasicVariables variables = new BasicVariables(); // An empty set of variables.
        VariablePointer pointerToUndeclaredVariable = new VariablePointer(variables, undeclaredVariableName);

        Step[] emptySteps = new Step[0];
        int currentStepIndex = 0; // This value is irrelevant as the exception is thrown before it's used.

        // Act & Assert: Expect an exception when trying to create a null pointer,
        // as the variable lookup will fail.
        try {
            SimplePathInterpreter.createNullPointer(
                    null, // EvalContext is not used before the variable is resolved.
                    pointerToUndeclaredVariable,
                    emptySteps,
                    currentStepIndex);
            fail("Expected an IllegalArgumentException for an undeclared variable.");
        } catch (IllegalArgumentException e) {
            // Verify that the exception message correctly identifies the missing variable.
            assertEquals("No such variable: 'ns:myVar'", e.getMessage());
        }
    }
}