package org.apache.commons.cli;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class OptionBuilder_ESTestTest12 extends OptionBuilder_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test11() throws Throwable {
        OptionBuilder.withLongOpt("");
        OptionBuilder.withArgName("=09QQ7 7&");
        Option option0 = OptionBuilder.create();
        assertEquals((-1), option0.getArgs());
    }
}
