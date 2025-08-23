package org.apache.commons.codec.language;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class Soundex_ESTestTest7 extends Soundex_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test06() throws Throwable {
        Soundex soundex0 = new Soundex("]jH");
        // Undeclared exception!
        try {
            soundex0.encode((Object) "]jH");
            fail("Expecting exception: IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            //
            // The character is not mapped: J (index=9)
            //
            verifyException("org.apache.commons.codec.language.Soundex", e);
        }
    }
}
