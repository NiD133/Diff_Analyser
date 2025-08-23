package org.apache.commons.io;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class IOCase_ESTestTest17 extends IOCase_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test16() throws Throwable {
        IOCase iOCase0 = IOCase.SENSITIVE;
        boolean boolean0 = iOCase0.checkRegionMatches("Sensitive", 258, "Insensitive");
        assertFalse(boolean0);
    }
}
