package com.fasterxml.jackson.core.io;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class NumberOutput_ESTestTest32 extends NumberOutput_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test31() throws Throwable {
        char[] charArray0 = new char[9];
        int int0 = NumberOutput.outputInt(0, charArray0, 0);
        assertEquals(1, int0);
        assertArrayEquals(new char[] { '0', '\u0000', '\u0000', '\u0000', '\u0000', '\u0000', '\u0000', '\u0000', '\u0000' }, charArray0);
    }
}
