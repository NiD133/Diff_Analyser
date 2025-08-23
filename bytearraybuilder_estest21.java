package com.fasterxml.jackson.core.util;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class ByteArrayBuilder_ESTestTest21 extends ByteArrayBuilder_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test20() throws Throwable {
        ByteArrayBuilder byteArrayBuilder0 = new ByteArrayBuilder();
        // Undeclared exception!
        try {
            byteArrayBuilder0.write((byte[]) null, 557, 557);
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
        }
    }
}
