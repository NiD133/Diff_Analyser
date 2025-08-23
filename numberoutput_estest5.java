package com.fasterxml.jackson.core.io;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class NumberOutput_ESTestTest5 extends NumberOutput_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test04() throws Throwable {
        char[] charArray0 = new char[7];
        // Undeclared exception!
        try {
            NumberOutput.outputLong(10000000000000L, charArray0, 340);
            fail("Expecting exception: ArrayIndexOutOfBoundsException");
        } catch (ArrayIndexOutOfBoundsException e) {
            //
            // 340
            //
            verifyException("com.fasterxml.jackson.core.io.NumberOutput", e);
        }
    }
}