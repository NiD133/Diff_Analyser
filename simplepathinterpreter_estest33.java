package org.apache.commons.jxpath.ri.axes;

import org.apache.commons.jxpath.BasicVariables;
import org.apache.commons.jxpath.ri.QName;
import org.apache.commons.jxpath.ri.compiler.Expression;
import org.apache.commons.jxpath.ri.compiler.Step;
import org.apache.commons.jxpath.ri.model.VariablePointer;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

/**
 * Contains tests for the {@link SimplePathInterpreter} class.
 * This test focuses on how the interpreter handles paths involving variables.
 */
public class SimplePathInterpreterTest {

    /**
     * Tests that interpretSimpleExpressionPath throws an IllegalArgumentException
     * when the path starts with a variable that has not been declared.
     */
    @Test
    public void interpretPathWithUndeclaredVariableShouldThrowException() {
        // Arrange
        // 1. Define a variable name that will not be present in the variables context.
        final String variableName = "undeclared_variable";
        QName undeclaredVariableName = new QName(variableName);

        // 2. Create an empty set of variables.
        BasicVariables variables = new BasicVariables();

        // 3. Create a pointer to the undeclared variable. This pointer acts as the
        //    root of the path to be interpreted.
        VariablePointer undeclaredVariablePointer = new VariablePointer(variables, undeclaredVariableName);

        // 4. The method requires non-null arrays for steps and predicates. Their content
        //    is irrelevant for this test, as the failure should occur before they are processed.
        Step[] pathSteps = new Step[1];
        Expression[] predicates = new Expression[0];

        // Act & Assert
        try {
            // Attempt to interpret a path starting from the undeclared variable.
            // The EvalContext can be null because it is not accessed before the variable lookup fails.
            SimplePathInterpreter.interpretSimpleExpressionPath(
                    null,
                    undeclaredVariablePointer,
                    predicates,
                    pathSteps);
            fail("Expected an IllegalArgumentException because the root variable is not declared.");
        } catch (IllegalArgumentException e) {
            // Verify that the exception was thrown for the correct reason.
            String expectedMessage = "No such variable: '" + variableName + "'";
            assertEquals(expectedMessage, e.getMessage());
        }
    }
}