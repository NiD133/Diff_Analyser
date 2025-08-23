package org.apache.commons.codec.language;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class Metaphone_ESTestTest46 extends Metaphone_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test45() throws Throwable {
        Metaphone metaphone0 = new Metaphone();
        try {
            metaphone0.encode((Object) metaphone0);
            fail("Expecting exception: Exception");
        } catch (Exception e) {
            //
            // Parameter supplied to Metaphone encode is not of type java.lang.String
            //
            verifyException("org.apache.commons.codec.language.Metaphone", e);
        }
    }
}
