package org.apache.commons.codec.language;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class RefinedSoundex_ESTestTest16 extends RefinedSoundex_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test15() throws Throwable {
        RefinedSoundex refinedSoundex0 = new RefinedSoundex();
        try {
            refinedSoundex0.encode((Object) null);
            fail("Expecting exception: Exception");
        } catch (Exception e) {
            //
            // Parameter supplied to RefinedSoundex encode is not of type java.lang.String
            //
            verifyException("org.apache.commons.codec.language.RefinedSoundex", e);
        }
    }
}
