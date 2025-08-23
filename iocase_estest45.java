package org.apache.commons.io;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class IOCase_ESTestTest45 extends IOCase_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test44() throws Throwable {
        IOCase iOCase0 = IOCase.INSENSITIVE;
        String string0 = iOCase0.toString();
        assertEquals("Insensitive", string0);
    }
}
