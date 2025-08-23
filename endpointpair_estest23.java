package com.google.common.graph;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import com.google.common.collect.UnmodifiableIterator;
import java.util.Locale;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class EndpointPair_ESTestTest23 extends EndpointPair_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test22() throws Throwable {
        Locale.Category locale_Category0 = Locale.Category.DISPLAY;
        EndpointPair<Locale.Category> endpointPair0 = EndpointPair.ordered(locale_Category0, locale_Category0);
        boolean boolean0 = endpointPair0.equals(endpointPair0);
        assertTrue(boolean0);
    }
}
