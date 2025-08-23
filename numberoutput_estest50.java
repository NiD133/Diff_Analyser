package com.fasterxml.jackson.core.io;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class NumberOutput_ESTestTest50 extends NumberOutput_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test49() throws Throwable {
        byte[] byteArray0 = new byte[12];
        // Undeclared exception!
        try {
            NumberOutput.outputLong(9999999999988L, byteArray0, (int) (byte) 57);
            fail("Expecting exception: ArrayIndexOutOfBoundsException");
        } catch (ArrayIndexOutOfBoundsException e) {
            //
            // 57
            //
            verifyException("com.fasterxml.jackson.core.io.NumberOutput", e);
        }
    }
}
