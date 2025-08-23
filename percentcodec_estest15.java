package org.apache.commons.codec.net;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class PercentCodec_ESTestTest15 extends PercentCodec_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test14() throws Throwable {
        PercentCodec percentCodec0 = new PercentCodec();
        byte[] byteArray0 = new byte[6];
        byteArray0[5] = (byte) 37;
        try {
            percentCodec0.decode(byteArray0);
            fail("Expecting exception: Exception");
        } catch (Exception e) {
            //
            // Invalid percent decoding:
            //
            verifyException("org.apache.commons.codec.net.PercentCodec", e);
        }
    }
}
