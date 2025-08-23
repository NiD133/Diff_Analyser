package com.fasterxml.jackson.core.io;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class NumberOutput_ESTestTest23 extends NumberOutput_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test22() throws Throwable {
        char[] charArray0 = new char[9];
        // Undeclared exception!
        try {
            NumberOutput.outputLong((-2147483648L), charArray0, 0);
            fail("Expecting exception: ArrayIndexOutOfBoundsException");
        } catch (ArrayIndexOutOfBoundsException e) {
            //
            // 9
            //
            verifyException("com.fasterxml.jackson.core.io.NumberOutput", e);
        }
    }
}
