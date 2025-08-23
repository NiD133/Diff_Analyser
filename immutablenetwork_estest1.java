package com.google.common.graph;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class ImmutableNetwork_ESTestTest1 extends ImmutableNetwork_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test0() throws Throwable {
        NetworkBuilder<Object, Object> networkBuilder0 = NetworkBuilder.directed();
        StandardNetwork<Integer, Integer> standardNetwork0 = new StandardNetwork<Integer, Integer>(networkBuilder0);
        NetworkBuilder<Integer, Integer> networkBuilder1 = NetworkBuilder.from((Network<Integer, Integer>) standardNetwork0);
        ImmutableNetwork.Builder<Integer, Integer> immutableNetwork_Builder0 = new ImmutableNetwork.Builder<Integer, Integer>(networkBuilder1);
        Integer integer0 = new Integer(1029);
        Integer integer1 = new Integer(1);
        ImmutableNetwork.Builder<Integer, Integer> immutableNetwork_Builder1 = immutableNetwork_Builder0.addEdge(integer0, integer1, integer1);
        assertNotNull(immutableNetwork_Builder1);
    }
}
