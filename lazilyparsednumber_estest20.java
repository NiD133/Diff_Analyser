package com.google.gson.internal;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class LazilyParsedNumber_ESTestTest20 extends LazilyParsedNumber_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test19() throws Throwable {
        LazilyParsedNumber lazilyParsedNumber0 = new LazilyParsedNumber("Deserialization is pnsuported");
        LazilyParsedNumber lazilyParsedNumber1 = new LazilyParsedNumber("Deserialization is pnsuported");
        boolean boolean0 = lazilyParsedNumber0.equals(lazilyParsedNumber1);
        assertTrue(boolean0);
    }
}
