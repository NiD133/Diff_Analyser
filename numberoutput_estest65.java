package com.fasterxml.jackson.core.io;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class NumberOutput_ESTestTest65 extends NumberOutput_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test64() throws Throwable {
        byte[] byteArray0 = new byte[1];
        // Undeclared exception!
        try {
            NumberOutput.outputLong(100000000000009L, byteArray0, (int) (byte) 49);
            fail("Expecting exception: ArrayIndexOutOfBoundsException");
        } catch (ArrayIndexOutOfBoundsException e) {
            //
            // 49
            //
            verifyException("com.fasterxml.jackson.core.io.NumberOutput", e);
        }
    }
}
