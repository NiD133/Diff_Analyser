package org.apache.commons.codec.language;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class RefinedSoundex_ESTestTest14 extends RefinedSoundex_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test13() throws Throwable {
        RefinedSoundex refinedSoundex0 = new RefinedSoundex("org.apache.commons.codec.language.RefinedSoundex");
        String string0 = refinedSoundex0.encode("org.apache.commons.codec.language.RefinedSoundex");
        assertEquals("Omsaogcagmom.gm.agcomaoasaphma.mom.ac", string0);
        assertNotNull(string0);
    }
}
