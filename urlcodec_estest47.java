package org.apache.commons.codec.net;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import java.io.UnsupportedEncodingException;
import java.util.BitSet;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class URLCodec_ESTestTest47 extends URLCodec_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test46() throws Throwable {
        byte[] byteArray0 = new byte[8];
        byteArray0[0] = (byte) 43;
        byte[] byteArray1 = URLCodec.decodeUrl(byteArray0);
        BitSet bitSet0 = URLCodec.WWW_FORM_URL;
        byte[] byteArray2 = URLCodec.encodeUrl(bitSet0, byteArray1);
        assertArrayEquals(new byte[] { (byte) 32, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0 }, byteArray1);
        assertEquals(22, byteArray2.length);
        assertNotNull(byteArray2);
    }
}
