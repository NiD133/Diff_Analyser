package com.google.common.graph;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class ImmutableNetwork_ESTestTest8 extends ImmutableNetwork_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test7() throws Throwable {
        NetworkBuilder<Object, Object> networkBuilder0 = NetworkBuilder.directed();
        StandardMutableNetwork<Object, Object> standardMutableNetwork0 = new StandardMutableNetwork<Object, Object>(networkBuilder0);
        ImmutableNetwork<Object, Object> immutableNetwork0 = ImmutableNetwork.copyOf((Network<Object, Object>) standardMutableNetwork0);
        ImmutableNetwork<Object, Object> immutableNetwork1 = ImmutableNetwork.copyOf(immutableNetwork0);
        assertSame(immutableNetwork0, immutableNetwork1);
    }
}
