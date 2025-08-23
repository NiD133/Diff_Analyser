package org.apache.commons.cli;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class PatternOptionBuilder_ESTestTest31 extends PatternOptionBuilder_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test30() throws Throwable {
        // Undeclared exception!
        try {
            PatternOptionBuilder.parsePattern("4K<:s%L=GX$");
            fail("Expecting exception: IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            //
            // Illegal option name '='.
            //
            verifyException("org.apache.commons.cli.OptionValidator", e);
        }
    }
}
