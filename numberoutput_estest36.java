package com.fasterxml.jackson.core.io;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class NumberOutput_ESTestTest36 extends NumberOutput_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test35() throws Throwable {
        String string0 = NumberOutput.toString((-2304.1F), false);
        assertEquals("-2304.1", string0);
    }
}
