package com.google.common.graph;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import com.google.common.collect.UnmodifiableIterator;
import java.util.Locale;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class EndpointPair_ESTestTest4 extends EndpointPair_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test03() throws Throwable {
        Locale.Category locale_Category0 = Locale.Category.DISPLAY;
        EndpointPair<Locale.Category> endpointPair0 = EndpointPair.unordered(locale_Category0, locale_Category0);
        Locale.Category locale_Category1 = endpointPair0.nodeV();
        assertSame(locale_Category0, locale_Category1);
    }
}
