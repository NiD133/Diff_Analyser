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

public class QuotedPrintableCodec_ESTestTest56 extends QuotedPrintableCodec_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test55() throws Throwable {
        byte[] byteArray0 = new byte[9];
        byteArray0[4] = (byte) 46;
        byteArray0[5] = (byte) (-14);
        BitSet bitSet0 = BitSet.valueOf(byteArray0);
        byte[] byteArray1 = QuotedPrintableCodec.encodeQuotedPrintable(bitSet0, byteArray0, false);
        assertFalse(byteArray1.equals((Object) byteArray0));
    }
}
