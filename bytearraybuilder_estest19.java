package com.fasterxml.jackson.core.util;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class ByteArrayBuilder_ESTestTest19 extends ByteArrayBuilder_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test18() throws Throwable {
        BufferRecycler bufferRecycler0 = new BufferRecycler();
        JsonRecyclerPools.ThreadLocalPool jsonRecyclerPools_ThreadLocalPool0 = JsonRecyclerPools.ThreadLocalPool.GLOBAL;
        BufferRecycler bufferRecycler1 = bufferRecycler0.withPool(jsonRecyclerPools_ThreadLocalPool0);
        ByteArrayBuilder byteArrayBuilder0 = new ByteArrayBuilder(bufferRecycler1, 1357);
        BufferRecycler bufferRecycler2 = byteArrayBuilder0.bufferRecycler();
        assertEquals(1, BufferRecycler.BYTE_WRITE_ENCODING_BUFFER);
    }
}
