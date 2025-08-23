package com.google.common.primitives;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import java.util.Comparator;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class SignedBytes_ESTestTest31 extends SignedBytes_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test30() throws Throwable {
        int int0 = SignedBytes.compare((byte) 0, (byte) 84);
        assertEquals((-84), int0);
    }
}
