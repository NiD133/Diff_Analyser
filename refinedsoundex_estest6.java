package org.apache.commons.codec.language;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class RefinedSoundex_ESTestTest6 extends RefinedSoundex_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test05() throws Throwable {
        RefinedSoundex refinedSoundex0 = new RefinedSoundex();
        String string0 = refinedSoundex0.encode("");
        assertEquals("", string0);
    }
}
