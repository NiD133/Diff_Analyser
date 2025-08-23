package org.apache.commons.codec.language;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class Soundex_ESTestTest17 extends Soundex_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test16() throws Throwable {
        Soundex soundex0 = new Soundex();
        soundex0.soundex((String) null);
        assertEquals(4, soundex0.getMaxLength());
    }
}
