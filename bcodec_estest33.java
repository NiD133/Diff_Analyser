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

public class BCodec_ESTestTest33 extends BCodec_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test32() throws Throwable {
        BCodec bCodec0 = new BCodec();
        String string0 = bCodec0.decode("=?UTF-8?B?IGVuY29kZWQgY29udGVudA==?=");
        assertEquals(" encoded content", string0);
    }
}
