package org.apache.commons.codec.language;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class Soundex_ESTestTest16 extends Soundex_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test15() throws Throwable {
        Soundex soundex0 = new Soundex();
        int int0 = soundex0.difference("01230120022455012623010202", "6]5]'j=[IE=9");
        assertEquals(4, soundex0.getMaxLength());
        assertEquals(0, int0);
    }
}
