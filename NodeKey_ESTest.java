package org.jfree.data.flow;

import org.junit.Test;
import static org.junit.Assert.*;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

@RunWith(EvoRunner.class)
@EvoRunnerParameters(
    mockJVMNonDeterminism = true,
    useVFS = true,
    useVNET = true,
    resetStaticState = true,
    separateClassLoader = true
)
public class NodeKeyTest extends NodeKey_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void testNodeKeyEqualityWithDifferentStages() {
        Integer nodeValue = 0;
        NodeKey<Integer> nodeKey1 = new NodeKey<>(-1934, nodeValue);
        NodeKey<Integer> nodeKey2 = new NodeKey<>(-3799, nodeValue);
        NodeKey<Integer> clonedNodeKey = (NodeKey<Integer>) nodeKey2.clone();

        assertFalse(nodeKey1.equals(clonedNodeKey));
        assertEquals(-3799, clonedNodeKey.getStage());
        assertFalse(nodeKey2.equals(nodeKey1));
    }

    @Test(timeout = 4000)
    public void testNodeKeyStageValueZero() {
        Integer nodeValue = 0;
        NodeKey<Integer> nodeKey = new NodeKey<>(0, nodeValue);

        assertEquals(0, nodeKey.getStage());
    }

    @Test(timeout = 4000)
    public void testNodeKeyStageValueNegative() {
        Integer nodeValue = 0;
        NodeKey<Integer> nodeKey = new NodeKey<>(-1934, nodeValue);

        assertEquals(-1934, nodeKey.getStage());
    }

    @Test(timeout = 4000)
    public void testNodeKeyCreationWithNullNodeThrowsException() {
        try {
            new NodeKey<>(617, null);
            fail("Expecting exception: IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            verifyException("org.jfree.chart.internal.Args", e);
        }
    }

    @Test(timeout = 4000)
    public void testNodeKeyEqualityWithDifferentNodeValues() {
        Integer nodeValue1 = 3;
        NodeKey<Integer> nodeKey1 = new NodeKey<>(2281, nodeValue1);
        Integer nodeValue2 = 2281;
        NodeKey<Integer> nodeKey2 = new NodeKey<>(2281, nodeValue2);
        NodeKey<Integer> clonedNodeKey = (NodeKey<Integer>) nodeKey2.clone();

        assertFalse(nodeKey1.equals(clonedNodeKey));
        assertEquals(2281, clonedNodeKey.getStage());
    }

    @Test(timeout = 4000)
    public void testNodeKeyInequalityWithDifferentObject() {
        Integer nodeValue = 3;
        NodeKey<Integer> nodeKey = new NodeKey<>(2281, nodeValue);
        Object differentObject = new Object();

        assertFalse(nodeKey.equals(differentObject));
        assertEquals(2281, nodeKey.getStage());
    }

    @Test(timeout = 4000)
    public void testNodeKeyInequalityWithNull() {
        Integer nodeValue = 504;
        NodeKey<Integer> nodeKey = new NodeKey<>(504, nodeValue);

        assertFalse(nodeKey.equals(null));
        assertEquals(504, nodeKey.getStage());
    }

    @Test(timeout = 4000)
    public void testNodeKeyEqualityWithItself() {
        Integer nodeValue = 5721;
        NodeKey<Integer> nodeKey = new NodeKey<>(5721, nodeValue);

        assertTrue(nodeKey.equals(nodeKey));
        assertEquals(5721, nodeKey.getStage());
    }

    @Test(timeout = 4000)
    public void testNodeKeyEqualityWithDifferentStagesAndSameNode() {
        Integer nodeValue = 3;
        NodeKey<Integer> nodeKey1 = new NodeKey<>(2281, nodeValue);
        NodeKey<Integer> nodeKey2 = new NodeKey<>(2725, nodeValue);

        assertFalse(nodeKey1.equals(nodeKey2));
        assertEquals(2725, nodeKey2.getStage());
        assertFalse(nodeKey2.equals(nodeKey1));
    }

    @Test(timeout = 4000)
    public void testNodeKeyToStringRepresentation() {
        Integer nodeValue = 3;
        NodeKey<Integer> nodeKey = new NodeKey<>(2281, nodeValue);

        assertEquals("[NodeKey: 2281, 3]", nodeKey.toString());
    }

    @Test(timeout = 4000)
    public void testNodeKeyCloneEquality() {
        Integer nodeValue = 3;
        NodeKey<Integer> nodeKey = new NodeKey<>(2281, nodeValue);
        NodeKey<Integer> clonedNodeKey = (NodeKey<Integer>) nodeKey.clone();

        assertTrue(nodeKey.equals(clonedNodeKey));
        assertEquals(2281, clonedNodeKey.getStage());
    }

    @Test(timeout = 4000)
    public void testNodeKeyGetNode() {
        Integer nodeValue = 504;
        NodeKey<Integer> nodeKey = new NodeKey<>(504, nodeValue);

        assertEquals(504, nodeKey.getStage());
        assertEquals(nodeValue, nodeKey.getNode());
    }

    @Test(timeout = 4000)
    public void testNodeKeyHashCode() {
        Integer nodeValue = 9;
        NodeKey<Integer> nodeKey = new NodeKey<>(9, nodeValue);

        nodeKey.hashCode(); // Just to ensure it doesn't throw
        assertEquals(9, nodeKey.getStage());
    }

    @Test(timeout = 4000)
    public void testNodeKeyStageValue() {
        Integer nodeValue = 3;
        NodeKey<Integer> nodeKey = new NodeKey<>(2281, nodeValue);

        assertEquals(2281, nodeKey.getStage());
    }
}