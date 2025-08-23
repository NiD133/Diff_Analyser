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

public class BCodec_ESTestTest3 extends BCodec_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test02() throws Throwable {
        BCodec bCodec0 = new BCodec();
        Charset charset0 = Charset.defaultCharset();
        String string0 = bCodec0.encode("^QOTD7,4PZ$(<r", charset0);
        assertEquals("=?UTF-8?B?XlFPVEQ3LDRQWiQoPHI=?=", string0);
    }
}
