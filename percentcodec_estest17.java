package org.apache.commons.codec.net;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class PercentCodec_ESTestTest17 extends PercentCodec_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test16() throws Throwable {
        byte[] byteArray0 = new byte[1];
        PercentCodec percentCodec0 = new PercentCodec((byte[]) null, true);
        byte[] byteArray1 = percentCodec0.encode(byteArray0);
        assertSame(byteArray1, byteArray0);
        assertNotNull(byteArray1);
    }
}
