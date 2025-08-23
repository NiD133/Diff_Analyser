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

public class RFC1522Codec_ESTestTest5 extends RFC1522Codec_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test04() throws Throwable {
        QCodec qCodec0 = new QCodec();
        Charset charset0 = qCodec0.getCharset();
        CodecPolicy codecPolicy0 = CodecPolicy.STRICT;
        BCodec bCodec0 = new BCodec(charset0, codecPolicy0);
        String string0 = bCodec0.decodeText("=?UTF-8?B?PT9eLT89Pz0=?=");
        assertEquals("=?^-?=?=", string0);
    }
}
