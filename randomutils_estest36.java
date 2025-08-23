package org.apache.commons.lang3;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import java.security.SecureRandom;
import java.util.Random;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class RandomUtils_ESTestTest36 extends RandomUtils_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test35() throws Throwable {
        // Undeclared exception!
        try {
            RandomUtils.nextFloat((float) 254551010162654302L, (-145.13795F));
            fail("Expecting exception: IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            //
            // Start value must be smaller or equal to end value.
            //
            verifyException("org.apache.commons.lang3.Validate", e);
        }
    }
}
