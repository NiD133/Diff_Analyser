package org.apache.commons.codec.net;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import java.io.UnsupportedEncodingException;
import java.util.BitSet;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class URLCodec_ESTestTest33 extends URLCodec_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test32() throws Throwable {
        byte[] byteArray0 = new byte[1];
        byteArray0[0] = (byte) 37;
        try {
            URLCodec.decodeUrl(byteArray0);
            fail("Expecting exception: Exception");
        } catch (Exception e) {
            //
            // Invalid URL encoding:
            //
            verifyException("org.apache.commons.codec.net.URLCodec", e);
        }
    }
}
