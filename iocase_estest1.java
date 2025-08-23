package org.apache.commons.io;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class IOCase_ESTestTest1 extends IOCase_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test00() throws Throwable {
        IOCase iOCase0 = IOCase.INSENSITIVE;
        int int0 = iOCase0.checkIndexOf("1", 0, "1");
        assertEquals(0, int0);
    }
}
