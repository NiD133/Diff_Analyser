package org.apache.commons.codec.digest;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class XXHash32_ESTestTest10 extends XXHash32_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test09() throws Throwable {
        XXHash32 xXHash32_0 = new XXHash32();
        byte[] byteArray0 = new byte[25];
        // Undeclared exception!
        try {
            xXHash32_0.update(byteArray0, 374761393, (int) (byte) 16);
            fail("Expecting exception: ArrayIndexOutOfBoundsException");
        } catch (ArrayIndexOutOfBoundsException e) {
            //
            // 374761393
            //
            verifyException("org.apache.commons.codec.digest.XXHash32", e);
        }
    }
}
