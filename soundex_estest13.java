package org.apache.commons.codec.language;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class Soundex_ESTestTest13 extends Soundex_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test12() throws Throwable {
        Soundex soundex0 = Soundex.US_ENGLISH;
        String string0 = soundex0.US_ENGLISH.soundex(")");
        assertEquals("", string0);
    }
}