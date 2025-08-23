package org.apache.commons.codec.digest;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class XXHash32_ESTestTest12 extends XXHash32_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test11() throws Throwable {
        byte[] byteArray0 = new byte[62];
        XXHash32 xXHash32_0 = new XXHash32();
        xXHash32_0.update(byteArray0, (int) (byte) 21, (int) (byte) 21);
        long long0 = xXHash32_0.getValue();
        assertEquals(86206869L, long0);
    }
}
