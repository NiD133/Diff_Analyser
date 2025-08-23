package org.apache.commons.codec.net;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class PercentCodec_ESTestTest5 extends PercentCodec_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test04() throws Throwable {
        PercentCodec percentCodec0 = new PercentCodec();
        byte[] byteArray0 = new byte[0];
        byte[] byteArray1 = percentCodec0.decode(byteArray0);
        assertEquals(0, byteArray1.length);
    }
}
