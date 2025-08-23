package org.apache.commons.codec.net;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import java.io.UnsupportedEncodingException;
import java.util.BitSet;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class URLCodec_ESTestTest1 extends URLCodec_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test00() throws Throwable {
        byte[] byteArray0 = new byte[3];
        byteArray0[0] = (byte) 65;
        BitSet bitSet0 = BitSet.valueOf(byteArray0);
        byte[] byteArray1 = URLCodec.encodeUrl(bitSet0, byteArray0);
        assertArrayEquals(new byte[] { (byte) 37, (byte) 52, (byte) 49, (byte) 0, (byte) 0 }, byteArray1);
    }
}
