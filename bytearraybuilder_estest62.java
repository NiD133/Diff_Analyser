package com.fasterxml.jackson.core.util;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class ByteArrayBuilder_ESTestTest62 extends ByteArrayBuilder_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test61() throws Throwable {
        BufferRecycler bufferRecycler0 = new BufferRecycler();
        ByteArrayBuilder byteArrayBuilder0 = new ByteArrayBuilder(bufferRecycler0, (-3320));
        byte[] byteArray0 = byteArrayBuilder0.getCurrentSegment();
        assertEquals(2000, byteArray0.length);
    }
}
