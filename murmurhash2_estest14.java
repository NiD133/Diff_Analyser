package org.apache.commons.codec.digest;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class MurmurHash2_ESTestTest14 extends MurmurHash2_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test13() throws Throwable {
        // Undeclared exception!
        try {
            MurmurHash2.hash64(",;9b|Qi Zv", (-2432), (-2432));
            fail("Expecting exception: StringIndexOutOfBoundsException");
        } catch (StringIndexOutOfBoundsException e) {
        }
    }
}
