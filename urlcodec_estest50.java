package org.apache.commons.codec.net;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import java.io.UnsupportedEncodingException;
import java.util.BitSet;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class URLCodec_ESTestTest50 extends URLCodec_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test49() throws Throwable {
        URLCodec uRLCodec0 = new URLCodec("");
        String string0 = uRLCodec0.getEncoding();
        assertEquals("", string0);
    }
}