package com.google.common.primitives;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import java.util.Comparator;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class SignedBytes_ESTestTest10 extends SignedBytes_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test09() throws Throwable {
        int int0 = SignedBytes.compare((byte) 111, (byte) 0);
        assertEquals(111, int0);
    }
}
