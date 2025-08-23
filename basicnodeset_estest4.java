package org.apache.commons.jxpath;

import org.apache.commons.jxpath.ri.QName;
import org.apache.commons.jxpath.ri.model.VariablePointer;
import org.junit.Test;

/**
 * Contains tests for the {@link BasicNodeSet} class.
 * This refactored test focuses on the behavior of getValues() with undeclared variables.
 */
public class BasicNodeSet_ESTestTest4 { // Original class name kept for context

    /**
     * Tests that getValues() throws an IllegalArgumentException if the node set
     * contains a pointer to a variable that has not been declared.
     *
     * The getValues() method iterates through its pointers and resolves their
     * values. For a VariablePointer, this triggers a lookup in the associated
     * variable context. This test confirms that an exception is thrown if the
     * variable is not found in that context.
     */
    @Test(expected = IllegalArgumentException.class)
    public void getValuesShouldThrowExceptionWhenVariableIsUndeclared() {
        // Arrange: Create a node set containing a pointer to an undeclared variable.
        BasicNodeSet nodeSet = new BasicNodeSet();
        BasicVariables variables = new BasicVariables(); // An empty variable scope.
        QName undeclaredVariableName = new QName("undeclaredVar");

        // This pointer points to a variable that does not exist in the 'variables' context.
        Pointer pointerToUndeclaredVariable = new VariablePointer(variables, undeclaredVariableName);
        nodeSet.add(pointerToUndeclaredVariable);

        // Act: Attempt to retrieve the values from the node set.
        // This should fail because 'undeclaredVar' cannot be resolved.
        nodeSet.getValues();

        // Assert: An IllegalArgumentException is expected, as declared in the @Test annotation.
    }
}