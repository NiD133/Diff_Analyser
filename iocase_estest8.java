package org.apache.commons.io;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class IOCase_ESTestTest8 extends IOCase_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test07() throws Throwable {
        IOCase iOCase0 = IOCase.INSENSITIVE;
        IOCase iOCase1 = IOCase.value(iOCase0, iOCase0);
        assertEquals(IOCase.INSENSITIVE, iOCase1);
    }
}
