package org.apache.commons.jxpath;

import org.apache.commons.jxpath.ri.QName;
import org.apache.commons.jxpath.ri.model.VariablePointer;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;

/**
 * Contains tests for the {@link BasicNodeSet} class.
 * This class focuses on improving the understandability of a specific test case.
 */
public class BasicNodeSetTest {

    /**
     * Tests that adding a BasicNodeSet to itself does not result in
     * duplicate pointers or an infinite loop. The operation should be idempotent
     * when the set already contains all its own pointers.
     */
    @Test
    public void addNodeSetToItselfShouldNotCreateDuplicates() {
        // Arrange: Create a node set and add a single pointer to it.
        BasicNodeSet nodeSet = new BasicNodeSet();
        QName qName = new QName("testVar");
        BasicVariables variables = new BasicVariables();
        Pointer pointer = new VariablePointer(variables, qName);

        nodeSet.add(pointer);
        assertEquals("Pre-condition: Node set should contain one pointer", 1, nodeSet.getPointers().size());

        // Act: Add the node set to itself.
        nodeSet.add(nodeSet);

        // Assert: The size of the node set should remain 1, as the pointer
        // was already present and duplicates are not allowed.
        List<Pointer> pointers = nodeSet.getPointers();
        assertEquals("Node set size should not change after self-addition", 1, pointers.size());
        assertSame("The original pointer should still be in the set", pointer, pointers.get(0));
    }
}