package org.apache.commons.codec.net;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import java.io.UnsupportedEncodingException;
import java.util.BitSet;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class URLCodec_ESTestTest9 extends URLCodec_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test08() throws Throwable {
        byte[] byteArray0 = new byte[0];
        BitSet bitSet0 = BitSet.valueOf(byteArray0);
        byte[] byteArray1 = URLCodec.encodeUrl(bitSet0, byteArray0);
        assertNotSame(byteArray1, byteArray0);
    }
}
