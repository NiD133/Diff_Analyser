package com.google.common.graph;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import com.google.common.collect.UnmodifiableIterator;
import java.util.Locale;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class EndpointPair_ESTestTest21 extends EndpointPair_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test20() throws Throwable {
        Locale.Category locale_Category0 = Locale.Category.FORMAT;
        EndpointPair<Object> endpointPair0 = EndpointPair.ordered((Object) locale_Category0, (Object) locale_Category0);
        Object object0 = endpointPair0.nodeU();
        EndpointPair<Object> endpointPair1 = EndpointPair.unordered((Object) locale_Category0, (Object) object0);
        assertFalse(endpointPair1.equals((Object) endpointPair0));
        boolean boolean0 = endpointPair0.equals(endpointPair1);
        assertFalse(boolean0);
    }
}
