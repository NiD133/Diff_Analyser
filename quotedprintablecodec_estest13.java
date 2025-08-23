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

public class QuotedPrintableCodec_ESTestTest13 extends QuotedPrintableCodec_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test12() throws Throwable {
        QuotedPrintableCodec quotedPrintableCodec0 = new QuotedPrintableCodec(true);
        Charset charset0 = quotedPrintableCodec0.getCharset();
        QuotedPrintableCodec quotedPrintableCodec1 = new QuotedPrintableCodec(charset0);
        String string0 = quotedPrintableCodec1.encode("", "UTF-8");
        assertEquals("", string0);
        assertNotNull(string0);
    }
}