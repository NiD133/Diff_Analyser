package com.fasterxml.jackson.core.io;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class NumberOutput_ESTestTest67 extends NumberOutput_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test66() throws Throwable {
        byte[] byteArray0 = new byte[25];
        int int0 = NumberOutput.outputLong(2084322364L, byteArray0, 0);
        assertEquals(10, int0);
    }
}
