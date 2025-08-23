package org.apache.commons.codec.net;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import java.io.UnsupportedEncodingException;
import java.util.BitSet;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class URLCodec_ESTestTest24 extends URLCodec_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test23() throws Throwable {
        URLCodec uRLCodec0 = new URLCodec((String) null);
        // Undeclared exception!
        try {
            uRLCodec0.encode((Object) "4r9MeMtOx{]6dKda");
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
        }
    }
}
