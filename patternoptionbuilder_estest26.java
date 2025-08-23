package org.apache.commons.cli;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class PatternOptionBuilder_ESTestTest26 extends PatternOptionBuilder_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test25() throws Throwable {
        boolean boolean0 = PatternOptionBuilder.isValueCode('>');
        assertTrue(boolean0);
    }
}
