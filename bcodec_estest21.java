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

public class BCodec_ESTestTest21 extends BCodec_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test20() throws Throwable {
        BCodec bCodec0 = null;
        try {
            bCodec0 = new BCodec((String) null);
            fail("Expecting exception: IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            //
            // Null charset name
            //
            verifyException("java.nio.charset.Charset", e);
        }
    }
}
