package org.apache.commons.io;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class IOCase_ESTestTest4 extends IOCase_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test03() throws Throwable {
        IOCase iOCase0 = IOCase.SENSITIVE;
        IOCase iOCase1 = IOCase.INSENSITIVE;
        IOCase iOCase2 = IOCase.value(iOCase0, iOCase1);
        assertEquals(IOCase.SENSITIVE, iOCase2);
    }
}
