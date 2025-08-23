package com.google.common.primitives;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import java.util.Comparator;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class SignedBytes_ESTestTest30 extends SignedBytes_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test29() throws Throwable {
        byte[] byteArray0 = new byte[3];
        SignedBytes.sortDescending(byteArray0);
        assertEquals(3, byteArray0.length);
    }
}
