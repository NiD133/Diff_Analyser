package org.apache.commons.codec.language;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class Soundex_ESTestTest11 extends Soundex_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test10() throws Throwable {
        Soundex soundex0 = new Soundex();
        String string0 = soundex0.US_ENGLISH.soundex("[&L!ug<F4wFviM+`RV{");
        assertEquals("L215", string0);
        assertEquals(4, soundex0.getMaxLength());
    }
}
