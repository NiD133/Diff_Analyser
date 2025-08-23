package com.google.common.graph;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import com.google.common.collect.UnmodifiableIterator;
import java.util.Locale;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class EndpointPair_ESTestTest24 extends EndpointPair_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test23() throws Throwable {
        Integer integer0 = new Integer((-1068));
        NetworkBuilder<Object, Object> networkBuilder0 = NetworkBuilder.directed();
        StandardNetwork<Object, Object> standardNetwork0 = new StandardNetwork<Object, Object>(networkBuilder0);
        EndpointPair<Object> endpointPair0 = EndpointPair.ordered((Object) integer0, (Object) integer0);
        EndpointPair<Object> endpointPair1 = EndpointPair.of((Network<?, ?>) standardNetwork0, (Object) integer0, (Object) integer0);
        boolean boolean0 = endpointPair0.equals(endpointPair1);
        assertTrue(boolean0);
    }
}
