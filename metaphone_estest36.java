package org.apache.commons.codec.language;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class Metaphone_ESTestTest36 extends Metaphone_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test35() throws Throwable {
        Metaphone metaphone0 = new Metaphone();
        String string0 = metaphone0.metaphone("wT0mBTkI2");
        //  // Unstable assertion: assertEquals("XMBX", string0);
    }
}
