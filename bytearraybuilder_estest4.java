package com.fasterxml.jackson.core.util;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class ByteArrayBuilder_ESTestTest4 extends ByteArrayBuilder_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test03() throws Throwable {
        byte[] byteArray0 = new byte[0];
        ByteArrayBuilder byteArrayBuilder0 = ByteArrayBuilder.fromInitial(byteArray0, 1);
        byteArrayBuilder0.appendThreeBytes((byte) (-48));
        assertEquals(3, byteArrayBuilder0.size());
    }
}
