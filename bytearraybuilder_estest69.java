package com.fasterxml.jackson.core.util;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class ByteArrayBuilder_ESTestTest69 extends ByteArrayBuilder_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test68() throws Throwable {
        JsonRecyclerPools.BoundedPool jsonRecyclerPools_BoundedPool0 = new JsonRecyclerPools.BoundedPool(0);
        BufferRecycler bufferRecycler0 = jsonRecyclerPools_BoundedPool0.createPooled();
        ByteArrayBuilder byteArrayBuilder0 = new ByteArrayBuilder(bufferRecycler0);
        byteArrayBuilder0.close();
        assertEquals(0, byteArrayBuilder0.getCurrentSegmentLength());
    }
}
