package com.fasterxml.jackson.core.io;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class NumberOutput_ESTestTest55 extends NumberOutput_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test54() throws Throwable {
        char[] charArray0 = new char[9];
        // Undeclared exception!
        try {
            NumberOutput.outputLong(833495342724150664L, charArray0, 1);
            fail("Expecting exception: ArrayIndexOutOfBoundsException");
        } catch (ArrayIndexOutOfBoundsException e) {
            //
            // 9
            //
            verifyException("com.fasterxml.jackson.core.io.NumberOutput", e);
        }
    }
}
