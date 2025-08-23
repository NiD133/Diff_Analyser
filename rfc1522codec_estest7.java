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

public class RFC1522Codec_ESTestTest7 extends RFC1522Codec_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test06() throws Throwable {
        QCodec qCodec0 = new QCodec();
        // Undeclared exception!
        try {
            qCodec0.encodeText("{i", (Charset) null);
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
        }
    }
}
