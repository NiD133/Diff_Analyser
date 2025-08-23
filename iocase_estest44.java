package org.apache.commons.io;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class IOCase_ESTestTest44 extends IOCase_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test43() throws Throwable {
        boolean boolean0 = IOCase.isCaseSensitive((IOCase) null);
        assertFalse(boolean0);
    }
}
