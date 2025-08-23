package org.apache.commons.codec.net;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import java.io.UnsupportedEncodingException;
import java.util.BitSet;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class URLCodec_ESTestTest28 extends URLCodec_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test27() throws Throwable {
        URLCodec uRLCodec0 = new URLCodec();
        try {
            uRLCodec0.decode("org.apache.commons.codec.DecoderException", "org.apache.commons.codec.DecoderException");
            fail("Expecting exception: UnsupportedEncodingException");
        } catch (UnsupportedEncodingException e) {
        }
    }
}
