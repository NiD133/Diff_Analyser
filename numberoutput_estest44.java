package com.fasterxml.jackson.core.io;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class NumberOutput_ESTestTest44 extends NumberOutput_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test43() throws Throwable {
        char[] charArray0 = new char[7];
        // Undeclared exception!
        try {
            NumberOutput.outputInt(1000000000, charArray0, 0);
            fail("Expecting exception: ArrayIndexOutOfBoundsException");
        } catch (ArrayIndexOutOfBoundsException e) {
            //
            // 7
            //
            verifyException("com.fasterxml.jackson.core.io.NumberOutput", e);
        }
    }
}
