package com.fasterxml.jackson.core.io;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class NumberOutput_ESTestTest30 extends NumberOutput_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test29() throws Throwable {
        char[] charArray0 = new char[14];
        int int0 = NumberOutput.outputLong((long) 2084322237, charArray0, 4);
        assertEquals(14, int0);
    }
}
