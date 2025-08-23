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

public class QuotedPrintableCodec_ESTestTest18 extends QuotedPrintableCodec_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test17() throws Throwable {
        Charset charset0 = Charset.defaultCharset();
        QuotedPrintableCodec quotedPrintableCodec0 = new QuotedPrintableCodec(charset0);
        byte[] byteArray0 = new byte[0];
        byte[] byteArray1 = quotedPrintableCodec0.decode(byteArray0);
        byte[] byteArray2 = quotedPrintableCodec0.encode(byteArray1);
        assertNotSame(byteArray1, byteArray2);
    }
}
