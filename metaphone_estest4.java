package org.apache.commons.codec.language;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class Metaphone_ESTestTest4 extends Metaphone_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test03() throws Throwable {
        Metaphone metaphone0 = new Metaphone();
        assertEquals(4, metaphone0.getMaxCodeLen());
        metaphone0.setMaxCodeLen(0);
        int int0 = metaphone0.getMaxCodeLen();
        assertEquals(0, int0);
    }
}
