package org.apache.commons.codec.net;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import java.io.UnsupportedEncodingException;
import java.util.BitSet;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class URLCodec_ESTestTest31 extends URLCodec_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test30() throws Throwable {
        byte[] byteArray0 = new byte[8];
        byteArray0[3] = (byte) (-93);
        BitSet bitSet0 = URLCodec.WWW_FORM_URL;
        byte[] byteArray1 = URLCodec.encodeUrl(bitSet0, byteArray0);
        assertEquals(24, byteArray1.length);
    }
}
