package org.apache.commons.codec.net;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.nio.charset.IllegalCharsetNameException;
import java.nio.charset.UnsupportedCharsetException;
import java.util.BitSet;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class QuotedPrintableCodec_ESTestTest1 extends QuotedPrintableCodec_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test00() throws Throwable {
        byte[] byteArray0 = new byte[8];
        long[] longArray0 = new long[9];
        longArray0[0] = (long) (byte) 125;
        BitSet bitSet0 = BitSet.valueOf(longArray0);
        byte[] byteArray1 = QuotedPrintableCodec.encodeQuotedPrintable(bitSet0, byteArray0, true);
        assertEquals(8, byteArray1.length);
        assertArrayEquals(new byte[] { (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0 }, byteArray1);
        assertNotNull(byteArray1);
    }
}