package com.google.common.graph;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import com.google.common.collect.UnmodifiableIterator;
import java.util.Locale;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class EndpointPair_ESTestTest5 extends EndpointPair_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test04() throws Throwable {
        Integer integer0 = new Integer((-1));
        NetworkBuilder<Object, Object> networkBuilder0 = NetworkBuilder.directed();
        StandardValueGraph<Object, Integer> standardValueGraph0 = new StandardValueGraph<Object, Integer>(networkBuilder0);
        ImmutableValueGraph<Object, Integer> immutableValueGraph0 = ImmutableValueGraph.copyOf((ValueGraph<Object, Integer>) standardValueGraph0);
        ImmutableGraph<Object> immutableGraph0 = new ImmutableGraph<Object>(immutableValueGraph0);
        EndpointPair<Object> endpointPair0 = EndpointPair.of((Graph<?>) immutableGraph0, (Object) integer0, (Object) standardValueGraph0);
        boolean boolean0 = endpointPair0.isOrdered();
        assertTrue(boolean0);
    }
}
