package org.apache.commons.codec.binary;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class BinaryCodec_ESTestTest15 extends BinaryCodec_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test14() throws Throwable {
        BinaryCodec binaryCodec0 = new BinaryCodec();
        try {
            binaryCodec0.decode((Object) binaryCodec0);
            fail("Expecting exception: Exception");
        } catch (Exception e) {
            //
            // argument not a byte array
            //
            verifyException("org.apache.commons.codec.binary.BinaryCodec", e);
        }
    }
}
