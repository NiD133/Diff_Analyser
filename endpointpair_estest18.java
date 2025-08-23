package com.google.common.graph;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import com.google.common.collect.UnmodifiableIterator;
import java.util.Locale;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class EndpointPair_ESTestTest18 extends EndpointPair_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test17() throws Throwable {
        Locale.Category locale_Category0 = Locale.Category.DISPLAY;
        Object object0 = new Object();
        EndpointPair<Object> endpointPair0 = EndpointPair.unordered((Object) locale_Category0, object0);
        boolean boolean0 = endpointPair0.equals(endpointPair0);
        assertTrue(boolean0);
    }
}
