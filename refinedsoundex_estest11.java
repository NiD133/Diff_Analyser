package org.apache.commons.codec.language;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class RefinedSoundex_ESTestTest11 extends RefinedSoundex_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test10() throws Throwable {
        RefinedSoundex refinedSoundex0 = new RefinedSoundex();
        int int0 = refinedSoundex0.difference((String) null, (String) null);
        assertEquals(0, int0);
    }
}
