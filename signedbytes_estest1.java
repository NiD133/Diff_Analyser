package com.google.common.primitives;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import java.util.Comparator;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class SignedBytes_ESTestTest1 extends SignedBytes_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test00() throws Throwable {
        byte byte0 = SignedBytes.saturatedCast((byte) (-128));
        assertEquals((byte) (-128), byte0);
    }
}
