package com.google.common.graph;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import com.google.common.collect.UnmodifiableIterator;
import java.util.Locale;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class EndpointPair_ESTestTest29 extends EndpointPair_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test28() throws Throwable {
        Locale.Category locale_Category0 = Locale.Category.FORMAT;
        EndpointPair<Locale.Category> endpointPair0 = EndpointPair.ordered(locale_Category0, locale_Category0);
        Object object0 = endpointPair0.source();
        EndpointPair<Object> endpointPair1 = EndpointPair.unordered((Object) object0, (Object) endpointPair0);
        boolean boolean0 = endpointPair1.equals(endpointPair0);
        assertFalse(boolean0);
    }
}
