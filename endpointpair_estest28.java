package com.google.common.graph;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import com.google.common.collect.UnmodifiableIterator;
import java.util.Locale;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class EndpointPair_ESTestTest28 extends EndpointPair_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test27() throws Throwable {
        Locale.Category locale_Category0 = Locale.Category.FORMAT;
        EndpointPair<Object> endpointPair0 = EndpointPair.ordered((Object) locale_Category0, (Object) locale_Category0);
        NetworkBuilder<Object, Object> networkBuilder0 = NetworkBuilder.undirected();
        StandardMutableNetwork<Object, Object> standardMutableNetwork0 = new StandardMutableNetwork<Object, Object>(networkBuilder0);
        // Undeclared exception!
        try {
            standardMutableNetwork0.incidentNodes(endpointPair0);
            fail("Expecting exception: IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            //
            // Edge <FORMAT -> FORMAT> is not an element of this graph.
            //
            verifyException("com.google.common.graph.StandardNetwork", e);
        }
    }
}
