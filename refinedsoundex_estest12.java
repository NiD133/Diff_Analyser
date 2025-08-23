package org.apache.commons.codec.language;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class RefinedSoundex_ESTestTest12 extends RefinedSoundex_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test11() throws Throwable {
        RefinedSoundex refinedSoundex0 = new RefinedSoundex("org.apache.commons.codec.language.RefinedSoundex");
        char char0 = refinedSoundex0.getMappingCode('+');
        assertEquals('\u0000', char0);
    }
}
