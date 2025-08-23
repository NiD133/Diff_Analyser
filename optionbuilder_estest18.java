package org.apache.commons.cli;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class OptionBuilder_ESTestTest18 extends OptionBuilder_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test17() throws Throwable {
        try {
            OptionBuilder.create();
            fail("Expecting exception: IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            //
            // must specify longopt
            //
            verifyException("org.apache.commons.cli.OptionBuilder", e);
        }
    }
}
