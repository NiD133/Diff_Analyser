package com.google.common.graph;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class ImmutableNetwork_ESTestTest9 extends ImmutableNetwork_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test8() throws Throwable {
        NetworkBuilder<Object, Object> networkBuilder0 = NetworkBuilder.directed();
        StandardNetwork<Integer, Integer> standardNetwork0 = new StandardNetwork<Integer, Integer>(networkBuilder0);
        ImmutableNetwork<Integer, Integer> immutableNetwork0 = ImmutableNetwork.copyOf((Network<Integer, Integer>) standardNetwork0);
        ImmutableGraph<Integer> immutableGraph0 = immutableNetwork0.asGraph();
        assertNotNull(immutableGraph0);
    }
}
