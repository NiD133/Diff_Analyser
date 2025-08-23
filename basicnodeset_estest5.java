package org.apache.commons.jxpath;

import org.apache.commons.jxpath.ri.QName;
import org.apache.commons.jxpath.ri.model.VariablePointer;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

/**
 * Contains tests for the {@link BasicNodeSet} class.
 * This class focuses on specific behaviors and edge cases.
 */
public class BasicNodeSetTest {

    /**
     * Tests that calling getNodes() on a set containing a pointer to an
     * undefined variable throws a RuntimeException.
     *
     * The VariablePointer is created with a null QName, which cannot be resolved
     * in any JXPathContext, thus simulating a reference to an undefined variable.
     */
    @Test
    public void getNodesShouldThrowExceptionForUndefinedVariable() {
        // Arrange: Create a node set and add a pointer to an undefined variable.
        BasicNodeSet nodeSet = new BasicNodeSet();
        Pointer undefinedVariablePointer = new VariablePointer((QName) null);
        nodeSet.add(undefinedVariablePointer);

        // Act & Assert: Attempting to get nodes should fail because the variable is undefined.
        try {
            nodeSet.getNodes();
            fail("Expected a RuntimeException because the node set contains a pointer to an undefined variable.");
        } catch (final RuntimeException e) {
            // Verify that the exception message correctly identifies the issue.
            assertEquals("Undefined variable: null", e.getMessage());
        }
    }
}