package org.apache.commons.codec.language;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class Metaphone_ESTestTest3 extends Metaphone_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test02() throws Throwable {
        Metaphone metaphone0 = new Metaphone();
        metaphone0.isMetaphoneEqual("", "");
        assertEquals(4, metaphone0.getMaxCodeLen());
    }
}
