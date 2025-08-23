package com.google.common.primitives;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import java.util.Comparator;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class SignedBytes_ESTestTest28 extends SignedBytes_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test27() throws Throwable {
        byte byte0 = SignedBytes.checkedCast((byte) 39);
        assertEquals((byte) 39, byte0);
    }
}
