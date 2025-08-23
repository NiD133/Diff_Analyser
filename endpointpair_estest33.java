package com.google.common.graph;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import com.google.common.collect.UnmodifiableIterator;
import java.util.Locale;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class EndpointPair_ESTestTest33 extends EndpointPair_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test32() throws Throwable {
        Locale.Category locale_Category0 = Locale.Category.DISPLAY;
        EndpointPair<Locale.Category> endpointPair0 = EndpointPair.unordered(locale_Category0, locale_Category0);
        NetworkBuilder<Object, Object> networkBuilder0 = NetworkBuilder.directed();
        StandardNetwork<Object, Object> standardNetwork0 = new StandardNetwork<Object, Object>(networkBuilder0);
        // Undeclared exception!
        try {
            standardNetwork0.incidentNodes(endpointPair0);
            fail("Expecting exception: IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            //
            // Edge [DISPLAY, DISPLAY] is not an element of this graph.
            //
            verifyException("com.google.common.graph.StandardNetwork", e);
        }
    }
}
