package org.apache.commons.codec.digest;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class MurmurHash2_ESTestTest32 extends MurmurHash2_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test31() throws Throwable {
        byte[] byteArray0 = new byte[0];
        // Undeclared exception!
        try {
            MurmurHash2.hash64(byteArray0, 2441);
            fail("Expecting exception: ArrayIndexOutOfBoundsException");
        } catch (ArrayIndexOutOfBoundsException e) {
            //
            // 0
            //
            verifyException("org.apache.commons.codec.digest.MurmurHash2", e);
        }
    }
}
