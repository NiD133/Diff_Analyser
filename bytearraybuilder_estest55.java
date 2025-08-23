package com.fasterxml.jackson.core.util;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class ByteArrayBuilder_ESTestTest55 extends ByteArrayBuilder_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test54() throws Throwable {
        byte[] byteArray0 = new byte[1];
        ByteArrayBuilder byteArrayBuilder0 = ByteArrayBuilder.fromInitial(byteArray0, (byte) 74);
        byteArrayBuilder0.appendTwoBytes(1981);
        byteArrayBuilder0.reset();
        assertEquals(0, byteArrayBuilder0.getCurrentSegmentLength());
    }
}
