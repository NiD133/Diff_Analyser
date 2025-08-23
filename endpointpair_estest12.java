package com.google.common.graph;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import com.google.common.collect.UnmodifiableIterator;
import java.util.Locale;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class EndpointPair_ESTestTest12 extends EndpointPair_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test11() throws Throwable {
        NetworkBuilder<Object, Object> networkBuilder0 = NetworkBuilder.directed();
        StandardMutableNetwork<Object, Object> standardMutableNetwork0 = new StandardMutableNetwork<Object, Object>(networkBuilder0);
        Graph<Object> graph0 = standardMutableNetwork0.asGraph();
        // Undeclared exception!
        try {
            EndpointPair.of((Graph<?>) graph0, (Comparable<Comparable>) null, (Comparable<Comparable>) null);
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            //
            // no message in exception (getMessage() returned null)
            //
            verifyException("com.google.common.base.Preconditions", e);
        }
    }
}
