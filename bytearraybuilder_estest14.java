package com.fasterxml.jackson.core.util;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class ByteArrayBuilder_ESTestTest14 extends ByteArrayBuilder_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test13() throws Throwable {
        JsonRecyclerPools.NonRecyclingPool jsonRecyclerPools_NonRecyclingPool0 = JsonRecyclerPools.NonRecyclingPool.GLOBAL;
        BufferRecycler bufferRecycler0 = jsonRecyclerPools_NonRecyclingPool0.acquirePooled();
        ByteArrayBuilder byteArrayBuilder0 = new ByteArrayBuilder(bufferRecycler0, (-1));
        byteArrayBuilder0.release();
        byte[] byteArray0 = byteArrayBuilder0.getCurrentSegment();
        assertNull(byteArray0);
    }
}
