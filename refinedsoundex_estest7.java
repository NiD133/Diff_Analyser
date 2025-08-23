package org.apache.commons.codec.language;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class RefinedSoundex_ESTestTest7 extends RefinedSoundex_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test06() throws Throwable {
        RefinedSoundex refinedSoundex0 = null;
        try {
            refinedSoundex0 = new RefinedSoundex((String) null);
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            //
            // no message in exception (getMessage() returned null)
            //
            verifyException("org.apache.commons.codec.language.RefinedSoundex", e);
        }
    }
}
