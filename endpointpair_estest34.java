package com.google.common.graph;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import com.google.common.collect.UnmodifiableIterator;
import java.util.Locale;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class EndpointPair_ESTestTest34 extends EndpointPair_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test33() throws Throwable {
        Locale.Category locale_Category0 = Locale.Category.DISPLAY;
        EndpointPair<Locale.Category> endpointPair0 = EndpointPair.unordered(locale_Category0, locale_Category0);
        // Undeclared exception!
        try {
            endpointPair0.target();
            fail("Expecting exception: UnsupportedOperationException");
        } catch (UnsupportedOperationException e) {
            //
            // Cannot call source()/target() on a EndpointPair from an undirected graph. Consider calling adjacentNode(node) if you already have a node, or nodeU()/nodeV() if you don't.
            //
            verifyException("com.google.common.graph.EndpointPair$Unordered", e);
        }
    }
}
