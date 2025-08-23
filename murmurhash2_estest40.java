package org.apache.commons.codec.digest;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class MurmurHash2_ESTestTest40 extends MurmurHash2_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test39() throws Throwable {
        int int0 = MurmurHash2.hash32("org.apache.commons.codec.binary.StringUtils");
        assertEquals((-1819289676), int0);
    }
}
