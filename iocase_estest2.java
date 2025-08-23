package org.apache.commons.io;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class IOCase_ESTestTest2 extends IOCase_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test01() throws Throwable {
        IOCase iOCase0 = IOCase.INSENSITIVE;
        int int0 = iOCase0.checkCompareTo("LINUX", "\"#S6?U_R7?'mwf");
        assertEquals(74, int0);
    }
}
