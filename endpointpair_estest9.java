package com.google.common.graph;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import com.google.common.collect.UnmodifiableIterator;
import java.util.Locale;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class EndpointPair_ESTestTest9 extends EndpointPair_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test08() throws Throwable {
        Locale.Category locale_Category0 = Locale.Category.DISPLAY;
        // Undeclared exception!
        try {
            EndpointPair.of((Network<?, ?>) null, locale_Category0, locale_Category0);
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            //
            // no message in exception (getMessage() returned null)
            //
            verifyException("com.google.common.graph.EndpointPair", e);
        }
    }
}
