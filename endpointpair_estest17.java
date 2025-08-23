package com.google.common.graph;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import com.google.common.collect.UnmodifiableIterator;
import java.util.Locale;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class EndpointPair_ESTestTest17 extends EndpointPair_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test16() throws Throwable {
        Locale.Category locale_Category0 = Locale.Category.DISPLAY;
        Object object0 = new Object();
        EndpointPair<Object> endpointPair0 = EndpointPair.unordered((Object) locale_Category0, object0);
        EndpointPair<Object> endpointPair1 = EndpointPair.unordered((Object) endpointPair0, (Object) locale_Category0);
        boolean boolean0 = endpointPair1.equals(endpointPair0);
        assertFalse(boolean0);
        assertFalse(endpointPair0.equals((Object) endpointPair1));
    }
}
