package org.apache.commons.lang3;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import java.security.SecureRandom;
import java.util.Random;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class RandomUtils_ESTestTest35 extends RandomUtils_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test34() throws Throwable {
        float float0 = RandomUtils.nextFloat(1.0F, 1.0F);
        assertEquals(1.0F, float0, 0.01F);
    }
}
