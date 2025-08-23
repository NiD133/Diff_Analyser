package com.fasterxml.jackson.core.util;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class ByteArrayBuilder_ESTestTest60 extends ByteArrayBuilder_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test59() throws Throwable {
        ByteArrayBuilder byteArrayBuilder0 = new ByteArrayBuilder(1);
        byteArrayBuilder0.appendThreeBytes(1);
        assertEquals(2, byteArrayBuilder0.getCurrentSegmentLength());
        byte[] byteArray0 = byteArrayBuilder0.completeAndCoalesce(1);
        assertEquals(2, byteArray0.length);
    }
}
