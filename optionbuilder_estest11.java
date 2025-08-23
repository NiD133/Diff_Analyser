package org.apache.commons.cli;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class OptionBuilder_ESTestTest11 extends OptionBuilder_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test10() throws Throwable {
        OptionBuilder.hasOptionalArgs(0);
        Option option0 = OptionBuilder.create('w');
        assertTrue(option0.hasOptionalArg());
        assertEquals(119, option0.getId());
        assertEquals(0, option0.getArgs());
    }
}