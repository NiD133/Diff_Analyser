package org.apache.commons.codec.digest;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class XXHash32_ESTestTest1 extends XXHash32_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test00() throws Throwable {
        XXHash32 xXHash32_0 = new XXHash32();
        byte[] byteArray0 = new byte[25];
        xXHash32_0.update(byteArray0, 1336530510, 1336530510);
        assertEquals(2363252416L, xXHash32_0.getValue());
    }
}
