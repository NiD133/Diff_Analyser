package org.apache.commons.lang3;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import java.security.SecureRandom;
import java.util.Random;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class RandomUtils_ESTestTest13 extends RandomUtils_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test12() throws Throwable {
        RandomUtils randomUtils0 = RandomUtils.secureStrong();
        // Undeclared exception!
        try {
            randomUtils0.randomLong((long) (-9), (long) (-9));
            fail("Expecting exception: IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            //
            // Both range values must be non-negative.
            //
            verifyException("org.apache.commons.lang3.Validate", e);
        }
    }
}
