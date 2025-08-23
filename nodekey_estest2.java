package org.jfree.data.flow;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class NodeKey_ESTestTest2 extends NodeKey_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test01() throws Throwable {
        Integer integer0 = new Integer(0);
        NodeKey<Integer> nodeKey0 = new NodeKey<Integer>(0, integer0);
        int int0 = nodeKey0.getStage();
        assertEquals(0, int0);
    }
}
