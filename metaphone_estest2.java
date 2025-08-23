package org.apache.commons.codec.language;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class Metaphone_ESTestTest2 extends Metaphone_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test01() throws Throwable {
        Metaphone metaphone0 = new Metaphone();
        String string0 = metaphone0.encode("GtA");
        //  // Unstable assertion: assertEquals("K0", string0);
        //  // Unstable assertion: assertEquals(4, metaphone0.getMaxCodeLen());
    }
}
