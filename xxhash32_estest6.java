package org.apache.commons.codec.digest;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class XXHash32_ESTestTest6 extends XXHash32_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test05() throws Throwable {
        XXHash32 xXHash32_0 = new XXHash32();
        byte[] byteArray0 = new byte[25];
        byteArray0[1] = (byte) 16;
        xXHash32_0.update(byteArray0, (int) (byte) 16, 4);
        xXHash32_0.update(byteArray0, 1, 4);
        xXHash32_0.update(byteArray0, 0, (int) (byte) 16);
        long long0 = xXHash32_0.getValue();
        assertEquals(1465785993L, long0);
    }
}
