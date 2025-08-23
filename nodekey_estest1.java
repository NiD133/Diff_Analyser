package org.jfree.data.flow;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class NodeKey_ESTestTest1 extends NodeKey_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test00() throws Throwable {
        Integer integer0 = new Integer(0);
        NodeKey<Integer> nodeKey0 = new NodeKey<Integer>((-1934), integer0);
        NodeKey<Integer> nodeKey1 = new NodeKey<Integer>((-3799), integer0);
        NodeKey nodeKey2 = (NodeKey) nodeKey1.clone();
        boolean boolean0 = nodeKey0.equals(nodeKey2);
        assertFalse(boolean0);
        assertEquals((-3799), nodeKey2.getStage());
        assertFalse(nodeKey1.equals((Object) nodeKey0));
    }
}
