package com.fasterxml.jackson.core.io;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class NumberOutput_ESTestTest22 extends NumberOutput_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test21() throws Throwable {
        char[] charArray0 = new char[3];
        // Undeclared exception!
        try {
            NumberOutput.outputLong(5486124068793688683L, charArray0, 1);
            fail("Expecting exception: ArrayIndexOutOfBoundsException");
        } catch (ArrayIndexOutOfBoundsException e) {
            //
            // 3
            //
            verifyException("com.fasterxml.jackson.core.io.NumberOutput", e);
        }
    }
}
