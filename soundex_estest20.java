package org.apache.commons.codec.language;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class Soundex_ESTestTest20 extends Soundex_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test19() throws Throwable {
        char[] charArray0 = new char[1];
        charArray0[0] = '-';
        Soundex soundex0 = new Soundex(charArray0);
        assertEquals(4, soundex0.getMaxLength());
    }
}