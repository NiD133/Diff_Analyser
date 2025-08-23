package org.apache.commons.codec.digest;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class XXHash32_ESTestTest4 extends XXHash32_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test03() throws Throwable {
        XXHash32 xXHash32_0 = new XXHash32();
        xXHash32_0.update(2026);
        long long0 = xXHash32_0.getValue();
        assertEquals(968812856L, long0);
    }
}
