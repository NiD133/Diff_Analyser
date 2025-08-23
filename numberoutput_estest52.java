package com.fasterxml.jackson.core.io;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class NumberOutput_ESTestTest52 extends NumberOutput_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test51() throws Throwable {
        // Undeclared exception!
        try {
            NumberOutput.outputLong(99999999999994L, (char[]) null, (-813));
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            //
            // no message in exception (getMessage() returned null)
            //
            verifyException("com.fasterxml.jackson.core.io.NumberOutput", e);
        }
    }
}
