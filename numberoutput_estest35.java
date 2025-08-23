package com.fasterxml.jackson.core.io;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class NumberOutput_ESTestTest35 extends NumberOutput_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test34() throws Throwable {
        // Undeclared exception!
        try {
            NumberOutput.outputLong((long) (byte) 0, (byte[]) null, (int) (byte) 0);
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            //
            // no message in exception (getMessage() returned null)
            //
            verifyException("com.fasterxml.jackson.core.io.NumberOutput", e);
        }
    }
}
