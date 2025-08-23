package com.google.common.graph;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import com.google.common.collect.UnmodifiableIterator;
import java.util.Locale;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class EndpointPair_ESTestTest1 extends EndpointPair_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test00() throws Throwable {
        NetworkBuilder<Object, Object> networkBuilder0 = NetworkBuilder.undirected();
        MutableNetwork<Integer, Locale.Category> mutableNetwork0 = networkBuilder0.build();
        Locale.Category locale_Category0 = Locale.Category.FORMAT;
        EndpointPair<Object> endpointPair0 = EndpointPair.of((Network<?, ?>) mutableNetwork0, (Object) networkBuilder0, (Object) locale_Category0);
        assertNotNull(endpointPair0);
    }
}
