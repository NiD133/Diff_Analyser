package org.apache.commons.jxpath;

import org.apache.commons.jxpath.ri.QName;
import org.apache.commons.jxpath.ri.model.VariablePointer;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.*;

/**
 * Unit tests for {@link BasicNodeSet}.
 * This class focuses on verifying the contract of BasicNodeSet regarding its collection-like
 * behavior for Pointers and its handling of pointer resolution.
 */
public class BasicNodeSetTest {

    // --- Tests for an empty NodeSet ---

    @Test
    public void newSetIsEmpty() {
        // Arrange
        BasicNodeSet nodeSet = new BasicNodeSet();

        // Act & Assert
        assertTrue("Pointers list should be empty for a new set", nodeSet.getPointers().isEmpty());
        assertTrue("Nodes list should be empty for a new set", nodeSet.getNodes().isEmpty());
        assertTrue("Values list should be empty for a new set", nodeSet.getValues().isEmpty());
    }

    @Test
    public void toString_onEmptySet_returnsEmptyBrackets() {
        // Arrange
        BasicNodeSet nodeSet = new BasicNodeSet();

        // Act
        String result = nodeSet.toString();

        // Assert
        assertEquals("[]", result);
    }

    @Test
    public void getPointers_calledMultipleTimes_returnsSameListInstance() {
        // Arrange
        BasicNodeSet nodeSet = new BasicNodeSet();

        // Act
        List<Pointer> pointers1 = nodeSet.getPointers();
        List<Pointer> pointers2 = nodeSet.getPointers();

        // Assert
        // This tests the caching mechanism, ensuring the same read-only list is returned.
        assertSame("Multiple calls to getPointers should return the same list instance", pointers1, pointers2);
    }

    // --- Tests for add() and remove() functionality ---

    @Test
    public void addAndRemove_withNullPointer_leavesSetEmpty() {
        // Arrange
        BasicNodeSet nodeSet = new BasicNodeSet();

        // Act
        nodeSet.add((Pointer) null);
        nodeSet.remove((Pointer) null);

        // Assert
        assertTrue("Set should be empty after adding and removing a null pointer", nodeSet.getPointers().isEmpty());
    }

    @Test
    public void remove_withNullPointerFromNonEmptySet_doesNotThrowException() {
        // Arrange
        BasicNodeSet nodeSet = new BasicNodeSet();
        nodeSet.add(new VariablePointer(new QName("dummy")));

        // Act & Assert
        try {
            nodeSet.remove((Pointer) null);
        } catch (Exception e) {
            fail("Removing a null pointer should not throw an exception.");
        }
        assertEquals("Set size should not change when removing null", 1, nodeSet.getPointers().size());
    }

    @Test
    public void addNodeSet_withSelfOnEmptySet_remainsEmpty() {
        // Arrange
        BasicNodeSet nodeSet = new BasicNodeSet();

        // Act
        // Adding an empty set to itself should result in no change.
        nodeSet.add((NodeSet) nodeSet);

        // Assert
        assertTrue("Node set should remain empty", nodeSet.getPointers().isEmpty());
    }

    @Test
    public void addNodeSet_withSelf_addsAllPointers() {
        // Arrange
        BasicNodeSet nodeSet = new BasicNodeSet();
        Pointer pointer = new VariablePointer(new QName("dummy"));
        nodeSet.add(pointer);
        assertEquals("Initial size should be 1", 1, nodeSet.getPointers().size());

        // Act
        // Add the nodeSet to itself. This should double the pointers.
        nodeSet.add((NodeSet) nodeSet);

        // Assert
        assertEquals("Size should be 2 after adding the set to itself", 2, nodeSet.getPointers().size());
        assertSame("The first pointer should be the original pointer", pointer, nodeSet.getPointers().get(0));
        assertSame("The second pointer should also be the original pointer", pointer, nodeSet.getPointers().get(1));
    }

    // --- Exception handling tests ---

    @Test(expected = NullPointerException.class)
    public void addNodeSet_withNullArgument_throwsNullPointerException() {
        // Arrange
        BasicNodeSet nodeSet = new BasicNodeSet();

        // Act
        nodeSet.add((NodeSet) null); // This should throw
    }

    @Test
    public void getValues_withUnresolvedVariablePointer_throwsException() {
        // Arrange
        BasicVariables variables = new BasicVariables();
        Pointer unresolvedPointer = new VariablePointer(variables, new QName("unresolvedVar"));
        BasicNodeSet nodeSet = new BasicNodeSet();
        nodeSet.add(unresolvedPointer);

        // Act & Assert
        try {
            nodeSet.getValues();
            fail("Expected an IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            assertEquals("No such variable: 'unresolvedVar'", e.getMessage());
        }
    }

    @Test
    public void getNodes_withUnresolvedVariablePointer_throwsException() {
        // Arrange
        BasicVariables variables = new BasicVariables();
        Pointer unresolvedPointer = new VariablePointer(variables, new QName("unresolvedVar"));
        BasicNodeSet nodeSet = new BasicNodeSet();
        nodeSet.add(unresolvedPointer);

        // Act & Assert
        try {
            nodeSet.getNodes();
            fail("Expected an IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            assertEquals("No such variable: 'unresolvedVar'", e.getMessage());
        }
    }
    
    @Test
    public void toString_withUnresolvedVariablePointer_throwsException() {
        // Arrange
        BasicVariables variables = new BasicVariables();
        Pointer unresolvedPointer = new VariablePointer(variables, new QName("unresolvedVar"));
        BasicNodeSet nodeSet = new BasicNodeSet();
        nodeSet.add(unresolvedPointer);

        // Act & Assert
        try {
            nodeSet.toString();
            fail("Expected an IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            assertEquals("No such variable: 'unresolvedVar'", e.getMessage());
        }
    }

    @Test
    public void getValues_withNullQNameVariablePointer_throwsException() {
        // Arrange
        Pointer pointerWithNullQName = new VariablePointer((QName) null);
        BasicNodeSet nodeSet = new BasicNodeSet();
        nodeSet.add(pointerWithNullQName);

        // Act & Assert
        try {
            nodeSet.getValues();
            fail("Expected a RuntimeException to be thrown");
        } catch (RuntimeException e) {
            assertEquals("Undefined variable: null", e.getMessage());
        }
    }

    @Test
    public void getNodes_withNullQNameVariablePointer_throwsException() {
        // Arrange
        Pointer pointerWithNullQName = new VariablePointer((QName) null);
        BasicNodeSet nodeSet = new BasicNodeSet();
        nodeSet.add(pointerWithNullQName);

        // Act & Assert
        try {
            nodeSet.getNodes();
            fail("Expected a RuntimeException to be thrown");
        } catch (RuntimeException e) {
            assertEquals("Undefined variable: null", e.getMessage());
        }
    }

    @Test(expected = NullPointerException.class)
    public void getValues_whenSetContainsNullPointer_throwsNullPointerException() {
        // Arrange
        BasicNodeSet nodeSet = new BasicNodeSet();
        nodeSet.add((Pointer) null);

        // Act
        nodeSet.getValues(); // This should throw
    }

    @Test(expected = NullPointerException.class)
    public void getNodes_whenSetContainsNullPointer_throwsNullPointerException() {
        // Arrange
        BasicNodeSet nodeSet = new BasicNodeSet();
        nodeSet.add((Pointer) null);

        // Act
        nodeSet.getNodes(); // This should throw
    }
}