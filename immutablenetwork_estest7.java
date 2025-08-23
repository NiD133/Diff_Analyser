package com.google.common.graph;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class ImmutableNetwork_ESTestTest7 extends ImmutableNetwork_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test6() throws Throwable {
        NetworkBuilder<Object, Object> networkBuilder0 = NetworkBuilder.directed();
        StandardNetwork<Integer, Integer> standardNetwork0 = new StandardNetwork<Integer, Integer>(networkBuilder0);
        ImmutableNetwork<Integer, Integer> immutableNetwork0 = ImmutableNetwork.copyOf((Network<Integer, Integer>) standardNetwork0);
        ImmutableNetwork<Integer, Integer> immutableNetwork1 = ImmutableNetwork.copyOf((Network<Integer, Integer>) immutableNetwork0);
        assertSame(immutableNetwork0, immutableNetwork1);
    }
}
