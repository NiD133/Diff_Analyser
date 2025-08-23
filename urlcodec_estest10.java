package org.apache.commons.codec.net;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import java.io.UnsupportedEncodingException;
import java.util.BitSet;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class URLCodec_ESTestTest10 extends URLCodec_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test09() throws Throwable {
        URLCodec uRLCodec0 = new URLCodec();
        byte[] byteArray0 = new byte[0];
        byte[] byteArray1 = uRLCodec0.encode(byteArray0);
        assertEquals(0, byteArray1.length);
    }
}
