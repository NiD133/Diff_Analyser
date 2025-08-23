package com.fasterxml.jackson.core.io;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class NumberOutput_ESTestTest40 extends NumberOutput_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test39() throws Throwable {
        byte[] byteArray0 = new byte[8];
        // Undeclared exception!
        try {
            NumberOutput.outputInt(1000000, byteArray0, 11);
            fail("Expecting exception: ArrayIndexOutOfBoundsException");
        } catch (ArrayIndexOutOfBoundsException e) {
            //
            // 11
            //
            verifyException("com.fasterxml.jackson.core.io.NumberOutput", e);
        }
    }
}
