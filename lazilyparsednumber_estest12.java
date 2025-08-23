package com.google.gson.internal;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class LazilyParsedNumber_ESTestTest12 extends LazilyParsedNumber_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test11() throws Throwable {
        LazilyParsedNumber lazilyParsedNumber0 = new LazilyParsedNumber("Deserialization is unsupported");
        // Undeclared exception!
        try {
            lazilyParsedNumber0.longValue();
            fail("Expecting exception: NumberFormatException");
        } catch (NumberFormatException e) {
            //
            // no message in exception (getMessage() returned null)
            //
            verifyException("java.math.BigDecimal", e);
        }
    }
}
