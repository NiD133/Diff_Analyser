package com.fasterxml.jackson.core.io;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class NumberOutput_ESTestTest26 extends NumberOutput_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test25() throws Throwable {
        char[] charArray0 = new char[7];
        // Undeclared exception!
        try {
            NumberOutput.outputLong(274877907L, charArray0, (int) (byte) 0);
            fail("Expecting exception: ArrayIndexOutOfBoundsException");
        } catch (ArrayIndexOutOfBoundsException e) {
            //
            // 7
            //
            verifyException("com.fasterxml.jackson.core.io.NumberOutput", e);
        }
    }
}
