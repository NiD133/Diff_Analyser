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

public class QuotedPrintableCodec_ESTestTest35 extends QuotedPrintableCodec_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test34() throws Throwable {
        QuotedPrintableCodec quotedPrintableCodec0 = null;
        try {
            quotedPrintableCodec0 = new QuotedPrintableCodec("g");
            fail("Expecting exception: UnsupportedCharsetException");
        } catch (UnsupportedCharsetException e) {
            //
            // g
            //
            verifyException("java.nio.charset.Charset", e);
        }
    }
}
