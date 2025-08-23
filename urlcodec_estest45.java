package org.apache.commons.codec.net;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import java.io.UnsupportedEncodingException;
import java.util.BitSet;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class URLCodec_ESTestTest45 extends URLCodec_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test44() throws Throwable {
        byte[] byteArray0 = new byte[9];
        byte[] byteArray1 = URLCodec.encodeUrl((BitSet) null, byteArray0);
        assertEquals(27, byteArray1.length);
    }
}
