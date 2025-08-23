package org.apache.commons.codec.language;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class RefinedSoundex_ESTestTest17 extends RefinedSoundex_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test16() throws Throwable {
        RefinedSoundex refinedSoundex0 = new RefinedSoundex("U>");
        String string0 = refinedSoundex0.soundex("<+Eq|qK!wg0f\u0006n_~");
        assertEquals("E", string0);
        assertNotNull(string0);
    }
}
