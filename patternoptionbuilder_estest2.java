package org.apache.commons.cli;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class PatternOptionBuilder_ESTestTest2 extends PatternOptionBuilder_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test01() throws Throwable {
        // Undeclared exception!
        try {
            PatternOptionBuilder.parsePattern((String) null);
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            //
            // no message in exception (getMessage() returned null)
            //
            verifyException("org.apache.commons.cli.PatternOptionBuilder", e);
        }
    }
}
