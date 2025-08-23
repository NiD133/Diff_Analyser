package org.apache.commons.io;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class IOCase_ESTestTest28 extends IOCase_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test27() throws Throwable {
        IOCase iOCase0 = IOCase.SENSITIVE;
        int int0 = iOCase0.checkIndexOf("gEp63)S_DE]h,eyp", (-231), (String) null);
        assertEquals((-1), int0);
    }
}
