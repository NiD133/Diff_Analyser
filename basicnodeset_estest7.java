package org.apache.commons.jxpath;

import org.apache.commons.jxpath.ri.QName;
import org.apache.commons.jxpath.ri.model.VariablePointer;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

/**
 * Contains tests for the {@link BasicNodeSet} class.
 */
// The original test class name 'BasicNodeSet_ESTestTest7' is kept, but would ideally be renamed.
// The scaffolding class is retained as it might contain necessary setup.
public class BasicNodeSet_ESTestTest7 extends BasicNodeSet_ESTest_scaffolding {

    /**
     * Tests that calling getNodes() on a set containing a VariablePointer
     * to an undefined variable throws an IllegalArgumentException.
     *
     * The getNodes() method triggers the evaluation of each pointer. When it
     * tries to evaluate the VariablePointer, the underlying BasicVariables
     * object fails to find the variable and throws the exception.
     */
    @Test
    public void getNodesShouldThrowExceptionForPointerWithUndefinedVariable() {
        // Arrange: Create a NodeSet containing a pointer to a variable that has not been declared.
        BasicNodeSet nodeSet = new BasicNodeSet();
        BasicVariables variables = new BasicVariables();

        // A QName with an empty prefix and local name corresponds to the variable name ":".
        QName undefinedVariableName = new QName("", "");
        VariablePointer pointerToUndefinedVariable = new VariablePointer(variables, undefinedVariableName);

        nodeSet.add(pointerToUndefinedVariable);

        // Act & Assert: Attempting to get nodes should fail because the variable cannot be resolved.
        try {
            nodeSet.getNodes();
            fail("Expected an IllegalArgumentException because the variable does not exist.");
        } catch (IllegalArgumentException e) {
            // Verify that the correct exception was thrown with the expected message.
            assertEquals("The exception message should indicate the missing variable.",
                    "No such variable: ':'", e.getMessage());
        }
    }
}