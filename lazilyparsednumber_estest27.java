package com.google.gson.internal;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class LazilyParsedNumber_ESTestTest27 extends LazilyParsedNumber_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test26() throws Throwable {
        LazilyParsedNumber lazilyParsedNumber0 = new LazilyParsedNumber("-6");
        int int0 = lazilyParsedNumber0.intValue();
        assertEquals((-6), int0);
    }
}