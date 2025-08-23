package org.apache.commons.cli;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class OptionBuilder_ESTestTest6 extends OptionBuilder_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test05() throws Throwable {
        OptionBuilder.hasOptionalArgs(0);
        Option option0 = OptionBuilder.create((String) null);
        assertEquals(0, option0.getArgs());
        assertTrue(option0.hasOptionalArg());
    }
}