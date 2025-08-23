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

public class BCodec_ESTestTest34 extends BCodec_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test33() throws Throwable {
        BCodec bCodec0 = new BCodec("l9");
        String string0 = bCodec0.encode("l9");
        assertEquals("=?ISO-8859-15?B?bDk=?=", string0);
    }
}