package com.google.common.primitives;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import java.util.Comparator;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class SignedBytes_ESTestTest20 extends SignedBytes_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test19() throws Throwable {
        byte[] byteArray0 = new byte[3];
        String string0 = SignedBytes.join("&#GMks!-I`k", byteArray0);
        assertEquals("0&#GMks!-I`k0&#GMks!-I`k0", string0);
    }
}
