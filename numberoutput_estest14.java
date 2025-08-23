package com.fasterxml.jackson.core.io;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class NumberOutput_ESTestTest14 extends NumberOutput_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test13() throws Throwable {
        char[] charArray0 = new char[7];
        int int0 = NumberOutput.outputLong((long) 0, charArray0, 1);
        assertEquals(2, int0);
        assertArrayEquals(new char[] { '\u0000', '0', '\u0000', '\u0000', '\u0000', '\u0000', '\u0000' }, charArray0);
    }
}
