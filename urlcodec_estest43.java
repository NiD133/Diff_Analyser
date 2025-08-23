package org.apache.commons.codec.net;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import java.io.UnsupportedEncodingException;
import java.util.BitSet;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class URLCodec_ESTestTest43 extends URLCodec_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test42() throws Throwable {
        URLCodec uRLCodec0 = new URLCodec("3X{`l{LuA3.3r K44");
        Object object0 = new Object();
        try {
            uRLCodec0.decode(object0);
            fail("Expecting exception: Exception");
        } catch (Exception e) {
            //
            // Objects of type java.lang.Object cannot be URL decoded
            //
            verifyException("org.apache.commons.codec.net.URLCodec", e);
        }
    }
}
