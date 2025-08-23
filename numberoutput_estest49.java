package com.fasterxml.jackson.core.io;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class NumberOutput_ESTestTest49 extends NumberOutput_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test48() throws Throwable {
        byte[] byteArray0 = new byte[0];
        // Undeclared exception!
        try {
            NumberOutput.outputLong(99999999999988L, byteArray0, 1436);
            fail("Expecting exception: ArrayIndexOutOfBoundsException");
        } catch (ArrayIndexOutOfBoundsException e) {
            //
            // 1436
            //
            verifyException("com.fasterxml.jackson.core.io.NumberOutput", e);
        }
    }
}
