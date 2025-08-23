package com.fasterxml.jackson.core.io;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class NumberOutput_ESTestTest47 extends NumberOutput_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test46() throws Throwable {
        byte[] byteArray0 = new byte[6];
        int int0 = NumberOutput.outputInt(1497, byteArray0, (int) (byte) 2);
        assertEquals(6, int0);
        assertArrayEquals(new byte[] { (byte) 0, (byte) 0, (byte) 49, (byte) 52, (byte) 57, (byte) 55 }, byteArray0);
    }
}
