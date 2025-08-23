package com.google.common.graph;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class ImmutableNetwork_ESTestTest4 extends ImmutableNetwork_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test3() throws Throwable {
        NetworkBuilder<Object, Object> networkBuilder0 = NetworkBuilder.undirected();
        StandardNetwork<Integer, Comparable<Integer>> standardNetwork0 = new StandardNetwork<Integer, Comparable<Integer>>(networkBuilder0);
        NetworkBuilder<Integer, Comparable<Integer>> networkBuilder1 = NetworkBuilder.from((Network<Integer, Comparable<Integer>>) standardNetwork0);
        networkBuilder1.allowsParallelEdges(true);
        StandardNetwork<Integer, Integer> standardNetwork1 = new StandardNetwork<Integer, Integer>(networkBuilder1);
        ImmutableNetwork<Integer, Integer> immutableNetwork0 = ImmutableNetwork.copyOf((Network<Integer, Integer>) standardNetwork1);
        NetworkBuilder<Integer, Integer> networkBuilder2 = NetworkBuilder.from((Network<Integer, Integer>) immutableNetwork0);
        ImmutableNetwork.Builder<Integer, Integer> immutableNetwork_Builder0 = networkBuilder2.immutable();
        Integer integer0 = new Integer(949);
        immutableNetwork_Builder0.addNode(integer0);
        ImmutableNetwork<Integer, Integer> immutableNetwork1 = immutableNetwork_Builder0.build();
        assertNotSame(immutableNetwork0, immutableNetwork1);
    }
}
