package org.apache.commons.lang3;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import java.security.SecureRandom;
import java.util.Random;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class RandomUtils_ESTestTest37 extends RandomUtils_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test36() throws Throwable {
        double double0 = RandomUtils.nextDouble(269.70357193641604, 269.70357193641604);
        assertEquals(269.70357193641604, double0, 0.01);
    }
}
