package com.fasterxml.jackson.core.util;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class ByteArrayBuilder_ESTestTest17 extends ByteArrayBuilder_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test16() throws Throwable {
        byte[] byteArray0 = new byte[1];
        ByteArrayBuilder byteArrayBuilder0 = ByteArrayBuilder.fromInitial(byteArray0, (byte) 74);
        ByteArrayBuilder byteArrayBuilder1 = ByteArrayBuilder.fromInitial(byteArrayBuilder0.NO_BYTES, 0);
        assertEquals(0, byteArrayBuilder1.size());
    }
}
