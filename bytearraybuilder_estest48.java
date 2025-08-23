package com.fasterxml.jackson.core.util;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class ByteArrayBuilder_ESTestTest48 extends ByteArrayBuilder_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test47() throws Throwable {
        ByteArrayBuilder byteArrayBuilder0 = new ByteArrayBuilder();
        byteArrayBuilder0.finishCurrentSegment();
        byteArrayBuilder0.toByteArray();
        assertEquals(0, byteArrayBuilder0.size());
    }
}
