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

public class QuotedPrintableCodec_ESTestTest60 extends QuotedPrintableCodec_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test59() throws Throwable {
        byte[] byteArray0 = new byte[38];
        byte[] byteArray1 = QuotedPrintableCodec.encodeQuotedPrintable((BitSet) null, byteArray0, true);
        assertEquals(117, byteArray1.length);
        assertNotNull(byteArray1);
    }
}
