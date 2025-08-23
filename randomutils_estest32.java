package org.apache.commons.lang3;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import java.security.SecureRandom;
import java.util.Random;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class RandomUtils_ESTestTest32 extends RandomUtils_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test31() throws Throwable {
        long long0 = RandomUtils.nextLong(1L, 6590828120745478464L);
        //  // Unstable assertion: assertEquals(2584399046869912767L, long0);
    }
}
