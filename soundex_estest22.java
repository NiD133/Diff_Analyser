package org.apache.commons.codec.language;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class Soundex_ESTestTest22 extends Soundex_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test21() throws Throwable {
        char[] charArray0 = new char[8];
        Soundex soundex0 = new Soundex(charArray0);
        String string0 = soundex0.encode("");
        assertEquals(4, soundex0.getMaxLength());
        assertEquals("", string0);
        assertNotNull(string0);
    }
}
