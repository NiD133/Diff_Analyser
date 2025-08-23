package com.google.gson.internal;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class LazilyParsedNumber_ESTestTest10 extends LazilyParsedNumber_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test09() throws Throwable {
        LazilyParsedNumber lazilyParsedNumber0 = new LazilyParsedNumber("0");
        double double0 = lazilyParsedNumber0.doubleValue();
        assertEquals(0.0, double0, 0.01);
    }
}
