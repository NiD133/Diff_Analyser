package com.google.gson.internal;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class LazilyParsedNumber_ESTestTest14 extends LazilyParsedNumber_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test13() throws Throwable {
        LazilyParsedNumber lazilyParsedNumber0 = new LazilyParsedNumber((String) null);
        // Undeclared exception!
        try {
            lazilyParsedNumber0.intValue();
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            //
            // no message in exception (getMessage() returned null)
            //
            verifyException("com.google.gson.internal.NumberLimits", e);
        }
    }
}
