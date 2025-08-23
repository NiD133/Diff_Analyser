package org.apache.commons.codec.net;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class PercentCodec_ESTestTest8 extends PercentCodec_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test07() throws Throwable {
        PercentCodec percentCodec0 = new PercentCodec();
        Object object0 = percentCodec0.encode((Object) null);
        assertNull(object0);
    }
}
