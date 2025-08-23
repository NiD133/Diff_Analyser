package com.google.common.graph;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import com.google.common.collect.UnmodifiableIterator;
import java.util.Locale;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class EndpointPair_ESTestTest31 extends EndpointPair_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test30() throws Throwable {
        Locale.Category locale_Category0 = Locale.Category.DISPLAY;
        Integer integer0 = new Integer(9);
        EndpointPair<Object> endpointPair0 = EndpointPair.unordered((Object) locale_Category0, (Object) integer0);
        Locale.Category locale_Category1 = Locale.Category.DISPLAY;
        EndpointPair<Object> endpointPair1 = EndpointPair.unordered((Object) integer0, (Object) locale_Category1);
        boolean boolean0 = endpointPair1.equals(endpointPair0);
        assertTrue(boolean0);
    }
}
