package com.google.common.graph;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import com.google.common.collect.UnmodifiableIterator;
import java.util.Locale;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class EndpointPair_ESTestTest15 extends EndpointPair_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test14() throws Throwable {
        Locale.Category locale_Category0 = Locale.Category.FORMAT;
        Object object0 = new Object();
        EndpointPair<Object> endpointPair0 = EndpointPair.unordered((Object) locale_Category0, object0);
        EndpointPair<Object> endpointPair1 = EndpointPair.unordered((Object) endpointPair0, (Object) locale_Category0);
        boolean boolean0 = endpointPair0.equals(endpointPair1);
        assertFalse(boolean0);
    }
}
