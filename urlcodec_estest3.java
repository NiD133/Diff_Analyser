package org.apache.commons.codec.net;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import java.io.UnsupportedEncodingException;
import java.util.BitSet;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class URLCodec_ESTestTest3 extends URLCodec_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test02() throws Throwable {
        URLCodec uRLCodec0 = new URLCodec("`=G;6CyMxaWcZUTj%C");
        try {
            uRLCodec0.decode("`=G;6CyMxaWcZUTj%C");
            fail("Expecting exception: Exception");
        } catch (Exception e) {
            //
            // Invalid URL encoding:
            //
            verifyException("org.apache.commons.codec.net.URLCodec", e);
        }
    }
}
