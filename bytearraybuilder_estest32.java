package com.fasterxml.jackson.core.util;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class ByteArrayBuilder_ESTestTest32 extends ByteArrayBuilder_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test31() throws Throwable {
        ByteArrayBuilder byteArrayBuilder0 = ByteArrayBuilder.fromInitial((byte[]) null, 2);
        // Undeclared exception!
        try {
            byteArrayBuilder0.completeAndCoalesce(2);
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
        }
    }
}
