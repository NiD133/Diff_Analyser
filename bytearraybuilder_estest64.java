package com.fasterxml.jackson.core.util;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class ByteArrayBuilder_ESTestTest64 extends ByteArrayBuilder_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test63() throws Throwable {
        ByteArrayBuilder byteArrayBuilder0 = new ByteArrayBuilder(131050);
        byteArrayBuilder0.write((-219));
        assertEquals(1, byteArrayBuilder0.getCurrentSegmentLength());
    }
}
