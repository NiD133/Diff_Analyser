package org.apache.commons.codec.net;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import java.io.UnsupportedEncodingException;
import java.util.BitSet;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class URLCodec_ESTestTest27 extends URLCodec_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test26() throws Throwable {
        URLCodec uRLCodec0 = new URLCodec();
        // Undeclared exception!
        try {
            uRLCodec0.decode("_iXXSIH;6c", (String) null);
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
        }
    }
}
