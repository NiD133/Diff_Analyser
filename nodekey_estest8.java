package org.jfree.data.flow;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class NodeKey_ESTestTest8 extends NodeKey_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test07() throws Throwable {
        Integer integer0 = new Integer(5721);
        NodeKey<Integer> nodeKey0 = new NodeKey<Integer>(5721, integer0);
        boolean boolean0 = nodeKey0.equals(nodeKey0);
        assertEquals(5721, nodeKey0.getStage());
        assertTrue(boolean0);
    }
}
