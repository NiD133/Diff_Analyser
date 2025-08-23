package com.fasterxml.jackson.core.io;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class NumberOutput_ESTestTest9 extends NumberOutput_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test08() throws Throwable {
        String string0 = NumberOutput.toString((long) (byte) (-11));
        assertEquals("-11", string0);
    }
}
