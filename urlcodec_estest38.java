package org.apache.commons.codec.net;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import java.io.UnsupportedEncodingException;
import java.util.BitSet;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class URLCodec_ESTestTest38 extends URLCodec_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test37() throws Throwable {
        URLCodec uRLCodec0 = new URLCodec();
        Object object0 = uRLCodec0.encode((Object) " cannot be URL decoded");
        assertNotNull(object0);
        assertEquals("+cannot+be+URL+decoded", object0);
    }
}
