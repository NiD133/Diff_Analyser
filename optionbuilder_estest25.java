package org.apache.commons.cli;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class OptionBuilder_ESTestTest25 extends OptionBuilder_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test24() throws Throwable {
        OptionBuilder.isRequired(true);
        Option option0 = OptionBuilder.create(' ');
        assertEquals((-1), option0.getArgs());
        assertEquals(127, option0.getId());
    }
}