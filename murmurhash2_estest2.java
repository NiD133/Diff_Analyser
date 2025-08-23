package org.apache.commons.codec.digest;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class MurmurHash2_ESTestTest2 extends MurmurHash2_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test01() throws Throwable {
        long long0 = MurmurHash2.hash64("q%DCbQXCHT4'G\"^L");
        assertEquals(3105811143660689330L, long0);
    }
}
