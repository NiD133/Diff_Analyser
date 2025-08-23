package com.fasterxml.jackson.core.util;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class ByteArrayBuilder_ESTestTest13 extends ByteArrayBuilder_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test12() throws Throwable {
        ByteArrayBuilder byteArrayBuilder0 = new ByteArrayBuilder();
        byteArrayBuilder0.setCurrentSegmentLength((-1));
        int int0 = byteArrayBuilder0.getCurrentSegmentLength();
        assertEquals((-1), byteArrayBuilder0.size());
        assertEquals((-1), int0);
    }
}
