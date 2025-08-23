package org.apache.commons.io;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class IOCase_ESTestTest11 extends IOCase_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test10() throws Throwable {
        IOCase iOCase0 = IOCase.SYSTEM;
        IOCase iOCase1 = IOCase.value(iOCase0, iOCase0);
        assertEquals(IOCase.SYSTEM, iOCase1);
        int int0 = iOCase1.checkCompareTo("\"#s6?ulinuxmwf", "#s6?u");
        assertEquals((-1), int0);
    }
}
