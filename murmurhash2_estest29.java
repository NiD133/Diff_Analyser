package org.apache.commons.codec.digest;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class MurmurHash2_ESTestTest29 extends MurmurHash2_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test28() throws Throwable {
        byte[] byteArray0 = new byte[6];
        // Undeclared exception!
        try {
            MurmurHash2.hash32(byteArray0, (int) (byte) (-1), (-652));
            fail("Expecting exception: ArrayIndexOutOfBoundsException");
        } catch (ArrayIndexOutOfBoundsException e) {
            //
            // -2
            //
            verifyException("org.apache.commons.codec.digest.MurmurHash2", e);
        }
    }
}
