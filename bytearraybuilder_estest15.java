package com.fasterxml.jackson.core.util;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class ByteArrayBuilder_ESTestTest15 extends ByteArrayBuilder_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test14() throws Throwable {
        BufferRecycler bufferRecycler0 = new BufferRecycler();
        ByteArrayBuilder byteArrayBuilder0 = new ByteArrayBuilder(bufferRecycler0, (-3320));
        ByteArrayBuilder byteArrayBuilder1 = ByteArrayBuilder.fromInitial(byteArrayBuilder0.NO_BYTES, 6);
        byteArrayBuilder1.getCurrentSegment();
        assertEquals(6, byteArrayBuilder1.size());
    }
}
