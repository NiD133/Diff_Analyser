package org.apache.commons.lang3;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import java.security.SecureRandom;
import java.util.Random;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class RandomUtils_ESTestTest49 extends RandomUtils_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test48() throws Throwable {
        // Undeclared exception!
        try {
            RandomUtils.nextBytes((-669));
            fail("Expecting exception: IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            //
            // Count cannot be negative.
            //
            verifyException("org.apache.commons.lang3.Validate", e);
        }
    }
}
