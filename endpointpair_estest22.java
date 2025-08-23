package com.google.common.graph;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import com.google.common.collect.UnmodifiableIterator;
import java.util.Locale;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class EndpointPair_ESTestTest22 extends EndpointPair_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test21() throws Throwable {
        Integer integer0 = new Integer((-1068));
        NetworkBuilder<Object, Object> networkBuilder0 = NetworkBuilder.directed();
        Object object0 = new Object();
        StandardMutableGraph<Integer> standardMutableGraph0 = new StandardMutableGraph<Integer>(networkBuilder0);
        ImmutableGraph<Integer> immutableGraph0 = new ImmutableGraph<Integer>(standardMutableGraph0);
        EndpointPair<Integer> endpointPair0 = EndpointPair.of((Graph<?>) immutableGraph0, integer0, integer0);
        boolean boolean0 = endpointPair0.equals(object0);
        assertFalse(boolean0);
    }
}
