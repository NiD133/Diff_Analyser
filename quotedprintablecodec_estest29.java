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

public class QuotedPrintableCodec_ESTestTest29 extends QuotedPrintableCodec_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test28() throws Throwable {
        QuotedPrintableCodec quotedPrintableCodec0 = new QuotedPrintableCodec();
        byte[] byteArray0 = new byte[2];
        byteArray0[0] = (byte) 61;
        try {
            quotedPrintableCodec0.decode(byteArray0);
            fail("Expecting exception: Exception");
        } catch (Exception e) {
            //
            // Invalid URL encoding: not a valid digit (radix 16): 0
            //
            verifyException("org.apache.commons.codec.net.Utils", e);
        }
    }
}
