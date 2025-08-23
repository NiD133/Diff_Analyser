package org.apache.commons.codec.net;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class PercentCodec_ESTestTest9 extends PercentCodec_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test08() throws Throwable {
        PercentCodec percentCodec0 = new PercentCodec();
        try {
            percentCodec0.encode((Object) percentCodec0);
            fail("Expecting exception: Exception");
        } catch (Exception e) {
            //
            // Objects of type org.apache.commons.codec.net.PercentCodec cannot be Percent encoded
            //
            verifyException("org.apache.commons.codec.net.PercentCodec", e);
        }
    }
}
