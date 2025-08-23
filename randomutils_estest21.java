package org.apache.commons.lang3;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import java.security.SecureRandom;
import java.util.Random;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class RandomUtils_ESTestTest21 extends RandomUtils_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test20() throws Throwable {
        RandomUtils randomUtils0 = RandomUtils.secure();
        double double0 = randomUtils0.randomDouble(117.68, 2958.43561);
        //  // Unstable assertion: assertEquals(1506.2807688613188, double0, 0.01);
    }
}
