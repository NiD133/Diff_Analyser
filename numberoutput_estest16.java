package com.fasterxml.jackson.core.io;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class NumberOutput_ESTestTest16 extends NumberOutput_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test15() throws Throwable {
        byte[] byteArray0 = new byte[2];
        int int0 = NumberOutput.outputLong(1L, byteArray0, (int) (byte) 0);
        assertArrayEquals(new byte[] { (byte) 49, (byte) 0 }, byteArray0);
        assertEquals(1, int0);
    }
}
