package org.apache.commons.codec.net;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import java.io.UnsupportedEncodingException;
import java.util.BitSet;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class URLCodec_ESTestTest30 extends URLCodec_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test29() throws Throwable {
        URLCodec uRLCodec0 = new URLCodec("org.apache.commons.codec.CharEncoding");
        try {
            uRLCodec0.encode("org.apache.commons.codec.CharEncoding", "org.apache.commons.codec.CharEncoding");
            fail("Expecting exception: UnsupportedEncodingException");
        } catch (UnsupportedEncodingException e) {
        }
    }
}
