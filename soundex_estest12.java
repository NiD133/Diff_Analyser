package org.apache.commons.codec.language;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class Soundex_ESTestTest12 extends Soundex_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test11() throws Throwable {
        Soundex soundex0 = Soundex.US_ENGLISH;
        String string0 = soundex0.soundex("}}'3N[nM+hnR ayR6");
        assertEquals("N660", string0);
    }
}
