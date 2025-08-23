package com.fasterxml.jackson.core.util;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class ByteArrayBuilder_ESTestTest59 extends ByteArrayBuilder_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test58() throws Throwable {
        BufferRecycler bufferRecycler0 = new BufferRecycler();
        ByteArrayBuilder byteArrayBuilder0 = new ByteArrayBuilder(bufferRecycler0, 2);
        byteArrayBuilder0.getClearAndRelease();
        byte[] byteArray0 = byteArrayBuilder0.getClearAndRelease();
        assertEquals(0, byteArray0.length);
    }
}
