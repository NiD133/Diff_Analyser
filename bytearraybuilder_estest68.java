package com.fasterxml.jackson.core.util;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class ByteArrayBuilder_ESTestTest68 extends ByteArrayBuilder_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test67() throws Throwable {
        ByteArrayBuilder byteArrayBuilder0 = new ByteArrayBuilder(131050);
        byteArrayBuilder0.completeAndCoalesce(131050);
        byteArrayBuilder0.append(131050);
        byteArrayBuilder0.finishCurrentSegment();
        byteArrayBuilder0.finishCurrentSegment();
        assertEquals(294862, byteArrayBuilder0.size());
    }
}