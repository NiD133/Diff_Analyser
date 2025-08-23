package org.apache.commons.cli;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class PatternOptionBuilder_ESTestTest24 extends PatternOptionBuilder_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test23() throws Throwable {
        Options options0 = PatternOptionBuilder.parsePattern("0dpy>mb!!Q*1_");
        assertNotNull(options0);
    }
}
