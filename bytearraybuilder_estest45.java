package com.fasterxml.jackson.core.util;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class ByteArrayBuilder_ESTestTest45 extends ByteArrayBuilder_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test44() throws Throwable {
        BufferRecycler bufferRecycler0 = new BufferRecycler(1, 1);
        ByteArrayBuilder byteArrayBuilder0 = null;
        try {
            byteArrayBuilder0 = new ByteArrayBuilder(bufferRecycler0);
            fail("Expecting exception: IndexOutOfBoundsException");
        } catch (IndexOutOfBoundsException e) {
            //
            // index 2
            //
            verifyException("java.util.concurrent.atomic.AtomicReferenceArray", e);
        }
    }
}
