package com.google.common.primitives;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import java.util.Comparator;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class SignedBytes_ESTestTest21 extends SignedBytes_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test20() throws Throwable {
        byte[] byteArray0 = new byte[3];
        byteArray0[1] = (byte) 110;
        byte byte0 = SignedBytes.max(byteArray0);
        assertEquals((byte) 110, byte0);
    }
}
