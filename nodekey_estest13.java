package org.jfree.data.flow;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class NodeKey_ESTestTest13 extends NodeKey_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test12() throws Throwable {
        Integer integer0 = new Integer(9);
        NodeKey<Integer> nodeKey0 = new NodeKey<Integer>(9, integer0);
        nodeKey0.hashCode();
        assertEquals(9, nodeKey0.getStage());
    }
}
