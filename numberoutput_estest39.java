package com.fasterxml.jackson.core.io;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class NumberOutput_ESTestTest39 extends NumberOutput_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test38() throws Throwable {
        byte[] byteArray0 = new byte[5];
        // Undeclared exception!
        try {
            NumberOutput.outputInt(1146455159, byteArray0, 739);
            fail("Expecting exception: ArrayIndexOutOfBoundsException");
        } catch (ArrayIndexOutOfBoundsException e) {
            //
            // 739
            //
            verifyException("com.fasterxml.jackson.core.io.NumberOutput", e);
        }
    }
}
