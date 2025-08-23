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

public class QuotedPrintableCodec_ESTestTest59 extends QuotedPrintableCodec_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test58() throws Throwable {
        byte[] byteArray0 = new byte[17];
        byteArray0[8] = (byte) 9;
        byte[] byteArray1 = QuotedPrintableCodec.encodeQuotedPrintable((BitSet) null, byteArray0, true);
        byte[] byteArray2 = QuotedPrintableCodec.encodeQuotedPrintable((BitSet) null, byteArray1, true);
        byte[] byteArray3 = QuotedPrintableCodec.encodeQuotedPrintable((BitSet) null, byteArray2, true);
        byte[] byteArray4 = QuotedPrintableCodec.encodeQuotedPrintable((BitSet) null, byteArray3, true);
        assertNotNull(byteArray4);
        assertEquals(177, byteArray4.length);
    }
}
