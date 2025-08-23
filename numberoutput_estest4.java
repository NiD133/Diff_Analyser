package com.fasterxml.jackson.core.io;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class NumberOutput_ESTestTest4 extends NumberOutput_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test03() throws Throwable {
        char[] charArray0 = new char[8];
        // Undeclared exception!
        try {
            NumberOutput.outputLong(9007199254740992000L, charArray0, (-2118));
            fail("Expecting exception: ArrayIndexOutOfBoundsException");
        } catch (ArrayIndexOutOfBoundsException e) {
            //
            // -2118
            //
            verifyException("com.fasterxml.jackson.core.io.NumberOutput", e);
        }
    }
}
