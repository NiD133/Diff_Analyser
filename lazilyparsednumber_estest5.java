package com.google.gson.internal;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class LazilyParsedNumber_ESTestTest5 extends LazilyParsedNumber_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test04() throws Throwable {
        LazilyParsedNumber lazilyParsedNumber0 = new LazilyParsedNumber("3");
        long long0 = lazilyParsedNumber0.longValue();
        assertEquals(3L, long0);
    }
}
