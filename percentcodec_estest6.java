package org.apache.commons.codec.net;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class PercentCodec_ESTestTest6 extends PercentCodec_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test05() throws Throwable {
        byte[] byteArray0 = new byte[1];
        byteArray0[0] = (byte) (-50);
        PercentCodec percentCodec0 = null;
        try {
            percentCodec0 = new PercentCodec(byteArray0, true);
            fail("Expecting exception: IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            //
            // byte must be >= 0
            //
            verifyException("org.apache.commons.codec.net.PercentCodec", e);
        }
    }
}
