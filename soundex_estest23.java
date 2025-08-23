package org.apache.commons.codec.language;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class Soundex_ESTestTest23 extends Soundex_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test22() throws Throwable {
        Soundex soundex0 = new Soundex();
        int int0 = soundex0.getMaxLength();
        assertEquals(4, int0);
    }
}
