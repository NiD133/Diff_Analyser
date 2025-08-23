package com.google.common.graph;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import com.google.common.collect.UnmodifiableIterator;
import java.util.Locale;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class EndpointPair_ESTestTest30 extends EndpointPair_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test29() throws Throwable {
        Integer integer0 = new Integer((-1));
        EndpointPair<Integer> endpointPair0 = EndpointPair.unordered(integer0, integer0);
        UnmodifiableIterator<Integer> unmodifiableIterator0 = endpointPair0.iterator();
        assertNotNull(unmodifiableIterator0);
    }
}
