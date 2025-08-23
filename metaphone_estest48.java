package org.apache.commons.codec.language;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class Metaphone_ESTestTest48 extends Metaphone_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test47() throws Throwable {
        Metaphone metaphone0 = new Metaphone();
        boolean boolean0 = metaphone0.isMetaphoneEqual("I", "X");
        assertEquals(4, metaphone0.getMaxCodeLen());
        assertFalse(boolean0);
    }
}
