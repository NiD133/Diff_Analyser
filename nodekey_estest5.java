package org.jfree.data.flow;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class NodeKey_ESTestTest5 extends NodeKey_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test04() throws Throwable {
        Integer integer0 = new Integer(3);
        NodeKey<Integer> nodeKey0 = new NodeKey<Integer>(2281, integer0);
        Integer integer1 = new Integer(2281);
        NodeKey<Integer> nodeKey1 = new NodeKey<Integer>(2281, integer1);
        NodeKey nodeKey2 = (NodeKey) nodeKey1.clone();
        boolean boolean0 = nodeKey0.equals(nodeKey2);
        assertFalse(boolean0);
        assertEquals(2281, nodeKey2.getStage());
    }
}
