package com.google.common.graph;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class ImmutableNetwork_ESTestTest5 extends ImmutableNetwork_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test4() throws Throwable {
        NetworkBuilder<Object, Object> networkBuilder0 = NetworkBuilder.directed();
        networkBuilder0.allowsParallelEdges(true);
        StandardNetwork<Integer, Integer> standardNetwork0 = new StandardNetwork<Integer, Integer>(networkBuilder0);
        ImmutableNetwork<Integer, Integer> immutableNetwork0 = ImmutableNetwork.copyOf((Network<Integer, Integer>) standardNetwork0);
        NetworkBuilder<Integer, Integer> networkBuilder1 = NetworkBuilder.from((Network<Integer, Integer>) immutableNetwork0);
        ImmutableNetwork.Builder<Integer, Integer> immutableNetwork_Builder0 = networkBuilder1.immutable();
        Integer integer0 = new Integer((-967));
        ImmutableNetwork.Builder<Integer, Integer> immutableNetwork_Builder1 = immutableNetwork_Builder0.addNode(integer0);
        ImmutableNetwork<Integer, Integer> immutableNetwork1 = immutableNetwork_Builder1.build();
        assertNotSame(immutableNetwork1, immutableNetwork0);
    }
}
