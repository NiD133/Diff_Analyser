package org.apache.commons.codec.digest;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class MurmurHash2_ESTestTest34 extends MurmurHash2_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test33() throws Throwable {
        long long0 = MurmurHash2.hash64("}oZe|_r,wwn+'.Z");
        assertEquals((-823493256237211900L), long0);
    }
}
