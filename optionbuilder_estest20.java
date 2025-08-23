package org.apache.commons.cli;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class OptionBuilder_ESTestTest20 extends OptionBuilder_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test19() throws Throwable {
        OptionBuilder.hasOptionalArgs();
        Option option0 = OptionBuilder.create('7');
        assertTrue(option0.hasOptionalArg());
        assertEquals((-2), option0.getArgs());
        assertEquals(55, option0.getId());
    }
}
