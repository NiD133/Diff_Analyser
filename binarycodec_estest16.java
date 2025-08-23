package org.apache.commons.codec.binary;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class BinaryCodec_ESTestTest16 extends BinaryCodec_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test15() throws Throwable {
        BinaryCodec binaryCodec0 = new BinaryCodec();
        Object object0 = binaryCodec0.decode((Object) "(mj0N>],r1/P");
        Object object1 = binaryCodec0.decode(object0);
        assertNotSame(object1, object0);
    }
}
