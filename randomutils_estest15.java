package org.apache.commons.lang3;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import java.security.SecureRandom;
import java.util.Random;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class RandomUtils_ESTestTest15 extends RandomUtils_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test14() throws Throwable {
        RandomUtils randomUtils0 = new RandomUtils();
        int int0 = randomUtils0.randomInt(228, Integer.MAX_VALUE);
        //  // Unstable assertion: assertEquals(811322057, int0);
    }
}
