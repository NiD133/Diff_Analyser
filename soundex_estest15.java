package org.apache.commons.codec.language;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class Soundex_ESTestTest15 extends Soundex_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test14() throws Throwable {
        Soundex soundex0 = Soundex.US_ENGLISH_GENEALOGY;
        String string0 = soundex0.soundex("org.apache.commons.codec.language.Soundex");
        assertEquals("O621", string0);
    }
}
