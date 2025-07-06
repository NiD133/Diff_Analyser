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
@EvoRunnerParameters(
    mockJVMNonDeterminism = true,
    useVFS = true,
    useVNET = true,
    resetStaticState = true,
    separateClassLoader = true
)
public class BasicNodeSet_ESTest extends BasicNodeSet_ESTest_scaffolding {

    /**
     * Test adding a VariablePointer with a null QName and expecting a RuntimeException when getting values.
     */
    @Test(timeout = 4000)
    public void testAddNullQNameVariablePointerAndGetValues() throws Throwable {
        BasicNodeSet nodeSet = new BasicNodeSet();
        VariablePointer variablePointer = new VariablePointer((QName) null);
        nodeSet.add((Pointer) variablePointer);

        try {
            nodeSet.getValues();
            fail("Expecting exception: RuntimeException");
        } catch (RuntimeException e) {
            verifyException("org.apache.commons.jxpath.ri.model.VariablePointer$1", e);
        }
    }

    /**
     * Test adding a null Pointer and expecting a NullPointerException when getting values.
     */
    @Test(timeout = 4000)
    public void testAddNullPointerAndGetValues() throws Throwable {
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
     * Test adding a VariablePointer with an empty QName and expecting an IllegalArgumentException when getting values.
     */
    @Test(timeout = 4000)
    public void testAddEmptyQNameVariablePointerAndGetValues() throws Throwable {
        BasicNodeSet nodeSet = new BasicNodeSet();
        BasicVariables variables = new BasicVariables();
        QName emptyQName = new QName("");
        VariablePointer variablePointer = new VariablePointer(variables, emptyQName);
        nodeSet.add((Pointer) variablePointer);

        try {
            nodeSet.getValues();
            fail("Expecting exception: IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            verifyException("org.apache.commons.jxpath.BasicVariables", e);
        }
    }

    /**
     * Test adding a VariablePointer with a null QName and expecting a RuntimeException when getting nodes.
     */
    @Test(timeout = 4000)
    public void testAddNullQNameVariablePointerAndGetNodes() throws Throwable {
        BasicNodeSet nodeSet = new BasicNodeSet();
        VariablePointer variablePointer = new VariablePointer((QName) null);
        nodeSet.add((Pointer) variablePointer);

        try {
            nodeSet.getNodes();
            fail("Expecting exception: RuntimeException");
        } catch (RuntimeException e) {
            verifyException("org.apache.commons.jxpath.ri.model.VariablePointer$1", e);
        }
    }

    /**
     * Test adding a null Pointer and expecting a NullPointerException when getting nodes.
     */
    @Test(timeout = 4000)
    public void testAddNullPointerAndGetNodes() throws Throwable {
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
     * Test adding a VariablePointer with an empty QName and expecting an IllegalArgumentException when getting nodes.
     */
    @Test(timeout = 4000)
    public void testAddEmptyQNameVariablePointerAndGetNodes() throws Throwable {
        BasicNodeSet nodeSet = new BasicNodeSet();
        BasicVariables variables = new BasicVariables();
        QName emptyQName = new QName("");
        VariablePointer variablePointer = new VariablePointer(variables, emptyQName);
        nodeSet.add((Pointer) variablePointer);

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
     * Test adding and removing a VariablePointer with an empty QName.
     */
    @Test(timeout = 4000)
    public void testAddAndRemoveVariablePointer() throws Throwable {
        BasicNodeSet nodeSet = new BasicNodeSet();
        QName emptyQName = new QName("");
        VariablePointer variablePointer = new VariablePointer(emptyQName);
        nodeSet.add((Pointer) variablePointer);
        nodeSet.remove(variablePointer);

        assertNull(variablePointer.getNamespaceURI());
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

        assertTrue(values.isEmpty());
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

        assertSame(nodes, nodeSet.getNodes());
    }

    /**
     * Test adding a NodeSet to itself.
     */
    @Test(timeout = 4000)
    public void testAddNodeSetToItself() throws Throwable {
        BasicNodeSet nodeSet = new BasicNodeSet();
        nodeSet.add((NodeSet) nodeSet);
    }

    /**
     * Test the string representation of an empty NodeSet.
     */
    @Test(timeout = 4000)
    public void testToStringOnEmptyNodeSet() throws Throwable {
        BasicNodeSet nodeSet = new BasicNodeSet();
        String nodeSetString = nodeSet.toString();

        assertEquals("[]", nodeSetString);
    }

    /**
     * Test adding a null Pointer and then adding the NodeSet to itself.
     */
    @Test(timeout = 4000)
    public void testAddNullPointerAndNodeSetToItself() throws Throwable {
        BasicNodeSet nodeSet = new BasicNodeSet();
        nodeSet.add((Pointer) null);
        nodeSet.add((NodeSet) nodeSet);
    }
}