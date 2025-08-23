package org.apache.commons.codec.net;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import java.io.UnsupportedEncodingException;
import java.util.BitSet;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class URLCodec_ESTestTest14 extends URLCodec_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test13() throws Throwable {
        byte[] byteArray0 = new byte[0];
        byte[] byteArray1 = URLCodec.decodeUrl(byteArray0);
        assertArrayEquals(new byte[] {}, byteArray1);
    }
}
