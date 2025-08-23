package org.jfree.data.flow;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class NodeKey_ESTestTest6 extends NodeKey_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test05() throws Throwable {
        Integer integer0 = new Integer(3);
        NodeKey<Integer> nodeKey0 = new NodeKey<Integer>(2281, integer0);
        Object object0 = new Object();
        boolean boolean0 = nodeKey0.equals(object0);
        assertEquals(2281, nodeKey0.getStage());
        assertFalse(boolean0);
    }
}
