package com.fasterxml.jackson.core.io;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class NumberOutput_ESTestTest60 extends NumberOutput_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test59() throws Throwable {
        String string0 = NumberOutput.toString((-9223372036854775805L));
        assertEquals("-9223372036854775805", string0);
    }
}
