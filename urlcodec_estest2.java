package org.apache.commons.codec.net;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import java.io.UnsupportedEncodingException;
import java.util.BitSet;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class URLCodec_ESTestTest2 extends URLCodec_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test01() throws Throwable {
        URLCodec uRLCodec0 = new URLCodec();
        byte[] byteArray0 = new byte[7];
        byteArray0[2] = (byte) (-63);
        byte[] byteArray1 = uRLCodec0.encode(byteArray0);
        byte[] byteArray2 = uRLCodec0.decode(byteArray1);
        assertArrayEquals(new byte[] { (byte) 0, (byte) 0, (byte) (-63), (byte) 0, (byte) 0, (byte) 0, (byte) 0 }, byteArray2);
        assertEquals(7, byteArray2.length);
        assertEquals(21, byteArray1.length);
    }
}
