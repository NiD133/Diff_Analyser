package org.apache.commons.jxpath;

import org.apache.commons.jxpath.ri.QName;
import org.apache.commons.jxpath.ri.model.VariablePointer;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

/**
 * Test suite for {@link BasicNodeSet}.
 */
// The original test class name is kept for consistency, but EvoSuite dependencies are removed.
public class BasicNodeSet_ESTestTest1 {

    /**
     * Tests that calling toString() on a BasicNodeSet throws an IllegalArgumentException
     * if it contains a pointer to a variable that has not been declared.
     *
     * The toString() method internally tries to resolve the value of each pointer,
     * which triggers the exception.
     */
    @Test
    public void toString_whenContainsPointerToUndeclaredVariable_throwsIllegalArgumentException() {
        // Arrange
        // 1. Create a variable context that does not have any variables declared.
        BasicVariables variables = new BasicVariables();
        QName undeclaredVariableName = new QName("undeclaredVar");

        // 2. Create a pointer that refers to the undeclared variable.
        Pointer pointerToUndeclaredVariable = new VariablePointer(variables, undeclaredVariableName);

        // 3. Create the node set and add the pointer to it.
        BasicNodeSet nodeSet = new BasicNodeSet();
        nodeSet.add(pointerToUndeclaredVariable);

        // Act & Assert
        try {
            nodeSet.toString();
            fail("Expected an IllegalArgumentException because the node set contains a pointer to an undeclared variable.");
        } catch (IllegalArgumentException e) {
            // Verify the exception message is correct.
            assertEquals("No such variable: 'undeclaredVar'", e.getMessage());
        }
    }
}