package com.fasterxml.jackson.core.io;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class NumberOutput_ESTestTest41 extends NumberOutput_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test40() throws Throwable {
        byte[] byteArray0 = new byte[5];
        int int0 = NumberOutput.outputInt(0, byteArray0, 0);
        assertArrayEquals(new byte[] { (byte) 48, (byte) 0, (byte) 0, (byte) 0, (byte) 0 }, byteArray0);
        assertEquals(1, int0);
    }
}
