package org.apache.commons.codec.digest;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class MurmurHash2_ESTestTest1 extends MurmurHash2_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test00() throws Throwable {
        long long0 = MurmurHash2.hash64("bPH \"XdK'x'8?hr", 4, 0);
        assertEquals((-7207201254813729732L), long0);
    }
}
