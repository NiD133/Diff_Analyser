package org.apache.commons.codec.net;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import java.io.UnsupportedEncodingException;
import java.util.BitSet;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class URLCodec_ESTestTest44 extends URLCodec_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test43() throws Throwable {
        URLCodec uRLCodec0 = new URLCodec();
        String string0 = uRLCodec0.encode("org.apache.commons.codec.DecoderException");
        assertEquals("org.apache.commons.codec.DecoderException", string0);
        assertNotNull(string0);
    }
}
