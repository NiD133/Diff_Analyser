package com.fasterxml.jackson.core.io;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class NumberOutput_ESTestTest27 extends NumberOutput_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test26() throws Throwable {
        byte[] byteArray0 = new byte[7];
        // Undeclared exception!
        try {
            NumberOutput.outputLong(274877893L, byteArray0, (int) (byte) 50);
            fail("Expecting exception: ArrayIndexOutOfBoundsException");
        } catch (ArrayIndexOutOfBoundsException e) {
            //
            // 50
            //
            verifyException("com.fasterxml.jackson.core.io.NumberOutput", e);
        }
    }
}
