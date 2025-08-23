package org.apache.commons.codec.digest;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class MurmurHash2_ESTestTest25 extends MurmurHash2_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test24() throws Throwable {
        // Undeclared exception!
        try {
            MurmurHash2.hash64((byte[]) null, (-420), (-420));
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            //
            // no message in exception (getMessage() returned null)
            //
            verifyException("org.apache.commons.codec.digest.MurmurHash2", e);
        }
    }
}
