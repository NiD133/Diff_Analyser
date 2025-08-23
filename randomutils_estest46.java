package org.apache.commons.lang3;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import java.security.SecureRandom;
import java.util.Random;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class RandomUtils_ESTestTest46 extends RandomUtils_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test45() throws Throwable {
        RandomUtils randomUtils0 = new RandomUtils();
        double double0 = randomUtils0.randomDouble();
        //  // Unstable assertion: assertEquals(3.3080842984798963E307, double0, 0.01);
    }
}
