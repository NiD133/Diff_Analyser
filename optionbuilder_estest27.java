package org.apache.commons.cli;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class OptionBuilder_ESTestTest27 extends OptionBuilder_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test26() throws Throwable {
        OptionBuilder.isRequired();
        OptionBuilder.withLongOpt("");
        Option option0 = OptionBuilder.create();
        assertTrue(option0.isRequired());
        assertEquals((-1), option0.getArgs());
    }
}
