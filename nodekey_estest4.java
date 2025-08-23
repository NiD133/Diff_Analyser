package org.jfree.data.flow;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class NodeKey_ESTestTest4 extends NodeKey_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test03() throws Throwable {
        NodeKey<Integer> nodeKey0 = null;
        try {
            nodeKey0 = new NodeKey<Integer>(617, (Integer) null);
            fail("Expecting exception: IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            //
            // Null 'node' argument.
            //
            verifyException("org.jfree.chart.internal.Args", e);
        }
    }
}
