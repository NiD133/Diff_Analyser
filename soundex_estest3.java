package org.apache.commons.codec.language;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class Soundex_ESTestTest3 extends Soundex_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test02() throws Throwable {
        char[] charArray0 = new char[3];
        Soundex soundex0 = new Soundex(charArray0);
        String string0 = soundex0.US_ENGLISH_GENEALOGY.encode((String) null);
        assertNull(string0);
        assertEquals(4, soundex0.getMaxLength());
    }
}
