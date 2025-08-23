package com.fasterxml.jackson.core.util;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class ByteArrayBuilder_ESTestTest47 extends ByteArrayBuilder_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test46() throws Throwable {
        JsonRecyclerPools.ConcurrentDequePool jsonRecyclerPools_ConcurrentDequePool0 = JsonRecyclerPools.ConcurrentDequePool.construct();
        BufferRecycler bufferRecycler0 = jsonRecyclerPools_ConcurrentDequePool0.acquirePooled();
        ByteArrayBuilder byteArrayBuilder0 = new ByteArrayBuilder(bufferRecycler0);
        byte[] byteArray0 = new byte[2];
        byteArrayBuilder0.write(byteArray0, 1, 1);
        byteArrayBuilder0.toByteArray();
        assertEquals(1, byteArrayBuilder0.getCurrentSegmentLength());
    }
}