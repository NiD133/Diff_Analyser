package org.apache.commons.io;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class IOCase_ESTestTest29 extends IOCase_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test28() throws Throwable {
        IOCase iOCase0 = IOCase.INSENSITIVE;
        int int0 = iOCase0.checkIndexOf("XH{o(jPCKq3L?>", (-31), "p");
        assertEquals(6, int0);
    }
}