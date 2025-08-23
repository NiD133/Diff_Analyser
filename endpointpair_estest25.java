package com.google.common.graph;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import com.google.common.collect.UnmodifiableIterator;
import java.util.Locale;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class EndpointPair_ESTestTest25 extends EndpointPair_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test24() throws Throwable {
        Locale.Category locale_Category0 = Locale.Category.DISPLAY;
        EndpointPair<Locale.Category> endpointPair0 = EndpointPair.unordered(locale_Category0, locale_Category0);
        Locale.Category locale_Category1 = Locale.Category.FORMAT;
        // Undeclared exception!
        try {
            endpointPair0.adjacentNode(locale_Category1);
            fail("Expecting exception: IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            //
            // EndpointPair [DISPLAY, DISPLAY] does not contain node FORMAT
            //
            verifyException("com.google.common.graph.EndpointPair", e);
        }
    }
}
