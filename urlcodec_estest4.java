package org.apache.commons.codec.net;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import java.io.UnsupportedEncodingException;
import java.util.BitSet;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class URLCodec_ESTestTest4 extends URLCodec_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test03() throws Throwable {
        URLCodec uRLCodec0 = new URLCodec("");
        try {
            uRLCodec0.decode("");
            fail("Expecting exception: Exception");
        } catch (Exception e) {
            //
            //
            //
            verifyException("org.apache.commons.codec.net.URLCodec", e);
        }
    }
}
