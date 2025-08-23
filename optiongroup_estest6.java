package org.apache.commons.cli;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import java.util.Collection;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class OptionGroup_ESTestTest6 extends OptionGroup_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test05() throws Throwable {
        OptionGroup optionGroup0 = new OptionGroup();
        // Undeclared exception!
        try {
            optionGroup0.addOption((Option) null);
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            //
            // no message in exception (getMessage() returned null)
            //
            verifyException("org.apache.commons.cli.OptionGroup", e);
        }
    }
}
