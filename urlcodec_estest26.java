package org.apache.commons.codec.net;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import java.io.UnsupportedEncodingException;
import java.util.BitSet;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class URLCodec_ESTestTest26 extends URLCodec_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test25() throws Throwable {
        URLCodec uRLCodec0 = new URLCodec();
        try {
            uRLCodec0.decode("#6|\"J%kyWIe[[:g", "#6|\"J%kyWIe[[:g");
            fail("Expecting exception: Exception");
        } catch (Exception e) {
            //
            // Invalid URL encoding: not a valid digit (radix 16): 107
            //
            verifyException("org.apache.commons.codec.net.Utils", e);
        }
    }
}
