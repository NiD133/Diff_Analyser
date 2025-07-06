package org.apache.commons.jxpath;

import org.apache.commons.jxpath.ri.QName;
import org.apache.commons.jxpath.ri.model.VariablePointer;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

public class BasicNodeSetTest {

    private BasicNodeSet nodeSet;

    @Before
    public void setUp() {
        nodeSet = new BasicNodeSet();
    }

    @Test
    public void testGetValuesThrowsRuntimeExceptionForUndefinedVariable() {
        VariablePointer variablePointer = new VariablePointer((QName) null);
        nodeSet.add(variablePointer);

        try {
            nodeSet.getValues();
            fail("Expected RuntimeException due to undefined variable");
        } catch (RuntimeException e) {
            assertEquals("Undefined variable: null", e.getMessage());
        }
    }

    @Test
    public void testGetValuesThrowsNullPointerExceptionForNullPointer() {
        nodeSet.add((Pointer) null);

        try {
            nodeSet.getValues();
            fail("Expected NullPointerException");
        } catch (NullPointerException e) {
            // Expected
        }
    }

    @Test
    public void testGetValuesThrowsIllegalArgumentExceptionForNonExistentVariable() {
        BasicVariables basicVariables = new BasicVariables();
        QName qName = new QName("");
        VariablePointer variablePointer = new VariablePointer(basicVariables, qName);
        nodeSet.add(variablePointer);

        try {
            nodeSet.getValues();
            fail("Expected IllegalArgumentException due to non-existent variable");
        } catch (IllegalArgumentException e) {
            assertEquals("No such variable: ''", e.getMessage());
        }
    }

    @Test
    public void testGetNodesThrowsRuntimeExceptionForUndefinedVariable() {
        VariablePointer variablePointer = new VariablePointer((QName) null);
        nodeSet.add(variablePointer);

        try {
            nodeSet.getNodes();
            fail("Expected RuntimeException due to undefined variable");
        } catch (RuntimeException e) {
            assertEquals("Undefined variable: null", e.getMessage());
        }
    }

    @Test
    public void testGetNodesThrowsNullPointerExceptionForNullPointer() {
        nodeSet.add((Pointer) null);

        try {
            nodeSet.getNodes();
            fail("Expected NullPointerException");
        } catch (NullPointerException e) {
            // Expected
        }
    }

    @Test
    public void testGetNodesThrowsIllegalArgumentExceptionForNonExistentVariable() {
        BasicVariables basicVariables = new BasicVariables();
        QName qName = new QName("");
        VariablePointer variablePointer = new VariablePointer(basicVariables, qName);
        nodeSet.add(variablePointer);

        try {
            nodeSet.getNodes();
            fail("Expected IllegalArgumentException due to non-existent variable");
        } catch (IllegalArgumentException e) {
            assertEquals("No such variable: ''", e.getMessage());
        }
    }

    @Test(expected = NullPointerException.class)
    public void testAddNullNodeSetThrowsNullPointerException() {
        nodeSet.add((NodeSet) null);
    }

    @Test
    public void testRemoveExistingPointer() {
        QName qName = new QName("");
        VariablePointer variablePointer = new VariablePointer(qName);
        nodeSet.add(variablePointer);
        nodeSet.remove(variablePointer);
        assertEquals(null, variablePointer.getNamespaceURI());
    }

    @Test
    public void testRemoveNullPointerDoesNotThrowException() {
        nodeSet.remove((Pointer) null);
    }

    @Test
    public void testGetValuesReturnsEmptyListForEmptyNodeSet() {
        List<?> values = nodeSet.getValues();
        assertTrue(values.isEmpty());
    }

    @Test
    public void testGetPointersReturnsSameListInstance() {
        List<Pointer> pointers1 = nodeSet.getPointers();
        List<Pointer> pointers2 = nodeSet.getPointers();
        assertSame(pointers1, pointers2);
    }

    @Test
    public void testGetNodesReturnsSameListInstance() {
        List<?> nodes1 = nodeSet.getNodes();
        List<?> nodes2 = nodeSet.getNodes();
        assertSame(nodes1, nodes2);
    }

    @Test
    public void testAddItself() {
        nodeSet.add(nodeSet);
    }

    @Test
    public void testToStringReturnsCorrectRepresentation() {
        assertEquals("[]", nodeSet.toString());
    }

    @Test
    public void testAddNullPointerThenAddItself() {
        nodeSet.add((Pointer) null);
        nodeSet.add(nodeSet);
    }
}