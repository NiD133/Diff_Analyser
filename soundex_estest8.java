package org.apache.commons.codec.language;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class Soundex_ESTestTest8 extends Soundex_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test07() throws Throwable {
        Soundex soundex0 = null;
        try {
            soundex0 = new Soundex((char[]) null);
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            //
            // no message in exception (getMessage() returned null)
            //
            verifyException("org.apache.commons.codec.language.Soundex", e);
        }
    }
}
