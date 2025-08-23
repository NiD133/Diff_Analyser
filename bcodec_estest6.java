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

public class BCodec_ESTestTest6 extends BCodec_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test05() throws Throwable {
        Charset charset0 = Charset.defaultCharset();
        BCodec bCodec0 = new BCodec(charset0);
        byte[] byteArray0 = new byte[7];
        byte[] byteArray1 = bCodec0.doEncoding(byteArray0);
        assertEquals(12, byteArray1.length);
    }
}
