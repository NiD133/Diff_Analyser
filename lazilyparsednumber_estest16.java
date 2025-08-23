package com.google.gson.internal;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class LazilyParsedNumber_ESTestTest16 extends LazilyParsedNumber_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test15() throws Throwable {
        LazilyParsedNumber lazilyParsedNumber0 = new LazilyParsedNumber((String) null);
        // Undeclared exception!
        try {
            lazilyParsedNumber0.floatValue();
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
        }
    }
}
