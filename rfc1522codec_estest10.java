package org.apache.commons.codec.net;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.nio.charset.IllegalCharsetNameException;
import java.nio.charset.UnsupportedCharsetException;
import org.apache.commons.codec.CodecPolicy;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class RFC1522Codec_ESTestTest10 extends RFC1522Codec_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test09() throws Throwable {
        BCodec bCodec0 = new BCodec();
        // Undeclared exception!
        try {
            bCodec0.encodeText("0T5`BTkU*|f-hr", (String) null);
            fail("Expecting exception: IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            //
            // Null charset name
            //
            verifyException("java.nio.charset.Charset", e);
        }
    }
}
