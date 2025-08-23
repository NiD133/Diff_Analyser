package org.apache.commons.codec.language;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class Soundex_ESTestTest1 extends Soundex_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test00() throws Throwable {
        char[] charArray0 = new char[5];
        Soundex soundex0 = new Soundex(charArray0);
        // Undeclared exception!
        try {
            soundex0.difference("EF\"kniaAVspLJDz", "$I<`&-Dq*{");
            fail("Expecting exception: IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            //
            // The character is not mapped: F (index=5)
            //
            verifyException("org.apache.commons.codec.language.Soundex", e);
        }
    }
}
