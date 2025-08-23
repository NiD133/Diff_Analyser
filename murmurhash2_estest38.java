package org.apache.commons.codec.digest;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class MurmurHash2_ESTestTest38 extends MurmurHash2_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test37() throws Throwable {
        int int0 = MurmurHash2.hash32("9chG_Yo[`m", 1, 1);
        assertEquals((-1877468854), int0);
    }
}
