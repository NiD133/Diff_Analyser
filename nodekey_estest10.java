package org.jfree.data.flow;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class NodeKey_ESTestTest10 extends NodeKey_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test09() throws Throwable {
        Integer integer0 = new Integer(3);
        NodeKey<Integer> nodeKey0 = new NodeKey<Integer>(2281, integer0);
        String string0 = nodeKey0.toString();
        assertEquals("[NodeKey: 2281, 3]", string0);
    }
}
