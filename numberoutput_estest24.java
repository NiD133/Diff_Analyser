package com.fasterxml.jackson.core.io;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class NumberOutput_ESTestTest24 extends NumberOutput_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test23() throws Throwable {
        byte[] byteArray0 = new byte[5];
        // Undeclared exception!
        try {
            NumberOutput.outputLong(7174648137343063403L, byteArray0, 0);
            fail("Expecting exception: ArrayIndexOutOfBoundsException");
        } catch (ArrayIndexOutOfBoundsException e) {
            //
            // 5
            //
            verifyException("com.fasterxml.jackson.core.io.NumberOutput", e);
        }
    }
}
