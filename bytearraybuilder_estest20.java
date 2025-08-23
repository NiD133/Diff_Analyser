package com.fasterxml.jackson.core.util;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class ByteArrayBuilder_ESTestTest20 extends ByteArrayBuilder_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test19() throws Throwable {
        JsonRecyclerPools.NonRecyclingPool jsonRecyclerPools_NonRecyclingPool0 = new JsonRecyclerPools.NonRecyclingPool();
        BufferRecycler bufferRecycler0 = jsonRecyclerPools_NonRecyclingPool0.acquirePooled();
        ByteArrayBuilder byteArrayBuilder0 = new ByteArrayBuilder(bufferRecycler0, 1);
        BufferRecycler bufferRecycler1 = byteArrayBuilder0.bufferRecycler();
        assertEquals(3, BufferRecycler.CHAR_NAME_COPY_BUFFER);
    }
}
