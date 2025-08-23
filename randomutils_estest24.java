package org.apache.commons.lang3;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import java.security.SecureRandom;
import java.util.Random;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class RandomUtils_ESTestTest24 extends RandomUtils_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test23() throws Throwable {
        RandomUtils randomUtils0 = RandomUtils.insecure();
        // Undeclared exception!
        try {
            randomUtils0.randomDouble(2316.0, 0.0);
            fail("Expecting exception: IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            //
            // Start value must be smaller or equal to end value.
            //
            verifyException("org.apache.commons.lang3.Validate", e);
        }
    }
}
