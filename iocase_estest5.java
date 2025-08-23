package org.apache.commons.io;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class IOCase_ESTestTest5 extends IOCase_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test04() throws Throwable {
        IOCase[] iOCaseArray0 = IOCase.values();
        assertEquals(3, iOCaseArray0.length);
    }
}
