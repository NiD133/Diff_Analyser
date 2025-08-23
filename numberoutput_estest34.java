package com.fasterxml.jackson.core.io;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class NumberOutput_ESTestTest34 extends NumberOutput_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test33() throws Throwable {
        int int0 = NumberOutput.divBy1000((-1652));
        assertEquals(67108862, int0);
    }
}
