package com.google.common.primitives;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import java.util.Comparator;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class SignedBytes_ESTestTest8 extends SignedBytes_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test07() throws Throwable {
        byte[] byteArray0 = new byte[1];
        byteArray0[0] = (byte) (-81);
        byte byte0 = SignedBytes.max(byteArray0);
        assertEquals((byte) (-81), byte0);
    }
}
