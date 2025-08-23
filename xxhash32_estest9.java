package org.apache.commons.codec.digest;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class XXHash32_ESTestTest9 extends XXHash32_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test08() throws Throwable {
        XXHash32 xXHash32_0 = new XXHash32();
        // Undeclared exception!
        try {
            xXHash32_0.update((byte[]) null, 2128, 2128);
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            //
            // no message in exception (getMessage() returned null)
            //
            verifyException("org.apache.commons.codec.digest.XXHash32", e);
        }
    }
}
