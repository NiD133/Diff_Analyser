package com.google.common.graph;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class ImmutableNetwork_ESTestTest6 extends ImmutableNetwork_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test5() throws Throwable {
        NetworkBuilder<Object, Object> networkBuilder0 = NetworkBuilder.undirected();
        StandardMutableNetwork<Integer, Integer> standardMutableNetwork0 = new StandardMutableNetwork<Integer, Integer>(networkBuilder0);
        Integer integer0 = new Integer((-405));
        NetworkBuilder<Integer, Integer> networkBuilder1 = NetworkBuilder.from((Network<Integer, Integer>) standardMutableNetwork0);
        ImmutableNetwork.Builder<Integer, Integer> immutableNetwork_Builder0 = networkBuilder1.immutable();
        Integer integer1 = new Integer((-1124));
        EndpointPair<Integer> endpointPair0 = EndpointPair.unordered(integer1, integer0);
        ImmutableNetwork.Builder<Integer, Integer> immutableNetwork_Builder1 = immutableNetwork_Builder0.addEdge(endpointPair0, integer1);
        ImmutableNetwork<Integer, Integer> immutableNetwork0 = immutableNetwork_Builder1.build();
        assertNotNull(immutableNetwork0);
    }
}
