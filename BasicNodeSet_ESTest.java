package org.apache.commons.jxpath;

import org.junit.Test;
import static org.junit.Assert.*;
import java.util.List;
import org.apache.commons.jxpath.BasicNodeSet;
import org.apache.commons.jxpath.BasicVariables;
import org.apache.commons.jxpath.NodeSet;
import org.apache.commons.jxpath.Pointer;
import org.apache.commons.jxpath.ri.QName;
import org.apache.commons.jxpath.ri.model.VariablePointer;

public class BasicNodeSetTest {

    // Test cases for successful operations
    
    @Test
    public void shouldReturnEmptyListWhenNodeSetIsEmpty() {
        BasicNodeSet nodeSet = new BasicNodeSet();
        
        List values = nodeSet.getValues();
        List nodes = nodeSet.getNodes();
        List<Pointer> pointers = nodeSet.getPointers();
        
        assertEquals(0, values.size());
        assertTrue(nodes.isEmpty());
        assertEquals(0, pointers.size());
    }
    
    @Test
    public void shouldReturnEmptyStringWhenNodeSetIsEmpty() {
        BasicNodeSet nodeSet = new BasicNodeSet();
        
        String result = nodeSet.toString();
        
        assertEquals("[]", result);
    }
    
    @Test
    public void shouldReturnSamePointersListOnMultipleCalls() {
        BasicNodeSet nodeSet = new BasicNodeSet();
        
        List<Pointer> firstCall = nodeSet.getPointers();
        List<Pointer> secondCall = nodeSet.getPointers();
        
        assertSame(firstCall, secondCall);
    }
    
    @Test
    public void shouldAllowAddingEmptyNodeSetToEmptyNodeSet() {
        BasicNodeSet nodeSet = new BasicNodeSet();
        BasicNodeSet emptyNodeSet = new BasicNodeSet();
        
        nodeSet.add(emptyNodeSet);
        
        // Should complete without exception
        assertTrue(nodeSet.getPointers().isEmpty());
    }
    
    @Test
    public void shouldAllowAddingNodeSetWithPointerToNodeSet() {
        BasicNodeSet nodeSet = new BasicNodeSet();
        BasicNodeSet nodeSetWithPointer = createNodeSetWithValidPointer();
        
        nodeSet.add(nodeSetWithPointer);
        
        // Should complete without exception
        assertFalse(nodeSet.getPointers().isEmpty());
    }
    
    @Test
    public void shouldAllowAddingNodeSetToItself() {
        BasicNodeSet nodeSet = new BasicNodeSet();
        
        nodeSet.add(nodeSet);
        
        // Should complete without exception - self-reference handling
        assertTrue(nodeSet.getPointers().isEmpty());
    }
    
    @Test
    public void shouldAllowRemovingNullPointer() {
        BasicNodeSet nodeSet = new BasicNodeSet();
        
        nodeSet.remove(null);
        
        // Should complete without exception
        assertTrue(nodeSet.getPointers().isEmpty());
    }
    
    @Test
    public void shouldAllowAddingAndRemovingNullPointer() {
        BasicNodeSet nodeSet = new BasicNodeSet();
        
        nodeSet.add((Pointer) null);
        nodeSet.remove(null);
        
        // Should complete without exception
    }

    // Test cases for exception scenarios
    
    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowExceptionWhenToStringCalledWithUndefinedVariable() {
        BasicNodeSet nodeSet = new BasicNodeSet();
        VariablePointer invalidPointer = createPointerWithUndefinedVariable();
        nodeSet.add(invalidPointer);
        
        nodeSet.toString();
    }
    
    @Test(expected = RuntimeException.class)
    public void shouldThrowExceptionWhenGetValuesCalledWithNullVariableName() {
        BasicNodeSet nodeSet = new BasicNodeSet();
        VariablePointer nullNamePointer = new VariablePointer((QName) null);
        nodeSet.add(nullNamePointer);
        
        nodeSet.getValues();
    }
    
    @Test(expected = NullPointerException.class)
    public void shouldThrowExceptionWhenGetValuesCalledWithNullPointer() {
        BasicNodeSet nodeSet = new BasicNodeSet();
        nodeSet.add((Pointer) null);
        
        nodeSet.getValues();
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowExceptionWhenGetValuesCalledWithUndefinedVariable() {
        BasicNodeSet nodeSet = new BasicNodeSet();
        VariablePointer undefinedPointer = createPointerWithUndefinedVariable();
        nodeSet.add(undefinedPointer);
        
        nodeSet.getValues();
    }
    
    @Test(expected = RuntimeException.class)
    public void shouldThrowExceptionWhenGetNodesCalledWithNullVariableName() {
        BasicNodeSet nodeSet = new BasicNodeSet();
        VariablePointer nullNamePointer = new VariablePointer((QName) null);
        nodeSet.add(nullNamePointer);
        
        nodeSet.getNodes();
    }
    
    @Test(expected = NullPointerException.class)
    public void shouldThrowExceptionWhenGetNodesCalledWithNullPointer() {
        BasicNodeSet nodeSet = new BasicNodeSet();
        nodeSet.add((Pointer) null);
        
        nodeSet.getNodes();
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowExceptionWhenGetNodesCalledWithUndefinedVariable() {
        BasicNodeSet nodeSet = new BasicNodeSet();
        VariablePointer undefinedPointer = createPointerWithEmptyVariable();
        nodeSet.add(undefinedPointer);
        
        nodeSet.getNodes();
    }
    
    @Test(expected = NullPointerException.class)
    public void shouldThrowExceptionWhenAddingNullNodeSet() {
        BasicNodeSet nodeSet = new BasicNodeSet();
        
        nodeSet.add((NodeSet) null);
    }

    // Helper methods for creating test objects
    
    private BasicNodeSet createNodeSetWithValidPointer() {
        BasicNodeSet nodeSet = new BasicNodeSet();
        BasicVariables variables = new BasicVariables();
        QName variableName = new QName("testVariable");
        VariablePointer pointer = new VariablePointer(variables, variableName);
        nodeSet.add(pointer);
        return nodeSet;
    }
    
    private VariablePointer createPointerWithUndefinedVariable() {
        BasicVariables variables = new BasicVariables();
        QName undefinedVariable = new QName("undefinedVariable");
        VariablePointer pointer = new VariablePointer(variables, undefinedVariable);
        pointer.setIndex(1805); // Set index to trigger toString behavior
        return pointer;
    }
    
    private VariablePointer createPointerWithEmptyVariable() {
        BasicVariables variables = new BasicVariables();
        QName emptyVariable = new QName("", "");
        return new VariablePointer(variables, emptyVariable);
    }
}