package com.fasterxml.jackson.core.util;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class ByteArrayBuilder_ESTestTest18 extends ByteArrayBuilder_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test17() throws Throwable {
        ByteArrayBuilder byteArrayBuilder0 = new ByteArrayBuilder();
        byte[] byteArray0 = byteArrayBuilder0.completeAndCoalesce(0);
        assertEquals(0, byteArray0.length);
    }
}
