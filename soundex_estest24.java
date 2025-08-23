package org.apache.commons.codec.language;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class Soundex_ESTestTest24 extends Soundex_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test23() throws Throwable {
        Soundex soundex0 = new Soundex("org.apache.commons.codec.EncoderException", false);
        Object object0 = new Object();
        try {
            soundex0.US_ENGLISH_SIMPLIFIED.encode(object0);
            fail("Expecting exception: Exception");
        } catch (Exception e) {
            //
            // Parameter supplied to Soundex encode is not of type java.lang.String
            //
            verifyException("org.apache.commons.codec.language.Soundex", e);
        }
    }
}
