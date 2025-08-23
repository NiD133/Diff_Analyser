package org.apache.commons.codec.digest;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class MurmurHash2_ESTestTest27 extends MurmurHash2_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test26() throws Throwable {
        byte[] byteArray0 = new byte[7];
        // Undeclared exception!
        try {
            MurmurHash2.hash64(byteArray0, 1677, 496);
            fail("Expecting exception: ArrayIndexOutOfBoundsException");
        } catch (ArrayIndexOutOfBoundsException e) {
            //
            // 7
            //
            verifyException("org.apache.commons.codec.digest.MurmurHash2", e);
        }
    }
}
