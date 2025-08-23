package org.apache.commons.io;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class IOCase_ESTestTest27 extends IOCase_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test26() throws Throwable {
        IOCase iOCase0 = IOCase.SYSTEM;
        int int0 = iOCase0.checkIndexOf("aRG9v=v)b", 2351, "");
        assertEquals((-1), int0);
    }
}
