package org.apache.commons.codec.net;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import java.io.UnsupportedEncodingException;
import java.util.BitSet;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class URLCodec_ESTestTest32 extends URLCodec_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test31() throws Throwable {
        BitSet bitSet0 = URLCodec.WWW_FORM_URL;
        byte[] byteArray0 = URLCodec.encodeUrl(bitSet0, (byte[]) null);
        assertNull(byteArray0);
    }
}
