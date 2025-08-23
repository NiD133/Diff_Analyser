package com.fasterxml.jackson.core.io;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class NumberOutput_ESTestTest74 extends NumberOutput_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test73() throws Throwable {
        char[] charArray0 = new char[4];
        // Undeclared exception!
        try {
            NumberOutput.outputLong(1000000000000L, charArray0, 2137541594);
            fail("Expecting exception: ArrayIndexOutOfBoundsException");
        } catch (ArrayIndexOutOfBoundsException e) {
            //
            // 2137541594
            //
            verifyException("com.fasterxml.jackson.core.io.NumberOutput", e);
        }
    }
}
