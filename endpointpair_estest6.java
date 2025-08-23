package com.google.common.graph;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import com.google.common.collect.UnmodifiableIterator;
import java.util.Locale;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class EndpointPair_ESTestTest6 extends EndpointPair_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test05() throws Throwable {
        GraphBuilder<Object> graphBuilder0 = GraphBuilder.undirected();
        StandardMutableGraph<Integer> standardMutableGraph0 = new StandardMutableGraph<Integer>(graphBuilder0);
        Integer integer0 = new Integer(0);
        EndpointPair<Integer> endpointPair0 = EndpointPair.of((Graph<?>) standardMutableGraph0, integer0, integer0);
        boolean boolean0 = endpointPair0.isOrdered();
        assertFalse(boolean0);
    }
}
