package org.apache.commons.codec.digest;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class XXHash32_ESTestTest5 extends XXHash32_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test04() throws Throwable {
        XXHash32 xXHash32_0 = new XXHash32();
        xXHash32_0.update(2);
        xXHash32_0.update(2);
        xXHash32_0.update((int) (byte) 0);
        xXHash32_0.update(8);
        long long0 = xXHash32_0.getValue();
        assertEquals(1429036944L, long0);
    }
}
