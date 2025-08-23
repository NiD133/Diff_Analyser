package org.apache.commons.codec.language;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class Soundex_ESTestTest6 extends Soundex_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test05() throws Throwable {
        Soundex soundex0 = new Soundex("@'0g");
        // Undeclared exception!
        try {
            soundex0.encode("@'0g");
            fail("Expecting exception: IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            //
            // The character is not mapped: G (index=6)
            //
            verifyException("org.apache.commons.codec.language.Soundex", e);
        }
    }
}
