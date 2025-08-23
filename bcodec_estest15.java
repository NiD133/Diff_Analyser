package org.apache.commons.codec.net;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import java.nio.charset.Charset;
import java.nio.charset.IllegalCharsetNameException;
import java.nio.charset.UnsupportedCharsetException;
import org.apache.commons.codec.CodecPolicy;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class BCodec_ESTestTest15 extends BCodec_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test14() throws Throwable {
        Charset charset0 = Charset.defaultCharset();
        CodecPolicy codecPolicy0 = CodecPolicy.STRICT;
        BCodec bCodec0 = new BCodec(charset0, codecPolicy0);
        byte[] byteArray0 = new byte[22];
        byteArray0[3] = (byte) 45;
        // Undeclared exception!
        try {
            bCodec0.doDecoding(byteArray0);
            fail("Expecting exception: IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            //
            // Strict decoding: Last encoded character (before the paddings if any) is a valid base 64 alphabet but not a possible encoding. Decoding requires at least two trailing 6-bit characters to create bytes.
            //
            verifyException("org.apache.commons.codec.binary.Base64", e);
        }
    }
}
