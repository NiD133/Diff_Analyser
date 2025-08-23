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

public class QuotedPrintableCodec_ESTestTest12 extends QuotedPrintableCodec_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test11() throws Throwable {
        QuotedPrintableCodec quotedPrintableCodec0 = new QuotedPrintableCodec(false);
        String string0 = quotedPrintableCodec0.encode("UTF-8", "UTF-8");
        assertEquals("UTF-8", string0);
    }
}
