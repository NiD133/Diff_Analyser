package com.google.gson.internal;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class LazilyParsedNumber_ESTestTest1 extends LazilyParsedNumber_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test00() throws Throwable {
        LazilyParsedNumber lazilyParsedNumber0 = new LazilyParsedNumber("Deserialization is unsupported");
        lazilyParsedNumber0.hashCode();
    }
}
