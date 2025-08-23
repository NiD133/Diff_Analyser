package com.fasterxml.jackson.core.io;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class NumberOutput_ESTestTest2 extends NumberOutput_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test01() throws Throwable {
        byte[] byteArray0 = new byte[3];
        // Undeclared exception!
        try {
            NumberOutput.outputLong(9164449253911987585L, byteArray0, (int) (byte) (-48));
            fail("Expecting exception: ArrayIndexOutOfBoundsException");
        } catch (ArrayIndexOutOfBoundsException e) {
            //
            // -48
            //
            verifyException("com.fasterxml.jackson.core.io.NumberOutput", e);
        }
    }
}
