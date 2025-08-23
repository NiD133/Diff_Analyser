package org.apache.commons.cli;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class OptionBuilder_ESTestTest28 extends OptionBuilder_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test27() throws Throwable {
        // Undeclared exception!
        try {
            OptionBuilder.withType((Object) "");
            fail("Expecting exception: ClassCastException");
        } catch (ClassCastException e) {
            //
            // java.lang.String cannot be cast to java.lang.Class
            //
            verifyException("org.apache.commons.cli.OptionBuilder", e);
        }
    }
}
