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

public class QuotedPrintableCodec_ESTestTest3 extends QuotedPrintableCodec_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test02() throws Throwable {
        byte[] byteArray0 = new byte[18];
        byte[] byteArray1 = QuotedPrintableCodec.encodeQuotedPrintable((BitSet) null, byteArray0, false);
        byte[] byteArray2 = QuotedPrintableCodec.encodeQuotedPrintable((BitSet) null, byteArray1, true);
        byte[] byteArray3 = QuotedPrintableCodec.decodeQuotedPrintable(byteArray2);
        assertEquals(54, byteArray3.length);
        assertNotNull(byteArray3);
        assertEquals(93, byteArray2.length);
    }
}
