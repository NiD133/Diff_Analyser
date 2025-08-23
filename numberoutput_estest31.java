package com.fasterxml.jackson.core.io;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class NumberOutput_ESTestTest31 extends NumberOutput_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test30() throws Throwable {
        char[] charArray0 = new char[6];
        int int0 = NumberOutput.outputLong((-3640L), charArray0, 0);
        assertArrayEquals(new char[] { '-', '3', '6', '4', '0', '\u0000' }, charArray0);
        assertEquals(5, int0);
    }
}
