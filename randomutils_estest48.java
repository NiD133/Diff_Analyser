package org.apache.commons.lang3;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import java.security.SecureRandom;
import java.util.Random;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class RandomUtils_ESTestTest48 extends RandomUtils_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test47() throws Throwable {
        RandomUtils randomUtils0 = RandomUtils.insecure();
        int int0 = randomUtils0.randomInt();
        //  // Unstable assertion: assertEquals(1322705324, int0);
    }
}
