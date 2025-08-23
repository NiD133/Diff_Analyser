package org.jfree.data.flow;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class NodeKey_ESTestTest9 extends NodeKey_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test08() throws Throwable {
        Integer integer0 = new Integer(3);
        NodeKey<Integer> nodeKey0 = new NodeKey<Integer>(2281, integer0);
        NodeKey<Integer> nodeKey1 = new NodeKey<Integer>(2725, integer0);
        boolean boolean0 = nodeKey0.equals(nodeKey1);
        assertEquals(2725, nodeKey1.getStage());
        assertFalse(nodeKey1.equals((Object) nodeKey0));
        assertFalse(boolean0);
    }
}
