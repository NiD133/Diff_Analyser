package org.apache.commons.jxpath;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import java.util.List;
import org.apache.commons.jxpath.BasicNodeSet;
import org.apache.commons.jxpath.BasicVariables;
import org.apache.commons.jxpath.NodeSet;
import org.apache.commons.jxpath.Pointer;
import org.apache.commons.jxpath.ri.QName;
import org.apache.commons.jxpath.ri.model.VariablePointer;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

/**
 * Test suite for the BasicNodeSet class.
 */
@RunWith(EvoRunner.class)
@EvoRunnerParameters(mockJVMNonDeterminism = true, useVFS = true, useVNET = true, resetStaticState = true, separateClassLoader = true)
public class BasicNodeSet_ESTest extends BasicNodeSet_ESTest_scaffolding {

    /**
     * Test adding a VariablePointer with an invalid QName and expecting an IllegalArgumentException.
     */
    @Test(timeout = 4000)
    public void testAddInvalidVariablePointer() throws Throwable {
        BasicVariables variables = new BasicVariables();
        QName invalidQName = new QName("5>?Fr~\"n B, <j");
        VariablePointer pointer = new VariablePointer(variables, invalidQName);
        pointer.setIndex(1805);

        BasicNodeSet nodeSet = new BasicNodeSet();
        nodeSet.add(pointer);

        try {
            nodeSet.toString();
            fail("Expecting exception: IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            verifyException("org.apache.commons.jxpath.BasicVariables", e);
        }
    }

    /**
     * Test adding a VariablePointer with a null QName and expecting a RuntimeException.
     */
    @Test(timeout = 4000)
    public void testAddNullQNameVariablePointer() throws Throwable {
        BasicNodeSet nodeSet = new BasicNodeSet();
        VariablePointer pointer = new VariablePointer((QName) null);
        nodeSet.add(pointer);

        try {
            nodeSet.getValues();
            fail("Expecting exception: RuntimeException");
        } catch (RuntimeException e) {
            verifyException("org.apache.commons.jxpath.ri.model.VariablePointer$1", e);
        }
    }

    /**
     * Test adding a null Pointer and expecting a NullPointerException.
     */
    @Test(timeout = 4000)
    public void testAddNullPointer() throws Throwable {
        BasicNodeSet nodeSet = new BasicNodeSet();
        nodeSet.add((Pointer) null);

        try {
            nodeSet.getValues();
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            verifyException("java.util.stream.ReferencePipeline$3$1", e);
        }
    }

    /**
     * Test adding a VariablePointer with an invalid QName and expecting an IllegalArgumentException.
     */
    @Test(timeout = 4000)
    public void testAddInvalidQNameVariablePointer() throws Throwable {
        BasicNodeSet nodeSet = new BasicNodeSet();
        BasicVariables variables = new BasicVariables();
        QName invalidQName = new QName("Y2:%V(;/u");
        VariablePointer pointer = new VariablePointer(variables, invalidQName);
        nodeSet.add(pointer);

        try {
            nodeSet.getValues();
            fail("Expecting exception: IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            verifyException("org.apache.commons.jxpath.BasicVariables", e);
        }
    }

    /**
     * Test adding a VariablePointer with a null QName and expecting a RuntimeException.
     */
    @Test(timeout = 4000)
    public void testGetNodesWithNullQNameVariablePointer() throws Throwable {
        BasicNodeSet nodeSet = new BasicNodeSet();
        VariablePointer pointer = new VariablePointer((QName) null);
        nodeSet.add(pointer);

        try {
            nodeSet.getNodes();
            fail("Expecting exception: RuntimeException");
        } catch (RuntimeException e) {
            verifyException("org.apache.commons.jxpath.ri.model.VariablePointer$1", e);
        }
    }

    /**
     * Test adding a null Pointer and expecting a NullPointerException.
     */
    @Test(timeout = 4000)
    public void testGetNodesWithNullPointer() throws Throwable {
        BasicNodeSet nodeSet = new BasicNodeSet();
        nodeSet.add((Pointer) null);

        try {
            nodeSet.getNodes();
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            verifyException("java.util.stream.ReferencePipeline$3$1", e);
        }
    }

    /**
     * Test adding a VariablePointer with an empty QName and expecting an IllegalArgumentException.
     */
    @Test(timeout = 4000)
    public void testAddEmptyQNameVariablePointer() throws Throwable {
        BasicNodeSet nodeSet = new BasicNodeSet();
        BasicVariables variables = new BasicVariables();
        QName emptyQName = new QName("", "");
        VariablePointer pointer = new VariablePointer(variables, emptyQName);
        nodeSet.add(pointer);

        try {
            nodeSet.getNodes();
            fail("Expecting exception: IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            verifyException("org.apache.commons.jxpath.BasicVariables", e);
        }
    }

    /**
     * Test adding a null NodeSet and expecting a NullPointerException.
     */
    @Test(timeout = 4000)
    public void testAddNullNodeSet() throws Throwable {
        BasicNodeSet nodeSet = new BasicNodeSet();

        try {
            nodeSet.add((NodeSet) null);
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            verifyException("org.apache.commons.jxpath.BasicNodeSet", e);
        }
    }

    /**
     * Test removing a null Pointer.
     */
    @Test(timeout = 4000)
    public void testRemoveNullPointer() throws Throwable {
        BasicNodeSet nodeSet = new BasicNodeSet();
        nodeSet.remove((Pointer) null);
    }

    /**
     * Test getting values from an empty NodeSet.
     */
    @Test(timeout = 4000)
    public void testGetValuesFromEmptyNodeSet() throws Throwable {
        BasicNodeSet nodeSet = new BasicNodeSet();
        List<?> values = nodeSet.getValues();
        assertEquals(0, values.size());
    }

    /**
     * Test getting pointers from an empty NodeSet.
     */
    @Test(timeout = 4000)
    public void testGetPointersFromEmptyNodeSet() throws Throwable {
        BasicNodeSet nodeSet = new BasicNodeSet();
        List<Pointer> pointers = nodeSet.getPointers();
        assertSame(pointers, nodeSet.getPointers());
    }

    /**
     * Test getting nodes from an empty NodeSet.
     */
    @Test(timeout = 4000)
    public void testGetNodesFromEmptyNodeSet() throws Throwable {
        BasicNodeSet nodeSet = new BasicNodeSet();
        List<?> nodes = nodeSet.getNodes();
        assertTrue(nodes.isEmpty());
    }

    /**
     * Test adding a self-referencing NodeSet.
     */
    @Test(timeout = 4000)
    public void testAddSelfReferencingNodeSet() throws Throwable {
        BasicNodeSet nodeSet = new BasicNodeSet();
        QName qName = new QName("D@-&EQH");
        BasicVariables variables = new BasicVariables();
        VariablePointer pointer = new VariablePointer(variables, qName);
        nodeSet.add(pointer);
        nodeSet.add(nodeSet);
    }

    /**
     * Test adding a self-referencing NodeSet to itself.
     */
    @Test(timeout = 4000)
    public void testAddNodeSetToItself() throws Throwable {
        BasicNodeSet nodeSet = new BasicNodeSet();
        nodeSet.add(nodeSet);
    }

    /**
     * Test the string representation of an empty NodeSet.
     */
    @Test(timeout = 4000)
    public void testToStringEmptyNodeSet() throws Throwable {
        BasicNodeSet nodeSet = new BasicNodeSet();
        String stringRepresentation = nodeSet.toString();
        assertEquals("[]", stringRepresentation);
    }

    /**
     * Test adding and removing a null Pointer.
     */
    @Test(timeout = 4000)
    public void testAddAndRemoveNullPointer() throws Throwable {
        BasicNodeSet nodeSet = new BasicNodeSet();
        nodeSet.add((Pointer) null);
        nodeSet.remove((Pointer) null);
    }
}