package com.fasterxml.jackson.core.io;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class NumberOutput_ESTestTest28 extends NumberOutput_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test27() throws Throwable {
        byte[] byteArray0 = new byte[3];
        // Undeclared exception!
        try {
            NumberOutput.outputLong((-424L), byteArray0, 0);
            fail("Expecting exception: ArrayIndexOutOfBoundsException");
        } catch (ArrayIndexOutOfBoundsException e) {
            //
            // 3
            //
            verifyException("com.fasterxml.jackson.core.io.NumberOutput", e);
        }
    }
}
