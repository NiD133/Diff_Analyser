package org.apache.commons.codec.language;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class RefinedSoundex_ESTestTest1 extends RefinedSoundex_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test00() throws Throwable {
        RefinedSoundex refinedSoundex0 = new RefinedSoundex("U>");
        int int0 = refinedSoundex0.difference("org.apache.commons.codec.EncoderException", "org.apache.commons.codec.EncoderException");
        assertEquals(3, int0);
    }
}