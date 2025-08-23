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

public class RFC1522Codec_ESTestTest8 extends RFC1522Codec_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test07() throws Throwable {
        BCodec bCodec0 = new BCodec();
        // Undeclared exception!
        try {
            bCodec0.encodeText("org.apache.commons.codec.net.RFC1522Codec", "org.apache.commons.codec.net.RFC1522Codec");
            fail("Expecting exception: UnsupportedCharsetException");
        } catch (UnsupportedCharsetException e) {
            //
            // org.apache.commons.codec.net.RFC1522Codec
            //
            verifyException("java.nio.charset.Charset", e);
        }
    }
}
