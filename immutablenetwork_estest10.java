package com.google.common.graph;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class ImmutableNetwork_ESTestTest10 extends ImmutableNetwork_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test9() throws Throwable {
        NetworkBuilder<Object, Object> networkBuilder0 = NetworkBuilder.directed();
        StandardNetwork<Integer, Integer> standardNetwork0 = new StandardNetwork<Integer, Integer>(networkBuilder0);
        NetworkBuilder<Integer, Integer> networkBuilder1 = NetworkBuilder.from((Network<Integer, Integer>) standardNetwork0);
        ImmutableNetwork.Builder<Integer, Integer> immutableNetwork_Builder0 = networkBuilder1.immutable();
        Integer integer0 = new Integer((-967));
        ImmutableNetwork.Builder<Integer, Integer> immutableNetwork_Builder1 = immutableNetwork_Builder0.addNode(integer0);
        ImmutableNetwork<Integer, Integer> immutableNetwork0 = immutableNetwork_Builder1.build();
        assertNotNull(immutableNetwork0);
    }
}
